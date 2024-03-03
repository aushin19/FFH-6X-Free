package com.onecode.ffhx.gfx.components.bottomsheets;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.onecode.ffhx.gfx.R;
import com.onecode.ffhx.gfx.databinding.BottomsheetSensiBinding;
import com.onecode.ffhx.gfx.lib.modal.Sensi;

public class SensiBottomsheet {
    public static BottomSheetDialog dialog;
    public static BottomsheetSensiBinding binding;

    public static void Show(Context context, Sensi sensi) {

        dialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);

        dialog.setCancelable(true);
        dialog.setDismissWithAnimation(true);
        binding = BottomsheetSensiBinding.inflate(LayoutInflater.from(context));
        dialog.setContentView(binding.getRoot());

        if(!((Activity) context).isFinishing())
        {
            dialog.show();
        }

        binding.textView7.setText(sensi.player_name + " SENSITIVITY");

        binding.seekBar.setProgress(sensi.general);
        binding.rdSeekBar.setProgress(sensi.red_dot);
        binding.x2SeekBar.setProgress(sensi.x2_scope);
        binding.x4SeekBar.setProgress(sensi.x4_scope);
        binding.awmSeekBar.setProgress(sensi.AWM_scope);

        binding.value.setText(String.valueOf(sensi.general));
        binding.rdValue.setText(String.valueOf(sensi.red_dot));
        binding.x2Value.setText(String.valueOf(sensi.x2_scope));
        binding.x4Value.setText(String.valueOf(sensi.x4_scope));
        binding.awmValue.setText(String.valueOf(sensi.AWM_scope));

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
