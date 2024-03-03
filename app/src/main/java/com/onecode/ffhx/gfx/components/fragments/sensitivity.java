package com.onecode.ffhx.gfx.components.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.onecode.ffhx.gfx.R;
import com.onecode.ffhx.gfx.databinding.FragmentSensitivityBinding;
import com.onecode.ffhx.gfx.lib.adapters.SensiAdapter;
import com.onecode.ffhx.gfx.lib.modal.Sensi;
import com.onecode.ffhx.gfx.lib.utils.PlayersSensitivities;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class sensitivity extends Fragment {
    FragmentSensitivityBinding binding;
    SensiAdapter sensiAdapter;
    ArrayList<Sensi> sensiList = new ArrayList<>();

    public sensitivity() {
        // Required empty public constructor
    }

    public static sensitivity newInstance(String param1, String param2) {
        sensitivity fragment = new sensitivity();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSensitivityBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Thread(new Runnable() {
            @Override
            public void run() {
                setSensitivity();
            }
        }).start();

    }

    void setSensitivity() {
        try {
            JSONArray jsonArray = new JSONArray(PlayersSensitivities.playerSensi);
            for (int i = 0; i < jsonArray.length(); i++) {
                sensiList.add(new Sensi(
                        jsonArray.getJSONObject(i).getString("player_name"),
                        jsonArray.getJSONObject(i).getInt("general"),
                        jsonArray.getJSONObject(i).getInt("red_dot"),
                        jsonArray.getJSONObject(i).getInt("x2_scope"),
                        jsonArray.getJSONObject(i).getInt("x4_scope"),
                        jsonArray.getJSONObject(i).getInt("AWM_scope")
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ((Activity)getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.sensiList.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
                sensiAdapter = new SensiAdapter(getContext(), sensiList);
                sensiAdapter.setHasStableIds(false);
                binding.sensiList.setAdapter(sensiAdapter);
            }
        });
    }
}