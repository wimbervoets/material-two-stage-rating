package be.jatra.materialtwostagerating.dialog;

import android.support.annotation.DrawableRes;

public final class RatePromptDialogContentHolder {

    private @DrawableRes int icon;
    private String ratePromptTitle;
    private boolean dismissible = true;

    public int getIcon() {
        return icon;
    }

    public void setIcon(@DrawableRes final int icon) {
        this.icon = icon;
    }

    public final String getRatePromptTitle() {
        return ratePromptTitle;
    }

    public final void setRatePromptTitle(final String ratePromptTitle) {
        this.ratePromptTitle = ratePromptTitle;
    }

    public final boolean isDismissible() {
        return dismissible;
    }

    public final void setDismissible(final boolean dismissible) {
        this.dismissible = dismissible;
    }
}
