package com.gmail.huashadow.wifipasswordviewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by wolf on 16/2/11.
 */
public class WifiListAdapter extends BaseAdapter {

    private Context mContext;
    private List<WifiModel> mItems;
    private LayoutInflater mInflater;

    public WifiListAdapter(Context context, List<WifiModel> items) {
        mContext = context;
        mItems = items;
        if (mContext != null) {
            mInflater = LayoutInflater.from(mContext);
        }
    }

    @Override
    public int getCount() {
        if (mItems != null) {
            return mItems.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (mItems != null && position < mItems.size()) {
            return mItems.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder = null;
        if (view == null) {
            view = mInflater.inflate(R.layout.activity_main_wifilist_item, null);
            holder = new ViewHolder();
            holder.tvSsid = (TextView) view.findViewById(R.id.tv_ssid);
            holder.tvKey = (TextView) view.findViewById(R.id.tv_key);
            view.setTag(R.integer.tag, holder);
        } else {
            holder = (ViewHolder) view.getTag(R.integer.tag);
        }

        WifiModel item = mItems.get(position);
        if (item != null) {
            holder.tvSsid.setText(item.ssid);
            if (item.key != null) {
                if (item.showPassword) {
                    holder.tvKey.setText(item.key);
                } else {
                    holder.tvKey.setText("********");
                }
            } else {
                holder.tvKey.setText(R.string.no_password);
            }
        }

        return view;
    }

    private static class ViewHolder {
        public TextView tvSsid;
        public TextView tvKey;
    }
}


