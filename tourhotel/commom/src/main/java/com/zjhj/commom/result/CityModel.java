package com.zjhj.commom.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 市
 * @author brain
 *
 */
public class CityModel implements Serializable {
	private String region_id;
	private String region_name;
	private List<DistrictModel> area_list = new ArrayList<DistrictModel>();
	
	public CityModel() {
		super();
	}

	public CityModel(String region_name, List<DistrictModel> area_list) {
		super();
		this.region_name = region_name;
		this.area_list = area_list;
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

	public List<DistrictModel> getArea_list() {
		return area_list;
	}

	public void setArea_list(List<DistrictModel> area_list) {
		this.area_list = area_list;
	}

	@Override
	public String toString() {
		return "CityModel [region_name=" + region_name + ", districtList=" + area_list
				+ "]";
	}
	
}
