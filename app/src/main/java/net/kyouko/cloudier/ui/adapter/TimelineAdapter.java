package net.kyouko.cloudier.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import net.kyouko.cloudier.R;
import net.kyouko.cloudier.model.SourceTweet;
import net.kyouko.cloudier.model.TimelineEntry;
import net.kyouko.cloudier.model.Tweet;
import net.kyouko.cloudier.util.ImageUtil;
import net.kyouko.cloudier.util.TimeUtil;

import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A {@link RecyclerView.ViewHolder} for the list of tweets.
 *
 * @author beta
 */
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.BaseViewHolder> {


    private final static int TYPE_MESSAGE = 0;
    private final static int TYPE_TWEET = 1;
    private final static int TYPE_LOAD_MORE = 2;

    private Context context;
    private List<TimelineEntry> entries;
    private LinkedHashMap<String, String> userList;
    private ImageUtil imageUtil;


    public TimelineAdapter(Context context, List<TimelineEntry> entries, LinkedHashMap<String, String> userList) {
        this.context = context;
        this.entries = entries;
        this.userList = userList;

        imageUtil = ImageUtil.getInstance(context);
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_TWEET:
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_tweet, parent, false);
                return new TweetViewHolder(view);

            case TYPE_LOAD_MORE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_tweet, parent, false);
                return new TweetViewHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_TWEET:
                bindTweetCardHolder((TweetViewHolder) holder, (Tweet) entries.get(position));
                break;

            case TYPE_LOAD_MORE:
                bindLoadMoreViewHolder((LoadMoreViewHolder) holder);
                break;
        }
    }


    private void bindTweetCardHolder(TweetViewHolder holder, Tweet tweet) {
        holder.imageAvatar.setImageURI(Uri.parse(
                imageUtil.parseImageUrl(tweet.user.avatarUrl)
        ));
        holder.textNickName.setText(tweet.user.nickName);
        holder.textTime.setText(TimeUtil.getDescription(context,
                TimeUtil.convertTimestampToCalendar(tweet.timestamp)));
        if (tweet.content.length() == 0) {
            holder.textContent.setVisibility(View.GONE);
        } else {
            holder.textContent.setVisibility(View.VISIBLE);
            holder.textContent.setText(replaceUsernameWithNicknameInContent(tweet.content));
        }

        holder.buttonRetweet.setText(String.valueOf(tweet.retweetedCount));
        holder.buttonComment.setText(String.valueOf(tweet.commentedCount));

        if (tweet.hasSourceTweet) {
            showSourceTweet(holder, tweet.sourceTweet);
        } else {
            hideSourceTweet(holder);
        }

        boolean hasImages = false;
        List<String> imageUrls = null;
        if (tweet.images.size() > 0) {
            hasImages = true;
            imageUrls = tweet.images;
        } else if (tweet.hasSourceTweet && tweet.sourceTweet.images.size() > 0) {
            hasImages = true;
            imageUrls = tweet.sourceTweet.images;
        }
        if (hasImages) {
            if (imageUrls.size() == 1) {
                loadSingleImage(holder, imageUrls.get(0));
                hideMultipleImages(holder);
            } else {
                loadMultipleImages(holder, imageUrls);
                hideSingleImage(holder);
            }
        } else {
            hideAllImages(holder);
        }
    }


    private void bindLoadMoreViewHolder(LoadMoreViewHolder holder) {
        // TODO: add a "load more" button
    }


    @Override
    public int getItemViewType(int position) {
        TimelineEntry entry = entries.get(position);
        if (entry instanceof Tweet) {
            return TYPE_TWEET;
        } else {
            return TYPE_TWEET;
        }
    }


    @Override
    public int getItemCount() {
        return entries.size();
    }


    private SpannableStringBuilder replaceUsernameWithNicknameInContent(String originalContent) {
        SpannableStringBuilder builder = new SpannableStringBuilder(originalContent);

        for (LinkedHashMap.Entry<String, String> entry : userList.entrySet()) {
            // content = content.replaceAll("@" + entry.getKey(), entry.getValue());
            int start = builder.toString().indexOf("@" + entry.getKey());
            if (start >= 0) {
                int end = start + entry.getKey().length() + 1;
                builder.replace(start, end, entry.getValue());
                int spanStart = start;
                int spanEnd = spanStart + entry.getValue().length();
                builder.setSpan(new StyleSpan(Typeface.BOLD), spanStart, spanEnd, 0);
            }
        }

        return builder;
    }


    private void showSourceTweet(TweetViewHolder holder, SourceTweet sourceTweet) {
        View viewSourceTweet;

        if (holder.itemView.findViewById(R.id.view_source_tweet) != null) {
            viewSourceTweet = holder.itemView.findViewById(R.id.view_source_tweet);
            viewSourceTweet.setVisibility(View.VISIBLE);
        } else {
            viewSourceTweet = holder.stubSourceTweet.inflate();
        }

        TextView textSourceTweetNickname = (TextView) viewSourceTweet.findViewById(R.id.text_source_nickname);
        TextView textSourceTweetTime = (TextView) viewSourceTweet.findViewById(R.id.text_source_time);
        TextView textSourceTweetContent = (TextView) viewSourceTweet.findViewById(R.id.text_source_content);

        textSourceTweetNickname.setText(sourceTweet.user.nickName);
        textSourceTweetTime.setText(TimeUtil.getDescription(context,
                TimeUtil.convertTimestampToCalendar(sourceTweet.timestamp)));
        if (sourceTweet.content.length() == 0) {
            textSourceTweetContent.setVisibility(View.GONE);
        } else {
            textSourceTweetContent.setVisibility(View.VISIBLE);
            textSourceTweetContent.setText(replaceUsernameWithNicknameInContent(sourceTweet.content));
        }
    }


    private void hideSourceTweet(TweetViewHolder holder) {
        if (holder.itemView.findViewById(R.id.view_source_tweet) != null) {
            holder.itemView.findViewById(R.id.view_source_tweet).setVisibility(View.GONE);
        }
    }


    private void loadSingleImage(TweetViewHolder holder, String imageUrl) {
        View viewImage;

        if (holder.itemView.findViewById(R.id.view_single_image) != null) {
            viewImage = holder.itemView.findViewById(R.id.view_single_image);
            viewImage.setVisibility(View.VISIBLE);
        } else {
            viewImage = holder.stubSingleImage.inflate();
        }

        ((SimpleDraweeView) viewImage).setImageURI(Uri.parse(ImageUtil.getInstance(context).parseImageUrl(imageUrl)));
    }


    private void loadMultipleImages(TweetViewHolder holder, List<String> imageUrls) {
        View viewImage;

        if (holder.itemView.findViewById(R.id.view_images) != null) {
            viewImage = holder.itemView.findViewById(R.id.view_images);
            viewImage.setVisibility(View.VISIBLE);
        } else {
            viewImage = holder.stubImage.inflate();
        }

        LinearLayout layoutImagesList = (LinearLayout) viewImage.findViewById(R.id.layout_image_list);
        layoutImagesList.removeAllViewsInLayout();
        for (String imageUrl : imageUrls) {
            SimpleDraweeView image = (SimpleDraweeView) LayoutInflater.from(context).inflate(R.layout.template_tweet_image, layoutImagesList, false);
            image.setImageURI(Uri.parse(ImageUtil.getInstance(context).parseImageUrl(imageUrl)));
            layoutImagesList.addView(image);
        }
    }


    private void hideSingleImage(TweetViewHolder holder) {
        if (holder.itemView.findViewById(R.id.view_single_image) != null) {
            holder.itemView.findViewById(R.id.view_single_image).setVisibility(View.GONE);
        }
    }


    private void hideMultipleImages(TweetViewHolder holder) {
        if (holder.itemView.findViewById(R.id.view_images) != null) {
            holder.itemView.findViewById(R.id.view_images).setVisibility(View.GONE);
        }
    }


    private void hideAllImages(TweetViewHolder holder) {
        hideSingleImage(holder);
        hideMultipleImages(holder);
    }


    /**
     * Base class for real holders of the recycler view.
     */
    public static abstract class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }


    /**
     * View holder for cards of tweets.
     */
    public static class TweetViewHolder extends BaseViewHolder {

        @Bind(R.id.image_avatar)
        SimpleDraweeView imageAvatar;
        @Bind(R.id.text_nickname)
        TextView textNickName;
        @Bind(R.id.text_time)
        TextView textTime;
        @Bind(R.id.stub_single_image)
        ViewStub stubSingleImage;
        @Bind(R.id.stub_images)
        ViewStub stubImage;
        @Bind(R.id.text_content)
        TextView textContent;
        @Bind(R.id.stub_source)
        ViewStub stubSourceTweet;
        @Bind(R.id.button_retweet)
        Button buttonRetweet;
        @Bind(R.id.button_comment)
        Button buttonComment;


        public TweetViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

    }


    public static class LoadMoreViewHolder extends BaseViewHolder {
        public LoadMoreViewHolder(View itemView) {
            super(itemView);
        }
    }

}
