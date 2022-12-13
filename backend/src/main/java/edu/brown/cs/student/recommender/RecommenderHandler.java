package edu.brown.cs.student.recommender;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs.student.closet.Closet;
import edu.brown.cs.student.server.UserData;
import spark.Request;
import spark.Response;
import spark.Route;
import java.io.IOException;
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
    public Object handle(Request request, Response response) throws IOException {
        return new GetSuccessResponse(recommender(this.data)).serialize();
    }

    public List<HashMap<String,Object>> recommender(UserData data) throws IOException {

        ArrayList<String> validOuterTopNames = new ArrayList<>(Arrays.asList("jacket", "coat"));
        ArrayList<String> validInnerTopNames = new ArrayList<>(Arrays.asList("top", "hoodie", "shirt", "long-sleeve", "short-sleeve", "sweater", "tank"));
        ArrayList<String> validBottomNames = new ArrayList<>(Arrays.asList("jeans", "pants", "sweats", "shorts", "skirt"));
        ArrayList<String> validShoeNames = new ArrayList<>(Arrays.asList("shoes", "sneakers", "boots"));


        List<String> colors = new ArrayList<>();
        colors.add("blue");
        colors.add("green");
        colors.add("black");
        colors.add("yellow");

        HashMap<String, ArrayList<HashMap<String, String>>> desiredFit =
            checkColor(checkCloset(data,validOuterTopNames,validInnerTopNames,validBottomNames,validShoeNames),colors);

        String outerTop = desiredFit.get("outfit").get(0).get("item");
        String innerTop = desiredFit.get("outfit").get(1).get("item");
        String bottom = desiredFit.get("outfit").get(2).get("item");
        String footWear = desiredFit.get("outfit").get(3).get("item");

        String outerTopColor = desiredFit.get("outfit").get(0).get("color");
        String innerTopColor = desiredFit.get("outfit").get(1).get("color");
        String bottomColor = desiredFit.get("outfit").get(2).get("color");
        String footWearColor = desiredFit.get("outfit").get(3).get("color");

        String weatherReport = data.getLocationData();
        WeatherReport forecast = this.fromWeatherJson(weatherReport);
        List<HashMap<String,Object>> outfitList = precipitationCheck(forecast, validBottomNames,validInnerTopNames, validOuterTopNames, validShoeNames);

        if (forecast.feelsLike() >= 70){
            if (outfitList.isEmpty()){

                HashMap<String,Object> innerTopMap = new HashMap();
//                innerTopMap.put("item", validInnerTopNames.get(new Random().nextInt(validInnerTopNames.size())));
                innerTopMap.put("item", innerTop);
//                innerTopMap.put("color", null);
                innerTopMap.put("color", innerTopColor);

                HashMap<String,Object> bottomMap = new HashMap();
//                bottomMap.put("item", validBottomNames.get(new Random().nextInt(validBottomNames.size())));
                bottomMap.put("item", bottom);
//                bottomMap.put("color", null);
                bottomMap.put("color", bottomColor);

                HashMap<String,Object> footwearMap = new HashMap();
                validShoeNames.remove("boots");
//                footwearMap.put("item", validShoeNames.get(new Random().nextInt(validShoeNames.size())));
                footwearMap.put("item", footWear);
//                footwearMap.put("color", null);
                footwearMap.put("color", footWearColor);

                outfitList.add(innerTopMap);
                outfitList.add(bottomMap);
                outfitList.add(footwearMap);

            }
        }
        else if (forecast.feelsLike() >= 60 && forecast.feelsLike() < 70){
            if (outfitList.isEmpty()){

                HashMap<String,Object> outerTopMap = new HashMap();
//                innerTopMap.put("item", validInnerTopNames.get(new Random().nextInt(validInnerTopNames.size())));
                outerTopMap.put("item", outerTop);
//                innerTopMap.put("color", null);
                outerTopMap.put("color", outerTopColor);

                HashMap<String,Object> innerTopMap = new HashMap();
//                innerTopMap.put("item", validInnerTopNames.get(new Random().nextInt(validInnerTopNames.size())));
                innerTopMap.put("item", innerTop);
//                innerTopMap.put("color", null);
                innerTopMap.put("color", innerTopColor);

                HashMap<String,Object> bottomMap = new HashMap();
//                bottomMap.put("item", validBottomNames.get(new Random().nextInt(validBottomNames.size())));
                bottomMap.put("item", bottom);
//                bottomMap.put("color", null);
                bottomMap.put("color", bottomColor);

                HashMap<String,Object> footwearMap = new HashMap();
                validShoeNames.remove("boots");
//                footwearMap.put("item", validShoeNames.get(new Random().nextInt(validShoeNames.size())));
                footwearMap.put("item", footWear);
//                footwearMap.put("color", null);
                footwearMap.put("color", footWearColor);

                outfitList.add(outerTopMap);
                outfitList.add(innerTopMap);
                outfitList.add(bottomMap);
                outfitList.add(footwearMap);

            }
        }
        else if (forecast.feelsLike() >= 40 && forecast.feelsLike() < 60){
            if (outfitList.isEmpty()){

                HashMap<String,Object> outerTopMap = new HashMap();
//                innerTopMap.put("item", validInnerTopNames.get(new Random().nextInt(validInnerTopNames.size())));
                outerTopMap.put("item", outerTop);
//                innerTopMap.put("color", null);
                outerTopMap.put("color", outerTopColor);

                HashMap<String,Object> innerTopMap = new HashMap();
//                innerTopMap.put("item", validInnerTopNames.get(new Random().nextInt(validInnerTopNames.size())));
                innerTopMap.put("item", innerTop);
//                innerTopMap.put("color", null);
                innerTopMap.put("color", innerTopColor);

                HashMap<String,Object> bottomMap = new HashMap();
//                bottomMap.put("item", validBottomNames.get(new Random().nextInt(validBottomNames.size())));
                bottomMap.put("item", bottom);
//                bottomMap.put("color", null);
                bottomMap.put("color", bottomColor);

                HashMap<String,Object> footwearMap = new HashMap();
                validShoeNames.remove("boots");
//                footwearMap.put("item", validShoeNames.get(new Random().nextInt(validShoeNames.size())));
                footwearMap.put("item", footWear);
//                footwearMap.put("color", null);
                footwearMap.put("color", footWearColor);

                outfitList.add(outerTopMap);
                outfitList.add(innerTopMap);
                outfitList.add(bottomMap);
                outfitList.add(footwearMap);
            }
        }
        else if (forecast.feelsLike() >= 33 && forecast.feelsLike() < 40){
            if (outfitList.isEmpty()){

                HashMap<String,Object> outerTopMap = new HashMap();
//                innerTopMap.put("item", validInnerTopNames.get(new Random().nextInt(validInnerTopNames.size())));
                outerTopMap.put("item", outerTop);
//                innerTopMap.put("color", null);
                outerTopMap.put("color", outerTopColor);

                HashMap<String,Object> innerTopMap = new HashMap();
//                innerTopMap.put("item", validInnerTopNames.get(new Random().nextInt(validInnerTopNames.size())));
                innerTopMap.put("item", innerTop);
//                innerTopMap.put("color", null);
                innerTopMap.put("color", innerTopColor);

                HashMap<String,Object> bottomMap = new HashMap();
//                bottomMap.put("item", validBottomNames.get(new Random().nextInt(validBottomNames.size())));
                bottomMap.put("item", bottom);
//                bottomMap.put("color", null);
                bottomMap.put("color", bottomColor);

                HashMap<String,Object> footwearMap = new HashMap();
                validShoeNames.remove("boots");
//                footwearMap.put("item", validShoeNames.get(new Random().nextInt(validShoeNames.size())));
                footwearMap.put("item", footWear);
//                footwearMap.put("color", null);
                footwearMap.put("color", footWearColor);

                outfitList.add(outerTopMap);
                outfitList.add(innerTopMap);
                outfitList.add(bottomMap);
                outfitList.add(footwearMap);

            }
        }
        else{

            HashMap<String,Object> outerTopMap = new HashMap();
//                innerTopMap.put("item", validInnerTopNames.get(new Random().nextInt(validInnerTopNames.size())));
            outerTopMap.put("item", outerTop);
//                innerTopMap.put("color", null);
            outerTopMap.put("color", outerTopColor);

            HashMap<String,Object> innerTopMap = new HashMap();
//                innerTopMap.put("item", validInnerTopNames.get(new Random().nextInt(validInnerTopNames.size())));
            innerTopMap.put("item", innerTop);
//                innerTopMap.put("color", null);
            innerTopMap.put("color", innerTopColor);

            HashMap<String,Object> bottomMap = new HashMap();
//                bottomMap.put("item", validBottomNames.get(new Random().nextInt(validBottomNames.size())));
            bottomMap.put("item", bottom);
//                bottomMap.put("color", null);
            bottomMap.put("color", bottomColor);

            HashMap<String,Object> footwearMap = new HashMap();
            validShoeNames.remove("boots");
//                footwearMap.put("item", validShoeNames.get(new Random().nextInt(validShoeNames.size())));
            footwearMap.put("item", footWear);
//                footwearMap.put("color", null);
            footwearMap.put("color", footWearColor);

            outfitList.add(outerTopMap);
            outfitList.add(innerTopMap);
            outfitList.add(bottomMap);
            outfitList.add(footwearMap);

        }
        return outfitList;
    }

    private List<HashMap<String,Object>> precipitationCheck(WeatherReport forecast, ArrayList<String> validBottomNames,
        ArrayList<String> validInnerTopNames, ArrayList<String> validOuterTopNames, ArrayList<String> validShoeNames) {

        List<HashMap<String,Object>> itemList = new ArrayList<>();

        //change to forecast.rain() >= 4
        if (forecast.rain() >= 0.1 || forecast.snow() > 0) {
            if(forecast.feelsLike > 40){

                HashMap<String,Object> outerTop = new HashMap();
                outerTop.put("item", validOuterTopNames.get(new Random().nextInt(validOuterTopNames.size())));
                outerTop.put("color", "null");

                HashMap<String,Object> innerTop = new HashMap();
                innerTop.put("item", validInnerTopNames.get(new Random().nextInt(validInnerTopNames.size())));
                innerTop.put("color", "null");

                HashMap<String,Object> bottom = new HashMap();
                validBottomNames.remove("shorts");
                validBottomNames.remove("skirt");
                bottom.put("item", validBottomNames.get(new Random().nextInt(validBottomNames.size())));
                bottom.put("color", "null");

                HashMap<String,Object> footwear = new HashMap();
                footwear.put("item", validShoeNames.get(new Random().nextInt(validShoeNames.size())));
                footwear.put("color", "null");

                itemList.add(outerTop);
                itemList.add(innerTop);
                itemList.add(bottom);
                itemList.add(footwear);

            }

            else{

                HashMap<String,Object> outerTop = new HashMap();
                validOuterTopNames.remove("jacket");
                outerTop.put("item", validOuterTopNames.get(new Random().nextInt(validOuterTopNames.size())));
                outerTop.put("color", "null");

                HashMap<String,Object> innerTop = new HashMap();
                validInnerTopNames.remove("tank");
                innerTop.put("item", validInnerTopNames.get(new Random().nextInt(validInnerTopNames.size())));
                innerTop.put("color", "null");

                HashMap<String,Object> bottom = new HashMap();
                bottom.put("item", validBottomNames.get(new Random().nextInt(validBottomNames.size())));
                bottom.put("color", "null");

                HashMap<String,Object> footwear = new HashMap();
                footwear.put("item", validShoeNames.get(new Random().nextInt(validShoeNames.size())));
                footwear.put("color", "null");

                itemList.add(outerTop);
                itemList.add(innerTop);
                itemList.add(bottom);
                itemList.add(footwear);
            }
        }

        return itemList;
    }


    public ArrayList<List<HashMap<String,String>>> checkCloset(UserData data, ArrayList<String> validOuterTopNames,
        ArrayList<String> validInnerTopNames, ArrayList<String> validBottomNames, ArrayList<String> validShoeNames){

        List<ArrayList<String>> availableClothing = new ArrayList<>();
        availableClothing.add(validOuterTopNames);
        availableClothing.add(validInnerTopNames);
        availableClothing.add(validBottomNames);
        availableClothing.add(validShoeNames);

        Closet closetReport = data.getCurrentCloset();
        List<Map<String, String>> closet = closetReport.getClothesData();

        HashMap<String,String> outerTop;
        ArrayList<HashMap<String,String>> outerTopList = new ArrayList<>();

        HashMap<String,String> innerTop;
        ArrayList<HashMap<String,String>> innerTopList = new ArrayList<>();

        HashMap<String,String> bottom;
        ArrayList<HashMap<String,String>> bottomList = new ArrayList<>();

        HashMap<String,String> footwear;
        ArrayList<HashMap<String,String>> footWearList = new ArrayList<>();

        ArrayList<List<HashMap<String,String>>> list = new ArrayList<>();

        for (Map<String, String> item : closet){
            for (int i = 0; i < availableClothing.size(); i++){
                if (i == 0 && availableClothing.get(0).contains(item.get("item"))){
                    outerTop = new HashMap<>();
                    outerTop.put("item", item.get("item"));
                    outerTop.put("color", item.get("color"));
                    outerTopList.add(outerTop);

                }
                else if (i == 1 && availableClothing.get(1).contains(item.get("item"))){
                    innerTop = new HashMap<>();
                    innerTop.put("item", item.get("item"));
                    innerTop.put("color", item.get("color"));
                    innerTopList.add(innerTop);
                }
                else if (i == 2 && availableClothing.get(2).contains(item.get("item"))){
                    bottom = new HashMap<>();
                    bottom.put("item", item.get("item"));
                    bottom.put("color", item.get("color"));
                    bottomList.add(bottom);
                }
                else if (i == 3 && availableClothing.get(3).contains(item.get("item"))){
                    footwear = new HashMap<>();
                    footwear.put("item", item.get("item"));
                    footwear.put("color", item.get("color"));
                    footWearList.add(footwear);
                }
            }
        }

        list.add(outerTopList);
        list.add(innerTopList);
        list.add(bottomList);
        list.add(footWearList);

        System.out.println("check: " + list);

        return list;
    }

    public HashMap<String,ArrayList<HashMap<String,String>>> checkColor(ArrayList<List<HashMap<String,String>>> filteredCloset, List<String> outfitColors){

        HashMap<String,String> outerTop;

        HashMap<String,String> innerTop;

        HashMap<String,String> bottom;

        HashMap<String,String> footwear;

        ArrayList<HashMap<String,String>> list = new ArrayList<>();

        HashMap<String,ArrayList<HashMap<String,String>>> outfit = new HashMap<>();

        for (int i = 0; i < filteredCloset.size(); i++){
            for (int j = 0; j < filteredCloset.get(i).size(); j ++){
                    if (i == 0 && filteredCloset.get(i).get(j).get("color").equals(outfitColors.get(0))){
                        outerTop = new HashMap<>();
                        outerTop.put("item", filteredCloset.get(i).get(j).get("item"));
                        outerTop.put("color", filteredCloset.get(i).get(j).get("color"));
                        list.add(outerTop);
                    }
                    else if ((i == 1) && (filteredCloset.get(i).get(j).get("color").equals(outfitColors.get(1)))){
                        innerTop = new HashMap<>();
                        innerTop.put("item", filteredCloset.get(i).get(j).get("item"));
                        innerTop.put("color", filteredCloset.get(i).get(j).get("color"));
                        list.add(innerTop);
                    }
                    else if ((i == 2) && filteredCloset.get(i).get(j).get("color").equals(outfitColors.get(2))){
                        bottom = new HashMap<>();
                        bottom.put("item", filteredCloset.get(i).get(j).get("item"));
                        bottom.put("color", filteredCloset.get(i).get(j).get("color"));
                        list.add(bottom);
                    }
                    else if ((i == 3) && filteredCloset.get(i).get(j).get("color").equals(outfitColors.get(3))){
                        footwear = new HashMap<>();
                        footwear.put("item", filteredCloset.get(i).get(j).get("item"));
                        footwear.put("color", filteredCloset.get(i).get(j).get("color"));
                        list.add(footwear);
                    }
            }
        }
        outfit.put("outfit", list);

        return outfit;
    }

    //TODO: might need to change successresponse parameters later
    //HashMap<String,String> data

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