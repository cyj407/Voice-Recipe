package inputProcessing;

import android.util.Log;

import fragmentPage.VoiceRecipeFragment;
import voiceRecipe.MainActivity;

import java.util.ArrayList;

public class OptimalCuisine {

    private ArrayList<String> cuisine;
    public static int optimalID;

    public String findOptimalCuisine(String userInput){
        WordSegmenter ws = new WordSegmenter();
        int maxMatch = -1;

        cuisine = new ArrayList<>(VoiceRecipeFragment.cuisine);

        for(int i = 0;i<cuisine.size();++i){
            ArrayList<String> result = ws.segWord(cuisine.get(i),"");
            Log.i("CuisineName",cuisine.get(i));

            int match = 0;
            for(int j = 0;j<result.size();++j) {
                if(result.get(j).equals("")){
                    continue;
                }// skip the segmenter
                if(userInput.indexOf(result.get(j)) > -1){
                    ++match;
                }// check whether the input contains the segment
            }
        //    Log.i("CuisineName", cuisine.get(i) + ",match:" + match);
            if(match > maxMatch){
                maxMatch = match;
                optimalID = i+1;    // ID start from 1
            }
        }

        /* get the recipe of optimal cuisine */

        Log.i("CuisineName", String.valueOf(optimalID));
        return cuisine.get(optimalID-1);
    }

}
