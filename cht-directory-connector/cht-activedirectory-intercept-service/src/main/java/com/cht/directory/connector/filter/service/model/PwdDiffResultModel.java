package com.cht.directory.connector.filter.service.model;

import lombok.Data;

import java.util.List;

@Data
public class PwdDiffResultModel {

    private String result;
    private String message;
    private List<PwdDiffModel> data;
    private int total;

}
