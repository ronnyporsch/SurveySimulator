package de.btu;

public class Config {
    public static final String API_ENDPOINT = "https://api.openai.com/v1/chat/completions";
    public static final String API_KEY = ConfigFileReader.getConfig().getString("API_KEY");
    public static final String USED_MODEL = ConfigFileReader.getConfig().getString("USED_MODEL");
    public static final String[] PARTICIPANTS = ConfigFileReader.getConfig().getString("PARTICIPANTS").split(",");
}
