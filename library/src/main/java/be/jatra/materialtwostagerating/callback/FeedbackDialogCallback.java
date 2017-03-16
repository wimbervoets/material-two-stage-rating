package be.jatra.materialtwostagerating.callback;

public interface FeedbackDialogCallback extends DialogCallback {

    void onFeedbackReceived(String feedback, float rating);

    void onCancel();
}
