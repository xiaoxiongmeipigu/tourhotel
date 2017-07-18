package com.zjhj.commom.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 省
 * @author brain
 *
 */
public class ProvinceModel implements Serializable{
	private String region_id;
	private String region_name;
	private List<CityModel> city_list = new ArrayList<CityModel>();
	
	public ProvinceModel() {
		super();
	}

	public ProvinceModel(String region_name, List<CityModel> city_list) {
		super();
		this.region_name = region_name;
		this.city_list = city_list;
	}

	public List<CityModel> getCity_list() {
		return city_list;
	}

	public void setCity_list(List<CityModel> city_list) {
		this.city_list = city_list;
	}

	public String getRegion_id() {
		return region_id;
	}

	public void setRegion_id(String region_id) {
		this.region_id = region_id;
	}

	public String getRegion_name() {
		return region_name;
	}

	public void setRegion_name(String region_name) {
		this.region_name = region_name;
	}

	@Override
	public String toString() {
		return "ProvinceModel [region_name=" + region_name + ", cityList=" + city_list + "]";
	}
	
}
