package voiceRecipe;

import android.content.Intent;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.linyunchen.voicerecipe.R;

public class WelcomeActivity extends AppCompatActivity {

    private TextView textView;
    private ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        textView = (TextView) findViewById(R.id.textView2);
        textBlink();


        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            }
        });
    }

    private void textBlink(){
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(500);
                }
                catch (Exception e){
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(textView.getVisibility() == TextView.VISIBLE){
                            textView.setVisibility(TextView.INVISIBLE);
                        }
                        else{
                            textView.setVisibility(TextView.VISIBLE);
                        }
                        textBlink();
                    }
                });
            }
        }).start();
    }   // let the text "click any body the start" blink
}