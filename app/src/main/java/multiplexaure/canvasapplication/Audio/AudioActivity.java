package multiplexaure.canvasapplication.Audio;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import multiplexaure.canvasapplication.Backgrounds.BackgroundSpheresView;
import multiplexaure.canvasapplication.R;

public class AudioActivity extends Activity implements MediaPlayer.OnCompletionListener, View.OnClickListener {
    MediaPlayer mediaPlayer;

    ImageButton playButton,previousButton,nextButton;
    TextView songNameText;

    BackgroundSpheresView backgroundSpheresView;

    FrameLayout frameLayoutAudio;

    List<File> allSongs = new ArrayList();
    int actualSong = 0;

    Boolean areThereSongs = false;

    File currentSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        playButton = (ImageButton) findViewById(R.id.playButton);
        previousButton = (ImageButton) findViewById(R.id.previousButton);
        nextButton = (ImageButton) findViewById(R.id.nextButton);
        songNameText = (TextView) findViewById(R.id.songNameText);

        playButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        frameLayoutAudio = (FrameLayout) findViewById(R.id.frameLayoutAudio);
        backgroundSpheresView = new BackgroundSpheresView(this);
        frameLayoutAudio.addView(backgroundSpheresView);

        backgroundSpheresView.setNColor(4);
    }


    @Override
    protected void onStart() {
        super.onStart();
        backgroundSpheresView.createThread();
        backgroundSpheresView.touchBallCenterWithDelay();
        mediaPlayer = new MediaPlayer();

        File sdCard = Environment.getExternalStorageDirectory();
        File musicFolder = new File(sdCard.getAbsolutePath() + "/Music");
        if (musicFolder.listFiles(new FileExtensionFilter()).length > 0)
        {
            areThereSongs = true;
            for (File file : musicFolder.listFiles(new FileExtensionFilter()))
            {
                allSongs.add(file);
            }
        }
        if(areThereSongs) {
            if(allSongs.size()>actualSong) {
                currentSong = allSongs.get(actualSong);
            }

            if(currentSong!=null) {
                songNameText.setText(currentSong.getName());
            }
            try {
                mediaPlayer.setDataSource(currentSong.getAbsolutePath());
                mediaPlayer.prepare();
                mediaPlayer.setOnCompletionListener(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            songNameText.setText("No songs in /Music folder");
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        //mediaPlayer.release();
        //checkPauseButtonState();
        playNext();
    }

    @Override
    protected void onStop() {
        super.onStop();
        backgroundSpheresView.stopThreads();
        mediaPlayer.release();
    }

    @Override
    public void onClick(View v) {
        if(areThereSongs) {
            if (v.getId() == playButton.getId()) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                }
            } else if (v.getId() == nextButton.getId()) {
                playNext();
            } else if (v.getId() == previousButton.getId()) {
                playPrevious();
            }
            checkPauseButtonState();
        }
    }

    void checkPauseButtonState(){
        if(mediaPlayer.isPlaying()){
            playButton.setImageResource(R.drawable.pause);
        }else{
            playButton.setImageResource(R.drawable.play);
        }
        if(currentSong!=null) {
            songNameText.setText(currentSong.getName());
        }
    }

    void playNext(){
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        actualSong = (actualSong+1)%allSongs.size();
        currentSong = allSongs.get(actualSong);
        try{
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(currentSong.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.start();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        checkPauseButtonState();
    }



    void playPrevious(){
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        mediaPlayer.release();
        if(actualSong == 0){ actualSong = allSongs.size()-1;}
        else {
            actualSong = (actualSong - 1);
        }
        currentSong = allSongs.get(actualSong);
        try{
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(currentSong.getAbsolutePath());
            mediaPlayer.prepare();
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.start();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        checkPauseButtonState();
    }

    class FileExtensionFilter implements FilenameFilter
    {
        public boolean accept(File dir, String name)
        {
            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
    }
}
