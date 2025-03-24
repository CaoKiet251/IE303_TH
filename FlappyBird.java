import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.awt.event.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
     int boardHeight = 640;
     int boardWidth = 360;

     Image backgroundImage;
     Image birdImage;
     Image topPipeImage;
     Image bottomPipeImage;

    public class Bird {
        int x;
        int y;
        int height = 24;
        int width = 34;
        Image image;        

        Bird(Image image){
            this.image = image;
            resetSetting();
        }
        public void resetSetting(){
            x = boardHeight/8;
            y = boardWidth/8;
        }
    }

    public class Pipe {
        int x = boardWidth;
        int y = 0;
        int height = 512;
        int width = 52;
        Image image;   
        boolean isTheBirdPass = false;

        Pipe( Image image){
            this.image = image;
        }
    }

    Bird bird;
    ArrayList<Pipe> pipes = new ArrayList<Pipe>();

    int gravity = 1;
    int birdVeLocity = 0;
    int pipeVelocity = -4;
    boolean isGameover = false;
    double score = 0;

    Timer gameloop;
    Timer placePipeLoop;



    FlappyBird( ){
        setFocusable(true);
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        addKeyListener(this);

        backgroundImage = new ImageIcon(getClass().getResource("./image/flappybirdbg.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("./image/flappybird.png")).getImage();
        topPipeImage = new ImageIcon(getClass().getResource("./image/toppipe.png")).getImage();
        bottomPipeImage = new ImageIcon(getClass().getResource("./image/bottompipe.png")).getImage();



        bird = new Bird(birdImage);

        gameloop = new Timer(1000/60, this);
        gameloop.start();

        placePipeLoop = new Timer(1200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                placePipes();
            }
        }); 
        placePipeLoop.start();  

    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(isGameover){
            gameloop.stop();
            placePipeLoop.stop();
        }
        moveBird();
        repaint();
        movePipe();

        
    }
    @Override
    public void keyPressed(KeyEvent e){
        if((e.getKeyCode())== KeyEvent.VK_SPACE | (e.getKeyCode())== KeyEvent.VK_ENTER){
            birdVeLocity = -10;
        }
        if(isGameover){
            resetSetting();
        }

    }

    @Override
    public void keyTyped(KeyEvent e){

    }
    @Override
    public void keyReleased(KeyEvent e){

    }


    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        draw(graphics);
    }

    public void draw(Graphics graphics){
        graphics.drawImage(backgroundImage, 0, 0, boardWidth, boardHeight, null);
        graphics.drawImage(bird.image, bird.x, bird.y, bird.width, bird.height, null);

        
        graphics.setColor(Color.white);
        graphics.setFont(new Font("Arial", Font.BOLD, 16));
        String scoreText = "Score: " + (int) score;
        graphics.drawString(scoreText, 10, 20);

        for(Pipe pipe: pipes){
            graphics.drawImage(pipe.image, pipe.x, pipe.y, pipe.width, pipe.height, null );
        }

        if (isGameover) {
            graphics.setColor(Color.RED);
            graphics.setFont(new Font("Arial", Font.BOLD, 24));
            String gameOverText = "Game Over";
            
            FontMetrics metrics = graphics.getFontMetrics();
            int xGameOver = (boardWidth - metrics.stringWidth(gameOverText)) / 2;            
            graphics.drawString(gameOverText, xGameOver, boardHeight / 2 - 20);
        }
        
    }

    public void moveBird(){
        birdVeLocity += gravity;
        bird.y += birdVeLocity;
        bird.y = Math.max(0, bird.y);
        bird.y = Math.min(boardHeight-bird.height, bird.y);

        if((bird.y + bird.height) >= boardHeight){
            isGameover = true;
        }
        if (bird.y == 0) {
            isGameover = true;
        }

    }

    public void placePipes( ){
        Pipe topPipe = new Pipe(topPipeImage);
        int ramdomPipe = (int) (topPipe.y - (topPipe.height/4) - Math.random()*(topPipe.height/2));
        topPipe.y += ramdomPipe;
        pipes.add(topPipe);

        int spaceBetweenPipes = boardWidth/4;

        Pipe bottomPipe = new Pipe(bottomPipeImage);
        bottomPipe.y += topPipe.y + topPipe.height + spaceBetweenPipes;
        pipes.add(bottomPipe);
    }

    public void movePipe(){
        for(Pipe pipe: pipes)
        {
            pipe.x += pipeVelocity;

            if(hasCollission(bird,pipe)){
                isGameover= true;
            }

            if(!pipe.isTheBirdPass && bird.x> pipe.x +pipe.width){
                pipe.isTheBirdPass = true;
                score +=0.5;
            }
        }
    }

    public boolean hasCollission(Bird bird, Pipe pipe){
        return bird.x < pipe.x + pipe.width &&
        bird.x + bird.width > pipe.x &&
        bird.y < pipe.y + pipe.height &&
        bird.y + bird.height > pipe.y;
    }
    public void resetSetting(){
        bird.resetSetting();
        pipes = new ArrayList<Pipe>();

        pipeVelocity = -4;
        birdVeLocity = 0;
        gravity = 1;
        isGameover = false;
        score = 0;

        gameloop.start();
        placePipeLoop.start();
    }
    
}
