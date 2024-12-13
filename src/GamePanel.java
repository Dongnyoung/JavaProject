import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioInputStream;
import java.io.File;

public class GamePanel extends JPanel {
    private Image backgroundImage;
    private ScorePanel scorePanel = null;
    private JTextField text = new JTextField(10);
    private SkillPanel skillPanel = null;

    // 부기 + 단어의 전체 사이즈
    private final int monsterWidth = 100;
    private final int monsterHeight = 108;

    // 마법사(사용자)
    private ImageIcon userIcon = new ImageIcon("resource/img/wizard.jpg");
    private ImageIcon attackWizard = new ImageIcon("resource/img/attackWizard.jpg");
    private JLabel userLabel;

    // 부기
    private ImageIcon boogiIcon = new ImageIcon("resource/img/boogi.jpg");
    private ImageIcon deathBoogi = new ImageIcon("resource/img/deathboogi.jpg");
    private JLabel[] attackingLabels;

    // 단어
    private WordVector wordVec = new WordVector();
    private String[] words;
    private JLabel[] wordLabels;

    // MoveBoogiThread 배열
    private MoveBoogiThread[] moveBoogiThreads;

    // 게임 배경화면
    private ImageIcon icon = new ImageIcon("resource/img/image.jpg");
    private Image img = icon.getImage();

    private AttackThread aThread = new AttackThread();
    private TimerThread tThread = new TimerThread();
    private BackgroundMusicThread musicThread = new BackgroundMusicThread();

    private JLabel timerLabel;

    public GamePanel(ScorePanel scorePanel, SkillPanel skillPanel) {
        this.scorePanel = scorePanel;
        this.skillPanel = skillPanel;

        this.setLayout(new BorderLayout());

        // 타이머 레이블 초기화
        timerLabel = new JLabel("60", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        timerLabel.setForeground(Color.RED);
        this.add(timerLabel, BorderLayout.NORTH);

        GameGroundPanel ground = new GameGroundPanel();
        this.add(ground, BorderLayout.CENTER);

        InputPanel input = new InputPanel();
        this.add(input, BorderLayout.SOUTH);
    }

    class GameGroundPanel extends JPanel {
        public GameGroundPanel() {
            setLayout(null);
            makeBoogi();
            makeUser();
        }
        
        public void paintComponent(Graphics g) {
            Dimension d = getSize();
            g.drawImage(img, 0, 0, d.width, d.height, this);
        }
        
        private void makeBoogi() {
            int numBoogis = 60; // 최대 퇴치 부기의 수
            attackingLabels = new JLabel[numBoogis];
            wordLabels = new JLabel[numBoogis];
            moveBoogiThreads = new MoveBoogiThread[numBoogis];
            words = new String[numBoogis];

            for (int i = 0; i < numBoogis; i++) {
                int locationY = (int) (Math.random() * 450);

                // 단어 설정
                words[i] = wordVec.get();
                if (words[i].length() * 15 > monsterWidth) {
                    i--;
                    continue;
                }

                wordLabels[i] = new JLabel(words[i]);
                wordLabels[i].setForeground(Color.white);
                wordLabels[i].setFont(new Font("Arial", Font.BOLD, 20));
                wordLabels[i].setSize(monsterWidth, 20);
                wordLabels[i].setHorizontalAlignment(SwingConstants.CENTER); 
                wordLabels[i].setLocation(700, monsterHeight + locationY);
                wordLabels[i].setVisible(false);
                add(wordLabels[i]);

                // 부기 설정
                attackingLabels[i] = new JLabel(boogiIcon);
                attackingLabels[i].setSize(monsterWidth, monsterHeight);
                attackingLabels[i].setLocation(700, locationY);
                attackingLabels[i].setVisible(false);
                add(attackingLabels[i]);
            }
        }

        private void makeUser() {
            userLabel = new JLabel(userIcon);
            userLabel.setSize(130, 100);
            userLabel.setLocation(10, 200);
            userLabel.setVisible(false);
            add(userLabel);
        }
    }

    class AttackThread extends Thread {
        @Override
        public void run() {
            userLabel.setVisible(true);
            for (int i = 0; i < attackingLabels.length; i++) {
                try {
                    wordLabels[i].setVisible(true);
                    attackingLabels[i].setVisible(true);

                    // MoveBoogiThread 생성 및 시작
                    moveBoogiThreads[i] = new MoveBoogiThread(wordLabels[i], attackingLabels[i], skillPanel);
                    moveBoogiThreads[i].start();

                    sleep(2000); // 다음 부기 등장까지 대기
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }

    class MoveBoogiThread extends Thread {
        private JLabel wordLabel;
        private JLabel attackingLabel;
        private SkillPanel skillPanel;
        private boolean isStopped = false;

        public MoveBoogiThread(JLabel wordLabel, JLabel attackingLabel, SkillPanel skillPanel) {
            this.wordLabel = wordLabel;
            this.attackingLabel = attackingLabel;
            this.skillPanel = skillPanel;
        }

        public synchronized void stopMoving() {
            isStopped = true;
        }

        private synchronized boolean isStopped() {
            return isStopped;
        }

        @Override
        public void run() {
        	try {
                while (attackingLabel.getX() > 100 && !isStopped()) {
                    synchronized (this) {
                        wordLabel.setLocation(wordLabel.getX() - 10, wordLabel.getY());
                        attackingLabel.setLocation(attackingLabel.getX() - 10, attackingLabel.getY());
                    }
                    sleep(300); // 부기 이동 속도
                }
                synchronized (this) {
                    if (attackingLabel.getX() <= 100 && !isStopped()) {
                        if (skillPanel.getLives() == 0) {
                            tThread.interrupt(); // tThread는 외부에서 선언된 스레드
                            aThread.interrupt(); // aThread는 외부에서 선언된 스레드
                            musicThread.stopMusic();
                            return;
                        }

                        shakeWindow();
                        skillPanel.loseLife();
                        wordLabel.setVisible(false);
                        attackingLabel.setVisible(false);
                    }
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    class TimerThread extends Thread {
        @Override
        public void run() {
            int timeLeft = 60;
            try {
                while (timeLeft > 0) {
                    int finalTimeLeft = timeLeft;
                    SwingUtilities.invokeLater(() -> timerLabel.setText(""+finalTimeLeft));
                    sleep(1000);
                    timeLeft--;
                    if(timeLeft<5) {
                    	blinkScreen();
                    }
                }
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Game Over!"));
                aThread.interrupt();
                tThread.interrupt();
                musicThread.stopMusic();
            } catch (InterruptedException e) {
                musicThread.stopMusic();
            }
        }
    }

    class BackgroundMusicThread extends Thread {
        private boolean keepPlaying = true;

        @Override
        public void run() {
            try {
                File musicFile = new File("resource/sound/backgroundMusic.wav");
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY);
                clip.start();
                while (keepPlaying) {
                    Thread.sleep(100);
                }
                clip.stop();
                clip.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void stopMusic() {
            keepPlaying = false;
        }
    }

    class InputPanel extends JPanel {
        public InputPanel() {
            this.setBackground(Color.RED);
            add(text);

            text.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String inputText = text.getText();
                    if (inputText.isEmpty()) return;

                    for (int i = 0; i < wordLabels.length; i++) {
                        if (wordLabels[i].isVisible() && inputText.equals(wordLabels[i].getText())) {
                            scorePanel.boogiKillscore();
                            userLabel.setIcon(attackWizard);
                            userLabel.setLocation(100, 200);
                            shakeWindow();
                            attackingLabels[i].setIcon(deathBoogi);

                            if (moveBoogiThreads[i] != null) {
                                moveBoogiThreads[i].stopMoving();
                            }

                            new Thread(() -> {
                                try {
                                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("resource/sound/deathSound.wav"));
                                    Clip clip = AudioSystem.getClip();
                                    clip.open(audioInputStream);
                                    clip.start();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }).start();

                            final int index = i;

                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    SwingUtilities.invokeLater(() -> {
                                        wordLabels[index].setVisible(false);
                                        attackingLabels[index].setVisible(false);
                                    });
                                }
                            }, 1000);

                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    SwingUtilities.invokeLater(() -> userLabel.setIcon(userIcon));
                                    userLabel.setLocation(10, 200);
                                }
                            }, 500);

                            text.setText("");
                            break;
                        }
                        else {
                        	text.setText("");
                        }
                    }
                }
            });
        }
    }
    private void blinkScreen() {
        SwingUtilities.invokeLater(() -> {
            Color originalColor = this.getBackground(); // 원래 배경색 저장
            int blinkCount = 5; // 깜빡임 횟수
            int blinkDuration = 100; // 깜빡임 지속 시간 (ms)

            new Thread(() -> {
                try {
                    for (int i = 0; i < blinkCount; i++) {
                        this.setBackground(Color.RED); // 깜빡임 효과 색상
                        Thread.sleep(blinkDuration);
                        this.setBackground(originalColor); // 원래 색상 복원
                        Thread.sleep(blinkDuration);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        });
    }
    private void shakeWindow() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this); // JFrame 가져오기
            if (frame != null) {
                Point originalLocation = frame.getLocation(); // 원래 위치 저장

                int shakeDistance = 10; // 흔들림 정도
                int shakeDuration = 10; // 흔들림 시간 (ms)
                int shakeCount = 2; // 흔들림 횟수

                for (int i = 0; i < shakeCount; i++) {
                    try {
                        frame.setLocation(originalLocation.x + shakeDistance, originalLocation.y);
                        Thread.sleep(shakeDuration);
                        frame.setLocation(originalLocation.x - shakeDistance, originalLocation.y);
                        Thread.sleep(shakeDuration);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                frame.setLocation(originalLocation); // 원래 위치로 복원
            }
        });
    }


    public void startGame() {
        musicThread.start();
        tThread.start();
        aThread.start();
    }
}
