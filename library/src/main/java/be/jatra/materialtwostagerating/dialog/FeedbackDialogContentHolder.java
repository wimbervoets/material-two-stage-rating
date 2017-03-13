package be.jatra.materialtwostagerating.dialog;

public final class FeedbackDialogContentHolder {

    private String feedbackPromptTitle; // = "We're Really Sorry";
    private String feedbackPromptText; // = "Could you tell us what problem you faced. This will help us improve.";
    private String feedbackPromptPositiveText; // = "Submit";
    private String feedbackPromptNegativeText; // = "No Thanks!";
    private boolean dismissible; // = true;

    public final String getFeedbackPromptTitle() {
        return feedbackPromptTitle;
    }

    public final void setFeedbackPromptTitle(final String feedbackPromptTitle) {
        this.feedbackPromptTitle = feedbackPromptTitle;
    }

    public final String getFeedbackPromptText() {
        return feedbackPromptText;
    }

    public final void setFeedbackPromptText(final String feedbackPromptText) {
        this.feedbackPromptText = feedbackPromptText;
    }

    public final String getFeedbackPromptPositiveText() {
        return feedbackPromptPositiveText;
    }

    public final void setFeedbackPromptPositiveText(final String feedbackPromptPositiveText) {
        this.feedbackPromptPositiveText = feedbackPromptPositiveText;
    }

    public final String getFeedbackPromptNegativeText() {
        return feedbackPromptNegativeText;
    }

    public final void setFeedbackPromptNegativeText(final String feedbackPromptNegativeText) {
        this.feedbackPromptNegativeText = feedbackPromptNegativeText;
    }

    public final boolean isDismissible() {
        return dismissible;
    }

    public final void setDismissible(final boolean dismissible) {
        this.dismissible = dismissible;
    }
}
