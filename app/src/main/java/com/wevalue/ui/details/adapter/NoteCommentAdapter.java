package com.wevalue.ui.details.adapter;

import android.content.Context;
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
import com.wevalue.utils.ImageUitls;
import com.wevalue.view.NoScrollListview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-08-17.
 */
public class NoteCommentAdapter extends BaseAdapter {

    private List<NoteInfoBean.NoteInfoEntity> mDatas;
    private Context mContext;
    private NoteReplyAdapter replyAdapter;

    public interface ReplyCommentInterface {
        //回复
        void replyComment(NoteInfoBean.NoteInfoEntity noteInfoEntity);

        //点赞
        void fabulouComment(NoteInfoBean.NoteInfoEntity noteInfoEntity);
    }

    ReplyCommentInterface mInterface;

    public NoteCommentAdapter(Context context, List<NoteInfoBean.NoteInfoEntity> list, ReplyCommentInterface mInterface) {
        mDatas = list;
        if (list == null) mDatas = new ArrayList<>();
        mContext = context;
        this.mInterface = mInterface;
    }

    public void setmDatas(List<NoteInfoBean.NoteInfoEntity> mDatas, int isWho) {
        if (mDatas == null) mDatas = new ArrayList();
        this.mDatas = mDatas;
    }
    public void setmDatas(List<NoteInfoBean.NoteInfoEntity> mDatas) {
        if (mDatas == null) mDatas = new ArrayList();
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_note_comment, null);
            vh = new ViewHolder();
            vh.tv_nickname = (TextView) convertView.findViewById(R.id.tv_nickname);
            vh.tv_dengji = (TextView) convertView.findViewById(R.id.tv_dengji);
            vh.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            vh.tv_fabulous = (TextView) convertView.findViewById(R.id.tv_fabulous);
            vh.tv_reply = (TextView) convertView.findViewById(R.id.tv_reply);
            vh.iv_user_img = (ImageView) convertView.findViewById(R.id.iv_user_img);
            vh.ls_reply = (NoScrollListview) convertView.findViewById(R.id.ls_reply);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final NoteInfoBean.NoteInfoEntity noteInfoEntity = mDatas.get(position);
        List<NoteInfoBean.NoteInfoEntity> replycomms = noteInfoEntity.getReplycomm();
        String commid = noteInfoEntity.getCommid();
        String repostid = noteInfoEntity.getRepostid();
        //评论分为 转发评论和正常评论 如果是转发评论则commid="0" 如果是正常评论repostid则="0"
        ImageUitls.setHead(noteInfoEntity.getCommface(), vh.iv_user_img);
        vh.tv_nickname.setText(noteInfoEntity.getCommuser());
        vh.tv_fabulous.setText(noteInfoEntity.getZannums());
        vh.tv_dengji.setText(noteInfoEntity.getUserlevel());
        vh.tv_content.setText(noteInfoEntity.getCommcontent());
        //如果回复不为空
        if (replycomms != null && replycomms.size() > 0) {
            vh.ls_reply.setVisibility(View.VISIBLE);
            replyAdapter = new NoteReplyAdapter(mContext);
            vh.ls_reply.setAdapter(replyAdapter);
            replyAdapter.setmDatas(replycomms);
            replyAdapter.notifyDataSetChanged();
            replyAdapter.setmInterface(new NoteReplyAdapter.FabulouCommentInterface() {
                @Override
                public void fabulouComment(NoteInfoBean.NoteInfoEntity noteInfoEntity) {
                    mInterface.fabulouComment(noteInfoEntity);
                }
            });
            vh.ls_reply.setSelector(R.color.gray_f5);
        } else {
            vh.ls_reply.setVisibility(View.GONE);
        }
        //点赞
        vh.tv_fabulous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mInterface.fabulouComment(noteInfoEntity);
            }
        });
        //回复
        vh.tv_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mInterface.replyComment(noteInfoEntity);
            }
        });

        return convertView;
    }


    class ViewHolder {
        ImageView iv_user_img;//头像
        TextView tv_nickname;//昵称
        TextView tv_dengji;//等级
        TextView tv_content;//评论内容
        TextView tv_fabulous; //点赞按钮
        TextView tv_reply; //回复按钮
        NoScrollListview ls_reply; //回复列表
    }
}
