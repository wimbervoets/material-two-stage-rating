package be.jatra.materialtwostagerating.sampleapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.Locale;

import be.jatra.materialtwostagerating.callback.ConfirmRateDialogCallback;
import be.jatra.materialtwostagerating.callback.RatePromptDialogCallback;
import be.jatra.materialtwostagerating.callback.FeedbackDialogCallback;
import be.jatra.materialtwostagerating.MaterialTwoStageRating;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        MaterialTwoStageRating.with(MainActivity.this).showIfMeetsConditions();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button dialog1 = (Button) findViewById(R.id.showMaterialTwoStageRatingDialog1);
        dialog1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMaterialTwoStageRatingDialog1();
            }
        });

        Button dialog2 = (Button) findViewById(R.id.showMaterialTwoStageRatingDialog2);
        dialog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMaterialTwoStageRatingDialog2();
            }
        });
    }

    private void showMaterialTwoStageRatingDialog1() {
        MaterialTwoStageRating dialog = MaterialTwoStageRating.with(this)
            .withIcon(true)
            .withInstallDays(5)
            .withEventsTimes(3)
            .withLaunchTimes(5)
            .resetOnDismiss(true)
            .resetOnFeedBackDeclined(true)
            .resetOnRatingDeclined(true)
            .withFeedbackDialogCallback(new FeedbackDialogCallback() {
                @Override
                public void onFeedbackReceived(final String feedback, final float rating) {
                    showToast(String.format(Locale.ENGLISH, "FeedbackDialogCallback-onFeedbackReceived(feedback=%s, rating=%f)", feedback, rating));
                }
                @Override
                public void onCancel() {
                    showToast("FeedbackDialogCallback-onCancel()");
                }
            })
            .withRatePromptDialogCallback(new RatePromptDialogCallback() {

                @Override
                public void onCancel() {
                    showToast("RatePromptDialogCallback-onCancel()");
                }
            })
            .withConfirmRateDialogCallback(new ConfirmRateDialogCallback() {
                @Override
                public void onCancel() {
                    showToast("ConfirmRateDialogCallback-onCancel()");
                }
            });
//        dialog.showIfMeetsConditions();
        dialog.showRatePromptDialog();
//        dialog.incrementEvent();
    }

    private void showMaterialTwoStageRatingDialog2() {
        MaterialTwoStageRating dialog = MaterialTwoStageRating.with(this)
                .withIcon(true)
            .withCustomIcon(R.drawable.gray_circle)
                .withInstallDays(5)
                .withEventsTimes(3)
                .withLaunchTimes(5)
                .resetOnDismiss(true)
                .resetOnFeedBackDeclined(true)
                .resetOnRatingDeclined(true)
                .withFeedbackDialogCallback(new FeedbackDialogCallback() {
                    @Override
                    public void onFeedbackReceived(final String feedback, final float rating) {
                        showToast(String.format(Locale.ENGLISH, "FeedbackDialogCallback-onFeedbackReceived(feedback=%s, rating=%f)", feedback, rating));
                    }

                    @Override
                    public void onCancel() {
                        showToast("FeedbackDialogCallback-onCancel()");
                    }
                })
                .withRatePromptDialogCallback(new RatePromptDialogCallback() {

                    @Override
                    public void onCancel() {
                        showToast("RatePromptDialogCallback-onCancel()");
                    }
                })
                .withConfirmRateDialogCallback(new ConfirmRateDialogCallback() {
                    @Override
                    public void onCancel() {
                        showToast("ConfirmRateDialogCallback-onCancel()");
                    }
                });
        dialog.showRatePromptDialog();

        //        /**
//         *  Provide your custom text on initial rate prompt dialog*/
//       materialTwoStageRating.with(this)
//               .withRatePromptTitle("INITIAL_TITLE")
//               .withRatePromptLaterText("LATER_TEXT")
//               .withRatePromptNeverText("NEVER_TEXT")
//               .withRatePromptDismissible(false);
//
//        /**
//         * provide custom text on the confirmation dialog*/
//
//         materialTwoStageRating.with(this)
//                 .withConfirmRateDialogTitle("CONFIRMATION_TITLE")
//                 .withConfirmRateDialogDescription("CONFIRMATION_DESCRITPION")
//                 .withConfirmRateDialogPositiveText("POSITIVE_BUTTON_TEXT")
//                 .withConfirmRateDialogNegativeText("NEGATIVE_BUTTON_TEXT")
//                 .withConfirmRateDialogDismissible(true);
//
//        /**
//         * provide custom text on feedback dialog*/
//        materialTwoStageRating.with(this)
//                .withFeedbackDialogTitle("FEEDBACK_TITLE")
//                .withFeedbackDialogDescription("FEEDBACK_DIALOG_DESCRIPTION")
//                .withFeedbackDialogPositiveText("POSITIVE_BUTTON_TEXT")
//                .withFeedbackDialogNegativeText("NEGATIVE_BUTTON_TEXT")
//                .withFeedbackDialogDismissible(false);
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
