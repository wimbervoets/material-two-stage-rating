package be.jatra.materialtwostagerating.pref;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefUtils {

    private static final String SHARED_PREFERENCES_NAME = "MaterialTwoStageRating-prefs";

    public static final String SHARED_PREFERENCES_SHOW_ICON_KEY = "pref_MaterialTwoStageRating_ShowAppIcon";
    public static final String SHARED_PREFERENCES_SHOULD_RESET_ON_DISMISS = "pref_MaterialTwoStageRating_ShouldRefreshOnPrimaryDismiss";
    public static final String SHARED_PREFERENCES_SHOULD_RESET_ON_RATING_DECLINED = "pref_MaterialTwoStageRating_ShouldResetOnDecliningToRate";
    public static final String SHARED_PREFERENCES_SHOULD_RESET_ON_FEEDBACK_DECLINED = "pref_MaterialTwoStageRating_ShouldResetOnDecliningForFeedBack";
    public static final String SHARED_PREFERENCES_TOTAL_LAUNCH_TIMES = "pref_MaterialTwoStageRating_TotalLaunchTimes";
    public static final String SHARED_PREFERENCES_TOTAL_EVENTS_COUNT = "pref_MaterialTwoStageRating_TotalEventCount";
    public static final String SHARED_PREFERENCES_TOTAL_INSTALL_DAYS = "pref_MaterialTwoStageRating_TotalInstallDays";

    private static final String LAUNCH_COUNT = "pref_MaterialTwoStageRating_LaunchCount";
    private static final String INSTALL_DAYS = "pref_MaterialTwoStageRating_InstallDays";
    private static final String INSTALL_DATE = "pref_MaterialTwoStageRating_InstallDate";
    private static final String EVENT_COUNT = "pref_MaterialTwoStageRating_EventCount";
    private static final String STOP_TRACK = "pref_MaterialTwoStageRating_StopTrack";

    public static boolean shouldResetOnDismiss(final Context context) {
        return getBooleanSystemValue(SHARED_PREFERENCES_SHOULD_RESET_ON_DISMISS, context, true);
    }

    public static boolean shouldResetOnRatingDeclined(final Context context) {
        return getBooleanSystemValue(SHARED_PREFERENCES_SHOULD_RESET_ON_RATING_DECLINED, context, false);
    }

    public static boolean shouldResetOnFeedbackDeclined(final Context context) {
        return getBooleanSystemValue(SHARED_PREFERENCES_SHOULD_RESET_ON_FEEDBACK_DECLINED, context);
    }

    public static int getTotalLaunchTimes(final Context context) {
        return getIntSystemValue(SHARED_PREFERENCES_TOTAL_LAUNCH_TIMES, context);
    }

    public static int getTotalEventsCount(final Context context) {
        return getIntSystemValue(SHARED_PREFERENCES_TOTAL_EVENTS_COUNT, context);
    }

    public static int getTotalInstallDays( final Context context) {
        return getIntSystemValue(SHARED_PREFERENCES_TOTAL_INSTALL_DAYS, context);
    }

    public static int getEventCount(final Context context) {
        return getIntSystemValue(EVENT_COUNT, context);
    }

    public static long getInstallDate(final Context context) {
        return getLongSystemValue(INSTALL_DATE, context);
    }

    public static int getInstallDays(final Context context) {
        return getIntSystemValue(INSTALL_DAYS, context);
    }

    public static int getLaunchCount(final Context context) {
        return getIntSystemValue(LAUNCH_COUNT, context);
    }

    public static boolean getStopTrack(final Context context) {
        return getBooleanSystemValue(STOP_TRACK, context);
    }

    public static void setResetOnDismiss(final boolean resetOnDismiss, final Context context) {
        setBooleanSystemValue(SHARED_PREFERENCES_SHOULD_RESET_ON_DISMISS, resetOnDismiss, context);
    }

    public static void setResetOnRatingDeclined(final boolean resetOnRatingDeclined, final Context context) {
        setBooleanSystemValue(SHARED_PREFERENCES_SHOULD_RESET_ON_RATING_DECLINED, resetOnRatingDeclined, context);
    }

    public static void setResetOnFeedbackDeclined(final boolean resetOnFeedbackDeclined, final Context context) {
        setBooleanSystemValue(SHARED_PREFERENCES_SHOULD_RESET_ON_FEEDBACK_DECLINED, resetOnFeedbackDeclined, context);
    }

    public static void setTotalLaunchTimes(final int totalLaunchTimes, final Context context) {
        setIntSystemValue(SHARED_PREFERENCES_TOTAL_LAUNCH_TIMES, totalLaunchTimes, context);
    }

    public static void setTotalEventsCount(final int totalEventsCount, final Context context) {
        setIntSystemValue(SHARED_PREFERENCES_TOTAL_EVENTS_COUNT, totalEventsCount, context);
    }

    public static void setTotalInstallDays(final int totalInstallDays, final Context context) {
        setIntSystemValue(SHARED_PREFERENCES_TOTAL_INSTALL_DAYS, totalInstallDays, context);
    }

    public static void setEventCount(final int eventCount, final Context context) {
        setIntSystemValue(EVENT_COUNT, eventCount, context);
    }

    public static void setInstallDate(final long installDate, final Context context) {
        setLongSystemValue(INSTALL_DATE, installDate, context);
    }

    public static void setInstallDays(final int installDays, final Context context) {
        setIntSystemValue(INSTALL_DAYS, installDays, context);
    }

    public static void setLaunchCount(final int launchCount, final Context context) {
        setIntSystemValue(LAUNCH_COUNT, launchCount, context);
    }

    public static void setStopTrack(final boolean stopTrack, final Context context) {
        setBooleanSystemValue(STOP_TRACK, stopTrack, context);
    }

    public static void setStringSystemValue(final String key, final String value, final Context context) {
        SharedPreferences myPrefs = context
                .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putString(key, value);
        prefsEditor.commit();
    }

    public static void setIntSystemValue(final String key, final int value, final Context context) {
        SharedPreferences myPrefs = context
                .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putInt(key, value);
        prefsEditor.commit();
    }

    public static void setLongSystemValue(final String key, final long value, final Context context) {
        SharedPreferences myPrefs = context
                .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putLong(key, value);
        prefsEditor.commit();
    }


    public static void setBooleanSystemValue(final String key, final boolean value, final Context context) {
        SharedPreferences myPrefs = context
                .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = myPrefs.edit();
        prefsEditor.putBoolean(key, value);
        prefsEditor.commit();
    }

    public static String getStringSystemValue(final String key, final Context context) {
        SharedPreferences myPrefs = context
                .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String value = myPrefs.getString(key, null);
        return value;
    }


    public static int getIntSystemValue(final String key, final Context context) {
        return getIntSystemValue(key, context, 0);
    }

    public static int getIntSystemValue(final String key, final Context context, final int defaultValue) {
        int value = -1;
        SharedPreferences myPrefs = context
                .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        value = myPrefs.getInt(key, defaultValue);

        return value;
    }

    public static long getLongSystemValue(final String key, final Context context) {
        long value = -1;
        SharedPreferences myPrefs = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        value = myPrefs.getLong(key, 0);

        return value;
    }

    public static boolean getBooleanSystemValue(final String key, final Context context) {
        return getBooleanSystemValue(key, context, false);
    }

    public static boolean getBooleanSystemValue(final String key, final Context context, final boolean defaultValue) {
        boolean value = false;
        SharedPreferences myPrefs = context
                .getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        value = myPrefs.getBoolean(key, defaultValue);

        return value;
    }
}
