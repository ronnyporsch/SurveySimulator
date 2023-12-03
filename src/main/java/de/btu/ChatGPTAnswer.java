package de.btu;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@ToString
public class ChatGPTAnswer {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<Choice> choices;
    private Usage usage;
    private String system_fingerprint;

    public String getMessage() {
        return this.choices.get(0).message.content;
    }


    @AllArgsConstructor
    @ToString
    public static class Choice {
        private int index;
        private Message message;
        private String finish_reason;
    }

    @AllArgsConstructor
    @ToString
    public static class Message {
        private String role;
        private String content;
    }

    @AllArgsConstructor
    @ToString
    public static class Usage {
        private int prompt_tokens;
        private int completion_tokens;
        private int total_tokens;
    }
}