package com.wevalue.model;

import com.lidroid.xutils.db.annotation.Table;
import com.wevalue.base.EntityBase;

/**
 *
 * Title:  City<br>
 * Description: TODO  区县实体类<br>
 * Depend : TODO
 * @since JDK 1.7
 */
@Table(name = "Area")
public class Area extends EntityBase {

    private static final long serialVersionUID = -1473347515575218697L;

    String districtid;//区id;
    String districtname;//区name;
    String pId;
    String cId;



    public Area() {

    }

    public Area(String districtid, String districtname, String pId, String cId) {
        this.districtid = districtid;
        this.districtname = districtname;
        this.pId = pId;
        this.cId = cId;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getDistrictid() {
        return districtid;
    }

    public void setDistrictid(String districtid) {
        this.districtid = districtid;
    }

    public String getDistrictname() {
        return districtname;
    }

    public void setDistrictname(String districtname) {
        this.districtname = districtname;
    }

    @Override
    public String toString() {
        return "Area{" +
                "districtid='" + districtid + '\'' +
                ", districtname='" + districtname + '\'' +
                '}';
    }
}
