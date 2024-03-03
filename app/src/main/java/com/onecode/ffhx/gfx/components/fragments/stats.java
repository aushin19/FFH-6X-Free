package com.onecode.ffhx.gfx.components.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.onecode.ffhx.gfx.databinding.FragmentStatsBinding;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class stats extends Fragment {
    Context context;
    FragmentStatsBinding binding;
    BroadcastReceiver batteryReceiver;
    private Handler handler;
    private Handler handler2;
    private Handler handler3;
    private Runnable runnable1;
    private HandlerThread runnable2;

    private static final int REQUEST_CODE_READ_CPU_TEMP = 101;
    private static final int REFRESH_INTERVAL = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatsBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        handler = new Handler();
        new Runnable() {
            @Override
            public void run() {
                getBatteryRemainingTime();
                handler.postDelayed(this, REFRESH_INTERVAL);

                int num = getRandomNumber(30, 99);
                binding.cpuUsage.setText(num+"%");
                //binding.cpuUsageProgressbar.setProgress(num, true);
            }
        }.run();

        //getCPUInfo();
        getBatteryInfo();
        //getDeviceInfo();

    }
    /*private void getCPUInfo(){
        binding.cpuCore.setText(getNumberOfCores() + "");
        binding.cpuCore2.setText(getNumberOfCores() + "");

        binding.cpuFrequency.setText(convertCpuInfo(getCpuFrequencyInfo()));
        binding.cpuFrequency2.setText(convertCpuInfo(getCpuFrequencyInfo()));

        binding.cpuHardware.setText(Build.HARDWARE.toUpperCase() + " " + getSoCModel());
        binding.cpuHardware2.setText(Build.HARDWARE.toUpperCase() + " " + getSoCModel());

        if (checkPermissions()) {
            startMonitoringTemperature();
        }
    }*/

    private void getBatteryRemainingTime(){
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, filter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPercentage = (level / (float) scale) * 100;

        float averageBatteryConsumptionRate = 0.2f;

        int remainingBatteryTimeMinutes = (int) (batteryPercentage / averageBatteryConsumptionRate);

        int remainingBatteryTimeHours = remainingBatteryTimeMinutes / 60;
        int remainingBatteryTimeMinutesRemaining = remainingBatteryTimeMinutes % 60;

        String remainingBatteryTime = String.valueOf(remainingBatteryTimeHours).trim() + "h "
                + remainingBatteryTimeMinutesRemaining + "m";

        binding.batteryTimeRemaining.setText(remainingBatteryTime);
    }

    /*private void getDeviceInfo(){
        binding.deviceBrand.setText(Build.BRAND);
        binding.deviceManu.setText(Build.MANUFACTURER);
        binding.deviceModel.setText(Build.MODEL);

        binding.systemCodenameTXT.setText(Build.VERSION.SDK_INT + "");
        binding.systemAndroidversionTXT.setText(Build.VERSION.RELEASE + "");
        binding.systemApilevelTXT.setText(Build.VERSION.SDK_INT + "");
        //binding.systemSecuritypatchTXT.setText(Build.VERSION.SECURITY_PATCH + "");
    }*/

    private void getBatteryInfo(){
        batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
                binding.batteryHealth.setText(getBatteryHealth(health));

                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                binding.batteryStatus.setText(getBatteryStatus(status));

                if(getBatteryStatus(status).equals("Charging"))
                    binding.batteryPower.setText("Plugged");
                else
                    binding.batteryPower.setText("Unplugged");

                int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
                binding.batteryVoltage.setText(voltage + " mV");

                binding.batteryLevelMain.setText(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) + "%");
                binding.batteryLevel.setText(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) + "%");

                int batTemp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
                binding.batteryTempMain.setText((Math.abs(batTemp) / 10) + "°C");
                binding.batteryTemp.setText((Math.abs(batTemp) / 10) + "°C");
            }
        };
        context.registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    private String getBatteryHealth(int health) {
        switch (health) {
            case BatteryManager.BATTERY_HEALTH_COLD:
                return "Cold";
            case BatteryManager.BATTERY_HEALTH_DEAD:
                return "Dead";
            case BatteryManager.BATTERY_HEALTH_GOOD:
                return "Good";
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                return "Overheat";
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                return "Over voltage";
            case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                return "Unknown";
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                return "Unspecified failure";
            default:
                return "N/A";
        }
    }

    private String getBatteryStatus(int status) {
        switch (status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                return "Charging";
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                return "Discharging";
            case BatteryManager.BATTERY_STATUS_FULL:
                return "Full";
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                return "Not charging";
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
                return "Unknown";
            default:
                return "N/A";
        }
    }

    private int getNumberOfCores() {
        return Runtime.getRuntime().availableProcessors();
    }

    private String getCpuFrequencyInfo() {
        StringBuilder cpuInfo = new StringBuilder();

        int numOfCores = Runtime.getRuntime().availableProcessors();
        for (int i = 0; i < numOfCores; i++) {
            float frequency = getCoreFrequency(i);
            cpuInfo.append(i + 1).append(" x ").append(String.format("%.2f", frequency)).append(" GHz");

            if (i < numOfCores - 1) {
                cpuInfo.append(" , ");
            }
        }

        return cpuInfo.toString();
    }

    private float getCoreFrequency(int coreIndex) {
        try {
            String corePath = "/sys/devices/system/cpu/cpu" + coreIndex + "/cpufreq/scaling_cur_freq";
            java.util.Scanner scanner = new java.util.Scanner(new FileReader(corePath)).useDelimiter("\\s+");
            if (scanner.hasNextInt()) {
                int frequencyInKHz = scanner.nextInt();
                return frequencyInKHz / 1000000.0f;
            }
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private String convertCpuInfo(String cpuInfo) {
        String[] frequencies = cpuInfo.split(" , ");

        Map<String, Integer> frequencyMap = new LinkedHashMap<>();

        for (String frequency : frequencies) {
            String[] parts = frequency.split(" x ");
            String value = parts[1];
            //frequencyMap.put(value, frequencyMap.getOrDefault(value, 0) + 1);
        }

        StringBuilder output = new StringBuilder();
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            String value = entry.getKey();
            int count = entry.getValue();
            if(count == 3){
                output.append(count).append(" x ").append(value).append(",\n");
                continue;
            }
            output.append(count).append(" x ").append(value).append(", ");
        }

        if (output.length() > 2) {
            output.setLength(output.length() - 2);
        }

        return output.toString();
    }

    private String getSoCModel() {
        String architecture = System.getProperty("os.arch");
        String abi = Build.CPU_ABI;
        String abi2 = Build.CPU_ABI2;

        if (abi.equals("arm64-v8a") || abi2.equals("arm64-v8a")) {
            return "ARM 64-bit";
        } else if (abi.equals("armeabi-v7a") || abi2.equals("armeabi-v7a")) {
            return "ARM 32-bit";
        } else if (abi.equals("x86") || abi2.equals("x86")) {
            return "x86";
        } else if (abi.equals("x86_64") || abi2.equals("x86_64")) {
            return "x86 64-bit";
        }

        return architecture;
    }

    private boolean checkPermissions() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_CPU_TEMP);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_CPU_TEMP) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startMonitoringTemperature();
            }
        }
    }

    private void startMonitoringTemperature() {
        handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                String cpuTemperature = getCpuTemperature();
                binding.cpuTemp.setText(cpuTemperature);

                handler2.postDelayed(this, REFRESH_INTERVAL);
            }
        }, REFRESH_INTERVAL);
    }

    private String getCpuTemperature() {
        String temperature = "N/A";

        try {
            File tempFile = new File("/sys/class/thermal/thermal_zone0/temp");
            if (tempFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(tempFile));
                String tempString = reader.readLine();
                reader.close();

                if (tempString != null) {
                    float tempValue = Float.parseFloat(tempString) / 1000.0f;
                    temperature = Math.round(tempValue) + "°C";
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return temperature;
    }

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable1);
        handler2.removeCallbacks(runnable2);
    }
}