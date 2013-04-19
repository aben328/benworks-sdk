/**
 * 
 */
package cn.benworks.sdk.event.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ben
 */
public class LoggerExceptionHandler implements ExceptionHandler {
	private static Logger logger = LoggerFactory.getLogger(LoggerExceptionHandler.class);

	@Override
	public boolean handle(Throwable t) {
		logger.error("unexpected exception from event listener");
		return false;
	}

}
