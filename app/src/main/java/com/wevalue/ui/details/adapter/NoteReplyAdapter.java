package com.wevalue.ui.details.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wevalue.R;
import com.wevalue.model.NoteInfoBean;
import com.wevalue.net.RequestPath;
import com.wevalue.ui.details.activity.UserDetailsActivity;
import com.wevalue.utils.ImageUitls;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-08-17.
 */
public class NoteReplyAdapter extends BaseAdapter {

    private List<NoteInfoBean.NoteInfoEntity> mDatas = new ArrayList<>();
    private Context mContext;

    public NoteReplyAdapter(Context context) {
        mContext = context;
    }

    public void setmDatas(List<NoteInfoBean.NoteInfoEntity> mDatas) {
        if (mDatas != null)
            this.mDatas = mDatas;
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
        ViewHolder vh = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_note_reply, null);
            vh = new ViewHolder();
            vh.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            vh.tv_dengji = (TextView) convertView.findViewById(R.id.tv_dengji);
            vh.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            vh.iv_user_img = (ImageView) convertView.findViewById(R.id.iv_user_img);
            vh.tv_fabulous = (TextView) convertView.findViewById(R.id.tv_fabulous);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final NoteInfoBean.NoteInfoEntity replycomm = mDatas.get(position);
        ImageUitls.setHead(replycomm.getCommface(), vh.iv_user_img);
        vh.tv_nickname.setText(replycomm.getCommuser());
        vh.tv_dengji.setText(replycomm.getUserlevel());
        vh.tv_content.setText(replycomm.getCommcontent());
        vh.tv_fabulous.setText(replycomm.getZannums());
        vh.tv_fabulous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterface!=null)
                    mInterface.fabulouComment(replycomm);
            }
        });
//        vh.iv_user_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(mContext, UserDetailsActivity.class);
//                intent.putExtra("detailuserid", replycomm.getUserid());
//                mContext.startActivity(intent);
//            }
//        });

        return convertView;
    }

    public interface FabulouCommentInterface {
        //点赞
        void fabulouComment(NoteInfoBean.NoteInfoEntity noteInfoEntity);
    }
    FabulouCommentInterface mInterface;

    public void setmInterface(FabulouCommentInterface mInterface) {
        this.mInterface = mInterface;
    }

    class ViewHolder {
        ImageView iv_user_img;
        TextView tv_nickname;
        TextView tv_dengji;
        TextView tv_content;
        TextView tv_fabulous;//点赞
    }

    private void setImgData(String imgUrl, ImageView iv) {
        Glide.with(mContext)
                .load(RequestPath.SERVER_PATH + imgUrl)
                .placeholder(R.mipmap.default_head)
                .crossFade()
                .into(iv);
    }
}
