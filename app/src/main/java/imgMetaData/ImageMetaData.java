package imgMetaData;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

import com.haelbito.sdmetadatareader.R;

public class ImageMetaData {

    private static ImageMetaData INSTANCE;

    public static ImageMetaData getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ImageMetaData();
        }

        return INSTANCE;
    }

    /**
     * The positiv prompt for the image
     */
    private String posPrompt;
    /**
     * The negative prompt for the image
     */
    private String negPrompt;
    /**
     * The number of steps for the image
     */
    private int steps;
    /**
     * The Sampler used for the image
     */
    private String sampler;
    /**
     * The cfg scale for the image
     */
    private int cfg;
    /**
     * The seed for the image
     */
    private long seed;
    /**
     * The dimensions of the image
     */
    private String size;
    /**
     * The name of the checkpoint used for the image
     */
    private String baseModelName;
    /**
     * The civitai.com ID of the base model
     */
    private String modelID;
    /**
     * The civitai.com version of the base model
     */

    private final String CIVITAI_URL = "https://civitai.com/models/";
    private String modelVersion;
    /**
     * The SD version of the base model
     * eg. SDXL, SD1.5, SD2.1
     */
    private String baseModelSDVersion;
    /**
     * The Hash of the VAE used for the image
     */
    private String vaeHash;
    /**
     * The version of A1111 used for the generation
     */
    private String uiVersion;

    public String getPosPrompt() {
        return posPrompt;
    }

    public void setPosPrompt(String posPrompt) {
        this.posPrompt = posPrompt;
    }

    public String getNegPrompt() {
        return negPrompt;
    }

    public void setNegPrompt(String negPrompt) {
        this.negPrompt = negPrompt;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public String getSampler() {
        return sampler;
    }

    public void setSampler(String sampler) {
        this.sampler = sampler;
    }

    public int getCfg() {
        return cfg;
    }

    public void setCfg(int cfg) {
        this.cfg = cfg;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBaseModelName() {
        return baseModelName;
    }

    public void setBaseModelName(String baseModelName) {
        this.baseModelName = baseModelName;
    }

    public String getModelID() {
        return modelID;
    }

    public void setModelID(String modelID) {
        this.modelID = this.CIVITAI_URL + modelID;
    }

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public String getBaseModelSDVersion() {
        return baseModelSDVersion;
    }

    public void setBaseModelSDVersion(String baseModelSDVersion) {
        this.baseModelSDVersion = baseModelSDVersion;
    }

    public String getVaeHash() {
        return vaeHash;
    }

    public void setVaeHash(String vaeHash) {
        this.vaeHash = vaeHash;
    }

    public String getUiVersion() {
        return uiVersion;
    }

    public void setUiVersion(String uiVersion) {
        this.uiVersion = uiVersion;
    }

    @NonNull
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("posPrompt: ").append(posPrompt).append("\n");
        sb.append("negPrompt: ").append(negPrompt).append("\n");
        sb.append("steps: ").append(steps).append("\n");
        sb.append("sampler: ").append(sampler).append("\n");
        sb.append("cfg: ").append(cfg).append("\n");
        sb.append("seed: ").append(seed).append("\n");
        sb.append("size: ").append(size).append("\n");
        sb.append("baseModelName: ").append(baseModelName).append("\n");
        sb.append("modelID: ").append(modelID).append("\n");
        sb.append("modelVersion: ").append(modelVersion).append("\n");
        sb.append("baseModelSDVersion: ").append(baseModelSDVersion).append("\n");
        sb.append("vaeHash: ").append(vaeHash).append("\n");
        sb.append("uiVersion: ").append(uiVersion).append("\n");
        return sb.toString();
    }

    public String get(Context context, String attribute){
        if (context.getString(R.string.pos_prompt).equals(attribute)) {
            return posPrompt;
        } else if (context.getString(R.string.neg_prompt).equals(attribute)) {
            return negPrompt;
        } else if (context.getString(R.string.steps).equals(attribute)) {
            return String.valueOf(steps);
        } else if (context.getString(R.string.sampler).equals(attribute)) {
            return sampler;
        } else if (context.getString(R.string.cfg).equals(attribute)) {
            return String.valueOf(cfg);
        } else if (context.getString(R.string.seed).equals(attribute)) {
            return String.valueOf(seed);
        } else if (context.getString(R.string.size).equals(attribute)) {
            return size;
        } else if (context.getString(R.string.base_model_name).equals(attribute)) {
            return baseModelName;
        } else if (context.getString(R.string.base_model_version).equals(attribute)) {
            return modelVersion;
        } else if (context.getString(R.string.base_model_sd_version).equals(attribute)) {
            return baseModelSDVersion;
        } else if (context.getString(R.string.base_model_id).equals(attribute)) {
            return modelID;
        } else {
            return "";
        }
    }
}
