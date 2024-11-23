import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.*;

public class GameFrame extends JFrame{
	
	private ScorePanel scorePanel = new ScorePanel();
	private SkillPanel skillPanel = new SkillPanel();
	private GamePanel gamePanel = new GamePanel(scorePanel);
	
	//private MonsterPanel monsterPanel = new MonsterPanel();
	public GameFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setTitle("게임");
		setSize(1000,600);
		
		makeMenu();
		makeSplit();
		this.setResizable(false);
		setVisible(true);
	}
	
	private void makeSplit() {
		JSplitPane hPane = new JSplitPane();
		hPane.setDividerLocation(800);
		getContentPane().add(hPane, BorderLayout.CENTER);
		hPane.setEnabled(false);

		JSplitPane vPane = new JSplitPane();
		vPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		vPane.setDividerLocation(400);
		vPane.setTopComponent(scorePanel);
		vPane.setBottomComponent(skillPanel);
		
		hPane.setRightComponent(vPane);
		hPane.setLeftComponent(gamePanel);
		vPane.setEnabled(false);

	}
	private void makeMenu() {
		JMenuBar mb = new JMenuBar();
		this.setJMenuBar(mb);
		
		JMenu fileMenu = new JMenu("File");
		mb.add(fileMenu);
		JMenuItem startItem = new JMenuItem("Start");
		fileMenu.add(startItem);
		

			
		JMenuItem stopItem = new JMenuItem("Stop");
		fileMenu.add(stopItem);
		fileMenu.addSeparator();
		JMenuItem exitItem = new JMenuItem("Exit");
		fileMenu.add(exitItem);
		
		
		JMenu editMenu = new JMenu("Edit");
		mb.add(editMenu);
		
	}
	
	public static void main(String[] args) {
		new GameFrame();
	}

}
