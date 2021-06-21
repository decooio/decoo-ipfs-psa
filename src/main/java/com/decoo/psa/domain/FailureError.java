package com.decoo.psa.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * FailureError
 */
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-05-21T18:02:25.130916+08:00[Asia/Shanghai]")
public class FailureError   {
  @JsonProperty("reason")
  private String reason;

  @JsonProperty("details")
  private String details;

  public FailureError reason(String reason) {
    this.reason = reason;
    return this;
  }

  /**
   * Mandatory string identifying the type of error
   * @return reason
  */
  @ApiModelProperty(example = "ERROR_CODE_FOR_MACHINES", required = true, value = "Mandatory string identifying the type of error")
  @NotNull


  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public FailureError details(String details) {
    this.details = details;
    return this;
  }

  /**
   * Optional, longer description of the error; may include UUID of transaction for support, links to documentation etc
   * @return details
  */
  @ApiModelProperty(example = "Optional explanation for humans with more details", value = "Optional, longer description of the error; may include UUID of transaction for support, links to documentation etc")


  public String getDetails() {
    return details;
  }

  public void setDetails(String details) {
    this.details = details;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FailureError failureError = (FailureError) o;
    return Objects.equals(this.reason, failureError.reason) &&
        Objects.equals(this.details, failureError.details);
  }

  @Override
  public int hashCode() {
    return Objects.hash(reason, details);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FailureError {\n");
    
    sb.append("    reason: ").append(toIndentedString(reason)).append("\n");
    sb.append("    details: ").append(toIndentedString(details)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

