package com.example.demo.model;

public class JusikProfile {
	private String name;
	private String ChungYakDay;
	private String RefundDay;
	private String SangJangDay;
	private String HopePrice;
	private String Companys;
	private String SetPrice;
	public JusikProfile(String name, String chungYakDay, String refundDay, String sangJangDay, String hopePrice,
			String companys, String setPrice) {
		super();
		this.name = name;
		ChungYakDay = chungYakDay;
		RefundDay = refundDay;
		SangJangDay = sangJangDay;
		HopePrice = hopePrice;
		Companys = companys;
		SetPrice = setPrice;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getChungYakDay() {
		return ChungYakDay;
	}
	public void setChungYakDay(String chungYakDay) {
		ChungYakDay = chungYakDay;
	}
	public String getRefundDay() {
		return RefundDay;
	}
	public void setRefundDay(String refundDay) {
		RefundDay = refundDay;
	}
	public String getSangJangDay() {
		return SangJangDay;
	}
	public void setSangJangDay(String sangJangDay) {
		SangJangDay = sangJangDay;
	}
	public String getHopePrice() {
		return HopePrice;
	}
	public void setHopePrice(String hopePrice) {
		HopePrice = hopePrice;
	}
	public String getCompanys() {
		return Companys;
	}
	public void setCompanys(String companys) {
		Companys = companys;
	}
	public String getSetPrice() {
		return SetPrice;
	}
	public void setSetPrice(String setPrice) {
		SetPrice = setPrice;
	}
	
	
	

}