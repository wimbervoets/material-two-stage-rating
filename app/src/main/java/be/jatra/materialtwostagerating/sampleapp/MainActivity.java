package be.jatra.materialtwostagerating.sampleapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initTwoStage();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(MainActivity.this, String.format("The settings menu was clicked."), Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initTwoStage() {
        MaterialTwoStageRating dialog = MaterialTwoStageRating.with(this)
            .withInstallDays(5)
            .withEventsTimes(3)
            .withLaunchTimes(5)
            .resetOnDismiss(true)
            .resetOnFeedBackDeclined(true)
            .resetOnRatingDeclined(true)
            .withIcon(true)
//            .withCustomIcon(R.drawable.gray_circle)
            .withFeedbackDialogCallback(new FeedbackDialogCallback() {
                @Override
                public void onFeedbackReceived(final String feedback, final float rating) {
                    Toast.makeText(MainActivity.this, String.format(Locale.ENGLISH, "FeedbackDialogCallback-onFeedbackReceived(feedback=%s, rating=%f)", feedback, rating), Toast.LENGTH_LONG).show();
                }
                @Override
                public void onCancel() {
                    Toast.makeText(MainActivity.this, "FeedbackDialogCallback-onCancel()", Toast.LENGTH_SHORT).show();
                }
            })
            .withRatePromptDialogCallback(new RatePromptDialogCallback() {

                @Override
                public void onCancel() {
                    Toast.makeText(MainActivity.this, "RatePromptDialogCallback-onCancel()", Toast.LENGTH_LONG).show();
                }
            })
            .withConfirmRateDialogCallback(new ConfirmRateDialogCallback() {
                @Override
                public void onCancel() {
                    Toast.makeText(MainActivity.this, "ConfirmRateDialogCallback-onCancel()", Toast.LENGTH_LONG).show();
                }
            });
//        dialog.showIfMeetsConditions();
        dialog.showRatePromptDialog();
//        dialog.incrementEvent();

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
}
