package edu.brown.cs.student.recommender;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.closet.Closet;
import edu.brown.cs.student.server.UserData;
import java.io.IOException;
import java.util.*;

public class Recommender {
    final JsonAdapter<WeatherReport> weatherReportJsonAdapter;
    final UserData data;

    public Recommender(UserData data){
        Moshi moshi = new Moshi.Builder().build();
        this.weatherReportJsonAdapter = moshi.adapter(WeatherReport.class);
        this.data = data;
    }

    /**
     *
     * @param data instance of UserData class that hold the information for the closet and weather api
     * @return a hashmap with a key value outfit and the given outfit for the weather and what's present in the closet
     * @throws IOException checks to see if the colors and any other strings are properly defined in any objects
     */
    public List<Map<String,String>> recommender(UserData data) throws IOException {
        ArrayList<String> validOuterTopNames = new ArrayList<>(Arrays.asList("jacket", "coat", "hoodie"));
        ArrayList<String> validInnerTopNames = new ArrayList<>(Arrays.asList("long-sleeve", "short-sleeve", "sweatshirt", "tank"));
        ArrayList<String> validBottomNames = new ArrayList<>(Arrays.asList("pants", "shorts", "skirt"));
        ArrayList<String> validShoeNames = new ArrayList<>(Arrays.asList("sneakers", "boots"));

        Map<String, ArrayList<Map<String, String>>> desiredFit = new HashMap<>();

        List<List<String>> pieces = validatePieces(data,validOuterTopNames,validInnerTopNames,validBottomNames,validShoeNames);

        if(data.getCurrentCloset().getClothesData().size() == 0){
            ArrayList<ArrayList<Map<String,String>>> presentCloset = checkCloset(data, pieces.get(0), pieces.get(1), pieces.get(2), pieces.get(3));
            desiredFit.put("outfit", presentCloset.get(0));
        }

        else {
            ArrayList<ArrayList<Map<String,String>>> presentCloset = checkCloset(data, pieces.get(0), pieces.get(1), pieces.get(2), pieces.get(3));
            List<String> outfit = choseColors(presentCloset);
            List<HashMap<String, Integer>> checkedColor = colorCheck(getValidColors(),outfit);
            Map<String,List> colorsRanked = rankColors(presentCloset,checkedColor);
            returnProperCloset(presentCloset,colorsRanked);
            validItem(presentCloset,checkedColor,colorsRanked);
            desiredFit = checkColor(presentCloset);
        }

        return desiredFit.get("outfit");
    }

    /**
     *
     * @param data instance of UserData class that hold the information for the closet and weather api
     * @return a boolean of true or false depending on how much it is raining or snowing
     * @throws IOException, the data containing the weather may have incorrect values which wouldn't fit the objects
     * we're holding them in
     */
    public Boolean precipitationCheck(UserData data) throws IOException {
        String weatherReport = data.getLocationData();
        WeatherReport forecast = this.fromWeatherJson(weatherReport);

        if (forecast.rain() >= 2 || forecast.snow() > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     *
     * @param data instance of UserData class that hold the information for the closet and weather api
     * @param validOuterTopNames the valid outer top clothes that we define in the scope of our app as wearable
     * @param validInnerTopNames the valid inner top clothes that we define in the scope of our app as wearable
     * @param validBottomNames the valid bottoms that we define in the scope of our app as wearable
     * @param validShoeNames the footwear that we define in the scope of our app as wearable
     * @return updated valid lists depending on the weather (would remove certain indexes depending on the weather)
     * @throws IOException the data containing the weather may have incorrect values which wouldn't fit the objects
     * we're holding them in
     */
    public List<List<String>> validatePieces(UserData data, ArrayList<String> validOuterTopNames,
                                             ArrayList<String> validInnerTopNames, ArrayList<String> validBottomNames, ArrayList<String> validShoeNames) throws IOException {

        String weatherReport = data.getLocationData();
        WeatherReport forecast = this.fromWeatherJson(weatherReport);
        List<List<String>> pieces = new ArrayList<>();

        if (forecast.feelsLike() >= 70) {
            if(precipitationCheck(data)){
                validOuterTopNames.remove("coat");
                validShoeNames.remove("sneakers");
                validBottomNames.remove("shorts");
                validBottomNames.remove("skirt");
            }
            else{
                validOuterTopNames.remove("jacket");
                validOuterTopNames.remove("coat");
                validOuterTopNames.remove("hoodie");
                validInnerTopNames.remove("sweatshirt");
                validShoeNames.remove("boots");
            }
        } else if (forecast.feelsLike() >= 60 && forecast.feelsLike() < 70) {
            if(precipitationCheck(data)){
                validOuterTopNames.remove("coat");
                validShoeNames.remove("sneakers");
                validBottomNames.remove("shorts");
                validBottomNames.remove("skirt");
            }
            else{
                validOuterTopNames.remove("jacket");
                validOuterTopNames.remove("coat");
                validShoeNames.remove("boots");
            }
        } else if (forecast.feelsLike() >= 40 && forecast.feelsLike() < 60) {
            if(precipitationCheck(data)){
                validInnerTopNames.remove("tank");
                validInnerTopNames.remove("short-sleeve");
                validBottomNames.remove("shorts");
                validBottomNames.remove("skirt");
                validShoeNames.remove("sneakers");
            }
            else{
                validInnerTopNames.remove("tank");
                validBottomNames.remove("shorts");
                validBottomNames.remove("skirt");
                validShoeNames.remove("boots");
            }
        } else if (forecast.feelsLike() >= 33 && forecast.feelsLike() < 40) {
            if(precipitationCheck(data)){
                validOuterTopNames.remove("hoodie");
                validInnerTopNames.remove("tank");
                validInnerTopNames.remove("short-sleeve");
                validBottomNames.remove("shorts");
                validBottomNames.remove("skirt");
                validShoeNames.remove("sneakers");
            }
            else{
                validOuterTopNames.remove("hoodie");
                validInnerTopNames.remove("tank");
                validInnerTopNames.remove("short-sleeve");
                validBottomNames.remove("shorts");
                validBottomNames.remove("skirt");
            }
        } else {
            validOuterTopNames.remove("hoodie");
            validOuterTopNames.remove("jacket");
            validInnerTopNames.remove("tank");
            validInnerTopNames.remove("short-sleeve");
            validBottomNames.remove("shorts");
            validBottomNames.remove("skirt");
            validShoeNames.remove("sneakers");
        }

        pieces.add(validOuterTopNames);
        pieces.add(validInnerTopNames);
        pieces.add(validBottomNames);
        pieces.add(validShoeNames);

        return pieces;
    }

    /**
     *
     * @param data instance of UserData class that hold the information for the closet and weather api
     * @param validOuterTopNames takes in the updated valid clothing items and cross-references that with the items in the closet
     * @param validInnerTopNames takes in the updated valid clothing items and cross-references that with the items in the closet
     * @param validBottomNames takes in the updated valid clothing items and cross-references that with the items in the closet
     * @param validShoeNames takes in the updated valid clothing items and cross-references that with the items in the closet
     * @return the data containing the weather may have incorrect values which wouldn't fit the objects
     * we're holding them in (meaning you shouldn't be wearing a t shirt when it's 20 degrees out), so we mutate the list
     * with the following valid pieces to fit the criteria.
     */
    public ArrayList<ArrayList<Map<String,String>>> checkCloset(UserData data, List<String> validOuterTopNames,
                                                               List<String> validInnerTopNames, List<String> validBottomNames, List<String> validShoeNames){

        List<List<String>> availableClothing = new ArrayList<>();
        availableClothing.add(validOuterTopNames);
        availableClothing.add(validInnerTopNames);
        availableClothing.add(validBottomNames);
        availableClothing.add(validShoeNames);

        Map<String,String> outerTop;
        Map<String,String> outerTop2 = new HashMap<>();
        ArrayList<Map<String,String>> outerTopList = new ArrayList<>();
        Map<String,String> innerTop;
        Map<String,String> innerTop2 = new HashMap<>();
        ArrayList<Map<String,String>> innerTopList = new ArrayList<>();
        Map<String,String> bottom;
        Map<String,String> bottom2 = new HashMap<>();
        ArrayList<Map<String,String>> bottomList = new ArrayList<>();
        Map<String,String> footwear;
        Map<String,String> footwear2 = new HashMap<>();
        ArrayList<Map<String,String>> footWearList = new ArrayList<>();
        ArrayList<ArrayList<Map<String,String>>> list = new ArrayList<>();

        Closet closetReport = data.getCurrentCloset();
        List<Map<String, String>> closet = closetReport.getClothesData();

        Boolean isEmpty = false;

        if (closet.isEmpty()){

            isEmpty = true;

            if(validOuterTopNames.size() != 0) {
                outerTop2.put("item", validOuterTopNames.get(new Random().nextInt(validOuterTopNames.size())));
                String randomColor = getColors().get(new Random().nextInt(getColors().size()));
                int randomColorIndex = getColors().indexOf(randomColor);
                List<HashMap<String, List<String>>> test = returnRandomColorMatch(getColors().get(new Random().nextInt(getColors().size())), getValidColors(),getColors());
                test.get(randomColorIndex);
                outerTop2.put("color", randomColor);
                outerTop2.put("inCloset", "false");
                closet.add(outerTop2);
            }

            innerTop2.put("item", validInnerTopNames.get(new Random().nextInt(validInnerTopNames.size())));
            String randomColor = getColors().get(new Random().nextInt(getColors().size()));
            int randomColorIndex = getColors().indexOf(randomColor);
            List<HashMap<String, List<String>>> test = returnRandomColorMatch(getColors().get(new Random().nextInt(getColors().size())), getValidColors(),getColors());
            test.get(randomColorIndex);
            innerTop2.put("color",randomColor);
            innerTop2.put("inCloset", "false");

            bottom2.put("item", validBottomNames.get(new Random().nextInt(validBottomNames.size())));
            String randomColor2 = getColors().get(new Random().nextInt(getColors().size()));
            int randomColorIndex2 = getColors().indexOf(randomColor2);
            List<HashMap<String, List<String>>> test2 = returnRandomColorMatch(getColors().get(new Random().nextInt(getColors().size())), getValidColors(),getColors());
            test2.get(randomColorIndex2);
            bottom2.put("color", randomColor2);
            bottom2.put("inCloset", "false");

            footwear2.put("item", validShoeNames.get(new Random().nextInt(validShoeNames.size())));
            String randomColor3 = getColors().get(new Random().nextInt(getColors().size()));
            int randomColorIndex3 = getColors().indexOf(randomColor2);
            List<HashMap<String, List<String>>> test3 = returnRandomColorMatch(getColors().get(new Random().nextInt(getColors().size())), getValidColors(),getColors());
            test3.get(randomColorIndex3);
            footwear2.put("color", randomColor3);
            footwear2.put("inCloset", "false");

            closet.add(innerTop2);
            closet.add(bottom2);
            closet.add(footwear2);
            ArrayList<Map<String, String>> newCloset = (ArrayList<Map<String, String>>) closet;
            list.add(newCloset);

        }

        if(!isEmpty) {
            for (int i = 0; i < availableClothing.size(); i++) {
                Boolean outerTracker = false;
                for (Map<String, String> item : closet) {
                    if (availableClothing.get(0).size() == 0) {
                        outerTracker = true;
                        outerTop = new HashMap<>();
                        outerTop.put("item", "outerTop");
                        outerTop.put("color", getColors().get(new Random().nextInt(getColors().size())));
                        outerTop.put("inCloset", "false");
                        outerTopList.add(outerTop);
                        checkList(outerTopList);
                    }
                    if (validOuterTopNames.contains(item.get("item"))) {
                        outerTop = new HashMap<>();
                        outerTop.put("item", item.get("item"));
                        outerTop.put("color", item.get("color"));
                        outerTop.put("inCloset", "true");
                        outerTopList.add(outerTop);
                        checkList(outerTopList);
                    }
                    if (validInnerTopNames.contains(item.get("item"))) {
                        innerTop = new HashMap<>();
                        innerTop.put("item", item.get("item"));
                        innerTop.put("color", item.get("color"));
                        innerTop.put("inCloset", "true");
                        innerTopList.add(innerTop);
                        checkList(innerTopList);
                    }
                    if (validBottomNames.contains(item.get("item"))) {
                        bottom = new HashMap<>();
                        bottom.put("item", item.get("item"));
                        bottom.put("color", item.get("color"));
                        bottom.put("inCloset", "true");
                        bottomList.add(bottom);
                        checkList(bottomList);
                    }
                    if (validShoeNames.contains(item.get("item"))) {
                        footwear = new HashMap<>();
                        footwear.put("item", item.get("item"));
                        footwear.put("color", item.get("color"));
                        footwear.put("inCloset", "true");
                        footWearList.add(footwear);
                        checkList(footWearList);
                    }
                    if (i == 0 && availableClothing.get(0).size() != 0) {
                        outerTop = new HashMap<>();
                        outerTop.put("item", validOuterTopNames.get(new Random().nextInt(validOuterTopNames.size())));
                        outerTop.put("color", "black");
                        outerTop.put("inCloset", "false");
                        outerTopList.add(outerTop);
                        checkList(outerTopList);
                    } else if (i == 1) {
                        innerTop = new HashMap<>();
                        innerTop.put("item", validInnerTopNames.get(new Random().nextInt(validInnerTopNames.size())));
                        innerTop.put("color", getColors().get(new Random().nextInt(getColors().size())));
                        innerTop.put("inCloset", "false");
                        innerTopList.add(innerTop);
                        checkList(innerTopList);
                    } else if (i == 2) {
                        bottom = new HashMap<>();
                        bottom.put("item", validBottomNames.get(new Random().nextInt(validBottomNames.size())));
                        bottom.put("color", getColors().get(new Random().nextInt(getColors().size())));
                        bottom.put("inCloset", "false");
                        bottomList.add(bottom);
                        checkList(bottomList);
                    } else if (i == 3) {
                        footwear = new HashMap<>();
                        footwear.put("item", validShoeNames.get(new Random().nextInt(validShoeNames.size())));
                        if (footwear.get("item").equals("sneakers")) {
                            footwear.put("color", "white");
                        } else {
                            footwear.put("color", "saddlebrown");
                        }
                        footwear.put("inCloset", "false");
                        footWearList.add(footwear);
                        checkList(footWearList);
                    }
                }

                if (!outerTracker) {
                    list.add(outerTopList);
                }

                list.add(innerTopList);
                list.add(bottomList);
                list.add(footWearList);
                checkArray(list);
            }
        }
        return list;
    }

    /**
     * @param pieceList holds a list of items and due to the iterating through multiple for loops, we check to see
     * if there is any duplicates
     */
    public void checkList(ArrayList<Map<String, String>> pieceList){

        String item;
        String color;
        String inCloset;

        for (int i = 0; i < pieceList.size(); i++) {
            item = pieceList.get(i).get("item");
            color = pieceList.get(i).get("color");
            inCloset = pieceList.get(i).get("inCloset");

            int tracker = 0;

            for (int j = 0; j < pieceList.size(); j++){
                if (pieceList.get(j).get("item").equals(item) && pieceList.get(j).get("color").equals(color) && !pieceList.get(j).get("color").equals("null") && pieceList.get(j).get("inCloset").equals(inCloset)){
                    tracker++;
                    if(tracker > 1) {
                        pieceList.remove(i);
                    }
                }
            }
            if(pieceList.size() > 1){
                if(inCloset.equals("false")){
                    pieceList.remove(i);
                }
            }
        }
    }

    /**
     *
     * @param list takes in the list of list of hashmaps
     * @return returns a list of list of hashmaps and due to the iterating through multiple for loops, we check to see
     if there is any duplicates
     */
    public ArrayList<ArrayList<Map<String, String>>> checkArray(ArrayList<ArrayList<Map<String, String>>> list){

        for (int i = 0; i < list.size(); i++) {
            List<Map<String, String>> currentIndex = list.get(i);

            int tracker = 0;

            for (int j = 0; j < list.size(); j++){
                if (list.get(j).equals(currentIndex)){
                    tracker++;
                    if(tracker > 1){
                        list.remove(i);
                    }
                }
            }
        }
        return list;
    }

    /**
     *
     * @param filteredCloset takes in the present closet that fits the criteria
     * @return organzies the list of the items in the closet present into order of outer top, inner top, etc in order
     * to give priority to not only what's available in the closet but also to clearly define what we're returning since
     * we wouldn't to have three pants that fit the weather and color criteria being returned. Uses for loops to iterate
     * through every list of items (ex. {item=boots, inCloset=false, color=saddlebrown}) and checks individual key values
     */
    public Map<String,ArrayList<Map<String,String>>> checkColor(ArrayList<ArrayList<Map<String,String>>> filteredCloset){

        Map<String,String> outerTop;

        Map<String,String> innerTop;

        Map<String,String> bottom;

        Map<String,String> footwear;

        Map<String,ArrayList<Map<String,String>>> outfit = new HashMap<>();

        ArrayList<Map<String,String>> list = new ArrayList<>();

        checkArray(filteredCloset);

        for (int i = 0; i < filteredCloset.size(); i++) {
            if(filteredCloset.get(i).size() >= 2){
                getFinalPiece(filteredCloset.get(i));
            }

            for (int j = 0; j < filteredCloset.get(i).size(); j++) {

                if (filteredCloset.size() == 3) {
                    if (i == 0) {
                        innerTop = new HashMap<>();
                        innerTop.put("item", filteredCloset.get(i).get(j).get("item"));
                        innerTop.put("color", filteredCloset.get(i).get(j).get("color"));
                        innerTop.put("inCloset", filteredCloset.get(i).get(j).get("inCloset"));
                        list.add(innerTop);

                    } else if (i == 1) {
                        bottom = new HashMap<>();
                        bottom.put("item", filteredCloset.get(i).get(j).get("item"));
                        bottom.put("color", filteredCloset.get(i).get(j).get("color"));
                        bottom.put("inCloset", filteredCloset.get(i).get(j).get("inCloset"));
                        list.add(bottom);

                    } else {
                        footwear = new HashMap<>();
                        footwear.put("item", filteredCloset.get(i).get(j).get("item"));
                        footwear.put("color", filteredCloset.get(i).get(j).get("color"));
                        footwear.put("inCloset", filteredCloset.get(i).get(j).get("inCloset"));
                        list.add(footwear);
                    }

                } else {

                    if (i == 0) {
                        outerTop = new HashMap<>();
                        outerTop.put("item", filteredCloset.get(i).get(j).get("item"));
                        outerTop.put("color", filteredCloset.get(i).get(j).get("color"));
                        outerTop.put("inCloset", filteredCloset.get(i).get(j).get("inCloset"));
                        list.add(outerTop);

                    } else if (i == 1) {
                        innerTop = new HashMap<>();
                        innerTop.put("item", filteredCloset.get(i).get(j).get("item"));
                        innerTop.put("color", filteredCloset.get(i).get(j).get("color"));
                        innerTop.put("inCloset", filteredCloset.get(i).get(j).get("inCloset"));
                        list.add(innerTop);

                    } else if (i == 2) {
                        bottom = new HashMap<>();
                        bottom.put("item", filteredCloset.get(i).get(j).get("item"));
                        bottom.put("color", filteredCloset.get(i).get(j).get("color"));
                        bottom.put("inCloset", filteredCloset.get(i).get(j).get("inCloset"));
                        list.add(bottom);

                    } else if (i == 3) {
                        footwear = new HashMap<>();
                        footwear.put("item", filteredCloset.get(i).get(j).get("item"));
                        footwear.put("color", filteredCloset.get(i).get(j).get("color"));
                        footwear.put("inCloset", filteredCloset.get(i).get(j).get("inCloset"));
                        list.add(footwear);
                    }
                }
            }
        }

        outfit.put("outfit", list);

        return outfit;
    }

    /**
     *
     * @param list takes in a list of hashmaps that contains all the pieces of items that passed the criteria for both
     * the weather and color matching scheme. While there can be multiple that pass, we return a random instance
     * of this list so that the user can receive multiple ideas of inspiration (given there are multiple outfits in the first place)
     */
    public void getFinalPiece(List<Map<String, String>> list){

        for(int i = 0; i < list.size(); i++){

            Boolean inCloset = false;

            for(Map<String, String> piece : list) {
                if (piece.get("inCloset").equals("true")) {
                    inCloset = true;
                }
            }

            if(!inCloset){
                list.remove(new Random().nextInt(list.size()));
            }
            else{
                if(list.get(i).get("inCloset").equals("false")){
                    list.remove(i);
                }
            }
        }
    }


    /**
     *
     * @return this method returns the list of valid colors and the colors we determined to match with. Meaning any change
     * in what matches with what, can result in a completely different color matching scheme for the algorithm
     */
    public List<HashMap<String,List<String>>> getValidColors(){

        List<HashMap<String, List<String>>> validColors = new ArrayList<>();

        HashMap<String, List<String>> white = new HashMap<>();
        List<String> matchesWhite = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "green", "yellow", "purple", "pink", "orange", "saddlebrown", "grey"));
        white.put("white", matchesWhite);
        validColors.add(white);

        HashMap<String, List<String>> black = new HashMap<>();
        List<String> matchesBlack = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "green", "yellow", "purple", "pink", "orange", "saddlebrown", "grey"));
        black.put("black", matchesBlack);
        validColors.add(black);

        HashMap<String, List<String>> blue = new HashMap<>();
        List<String> matchesBlue = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "green", "orange", "grey"));
        blue.put("blue", matchesBlue);
        validColors.add(blue);

        HashMap<String, List<String>> khaki = new HashMap<>();
        List<String> matchesKhaki = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "green", "yellow", "purple", "pink", "orange", "saddlebrown", "grey"));
        khaki.put("khaki", matchesKhaki);
        validColors.add(khaki);

        HashMap<String, List<String>> red = new HashMap<>();
        List<String> matchesRed = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "pink", "orange", "saddlebrown", "grey"));
        red.put("red", matchesRed);
        validColors.add(red);

        HashMap<String, List<String>> green = new HashMap<>();
        List<String> matchesGreen = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "green", "purple", "orange", "saddlebrown", "grey"));
        green.put("green", matchesGreen);
        validColors.add(green);

        HashMap<String, List<String>> yellow = new HashMap<>();
        List<String> matchesYellow = new ArrayList<>(Arrays.asList("white", "black", "khaki", "yellow", "purple", "orange", "saddlebrown", "grey"));
        yellow.put("yellow", matchesYellow);
        validColors.add(yellow);

        HashMap<String, List<String>> purple = new HashMap<>();
        List<String> matchesPurple = new ArrayList<>(Arrays.asList("white", "black", "khaki", "green", "yellow", "purple", "pink", "grey"));
        purple.put("purple", matchesPurple);
        validColors.add(purple);

        HashMap<String, List<String>> pink = new HashMap<>();
        List<String> matchesPink = new ArrayList<>(Arrays.asList("white", "black", "khaki", "red", "purple", "pink", "saddlebrown", "grey"));
        pink.put("pink", matchesPink);
        validColors.add(pink);

        HashMap<String, List<String>> orange = new HashMap<>();
        List<String> matchesOrange = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "green", "yellow", "orange", "saddlebrown", "grey"));
        orange.put("orange", matchesOrange);
        validColors.add(orange);

        HashMap<String, List<String>> brown = new HashMap<>();
        List<String> matchesBrown = new ArrayList<>(Arrays.asList("white", "black", "khaki", "red", "yellow", "pink", "orange", "saddlebrown", "grey"));
        brown.put("saddlebrown", matchesBrown);
        validColors.add(brown);

        HashMap<String, List<String>> grey = new HashMap<>();
        List<String> matchesGrey = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "green", "yellow", "purple", "pink", "orange", "saddlebrown", "grey"));
        grey.put("grey", matchesGrey);
        validColors.add(grey);

        return validColors;
    }

    /**
     *
     * @param color current color which would be used in reference in what to remove when given an empty closet
     * @param validColors list of hashmaps of colors and their respective matches
     * @param getColors list of colors
     * @return would return a list of hashmaps of colors and their respective matches, however, it will remove any color
     * that doesn't match with the inputted parameter `color`
     */
    public List<HashMap<String, List<String>>> returnRandomColorMatch(String color, List<HashMap<String, List<String>>> validColors, List<String> getColors) {

        for (String c : getColors) {
            for (HashMap<String, List<String>> colorMap : validColors) {
                if (colorMap.containsKey(color)) {
                    if (!getColors.contains(c)) {
                        validColors.remove(c);
                    }
                }
            }
        }
        return validColors;
    }

    /**
     *
     * @return a simple function that returns the colors available
     */
    public List<String> getColors(){
        return new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "green", "yellow", "purple", "pink", "orange", "saddlebrown", "grey"));
    }

    /**
     * @param validColors the hashmap of colors and its respective matches
     * @param outfitColors the colors available in the closet (whether its colors that was added that best fit
     * due to no workable color scheme or the color of the individual's item and whatever it's color is
     * @return by iterating through the validColors and outfit colors present, we can weight the individual colors
     * by how many colors it can be matched with
     */
    public List<HashMap<String, Integer>> colorCheck(List<HashMap<String,List<String>>> validColors, List<String> outfitColors) {

        HashMap<String, Integer> countTracker;
        List<HashMap<String, Integer>> counterTrackerList = new ArrayList<>();
        List<List<String>> test = new ArrayList<>();
        List<Integer> trial = new ArrayList<>();
        List<List> response = new ArrayList<>();

        for (HashMap<String, List<String>> vc : validColors) {
            for (String s : outfitColors) {
                if (vc.get(s) != null) {
                    test.add(vc.get(s));
                }
            }
        }

        for (String c : outfitColors) {
            int wordTrack = 0;
            for (int i = 0; i < test.size(); i++) {
                if (test.get(i).contains(c)) {
                    wordTrack++;
                }
                countTracker = new HashMap<>();
                countTracker.put(c, wordTrack);
                counterTrackerList.add(countTracker);
                trial.add(wordTrack);
            }
        }

        Set<Integer> sortedSet = new TreeSet<>(Comparator.reverseOrder());
        sortedSet.addAll(trial);
        List<Integer> top4 = sortedSet.stream().limit(4).toList();

        response.add(top4);
        response.add(counterTrackerList);

        return counterTrackerList;

    }

    /**
     *
     * @param filteredCloset holds the clothes present within the closet
     * @return helper function that would iterate through closet, retrieve each item's color and add it to a list
     */
    public List<String> choseColors(ArrayList<ArrayList<Map<String,String>>> filteredCloset){
        List<String> availableColors = new ArrayList<>();
        String color;

        checkArray(filteredCloset);

        for (List<Map<String, String>> hashMaps : filteredCloset) {
            for (Map<String, String> hashMap : hashMaps) {
                color = hashMap.get("color");
                availableColors.add(color);
            }
        }
        return filterColors(availableColors);
    }

    /**
     *
     * @param filteredCloset holds holds the clothes present within the closet
     * @param weightedColors holds the weighted scores from the helper function
     * @return reference the color values for the individual items present in the closet. We return
     * a hashmap holding the values of each weighted and ranked color in each respective type of clothing (outer
     * wear, inner wear, etc that corresponds with the index of each item present in the closet) in one key
     * and another key just holding a list of the colors present.
     */
    public Map<String,List> rankColors(ArrayList<ArrayList<Map<String,String>>> filteredCloset, List<HashMap<String, Integer>> weightedColors) {

        List<String> orderedColorsPresentInCloset = new ArrayList<>();
        List<List<Integer>> listOfColorValuePairs = new ArrayList<>();
        List<Integer> colorValuePair;
        Map<String,List> response = new HashMap<>();

        checkArray(filteredCloset);

        for (List<Map<String, String>> clothingPiece : filteredCloset) {

            String currentColor;
            colorValuePair = new ArrayList<>();

            for (Map<String, String> piece : clothingPiece) {
                currentColor = piece.get("color");
                colorValuePair.add(returnColorScore(weightedColors, currentColor));
                orderedColorsPresentInCloset.add(currentColor);
            }
            listOfColorValuePairs.add(colorValuePair);
        }

        response.put("values", listOfColorValuePairs);
        response.put("order", filterColors(orderedColorsPresentInCloset));

        return response;
    }

    /**
     *
     * @param list takes in a list, and in this case is the list of colors that used in the function rankColors
     * @return similar to checkArray, it removes any multiple duplicates since each color would have a designated
     * rank
     */
    public List<String> filterColors(List<String> list){

        for(String color : list){
            int colorTracker = 0;
            for(String c : getColors()){
                if(c.equals(color)){
                    colorTracker++;
                }
            }
            if(colorTracker >=2){
                list.remove(color);
            }
        }
        return list;
    }

    /**
     *
     * @param weightedColors takes in the weightedColor scores
     * @param color takes an individual color which would be used to find its score
     * @return in the rankColor method, we used this helper function after removing duplicates
     * to retrieve a color's score/weight.
     */
    public Integer returnColorScore(List<HashMap<String, Integer>> weightedColors, String color){

        int value = 0;
        for (HashMap<String, Integer> rm : weightedColors) {
            if(rm.containsKey(color)){
                value = rm.get(color);
            }
        }
        return value;
    }

    /**
     *
     * @param filteredCloset clothes present in the closet
     * @param response has all the values relating to the scoring and overall weights of the color
     * @return would return the proper closet given the weight of the colors for the clothes that passed the
     *      * weather criteria.
     */
    public ArrayList<ArrayList<Map<String,String>>> returnProperCloset(ArrayList<ArrayList<Map<String,String>>> filteredCloset, Map<String,List> response){

        List<String> orderedColorsPresentInCloset = response.get("order");
        List<List<Integer>> listOfColorValuePairs = response.get("values");

        checkArray(filteredCloset);

        for (int i = 0; i < filteredCloset.size(); i++) {
            String currentColor;
            Integer currentValue;
            for (int j = 0; j < filteredCloset.get(i).size(); j++) {
                currentColor = filteredCloset.get(i).get(j).get("color");
                currentValue = listOfColorValuePairs.get(i).get(j);
                if (orderedColorsPresentInCloset.contains(currentColor)){
                    Set<Integer> sortedSet = new TreeSet<>(Comparator.reverseOrder());
                    sortedSet.addAll(listOfColorValuePairs.get(i));
                    List<Integer> example = sortedSet.stream().limit(1).toList();
                    if(!currentValue.equals(example.get(0))){
                        filteredCloset.get(i).remove(j);
                    }
                }
            }
        }
        return filteredCloset;
    }

    /**
     *
     * @param filteredCloset clothes present in the closet (last filtering done)
     * @param counterTrackerList has the score for each item and its color's value
     * @param response has all the values relating to the overall weights of the color which we use as a reference
     * @return a completely filtered closet that prioritizes clothes that are present first, then its free game for any
     * item that may not be in the closet but would be a better color matching scheme.
     */
    public ArrayList<ArrayList<Map<String,String>>> validItem(ArrayList<ArrayList<Map<String,String>>> filteredCloset, List<HashMap<String, Integer>> counterTrackerList, Map<String,List> response){

        List<String> orderedColorsPresentInCloset = response.get("order");

        checkArray(filteredCloset);

        for(Map<String,Integer> ctl : counterTrackerList){
            for (List<Map<String, String>> hashMaps : filteredCloset) {
                Boolean bigList = false;
                int randomInteger = 0;
                String inCloset;
                for (int j = 0; j < hashMaps.size(); j++) {
                    inCloset = hashMaps.get(j).get("inCloset");
                    String item = hashMaps.get(j).get("item");
                    if (ctl.get(hashMaps.get(j).get("color")) != null) {
                        if (!(ctl.containsKey(hashMaps.get(j).get("color")))) {
                            hashMaps.remove(j);
                        }
                        if (hashMaps.size() == 0) {
                            randomInteger = new Random().nextInt(orderedColorsPresentInCloset.size());
                            String color = orderedColorsPresentInCloset.get(randomInteger);
                            hashMaps.get(j).put("item", item);
                            hashMaps.get(j).put("color", color);
                            hashMaps.get(j).put("inCloset", inCloset);
                        }
                        if (hashMaps.size() > 1) {
                            bigList = true;
                            randomInteger = new Random().nextInt(hashMaps.size());
                        }
                    }
                }
                if (bigList.equals(true)) {
                    hashMaps.remove(randomInteger);
                }
            }
        }

        return filteredCloset;
    }

    /**
     *
     * @param jsonList takes the jsonlist response from the weather api (which we actually get from the UserData class)
     * and uses the public record as a means of targeting specific key values
     * @return return the values at each key values of the deserialized json
     * @throws IOException any string or file exceptions it deals with
     */
    public WeatherReport fromWeatherJson(String jsonList) throws IOException {
        return this.weatherReportJsonAdapter.fromJson(jsonList);
    }

    public record WeatherReport(Float temp, Float feelsLike, String descr, String icon, Float rain, Float snow){
    }

}
