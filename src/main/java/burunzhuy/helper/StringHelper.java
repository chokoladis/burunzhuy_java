package burunzhuy.helper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Map;
import java.util.TreeMap;

public class StringHelper {

    public static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public static JsonNode sortNode(JsonNode node, ObjectMapper mapper) {
        if (node.isObject()) {
            Map<String, JsonNode> sortedMap = new TreeMap<>();
            node.fields().forEachRemaining(entry ->
                    sortedMap.put(entry.getKey(), sortNode(entry.getValue(), mapper)));
            ObjectNode result = mapper.createObjectNode();
            sortedMap.forEach(result::set);
            return result;
        }
        return node;
    }

    public static ObjectMapper getJsonReader() {
        return new ObjectMapper()
                .configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true)
                .configure(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN, true)
                .configure(JsonWriteFeature.ESCAPE_FORWARD_SLASHES.mappedFeature(), false);
    }
}
