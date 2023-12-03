package de.btu;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;

/**
 * this class is used to send prompts to ChatGPT
 */
public class ChatGPTManager {
    private final String apiKey;
    private final Gson gson = new Gson();
    public ChatGPTManager(String apiKey) {
        this.apiKey = apiKey;
    }
    public String ask(String prompt) throws IOException {
        System.out.println("asking chatGPT: " + prompt);
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        String bodyString = "{\"model\": \"" + Config.USED_MODEL + "\", \"messages\":" +
                " [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}";
        RequestBody body = RequestBody.create(bodyString, mediaType);

        Request request = new Request.Builder()
                .url(Config.API_ENDPOINT)
                .post(body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .build();

        Response response = client.newCall(request).execute();
        System.out.println(response);
        if (response.code() != 200) throw new IOException("server response code " + response.code());
        return gson.fromJson(response.body().string(), ChatGPTAnswer.class).getMessage();
    }
}
