import java.awt.*;



import javax.swing.*;
public class GamePanel extends JPanel{
	private Image backgroundImage;
	private ScorePanel scorePanel =null;
	private JTextField text = new JTextField(10);
	
	
	//부기 
	private ImageIcon boogiIcon = new ImageIcon("resource/img/boogi.jpg");
	private Image boogiImg = boogiIcon.getImage();
	private JLabel attackingLabel = new JLabel(boogiIcon);
	
	
	//게임 배경화면 
	private ImageIcon icon  = new ImageIcon("resource/img/image.jpg");
	private Image img = icon.getImage();
	private GameGroundPanel ground = new GameGroundPanel();
	private InputPanel input = new InputPanel();
	
	
	private AttackThread aThread = new AttackThread(); // 부기가 다가오는 스레드
	
	
	public GamePanel(ScorePanel scorePanel) {
			
		this.scorePanel = scorePanel;
		this.setLayout(new BorderLayout());
		add(ground,BorderLayout.CENTER);
		add(input,BorderLayout.SOUTH);
		
	}
	
	class GameGroundPanel extends JPanel{
		
		public void paintComponent(Graphics g) {
			
			Dimension d = getSize();
			g.drawImage(img, 0,0,d.width,d.height,this);
			
			
		}
		public GameGroundPanel() {
			setLayout(null);
			
			attackingLabel.setBounds(700, 200, 100, 100);
			attackingLabel.setVisible(false);
			add(attackingLabel);
			
		}
		
		
	}
	
	
	//부기가 다가오는(공격)스레드 
	class AttackThread extends Thread{
		@Override
		public void run() {
			while(true) {
				try {
					attackingLabel.setLocation(attackingLabel.getX()-10,attackingLabel.getY());
					sleep(400);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}
	
	public void startGame() {
		attackingLabel.setVisible(true);
		aThread.start();
	}
	
	class InputPanel extends JPanel{
		public InputPanel() {
			this.setBackground(Color.GREEN);
			add(text);
			
				
		}
	}
}
