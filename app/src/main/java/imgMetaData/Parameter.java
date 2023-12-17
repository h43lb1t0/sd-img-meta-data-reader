package imgMetaData;


import java.util.regex.Pattern;

public enum Parameter {
    POS_PROMPT("parameters: (.*?) Negative prompt:"),
    NEG_PROMPT("Negative prompt: (.*?) Steps:"),
    STEPS("Steps: (\\d+),"),
    SAMPLER("Sampler: ([^,]+),"),
    CFG("CFG scale: (\\d+),"),
    SEED("Seed: (\\d+),"),
    SIZE("Size: (\\d+x\\d+),"),
    BASE_MODEL_HASH("Model hash: ([A-Za-z0-9]+),"),
    VAE_HASH("VAE hash: ([A-Za-z0-9]+),"),
    LORA_HASHES("Lora hashes: \"[^:]+: ([a-fA-F0-9]+)\","),
    UI_VERSION("Version: v(\\d+\\.\\d+\\.\\d+)");

    private final String regex;


    Parameter(String regex) {
        this.regex = regex;
    }

    public Pattern getRegexPattern() {
        return  Pattern.compile(regex);
    }
}
