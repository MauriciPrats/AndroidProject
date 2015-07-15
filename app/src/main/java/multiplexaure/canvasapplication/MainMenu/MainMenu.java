package multiplexaure.canvasapplication.MainMenu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;

import multiplexaure.canvasapplication.Audio.AudioActivity;
import multiplexaure.canvasapplication.BBDD.MyDataAccess;
import multiplexaure.canvasapplication.BrickBreaker.BrickBreakerActivity;
import multiplexaure.canvasapplication.Calculator.CalculatorActivity;
import multiplexaure.canvasapplication.Backgrounds.BackgroundSpheresView;
import multiplexaure.canvasapplication.Others.OthersActivity;
import multiplexaure.canvasapplication.Profile.ProfileActivity;
import multiplexaure.canvasapplication.R;
import multiplexaure.canvasapplication.Ranking.RankingActivity;


public class MainMenu extends Activity implements View.OnClickListener {

    private Button brickBreakerButton,audioButton,profileButton,calculatorButton,rankingButton,othersButton;
    BackgroundSpheresView backgroundSpheresView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        brickBreakerButton = (Button)findViewById(R.id.brickBreakerButton);
        audioButton = (Button)findViewById(R.id.audioButton);
        profileButton = (Button)findViewById(R.id.profileButton);
        calculatorButton = (Button)findViewById(R.id.calculatorButton);
        rankingButton = (Button)findViewById(R.id.rankingButton);
        othersButton = (Button)findViewById(R.id.othersButton);

        brickBreakerButton.setOnClickListener(this);
        audioButton.setOnClickListener(this);
        profileButton.setOnClickListener(this);
        calculatorButton.setOnClickListener(this);
        rankingButton.setOnClickListener(this);
        othersButton.setOnClickListener(this);

        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.frameLayoutMainMenu);
        backgroundSpheresView = new BackgroundSpheresView(this);
        backgroundSpheresView.setNColor(2);
        frameLayout.addView(backgroundSpheresView);

        final Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.enter_scene_right);
        final Animation animTranslateO = AnimationUtils.loadAnimation(this, R.anim.enter_scene_left);

        brickBreakerButton.startAnimation(animTranslate);
        rankingButton.startAnimation(animTranslateO);
        audioButton.startAnimation(animTranslate);
        profileButton.startAnimation(animTranslateO);
        calculatorButton.startAnimation(animTranslate);
        othersButton.startAnimation(animTranslateO);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.brickBreakerButton){
            Intent intent = new Intent(this, BrickBreakerActivity.class);
            startActivityForResult(intent, 0);
        }else if(v.getId() == R.id.audioButton){
            Intent intent = new Intent(this, AudioActivity.class);
            startActivityForResult(intent, 0);
        }else if(v.getId() == R.id.profileButton){
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivityForResult(intent, 0);
        }else if(v.getId() == R.id.calculatorButton){
            Intent intent = new Intent(this, CalculatorActivity.class);
            startActivityForResult(intent, 0);
        }else if(v.getId() == R.id.rankingButton){
            Intent intent = new Intent(this, RankingActivity.class);
            startActivityForResult(intent, 0);
        }else if(v.getId() == R.id.othersButton){
            Intent intent = new Intent(this, OthersActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==2){
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        backgroundSpheresView.createThread();
        backgroundSpheresView.touchBallCenterWithDelay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        backgroundSpheresView.stopThreads();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        new MyDataAccess(this).logout();
        this.finish();
    }
}
