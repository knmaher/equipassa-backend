package com.equipassa.equipassa.exception;

public class S3OperationException extends RuntimeException {
    public S3OperationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
