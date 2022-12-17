import React, {useState, useEffect} from "react";
import './Components.css';

var headers = new Headers();
headers.append("X-CSCAPI-KEY", "cFF0alBJWllPWjVzOGhlTHRvMXRVdnUyM2dJeHhjTGR2SGFhd2hJUA==");

var requestOptions: RequestInit = {
 method: 'GET',
 headers: headers,
 redirect: 'follow'
};

function CitiesAutocomplete({onSelect}: {onSelect: any}) {
    const [allCities, setAllCities] = useState<string[]>([])
    const [searchCities, setSearchCities] = useState<string[]>([]);
    const [value, setValue] = useState("");
    useEffect(() => {
        let cities: string[] = [];
        let states = [ 'AL', 'AK', 'AS', 'AZ', 'AR', 'CA', 'CO', 'CT', 'DE', 'DC', 'FM', 'FL', 'GA', 'GU', 'HI', 'ID', 'IL', 'IN', 'IA', 'KS', 'KY', 'LA', 'ME', 'MH', 'MD', 'MA', 'MI', 'MN', 'MS', 'MO', 'MT', 'NE', 'NV', 'NH', 'NJ', 'NM', 'NY', 'NC', 'ND', 'MP', 'OH', 'OK', 'OR', 'PW', 'PA', 'PR', 'RI', 'SC', 'SD', 'TN', 'TX', 'UT', 'VT', 'VI', 'VA', 'WA', 'WV', 'WI', 'WY' ];
        const getStateCities = async(state: string) => { 
            let citiesFetch = await fetch("https://api.countrystatecity.in/v1/countries/US/states/" + state + "/cities", requestOptions)
            .then(response => response.json())
            .then(json => {return json})
            .catch(error => console.log('error', error));
            for (let city in citiesFetch) {
                cities.push(citiesFetch[city]["name"].concat(", ", state))
            }
        };
        const getCities = () => {
            for (let state of states) {
                getStateCities(state);
            }
        };
        getCities();
        setAllCities(cities);
     }, []);
    const handleSearch = (event: any) => {
        const currSearch = event.target.value;
        const filteredCities = allCities.filter((city) => {
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
                <input aria-label="enter city name here" type="text" value={value} placeholder={"Enter city name here"} onChange={handleSearch}/>
            </div>
            {searchCities.length > 0 && (
            <div className="citySearchOptions" onClick={handleCityClick}>
                {searchCities.slice(0, 5).map((city, index) => {
                    return <div aria-label={"city option: " + city} className="citySearchOption">{city} </div>
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
