package com.decoo.psa.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Accessors(chain = true)
public class PinningRequest {
    @NotBlank(message = "pinHash can not empty")
    @Pattern(regexp = "(^Qm[0-9a-zA-Z]{44}$)|(^ba[0-9a-zA-Z]{57}$)", message = "invalid pinHash (neither CID v0 nor CID v1)")
    private String hashToPin;
    @Valid
    private DecooMetadata decooMetadata;
}
