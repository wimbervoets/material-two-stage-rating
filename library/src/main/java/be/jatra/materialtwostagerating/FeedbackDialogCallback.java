package be.jatra.materialtwostagerating;

public interface FeedbackDialogCallback {

    void onSubmit(String feedback);

    void onCancel();
}
