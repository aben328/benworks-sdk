package cn.benworks.sdk.event.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.benworks.sdk.event.ApplicationEvent;
import cn.benworks.sdk.event.anno.AfterProcess;
import cn.benworks.sdk.event.anno.BeforeProcess;
import cn.benworks.sdk.event.exception.ExceptionHandler;

/**
 * 
 * 
 * @author Ben
 *
 * @param <E>
 */
public class MethodInvokerHolder<E extends ApplicationEvent> {
	private static final Logger logger = LoggerFactory.getLogger(MethodInvokerHolder.class);
	private Class<E> eventClass;
	Set<MethodInvoker<E>> beforeInvokers = new CopyOnWriteArraySet<MethodInvoker<E>>();
	Set<MethodInvoker<E>> normalInvokers = new CopyOnWriteArraySet<MethodInvoker<E>>();
	Set<MethodInvoker<E>> afterInvokers = new CopyOnWriteArraySet<MethodInvoker<E>>();

	public MethodInvokerHolder(Class<E> eventClass) {
		this.eventClass = eventClass;
	}

	public void add(MethodInvoker<E> invoker) {
		BeforeProcess before = invoker.getMethod().getAnnotation(BeforeProcess.class);
		AfterProcess after = invoker.getMethod().getAnnotation(AfterProcess.class);
		if (before != null)
			beforeInvokers.add(invoker);
		else if (after != null)
			afterInvokers.add(invoker);
		else
			normalInvokers.add(invoker);
	}

	public void remove(MethodInvoker<E> invoker) {
		BeforeProcess before = invoker.getMethod().getAnnotation(BeforeProcess.class);
		AfterProcess after = invoker.getMethod().getAnnotation(AfterProcess.class);
		if (before != null)
			beforeInvokers.remove(invoker);
		else if (after != null)
			afterInvokers.remove(invoker);
		else
			normalInvokers.remove(invoker);
	}

	public void clearAll() {
		beforeInvokers.clear();
		normalInvokers.clear();
		afterInvokers.clear();
	}

	public void invokeBefores(E event, ExceptionHandler handler) {
		innerInvoke(event, handler, beforeInvokers);
	}

	public void invokeAfters(E event, ExceptionHandler handler) {
		innerInvoke(event, handler, afterInvokers);
	}

	public void invokeNormals(E event, ExceptionHandler handler) {
		innerInvoke(event, handler, normalInvokers);
	}

	public int size() {
		return beforeInvokers.size() + normalInvokers.size() + afterInvokers.size();
	}

	// private method

	private void innerInvoke(E event, ExceptionHandler handler, Set<MethodInvoker<E>> invokerSet) {
		for (MethodInvoker<E> invoker : invokerSet) {
			try {
				invoker.invoke(event);
			} catch (IllegalAccessException e) {
				logger.error(e.getMessage());
			} catch (IllegalArgumentException e) {
				logger.error(e.getMessage());
			} catch (InvocationTargetException e) {
				if (!handler.handle(e.getCause())) {
					break;
				}
			}
		}
	}

	@Override
	public String toString() {
		return "MethodInvokerHolder<" + eventClass.getSimpleName() + ">";
	}
}
