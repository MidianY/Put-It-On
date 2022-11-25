import React from "react";
import './Components.css';

export default function CitySearch(){
    return(
        <div className="location-input">
            <div><strong>Where are you today?</strong></div>
            <input type="text" placeholder="Enter city name here"/>
        </div>
    )
}

