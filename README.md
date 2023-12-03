# Simulating Surveys with ChatGPT
## How to use
1. Install [Java](https://openjdk.org/)
2. Add Java to the environment variables ([Tutorial for Windows](https://www.onlinetutorialspoint.com/java8/java-8-how-to-set-java_home-on-windows10.html))
3. Download the latest release of the [SurveySimulator](https://github.com/ronnyporsch/SurveySimulator/releases/latest)
4. Unzip the files (using a tool such as [7-Zip](https://www.7-zip.org/))
5. Put your survey questions into the questions.txt using the same syntax as the example questions
6. Put your own OpenAI API key into the config.properties, while making sure that you have sufficient credits on your account
7. Open a command line in the directory of the downloaded files and start the program using the following command: ``java -jar .\SurveySimulator.jar``
8. You can find the filled out surveys as separate files in the output folder

## Config
- Use the config.properties file to configure the program:
  - API_KEY: Your OpenAI API Key
  - USED_MODEL: The model that is being queried (see [here](https://platform.openai.com/docs/models) for an overview of available models)
  - NUMBER_OF_FILLED_OUT_SURVEYS: The number of times ChatGPT is supposed to answer the survey. Each set of answers will be saved into a separate JSON file

## How it works internally
- The program reads your survey questions from the questions.txt
- Using your api key from the config.properties, each question will be sent to ChatGPT like this:
```
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
```
- ChatGPT will answer with a structure as seen in the ChatGPTAnswer class. We use the Gson library to create an instance of that class for every response that we get
- The important part of the answer for us is to be found in the message object. We retrieve it like this:
```
public String getMessage() {
        return this.choices.get(0).message.content;
  }
```

- We loop through all our questions, sending each as a separate prompt to ChatGPT, creating pairs of questions and their respective answers:
```
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
```
- Finally, we write our questions/answer pairs into a file, creating a new file for each iteration of the survey:
```
    public static void createFileAndWriteString(String text, String outputFileName) throws IOException {
        File file = new File(Utility.getProjectRoot() 
                + File.separator + "output" + File.separator + outputFileName);
        file.getParentFile().mkdir();
        if (file.createNewFile())
            System.out.println("file " + file.getName() + " created!");
        java.nio.file.Files.writeString(Path.of(file.getPath()), text);
        System.out.println("your file can be found here: " + file.getPath());
    }
```

## Troubleshooting
- **Error:** "'Java'" is not recognized as an internal or external command, operable program or batch file."
  - **Solution:** Make sure you have Java installed and added it to the environment variables
- **Error:** "Can't read config file" or "FileNotFoundException"
  - **Solution:** Make sure that your .jar File as well as both the config.properties and the questions.txt files are in the same directory   

## Possible future improvements
- currently errors from ChatGPT are not being handled (for example when calling the api with insufficient credits)