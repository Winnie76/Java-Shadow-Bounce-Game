import bagel.Window;
import bagel.util.Point;
import bagel.util.Vector2;

/**Bucket is subclass of ObjectInGame */
public class Bucket extends ObjectInGame {
    private static final String BUCKET_IMAGENAME = "res/bucket.png" ;
    private Vector2 velocity = new Vector2(-4, 0);

    public Bucket(Point position){
        super(position,BUCKET_IMAGENAME);
    }

    /**update the movement of bucket*/
    public void update(){

        super.draw();

        if (super.getRectangle().left() < 0 || super.getRectangle().right() > Window.getWidth()) {
            velocity = new Vector2(-velocity.x, velocity.y);
        }

        Point new_pt = super.getPosition().asVector().add(velocity).asPoint();
        super.setPosition(new_pt);
        super.setRectangle(new_pt);
    }
}
