package edu.brown.cs.student.handlers;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.closet.Closet;
import edu.brown.cs.student.server.UserData;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import java.io.*;
import java.util.*;
import java.util.Random;

public class RecommenderHandler implements Route {

    private final JsonAdapter<WeatherReport> weatherReportJsonAdapter;
    private final UserData data;

    public RecommenderHandler(UserData data){

        Moshi moshi = new Moshi.Builder().build();
        this.weatherReportJsonAdapter = moshi.adapter(WeatherReport.class);
        this.data = data;

    }

    @Override
    public Object handle(Request request, Response response) {

        try {
            return new GetSuccessResponse(recommender(this.data)).serialize();
        }
        catch (Exception e){
            e.printStackTrace();
            return new GetFailureResponse().serialize();
        }

    }

    public List<HashMap<String,String>> recommender(UserData data) throws IOException {

        ArrayList<String> validOuterTopNames = new ArrayList<>(Arrays.asList("jacket", "coat", "hoodie"));
        ArrayList<String> validInnerTopNames = new ArrayList<>(Arrays.asList("long-sleeve", "short-sleeve", "sweatshirt", "tank"));
        ArrayList<String> validBottomNames = new ArrayList<>(Arrays.asList("pants", "shorts", "skirt"));
        ArrayList<String> validShoeNames = new ArrayList<>(Arrays.asList("sneakers", "boots"));

        List<List<String>> pieces = validatePieces(data,validOuterTopNames,validInnerTopNames,validBottomNames,validShoeNames);
        ArrayList<List<HashMap<String,String>>> presentCloset = checkCloset(data, pieces.get(0), pieces.get(1), pieces.get(2), pieces.get(3));

        List<String> outfit = choseColors(presentCloset);
        List<HashMap<String, Integer>> checkedColor = colorCheck(getValidColors(),outfit);
        HashMap<String, List> colorsRanked = rankColors(presentCloset,checkedColor);
        returnProperCloset(presentCloset,colorsRanked);
        validItem(presentCloset,checkedColor,colorsRanked);
        HashMap<String, ArrayList<HashMap<String, String>>> desiredFit = checkColor(presentCloset);

        return desiredFit.get("outfit");
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

    public ArrayList<List<HashMap<String,String>>> checkCloset(UserData data, List<String> validOuterTopNames,
        List<String> validInnerTopNames, List<String> validBottomNames, List<String> validShoeNames){

        List<List<String>> availableClothing = new ArrayList<>();
        availableClothing.add(validOuterTopNames);
        availableClothing.add(validInnerTopNames);
        availableClothing.add(validBottomNames);
        availableClothing.add(validShoeNames);

        HashMap<String,String> outerTop;
        Map<String,String> outerTop2 = new HashMap<>();
        ArrayList<HashMap<String,String>> outerTopList = new ArrayList<>();
        HashMap<String,String> innerTop;
        Map<String,String> innerTop2 = new HashMap<>();
        ArrayList<HashMap<String,String>> innerTopList = new ArrayList<>();
        HashMap<String,String> bottom;
        Map<String,String> bottom2 = new HashMap<>();
        ArrayList<HashMap<String,String>> bottomList = new ArrayList<>();
        HashMap<String,String> footwear;
        Map<String,String> footwear2 = new HashMap<>();
        ArrayList<HashMap<String,String>> footWearList = new ArrayList<>();
        ArrayList<List<HashMap<String,String>>> list = new ArrayList<>();

        Closet closetReport = data.getCurrentCloset();
        List<Map<String, String>> closet = closetReport.getClothesData();

        if (closet.isEmpty()){

            if(validOuterTopNames.size() != 0) {
                outerTop2.put("item", validOuterTopNames.get(new Random().nextInt(validOuterTopNames.size())));
                outerTop2.put("color", getColors().get(new Random().nextInt(getColors().size())));
                outerTop2.put("inCloset", "false");
            }

            innerTop2.put("item", validInnerTopNames.get(new Random().nextInt(validInnerTopNames.size())));
            innerTop2.put("color", getColors().get(new Random().nextInt(getColors().size())));
            innerTop2.put("inCloset", "false");

            bottom2.put("item", validBottomNames.get(new Random().nextInt(validBottomNames.size())));
            bottom2.put("color", getColors().get(new Random().nextInt(getColors().size())));
            bottom2.put("inCloset", "false");

            footwear2.put("item", validShoeNames.get(new Random().nextInt(validShoeNames.size())));
            footwear2.put("color", getColors().get(new Random().nextInt(getColors().size())));
            footwear2.put("inCloset", "false");

            closet.add(outerTop2);
            closet.add(innerTop2);
            closet.add(bottom2);
            closet.add(footwear2);

        }

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
                    if(footwear.get("item").equals("sneakers")){
                        footwear.put("color", "white");
                    }
                    else{
                        footwear.put("color", "brown");
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
        return list;
    }

    public void checkList(ArrayList<HashMap<String, String>> pieceList){

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

    public ArrayList<List<HashMap<String, String>>> checkArray(ArrayList<List<HashMap<String, String>>> list){

        for (int i = 0; i < list.size(); i++) {
            List<HashMap<String, String>> currentIndex = list.get(i);

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

    public HashMap<String,ArrayList<HashMap<String,String>>> checkColor(ArrayList<List<HashMap<String,String>>> filteredCloset){

        HashMap<String,String> outerTop;

        HashMap<String,String> innerTop;

        HashMap<String,String> bottom;

        HashMap<String,String> footwear;

        HashMap<String,ArrayList<HashMap<String,String>>> outfit = new HashMap<>();

        ArrayList<HashMap<String,String>> list = new ArrayList<>();

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

    public void getFinalPiece(List<HashMap<String, String>> list){

        for(int i = 0; i < list.size(); i++){

            Boolean inCloset = false;

            for(HashMap<String, String> piece : list) {
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
        List<String> matchesBlue = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "green", "orange", "grey"));
        blue.put("blue", matchesBlue);
        validColors.add(blue);

        HashMap<String, List<String>> khaki = new HashMap<>();
        List<String> matchesKhaki = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "green", "yellow", "purple", "pink", "orange", "brown", "grey"));
        khaki.put("khaki", matchesKhaki);
        validColors.add(khaki);

        HashMap<String, List<String>> red = new HashMap<>();
        List<String> matchesRed = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "pink", "orange", "brown", "grey"));
        red.put("red", matchesRed);
        validColors.add(red);

        HashMap<String, List<String>> green = new HashMap<>();
        List<String> matchesGreen = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "green", "purple", "orange", "brown", "grey"));
        green.put("green", matchesGreen);
        validColors.add(green);

        HashMap<String, List<String>> yellow = new HashMap<>();
        List<String> matchesYellow = new ArrayList<>(Arrays.asList("white", "black", "khaki", "yellow", "purple", "orange", "brown", "grey"));
        yellow.put("yellow", matchesYellow);
        validColors.add(yellow);

        HashMap<String, List<String>> purple = new HashMap<>();
        List<String> matchesPurple = new ArrayList<>(Arrays.asList("white", "black", "khaki", "green", "yellow", "purple", "pink", "grey"));
        purple.put("purple", matchesPurple);
        validColors.add(purple);

        HashMap<String, List<String>> pink = new HashMap<>();
        List<String> matchesPink = new ArrayList<>(Arrays.asList("white", "black", "khaki", "red", "purple", "pink", "brown", "grey"));
        pink.put("pink", matchesPink);
        validColors.add(pink);

        HashMap<String, List<String>> orange = new HashMap<>();
        List<String> matchesOrange = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "green", "yellow", "orange", "brown", "grey"));
        orange.put("orange", matchesOrange);
        validColors.add(orange);

        HashMap<String, List<String>> brown = new HashMap<>();
        List<String> matchesBrown = new ArrayList<>(Arrays.asList("white", "black", "khaki", "red", "yellow", "pink", "orange", "brown", "grey"));
        brown.put("brown", matchesBrown);
        validColors.add(brown);

        HashMap<String, List<String>> grey = new HashMap<>();
        List<String> matchesGrey = new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "green", "yellow", "purple", "pink", "orange", "brown", "grey"));
        grey.put("grey", matchesGrey);
        validColors.add(grey);

        return validColors;
    }

    public List<String> getColors(){
        return new ArrayList<>(Arrays.asList("white", "black", "blue", "khaki", "red", "green", "yellow", "purple", "pink", "orange", "brown", "grey"));
    }

    public List<HashMap<String, Integer>> colorCheck(List<HashMap<String,List<String>>> validColors, List<String> outfit) {

        HashMap<String, Integer> countTracker;
        List<HashMap<String, Integer>> counterTrackerList = new ArrayList<>();
        List<List<String>> test = new ArrayList<>();
        List<Integer> trial = new ArrayList<>();
        List<List> response = new ArrayList<>();

        for (HashMap<String, List<String>> vc : validColors) {
            for (String s : outfit) {
                if (vc.get(s) != null) {
                    test.add(vc.get(s));
                }
            }
        }

        for (String c : outfit) {
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

    public List<String> choseColors(ArrayList<List<HashMap<String,String>>> filteredCloset){
        List<String> availableColors = new ArrayList<>();
        String color;

        checkArray(filteredCloset);

        for (List<HashMap<String, String>> hashMaps : filteredCloset) {
            for (HashMap<String, String> hashMap : hashMaps) {
                color = hashMap.get("color");
                availableColors.add(color);
            }
        }
        return filterColors(availableColors);
    }

    public HashMap<String,List> rankColors(ArrayList<List<HashMap<String,String>>> filteredCloset, List<HashMap<String, Integer>> rankMap) {

        List<String> orderedColorsPresentInCloset = new ArrayList<>();
        List<List<Integer>> listOfColorValuePairs = new ArrayList<>();
        List<Integer> colorValuePair;
        HashMap<String,List> response = new HashMap<>();

        checkArray(filteredCloset);

        for (List<HashMap<String, String>> clothingPiece : filteredCloset) {

            String currentColor;
            colorValuePair = new ArrayList<>();

            for (HashMap<String, String> piece : clothingPiece) {
                currentColor = piece.get("color");
                colorValuePair.add(returnColorScore(rankMap, currentColor));
                orderedColorsPresentInCloset.add(currentColor);
            }
            listOfColorValuePairs.add(colorValuePair);
        }

        response.put("values", listOfColorValuePairs);
        response.put("order", filterColors(orderedColorsPresentInCloset));

        return response;
    }

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

        checkArray(filteredCloset);

        for (int i = 0; i < filteredCloset.size(); i++) {
            String currentColor;
            String inCloset;
            Integer currentValue;
            for (int j = 0; j < filteredCloset.get(i).size(); j++) {
                currentColor = filteredCloset.get(i).get(j).get("color");
                inCloset = filteredCloset.get(i).get(j).get("inCloset");
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

        checkArray(filteredCloset);

        for(HashMap<String,Integer> ctl : counterTrackerList){
            for (List<HashMap<String, String>> hashMaps : filteredCloset) {
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

    public WeatherReport fromWeatherJson(String jsonList) throws IOException {
        return this.weatherReportJsonAdapter.fromJson(jsonList);
    }

    public record WeatherReport(Float temp, Float feelsLike, String descr, String icon, Float rain, Float snow){
    }

    public record GetSuccessResponse(List<HashMap<String,String>> data) {
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

    public record GetFailureResponse() {
        /**
         * @return this response, serialized as Json
         */
        String serialize() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("result", "bad_json_error");
            result.put("further description", "load weather");
            Moshi moshi = new Moshi.Builder().build();
            return moshi.adapter(Map.class).toJson(result);
        }
    }

}