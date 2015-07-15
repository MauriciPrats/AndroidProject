package multiplexaure.canvasapplication.BrickBreaker.CoreClasses;

import android.graphics.Canvas;

/**
 * Created by Multiplexor on 08/07/2015.
 */
public abstract class DrawableObject {

    protected Boolean isDestroyed = false;

    public abstract void onDraw(Canvas canvas);

    public abstract void Update(float deltaTime);

    public Boolean getIsDestroyed(){
        return isDestroyed;
    }
    public void Destroy(){
        isDestroyed = true;
    }
}
