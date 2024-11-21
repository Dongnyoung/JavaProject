import java.awt.*;
import javax.swing.*;
public class ScorePanel extends JPanel { //점수판
	private int score =0;
	private JLabel scoreLabel=new JLabel(Integer.toString(score));

	public ScorePanel() {
		this.setBackground(Color.YELLOW);
		this.add(new JLabel("점수: "));
		this.add(scoreLabel);
	}
	/* 점수판의 함수들 구현 
	public void increase() {
	
		
	}
	*/
}
