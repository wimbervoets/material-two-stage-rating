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

import be.jatra.materialtwostagerating.DialogDismissedCallback;
import be.jatra.materialtwostagerating.FeedbackDialogCallback;
import be.jatra.materialtwostagerating.FeedbackInclRatingCallback;
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
        initTwoStage();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialTwoStageRating t = MaterialTwoStageRating.with(MainActivity.this);
                t.withFeedbackInclRatingCallback(new FeedbackInclRatingCallback() {
                    @Override
                    public void onFeedbackReceived(float rating, String feedback) {
                        Toast.makeText(MainActivity.this, String.format(Locale.ENGLISH, "FeedbackInclRatingCallback - onFeedbackReceived() - rating=%f; feedback=%s", rating, feedback), Toast.LENGTH_LONG).show();

                    }
                });
                t.withDialogDismissedCallback(new DialogDismissedCallback() {

                    @Override
                    public void onDialogDismissed() {
                        Toast.makeText(MainActivity.this, "DialogDismissedCallback - onDialogDismissed()", Toast.LENGTH_LONG).show();
                    }
                });
                t.withFeedbackDialogCallback(new FeedbackDialogCallback() {
                    @Override
                    public void onSubmit(final String feedback) {
                        Toast.makeText(MainActivity.this, String.format("FeedbackDialogCallback - onSubmit() - feedback=%s", feedback), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(MainActivity.this, "FeedbackDialogCallback - onCancel()", Toast.LENGTH_LONG).show();
                    }
                });
                t.showRatePromptDialog();
                //.incrementEvent();
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
        MaterialTwoStageRating materialTwoStageRating = MaterialTwoStageRating.with(this);
        materialTwoStageRating.withInstallDays(5).withEventsTimes(3).withLaunchTimes(5);
        materialTwoStageRating.resetOnDismiss(true).resetOnFeedBackDeclined(true).resetOnRatingDeclined(true);
        materialTwoStageRating.withAppIcon(true);
        materialTwoStageRating.showIfMeetsConditions();

        /**
         * To receive feedback only, use this listener
         */
        materialTwoStageRating.withFeedbackDialogCallback(new FeedbackDialogCallback() {

            @Override
            public void onSubmit(String feedback) {
                Toast.makeText(MainActivity.this, "onSubmit() - feedback=" + feedback, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, "onCancel()", Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * To receive rating along with with feedback, use this.
         */
        materialTwoStageRating.withFeedbackInclRatingCallback(new FeedbackInclRatingCallback() {
            @Override
            public void onFeedbackReceived(float rating, String feedback) {
                Toast.makeText(MainActivity.this, "FeedbackInclRatingCallback - onFeedbackReceived() - rating=" + rating + ", feedback=" + feedback, Toast.LENGTH_SHORT).show();
            }
        });


        /**
         *  Provide your custom text on initial rate prompt dialog*/

       //materialTwoStageRating.with(this).setRatePromptTitle("INITIAL_TITLE").
        //        setRatePromptLaterText("LATER_TEXT").setRatePromptNeverText("NEVER_TEXT").setRatePromptDismissible(false);


        /**
         * provide custom text on the confirmation dialog*/

        // materialTwoStageRating.with(this).setConfirmRateDialogTitle("CONFIRMATION_TITLE").setConfirmRateDialogDescription("CONFIRMATION_DESCRITPION").
        //        setConfirmRateDialogPositiveText("POSITIVE_BUTTON_TEXT").setConfirmRateDialogNegativeText("NEGATIVE_BUTTON_TEXT").setConfirmRateDialogDismissible(true);


        /**
         * provide custom text on feedback dialog*/

       // materialTwoStageRating.with(this).setFeedbackDialogTitle("FEEDBACK_TITLE").setFeedbackDialogDescription("FEEDBACK_DIALOG_DESCRIPTION").
         //       setFeedbackDialogPositiveText("POSITIVE_BUTTON_TEXT").setFeedbackDialogNegativeText("NEGATIVE_BUTTON_TEXT").setFeedbackDialogDismissible(false);


    }
}
