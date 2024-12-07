import java.awt.*;
import javax.swing.*;

public class ScorePanel extends JPanel { // 점수판
    public int score = 0;
    private JLabel killScoreLabel = new JLabel();
    private ImageIcon boogiIcon = new ImageIcon("resource/img/boogi.jpg"); // 작은 부기 이미지 파일 경로
    private ImageIcon backgroundImage = new ImageIcon("resource/img/scoreBackground.jpg"); // 배경 이미지 경로
    private Image backImg = backgroundImage.getImage();
    public ScorePanel() {
        this.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setOpaque(false); // 배경 투명 처리
        leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        JLabel iconLabel = new JLabel(boogiIcon); // 부기 이미지
        leftPanel.add(iconLabel);

        killScoreLabel.setFont(new Font("Arial", Font.BOLD, 20)); // 점수 텍스트 크기 설정
        leftPanel.add(killScoreLabel);

        this.add(leftPanel, BorderLayout.WEST);

        updateScore(); // 초기 점수 업데이트
    }

    @Override
    protected void paintComponent(Graphics g) {
        
        // 배경 이미지 그리기
        g.drawImage(backImg, 0, 0, getWidth(), getHeight(), this);
    }

    // 점수판 구현
    public void boogiKillscore() {
        score++;
        updateScore();
    }

    // 점수 업데이트 메서드
    private void updateScore() {
        killScoreLabel.setText(Integer.toString(score));
    }
}
