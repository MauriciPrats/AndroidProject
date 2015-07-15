package multiplexaure.canvasapplication.Ranking;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import multiplexaure.canvasapplication.Backgrounds.BackgroundSpheresView;
import multiplexaure.canvasapplication.BBDD.MyDataAccess;
import multiplexaure.canvasapplication.BBDD.RankingUser;
import multiplexaure.canvasapplication.R;

public class RankingActivity extends Activity implements View.OnClickListener{

    MyDataAccess dataAccess;
    LinearLayout ranksLayoutName,ranksLayoutPoints,ranksLayoutTime;
    Button resetRankings;
    FrameLayout frameLayoutRanking;
    BackgroundSpheresView backgroundSpheresView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        dataAccess = new MyDataAccess(this);
        ranksLayoutName = (LinearLayout) findViewById(R.id.ranksLayoutName);
        ranksLayoutPoints = (LinearLayout) findViewById(R.id.ranksLayoutPoints);
        frameLayoutRanking = (FrameLayout) findViewById(R.id.frameLayoutRanking);
        backgroundSpheresView = new BackgroundSpheresView(this);
        frameLayoutRanking.addView(backgroundSpheresView);
        backgroundSpheresView.setNColor(3);

        //ranksLayoutTime = (LinearLayout) findViewById(R.id.ranksLayoutTime);
        resetRankings = (Button) findViewById(R.id.resetRankingButton);
        resetRankings.setOnClickListener(this);
        loadData();
    }

    void loadData(){
        List<RankingUser> rankings = dataAccess.getRankings();
        for(RankingUser rank : rankings){
            TextView viewName = new TextView(getApplicationContext());
            viewName.setText(rank.name);
            viewName.setTextSize(25f);
            viewName.setTextColor(Color.BLACK);

            TextView viewPoints = new TextView(getApplicationContext());
            viewPoints.setText(rank.points + "");
            viewPoints.setTextSize(25f);
            viewPoints.setTextColor(Color.BLACK);

            /*TextView viewTime = new TextView(getApplicationContext());
            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getApplicationContext());
            viewTime.setText(dateFormat.format(rank.time));
            viewTime.setTextSize(25f);
            viewTime.setTextColor(Color.BLACK);*/
            ranksLayoutName.addView(viewName);
            ranksLayoutPoints.addView(viewPoints);
            //ranksLayoutTime.addView(viewTime);
        }
    }

    void resetData(){
        ranksLayoutName.removeAllViews();
        ranksLayoutPoints.removeAllViews();
        //ranksLayoutTime.removeAllViews();
        loadData();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == resetRankings.getId()){
            dataAccess.deleteRankings();
            resetData();
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
