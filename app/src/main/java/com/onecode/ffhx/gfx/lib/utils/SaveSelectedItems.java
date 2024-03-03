package com.onecode.ffhx.gfx.lib.utils;

import android.content.Context;

import com.onecode.ffhx.gfx.Constants;

import java.util.ArrayList;

public class SaveSelectedItems {
    public static void setGameSpinnerItems(Context context, ArrayList<Integer> list){
        TinyDB tinyDB = new TinyDB(context);
        tinyDB.putListInt(Constants.GAME_SPINNER_ITEMS, list);
    }

    public static ArrayList getGameSpinnerItems(Context context){
        TinyDB tinyDB = new TinyDB(context);
        return tinyDB.getListString(Constants.GAME_SPINNER_ITEMS);
    }

    public static void setSettingsSpinnerItems(Context context, ArrayList<Boolean> list){
        TinyDB tinyDB = new TinyDB(context);
        tinyDB.putListBoolean(Constants.SETTINGS_SWITCH_STATUS, list);
    }

    public static ArrayList getSettingsSpinnerItems(Context context){
        TinyDB tinyDB = new TinyDB(context);
        return tinyDB.getListBoolean(Constants.SETTINGS_SWITCH_STATUS);
    }
}
