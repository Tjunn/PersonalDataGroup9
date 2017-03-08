package group9.assignment2;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import com.annimon.stream.Stream;
import com.annimon.stream.function.Function;
import com.annimon.stream.function.Predicate;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ResultStorageManager {

    //private static final List<DummyItem> ITEMS = new ArrayList<DummyItem>();
    //private static final Map<String, DummyItem> ITEM_MAP = new HashMap<String, DummyItem>();

    private static final String FILENAME = "assignment_2_data.csv";
    private static List<ResultItem> items = null;



    public static void addResult(Context context, long result){

        ResultItem item = new ResultItem(result);
        if(items != null)
            items.add(item);

        FileOutputStream fos;
        try {
            fos = context.openFileOutput(FILENAME, Context.MODE_APPEND);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        OutputStreamWriter osw = new OutputStreamWriter(fos,Charset.forName("UTF-8"));
        BufferedWriter bw = new BufferedWriter(osw);

        try {
            bw.write(item.toString());
            bw.newLine();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static List<UsageStats> getUsageStats(Context context){

        // Check settings and ask for them not set
        boolean granted = false;
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), context.getPackageName());

        if (mode == AppOpsManager.MODE_DEFAULT) {
            granted = (context.checkCallingOrSelfPermission(android.Manifest.permission.PACKAGE_USAGE_STATS) == PackageManager.PERMISSION_GRANTED);
        } else {
            granted = (mode == AppOpsManager.MODE_ALLOWED);
        }

        if(!granted)
        {
            //TODO use dialog for ethics and blah blah blah...
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            context.startActivity(intent);
        }

        UsageStatsManager usageStatsManager=(UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);

        Calendar endCal = Calendar.getInstance();

        Calendar beginCal = Calendar.getInstance();
        beginCal.roll(Calendar.WEEK_OF_YEAR,-1);

        List<UsageStats> queryUsageStats=usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginCal.getTimeInMillis(), endCal.getTimeInMillis());

        queryUsageStats = Stream
                .of(queryUsageStats)
                .filter(new Predicate<UsageStats>() {
                    @Override
                    public boolean test(UsageStats value) {
                        return value.getTotalTimeInForeground()>0;
                    }})
                /*.sortBy(new Function<UsageStats, Comparable>() {

                    @Override
                    public Comparable apply(UsageStats usageStats) {
                        return usageStats.getTotalTimeInForeground();
                    }
                })*/
                .toList();

        /*queryUsageStats.removeIf(new Predicate<UsageStats>() {
            @Override
            public boolean test(UsageStats usageStats) {
                return false;
            }
        });*/


        return queryUsageStats;
    }

    public static List<ResultItem> getItems(Context context) {
        if(items == null){
            items = new ArrayList<>();
            FileInputStream fis;
            try {
                fis = context.openFileInput(FILENAME);
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
                return new ArrayList<>(0);
            }

            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
            BufferedReader br = new BufferedReader(isr);
            try {
                while (true){
                    String line = br.readLine();
                    if(line==null)
                        break;
                    else
                        items.add(new ResultItem(line));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Collections.reverse(items);
        return items;
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class ResultItem {

        final long time;
        final long result;

        public ResultItem(long result) {
            this.result = result;

            Calendar c = Calendar.getInstance();
            this.time = c.getTimeInMillis();
        }

        public ResultItem(long time, long result) {
            this.result = result;
            this.time = time;
        }

        ResultItem(String line) {
            String[] fields = line.split(",");
            time = Long.parseLong(fields[0]);
            result =  Long.parseLong(fields[1]);
        }

        @Override
        public String toString() {
            return time + "," + result ;
        }
    }
}
