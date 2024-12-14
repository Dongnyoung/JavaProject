// EndGamePanel 클래스

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

class EndGamePanel extends JPanel {
    private JLabel finalScoreLabel;
    private JButton restartButton;
    private ImageIcon icon = new ImageIcon("resource/img/image.jpg");
    private Image img = icon.getImage();
    public EndGamePanel() {
        this.setLayout(new BorderLayout());

        finalScoreLabel = new JLabel("퇴치부기 수 : 0", SwingConstants.CENTER);
        finalScoreLabel.setFont(new Font("맑은 고딕", Font.BOLD, 32));
        this.add(finalScoreLabel, BorderLayout.CENTER);

        restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> restartGame());
        this.add(restartButton, BorderLayout.SOUTH);
        
    }
    public void paintComponent(Graphics g) {
        Dimension d = getSize();
        g.drawImage(img, 0, 0, d.width, d.height, this);
    }


    public void updateFinalScore(int score) {
        finalScoreLabel.setText("퇴치부기 수 : " + score);
    }

    private void restartGame() {
        // Restart
    	new GameFrame();
    }
}