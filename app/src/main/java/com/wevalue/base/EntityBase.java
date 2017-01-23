package com.wevalue.base;

import java.io.Serializable;


public class EntityBase implements Serializable {

	private static final long serialVersionUID = -1275850558776955843L;


	public Long id; // 本地id（pk）自增长


	public long getId() {
		if(id == null){
			return 0;
		}else{
			return id;
		}
	}

	public void setId(Long id) {
		this.id = id;
	}

}