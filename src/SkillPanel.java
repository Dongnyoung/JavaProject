import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
public class SkillPanel extends JPanel {
	
	 private int lives = 5; // 초기 목숨 개수
	 private JLabel[] lifeLabels; // 목숨을 표시할 레이블 배열
	    private Image backgroundImage = new ImageIcon("resource/img/lifeBackground.jpg").getImage(); // 배경 이미지 경로

	 
	 protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        // 배경 이미지 그리기
	        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
	    }
	 public SkillPanel() {
	        this.setBackground(Color.CYAN);
	        this.setLayout(new FlowLayout(FlowLayout.LEFT)); // 목숨 표시 정렬

	        lifeLabels = new JLabel[lives]; // 배열 초기화

	        // 초기 목숨 설정
	        for (int i = 0; i < lives; i++) {
	            lifeLabels[i] = new JLabel(new ImageIcon("resource/img/heart.jpg")); // 하트 아이콘 사용
	            lifeLabels[i].setSize(50,50);
	            this.add(lifeLabels[i]);
	        }
	 }
	 
	    // 목숨 감소 메서드
	 public void loseLife() {
	        if (lives > 0) {
	            lives--; // 목숨 감소
	            lifeLabels[lives].setVisible(false); // 배열에서 해당 레이블 숨김 처리
	        }

	        if (lives == 0) {
	            SwingUtilities.invokeLater(() -> {
	                JOptionPane.showMessageDialog(this, "Game Over!", "Game Over", JOptionPane.WARNING_MESSAGE);
	            });
	        }
	    }

	    public int getLives() {
	        return lives;
	    }
}

