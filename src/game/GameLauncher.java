package game;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class GameLauncher extends JFrame {

    public GameLauncher() {
        initUI();
    }

    private void initUI() {
        setTitle("Breakout Launcher");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 300); // Увеличенный размер окна
        setLocationRelativeTo(null);
        setResizable(false);

        // Основная панель с чёрным фоном
        var panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Отступы между кнопками
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // Добавляем кнопки с одинаковым текстом и разными параметрами уровня
        var button1 = createButtonWithImage("Играть", 1, "src/resources/image1.png");
        var button2 = createButtonWithImage("Играть", 2, "src/resources/image2.png");
        var button3 = createButtonWithImage("Играть", 3, "src/resources/image3.png");

        // Добавляем кнопки "играть" в ряд
        panel.add(button1, gbc);
        panel.add(button2, gbc);
        panel.add(button3, gbc);

        gbc.gridy = 1; // Вторая строка для кнопки "таблица лидеров"
        gbc.gridx = 0;
        gbc.gridwidth = 3; // Растягиваем кнопку на ширину
        var leaderboardButton = createStyledButton("Таблица лидеров");
        leaderboardButton.addActionListener(e -> showLeaderboard());
        panel.add(leaderboardButton, gbc);

        add(panel);
        pack();
    }


    private JButton createButtonWithImage(String buttonText, int level, String imagePath) {
        JButton button = new JButton(buttonText);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);

        // Устанавливаем изображение
        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH); // Масштабируем изображение
        button.setIcon(new ImageIcon(scaledImage));

        button.setBackground(Color.LIGHT_GRAY);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));

        // Устанавливаем параметр уровня через putClientProperty
        button.putClientProperty("level", level);

        // Назначаем действия кнопке
        button.addActionListener(e -> {
            int blockPositionType = (int) button.getClientProperty("level");
            startGame(blockPositionType);
        });

        return button;
    }


    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.LIGHT_GRAY);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }

    private void startGame(int blockPositionType) {
        EventQueue.invokeLater(() -> {
            var board = new Board(blockPositionType);
            var game = new Breakout(board);
            game.setVisible(true);
        });
        dispose(); // Закрываем лаунчер
    }

    private void showLeaderboard() {
        Leaderboard leaderboard = new Leaderboard();
        Map<String, String> allScores = leaderboard.getAllScores();

        // Формируем текст таблицы лидеров
        StringBuilder leaderboardText = new StringBuilder("Таблица лидеров:\n\n");
        allScores.forEach((player, score) ->
                leaderboardText.append(player).append(": ").append(score).append("\n")
        );

        // Создаём текстовое поле с кнопкой для скачивания
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.BLACK);

        JTextArea textArea = new JTextArea(leaderboardText.toString());
        textArea.setEditable(false);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.LIGHT_GRAY);
        textArea.setFont(new Font("Courier New", Font.PLAIN, 14));
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        JButton downloadButton = createStyledButton("Скачать");

        downloadButton.addActionListener(e -> downloadLeaderboard());

        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(downloadButton, BorderLayout.SOUTH);

        // Показ окна таблицы лидеров с увеличенным размером
        JFrame leaderboardFrame = new JFrame("Таблица лидеров");
        leaderboardFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        leaderboardFrame.setSize(500, 400); // Увеличенный размер окна таблицы
        leaderboardFrame.setLocationRelativeTo(this);
        leaderboardFrame.add(panel);
        leaderboardFrame.setVisible(true);
    }

    private void downloadLeaderboard() {
        String sourceFilePath = "src/resources/leaders.properties";

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Сохранить таблицу лидеров");
        fileChooser.setSelectedFile(new java.io.File("leaders.txt"));

        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            var fileToSave = fileChooser.getSelectedFile();

            try (FileInputStream inputStream = new FileInputStream(sourceFilePath);
                 FileOutputStream outputStream = new FileOutputStream(fileToSave)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                JOptionPane.showMessageDialog(
                        this,
                        "Файл успешно сохранён: " + fileToSave.getAbsolutePath(),
                        "Успех",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } catch (IOException e) {
                JOptionPane.showMessageDialog(
                        this,
                        "Ошибка при сохранении файла.",
                        "Ошибка",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            var launcher = new GameLauncher();
            launcher.setVisible(true);
        });
    }
}
