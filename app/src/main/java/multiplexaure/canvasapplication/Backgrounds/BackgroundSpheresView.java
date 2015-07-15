package multiplexaure.canvasapplication.Backgrounds;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.BackGround;
import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.DrawableObject;
import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.ExpandingBubble;
import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.Scene;
import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.Vector2;
import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.FloatingText;

/**
 * Created by Multiplexor on 10/07/2015.
 */
public class BackgroundSpheresView extends SurfaceView {

        long time;
        Boolean finished = false;
        float lastDeltaTime;
        Scene scene  = new Scene();
        List<DrawableObject> expandingBalls = new ArrayList();
        private int PIXEL_HEIGHT;
        private int PIXEL_WIDTH;

        String[] colors = {"#FF6961","#77DD77","#ACF3FD","#FFB347","#B39EB5","#FCDB5C","#7CC5E5"};
        int nColors = 0;
        Handler mHandler = new Handler();

        public void setNColor(int newNColor){
            nColors = (newNColor)%colors.length;
        }

        private void initializeVariables(Context context){
            Display display =  ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            PIXEL_WIDTH = size.x;
            PIXEL_HEIGHT = size.y;
            BackGround background = new BackGround();
            scene.registerNewDrawable(background);
            //Sets the enviroment variables
            setWillNotDraw(false);
        }

        public void setWelcomeText(){
            FloatingText ft = new FloatingText("Welcome!",0.5f,2f,new Vector2(PIXEL_WIDTH/2f,PIXEL_HEIGHT/2f));
            scene.registerNewDrawable(ft);
        }


        public BackgroundSpheresView(Context context) {
            super(context);
            initializeVariables(context);
        }

        public void createThread(){

            finished = false;
            new Thread(new Runnable() {
                //Thread that keeps on updating after 33 ms (30fps)
                @Override
                public void run() {
                    time = System.currentTimeMillis();
                    while (!finished) {
                        try {
                            Thread.sleep(16);
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    timeStep();
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        public void stopThreads(){
            finished = true;
        }

        void timeStep(){
            long timePassed = System.currentTimeMillis() - time;
            float milisecondsInASecond = 1000f;
            float deltaTime = ((float)timePassed) / milisecondsInASecond;
            Update(deltaTime);
            invalidate();
            time = System.currentTimeMillis();
        }

        void Update(float deltaTime){
            lastDeltaTime = deltaTime;
            scene.Update(deltaTime);
        }

        public void finish(){
            finished = true;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            scene.Draw(canvas);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            onTouchBall(event.getX(),event.getY());
            return super.onTouchEvent(event);
        }

        private void onTouchBall(float x,float y){
            Vector2 pointClicked = new Vector2(x,y);
            ExpandingBubble bubble = new ExpandingBubble(pointClicked,colors[nColors],scene);
            expandingBalls.add(bubble);
            scene.registerNewDrawable(bubble);
            nColors = (nColors+1)%colors.length;
        }

    Boolean firstBall = false;
        public void touchBallCenterWithDelay(){
            if(!firstBall) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onTouchBall(PIXEL_WIDTH / 2f, PIXEL_HEIGHT);
                    }
                }, 200);
                firstBall = true;
            }
        }

}
