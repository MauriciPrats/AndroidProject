package multiplexaure.canvasapplication.Calculator;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import multiplexaure.canvasapplication.Backgrounds.BackgroundSpheresView;
import multiplexaure.canvasapplication.R;

public class CalculatorActivity extends Activity implements View.OnClickListener{

    BackgroundSpheresView backgroundSpheresView;
    FrameLayout frameLayoutCalculator;

    Button button1,button2,button3,button4,button5,button6,button7,button8,button9,button0,buttonPoint,buttonDiv,buttonMul,buttonPlus,buttonMinus,buttonEquals,buttonC,buttonMC;
    TextView resultText;
    private String actualNumber = "";
    private float accumulatedNumber = 0f;
    private String lastOperation = "";
    private Boolean isShowingAccumulated = false;

    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        frameLayoutCalculator =  (FrameLayout) findViewById(R.id.frameLayoutCalculator);
        backgroundSpheresView = new BackgroundSpheresView(this);
        frameLayoutCalculator.addView(backgroundSpheresView);

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);
        button9 = (Button) findViewById(R.id.button9);
        button0 = (Button) findViewById(R.id.button0);
        buttonPoint = (Button) findViewById(R.id.buttonPoint);
        buttonDiv = (Button) findViewById(R.id.buttonDiv);
        buttonMul = (Button) findViewById(R.id.buttonMul);
        buttonPlus = (Button) findViewById(R.id.buttonPlus);
        buttonMinus = (Button) findViewById(R.id.buttonMinus);
        buttonEquals = (Button) findViewById(R.id.buttonEquals);
        buttonC= (Button) findViewById(R.id.buttonC);
        buttonMC= (Button) findViewById(R.id.buttonMC);

        resultText =(TextView) findViewById(R.id.resultText);


        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
        button8.setOnClickListener(this);
        button9.setOnClickListener(this);
        button0.setOnClickListener(this);
        buttonPoint.setOnClickListener(this);
        buttonDiv.setOnClickListener(this);
        buttonPlus.setOnClickListener(this);
        buttonMul.setOnClickListener(this);
        buttonMinus.setOnClickListener(this);
        buttonEquals.setOnClickListener(this);
        buttonC.setOnClickListener(this);
        buttonMC.setOnClickListener(this);

        backgroundSpheresView.setNColor(6);
        loadData();
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
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("actualValue");
        editor.remove("lastOperation");
        editor.remove("accumulatedNumber");
        editor.remove("isShowingAccumulated");
        editor.commit();
    }

    private void saveData(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("actualValue", actualNumber);
        editor.putString("lastOperation", lastOperation);
        editor.putFloat("accumulatedNumber", accumulatedNumber);
        editor.putBoolean("isShowingAccumulated", isShowingAccumulated);
        editor.commit();
    }

    private void loadData(){
        actualNumber = sharedPref.getString("actualValue","");
        accumulatedNumber = sharedPref.getFloat("accumulatedNumber",0f);
        isShowingAccumulated = sharedPref.getBoolean("isShowingAccumulated",false);
        lastOperation = sharedPref.getString("lastOperation","");
        if(isShowingAccumulated){
            showAccumulatedNumber();
        }else{
            showActualNumber();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button0){
            addNumber(0);
        }else if(v.getId() == R.id.button1){
            addNumber(1);
        }else if(v.getId() == R.id.button2){
            addNumber(2);
        }else if(v.getId() == R.id.button3){
            addNumber(3);
        }else if(v.getId() == R.id.button4){
            addNumber(4);
        }else if(v.getId() == R.id.button5){
            addNumber(5);
        }else if(v.getId() == R.id.button6){
            addNumber(6);
        }else if(v.getId() == R.id.button7){
            addNumber(7);
        }else if(v.getId() == R.id.button8){
            addNumber(8);
        }else if(v.getId() == R.id.button9){
            addNumber(9);
        }else if(v.getId() == R.id.buttonPoint){
            setPoint();
        }else if(v.getId() == R.id.buttonMul){
            operation("Mul");
        }else if(v.getId() == R.id.buttonMinus){
            operation("Minus");
        }else if(v.getId() == R.id.buttonPlus){
            operation("Plus");
        }else if(v.getId() == R.id.buttonDiv){
            operation("Div");
        }else if(v.getId() == R.id.buttonEquals){
            operation("Equals");
        }else if(v.getId() == R.id.buttonC){
            cleanNumber();
        }else if(v.getId() == R.id.buttonMC){
            cleanMemory();
        }
        saveData();
    }

    private void cleanNumber(){
        actualNumber = "";
        showActualNumber();
    }

    private void cleanMemory(){
        actualNumber = "";
        accumulatedNumber = 0f;
        lastOperation = "";
        showActualNumber();
    }

    private void addNumber(int number){
        if(lastOperation.equals("Equals")){
            accumulatedNumber = 0f;
            lastOperation="";
        }
        if(actualNumber.length()<12) {
            actualNumber += number;
        }
        showActualNumber();
    }

    private void setPoint(){
        if(lastOperation.equals("Equals")){
            accumulatedNumber = 0f;
            lastOperation = "";
        }
        if(!actualNumber.contains(".") && actualNumber.length()<12) {
            actualNumber += ".";
        }
        showActualNumber();
    }

    private void operation(String op){

        Boolean operationError = false;
        if(actualNumber.length()>0 && !lastOperation.equals("Equals")) {
            float newNumber = Float.parseFloat(actualNumber);

            if(op.equals("Equals") && !lastOperation.equals("")){
                if (lastOperation.equals("Mul")) {
                    accumulatedNumber *= newNumber;
                } else if (lastOperation.equals("Div")) {
                    if (newNumber != 0f) {
                        accumulatedNumber /= newNumber;
                    }else{
                        operationError = true;
                        resultText.setText("Error!");
                        lastOperation = "";
                        accumulatedNumber = 0f;
                        actualNumber = "";
                    }
                } else if (lastOperation.equals("Plus")) {
                    accumulatedNumber += newNumber;
                } else if (lastOperation.equals("Minus")) {
                    accumulatedNumber -= newNumber;
                }
            }else if(lastOperation.equals("")){
                accumulatedNumber = newNumber;
            }else{
                if (lastOperation.equals("Mul")) {
                    accumulatedNumber *= newNumber;
                } else if (lastOperation.equals("Div")) {
                    if (newNumber != 0f) {
                        accumulatedNumber /= newNumber;
                    }
                } else if (lastOperation.equals("Plus")) {
                    accumulatedNumber += newNumber;
                } else if (lastOperation.equals("Minus")) {
                    accumulatedNumber -= newNumber;
                }
            }

            if(!operationError) {
                cleanNumber();
                showAccumulatedNumber();
            }
        }
        if(!operationError) {
            lastOperation = op;
        }


    }
    private void showAccumulatedNumber(){
        resultText.setText(""+accumulatedNumber); isShowingAccumulated = true;
    }
    private void showActualNumber(){
        resultText.setText(actualNumber); isShowingAccumulated = false;
    }


}
