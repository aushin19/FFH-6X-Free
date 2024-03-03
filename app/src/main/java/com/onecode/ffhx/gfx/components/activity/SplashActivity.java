package com.onecode.ffhx.gfx.components.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.SkuDetails;
import com.onecode.ffhx.gfx.AppConfig;
import com.onecode.ffhx.gfx.Constants;
import com.onecode.ffhx.gfx.R;
import com.onecode.ffhx.gfx.databinding.ActivitySplashBinding;
import com.onecode.ffhx.gfx.lib.billing.IAPBilling;
import com.onecode.ffhx.gfx.lib.utils.TinyDB;

import java.util.List;

public class SplashActivity extends AppCompatActivity implements IAPBilling.BillingErrorHandler, IAPBilling.SkuDetailsListener {
    IAPBilling iapBilling;
    ActivitySplashBinding binding;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initTerms();
    }

    private void initTerms(){
        tinyDB = new TinyDB(this);

        if(tinyDB.getBoolean("is_Terms_Accepted")){
            initBilling();
        }else{
            binding.termsLayout.setVisibility(View.VISIBLE);
        }

        binding.agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(binding.privacyCheckBox.isChecked() && binding.termsCheckBox.isChecked()){
                    tinyDB.putBoolean("is_Terms_Accepted", true);
                    startActivity(new Intent(SplashActivity.this, ControllerActivity.class));
                    finish();
                }else{
                    Toast.makeText(SplashActivity.this, "Make sure, your are agree to everything above!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.termsLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.APP_TERMS_LINK));
                startActivity(browserIntent);
            }
        });

        binding.privacyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.APP_PRIVACY_LINK));
                startActivity(browserIntent);
            }
        });
    }

    private void initBilling(){
        iapBilling = new IAPBilling(SplashActivity.this);
        iapBilling.setmCallback( this, this);
        iapBilling.startConnection();
    }

    @Override
    public void displayErrorMessage(String message) {
        if(message.equalsIgnoreCase("done")){
            iapBilling.isSubscribedToSubscriptionItem(this);
        }
        else{
            AppConfig.isUserPaid = false;

            Intent intent = new Intent(SplashActivity.this, Subscription.class);
            intent.putExtra("status", "start");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            finish();
        }

    }

    @Override
    public void subscriptionsDetailList(List<SkuDetails> skuDetailsList) {

    }
}