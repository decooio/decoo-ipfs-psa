package com.decoo.psa.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Status a pin object can have at a pinning service
 */
public enum Status {
  
  QUEUED("queued"),
  
  PINNING("pinning"),
  
  PINNED("pinned"),
  
  FAILED("failed");

  private String value;

  Status(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static Status fromValue(String value) {
    for (Status b : Status.values()) {
      if (b.value.toUpperCase().equals(value.toUpperCase())) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

