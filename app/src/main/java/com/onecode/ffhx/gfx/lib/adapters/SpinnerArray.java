package com.onecode.ffhx.gfx.lib.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.onecode.ffhx.gfx.R;

public class SpinnerArray {
    public static String[] resolution = {"Default","144p","360p","480p","540p","640p","720p","1080p","1080p HD+","1440p"};
    public static String[] graphics = {"Default","Smooth","Balanced","HD","HDR","Ultra","Super Smooth"};
    public static String[] fps = {"Default","30FPS","40FPS","60FPS","90FPS","120FPS"};
    public static String[] styles = {"Classic","Colorful","Realistic","Soft","Movie"};
    public static String[] sound = {"Low","High","Ultra"};
    public static String[] water = {"Disable","Enable"};
    public static String[] shadow = {"Disable","Enable"};
    public static String[] detail = {"Less","Medium","Show all"};

    public static ArrayAdapter<CharSequence> spinnerAttach(Context context, String[] array){
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(context, R.layout.spinner_text, array);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown);

        return adapter;
    }

}
