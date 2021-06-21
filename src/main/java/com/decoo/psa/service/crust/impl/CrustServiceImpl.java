package com.decoo.psa.service.crust.impl;

import com.alibaba.fastjson.JSONObject;
import com.decoo.psa.constants.IPFSConstants;
import com.decoo.psa.domain.CrustOrderResult;
import com.decoo.psa.exception.CrustException;
import com.decoo.psa.service.crust.CrustService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class CrustServiceImpl implements CrustService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public CrustOrderResult orderByCidAndSize(String cid, Long size) throws CrustException {
        JSONObject json = new JSONObject();
        json.put("fileCid", cid);
        json.put("fileSize", size);
        json.put("seeds", IPFSConstants.CRUST_SEEDS);
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity<String> formEntity = new HttpEntity<String>(json.toString(), headers);
        String s = restTemplate.postForEntity(IPFSConstants.CRUST_URL_ORDER,formEntity,String.class).getBody();
        log.info("order result:" + s);
        JSONObject result = JSONObject.parseObject(s);
        if (Integer.valueOf(1).equals(result.getInteger("code"))) {
            CrustOrderResult order =  orderStateByCid(cid);
            return new CrustOrderResult().setCid(cid).setFileSize(size)
                    .setAmount(order.getAmount())
                    .setCalculatedAt(order.getCalculatedAt())
                    .setExpiredOn(order.getExpiredOn())
                    .setReportedReplicaCount(order.getReportedReplicaCount());
        }
        log.error("order failed:" + s);
        throw new CrustException("crust order failed");
    }

    @Override
    public CrustOrderResult orderStateByCid(String cid) throws CrustException {
        String s = restTemplate.getForObject(IPFSConstants.CRUST_URL_ORDER_STAT + cid, String.class);
        JSONObject result = JSONObject.parseObject(s);
        if (Integer.valueOf(1).equals(result.getInteger("code"))) {
            JSONObject data = result.getJSONObject("data");
            return new CrustOrderResult().setCid(cid)
                    .setAmount(data.getLong("amount"))
                    .setCalculatedAt(data.getLong("calculated_at"))
                    .setExpiredOn(data.getLong("expired_on"))
                    .setReportedReplicaCount(data.getInteger("reported_replica_count"));
        }
        log.error("crust query order failed:" + s);
        throw new CrustException("crust query order failed");
    }
}
