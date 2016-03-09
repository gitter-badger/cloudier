package net.kyouko.cloudier.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.drawable.ProgressBarDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.samples.zoomable.ZoomableDraweeView;

import net.kyouko.cloudier.R;
import net.kyouko.cloudier.util.ImageUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageActivity extends AppCompatActivity {

    public final static String ARG_IS_MULTIPLE_IMAGES = "IS_MULTIPLE_IMAGES";
    public final static String ARG_IMAGE_URL = "IMAGE_URL";
    public final static String ARG_IMAGE_URLS = "IMAGE_URLS";
    public final static String ARG_IMAGE_INDEX = "IMAGE_INDEX";


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.image)
    ZoomableDraweeView imageView;

    private boolean isMultipleImages;
    private String imageUrl;
    private List<String> imageUrls;
    private int imageIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        initView();
        fetchArguments();
        loadImage();
    }


    private void initView() {
        ButterKnife.bind(this);

        initToolbar();
    }


    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void fetchArguments() {
        if (getIntent().getBundleExtra("ARGS") != null) {
            Bundle args = getIntent().getBundleExtra("ARGS");

            isMultipleImages = args.getBoolean(ARG_IS_MULTIPLE_IMAGES, false);

            if (!isMultipleImages) {
                imageUrl = args.getString(ARG_IMAGE_URL);
            } else {
                imageUrls = args.getStringArrayList(ARG_IMAGE_URLS);
                imageIndex = args.getInt(ARG_IMAGE_INDEX);
            }
        }
    }


    private void loadImage() {
        if (!isMultipleImages) {
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(ImageUtil.getInstance(this).parseImageUrl(imageUrl)))
                    .setTapToRetryEnabled(true)
                    .build();
            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                    .setProgressBarImage(new ProgressBarDrawable())
                    .build();

            imageView.setController(controller);
            imageView.setHierarchy(hierarchy);
        } else {
            // TODO: load multiple images
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(Uri.parse(ImageUtil.getInstance(this).parseImageUrl(imageUrls.get(imageIndex))))
                    .setTapToRetryEnabled(true)
                    .build();
            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(getResources())
                    .setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER)
                    .setProgressBarImage(new ProgressBarDrawable())
                    .build();

            imageView.setController(controller);
            imageView.setHierarchy(hierarchy);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
