package de.btu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    private static ChatGPTManager chatGPTManager;
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void main(String[] args) throws IOException {
        chatGPTManager = new ChatGPTManager(Config.API_KEY);
        List<String> questions = TxtFileManager.readQuestionsFromFile();
        for (int i = 0; i < Config.NUMBER_OF_FILLED_OUT_SURVEYS; i++) {
            Map<String, String> filledOutSurvey = querySurveyAnswers(questions);
            TxtFileManager.createFileAndWriteString(gson.toJson(filledOutSurvey), "survey_answers" + i + ".json");
        }
    }

    /**
     * takes a list of questions, sending each of them as a prompt to ChatGPT
     * @param questions list of questions
     * @return Map with questions as keys and their answers as the respective values
     */
    private static Map<String, String> querySurveyAnswers(List<String> questions) {
        Map<String, String> filledOutSurvey = new HashMap<>();
        for (String question : questions) {
            try {
                filledOutSurvey.put(question, chatGPTManager.ask(question));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filledOutSurvey;
    }
}
