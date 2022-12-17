import React, { useEffect, useState } from "react";
import './Page.css';
import '../components/Components.css'
import { useNavigate } from 'react-router-dom';
import Button from 'react-bootstrap/Button';
import CitySearch from "../components/citySearch";
import WeatherDisplay from "../components/weatherDisplay";
import RecommendedOutfit from "../components/recOutfit";
import { clothingTypeMap } from "../components/clothingItem";
import { OverlayTrigger, Popover } from "react-bootstrap";

const clothingSectionMap: Map<string, string> = new Map();
clothingSectionMap.set("short-sleeve", "Top");
clothingSectionMap.set("long-sleeve", "Top");
clothingSectionMap.set("tank", "Top");
clothingSectionMap.set("sweatshirt", "Top");
clothingSectionMap.set("pants", "Bottom");
clothingSectionMap.set("shorts", "Bottom");
clothingSectionMap.set("skirt", "Bottom");
clothingSectionMap.set("hoodie", "Outer");
clothingSectionMap.set("jacket", "Outer");
clothingSectionMap.set("coat", "Outer");
clothingSectionMap.set("sneakers", "Shoes");
clothingSectionMap.set("boots", "Shoes");

function getStatsClothingIcon(clothingType : string) {
    const ClothingIcon = clothingTypeMap.get(clothingType);
    return(
        <ClothingIcon className="stats-icon" fill="white" />
    )
}

function ClosetStats() {
    const [tops, setTops] = useState(0);
    const [bottoms, setBottoms] = useState(0);
    const [outers, setOuters] = useState(0);
    const [shoes, setShoes] = useState(0);
    const fetchClosetStats = async() => {
        let stats = await fetch("http://localhost:3230/getClosetStats")
        .then(response => response.json()).then(json => {return json})
        .catch(error => console.log('error', error));
        if (stats.result === "success") {
            let closet = stats.closet;
            setTops(closet.Tops)
            setBottoms(closet.Bottoms)
            setOuters(closet.Outer)
            setShoes(closet.Shoes)
        }
    }

    useEffect(() => {
        fetchClosetStats();
    }, [])

    return(
        <div className="closet-stats" aria-label="closet stats">
            In your closet, you have:
            <div className="tops-stat">
                <div aria-label={tops + " tops"}>
                    <strong>{tops}</strong> {tops === 1 ? "Top" : "Tops"}
                </div>
                <div >
                    {getStatsClothingIcon("short-sleeve")}
                </div>
            </div>
            <div className="bottoms-stat">
                <div aria-label={bottoms + " bottoms"}>
                    <strong>{bottoms}</strong> {bottoms === 1 ? "Bottom" : "Bottoms"}
                </div>
                <div>
                    {getStatsClothingIcon("shorts")}
                </div>
            </div>
            <div className="outers-stat" >
                <div aria-label={outers + " outerwear"}>
                    <strong>{outers}</strong> Outerwear
                </div>
                <div>
                    {getStatsClothingIcon("jacket")}
                </div>
            </div>
            <div className="shoes-stat">
                <div aria-label={shoes + " shoes"}>
                    <strong>{shoes}</strong> {shoes === 1 ? "Pair of Shoes" : "Pairs of Shoes"}
                </div>
                <div>
                    {getStatsClothingIcon("sneakers")}
                </div>
            </div>
        </div>
    )
}

    
const instructionsPopover = (
  <Popover id="popover-basic">
    <Popover.Header as="h3">How to Use Put It On!</Popover.Header>
    <Popover.Body>
    1. Enter the name of your city
    <br/>
    2. Add your clothes to your closet
    <br/>
    3. Click <strong>Get Today's Outfit!</strong> to receive today's recommended outfit based on your weather and closet!
    </Popover.Body>
  </Popover>
);

export default function Home() {
    const navigate = useNavigate();
    const [chosenCity, setChosenCity] = useState("");
    const [weatherMap, setWeatherMap] = useState(new Map<string, string>());
    const [weatherLoaded, setWeatherLoaded] = useState(false);
    const [outfitButtonClicked, setOutfitButtonClicked] = useState(false);
    const weatherAvailable: boolean = weatherLoaded || sessionStorage.getItem("weatherLoaded") === "true";
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
            <div aria-label="put it on page header" className="Page-header">Put It On!</div>
            <CitySearch onSelect={chooseCity}/>
            <WeatherDisplay city={chosenCity} weather={weatherMap} loaded={weatherLoaded}/>
            {weatherAvailable &&<Button role="outfit-button" className="outfit-button" onClick={() => setOutfitButtonClicked(true)}>Get Today's Outfit!</Button>}
            <Button aria-label="button to go to closet page" className="page-button" onClick={() => {navigate("/closet")}}>Go to closet</Button>
            <ClosetStats/>
            <OverlayTrigger trigger="click" placement="bottom" overlay={instructionsPopover}>
                <Button className="instructions-button" variant="success">Click for Put It On! Instructions</Button>
            </OverlayTrigger>
            {outfitButtonClicked && <RecommendedOutfit />}
        </div>
    )
}
