package fragmentPage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.linyunchen.voicerecipe.R;

public class AboutFragment extends Fragment {

  //  private LinearLayout linearLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View aboutView = inflater.inflate(R.layout.fragment_about,container,false);
     //   linearLayout = (LinearLayout) aboutView.findViewById(R.id.fragment_about);

    //    linearLayout.setBackground(R.drawable.background_about);

        return aboutView;
    }
}
