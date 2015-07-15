package multiplexaure.canvasapplication.BrickBreaker.CoreClasses;

/**
 * Created by Multiplexor on 03/07/2015.
 */
public class Vector2 {
    private float x;
    private float y;

    public Vector2(float x,float y){
        this.x = x;
        this.y = y;
    }

    public Vector2 AddVector(Vector2 v){
        Vector2 result = new Vector2(x+v.x,y+v.y);
        return result;
    }

    public Vector2 SubstractVector(Vector2 v){
        Vector2 result = new Vector2(x-v.x,y-v.y);
        return result;
    }

    public Vector2 MulFloat(float f){
        Vector2 result = new Vector2(x*f,y*f);
        return result;
    }

    public Vector2 invertX(){
        Vector2 result = new Vector2(x*-1f,y);
        return result;
    }

    public Vector2 setGoingUp(){
        Vector2 result = new Vector2(x,Math.abs(y)*-1f);
        return result;
    }

    public Vector2 setGoingDown(){
        Vector2 result = new Vector2(x,Math.abs(y));
        return result;
    }

    public Vector2 setGoingLeft(){
        Vector2 result = new Vector2(Math.abs(x)*-1f,y);
        return result;
    }

    public Vector2 setGoingRight(){
        Vector2 result = new Vector2(Math.abs(x),y);
        return result;
    }

    public Vector2 invertY(){
        Vector2 result = new Vector2(x,y*-1f);
        return result;
    }

    public Vector2 Lerp(Vector2 endVector,float lerp){
        Vector2 direction = endVector.SubstractVector(this);
        return this.AddVector(direction.MulFloat(lerp));
    }

    public float distance(Vector2 otherV){
        Vector2 difference = SubstractVector(otherV);
        if(difference.getX()!=0f || difference.getY()!=0f) {
            float result = (float) Math.sqrt((double) ((difference.getX() * difference.getX()) + (difference.getY() * difference.getY())));
            return result;
        }else{
            return 0f;
        }
    }

    public float Length(){
        return ((float) Math.sqrt((x * x) + (y * y)));
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public void Normalize(){
        float length = this.Length();

        if(length != 0){
            x = x/length;
            y = y/length;
        }
    }
}
