import java.awt.*;
import javax.swing.*;

public class ScorePanel extends JPanel { // 점수판
    public int score = 0;
    //public int cannotKill = 0;
    private JLabel killScoreLabel = new JLabel(Integer.toString(score));
    //private JLabel notKillScoreLabel = new JLabel(Integer.toString(cannotKill));

    public ScorePanel() {
        this.setBackground(Color.YELLOW);
        this.setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.YELLOW);
        leftPanel.add(new JLabel("퇴치성공: "));
        leftPanel.add(killScoreLabel);

        /*JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.YELLOW);
        rightPanel.add(new JLabel("퇴치실패: "));
        rightPanel.add(notKillScoreLabel);

         */
        this.add(leftPanel, BorderLayout.WEST);
        
        //this.add(rightPanel, BorderLayout.EAST);
    }

    // 점수판 구현
    public void boogiKillscore() {
        score++;
        killScoreLabel.setText(Integer.toString(score));
    }
    /*
    public void boogicannotKill() {
        cannotKill++;
        notKillScoreLabel.setText(Integer.toString(cannotKill));
    }
    */
}
