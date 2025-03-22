import javax.swing.*;

public class App {

    public static void main(String[] args) throws Exception{
        JFrame mainWindow = new JFrame( "Flappy Bird");

        int boardWidth = 360;
        int boardHeight = 640;

        mainWindow.setSize(boardWidth, boardHeight);

        mainWindow.setLocationRelativeTo(null);

        mainWindow.setResizable(false);

        JLabel ImageBg = new JLabel(new ImageIcon("./image/flappybirdbg.png"));
        mainWindow.add(ImageBg);

        FlappyBird FlappyBird = new FlappyBird();

        mainWindow.add(FlappyBird);

        mainWindow.setVisible(true);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    } 
}

