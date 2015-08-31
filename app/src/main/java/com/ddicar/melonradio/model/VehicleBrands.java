package com.ddicar.melonradio.model;


public class VehicleBrands {
	private static VehicleBrands instance = null;
	public String vehicleBrands = "";

	public static VehicleBrands getInstance() {
		if (instance == null) {
			instance = new VehicleBrands();
		}
		return instance;
	}

	public String getVehicleBrands() {
		return vehicleBrands;
	}

	public void setVehicleBrands(String vehicleBrands) {
		this.vehicleBrands = vehicleBrands;
	}

}
