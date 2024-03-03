package com.onecode.ffhx.gfx.components.fragments;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.VERSION.SDK_INT;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.onecode.ffhx.gfx.Constants;
import com.onecode.ffhx.gfx.R;
import com.onecode.ffhx.gfx.databinding.FragmentPrimaryGfxBinding;
import com.onecode.ffhx.gfx.lib.adapters.SpinnerArray;
import com.onecode.ffhx.gfx.lib.ads.InterstitialAds;
import com.onecode.ffhx.gfx.lib.utils.SaveSelectedItems;
import com.onecode.ffhx.gfx.lib.utils.WaitingDialog;

import java.util.ArrayList;

public class PrimaryGFX extends Fragment implements AdapterView.OnItemSelectedListener {
    public static FragmentPrimaryGfxBinding binding;
    Context context;
    SharedPreferences sharedPreferences;
    int resolution_int, fps_int, graphics_int, styles_int, sound_int, water_int, shadow_int, detail_int;
    ArrayList<Integer> spinnerItemList = new ArrayList<>();
    InterstitialAds interstitialAds;
    WaitingDialog waitingDialog;

    public PrimaryGFX() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPrimaryGfxBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();
        getButton();
    }

    private void init(){
        context = getContext();

        interstitialAds = new InterstitialAds(context);
        interstitialAds.loadAd();

        waitingDialog = new WaitingDialog(context);
        sharedPreferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                setSpinners();
            }
        }).start();
    }

    private void setSpinners() {
        binding.resolutionSpinner.setAdapter(SpinnerArray.spinnerAttach(context, SpinnerArray.resolution));
        binding.resolutionSpinner.setOnItemSelectedListener(this);

        binding.fpsSpinner.setAdapter(SpinnerArray.spinnerAttach(context, SpinnerArray.fps));
        binding.fpsSpinner.setOnItemSelectedListener(this);

        binding.graphicsSpinner.setAdapter(SpinnerArray.spinnerAttach(context, SpinnerArray.graphics));
        binding.graphicsSpinner.setOnItemSelectedListener(this);

        binding.stylesSpinner.setAdapter(SpinnerArray.spinnerAttach(context, SpinnerArray.styles));
        binding.stylesSpinner.setOnItemSelectedListener(this);

        binding.soundSpinner.setAdapter(SpinnerArray.spinnerAttach(context, SpinnerArray.sound));
        binding.soundSpinner.setOnItemSelectedListener(this);

        binding.waterSpinner.setAdapter(SpinnerArray.spinnerAttach(context, SpinnerArray.water));
        binding.waterSpinner.setOnItemSelectedListener(this);

        binding.shadowSpinner.setAdapter(SpinnerArray.spinnerAttach(context, SpinnerArray.shadow));
        binding.shadowSpinner.setOnItemSelectedListener(this);

        binding.detailSpinner.setAdapter(SpinnerArray.spinnerAttach(context, SpinnerArray.detail));
        binding.detailSpinner.setOnItemSelectedListener(this);

        setSpinnerItems();
    }

    public void setSpinnerItems() {
        ArrayList<Integer> list = SaveSelectedItems.getGameSpinnerItems(context);
        if (!list.isEmpty()) {
            binding.resolutionSpinner.setSelection(Integer.parseInt(list.get(0) + ""));
            binding.fpsSpinner.setSelection(Integer.parseInt(list.get(1) + ""));
            binding.graphicsSpinner.setSelection(Integer.parseInt(list.get(2) + ""));
            binding.stylesSpinner.setSelection(Integer.parseInt(list.get(3) + ""));
            binding.soundSpinner.setSelection(Integer.parseInt(list.get(4) + ""));
            binding.waterSpinner.setSelection(Integer.parseInt(list.get(5) + ""));
            binding.shadowSpinner.setSelection(Integer.parseInt(list.get(6) + ""));
            binding.detailSpinner.setSelection(Integer.parseInt(list.get(7) + ""));
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int viewId = parent.getId();

        if (viewId == R.id.resolution_spinner) {
            resolution_int = binding.resolutionSpinner.getSelectedItemPosition();
        } else if (viewId == R.id.fps_spinner) {
            fps_int = binding.fpsSpinner.getSelectedItemPosition();
        } else if (viewId == R.id.graphics_spinner) {
            graphics_int = binding.graphicsSpinner.getSelectedItemPosition();
        } else if (viewId == R.id.styles_spinner) {
            styles_int = binding.stylesSpinner.getSelectedItemPosition();
        } else if (viewId == R.id.sound_spinner) {
            sound_int = binding.soundSpinner.getSelectedItemPosition();
        } else if (viewId == R.id.water_spinner) {
            water_int = binding.waterSpinner.getSelectedItemPosition();
        } else if (viewId == R.id.shadow_spinner) {
            shadow_int = binding.shadowSpinner.getSelectedItemPosition();
        } else if (viewId == R.id.detail_spinner) {
            detail_int = binding.detailSpinner.getSelectedItemPosition();
        }

        spinnerItemList.add(0, binding.resolutionSpinner.getSelectedItemPosition());
        spinnerItemList.add(1, binding.fpsSpinner.getSelectedItemPosition());
        spinnerItemList.add(2, binding.graphicsSpinner.getSelectedItemPosition());
        spinnerItemList.add(3, binding.stylesSpinner.getSelectedItemPosition());
        spinnerItemList.add(4, binding.soundSpinner.getSelectedItemPosition());
        spinnerItemList.add(5, binding.waterSpinner.getSelectedItemPosition());
        spinnerItemList.add(6, binding.shadowSpinner.getSelectedItemPosition());
        spinnerItemList.add(7, binding.detailSpinner.getSelectedItemPosition());

        SaveSelectedItems.setGameSpinnerItems(context, spinnerItemList);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getButton() {
        binding.primaryApplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.primaryApplyButton.getText().toString().equals("Settings Applied")){
                    Toast.makeText(context, "Settings Already Applied\nGo Back and Launch your game!", Toast.LENGTH_SHORT).show();
                }else{
                    waitingDialog.show();
                    int random = (int) (Math.random() * 5 + 2)*1000;
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            waitingDialog.dismiss();
                            binding.primaryApplyButton.setText("Settings Applied");
                            interstitialAds.showAd();
                        }
                    }, random);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}