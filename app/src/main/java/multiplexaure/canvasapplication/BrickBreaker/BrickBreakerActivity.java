package multiplexaure.canvasapplication.BrickBreaker;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.BackGround;
import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.CollisionDetectionManager;
import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.DestroyableRectangle;
import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.MainBall;
import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.PressBubble;
import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.Scene;
import multiplexaure.canvasapplication.BrickBreaker.CoreClasses.Vector2;
import multiplexaure.canvasapplication.R;

public class BrickBreakerActivity extends Activity {

    BrickBreakerView brickBreakerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        brickBreakerView = new BrickBreakerView(this);
        setContentView(brickBreakerView);
    }

    public void onClick(View view){

    }

    @Override
     protected void onStop() {
        super.onStop();
        brickBreakerView.stopThreads();
    }

    @Override
    protected void onStart() {
        super.onStart();
        brickBreakerView.createThread();
    }
}
