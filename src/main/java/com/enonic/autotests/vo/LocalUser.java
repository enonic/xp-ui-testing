package com.enonic.autotests.vo;

public class LocalUser extends User{
	private Profile profile;
	
	public Profile getProfile() {
	return profile;
}

public void setProfile(Profile profile) {
	this.profile = profile;
}

	public static class Profile{
		
		private String firstName;
		private String lastName;
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		
		
	}
}
