package com.wevalue.ui.we.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.wevalue.R;
import com.wevalue.model.SiteMessageModel;
import com.wevalue.net.FriendsManage.FriendManageBase;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.details.activity.FriendsNoteDetailsActivity;
import com.wevalue.ui.details.activity.NoteDetailsActivity;
import com.wevalue.ui.details.activity.UserDetailsActivity;
import com.wevalue.ui.we.activity.PushContentActivity;
import com.wevalue.ui.we.activity.ReplyCommentActivity;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;


/**
 * 作者：邹永奎
 * 创建时间：2016/10/171
 * 类说明：消息的adapter
 */

public class MessageDetailsAdapter extends BaseAdapter {
    Context mContext;
    List<SiteMessageModel.DataBean> dataBeanList;
    String messageType = null;

    Activity mActivity;

    public MessageDetailsAdapter(Context mContext, List<SiteMessageModel.DataBean> dataBeanList, String messagetype) {
        this.mContext = mContext;
        this.dataBeanList = dataBeanList;
        this.messageType = messagetype;
        mActivity = (Activity) mContext;
        LogUtils.e("idghj", messagetype);
    }

    @Override
    public int getCount() {
        return dataBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.itemmessagedetail, null);
            viewHolder.rootView = (LinearLayout) convertView.findViewById(R.id.rootView);
            viewHolder.ll_notedetail = (LinearLayout) convertView.findViewById(R.id.ll_notedetail);
            viewHolder.ll_anniu_ui = (LinearLayout) convertView.findViewById(R.id.ll_anniu_ui);
            viewHolder.iv_face = (ImageView) convertView.findViewById(R.id.iv_face);
            viewHolder.tv_tittle = (TextView) convertView.findViewById(R.id.tv_tittle);
            viewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            viewHolder.tv_agree = (TextView) convertView.findViewById(R.id.tv_agree);
            viewHolder.tv_report_conntent = (TextView) convertView.findViewById(R.id.tv_report_content);
            viewHolder.tv_user_nickname = (TextView) convertView.findViewById(R.id.tv_user_nickname);
            viewHolder.tv_note_content = (TextView) convertView.findViewById(R.id.tv_note_content);
            viewHolder.iv_userface = (ImageView) convertView.findViewById(R.id.iv_user_face);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_tittle.setText(dataBeanList.get(position).getMesstitle());
        viewHolder.tv_time.setText(dataBeanList.get(position).getAddtime());
        viewHolder.tv_content.setText(dataBeanList.get(position).getMesscontent());
        viewHolder.tv_agree.setVisibility(View.GONE);
//        viewHolder.iv_face.setOnClickListener(news View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = news Intent(mContext, UserDetailsActivity.class);
//                intent.putExtra("detailuserid", dataBeanList.get(position).getUserid());
////                SharedPreferencesUtil.setDetailUserid(mContext, dataBeanList.get(position).getUserid());
//                mContext.startActivity(intent);
//            }
//        });
        switch (messageType) {
            case "1"://好友申请消息
                viewHolder.tv_content.setText(dataBeanList.get(position).getUserinfo());
                Glide.with(mActivity)
                        .load(RequestPath.SERVER_PATH + dataBeanList.get(position).getUserface())
                        .placeholder(R.mipmap.default_head)
                        .into(viewHolder.iv_face);
                viewHolder.ll_anniu_ui.setVisibility(View.VISIBLE);
                viewHolder.ll_notedetail.setVisibility(View.GONE);
                viewHolder.tv_agree.setVisibility(View.VISIBLE);
                viewHolder.tv_tittle.setText(dataBeanList.get(position).getUsernickname());
                viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, UserDetailsActivity.class);
                        intent.putExtra("detailuserid", dataBeanList.get(position).getFromuserid());
                        mContext.startActivity(intent);
                    }
                });
                viewHolder.tv_agree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HashMap map = new HashMap();
                        map.put("code", RequestPath.CODE);
                        map.put("userid", SharedPreferencesUtil.getUid(mContext));
                        map.put("frienduserid", dataBeanList.get(position).getFromuserid());
                        NetworkRequest.postRequest(RequestPath.POST_AGREEADD, map, new WZHttpListener() {
                            @Override
                            public void onSuccess(String content, String isUrl) {
                                try {
                                    JSONObject jsonObject = new JSONObject(content);
                                    String result = jsonObject.getString("result");
                                    String message = jsonObject.getString("message");
                                    if (result.equals("1")) {
                                        dataBeanList.get(position).setIsfriend("1");
                                        notifyDataSetChanged();
                                    }
                                    ShowUtil.showToast(mContext, message);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(String content) {
                            }
                        });
                    }
                });

                LogUtils.e("-----getIsfriend333--=" + dataBeanList.get(position).getIsfriend());
                if (dataBeanList.get(position).getIsfriend().equals("1")) {
                    viewHolder.tv_agree.setText("已添加");
                    viewHolder.tv_agree.setEnabled(false);
                } else {
                    viewHolder.tv_agree.setText("同意");
                    viewHolder.tv_agree.setEnabled(true);
                }
                break;
            case "2"://被关注消息
                viewHolder.tv_content.setText(dataBeanList.get(position).getUserinfo());
                Glide.with(mActivity)
                        .load(RequestPath.SERVER_PATH + dataBeanList.get(position).getUserface())
                        .placeholder(R.mipmap.default_head)
                        .into(viewHolder.iv_face);
                viewHolder.ll_anniu_ui.setVisibility(View.VISIBLE);
                viewHolder.ll_notedetail.setVisibility(View.GONE);
                viewHolder.tv_agree.setText("加关注");
                viewHolder.tv_agree.setVisibility(View.VISIBLE);
                viewHolder.tv_tittle.setText(dataBeanList.get(position).getUsernickname());
                viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, UserDetailsActivity.class);
                        intent.putExtra("detailuserid", dataBeanList.get(position).getFromuserid());
                        mContext.startActivity(intent);
                    }
                });
                if (dataBeanList.get(position).getIsfocus().equals("1")) {
                    viewHolder.tv_agree.setText("已关注");
                    viewHolder.tv_agree.setEnabled(false);
                } else {
                    viewHolder.tv_agree.setText("加关注");
                    viewHolder.tv_agree.setEnabled(true);
                }
                viewHolder.tv_agree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FriendManageBase.AddFocus(mContext, dataBeanList.get(position).getFromuserid(), new WZHttpListener() {
                            @Override
                            public void onSuccess(String content, String isUrl) {
                                try {
                                    JSONObject jsonObject = new JSONObject(content);
                                    String result = jsonObject.getString("result");
                                    String message = jsonObject.getString("message");
                                    if (result.equals("1")) {
                                        dataBeanList.get(position).setIsfocus("1");
                                        notifyDataSetChanged();
                                    }
                                    ShowUtil.showToast(mContext, message);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(String content) {
                            }
                        });
                    }
                });
                break;
            case "3"://打赏消息
                viewHolder.tv_time.setVisibility(View.GONE);
                Glide.with(mActivity)
                        .load(RequestPath.SERVER_PATH + dataBeanList.get(position).getUserface())
                        .placeholder(R.mipmap.default_head)
                        .into(viewHolder.iv_face);
                Glide.with(mContext)
                        .load(RequestPath.SERVER_PATH + SharedPreferencesUtil.getAvatar(mContext))
                        .placeholder(R.mipmap.default_head)
                        .into(viewHolder.iv_userface);
                viewHolder.tv_report_conntent.setVisibility(View.VISIBLE);
                viewHolder.ll_notedetail.setVisibility(View.VISIBLE);
                viewHolder.tv_tittle.setText(dataBeanList.get(position).getUsernickname());
                viewHolder.tv_content.setText(dataBeanList.get(position).getAddtime());
                viewHolder.tv_user_nickname.setText(SharedPreferencesUtil.getNickname(mContext));
                viewHolder.tv_note_content.setText(dataBeanList.get(position).getMessnotecontent());
                viewHolder.tv_report_conntent.setText(dataBeanList.get(position).getMesscontent());
                viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtils.e("idghj", position + dataBeanList.get(position).getRepostid() + "--" + dataBeanList.get(position).getNoteid());
                        Intent intent = new Intent(mContext, UserDetailsActivity.class);
                        intent.putExtra("detailuserid", dataBeanList.get(position).getFromuserid());
                        mContext.startActivity(intent);
                    }
                });
//                repostid = getIntent().getStringExtra("repostid");
//                noteId = getIntent().getStringExtra("noteId");
                viewHolder.ll_notedetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LogUtils.e("idghj", position + dataBeanList.get(position).getRepostid() + "--" + dataBeanList.get(position).getNoteid());
                        if ( dataBeanList.get(position).getIsfree().equals("1")){
                            Intent intent = new Intent(mContext, FriendsNoteDetailsActivity.class);
                            intent.putExtra("noteId", dataBeanList.get(position).getNoteid());
                            intent.putExtra("repostid", dataBeanList.get(position).getRepostid());
                            mContext.startActivity(intent);

                        }else {
                            Intent intent = new Intent(mContext, NoteDetailsActivity.class);
                            intent.putExtra("repostid", dataBeanList.get(position).getRepostid());
                            intent.putExtra("noteId", dataBeanList.get(position).getNoteid());
                            mContext.startActivity(intent);
                        }
                    }
                });
                LogUtils.e("detailuserid", dataBeanList.get(position).getFromuserid());
                LogUtils.e("detailuserid", "position=" + position);
                break;
            case "4"://转发消息
                viewHolder.ll_anniu_ui.setVisibility(View.GONE);
                Glide.with(mActivity)
                        .load(RequestPath.SERVER_PATH + dataBeanList.get(position).getUserface())
                        .placeholder(R.mipmap.default_head)
                        .into(viewHolder.iv_face);
                Glide.with(mContext)
                        .load(RequestPath.SERVER_PATH + SharedPreferencesUtil.getAvatar(mContext))
                        .placeholder(R.mipmap.default_head)
                        .into(viewHolder.iv_userface);
                viewHolder.tv_tittle.setText(dataBeanList.get(position).getUsernickname());
                viewHolder.tv_time.setVisibility(View.GONE);
                viewHolder.tv_report_conntent.setVisibility(View.VISIBLE);
                viewHolder.ll_notedetail.setVisibility(View.VISIBLE);
                viewHolder.tv_content.setText(dataBeanList.get(position).getAddtime());
                viewHolder.tv_user_nickname.setText(SharedPreferencesUtil.getNickname(mContext));
                viewHolder.tv_note_content.setText(dataBeanList.get(position).getMessnotecontent());
                viewHolder.tv_report_conntent.setText(dataBeanList.get(position).getMesscontent());
                viewHolder.ll_notedetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( dataBeanList.get(position).getIsfree().equals("1")){
                            Intent intent = new Intent(mContext, FriendsNoteDetailsActivity.class);
                            intent.putExtra("noteId", dataBeanList.get(position).getNoteid());
                            intent.putExtra("repostid", dataBeanList.get(position).getRepostid());
                            mContext.startActivity(intent);

                        }else {
                            Intent intent = new Intent(mContext, NoteDetailsActivity.class);
                            intent.putExtra("noteId", dataBeanList.get(position).getNoteid());
                            intent.putExtra("repostid", dataBeanList.get(position).getRepostid());
                            mContext.startActivity(intent);
                        }

                    }
                });
                viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, UserDetailsActivity.class);
                        intent.putExtra("detailuserid", dataBeanList.get(position).getFromuserid());
                        mContext.startActivity(intent);
                    }
                });
                break;
            case "5"://点赞消息
                Glide.with(mActivity)
                        .load(RequestPath.SERVER_PATH + dataBeanList.get(position).getUserface())
                        .placeholder(R.mipmap.default_head)
                        .into(viewHolder.iv_face);
                Glide.with(mContext)
                        .load(RequestPath.SERVER_PATH + SharedPreferencesUtil.getAvatar(mContext))
                        .placeholder(R.mipmap.default_head)
                        .into(viewHolder.iv_userface);
                viewHolder.tv_tittle.setText(dataBeanList.get(position).getUsernickname());
                viewHolder.tv_time.setVisibility(View.GONE);
                viewHolder.tv_report_conntent.setVisibility(View.VISIBLE);
                viewHolder.ll_notedetail.setVisibility(View.VISIBLE);
                viewHolder.tv_content.setText(dataBeanList.get(position).getAddtime());
                viewHolder.tv_user_nickname.setText(SharedPreferencesUtil.getNickname(mContext));
                viewHolder.tv_note_content.setText(dataBeanList.get(position).getMessnotecontent());
                viewHolder.tv_report_conntent.setText(dataBeanList.get(position).getMesscontent());
                viewHolder.ll_notedetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if ( dataBeanList.get(position).getIsfree().equals("1")){
                            Intent intent = new Intent(mContext, FriendsNoteDetailsActivity.class);
                            intent.putExtra("noteId", dataBeanList.get(position).getNoteid());
                            intent.putExtra("repostid", dataBeanList.get(position).getRepostid());
                            mContext.startActivity(intent);

                        }else {
                            Intent intent = new Intent(mContext, NoteDetailsActivity.class);
                            intent.putExtra("noteId", dataBeanList.get(position).getNoteid());
                            intent.putExtra("repostid", dataBeanList.get(position).getRepostid());
                            mContext.startActivity(intent);
                        }
                    }
                });
                viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, UserDetailsActivity.class);
                        intent.putExtra("detailuserid", dataBeanList.get(position).getFromuserid());
                        mContext.startActivity(intent);
                    }
                });
                break;
            case "6"://评论消息
                viewHolder.ll_anniu_ui.setVisibility(View.VISIBLE);
                Glide.with(mActivity)
                        .load(RequestPath.SERVER_PATH + dataBeanList.get(position).getUserface())
                        .placeholder(R.mipmap.default_head)
                        .into(viewHolder.iv_face);
                Glide.with(mContext)
                        .load(RequestPath.SERVER_PATH + SharedPreferencesUtil.getAvatar(mContext))
                        .placeholder(R.mipmap.default_head)
                        .into(viewHolder.iv_userface);
                viewHolder.tv_tittle.setText(dataBeanList.get(position).getUsernickname());
                viewHolder.tv_time.setVisibility(View.GONE);
                viewHolder.tv_agree.setText("回复");
                viewHolder.tv_agree.setVisibility(View.VISIBLE);
                viewHolder.tv_report_conntent.setVisibility(View.VISIBLE);
                viewHolder.ll_notedetail.setVisibility(View.VISIBLE);
                viewHolder.tv_content.setText(dataBeanList.get(position).getAddtime());
                viewHolder.tv_user_nickname.setText(SharedPreferencesUtil.getNickname(mContext));
                viewHolder.tv_note_content.setText(dataBeanList.get(position).getMessnotecontent());
                viewHolder.tv_report_conntent.setText(dataBeanList.get(position).getMesscontent());
                viewHolder.tv_agree.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, ReplyCommentActivity.class);
                        intent.putExtra("noteid", String.valueOf(dataBeanList.get(position).getNoteid()));
                        intent.putExtra("repostid", String.valueOf(dataBeanList.get(position).getRepostid()));
                        intent.putExtra("messstate", dataBeanList.get(position).getMessstate());
                        intent.putExtra("replycommid", String.valueOf(dataBeanList.get(position).getMessreplyid()));
                        intent.putExtra("replyuserid", dataBeanList.get(position).getFromuserid());
                        mActivity.startActivity(intent);
                    }
                });
                viewHolder.ll_notedetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( dataBeanList.get(position).getIsfree().equals("1")){
                            Intent intent = new Intent(mContext, FriendsNoteDetailsActivity.class);
                            intent.putExtra("noteId", dataBeanList.get(position).getNoteid());
                            intent.putExtra("repostid", dataBeanList.get(position).getRepostid());
                            mContext.startActivity(intent);

                        }else {
                            Intent intent = new Intent(mContext, NoteDetailsActivity.class);
                            intent.putExtra("noteId", dataBeanList.get(position).getNoteid());
                            intent.putExtra("repostid", dataBeanList.get(position).getRepostid());
                            mContext.startActivity(intent);
                        }
                    }
                });
                viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, UserDetailsActivity.class);
                        intent.putExtra("detailuserid", dataBeanList.get(position).getFromuserid());
                        mContext.startActivity(intent);
                    }
                });
                break;
            case "7"://系统推送
                viewHolder.ll_anniu_ui.setVisibility(View.VISIBLE);
                viewHolder.ll_notedetail.setVisibility(View.GONE);
                Glide.with(mActivity)
                        .load(R.mipmap.we_sysmess)
                        .into(viewHolder.iv_face);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, PushContentActivity.class);
                        intent.putExtra("messcontent", dataBeanList.get(position).getWebcontent());
                        mContext.startActivity(intent);
                    }
                });
                break;
        }
        return convertView;
    }

    class ViewHolder {
        LinearLayout rootView;//好友申请的根视图
        LinearLayout ll_notedetail;//当帖子为转发帖子的时候显示当前视图
        LinearLayout ll_anniu_ui;//当帖子为转发帖子的时候显示当前视图
        ImageView iv_face;//
        ImageView iv_userface;//发帖人头像
        TextView tv_tittle, tv_content, tv_time, tv_agree, tv_report_conntent, tv_user_nickname, tv_note_content;
    }
}
