package net.kyouko.cloudier.ui.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import net.kyouko.cloudier.R;
import net.kyouko.cloudier.model.SourceTweet;
import net.kyouko.cloudier.model.TimelineEntry;
import net.kyouko.cloudier.model.Tweet;
import net.kyouko.cloudier.util.ImageUtil;
import net.kyouko.cloudier.util.TextUtil;
import net.kyouko.cloudier.util.TimeUtil;

import java.util.ArrayList;
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

    public interface OnLoadMoreTweetsListener {
        void onLoadMoreTweets();
    }


    public interface OnRetweetListener {
        void onRetweet(Tweet tweet);
    }


    public interface OnCommentListener {
        void onComment(Tweet tweet);
    }


    public interface OnShowTweetImagesListener {
        void onShowSingleImage(String imageUrl);

        void onShowImages(ArrayList<String> imageUrls, int index);
    }


    private final static int TYPE_TWEET = 0;
    private final static int TYPE_LOAD_MORE = 1;

    private Context context;
    private List<TimelineEntry> entries;
    private LinkedHashMap<String, String> userList;
    private ImageUtil imageUtil;

    private OnLoadMoreTweetsListener onLoadMoreTweetsListener;
    private LoadMoreViewHolder loadMoreViewHolder;

    private OnRetweetListener onRetweetListener;
    private OnCommentListener onCommentListener;
    private OnShowTweetImagesListener onShowTweetImagesListener;

    private int shortAnimationDuration;


    public TimelineAdapter(Context context, List<TimelineEntry> entries, LinkedHashMap<String, String> userList) {
        this.context = context;
        this.entries = entries;
        this.userList = userList;

        imageUtil = ImageUtil.getInstance(context);

        shortAnimationDuration = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
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
                        .inflate(R.layout.view_load_more, parent, false);
                return new LoadMoreViewHolder(view);
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
        showTweetContent(holder, tweet);

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
            showTweetImages(holder, imageUrls);
        } else {
            hideAllImages(holder);
        }
    }


    private void bindLoadMoreViewHolder(final LoadMoreViewHolder holder) {
        loadMoreViewHolder = holder;

        holder.buttonLoadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMoreTweets(holder);
            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        TimelineEntry entry = entries.get(position);
        if (entry instanceof Tweet) {
            return TYPE_TWEET;
        } else {
            return TYPE_LOAD_MORE;
        }
    }


    @Override
    public int getItemCount() {
        return entries.size();
    }


    private SpannableStringBuilder replaceUsernameWithNicknameInContent(String originalContent) {
        SpannableStringBuilder builder = new SpannableStringBuilder(originalContent);
        return replaceUsernameWithNicknameInContent(builder);
    }


    private SpannableStringBuilder replaceUsernameWithNicknameInContent(SpannableStringBuilder originalContent) {
        for (LinkedHashMap.Entry<String, String> entry : userList.entrySet()) {
            int lastPosition = 0;
            int start = originalContent.toString().indexOf("@" + entry.getKey(), lastPosition);
            while (start >= 0) {
                int end = start + entry.getKey().length() + 1;
                originalContent.replace(start, end, entry.getValue());

                int spanStart = start;
                int spanEnd = spanStart + entry.getValue().length();
                originalContent.setSpan(new StyleSpan(Typeface.BOLD), spanStart, spanEnd, 0);

                lastPosition = spanEnd;
                start = originalContent.toString().indexOf("@" + entry.getKey(), lastPosition);
            }
        }

        return originalContent;
    }


    private void showTweetContent(TweetViewHolder holder, final Tweet tweet) {
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
            holder.textContent.setText(
                    replaceUsernameWithNicknameInContent(
                            TextUtil.addLinkToTopicsInText(context,
                                    TextUtil.addLinkToUrlsInText(context, tweet.originalContent, false),
                                    false)
                    )
            );
            holder.textContent.setMovementMethod(new LinkMovementMethod());
        }

        holder.buttonRetweet.setText(String.valueOf(tweet.retweetedCount));
        holder.buttonRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRetweetListener != null) {
                    onRetweetListener.onRetweet(tweet);
                }
            }
        });

        holder.buttonComment.setText(String.valueOf(tweet.commentedCount));
        holder.buttonComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCommentListener != null) {
                    onCommentListener.onComment(tweet);
                }
            }
        });
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
            textSourceTweetContent.setText(replaceUsernameWithNicknameInContent(
                    replaceUsernameWithNicknameInContent(
                            TextUtil.addLinkToTopicsInText(context,
                                    TextUtil.addLinkToUrlsInText(context, sourceTweet.originalContent, false),
                                    false)
                    )
            ));
            textSourceTweetContent.setMovementMethod(new LinkMovementMethod());
        }
    }


    private void hideSourceTweet(TweetViewHolder holder) {
        if (holder.itemView.findViewById(R.id.view_source_tweet) != null) {
            holder.itemView.findViewById(R.id.view_source_tweet).setVisibility(View.GONE);
        }
    }


    private void showTweetImages(TweetViewHolder holder, List<String> imageUrls) {
        if (imageUrls.size() == 1) {
            loadSingleImage(holder, imageUrls.get(0));
            hideMultipleImages(holder);
        } else {
            loadMultipleImages(holder, imageUrls);
            hideSingleImage(holder);
        }
    }


    private void loadSingleImage(TweetViewHolder holder, final String imageUrl) {
        View viewImage;

        if (holder.itemView.findViewById(R.id.view_single_image) != null) {
            viewImage = holder.itemView.findViewById(R.id.view_single_image);
            viewImage.setVisibility(View.VISIBLE);
        } else {
            viewImage = holder.stubSingleImage.inflate();
        }

        ((SimpleDraweeView) viewImage).setImageURI(Uri.parse(ImageUtil.getInstance(context).parseImageUrl(imageUrl)));
        viewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onShowTweetImagesListener != null) {
                    onShowTweetImagesListener.onShowSingleImage(imageUrl);
                }
            }
        });
    }


    private void loadMultipleImages(TweetViewHolder holder, final List<String> imageUrls) {
        View viewImage;

        if (holder.itemView.findViewById(R.id.view_images) != null) {
            viewImage = holder.itemView.findViewById(R.id.view_images);
            viewImage.setVisibility(View.VISIBLE);
        } else {
            viewImage = holder.stubImage.inflate();
        }

        LinearLayout layoutImagesList = (LinearLayout) viewImage.findViewById(R.id.layout_image_list);
        layoutImagesList.removeAllViewsInLayout();
        for (int i = 0; i < imageUrls.size(); i += 1) {
            final int index = i;
            SimpleDraweeView image = (SimpleDraweeView) LayoutInflater.from(context).inflate(R.layout.template_tweet_image, layoutImagesList, false);
            image.setImageURI(Uri.parse(ImageUtil.getInstance(context).parseImageUrl(imageUrls.get(i))));
            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onShowTweetImagesListener != null) {
                        ArrayList<String> urls = new ArrayList<String>(imageUrls);
                        onShowTweetImagesListener.onShowImages(urls, index);
                    }
                }
            });
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


    private void loadMoreTweets(final LoadMoreViewHolder holder) {
        if (onLoadMoreTweetsListener != null) {
            holder.buttonLoadMore.setClickable(false);

            holder.progressBar.setAlpha(0f);
            holder.progressBar.setVisibility(View.VISIBLE);

            holder.progressBar.animate()
                    .alpha(1f)
                    .setDuration(shortAnimationDuration)
                    .setListener(null);

            holder.buttonLoadMore.animate()
                    .alpha(0f)
                    .setDuration(shortAnimationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            holder.buttonLoadMore.setVisibility(View.GONE);
                            holder.buttonLoadMore.setClickable(true);

                            onLoadMoreTweetsListener.onLoadMoreTweets();
                        }
                    });
        }
    }


    public void completeLoadingMore() {
        if (loadMoreViewHolder != null) {
            loadMoreViewHolder.progressBar.setVisibility(View.GONE);

            loadMoreViewHolder.buttonLoadMore.setAlpha(1f);
            loadMoreViewHolder.buttonLoadMore.setVisibility(View.VISIBLE);
        }
    }


    public void setOnLoadMoreTweetsListener(OnLoadMoreTweetsListener listener) {
        onLoadMoreTweetsListener = listener;
    }


    public void setOnRetweetListener(OnRetweetListener listener) {
        onRetweetListener = listener;
    }


    public void setOnCommentListener(OnCommentListener listener) {
        onCommentListener = listener;
    }


    public void setOnShowTweetImagesListener(OnShowTweetImagesListener listener) {
        onShowTweetImagesListener = listener;
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


    /**
     * View holder for views of loading more.
     */
    public static class LoadMoreViewHolder extends BaseViewHolder {

        @Bind(R.id.button_load_more)
        View buttonLoadMore;
        @Bind(R.id.progress_load_more)
        ProgressBar progressBar;


        public LoadMoreViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

    }

}
