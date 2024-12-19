package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Board extends JPanel {

    private Timer timer;
    private String message = "Игра окончена";
    private Ball ball;
    private Paddle paddle;
    private Brick[] bricks;
    private int inGame = 2;
    private String playerName = "-";

    public Board(int blockPositionType) {

        initBoard(blockPositionType);
    }

    private void initBoard(int blockPositionType) {

        addKeyListener(new TAdapter());
        setFocusable(true);
        setPreferredSize(new Dimension(Commons.WIDTH, Commons.HEIGHT));

        gameInit(blockPositionType);
    }

    private void gameInit(int blockPositionType) {

        bricks = new Brick[Commons.N_OF_BRICKS];

        ball = new Ball();
        paddle = new Paddle();
        int k = 0;

        switch (blockPositionType) {
            case 1:
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 6; j++) {
                        if (k < bricks.length) {
                            bricks[k] = new Brick(j * 40 + 30, i * 10 + 30); // Убираем пробелы между блоками
                            k++;
                        }
                    }
                }
                break;
            case 2:
                for (int i = 0; i < 10; i++) {
                    for (int j = 0; j < 6; j++) {
                        if (k < bricks.length) {
                        if (i % 2 != 0) {
                            if (j == 0 || j == 2 || j == 3 || j == 5) {
                                bricks[k] = new Brick(j * 40 + 30, i * 10 + 30); // Убираем пробелы между блоками
                                k++;
                            }
                        }
                        else {
                            if (j == 1 || j == 4) {
                                bricks[k] = new Brick(j * 40 + 30, i * 10 + 30); // Убираем пробелы между блоками
                                k++;
                            }
                        }

                        }
                    }
                }
                break;
            case 3:
                for (int i = 0; i < 7; i++) {
                    for (int j = 0; j < 6; j++) {
                        if (k < bricks.length) {
                            if (i == 0 || i == 1 || i == 2) {
                                bricks[k] = new Brick(j * 40 + 30, i * 10 + 30); // Убираем пробелы между блоками
                                k++;
                            }
                           else if (i == 3 || i == 4) {
                               if (j != 0 && j != 5) {
                                   bricks[k] = new Brick(j * 40 + 30, i * 10 + 30); // Убираем пробелы между блоками
                                   k++;
                               }
                            }
                            else if (i == 5 || i == 6) {
                                if (j != 0 && j != 1 &&  j != 4 &&j != 5) {
                                    bricks[k] = new Brick(j * 40 + 30, i * 10 + 30); // Убираем пробелы между блоками
                                    k++;
                                }
                            }
                        }
                    }
                }
                break;
            default:
                System.out.println("Invalid block position type: " + blockPositionType);
                break;
        }


        timer = new Timer(Commons.PERIOD, new GameCycle());
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        var g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        if (inGame == 2) {

            drawObjects(g2d);
        } else {
            gameFinished(g2d, inGame);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private void drawObjects(Graphics2D g2d) {

        g2d.drawImage(ball.getImage(), ball.getX(), ball.getY(),
                ball.getImageWidth(), ball.getImageHeight(), this);
        g2d.drawImage(paddle.getImage(), paddle.getX(), paddle.getY(),
                paddle.getImageWidth(), paddle.getImageHeight(), this);

        for (int i = 0; i < Commons.N_OF_BRICKS; i++) {

            if (!bricks[i].isDestroyed()) {

                g2d.drawImage(bricks[i].getImage(), bricks[i].getX(),
                        bricks[i].getY(), bricks[i].getImageWidth(),
                        bricks[i].getImageHeight(), this);
            }
        }
    }

    private void gameFinished(Graphics2D g2d, int inGame) {
        var font = new Font("Verdana", Font.BOLD, 18);
        FontMetrics fontMetrics = this.getFontMetrics(font);

        g2d.setColor(Color.BLACK);
        g2d.setFont(font);

        if (inGame == 1) {
            g2d.drawString(message,
                    (Commons.WIDTH - fontMetrics.stringWidth(message)) / 2,
                    Commons.HEIGHT / 2 - 20);
            g2d.drawString("Игрок: " + playerName,
                    (Commons.WIDTH - fontMetrics.stringWidth("Игрок: " + playerName)) / 2,
                    Commons.HEIGHT / 2 + 20);

            Leaderboard leaderboard = new Leaderboard();
            leaderboard.setScore(playerName);
        } else {
            g2d.drawString("Поражение",
                    (Commons.WIDTH - fontMetrics.stringWidth("Поражение")) / 2,
                    Commons.HEIGHT / 2);
        }

//        // Добавление кнопки для возврата в главное меню
//        JButton mainMenuButton = new JButton("Главное меню");
//        mainMenuButton.setBounds(
//                (Commons.WIDTH - 150) / 2, // Центрирование по ширине
//                Commons.HEIGHT / 2 + 50,  // Расположение ниже текста
//                150, 40
//        );
//
//        mainMenuButton.setBackground(Color.LIGHT_GRAY);
//        mainMenuButton.setForeground(Color.BLACK);
//        mainMenuButton.setFont(new Font("Arial", Font.BOLD, 14));
//
//        // Действие для кнопки "Главное меню"
//        mainMenuButton.addActionListener(e -> returnToMainMenu());
//
//        // Добавляем кнопку на панель
//        this.setLayout(null);
//        this.add(mainMenuButton);
//        this.repaint();
    }

//    // Метод для возврата в главное меню
//    private void returnToMainMenu() {
//        EventQueue.invokeLater(() -> {
//            var launcher = new GameLauncher();
//            launcher.setVisible(true);
//        });
//        SwingUtilities.getWindowAncestor(this).dispose(); // Закрываем текущее окно
//    }


    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {

            paddle.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {

            paddle.keyPressed(e);
        }
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            doGameCycle();
        }
    }

    private void doGameCycle() {

        ball.move();
        paddle.move();
        checkCollision();
        repaint();
    }

    private void stopGame(boolean isWin) {
        if (isWin) {
            if (playerName.equals("-")) {
                String inputName = JOptionPane.showInputDialog(this, "Введите ваш псевдоним:", "Имя игрока", JOptionPane.PLAIN_MESSAGE);
                if (inputName != null && !inputName.trim().isEmpty()) {
                    playerName = inputName.trim();
                }
            }
            inGame = 1;
        }
        else{
            inGame = 0;
        }
        timer.stop();

    }

    private void checkCollision() {

        if (ball.getRect().getMaxY() > Commons.BOTTOM_EDGE) {

            stopGame(false);
        }

        for (int i = 0, j = 0; i < Commons.N_OF_BRICKS; i++) {

            if (bricks[i].isDestroyed()) {

                j++;
            }

            if (j == Commons.N_OF_BRICKS || true) {

                message = "Победа!";
                stopGame(true);
            }
        }

        if ((ball.getRect()).intersects(paddle.getRect())) {

            int paddleLPos = (int) paddle.getRect().getMinX();
            int ballLPos = (int) ball.getRect().getMinX();

            int first = paddleLPos + 8;
            int second = paddleLPos + 16;
            int third = paddleLPos + 24;
            int fourth = paddleLPos + 32;

            if (ballLPos < first) {

                ball.setXDir(-1);
                ball.setYDir(-1);
            }

            if (ballLPos >= first && ballLPos < second) {

                ball.setXDir(-1);
                ball.setYDir(-1 * ball.getYDir());
            }

            if (ballLPos >= second && ballLPos < third) {

                ball.setXDir(0);
                ball.setYDir(-1);
            }

            if (ballLPos >= third && ballLPos < fourth) {

                ball.setXDir(1);
                ball.setYDir(-1 * ball.getYDir());
            }

            if (ballLPos > fourth) {

                ball.setXDir(1);
                ball.setYDir(-1);
            }
        }

        for (int i = 0; i < Commons.N_OF_BRICKS; i++) {

            if ((ball.getRect()).intersects(bricks[i].getRect())) {

                int ballLeft = (int) ball.getRect().getMinX();
                int ballHeight = (int) ball.getRect().getHeight();
                int ballWidth = (int) ball.getRect().getWidth();
                int ballTop = (int) ball.getRect().getMinY();

                var pointRight = new Point(ballLeft + ballWidth + 1, ballTop);
                var pointLeft = new Point(ballLeft - 1, ballTop);
                var pointTop = new Point(ballLeft, ballTop - 1);
                var pointBottom = new Point(ballLeft, ballTop + ballHeight + 1);

                if (!bricks[i].isDestroyed()) {

                    if (bricks[i].getRect().contains(pointRight)) {

                        ball.setXDir(-1);
                    } else if (bricks[i].getRect().contains(pointLeft)) {

                        ball.setXDir(1);
                    }

                    if (bricks[i].getRect().contains(pointTop)) {

                        ball.setYDir(1);
                    } else if (bricks[i].getRect().contains(pointBottom)) {

                        ball.setYDir(-1);
                    }

                    bricks[i].setDestroyed(true);
                }
            }
        }
    }
}
