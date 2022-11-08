package org.mocken.user;


import org.json.JSONArray;
import org.json.JSONObject;

public class User {
	
	
	private final long user_id;
	private final String email;
	private final long trainer_id;
	private String first_name = "";
	private String last_name = "";
	private String profile_picture_link = null;
	private String street = "";
	private String zip_code = "";
	private String city = "";
	private String country = "";
	private long company_id = -1;
	private String company_name = null;
	private String company_logo = null;
	private JSONArray company_subscriptions = new JSONArray();
	private JSONObject company_configuration = new JSONObject();
	private JSONArray equipment = new JSONArray();
	
	
	public User (long user_id, String email,long trainer_id) {
		this.user_id = user_id;
		this.email = email;
		this.trainer_id=trainer_id;
	}
	
	public User (JSONObject json) {
		this.user_id = json.getLong("user_id");
		this.email = json.getString("email");
		this.trainer_id=json.getLong("trainer_id");
		setFirstName(json.has("first_name")?json.getString("first_name"):"");
		setLastName(json.has("last_name")?json.getString("last_name"):"");
		setCity(json.has("city")?json.getString("city"):"");
		setProfilePictureLink(json.has("profile_picture_link")?json.getString("profile_picture_link"):null);
		setCountry(json.has("country")?json.getString("country"):"");
		setZipCode(json.has("zip_code")?json.getString("zip_code"):"");
		setStreet(json.has("street")?json.getString("street"):"");
		setEquipment(json.has(last_name)?json.getJSONArray("user_equipment"):new JSONArray());
		if (json.has("company"))
			setCompany(json.getJSONObject("company"));
	}
		
	public long getUserId () {
		return user_id;
	}

	public String getEmail() {
		return email;
	}

	public String getFirstName() {
		return first_name;
	}

	public void setFirstName(String first_name) {
		this.first_name = first_name;
	}

	public String getLastName() {
		return last_name;
	}

	public void setLastName(String last_name) {
		this.last_name = last_name;
	}
	
	public long getTrainerId() {
		return trainer_id;
	}

	public String getProfilePictureLink() {
		return profile_picture_link;
	}

	public void setProfilePictureLink(String profile_picture_link) {
		this.profile_picture_link = profile_picture_link;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZipCode() {
		return zip_code;
	}

	public void setZipCode(String zip_code) {
		this.zip_code = zip_code;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	
	public JSONObject getCompany() {
		JSONObject json = new JSONObject();
		json.put("company_id", company_id);
		json.put("company_name", company_name);
		json.put("company_logo", company_logo);
		json.put("company_subscription_id",company_subscriptions.isEmpty()?0L:Long.valueOf((int)company_subscriptions.get(0)));
		json.put("company_subscriptions",company_subscriptions);
		json.put("company_configuration", company_configuration);
		return json;
	}	

	public void setCompany(long company_id,String company_name,String company_logo,
			String company_subscriptions,
			String company_configuration) {
		this.company_id = company_id;
		this.company_name = company_name;
		this.company_logo = company_logo;
		if (company_subscriptions!=null)
			this.company_subscriptions = new JSONArray(company_subscriptions);
		this.company_configuration = company_configuration!=null?new JSONObject(company_configuration):new JSONObject();
		
	}
	
	public void setCompany(JSONObject company) {
		this.company_id = company.getLong("company_id");
		this.company_name = company.has("company_name")?company.getString("company_name"):null;
		this.company_logo = company.has("company_logo")?company.getString("company_logo"):null;
		this.company_subscriptions = company.has("company_subscriptions")?company.getJSONArray("company_subscriptions"):new JSONArray();
		this.company_configuration = company.has("company_configuration")?company.getJSONObject("company_configuration"):new JSONObject();
		
	}

	public void setEquipment(String equipment) {
		try {
			this.equipment=new JSONArray(equipment);
		}
		catch (Exception e) {
			//ignore
		}	
	}
	
	public void setEquipment(JSONArray equipment) {
		this.equipment=equipment;
	}
	
	public JSONArray getEquipment() {
		return equipment;
	}

	
	public JSONObject toJSON() {
		JSONObject json = new JSONObject();
		json.put("user_id", getUserId());
		json.put("trainer_id", getTrainerId());
		json.put("first_name", getFirstName());
		json.put("last_name", getLastName());
		json.put("email", getEmail());
		json.put("profile_picture_link", getProfilePictureLink());
		json.put("street", getStreet());
		json.put("zip_code", getZipCode());
		json.put("city", getCity());
		json.put("country", getCountry());
		json.put("company", getCompany());
		json.put("user_equipment", getEquipment());
		
		return json;
	}
	
	public long getCompanySubscriptionState() {
		return (long) company_subscriptions.get(0);
	}

	
	public long getCompanyId() {
		return company_id;
		
	}
}



