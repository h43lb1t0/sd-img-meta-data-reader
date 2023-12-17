package imgMetaData;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import imgMetaData.Parameter;

public class readImgMetaData {

    File imageFile;
    List<? extends ImageMetadata.ImageMetadataItem> metadata;


    public readImgMetaData(Context context, Uri imageUri) {
        imageFile = new File(Objects.requireNonNull(FileUtil.getPath(context, imageUri)));
        try {
            final ImageInfo imageInfo = Imaging.getImageInfo(imageFile);
            metadata = Imaging.getMetadata(imageFile).getItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getParams(ParamsCallback callback) {
        StringBuilder params = new StringBuilder();

        String unformattedParams = metadata.get(0).toString();
        String oneLineParams = unformattedParams.replaceAll("\\R", " ");


        AtomicInteger asyncTasksCount = new AtomicInteger(0);
        AtomicBoolean isAsyncOperationInvoked = new AtomicBoolean(false);

        for (Parameter p : Parameter.values()) {
            Pattern pattern = p.getRegexPattern();
            Matcher matcher = pattern.matcher(oneLineParams);

            if (matcher.find() && matcher.groupCount() >= 1) {

                if (p.name().equals("BASE_MODEL_HASH")) {
                    isAsyncOperationInvoked.set(true);
                    asyncTasksCount.incrementAndGet();
                    getModelByHash(matcher.group(1), new ModelByHashCallback() {
                        @Override
                        public void onResult(String result) {
                            params.append(result);
                            // Check if all asynchronous tasks are completed
                            if (asyncTasksCount.decrementAndGet() == 0) {
                                callback.onCompleted(params.toString());
                            }
                        }
                    });
                } else {
                    params.append(p.name()).append(": ").append(matcher.group(1)).append("\n");
                }

                if (!isAsyncOperationInvoked.get()) {
                    callback.onCompleted(params.toString());
                }

                Log.d("PARAMS", "getParams: " + p.name() + ": " + matcher.group(1));
                //params.append(p.name()).append(": ").append(matcher.group(1)).append("\n\n\n");
            }
        }
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

    private void getModelByHash(String hash, ModelByHashCallback callback) {
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

