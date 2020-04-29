package com.lmax.disruptor;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class IgnoreExceptionHandler implements ExceptionHandler<Object> {
    private static final Logger LOGGER = Logger.getLogger(IgnoreExceptionHandler.class.getName());
    private final Logger logger;

    public IgnoreExceptionHandler() {
        this.logger = LOGGER;
    }

    public IgnoreExceptionHandler(Logger logger2) {
        this.logger = logger2;
    }

    public void handleEventException(Throwable ex, long sequence, Object event) {
        this.logger.log(Level.INFO, "Exception processing: " + sequence + " " + event, ex);
    }

    public void handleOnStartException(Throwable ex) {
        this.logger.log(Level.INFO, "Exception during onStart()", ex);
    }

    public void handleOnShutdownException(Throwable ex) {
        this.logger.log(Level.INFO, "Exception during onShutdown()", ex);
    }
}
