package multiplexaure.canvasapplication.Others;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

import multiplexaure.canvasapplication.Backgrounds.BackgroundSpheresView;
import multiplexaure.canvasapplication.R;

public class OthersActivity extends Activity implements View.OnClickListener {

    BackgroundSpheresView backgroundSpheresView;

    Button phoneButton,internetButton;

    ImageView internetImage;


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_others);

        phoneButton = (Button) findViewById(R.id.phoneButton);
        internetButton = (Button) findViewById(R.id.internetButton);

        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.frameLayoutOthers);
        backgroundSpheresView = new BackgroundSpheresView(this);
        backgroundSpheresView.setNColor(0);
        frameLayout.addView(backgroundSpheresView);

        final Animation animTranslate = AnimationUtils.loadAnimation(this, R.anim.enter_scene_right);
        final Animation animTranslateO = AnimationUtils.loadAnimation(this, R.anim.enter_scene_left);

        phoneButton.startAnimation(animTranslate);
        internetButton.startAnimation(animTranslateO);

        phoneButton.setOnClickListener(this);
        internetButton.setOnClickListener(this);
        internetImage = (ImageView) findViewById(R.id.internetImage);
        //System.out.println(LoadImageFromWebOperations("http://i.imgur.com/bIRGzVO.jpg").getBounds());
        LoadImageFromWebOperations("https://pbs.twimg.com/profile_images/2126943138/wololo-1.png");
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == internetButton.getId()){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.reddit.com/"));
            startActivity(intent);
        }else if(v.getId() == phoneButton.getId()){
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "112"));
            startActivity(intent);
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

    public void LoadImageFromWebOperations(String address) {
        try {
            URL url = new URL(address);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            internetImage.setImageBitmap(bmp);
            /*URL url = new URL(address);
            InputStream content = (InputStream)url.getContent();
            Drawable d = Drawable.createFromStream(content , "src");
            return d;*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
