package personal_data_interaction.group9.final_project;

import android.app.FragmentTransaction;
import android.app.Fragment;

import android.app.job.JobScheduler;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import group9.assignment2.R;

import java.util.Stack;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private static int jobID = 0xdd7f5d51;
    private Stack<Screen> backStack = new Stack<>();
    private Screen currentScreen;
    private enum Screen{
         USAGE_STATS//, TEST LIST,
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.hide();
            }
        });*/

        changeScreen(Screen.USAGE_STATS);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        startDataCollectionService();
    }

    private void startDataCollectionService(){

        Context context = getApplicationContext();
        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);

        //Check that job is running
        /*for(JobInfo jobInfo : scheduler.getAllPendingJobs()){
            if(jobInfo.getId() == jobID)
                return;
        }*/

        //Stop All
        scheduler.cancelAll();

        //Start new job
        //TODO Switch to https://developer.android.com/training/scheduling/alarms.html
        /*ComponentName serviceName = new ComponentName(context, GranularDataJobService.class);
        JobInfo info = new JobInfo.Builder(jobID,serviceName)
                .setPeriodic(1000*60*60) //1000 mils/s * 60s/min * 60min/h = once an hour
                .setPersisted(true)
                .build();

        scheduler.schedule(info);*/
    }

    private void changeScreen(Screen newScreen){
        if(currentScreen == newScreen)
            return;

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Fragment newFragment;

        if(currentScreen != null)
            backStack.push(currentScreen);

        switch (newScreen){
            /*case TEST:
                currentScreen = Screen.TEST;
                newFragment = new TestFragment();
                break;
            case LIST:
                currentScreen = Screen.LIST;
                newFragment = new ResultsFragment();
                break;*/
            case USAGE_STATS:
                currentScreen = Screen.USAGE_STATS;
                newFragment = new UsageStatsFragment();
                break;
            default:
                throw new IllegalArgumentException("Screen not recognized");
        }



        transaction.replace(R.id.activity_main_fragment, newFragment);
        transaction.commit();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else if (!backStack.empty()){
            changeScreen(backStack.pop());
        }


        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_results) {
            changeScreen(Screen.LIST);
        }
        else if (id == R.id.nav_usage_stats) {
            changeScreen(Screen.USAGE_STATS);
        }*/
        /* else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
