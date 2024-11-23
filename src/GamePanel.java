import java.awt.*;



import javax.swing.*;
public class GamePanel extends JPanel{
	private Image backgroundImage;
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
		//게임 배경화면 
		private ImageIcon icon  = new ImageIcon("resource/img/image.jpg");
		private Image img = icon.getImage();
		public void paintComponent(Graphics g) {
			
			Dimension d = getSize();
			g.drawImage(img, 0,0,d.width,d.height,this);
			
			
		}
		public GameGroundPanel() {
	        
	      
			setLayout(null);

			
		}
		
		
	}
	
	
	
	
	class InputPanel extends JPanel{
		public InputPanel() {
			this.setBackground(Color.GREEN);
			add(text);
			
				
		}
	}
}
