package com.decoo.psa.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import java.util.Map;
import java.util.Set;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DecooMetadata {
    @Length(max = 64, message = "name max length 64")
    private String name;
    private Map<String, String> keyValues;
    private Set<String> origins;
}
