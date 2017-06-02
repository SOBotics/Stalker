package in.bhargavrao.stalker.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * AKA, TunaLib - All code is courtesy of Lord Tunaki
 */
public class JsonUtils {
    public static JsonObject get(String url, String... data) throws IOException {
        Connection.Response response = Jsoup.connect(url).data(data).method(Connection.Method.GET).ignoreContentType(true).ignoreHttpErrors(true).execute();
        String json = response.body();
        if (response.statusCode() != 200) {
            throw new IOException("HTTP " + response.statusCode() + " fetching URL " + (url) + ". Body is: " + response.body());
        }
        JsonObject root = new JsonParser().parse(json).getAsJsonObject();
        return root;
    }
    public static void handleBackoff(JsonObject root) {
        if (root.has("backoff")) {
            int backoff = root.get("backoff").getAsInt();
            System.out.println("Backing off for " + backoff+ " seconds. Quota left "+root.get("quota_remaining").getAsString());
            try {
                Thread.sleep(1000 * backoff);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
