import java.awt.*;
import java.io.*;
import javax.swing.*;

class EndGamePanel extends JPanel {
    private GameFrame gameFrame;
    private String difficulty;
    private String userName;
    private JLabel finalScoreLabel;
    private JButton restartButton;
    private JButton backToMainButton;
    private JButton exitButton;
    private ImageIcon icon = new ImageIcon("resource/img/image.jpg");
    private Image img = icon.getImage();

    public EndGamePanel(GameFrame gameFrame, String difficulty, String userName) {
        this.gameFrame = gameFrame;
        this.difficulty = difficulty;
        this.userName = userName;
        this.setLayout(new BorderLayout());

        // 상단 패널: Game Over 제목
        JLabel titleLabel = new JLabel("GAME OVER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 50));
        titleLabel.setForeground(Color.RED);
        this.add(titleLabel, BorderLayout.NORTH);

        // 중앙 패널: 사용자 이름과 점수, 퇴치 부기 수
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setOpaque(false); // 투명 배경

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        // 마법사 이미지
        JLabel wizardLabel = new JLabel(new ImageIcon(new ImageIcon("resource/img/wizard.jpg")
                .getImage()
                .getScaledInstance(80, 80, Image.SCALE_SMOOTH))); // 마법사 이미지 크기 조정
        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(wizardLabel, gbc);
        
        // 사용자 이름
        JLabel userNameLabel = new JLabel("사용자 이름: " + userName, SwingConstants.LEFT);
        userNameLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        userNameLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(userNameLabel, gbc);

        // 부기 이미지
        JLabel boogiLabel = new JLabel(new ImageIcon(new ImageIcon("resource/img/boogi.jpg")
                .getImage()
                .getScaledInstance(80, 80, Image.SCALE_SMOOTH))); // 부기 이미지 크기 조정
        gbc.gridx = 0;
        gbc.gridy = 1;
        infoPanel.add(boogiLabel, gbc);

        // 퇴치 부기 수
        finalScoreLabel = new JLabel("퇴치 부기 수: 0", SwingConstants.LEFT);
        finalScoreLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        finalScoreLabel.setForeground(Color.WHITE);
        gbc.gridx = 1;
        gbc.gridy = 1;
        infoPanel.add(finalScoreLabel, gbc);

        this.add(infoPanel, BorderLayout.CENTER);

        // 하단 버튼 패널
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        buttonPanel.setOpaque(false); // 버튼 패널 배경 투명화

        // "Restart" 버튼
        restartButton = createHoverEffectButton("Restart");
        restartButton.addActionListener(e -> restartGame());
        buttonPanel.add(restartButton);

        // "Main Menu" 버튼
        backToMainButton = createHoverEffectButton("Main Menu");
        backToMainButton.addActionListener(e -> backToMainMenu());
        buttonPanel.add(backToMainButton);

        // "Exit" 버튼
        exitButton = createHoverEffectButton("Exit");
        exitButton.addActionListener(e -> saveScoreAndExit());
        buttonPanel.add(exitButton);

        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension d = getSize();
        g.drawImage(img, 0, 0, d.width, d.height, this); // 배경 이미지 그리기
    }

    public void updateFinalScore(int score) {
        finalScoreLabel.setText("퇴치 부기 수: " + score); // 최종 점수 업데이트
    }

    private void restartGame() {
        SwingUtilities.invokeLater(() -> {
            JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (currentFrame != null) {
                currentFrame.dispose();
            }
            new GameFrame(difficulty, userName);
        });
    }

    private void backToMainMenu() {
        SwingUtilities.invokeLater(() -> {
            JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            if (currentFrame != null) {
                currentFrame.dispose();
            }
            new MainGameUI();
        });
    }

    private void saveScoreAndExit() {
        saveScoreToFile(userName, finalScoreLabel.getText());
        System.exit(0);
    }

    private void saveScoreToFile(String userName, String scoreText) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("rank.txt", true))) {
            writer.write("사용자 이름: " + userName + ", " + scoreText + " (" + difficulty + ")" + "\n");
            JOptionPane.showMessageDialog(this, "이름과 점수가 저장되었습니다!", "저장 완료", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "점수를 저장하는 중 오류가 발생했습니다.", "저장 실패", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private JButton createHoverEffectButton(String text) {
        JButton button = new JButton(text);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        button.setForeground(Color.WHITE);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setFont(new Font("맑은 고딕", Font.BOLD, 28));
                button.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setFont(new Font("맑은 고딕", Font.BOLD, 20));
                button.setForeground(Color.WHITE);
            }
        });

        return button;
    }
}
