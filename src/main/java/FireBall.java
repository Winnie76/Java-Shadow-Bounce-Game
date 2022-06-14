import bagel.Image;
import bagel.util.Point;
import bagel.util.Vector2;

import java.util.ArrayList;

/**FireBall is subclass of Ball */
public class FireBall extends Ball {
    private static final String FIREB_IMAGENAME = "res/fireball.png";
    private static final double WITHIN_SEVENTY = 70;

    public FireBall(Point position, Vector2 direction){
        super(position,direction,FIREB_IMAGENAME);
    }
    /**fireball will destroy all pegs within 70 pixels from it once intersects with a peg*/
    public void explode(ArrayList<Peg> pegs, Point position){
        for(Peg peg: new ArrayList<>(pegs)){
            if(!(peg instanceof GreyPeg) && peg.getPosition().asVector().sub(position.asVector()).length()
                    <= WITHIN_SEVENTY){
                pegs.remove(peg);
            }
        }
    }
}
