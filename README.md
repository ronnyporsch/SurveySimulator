# Simulating Surveys with ChatGPT

## Table of contents

- [Introduction](#introduction)
- [How to use](#how-to-use)
  - [Setting up an OpenAI API Key](#setting-up-an-openai-api-key)
  - [Configuration](#configuration)
  - [Troubleshooting](#troubleshooting)
- [How it works internally](#how-it-works-internally)

## Introduction
This guide explains how to use ChatGPT to simulate surveys. You set your own questions and define what kind of participants ChatGPT should impersonate. 

## How to use
1. Download and install [Java](https://www.java.com/en/download/)
2. Download the latest release's Executables.zip of the [SurveySimulator](https://github.com/ronnyporsch/SurveySimulator/releases/latest)
3. Unzip the files (using a tool such as [7-Zip](https://www.7-zip.org/))
4. Put your survey questions into the questions.txt using the same syntax as the example questions
5. [Set up an OpenAI API key](#setting-up-an-openai-api-key) 
6. Put your own OpenAI API key into the [config.txt](#configuration), while making sure that you have sufficient credits on your account
7. Start the program with a double click on the .jar file. Alternatively, open a command line in the executables folder and type "java -jar SurveySimulator.jar" and then confirm by pressing Enter
8. Wait for the program to complete the survey (this can take a few seconds depending on how many questions your survey consists of)
9. You can find the filled out surveys in [JSON format](https://en.wikipedia.org/wiki/JSON) as separate files in the automatically created output folder

### Setting up an OpenAI API Key
1. Create an account on [OpenAI.com](https://platform.openai.com/)
2. Go to the [API Keys page](https://platform.openai.com/api-keys) and click on "Create new secret key"
3. (Optional) Set a name for your key
4. Copy the key that will be generated for you. Save it into a text file for later use
5. Go to the [Billing settings page](https://platform.openai.com/account/billing/overview) and click on "Add to credit balance"
6. Add a payment method and specify an amount to add to your balance (yes, this will cost real money)
7. Click on "Continue"
8. Review your Payment and click on "Confirm Payment"
9. OpenAI might take a few minutes to process your payment. Wait for that to happen 

### Configuration
- The program comes with a config.txt that looks like this:
 ```
API_KEY = PUT_YOUR_API_KEY_HERE
USED_MODEL = gpt-3.5-turbo
PARTICIPANTS = Donald Duck,Obama,a 13 year old boy with a skateboard
```
- Edit this file to configure the program:
  - API_KEY: Your OpenAI API Key
  - USED_MODEL: The model that is being queried (see [here](https://platform.openai.com/docs/models) for an overview of available models) 
  - PARTICIPANTS: participants that should be simulated. Divided by commas. Each set of answers will be saved into a separate file

### Troubleshooting
- **Error:** "'Java'" is not recognized as an internal or external command, operable program or batch file."
  - **Solution:** Make sure you have Java installed
- **Error:** "Can't read config file" or "FileNotFoundException"
  - **Solution:** Make sure that your .jar File as well as both the config.properties and the questions.txt files are in the same directory
- **Error:** "java.io.IOException: server response code 401"
  - **Solution:** Make sure that the config.properties file contains a correct [OpenAI API key](#setting-up-an-openai-api-key)

## How it works internally
- The program reads your survey questions from the questions.txt
- Using your api key from the config.txt, each question will be sent to ChatGPT like this:
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
- When participants are declared in the config file, each prompt will be asked like this: ``"pretend that you are " + participant + " for next question:" + prompt;``
- ChatGPT will answer with a structure as seen in the ChatGPTAnswer class. We use the Gson library to create an instance of that class for every response that we get
- The important part of the answer for us is to be found in the message object. We retrieve it like this:
```
public String getMessage() {
        return this.choices.get(0).message.content;
  }
```

- We loop through all our questions, sending each as a separate prompt to ChatGPT, creating pairs of questions and their respective answers:
```
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
```
- Finally, we write our questions/answer pairs into a file, creating a new file for each participant of the survey:
```
    public static void createFileAndWriteString(String text, String outputFileName) throws IOException {
        File file = new File(Utility.getDirectoryOfExecutable()
                + File.separator + "output" + File.separator + outputFileName);
        file.getParentFile().mkdir();
        if (file.createNewFile())
            System.out.println("file " + file.getName() + " created!");
        java.nio.file.Files.write(Paths.get(file.getPath()), text.getBytes());
        System.out.println("your file can be found here: " + file.getPath());
    }
```