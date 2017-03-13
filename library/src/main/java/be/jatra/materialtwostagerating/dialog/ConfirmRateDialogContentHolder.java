package be.jatra.materialtwostagerating.dialog;

public final class ConfirmRateDialogContentHolder {

    private String confirmRateTitle; // = "Thank you!";
    private String confirmRateText; // = "Would you like to post your review on app store. This will help and motivate us a lot.";
    private String confirmRateNegativeText; // = "No Thanks!";
    private String confirmRatePositiveText; // = "Sure";
    private boolean dismissible; // = true;

    public final String getConfirmRateTitle() {
        return confirmRateTitle;
    }

    public final void setConfirmRateTitle(final String confirmRateTitle) {
        this.confirmRateTitle = confirmRateTitle;
    }

    public final String getConfirmRateText() {
        return confirmRateText;
    }

    public final void setConfirmRateText(final String confirmRateText) {
        this.confirmRateText = confirmRateText;
    }

    public final String getConfirmRateNegativeText() {
        return confirmRateNegativeText;
    }

    public final void setConfirmRateNegativeText(final String confirmRateNegativeText) {
        this.confirmRateNegativeText = confirmRateNegativeText;
    }

    public final String getConfirmRatePositiveText() {
        return confirmRatePositiveText;
    }

    public final void setConfirmRatePositiveText(final String confirmRatePositiveText) {
        this.confirmRatePositiveText = confirmRatePositiveText;
    }

    public final boolean isDismissible() {
        return dismissible;
    }

    public final void setDismissible(final boolean dismissible) {
        this.dismissible = dismissible;
    }
}
