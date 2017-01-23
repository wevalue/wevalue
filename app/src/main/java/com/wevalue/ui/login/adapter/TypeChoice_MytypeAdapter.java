package com.wevalue.ui.login.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wevalue.R;
import com.wevalue.model.ChannelBean;
import com.wevalue.ui.login.TypeChoiceActivity;
import com.wevalue.view.DragGridBaseAdapter;

import java.util.List;

/**
 * 已选类别的适配器
 */
public class TypeChoice_MytypeAdapter extends BaseAdapter implements DragGridBaseAdapter{
    private List<ChannelBean.Channel> mDatas;
    private TypeChoiceActivity mActivity;
    private Context mContext;

    public TypeChoice_MytypeAdapter(List<ChannelBean.Channel> mDatas, TypeChoiceActivity mActivity) {
        this.mDatas = mDatas;
        this.mActivity = mActivity;
        mContext = mActivity;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if(convertView==null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_type_choice,null);
            viewHolder = new ViewHolder();

            viewHolder.tv_type_nema= (TextView) convertView.findViewById(R.id.tv_type_name);
            viewHolder.iv_delete= (ImageView) convertView.findViewById(R.id.iv_delete);

            convertView.setTag(viewHolder);

        }else {
            viewHolder= (ViewHolder) convertView.getTag();
        }

        viewHolder.iv_delete.setVisibility(View.GONE);
        viewHolder.tv_type_nema.setText(mDatas.get(position).getTypename());
        viewHolder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.delClick(position);
            }
        });

        return convertView;
    }

    @Override
    public void reorderItems(int oldPosition, int newPosition) {

    }

    @Override
    public void setHideItem(int hidePosition) {

    }


    class ViewHolder{

        TextView tv_type_nema;
        ImageView iv_delete;
    }
}
