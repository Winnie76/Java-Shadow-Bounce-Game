import bagel.util.Point;
import bagel.util.Vector2;

/**NormalBall is subclass of Ball*/
public class NormalBall extends Ball{
    private static final String BALL_IMAGENAME = "res/ball.png";

    public NormalBall(Point position, Vector2 direction) {
        super(position, direction,BALL_IMAGENAME);
    }

}
