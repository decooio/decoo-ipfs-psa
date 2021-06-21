package com.decoo.psa.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Alternative text matching strategy
 */
public enum TextMatchingStrategy {
  
  EXACT("exact"),
  
  IEXACT("iexact"),
  
  PARTIAL("partial"),
  
  IPARTIAL("ipartial");

  private String value;

  TextMatchingStrategy(String value) {
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
  public static TextMatchingStrategy fromValue(String value) {
    for (TextMatchingStrategy b : TextMatchingStrategy.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

