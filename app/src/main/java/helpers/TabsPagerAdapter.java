package helpers;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.haelbito.sdmetadatareader.gen_data;
import com.haelbito.sdmetadatareader.model_info;

public class TabsPagerAdapter extends FragmentStateAdapter {

    public TabsPagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                Log.d("FRAGMENT", "createFragment: " + position);
                return new gen_data();
            case 1:
                Log.d("FRAGMENT", "createFragment: " + position);
                return new model_info();
            // add more cases for additional tabs
        }
        Log.d("FRAGMENT", "createFragment: default");
        return new Fragment(); // default
    }

    @Override
    public int getItemCount() {
        return 3; // number of tabs
    }
}
