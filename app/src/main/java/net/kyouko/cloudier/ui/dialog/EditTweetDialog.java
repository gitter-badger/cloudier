package net.kyouko.cloudier.ui.dialog;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import net.kyouko.cloudier.R;
import net.kyouko.cloudier.model.Account;
import net.kyouko.cloudier.model.SourceTweet;
import net.kyouko.cloudier.model.Tweet;
import net.kyouko.cloudier.util.AccountUtil;
import net.kyouko.cloudier.util.ImageUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Dialog for editing a tweet.
 *
 * @author beta
 */
public class EditTweetDialog extends DialogFragment {

    public interface OnEditFinishedListener {
        void onEditFinished(String content);
    }


    public final static String ARG_IS_RETWEET_OR_COMMENT = "IS_RETWEET_OR_COMMENT";
    public final static String ARG_SOURCE_TWEET = "SOURCE_TWEET";


    @Bind(R.id.image_avatar)
    SimpleDraweeView imageAvatar;
    @Bind(R.id.text_nickname)
    TextView textNickname;
    @Bind(R.id.text_username)
    TextView textUsername;
    @Bind(R.id.button_send)
    ImageButton buttonSend;
    @Bind(R.id.stub_source_tweet)
    ViewStub stubSourceTweet;
    @Bind(R.id.edit_content)
    EditText editContent;
    @Bind(R.id.button_add_image)
    ImageButton buttonAddImage;
    @Bind(R.id.button_add_person)
    ImageButton buttonAddPerson;
    @Bind(R.id.text_word_count)
    TextView textWordCount;


    private Account currentAccount;
    private int wordCountAvailable = 140;
    private OnEditFinishedListener onEditFinishedListener;

    private boolean isRetweetOrComment = false;
    private Tweet sourceTweet;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View dialogView = inflater.inflate(R.layout.dialog_edit_tweet, container);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().gravity = Gravity.FILL_HORIZONTAL;

        currentAccount = AccountUtil.readAccount(getActivity());

        ButterKnife.bind(this, dialogView);

        imageAvatar.setImageURI(Uri.parse(
                ImageUtil.getInstance(getActivity()).parseImageUrl(currentAccount.avatarUrl)
        ));
        textNickname.setText(currentAccount.nickname);
        String username = "@" + currentAccount.username;
        textUsername.setText(username);

        editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Ignored
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                wordCountAvailable = 140 - charSequence.length();
                textWordCount.setText(String.valueOf(wordCountAvailable));
                if (wordCountAvailable < 0) {
                    textWordCount.setTextColor(getResources().getColor(R.color.red_500));
                } else {
                    textWordCount.setTextColor(getResources().getColor(R.color.black_87alpha));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Ignored
            }
        });

        textWordCount.setText(String.valueOf(wordCountAvailable));

        // Source tweet
        if (getArguments() != null) {
            isRetweetOrComment = getArguments().getBoolean(ARG_IS_RETWEET_OR_COMMENT, false);
            if (isRetweetOrComment) {
                sourceTweet = (Tweet) getArguments().getSerializable(ARG_SOURCE_TWEET);

                View viewSourceTweet = stubSourceTweet.inflate();
                TextView textSourceTweetNickname = (TextView) viewSourceTweet.findViewById(R.id.text_source_nickname);
                TextView textSourceTweetContent = (TextView) viewSourceTweet.findViewById(R.id.text_source_content);

                String sourceTweetNickname = sourceTweet.user.nickName + getString(R.string.text_symbol_colon);
                textSourceTweetNickname.setText(sourceTweetNickname);

                textSourceTweetContent.setText(sourceTweet.originalContent); // TODO: replace original content with parsed content

                if (sourceTweet.type == Tweet.TYPE_RETWEET || sourceTweet.type == Tweet.TYPE_COMMENT || sourceTweet.type == Tweet.TYPE_REPLY) {
                    editContent.setText(getString(R.string.text_pattern_original_tweet_content, sourceTweet.user.username, sourceTweet.originalContent));
                }

                buttonAddImage.setVisibility(View.GONE);
            }
        }

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onEditFinishedListener != null) {
                    onEditFinishedListener.onEditFinished(editContent.getText().toString());
                }
            }
        });

        return dialogView;
    }


    public void setOnEditFinishedListener(OnEditFinishedListener onEditFinishedListener) {
        this.onEditFinishedListener = onEditFinishedListener;
    }

}
