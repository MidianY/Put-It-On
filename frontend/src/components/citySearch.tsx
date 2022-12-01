import React, {useState, useEffect, MouseEventHandler} from "react";
import './Components.css';

const cities: string[] = []

async function fetchCities() {
    let fetchedCities = await fetch("https://countriesnow.space/api/v0.1/countries")
    .then(response => response.json()).then(json => {return json})
    .catch(err => {console.log(err);})
    let cityList = fetchedCities.data[218].cities;
    for (let city in cityList) {
        cities.push(cityList[city]);
    }
}

fetchCities();

function CitiesAutocomplete({onSelect}: {onSelect: any}) {
    const [searchCities, setSearchCities] = useState<string[]>([]);
    const [value, setValue] = useState("");

    const handleSearch = (event: any) => {
        const currSearch = event.target.value;
        const filteredCities = cities.filter((city) => {
            return city.toLowerCase().startsWith(currSearch.toLowerCase());
        });
        if (currSearch === "") {
            setSearchCities([]);
        }
        else{
            setSearchCities(filteredCities);
        }
        setValue(currSearch);
    }

    const handleCityClick = (event: any) => {
        const currCity = event.target.textContent;
        setSearchCities([]);
        setValue(currCity);
        onSelect(currCity);
    }
    return(
        <div>
            <div>
                <input type="text" value={value} placeholder={"Enter city name here"} onChange={handleSearch}/>
            </div>
            {searchCities.length > 0 && (
            <div className="citySearchOptions" onClick={handleCityClick}>
                {searchCities.slice(0, 5).map((city, index) => {
                    return <div className="citySearchOption">{city} </div>
                })}
            </div>
            )}
        </div>
    )
}

export default function CitySearch({onSelect}: {onSelect: any}){
    return(
        <div className="location-input">
            <div><strong>Where are you today?</strong></div>
            <CitiesAutocomplete onSelect={onSelect}/>
        </div>
    )
}
