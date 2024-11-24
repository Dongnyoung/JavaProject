import java.awt.*;



import javax.swing.*;
public class GamePanel extends JPanel{
	private Image backgroundImage;
	private ScorePanel scorePanel =null;
	private JTextField text = new JTextField(10);
	
	//부기+단어의 전체사이즈
	private final int monsterWidth = 100;
	private final int monsterHeight =108;
			
	//부기 
	private ImageIcon boogiIcon = new ImageIcon("resource/img/boogi.jpg");
	private Image boogiImg = boogiIcon.getImage();
	private JLabel attackingLabel = new JLabel(boogiIcon);
			
			
	//단어
	private WordVector wordVec = new WordVector(); // 단어 벡터 생성
	private String word = wordVec.get(); //단어 가져오기 
	private JLabel wordLabel = new JLabel(word);
			
	//게임 배경화면 
	private ImageIcon icon  = new ImageIcon("resource/img/image.jpg");
	private Image img = icon.getImage();
			
	
	
	private AttackThread aThread = new AttackThread(); // 부기가 다가오는 스레드
	
	
	private GameGroundPanel ground = new GameGroundPanel();
	private InputPanel input = new InputPanel();
	
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
			int locationY = (int)(Math.random()*200);
			setLayout(null);
			
			wordLabel.setForeground(Color.white);
			wordLabel.setFont(new Font("Arial",Font.BOLD,20));
			wordLabel.setSize(monsterWidth,20);
			wordLabel.setLocation(700,monsterHeight+locationY);
			wordLabel.setVisible(false);
			add(wordLabel);
			
			attackingLabel.setSize(monsterWidth,monsterHeight);
			attackingLabel.setLocation(700,locationY);
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
					wordLabel.setLocation(wordLabel.getX()-10,wordLabel.getY());
					attackingLabel.setLocation(attackingLabel.getX()-10,attackingLabel.getY());
					sleep(400);
				} catch (InterruptedException e) {
					return;
				}
			}
		}
	}
	
	public void startGame() {
		wordLabel.setVisible(true);
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
