package multiplexaure.canvasapplication.BrickBreaker.CoreClasses;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.util.Random;

/**
 * Created by Multiplexor on 08/07/2015.
 */
public class DestroyableRectangle extends DrawableObject{

    private float x,y;
    private float width,height;
    private float scalex = 1f;
    private float scaley = 1f;
    private String color = "#E54028";
    private float pointsAmmount = 10f;
    public float timeIncreasePoints = 5f;
    private int posX,posY;

    public DestroyableRectangle(float x,float y,float width,float height,int posX,int posY){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        originalScaleX = scalex;
        originalScaleY = scaley;
        this.posX = posX;
        this.posY = posY;

        rect = new Rect((int)getLeft(),(int)getTop(),(int)getRight(),(int)getBottom());
    }

    public int getPosX(){return posX;}
    public int getPosY(){ return posY;}
    public float getLeft(){return x -(width*scalex)/2f ;}
    public float getRight(){return x+(width*scalex)/2f;}
    public float getTop(){return y-(height*scaley)/2f;}
    public float getBottom(){return y+(height*scaley)/2f;}
    public float getX(){return x;}
    public float getWidth(){return width * scalex;}
    public float getY(){return y;}
    public float getHeight(){return height * scaley;}
    public float getCenterX(){return x;}
    public float getCenterY(){return y;}
    public Vector2 getCenter(){return new Vector2(getCenterX(),getCenterY());}
    Paint paint = new Paint();
    Rect rect;
    @Override
    public void onDraw(Canvas canvas) {

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor(color));
        paint.setAlpha(60);

        canvas.drawRect(rect, paint);
        //canvas.drawCircle(x,y,20f,paint);
    }

    public void OnHit(){
        float randomVal = new Random().nextFloat();
        if(randomVal<0.25f){
            color = "#D70060";
        }else if(randomVal<0.5f){
            color = "#32742C";
        }else if(randomVal<0.75f){
            color = "#01A4A4";
        }else if(randomVal<1f){
            color = "#00A1CB";
        }
        Beat();
    }

    private Boolean isBeating = false;
    private Boolean isBeatingDown = false;
    private float timeToBeat = 0.07f;
    private float scaleExtra = 0.5f;
    private float timeAccumulated = 0f;
    private float originalScaleX,originalScaleY;

    public void Beat(){
        EndBeat();
        timeAccumulated = 0f;
        isBeating = true;
        isBeatingDown = true;
    }

    private void EndBeat(){
        scalex = originalScaleX;
        scaley = originalScaleY;
        isBeating = false;
        Destroy();
    }

    @Override
    public void Update(float deltaTime) {
        if(isBeating){
            timeAccumulated+=deltaTime;
            if(timeAccumulated>timeToBeat){
                if(isBeatingDown){
                    timeAccumulated = 0f;
                    isBeatingDown = false;
                }else{
                    EndBeat();
                }
            }else{
                if(isBeatingDown){
                    float ratio = (timeAccumulated/timeToBeat);
                    scalex = originalScaleX - (ratio * scaleExtra);
                    scaley = originalScaleY - (ratio * scaleExtra);
                }else{
                    float invertRatio = 1f-(timeAccumulated/timeToBeat);
                    scalex = originalScaleX - (invertRatio * scaleExtra);
                    scaley = originalScaleY - (invertRatio * scaleExtra);
                }
            }
        }
        pointsAmmount+= (deltaTime * timeIncreasePoints);
    }

    public float getPointAmmount(){
        return pointsAmmount;
    }
}
