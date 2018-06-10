package com.example.linyunchen.voicerecipe;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private MessageAdapter messageAdapter;
    private ListView listView;
    private TextToSpeech textToSpeech;
    private ImageButton imageButton;

    //
    private String[] recipe = { "需要準備的材料有：雞腿肉1斤、香菇15朵、薑3片、蔥1支、米酒少許、鹽適量。完成後請回覆。",
                                "第一步，川燙雞腿肉，沖洗後備用。完成後請回覆。",
                                "第二步，香菇泡水，薑切片，蔥切段。完成後請回覆。",
                                "第三步，準備一鍋1200毫升的水，放入雞腿肉，加點米酒。完成後請回覆。",
                                "第四步，蓋鍋蓋，大火滾煮後轉小火，燉煮30分鐘。完成後請回覆。",
                                "第五步，加入薑片、香菇及泡香菇的水，蓋鍋蓋，轉大火滾煮後再轉小火續煮20分鐘。完成後請回覆。",
                                "第六步，加入蔥段後，再小火煮10分鐘。完成後請回覆。",
                                "第七步，火，加入適當鹽巴調味。完成後請回覆。",
                                "恭喜你完成了！" };
    private int count = 0;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageAdapter = new MessageAdapter(this);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(messageAdapter);

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    textToSpeech.setLanguage(Locale.CHINESE);
                    appSpeak("歡迎使用說一口好菜！請問你想要做什麼料理？");
                }
            }
        });

        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSpeak();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    void newMessage(String text, boolean belongsToCurrentUser){
        messageAdapter.add(new Message(text, belongsToCurrentUser));
        listView.smoothScrollToPosition(listView.getCount() - 1);
    }

    public void appSpeak(final String text) {
        newMessage(text, false);
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void userSpeak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請說話");
        try{
            startActivityForResult(intent, 1);
        }
        catch(ActivityNotFoundException e){
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if((requestCode == 1) && (data != null) && (resultCode == RESULT_OK)){
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            newMessage(result.get(0).toString(), true);
            appReply(result.get(0).toString());
        }
    }

    //
    public void appReply(String message) {
        ArrayList<String> seg = new WordSegmenter().segWord(message, "");
        for(int i = 0; i < seg.size(); i++) {
            if(seg.get(i).equals("香菇")) {
                if(count < recipe.length) {
                    appSpeak(recipe[count]);
                    count++;
                    return;
                }
            }
            else if(seg.get(i).equals("完成") || seg.get(i).equals("準備好") || seg.get(i).equals("做好") || seg.get(i).equals("好了") || seg.get(i).equals("下一步")) {
                if(count < recipe.length) {
                    appSpeak(recipe[count]);
                    count++;
                    return;
                }
            }
        }
        appSpeak("對不起，我沒有聽懂，請你再說一次。");
        return;
    }
    //
}