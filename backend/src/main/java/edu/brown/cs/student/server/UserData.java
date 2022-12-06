package edu.brown.cs.student.server;

public class UserData {
  private String locationData; // json string of weather data including city/state
  // reference closet data structure here as attribute

  public UserData() {
    this.locationData = null;
    // initialize empty closet here
  }

  public void setLocationData(String locationData) {
    this.locationData = locationData;
  }

  public String getLocationData() {
    return locationData;
  }
}
