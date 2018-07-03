package fragmentPage;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.linyunchen.voicerecipe.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cuisineList.CuisineAdapter;
import cuisineList.CuisineItem;
import cuisineList.StringRequest_utf8;

public class CuisineFragment extends Fragment {

  //  private static final String urlData = "https://api.myjson.com/bins/74t7u";
    private static final String urlData = "https://api.myjson.com/bins/bihb0";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<CuisineItem> cuisineItems;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View cuisineView = inflater.inflate(R.layout.fragment_cuisine,container,false);
        recyclerView = (RecyclerView) cuisineView.findViewById(R.id.recycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        cuisineItems = new ArrayList<>();

        loadRecycleViewData();

        return cuisineView;
    }

    private void loadRecycleViewData(){
        final ProgressDialog pd = new ProgressDialog(this.getContext());
        pd.setMessage("正在加載中...");
        pd.show();

        StringRequest_utf8 sr = new StringRequest_utf8(Request.Method.GET,
                urlData,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        pd.dismiss();
                        try {
                            JSONArray array = new JSONArray(s);
                            for(int i = 0;i<array.length();++i){
                                JSONObject jo = array.getJSONObject(i);
                                CuisineItem item = new CuisineItem(
                                        jo.getString("name"),
                                        jo.getString("desc"),
                                        jo.getString("image"),
                                        jo.getString("url")
                                );
                                cuisineItems.add(item);
                            }

                            adapter = new CuisineAdapter(cuisineItems,getContext());
                            recyclerView.setAdapter(adapter);

                        }catch (JSONException e){
                            Log.i("fetchRecycleView","fail to load recycleview");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.dismiss();
                        Toast.makeText(getContext(),error.getMessage(),Toast.LENGTH_LONG);
                    }
                }
        );
        RequestQueue rq = Volley.newRequestQueue(this.getContext());
        rq.add(sr);
    }
}
