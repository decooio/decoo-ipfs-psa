package com.decoo.psa.domain;


import com.decoo.psa.constants.IPFSConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 * Pin object with status
 */
@ApiModel(description = "Pin object with status")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-05-21T18:02:25.130916+08:00[Asia/Shanghai]")
public class PinStatus   {
  @JsonProperty("requestid")
  private String requestid;

  @JsonProperty("status")
  private Status status;

  @JsonProperty("created")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  @JsonFormat(timezone = "GMT+0", pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
  private OffsetDateTime created;

  @JsonProperty("pin")
  private Pin pin;

  @JsonProperty("delegates")
  @Valid
  private Set<String> delegates = new LinkedHashSet<>();

  @JsonProperty("info")
  @Valid
  private Map<String, String> info = null;

  public PinStatus requestid(String requestid) {
    this.requestid = requestid;
    return this;
  }

  /**
   * Globally unique identifier of the pin request; can be used to check the status of ongoing pinning, or pin removal
   * @return requestid
  */
  @ApiModelProperty(example = "UniqueIdOfPinRequest", required = true, value = "Globally unique identifier of the pin request; can be used to check the status of ongoing pinning, or pin removal")
  @NotNull


  public String getRequestid() {
    return requestid;
  }

  public void setRequestid(String requestid) {
    this.requestid = requestid;
  }

  public PinStatus status(Status status) {
    this.status = status;
    return this;
  }

  /**
   * Get status
   * @return status
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public PinStatus created(OffsetDateTime created) {
    this.created = created;
    return this;
  }

  /**
   * Immutable timestamp indicating when a pin request entered a pinning service; can be used for filtering results and pagination
   * @return created
  */
  @ApiModelProperty(example = "2020-07-27T17:32:28Z", required = true, value = "Immutable timestamp indicating when a pin request entered a pinning service; can be used for filtering results and pagination")
  @NotNull

  @Valid

  public OffsetDateTime getCreated() {
    return created;
  }

  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }

  public PinStatus pin(Pin pin) {
    this.pin = pin;
    return this;
  }

  /**
   * Get pin
   * @return pin
  */
  @ApiModelProperty(required = true, value = "")
  @NotNull

  @Valid

  public Pin getPin() {
    return pin;
  }

  public void setPin(Pin pin) {
    this.pin = pin;
  }

  public PinStatus delegates(Set<String> delegates) {
    this.delegates = delegates;
    return this;
  }

  public PinStatus addDelegatesItem(String delegatesItem) {
    this.delegates.add(delegatesItem);
    return this;
  }

  /**
   * List of multiaddrs designated by pinning service for transferring any new data from external peers
   * @return delegates
  */
  @ApiModelProperty(example = "[\"/ip4/203.0.113.1/tcp/4001/p2p/QmServicePeerId\"]", required = true, value = "List of multiaddrs designated by pinning service for transferring any new data from external peers")
  @NotNull

@Size(min=1,max=20)
  public Set<String> getDelegates() {
    return delegates;
  }

  public void setDelegates(Set<String> delegates) {
    this.delegates = delegates;
  }

  public PinStatus info(Map<String, String> info) {
    this.info = info;
    return this;
  }

  public PinStatus putInfoItem(String key, String infoItem) {
    if (this.info == null) {
      this.info = new HashMap<>();
    }
    this.info.put(key, infoItem);
    return this;
  }

  /**
   * Optional info for PinStatus response
   * @return info
  */
  @ApiModelProperty(example = "{\"status_details\":\"Queue position: 7 of 9\"}", value = "Optional info for PinStatus response")


  public Map<String, String> getInfo() {
    return info;
  }

  public void setInfo(Map<String, String> info) {
    this.info = info;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PinStatus pinStatus = (PinStatus) o;
    return Objects.equals(this.requestid, pinStatus.requestid) &&
        Objects.equals(this.status, pinStatus.status) &&
        Objects.equals(this.created, pinStatus.created) &&
        Objects.equals(this.pin, pinStatus.pin) &&
        Objects.equals(this.delegates, pinStatus.delegates) &&
        Objects.equals(this.info, pinStatus.info);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestid, status, created, pin, delegates, info);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PinStatus {\n");
    
    sb.append("    requestid: ").append(toIndentedString(requestid)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    created: ").append(toIndentedString(created)).append("\n");
    sb.append("    pin: ").append(toIndentedString(pin)).append("\n");
    sb.append("    delegates: ").append(toIndentedString(delegates)).append("\n");
    sb.append("    info: ").append(toIndentedString(info)).append("\n");
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

  public static PinStatus convertByPinFileState(PinFileState pinFileState) {
    return convertByPinFile(pinFileState, pinFileState.getJobStatus());
  }

  public static Status convertStatusByPinFileAndQueueState(Integer pinFileState, Integer queueState) {
    return Status.valueOf(PinFile.FileState.getFileStateByCode(pinFileState)
            .convertPinningServiceState(PinJobs.PinJobStatus.getStatusByCode(queueState))
            .getMsg().toUpperCase());
  }

  public static PinStatus convertByPinFile(PinFile pinFile, Integer queueState) {
    return converByPinFileAndStatus(pinFile, convertStatusByPinFileAndQueueState(pinFile.getState(), queueState));
  }

  public static PinStatus converByPinFileAndStatus(PinFile pinFile, Status status) {
      PinStatus pinStatus = new PinStatus();
      pinStatus.setRequestid(pinFile.getUuid());
      pinStatus.setCreated(pinFile.getCreateAt().toInstant().atOffset(ZoneOffset.UTC));
      pinStatus.setDelegates(IPFSConstants.IPFS_DELEGATES);
      pinStatus.setStatus(status);
      Pin pin = new Pin();
      pin.setCid(pinFile.getIpfsPinHash());
      if (pinFile.getMetaData() != null) {
        pin.setMeta(pinFile.getMetaData().getKeyValues());
        pin.setName(pinFile.getMetaData().getName());
        pin.setOrigins(pinFile.getMetaData().getOrigins());
      }
      pinStatus.setPin(pin);
      return pinStatus;
  }
}

