package net.kyouko.cloudier.util;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Util class for generating and modifying texts.
 *
 * @author beta
 */
public class TextUtil {

    public static SpannableStringBuilder addLinkToUrlsInText(final Context context, String text) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        String regexUrl = "(https?://)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([/\\w\\.-]*)*/?";

        Matcher urlMatcher = Pattern.compile(regexUrl).matcher(text);
        while (urlMatcher.find()) {
            final String url = text.substring(urlMatcher.start(), urlMatcher.end());
            ClickableSpan linkSpan = new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    UrlUtil.openUrl(context, url);
                }
            };
            builder.setSpan(linkSpan, urlMatcher.start(), urlMatcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return builder;
    }

}
