package com.cht.directory.connector.service.objects;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by sychuang on 2018/8/21.
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ModifyObject extends BaseObject implements Serializable {
    private String dn;
    private List<Modification> modifications;

    private String logname;

    @Data
    public static class Modification implements Serializable {

        private int type;
        private String attribute;
        private String[] values;
        private boolean binary;
    }
}
