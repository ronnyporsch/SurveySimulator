package de.btu;

import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * this class is used to send prompts to ChatGPT
 */
public class ChatGPTManager {
    private final String apiKey;
    private final Gson gson = new Gson();
        private final OkHttpClient client = new OkHttpClient.Builder().readTimeout(10, TimeUnit.SECONDS).build();

    public ChatGPTManager(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * asks ChatGPT to pretend to be a given participant for a question
     *
     * @param prompt      the prompt to be asked
     * @param participant the simulated participant
     */
    public String ask(String prompt, String participant) throws IOException {
        if (participant == null || participant.isEmpty()) return ask(prompt);
        return ask("pretend that you are " + participant + " for next question:" + prompt);
    }

    public String ask(String prompt) throws IOException {
        System.out.println("asking chatGPT: " + prompt);
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
