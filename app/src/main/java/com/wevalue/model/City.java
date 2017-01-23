package com.wevalue.model;

import com.lidroid.xutils.db.annotation.Table;
import com.wevalue.base.EntityBase;

import java.util.List;

/**
 * 
 * Title:  City<br>
 * Description: TODO  市实体类<br>
 * Depend : TODO
 * @since JDK 1.7
 */
@Table(name = "City")
public class City extends EntityBase {

	private static final long serialVersionUID = -1418371373549343423L;

	private String cityid;//市id
	private String cityname;//市名字
	private List<Area> list;
	private String pId;



	public City() {
	}

	public City(String cityid, String cityname, List<Area> list, String pId) {
		this.cityid = cityid;
		this.cityname = cityname;
		this.list = list;
		this.pId = pId;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getCityid() {
		return cityid;
	}

	public void setCityid(String cityid) {
		this.cityid = cityid;
	}

	public String getCityname() {
		return cityname;
	}

	public void setCityname(String cityname) {
		this.cityname = cityname;
	}

	public List<Area> getList() {
		return list;
	}

	public void setList(List<Area> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "City{" +
				"cityid='" + cityid + '\'' +
				", cityname='" + cityname + '\'' +
				", list=" + list +
				'}';
	}
}
