package com.cube.geofencing;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import androidx.annotation.Nullable;
import android.util.Log;

import com.facebook.react.HeadlessJsTaskService;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.jstasks.HeadlessJsTaskConfig;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import static com.cube.geofencing.RNRegionMonitorModule.TAG;

/**
 * Service launched when a region transition occurs
 */
public class RNRegionTransitionService extends HeadlessJsTaskService
{
	@Override
	public void onCreate() {
		super.onCreate();

		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
			Context mContext = this.getApplicationContext();
			Resources res = mContext.getResources();
			String packageName = mContext.getPackageName();
			String CHANNEL_ID = "rn-push-notification-channel-id";
			String CHANNEL_NAME = res.getString(res.getIdentifier("notification_channel_name", "string", packageName));
			String CHANNEL_DESCRIPTION = res.getString(res.getIdentifier("notification_channel_description", "string", packageName));

			NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
			channel.setDescription(CHANNEL_DESCRIPTION);
			((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

			Notification notification =
					new Notification.Builder(mContext, CHANNEL_ID)
							.setContentTitle("")
							.setContentText("")
							.setSmallIcon(res.getIdentifier("ic_notification", "mipmap", packageName))
							.build();

			startForeground(1, notification);
		}
	}

	@Override
	@Nullable
	protected HeadlessJsTaskConfig getTaskConfig(Intent intent)
	{
		if (intent.getExtras() == null)
		{
			return null;
		}

		GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

		if (geofencingEvent.hasError())
		{
			// Suppress geofencing event with error
			Log.d(TAG, "Suppress geocoding event with error");
			return null;
		}

		WritableMap location = Arguments.createMap();
		location.putDouble("latitude", geofencingEvent.getTriggeringLocation().getLatitude());
		location.putDouble("longitude", geofencingEvent.getTriggeringLocation().getLongitude());

		WritableMap region = Arguments.createMap();
		region.putString("identifier", geofencingEvent.getTriggeringGeofences().get(0).getRequestId());

		WritableArray regionIdentifiers = Arguments.createArray();
		for (Geofence triggered: geofencingEvent.getTriggeringGeofences())
		{
			regionIdentifiers.pushString(triggered.getRequestId());
		}
		region.putArray("identifiers", regionIdentifiers);

		WritableMap jsArgs = Arguments.createMap();
		jsArgs.putMap("location", location);
		jsArgs.putMap("region", region);
		jsArgs.putBoolean("didEnter", geofencingEvent.getGeofenceTransition() == Geofence.GEOFENCE_TRANSITION_ENTER);
		jsArgs.putBoolean("didExit", geofencingEvent.getGeofenceTransition() == Geofence.GEOFENCE_TRANSITION_EXIT);
		//jsArgs.putBoolean("didDwell", geofencingEvent.getGeofenceTransition() == Geofence.GEOFENCE_TRANSITION_DWELL);

		Log.d(TAG, "Report geofencing event to JS: " + jsArgs);
		return new HeadlessJsTaskConfig(RNRegionMonitorModule.TRANSITION_TASK_NAME, jsArgs, 0, true);
	}
}
