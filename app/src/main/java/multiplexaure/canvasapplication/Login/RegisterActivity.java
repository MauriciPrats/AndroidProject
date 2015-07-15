package multiplexaure.canvasapplication.Login;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import multiplexaure.canvasapplication.Backgrounds.BackgroundSpheresView;
import multiplexaure.canvasapplication.BBDD.MyDataAccess;
import multiplexaure.canvasapplication.BBDD.User;
import multiplexaure.canvasapplication.R;

public class RegisterActivity extends Activity implements View.OnClickListener {

    private EditText nameText,passwordText,colorText;
    private Button registerButton;
    BackgroundSpheresView backgroundSpheresView;
    LinearLayout usernameLayout,passwordLayout,colorLayout;

    MyDataAccess dataAccess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nameText = (EditText) findViewById(R.id.registerUser);
        passwordText = (EditText) findViewById(R.id.registerPassword);
        colorText = (EditText) findViewById(R.id.registerColor);
        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(this);
        dataAccess = new MyDataAccess(this);
        FrameLayout frameLayoutRegister = (FrameLayout) findViewById(R.id.frameLayoutRegister);
        backgroundSpheresView = new BackgroundSpheresView(this);
        frameLayoutRegister.addView(backgroundSpheresView);


        usernameLayout = (LinearLayout) findViewById(R.id.usernameLayout);
        passwordLayout = (LinearLayout) findViewById(R.id.passwordLayout);
        colorLayout = (LinearLayout) findViewById(R.id.colorLayout);

        usernameLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_scene_top));
        passwordLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_scene_right));
        colorLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_scene_left));
        registerButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_scene_bottom));

        backgroundSpheresView.setNColor(1);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.registerButton){
            if(nameText.getText().length()==0){
                Toast.makeText(this, "Name Missing", Toast.LENGTH_SHORT).show();
            }else if(passwordText.getText().length()==0){
                Toast.makeText(this, "Password Missing", Toast.LENGTH_SHORT).show();
            }else if(colorText.getText().length()==0){
                Toast.makeText(this, "Favourite Color Missing!", Toast.LENGTH_SHORT).show();
            }else{
                User user = new User();
                user.name = nameText.getText().toString();
                user.password = passwordText.getText().toString();
                user.color = colorText.getText().toString();
                if(!dataAccess.insertNewUser(user)){
                    Toast.makeText(this, "Meeeec!! It already exists!", Toast.LENGTH_SHORT).show();
                }else{
                    dataAccess.setLastUserLogged(user);
                    Toast.makeText(this, "Registrado Correctamente!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
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
}
