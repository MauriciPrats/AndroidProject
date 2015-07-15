package multiplexaure.canvasapplication.BrickBreaker.CoreClasses;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Multiplexor on 06/07/2015.
 */
public class PressBubble extends DrawableObject {

    private float maxRadius = 200f;
    private float timeToGrow = 0.5f;

    private float radius = 0f;
    private float time = 0f;
    private Vector2 position;

    public PressBubble(Vector2 position){
        this.position = position;
    }
    public PressBubble(Vector2 position,float maxRadius){
        this.position = position;
        this.maxRadius = maxRadius;
    }
    @Override
    public void Update(float deltaTime){
        time+=deltaTime;
        float ratio = time/timeToGrow;
        radius = ratio * maxRadius;

        if(ratio>=1f){
            Destroy();
        }
    }

    Paint paint = new Paint();
    @Override
    public void onDraw(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#CD5C5C"));
        canvas.drawCircle(position.getX(),position.getY(),radius, paint);
    }
}

