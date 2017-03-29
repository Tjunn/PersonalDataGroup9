package personal_data_interaction.group9.final_project;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;

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
public class DataManager {


    private static void checkForUsageStatsPermission(Context context){
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
    }

    static List<UsageStats> getLastWeekUsageStats(final Context context){

        Calendar beginCal = Calendar.getInstance();
        beginCal.roll(Calendar.WEEK_OF_YEAR,-1);

        Calendar endCal = Calendar.getInstance();

        return getUsageStats(context,beginCal,endCal);
    }

    static List<UsageStats> getDayUsageStats(final Context context){

        Calendar beginCal = Calendar.getInstance();
        beginCal.roll(Calendar.DAY_OF_YEAR,-1);
        /*beginCal.set(Calendar.HOUR_OF_DAY,12);
        beginCal.set(Calendar.MINUTE,0);
        beginCal.set(Calendar.SECOND,0);
        beginCal.set(Calendar.MILLISECOND,0)*/;

        Calendar endCal = Calendar.getInstance();

        /*return Stream.of(getUsageStats(context,beginCal,endCal)).filter(new Predicate<UsageStats>() {
            @Override
            public boolean test(UsageStats value) {
                Calendar start = Calendar.getInstance();
                start.setTimeInMillis(value.getFirstTimeStamp());
                Calendar end = Calendar.getInstance();
                end.setTimeInMillis(value.getLastTimeStamp());
                return start.get(Calendar.DAY_OF_YEAR) == end.get(Calendar.DAY_OF_YEAR) && start.get(Calendar.YEAR) == end.get(Calendar.YEAR);
            }
        }).toList();*/
        return getUsageStats(context,beginCal,endCal);
    }

    static List<UsageStatsItem> toUsageStatsItem( List<UsageStats> usageStats, Context context){
        final PackageManager maneger = context.getPackageManager();
        return Stream
                .of(usageStats)
                .filter(new Predicate<UsageStats>() {
                    @Override
                    public boolean test(UsageStats value) {
                        return value.getTotalTimeInForeground()>0;
                    }})
                .map(new Function<UsageStats, UsageStatsItem>() {
                    @Override
                    public UsageStatsItem apply(UsageStats usageStats) {
                        return new UsageStatsItem(usageStats, maneger);
                    }
                })
                /*.sortBy(new Function<UsageStats, Comparable>() {

                    @Override
                    public Comparable apply(UsageStats usageStats) {
                        return usageStats.getTotalTimeInForeground();
                    }
                })*/
                .toList();
    };

    static List<UsageStats> getUsageStats(final Context context, Calendar beginCal, Calendar endCal){

        checkForUsageStatsPermission(context);
        android.app.usage.UsageStatsManager usageStatsManager=(android.app.usage.UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);

        List<UsageStats> queryUsageStats=usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginCal.getTimeInMillis(), endCal.getTimeInMillis());

        /*List<UsageStatsItem> UsageStatsItems = Stream
                .of(queryUsageStats)
                .filter(new Predicate<UsageStats>() {
                    @Override
                    public boolean test(UsageStats value) {
                        return value.getTotalTimeInForeground()>0;
                    }})
                .map(new Function<UsageStats, UsageStatsItem>() {
                    @Override
                    public UsageStatsItem apply(UsageStats usageStats) {
                        return new UsageStatsItem(usageStats, context.getPackageManager());
                    }
                })
                /*.sortBy(new Function<UsageStats, Comparable>() {

                    @Override
                    public Comparable apply(UsageStats usageStats) {
                        return usageStats.getTotalTimeInForeground();
                    }
                })
                .toList();

        /*queryUsageStats.removeIf(new Predicate<UsageStats>() {
            @Override
            public boolean test(UsageStats usageStats) {
                return false;
            }
        });*/


        return queryUsageStats;
    }

    static List<UsageStatsItem> getDayUsageStatsAsItems(Context context) {
        return toUsageStatsItem(getDayUsageStats(context),context);
    }
}
