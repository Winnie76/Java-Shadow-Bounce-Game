import bagel.Window;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.Random;

/**powerup is subclass of ObjectInGame and it stores the random position currently moving to in constructor */
public class Powerup extends ObjectInGame{
    private static final int SPEED = 3;
    private static final double WITHINFIVE = 5;
    private Point random_pt;

    public Powerup(Point position, String imagename){
        super( position, imagename);
        random_pt = randomPosition();
    }

    /**get a new random position */
    public Point randomPosition(){
        Random random = new Random();
        random_pt = new Point(Window.getWidth() * random.nextDouble(),
                Window.getHeight() * random.nextDouble());
        return random_pt;
    }

    /**get the new velocity from the new random destination it is moving to*/
    public Vector2 moveTowards(Point destination, Point position){
        Vector2  vector = destination.asVector().sub(position.asVector());
        Vector2 velocity = vector.normalised().mul(SPEED);
        return velocity;
    }

    /**check if powerup is within 5 pixels from its destination*/
    public boolean withinFive(Point current_pt, Point destination){
        Vector2 vector = destination.asVector().sub(current_pt.asVector());
        double length = vector.length();
        if(length<=WITHINFIVE){
            return true;
        }else{
            return false;
        }
    }

    /**update the new position of powerup (towards its destination)
     * if it is within five pixels from destination, it will move to a new random destination
     */
    public void update(){
        if(withinFive(super.getPosition(), random_pt)){
            random_pt = randomPosition();
        }else{
            Point new_powerpt = super.getPosition().asVector().add(moveTowards(random_pt,super.getPosition())).asPoint();
            super.setPosition(new_powerpt);
            super.setRectangle(new_powerpt);
        }
    }
}
