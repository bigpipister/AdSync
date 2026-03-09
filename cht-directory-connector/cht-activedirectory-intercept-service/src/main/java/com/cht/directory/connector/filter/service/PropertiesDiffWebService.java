package com.cht.directory.connector.filter.service;

import com.cht.directory.connector.filter.service.model.PropertiesDiffModel;
import com.cht.directory.connector.filter.service.model.PropertiesDiffResultModel;
import com.cht.directory.connector.filter.service.model.PwdDiffModel;
import com.cht.directory.connector.filter.service.model.PwdDiffResultModel;
import com.cht.directory.connector.filter.web.entity.WebConnectorSpaceAdPersonDetails;
import com.cht.directory.connector.filter.web.entity.WebConnectorSpaceAdPersonSyncDetails;
import com.cht.directory.connector.filter.web.repository.WebConnectorSpaceAdPersonDetailsRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PropertiesDiffWebService {

    private static final Logger log = LoggerFactory.getLogger(PropertiesDiffWebService.class);

    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private WebConnectorSpaceAdPersonDetailsRepository webConnectorSpaceAdPersonDetailsRepository;

    public PropertiesDiffResultModel getInvalidPropertiesDiff(int page, int size) {

        int actualPage = Math.max(0, page - 1);
        int total = 0;
        List<Object[]> propertiesInvalidList = webConnectorSpaceAdPersonDetailsRepository.findInvalidPropertiesDiff();

        List<PropertiesDiffModel> resultList = new ArrayList<>();
        List<PropertiesDiffModel> propertiesDiffModelList = new ArrayList<>();
        if (propertiesInvalidList != null) {
            for (Object[] object : propertiesInvalidList) {

                WebConnectorSpaceAdPersonDetails connectorSpaceAdPersonDetails = (WebConnectorSpaceAdPersonDetails) object[0];
                WebConnectorSpaceAdPersonSyncDetails connectorSpaceAdPersonSyncDetails = (WebConnectorSpaceAdPersonSyncDetails) object[1];

                // dnHash 、 pager 跟 userprincipalname 可能會有差異，已在entity裡額外設定忽略掉
                String innerJsonString = "";
                try {
                    innerJsonString = mapper.writeValueAsString(connectorSpaceAdPersonDetails);
                } catch (Exception e) {
                    innerJsonString = e.getMessage();
                }
                innerJsonString = innerJsonString.replaceAll(",OU=MOF,[^,]*,DC=gov,DC=tw", "");

                String externalJsonString = "";
                try {
                    externalJsonString = mapper.writeValueAsString(connectorSpaceAdPersonSyncDetails);
                } catch (Exception e) {
                    externalJsonString = e.getMessage();
                }
                externalJsonString = externalJsonString.replaceAll(",OU=MOF,[^,]*,DC=gov,DC=tw", "");

                if (innerJsonString.equalsIgnoreCase(externalJsonString)) {
                    continue; // Skip if inner and external properties are the same
                } else {
                    total++; // Count only if there is a difference
                }

                PropertiesDiffModel propertiesDiffModel = new PropertiesDiffModel();
                propertiesDiffModel.setSamaccountname(connectorSpaceAdPersonDetails.getSamaccountname());
                propertiesDiffModel.setSn(connectorSpaceAdPersonDetails.getSn());
                propertiesDiffModel.setDn(connectorSpaceAdPersonDetails.getDn());
                propertiesDiffModel.setInnerProperties(innerJsonString);
                propertiesDiffModel.setExternalProperties(externalJsonString);

                propertiesDiffModelList.add(propertiesDiffModel);
            }
        }
        int fromIndex = actualPage * size;
        int toIndex = Math.min(fromIndex + size, propertiesDiffModelList.size());

        // 避免 index overflow 錯誤
        if (fromIndex < propertiesDiffModelList.size()) {
            resultList = propertiesDiffModelList.subList(fromIndex, toIndex);
        }

        PropertiesDiffResultModel propertiesDiffResultModel = new PropertiesDiffResultModel();
        propertiesDiffResultModel.setResult("T");
        propertiesDiffResultModel.setMessage("");
        propertiesDiffResultModel.setData(resultList);
        propertiesDiffResultModel.setTotal(total);

        return propertiesDiffResultModel;
    }
}
