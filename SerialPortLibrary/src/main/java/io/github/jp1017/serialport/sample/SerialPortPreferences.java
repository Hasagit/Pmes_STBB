/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package io.github.jp1017.serialport.sample;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

import android_serialport_api.sample.R;
import io.github.jp1017.serialport.SerialPort;
import io.github.jp1017.serialport.SerialPortFinder;

public class SerialPortPreferences extends PreferenceActivity {

	private Application mApplication;
	private SerialPortFinder mSerialPortFinder;
	private SerialPort sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mApplication = (Application) getApplication();
		mSerialPortFinder = mApplication.mSerialPortFinder;

		addPreferencesFromResource(R.xml.serial_port_preferences);

		// Devices
		final ListPreference devices = (ListPreference)findPreference("DEVICE");
        String[] entries = mSerialPortFinder.getAllDevices();
        String[] entryValues = mSerialPortFinder.getAllDevicesPath();
		devices.setEntries(entries);
		devices.setEntryValues(entryValues);
		devices.setSummary(devices.getValue());
		devices.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				return true;
			}
		});

		// Baud rates
		final ListPreference baudrates = (ListPreference)findPreference("BAUDRATE");
		baudrates.setSummary(baudrates.getValue());
		baudrates.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				preference.setSummary((String)newValue);
				return true;
			}
		});
		try {
			test();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void test() throws IOException {
		sp=new SerialPort(new File("/dev/ttyUSB10"), 9600,0);
		final InputStream in=sp.getInputStream();
		OutputStream out=sp.getOutputStream();
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					int size;
					try {
						byte[] buffer = new byte[12];
						byte[] cardId=new byte[4];
						if (in == null) return;
						size = in.read(buffer);
						if (size > 0) {
							for (int i=7;i<11;i++){
								cardId[i-7]=buffer[i];
							}
							//onDataReceived(buffer, size);
							Log.e("read",binary(cardId,10));
						}
					} catch (IOException e) {
						e.printStackTrace();
						return;
					}
				}
			}
		}).start();

	}

	public static String bytes2hex01(byte[] bytes)
	{
		/**
		 * 第一个参数的解释，记得一定要设置为1
		 *  signum of the number (-1 for negative, 0 for zero, 1 for positive).
		 */
		BigInteger bigInteger = new BigInteger(1, bytes);
		return bigInteger.toString(16);
	}

	public static String binary(byte[] bytes, int radix){
		return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
	}
}
