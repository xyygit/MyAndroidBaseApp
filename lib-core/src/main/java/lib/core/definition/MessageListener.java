package lib.core.definition;

/**
 * Created by lightning on 2017/1/3.
 * 消息监听-用于应用内消息传递接受
 */
public interface MessageListener {

    boolean isInterrupt();

    void onMessage(Object msg);

}
