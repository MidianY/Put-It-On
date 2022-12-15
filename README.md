# Put it On!

1. Project Details
2. Backend
3. Frontend
4. Tests 
5. Errors
6. Contributors/Resources


## Project Details
Project name: Put it On!

Project Description: A web application that saves users the hassle of deciding what to wear by recommending matching, weather-appropriate outfits based on their closet. 

Team members and contributions: 

Time Spent:4 weeks

Github Repo:

## Backend 
The `backend` directory is made up of 4 main components: the weather, closet, recommender. We have a server that is implemented within our server class with the purpose of adding different API endpoints, each endpoint includes the shared state object, UserData, which contains the current user’s location data (weather, city name) as well as their closet data. Using these endpoints enables our frontend to fetch weather data, store location data, store pieces of clothing in the closet, fetch closet data, modify the closet, and fetch the recommended outfit. Each of these handlers output serialized json data to the frontend, which the front end converted to a typescript map upon retrieval.

Below we will go in depth into each of the individual components.  

### Weather

To fetch weather data, we utilized the OpenWeatherAPI to fetch current weather data. Rather than using all of the different types of weather data that this API records, we focused on the following key categories we thought were most relevant for our purposes: snow level, rain level, temperature, feels like temperature, city name, state name, icon, and the weather description tag. Although OpenWeatherAPI can process data for cities outside the United States, we focused only on American cities for the purposes of this project. 

### Closet 


### Recommender



## Frontend


## Tests


## Errors


## Contributors 

