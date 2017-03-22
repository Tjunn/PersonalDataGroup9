package personal_data_interaction.group9.final_project;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Tjunn on 22/03/2017.
 */
public class GranularDataJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        // Note: this is preformed on the main thread.

        GranularDataAsyncTask updateTask = new GranularDataAsyncTask(getApplicationContext());
        updateTask.execute(params);

        return true;
    }

    // Stopping jobs if our job requires change.
    @Override
    public boolean onStopJob(JobParameters params) {
        // Note: return true to reschedule this job.

        return true;
    }

    private class GranularDataAsyncTask extends AsyncTask<JobParameters, Void, Void> {

        private Context context;

        GranularDataAsyncTask(Context context){
            this.context = context;
        }

        @Override
        protected Void doInBackground(JobParameters... params) {

            Calendar date = Calendar.getInstance();
            String text = "Running job for " + date.get(Calendar.HOUR_OF_DAY) + ":" + date.get(Calendar.MINUTE) + ":" + date.get(Calendar.SECOND);
            /*int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();*/

            Log.d("Job",text);

            JobParameters parameters = params[0];
            jobFinished(parameters,false);
            return null;
        }


    }
}