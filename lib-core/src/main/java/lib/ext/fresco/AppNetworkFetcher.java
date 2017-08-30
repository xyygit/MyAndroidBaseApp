package lib.ext.fresco;

import com.facebook.imagepipeline.image.EncodedImage;
import com.facebook.imagepipeline.producers.BaseNetworkFetcher;
import com.facebook.imagepipeline.producers.Consumer;
import com.facebook.imagepipeline.producers.FetchState;
import com.facebook.imagepipeline.producers.ProducerContext;

/**
 * Created by lightning on 17/3/5.
 */

public class AppNetworkFetcher extends BaseNetworkFetcher<FetchState> {

    @Override
    public FetchState createFetchState(Consumer<EncodedImage> consumer, ProducerContext producerContext) {
        return new FetchState(consumer, producerContext);
    }

    @Override
    public void fetch(FetchState fetchState, Callback callback) {

    }
}
