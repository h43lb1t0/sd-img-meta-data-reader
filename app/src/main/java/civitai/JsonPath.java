package civitai;

public enum JsonPath {

    VERSION(new String[]{"name"}),
    BASE_MODEL(new String[]{"baseModel"}),
    MODEL_ID(new String[]{"modelId"}),
    NAME(new String[]{"model","name"});

    private final String[] path;

    JsonPath (String[] path) {
        this.path = path;
    }

    public String[] getPath() {
        return path;
    }


}
