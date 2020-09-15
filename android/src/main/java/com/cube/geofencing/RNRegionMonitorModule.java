package com.cube.geofencing;

import androidx.annotation.NonNull;
import android.util.Log;

import com.cube.geofencing.model.MonitoredRegion;
import com.cube.geofencing.model.PersistableData;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Defines the interface available from Javascript in order to manage monitored regions
 */
public class RNRegionMonitorModule extends ReactContextBaseJavaModule
{
	public static final String TAG = "RNRM";
	public static final String TRANSITION_TASK_NAME = "region-monitor-transition";
	public static final String REGION_SYNC_TASK_NAME = "region-monitor-sync";

	private PersistableData data;
	private GeofenceManager geofenceManager;

	public RNRegionMonitorModule(@NonNull ReactApplicationContext reactContext)
	{
		super(reactContext);
		data = PersistableData.load(reactContext);
		geofenceManager = new GeofenceManager(reactContext);
	}

	@ReactMethod
	public void addCircularRegion(@NonNull ReadableMap location, int radiusMetres, @NonNull String requestId, @NonNull final Promise promise)
	{
		try
		{
			Log.d(TAG, "addCircularRegion: " + requestId);

			double latitude = location.getDouble("latitude");
			double longitude = location.getDouble("longitude");
			final MonitoredRegion region = new MonitoredRegion(requestId, latitude, longitude, radiusMetres);
			Geofence geofence = region.createGeofence();
			geofenceManager.addGeofences(Collections.singletonList(geofence), new OnSuccessListener<Void>() {
				@Override
				public void onSuccess(Void aVoid) {
					Log.d(TAG, "addCircularRegion: onSuccess");
					data.addRegion(region);
					data.save(getReactApplicationContext());
					promise.resolve(null);
				}
			}, new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					Log.d(TAG, "addCircularRegion: onFailure");
					promise.reject(TAG, e.getMessage());
				}
			});
		}
		catch (Exception e)
		{
			promise.reject("addCircularRegion exception", e);
		}
	}

	@ReactMethod
	public void clearRegions(@NonNull final Promise promise)
	{
		try
		{
			Log.d(TAG, "clearRegions");
			geofenceManager.clearGeofences(new OnSuccessListener<Void>() {
				@Override
				public void onSuccess(Void aVoid) {
					Log.d(TAG, "clearRegions: onSuccess");
					data.clearRegions();
					data.save(getReactApplicationContext());
					promise.resolve(null);
				}
			}, new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					Log.d(TAG, "clearRegions: onFailure");
					promise.reject(TAG, e.getMessage());
				}
			});
		}
		catch (Exception e)
		{
			promise.reject("clearRegions exception", e);
		}
	}

	@Nullable
	@Override
	public Map<String, Object> getConstants()
	{
		Map<String, String> constants = new HashMap<>();
		constants.put("TRANSITION_TASK_NAME", TRANSITION_TASK_NAME);
		constants.put("REGION_SYNC_TASK_NAME", REGION_SYNC_TASK_NAME);
		return super.getConstants();
	}

	@Override
	public String getName()
	{
		return "RNRegionMonitor";
	}

	@ReactMethod
	public void removeCircularRegion(@NonNull final String requestId, @NonNull final Promise promise)
	{
		try
		{
			Log.d(TAG, "removeCircularRegion: " + requestId);
			geofenceManager.removeGeofence(requestId, new OnSuccessListener<Void>() {
				@Override
				public void onSuccess(Void aVoid) {
					Log.d(TAG, "removeCircularRegion: onSuccess");
					data.removeRegion(requestId);
					data.save(getReactApplicationContext());
					promise.resolve(null);
				}
			}, new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					Log.d(TAG, "removeCircularRegion: onFailure");
					promise.reject(TAG, e.getMessage());
				}
			});
		}
		catch (Exception e)
		{
			promise.reject("removeCircularRegion exception", e);
		}
	}
}
