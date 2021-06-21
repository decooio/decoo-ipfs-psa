package com.decoo.psa.controller;


import com.decoo.psa.dao.PinFileDao;
import com.decoo.psa.domain.*;
import com.decoo.psa.service.auth.AuthService;
import com.decoo.psa.service.pinning.PinningService;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/psa")
public class PinningServiceController {

    @Autowired
    private AuthService authService;

    @Autowired
    private PinFileDao pinFileDao;

    @Autowired
    private PinningService pinningService;

    @GetMapping(value = "/pins")
    public PinResults pinsGet(@Size(min=1,max=10)  @Valid @RequestParam(value = "cid", required = false) Set<String> cid,
                              @Size(max=255) @Valid @RequestParam(value = "name", required = false) String name,
                              @Valid @RequestParam(value = "match", required = false) TextMatchingStrategy match,
                              @Size(min=1) @Valid @RequestParam(value = "status", required = false) Set<Status> status,
                              @Valid @RequestParam(value = "before", required = false)
                              @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
                              Date before,
                              @Valid @RequestParam(value = "after", required = false)
                              @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
                              Date after,
                              @Min(1) @Max(1000) @ApiParam(value = "Max records to return", defaultValue = "10")
                              @Valid @RequestParam(value = "limit", required = false, defaultValue="10") Integer limit,
                              @Valid @RequestParam(value = "", required = false) Map<String, String> meta) {
        Long uid = authService.uid();
        AtomicBoolean containsPinned = new AtomicBoolean(false);
        // pinned contains in_ipfs_cluster and in_order
        List<Integer> stateList = null;
        if (status != null) {
            stateList = status.stream().map(i -> {
                switch (i) {
                    case FAILED:
                        return PinFile.FileState.EXPIRED.getCode();
                    case PINNED:
                        containsPinned.set(true);
                        return PinFile.FileState.IN_IPFS_CLUSTER.getCode();
                    default:
                        return PinFile.FileState.IN_PIN_QUEUE.getCode();
                }
            }).collect(Collectors.toList());
            if (containsPinned.get()) {
                stateList.add(PinFile.FileState.IN_ORDER.getCode());
            }
            stateList = stateList.stream().distinct().collect(Collectors.toList());
        }
        // remove param
        removeBaseParam(meta);
        Integer count = pinFileDao.selectPinFileCountByPinningService(cid, name, stateList, before, after, meta, uid, match == null ? null : match.getValue());
        List<PinFileState> list = pinFileDao.selectPinFileByPinningService(cid, name, stateList, before, after, limit, meta, uid, match == null ? null : match.getValue());
        PinResults pinResults = new PinResults();
        pinResults.setCount(count);
        pinResults.setResults(list.stream().map(PinStatus::convertByPinFileState).collect(Collectors.toSet()));
        return pinResults;
    }

    private void removeBaseParam(Map<String, String> meta) {
        meta.remove("limit");
        meta.remove("after");
        meta.remove("before");
        meta.remove("status");
        meta.remove("name");
        meta.remove("match");
        meta.remove("cid");
    }

    @PostMapping(value = "/pins")
    public PinStatus pinsPost(@ApiParam(value = "" ,required=true )  @Valid @RequestBody Pin pin) {
        return pinningService.pinFromPinningService(pin);
    }

    @DeleteMapping(value = "/pins/{requestid}")
    public void pinsRequestidDelete(@ApiParam(value = "",required=true) @PathVariable("requestid") String requestid) {
        pinningService.removeByRequestIdAndUid(requestid, authService.uid());
    }

    @GetMapping(value = "/pins/{requestid}")
    public PinStatus pinsRequestidGet(@ApiParam(value = "",required=true) @PathVariable("requestid") String requestid) {
        PinStatus pinStatus = pinningService.getPinStatusByRequestId(requestid);
        if (pinStatus == null) {
            throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
        }
        return pinStatus;
    }

    @PostMapping(value = "/pins/{requestid}")
    public PinStatus pinsRequestidPost(@ApiParam(value = "",required=true) @PathVariable("requestid") String requestid,
                                       @ApiParam(value = "" ,required=true ) @Valid @RequestBody Pin pin) {
        pinningService.removeByRequestIdAndUid(requestid, authService.uid());
        return pinningService.pinFromPinningService(pin);
    }

}
