package com.decoo.psa.service.pinning;


import com.decoo.psa.domain.DecooMetadata;
import com.decoo.psa.domain.PinFile;
import com.decoo.psa.domain.PinJobs;
import com.decoo.psa.exception.IPFSException;

public interface PinFileService {
    PinFile createBasePinJobAndPinFile(PinJobs jobs, PinFile pinFile, DecooMetadata decooMetadata);

    PinJobs createPinJobAndUpsertPinFileByPinFile(PinFile pinFile, PinJobs.PinJobStatus status, PinJobs.PinJobType pinJobType);

    void convertPinJobsToPinFile(PinJobs pinJobs) throws IPFSException;

    void convertExpirePinJobs(PinJobs pinJobs);
}
