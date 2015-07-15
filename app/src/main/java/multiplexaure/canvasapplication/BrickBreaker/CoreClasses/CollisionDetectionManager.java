package multiplexaure.canvasapplication.BrickBreaker.CoreClasses;

import java.util.Iterator;
import java.util.List;

import multiplexaure.canvasapplication.BrickBreaker.BrickBreakerView;

/**
 * Created by Multiplexor on 08/07/2015.
 */
public class CollisionDetectionManager {

    public static Boolean intersects(DestroyableRectangle rectangle,MainBall ball){
        Vector2 circleDistance = new Vector2(Math.abs(ball.getPosition().getX()-(rectangle.getCenterX())),Math.abs(ball.getPosition().getY()-rectangle.getCenterY()));

        if (circleDistance.getX() > (rectangle.getWidth()/2 + ball.getBallRadius())) { return false; }
        if (circleDistance.getY() > (rectangle.getHeight()/2 + ball.getBallRadius())) { return false; }

        if (circleDistance.getX() <= (rectangle.getWidth()/2)) { return true; }
        if (circleDistance.getY() <= (rectangle.getHeight()/2)) { return true; }

        float xSq = (circleDistance.getX() - rectangle.getWidth()/2);
        float ySq = (circleDistance.getY() - rectangle.getHeight()/2);
        float sqDistance = (xSq *xSq) + (ySq *ySq);

        return (sqDistance <= (ball.getBallRadius()*ball.getBallRadius()));

    }

    public static Boolean intersects(DeathBlock rectangle,MainBall ball){
        Vector2 circleDistance = new Vector2(Math.abs(ball.getPosition().getX()-(rectangle.getCenterX())),Math.abs(ball.getPosition().getY()-rectangle.getCenterY()));

        if (circleDistance.getX() > (rectangle.getWidth()/2 + ball.getBallRadius())) { return false; }
        if (circleDistance.getY() > (rectangle.getHeight()/2 + ball.getBallRadius())) { return false; }

        if (circleDistance.getX() <= (rectangle.getWidth()/2)) { return true; }
        if (circleDistance.getY() <= (rectangle.getHeight()/2)) { return true; }

        float xSq = (circleDistance.getX() - rectangle.getWidth()/2);
        float ySq = (circleDistance.getY() - rectangle.getHeight()/2);
        float sqDistance = (xSq *xSq) + (ySq *ySq);

        return (sqDistance <= (ball.getBallRadius()*ball.getBallRadius()));

    }

    public static void findCollisions(List<DestroyableRectangle> collidingRectangles,MainBall ball,BrickBreakerView bbv){
        Iterator<DestroyableRectangle> rectangles = collidingRectangles.iterator();
        while(rectangles.hasNext()){
            DestroyableRectangle dr = rectangles.next();
            if(intersects(dr,ball)) {
                float xAxisDistanceProportion = Math.abs(ball.getPosition().getX() - dr.getCenterX())/dr.getWidth();
                float yAxisDistanceProportion = Math.abs(ball.getPosition().getY() - dr.getCenterY())/dr.getHeight();
                if(xAxisDistanceProportion>yAxisDistanceProportion) {
                    if (Math.abs(ball.getPosition().getX() - dr.getLeft()) < ball.getBallRadius()) {
                        ball.setDirection(ball.getDirection().setGoingLeft());
                        dr.OnHit();
                        rectangles.remove();
                        bbv.gainPoints(dr);
                    } else if (Math.abs(ball.getPosition().getX() - dr.getRight()) < ball.getBallRadius()) {
                        ball.setDirection(ball.getDirection().setGoingRight());// Hit the right
                        dr.OnHit();
                        rectangles.remove();
                        bbv.gainPoints(dr);
                    }
                } else{
                    if (Math.abs(ball.getPosition().getY() - dr.getTop()) < ball.getBallRadius()) {
                        ball.setDirection(ball.getDirection().setGoingUp());
                        dr.OnHit();
                        rectangles.remove();
                        bbv.gainPoints(dr);
                    } else if (Math.abs(ball.getPosition().getY() - dr.getBottom()) < ball.getBallRadius()) {
                        ball.setDirection(ball.getDirection().setGoingDown());
                        dr.OnHit();
                        rectangles.remove();
                        bbv.gainPoints(dr);
                    }
                }

            }
        }
    }
}
