package com.zjhj.commom.result;

import java.io.Serializable;

/**
 * 区/县
 * @author brain
 *
 */
public class DistrictModel implements Serializable {

	private String region_id;
	private String region_name;
	
	public DistrictModel() {
		super();
	}

	public DistrictModel(String region_name, String region_id) {
		super();
		this.region_name = region_name;
		this.region_id = region_id;
	}

	public String getRegion_name() {
		return region_name;
	}

	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}

	public String getRegion_id() {
		return region_id;
	}

	public void setRegion_id(String region_id) {
		this.region_id = region_id;
	}

	@Override
	public String toString() {
		return "DistrictModel [region_name=" + region_name + ", region_id=" + region_id + "]";
	}

}
