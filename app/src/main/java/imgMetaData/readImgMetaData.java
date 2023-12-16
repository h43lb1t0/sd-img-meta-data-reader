package imgMetaData;

import android.content.Context;
import android.net.Uri;

import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;

import java.io.File;
import java.util.List;
import java.util.Objects;

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

    public List<? extends ImageMetadata.ImageMetadataItem> getMetaData() {
        if (metadata == null) {
            return null;
        } else {
            return metadata;
        }
    }
}
