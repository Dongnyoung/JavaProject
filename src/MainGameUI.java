import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;

public class MainGameUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private BackgroundMusicThread musicThread; // 배경 음악 스레드

    public MainGameUI() {
        setTitle("게임 메인 화면");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 화면 중앙에 표시

        // 배경 음악 스레드 시작
        musicThread = new BackgroundMusicThread("resource/sound/mainMusic.wav");
        musicThread.start();

        // CardLayout과 메인 패널 설정
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // 메인 화면 (첫 화면)
        JPanel mainPanel = createMainPanel();
        cardPanel.add(mainPanel, "Main");

        add(cardPanel);
        setVisible(true);
    }

    // 메인 화면 구성
    private JPanel createMainPanel() {
        // 배경 이미지를 사용하는 패널
        JPanel mainPanel = new BackgroundPanel("resource/img/main.jpg");
        mainPanel.setLayout(new BorderLayout());

        // 상단 타이틀
        JLabel titleLabel = new JLabel("부기를 퇴치하라..!", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE); // 글자 색상 설정
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // 버튼 레이아웃
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonPanel.setOpaque(false); // 버튼 패널 배경 투명화

        // "게임 시작" 버튼
        JButton startButton = createHoverEffectButton("게임 시작");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GameFrame(); // GameFrame 실행
                musicThread.stopMusic(); // 음악 종료
                dispose(); // 현재 MainGameUI 창 닫기
            }
        });
        buttonPanel.add(startButton);

        // "게임 도움말" 버튼
        JButton helpButton = createHoverEffectButton("게임 도움말");
        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showHelpDialog();
            }
        });
        buttonPanel.add(helpButton);

        // "난이도 설정" 버튼
        JButton difficultyButton = createHoverEffectButton("난이도 설정");
        difficultyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDifficultyDialog();
            }
        });
        buttonPanel.add(difficultyButton);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        // 하단 정보
        JLabel footerLabel = new JLabel("© 2024 My Game Company", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        footerLabel.setForeground(Color.WHITE); // 글자 색상 설정
        mainPanel.add(footerLabel, BorderLayout.SOUTH);

        return mainPanel;
    }

    private JButton createHoverEffectButton(String text) {
        JButton button = new JButton(text);
        button.setContentAreaFilled(false); // 버튼 배경 투명
        button.setFocusPainted(false); // 포커스 테두리 제거
        button.setBorderPainted(false); // 버튼 테두리 제거
        button.setFont(new Font("맑은 고딕", Font.BOLD, 20)); // 기본 글꼴 설정
        button.setForeground(Color.WHITE); // 기본 글자 색상 설정

        // 마우스 이벤트 추가
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setFont(new Font("맑은 고딕", Font.BOLD, 28)); // 글꼴 크기 증가
                button.setForeground(Color.YELLOW); // 텍스트 색상 변경
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setFont(new Font("맑은 고딕", Font.BOLD, 20)); // 기본 글꼴로 복원
                button.setForeground(Color.WHITE); // 기본 텍스트 색상 복원
            }
        });

        return button;
    }

    private void showHelpDialog() {
        JOptionPane.showMessageDialog(this,
                "게임 도움말:\n" +
                        "1. 방향키로 캐릭터를 움직입니다.\n" +
                        "2. 목표를 달성하세요!\n" +
                        "3. 즐겁게 플레이하세요!",
                "게임 도움말",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showDifficultyDialog() {
        String[] options = {"쉬움", "보통", "어려움"};
        int choice = JOptionPane.showOptionDialog(this,
                "난이도를 선택하세요:",
                "난이도 설정",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null, options, options[1]);
        if (choice >= 0) {
            JOptionPane.showMessageDialog(this,
                    "선택된 난이도: " + options[choice],
                    "난이도 설정",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // 배경 이미지를 표시하는 패널
    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel(String imagePath) {
            try {
                backgroundImage = new ImageIcon(imagePath).getImage();
            } catch (Exception e) {
                System.err.println("이미지를 로드할 수 없습니다: " + imagePath);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                // 패널 크기에 맞게 배경 이미지 그리기
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    // 배경 음악 스레드 클래스
    private class BackgroundMusicThread extends Thread {
        private String musicFilePath;
        private boolean keepPlaying = true;

        public BackgroundMusicThread(String musicFilePath) {
            this.musicFilePath = musicFilePath;
        }

        @Override
        public void run() {
            try {
                File musicFile = new File(musicFilePath);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY); // 반복 재생
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

    public static void main(String[] args) {
        new MainGameUI();
    }
}
