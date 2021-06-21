package com.decoo.psa.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

/**
 * Pin object
 */
@ApiModel(description = "Pin object")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-05-21T18:02:25.130916+08:00[Asia/Shanghai]")
public class Pin   {
  @JsonProperty("cid")
  private String cid;

  @JsonProperty("name")
  private String name;

  @JsonProperty("origins")
  @Valid
  private Set<String> origins = null;

  @JsonProperty("meta")
  @Valid
  private Map<String, String> meta = null;

  public Pin cid(String cid) {
    this.cid = cid;
    return this;
  }

  /**
   * Content Identifier (CID) to be pinned recursively
   * @return cid
  */
  @ApiModelProperty(example = "QmCIDToBePinned", required = true, value = "Content Identifier (CID) to be pinned recursively")
  @NotNull


  public String getCid() {
    return cid;
  }

  public void setCid(String cid) {
    this.cid = cid;
  }

  public Pin name(String name) {
    this.name = name;
    return this;
  }

  /**
   * Optional name for pinned data; can be used for lookups later
   * @return name
  */
  @ApiModelProperty(example = "PreciousData.pdf", value = "Optional name for pinned data; can be used for lookups later")

@Size(max=255)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Pin origins(Set<String> origins) {
    this.origins = origins;
    return this;
  }

  public Pin addOriginsItem(String originsItem) {
    if (this.origins == null) {
      this.origins = new LinkedHashSet<>();
    }
    this.origins.add(originsItem);
    return this;
  }

  /**
   * Optional list of multiaddrs known to provide the data
   * @return origins
  */
  @ApiModelProperty(example = "[\"/ip4/203.0.113.142/tcp/4001/p2p/QmSourcePeerId\",\"/ip4/203.0.113.114/udp/4001/quic/p2p/QmSourcePeerId\"]", value = "Optional list of multiaddrs known to provide the data")

@Size(min=0,max=20)
  public Set<String> getOrigins() {
    return origins;
  }

  public void setOrigins(Set<String> origins) {
    this.origins = origins;
  }

  public Pin meta(Map<String, String> meta) {
    this.meta = meta;
    return this;
  }

  public Pin putMetaItem(String key, String metaItem) {
    if (this.meta == null) {
      this.meta = new HashMap<>();
    }
    this.meta.put(key, metaItem);
    return this;
  }

  /**
   * Optional metadata for pin object
   * @return meta
  */
  @ApiModelProperty(example = "{\"app_id\":\"99986338-1113-4706-8302-4420da6158aa\"}", value = "Optional metadata for pin object")


  public Map<String, String> getMeta() {
    return meta;
  }

  public void setMeta(Map<String, String> meta) {
    this.meta = meta;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Pin pin = (Pin) o;
    return Objects.equals(this.cid, pin.cid) &&
        Objects.equals(this.name, pin.name) &&
        Objects.equals(this.origins, pin.origins) &&
        Objects.equals(this.meta, pin.meta);
  }

  @Override
  public int hashCode() {
    return Objects.hash(cid, name, origins, meta);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Pin {\n");
    
    sb.append("    cid: ").append(toIndentedString(cid)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    origins: ").append(toIndentedString(origins)).append("\n");
    sb.append("    meta: ").append(toIndentedString(meta)).append("\n");
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

