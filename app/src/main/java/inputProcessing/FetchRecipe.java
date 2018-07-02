package inputProcessing;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import fragmentPage.VoiceRecipeFragment;
import voiceRecipe.MainActivity;


public class FetchRecipe extends AsyncTask<Void,Void,Void>{
    private int recipeID;
    private String urlContent = "";
    private ArrayList<String> recipe = new ArrayList<>();

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        recipeID = OptimalCuisine.optimalID;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            URL url = new URL("https://api.mlab.com/api/1/databases/recipe/collections/_" + recipeID + "?apiKey=mlAf4szqYK9PdnSO1i7qrJwGGttCL07O");
            HttpURLConnection connect = (HttpURLConnection) url.openConnection();

            InputStream is = connect.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf8"));

            String tmp;
            while((tmp = br.readLine()) != null){
                urlContent += tmp;
            }

            JSONArray ja = new JSONArray(urlContent);
            JSONObject jo = ja.getJSONObject(0);

            recipe.add(jo.getString("prepare"));

            int totalStep = jo.getInt("totalStep");
            int currentStep = 1;
            while (currentStep <= totalStep) {
                recipe.add(jo.getString("step" + currentStep));
                ++currentStep;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        VoiceRecipeFragment.recipe = this.recipe;
    }

}
