package com.decoo.psa.service.crust;


import com.decoo.psa.domain.CrustOrderResult;
import com.decoo.psa.exception.CrustException;

public interface CrustService {
    public CrustOrderResult orderByCidAndSize(String cid, Long size) throws CrustException;

    public CrustOrderResult orderStateByCid(String cid) throws CrustException;
}
