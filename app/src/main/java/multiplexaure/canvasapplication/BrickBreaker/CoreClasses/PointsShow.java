package multiplexaure.canvasapplication.BrickBreaker.CoreClasses;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Multiplexor on 14/07/2015.
 */
public class PointsShow extends DrawableObject {

    private float pointsAccumulated;

    public void setPointsAccumulated(float pointsAccumulated){
        this.pointsAccumulated = pointsAccumulated;
    }
    Paint paint = new Paint();
    @Override
    public void onDraw(Canvas canvas) {
        paint.setStyle(Paint.Style.FILL);
        paint.setFakeBoldText(true);
        paint.setColor(Color.parseColor("#888888"));
        paint.setTextSize(60f);
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Points: "+((int)pointsAccumulated),10, 50, paint);
    }

    @Override
    public void Update(float deltaTime) {

    }
}
