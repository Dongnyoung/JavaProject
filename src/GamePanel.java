import java.awt.*;



import javax.swing.*;
public class GamePanel extends JPanel{
	
	private ScorePanel scorePanel =null;
	private JTextField text = new JTextField(10);
	

	private GameGroundPanel ground = new GameGroundPanel();
	
	private InputPanel input = new InputPanel();
	
	public GamePanel(ScorePanel scorePanel) {
		
		this.scorePanel = scorePanel;
		this.setLayout(new BorderLayout());
		add(ground,BorderLayout.CENTER);
		add(input,BorderLayout.SOUTH);
	}
	class GameGroundPanel extends JPanel{
		public GameGroundPanel() {
			setLayout(null);
			
		}
		
	}
	
	
	
	class InputPanel extends JPanel{
		public InputPanel() {
			this.setBackground(Color.LIGHT_GRAY);
			add(text);
			
				
		}
	}
}
