package com.decoo.psa.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PinFileState extends PinFile {
    private Integer jobStatus;
}
