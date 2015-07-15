package multiplexaure.canvasapplication.BrickBreaker.CoreClasses;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Multiplexor on 10/07/2015.
 */
public class ExpandingBubble extends DrawableObject{

    private float timeToGrow = 1f;
    private float growPerSecond = 300f;
    private float maxRadius = 1000;

    private float radius = 0f;
    private float time = 0f;
    private Vector2 position;
    private String color;
    private Scene scene;
    private Boolean maxGrown = false;

    public ExpandingBubble(Vector2 position,String color,Scene scene){
        this.position = position;
        this.color = color;
        this.scene = scene;
    }

    @Override
    public void Update(float deltaTime){
        if(!maxGrown) {
            time += deltaTime;
            float ratio = time / timeToGrow;
            radius = ratio * growPerSecond;
            if (radius > maxRadius) {
                maxGrown = true;
                scene.deleteAllLowerOnes(this);
                radius = maxRadius;
            }
        }
    }

    Paint paint = new Paint();
    @Override
    public void onDraw(Canvas canvas) {

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor(color));
        canvas.drawCircle(position.getX(),position.getY(),radius, paint);
    }
}
