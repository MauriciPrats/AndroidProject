package multiplexaure.canvasapplication.BrickBreaker.CoreClasses;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Created by Multiplexor on 14/07/2015.
 */
public class DeathBlock extends DrawableObject {

Vector2 position,dimensions;
    public DeathBlock(Vector2 position,Vector2 dimensions){
        this.position = position;
        this.dimensions = dimensions;
        rect = new Rect((int)position.getX(),(int)position.getY(),(int)(dimensions.getX()+position.getX()),(int)(position.getY()+dimensions.getY()));
    }

    public float getX(){return position.getX();}
    public float getWidth(){return dimensions.getX();}
    public float getY(){return position.getY();}
    public float getHeight(){return dimensions.getY();}
    public float getCenterX(){return position.getX() +(dimensions.getX()/2);}
    public float getCenterY(){return position.getY() + (dimensions.getY()/2);}

    Paint paint = new Paint();
    Rect rect;
    @Override
    public void onDraw(Canvas canvas) {

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#FF0000"));

        canvas.drawRect(rect, paint);
    }

    @Override
    public void Update(float deltaTime) {

    }
}
