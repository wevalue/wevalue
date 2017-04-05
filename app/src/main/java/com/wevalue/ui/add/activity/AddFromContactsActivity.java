package com.wevalue.ui.add.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.wevalue.PermissionsActivity;
import com.wevalue.R;
import com.wevalue.base.BaseActivity;
import com.wevalue.model.ContactsBean;
import com.wevalue.model.SortModel;
import com.wevalue.net.FriendsManage.FriendManageBase;
import com.wevalue.net.FriendsManage.FriendManagerInterface;
import com.wevalue.net.RequestPath;
import com.wevalue.net.requestbase.NetworkRequest;
import com.wevalue.net.requestbase.WZHttpListener;
import com.wevalue.ui.details.activity.UserDetailsActivity;
import com.wevalue.utils.LogUtils;
import com.wevalue.utils.PermissionsChecker;
import com.wevalue.utils.SharedPreferencesUtil;
import com.wevalue.utils.ShowUtil;
import com.wevalue.view.sortview.SortAdapter_2;
import com.wevalue.view.sortview.view.EditTextWithDel;
import com.wevalue.view.sortview.view.PinyinComparator;
import com.wevalue.view.sortview.view.PinyinUtils;
import com.wevalue.view.sortview.view.SideBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

//从通讯录添加好友的界面
public class AddFromContactsActivity extends BaseActivity implements WZHttpListener, FriendManagerInterface {
    private ListView sortListView;
    private SideBar sideBar;
    private TextView dialog, mTvTitle;
    private SortAdapter_2 adapter;
    private EditTextWithDel mEtCityName;
    private List<SortModel> sourceDateList;
    private ArrayList<ContactsBean.Contacts> contactsList;
    private RelativeLayout rl_back;
    //序列化后的json字符串
    String phonelist;
    List<ContactsBean.DataBean> dataBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            //申请权限  第二个参数是一个 数组 说明可以同时申请多个权限
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, 1);
        } else {//已授权
            initViews();
        }
    }

    private void initViews() {
        mEtCityName = (EditTextWithDel) findViewById(R.id.et_search);
        sideBar = (SideBar) findViewById(R.id.sidrbar);
        dialog = (TextView) findViewById(R.id.dialog);
        mTvTitle = (TextView) findViewById(R.id.tv_head_title);
        mTvTitle.setText("通讯录好友");
        sortListView = (ListView) findViewById(R.id.country_lvcountry);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        queryUserContacts();
    }

    //获取 数据后绑定适配器
    private void setAdapter() {
        sourceDateList = filledData(dataBeanList);
        if (sourceDateList.size() > 0) {
            Collections.sort(sourceDateList, new PinyinComparator());
        }
        LogUtils.e("sourceDateList.size(); = = =" + sourceDateList.get(0).getName());
        adapter = new SortAdapter_2(this, sourceDateList, this);
        sortListView.setAdapter(adapter);
        //点击条目跳转用户详情页
        initDatas();
        initEvents();
    }

    //初始化动作事件
    private void initEvents() {
        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position + 1);
                }
            }
        });

        //ListView的点击事件
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // mTvTitle.setText(((SortModel) adapter.getItem(position)).getName());
                final SortModel mContent = (SortModel) parent.getAdapter().getItem(position);
                Intent intent = new Intent(AddFromContactsActivity.this, UserDetailsActivity.class);
                intent.putExtra("detailuserid",mContent.getUserId());
                startActivity(intent);
            }
        });

        //根据输入框输入值的改变来过滤搜索
        mEtCityName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initDatas() {
        sideBar.setTextView(dialog);
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> mSortList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            mSortList = sourceDateList;
        } else {
            mSortList.clear();
            for (SortModel sortModel : sourceDateList) {
                String name = sortModel.getName();
                if (name.toUpperCase().indexOf(filterStr.toString().toUpperCase()) != -1 || PinyinUtils.getPingYin(name).toUpperCase().startsWith(filterStr.toString().toUpperCase())) {
                    mSortList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(mSortList, new PinyinComparator());
        adapter.updateListView(mSortList);
    }

    //填充未排序之前的数据 输出排序后的数据
    private List<SortModel> filledData(List<ContactsBean.DataBean> dataBeanList) {
        List<SortModel> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();
        for (int i = 0; i < dataBeanList.size(); i++) {
            SortModel sortModel = new SortModel();
            sortModel.setName(dataBeanList.get(i).getPhonename());
            sortModel.setUserId(dataBeanList.get(i).getUserid());
            sortModel.setIcon(dataBeanList.get(i).getUserface());
            sortModel.setJianjie(dataBeanList.get(i).getUserinfo());
            sortModel.setIsFans(dataBeanList.get(i).getIsfans());
            sortModel.setIsFocuse(dataBeanList.get(i).getIsfocus());
            sortModel.setIsFriend(dataBeanList.get(i).getIsfriend());
            sortModel.setUserlevel(dataBeanList.get(i).getUserlevel());
            String pinyin = PinyinUtils.getPingYin(dataBeanList.get(i).getPhonename());
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            } else {
                sortModel.setSortLetters("#");
                if (!indexString.contains("#")) {
                    indexString.add("#");
                }
            }
            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
        if (indexString != null && indexString.size() > 0) {
            indexString.add(indexString.size(), indexString.get(0));
            indexString.remove(0);
            sideBar.setIndexText(indexString);
        }

        return mSortList;
    }

    //获取用户手机联系人
    private void queryUserContacts() {
        getContact();
    }

    public ArrayList<ContactsBean.Contacts> getContact() {
        ArrayList<ContactsBean.Contacts> listMembers = new ArrayList<>();
        contactsList = new ArrayList<>();
        Cursor cursor = null;
        try {
            Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            // 这里是获取联系人表的电话里的信息  包括：名字，名字拼音，联系人id,电话号码；
            // 然后在根据"sort-key"排序
            cursor = context.getContentResolver().query(uri, null, null, null, null);
//            cursor = this.getContentResolver().query(
//                    uri,
//                    new String[]{"display_name", "sort_key", "contact_id", "data1"}, null, null, "sort_key");

            if (cursor.moveToFirst()) {
                do {
                    ContactsBean.Contacts contact = new ContactsBean.Contacts();
                    String contact_phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    String contact_name = cursor.getString(0);
//                    int contact_id = cursor.getInt(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
//                    contact.setId(String.valueOf(contact_id));/**/
                    contact.setUserPhone(contact_phone.replace(" ", "").replace("-", ""));
                    contact.setUserName(contact_name);
                    contactsList.add(contact);
//                    if (contact_name != null) listMembers.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        getContactsFromServe();
        return contactsList;
    }

    @Override
    public void onSuccess(String content, String isUrl) {
        switch (isUrl) {
            //上传通讯录请求
            case RequestPath.POST_PHONELIST:
                Gson gson = new Gson();
                ContactsBean contactsBean = gson.fromJson(content, ContactsBean.class);
                if (contactsBean.getResult() == 1 && !contactsBean.getData().isEmpty()) {
                    dataBeanList = contactsBean.getData();
                    setAdapter();
                } else {
                    ShowUtil.showToast(this, contactsBean.getMessage());
                }
                break;
            //添加关注请求
            case RequestPath.POST_ADDFOCUES:
                JSONObject objectAddFocus = null;
                String messageAddFocus = "";
                try {
                    objectAddFocus = new JSONObject(content);
                    messageAddFocus = objectAddFocus.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, messageAddFocus, Toast.LENGTH_SHORT).show();
                break;
            //添加好友请求
            case RequestPath.POST_ADDFRIEND:
                JSONObject objectAddFriend = null;
                String messageAddFriend = "";
                try {
                    objectAddFriend = new JSONObject(content);
                    messageAddFriend = objectAddFriend.getString("message");
                    getContactsFromServe();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(this, messageAddFriend, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onFailure(String content) {

    }

    @Override
    public void manageFriend(String content) {

    }

    @Override
    public void manageFriend(String content, String userid) {
        switch (content) {
            case "加好友":
                FriendManageBase.AddFriend(this, userid, this);
                break;
            case "解除好友":
                FriendManageBase.DelFriend(this, userid, this);
                break;
            case "加关注":
                FriendManageBase.AddFocus(this, userid, this);
                break;
            case "取消关注":
                FriendManageBase.DelFocus(this, userid, this);
                break;
        }
    }

    private void getContactsFromServe() {
        Gson gson = new Gson();
        phonelist = gson.toJson(contactsList);
        LogUtils.e("phone", phonelist);
        HashMap map = new HashMap();
        map.put("code", RequestPath.CODE);
        map.put("userid", SharedPreferencesUtil.getUid(this));
        map.put("phonelist", phonelist);
        NetworkRequest.postRequest(RequestPath.POST_PHONELIST, map, this);
    }

    @Override
    public int checkPermission(String permission, int pid, int uid) {
        return super.checkPermission(permission, pid, uid);
    }
}
