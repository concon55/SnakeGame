package snakegame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Gameplay extends JPanel implements KeyListener, ActionListener{
	
	private static boolean gameStart = false; //checks if the game is currently being played or if game over
	private boolean init = true; //checks if it is currently the first move
	private Direction direction; //a check of key pressed and associated direction (prevents quickly pressing keys)
	
	//maybe use linkedlist instead?
	private int[] snakexlength = new int[750];
	private int[] snakeylength = new int[750];
	
	//checks for direction
	private boolean left= false;
	private boolean right= false;
	private boolean up= false;
	private boolean down= false;
	
	//image variables
	private ImageIcon titleImage;
	private ImageIcon rightmouth;
	private ImageIcon leftmouth;
	private ImageIcon upmouth;
	private ImageIcon downmouth;
	private ImageIcon body;
	private ImageIcon food;
	
	//initial length of snake
	private int lengthOfSnake = 3;
	
	//timer
	private Timer timer;
	private int delay = 100;
	
	//scores
	private int moves = 0;
	private int score = 0;
	private int highScore=0;

	//possible positions for the food
	private int[] enemyxpos = {25, 50, 75 ,100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 
	                           350, 375, 400, 425, 450, 475, 500, 525, 550, 575, 600, 625, 650,
	                           675, 700, 725, 750, 775, 800, 825, 850};
	private int[] enemyypos = {75 ,100, 125, 150, 175, 200, 225, 250, 275, 300, 325, 
	                           350, 375, 400, 425, 450, 475, 500, 525, 550, 575, 600, 625};
	
	//generate random position of the food
	private Random random = new Random();
	private int xpos = random.nextInt(34);
	private int ypos = random.nextInt(23);
	
	//check for collision
	private boolean crash = false;
	
	/**
	 * Constructor of Gameplay
	 */
	public Gameplay(){
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}
	
	/**
	 * paints the graphic of the game screen 
	 * @param g Graphics object
	 */
	public void paint(Graphics g){
		gameStart = true;
		if(moves==0){ //initial position of snake
			snakexlength[2] = 350;
			snakexlength[1] = 375;
			snakexlength[0] = 400;
			
			snakeylength[2] = 300;
			snakeylength[1] = 300;
			snakeylength[0] = 300;
		}
		
		//draw title image border
		g.setColor(Color.white);
		g.fillRect(24, 10, 851, 60);
		
		//draw title image
		titleImage = new ImageIcon("title.png");
		titleImage.paintIcon(this, g, 220, 10);
		
		//draw border
		g.setColor(Color.WHITE);
		g.drawRect(24, 74, 851, 577);
		
		//draw background
		g.setColor(Color.BLACK);
		g.fillRect(25, 75, 850, 575);
		
		//draw score
		g.setColor(Color.black);
		g.setFont(new Font("arial", Font.PLAIN, 14));
		g.drawString("Score: "+score, 780, 30);
		
		//draw high score
		g.setColor(Color.blue);
		g.setFont(new Font("arial", Font.PLAIN, 14));
		g.drawString("High Score: "+highScore, 780, 45);
		
		//draw length
		g.setColor(Color.black);
		g.setFont(new Font("arial", Font.PLAIN, 14));
		g.drawString("Length: "+lengthOfSnake, 780, 60);
		
		//start game with right mouth
		if(init){
			rightmouth = new ImageIcon("right.png");
			rightmouth.paintIcon(this, g, snakexlength[0], snakeylength[0]);
		}
		
		//display snake mouth image according to direction, display body image
		for(int a=0; a<lengthOfSnake; a++){
			if(a==0){
				if(right){
					rightmouth = new ImageIcon("right.png");
					rightmouth.paintIcon(this, g, snakexlength[a], snakeylength[a]);
				}
				if(left){
					leftmouth = new ImageIcon("left.png");
					leftmouth.paintIcon(this, g, snakexlength[a], snakeylength[a]);
				}
				if(down){
					downmouth = new ImageIcon("down.png");
					downmouth.paintIcon(this, g, snakexlength[a], snakeylength[a]);
				}
				if(up){
					upmouth = new ImageIcon("up.png");
					upmouth.paintIcon(this, g, snakexlength[a], snakeylength[a]);
				}
			}
			if(a!=0){
				body = new ImageIcon("body.png");
				body.paintIcon(this, g, snakexlength[a], snakeylength[a]);
			}
		}
		
		//generate random position for food
		food = new ImageIcon("food.png");
		if((enemyxpos[xpos] == snakexlength[0]) && (enemyypos[ypos] == snakeylength[0])){
			score++;
			lengthOfSnake++;
			xpos = random.nextInt(34);
			ypos = random.nextInt(23);
		}
		food.paintIcon(this, g, enemyxpos[xpos], enemyypos[ypos]);
		
		//check if snake crashes into itself or to wall
		for(int i = 1; i<lengthOfSnake; i++){
			if((snakexlength[i]==snakexlength[0] && snakeylength[i]==snakeylength[0]) ||
					crash){
				right=false;
				left=false;
				up=false;
				down=false;
				init = true;
				highScore= Math.max(highScore, score);
				score=0;
				gameStart = false;
				g.setColor(Color.white);
				g.setFont(new Font("arial", Font.BOLD, 50));
				g.drawString("Game Over", 300, 300);
				g.setFont(new Font("arial", Font.BOLD, 20));
				g.drawString("Space to RESTART", 350, 340);
			}
		}
		
		g.dispose();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	/**
	 * Key pressed events, update direction booleans
	 * @param e KeyEvent
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			moves=0;
			highScore= Math.max(highScore, score);
			score=0;
			lengthOfSnake = 3;
			gameStart = true;
			crash=false;
			init = true;
			left=false;
			right=false;
			up=false;
			down=false;
			this.direction = Direction.Right;
			repaint();
		}
		if(gameStart){
			if(e.getKeyCode()==KeyEvent.VK_RIGHT){
				moves++;
				right=true;
				if(this.direction != Direction.Left && !left){
					right=true;
				}else{
					right=false;
					left=true;
				}
				up=false;
				down=false;
				init=false;
			}
			if(e.getKeyCode()==KeyEvent.VK_LEFT && init==false){
				moves++;
				left=true;
				if(this.direction != Direction.Right && !right){
					left=true;
				}else{
					left=false;
					right=true;
				}
				up=false;
				down=false;
				init=false;
			}
			if(e.getKeyCode()==KeyEvent.VK_UP){
				moves++;
				up=true;
				if(this.direction != Direction.Down && !down){
					up=true;
				}else{
					up=false;
					down=true;
				}
				left=false;
				right=false;
				init=false;
			}
			if(e.getKeyCode()==KeyEvent.VK_DOWN){
				moves++;
				down=true;
				if(this.direction != Direction.Up && !up){
					down=true;
				}else{
					down=false;
					up=true;
				}
				left=false;
				right=false;
				init=false;
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	/**
	 * Updates snake position according to key pressed event
	 * @param e ActionEvent
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		if(right){
			this.direction=Direction.Right;
			for(int r=lengthOfSnake-1; r>=0; r--){
				snakeylength[r+1] = snakeylength[r];
			}
			for(int r=lengthOfSnake; r>=0; r--){
				if(r==0){
					snakexlength[r] = snakexlength[r] + 25;
				}else{
					snakexlength[r] = snakexlength[r-1];
				}
				if(snakexlength[r]>850){
					snakexlength[r]=850;
					crash=true;
				}
			}
			repaint();
		}
		if(left){
			this.direction=Direction.Left;
			for(int r=lengthOfSnake-1; r>=0; r--){
				snakeylength[r+1] = snakeylength[r];
			}
			for(int r=lengthOfSnake; r>=0; r--){
				if(r==0){
					snakexlength[r] = snakexlength[r] - 25;
				}else{
					snakexlength[r] = snakexlength[r-1];
				}
				if(snakexlength[r]<25){
					snakexlength[r]=25;
					crash=true;
				}
			}
			repaint();
		}
		if(down){
			this.direction=Direction.Down;
			for(int r=lengthOfSnake-1; r>=0; r--){
				snakexlength[r+1] = snakexlength[r];
			}
			for(int r=lengthOfSnake; r>=0; r--){
				if(r==0){
					snakeylength[r] = snakeylength[r] + 25;
				}else{
					snakeylength[r] = snakeylength[r-1];
				}
				if(snakeylength[r]>625){
					snakeylength[r]=625;
					crash=true;
				}
			}
			repaint();
		}
		if(up){
			this.direction=Direction.Up;
			for(int r=lengthOfSnake-1; r>=0; r--){
				snakexlength[r+1] = snakexlength[r];
			}
			for(int r=lengthOfSnake; r>=0; r--){
				if(r==0){
					snakeylength[r] = snakeylength[r] - 25;
				}else{
					snakeylength[r] = snakeylength[r-1];
				}
				if(snakeylength[r]<75){
					snakeylength[r]=75;
					crash=true;
				}
			}
			repaint();
		}
		
	}
}
