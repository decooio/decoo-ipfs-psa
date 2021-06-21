package com.decoo.psa.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CrustOrderResult {
    private String cid;
    private Long fileSize;
    private Long expiredOn;
    private Long calculatedAt;
    private Long amount;
    private Integer reportedReplicaCount;
}
