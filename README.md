# Put it On!
1. Project Details
2. Backend
3. Frontend 
4. Contributors 

## Project Details
Project name: Put it On!

Project Description: A web application that saves users the hassle of deciding what to wear by recommending matching, weather-appropriate outfits based on their closet. 

Team members and contributions: Adam Remels (aremels), Jarra Omar (jomar), Midian Yoseph (myoseph), Musa Tahir (mtahir1) 

Time Spent: 4 weeks

Github Repo: https://github.com/cs0320-f2022/term-project-aremels-jomar-mtahir1-myoseph.git 

## Backend 
The `backend` directory is made up of 4 main components: the weather, closet, recommender, and server. We have a server that is implemented within our server class with the purpose of adding different API endpoints, each endpoint includes the shared state object, UserData, which contains the current user’s location data (weather, city name) as well as their closet data. Using these endpoints enables our frontend to fetch weather data, store location data, store pieces of clothing in the closet, fetch closet data, modify the closet, and fetch the recommended outfit. Each of these handlers output serialized json data to the frontend, which the front end converted to a typescript map upon retrieval.

Below we will go in depth into each of the individual components.  

### Weather
To fetch weather data, we utilized the OpenWeatherAPI to fetch current weather data within the Weather class. Rather than using all of the different types of weather data that this API records, we focused on the following key categories we thought were most relevant for our purposes: snow level, rain level, temperature, feels like temperature, city name, state name, icon, and the weather description tag. Although OpenWeatherAPI can process data for cities outside the United States, we focused only on American cities for the purposes of this project. The key method within the Weather class, `getWeatherDataFromJson`, filtered the OpenWeatherAPI’s json response for data relevant for our purposes.

For the weather handler, we created two API endpoints to optimize runtime performance. The first endpoint was designed to both store and retrieve weather information, while the second endpoint was designed solely for retrieving weather information. This optimization avoids unnecessary external API calls, which may be limited. This allows for faster and more efficient access of weather information, and helps to avoid potential issues with limited API calls. 

 Methods for the weather component were unit tested via mocks we created to model OpenWeatherAPI’s responses. We also ran integration testing on our weather component by verifying our weather associated handlers were properly updating the user data object and fetching weather data as expected. 

### Closet 
The Closet class is responsible for adding/removing items from the closet, getting statistics on the number of types of clothing items and returning the status of the current closet. The closet data, i.e the clothes, is stored in a dictionary mapped from string to another dictionary. This was done for the purposes of simplifying adding/removing clothing, allowing multiple clothing types of different colors to be added, as well as cleaner formating for the frontend. 

The data contained in the Closet Class was utilized by three handlers in the backend: `EditClosetHandler`, `GetClosetHandler`, and `GetClosetStatsHandler`. The first handler calls on either the addClothing() or removeClothing() method within the closet class, depending on the actions of the user in the frontend. The second handler utilizes the getClothesData() method, returning all the items in the users closet. The last handler calls on the getStats() method which returns the users number of tops, bottoms, outer, and shoes in their closet. All three endpoints include the shared state object to ensure that the closet is updating according to the needs of the user.  

Methods for the closet component were unit tested by creating mock closet objects and adding/removing them to that closet. Integration testing was ran to ensure that the three endpoints were properly updating and ensuring that the correct user data was being fetched. 

### Recommender

The recommendation class is responsible for handling an instance of the UserData class which has access to both the contents within the closet and weather. By having this information, we can make an informed decision on what to wear depending on the weather for that particular day and location. With pre-set colors and clothes, it’s a bit easier to distinguish and clearly identify what an individual can wear and what color (e.x. Having a list of colors or a list of lists of clothing articles)

First step is to do a simple weather check and remove any items within our validated lists of clothes. So if the temperature is less than 20 degrees and snowing, you wouldn’t want to wear any shorts with shoes and no jacket. With the use of if statements, we can remove clothing items from a validated set. From there, it’s a bit subjective; however, if it’s raining a certain mm/hr, then we would advise the user to wear boots instead of sneakers (leaving the decision to us). Once we update our validated list of what is “acceptable” for the weather, we can now iterate through each item within our class (given to us through the instance of the UserData class) and check if that item fits the description. This is where some certain edge cases come into play. What if the user didn’t give us any items within their closet, they could either be prompted to simply place items within it but we decided to give a recommendation through a variety of items that fits a certain color scheme (will get back to that later). Other questions like what if only a few of the items found in the closet work for an outfit. We start by checking the description of the item itself. What color is it and whether it's within the closet or not? If it does, then we can place it in a list of Hashmap that would be for its respective category (for example, if shorts are valid for the weather, then we’d simply place it in a list for bottoms with each index being a hashmap that maps the item’s name, color, and whether if it’s in the closet or not. We’ve organized the array of items entered from the front end into different categories (more specifically hashmaps of the same type whether its outer, inner wear, etc). Next we can check its color by iterating through each item within the respective categories and check it with a registered list of colors and its respective matches. 

For example: 
List<String> matchesRed = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "pink", "orange", "saddlebrown", "grey"));

Where within this array list would have all of the colors that matchesRed. By having a set of 12 colors with distinct colors it matches with, we can score these values. Continuing where we left off from, by iterating through the hashmap, we can not only access its color but place it in a list of present colors. Which we can use to iterate through both that list of colors and the list of matches and count each color it can match with. Filter out and get the top 4 colors present, and these values would represent the color with the highest “grade” which would be used in the outfit. However, we want to prioritize clothes that are present in the closet and then whatever color works with that group. Through the use of if statements and boolean statements checking either states of variables, we can prioritize high grade colors present but also clothes that are actually present within the closet. Lastly, depending on the size of this closet, we could have a bunch of different items that fit a certain color scheme. We can now return one piece of clothing for each category and first prioritize an item that is present, if a color isn’t present than we could simply return an item that isn’t present within the closet that works (we would tell that to the user of course). Lastly if the user does not input any clothes within the closet, then we can simply return 4 random items that fit the description of the weather. The first item and its color will be random; however, we’d remove any color it doesn’t match to. And continue to do so until we can get a 4th color for the 4th item. 
Some of the bugs I experienced was duplicates of items due to me reiterating over a list nested within another list or hashmap, so oftentimes had to make helper functions that would remove the duplicates since it wouldn’t affect the scoring or the items present or not present within the closet.


## Frontend
At the high level of our frontend, we have two components – Home and Closet – which represent the two pages of our webapp. Within the Home component, there are several child components. First, the `CitySearch` component handles a user’s searching of a city in the United States. Next, the `WeatherDisplay` component takes the `chosenCity` state (changed through the CitySearch component) to return a display of the current weather in the chosen city. There is also a ClosetStats component that displays the number of each type of clothing (top, bottom, outer, shoes) that a user currently has in their closet. Finally, the RecommendedOutfit component displays a user’s recommended outfit based on their weather and closet. All of these components interact with APIs. CitySearch interacts with the external api, Country State City API. Every other component uses endpoints to interact with our own backend server.

The next high-level component, `Closet`, is centered around the `ClothingOptions` component and the `UserCloset` component. `ClothingOptions` consists of Tabs of different `ClothingItem` components. `UserCloset` also contains `ClothingItem` components based on which clothes are selected in the `ClothingOptions` component. The `ClothingItem` component is an image of a clothing item. It contains a `ColorSelection` component that is activated when clicked. The `ColorSelection` component contains 12 `ColorBox` components. Again, these all interact with our backend server endpoints to send and receive information.

## Contributors 

To fetch weather data, we utilized the OpenWeatherAPI to fetch current weather data within the Weather class. Rather than using all of the different types of weather data that this API records, we focused on the following key categories we thought were most relevant for our purposes: snow level, rain level, temperature, feels like temperature, city name, state name, icon, and the weather description tag. Although OpenWeatherAPI can process data for cities outside the United States, we focused only on American cities for the purposes of this project. We also use the Country State City API to fetch city and state names in the United States.

For frontend design and icons, we used React Bootstrap, React Icons, and Adobe Stock.

## How to...
To use our web app, open our backend contents and run the Server.java file. Then, access the frontend directory in the terminal and type `npm start` to run the React frontend. From here, a tab with the address localhost:3000 should open. If not, type that address into the search bar. To use the app, enter the city you are in into the search bar. Then, add clothing you own to your closet. Finally, get a recommended outfit based on the weather in your city and the clothes in your closet.


