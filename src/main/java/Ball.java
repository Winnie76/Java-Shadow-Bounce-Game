import bagel.Window;
import bagel.util.Point;
import bagel.util.Side;
import bagel.util.Vector2;

/**Ball is subclass of ObjectInGame, it includes everything from superclass
 * In addition it has velocity and direction (of movement) of object in constructor */
public class Ball extends ObjectInGame {
    private static final double GRAVITY = 0.15;
    private static final double SPEED = 10;
    private Vector2 velocity;
    private Vector2 direction;

    public Ball(Point position, Vector2 direction, String imagename) {

        super(position, imagename);
        this.velocity = direction.mul(SPEED);
        this.direction = direction;
    }

    /**check if ball falls out of screen*/
    public boolean outOfScreen() {
        return getRectangle().top() >= Window.getHeight();
    }
    /**get direction of movement of object*/
    public Vector2 getDirection(){
        return direction;
    }

    /**update the new position of the ball from its velocity*/
    public void update() {

        velocity = velocity.add(Vector2.down.mul(GRAVITY));

        if (super.getRectangle().left() < 0 || super.getRectangle().right() > Window.getWidth()) {
            velocity = new Vector2(-velocity.x, velocity.y);
        }
        Point new_bpt = super.getPosition().asVector().add(velocity).asPoint();
        super.setPosition(new_bpt);
        super.setRectangle(new_bpt);

    }

    /**change velocity when ball strikes with differ sides of a peg*/
    public void setVelocity(Side side){
        if(side.equals(Side.LEFT)||side.equals(Side.RIGHT)){
            velocity = new Vector2(-velocity.x, velocity.y);
        }else if(side.equals(Side.TOP)||side.equals(Side.BOTTOM)){
            velocity = new Vector2(velocity.x, -velocity.y);
        }else{
            velocity = new Vector2(-velocity.x, velocity.y);
        }
    }
    /**get velocity of ball*/
    public Vector2 getVelocity(){
        return velocity;
    }
}

