package edu.brown.cs.student.recommender;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.closet.Closet;
import edu.brown.cs.student.server.UserData;
import spark.Request;
import spark.Response;
import spark.Route;
import java.io.*;
import java.util.*;
import java.util.Random;
import java.util.stream.Collectors;

public class RecommenderHandler implements Route {

    private final JsonAdapter<WeatherReport> weatherReportJsonAdapter;
    private final UserData data;

    public RecommenderHandler(UserData data){

        Moshi moshi = new Moshi.Builder().build();
        this.weatherReportJsonAdapter = moshi.adapter(WeatherReport.class);
        this.data = data;

    }

    @Override
    public Object handle(Request request, Response response) throws IOException {

        return new GetSuccessResponse(recommender(this.data)).serialize();

    }

    public List<HashMap<String,Object>> recommender(UserData data) throws IOException {

        ArrayList<String> validOuterTopNames = new ArrayList<>(Arrays.asList("jacket", "coat"));
        ArrayList<String> validInnerTopNames = new ArrayList<>(Arrays.asList("top", "hoodie", "shirt", "long-sleeve", "short-sleeve", "sweater", "tank"));
        ArrayList<String> validBottomNames = new ArrayList<>(Arrays.asList("jeans", "pants", "sweats", "shorts", "skirt"));
        ArrayList<String> validShoeNames = new ArrayList<>(Arrays.asList("shoes", "sneakers", "boots"));

        List<List<String>> pieces = validatePieces(data,validOuterTopNames,validInnerTopNames,validBottomNames,validShoeNames);

//        System.out.println(pieces);

        ArrayList<List<HashMap<String,String>>> presentCloset = checkCloset(data, pieces.get(0), pieces.get(1), pieces.get(2), pieces.get(3));
        List<HashMap<String, Object>> outfitList = new ArrayList<>();

        returnProperCloset(presentCloset,rankColors(presentCloset,colorCheck(getValidColors(),choseColors(presentCloset).get("colors"))));
        validItem(presentCloset,colorCheck(getValidColors(),choseColors(presentCloset).get("colors")),rankColors(presentCloset,colorCheck(getValidColors(),choseColors(presentCloset).get("colors"))));
        HashMap<String, ArrayList<HashMap<String, String>>> desiredFit = checkColor(presentCloset, getColors(data));


        if(desiredFit.get("outfit").size() == 3){

            String innerTop = desiredFit.get("outfit").get(0).get("item");
            String bottom = desiredFit.get("outfit").get(1).get("item");
            String footWear = desiredFit.get("outfit").get(2).get("item");

            String innerTopColor = desiredFit.get("outfit").get(0).get("color");
            String bottomColor = desiredFit.get("outfit").get(1).get("color");
            String footWearColor = desiredFit.get("outfit").get(2).get("color");

            String innerTopInCloset = desiredFit.get("outfit").get(0).get("inCloset");
            String bottomInCloset = desiredFit.get("outfit").get(1).get("inCloset");
            String footWearInCloset = desiredFit.get("outfit").get(2).get("inCloset");


            HashMap<String, Object> innerTopMap = new HashMap();
            innerTopMap.put("item", innerTop);
            innerTopMap.put("color", innerTopColor);
            innerTopMap.put("inCloset", innerTopInCloset);

            HashMap<String, Object> bottomMap = new HashMap();
            bottomMap.put("item", bottom);
            bottomMap.put("color", bottomColor);
            bottomMap.put("inCloset", bottomInCloset);

            HashMap<String, Object> footwearMap = new HashMap();
            footwearMap.put("item", footWear);
            footwearMap.put("color", footWearColor);
            footwearMap.put("inCloset", footWearInCloset);

            outfitList.add(innerTopMap);
            outfitList.add(bottomMap);
            outfitList.add(footwearMap);
        }

        else{

            String outerTop = desiredFit.get("outfit").get(0).get("item");
            String innerTop = desiredFit.get("outfit").get(1).get("item");
            String bottom = desiredFit.get("outfit").get(2).get("item");
            String footWear = desiredFit.get("outfit").get(3).get("item");

            String outerTopColor = desiredFit.get("outfit").get(0).get("color");
            String innerTopColor = desiredFit.get("outfit").get(1).get("color");
            String bottomColor = desiredFit.get("outfit").get(2).get("color");
            String footWearColor = desiredFit.get("outfit").get(3).get("color");

            String outerWayInCloset = desiredFit.get("outfit").get(0).get("inCloset");
            String innerTopInCloset = desiredFit.get("outfit").get(1).get("inCloset");
            String bottomInCloset = desiredFit.get("outfit").get(2).get("inCloset");
            String footWearInCloset = desiredFit.get("outfit").get(3).get("inCloset");

            HashMap<String, Object> outerTopMap = new HashMap();
            outerTopMap.put("item", outerTop);
            outerTopMap.put("color", outerTopColor);
            outerTopMap.put("inCloset", outerWayInCloset);

            HashMap<String, Object> innerTopMap = new HashMap();
            innerTopMap.put("item", innerTop);
            innerTopMap.put("color", innerTopColor);
            innerTopMap.put("inCloset", innerTopInCloset);

            HashMap<String, Object> bottomMap = new HashMap();
            bottomMap.put("item", bottom);
            bottomMap.put("color", bottomColor);
            bottomMap.put("inCloset", bottomInCloset);

            HashMap<String, Object> footwearMap = new HashMap();
            footwearMap.put("item", footWear);
            footwearMap.put("color", footWearColor);
            footwearMap.put("inCloset", footWearInCloset);

            outfitList.add(outerTopMap);
            outfitList.add(innerTopMap);
            outfitList.add(bottomMap);
            outfitList.add(footwearMap);

        }

        return outfitList;
    }

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

    public List<String> getColors(UserData data){

        List<String> colors = new ArrayList<>();
        Closet closetReport = data.getCurrentCloset();
        List<Map<String, String>> closet = closetReport.getClothesData();

        for (Map<String, String> item : closet){
            colors.add(item.get("color"));
        }
        return colors;
    }

    public List<List<String>> validatePieces(UserData data, ArrayList<String> validOuterTopNames,
         ArrayList<String> validInnerTopNames, ArrayList<String> validBottomNames, ArrayList<String> validShoeNames) throws IOException {

        String weatherReport = data.getLocationData();
        WeatherReport forecast = this.fromWeatherJson(weatherReport);

        List<List<String>> pieces = new ArrayList<>();

        if (forecast.feelsLike() >= 70) {
            if(precipitationCheck(data)){
                validOuterTopNames.remove("coat");
                validShoeNames.remove("shoes");
                validShoeNames.remove("sneakers");
            }
            else{
                validOuterTopNames.remove("jacket");
                validOuterTopNames.remove("coat");
                validInnerTopNames.remove("hoodie");
                validInnerTopNames.remove("sweater");
                validShoeNames.remove("boots");
            }
        } else if (forecast.feelsLike() >= 60 && forecast.feelsLike() < 70) {
            if(precipitationCheck(data)){
                validOuterTopNames.remove("coat");
                validShoeNames.remove("shoes");
                validShoeNames.remove("sneakers");
            }
            else{
                validOuterTopNames.remove("jacket");
                validOuterTopNames.remove("coat");
                validInnerTopNames.remove("hoodie");
                validInnerTopNames.remove("sweater");
                validShoeNames.remove("boots");
            }
        } else if (forecast.feelsLike() >= 40 && forecast.feelsLike() < 60) {
            if(precipitationCheck(data)){
                validInnerTopNames.remove("tank");
                validBottomNames.remove("shorts");
                validBottomNames.remove("skirt");
                validShoeNames.remove("shoes");
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
                validInnerTopNames.remove("tank");
                validBottomNames.remove("shorts");
                validBottomNames.remove("skirt");
                validShoeNames.remove("shoes");
                validShoeNames.remove("sneakers");
            }
            else{
                validInnerTopNames.remove("tank");
                validBottomNames.remove("shorts");
                validBottomNames.remove("skirt");
                validShoeNames.remove("boots");
            }
        } else {
            if(precipitationCheck(data)){
                validInnerTopNames.remove("tank");
                validBottomNames.remove("shorts");
                validBottomNames.remove("skirt");
                validBottomNames.remove("shoes");
                validBottomNames.remove("sneakers");
            }
            else{
                validInnerTopNames.remove("tank");
                validBottomNames.remove("shorts");
                validBottomNames.remove("skirt");
                validShoeNames.remove("shoes");
                validShoeNames.remove("sneakers");
            }
        }

        pieces.add(validOuterTopNames);
        pieces.add(validInnerTopNames);
        pieces.add(validBottomNames);
        pieces.add(validShoeNames);

        return pieces;
    }

    public ArrayList<List<HashMap<String,String>>> checkCloset(UserData data, List<String> validOuterTopNames,
        List<String> validInnerTopNames, List<String> validBottomNames, List<String> validShoeNames){

        List<List<String>> availableClothing = new ArrayList<>();
        availableClothing.add(validOuterTopNames);
        availableClothing.add(validInnerTopNames);
        availableClothing.add(validBottomNames);
        availableClothing.add(validShoeNames);

        HashMap<String,String> outerTop;
        ArrayList<HashMap<String,String>> outerTopList = new ArrayList<>();
        HashMap<String,String> innerTop;
        ArrayList<HashMap<String,String>> innerTopList = new ArrayList<>();
        HashMap<String,String> bottom;
        ArrayList<HashMap<String,String>> bottomList = new ArrayList<>();
        HashMap<String,String> footwear;
        ArrayList<HashMap<String,String>> footWearList = new ArrayList<>();
        ArrayList<List<HashMap<String,String>>> list = new ArrayList<>();

        Closet closetReport = data.getCurrentCloset();
        List<Map<String, String>> closet = closetReport.getClothesData();

        Boolean outerTracker = false;

        for (int i = 0; i < availableClothing.size(); i++){

            String outerWear = null;
            String innerWear = null;
            String bottomWear = null;
            String footWear = null;

            if (availableClothing.get(0).size() == 0){
                outerTracker = true;
                outerTop = new HashMap<>();
                outerTop.put("item", "outerTop");
                outerTop.put("color", "brown");
                outerTop.put("inCloset","false");
                outerTopList.add(outerTop);
            }

            for (Map<String, String> item : closet){

                if (validOuterTopNames.contains(item.get("item"))){
                    outerTop = new HashMap<>();
                    outerTop.put("item", item.get("item"));
                    outerTop.put("color", item.get("color"));
                    outerTop.put("inCloset","true");
                    outerTopList.add(outerTop);
                }
                else if (validInnerTopNames.contains(item.get("item"))){
                    innerTop = new HashMap<>();
                    innerTop.put("item", item.get("item"));
                    innerTop.put("color", item.get("color"));
                    innerTop.put("inCloset","true");
                    innerTopList.add(innerTop);
                }
                else if (validBottomNames.contains(item.get("item"))){
                    bottom = new HashMap<>();
                    bottom.put("item", item.get("item"));
                    bottom.put("color", item.get("color"));
                    bottom.put("inCloset","true");
                    bottomList.add(bottom);
                }
                else if (validShoeNames.contains(item.get("item"))){
                    footwear = new HashMap<>();
                    footwear.put("item", item.get("item"));
                    footwear.put("color", item.get("color"));
                    footwear.put("inCloset","true");
                    footWearList.add(footwear);
                }
            }

            if (i == 0 && availableClothing.get(0).size() != 0){
                outerTop = new HashMap<>();
                outerTop.put("item", validOuterTopNames.get(new Random().nextInt(validOuterTopNames.size())));
                outerTop.put("color", "white");
                outerTop.put("inCloset","false");
                outerTopList.add(outerTop);
            }
            else if(i == 1){
                innerTop = new HashMap<>();
                innerTop.put("item", validInnerTopNames.get(new Random().nextInt(validInnerTopNames.size())));
                innerTop.put("color", "white");
                innerTop.put("inCloset","false");
                innerTopList.add(innerTop);
            }
            else if(i == 2){
                bottom = new HashMap<>();
                bottom.put("item", validBottomNames.get(new Random().nextInt(validBottomNames.size())));
                bottom.put("color", "black");
                bottom.put("inCloset","false");
                bottomList.add(bottom);
            }
            else if (i == 3){
                footwear = new HashMap<>();
                footwear.put("item", validShoeNames.get(new Random().nextInt(validShoeNames.size())));
                footwear.put("color", "white");
                footwear.put("inCloset","false");
                footWearList.add(footwear);
            }
        }



        if(outerTracker){
            list.add(innerTopList);
            list.add(bottomList);
            list.add(footWearList);
        }
        else{
            list.add(outerTopList);
            list.add(innerTopList);
            list.add(bottomList);
            list.add(footWearList);
        }

        return list;
    }

    public HashMap<String,ArrayList<HashMap<String,String>>> checkColor(ArrayList<List<HashMap<String,String>>> filteredCloset, List<String> outfitColors){

        HashMap<String,String> outerTop;

        HashMap<String,String> innerTop;

        HashMap<String,String> bottom;

        HashMap<String,String> footwear;

        ArrayList<HashMap<String,String>> list = new ArrayList<>();

        HashMap<String,ArrayList<HashMap<String,String>>> outfit = new HashMap<>();

            for (int i = 0; i < filteredCloset.size(); i++) {
                for (int j = 0; j < filteredCloset.get(i).size(); j++) {

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
            outfit.put("outfit", list);

        return outfit;
    }

    public List<HashMap<String,List<String>>> getValidColors(){

        List<HashMap<String, List<String>>> validColors = new ArrayList<>();

        HashMap<String, List<String>> white = new HashMap<>();
        List<String> matchesWhite = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "green", "yellow", "purple", "pink", "orange", "brown", "grey"));
        white.put("white", matchesWhite);
        validColors.add(white);

        HashMap<String, List<String>> black = new HashMap<>();
        List<String> matchesBlack = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "green", "yellow", "purple", "pink", "orange", "brown", "grey"));
        black.put("black", matchesBlack);
        validColors.add(black);

        HashMap<String, List<String>> blue = new HashMap<>();
        List<String> matchesBlue = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "green", "yellow", "orange", "brown", "grey"));
        blue.put("blue", matchesBlue);
        validColors.add(blue);

        HashMap<String, List<String>> khaki = new HashMap<>();
        List<String> matchesKhaki = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "green", "yellow", "purple", "pink", "orange", "brown", "grey"));
        khaki.put("khaki", matchesKhaki);
        validColors.add(khaki);

        HashMap<String, List<String>> red = new HashMap<>();
        List<String> matchesRed = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "yellow", "purple", "pink", "orange", "brown", "grey"));
        red.put("red", matchesRed);
        validColors.add(red);

        HashMap<String, List<String>> green = new HashMap<>();
        List<String> matchesGreen = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "green", "purple", "orange", "brown", "grey"));
        green.put("green", matchesGreen);
        validColors.add(green);

        HashMap<String, List<String>> yellow = new HashMap<>();
        List<String> matchesYellow = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "yellow", "orange", "brown", "grey"));
        yellow.put("yellow", matchesYellow);
        validColors.add(yellow);

        HashMap<String, List<String>> purple = new HashMap<>();
        List<String> matchesPurple = new ArrayList<>(Arrays.asList("white", "black", "khaki", "red", "green", "purple", "pink", "grey"));
        purple.put("purple", matchesPurple);
        validColors.add(purple);

        HashMap<String, List<String>> pink = new HashMap<>();
        List<String> matchesPink = new ArrayList<>(Arrays.asList("white", "black", "khaki", "red", "purple", "pink", "brown", "grey"));
        pink.put("pink", matchesPink);
        validColors.add(pink);

        HashMap<String, List<String>> orange = new HashMap<>();
        List<String> matchesOrange = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "green", "orange", "brown", "grey", "yellow"));
        orange.put("orange", matchesOrange);
        validColors.add(orange);

        HashMap<String, List<String>> brown = new HashMap<>();
        List<String> matchesBrown = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "yellow", "pink", "orange", "brown", "grey"));
        brown.put("brown", matchesBrown);
        validColors.add(brown);

        HashMap<String, List<String>> grey = new HashMap<>();
        List<String> matchesGrey = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "green", "yellow", "purple", "pink", "orange", "brown", "grey"));
        grey.put("grey", matchesGrey);
        validColors.add(grey);

        return validColors;
    }

    public List<HashMap<String, Integer>> colorCheck(List<HashMap<String,List<String>>> validColors, List<String> outfit) {


        HashMap<String, Integer> countTracker;
        List<HashMap<String, Integer>> counterTrackerList = new ArrayList<>();
        List<List<String>> test = new ArrayList<>();
        List<Integer> trial = new ArrayList<>();
        List<List> response = new ArrayList<>();

        for (HashMap<String, List<String>> vc : validColors) {
            for (int i = 0; i < outfit.size(); i++) {
                if (vc.get(outfit.get(i)) != null) {
                    test.add(vc.get(outfit.get(i)));
                }
            }
        }

        for (String c : outfit){
            int wordTrack = 0;
            for (int i = 0; i < test.size(); i ++){
                if (test.get(i).contains(c)){
                    wordTrack++;
                }
            }
            countTracker = new HashMap<>();
            countTracker.put(c,wordTrack);
            counterTrackerList.add(countTracker);
            trial.add(wordTrack);
        }

        Set<Integer> sortedSet = new TreeSet<>(Comparator.reverseOrder());
        sortedSet.addAll(trial);

        response.add(trial);
        response.add(counterTrackerList);

        return counterTrackerList;

    }

    public HashMap<String, List> choseColors(ArrayList<List<HashMap<String,String>>> filteredCloset){
        List<String> availableColors = new ArrayList<>();
        HashMap<String,List> availableColorsMap = new HashMap<>();
        String color = null;
        for (int i = 0; i < filteredCloset.size(); i ++){
            for (int j = 0; j < filteredCloset.get(i).size(); j ++){
                color = filteredCloset.get(i).get(j).get("color");
                availableColors.add(color);
            }
            availableColorsMap = new HashMap<>();
            availableColorsMap.put("colors", availableColors);
        }
        return availableColorsMap;
    }

    public HashMap<String,List> rankColors(ArrayList<List<HashMap<String,String>>> filteredCloset, List<HashMap<String, Integer>> rankMap) {

        List<String> orderedColorsPresentInCloset = new ArrayList<>();
        List<List<Integer>> listOfColorValuePairs = new ArrayList<>();
        List<Integer> colorValuePair;
        HashMap<String,List> response = new HashMap<>();

        for (int i = 0; i < filteredCloset.size(); i++) {

            String currentColor = null;
            colorValuePair = new ArrayList<>();

            for (int j = 0; j < filteredCloset.get(i).size(); j++) {
                currentColor = filteredCloset.get(i).get(j).get("color");
                colorValuePair.add(returnColorScore(rankMap,currentColor));
                orderedColorsPresentInCloset.add(currentColor);
            }
            listOfColorValuePairs.add(colorValuePair);
        }

        response.put("values", listOfColorValuePairs);
        response.put("order", orderedColorsPresentInCloset);

        return response;
    }

    public Integer returnColorScore(List<HashMap<String, Integer>> rankMap, String color){

        int value = 0;
        for (HashMap<String, Integer> rm : rankMap) {
            if(rm.containsKey(color)){
                value = rm.get(color);
            }
        }
        return value;
    }

    public ArrayList<List<HashMap<String,String>>> returnProperCloset(ArrayList<List<HashMap<String,String>>> filteredCloset, HashMap<String,List> response){

        List<String> orderedColorsPresentInCloset = response.get("order");
        List<List<Integer>> listOfColorValuePairs = response.get("values");

        for (int i = 0; i < filteredCloset.size(); i++) {
            String currentColor;
            Integer currentValue = 0;
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

    public ArrayList<List<HashMap<String,String>>> validItem(ArrayList<List<HashMap<String,String>>> filteredCloset, List<HashMap<String, Integer>> counterTrackerList, HashMap<String,List> response){

        List<String> orderedColorsPresentInCloset = response.get("order");

        for(HashMap<String,Integer> ctl : counterTrackerList){
            for (int i = 0; i < filteredCloset.size(); i++) {
                Boolean bigList = false;
                int randomInteger = 0;
                String inCloset = null;
                for (int j = 0; j < filteredCloset.get(i).size(); j++) {
                    inCloset = filteredCloset.get(i).get(j).get("inCloset");
                    String item = filteredCloset.get(i).get(j).get("item");
                    if(ctl.get(filteredCloset.get(i).get(j).get("color")) != null) {
                        if (!(ctl.containsKey(filteredCloset.get(i).get(j).get("color")))) {
                            filteredCloset.get(i).remove(j);
                        }
                        if (filteredCloset.get(i).size() == 0) {
                            randomInteger = new Random().nextInt(orderedColorsPresentInCloset.size());
                            String color = orderedColorsPresentInCloset.get(randomInteger);
                            filteredCloset.get(i).get(j).put("item", item);
                            filteredCloset.get(i).get(j).put("color", color);
                            filteredCloset.get(i).get(j).put("inCloset",inCloset);
                        }
                        if (filteredCloset.get(i).size() > 1) {
                            bigList = true;
                            randomInteger = new Random().nextInt(filteredCloset.get(i).size());
                        }
                    }
                }
                if (bigList.equals(true)) {
                    filteredCloset.get(i).remove(randomInteger);
                }
            }
        }

        return filteredCloset;
    }

    public record GetSuccessResponse(List<HashMap<String,Object>> data) {
        /**
         * @return this response, serialized as Json
         */
        String serialize() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "success");
            result.put("outfit", data);
            Moshi moshi = new Moshi.Builder().build();
            return moshi.adapter(Map.class).toJson(result);
        }
    }

    public WeatherReport fromWeatherJson(String jsonList) throws IOException {
        return this.weatherReportJsonAdapter.fromJson(jsonList);
    }

    public record WeatherReport(Float temp, Float feelsLike, String descr, String icon, Float rain, Float snow){
    }

}