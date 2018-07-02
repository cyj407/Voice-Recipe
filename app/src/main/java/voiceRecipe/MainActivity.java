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
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.linyunchen.voicerecipe.R;

import fragmentPage.AboutFragment;
import fragmentPage.CuisineFragment;
import fragmentPage.VoiceRecipeFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    public android.support.v4.app.FragmentManager fragmentManager;

    public ImageView imageView;

    private Fragment curFragment;
    public static boolean microphoneOn = true;

    private CuisineFragment cuisineFragment;
    private AboutFragment aboutFragment;
    private VoiceRecipeFragment voiceRecipeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cuisineFragment = new CuisineFragment();
        aboutFragment = new AboutFragment();
        voiceRecipeFragment = new VoiceRecipeFragment();

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
            case R.id.nag_cuisine:
                microphoneOn = false;
                nextFragmentTag = "cuisine";
                if(getSupportFragmentManager().findFragmentByTag("cuisine") == null){
                    nextFragment = cuisineFragment;
                }
                else{
                    nextFragment = getSupportFragmentManager().findFragmentByTag("cuisine");
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
            case R.id.nag_countdown:
                if(!VoiceRecipeFragment.timeLeftText.equals("00:01"))
                    Toast.makeText(this,"還剩下"+VoiceRecipeFragment.timeLeftText,Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,"目前沒有正在倒數的項目",Toast.LENGTH_SHORT).show();
                return true;
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