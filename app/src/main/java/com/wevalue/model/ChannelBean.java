package com.wevalue.model;

import java.util.List;

/**
 * 频道实体类
 */
public class ChannelBean  {

    private String result;//": 1,
    private String message;//": "",
    private List<Channel> data;//": [{



     public static class  Channel {
         private String id;//": 1,//帖子类型id
         private String classname;//": "推荐",//帖子类型名称
         private String remark;//": "推荐推荐推荐",//帖子类型备注
         private String classorder;//": 1//帖子类型排序

         public String getId() {
             return id;
         }

         public void setId(String id) {
             this.id = id;
         }

         public String getTypename() {
             return classname;
         }

         public void setTypename(String typename) {
             this.classname = typename;
         }

         public String getRemark() {
             return remark;
         }

         public void setRemark(String remark) {
             this.remark = remark;
         }

         public String getTypeorder() {
             return classorder;
         }

         public void setTypeorder(String typeorder) {
             this.classorder = typeorder;
         }

//         @Override
//         public String toString() {
//             return "Channel{" +
//                     "id='" + id + '\'' +
//                     ", typename='" + classname + '\'' +
//                     ", remark='" + remark + '\'' +
//                     ", typeorder='" + classorder + '\'' +
//                     '}';
//         }
     }


    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Channel> getData() {
        return data;
    }

    public void setData(List<Channel> data) {
        this.data = data;
    }

//    @Override
//    public String toString() {
//        return "ChannelBean{" +
//                "result='" + result + '\'' +
//                ", message='" + message + '\'' +
//                ", data=" + data +
//                '}';
//    }
}
