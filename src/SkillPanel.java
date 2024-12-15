import java.awt.*;
import javax.swing.*;

public class SkillPanel extends JPanel {

    private int lives = 5; // 초기 목숨 개수
    private JLabel[] lifeLabels; // 목숨을 표시할 레이블 배열
    private JLabel wizardLabel; // 마법사 이미지 레이블
    
    private Image backgroundImage = new ImageIcon("resource/img/lifeBackground.jpg").getImage(); // 배경 이미지 경로

    public SkillPanel() {
        // Panel 기본 레이아웃 설정
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); // 세로로 배치
        this.setPreferredSize(new Dimension(600, 150)); // SkillPanel 크기 조정

        // 마법사 이미지패널
        JPanel wizardPanel = new JPanel();
        wizardPanel.setLayout(new BoxLayout(wizardPanel, BoxLayout.Y_AXIS)); // 세로로 배치
        wizardPanel.setOpaque(false); // 배경 투명

        // 마법사 이미지 추가
        wizardLabel = new JLabel(new ImageIcon(new ImageIcon("resource/img/wizard.jpg")
                .getImage()
                .getScaledInstance(80, 80, Image.SCALE_SMOOTH))); // 이미지 크기 조정
        wizardLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 중앙 정렬
        wizardPanel.add(wizardLabel);

        

        // 목숨 표시 패널
        JPanel lifePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5)); // 목숨 배치 정렬
        lifePanel.setOpaque(false); // 배경 투명

        lifeLabels = new JLabel[lives];
        for (int i = 0; i < lives; i++) {
            lifeLabels[i] = new JLabel(new ImageIcon(new ImageIcon("resource/img/heart.jpg")
                    .getImage()
                    .getScaledInstance(30, 30, Image.SCALE_SMOOTH))); // 하트 이미지 크기 조정
            lifePanel.add(lifeLabels[i]);
        }

        // 패널 배치
        this.add(wizardPanel); // 위쪽에 마법사 이미지와 이름 추가
        this.add(Box.createRigidArea(new Dimension(0, 10))); // 마법사 패널과 목숨 패널 사이 간격 추가
        this.add(lifePanel); // 아래쪽에 목숨 패널 추가
    }

    // 목숨 감소 메서드
    public void loseLife() {
        if (lives > 0) {
            lives--; // 목숨 감소
            lifeLabels[lives].setVisible(false); // 해당 목숨 레이블 숨김
        }
    }

    public int getLives() {
        return lives;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 배경 이미지 그리기
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
