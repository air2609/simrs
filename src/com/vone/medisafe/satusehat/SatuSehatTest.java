package com.vone.medisafe.satusehat;

import com.vone.medisafe.satusehat.service.LocationService;
import com.vone.medisafe.satusehat.service.PatientService;
import com.vone.medisafe.satusehat.service.TokenService;

public class SatuSehatTest {

    public static void main(String[] args) {
//        PatientService service = new PatientService();
//        PatientService.registerPatient("3601171701820001","Arifullah", "male",
//                "1982-01-17",0, "Kp. Bara", "Serang", "42195",
//                "36", "3604", "360417", "3604172003", "019", "04");

//        LocationService service = new LocationService();
//        service.getAllPropinsi();

        String ihsNumber = PatientService.getIhsNumber("3601171701820001");
        System.out.println("nomor ihs : "+ ihsNumber);

//        String token = TokenService.getSatuSehatToken();
//        System.out.println(token);
    }


}
