package personal_data_interaction.group9.final_project;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import group9.assignment2.R;

public class CraigDavid extends AppCompatActivity {

    private Screen currentScreen;


    private void changeScreen(CraigDavid.Screen newScreen){
        if(currentScreen == newScreen)
            return;

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment newFragment;

        //if(currentScreen != null)
            //backStack.push(currentScreen);

        switch (newScreen){
            case DAVID:
                currentScreen = Screen.DAVID;
                newFragment = TheRealCraigDavid.newInstance("lol-+","dd");
                break;
            case USAGE_STATS:
                currentScreen = CraigDavid.Screen.USAGE_STATS;
                newFragment = new UsageStatsFragment();
                break;
            default:
                throw new IllegalArgumentException("Screen not recognized");
        }



        transaction.replace(R.id.activity_craig_david_content, newFragment);
        transaction.commit();


    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    changeScreen(Screen.USAGE_STATS);
                    return true;
                case R.id.navigation_dashboard:
                    changeScreen(Screen.DAVID);
                    return true;
                /*
                case R.id.navigation_notifications:
                    //changeScreen(Screen.USAGE_STATS);
                    return true;
                    */
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_craig_david);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        changeScreen(Screen.USAGE_STATS);
    }

    public enum Screen {DAVID, USAGE_STATS}
}
