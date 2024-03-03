package com.onecode.ffhx.gfx.components.bottomsheets;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.onecode.ffhx.gfx.R;
import com.onecode.ffhx.gfx.databinding.BottomsheetAdBlockerBinding;

public class AdBlockerBottomsheet {
    public static BottomSheetDialog dialog;
    public static BottomsheetAdBlockerBinding binding;

    public static void Show(Context context) {

        dialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);

        dialog.setCancelable(false);
        dialog.setDismissWithAnimation(true);
        binding = BottomsheetAdBlockerBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).finish();
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
