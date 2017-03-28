package com.wevalue.model;

import com.wevalue.view.CarouselView;

import java.util.List;

import static com.wevalue.view.CarouselView.*;

/**
 * 世界帖子列表实体类
 */
public class CarouselBean  {
    private  String lunbotitle;//"发动机可撒了房间里看书了",
    private String lunbourl;//"#",
    private String lunboimage;//"/upload/notice/20170316160029492.png",
    private String lunbotype;// 0:app    1:html

    public String getLunbotitle() {
        return lunbotitle;
    }

    public void setLunbotitle(String lunbotitle) {
        this.lunbotitle = lunbotitle;
    }

    public String getLunbourl() {
        return lunbourl;
    }

    public void setLunbourl(String lunbourl) {
        this.lunbourl = lunbourl;
    }

    public String getLunboimage() {
        return lunboimage;
    }

    public void setLunboimage(String lunboimage) {
        this.lunboimage = lunboimage;
    }

    public String getLunbotype() {
        return lunbotype;
    }

    public void setLunbotype(String lunbotype) {
        this.lunbotype = lunbotype;
    }
}

