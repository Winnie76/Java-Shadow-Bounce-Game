import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;

/**
 * This is the superclass of objects in the game
 * the constructor includes position of object, image & imagename of object and also bounding box for the image
 */

public class ObjectInGame {
    private Point position;
    private Image image;
    private Rectangle rectangle;
    private String imagename;

    /**constructor ObjectInGame*/
    public ObjectInGame(Point position, String imagename) {
        this.imagename = imagename;
        this.image = new Image(imagename);
        this.position = position;
        this.rectangle = this.image.getBoundingBoxAt(position);
    }

    /**get String imagename (address of image) of the object*/
    public String getImagename(){
        return this.imagename;
    }

    /**get the position of the object*/
    public Point getPosition() {
        return this.position;
    }

    /**set the new position of the object*/
    public void setPosition(Point position) {
        this.position = position;
    }

    /**get the rectangle(bounding box) of the object's image to use for collision detection*/
    public Rectangle getRectangle() {
        return this.rectangle;
    }

    /**set the rectangle(bounding box) of the object's image to use for collision detection*/
    public void setRectangle(Point position){
        this.rectangle=this.image.getBoundingBoxAt(position);
    }

    /**draw the object*/
    public void draw() {
        this.image.draw(position.x, position.y);
    }

}
