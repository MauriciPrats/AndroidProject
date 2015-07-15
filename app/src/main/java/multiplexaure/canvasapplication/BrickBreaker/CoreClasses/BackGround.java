package multiplexaure.canvasapplication.BrickBreaker.CoreClasses;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Multiplexor on 08/07/2015.
 */
public class BackGround  extends DrawableObject{
    Paint paint = new Paint();
    @Override
    public void onDraw(Canvas canvas) {

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.LTGRAY);
        canvas.drawPaint(paint);
    }

    @Override
    public void Update(float deltaTime) {

    }
}
