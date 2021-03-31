package com.example.covid19helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class StateAndDistrictData {

    public static List<String> state = new ArrayList<String>(Arrays.asList("Maharashtra",
            "Gujarat",
            "Uttar Pradesh"));

    public static Map<String, List<String>> district = new HashMap<String, List<String>>();

    public StateAndDistrictData() {
        district.put("Maharashtra", new ArrayList<String>(Arrays.asList("Ahmednagar",
                "Akola",
                "Amravati",
                "Aurangabad",
                "Beed",
                "Bhandara",
                "Buldhana",
                "Chandrapur",
                "Dhule",
                "Gadchiroli",
                "Gondia",
                "Hingoli",
                "Jalgaon",
                "Jalna",
                "Kolhapur",
                "Latur",
                "Mumbai City",
                "Mumbai Suburban",
                "Nagpur",
                "Nanded",
                "Nandurbar",
                "Nashik",
                "Osmanabad",
                "Palghar",
                "Parbhani",
                "Pune",
                "Raigad",
                "Ratnagiri",
                "Sangli",
                "Satara",
                "Sindhudurg",
                "Solapur",
                "Thane",
                "Wardha",
                "Washim",
                "Yavatmal"
        )));

        district.put("Gujarat", new ArrayList<String>(Arrays.asList("Ahmedabad",
                "Amreli",
                "Anand",
                "Aravalli",
                "Banaskantha (Palanpur)",
                "Bharuch",
                "Bhavnagar",
                "Botad",
                "Chhota Udepur",
                "Dahod",
                "Dangs (Ahwa)",
                "Devbhoomi Dwarka",
                "Gandhinagar",
                "Gir Somnath")));

        district.put("Uttar Pradesh", new ArrayList<String>(Arrays.asList("Bareilly", "Gorakhpur", "Mirzapur", "Kanpur",
                "Agra", "Lucknow", "Prayagraj", "Ayodhya")));
    }

    public ArrayList<String> getDistricts(String state) {
        return (ArrayList<String>) district.get(state);
    }
}
