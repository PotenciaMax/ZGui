package me.zmaster.zgui.sign.util;

import com.google.gson.*;

public final class ChatComponentUtil {

    private static final Gson GSON = new Gson();
    private static final JsonParser PARSER = new JsonParser();

    public static String toPlainText(String json) {
        if (json == null || json.isEmpty())
            return "";

        try {
            JsonElement element = PARSER.parse(json);
            return extractText(element);
        } catch (Exception e) {
            return json; // fallback se não for JSON
        }
    }

    private static String extractText(JsonElement element) {
        if (element.isJsonPrimitive()) {
            return element.getAsString();
        }

        if (!element.isJsonObject())
            return "";

        JsonObject obj = element.getAsJsonObject();
        StringBuilder sb = new StringBuilder();

        // texto principal
        if (obj.has("text")) {
            sb.append(obj.get("text").getAsString());
        }

        // extra (recursivo)
        if (obj.has("extra")) {
            JsonArray extra = obj.getAsJsonArray("extra");
            for (JsonElement child : extra) {
                sb.append(extractText(child));
            }
        }

        return sb.toString();
    }
}