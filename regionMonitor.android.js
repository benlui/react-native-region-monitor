import {AppRegistry, NativeModules, PermissionsAndroid} from 'react-native';

const {RNRegionMonitor} = NativeModules;

var callbacks = [];

// for backgorund, to allow custom callback in RegionMonitorTask.js file, default at the same level of index.js of your project
AppRegistry.registerHeadlessTask("region-monitor-transition", () => require('../../../RegionMonitorTask'));

const permissionsCheck = () => {
	return PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION).then(permissionStatus => {
		switch (permissionStatus) {
			case true: // Pre-Marshmallow Android devices send true back
			case PermissionsAndroid.RESULTS.GRANTED: {
				return true;
			}
			case PermissionsAndroid.RESULTS.NEVER_ASK_AGAIN:
			case PermissionsAndroid.RESULTS.DENIED:
			default: {
				throw new Error("Cannot obtain permissions to perform geofencing");
			}
		}
	});
};

export default {
	addCircularRegion: (center, radius, id) => {
		return permissionsCheck().then(() => {
			return RNRegionMonitor.addCircularRegion(center, radius, id);
		});
	},
	clearRegions: RNRegionMonitor.clearRegions,
	removeCircularRegion: RNRegionMonitor.removeCircularRegion,
	requestAuthorization: permissionsCheck,
	onRegionChange: (callback) => {
		callbacks.push(callback);
		return function off() {
			const idx = callbacks.indexOf(callback);
			if(idx >= 0) {
				callbacks.splice(i, 1);
			}
		};
	},
}
