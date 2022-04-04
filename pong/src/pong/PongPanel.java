package pong;
import java.awt.Color;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Font;


import javax.swing.JPanel;

public class PongPanel extends JPanel implements ActionListener, KeyListener {

	 private final static Color BACKGROUND_COLOUR = Color.BLUE;
	private static final int TIMER_DELAY = 3;
	//boolean gameInitialised = false;
	Ball ball;
	GameState gameState = GameState.Initialising;
	Paddle paddle1, paddle2;
	private static final int BALL_MOVEMENT_SPEED = 2;
	private final static int POINTS_TO_WIN = 3;
	  int player1Score = 0, player2Score = 0;
	  Player gameWinner;
	  
	  private void addScore(Player player) {
          if(player == Player.One) {
              player1Score++;
          } else if(player == Player.Two) {
              player2Score++;
          }
      }
	  private void checkWin() {
          if(player1Score >= POINTS_TO_WIN) {
              gameWinner = Player.One;
              gameState = GameState.GameOver;
          } else if(player2Score >= POINTS_TO_WIN) {
              gameWinner = Player.Two;
              gameState = GameState.GameOver;
          }
      }
	
	public void createObjects() {
        ball = new Ball(getWidth(), getHeight());
        paddle1 = new Paddle(Player.One, getWidth(), getHeight());
        paddle2 = new Paddle(Player.Two, getWidth(), getHeight());
        
       
 }
 
	private void update() {
        switch(gameState) {
            case Initialising: {
                createObjects();
                gameState = GameState.Playing;
                ball.setxVelocity(BALL_MOVEMENT_SPEED);
                ball.setyVelocity(BALL_MOVEMENT_SPEED);
                break;
            }
           case Playing: {
               moveObject(paddle1);
               moveObject(paddle2);
               moveObject(ball);            // Move ball
               checkWallBounce(); 
               checkPaddleBounce();
               checkWin();// Check for wall bounce
               break;
           }
           case GameOver: {
               break;
           }
       }
   }

@Override
   public void keyPressed(KeyEvent event) {
       if(event.getKeyCode() == KeyEvent.VK_W) {
           paddle1.setyVelocity(-1);
       } else if(event.getKeyCode() == KeyEvent.VK_S) {
           paddle1.setyVelocity(1);
       }
       if(event.getKeyCode() == KeyEvent.VK_UP) {
           paddle2.setyVelocity(-1);
       } else if(event.getKeyCode() == KeyEvent.VK_DOWN) {
           paddle2.setyVelocity(1);
       }
   }

   @Override
   public void keyReleased(KeyEvent event) {
       if(event.getKeyCode() == KeyEvent.VK_W || event.getKeyCode() == KeyEvent.VK_S) {
           paddle1.setyVelocity(0);
       }
       if(event.getKeyCode() == KeyEvent.VK_UP || event.getKeyCode() == KeyEvent.VK_DOWN) {
           paddle2.setyVelocity(0);
       }
   }
 
 private void paintScores(Graphics g) {
     int xPadding = 100;
     int yPadding = 100;
     int fontSize = 50; 
     Font scoreFont = new Font("Serif", Font.BOLD, fontSize);
     String leftScore = Integer.toString(player1Score);
     String rightScore = Integer.toString(player2Score);
     g.setFont(scoreFont);
     g.drawString(leftScore, xPadding, yPadding);
    g.drawString(rightScore, getWidth()-xPadding, yPadding);
}
 
 private void checkPaddleBounce() {
	 if(ball.getxVelocity() < 0 && ball.getRectangle().intersects(paddle1.getRectangle())) {
         ball.setxVelocity(BALL_MOVEMENT_SPEED);
     }
     if(ball.getxVelocity() > 0 && ball.getRectangle().intersects(paddle2.getRectangle())) {
         ball.setxVelocity(-BALL_MOVEMENT_SPEED);
 }
     }
 
 private void moveObject(Sprite obj) {
	 obj.setxPosition(obj.getxPosition() + obj.getxVelocity(), getWidth());
	 obj.setyPosition(obj.getyPosition() + obj.getxVelocity(), getHeight());
 }
 
 private void checkWallBounce() {
     if(ball.getxPosition() <= 0) {
         // Hit left side of screen
         ball.setxVelocity(-ball.getxVelocity());
         addScore(Player.Two);
         resetBall();
     } else if(ball.getxPosition() >= getWidth() - ball.getWidth()) {
         // Hit right side of screen
         ball.setxVelocity(-ball.getxVelocity());
         addScore(Player.One);
         resetBall();
     }
     if(ball.getyPosition() <= 0 || ball.getyPosition() >= getHeight() - ball.getHeight()) {
         // Hit top or bottom of screen
         ball.setyVelocity(-ball.getyVelocity());
     }
 }
	 private void resetBall() {
		 ball.resetToInitialPosition();
	 }
	 public PongPanel() {
         setBackground(BACKGROUND_COLOUR);
         Timer timer = new Timer(TIMER_DELAY, this);
         timer.start();
         addKeyListener(this);
         setFocusable(true);
     }
	 
	 private void paintSprite(Graphics g, Sprite sprite) {
	      g.setColor(sprite.getColour());
	      g.fillRect(sprite.getxPosition(), sprite.getyPosition(), sprite.getWidth(), sprite.getHeight());
	 
	 }
	 @Override
	 public void paintComponent(Graphics g) {
	     super.paintComponent(g);
	     paintDottedLine(g);
	     //g.setColor(Color.WHITE);
	     //g.fillRect(20, 20, 100, 100);
	     if(gameState != GameState.Initialising) {
             paintSprite(g, ball);
             paintSprite(g, paddle1);
             paintSprite(g, paddle2);
             paintScores(g);
         }
	 
	 }
	 

	 
	 private void paintDottedLine(Graphics g) {
	      Graphics2D g2d = (Graphics2D) g.create();
	         Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
	         g2d.setStroke(dashed);
	         g2d.setPaint(Color.WHITE);
	         g2d.drawLine(getWidth() / 2, 0, getWidth() / 2, getHeight());
	         g2d.dispose();
	 }
	 
	@Override
	public void keyTyped(KeyEvent event) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		update();
		repaint();
	}

}
