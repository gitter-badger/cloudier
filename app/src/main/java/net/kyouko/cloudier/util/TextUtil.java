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
        String regexUrl = "(https?://)?([\\da-z-]+.)+\\.([a-z\\.]{2,6})([/\\w\\.-]*)*/?";

        Matcher urlMatcher = Pattern.compile(regexUrl).matcher(text);
        while (urlMatcher.find()) {
            final String url = text.substring(urlMatcher.start(), urlMatcher.end());

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

}
