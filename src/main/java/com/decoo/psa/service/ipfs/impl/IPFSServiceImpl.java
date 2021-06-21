package com.decoo.psa.service.ipfs.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.decoo.psa.constants.IPFSConstants;
import com.decoo.psa.domain.IPFSFile;
import com.decoo.psa.exception.IPFSException;
import com.decoo.psa.service.ipfs.IPFSService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
public class IPFSServiceImpl implements IPFSService {

    public static int times = 0;

    @Autowired
    RestTemplate restTemplate;

    private String replaceHost (String url, String host) {
        return StringUtils.isEmpty(host) ? url : url.replaceAll(IPFSConstants.IPFS_CLUSTER_HOST, host);
    }


    @Override
    public void mkdir(String folderPath, String host) throws IPFSException {
        String url = replaceHost(IPFSConstants.IPFS_URL_FILE_MKDIR, host);
        try {
            restTemplate.postForObject(url + "?arg=" + folderPath , null, String.class);
        } catch (Throwable e) {
            log.error("ipfs mkdir err:" + e.getMessage());
            throw new IPFSException("create IPFS dir err");
        }
    }

    @Override
    public IPFSFile addCluster(File file, IPFSConstants.CidVersion cidVersion) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Basic " + IPFSConstants.IPFS_CLUSTER_AUTH_PASSWORD);

        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file", new FileSystemResource(file));

        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);
        String url = IPFSConstants.IPFS_URL_CLUSTER_ADD + (IPFSConstants.CidVersion.CID0.getCode().equals(cidVersion.getCode()) ? "" : "&cid-version=1");
        String s = restTemplate.postForObject(url, files, String.class);
        Assert.notNull(s, "ipfs ping file response not null");
        log.info("file response {}", s);
        JSONObject jsonObject = JSON.parseObject(s);
        JSONObject cidJson = jsonObject.getJSONObject("cid");
        Assert.notNull(cidJson, "ping file result parse not null");
        String cid = cidJson.getString("/");
        Long size = jsonObject.getLong("size");
        Assert.notNull(cidJson, "ping file result parse not null");
        Assert.notNull(size, "ping file pinSize not null");
        return new IPFSFile().setCid(cid).setSize(size);
    }

    @Override
    public IPFSFile addCluster(InputStream inputStream, IPFSConstants.CidVersion cidVersion) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Basic " + IPFSConstants.IPFS_CLUSTER_AUTH_PASSWORD);

        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("file", new InputStreamResource(inputStream));

        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);
        String url = IPFSConstants.IPFS_URL_CLUSTER_ADD + (IPFSConstants.CidVersion.CID0.getCode().equals(cidVersion.getCode()) ? "" : "&cid-version=1");
        String s = restTemplate.postForObject(url, files, String.class);
        Assert.notNull(s, "ipfs ping file response not null");
        log.info("file response {}", s);
        JSONObject jsonObject = JSON.parseObject(s);
        JSONObject cidJson = jsonObject.getJSONObject("cid");
        Assert.notNull(cidJson, "ping file result parse not null");
        String cid = cidJson.getString("/");
        Long size = jsonObject.getLong("size");
        Assert.notNull(cidJson, "ping file result parse not null");
        Assert.notNull(size, "ping file pinSize not null");
        return new IPFSFile().setCid(cid).setSize(size);
    }

    @Override
    public IPFSFile addClusterInDefaultDir(File file, IPFSConstants.CidVersion cidVersion) throws IOException {
        return addClusterInDefaultDir(RequestBody.create(okhttp3.MediaType.parse("application/octet-stream"),
                file), file.getName(), cidVersion);
    }

    @Override
    public IPFSFile addClusterInDefaultDir(String fileName, InputStream inputStream, IPFSConstants.CidVersion cidVersion) throws IOException {
        return addClusterInDefaultDir(createRequestBody(okhttp3.MediaType.parse("application/octet-stream"), inputStream), fileName, cidVersion);
    }

    @Override
    public IPFSFile addClusterInDefaultDir(RequestBody requestBody, String fileName, IPFSConstants.CidVersion cidVersion) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(300l, TimeUnit.SECONDS)
                .writeTimeout(300l, TimeUnit.SECONDS)
                .readTimeout(300l, TimeUnit.SECONDS).build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("filename", null,
                        RequestBody.create(okhttp3.MediaType.parse("application/x-directory"), "default".getBytes()))
                .addFormDataPart("file",fileName,
                        requestBody)
                .build();
        String url = IPFSConstants.IPFS_URL_CLUSTER_ADD + (IPFSConstants.CidVersion.CID0.getCode().equals(cidVersion.getCode()) ? "" : "&cid-version=1");
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Authorization", "Basic " + IPFSConstants.IPFS_CLUSTER_AUTH_PASSWORD)
                .build();
        Response response = client.newCall(request).execute();
        String[] cids = response.body().string().split("\n");
        IPFSFile ipfsFile = new IPFSFile();
        for (String cid: cids) {
            JSONObject jsonObject = JSON.parseObject(cid);
            String name = jsonObject.getString("name");
            if (fileName.equals(name)) {
                ipfsFile.setCid(jsonObject.getJSONObject("cid").getString("/"))
                        .setSize(jsonObject.getLong("size"));
            } else {
                ipfsFile.setFolderCid(jsonObject.getJSONObject("cid").getString("/"))
                        .setFolderSize(jsonObject.getLong("size"));;
            }
        }
        return ipfsFile;
    }

    @Override
    public void pinClusterByCid(String cid) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + IPFSConstants.IPFS_CLUSTER_AUTH_PASSWORD);
            restTemplate.exchange(IPFSConstants.IPFS_URL_CLUSTER_PIN + cid, HttpMethod.POST,
                    new HttpEntity(headers), String.class, new Object());
        } catch (Throwable e) {
            log.error("ipfs cluster pin err:" + e.getMessage());
        }
    }

    @Override
    public IPFSConstants.ClusterPinStatus getClusterPinStatus(String cid) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + IPFSConstants.IPFS_CLUSTER_AUTH_PASSWORD);
            ResponseEntity<String> response = restTemplate.exchange(IPFSConstants.IPFS_URL_CLUSTER_PIN + cid, HttpMethod.GET,
                    new HttpEntity(headers), String.class, new Object());
            if (response.getStatusCode().equals(HttpStatus.OK) && !StringUtils.isEmpty(response.getBody())) {
                JSONObject jsonObject = JSON.parseObject(response.getBody());
                JSONObject peers = jsonObject.getJSONObject("peer_map");
                for (String key: peers.keySet()) {
                    String status = peers.getJSONObject(key).getString("status");
                    log.info("getClusterPinStatus cid: {} status: {}", cid, status);
                    // cluster all the same
                    return IPFSConstants.ClusterPinStatus.valueOf(status.toUpperCase());
                }
            }
        } catch (Throwable e) {
            log.error("ipfs get cluster pin status err:" + e.getMessage());
        }
        return null;
    }

    public RequestBody createRequestBody(final okhttp3.MediaType mediaType, final InputStream inputStream) {
        return new RequestBody() {
            @Override
            public okhttp3.MediaType contentType() {
                return mediaType;
            }

            @Override
            public long contentLength() {
                try {
                    return inputStream.available();
                } catch (IOException e) {
                    return 0;
                }
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source = null;
                try {
                    source = Okio.source(inputStream);
                    sink.writeAll(source);
                } finally {
                    Util.closeQuietly(source);
                }
            }
        };
    }

    @Override
    public void fileCp(String cid, String folderPath, String host) throws IPFSException {
        String url = replaceHost(IPFSConstants.IPFS_URL_FILE_CP, host);
        try {
            log.info("cp url:" + url + "?arg=/ipfs/" + cid + "&arg=" + folderPath);
            restTemplate.postForObject(url + "?arg=/ipfs/" + cid + "&arg=" + folderPath,
                    null, String.class);
        } catch (Throwable e) {
            log.error("ipfs file cp failed:" + e.getMessage());
            throw new IPFSException("ipfs file cp failed");
        }
    }

    @Override
    public IPFSFile fileStat(String arg, String host) throws IPFSException {
        String url = replaceHost(IPFSConstants.IPFS_URL_FILE_STAT, host);
        try {
            String stat = restTemplate.postForObject(url + "?arg=" + arg,
                    null, String.class);
            JSONObject jsonObject = JSON.parseObject(stat);
            Assert.notNull(jsonObject, "dir stat not null");
            if (!StringUtils.isEmpty(jsonObject.getString("Hash"))) {
                return new IPFSFile().setSize(jsonObject.getLong("CumulativeSize"))
                        .setCid(jsonObject.getString("Hash"));
            }
            throw new IPFSException("get ipfs stat failed");
        } catch (Throwable e) {
            log.error("ipfs file stat err:" + e.getMessage());
            throw new IPFSException("get file stat err");
        }
    }

    @Override
    public IPFSFile fileLs(String cid, String host) throws IPFSException {
        String url = replaceHost(IPFSConstants.IPFS_URL_FILE_LS, host);
        try {
            String ls = restTemplate.getForObject(url + "?arg=" + cid, String.class);
            JSONObject jsonObject = JSON.parseObject(ls);
            Assert.notNull(jsonObject, "file ls not null");
            JSONObject obj = jsonObject.getJSONObject("Objects").getJSONObject(cid);
            return new IPFSFile().setCid(cid).setSize(obj.getLong("Size"));
        } catch (Throwable e) {
            log.error("ipfs file ls err:" + e.getMessage());
            throw new IPFSException("ipfs file ls err");
        }
    }

    @Override
    public List<String> peers() throws IPFSException {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + IPFSConstants.IPFS_CLUSTER_AUTH_PASSWORD);
        ResponseEntity<String> response = restTemplate.exchange(IPFSConstants.IPFS_URL_CLUSTER_PEERS, HttpMethod.GET,
                new HttpEntity(headers), String.class, new Object());
        if (response.getStatusCode().equals(HttpStatus.OK) && !StringUtils.isEmpty(response.getBody())) {
            List<String> result = new ArrayList<>();
            JSONArray array = JSONObject.parseArray(response.getBody());
            for (int i = 0; i < array.size(); i++) {
                JSONObject item = array.getJSONObject(i);
                JSONArray jsonArray = item.getJSONArray("addresses");
                jsonArray.stream().forEach(a -> {
                    String[] addresses = a.toString().split("/");
                    if (addresses.length > 2 && !StringUtils.isEmpty(addresses[2]) && addresses[2].startsWith("10.")) {
                        result.add(addresses[2]);
                    }
                });
            }
            return result;
        }
        log.error("get peers code: " + response.getStatusCode().value() + ", response: " +
                (response.getBody() == null ? "empty" : response.getBody()));
        throw new IPFSException("ipfs cluster get peers failed");
    }

    @Override
    public void unPinFiles(List<String> cids,String host) throws IPFSException {
        String url = replaceHost(IPFSConstants.IPFS_URL_PIN_RM, host);;
        try {
            restTemplate.postForObject(url + "?"
                            + String.join("&", cids.stream().map(i -> "arg=/ipfs/" + i)
                            .collect(Collectors.toList())),
                    null, String.class);
        } catch (Throwable e) {
            log.error("un pin files stat err:" + e.getMessage());
            throw new IPFSException("un pin files stat err");
        }
    }

    @Override
    public void unPinInCluster(String cid) throws IPFSException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + IPFSConstants.IPFS_CLUSTER_AUTH_PASSWORD);
            ResponseEntity<String> response = restTemplate.exchange(IPFSConstants.IPFS_URL_CLUSTER_UNPIN + cid, HttpMethod.DELETE,
                    new HttpEntity(headers), String.class, new Object());
        } catch (Throwable e) {
            log.error("unpin file err:" + e);
            throw new IPFSException("unpin file failed");
        }

    }

    @Override
    public void pinFile(String cid, String host) throws IPFSException {
        String url = replaceHost(IPFSConstants.IPFS_URL_PIN_ADD, host);
        try {
            restTemplate.postForObject(url + "?arg=/ipfs/" + cid,
                    null, String.class);
        } catch (Throwable e) {
            log.error("pin add pinHash err:" + e.getMessage());
            throw new IPFSException("pin add pinHash err");
        }
    }

    @Override
    public String getRandomHostByPeers() throws IPFSException {
        List<String> peerAddress = peers();
        Assert.notEmpty(peerAddress, "peers not empty");
        int randomPeer = times++ % peerAddress.size();
        times = times % peerAddress.size();
        return "http://" + peerAddress.get(randomPeer) + ":" + IPFSConstants.IPFS_NODE_PORT;
    }

    @Override
    public void swarmConnect(Set<String> origins){
        try {
            List<String> peerAddress = peers();
            Assert.notEmpty(peerAddress, "peers not empty");
            for (String address : peerAddress) {
                String host = "http://" + address + ":" + IPFSConstants.IPFS_NODE_PORT;
                String url = replaceHost(IPFSConstants.IPFS_URL_SWARM_CONNECT, host);
                for (String arg : origins) {
                    String result = restTemplate.postForObject(url + "?arg=" + arg,
                            null, String.class);
                    log.info("swarm result: {}", result);
                }
            }
        } catch (Throwable e) {
            log.error("swarm connect err:" + e.getMessage());
        }
    }


}
