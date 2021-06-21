package com.decoo.psa.domain;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class IPFSFile {
    private String folderCid;
    private Long folderSize;
    private String cid;
    private Long size;
}
