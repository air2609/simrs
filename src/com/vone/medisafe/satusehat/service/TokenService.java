package com.vone.medisafe.satusehat.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vone.medisafe.mapping.SatuSehatToken;
import com.vone.medisafe.satusehat.SatuSehatConstant;
import com.vone.medisafe.satusehat.SatuSehatManager;
import com.vone.medisafe.service.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Scanner;

public class TokenService {

    public static  String getSatuSehatToken(){

        SatuSehatManager service = Service.getSatuSehatManager();
        SatuSehatToken token = service.getToken();
        if(null == token){
            SatuSehatToken satuSehatToken = new SatuSehatToken();
            satuSehatToken.setAccessToken(getTOken());
            satuSehatToken.setLastAccess(new Date());
            service.saveToken(satuSehatToken);

            return satuSehatToken.getAccessToken();
        }else{
            //compare last token created with time in now (minute base)
            Date now = new Date();
            long selish = now.getTime() - token.getLastAccess().getTime();

            long difference_In_Hours = (selish / (1000 * 60 * 60)) % 24;
            System.out.println("selisih jam : " + difference_In_Hours);
            if( difference_In_Hours > 0 ){
                token.setAccessToken(getTOken());
                token.setLastAccess(new Date());
                service.saveToken(token);
            }

            return token.getAccessToken();
        }

    }

    public static String getTOken(){
        String response = "";
        try {
            // URL endpoint untuk generate token
            String satuSehatUrl = SatuSehatConstant.SATU_SEHAT_URL+"/oauth2/v1/accesstoken?grant_type=client_credentials";
            URL url = new URL(satuSehatUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Mengatur metode request dan header
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setDoOutput(true);

            // Data yang akan dikirimkan dalam body request
            String clientId = SatuSehatConstant.CLINT_ID;
            String clientSecret = SatuSehatConstant.CLIENT_SECRET;
            String body = "client_id=" + clientId + "&client_secret=" + clientSecret;

            // Menulis data ke output stream
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Memeriksa kode respons
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Membaca respons dari server
                try (Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8.name())) {
                    String responseBody = scanner.useDelimiter("\\A").next();
                    response = getAccessToken(responseBody);
                }
            } else {
                System.out.println("Request failed. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String getAccessToken(String jsonString){
        Gson gson = new Gson();

        // Mengubah JSON string menjadi objek JsonObject
        JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);

        // Mengambil nilai access_token
        String accessToken = jsonObject.get("access_token").getAsString();
        return accessToken;
    }
}
