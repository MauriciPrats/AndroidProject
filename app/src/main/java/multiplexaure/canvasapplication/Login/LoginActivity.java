package multiplexaure.canvasapplication.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import multiplexaure.canvasapplication.Backgrounds.BackgroundSpheresView;
import multiplexaure.canvasapplication.BBDD.MyDataAccess;
import multiplexaure.canvasapplication.BBDD.User;
import multiplexaure.canvasapplication.MainMenu.MainMenu;
import multiplexaure.canvasapplication.R;


public class LoginActivity extends Activity implements View.OnClickListener {

    Boolean finished = false;
    Handler mHandler = new Handler();
    Boolean showingLoginButton = true;
    MyDataAccess dataAccess = new MyDataAccess(this);
    BackgroundSpheresView backgroundView;
    FrameLayout frameLayoutBottom;
    BackgroundSpheresView backgroundSpheresView;
    LinearLayout layout1,layout2;

    EditText nameText,passwordText;
    Button loginButton,goToRegisterButton;
    private boolean hasShowedFirstAnimation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        nameText = (EditText)findViewById(R.id.editName);
        passwordText = (EditText)findViewById(R.id.editPassword);
        loginButton = (Button) findViewById(R.id.loginButton);
        goToRegisterButton = (Button) findViewById(R.id.goToRegisterButton);
        loginButton.setOnClickListener(this);
        goToRegisterButton.setOnClickListener(this);
        frameLayoutBottom =  (FrameLayout) findViewById(R.id.frameLayoutBottom);
        backgroundSpheresView = new BackgroundSpheresView(this);
        frameLayoutBottom.addView(backgroundSpheresView);
        layout1 = (LinearLayout) findViewById(R.id.linearLayoutName);
        layout2 = (LinearLayout) findViewById(R.id.linearLayoutPassword);
        backgroundSpheresView.setNColor(0);
        hideButtons();
        deactivateButtons();

        autoLogin();
    }

    void hideButtons(){
        layout1.setVisibility(View.INVISIBLE);
        layout2.setVisibility(View.INVISIBLE);
        loginButton.setVisibility(View.INVISIBLE);
        goToRegisterButton.setVisibility(View.INVISIBLE);
    }

    void deactivateButtons(){
        layout1.setEnabled(false);
        layout2.setEnabled(false);
        loginButton.setEnabled(false);
        goToRegisterButton.setEnabled(false);
    }

    void activateButtons(){
        layout1.setEnabled(true);
        layout2.setEnabled(true);
        loginButton.setEnabled(true);
        goToRegisterButton.setEnabled(true);
    }

    void showButtons(){
        layout1.setVisibility(View.VISIBLE);
        layout2.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.VISIBLE);
        goToRegisterButton.setVisibility(View.VISIBLE);
    }

    void animateEnterButtons(){
        if(!hasShowedFirstAnimation) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    activateButtons();
                    backgroundSpheresView.touchBallCenterWithDelay();
                }
            }, 2500);

            showButtons();
            backgroundSpheresView.setWelcomeText();
            final Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.enter_scene_right);
            animTranslate.setStartOffset(2500);
            layout1.startAnimation(animTranslate);
            layout2.startAnimation(animTranslate);
            loginButton.startAnimation(animTranslate);
            goToRegisterButton.startAnimation(animTranslate);
            hasShowedFirstAnimation = true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        backgroundSpheresView.createThread();
        animateEnterButtons();
    }

    @Override
    protected void onStop() {
        super.onStop();
        backgroundSpheresView.stopThreads();
    }

    void autoLogin(){
        User lastUser = dataAccess.getLastUserLogged();
        if(lastUser!=null){
            nameText.setText(lastUser.name);
            passwordText.setText(lastUser.password);
            if(dataAccess.checkIsUserCorrect(lastUser)){
                doLogin();
            }
        }
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.loginButton) {
            if(validatePasswordAndUser()) {
                Toast.makeText(this,"Login Succesful!",Toast.LENGTH_SHORT).show();
               doLogin();
            }else{
                Toast.makeText(this,"Wrong Login!",Toast.LENGTH_SHORT).show();
            }
        }else if(v.getId() == R.id.goToRegisterButton){
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        autoLogin();
    }

    private void doLogin(){
        dataAccess.setLastUserLogged(getActualUser());
        Intent intent = new Intent(this, MainMenu.class);
        startActivityForResult(intent, 0);
    }

    private User getActualUser(){
        User user = new User();
        user.name = nameText.getText().toString();
        user.password = passwordText.getText().toString();
        return user;
    }

    public Boolean validatePasswordAndUser(){
        return dataAccess.checkIsUserCorrect(getActualUser());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }
}
