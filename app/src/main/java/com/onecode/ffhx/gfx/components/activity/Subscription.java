package com.onecode.ffhx.gfx.components.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.billingclient.api.SkuDetails;
import com.google.android.material.card.MaterialCardView;
import com.onecode.ffhx.gfx.Constants;
import com.onecode.ffhx.gfx.R;
import com.onecode.ffhx.gfx.components.bottomsheets.ChargeReasonBottomsheet;
import com.onecode.ffhx.gfx.lib.ads.InterstitialAds;
import com.onecode.ffhx.gfx.lib.billing.IAPBilling;
import com.onecode.ffhx.gfx.lib.utils.WaitingDialog;
import com.onecode.ffhx.gfx.lib.utils.setBackground;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Subscription extends AppCompatActivity implements IAPBilling.BillingErrorHandler, IAPBilling.SkuDetailsListener {

    private ConstraintLayout weeklyCard, oneMonthCard, threeMonthsCard;
    private TextView weeklyPayment, oneMonthPayment, threeMonthsPayment;
    private TextView weekly_cost_per_month, one_month_cost_per_month, three_months_cost_per_month;
    private IAPBilling billingClass;
    WaitingDialog waitingDialog;
    WaitingDialog waitingDialogAds;
    InterstitialAds interstitialAds;
    Context context;
    public static MaterialCardView pro_version_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        setBackground.setGradientStatusBar(Subscription.this, getDrawable(R.drawable.subscription_background));

        initComponents();

        initBilling();
        //initClickListeners();

        ChargeReasonBottomsheet.Show(this);

        interstitialAds = new InterstitialAds(this);
        interstitialAds.loadAd();

        waitingDialogAds = new WaitingDialog(context);
        waitingDialogAds.show();

        new CountDownTimer(5000, 1000) {
            public void onTick(long millisUntilFinished) {
                if(interstitialAds.isAdLoaded()){
                    if(!((Activity) context).isFinishing())
                    {
                        waitingDialogAds.dismiss();
                        interstitialAds.showAd();
                    }
                    cancel();
                }
            }
            public void onFinish() {
                if(!((Activity) context).isFinishing())
                {
                    waitingDialogAds.dismiss();
                    interstitialAds.showAd();
                }
                cancel();
            }
        }.start();

        findViewById(R.id.button_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getStringExtra("status") != null){
                    if(getIntent().getStringExtra("status").equals("start")){
                        startActivity(new Intent(Subscription.this, ControllerActivity.class));
                        overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                        finish();
                    }else{
                        finish();
                    }
                }else{
                    startActivity(new Intent(Subscription.this, ControllerActivity.class));
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                    finish();
                }
            }
        });


        findViewById(R.id.pro_version_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.PRO_APP_LINK));
                startActivity(browserIntent);
            }
        });

    }

    private void initBilling(){
        billingClass = new IAPBilling(Subscription.this);
        billingClass.setmCallback(this, this);
        billingClass.startConnection();

        waitingDialog = new WaitingDialog(this);
        waitingDialog.show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initComponents() {

        context = this;

        pro_version_layout = findViewById(R.id.pro_version_layout);
        weeklyCard = findViewById(R.id.weekly);
        oneMonthCard = findViewById(R.id.one_month);
        threeMonthsCard = findViewById(R.id.three_months);

        //        Weekly Card Details
        weeklyPayment = findViewById(R.id.weekly_payment);
        weekly_cost_per_month = findViewById(R.id.weekly_cost_per_month);

        //        One Month Card Details
        oneMonthPayment = findViewById(R.id.one_month_payment);
        one_month_cost_per_month = findViewById(R.id.one_month_cost_per_month);

        //        Three Months Card Details
        threeMonthsPayment = findViewById(R.id.three_months_payment);
        three_months_cost_per_month = findViewById(R.id.three_months_cost_per_month);
    }

    @Override
    public void displayErrorMessage(String message) {
        if (message.equals("done")) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    initClickListeners();
                }
            });
        } else if (message.equals("error")) {
            Toast.makeText(Subscription.this, "Error getting billing services", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Subscription.this, "Error getting billing services", Toast.LENGTH_SHORT).show();
        }
    }

    private void initClickListeners() {
        weeklyCard.setOnClickListener(view -> {
            try {
                billingClass.purchaseSubscriptionItemByPos(2);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        oneMonthCard.setOnClickListener(view -> {
            try {
                billingClass.purchaseSubscriptionItemByPos(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        threeMonthsCard.setOnClickListener(view -> {
            try {
                billingClass.purchaseSubscriptionItemByPos(1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void subscriptionsDetailList(List<SkuDetails> skuDetailsList) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                try{
                    weeklyPayment.setText("Total Price " + skuDetailsList.get(1).getPrice());
                    //weekly_cost_per_month.setText(DecimalFormat((convertToInt(skuDetailsList.get(2).getOriginalPriceAmountMicros()) * 4.00)) + ".00");
                    weekly_cost_per_month.setText(DecimalFormat((convertToInt(skuDetailsList.get(1).getOriginalPriceAmountMicros()) * 4.00)) + ".00");

                    oneMonthPayment.setText("Total Price " + skuDetailsList.get(0).getPrice());
                    //one_month_cost_per_month.setText(convertToSimplePrice(skuDetailsList.get(0).getPrice()));
                    one_month_cost_per_month.setText(convertToSimplePrice(skuDetailsList.get(0).getPrice()));

                    threeMonthsPayment.setText("Total Price " + skuDetailsList.get(1).getPrice());
                    three_months_cost_per_month.setText(DecimalFormat((convertToInt(skuDetailsList.get(1).getOriginalPriceAmountMicros()) / 3.00)));
                }catch (Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            waitingDialog.dismiss();
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        waitingDialog.dismiss();
                    }
                });
            }
        });
    }

    private String convertToSimplePrice(String price){
        String input = price;
        String pattern = "\\d+\\.\\d+";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(input);
        if (m.find()) {
            String result = m.group(0);
            return result;
        } else {
            return "0";
        }
    }

    private double convertToInt(long price){
        long num = price;
        float dnum = (float) (num / 1000000);
        return dnum;
    }

    public String DecimalFormat(double price) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);
        return df.format(price);
    }

}