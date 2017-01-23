package com.wevalue.model;

import com.lidroid.xutils.db.annotation.Table;
import com.wevalue.base.EntityBase;

import java.util.List;

/**
 * 
 * Title:  Province<br>
 * Description: TODO  省实体类<br>
 * Depend : TODO
 * @ModifiedDate 
 * @since JDK 1.7
 */
@Table(name = "Province")
public class Province extends EntityBase {


	private static final long serialVersionUID = -1473347515575248697L;

	String provinceid;//省ID
	String provincename;//省名称
	List<City> list;//省中城市集合;

	public Province() {
	}

	public Province(String provinceid, String provincename, List<City> list) {
		this.provinceid = provinceid;
		this.provincename = provincename;
		this.list = list;
	}

	public String getProvinceid() {
		return provinceid;
	}

	public void setProvinceid(String provinceid) {
		this.provinceid = provinceid;
	}

	public String getProvincename() {
		return provincename;
	}

	public void setProvincename(String provincename) {
		this.provincename = provincename;
	}

	public List<City> getList() {
		return list;
	}

	public void setList(List<City> list) {
		this.list = list;
	}


	//这个用来显示在PickerView上面的字符串,PickerView会通过反射获取getPickerViewText方法显示出来。
	public String getPickerViewText() {
		//这里还可以判断文字超长截断再提供显示
		return provincename;
	}

	@Override
	public String toString() {
		return "Province{" +
				"provinceid='" + provinceid + '\'' +
				", provincename='" + provincename + '\'' +
				", list=" + list +
				'}';
	}
}
