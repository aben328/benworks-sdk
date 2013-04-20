package cn.benworks.sdk.event.listener;

import cn.benworks.sdk.event.ApplicationEvent;
import cn.benworks.sdk.event.ApplicationEventListener;

/**
 * @author Ben
 * @param <T>
 */
public abstract class GenericEventListener<T extends ApplicationEvent> implements ApplicationEventListener<T> {
	void deal(T event) {
		// 首先获取所有的handle like method
		System.out.println("GenericEventListener.deal " + event);
	}
}
