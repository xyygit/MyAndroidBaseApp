package lib.core.http;

import lib.core.http.definition.HttpCall;
import okhttp3.Call;

public class OKHttpCall implements HttpCall {

    private Call call;

    public OKHttpCall(Call call) {
        this.call = call;
    }

    @Override
    public void cancel() {
        if(call != null && !call.isCanceled()) call.cancel();
    }

    @Override
    public boolean isCanceled() {
        return call != null ? call.isCanceled() : false;
    }

    @Override
    public boolean isExecuted() {
        return call != null ? call.isExecuted() : false;
    }
}
