package com.onecode.ffhx.gfx.components.bottomsheets;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.onecode.ffhx.gfx.R;

public class PurchaseSuccessBottomsheet {
    public BottomSheetDialog dialog;

    public PurchaseSuccessBottomsheet(Context context){
        dialog = new BottomSheetDialog(context, R.style.BottomSheetTheme);
    }

    public void Show(Context context){
        dialog.setCancelable(false);
        dialog.setDismissWithAnimation(true);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.bottomsheet_purchased_successfull, dialog.findViewById(R.id.bottomSheetClearDataContainer));
        dialog.setContentView(dialogView);

        if(!((Activity) context).isFinishing())
        {
            dialog.show();
        }
    }
}
