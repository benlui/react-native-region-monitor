package com.cube.geofencing;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static com.cube.geofencing.RNRegionMonitorModule.TAG;

/**
 * Created by tim on 19/01/2017.
 */
public class GeofenceManager implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
{
	private GoogleApiClient googleApiClient;
	private GeofencingClient mGeofencingClient;
	private PendingIntent geofencePendingIntent;
	private CountDownLatch countDownLatch = new CountDownLatch(1);

	public GeofenceManager(@NonNull Context context)
	{
		/*
		googleApiClient = new GoogleApiClient.Builder(context).addConnectionCallbacks(this)
		                                                      .addOnConnectionFailedListener(this)
		                                                      .addApi(LocationServices.API)
		                                                      .build();
		googleApiClient.connect();
*/
		geofencePendingIntent = getGeofenceTransitionPendingIntent(context);
		mGeofencingClient = LocationServices.getGeofencingClient(context);
	}

	@Override
	public void onConnected(@Nullable Bundle bundle)
	{
		countDownLatch.countDown();
		Log.d(TAG, "RNRegionMonitor Google client connected");
	}

	@Override
	public void onConnectionSuspended(int i)
	{
		Log.d(TAG, "RNRegionMonitor Google client suspended: " + i);
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
	{
		countDownLatch.countDown();
		Log.d(TAG, "RNRegionMonitor Google client failed: " + connectionResult.getErrorMessage());
	}

	public void removeGeofence(@NonNull String id, OnSuccessListener<Void> successListener, OnFailureListener failureListener)
	{
		mGeofencingClient.removeGeofences(Collections.singletonList(id))
				.addOnSuccessListener(successListener)
				.addOnFailureListener(failureListener);
	}

	public void addGeofences(@NonNull List<Geofence> geofences, OnSuccessListener<Void> successListener, OnFailureListener failureListener)
	{
		GeofencingRequest request = new GeofencingRequest.Builder().addGeofences(geofences).setInitialTrigger(Geofence.GEOFENCE_TRANSITION_ENTER).build();
		mGeofencingClient.addGeofences(request, geofencePendingIntent)
				.addOnSuccessListener(successListener)
				.addOnFailureListener(failureListener);
	}

	public void clearGeofences(OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
		mGeofencingClient.removeGeofences(geofencePendingIntent)
				.addOnSuccessListener(successListener)
				.addOnFailureListener(failureListener);
	}

	private PendingIntent getGeofenceTransitionPendingIntent(Context context) {
		Intent intent = new Intent(context, RNRegionTransitionService.class);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return PendingIntent.getForegroundService(context, 0, intent, PendingIntent.FLAG_MUTABLE);
		} else {
			return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		}
		//return PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
}
