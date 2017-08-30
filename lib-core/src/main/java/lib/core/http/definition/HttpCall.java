package lib.core.http.definition;

public interface HttpCall {

    void cancel();

    boolean isCanceled();

    boolean isExecuted();
}
