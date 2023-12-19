package imgMetaData;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;

import civitai.CivitaiJsonReader;
import helpers.SharedViewModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.lifecycle.ViewModelProvider;
import android.app.Activity;


import imgMetaData.Parameter;

public class readImgMetaData {

    File imageFile;
    static List<? extends ImageMetadata.ImageMetadataItem> metadata;


    public readImgMetaData(Context context, Uri imageUri) {
        imageFile = new File(Objects.requireNonNull(FileUtil.getPath(context, imageUri)));
        try {
            final ImageInfo imageInfo = Imaging.getImageInfo(imageFile);
            metadata = Imaging.getMetadata(imageFile).getItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void getParams(ParamsCallback callback) {
        StringBuilder params = new StringBuilder();

        String unformattedParams = metadata.get(0).toString();
        String oneLineParams = unformattedParams.replaceAll("\\R", " ");

        String[] civitaiRelevantParams = new String[] {
                Parameter.BASE_MODEL_HASH.name()
        };

        final ImageMetaData[] imageMetaData = {ImageMetaData.getInstance()};

        AtomicInteger asyncTasksCount = new AtomicInteger(0);
        AtomicBoolean isAsyncOperationInvoked = new AtomicBoolean(false);

        for (Parameter p : Parameter.values()) {
            Pattern pattern = p.getRegexPattern();
            Matcher matcher = pattern.matcher(oneLineParams);

            if (matcher.find() && matcher.groupCount() >= 1) {

                if (Arrays.asList(civitaiRelevantParams).contains(p.name())) {
                    isAsyncOperationInvoked.set(true);
                    asyncTasksCount.incrementAndGet();

                    CivitaiJsonReader civitaiJsonReader = new CivitaiJsonReader();
                    getModelByHash(matcher.group(1), new ModelByHashCallback() {
                        @Override
                        public void onResult(String result) {
                            imageMetaData[0] = civitaiJsonReader.getParams(result, imageMetaData[0]);

                            // Check if all asynchronous tasks are completed
                            if (asyncTasksCount.decrementAndGet() == 0) {
                                callback.onCompleted(imageMetaData[0].toString());
                            }
                        }
                    });
                } else {
                    switch (p) {
                        case POS_PROMPT:
                            imageMetaData[0].setPosPrompt(matcher.group(1));
                            break;
                        case NEG_PROMPT:
                            imageMetaData[0].setNegPrompt(matcher.group(1));
                            break;
                        case STEPS:
                            imageMetaData[0].setSteps(Integer.parseInt(Objects.requireNonNull(matcher.group(1))));
                            break;
                        case SAMPLER:
                            imageMetaData[0].setSampler(matcher.group(1));
                            break;
                        case CFG:
                            imageMetaData[0].setCfg(Integer.parseInt(Objects.requireNonNull(matcher.group(1))));
                            break;
                        case SEED:
                            imageMetaData[0].setSeed(Long.parseLong(Objects.requireNonNull(matcher.group(1))));
                            break;
                        case SIZE:
                            imageMetaData[0].setSize(matcher.group(1));
                            break;
                        case VAE_HASH:
                            imageMetaData[0].setVaeHash(matcher.group(1));
                            break;
                        case UI_VERSION:
                            imageMetaData[0].setUiVersion(matcher.group(1));
                            break;

                    };
                }

                if (!isAsyncOperationInvoked.get()) {
                    callback.onCompleted(params.toString());
                }

                Log.d("PARAMS", "getParams: " + p.name() + ": " + matcher.group(1));
                //params.append(p.name()).append(": ").append(matcher.group(1)).append("\n\n\n");
            }
        }
    }

    public static void getParams2(Activity activity) {
        SharedViewModel sharedViewModel = new ViewModelProvider((ViewModelStoreOwner) activity).get(SharedViewModel.class);

        getParams(new ParamsCallback() {
            @Override
            public void onCompleted(String params) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("DATA", "run: " + params);
                    }
                });
            }
        });
    }



    public String getPosPrompt() {
        if (metadata == null) {
            return null;
        } else {
            return metadata.get(0).toString();
        }
    }

    public List<? extends ImageMetadata.ImageMetadataItem> getMetaData() {
        if (metadata == null) {
            return null;
        } else {
            return metadata;
        }
    }

    private static void getModelByHash(String hash, ModelByHashCallback callback) {
        Log.d("HASH", "getModelByHash: " + hash);
        new Thread(() -> {
            try {
                OkHttpClient client = new OkHttpClient();
                final String url = "https://civitai.com/api/v1/model-versions/by-hash/";
                Request request = new Request.Builder()
                        .url(url + hash)
                        .build();

                try (Response response = client.newCall(request).execute()) {
                    if (response.body() != null) {
                        String result = response.body().string();
                        callback.onResult(result);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}

