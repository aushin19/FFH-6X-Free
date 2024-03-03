package com.onecode.ffhx.gfx.lib.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onecode.ffhx.gfx.R;
import com.onecode.ffhx.gfx.components.bottomsheets.SensiBottomsheet;
import com.onecode.ffhx.gfx.databinding.BottomsheetSensiBinding;
import com.onecode.ffhx.gfx.databinding.SensiListBinding;
import com.onecode.ffhx.gfx.lib.ads.InterstitialAds;
import com.onecode.ffhx.gfx.lib.modal.Sensi;
import java.util.ArrayList;

public class SensiAdapter extends RecyclerView.Adapter<SensiAdapter.ViewHolder>{
    Context context;
    ArrayList<Sensi> sensiList;
    SensiListBinding binding;
    InterstitialAds interstitialAds;
    int[] drawable = {R.drawable.avatar_1, R.drawable.avatar_2, R.drawable.avatar_3,
            R.drawable.avatar_1, R.drawable.avatar_2, R.drawable.avatar_3};

    public SensiAdapter(Context context, ArrayList<Sensi> sensiList) {
        this.context = context;
        this.sensiList = sensiList;
        interstitialAds = new InterstitialAds(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = SensiListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sensi sensi = sensiList.get(position);

        binding.playerName.setText(sensi.player_name.toUpperCase());
        binding.imageView2.setImageDrawable(context.getResources().getDrawable(drawable[position]));
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interstitialAds.loadAd();
                SensiBottomsheet.Show(context, sensi);
                interstitialAds.showAd();
            }
        });
    }

    @Override
    public int getItemCount() {
        return sensiList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(@NonNull SensiListBinding sensiListBinding) {
            super(sensiListBinding.getRoot());
        }
    }
}
