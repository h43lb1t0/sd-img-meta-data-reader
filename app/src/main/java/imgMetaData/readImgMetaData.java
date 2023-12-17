package imgMetaData;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;

import java.io.File;
import java.util.List;
import java.util.Objects;

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

    public String getParams() {
        StringBuilder params = new StringBuilder();

        if (metadata == null || metadata.isEmpty()) {
            return "";  // Return an empty string instead of null
        }

        String unformattedParams = metadata.get(0).toString();
        String oneLineParams = unformattedParams.replaceAll("\\R", " ");

        if (oneLineParams.isEmpty()) {
            return "";  // Again, return an empty string if there's no data
        }

        for (Parameter p : Parameter.values()) {
            Pattern pattern = p.getRegexPattern();
            Matcher matcher = pattern.matcher(oneLineParams);

            if (matcher.find() && matcher.groupCount() >= 1) {
                Log.d("PARAMS", "getParams: " + p.name() + ": " + matcher.group(1));
                params.append(p.name()).append(": ").append(matcher.group(1)).append("\n\n\n");
            }
        }

        return params.toString();
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
}
