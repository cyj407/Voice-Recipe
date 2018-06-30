package voiceRecipe;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.linyunchen.voicerecipe.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import inputProcessing.FetchRecipe;
import inputProcessing.OptimalCuisine;
import inputProcessing.WordSegmenter;

public class MainActivity extends AppCompatActivity {

    public static ArrayList<String> cuisine,recipe;

    private MessageAdapter messageAdapter;  // show the message
    private ListView listView;              // show the whole conversation
    private TextToSpeech textToSpeech;
    private ImageButton imageButton;        // microphone

    private String localCuisineData;
    private boolean decideCuisine = false;
    private int count = 0;

    public void getAllCuisine(){
        cuisine = new ArrayList<>();
        try{
            InputStream is = getAssets().open("cuisine.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            localCuisineData = new String(buffer);
            JSONArray localDataArray = new JSONArray(localCuisineData);

            for(int i = 0;i<localDataArray.length();++i){
                JSONObject obj = localDataArray.getJSONObject(i);
                cuisine.add(obj.getString("name"));
            }
        }catch (Exception e){   e.printStackTrace(); }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageAdapter = new MessageAdapter(this);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(messageAdapter);

        getAllCuisine();

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

    // always let the latest message show on top
    void newMessage(String text, boolean belongsToCurrentUser){
        messageAdapter.add(new Message(text, belongsToCurrentUser));
        listView.smoothScrollToPosition(listView.getCount() - 1);
    }

    public void appSpeak(final String text) {
        newMessage(text, false);
    //    textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);   // ?
        textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
    }

    public void userSpeak() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "請說話");
        try{
            startActivityForResult(intent, 1);
        }
        catch(ActivityNotFoundException e){}
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if((requestCode == 1) && (data != null) && (resultCode == RESULT_OK)){
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        //    Log.i("onActivityResult",result.get(0).toString());
            newMessage(result.get(0).toString(), true);     // true -> user talks
            appReply(result.get(0).toString());
        }
    }

    public void appReply(String message) {
        ArrayList<String> userSpeakSeg = new WordSegmenter().segWord(message, "");
        // get the vocal result after segmenting words

        if(!decideCuisine) {
            // send the message to the RecipeDatabase
            OptimalCuisine oc = new OptimalCuisine();
            String dishName = oc.findOptimalCuisine(message);
            FetchRecipe fetchRecipe = new FetchRecipe();
            fetchRecipe.execute();

            appSpeak("我們推薦你的料理是："+dishName+"\n你要做這道料理嗎？請回覆。");
            decideCuisine = true;
            return;
        }

        for(int i = 0; i < userSpeakSeg.size(); i++) {
            // "finish" message from user
            if(count == 0){
                if(userSpeakSeg.get(i).equals("好") || userSpeakSeg.get(i).equals("可以") || userSpeakSeg.get(i).equals("繼續") || userSpeakSeg.get(i).equals("需要") || userSpeakSeg.get(i).equals("準備")){
                    appSpeak(recipe.get(count++));
                    return;
                }
                else{
                    appSpeak("請輸入其他的料理。若輸入同樣的料理名，我們會推薦一樣的結果。");
                    decideCuisine = false;
                    return;
                }
            }

            if(userSpeakSeg.get(i).equals("完成") || userSpeakSeg.get(i).equals("準備好") || userSpeakSeg.get(i).equals("做好") || userSpeakSeg.get(i).equals("好了") || userSpeakSeg.get(i).equals("下一步")) {
                if(count < recipe.size()) {

                    appSpeak(recipe.get(count));

                    // seg recipe
                    ArrayList<String> segStepContent = new WordSegmenter().segWord(recipe.get(count), "");
                    for(int j = 0;j<segStepContent.size();++j){
                        if(segStepContent.get(j).equals("分鐘")){
                            appSpeak("要幫你計時"+segStepContent.get(j-2)+"分鐘嗎？請回覆。");
                            break;
                        }
                    }
                    count++;
                    return;
                }
            }
        }
        // vocal result can't be recognized
        appSpeak("對不起，我沒有聽懂，請你再說一次。");
        return;
    }
}