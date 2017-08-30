package lib.ext.fresco;

import android.net.Uri;

import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.imagepipeline.cache.BitmapMemoryCacheKey;
import com.facebook.imagepipeline.cache.CacheKeyFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.Postprocessor;

/**
 * Created by lightning on 17/3/5.
 */

public class AppCacheKeyFactory implements CacheKeyFactory {

    @Override
    public CacheKey getBitmapCacheKey(ImageRequest request, Object callerContext) {
        return new BitmapMemoryCacheKey(
                getCacheKeySourceUri(request.getSourceUri()),
                request.getResizeOptions(),
                request.getAutoRotateEnabled(),
                request.getImageDecodeOptions(),
                null,
                null,
                callerContext);
    }

    @Override
    public CacheKey getPostprocessedBitmapCacheKey(ImageRequest request, Object callerContext) {
        final Postprocessor postprocessor = request.getPostprocessor();
        final CacheKey postprocessorCacheKey;
        final String postprocessorName;
        if (postprocessor != null) {
            postprocessorCacheKey = postprocessor.getPostprocessorCacheKey();
            postprocessorName = postprocessor.getClass().getName();
        } else {
            postprocessorCacheKey = null;
            postprocessorName = null;
        }
        return new BitmapMemoryCacheKey(
                getCacheKeySourceUri(request.getSourceUri()),
                request.getResizeOptions(),
                request.getAutoRotateEnabled(),
                request.getImageDecodeOptions(),
                postprocessorCacheKey,
                postprocessorName,
                callerContext);
    }

    @Override
    public CacheKey getEncodedCacheKey(ImageRequest request, Object callerContext) {
        return new SimpleCacheKey(getCacheKeySourceUri(request.getSourceUri()));
    }

    protected String getCacheKeySourceUri(Uri sourceUri) {
        return sourceUri.getPath();
    }

}
