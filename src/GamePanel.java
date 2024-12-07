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
public class GamePanel extends JPanel{
    private Image backgroundImage;
    private ScorePanel scorePanel = null;
    private JTextField text = new JTextField(10);

    // 부기 + 단어의 전체 사이즈
    private final int monsterWidth = 100;
    private final int monsterHeight = 108;

    //마법사(사용자)
    private ImageIcon userIcon = new ImageIcon("resource/img/wizard.jpg");
    private ImageIcon attackWizard = new ImageIcon("resource/img/attackWizard.jpg");
    private Image userImg = userIcon.getImage();
    private Image attackImg = attackWizard.getImage();
    private JLabel userLabel;
    
    // 부기
    private ImageIcon boogiIcon = new ImageIcon("resource/img/boogi.jpg");
    private ImageIcon deathBoogi = new ImageIcon("resource/img/deathboogi.jpg");
    private Image deathBoogiImg = deathBoogi.getImage();
    private Image boogiImg = boogiIcon.getImage();
    private JLabel[] attackingLabels;

    // 단어
    private WordVector wordVec = new WordVector(); // 단어 벡터 생성
    private String[] words;
    private JLabel[] wordLabels;

    // 게임 배경화면
    private ImageIcon icon = new ImageIcon("resource/img/image.jpg");
    private Image img = icon.getImage();

    private AttackThread aThread = new AttackThread();
    private TimerThread tThread = new TimerThread();
    private GameGroundPanel ground = new GameGroundPanel();
    private InputPanel input = new InputPanel();
    
    public GamePanel(ScorePanel scorePanel) {
        this.scorePanel = scorePanel;
        this.setLayout(new BorderLayout());
        add(ground, BorderLayout.CENTER);
        add(input, BorderLayout.SOUTH);
        
    }

    class GameGroundPanel extends JPanel {
    	
        public void paintComponent(Graphics g) {
        	Dimension d = getSize();
            g.drawImage(img, 0, 0, d.width, d.height, this);
        }
        private void makeBoogi() {
        	// 여러 부기 생성
            int numBoogis = 60; // 최대퇴치부기의 수
            attackingLabels = new JLabel[numBoogis];
            wordLabels = new JLabel[numBoogis];
            words = new String[numBoogis];

            

            for (int i = 0; i < numBoogis; i++) {
            	
                int locationY=(int) (Math.random() * 450);
                

                // 단어 설정
                
                words[i] = wordVec.get();
                if (words[i].length() * 10 > monsterWidth) {
                    i--; // 단어 길이가 너무 길면 다시 가져옴
                    continue;
                }	
                wordLabels[i] = new JLabel(words[i]);
                wordLabels[i].setForeground(Color.white);
                wordLabels[i].setFont(new Font("Arial", Font.BOLD, 15));
                wordLabels[i].setSize(monsterWidth, 20);
                wordLabels[i].setLocation((monsterWidth - wordLabels[i].getPreferredSize().width) / 2 + 700, monsterHeight + locationY);

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
        public void makeUser() {
        	userLabel = new JLabel(userIcon);
        	userLabel.setSize(100,100);
        	userLabel.setLocation(100,200);
        	userLabel.setVisible(false);
        	add(userLabel);
        }
        public GameGroundPanel() {
            setLayout(null);
            makeBoogi();
            makeUser();
        }
        
    }

    // 부기가 다가오는(공격) 스레드
    class AttackThread extends Thread {
    	
        @Override
        public void run() {
        	userLabel.setVisible(true);
            for (int i = 0; i < attackingLabels.length; i++) {
                try {
                    wordLabels[i].setVisible(true);
                    attackingLabels[i].setVisible(true);

                    // 각 부기가 이동하는 스레드 시작
                    MoveBoogiThread moveBoogiThread = new MoveBoogiThread(wordLabels[i], attackingLabels[i]);
                    moveBoogiThread.start();
                    
                    sleep(3000); // 다음 부기 등장까지 3초 대기
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
    
    class MoveBoogiThread extends Thread {
        private JLabel wordLabel;
        private JLabel attackingLabel;
        
        public MoveBoogiThread(JLabel wordLabel, JLabel attackingLabel) {
            this.wordLabel = wordLabel;
            this.attackingLabel = attackingLabel;
        }

        @Override
        public void run() {
            try {
                while (attackingLabel.getX() > 0) {
                    wordLabel.setLocation(wordLabel.getX() - 10, wordLabel.getY());
                    attackingLabel.setLocation(attackingLabel.getX() - 10, attackingLabel.getY());
                    sleep(300); // 부기의 이동 속도
                }
                //scorePanel.boogicannotKill();
                
                wordLabel.setVisible(false);
                attackingLabel.setVisible(false);
                
            } catch (InterruptedException e) {
                return;
            }
        }
    }
    
    
    
    class TimerThread extends Thread {
        @Override
        public void run() {
            try {
                sleep(60000); // 1분 후에 게임 종료
                aThread.interrupt();
                JOptionPane.showMessageDialog(null, "게임 종료! 퇴치한 부기 수: " + scorePanel.score);
            } catch (InterruptedException e) {
                return;
            }
        }
    } // 1분 후에 게임 종료
    
    public void startGame() {
        tThread.start();
    	aThread.start();
      
    }

    class InputPanel extends JPanel {
        public InputPanel() {
            this.setBackground(Color.GREEN);
            add(text);

            text.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String inputText = text.getText();
                    if (inputText.length() == 0) {
                        return;
                    }
                    for (int i = 0; i < wordLabels.length; i++) {
                        if (wordLabels[i].isVisible() && inputText.equals(wordLabels[i].getText())) {
                            // 단어가 맞았을 때 해당 부기와 단어를 숨김
                        	scorePanel.boogiKillscore();
                        	attackingLabels[i].setIcon(deathBoogi);
                        	// Play sound effect in a separate thread
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
                            
                            // final index to use inside TimerTask
                            final int index = i;
                            
                            // 1초 후에 부기와 단어 숨기기 (별도의 타이머 사용)
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
                            
                           
                            text.setText("");
                            break;
                        }
                        else
                        	text.setText("");
                    }
                }
            });
        }
    }
}
