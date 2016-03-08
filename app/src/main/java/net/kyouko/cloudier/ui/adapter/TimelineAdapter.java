package net.kyouko.cloudier.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import net.kyouko.cloudier.R;
import net.kyouko.cloudier.model.AppMessage;
import net.kyouko.cloudier.model.TimelineEntry;
import net.kyouko.cloudier.model.Tweet;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A {@link RecyclerView.ViewHolder} for the list of tweets.
 *
 * @author beta
 */
public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.BaseCardHolder> {

    private final static int TYPE_MESSAGE = 0;
    private final static int TYPE_TWEET = 1;
    private final static int TYPE_LOAD_MORE = 2;

    private List<TimelineEntry> entries;


    public TimelineAdapter(List<TimelineEntry> entries) {
        this.entries = entries;
    }


    @Override
    public BaseCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case TYPE_MESSAGE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_tweet, parent, false);
                return new MessageCardHolder(view);

            case TYPE_TWEET:
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_tweet, parent, false);
                return new TweetCardHolder(view);

            case TYPE_LOAD_MORE:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.card_tweet, parent, false);
                return new TweetCardHolder(view);
        }
    }


    @Override
    public void onBindViewHolder(BaseCardHolder holder, int position) {
        //
    }


    private void bindMessageCardHolder(MessageCardHolder holder, AppMessage message) {
        //
    }


    private void bindTweetCardHolder(TweetCardHolder holder, Tweet tweet) {
        //
    }


    //private void bindLoadMoreViewHolder()


    @Override
    public int getItemViewType(int position) {
        TimelineEntry entry = entries.get(position);
        if (entry instanceof AppMessage) {
            return TYPE_MESSAGE;
        } else if (entry instanceof Tweet) {
            return TYPE_TWEET;
        } else {
            return TYPE_TWEET;
        }
    }


    @Override
    public int getItemCount() {
        return entries.size();
    }


    /**
     * Base class for real holders of the recycler view.
     */
    public static abstract class BaseCardHolder extends RecyclerView.ViewHolder {
        public BaseCardHolder(View itemView) {
            super(itemView);
        }
    }


    /**
     * View holder for cards of app messages.
     */
    public static class MessageCardHolder extends BaseCardHolder {
        public MessageCardHolder(View itemView) {
            super(itemView);
        }
    }


    /**
     * View holder for cards of tweets.
     */
    public static class TweetCardHolder extends BaseCardHolder {

        @Bind(R.id.image_avatar)
        SimpleDraweeView imageAvatar;
        @Bind(R.id.text_nick_name)
        TextView textNickName;
        @Bind(R.id.text_username)
        TextView textUsername;
        @Bind(R.id.stub_image)
        ViewStub stubImage;
        @Bind(R.id.text_content)
        TextView textContent;
        @Bind(R.id.button_retweet)
        ImageButton buttonRetweet;
        @Bind(R.id.button_comment)
        ImageButton buttonComment;
        @Bind(R.id.button_favorite)
        ImageButton buttonFavorite;
        @Bind(R.id.button_menu)
        ImageButton buttonMenu;


        public TweetCardHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

}
