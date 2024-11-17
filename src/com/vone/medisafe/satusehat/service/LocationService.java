package com.vone.medisafe.satusehat.service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.vone.medisafe.satusehat.SatuSehatConstant;
import com.vone.medisafe.satusehat.masterdata.Location;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LocationService {

    public static List<Location> getAllPropinsi(){
        String provUrl = SatuSehatConstant.SATU_SEHAT_URL+"/masterdata/v1/provinces";
        String accessToken = TokenService.getSatuSehatToken();
        try{
            URL url = new URL(provUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Mengatur metode request dan header
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            // Membaca respons dari server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
            JsonArray dataArray = jsonObject.getAsJsonArray("data");

            // Mengonversi JsonArray ke ArrayList
            List<Location> provinceList = gson.fromJson(dataArray, new TypeToken<ArrayList<Location>>() {}.getType());

           return provinceList;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<Location> getCitiesByPronvinceCode(String provinceCode){
        String citiesUrl = SatuSehatConstant.SATU_SEHAT_URL+"/masterdata/v1/cities?province_codes="+provinceCode;
        String accessToken = TokenService.getSatuSehatToken();
        try{
            URL url = new URL(citiesUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Mengatur metode request dan header
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            // Membaca respons dari server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
            JsonArray dataArray = jsonObject.getAsJsonArray("data");

            // Mengonversi JsonArray ke ArrayList
            List<Location> cityList = gson.fromJson(dataArray, new TypeToken<ArrayList<Location>>() {}.getType());

            return cityList;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<Location> getDistrictsByCityCode(String cityCode){
        String citiesUrl = SatuSehatConstant.SATU_SEHAT_URL+"/masterdata/v1/districts?city_codes="+cityCode;
        String accessToken = TokenService.getSatuSehatToken();
        try{
            URL url = new URL(citiesUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Mengatur metode request dan header
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            // Membaca respons dari server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
            JsonArray dataArray = jsonObject.getAsJsonArray("data");

            // Mengonversi JsonArray ke ArrayList
            List<Location> districtList = gson.fromJson(dataArray, new TypeToken<ArrayList<Location>>() {}.getType());

            return districtList;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static List<Location> getSubdistrictsByDistrictCode(String districtCode){
        String citiesUrl = SatuSehatConstant.SATU_SEHAT_URL+"/masterdata/v1/sub-districts?district_codes="+districtCode;
        String accessToken = TokenService.getSatuSehatToken();
        try{
            URL url = new URL(citiesUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Mengatur metode request dan header
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            // Membaca respons dari server
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(response.toString(), JsonObject.class);
            JsonArray dataArray = jsonObject.getAsJsonArray("data");

            // Mengonversi JsonArray ke ArrayList
            List<Location> subdistrictList = gson.fromJson(dataArray, new TypeToken<ArrayList<Location>>() {}.getType());

            return subdistrictList;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
