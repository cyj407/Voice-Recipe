package voiceRecipe;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.MenuItem;

import com.example.linyunchen.voicerecipe.R;

import java.util.ArrayList;
import java.util.List;

import fragmentPage.AboutFragment;
import fragmentPage.SettingFragment;
import fragmentPage.TimerFragment;
import fragmentPage.VoiceRecipeFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    public android.support.v4.app.FragmentManager fragmentManager;

    private Fragment curFragment;
    public static boolean microphoneOn = true;

    private TimerFragment timerFragment = new TimerFragment();
    private SettingFragment settingFragment = new SettingFragment();
    private AboutFragment aboutFragment = new AboutFragment();
    private VoiceRecipeFragment voiceRecipeFragment = new VoiceRecipeFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        if(findViewById(R.id.fragment_container)!=null){
            if(savedInstanceState != null){
                return;
            }
            // set initial fragment to the voice recipe
            android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container,voiceRecipeFragment,"voiceRecipe").commit();
            onAttachFragment(voiceRecipeFragment);
        }

        curFragment = getSupportFragmentManager().findFragmentByTag("voiceRecipe");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        Fragment nextFragment = null;
        String nextFragmentTag = "";
        switch (item.getItemId()){
            case R.id.nag_chat:
                microphoneOn = true;
                nextFragmentTag = "voiceRecipe";
                nextFragment = getSupportFragmentManager().findFragmentByTag("voiceRecipe");
                break;
            case R.id.nag_countdown:
                microphoneOn = false;
                nextFragmentTag = "timer";
                if(getSupportFragmentManager().findFragmentByTag("timer") == null){
                    nextFragment = timerFragment;
                }
                else{
                    nextFragment = getSupportFragmentManager().findFragmentByTag("timer");
                }
                break;
            case R.id.nag_setting:
                microphoneOn = false;
                nextFragmentTag = "setting";
                if(getSupportFragmentManager().findFragmentByTag("setting") == null){
                    nextFragment = settingFragment;
                }
                else{
                    nextFragment = getSupportFragmentManager().findFragmentByTag("setting");
                }
                break;
            case R.id.nag_about:
                microphoneOn = false;
                nextFragmentTag = "about";
                if(getSupportFragmentManager().findFragmentByTag("about") == null){
                    nextFragment = aboutFragment;
                }
                else{
                    nextFragment = getSupportFragmentManager().findFragmentByTag("about");
                }
                break;
        }
        if(curFragment != nextFragment){
            for(int i = 0;i< fm.getFragments().size();++i){
                Fragment tmp = fm.getFragments().get(i);
                if(!fm.getFragments().get(i).getTag().equals(nextFragmentTag)) {
                    fragmentTransaction.hide(tmp);
                }
            }
            if(!nextFragment.isAdded()){
                fragmentTransaction.add(R.id.fragment_container,nextFragment,nextFragmentTag).commit();
            }
            else{
                fragmentTransaction.show(nextFragment).commit();
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

}