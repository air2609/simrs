package com.vone.medisafe.satusehat.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vone.medisafe.satusehat.SatuSehatConstant;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class PatientService {

    public static String registerPatient(String nik, String name, String gender, String birthDate,
                        int lahirKembar, String alamat, String kota, String kodePos, String kodePropinsi, String kodeKota, String kodeKecamatan,
                                         String kodeDesa, String rt, String rw, String contactName, String contactPhone) {

        String ihsNumber = null;
        try {
            // URL endpoint untuk membuat pasien baru
            String registrationUrl = SatuSehatConstant.SATU_SEHAT_URL + "/fhir-r4/v1/Patient";
            String accessToken = TokenService.getSatuSehatToken();
            URL url = new URL(registrationUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Mengatur metode request dan header
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            connection.setDoOutput(true);

            // JSON data untuk membuat pasien baru
            String jsonInputString = String.format("{"
                    + "\"resourceType\": \"Patient\","
                    + "\"identifier\": [{"
                    + "    \"use\": \"official\","
                    + "    \"system\": \"https://fhir.kemkes.go.id/id/nik\","
                    + "    \"value\": \"%s\""
                    + "}],"
                    + "\"name\": [{"
                    + "    \"use\": \"official\","
                    + "    \"text\": \"%s\" "
                    + "}],"
                    + "\"gender\": \"%s\","
                    + "\"birthDate\": \"%s\","
                    + "\"multipleBirthInteger\": %s ,"
                    + "\"address\": [{"
                    + "    \"use\": \"home\","
                    + "    \"line\": [\"%s\"],"
                    + "    \"city\": \"%s\","
                    + "    \"postalCode\": \"%s\","
                    + "    \"country\": \"ID\","
                    + "    \"extension\": [{"
                    + "        \"url\": \"https://fhir.kemkes.go.id/r4/StructureDefinition/administrativeCode\","
                    + "        \"extension\": [{"
                    + "            \"url\": \"province\","
                    + "            \"valueCode\": \"%s\""
                    + "        }, {"
                    + "            \"url\": \"city\","
                    + "            \"valueCode\": \"%s\""
                    + "        }, {"
                    + "            \"url\": \"district\","
                    + "            \"valueCode\": \"%s\""
                    + "        }, {"
                    + "            \"url\": \"village\","
                    + "            \"valueCode\": \"%s\""
                    + "        }, {"
                    + "            \"url\": \"rt\","
                    + "            \"valueCode\": \"%s\""
                    + "        }, {"
                    + "            \"url\": \"rw\","
                    + "            \"valueCode\": \"%s\""
                    + "        }]"
                    + "    }]"
                    + "}]," +
                    "\"contact\": [\n" +
                    "      {\n" +
                    "         \"relationship\": [\n" +
                    "            {\n" +
                    "               \"coding\": [\n" +
                    "                  {\n" +
                    "                     \"system\": \"http://terminology.hl7.org/CodeSystem/v2-0131\",\n" +
                    "                     \"code\": \"C\"\n" +
                    "                  }\n" +
                    "               ]\n" +
                    "            }\n" +
                    "         ],\n" +
                    "         \"name\": {\n" +
                    "            \"use\": \"official\",\n" +
                    "            \"text\": \"%s\"\n" +
                    "         },\n" +
                    "         \"telecom\": [\n" +
                    "            {\n" +
                    "               \"system\": \"phone\",\n" +
                    "               \"value\": \"%s\",\n" +
                    "               \"use\": \"mobile\"\n" +
                    "            }\n" +
                    "         ]\n" +
                    "      }\n" +
                    "   ]"
                    + "}", nik, name, gender, birthDate, lahirKembar, alamat, kota, kodePos,kodePropinsi, kodeKota, kodeKecamatan, kodeDesa, rt, rw, contactName, contactPhone);

            System.out.println(jsonInputString);

            // Menulis data ke output stream
           try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Memeriksa kode respons
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Membaca respons dari server
                try (Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8.name())) {
                    String responseBody = scanner.useDelimiter("\\A").next();
                    System.out.println("Response: " + responseBody);
                    ihsNumber = getPatientId(responseBody, "patient_id");
                }
            }else if(responseCode == HttpURLConnection.HTTP_BAD_REQUEST){
                System.out.println(connection.getResponseMessage());
                try (Scanner scanner = new Scanner(connection.getErrorStream(), StandardCharsets.UTF_8.name())) {
                    String responseBody = scanner.useDelimiter("\\A").next();
                    System.out.println("Response: " + responseBody);
                    ihsNumber = getPatientId(responseBody, "resourceId");
                }
            }
            else {
                System.out.println("Request failed. Response Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ihsNumber;
    }

    private static String getPatientId(String responseBody, String fieldName) {
        String ihsNumber = null;
        JsonObject jsonObject = JsonParser.parseString(responseBody).getAsJsonObject();
        ihsNumber  = jsonObject.getAsJsonObject("data").get(fieldName).getAsString();
        return ihsNumber;
    }

    public static String getIhsNumber(String nik) {
        String inquiryIhsUrlbyNik = SatuSehatConstant.SATU_SEHAT_URL + "/fhir-r4/v1/Patient?identifier=https://fhir.kemkes.go.id/id/nik|" + nik;
        try {
            // Create URL object
            String accessToken = TokenService.getSatuSehatToken();
            URL url = new URL(inquiryIhsUrlbyNik);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);

            // Get response code
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println("response : "+response);
                return getIhsFromJson(response.toString());
            } else {
                System.out.println("GET request failed: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getIhsFromJson(String stringJson){
        JsonObject jsonObject = JsonParser.parseString(stringJson).getAsJsonObject();
        JsonArray entries = jsonObject.getAsJsonArray("entry");

        if (!entries.isEmpty()) {
            JsonObject resource = entries.get(0).getAsJsonObject().getAsJsonObject("resource");
            String id = resource.get("id").getAsString();
            return id;
        }
        return null;
    }
}


