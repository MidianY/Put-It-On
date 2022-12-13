import React, { useEffect, useState } from "react";
import './Page.css';
import '../components/Components.css'
import { useNavigate } from 'react-router-dom';
import Button from 'react-bootstrap/Button';
import CitySearch from "../components/citySearch";
import WeatherDisplay from "../components/weatherDisplay";
import {ReactComponent as PersonIcon} from '../icons/person.svg'

export default function Home() {
    const navigate = useNavigate();
    const [chosenCity, setChosenCity] = useState("");
    const [weatherMap, setWeatherMap] = useState(new Map<string, string>());
    const [weatherLoaded, setWeatherLoaded] = useState(false);
    const chooseCity = (city: string) => {
        setChosenCity(city);
        getWeather(city);
        sessionStorage.setItem("weatherLoaded", "true");
        setWeatherLoaded(true);
    }

    const getWeather = async(city: string) => {
        let cityName: string = city.split(", ")[0];
        let stateName: string = city.split(", ")[1].slice(0, -1);
        let storeWeatherFetch = await fetch("http://localhost:3230/storeGetWeatherData?city=" + cityName + "&state=" + stateName)
        .then(response => response.json()).then(json => {return json})
        .catch(error => console.log('error', error));
        if (storeWeatherFetch.result === "success") {
            let map = new Map();
            map.set("temp", storeWeatherFetch.temp);
            let descriptionWords: string[] = storeWeatherFetch.descr.split(" ");
            let description: string = descriptionWords.map((word: string) => { 
                return word[0].toUpperCase() + word.substring(1); 
            }).join(" ");
            map.set("description", description);
            map.set("feelsLike", storeWeatherFetch.feelsLike);
            map.set("icon", storeWeatherFetch.icon);
            setWeatherMap(map);
        }
    }
    return(
        <div>
            <div className="Page-header">Put It On!</div>
            <CitySearch onSelect={chooseCity}/>
            <WeatherDisplay city={chosenCity} weather={weatherMap} loaded={weatherLoaded}/>
            <Button className="page-button" onClick={() => {navigate("/closet")}}>Go to closet</Button>
            <PersonIcon className="person" fill="white" />
        </div>
    )
}
