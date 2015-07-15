package multiplexaure.canvasapplication.BrickBreaker.CoreClasses;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.LinkedList;

/**
 * Created by Multiplexor on 06/07/2015.
 */
public class Trail {

    private LinkedList<Vector2> points;
    private float length = 500f;
    private int maxSegments = 20;

    public Trail(){
        points = new LinkedList();
    }

    public void AddPoint(Vector2 point){
        points.addFirst(point);
        if(points.size()==maxSegments){
            points.remove(maxSegments-1);
        }
    }

    Paint paint = new Paint();
    Path path = new Path();
    void Draw(Canvas canvas,Vector2 actualPosition){

        //Definim el paint de totes les linies (trails)

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#F18D05"));
        paint.setStrokeWidth(10f);
        paint.setStrokeJoin(Paint.Join.ROUND);

        float accumulatedDistance = 0f;
        Boolean trailCompleted = false;

        path.reset();
        path.moveTo(actualPosition.getX(), actualPosition.getY());
        if(points.size()>0 && actualPosition.distance(points.get(0))>length){
            float proportion = (length)/actualPosition.distance(points.get(0));
            Vector2 newPosition = actualPosition.Lerp(points.get(0),proportion);
            path.lineTo(newPosition.getX(),newPosition.getY());
        }else if(points.size()>0) {
            path.lineTo(points.get(0).getX(),points.get(0).getY());
            accumulatedDistance = actualPosition.distance(points.get(0));
            for (int i = 1; i < points.size(); i++) {
                if((points.get(i-1).distance(points.get(i))+accumulatedDistance)>length){
                    float proportion = (length - accumulatedDistance)/points.get(i-1).distance(points.get(i));
                    Vector2 newPosition = points.get(i-1).Lerp(points.get(i),proportion);
                    path.lineTo(newPosition.getX(),newPosition.getY());
                    break;
                }else {
                    accumulatedDistance+=points.get(i-1).distance(points.get(i));
                    path.lineTo(points.get(i).getX(), points.get(i).getY());
                }
            }
        }
        canvas.drawPath(path,paint);
    }

}
