import React, { useState } from "react";
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
    const chooseCity = (city: string) => {
        setChosenCity(city);
    }
    return(
        <div>
            <div className="Page-header">Put It On!</div>
            <CitySearch onSelect={chooseCity}/>
            {chosenCity !== "" &&
            <WeatherDisplay city={chosenCity}/>}
            <Button className="page-button" onClick={() => {navigate("/closet")}}>Go to closet</Button>
            <PersonIcon className="person" fill="white" />
        </div>
    )
}
