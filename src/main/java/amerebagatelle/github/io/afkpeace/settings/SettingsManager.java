package amerebagatelle.github.io.afkpeace.settings;

import java.util.Properties;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SettingsManager {
    // * Settings File. DO NOT CHANGE UNLESS YOU KNOW WHAT YOU ARE DOING
    public static final File settingsFilePath = new File("afkpeace.properties");

    // * Reconnection
    public static int maxReconnectTries;
    public static int secondsBetweenReconnectionAttempts;

    // * Active toggles
    public static boolean isReconnectOnTimeoutActive = false;
    public static boolean isDamageProtectActive = false;

    public static void initSettings() {
        if(!settingsFilePath.exists()) {
            try {
                settingsFilePath.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("Can't create a properties file for AFKPeace!");
            }
        }
    }

    public static void loadSettings() {
        BufferedReader inputStream;

        try {
            Properties prop = new Properties();

            inputStream = new BufferedReader(new FileReader(settingsFilePath));
            prop.load(inputStream);
            inputStream.close();

            maxReconnectTries = Integer.parseInt(prop.getProperty("maxReconnectTries", "3"));
            secondsBetweenReconnectionAttempts = Integer.parseInt(prop.getProperty("secondsBetweenReconnectionAttempts", "10"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeSetting(String setting, String setpoint) throws IOException {
        BufferedReader inputStream;
        BufferedWriter outputStream;
        Properties prop = new Properties();

        System.out.println(settingsFilePath.getAbsolutePath());

        inputStream = new BufferedReader(new FileReader(settingsFilePath));
        prop.load(inputStream);
        inputStream.close();

        outputStream = new BufferedWriter(new FileWriter(settingsFilePath));
        prop.setProperty(setting, setpoint);
        prop.store(outputStream, null);
        outputStream.flush();
        outputStream.close();

        loadSettings();
    }
}