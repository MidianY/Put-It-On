import React from "react";
import './Components.css';
import mockWeatherData from '../mocks/mockWeather.json';
import { BsFillCircleFill } from "react-icons/bs";
import { WiDayCloudy, WiCloud, WiCloudy, WiDayRain, WiRain, WiThunderstorm, WiSnow, WiFog } from "react-icons/wi";

const weatherIconMap: Map<string, any[]> = new Map();
weatherIconMap.set("01d", [BsFillCircleFill, "#f9d71c"])
weatherIconMap.set("02d", [WiDayCloudy])
weatherIconMap.set("03d", [WiCloud])
weatherIconMap.set("04d", [WiCloudy])
weatherIconMap.set("09d", [WiRain])
weatherIconMap.set("10d", [WiDayRain])
weatherIconMap.set("11d", [WiThunderstorm])
weatherIconMap.set("13d", [WiSnow])
weatherIconMap.set("50d", [WiFog])


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

function WeatherBox({city}: {city: string}) {
    const weather = mockWeatherData.weather[0];
    const description = weather.description;
    const icon = weather.icon;
    const temp = mockWeatherData.main.temp;
    const feels_like = mockWeatherData.main.feels_like
    return(
        <div>
            {returnWeatherIcon(icon)}
            {temp} °F
        </div>
    )
}

export default function WeatherDisplay({city}: {city: string}) {
    return(
        <div>
        <div>The weather in <strong>{city}</strong> is:</div>
        <WeatherBox city={city}/>
        </div>
    )
}