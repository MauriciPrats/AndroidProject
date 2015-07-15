package multiplexaure.canvasapplication.BrickBreaker.CoreClasses;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Multiplexor on 08/07/2015.
 */
public class Scene {

    private List<DrawableObject> drawableObjects = new ArrayList();

    private DrawableObject toDelete;

    public void Update(float deltaTime){
        Iterator<DrawableObject> iteratorDrawable = drawableObjects.iterator();
        while(iteratorDrawable.hasNext()){
            DrawableObject drawable = iteratorDrawable.next();
            if(drawable.getIsDestroyed()){
                iteratorDrawable.remove();
            }else {
                drawable.Update(deltaTime);
            }
        }
       if(toDelete!=null){
           clearLowerOnes();
           toDelete = null;
       }
    }

    public void Draw(Canvas canvas){
        for(DrawableObject drawableObject : drawableObjects){
            drawableObject.onDraw(canvas);
        }
    }

    public void deleteAllLowerOnes(DrawableObject drawableObject){
        toDelete = drawableObject;
    }

    private void clearLowerOnes(){
        if(drawableObjects.contains(toDelete)){
            int indexD = drawableObjects.indexOf(toDelete);
            Iterator<DrawableObject> it = drawableObjects.iterator();
            int i = 0;
            while(i<indexD){
                i++;
                it.next();
                it.remove();
            }
        }
    }

    public void clear(){
        drawableObjects.clear();
    }

    public void registerNewDrawable(DrawableObject ob){
        drawableObjects.add(ob);
    }
}
