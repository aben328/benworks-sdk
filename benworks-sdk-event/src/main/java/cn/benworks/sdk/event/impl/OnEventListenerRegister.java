package cn.benworks.sdk.event.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.benworks.sdk.event.ApplicationEvent;
import cn.benworks.sdk.event.ApplicationEventListener;
import cn.benworks.sdk.event.anno.AwareByListener;
import cn.benworks.sdk.event.anno.Order;
import cn.benworks.sdk.event.exception.ExceptionHandler;
import cn.benworks.sdk.event.exception.LoggerExceptionHandler;

/**
 * @author Ben
 */
public abstract class OnEventListenerRegister<Event extends ApplicationEvent> {
	private static final LoggerExceptionHandler EXCEPTION_HANDLER = new LoggerExceptionHandler();
	private static final Logger logger = LoggerFactory.getLogger(AbstractEventListenerRegister.class);

	private final Comparator<? super ApplicationEventListener<Event>> LISTENER_ORDER_COMPARATOR = new Comparator<ApplicationEventListener<Event>>() {
		@Override
		public int compare(ApplicationEventListener<Event> listener1, ApplicationEventListener<Event> listener2) {
			Order order1 = listener1.getClass().getAnnotation(Order.class);
			Order order2 = listener2.getClass().getAnnotation(Order.class);
			int value1 = order1 == null ? 0 : order1.value();
			int value2 = order2 == null ? 0 : order2.value();
			return value2 - value1;
		}
	};

	private final Class<Event> genericEventClass;
	private Map<Class<Event>, ArrayList<ApplicationEventListener<Event>>> acceptEventMapping;
	private Map<Class<Event>, CopyOnWriteArraySet<Class<Event>>> eventClassesMapping;

	@SuppressWarnings("unchecked")
	public OnEventListenerRegister() {
		genericEventClass = (Class<Event>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
		acceptEventMapping = new ConcurrentHashMap<Class<Event>, ArrayList<ApplicationEventListener<Event>>>();
		eventClassesMapping = new ConcurrentHashMap<Class<Event>, CopyOnWriteArraySet<Class<Event>>>();
	}

	/**
	 * 注册监听器实例
	 * @param listener
	 */
	@SuppressWarnings("unchecked")
	public synchronized void register(ApplicationEventListener<Event> listener) {
		for (Method m : listener.getClass().getMethods()) {
			if (isVaildListenerMethod(m)) {
				Class<Event> eventClass = (Class<Event>) m.getParameterTypes()[0];
				// MethodInvoker<Event> invoker = new MethodInvoker<Event>(m, listener);
				// add to AcceptEvent Mapping
				synchronized (acceptEventMapping) {
					ArrayList<ApplicationEventListener<Event>> holder = acceptEventMapping.get(eventClass);
					if (holder == null) {
						holder = new ArrayList<ApplicationEventListener<Event>>();
						acceptEventMapping.put(eventClass, holder);
					}
					if (holder.contains(listener))
						return;
					holder.add(listener);
					Collections.sort(holder, LISTENER_ORDER_COMPARATOR);
				}
			}
		}
	}

	/**
	 * @param listener
	 */
	@SuppressWarnings("unchecked")
	public synchronized void unregister(ApplicationEventListener<Event> listener) {
		for (Method m : listener.getClass().getMethods()) {
			if (isVaildListenerMethod(m)) {
				Class<Event> eventClass = (Class<Event>) m.getParameterTypes()[0];
				// MethodInvoker<Event> invoker = new MethodInvoker<Event>(m, listener);
				// remove from AcceptEvent Mapping
				synchronized (acceptEventMapping) {
					List<ApplicationEventListener<Event>> holder = acceptEventMapping.get(eventClass);
					if (holder != null) {
						holder.remove(listener);
						if (holder.size() == 0) {
							acceptEventMapping.remove(eventClass);
						}
					}
				}
			}
		}
	}

	public synchronized void unregisterAll() {
		Set<Class<Event>> classSet = acceptEventMapping.keySet();
		for (Class<Event> class1 : classSet) {
			List<ApplicationEventListener<Event>> holder = acceptEventMapping.get(class1);
			holder.clear();
		}
		acceptEventMapping.clear();

		Set<Class<Event>> eventSet = eventClassesMapping.keySet();
		for (Class<Event> class2 : eventSet) {
			CopyOnWriteArraySet<Class<Event>> eventList = eventClassesMapping.get(class2);
			eventList.clear();
		}
		eventClassesMapping.clear();
	}

	/**
	 * dispatch event
	 * @param event
	 */
	public void dispatch(Event event) {
		dispatch(event, EXCEPTION_HANDLER);
	}

	/**
	 * dispatch event
	 * @param event
	 * @param handler
	 */
	@SuppressWarnings("unchecked")
	public void dispatch(Event event, ExceptionHandler handler) {
		long t1 = System.currentTimeMillis();

		Set<Class<Event>> eventList = getEventList((Class<Event>) event.getClass());

		List<MethodInvokerHolder<Event>> holders = new LinkedList<MethodInvokerHolder<Event>>();
		for (Class<Event> evtClass : eventList) {
			List<ApplicationEventListener<Event>> holder = acceptEventMapping.get(evtClass);
			if (holder != null) {
				for (ApplicationEventListener<Event> appEventListener : holder) {
					appEventListener.on(event);
				}
			}
		}

		for (MethodInvokerHolder<Event> holder : holders) {
			holder.invokeBefores(event, handler);
		}

		for (MethodInvokerHolder<Event> holder : holders) {
			holder.invokeNormals(event, handler);
		}
		for (MethodInvokerHolder<Event> holder : holders) {
			holder.invokeAfters(event, handler);
		}

		long t2 = System.currentTimeMillis();
		if ((t2 - t1) > 100) {
			System.out.println(this.getClass().getName() + "EventListerRegister.dispatch(): " + (t2 - t1) + "  "
					+ event.getClass().getName() + " eventList" + eventList.size());
			logger.info(this.getClass().getName() + "EventListerRegister.dispatch(): " + (t2 - t1) + "  "
					+ event.getClass().getName() + " eventList" + eventList.size());
		}
	}

	// private method

	private boolean isVaildListenerMethod(Method m) {
		boolean nonPublic = !Modifier.isPublic(m.getModifiers());
		boolean nonVoidReturn = !m.getReturnType().equals(void.class);
		if (nonPublic || nonVoidReturn)
			return false;
		Class<?>[] parameters = m.getParameterTypes();
		if (parameters.length == 1) {
			if (genericEventClass.isAssignableFrom(parameters[0]))
				return true;
		}
		return false;
	}

	private Set<Class<Event>> getEventList(Class<Event> eventClass) {
		CopyOnWriteArraySet<Class<Event>> eventClasses = eventClassesMapping.get(eventClass);
		if (eventClasses == null) {
			eventClasses = new CopyOnWriteArraySet<Class<Event>>();
			travelEventStructure(eventClasses, eventClass);
			eventClassesMapping.put(eventClass, eventClasses);
		}
		return eventClasses;
	}

	@SuppressWarnings("unchecked")
	private void travelEventStructure(Set<Class<Event>> eventClasses, Class<Event> eventClass) {
		if (eventClass == genericEventClass) {
			eventClasses.add(eventClass);
			return;
		}
		eventClasses.add(eventClass);
		if (genericEventClass.isInterface()) {
			for (Class<?> eventInterface : eventClass.getInterfaces()) {
				travelEventInterfaceStructure(eventClasses, (Class<Event>) eventInterface);
			}
		}
		if (genericEventClass.isAssignableFrom(eventClass.getSuperclass())) {
			travelEventStructure(eventClasses, (Class<Event>) eventClass.getSuperclass());
		}
	}

	private void travelEventInterfaceStructure(Set<Class<Event>> eventClasses, Class<Event> eventInterface) {
		if (isListenerAwareInterface(eventInterface) || eventInterface == genericEventClass) {
			eventClasses.add(eventInterface);
		}
		if (eventInterface == genericEventClass) {
			// eventClasses.add(eventInterface);
		}
		for (Class<?> evtIf : eventInterface.getInterfaces()) {
			if (genericEventClass.isAssignableFrom(evtIf)) {
				travelEventInterfaceStructure(eventClasses, eventInterface);
			}
		}
	}

	private boolean isListenerAwareInterface(Class<Event> eventInterface) {
		boolean legalInterface = genericEventClass.isAssignableFrom(eventInterface)
				&& genericEventClass != eventInterface;
		boolean awareAnno = eventInterface.getAnnotation(AwareByListener.class) != null;

		return legalInterface && awareAnno;
	}

}
