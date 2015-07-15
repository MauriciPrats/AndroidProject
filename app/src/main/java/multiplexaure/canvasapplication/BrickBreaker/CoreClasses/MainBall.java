package multiplexaure.canvasapplication.BrickBreaker.CoreClasses;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Multiplexor on 06/07/2015.
 */
public class MainBall extends DrawableObject {

    private float radius = 20f;

    private Vector2 position;
    private Vector2 direction;
    private float speed = 400f;
    private Trail trail;
    private boolean isActive = true;

    public MainBall(){
        trail = new Trail();
    }

    public float getBallRadius(){
        return radius;
    }
    public Vector2 getPosition(){
        return position;
    }

    public Vector2 getDirection(){
        return direction;
    }

    public void initialize(int levelWidth,int levelHeight){
        position = new Vector2(levelWidth/2f,levelHeight-120);
        direction = new Vector2(0f,0f);
        direction.Normalize();
        System.out.println(position.getX()+" "+position.getY());
    }

    public void setPosition(Vector2 pos){
        position = pos;
    }

    public void setDirection(Vector2 dir){
        trail.AddPoint(position);
        direction = dir;
        direction.Normalize();
    }

    public Vector2 getNextPosition(float deltaTime){
        return position.AddVector(direction.MulFloat(speed).MulFloat(deltaTime));
    }

    Paint paint = new Paint();
    public void Draw(Canvas canvas){
        trail.Draw(canvas,position);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#FF3C08"));
        canvas.drawCircle(getPosition().getX(),getPosition().getY(),getBallRadius(), paint);
    }

    @Override
    public void onDraw(Canvas canvas) {
        trail.Draw(canvas,position);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#F18D05"));
        canvas.drawCircle(getPosition().getX(),getPosition().getY(),getBallRadius(), paint);
    }

    public void deactivate(){
        isActive = false;
    }

    @Override
    public void Update(float deltaTime) {
        speed+=(deltaTime*20f);
        if(isActive) {
            setPosition(getNextPosition(deltaTime));
        }
    }
}
