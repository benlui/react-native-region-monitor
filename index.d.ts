declare module '@hkpuits/react-native-region-monitor' {
    class RegionMonitor {
        clearRegions(): Promise<void>;
        removeCircularRegion(regionId: string): Promise<void>;
        addCircularRegion(center: { latitude: number, longitude: number }, radius: number, regionId: string): Promise<void>;
        onRegionChange(callback: (event: any) => void): () => void;
    }

    const regionMonitor: RegionMonitor;
    export default regionMonitor;
}