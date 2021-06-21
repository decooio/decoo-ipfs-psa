package com.decoo.psa.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY,getterVisibility= JsonAutoDetect.Visibility.NONE)
public class PinningResponse {
    private String PinHash;
    private Long PinSize;
    private Date PinDate;
    private String HashToPin;
    private String JobId;
    private String JobStatus;
}
