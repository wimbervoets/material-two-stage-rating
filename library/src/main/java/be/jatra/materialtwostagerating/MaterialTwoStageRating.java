package be.jatra.materialtwostagerating;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.Date;

import be.jatra.materialtwostagerating.dialog.ConfirmRateDialogContentHolder;
import be.jatra.materialtwostagerating.dialog.FeedbackDialogContentHolder;
import be.jatra.materialtwostagerating.dialog.RatePromptDialogContentHolder;
import be.jatra.materialtwostagerating.pref.PrefUtils;
import be.jatra.materialtwostagerating.pref.Utils;

import static be.jatra.materialtwostagerating.pref.PrefUtils.*;

public class MaterialTwoStageRating {

    private static Settings settings = new Settings();

    private boolean isDebug = false;

    private Context mContext;

    private RatePromptDialogContentHolder ratePromptDialogContentHolder = new RatePromptDialogContentHolder();
    private FeedbackDialogContentHolder feedbackDialogContentHolder = new FeedbackDialogContentHolder();
    private ConfirmRateDialogContentHolder confirmRateDialogContentHolder = new ConfirmRateDialogContentHolder();

    private FeedbackDialogListener feedbackDialogListener;
    private DialogDismissedListener dialogDismissedListener;
    private FeedbackWithRatingReceivedListener feedbackWithRatingReceivedListener;

    private MaterialTwoStageRating(final Context context) {
        this.mContext = context;
        init();
    }

    private void init() {
        ratePromptDialogContentHolder.setRatePromptTitle(mContext.getString(R.string.label_rateprompt_title));
        ratePromptDialogContentHolder.setRatePromptLaterText(mContext.getString(R.string.label_rateprompt_remind_me_later));
        ratePromptDialogContentHolder.setRatePromptNeverText(mContext.getString(R.string.label_rateprompt_never_show_again));
        feedbackDialogContentHolder.setFeedbackPromptTitle(mContext.getString(R.string.label_feedback_title));
        feedbackDialogContentHolder.setFeedbackPromptText(mContext.getString(R.string.label_feedback_content));
        feedbackDialogContentHolder.setFeedbackPromptPositiveText(mContext.getString(R.string.label_feedback_positive_button));
        feedbackDialogContentHolder.setFeedbackPromptNegativeText(mContext.getString(R.string.label_feedback_negative_button));
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

        if (!PrefUtils.getStopTrack(mContext)) {
            if (checkIfMeetsCondition() || isDebug) {
                showRatePromptDialog();
                PrefUtils.setStopTrack(true, mContext);
            }
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
        final MaterialDialog dialog = new MaterialDialog.Builder(context).customView(R.layout.dialog_rate_initial, true).build();
//        final MaterialDialog dialog = new MaterialDialog.Builder(context)
//                .content(R.string.label_question_rate_app)
//                .positiveText(R.string.label_remind_me_later)
//                .negativeText(R.string.label_never_show_again)
//                .build();
        if ((Utils.getBooleanSystemValue(SHARED_PREFERENCES_SHOW_ICON_KEY, context, true))) {
            dialog.setIcon(Utils.twoStageGetAppIconResourceId(context));
        }
        dialog.setCancelable(this.ratePromptDialogContentHolder.isDismissible());

//        // set the custom dialog components - text, image and button
//        TextView title = (TextView) dialog.findViewById(R.id.tvRatePromptTitle);
//        title.setText(ratePromptDialogContentHolder.getRatePromptTitle());
        RatingBar rbRating = (RatingBar) dialog.findViewById(R.id.rbRatePromptBar);
//        ImageView ivAppIcon = (ImageView) dialog.findViewById(R.id.ivAppIcon);
//
//        if ((Utils.getBooleanSystemValue(SHARED_PREFERENCES_SHOW_ICON_KEY, context, true))) {
//            ivAppIcon.setImageResource(Utils.twoStageGetAppIconResourceId(context));
//            ivAppIcon.setVisibility(View.VISIBLE);
//        } else {
//            ivAppIcon.setVisibility(View.GONE);
//        }

        rbRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, final float rating, boolean fromUser) {
                if (rating > threshold) {
                    MaterialDialog dialog1 = createConfirmRateDialog(context, confirmRateDialogContentHolder);
                    if (dialog1 != null) {
                        dialog1.show();
                    }
                } else {
                    MaterialDialog dialog1 = createDefaultFeedbackDialog(context, feedbackDialogContentHolder, new FeedbackDialogListener() {
                        @Override
                        public void onSubmit(String feedback) {
                            if (feedbackDialogListener != null) {
                                feedbackDialogListener.onSubmit(feedback);
                            }
                            if (feedbackWithRatingReceivedListener != null) {
                                feedbackWithRatingReceivedListener.onFeedbackReceived(rating, feedback);
                            }
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                    if (dialog1 != null) {
                        dialog1.show();
                    }
                }
                dialog.dismiss();
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                onDialogDismissed();
            }
        });

        return dialog;
    }

    protected MaterialDialog createConfirmRateDialog(final Context context, final ConfirmRateDialogContentHolder confirmRateDialogContentHolder) {
        final MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(context.getString(R.string.label_confirm_title))
                .content(context.getString(R.string.label_confirm_content))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        final Intent intentToAppstore = settings.getStoreType() == Settings.StoreType.GOOGLEPLAY ?
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
                        dialog.dismiss();
                    }
                })
                .build();
        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                onDialogDismissed();
            }
        });
        return dialog;
    }

    protected MaterialDialog createDefaultFeedbackDialog(final Context context, final FeedbackDialogContentHolder feedbackDialogContentHolder, final FeedbackDialogListener feedbackDialogListener) {
        final MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(R.string.label_feedback_title)
                .content(R.string.label_feedback_content)
                .positiveText(R.string.label_feedback_positive_button)
                .negativeText(R.string.label_feedback_negative_button)
                .inputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT)
                .inputRangeRes(3, -1, R.color.md_edittext_error)
                .input(context.getString(R.string.label_feedback_feedback), "", new MaterialDialog.InputCallback() {
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
                            if (feedbackDialogListener != null) {
                                feedbackDialogListener.onSubmit(inputText);
                            }
                        } else {
                            Toast.makeText(context, "Bro.. Write Something", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {

                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (PrefUtils.shouldResetOnFeedbackDeclined(mContext)) {
                            resetTwoStage();
                        }
                        if (feedbackDialogListener != null) {
                            feedbackDialogListener.onCancel();
                        }
                    }
                })
                .build();

        dialog.setCancelable(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                onDialogDismissed();
            }
        });
        return dialog;
    }

    public MaterialTwoStageRating withAppIcon(final boolean showAppIcon) {
        Utils.setBooleanSystemValue(SHARED_PREFERENCES_SHOW_ICON_KEY, showAppIcon, mContext);
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

    public final MaterialTwoStageRating withRatePromptLaterText(final String ratePromptLaterText) {
        this.ratePromptDialogContentHolder.setRatePromptLaterText(ratePromptLaterText);
        return this;
    }

    public final MaterialTwoStageRating withRatePromptNeverText(final String ratePromptNeverText) {
        this.ratePromptDialogContentHolder.setRatePromptNeverText(ratePromptNeverText);
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

    public final MaterialTwoStageRating withFeedbackDialogListener(final FeedbackDialogListener feedbackDialogListener) {
        this.feedbackDialogListener = feedbackDialogListener;
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
        settings.installDays = installDays;
        return this;
    }

    public MaterialTwoStageRating withLaunchTimes(final int launchTimes) {
        PrefUtils.setTotalLaunchTimes(launchTimes, mContext);
        settings.launchTimes = launchTimes;
        return this;
    }

    public MaterialTwoStageRating withEventsTimes(final int eventsTimes) {
        PrefUtils.setTotalEventsCount(eventsTimes, mContext);
        settings.eventsTimes = eventsTimes;
        return this;
    }


    public MaterialTwoStageRating withThresholdRating(final float thresholdRating) {
        settings.thresholdRating = thresholdRating;
        return this;
    }

    public MaterialTwoStageRating withStoreType(final Settings.StoreType storeType) {
        settings.storeType = storeType;
        return this;
    }

    public MaterialTwoStageRating resetOnDismiss(final boolean shouldReset) {
        PrefUtils.setResetOnDismiss(shouldReset, mContext);
        return this;
    }

    /* ******************************************************************************************
     * Listeners.                                                                               *
     ****************************************************************************************** */
    public MaterialTwoStageRating withFeedbackWithRatingReceivedListener(FeedbackWithRatingReceivedListener feedbackWithRatingReceivedListener) {
        this.feedbackWithRatingReceivedListener = feedbackWithRatingReceivedListener;
        return this;
    }

    public MaterialTwoStageRating withOnDialogDismissedListener(final DialogDismissedListener dialogDismissedListener) {
        this.dialogDismissedListener = dialogDismissedListener;
        return this;
    }

    public void onDialogDismissed() {
        if (PrefUtils.shouldResetOnDismiss(mContext))
            resetTwoStage();
        if (dialogDismissedListener != null)
            dialogDismissedListener.onDialogDismissed();
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
            if (days >= settings.getInstallDays()) {
                return true;
            } else {
                return false;
            }
        }

    }

    private boolean isOverEventCounts() {
        if (PrefUtils.getEventCount(mContext) >= settings.getEventsTimes()) {
            return true;
        } else {
            return false;
        }
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
    private static void setUpSettingsIfNotExists(Context context) {
        settings.setEventsTimes(Utils.getIntSystemValue(SHARED_PREFERENCES_TOTAL_EVENTS_COUNT, context, 10));
        settings.setInstallDays(Utils.getIntSystemValue(SHARED_PREFERENCES_TOTAL_INSTALL_DAYS, context, 5));
        settings.setLaunchTimes(Utils.getIntSystemValue(SHARED_PREFERENCES_TOTAL_LAUNCH_TIMES, context, 5));

    }
}
