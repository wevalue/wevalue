package com.wevalue.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wevalue.R;
import com.wevalue.model.ChannelBean;
import com.wevalue.model.NoteBean;
import com.wevalue.net.RequestPath;
import com.wevalue.ui.login.TypeChoiceActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-07-27.
 */
public class ChannelGridViewAdapter extends ArrayAdapter<ChannelBean.Channel> {
    private TypeChoiceActivity mActivity;
    private List<String> channels = new ArrayList<>();

    public ChannelGridViewAdapter(Context context) {
        super(context, 0);
        channels.add("推荐");
        channels.add("视频");
        channels.add("地区");
    }

    public void setData(List<ChannelBean.Channel> channel) {
        //去掉频道中的前三个 推荐 视频 地区  不显示出来
        addAll(channel);

    }

    public List<String> getChannels() {
        return channels;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(getContext());
        final ChannelBean.Channel channel = getItem(position);
        textView.setText(channel.getTypename());
        textView.setTextSize(16);
        textView.setTextColor(getContext().getResources().getColor(R.color.blue_price));
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.iv_channel_text_n);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isCheck = view.getTag() == null ? false : (boolean) view.getTag();
                if (!isCheck) {
                    view.setTag(true);
                    channels.add(1,channel.getTypename());
                    view.setBackgroundResource(R.drawable.iv_channel_text_p);
                } else {
                    view.setTag(false);
                    channels.remove(channel.getTypename());
                    view.setBackgroundResource(R.drawable.iv_channel_text_n);
                }
                //通知
                if (notificationBoolean!=null){
                    if (channels.size()>3){
                        notificationBoolean.notification(true);
                    }else{
                        notificationBoolean.notification(false);
                    }
                }

            }
        });
        return textView;
    }
    NotificationBoolean notificationBoolean;

    public void setNotificationBoolean(NotificationBoolean notificationBoolean) {
        this.notificationBoolean = notificationBoolean;
    }

    public interface NotificationBoolean{
        void notification(boolean bol);
    }
}
