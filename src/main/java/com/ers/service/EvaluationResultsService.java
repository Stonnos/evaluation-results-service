package com.ers.service;

import com.ers.dto.EvaluationResultsRequest;
import com.ers.dto.EvaluationResultsResponse;
import com.ers.dto.ResponseStatus;
import com.ers.mapping.EvaluationResultsRequestMapper;
import com.ers.model.EvaluationResultsInfo;
import com.ers.model.InstancesInfo;
import com.ers.repository.EvaluationResultsInfoRepository;
import com.ers.repository.InstancesInfoRepository;
import com.ers.util.Utils;
import com.google.common.base.Charsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.xml.transform.StringResult;
import org.w3c.dom.Document;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implements service for saving evaluation results into database.
 *
 * @author Roman Batygin
 */
@Slf4j
@Service
public class EvaluationResultsService {

    private final EvaluationResultsRequestMapper evaluationResultsRequestMapper;
    private final EvaluationResultsInfoRepository evaluationResultsInfoRepository;
    private final InstancesInfoRepository instancesInfoRepository;

    private ConcurrentHashMap<String, Object> cachedIds = new ConcurrentHashMap<>();

    /**
     * Constructor with spring dependency injection.
     *
     * @param evaluationResultsRequestMapper  - evaluation results request mapper bean
     * @param evaluationResultsInfoRepository - evaluation results info repository bean
     * @param instancesInfoRepository         - instances info repository bean
     */
    @Inject
    public EvaluationResultsService(EvaluationResultsRequestMapper evaluationResultsRequestMapper,
                                    EvaluationResultsInfoRepository evaluationResultsInfoRepository,
                                    InstancesInfoRepository instancesInfoRepository) {
        this.evaluationResultsRequestMapper = evaluationResultsRequestMapper;
        this.evaluationResultsInfoRepository = evaluationResultsInfoRepository;
        this.instancesInfoRepository = instancesInfoRepository;
    }

    @PostConstruct
    public void init() {
        List<InstancesInfo> instancesInfoList = instancesInfoRepository.findByDataPathIsNotNull();
        if (!CollectionUtils.isEmpty(instancesInfoList)) {
            for (InstancesInfo instancesInfo : instancesInfoList) {
                try {
                    File file = new File(instancesInfo.getDataPath());
                    Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                    StringResult xmlData = new StringResult();
                    transformer.transform(new DOMSource(document), xmlData);
                    instancesInfo.setXmlData(xmlData.toString().getBytes(Charsets.UTF_8));
                    log.info("MD5 #{}: {}", instancesInfo.getId(), DigestUtils.md5DigestAsHex(instancesInfo.getXmlData()));
                    instancesInfo.setDataPath(null);
                    instancesInfoRepository.save(instancesInfo);
                } catch (Exception ex) {
                    log.error("There was an error for instances {}: {}", instancesInfo.getId(), ex.getMessage());
                }
            }
        }
    }

    /**
     * Saves evaluation results report into database.
     *
     * @param evaluationResultsRequest - evaluation results report
     * @return evaluation results response
     */
    public EvaluationResultsResponse saveEvaluationResults(EvaluationResultsRequest evaluationResultsRequest) {
        ResponseStatus responseStatus = ResponseStatus.SUCCESS;
        if (!Utils.hasRequestId(evaluationResultsRequest)) {
            log.error("Request id isn't specified!");
            responseStatus = ResponseStatus.INVALID_REQUEST_ID;
        } else {
            log.info("Starting to save evaluation results report with request id = {}.",
                    evaluationResultsRequest.getRequestId());
            cachedIds.putIfAbsent(evaluationResultsRequest.getRequestId(), new Object());
            synchronized (cachedIds.get(evaluationResultsRequest.getRequestId())) {
                if (evaluationResultsInfoRepository.existsByRequestId(evaluationResultsRequest.getRequestId())) {
                    log.warn("Evaluation results with request id = {} is already exists!",
                            evaluationResultsRequest.getRequestId());
                    responseStatus = ResponseStatus.DUPLICATE_REQUEST_ID;
                } else {
                    try {
                        EvaluationResultsInfo evaluationResultsInfo =
                                evaluationResultsRequestMapper.map(evaluationResultsRequest);
                        populateAndSaveInstancesInfo(evaluationResultsRequest, evaluationResultsInfo);
                        evaluationResultsInfo.setSaveDate(LocalDateTime.now());
                        evaluationResultsInfoRepository.save(evaluationResultsInfo);
                        log.info("Evaluation results report with request id = {} has been successfully saved.",
                                evaluationResultsRequest.getRequestId());
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                        responseStatus = ResponseStatus.ERROR;
                    }
                }
            }
            cachedIds.remove(evaluationResultsRequest.getRequestId());
        }
        return Utils.buildResponse(evaluationResultsRequest.getRequestId(), responseStatus);
    }

    private synchronized void populateAndSaveInstancesInfo(EvaluationResultsRequest evaluationResultsRequest,
                                       EvaluationResultsInfo evaluationResultsInfo) {
        if (evaluationResultsRequest.getInstances() != null) {
            String xmlData = evaluationResultsRequest.getInstances().getXmlInstances();
            if (!StringUtils.isEmpty(xmlData)) {
                byte[] xmlDataBytes = xmlData.getBytes(Charsets.UTF_8);
                String md5Hash = DigestUtils.md5DigestAsHex(xmlDataBytes);
                InstancesInfo instancesInfo = instancesInfoRepository.findByDataMd5Hash(md5Hash);
                if (instancesInfo != null) {
                    evaluationResultsInfo.setInstances(instancesInfo);
                } else {
                    evaluationResultsInfo.getInstances().setXmlData(xmlDataBytes);
                    evaluationResultsInfo.getInstances().setDataMd5Hash(md5Hash);
                    instancesInfoRepository.save(evaluationResultsInfo.getInstances());
                }
            } else {
                instancesInfoRepository.save(evaluationResultsInfo.getInstances());
            }
        }
    }
}
