import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class MainGameUI extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private BackgroundMusicThread musicThread; // 배경 음악 스레드
    private String difficulty = "쉬움";
    private String userName=null;
    public MainGameUI() {
        setTitle("게임 메인 화면");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 화면 중앙에 표시

        // 사용자 이름 입력
        if(userName==null)
        	showNameInputDialog();
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
    public MainGameUI(String userName) {
        setTitle("게임 메인 화면");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 화면 중앙에 표시
        this.userName = userName;
        
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
    public String getDifficulty() {
        return difficulty; //난이도 리턴
    }
    // 사용자 이름 입력을 위한 대화창
    private void showNameInputDialog() {
        NameInputDialog nameDialog = new NameInputDialog(this);
        userName = nameDialog.getUserName();
        if (userName == null || userName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "사용자 이름을 입력해야 게임을 시작할 수 있습니다.", 
                "이름 입력 필요", JOptionPane.WARNING_MESSAGE);
            System.exit(0); // 이름 입력이 없으면 프로그램 종료
        }
    }
    // 버튼 호버 효과음 재생
    private void playHoverSound(String soundFilePath) {
        try {
            File soundFile = new File(soundFilePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // 사용자 이름 입력창
    class NameInputDialog extends JDialog {
        private JTextField nameField;
        private String userName;

        public NameInputDialog(JFrame parent) {
            super(parent, "사용자 이름 입력", true);
            setSize(400, 300); // 크기 조정
            setLayout(new BorderLayout());
            setLocationRelativeTo(parent);

            // 프롬프트 라벨 설정
            JLabel promptLabel = new JLabel("사용자의 이름을 입력하세요:", SwingConstants.CENTER);
            promptLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18)); // 글씨 크기 증가

            // 입력 필드 설정
            nameField = new JTextField();
            nameField.setFont(new Font("맑은 고딕", Font.PLAIN, 16)); // 글씨 크기 
            nameField.setHorizontalAlignment(JTextField.CENTER);
            // 확인 버튼 설정
            JButton okButton = new JButton("확인");
            okButton.setFont(new Font("맑은 고딕", Font.BOLD, 16)); // 글씨 크기 
            okButton.addActionListener(e -> {
                userName = nameField.getText().trim();
                dispose();
            });

            // 마법사 이미지를 보여줄 라벨 설정
            ImageIcon wizardIcon = new ImageIcon("resource/img/wizard.jpg"); // 마법사 이미지 경로
            JLabel wizardLabel = new JLabel(wizardIcon);
            wizardLabel.setHorizontalAlignment(SwingConstants.CENTER); // 중앙 정렬

            // 레이아웃 설정
            JPanel inputPanel = new JPanel(new BorderLayout());
            inputPanel.add(promptLabel, BorderLayout.NORTH);
            inputPanel.add(nameField, BorderLayout.CENTER);
            inputPanel.add(okButton, BorderLayout.SOUTH);

            // 마법사 이미지와 입력 패널을 통합
            add(wizardLabel, BorderLayout.CENTER); // 마법사 이미지를 중앙에 배치
            add(inputPanel, BorderLayout.SOUTH); // 입력 필드와 버튼을 하단에 배치

            setVisible(true);
        }

        public String getUserName() {
            return userName;
        }
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new BackgroundPanel("resource/img/main.jpg");
        mainPanel.setLayout(new BorderLayout());

        // 상단 패널
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        // 제목 라벨
        JLabel titleLabel = new JLabel("Kill the Boogi", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 40)); // 글꼴 크기
        titleLabel.setForeground(Color.RED); // 텍스트 색상

        // 제목 효과를 위한 패널
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // 상단/하단 패딩 추가

        // 시각화를 위한 배경 라벨
        JLabel backgroundTitle = new JLabel();
        backgroundTitle.setOpaque(true);
        backgroundTitle.setBackground(new Color(0, 0, 0, 150)); // 반투명 배경
        backgroundTitle.setBorder(BorderFactory.createLineBorder(Color.RED, 5)); // 테두리
        backgroundTitle.setLayout(new BorderLayout());
        backgroundTitle.setPreferredSize(new Dimension(0, 80)); // 높이 설정

        backgroundTitle.add(titleLabel, BorderLayout.CENTER); // 제목 중앙 배치
        titlePanel.add(backgroundTitle, BorderLayout.CENTER);

        // 제목 아래 음소거 버튼 패널
        JPanel muteButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        muteButtonPanel.setOpaque(false);

        // 음소거 버튼
        JButton muteButton = new JButton(new ImageIcon("resource/img/unmute.jpg")); // 음소거 이미지
        muteButton.setBorderPainted(false);
        muteButton.setContentAreaFilled(false);
        muteButton.setFocusPainted(false);
        muteButton.setPreferredSize(new Dimension(50, 50)); // 버튼 크기 고정

        muteButton.addActionListener(e -> {
            if (musicThread.isPlaying()) {
                musicThread.stopMusic();
                muteButton.setIcon(new ImageIcon("resource/img/mute.jpg"));
            } else {
                musicThread.playMusic();
                muteButton.setIcon(new ImageIcon("resource/img/unmute.jpg"));
            }
        });

        muteButtonPanel.add(muteButton);

        // 제목과 음소거 버튼을 포함하는 패널
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.add(titlePanel, BorderLayout.NORTH); // 제목 패널 추가
        headerPanel.add(muteButtonPanel, BorderLayout.WEST); // 음소거 버튼 추가

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // 중앙 버튼 패널
        JPanel buttonPanel = new JPanel(new GridBagLayout()); // GridBagLayout 사용
        buttonPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 버튼 간격 추가
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        //게임시작
        JButton startButton = createHoverEffectButton("게임 시작", "resource/sound/hover.wav");
        startButton.addActionListener(e -> {
            new GameFrame(difficulty, userName);
            musicThread.stopMusic();
            dispose();
        });
        gbc.gridx = 0;
        gbc.gridy = 0;
        buttonPanel.add(startButton, gbc);

        //난이도
        JButton difficultyButton = createHoverEffectButton("난이도 설정", "resource/sound/hover.wav");
        difficultyButton.addActionListener(e -> showDifficultyDialog());
        gbc.gridy = 1;
        buttonPanel.add(difficultyButton, gbc);
        
        //도움말
        JButton helpButton = createHoverEffectButton("게임 도움말", "resource/sound/hover.wav");
        helpButton.addActionListener(e -> new HelpFrame());
        gbc.gridy = 2;
        buttonPanel.add(helpButton, gbc);

        //점수판
        JButton scoreButton = createHoverEffectButton("점수판", "resource/sound/hover.wav");
        scoreButton.addActionListener(e -> new ScoreboardFrame());
        gbc.gridy = 3;
        buttonPanel.add(scoreButton, gbc);

        //게임종료
        JButton exitButton = createHoverEffectButton("게임 종료", "resource/sound/hover.wav");
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "게임을 종료하시겠습니까?", "게임 종료", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        gbc.gridy = 4;
        buttonPanel.add(exitButton, gbc);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        //제작자
        JLabel footerLabel = new JLabel("© 2024 Final project by Dongnyoung", SwingConstants.CENTER);
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        footerLabel.setForeground(Color.WHITE);
        mainPanel.add(footerLabel, BorderLayout.SOUTH);

        return mainPanel;
    }


    private JButton createHoverEffectButton(String text,String soundFilePath) {
        JButton button = new JButton(text);
        button.setContentAreaFilled(false); // 버튼 배경 투명
        button.setFocusPainted(false); // 포커스 테두리 제거
        button.setBorderPainted(false); // 버튼 테두리 제거
        button.setFont(new Font("맑은 고딕", Font.BOLD, 24)); // 기본 글꼴 설정
        button.setForeground(Color.WHITE); // 기본 글자 색상 설정

        // 마우스 이벤트 추가
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setFont(new Font("맑은 고딕", Font.BOLD, 30)); // 글꼴 크기 
                button.setForeground(Color.YELLOW); // 텍스트 색상 변경
                playHoverSound(soundFilePath);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setFont(new Font("맑은 고딕", Font.BOLD, 24)); // 기본 글꼴
                button.setForeground(Color.WHITE); // 기본 텍스트 색상 복원
            }
        });

        return button;
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
            difficulty = options[choice];
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

    private class BackgroundMusicThread extends Thread {
        private final String musicFilePath;
        private Clip clip;
        private boolean keepPlaying = true;

        public BackgroundMusicThread(String musicFilePath) {
            this.musicFilePath = musicFilePath;
        }

        @Override
        public void run() {
            try {
                File musicFile = new File(musicFilePath);
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
                clip = AudioSystem.getClip();
                clip.open(audioStream);
                clip.loop(Clip.LOOP_CONTINUOUSLY); // 반복 재생
                while (keepPlaying) {
                    Thread.sleep(100); // 스레드 유지
                }
                clip.stop(); // 음악 정지
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public synchronized void stopMusic() {
            if (clip != null && clip.isRunning()) {
                clip.stop(); // 재생 중이면 정지
            }
        }

        public synchronized void playMusic() {
            if (clip != null) {
                clip.setFramePosition(0); // 재생 위치를 처음으로 초기화
                clip.start(); // 재생 시작
                clip.loop(Clip.LOOP_CONTINUOUSLY); // 반복 재생
            }
        }

        public synchronized boolean isPlaying() {
            return clip != null && clip.isRunning();
        }
    }

    public static void main(String[] args) {
        new MainGameUI();
    }
}

//게임 도움말 창
class HelpFrame extends JFrame {

 public HelpFrame() {
     setTitle("게임 도움말");
     setSize(700, 600); // 창 크기 조정
     setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
     setLocationRelativeTo(null); // 화면 중앙에 표시

     JPanel contentPanel = new JPanel(new BorderLayout());
     contentPanel.setBackground(Color.BLACK); // 배경색 설정

     // 상단: 캐릭터 설명
     JPanel characterPanel = new JPanel();
     characterPanel.setBackground(Color.BLACK);
     characterPanel.setLayout(new BoxLayout(characterPanel, BoxLayout.Y_AXIS));

     // 마법사 설명
     JLabel wizardLabel = new JLabel("마법사(user): 부기를 퇴치하기 위해 단어주문을 외치는 용감한 주인공.");
     wizardLabel.setForeground(Color.WHITE);
     wizardLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
     wizardLabel.setIcon(new ImageIcon(new ImageIcon("resource/img/wizard.jpg")
             .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH))); // 마법사 이미지 추가 및 크기 조정
     wizardLabel.setHorizontalTextPosition(SwingConstants.RIGHT); // 텍스트를 이미지 오른쪽에 배치
     wizardLabel.setVerticalTextPosition(SwingConstants.CENTER); // 텍스트를 이미지 수평 중앙에 맞춤
     wizardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
     characterPanel.add(wizardLabel);
     characterPanel.add(Box.createRigidArea(new Dimension(0, 20))); // 간격 추가

     // 부기 설명
     JLabel boogiLabel = new JLabel("변형부기(moster): 침략해온 몬스터로, 단어를 입력해 퇴치해야 합니다.");
     boogiLabel.setForeground(Color.WHITE);
     boogiLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
     boogiLabel.setIcon(new ImageIcon(new ImageIcon("resource/img/boogi.jpg")
             .getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH))); // 부기 이미지 추가 및 크기 조정
     boogiLabel.setHorizontalTextPosition(SwingConstants.RIGHT); // 텍스트를 이미지 오른쪽에 배치
     boogiLabel.setVerticalTextPosition(SwingConstants.CENTER); // 텍스트를 이미지 수평 중앙에 맞춤
     boogiLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
     characterPanel.add(boogiLabel);

     // 중앙: 게임 방법 설명
     JLabel helpLabel = new JLabel("<html><div style='text-align: center;'>"
             + "<h1 style='color: white;'>게임 방법</h2>"
             + "<p style='color: white; font-size: 18px;'>"
             + "1. 60초 이내에 단어를 입력하여 최대한 많은 부기를 퇴치하세요.</p>"
             + "<p style='color: white; font-size: 18px;'>"
             + "2. 목숨은 5번입니다. 부기가 마법사에게 닿으면 목숨을 잃습니다.</p>"
             + "<p style='color: white; font-size: 18px;'>"
             + "3. 단어를 정확히 입력하면 부기를 퇴치할 수 있습니다.</p>"
             + "<p style='color: white; font-size: 18px;'>"
             + "4. 즐겁게 플레이하세요!</p>"
             + "</div></html>", SwingConstants.CENTER);
     helpLabel.setForeground(Color.WHITE);

     // 하단: 돌아가기 버튼
     JButton backButton = new JButton("돌아가기");
     backButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
     backButton.setFocusPainted(false);
     backButton.addActionListener(e -> dispose());

     JPanel buttonPanel = new JPanel();
     buttonPanel.setBackground(Color.BLACK);
     buttonPanel.add(backButton);

     // 컴포넌트 배치
     contentPanel.add(characterPanel, BorderLayout.NORTH); // 상단: 캐릭터 설명
     contentPanel.add(helpLabel, BorderLayout.CENTER); // 중앙: 게임 방법 설명
     contentPanel.add(buttonPanel, BorderLayout.SOUTH); // 하단: 돌아가기 버튼

     add(contentPanel);
     setVisible(true);
 }
}

class ScoreboardFrame extends JFrame {
    public ScoreboardFrame() {
        setTitle("Scoreboard");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 스코어 데이터를 표시할 JTextArea
        JTextArea scoreArea = new JTextArea();
        scoreArea.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        scoreArea.setEditable(false); // 수정 불가
        scoreArea.setOpaque(false); // 배경 투명 처리
        scoreArea.setForeground(Color.WHITE); // 텍스트 색상: 흰색

        // JScrollPane 설정
        JScrollPane scrollPane = new JScrollPane(scoreArea);
        scrollPane.setOpaque(false); // JScrollPane 배경 투명 처리
        scrollPane.getViewport().setOpaque(false); // 내부 뷰포트 배경 투명

        // 프레임 배경색 설정
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.BLACK); // 배경색: 검정
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // 닫기 버튼
        JButton closeButton = new JButton("닫기");
        closeButton.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        closeButton.setForeground(Color.WHITE); // 버튼 텍스트 색상: 흰색
        closeButton.setBackground(Color.BLACK); // 버튼 배경색: 검정
        closeButton.setFocusPainted(false); // 버튼 포커스 테두리 제거
        closeButton.setBorderPainted(false); // 버튼 테두리 제거
        closeButton.addActionListener(e -> dispose());
        contentPanel.add(closeButton, BorderLayout.SOUTH);

        add(contentPanel);

        // 점수 읽기
        String scores = readAndSortScores();
        scoreArea.setText(scores);

        setVisible(true);
    }

    private String readAndSortScores() {
        File scoreFile = new File("rank.txt");
        if (!scoreFile.exists()) {
            return "점수 기록이 없습니다.\n";
        }

        ArrayList<ScoreEntry> scoreEntries = new ArrayList<>();

        // 파일 읽기 및 파싱
        try (Scanner scanner = new Scanner(scoreFile)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                ScoreEntry entry = parseScoreEntry(line);
                if (entry != null) {
                    scoreEntries.add(entry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "점수를 읽는 중 오류가 발생했습니다.\n";
        }

        // 점수 내림차순 정렬
        scoreEntries.sort((a, b) -> Integer.compare(b.score, a.score));

        // 랭킹 출력
        StringBuilder sortedScores = new StringBuilder();
        int rank = 1;
        for (ScoreEntry entry : scoreEntries) {
            sortedScores.append(String.format("%d위: %s, 퇴치부기 수 : %d(%s)%n",
                    rank++, entry.userName, entry.score, entry.difficulty));
        }

        return sortedScores.toString();
    }

 // 점수 파싱 메서드
    private ScoreEntry parseScoreEntry(String line) {
        try {
            String[] parts = line.split(", ");
            String userNamePart = parts[0].split(": ")[1]; // 사용자 이름
            String scorePart = parts[1].split(": ")[1].trim(); // 점수 (공백 제거)
            String difficultyPart = scorePart.split("\\(")[1].replace(")", ""); // 난이도
            int score = Integer.parseInt(scorePart.split("\\(")[0].trim()); // 숫자 부분만 파싱 (공백 제거)

            return new ScoreEntry(userNamePart, score, difficultyPart);
        } catch (Exception e) {
            e.printStackTrace();
            return null; // 잘못된 형식의 데이터 무시
        }
    }


    // 점수 데이터를 저장할 클래스
    private static class ScoreEntry {
        String userName;
        int score;
        String difficulty;

        public ScoreEntry(String userName, int score, String difficulty) {
            this.userName = userName;
            this.score = score;
            this.difficulty = difficulty;
        }
    }
}


