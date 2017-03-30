package be.jatra.materialtwostagerating.sampleapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
            .withFeedbackDialogTitle("Feedback Title (customized)")
            .withFeedbackDialogDescription("Feedback Description (customized)")
            .withFeedbackDialogPositiveText("OK (customized)")
            .withFeedbackDialogNegativeText("Cancel (customized)")
            .withFeedbackDialogDismissible(false)
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
            .withRatePromptTitle("Rate Prompt Title (customized)")
            .withRatePromptDismissible(false)
            .withRatePromptDialogCallback(new RatePromptDialogCallback() {
                @Override
                public void onCancel() {
                    showToast("RatePromptDialogCallback-onCancel()");
                }
            })
            .withConfirmRateDialogTitle("Confirmation Title (customized)")
            .withConfirmRateDialogDescription("Confirmation Description (customized)")
            .withConfirmRateDialogPositiveText("OK (customized)")
            .withConfirmRateDialogNegativeText("Cancel (customized)")
            .withConfirmRateDialogDismissible(true)
            .withConfirmRateDialogCallback(new ConfirmRateDialogCallback() {
                @Override
                public void onCancel() {
                    showToast("ConfirmRateDialogCallback-onCancel()");
                }
            });
        dialog.showRatePromptDialog();
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
