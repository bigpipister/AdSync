package com.cht.directory.connector.service.objects;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by sychuang on 2018/8/21.
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class DeleteObject extends BaseObject implements Serializable {

    private String dn;
    private String logname;
}
