package br.ufpb.dcx.appalpha.control.util;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;

import java.io.FileNotFoundException;
import java.io.IOException;

import br.ufpb.dcx.appalpha.R;

/**
 * Class to manage image load with Glide framework
 */
public class ImageLoadUtil {
    private final String TAG = "ImageLoadUtil";
    private static ImageLoadUtil instance;

    private ImageLoadUtil(){}

    /**
     * Get shared instance
     * @return
     */
    public static ImageLoadUtil getInstance(){
        if(instance == null){
            instance = new ImageLoadUtil();
        }

        return instance;
    }

    /**
     * Load image in an ImageView
     * @param imageUrl
     * @param themeImageLeft
     * @param context
     */
    public void loadImage(String imageUrl, ImageView themeImageLeft, Context context){
        DiskCacheStrategy diskCacheStrategy = new DiskCacheStrategy() {
            @Override
            public boolean isDataCacheable(DataSource dataSource) {
                return true;
            }

            @Override
            public boolean isResourceCacheable(boolean isFromAlternateCacheKey, DataSource dataSource, EncodeStrategy encodeStrategy) {
                return true;
            }

            @Override
            public boolean decodeCachedResource() {
                return true;
            }

            @Override
            public boolean decodeCachedData() {
                return true;
            }
        };

        int erroImg = -1;
        try{
            erroImg = Integer.parseInt(imageUrl);
        }catch(NumberFormatException e){
            erroImg = R.drawable.no_image;
        }

        Glide.with(context)
                .load(imageUrl)
                .error(erroImg)
                .diskCacheStrategy(diskCacheStrategy)
                .into(themeImageLeft);
    }
}
