package com.motorola.fmradio;

import java.util.Arrays;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

public class FMWidgetProvider extends AppWidgetProvider {
    private static final String TAG = "FMWidgetProvider";
    public static final String ACTION_UPDATE = "Update";
    private RemoteViews mWidgetView;

    private String mStationName;
    private String mRds;
    private int mFreq = -1;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION_UPDATE)) {
            mStationName = intent.getStringExtra("Station");
            mRds = intent.getStringExtra("Rds");
            if(intent.getIntExtra("CurFreq", 0)==0) {
                Log.i(TAG, "onRecieve " + action + " " + mStationName + " " + mRds + " " + 0);
                return;
            }
            mFreq = intent.getIntExtra("CurFreq", 0);
            Log.i(TAG, "onRecieve " + action + " " + mStationName + " " + mRds + " " + mFreq);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName thisAppWidget = new ComponentName(context.getPackageName(), FMWidgetProvider.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);
            onUpdate(context, appWidgetManager, appWidgetIds);
        } else {
            super.onReceive(context, intent);
        }
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        Log.i(TAG,"Updating widgets " + Arrays.asList(appWidgetIds));
        
        boolean isRunning = isMyServiceRunning(context);

        if(isRunning && (mFreq == -1)) {
            Intent i = new Intent(context, FMRadioPlayerService.class);
            i.setAction(FMRadioPlayerService.ACTION_FM_COMMAND);
            i.putExtra(FMRadioPlayerService.EXTRA_COMMAND, FMRadioPlayerService.COMMAND_UPDATE);
            context.startService(i);
        }
        
        // Perform this loop procedure for each App Widget that belongs to this
        // provider
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Get the layout for the App Widget and attach on-click listeners
            mWidgetView = new RemoteViews(context.getPackageName(),
                    R.layout.fm_widget);

            mWidgetView.setOnClickPendingIntent(R.id.widget_previous,
            		buildServiceIntent(context,FMRadioPlayerService.COMMAND_PREV));
            mWidgetView.setOnClickPendingIntent(R.id.widget_next,
            		buildServiceIntent(context,FMRadioPlayerService.COMMAND_NEXT));
            mWidgetView.setOnClickPendingIntent(R.id.widget_collapse,
            		buildServiceIntent(context,FMRadioPlayerService.COMMAND_STOP));

            Intent launchIntent = new Intent();
            launchIntent.setAction(Intent.ACTION_MAIN);
            launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            launchIntent.setComponent(new ComponentName("com.motorola.fmradio",
            		"com.motorola.fmradio.FMRadioMain"));
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,    launchIntent, 0);
            mWidgetView.setOnClickPendingIntent(R.id.widget_station, pendingIntent);
            mWidgetView.setOnClickPendingIntent(R.id.widget_icon, pendingIntent);
            if (mFreq == -1) {
                mWidgetView.setTextViewText(R.id.widget_station, context.getString(R.string.start));
                mWidgetView.setTextViewText(R.id.widget_rds, context.getString(R.string.no_rss));
            }
            else {
                mWidgetView.setTextViewText(R.id.widget_station, mStationName);
                mWidgetView.setTextViewText(R.id.widget_rds, mRds);
            }
            // Tell the AppWidgetManager to perform an update on the current app
            // widget
            appWidgetManager.updateAppWidget(appWidgetId, mWidgetView);
        }
    }

    private PendingIntent buildServiceIntent(Context context, String command) {
        Intent i = new Intent(context, FMRadioPlayerService.class);
        i.setAction(FMRadioPlayerService.ACTION_FM_COMMAND);
        i.putExtra(FMRadioPlayerService.EXTRA_COMMAND, command);

        return PendingIntent.getService(context,
                0, i, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    
    private boolean isMyServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.motorola.fmradio.FMRadioPlayerService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
