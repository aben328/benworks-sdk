package cn.benworks.sdk.event;

/**
 * 事件监听器接口
 * @author Ben
 * @param <E>
 */
public interface ApplicationEventListener<E extends ApplicationEvent> {

	public void on(E event);

}
