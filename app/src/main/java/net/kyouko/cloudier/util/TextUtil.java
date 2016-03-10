package net.kyouko.cloudier.util;

import android.content.Context;
import android.graphics.Paint;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import net.kyouko.cloudier.R;
import net.kyouko.cloudier.ui.style.ClickableWithoutUnderlineSpan;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Util class for generating and modifying texts.
 *
 * @author beta
 */
public class TextUtil {

    public static SpannableStringBuilder addLinkToUrlsInText(final Context context, String text, final boolean clickable) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        return addLinkToUrlsInText(context, builder, clickable);
    }


    public static SpannableStringBuilder addLinkToUrlsInText(final Context context, SpannableStringBuilder builder, final boolean clickable) {
        String regexUrl = "(https?://)?([\\da-z-]+.)+\\.([a-z\\.]{2,6})([/\\w\\.-]*)*/?";

        Matcher urlMatcher = Pattern.compile(regexUrl).matcher(builder.toString());
        while (urlMatcher.find()) {
            final String url = builder.toString().substring(urlMatcher.start(), urlMatcher.end());

            ClickableWithoutUnderlineSpan linkSpan = new ClickableWithoutUnderlineSpan() {
                @Override
                public void onClick(View widget) {
                    if (clickable) {
                        UrlUtil.openUrl(context, url);
                    }
                }
            };

            builder.setSpan(linkSpan, urlMatcher.start(), urlMatcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return builder;
    }


    public static SpannableStringBuilder addLinkToTopicsInText(final Context context, String text, final boolean clickable) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        return addLinkToTopicsInText(context, builder, clickable);
    }


    public static SpannableStringBuilder addLinkToTopicsInText(final Context context, SpannableStringBuilder builder, final boolean clickable) {
        int topicStart = -1, topicEnd;
        for (int i = 0; i < builder.length(); i += 1) {
            if (builder.charAt(i) == '#') {
                if (topicStart < 0) {
                    topicStart = i;
                } else {
                    topicEnd = i + 1;

                    ClickableWithoutUnderlineSpan linkSpan = new ClickableWithoutUnderlineSpan() {
                        @Override
                        public void onClick(View widget) {
                            if (clickable) {
                                // TODO: view topic
                            }
                        }
                    };

                    builder.setSpan(linkSpan, topicStart, topicEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    topicStart = -1;
                }
            }
        }

        return builder;
    }

}
