package org.example;

import org.example.adminModels.ClaimParams;
import org.example.models.UserData;

import java.util.HashMap;
import java.util.Map;

public class Context {
    public Map<String, UserData> dataStore = new HashMap<>();
    ClaimParams claimParams;
    public String adminJson ="";
}