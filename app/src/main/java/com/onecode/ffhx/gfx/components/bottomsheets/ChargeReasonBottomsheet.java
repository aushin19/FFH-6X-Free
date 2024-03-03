package com.onecode.ffhx.gfx.components.bottomsheets;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.onecode.ffhx.gfx.R;
import com.onecode.ffhx.gfx.databinding.BottomsheetChargingReasonBinding;

public class ChargeReasonBottomsheet {
    public static BottomSheetDialog dialog;
    public static BottomsheetChargingReasonBinding binding;

    public static void Show(Context context) {

        dialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);

        dialog.setCancelable(true);
        dialog.setDismissWithAnimation(true);
        binding = BottomsheetChargingReasonBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        binding.understandButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss(context);
            }
        });

        if(!((Activity) context).isFinishing())
        {
            dialog.show();
        }
    }

    public static void dismiss(Context context) {
        ((Activity)context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing())
                    dialog.dismiss();
            }
        });

    }

}
