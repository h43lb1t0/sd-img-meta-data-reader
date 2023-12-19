package com.haelbito.sdmetadatareader;


import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import helpers.SharedViewModel;
import helpers.TabsPagerAdapter;
import imgMetaData.ImageMetaData;
import imgMetaData.ParamsCallback;
import imgMetaData.readImgMetaData;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private SharedViewModel sharedViewModel;

    private ImageView imageView;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        // Initialize ViewPager and TabLayout here
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        viewPager.setAdapter(new TabsPagerAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    // Set the tab titles here
                    switch (position) {
                        case 0:
                            tab.setText(R.string.gen_data);
                            break;
                        case 1:
                            tab.setText(R.string.model_info);
                            break;
                        case 2:
                            tab.setText(R.string.lora_info);
                            break;
                    }
                }).attach();


        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent getIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(getIntent, PICK_IMAGE);
            }
        });
    }
    
    
    private void requestPermissions() {
        // Register ActivityResult handler
        ActivityResultLauncher<String[]> requestPermissions = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), results -> {
            // Handle permission requests results
            // See the permission example in the Android platform samples: https://github.com/android/platform-samples
        });

        // Permission request logic
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    requestPermissions.launch(new String[]{READ_MEDIA_IMAGES, READ_MEDIA_VISUAL_USER_SELECTED});
                } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
                    requestPermissions.launch(new String[]{READ_MEDIA_IMAGES});
                } else {
                    requestPermissions.launch(new String[]{READ_EXTERNAL_STORAGE});
                }

            }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);

            // Handle image metadata
            handleImageMetadata(imageUri);
        }
    }

    @SuppressLint("SetTextI18n")
    private void handleImageMetadata(Uri imageUri) {
        // Creating an instance of readImgMetaData
        readImgMetaData imgMetaDataReader = new readImgMetaData(this, imageUri);


        readImgMetaData.getParams2(this);
        sharedViewModel.setData("");
        ImageMetaData imageMetaData = ImageMetaData.getInstance();
        Log.d("DATA", imageMetaData.toString());

    }



}
