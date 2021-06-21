package com.decoo.psa.service.ipfs;


import com.decoo.psa.constants.IPFSConstants;
import com.decoo.psa.domain.IPFSFile;
import com.decoo.psa.exception.IPFSException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

public interface IPFSService {

    void mkdir(String folderPath, String host) throws IPFSException;

    IPFSFile addCluster(File file, IPFSConstants.CidVersion cidVersion);

    IPFSFile addCluster(InputStream inputStream, IPFSConstants.CidVersion cidVersion);

    IPFSFile addClusterInDefaultDir(File file, IPFSConstants.CidVersion cidVersion) throws IOException;

    IPFSFile addClusterInDefaultDir(String fileName, InputStream inputStream, IPFSConstants.CidVersion cidVersion) throws IOException;

    IPFSFile addClusterInDefaultDir(okhttp3.RequestBody requestBody, String fileName, IPFSConstants.CidVersion cidVersion) throws IOException;

    void pinClusterByCid(String cid);

    IPFSConstants.ClusterPinStatus getClusterPinStatus(String cid);

    void fileCp(String cid, String folderPath, String host) throws IPFSException;

    IPFSFile fileStat(String folderPath, String host) throws  IPFSException;

    IPFSFile fileLs(String cid, String host) throws IPFSException;

    List<String> peers() throws IPFSException;

    void unPinFiles(List<String> cids, String host) throws IPFSException;

    void unPinInCluster(String cid) throws IPFSException;

    void pinFile(String cid, String host) throws IPFSException;

    String getRandomHostByPeers() throws IPFSException;

    void swarmConnect(Set<String> origins);
}
