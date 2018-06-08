import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.util.ArrayList;

public class MazePanel extends JPanel implements KeyListener, ActionListener, MouseListener{
	private int width;
	private int height;
	private Tile[][] maze;
	ArrayList<ArrayList<Integer>> changedTiles;
	private int playerY;
	private int playerX;
	public int tileSize;
	public Dimension screenSize;
	public boolean first;
	private boolean gameOver;
	private boolean inStartMenu;
	private boolean addedButton;
	private boolean justSwitched;
    //construct a PongPanel
    public MazePanel(int width, int height, Tile[][] maze){
        setBackground(Color.WHITE);
        this.width = width;
        this.height = height;
        this.maze = maze;
        this.playerY = 0;
        this.playerX = 0;
        this.gameOver = false;
        this.addedButton = false;
        this.inStartMenu = true;
        this.justSwitched = false;
        this.setFocusable(true);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.changedTiles = new ArrayList<>();
        for (int i = 0; i < maze.length; i++) {
        	for (int j = 0; j < maze[0].length; j++) {
        		ArrayList<Integer> temp = new ArrayList<>();
        		temp.add(i);
        		temp.add(j);
        		this.changedTiles.add(temp);
        	}
        }
        this.first = true;

    }

    //paint a ball
    public void paintComponent(Graphics g){
        screenSize = Toolkit.getDefaultToolkit().getScreenSize(); //get screen dimensions
        double scalingFactor = .75;
        int side = Math.min(screenSize.height, screenSize.width);
        if (this.inStartMenu) {
        	if (!this.addedButton) {
        		JButton playButton = new JButton("Play Game");
        		playButton.addActionListener(new MazeListener(this));
        		playButton.setBounds((int)(side*.25), (int)(side*.25), (int)(side*.15), (int)(side*.15));
        	
        		this.add(playButton);
        		addedButton = true;
        	}
        }
        else if (this.gameOver) {
        	super.paintComponent(g);
        	g.clearRect((int)(side*.5), (int)(side*.5), (int)(side*.75), (int)(side*.75));
			Font f = g.getFont().deriveFont(30f);
			g.setFont(f);
			g.drawString("You won!",(int)(screenSize.width*.5),(int)(screenSize.height*.5));
        }
        else {
        	//for (Component component: this.getComponents()) {
        		//if (!component.getName().equals("Quit")) {
        			//this.remove(component);
        		//}
        	//}
        	
        	if (this.justSwitched) {
        		this.removeAll();
            	this.revalidate();
            	this.requestFocus();
        		super.paintComponent(g);
        		JButton exitButton = new JButton("Quit");
        		JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                exitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        topFrame.setVisible(false);
                        topFrame.dispose();
                    }
                });
                this.add(exitButton);
        		this.justSwitched = false;
        	}
	        //use the screen size to determine the ideal tile size
	        if (screenSize.height > screenSize.width) {
	        	if (maze.length > maze[0].length) {
	        		tileSize = (int) (screenSize.width * scalingFactor / maze[0].length);
	        	}
	        	else {
	        		tileSize = (int) (screenSize.width * scalingFactor / maze.length);
	        	}
	        }
	        else {
	        	if (maze.length > maze[0].length) {
	        		tileSize = (int) (screenSize.height * scalingFactor / maze[0].length);
	        	}
	        	else {
	        		tileSize = (int) (screenSize.height * scalingFactor / maze.length);
	        	}
	        }
	        //Draw tiles
	        for (int row = 0; row < maze.length; row++) {
	        	for (int col = 0; col < maze[0].length; col++) {
	        		try {
	        			Image image;
	        			Toolkit t = Toolkit.getDefaultToolkit();
	        			if (maze[row][col].getType() == TileType.WALL) {
	        				image = t.getImage(getClass().getResource("/Images/wallPixelArt.jpg"));
	        			}
	        			else if (maze[row][col].getType() == TileType.DOOR && maze[row][col].isWalkable()) {
	        				image = t.getImage(getClass().getResource("/Images/openDoor.png"));
	        			}
	        			else if (maze[row][col].getType() == TileType.DOOR && !maze[row][col].isWalkable()) {
	        				image = t.getImage(getClass().getResource("/Images/closedDoor.jpg"));
	        			}
	        			else if (maze[row][col].getType() == TileType.LOCKED_DOOR) {
	        				image = t.getImage(getClass().getResource("/Images/lockedDoor.png"));
	        			}
	        			else if (maze[row][col].getType() == TileType.FLOOR) {
	        				image = t.getImage(getClass().getResource("Images/tilePixelArt.png"));
	        			}
	        			else if (maze[row][col].getType() == TileType.PORTAL) {
	        				image = t.getImage(getClass().getResource("Images/portal.png"));
	        			}
	        			else if (maze[row][col].getType() == TileType.KEY) {
	        				image = t.getImage(getClass().getResource("Images/key.png"));
	        			}
	        			else {
	        				image = t.getImage(getClass().getResource("Images/finish.png"));
	        			}
	        			//System.out.println("changedTiles.size(): " + changedTiles.size());
	        			for (int i = 0; i < changedTiles.size(); i++) {
	        				if (changedTiles.get(i).get(0) == row && changedTiles.get(i).get(1) == col) {
	        					g.drawImage(image, row*tileSize + (screenSize.width - maze.length * tileSize)/2, col*tileSize + (screenSize.height - maze[0].length * tileSize)/2, tileSize, tileSize, Color.WHITE, this);
	        				}
	        			}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        }
	        //Draw player
	        try {
				g.drawImage(ImageIO.read(getClass().getResource("/Images/kanyePixelArt.jpg")), playerY * tileSize + (screenSize.width - maze.length * tileSize)/2, playerX*tileSize + (screenSize.height - maze[0].length * tileSize)/2, (int) (tileSize * 1.00), (int) (tileSize * 1.00), this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        if (!this.first) {
	        	changedTiles.clear();
	        }
        }
        
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		//repaint();
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if (this.first) {
			changedTiles.clear();
		}
		int keyCode = e.getKeyCode();
		//System.out.println(keyCode);
	    switch( keyCode ) { 
	        case KeyEvent.VK_UP:
	            // handle up
	        	//System.out.println("Arrow key pressed");
	        	if (playerX != 0 && maze[playerY][playerX-1].isWalkable()) {
	        		ArrayList<Integer> temp = new ArrayList<>();
	        		temp.add(playerY);
	        		temp.add(playerX-1);
	        		changedTiles.add(temp);
	        		ArrayList<Integer> temp2 = new ArrayList<>();
	        		temp2.add(playerY);
	        		temp2.add(playerX);
	        		changedTiles.add(temp2);
	        		playerX--;
	        		this.first = false;
	        	}
	            break;
	        case KeyEvent.VK_DOWN:
	            // handle down 
	        	//System.out.println("Arrow key pressed");
	        	if (playerX != height - 1 && maze[playerY][playerX+1].isWalkable()) {
	        		ArrayList<Integer> temp = new ArrayList<>();
	        		temp.add(playerY);
	        		temp.add(playerX+1);
	        		changedTiles.add(temp);
	        		ArrayList<Integer> temp2 = new ArrayList<>();
	        		temp2.add(playerY);
	        		temp2.add(playerX);
	        		changedTiles.add(temp2);
	        		playerX++;
	        		this.first = false;
	        	}
	            break;
	        case KeyEvent.VK_LEFT:
	            // handle left
	        	//System.out.println("Arrow key pressed");
	        	if (playerY != 0 && maze[playerY-1][playerX].isWalkable()) {
	        		ArrayList<Integer> temp = new ArrayList<>();
	        		temp.add(playerY-1);
	        		temp.add(playerX);
	        		changedTiles.add(temp);
	        		ArrayList<Integer> temp2 = new ArrayList<>();
	        		temp2.add(playerY);
	        		temp2.add(playerX);
	        		changedTiles.add(temp2);
	        		playerY--;
	        		this.first = false;
	        	}
	            break;
	        case KeyEvent.VK_RIGHT:
	            // handle right
	        	//System.out.println("Arrow key pressed");
	        	if (playerY != width - 1 && maze[playerY+1][playerX].isWalkable()) {
	        		ArrayList<Integer> temp = new ArrayList<>();
	        		temp.add(playerY+1);
	        		temp.add(playerX);
	        		changedTiles.add(temp);
	        		ArrayList<Integer> temp2 = new ArrayList<>();
	        		temp2.add(playerY);
	        		temp2.add(playerX);
	        		changedTiles.add(temp2);
	        		playerY++;
	        		this.first = false;
	        	}
	            break;
	        case KeyEvent.VK_ENTER:
	        	//handle enter key for going through portals
	        	if (maze[playerY][playerX].getType() == TileType.PORTAL) {
	        		ArrayList<Integer> temp = new ArrayList<>();
	        		temp.add(maze[playerY][playerX].relatedRow());
	        		temp.add(maze[playerY][playerX].relatedCol());
	        		changedTiles.add(temp);
	        		ArrayList<Integer> temp2 = new ArrayList<>();
	        		temp2.add(playerY);
	        		temp2.add(playerX);
	        		changedTiles.add(temp2);
	        		playerY = maze[playerY][playerX].relatedRow();
	        		playerX = maze[playerY][playerX].relatedCol();
	        		this.first = false;
	        	}
	     }
	    if (maze[playerY][playerX].getType() == TileType.KEY) {
	    	int row = maze[playerY][playerX].relatedRow();
	    	int col = maze[playerY][playerX].relatedCol();
	    	maze[row][col].getKey();
	    	maze[playerY][playerX] = new Tile(TileType.FLOOR);
	    }
	    else if (maze[playerY][playerX].getType() == TileType.FINISH_FLAG) {
	    	this.gameOver = true;
	    }
	    if (!this.first) {
			repaint();
		}
	    //else {
	    	//this.first = false;
	    //}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//find the tile that was clicked on
		int row = 0;
		int col = 0;
		for (int i = 0; i < maze.length; i++) {
			for (int j = 0; j < maze.length; j++) {
				if (i*tileSize + (screenSize.width - maze.length * tileSize)/2 < arg0.getX() && (i+1)*tileSize + (screenSize.width - maze.length * tileSize)/2 > arg0.getX() && j*tileSize + (screenSize.height - maze[0].length * tileSize)/2 < arg0.getY() && (j+1)*tileSize + (screenSize.height - maze[0].length * tileSize)/2 > arg0.getY()) {
					row = i;
					col = j;
				}
			}
		}
		//System.out.println("row: " + row);
		//System.out.println("col: " + col);
		if (maze[row][col].getType() == TileType.DOOR && maze[row][col].isWalkable()) {
			maze[row][col].close();
			ArrayList<Integer> temp = new ArrayList<>();
    		temp.add(row);
    		temp.add(col);
    		changedTiles.add(temp);
		}
		else if (maze[row][col].getType() == TileType.DOOR && !maze[row][col].isWalkable()) {
			maze[row][col].open();
			ArrayList<Integer> temp = new ArrayList<>();
    		temp.add(row);
    		temp.add(col);
    		changedTiles.add(temp);
		}
		else if (maze[row][col].getType() == TileType.LOCKED_DOOR) {
			System.out.println(maze[row][col].unlock());
			ArrayList<Integer> temp = new ArrayList<>();
    		temp.add(row);
    		temp.add(col);
    		changedTiles.add(temp);
		}
		ArrayList<Integer> temp2 = new ArrayList<>();
		temp2.add(row);
		temp2.add(col);
		changedTiles.add(temp2);
		if (!this.first) {
			repaint();
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void playPressed() {
		this.inStartMenu = false;
		this.justSwitched = true;
		//playButton.setVisible(false);
		//this.remove(playButton);
		//this.setLayout(null);
		repaint();
	}

}