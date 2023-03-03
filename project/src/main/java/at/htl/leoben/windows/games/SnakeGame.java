package at.htl.leoben.windows.games;

import at.htl.leoben.engine.Window;
import at.htl.leoben.engine.configurations.AdvancedColor;
import at.htl.leoben.engine.configurations.AlignmentHorizontal;
import at.htl.leoben.engine.configurations.AlignmentVertical;
import at.htl.leoben.engine.configurations.data.AlignmentTypeHorizontal;
import at.htl.leoben.engine.configurations.data.AlignmentTypeVertical;
import at.htl.leoben.engine.data.WindowElementItem;
import at.htl.leoben.socket.data.SpecialCharacterKey;
import at.htl.leoben.windows.style.DefaultStyle;
import static org.fusesource.jansi.Ansi.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


public class SnakeGame extends GameWindowBase<String> {
    public SnakeGame() {
        super(2);
    }

    private Window root = new Window(20, 40);

    @Override
    public Window getWindow() {
        return root;
    }

    WindowElementItem[] items = new WindowElementItem[10];

    WindowElementItem gameOverScreen = null;
    WindowElementItem player0 = null;

    String gameOverText = "Game Over";

    SpecialCharacterKey direction0 = SpecialCharacterKey.RIGHT;

    SpecialCharacterKey direction1 = SpecialCharacterKey.RIGHT;


    HashMap<Integer, Integer> positionPlayer0 = new HashMap<Integer, Integer>();
    ArrayList<WindowElementItem> snakeBodyPlayer0 = new ArrayList<>();

    ArrayList<WindowElementItem> snakeBodyPlayer1 = new ArrayList<>();

    ArrayList<WindowElementItem> apples = new ArrayList<>();

    int appleCount = 6;

    boolean gameOver = false;


    @Override
    public void onStart() throws Exception {
        root.setTitle("SNAKE 2023");
        items[2] = root.setText(new AlignmentHorizontal(AlignmentTypeHorizontal.LEFT, 2, 0), root.getHeight() - 2, null, root.formatter.getFormat(AdvancedColor.WHITE, Attribute.INTENSITY_BOLD, DefaultStyle.borderFormat));
        items[3] = root.setText(new AlignmentHorizontal(AlignmentTypeHorizontal.RIGHT, 2, 1), root.getHeight() - 2, null, root.formatter.getFormat(AdvancedColor.WHITE, Attribute.INTENSITY_BOLD, DefaultStyle.borderFormat));


        //#####################################################################################################
        // STUDENT TODO: Create snakes here

        for (int i = 0; i < 5; i++) {
            snakeBodyPlayer0.add(root.setElement(1+i,1, 'x',null));
        }

        for (int i = 0; i < 5; i++) {
            snakeBodyPlayer1.add(root.setElement(10+i,10, 'o',null));
        }


        //#####################################################################################################


        gameOverScreen = root.setText(new AlignmentHorizontal(AlignmentTypeHorizontal.CENTER), new AlignmentVertical(AlignmentTypeVertical.MIDDLE), null, null);


    }

    @Override
    public void onTick() throws Exception {
        SpecialCharacterKey keyStrokePlayer0 = getSpecialInput(0);
        SpecialCharacterKey keyStrokePlayer1 = getSpecialInput(1);
        items[2].setText("P1: " + String.valueOf(keyStrokePlayer0));
        items[3].setText("P2: " + String.valueOf(keyStrokePlayer1));
        //#####################################################################################################
        // STUDENT TODO: Implement a method to get the players keystroke and update the direction accordingly.

        if(keyStrokePlayer0 != SpecialCharacterKey.NONE)
            direction0 = keyStrokePlayer0;

        if(keyStrokePlayer1 != SpecialCharacterKey.NONE)
            direction1 = keyStrokePlayer1;

        //#####################################################################################################

        // DO NOT CHANGE SOMETHING IN HERE

        this.updatePosition(this.snakeBodyPlayer0, direction0);
        this.updatePosition(this.snakeBodyPlayer1, direction1);

        //#####################################################################################################
        // STUDENT TODO: Check if a payer is gameover

        this.isGameOver(this.snakeBodyPlayer0);
        this.isGameOver(this.snakeBodyPlayer1);

        //#####################################################################################################


    }



    public void isGameOver(ArrayList<WindowElementItem> snakeBody)
    {
        // STUDENT TODO: Implement logic to check if a snake hit another snake or border. To do this you mus
        // implement the function Boolean inSnake(ArrayList<WindowElementItem> snakeBody)
        // After that set class member gameover to true and clear screen
        //Border 2
        if(gameOver)
            return;


        gameOver = gameOver || snakeBody.get(0).getX() == root.getWidth()-2;
        gameOver = gameOver ||snakeBody.get(0).getX() == 0;

        gameOver = gameOver || snakeBody.get(0).getY() == root.getHeight()-2;
        gameOver = gameOver ||snakeBody.get(0).getY() == 0;

        gameOver = gameOver || inSnake(snakeBody);

        if(gameOver) {

            this.snakeBodyPlayer0.clear();
            this.snakeBodyPlayer1.clear();

            gameOverScreen.setText(gameOverText);
            direction0 = direction1 = SpecialCharacterKey.NONE;

        }
    }

    public Boolean inSnake(ArrayList<WindowElementItem> snakeBody)
    {
        // STUDENT TODO: Implement a method to check if a head of the provided snake body hit
        // another snake body
        WindowElementItem snakeHead = snakeBody.get(0);

        // Player 0
        for (WindowElementItem snakeElement: snakeBodyPlayer0) {

            if(!snakeElement.equals(snakeHead) &&
                    snakeHead.getX() == snakeElement.getX() &&
                    snakeHead.getY() == snakeElement.getY()){

                return true;
            }
        }

        // Player 1
        for (WindowElementItem snakeElement: snakeBodyPlayer1) {

            if(!snakeElement.equals(snakeHead) &&
                    snakeHead.getX() == snakeElement.getX() &&
                    snakeHead.getY() == snakeElement.getY()){

                return true;
            }
        }

        return false;
    }

    public void updatePosition(ArrayList<WindowElementItem> snakeBody, SpecialCharacterKey direction)
    {

        if (this.gameOver)
            return;

        int oldLastY = snakeBody.get(snakeBody.size()-1).getY();
        int oldLastX = snakeBody.get(snakeBody.size()-1).getX();
        if (direction == SpecialCharacterKey.LEFT) {
            int old_x = snakeBody.get(0).getX();
            int old_y = snakeBody.get(0).getY();;
            snakeBody.get(0).decrementX();
            for (int index = 1; index < snakeBody.size(); index++) {
                int tmp_x = snakeBody.get(index).getX();
                int tmp_y = snakeBody.get(index).getY();
                snakeBody.get(index).setX(old_x);
                snakeBody.get(index).setY(old_y);
                old_x = tmp_x;
                old_y = tmp_y;
            }
        } else if (direction == SpecialCharacterKey.RIGHT) {
            int old_x = snakeBody.get(0).getX();
            int old_y = snakeBody.get(0).getY();;
            snakeBody.get(0).incrementX();
            for (int index = 1; index < snakeBody.size(); index++) {
                int tmp_x = snakeBody.get(index).getX();
                int tmp_y = snakeBody.get(index).getY();
                snakeBody.get(index).setX(old_x);
                snakeBody.get(index).setY(old_y);
                old_x = tmp_x;
                old_y = tmp_y;
            }
        } else if (direction == SpecialCharacterKey.DOWN) {
            int old_x = snakeBody.get(0).getX();
            int old_y = snakeBody.get(0).getY();;
            snakeBody.get(0).incrementY();
            for (int index = 1; index < snakeBody.size(); index++) {
                int tmp_x = snakeBody.get(index).getX();
                int tmp_y = snakeBody.get(index).getY();
                snakeBody.get(index).setX(old_x);
                snakeBody.get(index).setY(old_y);
                old_x = tmp_x;
                old_y = tmp_y;
            }
        } else if (direction == SpecialCharacterKey.UP)  {
            int old_x = snakeBody.get(0).getX();
            int old_y = snakeBody.get(0).getY();;
            snakeBody.get(0).decrementY();
            for (int index = 1; index < snakeBody.size(); index++) {
                int tmp_x = snakeBody.get(index).getX();
                int tmp_y = snakeBody.get(index).getY();
                snakeBody.get(index).setX(old_x);
                snakeBody.get(index).setY(old_y);
                old_x = tmp_x;
                old_y = tmp_y;
            }
        }

        // Student TODO: Call a method to detect if the snake just ate an apple

    }


    public void hitApple(int oldLastX, int oldLastY, ArrayList<WindowElementItem> snakeBody)
    {
        int posHeadX = snakeBody.get(0).getX();
        int posHeadY = snakeBody.get(0).getY();
        int hittedApple = -1;
        for (int i = 0; i < apples.size(); i++)
        {
            if (apples.get(i).getX() == posHeadX && apples.get(i).getY() == posHeadY)
            {
                hittedApple = i;
                break;
            }
        }

        // Student TODO: Write a method to add a new body element to the snake and print it to the gamescreen


    }


    public void spawnApple()
    {
        // STUDENT TODO: Write a method to randomly spawn apples. Use the applescount member variable for this task
        int posX =  (int) (Math.random() * 40) + 1;
        int posY =  (int) (Math.random() * 20) + 1;

        apples.add(root.setElement(posX, posY, 'a', null));


    }

}
