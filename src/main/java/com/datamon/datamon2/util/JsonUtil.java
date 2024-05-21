package com.datamon.datamon2.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class JsonUtil {
    private Gson gson = new Gson();

    public String toJsonStringByMap(Map value){
        return gson.toJson(value);
    }

    public Map toMapByJsonString(String json){
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        return gson.fromJson(json, type);
    }
}
