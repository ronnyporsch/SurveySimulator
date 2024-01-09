package de.btu;

import lombok.Data;

public class SurveyResult {
    private final String question;
    private final String answer;
    private final String participant;

    public SurveyResult(String question, String answer, String participant) {
        this.question = question;
        this.answer = answer;
        if (participant == null) {
            this.participant = "ChatGPT";
        } else {
            this.participant = participant;
        }
    }
}
