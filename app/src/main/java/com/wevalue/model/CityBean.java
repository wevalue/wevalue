package com.wevalue.model;

/**
 * 选择城市的实体类
 */
public class CityBean {

    private String provinceid;//": 1,//1表示热门城市 0表示非热门城市
    private String cityid;//": 0,
    private String distinctid;//": 0,
    private String cityname;//": "北京市",
    private String spell;//": "beijingshi"
    private String istop;//": "beijingshi"
    private String sortLetters;//首字母

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getIstop() {
        return istop;
    }

    public void setIstop(String istop) {
        this.istop = istop;
    }

    public String getProvinceid() {
        return provinceid;
    }

    public void setProvinceid(String provinceid) {
        this.provinceid = provinceid;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public String getDistinctid() {
        return distinctid;
    }

    public void setDistinctid(String distinctid) {
        this.distinctid = distinctid;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getSpell() {
        return spell;
    }

    public void setSpell(String spell) {
        this.spell = spell;
    }

    @Override
    public String toString() {
        return "CityBean{" +
                "provinceid='" + provinceid + '\'' +
                ", cityid='" + cityid + '\'' +
                ", distinctid='" + distinctid + '\'' +
                ", cityname='" + cityname + '\'' +
                ", spell='" + spell + '\'' +
                ", istop='" + istop + '\'' +
                '}';
    }
}
