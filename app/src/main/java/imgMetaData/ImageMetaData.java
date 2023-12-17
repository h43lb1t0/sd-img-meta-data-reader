package imgMetaData;

public class ImageMetaData {
    private String posPrompt;
    private String negPrompt;
    private int steps;
    private String sampler;
    private int cfg;
    private long seed;
    private String size;
    private String baseModelHash;
    private String baseModelName;
    private String vaeHash;
    private String vaeName;
    private String[] usedScripts;
    //private map<String, map<String, String>> scriptParams;
    private String[] loraHashes;
    private String[] loraNames;
    private String uiVersion;
}
