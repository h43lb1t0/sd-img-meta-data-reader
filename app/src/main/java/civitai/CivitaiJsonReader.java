package civitai;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

import imgMetaData.ImageMetaData;

public class CivitaiJsonReader {

    public ImageMetaData getParams(String jsonAsString, ImageMetaData imgMetaData) {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<String> params = new ArrayList<>();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonAsString);
            for (JsonPath pathEnum : JsonPath.values()) {
                switch (pathEnum) {
                    case VERSION:
                        imgMetaData.setModelVersion(getValue(jsonNode, pathEnum));
                        break;
                    case BASE_MODEL:
                        imgMetaData.setBaseModelSDVersion(getValue(jsonNode, pathEnum));
                        break;
                    case MODEL_ID:
                        imgMetaData.setModelID(getValue(jsonNode, pathEnum));
                        break;
                    case NAME:
                        imgMetaData.setBaseModelName(getValue(jsonNode, pathEnum));
                        break;
                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return imgMetaData;
    }

    private static String getValue(JsonNode jsonNode, JsonPath pathEnum) {
        JsonNode currentNode = jsonNode;
        for (String path : pathEnum.getPath()) {
            currentNode = currentNode.get(path);
            if (currentNode == null) {
                // Handle error or return a default value as needed
                return "";
            }
        }
        return currentNode.asText();
    }
}
