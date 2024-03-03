package com.onecode.ffhx.gfx.components.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.onecode.ffhx.gfx.AppConfig;
import com.onecode.ffhx.gfx.Constants;
import com.onecode.ffhx.gfx.R;
import com.onecode.ffhx.gfx.components.bottomsheets.AdBlockerBottomsheet;
import com.onecode.ffhx.gfx.databinding.ActivityControllerBinding;
import com.onecode.ffhx.gfx.lib.adapters.MainPageViewAdapter;
import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;

import java.net.HttpURLConnection;
import java.net.URL;

public class ControllerActivity extends AppCompatActivity {
     ActivityControllerBinding binding;
    private AppUpdateManager mAppUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityControllerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initFirebaseAnalytics();
        checkInAppUpdate();
        initOneSignal();

        checkAdBlocker();
        checkPremium();
        setNavigation();
    }

    private void checkAdBlocker() {
        if (!AppConfig.isUserPaid) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(Constants.AD_BLOCKER_LINK);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.connect();
                    } catch (Exception e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AdBlockerBottomsheet.Show(ControllerActivity.this);
                            }
                        });
                    }
                }
            }).start();
        }
    }

    private void checkInAppUpdate() {
        mAppUpdateManager = AppUpdateManagerFactory.create(this);
        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(result, AppUpdateType.IMMEDIATE, ControllerActivity.this, 100);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void checkPremium() {
        if (AppConfig.isUserPaid) {
            binding.premiumIcon.setVisibility(View.GONE);
        } else {
            binding.premiumIcon.setVisibility(View.VISIBLE);
            binding.premiumIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ControllerActivity.this, Subscription.class);
                    intent.putExtra("status", "start");
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                    finish();
                }
            });
        }
    }

    private void initOneSignal() {
        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);

        OneSignal.initWithContext(this, AppConfig.ONE_SIGNAL_ID);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            OneSignal.getNotifications().requestPermission(true, Continue.with(r -> {
                if (r.isSuccess()) {
                    if (r.getData()) {
                        // `requestPermission` completed successfully and the user has accepted permission
                    }
                    else {
                        // `requestPermission` completed successfully but the user has rejected permission
                    }
                }
                else {
                    // `requestPermission` completed unsuccessfully, check `r.getThrowable()` for more info on the failure reason
                }
            }));
        }
    }

    private void initFirebaseAnalytics() {
        FirebaseAnalytics.getInstance(this);
    }

    private void setNavigation() {
        binding.mainPageView.setAdapter(new MainPageViewAdapter(getSupportFragmentManager()));
        binding.mainPageView.setOffscreenPageLimit(2);

        binding.bottomNav.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.dashboard) {
                binding.mainPageView.setCurrentItem(0);
            } else if (item.getItemId() == R.id.sensitivity) {
                binding.mainPageView.setCurrentItem(1);
            }
            return true;
        });

        binding.mainPageView.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position) {
                    case 0:
                        runTextAnim("Game Basics");
                        break;
                    case 1:
                        runTextAnim("Sensitivity");
                }
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        binding.bottomNav.getMenu().findItem(R.id.dashboard).setChecked(true);
                        break;
                    case 1:
                        binding.bottomNav.getMenu().findItem(R.id.sensitivity).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    void runTextAnim(String text) {
        binding.appbarMainText.setText(text);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo result) {
                if (result.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    try {
                        mAppUpdateManager.startUpdateFlowForResult(result, AppUpdateType.IMMEDIATE, ControllerActivity.this, 100);
                    } catch (IntentSender.SendIntentException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}