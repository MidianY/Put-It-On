package edu.brown.cs.student.server;

import edu.brown.cs.student.closet.Closet;

public class UserData {
  private String locationData; // json string of weather data including city/state
  private Closet currentCloset; // class representing the current closet data

  public UserData() {
    this.locationData = null;
    this.currentCloset = new Closet();
  }

  //method sets the cuurrent location data
  public void setLocationData(String locationData) {
    this.locationData = locationData;
  }

  //method returns the current location data
  public String getLocationData() {
    return locationData;
  }

  //method returns the current closet the user has
  public Closet getCurrentCloset(){
    return this.currentCloset;
  }
}
