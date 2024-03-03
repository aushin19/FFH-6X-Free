package com.onecode.ffhx.gfx.lib.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.onecode.ffhx.gfx.components.fragments.dashboard;
import com.onecode.ffhx.gfx.components.fragments.sensitivity;
import com.onecode.ffhx.gfx.components.fragments.stats;

public class MainPageViewAdapter extends FragmentStatePagerAdapter {
    public MainPageViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new dashboard();
            case 1:
                return new sensitivity();
            /*case 2:
                return new SecondaryGFX();
           case 3:
                return new Stats();*/
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
