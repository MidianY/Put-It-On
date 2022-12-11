package edu.brown.cs.student.server;

import edu.brown.cs.student.closet.Closet;

public class UserData {
  private String locationData; // json string of weather data including city/state
  private Closet currentCloset; // class representing the current closet data

  /**
   * Class represents shared user state, stores closet data and weather data for
   * the recommender to use
   */
  public UserData() {
    this.locationData = null;
    this.currentCloset = new Closet();
  }

  //method sets the current location data
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
