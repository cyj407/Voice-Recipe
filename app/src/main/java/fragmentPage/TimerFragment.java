package fragmentPage;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.linyunchen.voicerecipe.R;

import java.util.Locale;

public class TimerFragment extends Fragment {

    private View timerView;
    private TextView countdownText;
    private CountDownTimer countDownTimer;


    private TextToSpeech tts;
    private long timerLeftInMillisec;
    private boolean timerRunning;
    private String temp;

    private static OnCountdownFinishListener countdownFinishListener;

    public TimerFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        timerView = inflater.inflate(R.layout.fragment_countdown,container,false);
        countdownText = timerView.findViewById(R.id.textView_countdown);

        tts = new TextToSpeech(this.getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    tts.setLanguage(Locale.CHINESE);
                }
            }
        });
        return timerView;
    }

    public interface OnCountdownFinishListener{
        public void onCountdownFinish(String text);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            countdownFinishListener = (OnCountdownFinishListener) activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString()+" must implement");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        countdownText = (TextView) timerView.findViewById(R.id.textView_countdown);
        if(!TextUtils.isEmpty(temp) && !this.isHidden()){
            countdownText.setText(temp);
        }
    }

    public void startStop(int init){
        if(timerRunning){
            stopTimer();
        }
        else{
        //    timerLeftInMillisec = 10000;
            timerLeftInMillisec = (long)init*1000*60;
            startTimer();
        }
    }

    public void setText(String text){
        if(countdownText == null){
            temp = text;
        }
        else {
            countdownText.setText(text);
            countdownText.setTextSize(50);
        }
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(timerLeftInMillisec,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                timerLeftInMillisec = millisUntilFinished/1000;
                String timeLeftText = String.format("%02d:%02d",(timerLeftInMillisec%3600)/60,(timerLeftInMillisec%60));
                temp = timeLeftText;
                if(countdownText != null){
                    countdownText.setTextSize(50);
                    countdownText.setText(temp);
                }
            }
            @Override
            public void onFinish() {
                /* return the finish message */
                temp = "目前沒有正在倒數的項目";
                if(countdownText != null){
                    countdownText.setText(temp);
                    countdownText.setTextSize(25);
                }
                countdownFinishListener.onCountdownFinish("倒數計時已結束、倒數計時已結束、倒數計時已結束");
            }
        }.start();
        timerRunning = true;
    }

    public void stopTimer(){
        countDownTimer.cancel();
        timerRunning = false;
    }

}
