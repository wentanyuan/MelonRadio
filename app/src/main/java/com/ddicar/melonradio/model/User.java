package com.ddicar.melonradio.model;

import org.json.JSONException;
import org.json.JSONObject;

public class User {


	public User() {

	}

	public User(JSONObject data) {
		try {
			_id = (String) data.get("_id");
			pic = (String) data.get("pic");
			deviceSN = (String) data.get("deviceSN");
			name = (String) data.get("name");
			phone = (String) data.get("phone");
			sex = (String) data.get("sex");
			intro = (String) data.get("intro");

            JSONObject truckJson = (JSONObject) data.get("truck");
            truck = new Truck(truckJson);

            JSONObject teamJson = (JSONObject) data.get("team");
            team = new Team(teamJson);


        } catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public String _id;

	public String pic;

	public String deviceSN;

	public String name;

	public String phone;

	public String sex;

	public String intro;

	public Truck truck;

    public Team team;

}
