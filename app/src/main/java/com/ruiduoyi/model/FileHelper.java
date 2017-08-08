package com.ruiduoyi.model;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by admin on 2015/5/16.
 */
public class FileHelper {

    private static String TAG;
    private static Date tsTime;
    private static SimpleDateFormat formatter =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    private static String cLogPath = "/sdcard/RDYPmes/";

    public static String ReadTxtFile(String strFilePath) {
        String TAG = "FileHelper.ReadTxtFile";
        String path = strFilePath;
        StringBuilder sb = new StringBuilder();
        //打开文件
        File file = new File(path);
        //如果path是传递过来的参数，可以做一个非目录的判断
        if (file.isDirectory())
        {
            Log.e(TAG, "The file " + file + " is directory.");
        }
        else
        {
            try {
                InputStream instream = new FileInputStream(file);
                if (instream != null)
                {
                    InputStreamReader inputreader = new InputStreamReader(instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    //分行读取
                    while (( line = buffreader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    instream.close();
                }
            }
            catch (java.io.FileNotFoundException e)
            {
                Log.e(TAG, "The File doesn't not exist.");
            }
            catch (IOException e)
            {
                Log.e(TAG, "Unknown error: "+e.getMessage());
            }
        }
        return sb.toString();
    }

    public static void WriteLog(String context) {
        TAG = "FileHelper.WriteLog";
        tsTime = new Date(System.currentTimeMillis());
        String cTime = formatter.format(tsTime);
        String fileName = "rdypmes_error_"+cTime.substring(0,10).replace("-","")+".log";

        WriteLog(context, cLogPath, fileName);
    }

    // WriteLog to logFileName
    public static void WriteLog(String context, String logFilePath, String logFileName) {
        TAG = "FileHelper.WriteLog";
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
            Log.e(TAG, "SD card is not available or writable right now, WriteLog error!");
            return;
        }
        try{
            File path = new File(logFilePath);
            File fileName = new File(logFilePath+logFileName);
            if (!path.exists()){
                path.mkdir();
            }
            if(!fileName.exists()){
                fileName.createNewFile();
            }

            tsTime = new Date(System.currentTimeMillis());
            String s = formatter.format(tsTime)+"\t"+context+"\r\n";

            RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
            raf.seek(fileName.length());
            raf.write(s.getBytes());
            raf.close();

        }catch(IOException e){
            Log.e(TAG, "Error on write log on SDCard.");
        }
    }

    //Delete 3 days before old error log files
    public static void DeleteOldErrorLogs(){
        TAG = "FileHelper.DeleteOldErrorLogs";

        tsTime = new Date(System.currentTimeMillis());
        String cTime = formatter.format(tsTime);
        String fileName0 = "rdypmes_error_"+cTime.substring(0,10).replace("-","")+".log";

        Calendar calendar = Calendar.getInstance();
        calendar.add(calendar.DAY_OF_MONTH, -1);
        tsTime = calendar.getTime();
        cTime = formatter.format(tsTime);
        String fileName1 = "rdypmes_error_"+cTime.substring(0,10).replace("-","")+".log";

        calendar.add(calendar.DAY_OF_MONTH, -1);
        tsTime = calendar.getTime();
        cTime = formatter.format(tsTime);
        String fileName2 = "rdypmes_error_"+cTime.substring(0,10).replace("-","")+".log";

        calendar.add(calendar.DAY_OF_MONTH, -1);
        tsTime = calendar.getTime();
        cTime = formatter.format(tsTime);
        String fileName3 = "rdypmes_error_"+cTime.substring(0,10).replace("-","")+".log";

        calendar.add(calendar.DAY_OF_MONTH, -1);
        tsTime = calendar.getTime();
        cTime = formatter.format(tsTime);
        String fileName4 = "rdypmes_error_"+cTime.substring(0,10).replace("-","")+".log";

        calendar.add(calendar.DAY_OF_MONTH, -1);
        tsTime = calendar.getTime();
        cTime = formatter.format(tsTime);
        String fileName5 = "rdypmes_error_"+cTime.substring(0,10).replace("-","")+".log";

        calendar.add(calendar.DAY_OF_MONTH, -1);
        tsTime = calendar.getTime();
        cTime = formatter.format(tsTime);
        String fileName6 = "rdypmes_error_"+cTime.substring(0,10).replace("-","")+".log";

        calendar.add(calendar.DAY_OF_MONTH, -1);
        tsTime = calendar.getTime();
        cTime = formatter.format(tsTime);
        String fileName7 = "rdypmes_error_"+cTime.substring(0,10).replace("-","")+".log";

        calendar.add(calendar.DAY_OF_MONTH, -1);
        tsTime = calendar.getTime();
        cTime = formatter.format(tsTime);
        String fileName8 = "rdypmes_error_"+cTime.substring(0,10).replace("-","")+".log";

        calendar.add(calendar.DAY_OF_MONTH, -1);
        tsTime = calendar.getTime();
        cTime = formatter.format(tsTime);
        String fileName9 = "rdypmes_error_"+cTime.substring(0,10).replace("-","")+".log";

        try{
            File cFolder = new File(cLogPath);
            File[] cFile = cFolder.listFiles();
            for (File f : cFile){
                if (f.isFile()
                        && !f.getName().equals(fileName0)
                        && !f.getName().equals(fileName1)
                        && !f.getName().equals(fileName2)
                        && !f.getName().equals(fileName3)
                        && !f.getName().equals(fileName4)
                        && !f.getName().equals(fileName5)
                        && !f.getName().equals(fileName6)
                        && !f.getName().equals(fileName7)
                        && !f.getName().equals(fileName8)
                        && !f.getName().equals(fileName9)
                        ){
                    Log.i(TAG,"Delete old log files :" + f.getName());
                    f.delete();
                }
            }
        }catch (Exception e){

        }


    }

    //根据文件后缀名获得对应的MIME类型。
    /**
     * 根据文件后缀名获得对应的MIME类型。
     * @param file
     */
    public static String getMIMEType(File file) {

        String type="*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if(dotIndex < 0){
            return type;
        }
        /* 获取文件的后缀名*/
        String end=fName.substring(dotIndex,fName.length()).toLowerCase();
        if(end=="")return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for(int i=0;i<MIME_MapTable.length;i++){
            if(end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    public static final String[][] MIME_MapTable={
            //{后缀名，MIME类型}
            {".3gp",    "video/3gpp"},
            {".apk",    "application/vnd.android.package-archive"},
            {".asf",    "video/x-ms-asf"},
            {".avi",    "video/x-msvideo"},
            {".bin",    "application/octet-stream"},
            {".bmp",    "image/bmp"},
            {".c",  "text/plain"},
            {".class",  "application/octet-stream"},
            {".conf",   "text/plain"},
            {".cpp",    "text/plain"},
            {".doc",    "application/msword"},
            {".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls",    "application/vnd.ms-excel"},
            {".xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe",    "application/octet-stream"},
            {".gif",    "image/gif"},
            {".gtar",   "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h",  "text/plain"},
            {".htm",    "text/html"},
            {".html",   "text/html"},
            {".jar",    "application/java-archive"},
            {".java",   "text/plain"},
            {".jpeg",   "image/jpeg"},
            {".jpg",    "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log",    "text/plain"},
            {".m3u",    "audio/x-mpegurl"},
            {".m4a",    "audio/mp4a-latm"},
            {".m4b",    "audio/mp4a-latm"},
            {".m4p",    "audio/mp4a-latm"},
            {".m4u",    "video/vnd.mpegurl"},
            {".m4v",    "video/x-m4v"},
            {".mov",    "video/quicktime"},
            {".mp2",    "audio/x-mpeg"},
            {".mp3",    "audio/x-mpeg"},
            {".mp4",    "video/mp4"},
            {".mpc",    "application/vnd.mpohun.certificate"},
            {".mpe",    "video/mpeg"},
            {".mpeg",   "video/mpeg"},
            {".mpg",    "video/mpeg"},
            {".mpg4",   "video/mp4"},
            {".mpga",   "audio/mpeg"},
            {".msg",    "application/vnd.ms-outlook"},
            {".ogg",    "audio/ogg"},
            {".pdf",    "application/pdf"},
            {".png",    "image/png"},
            {".pps",    "application/vnd.ms-powerpoint"},
            {".ppt",    "application/vnd.ms-powerpoint"},
            {".pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop",   "text/plain"},
            {".rc", "text/plain"},
            {".rmvb",   "audio/x-pn-realaudio"},
            {".rtf",    "application/rtf"},
            {".sh", "text/plain"},
            {".tar",    "application/x-tar"},
            {".tgz",    "application/x-compressed"},
            {".txt",    "text/plain"},
            {".wav",    "audio/x-wav"},
            {".wma",    "audio/x-ms-wma"},
            {".wmv",    "audio/x-ms-wmv"},
            {".wps",    "application/vnd.ms-works"},
            {".xml",    "text/plain"},
            {".z",  "application/x-compress"},
            {".zip",    "application/x-zip-compressed"},
            {"",        "*/*"}
    };
}
