package com.example.myapplication.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static com.example.myapplication.utils.Constants.KEY_API_TOKEN;
import static com.example.myapplication.utils.Constants.KEY_LOGIN_STATE;
import static com.example.myapplication.utils.Constants.KEY_USER_EMAIL;
import static com.example.myapplication.utils.Constants.KEY_USER_NAME;
import static com.example.myapplication.utils.Constants.KEY_USER_PHONE;
import static com.example.myapplication.utils.Constants.KEY_USER_ROLE;

public class FactsPreferences {
    private static SharedPreferences preferences;

    public FactsPreferences(Context context) {
        preferences = context.getSharedPreferences("facts-africa", Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn() {
        return preferences.getBoolean(KEY_LOGIN_STATE, false);
    }

    public void setLoggedIn(boolean loggedIn) {
        preferences.edit().putBoolean(KEY_LOGIN_STATE, loggedIn).apply();
    }

    public void setApiToken(String token) {
        preferences.edit().putString(KEY_API_TOKEN, token).apply();
    }

    public static String getApiToken() {
        String token = preferences.getString(KEY_API_TOKEN, "");
        return "Bearer " +token;
    }



    public void setUserName(String userName) {
        preferences.edit().putString(KEY_USER_NAME, userName).apply();
    }

    public static String getUserName() {
        return preferences.getString(KEY_USER_NAME, "");
    }

    public void setUserEmail(String userEmail) {
        preferences.edit().putString(KEY_USER_EMAIL, userEmail).apply();
    }

    public static String getUserEmail() {
        return preferences.getString(KEY_USER_EMAIL,"");
    }

    public void setUserRole(int userRole) {
        preferences.edit().putInt(KEY_USER_ROLE, userRole).apply();
    }

    public static int getUserRole() { return preferences.getInt(KEY_USER_ROLE, 0); }

    public static String getUserPhone(){
        return preferences.getString(KEY_USER_PHONE, "");
    }
    public void setUserPhone(String phone){
        preferences.edit().putString(KEY_USER_PHONE, phone).apply();
    }
}
