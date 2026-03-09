package com.cht.directory.connector.service.objects;

import java.io.Serializable;
import java.util.Map;

import com.cht.directory.connector.type.AuditLogsCategoryType;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by sychuang on 2018/8/21.
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class AddObject extends BaseObject implements Serializable {

    private String dn;
    private Map<String, String> attributes;
    private Map<String, String> binaryAttributes;
}
