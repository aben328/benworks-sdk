/**
 * 
 */
package cn.benworks.sdk.event.impl;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.benworks.sdk.event.ApplicationEvent;
import cn.benworks.sdk.event.exception.LoggerExceptionHandler;

/**
 * @author Ben
 */
public abstract class AbstractEventListenerRegister<Event extends ApplicationEvent> {
	private static final LoggerExceptionHandler EXCEPTION_HANDLER = new LoggerExceptionHandler();

	private final Class<Event> genericEventClass;
	private Map<Class<Event>, MethodInvokerHolder<Event>> acceptEventMapping;
	private Map<Class<Event>, HashSet<Class<Event>>> eventClassesMapping;
	
	@SuppressWarnings("unchecked")
	public AbstractEventListenerRegister() {
		genericEventClass = (Class<Event>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		acceptEventMapping = new ConcurrentHashMap<Class<Event>, MethodInvokerHolder<Event>>();
		eventClassesMapping = new HashMap<Class<Event>, HashSet<Class<Event>>>();
	}

}
