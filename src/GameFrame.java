import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;

public class GameFrame extends JFrame {
    private String difficulty;
    private String userName;
    private ScorePanel scorePanel = new ScorePanel();
    private LifePanel skillPanel = new LifePanel();
    private EndGamePanel endPanel; // 생성자에서 초기화
    private GamePanel gamePanel;  // 생성자에서 초기화

    public GameFrame(String difficulty,String userName) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.difficulty = difficulty;
        this.userName=userName;
        this.endPanel = new EndGamePanel(this, difficulty,userName); // endPanel 초기화
        this.gamePanel = new GamePanel(scorePanel, skillPanel, endPanel, difficulty,userName); // gamePanel 초기화
        
        setTitle("Kill the Boogi Game");
        setSize(1000, 700);
        setLocationRelativeTo(null); // 화면 중앙에 표시
        makeMenu();
        makeSplit();
        this.setResizable(false);
        setVisible(true);
        
        //디버깅을 위한
        //System.out.println("Difficulty in GameFrame: " + difficulty);
        //System.out.println("user in GameFrame: " + userName);
        
        gamePanel.startGame();
    }

    //창나누기
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
        
        /*
        // 게임 중단 버튼
        JMenuItem stopItem = new JMenuItem("Stop Game");
        stopItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Game Stopped");
                gamePanel.stopGame(); // 게임 중단 로직 추가
            }
        });
        mb.add(stopItem);
        // 게임 재개 버튼
        JMenuItem resumeItem = new JMenuItem("Resume Game");
        resumeItem.addActionListener(e -> gamePanel.resumeGame());
        mb.add(resumeItem);
        */
        
        // 게임 종료 버튼
        JMenuItem exitItem = new JMenuItem("Not Save Exit");
        exitItem.setIcon(new javax.swing.ImageIcon("resource/img/exit.jpg")); // 아이콘 추가
        exitItem.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 14)); // 폰트 설정
        exitItem.setBackground(java.awt.Color.RED); // 배경색 설정
        exitItem.setForeground(java.awt.Color.WHITE); // 텍스트 색상 설정
        exitItem.setOpaque(true); // 배경색 보이도록 설정
        exitItem.setBorderPainted(false); // 테두리 제거

        // 종료 버튼 동작
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = javax.swing.JOptionPane.showConfirmDialog(
                    GameFrame.this,
                    "저장하지 않고 나가시겠습니까?",
                    "Exit Game",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.WARNING_MESSAGE
                );
                if (response == javax.swing.JOptionPane.YES_OPTION) {
                    System.out.println("Game Exited");
                    System.exit(0);
                    dispose(); // 창 닫기
                }
            }
        });

        mb.add(exitItem);
    }
}
