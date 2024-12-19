package game;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Leaderboard {
    private Properties properties;
    private final String filePath = "src/resources/leaders.properties";

    public Leaderboard() {
        properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        try (FileInputStream input = new FileInputStream(filePath)) {
            properties.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getScore(String player) {
        return properties.getProperty(player, "0"); // Возвращаем 0, если игрока нет
    }

    public void setScore(String player) {
        // Получаем текущий счет игрока
        String currentScore = properties.getProperty(player);

        if (currentScore != null) {
            // Если игрок существует, увеличиваем его счет на 1
            int newScore = Integer.parseInt(currentScore) + 1;
            properties.setProperty(player, String.valueOf(newScore));
        } else {
            // Если игрока нет, добавляем его с начальным значением 1
            properties.setProperty(player, "1");
        }

        saveProperties(); // Сохраняем изменения
    }

    // Новый метод для получения всех игроков и их счетов
    public Map<String, String> getAllScores() {
        Map<String, String> scores = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            scores.put(key, properties.getProperty(key));
        }
        return scores;
    }

    private void saveProperties() {
        try (FileOutputStream output = new FileOutputStream(filePath)) {
            properties.store(output, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}