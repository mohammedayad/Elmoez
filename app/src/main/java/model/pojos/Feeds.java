package model.pojos;

import android.graphics.Bitmap;

/**
 * Created by sh on 6/12/2016.
 */
public class Feeds {
    private Integer feedsId;
    private String userName;
    private String userImage;
    private String feed;
    private String feedImage;
    private String feedTime;
    private int likeFeed;
    private Bitmap offlineImage;

    public Feeds() {

    }

    public Feeds(Integer feedsId, String userName, String userImage, String feed, String feedImage, String feedTime, int likeFeed) {
        this.feedsId = feedsId;
        this.userName = userName;
        this.userImage = userImage;
        this.feed = feed;
        this.feedImage = feedImage;
        this.feedTime = feedTime;
        this.likeFeed = likeFeed;
    }

    public Integer getFeedsId() {
        return feedsId;
    }

    public void setFeedsId(Integer feedsId) {
        this.feedsId = feedsId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getFeed() {
        return feed;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }

    public String getFeedImage() {
        return feedImage;
    }

    public void setFeedImage(String feedImage) {
        this.feedImage = feedImage;
    }

    public String getFeedTime() {
        return feedTime;
    }

    public void setFeedTime(String feedTime) {
        this.feedTime = feedTime;
    }

    public int getLikeFeed() {
        return likeFeed;
    }

    public void setLikeFeed(int likeFeed) {
        this.likeFeed = likeFeed;
    }

    public Bitmap getOfflineImage() {
        return offlineImage;
    }

    public void setOfflineImage(Bitmap offlineImage) {
        this.offlineImage = offlineImage;
    }
}