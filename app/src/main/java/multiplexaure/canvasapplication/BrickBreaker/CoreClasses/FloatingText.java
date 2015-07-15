package multiplexaure.canvasapplication.BrickBreaker.CoreClasses;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.DrawableObject;
import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.Vector2;

/**
 * Created by Multiplexor on 11/07/2015.
 */
public class FloatingText extends DrawableObject {
    float timeToFadeIn;
    float timeToFadeOut;
    String text;
    float ySpeed = 0f;
    Vector2 position;
    private float textSize = 100f;


    private float alpha = 0f;
    private Boolean fadingIn = true;
    private float timer = 0f;

    public FloatingText(String text,float timeFadeIn,float timeFadeOut,Vector2 position){
        timeToFadeIn = timeFadeIn;
        timeToFadeOut = timeFadeOut;
        this.position = position;
        this.text = text;
        if(timeFadeIn<=0f){
            alpha = 1f;
            fadingIn = false;
        }
    }

    public FloatingText(String text,float timeFadeIn,float timeFadeOut,Vector2 position,float textSize){
        timeToFadeIn = timeFadeIn;
        timeToFadeOut = timeFadeOut;
        this.position = position;
        this.text = text;
        if(timeFadeIn<=0f){
            alpha = 1f;
            fadingIn = false;
        }
        this.textSize = textSize;
    }


    @Override
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#444444"));
        paint.setAlpha((int) (255 * alpha));
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(text,position.getX(),position.getY(),paint);
    }

    @Override
    public void Update(float deltaTime) {
        timer+=deltaTime;
        if(fadingIn){
            if(timer>=timeToFadeIn){
                fadingIn = false;
                alpha = 1f;
                timer = 0f;
            }else{
                float ratio = timer/timeToFadeIn;
                alpha = ratio;
            }
        }else{
            if(timer>=timeToFadeOut){
                Destroy();
            }else{
                float invertRatio = 1f-(timer/timeToFadeOut);
                alpha = invertRatio;
            }

        }
    }
}
