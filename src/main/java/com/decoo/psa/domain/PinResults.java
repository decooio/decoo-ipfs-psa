package com.decoo.psa.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Response used for listing pin objects matching request
 */
@ApiModel(description = "Response used for listing pin objects matching request")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-05-21T18:02:25.130916+08:00[Asia/Shanghai]")
public class PinResults   {
  @JsonProperty("count")
  private Integer count;

  @JsonProperty("results")
  @Valid
  private Set<PinStatus> results = new LinkedHashSet<>();

  public PinResults count(Integer count) {
    this.count = count;
    return this;
  }

  /**
   * The total number of pin objects that exist for passed query filters
   * minimum: 0
   * @return count
  */
  @ApiModelProperty(example = "1", required = true, value = "The total number of pin objects that exist for passed query filters")
  @NotNull

@Min(0)
  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public PinResults results(Set<PinStatus> results) {
    this.results = results;
    return this;
  }

  public PinResults addResultsItem(PinStatus resultsItem) {
    this.results.add(resultsItem);
    return this;
  }

  /**
   * An array of PinStatus results
   * @return results
  */
  @ApiModelProperty(required = true, value = "An array of PinStatus results")
  @NotNull

  @Valid
@Size(min=0,max=1000)
  public Set<PinStatus> getResults() {
    return results;
  }

  public void setResults(Set<PinStatus> results) {
    this.results = results;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PinResults pinResults = (PinResults) o;
    return Objects.equals(this.count, pinResults.count) &&
        Objects.equals(this.results, pinResults.results);
  }

  @Override
  public int hashCode() {
    return Objects.hash(count, results);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PinResults {\n");
    
    sb.append("    count: ").append(toIndentedString(count)).append("\n");
    sb.append("    results: ").append(toIndentedString(results)).append("\n");
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

