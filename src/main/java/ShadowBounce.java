/**
 * Shadow Bounce is a game where the player tries to clear red pegs on the board with limited shots
 * the player either progresses to next board when all red pegs are cleared
 * and finish if all red pegs on all boards are cleared
 * or end game if run out of shots
 *   */

import bagel.*;
import bagel.util.Point;
import bagel.Input;
import bagel.Window;
import bagel.util.Vector2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


public class ShadowBounce extends AbstractGame {
    private String BLUE_PEG= "res/peg.png";
    private String BLUE_PEG_HORIZONTAL = "res/horizontal-peg.png";
    private String BLUE_PEG_VERTICAL = "res/vertical-peg.png";
    private String RED_PEG = "res/red-peg.png";
    private String RED_PEG_HORIZONTAL ="res/red-horizontal-peg.png";
    private String RED_PEG_VERTICAL = "res/red-vertical-peg.png";
    private String GREY_PEG= "res/grey-peg.png";
    private String GREY_PEG_HORIZONTAL = "res/grey-horizontal-peg.png";
    private String GREY_PEG_VERTICAL = "res/grey-vertical-peg.png";
    private String GREEN_PEG = "res/green-peg.png";
    private String GREEN_PEG_HORIZONTAL ="res/green-horizontal-peg.png";
    private String GREEN_PEG_VERTICAL = "res/green-vertical-peg.png";
    private String POWERUP = "res/powerup.png";
    private String[] board = new String[]{"res/0.csv","res/1.csv","res/2.csv","res/3.csv","res/4.csv"};
    private String IS_BLUE_PEG = "blue_peg";
    private String IS_BLUE_HORIZONTAL = "blue_peg_horizontal";
    private String IS_BLUE_VERTICAL = "blue_peg_vertical";
    private String IS_GREY_PEG = "grey_peg";
    private String IS_GREY_HORIZONTAL = "grey_peg_horizontal";
    private String IS_GREY_VERTICAL = "grey_peg_vertical";
    private static final Point COORDINATION = new Point(512, 744);
    private static final Point BALL_POSITION = new Point(512, 32);
    private static final int MAX_NUM_BOARD = 5;
    private ArrayList<Ball> balls;
    private ArrayList<Peg> pegs;
    private Powerup powerup;
    private Bucket bucket;
    private Ball ball_one ;
    private Ball ball_two ;
    private int shots = 20;
    private int nth_board = 0;
    private boolean new_turn = false;


    public ShadowBounce() {
        balls = new ArrayList<Ball>();
        pegs = new ArrayList<Peg>();
        readBoard(board[nth_board]);

    }


    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        ShadowBounce game = new ShadowBounce();
        game.run();
    }


    @Override
    /**update of the whole ShadowBounce game*/
    public void update(Input input) {
        // create a ball if no balls in the window and have shots available
        if (input.wasPressed(MouseButtons.LEFT) && shots > 0 && balls.size()==0) {
            new_turn = true;
            shots-=1;
            balls.add(new NormalBall(BALL_POSITION, input.directionToMouse(BALL_POSITION))) ;
        }

        //update the movement of the balls
        for(Ball ball:balls){
            ball.update();
            ball.draw();
        }

        //check if balls intersect with pegs and then update balls and pegs
        for(Ball ball: new ArrayList<>(balls)){
            for(Peg peg: new ArrayList<>(pegs)){
                if(ball.getRectangle().intersects(peg.getPosition())){
                    ball.setVelocity(ball.getRectangle().intersectedAt(ball.getPosition(), ball.getVelocity()));
                    //if it is fireball, all pegs within 70 pixels will be destroyed
                    if(ball instanceof FireBall){
                        ((FireBall) ball).explode(pegs, peg.getPosition());
                    }
                    //if ball intersect with red or blue peg, red or blue peg will be destroyed
                    if(peg instanceof RedPeg||peg instanceof BluePeg) {
                        pegs.remove(peg);
                    }

                    //if ball intersects with green peg, another two same type of balls created & green peg destroyed
                    if(peg instanceof GreenPeg){
                        createBall(peg.getPosition(), ball);
                        pegs.remove(peg);
                    }

                    //if ball intersects with grey peg, grey peg will not be destroyed
                    if(peg instanceof GreyPeg){

                    }
                }
            }
        }
        //draw the updated pegs
        for (Peg peg : pegs){
            peg.draw();
        }

        for(Ball ball: new ArrayList<>(balls)){
            //check if ball intersects with bucket while leaving out of screen
            if(ball.outOfScreen()) {
                if (ball.getRectangle().intersects(bucket.getRectangle())) {
                    shots += 1;
                }
                balls.remove(ball);
            }
            //check if ball intersects with powerup to become fireball
            if(ball instanceof NormalBall && powerup!=null && ball.getRectangle().intersects(powerup.getRectangle())){
                powerup = null;
                Point fireball_pt = ball.getPosition();
                Vector2 fireball_direction = ball.getDirection();
                balls.remove(ball);
                balls.add(new FireBall(fireball_pt, fireball_direction));

            }
        }

        //update and draw powerup if not null
        if(powerup!=null) {
            powerup.draw();
            powerup.update();
        }

        //update bucket(draw function include in update)
        bucket.update();

        //all balls fall out of screen , another new turn should begin
        if(new_turn && balls.size()==0){
            setTurn();
            new_turn = false;
        }

        //if all red pegs are cleared for that board
        if(noRedPeg() && !new_turn){
            nth_board++;
            //progress to another board if there is more
            if(nth_board < MAX_NUM_BOARD){
                readBoard(board[nth_board]);
            //finish game if all red pegs are cleared and no more board
            }else{
                System.out.println("congratulations - all boards are cleared");
                System.exit(0);
            }
        }

        //end game when out of shots
        if(shots==0){
            System.out.println("game over - run out of shots");
            System.exit(0);
        }
    }

    /**read board(put blue and grey pegs in arraylist pegs) from csv file and set the board*/
    public void readBoard(String file){
        balls.clear();
        pegs.clear();

        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            String line;
            while((line= br.readLine())!= null){
                String[] tuples = line.split(",");
                int x = Integer.parseInt(tuples[1]);
                int y = Integer.parseInt(tuples[2]);
                Point peg_position = new Point(x, y);
                if(tuples[0].equals(IS_BLUE_PEG)){
                    pegs.add(new BluePeg(peg_position, BLUE_PEG));
                }else if(tuples[0].equals(IS_BLUE_HORIZONTAL)){
                    pegs.add(new BluePeg(peg_position,BLUE_PEG_HORIZONTAL));
                }else if(tuples[0].equals(IS_BLUE_VERTICAL)){
                    pegs.add(new BluePeg(peg_position,BLUE_PEG_VERTICAL));
                }else if(tuples[0].equals(IS_GREY_PEG)){
                    pegs.add(new GreyPeg(peg_position,GREY_PEG));
                }else if(tuples[0].equals(IS_GREY_VERTICAL)){
                    pegs.add(new GreyPeg(peg_position,GREY_PEG_VERTICAL ));
                }else if(tuples[0].equals(IS_GREY_HORIZONTAL)){
                    pegs.add(new GreyPeg(peg_position,GREY_PEG_HORIZONTAL));
                }
            }
        }catch(IOException I){
            I.printStackTrace();
        }
        setBoard();
    }

    /**set the board by changing 1/5 blue pegs to red and initiate bucket  */
    public void setBoard(){
        int count_blue = 0;
        int red_peg_num;
        for (Peg p : pegs){
            if(p instanceof BluePeg){
                count_blue++;
            }
        }
        red_peg_num = count_blue/5;
        Random random = new Random();
        int red_i = 0;
        while(red_i < red_peg_num){
            int pegs_i = random.nextInt(pegs.size());
            if(pegs.get(pegs_i) instanceof BluePeg) {
                red_i++;
                String peg_imagename = pegs.get(pegs_i).getImagename();
                Point peg_position = pegs.get(pegs_i).getPosition();
                if (peg_imagename.equals(BLUE_PEG)) {
                    pegs.set(pegs_i, new RedPeg(peg_position, RED_PEG));
                }else if(peg_imagename.equals(BLUE_PEG_HORIZONTAL)){
                    pegs.set(pegs_i, new RedPeg(peg_position, RED_PEG_HORIZONTAL));
                }else if(peg_imagename.equals(BLUE_PEG_VERTICAL)){
                    pegs.set(pegs_i, new RedPeg(peg_position, RED_PEG_VERTICAL));
                }
            }
        }
        bucket = new Bucket(COORDINATION);
        setTurn();
    }

    /**set a new turn for the game
     * make sure green peg change back to blue from last turn
     * then change one random blue peg to green and set 1/10 chance of having powerup
     */
    public void setTurn(){
        for (Peg p : pegs){
            if(p instanceof GreenPeg){
                if (p.getImagename().equals(GREEN_PEG)) {
                    pegs.set(pegs.indexOf(p), new BluePeg(p.getPosition(), BLUE_PEG));
                }else if(p.getImagename().equals(GREEN_PEG_HORIZONTAL)){
                    pegs.set(pegs.indexOf(p), new BluePeg(p.getPosition(), BLUE_PEG_HORIZONTAL));
                }else if(p.getImagename().equals(GREEN_PEG_VERTICAL)){
                    pegs.set(pegs.indexOf(p), new BluePeg(p.getPosition(), BLUE_PEG_VERTICAL));
                }
            }
        }
        int blue_total = 0;
        for (Peg p : pegs){
            if(p instanceof BluePeg){
                blue_total++;
            }
        }
        //if have one or more blue peg, set a random blue peg to green
        if (blue_total > 0) {
            int j = 0;
            Random random = new Random();
            while(j < 1){
                int blue_i = random.nextInt(pegs.size());
                if(pegs.get(blue_i) instanceof BluePeg){
                    j++;
                    if (pegs.get(blue_i).getImagename().equals(BLUE_PEG)) {
                        pegs.set(blue_i, new GreenPeg(pegs.get(blue_i).getPosition(), GREEN_PEG ));
                    }else if(pegs.get(blue_i).getImagename().equals(BLUE_PEG_HORIZONTAL)){
                        pegs.set(blue_i, new GreenPeg(pegs.get(blue_i).getPosition(), GREEN_PEG_HORIZONTAL));
                    }else if(pegs.get(blue_i).getImagename().equals(BLUE_PEG_VERTICAL)){
                        pegs.set(blue_i, new GreenPeg(pegs.get(blue_i).getPosition(), GREEN_PEG_VERTICAL));
                    }
                }
            }
        }
        //1/10 chance of having powerup
        Random random = new Random();
        int powerup_chance = random.nextInt(10);
        if(powerup_chance==1){
            Point powerup_pt = new Point(Window.getWidth() * random.nextDouble(),
                    Window.getHeight() * random.nextDouble());
            powerup = new Powerup(powerup_pt, POWERUP);
        }else{
            powerup = null;
        }
    }

    /**create balls of same type by striking green peg*/
    public void createBall(Point position, Ball ball){
        Vector2 diagonally_left = new Vector2(-2,-2);
        Vector2 diagonally_right = new Vector2(2,-2);
        if(ball instanceof NormalBall) {
            ball_one = new NormalBall(position, diagonally_left.normalised());
            ball_two = new NormalBall(position, diagonally_right.normalised());
        }else if(ball instanceof FireBall){
            ball_one = new FireBall(position, diagonally_left.normalised());
            ball_two = new FireBall(position, diagonally_right.normalised());
        }
        balls.add(ball_one);
        balls.add(ball_two);
    }

    /**check if board has any red peg*/
    public boolean noRedPeg(){
        for(Peg peg : pegs){
            if(peg instanceof RedPeg){
                return false;
            }
        }
        return true;
    }

}
