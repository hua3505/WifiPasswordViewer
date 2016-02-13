package com.gmail.huashadow.wifipasswordviewer;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileLockInterruptionException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wolf on 16/2/8.
 */
public class WifiManager {

    private static final String TAG = WifiManager.class.getSimpleName();
    private List<WifiModel> mWifiList;
    private Pattern mNetworkPattern = Pattern.compile("network=\\{([^}]+)\\}");
    private Pattern mSsidPattern = Pattern.compile("ssid=\"([^\"]+)\"");
    private Pattern mKeyPattern = Pattern.compile("psk=\"([^\"]+)\"");

    private static class WifiManagerHolder {
        static final WifiManager sInstance = new WifiManager();
    }

    public static WifiManager getInstance() {
        return WifiManagerHolder.sInstance;
    }

    public List<WifiModel> getWifiList() {
        if (mWifiList == null) {
            mWifiList = new ArrayList<WifiModel>();
        } else {
            mWifiList.clear();
        }
        String wifiData = getWifiData();
        Matcher networkMatcher = mNetworkPattern.matcher(wifiData);
        while (networkMatcher.find()) {
            String network = networkMatcher.group(1);
            Matcher ssidMatcher = mSsidPattern.matcher(network);
            if (ssidMatcher.find()) {
                WifiModel wifiModel = new WifiModel();
                wifiModel.ssid = ssidMatcher.group(1);
                Matcher keyMatcher = mKeyPattern.matcher(network);
                if (keyMatcher.find()) {
                    wifiModel.key = keyMatcher.group(1);
                } else {
                    wifiModel.key = null;
                }
                mWifiList.add(wifiModel);
            }
        }
        return Collections.unmodifiableList(mWifiList);
    }

    private String getWifiData() {
        String content = "empty";
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream outputStream = new DataOutputStream(process.getOutputStream());
            String command = "cat /data/misc/wifi/wpa_supplicant.conf\n";
            outputStream.writeBytes(command);
            outputStream.writeBytes("exit\n");
            outputStream.flush();
            content = readFromInputStream(process.getInputStream());
            Log.v(TAG, content);
        } catch (IOException e) {
            Log.e(TAG, "", e);
        }
        return content;
    }

    private String readFromInputStream(InputStream is) {
        String s = "";
        String line = "";

        // Wrap a BufferedReader around the InputStream
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        // Read response until the end
        try {
            while ((line = rd.readLine()) != null) {
                s += line;
            }
        } catch (IOException e) {
            Log.e(TAG, "", e);
        }

        // Return full string
        return s;
    }
}
