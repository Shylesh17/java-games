import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;



public class SnakeGame extends JPanel implements ActionListener, KeyListener{
    // Overrides are placed down the code
    
    private class Tile{
        int x;
        int y;
        Tile(int x,int y){
            this.x=x;
            this.y=y;
        }
    }
    int boardWidth;
    int boardHeight;
    int TileSize=25;
    Tile SnakeHead;
    ArrayList<Tile> SnakeBody;
    Tile Food;
    Random FoodPosition;
    Timer gameloop ;
    int SnakeMovementX;
    int SnakeMovementY;
    boolean GameOver = false;

    SnakeGame(int boardWidth , int boardHeight){
        this.boardWidth=boardWidth;
        this.boardHeight=boardHeight;
        setPreferredSize(new Dimension(this.boardWidth,this.boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true);

        SnakeHead= new Tile(5,5);
        SnakeBody= new ArrayList<Tile>();

        Food = new Tile(10,10);
        FoodPosition = new Random();
        placefood();

        SnakeMovementX=0;
        SnakeMovementY=0;

        gameloop = new Timer(100,this);
        gameloop.start();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g){
        //grid 
       /*  for(int i=0 ; i<boardWidth/TileSize; i++){
            //(x1,y1,x2,y2)
            g.drawLine(i*TileSize, 0 , i*TileSize, boardHeight);
            g.drawLine(0, i*TileSize, boardWidth, i*TileSize);
        
        } */
        
        //food
        g.setColor(Color.red);
        g.fillOval(Food.x*TileSize,Food.y*TileSize, TileSize,TileSize);
    
        //SnakeHead
        g.setColor(Color.green);
        g.setColor(new Color(FoodPosition.nextInt(255),FoodPosition.nextInt(255),FoodPosition.nextInt(255)));
        g.fill3DRect(SnakeHead.x*TileSize,SnakeHead.y*TileSize, TileSize,TileSize,true);
    
        //SnakeBody
        for(int i=0; i<SnakeBody.size(); i++){
            Tile SnakePart= SnakeBody.get(i);
            g.fill3DRect(SnakePart.x*TileSize, SnakePart.y*TileSize, TileSize, TileSize,true);
        }

        /// till this if we eat food, the body will generate at the position of the
        /// food and stay there at food position ,to move it along with the head go to move().

        //gameover and score
        g.setFont(new Font("ariel" , Font.BOLD,20));
        if(GameOver){
                g.setFont(new Font("ariel",Font.BOLD,50));
                g.setColor(Color.red);
                g.drawString("GAME OVER : "+String.valueOf(SnakeBody.size()), TileSize*4,TileSize*12);
            }
        else{
                g.setColor(Color.blue);
                g.drawString("SCORE : "+String.valueOf(SnakeBody.size()), TileSize - 16,TileSize);

            }
        
           
    }

    public void placefood(){
        Food.x=FoodPosition.nextInt(boardWidth/TileSize);
        Food.y=FoodPosition.nextInt(boardHeight/TileSize);
    }

    public boolean  collision( Tile tile1, Tile tile2){
        return tile1.x == tile2.x && tile1.y==tile2.y; 
    } 

    public void move(){
        //to eat food
        if(collision(SnakeHead,Food)){
            SnakeBody.add(new Tile(Food.x,Food.y));
            placefood();
        } 

        //to move snakebody with snakehead
        for(int i=SnakeBody.size()-1; i>=0; i--){
            Tile snakepart = SnakeBody.get(i);
            if(i==0){
                snakepart.x=SnakeHead.x;
                snakepart.y=SnakeHead.y;
            }
            else{
                Tile previoussnakepart = SnakeBody.get(i-1);
                snakepart.x=previoussnakepart.x;
                snakepart.y=previoussnakepart.y;
            }
        }
        

        //to move snakehead
        SnakeHead.x += SnakeMovementX;
        SnakeHead.y += SnakeMovementY;

        //gameover connditions
        for(int i=0; i<SnakeBody.size(); i++){
            Tile snakepart=SnakeBody.get(i);
            //hitting itself
            if(collision(SnakeHead,snakepart)){
                GameOver=true;
            }
        }
        // hitting the wall
        if(SnakeHead.x*TileSize<0 || SnakeHead.x*TileSize>boardWidth || SnakeHead.y*TileSize<0 || SnakeHead.y*TileSize>boardHeight){
            GameOver=true;
        }
 
    }


    
    @Override //Action listener
    public void actionPerformed(ActionEvent e) {
        move(); // to call x and y position
        repaint(); // call draw over again
        if(GameOver){
            gameloop.stop();
        }
    }
    
    @Override //Key listener
    public void keyPressed(KeyEvent e) {
        /// the values for x and y will be saved in getkeycode ,so when we press 
        /// the key for opposite direction it will check for the keycode condition
        /// if the keycode comes under the condition then the keycode is invalid .
        /// if the keypress is UP then code is Y=-1 , if we press DOWN key the keycode
        /// will change to Y=1 but as by the condition it is not possible to use DOWN key
        /// when the keycode is Y=-1
       
        if (e.getKeyCode()== KeyEvent.VK_UP && SnakeMovementY != 1){
            SnakeMovementX=0;
            SnakeMovementY=-1;
        }
        else if(e.getKeyCode()== KeyEvent.VK_DOWN && SnakeMovementY != -1){
            SnakeMovementX=0;
            SnakeMovementY=1;
        }
        
        else if(e.getKeyCode()== KeyEvent.VK_RIGHT && SnakeMovementX != -1){
            SnakeMovementX=1;
            SnakeMovementY=0;
        }
        
        else if(e.getKeyCode()== KeyEvent.VK_LEFT && SnakeMovementX != 1){
            SnakeMovementX=-1;
            SnakeMovementY=0;
        }

    }

    // no need for typed and released
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

}
