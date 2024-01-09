package de.btu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.*;

public class Main {

    private static ChatGPTManager chatGPTManager;
    private static final String[] participants = Config.PARTICIPANTS;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) throws IOException {
        chatGPTManager = new ChatGPTManager(Config.API_KEY);
        List<String> questions = TxtFileManager.readQuestionsFromFile();
        for (String participant : participants) {
            List<SurveyResult> filledOutSurvey = querySurveyAnswers(questions, participant);
            TxtFileManager.createFileAndWriteString(gson.toJson(filledOutSurvey), "survey_answers_" + participant + ".txt");
        }
    }

    /**
     * takes a list of questions, sending each of them as a prompt to ChatGPT
     * @param questions list of questions
     * @param participant the participant that is simulated to ask the question
     * @return list of SurveyResults
     */
    private static List<SurveyResult> querySurveyAnswers(List<String> questions, String participant) {
        List<SurveyResult> filledOutSurvey = new LinkedList<>();
        for (String question : questions) {
            try {
                filledOutSurvey.add(new SurveyResult(question, chatGPTManager.ask(question, participant), participant));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filledOutSurvey;
    }
}
