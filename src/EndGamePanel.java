import java.awt.*;
import javax.swing.*;

class EndGamePanel extends JPanel {
    private JLabel finalScoreLabel;
    private JButton restartButton;
    private JButton backToMainButton; // 게임 첫 화면으로 돌아가는 버튼
    private ImageIcon icon = new ImageIcon("resource/img/image.jpg");
    private Image img = icon.getImage();

    public EndGamePanel() {
        this.setLayout(new BorderLayout());

        // 상단: 최종 점수 레이블
        finalScoreLabel = new JLabel("퇴치부기 수 : 0", SwingConstants.CENTER);
        finalScoreLabel.setFont(new Font("맑은 고딕", Font.BOLD, 40)); // 더 크고 두꺼운 폰트
        finalScoreLabel.setForeground(Color.WHITE); // 흰색 글씨로 변경
        this.add(finalScoreLabel, BorderLayout.CENTER);

        // 하단 버튼 패널
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        buttonPanel.setOpaque(false); // 버튼 패널 배경 투명화

        // "Restart" 버튼
        restartButton = createHoverEffectButton("Restart");
        restartButton.addActionListener(e -> restartGame());
        buttonPanel.add(restartButton);

        // "Main Menu" 버튼
        backToMainButton = createHoverEffectButton("Main Menu");
        backToMainButton.addActionListener(e -> backToMainMenu());
        buttonPanel.add(backToMainButton);

        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // 기본 페인트 동작 호출
        Dimension d = getSize();
        g.drawImage(img, 0, 0, d.width, d.height, this); // 배경 이미지 그리기
    }

    public void updateFinalScore(int score) {
        finalScoreLabel.setText("퇴치부기 수 : " + score); // 최종 점수 업데이트
    }

    private void restartGame() {
        // 게임 재시작
        new GameFrame();
        
    }

    private void backToMainMenu() {
        // 게임 첫 화면으로 돌아가기
        new MainGameUI();
        
    }

    // Hover 기능을 추가한 버튼 생성
    private JButton createHoverEffectButton(String text) {
        JButton button = new JButton(text);
        button.setContentAreaFilled(false); // 버튼 배경 투명
        button.setFocusPainted(false); // 포커스 테두리 제거
        button.setBorderPainted(false); // 버튼 테두리 제거
        button.setFont(new Font("맑은 고딕", Font.BOLD, 20)); // 기본 글꼴 설정
        button.setForeground(Color.WHITE); // 기본 글자 색상 설정

        // 마우스 이벤트 추가
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setFont(new Font("맑은 고딕", Font.BOLD, 28)); // 글꼴 크기 증가
                button.setForeground(Color.YELLOW); // 텍스트 색상 변경
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setFont(new Font("맑은 고딕", Font.BOLD, 20)); // 기본 글꼴로 복원
                button.setForeground(Color.WHITE); // 기본 텍스트 색상 복원
            }
        });

        return button;
    }
}
