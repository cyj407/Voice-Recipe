package fragmentPage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.linyunchen.voicerecipe.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;

import inputProcessing.FetchRecipe;
import inputProcessing.OptimalCuisine;
import inputProcessing.WordSegmenter;
import voiceRecipe.MainActivity;
import voiceRecipe.Message;
import voiceRecipe.MessageAdapter;

public class VoiceRecipeFragment extends Fragment {

    public static ArrayList<String> cuisine,recipe;

    public static String timeLeftText = "00:01";

    private MessageAdapter messageAdapter;  // show the message
    private ListView listView;              // show the whole conversation

    private TextToSpeech textToSpeech;
    private ImageButton imageButton;        // microphone

    private CountDownTimer countDownTimer;

    private String localCuisineData;
    private boolean decideCuisine = false;
    private int countDownTime = 0;
    private int count = 0;

    public VoiceRecipeFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_cook,container,false);

        messageAdapter = new MessageAdapter(this.getActivity());
        listView = rootView.findViewById(R.id.listView);
        listView.setAdapter(messageAdapter);

        getAllCuisine();

        textToSpeech = new TextToSpeech(this.getActivity().getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    textToSpeech.setLanguage(Locale.CHINESE);
                    appSpeak("歡迎使用說一口好菜！請問你想要做什麼料理？",true);
                }
            }
        });

        imageButton = rootView.findViewById(R.id.imageButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view){
                if(MainActivity.microphoneOn) {
                    userSpeak();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        if(textToSpeech != null){
            textToSpeech.stop();
        }
        super.onDestroy();
    }

    public void getAllCuisine(){
        cuisine = new ArrayList<>();
        try{
            InputStream is = this.getActivity().getAssets().open("cuisine.json");
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

    // always let the latest message show on top
    public void newMessage(String text, boolean belongsToCurrentUser){
        messageAdapter.add(new Message(text, belongsToCurrentUser));
        listView.smoothScrollToPosition(listView.getCount() - 1);
    }

    public void appSpeak(final String text,boolean addToListView) {
        if(addToListView){
            newMessage(text, false);
        }
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if((requestCode == 1) && (data != null) && (resultCode == this.getActivity().RESULT_OK)){
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
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
            appSpeak("我們推薦你的料理是："+dishName+"\n你要做這道料理嗎？請回覆。",true);
            decideCuisine = true;
            return;
        }


        for(int i = 0; i < userSpeakSeg.size(); i++) {

            if(count == 0){
                if(userSpeakSeg.get(i).equals("好") || userSpeakSeg.get(i).equals("可以") || userSpeakSeg.get(i).equals("繼續") || userSpeakSeg.get(i).equals("需要") || userSpeakSeg.get(i).equals("準備")){
                    appSpeak(recipe.get(count++),true);
                    return;
                }
                else{
                    appSpeak("請輸入其他的料理。若輸入同樣的料理名，我們會推薦一樣的結果。",true);
                    decideCuisine = false;
                    return;
                }
            }

            if (recipe.get(count).equals("恭喜你完成了！")) {
                appSpeak(recipe.get(count)+"\n你還想要繼續做什麼料理嗎？請回覆想要做的料理。", true);
                recipe.clear();
                decideCuisine = false;
                count = 0;
                return;
            }

            if(userSpeakSeg.get(i).equalsIgnoreCase("ok") || userSpeakSeg.get(i).equals("然後") ||userSpeakSeg.get(i).equals("完成") || userSpeakSeg.get(i).equals("準備好") || userSpeakSeg.get(i).equals("做好") || userSpeakSeg.get(i).equals("好了") || userSpeakSeg.get(i).equals("下一步")) {
                if(count < recipe.size()) {
                    appSpeak(recipe.get(count),true);
                    // seg recipe
                    ArrayList<String> segStepContent = new WordSegmenter().segWord(recipe.get(count), "");
                    for(int j = 0;j<segStepContent.size();++j){
                        if(segStepContent.get(j).equals("分鐘")){

                            String text = "要幫你計時"+segStepContent.get(j-2)+"分鐘嗎？請選擇";
                            appSpeak(text,false);
                            countDownTime = Integer.valueOf(segStepContent.get(j-2));

                            AlertDialog.Builder ab = new AlertDialog.Builder(this.getContext());
                            ab.setMessage(text)
                                    .setCancelable(false)
                                    .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            startTimer();
                                            //     countdownStartListener.onCountdownStart(countDownTime);
                                        }
                                    })
                                    .setNegativeButton("不用", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            final AlertDialog ad = ab.create();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ad.show();
                                }
                            },8*1000);
                            break;
                        }
                    }
                    count++;
                    return;
                }
            }
        }
        // vocal result can't be recognized
        appSpeak("對不起，我沒有聽懂，請你再說一次。",true);
        return;
    }

    public void startTimer(){
        countDownTimer = new CountDownTimer(countDownTime*60*1000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                long timerLeftInMillisec = millisUntilFinished/1000;
                timeLeftText = String.format("%02d:%02d",(timerLeftInMillisec%3600)/60,(timerLeftInMillisec%60));
            }
            @Override
            public void onFinish() {
                Toast.makeText(getContext(),"倒數計時已結束",Toast.LENGTH_SHORT).show();
                appSpeak("倒數計時已結束、倒數計時已結束、倒數計時已結束。",false);
            }
        }.start();
    }
}
