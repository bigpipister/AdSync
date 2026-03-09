package com.cht.directory.connector.filter.service;

import com.cht.directory.connector.filter.web.entity.WebConnectorSpaceAdPersonDetails;
import com.cht.directory.connector.filter.web.entity.WebConnectorSpaceAdPersonSyncDetails;
import com.cht.directory.connector.filter.web.repository.WebConnectorSpaceAdPersonDetailsRepository;
import com.cht.directory.connector.filter.service.model.PwdDiffModel;
import com.cht.directory.connector.filter.service.model.PwdDiffResultModel;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PwdDiffWebService {

    private static final Logger log = LoggerFactory.getLogger(PwdDiffWebService.class);

    @Autowired
    private WebConnectorSpaceAdPersonDetailsRepository webConnectorSpaceAdPersonDetailsRepository;

    public PwdDiffResultModel getInvalidPwdDiff(int page, int size) {

        Pageable pageable = PageRequest.of(page-1, size);

        int total = webConnectorSpaceAdPersonDetailsRepository.findAllInvalidPwdDiff().size();
        List<Object[]> pwdInvalidList = webConnectorSpaceAdPersonDetailsRepository.findInvalidPwdDiff(pageable);

        List<PwdDiffModel> pwdDiffModelList = new ArrayList<>();
        if (pwdInvalidList != null) {
            for (Object[] object : pwdInvalidList) {

                WebConnectorSpaceAdPersonDetails connectorSpaceAdPersonDetails = (WebConnectorSpaceAdPersonDetails) object[0];
                WebConnectorSpaceAdPersonSyncDetails connectorSpaceAdPersonSyncDetails = (WebConnectorSpaceAdPersonSyncDetails) object[1];
                PwdDiffModel pwdDiffModel = new PwdDiffModel();
                pwdDiffModel.setSamaccountname(connectorSpaceAdPersonDetails.getSamaccountname());
                pwdDiffModel.setSn(connectorSpaceAdPersonDetails.getSn());
                pwdDiffModel.setDn(connectorSpaceAdPersonDetails.getDn());
                pwdDiffModel.setInnerTimestamp(connectorSpaceAdPersonDetails.getPwdlastset());
                pwdDiffModel.setExternalTimestamp(connectorSpaceAdPersonSyncDetails.getPwdlastset());
                pwdDiffModelList.add(pwdDiffModel);
            }
        }
        PwdDiffResultModel pwdDiffResultModel = new PwdDiffResultModel();
        pwdDiffResultModel.setResult("T");
        pwdDiffResultModel.setMessage("");
        pwdDiffResultModel.setData(pwdDiffModelList);
        pwdDiffResultModel.setTotal(total);

        return pwdDiffResultModel;
    }
}
