package com.onecode.ffhx.gfx.components.fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.health.connect.datatypes.AppInfo;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.TransitionManager;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.transition.MaterialSharedAxis;
import com.onecode.ffhx.gfx.R;
import com.onecode.ffhx.gfx.databinding.FragmentDashboardBinding;
import com.onecode.ffhx.gfx.lib.adapters.DashboardAdapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
public class dashboard extends Fragment{
    public static FragmentDashboardBinding binding;
    static Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

        setupViewPager(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);

        new Thread(new Runnable() {
            @Override
            public void run() {
                updateInfo();
            }
        }).start();
    }

    private void init(){
        context = getContext();
    }

    private void setupViewPager(ViewPager viewPager) {
        DashboardAdapter adapter = new DashboardAdapter(getChildFragmentManager());

        adapter.addFragment(new PrimaryGFX(), "Primary GFX");

        viewPager.setAdapter(adapter);
    }

    private void updateInfo() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                updateRamUsage();
                getCpuTemperature();
            }
        });
    }

    private void updateRamUsage() {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);

        long availableMegs = memoryInfo.availMem / 0x100000L;
        long totalMegs = memoryInfo.totalMem / 0x100000L;
        long usedMegs = totalMegs - availableMegs;

        int usedPercentage = Math.toIntExact(Math.round((double) usedMegs / totalMegs * 100));

        double totalGigs = (double) totalMegs / 1024;
        double usedGigs = (double) usedMegs / 1024;

        String ramUsage = usedPercentage + "%";
        binding.totalRam.setText(String.format("%.2f", totalGigs) + " GB");
        binding.usedRam.setText(String.format("%.2f", usedGigs) + " GB");

        binding.ramUsagePercentage.setText(ramUsage);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.ramUsageProgressbar.setProgress(usedPercentage, true);
        }else{
            binding.ramUsageProgressbar.setProgress(usedPercentage);
        }

        new Handler().postDelayed(this::updateInfo, 5000);
    }

    public void getCpuTemperature() {
        Process process;
        try {
            process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = reader.readLine();
            if (line != null) {
                double temp = (Float.parseFloat(line)) / 1000.0f;
                //Log.d("shivam",  (int) temp + "");
            } else {
                //return 0.0f;
            }
        } catch (Exception e) {
            e.printStackTrace();
            //return 0.0f;
        }

        new Handler().postDelayed(this::getCpuTemperature, 5000);
    }

    @Override
    public void onResume() {
        super.onResume();
        //cpuUsageMonitor.startMonitoring(context, this);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}