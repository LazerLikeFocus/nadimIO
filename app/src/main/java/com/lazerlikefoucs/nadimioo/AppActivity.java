package com.lazerlikefoucs.nadimioo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.ArrayList;

public class AppActivity extends AppCompatActivity implements View.OnClickListener {

    private Button tutorial, type, options, ask;
    private TextView textView, textView_AI;
    private LottieAnimationView lottieAnimationView;
    private static final int RECOGNIZER_RESULT = 1;
    private long backPressedTime;
    private String stringToPython = "abuse abused abuses accurately achievable achievement achievements achievible acumen adaptable adaptive adequate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        tutorial = (Button) findViewById(R.id.button_tutorial);
        type = (Button) findViewById(R.id.button_type);
        ask = (Button) findViewById(R.id.button_ask);
        textView = (TextView) findViewById(R.id.textView);
        textView_AI = (TextView) findViewById(R.id.textView_AI);
        options = (Button)  findViewById(R.id.button_options);
        lottieAnimationView = (LottieAnimationView) findViewById(R.id.lottie_anim);

        tutorial.setOnClickListener(this);
        type.setOnClickListener(this);
        ask.setOnClickListener(this);
        textView.setOnClickListener(this);
        options.setOnClickListener(this);

        pythonInitialize();
    }


    @Override
    public void onClick(View view) {
        if (view == tutorial){ }
        if (view == options){ Intent intent = new Intent(AppActivity.this, OptionsActivity.class);  startActivity(intent); }
        if (view == ask){ askMethod(); }
        if (view == type){ typeMethod();}
        if (view == textView){ textMethod();}
    }


    //python
    private void pythonInitialize() {
        if (!Python.isStarted()){
            Python.start(new AndroidPlatform(this));
        }
        Python py = Python.getInstance();
        final PyObject pyObj  = py.getModule("nadim");
        PyObject obj = pyObj.callAttr("find",stringToPython);
        stringToPython = obj.toString();

        //testing
        //Log.d("the probability is: ",stringToPython);
        //textView_AI.setText(stringToPython);
    }


    //Press twice to exit
    @Override
    public void onBackPressed() {

        if (backPressedTime + 1500 > System.currentTimeMillis()){
            super.onBackPressed();
            finishAffinity(); //clears all the activities and closes the app
            return;
        }else {
            makeToastExit();
        }

        backPressedTime = System.currentTimeMillis();
    }

    //toast
    private void makeToastExit() {

        LayoutInflater inflater = getLayoutInflater();
        View layout  = inflater.inflate(R.layout.toast_exit, (ViewGroup) findViewById(R.id.layout_toast_exit));

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM ,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }


    // loading... ANIMATION
    private void startLoadingAnimation() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        //create animator
        final ValueAnimator animator = ValueAnimator.ofFloat(0f,1f).setDuration(1500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                lottieAnimationView.setProgress((Float)animation.getAnimatedValue());
            }
        });
        //loop the animation
        if (lottieAnimationView.getProgress() == 0f){ animator.setStartDelay(0); animator.start();}
        else {lottieAnimationView.setProgress(0f); animator.start();}
    }


    /* TEXT */
    private void typeMethod() { }
    private void textMethod() {
        textView.setCursorVisible(true);
        textView.setFocusableInTouchMode(true);
        textView.setInputType(InputType.TYPE_CLASS_TEXT);
        textView.requestFocus(); //to trigger the soft input

        textView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    textView.setCursorVisible(false);

                    //trigger animation of working
                    //display P(getting yes)
                }
                return false;
            }
        });
    }


    /*SPEECH recognition*/
    private void askMethod() {
        textView.setText("");
        Intent speechintent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechintent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechintent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Ask nad.im");
        startActivityForResult(speechintent,RECOGNIZER_RESULT);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == RECOGNIZER_RESULT && resultCode == RESULT_OK){
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            textView.setText(matches.get(0).toString());
            stringToPython = matches.get(0).toString();

            //startLoadingAnimation();
            pythonInitialize();

            //wait for 2 secs.
            textView_AI.setText(stringToPython);
        }else {
            textView.setText("Any problem?");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}