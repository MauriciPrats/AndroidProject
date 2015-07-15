package multiplexaure.canvasapplication.BrickBreaker;

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
import java.util.Random;

import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.BackGround;
import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.CollisionDetectionManager;
import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.DeathBlock;
import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.DestroyableRectangle;
import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.ExpandingBubble;
import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.FloatingText;
import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.MainBall;
import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.PointsShow;
import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.PressBubble;
import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.Scene;
import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.Vector2;
import multiplexaure.canvasapplication.BBDD.MyDataAccess;
import multiplexaure.canvasapplication.BBDD.RankingUser;
import multiplexaure.canvasapplication.BBDD.User;

/**
 * Created by Multiplexor on 09/07/2015.
 */
public class BrickBreakerView extends SurfaceView {

    private List<DestroyableRectangle> collidingRectangles = new ArrayList();
    private float lastDeltaTime = 0f;
    private Scene scene  = new Scene();
    private int PIXEL_HEIGHT;
    private int PIXEL_WIDTH;

    float pointsAccumulated = 0;


    MainBall ball;
    float speed = 1000f;
    float ballRadius = 20f;

    int xLength = 4;
    int yLength = 4;

    Random random = new Random();

    Boolean[][] blocksGrid = new Boolean[xLength][yLength];
    PointsShow points = new PointsShow();
    DeathBlock deathBlock;
    private Boolean isGamePlaying = false;
    long time;
    Boolean finished = false;

    MyDataAccess dataAccess;

    Handler mHandler = new Handler();
    BackGround backGround;
    void registerNewRectangle(DestroyableRectangle rect){
        scene.registerNewDrawable(rect);
        blocksGrid[rect.getPosX()][rect.getPosY()] = true;
        collidingRectangles.add(rect);
    }

    private void initializeVariables(Context context){

        scene.clear();
        collidingRectangles.clear();
        //Sets the enviroment variables
        setWillNotDraw(false);
        Display display =  ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        PIXEL_WIDTH = size.x;
        PIXEL_HEIGHT = size.y;
        calculateDistances();
        time = System.currentTimeMillis();

        for (int i = 0; i < blocksGrid.length; i++) {
            for (int j = 0; j < blocksGrid[i].length; j++) {
                blocksGrid[i][j] = false;
            }
        }
        //Elements in the scene
         backGround  = new BackGround();
        scene.registerNewDrawable(backGround);

        deathBlock = new DeathBlock(new Vector2(0,PIXEL_HEIGHT-90),new Vector2(PIXEL_WIDTH,90));
        scene.registerNewDrawable(deathBlock);

        ball = new MainBall();
        ball.initialize(PIXEL_WIDTH, PIXEL_HEIGHT);
        scene.registerNewDrawable(ball);
        scene.registerNewDrawable(points);
        pointsAccumulated = 0f;
        dataAccess = new MyDataAccess(getContext());
        isGamePlaying = true;
    }

    public BrickBreakerView(Context context) {
        super(context);
        initializeVariables(context);
    }

    public void createThread(){
        finished = false;
        new Thread(new Runnable() {
            //Thread that keeps on updating after 33 ms (30fps)
            @Override
            public void run() {
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
        time = System.currentTimeMillis();
    }

    public void stopThreads(){
        finished = true;
    }

    float timeToSpawnn = 0.5f;
    float timerSpawn = 0f;

    float offsetY = 40f;
    float offsetX = 40f;

    float widthBlock = 60f;
    float heightBlock = 40f;

    float offsetXStart;
    float offsetYStart;

    private void calculateDistances(){
        offsetXStart = (PIXEL_WIDTH - ((widthBlock * xLength) + (offsetX * (xLength-1))))/2f;
        offsetYStart = (PIXEL_HEIGHT - ((heightBlock * yLength) + (offsetY * (yLength-1))))/2f;
    }

    private void spawnBlocks(float deltaTime){
        if(isGamePlaying) {
            timerSpawn += deltaTime;
            if (timerSpawn >= timeToSpawnn) {
                //Spawn blocks
                int randomX = random.nextInt(xLength);
                int randomY = random.nextInt(yLength);

                float positionX = offsetXStart + offsetX / 2f + ((randomX) * offsetX) + (widthBlock * randomX);
                float positionY = offsetYStart + offsetY / 2f + ((randomY) * offsetY) + (heightBlock * randomY);

                if (!blocksGrid[randomX][randomY]) {
                    registerNewRectangle(new DestroyableRectangle(positionX, positionY, widthBlock, heightBlock, randomX, randomY));
                }

                timerSpawn = 0f;
            }
        }
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
        checkWallCollisions(deltaTime);
        if(isGamePlaying) {
            CollisionDetectionManager.findCollisions(collidingRectangles, ball, this);
        }
        scene.Update(deltaTime);
        spawnBlocks(deltaTime);
        points.setPointsAccumulated(pointsAccumulated);
        if(CollisionDetectionManager.intersects(deathBlock,ball) && isGamePlaying){
            isGamePlaying = false;
            //scene.registerNewDrawable(backGround);
            scene.registerNewDrawable(new ExpandingBubble(ball.getPosition(), "#F18D05", scene));
            scene.registerNewDrawable(points);
            finishMatch();
            ball.deactivate();
        }
    }

    private void finishMatch(){
        RankingUser user = new RankingUser();
        User actualUser = dataAccess.getLastUserLogged();
        user.name = actualUser.name;
        user.points = (int)pointsAccumulated;
        dataAccess.insertNewRanking(user);
    }

    public void finish(){
        finished = true;
    }

    void checkWallCollisions(float deltaTime){
        float futurePositionX = ball.getNextPosition(deltaTime).getX();
        float futurePositionY = ball.getNextPosition(deltaTime).getY();

        if(futurePositionX<ballRadius || futurePositionX>PIXEL_WIDTH-ballRadius){
            ball.setDirection(ball.getDirection().invertX());
        }

        if(futurePositionY<ballRadius || futurePositionY>(PIXEL_HEIGHT-ballRadius)){
            ball.setDirection(ball.getDirection().invertY());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        scene.Draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Vector2 pointClicked = new Vector2(event.getX(),event.getY());
        if(pointClicked.distance(ball.getPosition())<200f){
            ball.setDirection(ball.getPosition().SubstractVector(pointClicked));
        }
        scene.registerNewDrawable(new PressBubble(pointClicked));
        if(!isGamePlaying){
            initializeVariables(getContext());
        }
        return super.onTouchEvent(event);
    }

    public void gainPoints(DestroyableRectangle dr){
        pointsAccumulated+=dr.getPointAmmount();
        FloatingText text = new FloatingText(((int)dr.getPointAmmount())+"",0.2f,1f,dr.getCenter(),40f);
        scene.registerNewDrawable(text);
        blocksGrid[dr.getPosX()][dr.getPosY()] = false;
        scene.registerNewDrawable(new PressBubble(dr.getCenter(),50f));
    }
}
