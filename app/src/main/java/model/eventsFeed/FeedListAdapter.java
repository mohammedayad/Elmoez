package model.eventsFeed;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import eu.kudan.kudansamples.R;
import http_utilities.AppController;
import http_utilities.HttpHandler;
import model.pojos.Feeds;


public class FeedListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<Feeds> feedItems;
	private ImageLoader imageLoader ;


	public FeedListAdapter(Activity activity, List<Feeds> feedItems) {
		this.activity = activity;
		this.feedItems = feedItems;
		imageLoader = AppController.getInstance(activity).getImageLoader();
	}

	@Override
	public int getCount() {
		return feedItems.size();
	}

	@Override
	public Object getItem(int location) {
		return feedItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.feed_item, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance(activity).getImageLoader();

		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView timestamp = (TextView) convertView
				.findViewById(R.id.timestamp);
		TextView statusMsg = (TextView) convertView
				.findViewById(R.id.txtStatusMsg);
		//TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
		NetworkImageView profilePic = (NetworkImageView) convertView
				.findViewById(R.id.profilePic);
		FeedImageView feedImageView = (FeedImageView) convertView
				.findViewById(R.id.feedImage1);

		Feeds item = feedItems.get(position);

		name.setText(item.getUserName());

		 //Converting timestamp into x ago format
//		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
//				Long.parseLong(item.getFeedTime().toString()),
//				System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
		timestamp.setText(item.getFeedTime());
//		Log.e("tim***********", item.getFeedTime());

		// Chcek for empty status message
		if (!TextUtils.isEmpty(item.getFeed())) {
			statusMsg.setText(item.getFeed());
			statusMsg.setVisibility(View.VISIBLE);
		} else {
			// status is empty, remove from view
			statusMsg.setVisibility(View.GONE);
		}

		// Checking for null feed url
//		if (item.getUrl() != null) {
//			url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
//					+ item.getUrl() + "</a> "));
//
//			// Making url clickable
//			url.setMovementMethod(LinkMovementMethod.getInstance());
//			url.setVisibility(View.VISIBLE);
//		} else {
//			// url is null, remove from the view
//			url.setVisibility(View.GONE);
//		}

		// user profile pic
		profilePic.setImageUrl(HttpHandler.userImagesUrl+item.getUserImage(), imageLoader);

		// Feed image
		if (item.getFeedImage() != null) {
			feedImageView.setImageUrl(HttpHandler.userImagesUrl+item.getFeedImage(), imageLoader);
			feedImageView.setVisibility(View.VISIBLE);
			feedImageView
					.setResponseObserver(new FeedImageView.ResponseObserver() {
						@Override
						public void onError() {
						}

						@Override
						public void onSuccess() {
						}
					});
		} else {
//			feedImageView.setVisibility(View.GONE);
			if (item.getOfflineImage()!=null) {
//				feedImageView.setImageResource(R.drawable.splash);
				feedImageView.setImageBitmap(item.getOfflineImage());
				feedImageView.setVisibility(View.VISIBLE);
			}else{
				feedImageView.setVisibility(View.GONE);

			}
		}

		return convertView;
	}

}
