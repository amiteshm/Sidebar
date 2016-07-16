package com.fusebulb.sidebar;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by amiteshmaheshwari on 12/07/16.
 */
public class SharedPreference {
    public static final String USER_SETTINGS= "USER_SETTINGS";
    public static final String USER_NAME = "user_name";
    public static final String USER_LANG = "language";



    public SharedPreference(){
        super();

    }



    public void setUserName(Context context, String user_name){
        SharedPreferences settings = context.getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);
        Editor editor = settings.edit();
        editor.putString(USER_NAME, user_name);
        editor.commit();
    }

    public String getUserName(Context context){
        SharedPreferences settings = context.getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);
        return settings.getString(USER_NAME, null);
    }


    public void setUserLanguage(Context context, String user_language){
        SharedPreferences settings = context.getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);
        Editor editor = settings.edit();
        editor.putString(USER_LANG, user_language);
        editor.commit();
    }


    public String getUserLanguage(Context context){
        SharedPreferences settings = context.getSharedPreferences(USER_SETTINGS, Context.MODE_PRIVATE);
        String userLang = settings.getString(USER_LANG, null);
        if(userLang != null){
            return userLang;
        } else {
            return UserSettings.lang_en;
        }
    }


}
