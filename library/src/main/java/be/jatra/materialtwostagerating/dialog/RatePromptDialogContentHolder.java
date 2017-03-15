package be.jatra.materialtwostagerating.dialog;

public final class RatePromptDialogContentHolder {

    private String ratePromptTitle;
    private String ratePromptLaterText;
    private String ratePromptNeverText;
    private boolean dismissible = true;

    public final String getRatePromptTitle() {
        return ratePromptTitle;
    }

    public final void setRatePromptTitle(final String ratePromptTitle) {
        this.ratePromptTitle = ratePromptTitle;
    }

    public final String getRatePromptLaterText() {
        return ratePromptLaterText;
    }

    public final void setRatePromptLaterText(final String ratePromptLaterText) {
        this.ratePromptLaterText = ratePromptLaterText;
    }

    public final String getRatePromptNeverText() {
        return ratePromptNeverText;
    }

    public final void setRatePromptNeverText(final String ratePromptNeverText) {
        this.ratePromptNeverText = ratePromptNeverText;
    }

    public final boolean isDismissible() {
        return dismissible;
    }

    public final void setDismissible(final boolean dismissible) {
        this.dismissible = dismissible;
    }
}
