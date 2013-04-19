/**
 * 
 */
package cn.benworks.sdk.event.exception;

/**
 * @author Ben
 */
public interface ExceptionHandler {

	public boolean handle(Throwable t);
}
