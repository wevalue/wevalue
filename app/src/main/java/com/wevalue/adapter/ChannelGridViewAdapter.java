package com.wevalue.adapter;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.model.ChannelBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-07-27.
 */
public class ChannelGridViewAdapter extends ArrayAdapter<ChannelBean.Channel> {
    private List<String> channels = new ArrayList<>();
    List<ChannelBean.Channel> listChannel = new ArrayList<>();
    private Context context;

    public ChannelGridViewAdapter(Context context) {
        super(context, 0);
        this.context = context;
    }

    public void setData(List<ChannelBean.Channel> list) {
        //前两个数据不显示 但是必须加进 channels 频道分类里面
        listChannel.addAll(list);
        if (list.size() > 2) {
            channels.add(listChannel.get(0).getTypename());
            channels.add(listChannel.get(1).getTypename());
            listChannel.remove(0);
            listChannel.remove(0);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listChannel.size();
    }

    public List<String> getChannels() {
        return channels;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ChannelBean.Channel channel = listChannel.get(position);
        final int p = position;
        TextView textView = new TextView(getContext());
        textView.setText(channel.getTypename());
        textView.setTextSize(15);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(getContext().getResources().getColor(R.color.blue_price));
        textView.setBackgroundResource(R.drawable.iv_channel_text_n);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCheck(view, p, channel);
            }
        });
        return textView;
    }

    private void setCheck(View view, int p, ChannelBean.Channel channel) {
        boolean isCheck = view.getTag() == null ? false : (boolean) view.getTag();
        if (!isCheck) {
            view.setTag(true);
            channels.add(channel.getTypename());
            view.setBackgroundResource(R.drawable.iv_channel_text_p);
        } else {
            view.setTag(false);
            channels.remove(channel.getTypename());
            view.setBackgroundResource(R.drawable.iv_channel_text_n);
        }
        //通知
        if (notificationBoolean != null) {
            if (channels.size() > 3) {
                notificationBoolean.notification(true);
            } else {
                notificationBoolean.notification(false);
            }
        }
        String chan = "";
        for (int i = 0; i < channels.size(); i++) {
            chan += channels.get(i);
        }
        Log.e("setCheck", "channels = " + chan);
    }

    NotificationBoolean notificationBoolean;

    public void setNotificationBoolean(NotificationBoolean notificationBoolean) {
        this.notificationBoolean = notificationBoolean;
    }

    public interface NotificationBoolean {
        void notification(boolean bol);
    }
}
