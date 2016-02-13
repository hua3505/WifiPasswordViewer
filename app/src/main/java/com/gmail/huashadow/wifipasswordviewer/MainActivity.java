package com.gmail.huashadow.wifipasswordviewer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wolf on 16/2/7.
 */
public class MainActivity extends Activity {

    private List<WifiModel> mWifiList;
    private WifiListAdapter mAdapter;

    // view
    private ListView mListView;

    private BroadcastReceiver mWifiChangedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateWifiList();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        registerWifiChangedReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterWifiChangedReceiver();
    }

    private void init() {
        mListView = (ListView) findViewById(R.id.listview);
        initWifiList();
    }

    private void initWifiList() {
        mWifiList = new ArrayList<WifiModel>();
        mAdapter = new WifiListAdapter(this, mWifiList);
        mListView.setAdapter(mAdapter);
        updateWifiList();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mWifiList != null && position < mWifiList.size()) {
                    WifiModel wifiModel = mWifiList.get(position);
                    if (wifiModel != null) {
                        wifiModel.showPassword = !wifiModel.showPassword;
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void updateWifiList() {
        List<WifiModel> list = WifiManager.getInstance().getWifiList();
        mWifiList.clear();
        mWifiList.addAll(list);
        mAdapter.notifyDataSetChanged();
    }

    private void registerWifiChangedReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(android.net.wifi.WifiManager.NETWORK_IDS_CHANGED_ACTION);
        filter.addAction(android.net.wifi.WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(mWifiChangedReceiver, filter);
    }

    private void unRegisterWifiChangedReceiver() {
        unregisterReceiver(mWifiChangedReceiver);
    }
}
