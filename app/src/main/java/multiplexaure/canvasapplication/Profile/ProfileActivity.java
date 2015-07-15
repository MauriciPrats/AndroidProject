package multiplexaure.canvasapplication.Profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import multiplexaure.canvasapplication.Backgrounds.BackgroundSpheresView;
import multiplexaure.canvasapplication.BBDD.MyDataAccess;
import multiplexaure.canvasapplication.BBDD.User;
import multiplexaure.canvasapplication.R;

public class ProfileActivity extends Activity implements View.OnClickListener,LocationListener {

    BackgroundSpheresView backgroundSpheresView;
    FrameLayout frameLayoutProfile;
    Button saveChangesButton,logoutButton;

    EditText password,color;
    TextView localization,username,bestScore;

    MyDataAccess dataAccess;
    LocationManager locationManager;

    Button avatarButton;
    ImageView avatarView;
    ScrollView scrollView;

    byte[] image;

    LinearLayout usernameLayout,passwordLayout,colorLayout,localizationLayout,buttonsLayout,bestScoreLayout;

    private static final int SELECT_PICTURE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_profile);

        frameLayoutProfile =  (FrameLayout) findViewById(R.id.frameLayoutProfile);
        backgroundSpheresView = new BackgroundSpheresView(this);
        frameLayoutProfile.addView(backgroundSpheresView);

        saveChangesButton = (Button) findViewById(R.id.saveChangesButton);
        logoutButton = (Button) findViewById(R.id.logoutButton);

        logoutButton.setOnClickListener(this);
        saveChangesButton.setOnClickListener(this);

        username = (TextView) findViewById(R.id.usernameProfile);
        password = (EditText) findViewById(R.id.passwordProfile);
        color = (EditText) findViewById(R.id.colorProfile);
        localization = (TextView) findViewById(R.id.localizationProfile);
        avatarButton = (Button) findViewById(R.id.chooseAvatar);
        avatarView = (ImageView) findViewById(R.id.avatarView);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        bestScore = (TextView) findViewById(R.id.bestScore);


        avatarButton.setOnClickListener(this);
        dataAccess = new MyDataAccess(this);


        User actualUser = dataAccess.getLastUserLoggedComplete();

        if(actualUser==null){
            System.out.println("WTF BBQ");
        }else{
            username.setText(actualUser.name);
            password.setText(actualUser.password);
            color.setText(actualUser.color);
            if(actualUser.avatar!=null){
                image = actualUser.avatar;
                loadDrawable();
            }
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);

        usernameLayout = (LinearLayout) findViewById(R.id.usernameLayout);
        passwordLayout = (LinearLayout) findViewById(R.id.passwordLayout);
        colorLayout = (LinearLayout) findViewById(R.id.colorLayout);
        localizationLayout = (LinearLayout) findViewById(R.id.localizationLayout);
        buttonsLayout = (LinearLayout) findViewById(R.id.buttonsLayout);
        bestScoreLayout = (LinearLayout) findViewById(R.id.bestScoreLayout);

        usernameLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_scene_left));
        passwordLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_scene_right));
        colorLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_scene_left));
        localizationLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_scene_right));
        buttonsLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_scene_bottom));
        avatarButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_scene_top));
        avatarView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_scene_top));
        bestScoreLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter_scene_bottom));
        backgroundSpheresView.setNColor(5);
    }

    private void loadDrawable(){
        if(image!=null){
            Bitmap b = getImage(image);
            avatarView.setImageBitmap(b);
        }
       /* if(textUri!=null && textUri.length()>0){
            try {
                System.out.println(textUri);
                Uri selectedImageUri = Uri.parse(textUri);
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                Drawable drawable = Drawable.createFromStream(inputStream, selectedImageUri.toString() );
                drawable.
                avatarView.setImageDrawable(drawable);
                scrollView.recomputeViewAttributes(avatarView);
            } catch (FileNotFoundException e) {
                Toast.makeText(this,"Error loading image!",Toast.LENGTH_SHORT).show();
            }
        }*/
    }

    public Bitmap getImage(byte[] imgByte){
        return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    image = getBitmapAsByteArray(bitmap);

                }catch (Exception e){
                    Toast.makeText(this,"Error loading image!",Toast.LENGTH_SHORT).show();
                }
                loadDrawable();

            }
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.logoutButton){
            setResult(2);
            dataAccess.logout();
            finish();
        }else if(v.getId() == R.id.saveChangesButton){
            if(password.getText().length()==0){
                Toast.makeText(this, "Password Missing", Toast.LENGTH_SHORT).show();
            }else if(color.getText().length()==0){
                Toast.makeText(this, "Favourite Color Missing!", Toast.LENGTH_SHORT).show();
            }else{
                User user = new User();
                user.name = username.getText().toString();
                user.password = password.getText().toString();
                user.color = color.getText().toString();
                if(image!=null){
                    user.avatar = image;
                }
                dataAccess.updateUser(user);
                dataAccess.setLastUserLogged(user);
                Toast.makeText(this, "Changes Updated!", Toast.LENGTH_SHORT).show();
            }
        }else if(v.getId() == avatarButton.getId()){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_PICTURE);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
            if (addresses.size() > 0){
                String text = addresses.get(0).getAddressLine(0)+" "+addresses.get(0).getAddressLine(1);
                localization.setText(text);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        backgroundSpheresView.createThread();
        backgroundSpheresView.touchBallCenterWithDelay();
        bestScore.setText(dataAccess.getBestPoints(dataAccess.getLastUserLogged())+"");
    }

    @Override
    protected void onStop() {
        super.onStop();
        backgroundSpheresView.stopThreads();

    }
}
