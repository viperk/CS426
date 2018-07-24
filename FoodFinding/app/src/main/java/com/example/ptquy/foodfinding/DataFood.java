package com.example.ptquy.foodfinding;

import android.content.Intent;

import java.io.Serializable;

public class DataFood implements Serializable{
	private String placeId;
	private String RestaurantName;
	private float Rating;
	private String AvtID;
	private String DetailAddress;
	private String Description;
	private String phone;
	private String websiteURL;
	private int priceRange;
	private Boolean isOpen;

	public DataFood(){};

	public DataFood(String placeId, String restaurantName,
					float rating, String avtID,
					String detailAddress, String phone, String websiteURL, int priceRange, Boolean isOpen) {
		this.placeId = placeId;
		RestaurantName = restaurantName;
		Rating = rating;
		AvtID = avtID;
		DetailAddress = detailAddress;
		this.phone = phone;
		this.websiteURL = websiteURL;
		this.priceRange = priceRange;
		this.isOpen = isOpen;
		this.Description = "none";
	}

	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getRestaurantName() {
		return RestaurantName;
	}

	public void setRestaurantName(String restaurantName) {
		RestaurantName = restaurantName;
	}

	public float getRating() {
		return Rating;
	}

	public void setRating(float rating) {
		Rating = rating;
	}

	public String getAvtID() {
		return AvtID;
	}

	public void setAvtID(String avtID) {
		AvtID = avtID;
	}

	public String getDetailAddress() {
		return DetailAddress;
	}

	public void setDetailAddress(String detailAddress) {
		DetailAddress = detailAddress;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWebsiteURL() {
		return websiteURL;
	}

	public void setWebsiteURL(String websiteURL) {
		this.websiteURL = websiteURL;
	}

	public int getPriceRange() {
		return priceRange;
	}

	public void setPriceRange(int priceRange) {
		this.priceRange = priceRange;
	}

	public Boolean getOpen() {
		return isOpen;
	}

	public void setOpen(Boolean open) {
		isOpen = open;
	}
}
