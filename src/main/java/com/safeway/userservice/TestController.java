package com.safeway.userservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestController {

    public static String basePath = System.getProperty("user.dir") + "/src/main/resources/";

    public static void main(String[] args) {
        String filedata = readJsonFileAsString("csvjson.json");
//        try {
//            JSONObject jsonObject = new JSONObject(filedata);
//        }catch (JSONException err){
////            Log.d("Error", err.toString());
//        }
        System.out.println(filedata);
    }



    public static String readJsonFileAsString(String fileName) {
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(basePath, fileName)));
            return jsonString;
        } catch (IOException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        return null;
    }

}
