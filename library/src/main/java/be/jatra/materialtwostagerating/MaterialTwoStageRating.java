package be.jatra.materialtwostagerating;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.widget.RatingBar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Date;

import be.jatra.materialtwostagerating.callback.ConfirmRateDialogCallback;
import be.jatra.materialtwostagerating.callback.FeedbackDialogCallback;
import be.jatra.materialtwostagerating.callback.RatePromptDialogCallback;
import be.jatra.materialtwostagerating.dialog.ConfirmRateDialogContentHolder;
import be.jatra.materialtwostagerating.dialog.FeedbackDialogContentHolder;
import be.jatra.materialtwostagerating.dialog.RatePromptDialogContentHolder;
import be.jatra.materialtwostagerating.pref.PrefUtils;
import be.jatra.materialtwostagerating.pref.Utils;

public class MaterialTwoStageRating {

    private static Settings settings = new Settings();

    private boolean isDebug = false;

    private Context mContext;

    private RatePromptDialogContentHolder ratePromptDialogContentHolder = new RatePromptDialogContentHolder();
    private FeedbackDialogContentHolder feedbackDialogContentHolder = new FeedbackDialogContentHolder();
    private ConfirmRateDialogContentHolder confirmRateDialogContentHolder = new ConfirmRateDialogContentHolder();

    private FeedbackDialogCallback feedbackDialogCallback;
    private RatePromptDialogCallback ratePromptDialogCallback;
    private ConfirmRateDialogCallback confirmRateDialogCallback;

    private MaterialTwoStageRating(final Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        ratePromptDialogContentHolder.setIcon(Utils.getAppIconResourceId(mContext));
        ratePromptDialogContentHolder.setRatePromptTitle(mContext.getString(R.string.label_rateprompt_title));
        feedbackDialogContentHolder.setFeedbackPromptTitle(mContext.getString(R.string.label_feedback_title));
        feedbackDialogContentHolder.setFeedbackPromptText(mContext.getString(R.string.label_feedback_content));
        feedbackDialogContentHolder.setFeedbackPromptPositiveText(mContext.getString(R.string.label_feedback_positive_button));
        feedbackDialogContentHolder.setFeedbackPromptNegativeText(mContext.getString(R.string.label_feedback_negative_button));
        feedbackDialogContentHolder.setFeedbackPlaceholder(mContext.getString(R.string.label_feedback_feedback));
        confirmRateDialogContentHolder.setConfirmRateTitle(mContext.getString(R.string.label_confirm_title));
        confirmRateDialogContentHolder.setConfirmRateText(mContext.getString(R.string.label_confirm_content));
        confirmRateDialogContentHolder.setConfirmRatePositiveText(mContext.getString(R.string.label_confirm_positive_button));
        confirmRateDialogContentHolder.setConfirmRateNegativeText(mContext.getString(R.string.label_confirm_negative_button));
    }

    public static MaterialTwoStageRating with(final Context context) {
        setUpSettingsIfNotExists(context);
        return new MaterialTwoStageRating(context);
    }

    /**
     * Checks if the conditions are met (anu one ) and shows prompt if yes.
     * But before it checks whether it has already shown the prompt and user has responded
     * <p>
     * Also it always shows up in Debug mode
     */
    public void showIfMeetsConditions() {

        if (!PrefUtils.getStopTrack(mContext) && (checkIfMeetsCondition() || isDebug)) {
            showRatePromptDialog();
            PrefUtils.setStopTrack(true, mContext);
        }
    }

    private boolean checkIfMeetsCondition() {
        return isOverLaunchTimes() ||
                isOverInstallDays() || isOverEventCounts();

    }

    public void showRatePromptDialog() {
        MaterialDialog dialog = createRatePromptDialog(mContext, ratePromptDialogContentHolder, settings.getThresholdRating());
        if (dialog != null) {
            dialog.show();
        }
    }

    protected MaterialDialog createRatePromptDialog(final Context context, final RatePromptDialogContentHolder ratePromptDialogContentHolder, final float threshold) {
        final MaterialDialog dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.ratingbar, true)
                .title(ratePromptDialogContentHolder.getRatePromptTitle())
                .build();

        if (PrefUtils.showAppIcon(context)) {
            dialog.setIcon(ratePromptDialogContentHolder.getIcon());//Utils.getAppIconResourceId(context)
        }

        // set the custom dialog components - text, image and button
        RatingBar rbRating = (RatingBar) dialog.findViewById(R.id.ratePromptBar);
        rbRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
                if (rating > threshold) {
                    createConfirmRateDialog(context, confirmRateDialogContentHolder).show();
                } else {
                    createFeedbackDialog(context, feedbackDialogContentHolder, rating).show();
                }
                dialog.dismiss();
            }
        });
        dialog.setCancelable(this.ratePromptDialogContentHolder.isDismissible());
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                resetTwoStageIfPreferred();
                if (ratePromptDialogCallback != null) {
                    ratePromptDialogCallback.onCancel();
                }
            }
        });
        return dialog;
    }

    protected MaterialDialog createConfirmRateDialog(final Context context, final ConfirmRateDialogContentHolder confirmRateDialogContentHolder) {
        final MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(confirmRateDialogContentHolder.getConfirmRateTitle())
                .content(confirmRateDialogContentHolder.getConfirmRateText())
                .positiveText(confirmRateDialogContentHolder.getConfirmRatePositiveText())
                .negativeText(confirmRateDialogContentHolder.getConfirmRateNegativeText())
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        final Intent intentToAppstore = settings.getStoreType() == Settings.StoreType.GOOGLE_PLAY ?
                                IntentHelper.createIntentForGooglePlay(context) : IntentHelper.createIntentForAmazonAppstore(context);
                        context.startActivity(intentToAppstore);
                        dialog.dismiss();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        //Reseting twostage if declined and setting is done so
                        if ((PrefUtils.shouldResetOnRatingDeclined(mContext))) {
                            resetTwoStage();
                        }

                        if (confirmRateDialogCallback != null) {
                            confirmRateDialogCallback.onCancel();
                        }
                        dialog.dismiss();
                    }
                })
                .build();
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                resetTwoStageIfPreferred();
                if (confirmRateDialogCallback != null) {
                    confirmRateDialogCallback.onCancel();
                }
            }
        });
        return dialog;
    }

    protected MaterialDialog createFeedbackDialog(final Context context, final FeedbackDialogContentHolder feedbackDialogContentHolder, final float rating) {
        final MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(feedbackDialogContentHolder.getFeedbackPromptTitle())
                .content(feedbackDialogContentHolder.getFeedbackPromptText())
                .positiveText(feedbackDialogContentHolder.getFeedbackPromptPositiveText())
                .negativeText(feedbackDialogContentHolder.getFeedbackPromptNegativeText())
                .inputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT)
                .inputRangeRes(3, -1, R.color.md_edittext_error)
                .input(feedbackDialogContentHolder.getFeedbackPlaceholder(), "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {

                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        final String inputText = dialog.getInputEditText().getText().toString();
                        if (inputText != null && inputText.length() > 0) {
                            dialog.dismiss();
                            if (feedbackDialogCallback != null) {
                                feedbackDialogCallback.onFeedbackReceived(inputText, rating);
                            }
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {

                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (PrefUtils.shouldResetOnFeedbackDeclined(mContext)) {
                            resetTwoStage();
                        }
                        if (feedbackDialogCallback != null) {
                            feedbackDialogCallback.onCancel();
                        }
                    }
                })
                .build();
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                resetTwoStageIfPreferred();
                if (feedbackDialogCallback != null) {
                    feedbackDialogCallback.onCancel();
                }
            }
        });
        return dialog;
    }

    public MaterialTwoStageRating withIcon(final boolean showIcon) {
        PrefUtils.setShowIcon(showIcon, mContext);
        return this;
    }

    public MaterialTwoStageRating withCustomIcon(@DrawableRes final int icon) {
        this.ratePromptDialogContentHolder.setIcon(icon);
        return this;
    }

    public void isDebug(final boolean isDebug) {
        this.isDebug = isDebug;
    }

    /* ******************************************************************************************
     * Methods to configure RatePromptDialog.                                                   *
     ****************************************************************************************** */
    public final MaterialTwoStageRating withRatePromptTitle(final String ratePromptTitle) {
        this.ratePromptDialogContentHolder.setRatePromptTitle(ratePromptTitle);
        return this;
    }

    public final MaterialTwoStageRating withRatePromptDismissible(final boolean dismissible) {
        this.ratePromptDialogContentHolder.setDismissible(dismissible);
        return this;
    }

    /* ******************************************************************************************
     * Methods to configure FeedbackDialog.                                                     *
     ****************************************************************************************** */
    public final MaterialTwoStageRating withFeedbackDialogTitle(final String feedbackPromptTitle) {
        this.feedbackDialogContentHolder.setFeedbackPromptTitle(feedbackPromptTitle);
        return this;
    }

    public final MaterialTwoStageRating withFeedbackDialogDescription(final String feedbackPromptText) {
        this.feedbackDialogContentHolder.setFeedbackPromptText(feedbackPromptText);
        return this;
    }

    public final MaterialTwoStageRating withFeedbackDialogPositiveText(final String feedbackPromptPositiveText) {
        this.feedbackDialogContentHolder.setFeedbackPromptPositiveText(feedbackPromptPositiveText);
        return this;
    }

    public MaterialTwoStageRating withFeedbackDialogNegativeText(final String feedbackPromptNegativeText) {
        this.feedbackDialogContentHolder.setFeedbackPromptNegativeText(feedbackPromptNegativeText);
        return this;
    }

    public final MaterialTwoStageRating withFeedbackDialogDismissible(final boolean dismissible) {
        this.feedbackDialogContentHolder.setDismissible(dismissible);
        return this;
    }

    /* ******************************************************************************************
     * Methods to configure ConfirmRateDialog.                                                  *
     ****************************************************************************************** */
    public final MaterialTwoStageRating withConfirmRateDialogTitle(final String confirmRateTitle) {
        this.confirmRateDialogContentHolder.setConfirmRateTitle(confirmRateTitle);
        return this;
    }


    public final MaterialTwoStageRating withConfirmRateDialogDescription(final String confirmRateText) {
        this.confirmRateDialogContentHolder.setConfirmRateText(confirmRateText);
        return this;
    }

    public final MaterialTwoStageRating withConfirmRateDialogNegativeText(final String confirmRateNegativeText) {
        this.confirmRateDialogContentHolder.setConfirmRateNegativeText(confirmRateNegativeText);
        return this;
    }

    public final MaterialTwoStageRating withConfirmRateDialogPositiveText(final String confirmRatePositiveText) {
        this.confirmRateDialogContentHolder.setConfirmRatePositiveText(confirmRatePositiveText);
        return this;
    }

    public final MaterialTwoStageRating withConfirmRateDialogDismissible(final boolean dismissible) {
        this.confirmRateDialogContentHolder.setDismissible(dismissible);
        return this;
    }

    /* ******************************************************************************************
     * Methods to configure the settings.                                                       *
     ****************************************************************************************** */
    public MaterialTwoStageRating withInstallDays(final int installDays) {
        PrefUtils.setTotalInstallDays(installDays, mContext);
        settings.setInstallDays(installDays);
        return this;
    }

    public MaterialTwoStageRating withLaunchTimes(final int launchTimes) {
        PrefUtils.setTotalLaunchTimes(launchTimes, mContext);
        settings.setLaunchTimes(launchTimes);
        return this;
    }

    public MaterialTwoStageRating withEventsTimes(final int eventsTimes) {
        PrefUtils.setTotalEventsCount(eventsTimes, mContext);
        settings.setEventsTimes(eventsTimes);
        return this;
    }

    public MaterialTwoStageRating withThresholdRating(final float thresholdRating) {
        settings.setThresholdRating(thresholdRating);
        return this;
    }

    public MaterialTwoStageRating withStoreType(final Settings.StoreType storeType) {
        settings.setStoreType(storeType);
        return this;
    }

    public MaterialTwoStageRating resetOnDismiss(final boolean shouldReset) {
        PrefUtils.setResetOnDismiss(shouldReset, mContext);
        return this;
    }

    /* ******************************************************************************************
     * Callbacks.                                                                               *
     ****************************************************************************************** */
    public final MaterialTwoStageRating withFeedbackDialogCallback(final FeedbackDialogCallback feedbackDialogCallback) {
        this.feedbackDialogCallback = feedbackDialogCallback;
        return this;
    }

    public MaterialTwoStageRating withRatePromptDialogCallback(final RatePromptDialogCallback ratePromptDialogCallback) {
        this.ratePromptDialogCallback = ratePromptDialogCallback;
        return this;
    }

    public MaterialTwoStageRating withConfirmRateDialogCallback(final ConfirmRateDialogCallback confirmRateDialogCallback) {
        this.confirmRateDialogCallback = confirmRateDialogCallback;
        return this;
    }

    /* ******************************************************************************************
     * Settings.                                                                                *
     ****************************************************************************************** */
    public void incrementEvent() {
        int eventCount = PrefUtils.getEventCount(mContext) + 1;
        PrefUtils.setEventCount(eventCount, mContext);
        showIfMeetsConditions();
    }

    /**
     * Setting install date of app
     */
    public void setInstallDate() {
        if (PrefUtils.getInstallDate(mContext) == 0) {
            //getting the current time in milliseconds, and creating a Date object from it:
            Date date = new Date(System.currentTimeMillis()); //or simply new Date();
            long millis = date.getTime();
            PrefUtils.setInstallDate(millis, mContext);
        }
    }

    private boolean isOverLaunchTimes() {
        int launches = PrefUtils.getLaunchCount(mContext);
        if (launches >= settings.getLaunchTimes()) {
            return true;
        } else {
            int count = launches + 1;
            PrefUtils.setLaunchCount(count, mContext);
            return false;
        }
    }

    private boolean isOverInstallDays() {
        if (PrefUtils.getInstallDate(mContext) == 0) {
            setInstallDate();
            return false;
        } else {
            Date installDate = new Date(PrefUtils.getInstallDate(mContext));
            Date currentDate = new Date(System.currentTimeMillis());
            long days = Utils.daysBetween(installDate, currentDate);
            return days >= settings.getInstallDays();
        }
    }

    private boolean isOverEventCounts() {
        return PrefUtils.getEventCount(mContext) >= settings.getEventsTimes();
    }

    /**
     * User gave good rating at first but declined to rate on playstore.
     *
     * @param shouldReset the flag indicating if the rating should be reset or not
     * @return the current instance
     */
    public MaterialTwoStageRating resetOnRatingDeclined(boolean shouldReset) {
        PrefUtils.setResetOnRatingDeclined(shouldReset, mContext);
        return this;
    }

    public MaterialTwoStageRating resetOnFeedBackDeclined(boolean shouldReset) {
        PrefUtils.setResetOnFeedbackDeclined(shouldReset, mContext);
        return this;
    }

    private void resetTwoStageIfPreferred() {
        resetTwoStage(PrefUtils.shouldResetOnDismiss(mContext));
    }

    private void resetTwoStage(final boolean resetOnDismiss) {
        if (resetOnDismiss)
            resetTwoStage();
    }

    /**
     * This method is called when the user chooses 'remind me later'.
     */
    private void resetTwoStage() {
        PrefUtils.setInstallDate(System.currentTimeMillis(), mContext);
        PrefUtils.setInstallDays(0, mContext);
        PrefUtils.setEventCount(0, mContext);
        PrefUtils.setLaunchCount(0, mContext);
        PrefUtils.setStopTrack(false, mContext);
    }

    /**
     * Sets up setting items if they are in preferences. Else it just sets the default values
     * @param context the context
     */
    private static void setUpSettingsIfNotExists(final Context context) {
        settings.setEventsTimes(PrefUtils.getTotalEventsCount(context));
        settings.setInstallDays(PrefUtils.getTotalInstallDays(context));
        settings.setLaunchTimes(PrefUtils.getTotalLaunchTimes(context));
    }
}
