package be.jatra.materialtwostagerating;

public interface FeedbackWithRatingReceivedListener {

    void onFeedbackReceived(float rating, String feedback);
}