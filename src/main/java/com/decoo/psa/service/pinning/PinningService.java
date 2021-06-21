package com.decoo.psa.service.pinning;


import com.decoo.psa.constants.IPFSConstants;
import com.decoo.psa.domain.*;

import java.io.IOException;
import java.io.InputStream;

public interface PinningService {

    public PinningResponse pinFileToIPFS(String fileName, InputStream inputStream, boolean wrapWithDirectory,
                                         IPFSConstants.CidVersion cidVersion, DecooMetadata decooMetadata) throws IOException;

    public PinningResponse pinByCid(PinningRequest pinningRequest);

    public PinStatus pinFromPinningService(Pin pin);

    public PinStatus getPinStatusByRequestId(String uuid);

    public void removeByRequestIdAndUid(String requestId, Long uid);
}
