/**
 * 
 */
package cn.benworks.sdk.event.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import cn.benworks.sdk.event.ApplicationEvent;
import cn.benworks.sdk.event.ApplicationEventListener;

/**
 * @author Ben
 */
public class MethodInvoker<E extends ApplicationEvent> {
	private Method method;
	private ApplicationEventListener<E> instance;

	public MethodInvoker(Method m, ApplicationEventListener<E> listener) {
		this.instance = listener;
		this.method = m;
	}

	public void invoke(E event) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		method.invoke(instance, event);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object invoker) {
		if (this == invoker) {
			return true;
		}
		if (invoker instanceof MethodInvoker) {
			MethodInvoker<E> minvoker = (MethodInvoker<E>) invoker;
			return this.method.equals(minvoker.method) && this.instance.equals(minvoker.instance);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return method.hashCode() ^ instance.hashCode();
	}

	@Override
	public String toString() {
		return "Method" + instance + "." + method.getName();
	}

	public Method getMethod() {
		return method;
	}

	public ApplicationEventListener<E> getInstance() {
		return instance;
	}

}
