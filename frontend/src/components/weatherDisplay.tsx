import React, { useEffect, useState } from "react";
import './Components.css';
import mockWeatherData from '../mocks/mockWeather.json';
import { BsFillCircleFill } from "react-icons/bs";
import { WiDayCloudy, WiCloud, WiCloudy, WiDayRain, WiRain, WiThunderstorm, WiSnow, WiFog, WiNightCloudy, WiNightRain } from "react-icons/wi";

const weatherIconMap: Map<string, any[]> = new Map();
weatherIconMap.set("01d", [BsFillCircleFill, "#f9d71c"])
weatherIconMap.set("01n", [BsFillCircleFill, "#383838"])
weatherIconMap.set("02d", [WiDayCloudy])
weatherIconMap.set("02n", [WiNightCloudy])
weatherIconMap.set("03d", [WiCloud])
weatherIconMap.set("03n", [WiCloud])
weatherIconMap.set("04d", [WiCloudy])
weatherIconMap.set("04n", [WiCloudy])
weatherIconMap.set("09d", [WiRain])
weatherIconMap.set("09n", [WiRain])
weatherIconMap.set("10d", [WiDayRain])
weatherIconMap.set("10n", [WiNightRain])
weatherIconMap.set("11d", [WiThunderstorm])
weatherIconMap.set("11n", [WiThunderstorm])
weatherIconMap.set("13d", [WiSnow])
weatherIconMap.set("13n", [WiSnow])
weatherIconMap.set("50d", [WiFog])
weatherIconMap.set("50n", [WiFog])

// const weatherMap: Map<string, string> = new Map();


function returnWeatherIcon(icon : string) {
    const iconResult = weatherIconMap.get(icon);
    if (iconResult !== undefined) {
        const WeatherIcon = iconResult[0];
        if (iconResult.length === 2) {
            return(
                <WeatherIcon size={50} fill={iconResult[1]}/>
            )
        }
        else {
            return(
                <WeatherIcon size={50}/>
            )
        }
    }
}

function WeatherBox({weather, loaded}: {weather: Map<string, string>, loaded: boolean}) {
    const [weatherData, setWeatherData] = useState(new Map());
    const previousWeather = async() => {
        let getWeatherFetch = await fetch("http://localhost:3230/getWeatherData")
        .then(response => response.json()).then(json => {return json})
        .catch(error => console.log('error', error));
        if (getWeatherFetch.result === "success") {
            let map = new Map();
            map.set("temp", getWeatherFetch.temp);
            let descriptionWords: string[] = getWeatherFetch.descr.split(" ");
            let description: string = descriptionWords.map((word: string) => { 
                return word[0].toUpperCase() + word.substring(1); 
            }).join(" ");
            map.set("description", description);
            map.set("feelsLike", getWeatherFetch.feelsLike);
            map.set("icon", getWeatherFetch.icon);
            map.set("city", getWeatherFetch.city);
            map.set("state", getWeatherFetch.state)
            setWeatherData(map);
        }
    }
    const weatherNeedsRetrieval: boolean = !loaded && sessionStorage.getItem("weatherLoaded") === "true";

    useEffect(() => {
        if (weatherNeedsRetrieval) {
            previousWeather();
        }
    }, [])

    const weatherLoaded: boolean = weatherData.size > 0 || weather.size > 0;
    if (weather.size === 0) {
        return(
            <div>
            {weatherNeedsRetrieval && weatherData.get("city") !== undefined && <div>The weather in <strong>{`${weatherData.get("city")}, ${weatherData.get("state")}`}</strong> is:</div>}
            {weatherLoaded &&
            <div>
                {returnWeatherIcon(weatherData.get("icon")!)}
                {weatherData.get("temp")!} °F
            </div>
            }
            {weatherLoaded &&
            <div>
                {weatherData.get("description")}
            </div>
            }
            </div>
        )
    }
    else {return (
        <div>
        <div>
            {returnWeatherIcon(weather.get("icon")!)}
            {weather.get("temp")!} °F
        </div>
        <div>
            {weather.get("description")}
        </div>
        </div>
    )
    }
}

export default function WeatherDisplay({city, weather, loaded}: {city: string, weather: Map<string, string>, loaded: boolean}) {
    return(
        <div>
        {loaded &&
        <div>The weather in <strong>{city}</strong> is:</div>
        }
        <WeatherBox weather={weather} loaded={loaded}/>
        </div>
    )
}