package com.research.exception;

public class ValidationException extends RuntimeException {
  public ValidationException(String message) {
    super(message);
  }
}
