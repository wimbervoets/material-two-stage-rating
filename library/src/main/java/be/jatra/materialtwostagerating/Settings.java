package be.jatra.materialtwostagerating;

final class Settings {

    public enum StoreType {
        GOOGLE_PLAY,
        AMAZON
    }

    private int launchTimes = 10;

    private int installDays = 5;

    private int eventsTimes = 10;

    private float thresholdRating = 3;

    private StoreType storeType = StoreType.GOOGLE_PLAY;

    public void setInstallDays(final int installDays) {
        this.installDays = installDays;
    }

    public int getInstallDays() {
        return this.installDays;
    }

    public void setLaunchTimes(final int launchTimes) {
        this.launchTimes = launchTimes;
    }

    public int getLaunchTimes() {
        return this.launchTimes;
    }

    public void setEventsTimes(final int eventsTimes) {
        this.eventsTimes = eventsTimes;
    }

    public int getEventsTimes() {
        return this.eventsTimes;
    }

    public void setThresholdRating(final float thresholdRating) {
        this.thresholdRating = thresholdRating;
    }

    public float getThresholdRating() {
        return this.thresholdRating;
    }

    public void setStoreType(final StoreType storeType) {
        this.storeType = storeType;
    }

    public StoreType getStoreType() {
        return this.storeType;
    }
}
