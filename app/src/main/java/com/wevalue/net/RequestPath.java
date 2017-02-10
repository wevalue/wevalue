package com.wevalue.net;


/**
 * Created by Administrator on 2016-06-24.
 */
public class RequestPath {

     /*客户网站域名*/
    // public static final String SERVER_PATH = "https://mp.wzbz.cn";
    /* 公司服务器地址 测试地址*/
     public static final String SERVER_PATH = "http://192.168.9.109";
    // public static final String SERVER_PATH = "http://192.168.9.117:8080";

    /*分享到第三方的链接*/
    public static final String SHARE_HTML = SERVER_PATH + "/web/note.html?";

    /**刘燚  主机地址*/
//    public static final String  SERVER_PATH="http://192.168.1.103:8002";

    /**
     * code
     */
    public static final String CODE = "weizhi";
    /**
     * 注册接口
     */
    public static final String POST_REGUSER = SERVER_PATH + "/api_3/dbuser.ashx?method=reguser";
    /**
     * 快速注册接口
     */
    public static final String POST_QUICKREG_REGUSER = SERVER_PATH + "/api_3/dbuser.ashx?method=quickreg";
    /**
     * 用户名验证
     */
    public static final String POST_REGUSERCHECK = SERVER_PATH + "/api_3/dbuser.ashx?method=regusercheck";

    /**
     * 获取验证码接口
     */
    // TODO: 2017/1/10 修改为post请求
    public static final String POST_GETCODE = SERVER_PATH + "/api_3/dbuser.ashx?method=getcode";

    /**
     * 登录接口
     */
    public static final String POST_LOGIN = SERVER_PATH + "/api_3/dbuser.ashx?method=login";

    /**
     * 重置密码接口
     */
    public static final String POST_RESETUSERPWD = SERVER_PATH + "/api_3/dbuser.ashx?method=resetuserpwd";

    /**
     * 修改密码接口
     */
    public static final String POST_UPDATEUSERPWD = SERVER_PATH + "/api_3/dbuser.ashx?method=updateuserpwd";

    /**
     * 获取用户信息接口
     */
    // TODO: 2017/1/10 修改为post请求
    public static final String POST_USERINFO = SERVER_PATH + "/api_3/dbuser.ashx?method=userinfo";

    /**
     * 版本更新接口
     */
    public static final String GET_GETVERSION = SERVER_PATH + "/api_3/dbbase.ashx?method=getversion";

    /**
     * 头像修改接口
     */
    public static final String POST_UPDATE_USERFACE = SERVER_PATH + "/api_3/dbuser.ashx?method=updateuserface";

    /**
     * 用户信息修改接口
     */
    public static final String POST_UPDATEUSERINFO = SERVER_PATH + "/api_3/dbuser.ashx?method=updateuserinfo";

    /**
     * 关于我们接口
     */                                                     ///api_3/dbbase.ashx?method=weizhihelp
    public static final String POST_ABOUTUS = SERVER_PATH + "/api_3/dbbase.ashx?method=weizhihelp";

    /**
     * 意见反馈接口
     */
    public static final String POST_ADDADVICE = SERVER_PATH + "/api_3/dbbase.ashx?method=addadvice";

    /**
     * 热门城市接口
     */
    public static final String GET_GETHOTCITY = SERVER_PATH + "/api_3/dbbase.ashx?method=gethotcity";

    /**
     * 获取所有省和城市接口
     */
    public static final String GET_GETALLCITY = SERVER_PATH + "/api_3/dbbase.ashx?method=getallcity";

    /**
     * 获取所有频道接口
     */
    public static final String GET_GETNOTETYPE = SERVER_PATH + "/api_3/dbsocial.ashx?method=getnoteclass";

    /**
     * 获取微值协议
     */
    public static final String GET_GETAGREEMENT = SERVER_PATH + "/api_3/dbbase.ashx?method=weizhiregister";

    /**
     * 获取等级升级规则
     */
    public static final String GET_WEIZHILEVEL = SERVER_PATH + "/api_3/dbbase.ashx?method=weizhilevel";

    /**
     * 获取碎银说明
     */
    public static final String GET_WEIZHIMONEY = SERVER_PATH + "/api_3/dbbase.ashx?method=weizhimoney";

    /**
     * 获取权限说明
     */
    public static final String GET_WEIZHIPOWER = SERVER_PATH + "/api_3/dbbase.ashx?method=weizhipower";

    /**
     * 获取获奖名单接口
     */
    public static final String GET_WEIZHIREWARD = SERVER_PATH + "/api_3/dbbase.ashx?method=weizhireward";

    /**
     * 验证密保答案接口
     */
    public static final String POST_GETPAYRESULT = SERVER_PATH + "/api_3/dbuser.ashx?method=getpayresult";

    /**
     * 记录密保问题及答案接口
     */
    public static final String POST_SETPAYQUESTION = SERVER_PATH + "/api_3/dbuser.ashx?method=setpayquestion";

    /**
     * 获取用户站内消息列表
     */
//    public static final String GET_USERSITEMESS = SERVER_PATH + "/api_3/dbuser.ashx?method=usersitemess";

    /**
     * 获取帖子详情
     */
    public static final String GET_NOTEINFO = SERVER_PATH + "/api_3/dbsocial.ashx?method=noteinfo";

    /**
     * 获取帖子列表
     */
    public static final String GET_GETNOTE = SERVER_PATH + "/api_3/dbsocial.ashx?method=getnote";

    /**
     * 综合搜索接口
     */
    public static final String GETUSERNOTEBYSEARCH = SERVER_PATH + "/api_3/dbfriends.ashx?method=getusernotebysearch";

    /**
     * 获取我发布的 或者 我的转发帖子列表,我的打赏
     */
    public static final String GET_GETNOTES = SERVER_PATH + "/api_3/dbsocial.ashx?method=getmynote";

    /**
     * 帖子被转发列表 接口
     */
    public static final String GET_NOTEREPOSTLIST = SERVER_PATH + "/api_3/dbsocial.ashx?method=noterepostlist";

    /**
     * 获取帖子 评论列表
     */
    public static final String GET_NOTECOMMLIST = SERVER_PATH + "/api_3/dbsocial.ashx?method=notecommlist";

    /**
     * 获取帖子 情绪 列表
     */
    public static final String GET_NOTEMOODLIST = SERVER_PATH + "/api_3/dbsocial.ashx?method=notemoodlist";

    /**
     * 获取帖子点赞列表
     */
    public static final String GET_NOTEZANLIST = SERVER_PATH + "/api_3/dbsocial.ashx?method=notezanlist";

    /**
     * 获取帖子 打赏 列表
     */
    public static final String GET_NOTEREWARDLIST = SERVER_PATH + "/api_3/dbsocial.ashx?method=noterewardlist";

    /**
     * 发布帖子
     */
    public static final String POST_RELEASE = SERVER_PATH + "/api_3/dbsocial.ashx?method=release";

    /**
     * 上传视频或音频接口
     *///测试
    public static final String POST_SENDVIDEO = SERVER_PATH + "/api_3/dbsocial.ashx?method=sendvideo";

    /**
     * 获取视频或音频接口
     *///测试
    public static final String GET_GETVIDEO = SERVER_PATH + "/api_3/dbsocial.ashx?method=getvideo";

    /**
     * 点赞/取消点赞
     */
    public static final String POST_EDITNOTEZAN = SERVER_PATH + "/api_3/dbsocial.ashx?method=editnotezan";

    /**
     * 添加情绪
     */
    public static final String POST_ADD_MOOD = SERVER_PATH + "/api_3/dbsocial.ashx?method=add_mood";

    /**
     * 回复评论
     */
    public static final String POST_ADDCOMMENT = SERVER_PATH + "/api_3/dbsocial.ashx?method=addcomment";

    /**
     * 转发
     */
//    public static final String POST_SHARENOTE = SERVER_PATH + "/api_3/dbsocial.ashx?method=sharenote";
    public static final String POST_ZHUANFA = SERVER_PATH + "/api_3/dbsocial.ashx?method=repostnote";

    /**
     * 删除信息
     */
    public static final String POST_DELNOTE = SERVER_PATH + "/api_3/dbsocial.ashx?method=delnote";
    //添加心愿单
    public static final String POST_WISH = SERVER_PATH + "/api_3/dbuser.ashx?method=adduserlike";
    //我的心愿单
    public static final String GET_WISH = SERVER_PATH + "/api_3/dbuser.ashx?method=getuserlike";
    //修改我的频道信息(暂时未使用)
    public static final String POST_MODIFY_CHANEL = SERVER_PATH + "/api_3/dbuser.ashx?method=setuserlike";
    //打赏接口
    public static final String POST_DASHANG = SERVER_PATH + "/api_3/dbsocial.ashx?method=rewardnote";
    //获取我的好友列表
    public static final String GET_MYFRIENDS = SERVER_PATH + "/api_3/dbfriends.ashx?method=getmyfriends";
    //影响力帖子获取接口
    public static final String GET_INFLUENCENOTES = SERVER_PATH + "/api_3/dbsocial.ashx?method=getpowernote";
    //我们界面接口
    public static final String GET_US = SERVER_PATH + "/api_3/dbfriends.ashx?method=getwepageload";
    //获取附近用户接口
    public static final String GET_NEARBYUSER = SERVER_PATH + "/api_3/dbfriends.ashx?method=getnearuser";
    //添加好友关系
    public static final String POST_ADDFRIEND = SERVER_PATH + "/api_3/dbfriends.ashx?method=addfriend";
    //解除好友关系
    public static final String POST_DELFRIEND = SERVER_PATH + "/api_3/dbfriends.ashx?method=delfriend";
    //添加关注关系
    public static final String POST_ADDFOCUES = SERVER_PATH + "/api_3/dbfriends.ashx?method=addfocus";
    //取消关注
    public static final String POST_DELFOCUES = SERVER_PATH + "/api_3/dbfriends.ashx?method=delfocus";
    //通讯录好友匹配接口
    public static final String POST_PHONELIST = SERVER_PATH + "/api_3/dbfriends.ashx?method=getphonelistuser";
    //获取用户详情接口
    public static final String GET_USERDETAILS = SERVER_PATH + "/api_3/dbfriends.ashx?method=getuserdetail";
    //不喜欢接口
    public static final String POST_HIDENOTE = SERVER_PATH + "/api_3/dbsocial.ashx?method=hidenote";
    //获取用户站内信息
    public static final String GET_USERSITEMESS = SERVER_PATH + "/api_3/dbfriends.ashx?method=getwepageload";
    //扫描二维码加好友接口
    public static final String GET_ADDCODEUSER = SERVER_PATH + "/api_3/dbfriends.ashx?method=getcodeuser";
    //搜索昵称加好友
    public static final String GET_ADDFRIENDBYSEARCH = SERVER_PATH + "/api_3/dbfriends.ashx?method=getuserbysearch";
    //获取用户详情页的粉丝/好友列表
    public static final String GET_USERFRIEND = SERVER_PATH + "/api_3/dbfriends.ashx?method=getuserdetailfriend";
    //获取周排行榜
    public static final String GET_RANKLIST = SERVER_PATH + "/api_3/dbsocial.ashx?method=getrank";
    //获取我的碎银接口
    // TODO: 2017/1/10 修改为post接口
    public static final String POST_USERMONRY = SERVER_PATH + "/api_3/dbuser.ashx?method=getusermoney";
    //获取用户收支明细
    // TODO: 2017/1/10 修改为post请求
    public static final String POST_PENDIST = SERVER_PATH + "/api_3/dbuser.ashx?method=getspendist";
    //同意好友申请
    public static final String POST_AGREEADD = SERVER_PATH + "/api_3/dbfriends.ashx?method=agreefriend";
    //请求生成支付订单
    public static final String POST_PAYMONEY = SERVER_PATH + "/api_3/dbuser.ashx?method=topayorder";
    //充值成功
    public static final String POST_RECHARGE_SUCC = SERVER_PATH + "/api_3/dbuser.ashx?method=rechargesucc";
    //用户定位信息更新
    public static final String POST_UPDATEADDR = SERVER_PATH + "/api_3/dbuser.ashx?method=updateuseraddr";
    //验证支付密码是否正确
    public static final String POST_VERIFYPAYCODE = SERVER_PATH + "/api_3/dbuser.ashx?method=validpaypwd";
    //获取用户能够免费发布帖子的数目
    // TODO: 2017/1/10 修改为post请求 
    public static final String POST_USERPOWER = SERVER_PATH + "/api_3/dbuser.ashx?method=getuserpower";
    //购买权限成功的验证
    public static final String POST_BUYPOWERSUCC = SERVER_PATH + "/api_3/dbuser.ashx?method=buypowersucc";
    //获取我的收支明细详情
    public static final String POST_PENDISTINFO = SERVER_PATH + "/api_3/dbuser.ashx?method=getspendistinfo";

    /*微信提现用户id绑定*/
    public static final String POST_BINDWXCODE = SERVER_PATH + "/api_3/dbuser.ashx?method=binduserwxcode";

    /*微信openid获取*/
    public static final String GET_WXOPENID = "https://api.weixin.qq.com/sns/oauth2/access_token?";

    /*获取微信用户个人信息*/
    public static final String GET_WXUSERINFO = "https://api.weixin.qq.com/sns/userinfo?";

    /*获取提升权限套餐接口*/
    public static final String GET_GETPOWERLIST = SERVER_PATH + "/api_3/dbuser.ashx?method=getpowerlist";

    /*分享成功后告知后台记录72小时免费分享接口*/
    public static final String POST_NOTE_SHARESUCC = SERVER_PATH + "/api_3/dbsocial.ashx?method=notesharesucc";

    /*设置好友备注的接口*/
    public static final String POST_SETFRIENDREMARK = SERVER_PATH + "/api_3/dbfriends.ashx?method=setfriendremark";

    /*验证昵称是否已经存在/是否被保护*/
    public static final String POST_REGCHECKNICK = SERVER_PATH + "/api_3/dbuser.ashx?method=regchecknick";

    /*注册后进行的认证接口*/
    public static final String POST_SETTRUEINFO = SERVER_PATH + "/api_3/dbuser.ashx?method=settrueinfo";

    /*第三方支付请求退款*/
    public static final String POST_TOBACKORDER = SERVER_PATH + "/api_3/dbuser.ashx?method=tobackorder";

    /*获取用户验证资料*/
    public static final String POST_USERTRUEINFO = SERVER_PATH + "/api_3/dbuser.ashx?method=usertrueinfo";

    //拉黑好友
    public static final String POST_BLACKFRIEND = SERVER_PATH + "/api_3/dbfriends.ashx?method=blackfriend";
    //取消拉黑好友
    public static final String POST_DELBLACKFRIEND = SERVER_PATH + "/api_3/dbfriends.ashx?method=delblackfriend";
    //登录token延期
    public static final String POST_SETTOKENLONGTIME = SERVER_PATH + "/api_3/dbuser.ashx?method=settokenlongtime";
    //获取启动页图片  lanucher
    public static final String POST_LANYCHER_IMAGE = SERVER_PATH + "/api_3/dbbase.ashx?method=getuipage";

    //需要加密参数的接口
    public static final String[] EncodePath = {POST_QUICKREG_REGUSER,POST_REGUSER, POST_UPDATEUSERPWD, POST_RESETUSERPWD, POST_SETTRUEINFO, POST_VERIFYPAYCODE, POST_PAYMONEY};
}
