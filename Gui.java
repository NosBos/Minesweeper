import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static javax.swing.BoxLayout.X_AXIS;


public class Gui extends Game implements ActionListener {
    private Game game;
    private JFrame frame;
    private int boardWidth;
    private int boardHeight;
    private int mineCounter;
    private JButton[][] gameGrid;
    private ImageIcon imageHidden;
    private ImageIcon image0;
    private ImageIcon image1;
    private ImageIcon image2;
    private ImageIcon image3;
    private ImageIcon image4;
    private ImageIcon image5;
    private ImageIcon image6;
    private ImageIcon image7;
    private ImageIcon image8;
    private ImageIcon image9;
    private ImageIcon imageFlag;
    private ImageIcon mineClick;
    private ImageIcon wrongFlag;


    public static void main(String args[]){

        Gui generateBoard = new Gui(10,10,10);

    }
    public Gui(int boardWidth2,int boardHeight2,int mineCounter2)  {
        this.boardHeight=boardHeight2;
        this.boardWidth=boardWidth2;
        this.mineCounter=mineCounter2;
        gameGrid=new JButton[boardHeight][boardWidth];
        loadImages();
        frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);
       // frame.setResizable(false);
        //Menu Bar
        JMenuBar menuBar= new JMenuBar();
        JMenu gameOptions = new JMenu("Game");
        JMenu helpOptions = new JMenu("Help");
        //Game Options
        JMenuItem gameOptionOptions = new JMenuItem("Options");
        JMenuItem gameOptionsNG = new JMenuItem("New Game");
        JMenuItem gamesOptionsExit =new JMenuItem("Exit");
        gameOptions.add(gameOptionsNG);
        gameOptions.add(gameOptionOptions);
        gameOptions.add(gamesOptionsExit);
        //GameOptions Action Listener
        gameOptionOptions.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ev) {
                //int ans = Integer.parseInt( JOptionPane.showInputDialog(frame, "Text", JOptionPane.INFORMATION_MESSAGE, null, null, "[sample text to help input]"));
                JFrame optionsFrame=new JFrame();
                JPanel options = new JPanel();
                optionsFrame.add(options);
                options.setLayout(new BoxLayout(options,BoxLayout.Y_AXIS));
                Container contentPane = optionsFrame.getContentPane();
                contentPane.add(options);
                optionsFrame.setSize(155,155);
                optionsFrame.setLocationRelativeTo(frame);
                options.setBorder(new EmptyBorder(25,25,25,25));
                //Panel Width
                JPanel width = new JPanel();
                width.setLayout(new BoxLayout(width,X_AXIS));
                JLabel widthLabel = new JLabel("Width ");
                widthLabel.setSize(40,15);
                JTextField widthValue = new JTextField();
                widthValue.setSize(40,15);
                //Panel Height
                JPanel height= new JPanel();
                height.setLayout(new BoxLayout(height,X_AXIS));
                JLabel heightLabel = new JLabel("Height ");
                heightLabel.setSize(40,15);
                JTextField heightValue = new JTextField();
                heightValue.setSize(40,15);
                //JPanel Mines
                JPanel mines = new JPanel();
                mines.setLayout(new BoxLayout(mines,X_AXIS));
                JLabel minesLabel = new JLabel("Mines ");
                minesLabel.setSize(40,15);
                JTextField minesValue = new JTextField();
                minesValue.setSize(40,15);
                mines.add(minesLabel);
                mines.add(minesValue);
                options.add(mines);
                height.add(heightLabel);
                height.add(heightValue);
                options.add(height);
                width.add(widthLabel);
                width.add(widthValue);
                JButton confirm =new JButton("OK");
                confirm.addActionListener(new ActionListener() {


                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        boardWidth=Integer.parseInt(widthValue.getText());
                        boardHeight=Integer.parseInt(heightValue.getText());
                        mineCounter=Integer.parseInt(minesValue.getText());
                        optionsFrame.dispose();
                    }
                });
                options.add(width);
                options.add(confirm);
                optionsFrame.setVisible(true);
            }
        });
        //Help Options
        JMenuItem helpOptionsPlay = new JMenuItem("How to Play");
        JMenuItem helpOptionsAbout = new JMenuItem("About");
        helpOptions.add(helpOptionsPlay);
        helpOptions.add(helpOptionsAbout);
        menuBar.add(gameOptions);
        menuBar.add(helpOptions);

        //Game Info
        JPanel gameInfoPanel = new JPanel();
        JLabel mineRemain = new JLabel("Mines");
        JLabel time = new JLabel("000");
        JButton newGameBtn = new JButton("New Game");
        gameInfoPanel.add(mineRemain);
        gameInfoPanel.add(newGameBtn);
        gameInfoPanel.add(time);

        //Game Grid Layout
        GridLayout boardLayout = new GridLayout(boardHeight,boardWidth);
        JPanel gameGridPanel = new JPanel();
        gameGridPanel.setLayout(boardLayout);
        for(int y=0;y<boardHeight;y++){
            for(int x=0;x<boardWidth;x++){
                gameGrid[y][x]=new JButton();
                gameGrid[y][x].setName(x+","+y);
                gameGrid[y][x].setPreferredSize(new Dimension(25,25));

                gameGrid[y][x].addActionListener(this);
                gameGridPanel.add(gameGrid[y][x]).setLocation(y,x);

            }
        }
        frame.getContentPane().add(BorderLayout.NORTH, menuBar);
        frame.getContentPane().add(BorderLayout.CENTER,gameInfoPanel);
        frame.getContentPane().add(BorderLayout.SOUTH,gameGridPanel);
        frame.pack();
        //frame.setSize(400,400);
        frame.setVisible(true);

        game=new Game(boardHeight,boardWidth,mineCounter);
       updateIcon();
        for(int x=0;x<boardWidth;x++){
            for(int y=0;y<boardHeight;y++) {
                Square currentSpace=game.getSquare(x,y);
                if(currentSpace.getSpaceType()!=0){
                    if(currentSpace.getSpaceType()==9){
                        //gameGrid[y][x].setText("X");
                    }
                    else{
                        //gameGrid[y][x].setText(currentSpace.getSpaceType()+"");
                    }

                }

            }
        }


        newGameBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                frame.dispose();
                new Gui(boardWidth,boardHeight,mineCounter);
            }
        });

    }

    public void actionPerformed(ActionEvent e){
        int xcoord;
        int ycoord;
        String[] coord;
        Object o = e.getSource();

        if(o instanceof JButton){
            JButton button = (JButton)o;
            coord=button.getName().split(",");
            xcoord=Integer.parseInt(coord[0]);
            ycoord=Integer.parseInt(coord[1]);
            System.out.println("x "+xcoord+" y: "+ycoord);
            Square currentSpace=game.getSquare(xcoord,ycoord);

            game.revealSquares(ycoord,xcoord);
            updateIcon();
            if(currentSpace.getSpaceType()==9){
                System.out.println("I'm a bomb :D");
                //reveal all mines

                //change icon of clicked mine

                //message box
                JOptionPane.showMessageDialog(frame, "You lose","Oh No!",JOptionPane.WARNING_MESSAGE);
                frame.dispose();
                new Gui(boardWidth,boardHeight,mineCounter);
            }
            else if(checkWin()){
                JOptionPane.showMessageDialog(frame, "You Win","Oh Yeah!",JOptionPane.WARNING_MESSAGE);
                frame.dispose();
                new Gui(boardWidth,boardHeight,mineCounter);
            }
        }
    }
    public boolean checkWin(){
        int spacesLeft=0;
        for(int x=0;x<boardWidth;x++){
            for(int y=0;y<boardHeight;y++) {
                Square currentSpace=game.getSquare(x,y);
                if (currentSpace.isHidden()){
                    spacesLeft++;
                }
            }
        }
        if(spacesLeft == mineCounter){
            return true;
        }
        return false;
    }
    public void updateIcon(){
        //turn into switch case and load all pictures beforehand (in main)
        for(int y=0;y<boardHeight;y++){
            for(int x=0;x<boardWidth;x++){
                Square space=game.getSquare(x,y);
                int spaceVal= space.getSpaceType();
                if(space.isHidden()){

                    this.gameGrid[y][x].setIcon(imageHidden);
                }
                else{
                    if(space.hasFlag()){
                        this.gameGrid[y][x].setIcon(imageFlag);
                    }
                    else{
                        switch(spaceVal){
                            case 0:
                                this.gameGrid[y][x].setIcon(image0);
                                break;
                            case 1:
                                this.gameGrid[y][x].setIcon(image1);
                                break;
                            case 2:
                                this.gameGrid[y][x].setIcon(image2);
                                break;
                            case 3:
                                this.gameGrid[y][x].setIcon(image3);
                                break;
                            case 4:
                                this.gameGrid[y][x].setIcon(image4);
                                break;
                            case 5:
                                this.gameGrid[y][x].setIcon(image5);
                                break;
                            case 6:
                                this.gameGrid[y][x].setIcon(image6);
                                break;
                            case 7:
                                this.gameGrid[y][x].setIcon(image7);
                                break;
                            case 8:
                                this.gameGrid[y][x].setIcon(image8);
                                break;
                            case 9:
                                this.gameGrid[y][x].setIcon(mineClick);
                                break;
                        }
                    }


                  }
                }

            }
        }
    public void loadImages(){
        ImageIcon imageHidden = (new ImageIcon (getClass().getResource("Graphics/hidden.png") ));
        ImageIcon image0 = (new ImageIcon (getClass().getResource("Graphics/0.png") ));
        ImageIcon image1 = (new ImageIcon (getClass().getResource("Graphics/1.png") ));
        ImageIcon image2 = (new ImageIcon (getClass().getResource("Graphics/2.png") ));
        ImageIcon image3 = (new ImageIcon (getClass().getResource("Graphics/3.png") ));
        ImageIcon image4 = (new ImageIcon (getClass().getResource("Graphics/4.png") ));
        ImageIcon image5 = (new ImageIcon (getClass().getResource("Graphics/5.png") ));
        ImageIcon image6 = (new ImageIcon (getClass().getResource("Graphics/6.png") ));
        ImageIcon image7 = (new ImageIcon (getClass().getResource("Graphics/7.png") ));
        ImageIcon image8 = (new ImageIcon (getClass().getResource("Graphics/8.png") ));
        ImageIcon image9 = (new ImageIcon (getClass().getResource("Graphics/9.png") ));
        ImageIcon imageFlag = (new ImageIcon (getClass().getResource("Graphics/flag.png") ));
        ImageIcon mineClick = (new ImageIcon (getClass().getResource("Graphics/mineClick.png") ));
        ImageIcon wrongFlag = (new ImageIcon (getClass().getResource("Graphics/wrongFlag.png") ));
        this.imageHidden = resizeImage(imageHidden);
        this.image0 = resizeImage(image0);
        this.image1 = resizeImage(image1);
        this.image2 = resizeImage(image2);
        this.image3 = resizeImage(image3);
        this.image4 = resizeImage(image4);
        this.image5 = resizeImage(image5);
        this.image6 = resizeImage(image6);
        this.image7 = resizeImage(image7);
        this.image8 = resizeImage(image8);
        this.image9 = resizeImage(image9);
        this.imageFlag = resizeImage(imageFlag);
        this.mineClick = resizeImage(mineClick);
        this.wrongFlag = resizeImage(wrongFlag);
    }
    public ImageIcon resizeImage(ImageIcon oldImage){
        Image image = oldImage.getImage(); // transform it
        Image newimg = image.getScaledInstance(25, 25,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        ImageIcon resizedImage = new ImageIcon(newimg);
        return resizedImage;
    }


}


