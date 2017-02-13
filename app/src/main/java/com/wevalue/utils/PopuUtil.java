package com.wevalue.utils;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jungly.gridpasswordview.GridPasswordView;
import com.umeng.analytics.MobclickAgent;
import com.wevalue.MainActivity;
import com.wevalue.R;
import com.wevalue.WeValueApplication;
import com.wevalue.net.FriendsManage.FriendManagerInterface;
import com.wevalue.net.Interfacerequest.NoteRequestBase;
import com.wevalue.net.RequestPath;
import com.wevalue.net.payment.PayInterface;
import com.wevalue.net.payment.PaymentBase;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.add.activity.AddFriendsActivity;
import com.wevalue.ui.add.activity.AddFromNearbyActivity;
import com.wevalue.ui.add.activity.CaptureActivity;
import com.wevalue.ui.add.activity.RankingListActivity;
import com.wevalue.ui.influence.PopClickInterface;
import com.wevalue.ui.login.LoginActivity;
import com.wevalue.ui.login.TypeChoiceActivity;
import com.wevalue.ui.mine.activity.FeedbackActivity;
import com.wevalue.ui.mine.activity.SetPayPswActivity;
import com.wevalue.ui.mine.activity.WebActivity;
import com.wevalue.ui.release.ReleaseNoteActivity;
import com.wevalue.youmeng.StatisticsConsts;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-08-01.
 */
public class PopuUtil {
    public static PopupWindow promptBoxPopupWindow;
    public static View prompt_box;
    public  Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };
    /**
     * 三个点 更多
     */
    public static void initpopu(final Activity main, ImageView iv) {
        // 空白区域
        prompt_box = main.getLayoutInflater().inflate(R.layout.popu_main_add, null);
        prompt_box.setOnClickListener(new View.OnClickListener() {
            // 空白区域
            @Override
            public void onClick(View v) {
                promptBoxPopupWindow.dismiss();
            }
        });
        //附近
        TextView tv_fujin = (TextView) prompt_box.findViewById(R.id.tv_fujin);
//        TextView tv_add_channel = (TextView) prompt_box.findViewById(R.id.tv_add_channel);
//        TextView tv_saoyisao = (TextView) prompt_box.findViewById(R.id.tv_saoyisao);
        //意见反馈
        TextView tv_feedback = (TextView) prompt_box.findViewById(R.id.tv_feedback);
       //排行榜
        TextView tv_paihangbang = (TextView) prompt_box.findViewById(R.id.tv_paihangbang);
        //添加好友
        TextView tv_add_haoyou = (TextView) prompt_box.findViewById(R.id.tv_add_haoyou);
        //邀请好友
//      TextView tv_invitation = (TextView) prompt_box.findViewById(R.id.tv_invitation);
        //帮助与说明
        TextView tv_shuoming = (TextView) prompt_box.findViewById(R.id.tv_shuoming);
        //附近的人
        tv_fujin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main, AddFromNearbyActivity.class);
                main.startActivity(intent);
                promptBoxPopupWindow.dismiss();
                HashMap map = new HashMap();
                MobclickAgent.onEventValue(main, StatisticsConsts.event_more, map, 1);

            }
        });
        //意见反馈点击
        tv_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main, FeedbackActivity.class);
                main.startActivity(intent);

                MobclickAgent.onEvent(main, StatisticsConsts.event_more, "feedback");
            }
        });
        //邀请好友
//        tv_invitation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HashMap<String,String> map = new HashMap<String, String>();
//                map.put("url","www.baidu.com");
//                map.put("message","邀请好友加入微值");
//                promptBoxPopupWindow.dismiss();
//                initShareInvitePopup(main,new Handler(),map);
//            }
//        });
        //添加频道
//        tv_add_channel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                main.startActivity(new Intent(main, TypeChoiceActivity.class));
//                promptBoxPopupWindow.dismiss();
//                HashMap map = new HashMap();
//
//                MobclickAgent.onEvent(main, StatisticsConsts.event_more, "addChanel");
//            }
//        });
        //扫一扫
//        tv_saoyisao.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //TODO 跳转扫码
//                Intent intent;
//                if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(main))) {
//                    intent = new Intent(main, LoginActivity.class);
//                    main.startActivity(intent);
//                    promptBoxPopupWindow.dismiss();
//                } else {
//                    intent = new Intent(main, CaptureActivity.class);
//                    main.startActivity(intent);
//                    HashMap map = new HashMap();
//
//                    MobclickAgent.onEvent(main, StatisticsConsts.event_more, "scanQr");
//                    promptBoxPopupWindow.dismiss();
//                }
//            }
//        });
        //排行榜
        tv_paihangbang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main, RankingListActivity.class);
                main.startActivity(intent);
                promptBoxPopupWindow.dismiss();
                HashMap map = new HashMap();
                MobclickAgent.onEvent(main, StatisticsConsts.event_more, "rankList");
            }
        });
        //添加好友
        tv_add_haoyou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                if (TextUtils.isEmpty(SharedPreferencesUtil.getUid(main))) {
                    intent = new Intent(main, LoginActivity.class);
                    main.startActivity(intent);
                    promptBoxPopupWindow.dismiss();
                } else {
                    intent = new Intent(main, AddFriendsActivity.class);
                    main.startActivity(intent);
                    promptBoxPopupWindow.dismiss();
                }
                HashMap map = new HashMap();

                MobclickAgent.onEvent(main, StatisticsConsts.event_more, "addFriend");
            }

        });
        //帮助与说明
        tv_shuoming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main, WebActivity.class);
                intent.putExtra("isWho", 3);
                intent.putExtra("url", RequestPath.POST_ABOUTUS);
                main.startActivity(intent);
                promptBoxPopupWindow.dismiss();
                HashMap map = new HashMap();

                MobclickAgent.onEvent(main, StatisticsConsts.event_more, "suoMing");
            }
        });


        promptBoxPopupWindow = new PopupWindow(prompt_box, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        promptBoxPopupWindow.setFocusable(true);
        // 设置弹出动画
        promptBoxPopupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);
        // 设置popupWindow背景图片(只能通过popupWindow提供的返回键返回)
        ColorDrawable dw = new ColorDrawable(0x32000000);
        promptBoxPopupWindow.setBackgroundDrawable(dw);
        promptBoxPopupWindow.setOutsideTouchable(true);

        promptBoxPopupWindow.showAsDropDown(iv);
    }

    //点击影响力
    public static void initpopu(Activity activity, final LinearLayout ll_quanbu_ui, final TextView tv, String position, final PopClickInterface popuInterface) {
        prompt_box = activity.getLayoutInflater().inflate(R.layout.popu_yingxiangli, null);

        final TextView tv_yxl = (TextView) prompt_box.findViewById(R.id.tv_yxl);
        final TextView tv_pym = (TextView) prompt_box.findViewById(R.id.tv_pym);
        if (Integer.parseInt(position) == 2) {
            tv_yxl.setTextColor(activity.getResources().getColor(R.color.login_text_blue));
            tv_pym.setTextColor(Color.BLACK);
        } else if (Integer.parseInt(position) == 1) {
            tv_pym.setTextColor(activity.getResources().getColor(R.color.login_text_blue));
            tv_yxl.setTextColor(Color.BLACK);
        }
        prompt_box.setOnClickListener(new View.OnClickListener() {
            // 空白区域
            @Override
            public void onClick(View v) {
                promptBoxPopupWindow.dismiss();
            }
        });

        //选择了影响力
        tv_yxl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("影响力");
                popuInterface.onClickOk("影响力");
                promptBoxPopupWindow.dismiss();
            }
        });
        //选择了朋友们
        tv_pym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("朋友们");
                popuInterface.onClickOk("朋友们");
                promptBoxPopupWindow.dismiss();
            }
        });
        promptBoxPopupWindow = new PopupWindow(prompt_box, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        promptBoxPopupWindow.setFocusable(true);
        // 设置弹出动画
        promptBoxPopupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);
        // 设置popupWindow背景图片(只能通过popupWindow提供的返回键返回)
        ColorDrawable dw = new ColorDrawable(0x32000000);
        promptBoxPopupWindow.setBackgroundDrawable(dw);
        promptBoxPopupWindow.setOutsideTouchable(true);
        promptBoxPopupWindow.showAsDropDown(ll_quanbu_ui);
    }

    /**
     * 发布信息popu
     */
    public static void initpopu(final MainActivity main) {
        // 空白区域
        prompt_box = main.getLayoutInflater().inflate(R.layout.popu_send_note, null);
        LinearLayout ll_send_video = (LinearLayout) prompt_box.findViewById(R.id.ll_send_video);
        LinearLayout ll_send_audio = (LinearLayout) prompt_box.findViewById(R.id.ll_send_audio);
        LinearLayout ll_send_text = (LinearLayout) prompt_box.findViewById(R.id.ll_send_text);
        LinearLayout ll_send_img = (LinearLayout) prompt_box.findViewById(R.id.ll_send_img);
        ImageView iv_send_quxiao = (ImageView) prompt_box.findViewById(R.id.iv_send_quxiao);

        TextView tv_nong_li_nian = (TextView) prompt_box.findViewById(R.id.tv_nong_li_nian);
        TextView tv_day_popu = (TextView) prompt_box.findViewById(R.id.tv_day_popu);
        TextView tv_week_popu = (TextView) prompt_box.findViewById(R.id.tv_week_popu);
        TextView tv_nianyue = (TextView) prompt_box.findViewById(R.id.tv_nianyue);

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        SimpleDateFormat sdf_2 = new SimpleDateFormat("dd");
        SimpleDateFormat sdf_3 = new SimpleDateFormat("EEEE");
        String dateNowStr = sdf.format(d);
        String dateNowStr_2 = sdf_2.format(d);
        String dateNowStr_3 = sdf_3.format(d);

        tv_nong_li_nian.setText(ShowUtil.getYear());
        tv_nianyue.setText(dateNowStr);
        tv_day_popu.setText(dateNowStr_2);
        tv_week_popu.setText(dateNowStr_3);

        prompt_box.setOnClickListener(new View.OnClickListener() {
            // 空白区域
            @Override
            public void onClick(View v) {
                promptBoxPopupWindow.dismiss();
            }
        });
        ll_send_video.setOnClickListener(new View.OnClickListener() {
            // 发布视频
            @Override
            public void onClick(View v) {
                main.addImg("拍摄视频", "本地选择", 3);
                promptBoxPopupWindow.dismiss();
            }
        });
        ll_send_audio.setOnClickListener(new View.OnClickListener() {
            // 发布音乐
            @Override
            public void onClick(View v) {
                main.addImg("录音", "本地选择", 2);
                promptBoxPopupWindow.dismiss();
            }
        });
        ll_send_img.setOnClickListener(new View.OnClickListener() {
            // 发布图片
            @Override
            public void onClick(View v) {
                main.addImg("手机拍照", "相册选择", 1);
                promptBoxPopupWindow.dismiss();
            }
        });
        ll_send_text.setOnClickListener(new View.OnClickListener() {
            // 发布文本
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(main, ReleaseNoteActivity.class);
                intent.putExtra("isSendType", 4);
                main.startActivity(intent);
                promptBoxPopupWindow.dismiss();
            }
        });
        iv_send_quxiao.setOnClickListener(new View.OnClickListener() {
            // 取消  图片按钮
            @Override
            public void onClick(View v) {
                promptBoxPopupWindow.dismiss();
            }
        });

        promptBoxPopupWindow = new PopupWindow(prompt_box, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        promptBoxPopupWindow.setFocusable(true);
        // 设置弹出动画
        promptBoxPopupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);
        // 设置popupWindow背景图片(只能通过popupWindow提供的返回键返回)
        ColorDrawable dw = new ColorDrawable(0x90ffffff);
        promptBoxPopupWindow.setBackgroundDrawable(dw);
        promptBoxPopupWindow.setOutsideTouchable(true);
        promptBoxPopupWindow.showAtLocation(main.getWindow().getDecorView(), Gravity.CENTER, 0, 0);

    }

    /**
     * 密保问题弹出框
     */
    public static void initpopu(Activity main, final TextView tv, final List<String> list, final EditText daan) {

        // 空白区域
        prompt_box = main.getLayoutInflater().inflate(R.layout.popu_pay_wsd, null);

        ListView lv_poup_wenti_list = (ListView) prompt_box.findViewById(R.id.lv_poup_wenti_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(main, R.layout.item_popu_wenti_list, R.id.tv_wenti_name, list);
        lv_poup_wenti_list.setAdapter(adapter);

        lv_poup_wenti_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tv.setText(list.get(position));
                daan.setVisibility(View.VISIBLE);
                promptBoxPopupWindow.dismiss();
            }
        });


        promptBoxPopupWindow = new PopupWindow(prompt_box, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        promptBoxPopupWindow.setFocusable(true);
        // 设置弹出动画
        promptBoxPopupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);
        // 设置popupWindow背景图片(只能通过popupWindow提供的返回键返回)
        ColorDrawable dw = new ColorDrawable(0x32000000);
        promptBoxPopupWindow.setBackgroundDrawable(dw);
        promptBoxPopupWindow.setOutsideTouchable(true);

        promptBoxPopupWindow.showAsDropDown(tv);
    }


    /**
     * 退出登录popu
     */
    public static void initpopu(final Activity main, String content, final PopClickInterface popClickInterface) {
        TextView promptBox_tv_content, promptBox_tv_submit, promptBox_tv_cancel;
        // 空白区域
        prompt_box = main.getLayoutInflater().inflate(R.layout.wz_popupwindow_prompt_box, null);
        // 提示文字
        promptBox_tv_content = (TextView) prompt_box.findViewById(R.id.promptBox_tv_content);
        promptBox_tv_content.setText(content);
        // 确定按钮
        promptBox_tv_submit = (TextView) prompt_box.findViewById(R.id.promptBox_tv_submit);
        // 取消按钮
        promptBox_tv_cancel = (TextView) prompt_box.findViewById(R.id.promptBox_tv_cancel);

        prompt_box.setOnClickListener(new View.OnClickListener() {
            // 空白区域
            @Override
            public void onClick(View v) {
                promptBoxPopupWindow.dismiss();
            }
        });
        promptBox_tv_submit.setTextColor(main.getResources().getColor(R.color.but_text_color));
        promptBox_tv_submit.setOnClickListener(new View.OnClickListener() {
            // 确定按钮
            @Override
            public void onClick(View v) {
                popClickInterface.onClickOk("logout");
                promptBoxPopupWindow.dismiss();
            }

        });
        promptBox_tv_cancel.setTextColor(main.getResources().getColor(R.color.orange));
        promptBox_tv_cancel.setOnClickListener(new View.OnClickListener() {
            // 取消按钮
            @Override
            public void onClick(View v) {
                promptBoxPopupWindow.dismiss();
            }
        });

        promptBoxPopupWindow = new PopupWindow(prompt_box, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        promptBoxPopupWindow.setFocusable(true);
        // 设置弹出动画
        promptBoxPopupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);
        // 设置popupWindow背景图片(只能通过popupWindow提供的返回键返回)
        ColorDrawable dw = new ColorDrawable(0x32000000);
        promptBoxPopupWindow.setBackgroundDrawable(dw);
        promptBoxPopupWindow.setOutsideTouchable(true);

        promptBoxPopupWindow.showAtLocation(main.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /**
     * 支付的popupWindow
     */
    public static void initPayPopu(final Activity activity, final PayInterface payInterface, HashMap<String, String> map) {
        String payPswStatus = SharedPreferencesUtil.getPayPswStatus(activity);
        if (payPswStatus.equals("0")) {
            Intent intent = new Intent(activity, SetPayPswActivity.class);
            intent.putExtra("isSet", "set");
            activity.startActivity(intent);
            return;
        }
        RelativeLayout rl_choice_pay_type;
        final String paytype = map.get("paytype");
        String spendtype = map.get("spendtype");
        String money = map.get("money");
        final String outaccount = map.get("outaccount");
        final String outtype = map.get("outtype");
        String taocan = map.get("taocan");
        final String outtruename = map.get("outtruename");
        // 空白区域
        prompt_box = activity.getLayoutInflater().inflate(R.layout.wz_popu_pay_ui, null);
        rl_choice_pay_type = (RelativeLayout) prompt_box.findViewById(R.id.rl_choice_pay_type);
        final TextView tv_paytype = (TextView) prompt_box.findViewById(R.id.tv_paytype);
        //设置碎银的数量
        String suiyin = SharedPreferencesUtil.getSuiYinCount(activity);
        tv_paytype.setText("碎银（剩余¥" + suiyin + ")");

        final EditText et_jiage = (EditText) prompt_box.findViewById(R.id.et_jiage);
        et_jiage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            //限制用户的输入金额  第一位数字不能为零
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = et_jiage.getText().toString().trim();
                if (str.indexOf('0') == 0) {
                    et_jiage.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        final GridPasswordView gpv_normal = (GridPasswordView) prompt_box.findViewById(R.id.gpv_normal);
        final Button but_go_pay = (Button) prompt_box.findViewById(R.id.but_go_pay);
        final ImageView iv_right_arrow = (ImageView) prompt_box.findViewById(R.id.iv_right_arrow);
        ImageView imageView = (ImageView) prompt_box.findViewById(R.id.iv_dismiss);
        TextView tv_title = (TextView) prompt_box.findViewById(R.id.tv_title);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptBoxPopupWindow.dismiss();
            }
        });
//start  取消点击背景弹出框消失 by huiwen 2017、1、26
//        prompt_box.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                promptBoxPopupWindow.dismiss();
//            }
//        });
//end
        if (!TextUtils.isEmpty(money)) {
            et_jiage.setText(money);
        }
        switch (paytype) {
            case Constants.charge:
                //充值订单的操作
                tv_title.setText("您将充值");
                break;
            case Constants.withdraw:
                //提现的操作
                et_jiage.setFocusable(false);
                et_jiage.setFocusableInTouchMode(false);
                switch (outtype) {
                    case Constants.aliwithdraw:
                        tv_title.setText("您将进行提现至支付宝");
                        break;
                    case Constants.wxwithdraw:
                        tv_title.setText("您将进行提现至微信");
                        break;
                }
                break;
            case Constants.transmit:
                //转发的操作
                tv_title.setText("转发信息");
                et_jiage.setFocusable(false);
                et_jiage.setFocusableInTouchMode(false);
                break;
            case Constants.dasahng:
                //打赏的操作
                tv_title.setText("打赏发布者");
//                et_jiage.setText("1");//干掉打赏默认金额！！！！！！！！！！！！！！！！！！！！
                break;
            case Constants.buypermission:
                //购买权限的操作
                tv_title.setText(taocan);
                et_jiage.setFocusable(false);
                et_jiage.setFocusableInTouchMode(false);
                break;
            case Constants.release:
                //发布帖子的操作
                tv_title.setText("定价");
                break;
            case Constants.share:
                tv_title.setText("分享至第三方");
                et_jiage.setFocusable(false);
                et_jiage.setFocusableInTouchMode(false);
                break;
        }
        if (!TextUtils.isEmpty(spendtype)) {
            switch (spendtype) {
                case Constants.alipay:
                    gpv_normal.setVisibility(View.GONE);
                    but_go_pay.setVisibility(View.VISIBLE);
                    tv_paytype.setText("支付宝");
                    break;
                case Constants.weixinpay:
                    gpv_normal.setVisibility(View.GONE);
                    but_go_pay.setVisibility(View.VISIBLE);
                    tv_paytype.setText("微信");
                    break;
                default:
                    gpv_normal.setVisibility(View.VISIBLE);
                    but_go_pay.setVisibility(View.GONE);
                    tv_paytype.setText("碎银（剩余¥" + SharedPreferencesUtil.getSuiYinCount(activity) + ")");
                    break;
            }
        }
        //判断支付类型   非提现操作则可以切换支付方式
        if (!paytype.equals(Constants.withdraw)) {
            iv_right_arrow.setVisibility(View.VISIBLE);
            rl_choice_pay_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prompt_box.setVisibility(View.GONE);
                    initChoicePayTypePopu(activity, tv_paytype, gpv_normal, but_go_pay);
                }
            });
        } else {
            //体现操作隐藏向右小箭头
            iv_right_arrow.setVisibility(View.GONE);
        }
        //去支付按钮 点击事件 此时调用第三方支付工具
        but_go_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = et_jiage.getText().toString().trim();
                if (TextUtils.isEmpty(et_jiage.getText().toString().trim())) {
                    ShowUtil.showToast(activity, "请先输入金额!");
                    return;
                }
                if (money.equals("0")) {
                    ShowUtil.showToast(activity, "请输入O元以上的金额");
                    gpv_normal.clearPassword();
                    return;
                }
                if (paytype.equals(Constants.dasahng)) {
                    if (Integer.parseInt(money) < 1 || Integer.parseInt(money) > 888) {
                        ShowUtil.showToast(activity, "打赏金额需要在1-888元之间哦");
                        gpv_normal.clearPassword();
                        return;
                    }
                }
                HashMap map = new HashMap();
                map.put("money", money);
                if (Constants.share.equals(paytype)) {
                    map.put("paytype", Constants.transmit);
                } else {
                    map.put("paytype", paytype);
                }
                switch (tv_paytype.getText().toString()) {
                    case "微信":
                        map.put("spendtype", Constants.weixinpay);
                        payInterface.payStart(Constants.weixinpay);
//                        ShowUtil.showToast(WeValueApplication.applicationContext, "正在启动微信...");
                        break;
                    case "支付宝":
                        map.put("spendtype", Constants.alipay);
                        payInterface.payStart(Constants.alipay);
                        break;
                }
                PaymentBase paymentBase = new PaymentBase(activity, payInterface, map);
                paymentBase.initOrderInfo();
                promptBoxPopupWindow.dismiss();
            }
        });
//        prompt_box.setOnClickListener(news View.OnClickListener() {
//            // 空白区域
//            @Override
//            public void onClick(View v) {
//                promptBoxPopupWindow.dismiss();
//            }
//        });

        //输入支付密码时调用碎银支付的方法
        gpv_normal.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                String money = et_jiage.getText().toString().trim();
                if (psw.length() == 1) {
                    if (TextUtils.isEmpty(et_jiage.getText().toString().trim())) {
                        gpv_normal.clearPassword();
                        ShowUtil.showToast(activity, "请先输入金额!");
                        return;
                    }
                    if (money.equals("0")) {
                        ShowUtil.showToast(activity, "请输入O元以上的金额");
                        gpv_normal.clearPassword();
                        return;
                    }
                    if (paytype.equals(Constants.dasahng)) {
                        if (Integer.parseInt(money) < 1 || Integer.parseInt(money) > 888) {
                            ShowUtil.showToast(activity, "打赏金额需要在1-888元之间哦");
                            gpv_normal.clearPassword();
                        }
                    }
                }
                if (psw.length() == 6) {
                    HashMap map = new HashMap();
                    map.put("money", et_jiage.getText().toString().trim());
//                    map.put("money", "0.01");
                    if (Constants.share.equals(paytype)) {
                        map.put("paytype", Constants.transmit);
                    } else {
                        map.put("paytype", paytype);
                    }
                    map.put("userpaypwd", gpv_normal.getPassWord());
                    map.put("spendtype", Constants.suiyinpay);
                    map.put("outaccount", outaccount);
                    map.put("outtype", outtype);
                    map.put("outtruename", outtruename);
                    PaymentBase paymentBase = new PaymentBase(activity, payInterface, promptBoxPopupWindow, gpv_normal, map);
                    paymentBase.initOrderInfo();
                }
            }

            @Override
            public void onInputFinish(String psw) {

            }
        });
        promptBoxPopupWindow = new PopupWindow(prompt_box, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, true);
        promptBoxPopupWindow.setFocusable(true);
        // 设置弹出动画
        promptBoxPopupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);
        // 设置popupWindow背景图片(只能通过popupWindow提供的返回键返回)
        ColorDrawable dw = new ColorDrawable(0x32000000);
        promptBoxPopupWindow.setBackgroundDrawable(dw);
        promptBoxPopupWindow.setOutsideTouchable(true);

        promptBoxPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        promptBoxPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        promptBoxPopupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /**
     * 选择支付方式 的popupwindow
     */
    public static void initChoicePayTypePopu(final Activity act, final TextView tv_paytype, final GridPasswordView gpv_normal, final Button button) {
        View pPrompt_box = null;
        RelativeLayout rl_suiyin_but;
        RelativeLayout rl_alipay_but;
        RelativeLayout rl_weixin_but;
        TextView tv_suiyin_num;
        // 空白区域
        pPrompt_box = act.getLayoutInflater().inflate(R.layout.wz_popu_choice_pay_type, null);

        rl_suiyin_but = (RelativeLayout) pPrompt_box.findViewById(R.id.rl_suiyin_but);
        rl_alipay_but = (RelativeLayout) pPrompt_box.findViewById(R.id.rl_alipay_but);
        rl_weixin_but = (RelativeLayout) pPrompt_box.findViewById(R.id.rl_weixin_but);
        tv_suiyin_num = (TextView) pPrompt_box.findViewById(R.id.tv_suiyin_num);
        tv_suiyin_num.setText("碎银（剩余¥" + SharedPreferencesUtil.getSuiYinCount(act) + ")");
        final PopupWindow pPopupWindow = new PopupWindow(pPrompt_box, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        pPopupWindow.setFocusable(true);
        // 设置弹出动画
        pPopupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);
        // 设置popupWindow背景图片(只能通过popupWindow提供的返回键返回)
        ColorDrawable dw = new ColorDrawable(0x32000000);
        pPopupWindow.setBackgroundDrawable(dw);
        pPopupWindow.setOutsideTouchable(true);
        pPopupWindow.showAtLocation(act.getWindow().getDecorView(), Gravity.CENTER, 0, 0);

        pPrompt_box.setOnClickListener(new View.OnClickListener() {
            // 空白区域
            @Override
            public void onClick(View v) {
                pPopupWindow.dismiss();
                prompt_box.setVisibility(View.VISIBLE);
            }
        });

        //碎银点击事件
        rl_suiyin_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_paytype.setText("碎银（剩余¥" + SharedPreferencesUtil.getSuiYinCount(act) + ")");
                pPopupWindow.dismiss();
                gpv_normal.setVisibility(View.VISIBLE);
                prompt_box.setVisibility(View.VISIBLE);
                button.setVisibility(View.GONE);
            }
        });
        //支付宝点击事件
        rl_alipay_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_paytype.setText("支付宝");
                pPopupWindow.dismiss();
                prompt_box.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                gpv_normal.setVisibility(View.GONE);
            }
        });
        //微信点击事件
        rl_weixin_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_paytype.setText("微信");
                pPopupWindow.dismiss();
                prompt_box.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                gpv_normal.setVisibility(View.GONE);
            }
        });
    }

    /*管理好友的popuwindow*/
    public static void initManageFriend(Activity activity, final String content, final FriendManagerInterface friendManager) {
        TextView promptBox_tv_content, promptBox_tv_submit, promptBox_tv_cancel;
        // 空白区域
        prompt_box = activity.getLayoutInflater().inflate(R.layout.wz_popupwindow_prompt_box, null);
        // 提示文字
        promptBox_tv_content = (TextView) prompt_box.findViewById(R.id.promptBox_tv_content);
        // 确定按钮
        promptBox_tv_submit = (TextView) prompt_box.findViewById(R.id.promptBox_tv_submit);
        // 取消按钮
        promptBox_tv_cancel = (TextView) prompt_box.findViewById(R.id.promptBox_tv_cancel);
        promptBox_tv_content.setText(content);
        prompt_box.setOnClickListener(new View.OnClickListener() {
            // 空白区域
            @Override
            public void onClick(View v) {
                promptBoxPopupWindow.dismiss();
            }
        });
        promptBox_tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptBoxPopupWindow.dismiss();
            }
        });
        promptBox_tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                friendManager.manageFriend(content);
                promptBoxPopupWindow.dismiss();
            }
        });
        promptBoxPopupWindow = new PopupWindow(prompt_box, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        promptBoxPopupWindow.setFocusable(true);
        // 设置弹出动画
        promptBoxPopupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);
        // 设置popupWindow背景图片(只能通过popupWindow提供的返回键返回)
        ColorDrawable dw = new ColorDrawable(0x32000000);
        promptBoxPopupWindow.setBackgroundDrawable(dw);
        promptBoxPopupWindow.setOutsideTouchable(true);
        promptBoxPopupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /*管理好友的popuwindow*/
    public static void initManageFriend(Activity activity, final String content, final FriendManagerInterface friendManager, final String frienduserid) {
        TextView promptBox_tv_content, promptBox_tv_submit, promptBox_tv_cancel;
        // 空白区域
        prompt_box = activity.getLayoutInflater().inflate(R.layout.wz_popupwindow_prompt_box, null);
        // 提示文字
        promptBox_tv_content = (TextView) prompt_box.findViewById(R.id.promptBox_tv_content);
        // 确定按钮
        promptBox_tv_submit = (TextView) prompt_box.findViewById(R.id.promptBox_tv_submit);
        // 取消按钮
        promptBox_tv_cancel = (TextView) prompt_box.findViewById(R.id.promptBox_tv_cancel);
        switch (content) {
            case "加好友":
                promptBox_tv_content.setText("发送好友申请给对方");
                break;
            case "加关注":
                promptBox_tv_content.setText("确认加关注");
                break;
            case "取消关注":
                promptBox_tv_content.setText("确认取消关注");
                break;
            case "解除好友":
                promptBox_tv_content.setText("确认取消好友");
                break;
        }
        prompt_box.setOnClickListener(new View.OnClickListener() {
            // 空白区域
            @Override
            public void onClick(View v) {
                promptBoxPopupWindow.dismiss();
            }
        });
        promptBox_tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptBoxPopupWindow.dismiss();
            }
        });
        promptBox_tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friendManager.manageFriend(content, frienduserid);
                promptBoxPopupWindow.dismiss();
            }
        });
        promptBoxPopupWindow = new PopupWindow(prompt_box, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        promptBoxPopupWindow.setFocusable(true);
        // 设置弹出动画
        promptBoxPopupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);
        // 设置popupWindow背景图片(只能通过popupWindow提供的返回键返回)
        ColorDrawable dw = new ColorDrawable(0x32000000);
        promptBoxPopupWindow.setBackgroundDrawable(dw);
        promptBoxPopupWindow.setOutsideTouchable(true);
        promptBoxPopupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /*
    长按不喜欢的popuwindow
    * */
    public static void initDislikePopuwindow(Activity activity, final PopClickInterface popuInterface, AdapterView<?> parent, int position) {
        prompt_box = activity.getLayoutInflater().inflate(R.layout.popu_dislike, null);
        View pagerView = activity.getWindow().getDecorView().getRootView().findViewById(R.id.vp_world);
        ImageView iv_dislike = (ImageView) prompt_box.findViewById(R.id.iv_dislike);
        prompt_box.setOnClickListener(new View.OnClickListener() {
            // 空白区域
            @Override
            public void onClick(View v) {
                promptBoxPopupWindow.dismiss();
            }
        });
        iv_dislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popuInterface.onClickOk("");
                promptBoxPopupWindow.dismiss();
            }
        });
        promptBoxPopupWindow = new PopupWindow(prompt_box, parent.getChildAt(position).getWidth(), parent.getChildAt(position).getHeight(), true);
        promptBoxPopupWindow.setFocusable(true);
        // 设置弹出动画
        promptBoxPopupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);
        // 设置popupWindow背景图片(只能通过popupWindow提供的返回键返回)
        ColorDrawable dw = new ColorDrawable(0x32000000);
        promptBoxPopupWindow.setBackgroundDrawable(dw);
        promptBoxPopupWindow.setOutsideTouchable(true);
        if (position != 0) {
            promptBoxPopupWindow.showAsDropDown(parent.getChildAt(position - 1));
        } else {
            promptBoxPopupWindow.showAsDropDown(pagerView);
        }
    }

    /*邀请分享的*/
    public static void initShareInvitePopup(Activity activity, final Handler handler, HashMap<String, String> map) {
        String url = "";
        ImageView iv_sina;//微博分享
        final ImageView iv_weixin;//朋友圈
        ImageView iv_weixin_friend;//微信好友
        ImageView iv_qzone;//空间
        TextView tv_cancel; //取消按钮

        final String message = map.get("message")==null? "微值价值分享" : map.get("message");
        final String shareUrl = map.get("url")==null? RequestPath.SHARE_HTML : map.get("url");

        final ShareHelper shareHelper = new ShareHelper(activity, handler);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.iv_weixin:
                        shareHelper.initShare(Constants.shareWeixinMoment, shareUrl,message);
                        promptBoxPopupWindow.dismiss();
                        break;
                    case R.id.iv_weixin_friend:
                        shareHelper.initShare(Constants.shareWeixinFriend, shareUrl,message);
                        promptBoxPopupWindow.dismiss();
                        break;
                    case R.id.iv_qzone:
                        shareHelper.initShare(Constants.shareQzone, shareUrl,message);
                        promptBoxPopupWindow.dismiss();
                        break;
                    case R.id.iv_sina:
                        shareHelper.initShare(Constants.shareSina, shareUrl,message);
                        promptBoxPopupWindow.dismiss();
                        break;
                    case R.id.tv_quxiao:
                        handler.sendEmptyMessage(3);
                        promptBoxPopupWindow.dismiss();
                        break;
                }
            }
        };
        // 空白区域
        prompt_box = activity.getLayoutInflater().inflate(R.layout.popu_share_item, null);
        iv_weixin = (ImageView) prompt_box.findViewById(R.id.iv_weixin);
        iv_weixin_friend = (ImageView) prompt_box.findViewById(R.id.iv_weixin_friend);
        iv_sina = (ImageView) prompt_box.findViewById(R.id.iv_sina);
        iv_qzone = (ImageView) prompt_box.findViewById(R.id.iv_qzone);
        tv_cancel = (TextView) prompt_box.findViewById(R.id.tv_quxiao);
        tv_cancel.setOnClickListener(onClickListener);
        iv_weixin.setOnClickListener(onClickListener);
        iv_weixin_friend.setOnClickListener(onClickListener);
        iv_sina.setOnClickListener(onClickListener);
        iv_qzone.setOnClickListener(onClickListener);

        promptBoxPopupWindow = new PopupWindow(prompt_box, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        promptBoxPopupWindow.setFocusable(true);
        // 设置弹出动画
        promptBoxPopupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);
        // 设置popupWindow背景图片(只能通过popupWindow提供的返回键返回)
        ColorDrawable dw = new ColorDrawable(0x32000000);
        promptBoxPopupWindow.setBackgroundDrawable(dw);
        promptBoxPopupWindow.setOutsideTouchable(true);
        promptBoxPopupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /*分享的popuwindow*/
    public static void initSharePopup(Activity activity, final Handler handler, HashMap<String, String> map) {
        String url = "";
        ImageView iv_sina;//微博分享
        final ImageView iv_weixin;//朋友圈
        ImageView iv_weixin_friend;//微信好友
        ImageView iv_qzone;//空间
        TextView tv_cancel; //取消按钮
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String a = entry.getKey();
                String b = entry.getValue();
                url += "&" + a + "=" + b;
            }
        }
        final String message = map.get("message")==null? "微值价值分享" : map.get("message");
        final String shareUrl = RequestPath.SHARE_HTML + url;
        LogUtils.e("MSSSSS", shareUrl);
        final ShareHelper shareHelper = new ShareHelper(activity, handler);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.iv_weixin:
                        shareHelper.initShare(Constants.shareWeixinMoment, shareUrl,message);
                        promptBoxPopupWindow.dismiss();
                        break;
                    case R.id.iv_weixin_friend:
                        shareHelper.initShare(Constants.shareWeixinFriend, shareUrl,message);
                        promptBoxPopupWindow.dismiss();
                        break;
                    case R.id.iv_qzone:
                        shareHelper.initShare(Constants.shareQzone, shareUrl,message);
                        promptBoxPopupWindow.dismiss();
                        break;
                    case R.id.iv_sina:
                        shareHelper.initShare(Constants.shareSina, shareUrl,message);
                        promptBoxPopupWindow.dismiss();
                        break;
                    case R.id.tv_quxiao:
                        handler.sendEmptyMessage(3);
                        promptBoxPopupWindow.dismiss();
                        break;
                }
            }
        };
        // 空白区域
        prompt_box = activity.getLayoutInflater().inflate(R.layout.popu_share_item, null);
        iv_weixin = (ImageView) prompt_box.findViewById(R.id.iv_weixin);
        iv_weixin_friend = (ImageView) prompt_box.findViewById(R.id.iv_weixin_friend);
        iv_sina = (ImageView) prompt_box.findViewById(R.id.iv_sina);
        iv_qzone = (ImageView) prompt_box.findViewById(R.id.iv_qzone);
        tv_cancel = (TextView) prompt_box.findViewById(R.id.tv_quxiao);
        tv_cancel.setOnClickListener(onClickListener);
        iv_weixin.setOnClickListener(onClickListener);
        iv_weixin_friend.setOnClickListener(onClickListener);
        iv_sina.setOnClickListener(onClickListener);
        iv_qzone.setOnClickListener(onClickListener);

        promptBoxPopupWindow = new PopupWindow(prompt_box, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        promptBoxPopupWindow.setFocusable(true);
        // 设置弹出动画
        promptBoxPopupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);
        // 设置popupWindow背景图片(只能通过popupWindow提供的返回键返回)
        ColorDrawable dw = new ColorDrawable(0x32000000);
        promptBoxPopupWindow.setBackgroundDrawable(dw);
        promptBoxPopupWindow.setOutsideTouchable(true);
        promptBoxPopupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /*确认删除帖子的popuwindow*/
    public static void initDelNotePopu(final Activity activity, final NoteRequestBase noteRequestBase, final String noteId, final WZHttpListener wzHttpListener) {
        TextView promptBox_tv_content, promptBox_tv_submit, promptBox_tv_cancel;
        // 空白区域
        prompt_box = activity.getLayoutInflater().inflate(R.layout.wz_popupwindow_prompt_box, null);
        // 提示文字
        promptBox_tv_content = (TextView) prompt_box.findViewById(R.id.promptBox_tv_content);
        // 确定按钮
        promptBox_tv_submit = (TextView) prompt_box.findViewById(R.id.promptBox_tv_submit);
        // 取消按钮
        promptBox_tv_cancel = (TextView) prompt_box.findViewById(R.id.promptBox_tv_cancel);

        prompt_box.setOnClickListener(new View.OnClickListener() {
            // 空白区域
            @Override
            public void onClick(View v) {
                promptBoxPopupWindow.dismiss();
            }
        });
        promptBox_tv_submit.setTextColor(activity.getResources().getColor(R.color.but_text_color));
        promptBox_tv_submit.setOnClickListener(new View.OnClickListener() {
            // 确定按钮
            @Override
            public void onClick(View v) {
                noteRequestBase.postDelete(noteId, wzHttpListener);
                promptBoxPopupWindow.dismiss();
            }

        });
        promptBox_tv_cancel.setTextColor(activity.getResources().getColor(R.color.orange));
        promptBox_tv_cancel.setOnClickListener(new View.OnClickListener() {
            // 取消按钮
            @Override
            public void onClick(View v) {
                promptBoxPopupWindow.dismiss();
            }
        });

        promptBoxPopupWindow = new PopupWindow(prompt_box, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        promptBoxPopupWindow.setFocusable(true);
        // 设置弹出动画
        promptBoxPopupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);
        // 设置popupWindow背景图片(只能通过popupWindow提供的返回键返回)
        ColorDrawable dw = new ColorDrawable(0x32000000);
        promptBoxPopupWindow.setBackgroundDrawable(dw);
        promptBoxPopupWindow.setOutsideTouchable(true);
        promptBox_tv_content.setText("是否删除");
        promptBoxPopupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /*确认举报帖子的popuwindow*/
    public static void initJubaoNotePopu(final Activity activity, final NoteRequestBase noteRequestBase, final String noteId, final String repostId, final WZHttpListener wzHttpListener) {
        TextView promptBox_tv_content, promptBox_tv_submit, promptBox_tv_cancel;
        // 空白区域
        prompt_box = activity.getLayoutInflater().inflate(R.layout.wz_popupwindow_prompt_box, null);
        // 提示文字
        promptBox_tv_content = (TextView) prompt_box.findViewById(R.id.promptBox_tv_content);
        // 确定按钮
        promptBox_tv_submit = (TextView) prompt_box.findViewById(R.id.promptBox_tv_submit);
        // 取消按钮
        promptBox_tv_cancel = (TextView) prompt_box.findViewById(R.id.promptBox_tv_cancel);

        prompt_box.setOnClickListener(new View.OnClickListener() {
            // 空白区域
            @Override
            public void onClick(View v) {
                promptBoxPopupWindow.dismiss();
            }
        });
        promptBox_tv_submit.setTextColor(activity.getResources().getColor(R.color.but_text_color));
        promptBox_tv_submit.setOnClickListener(new View.OnClickListener() {
            // 确定按钮
            @Override
            public void onClick(View v) {
                noteRequestBase.postJubao(noteId, repostId, wzHttpListener);
                promptBoxPopupWindow.dismiss();
            }

        });
        promptBox_tv_cancel.setTextColor(activity.getResources().getColor(R.color.orange));
        promptBox_tv_cancel.setOnClickListener(new View.OnClickListener() {
            // 取消按钮
            @Override
            public void onClick(View v) {
                promptBoxPopupWindow.dismiss();
            }
        });

        promptBoxPopupWindow = new PopupWindow(prompt_box, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        promptBoxPopupWindow.setFocusable(true);
        // 设置弹出动画
        promptBoxPopupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);
        // 设置popupWindow背景图片(只能通过popupWindow提供的返回键返回)
        ColorDrawable dw = new ColorDrawable(0x32000000);
        promptBoxPopupWindow.setBackgroundDrawable(dw);
        promptBoxPopupWindow.setOutsideTouchable(true);
        promptBox_tv_content.setText("是否举报");
        promptBoxPopupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }


    public static void initSetRemark(final Activity activity, final PopClickInterface popuInterface) {
        final EditText et_content;
        TextView tv_ok, tv_cancel;
        // 空白区域
        prompt_box = activity.getLayoutInflater().inflate(R.layout.wz_remarkui, null);
        // 编辑文本框
        et_content = (EditText) prompt_box.findViewById(R.id.edt_remarkname);
        // 确定按钮
        tv_ok = (TextView) prompt_box.findViewById(R.id.tv_ok);
        // 取消按钮
        tv_cancel = (TextView) prompt_box.findViewById(R.id.tv_cancel);
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String s = et_content.getText().toString();
                if (s.contains("\'")) {
                    et_content.setText(s.replace("\'", ""));
                }
                if (s.contains("\"")) {
                    et_content.setText(s.replace("\"", ""));
                }
                if (s.contains("\\")) {
                    et_content.setText(s.replace("\\", ""));

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

//        prompt_box.setOnClickListener(news View.OnClickListener() {
//            // 空白区域
//            @Override
//            public void onClick(View v) {
//                promptBoxPopupWindow.dismiss();
//            }
//        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptBoxPopupWindow.dismiss();
            }
        });
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String remarkName = et_content.getText().toString().trim();
//                if (TextUtils.isEmpty(remarkName)) {
//                    ShowUtil.showToast(activity, "备注不能为空哦！");
//                    return;
//                }
                popuInterface.onClickOk(remarkName);
                promptBoxPopupWindow.dismiss();
            }
        });

        promptBoxPopupWindow = new PopupWindow(prompt_box, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        promptBoxPopupWindow.setFocusable(true);
        // 设置弹出动画
        promptBoxPopupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);
        // 设置popupWindow背景图片(只能通过popupWindow提供的返回键返回)
        ColorDrawable dw = new ColorDrawable(0x32000000);
        promptBoxPopupWindow.setBackgroundDrawable(dw);
        promptBoxPopupWindow.setOutsideTouchable(true);
        promptBoxPopupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }


    /*邀请好友*/
    public static void inviteFriends(final Activity activity, final Handler handler, final HashMap<String, String> map) {
        // 空白区域
        prompt_box = activity.getLayoutInflater().inflate(R.layout.popu_invite_friends, null);
        TextView tv_goto = (TextView) prompt_box.findViewById(R.id.tv_goto);
        tv_goto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                HashMap<String,String> map = new HashMap<String, String>();
//                map.put("url",shareUrl);
//                map.put("message",message);
                promptBoxPopupWindow.dismiss();
                PopuUtil.initShareInvitePopup(activity,handler,map);
            }
        });
        promptBoxPopupWindow = new PopupWindow(prompt_box, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, true);
        promptBoxPopupWindow.setFocusable(true);
        // 设置弹出动画
        promptBoxPopupWindow.setAnimationStyle(R.style.ActionSheetDialogStyle);
        // 设置popupWindow背景图片(只能通过popupWindow提供的返回键返回)
        ColorDrawable dw = new ColorDrawable(0x32000000);
        promptBoxPopupWindow.setBackgroundDrawable(dw);
        promptBoxPopupWindow.setOutsideTouchable(true);
        promptBoxPopupWindow.showAtLocation(activity.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }
}
