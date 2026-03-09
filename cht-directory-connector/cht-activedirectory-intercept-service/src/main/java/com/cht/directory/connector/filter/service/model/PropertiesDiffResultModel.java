package com.cht.directory.connector.filter.service.model;

import lombok.Data;

import java.util.List;

@Data
public class PropertiesDiffResultModel {

    private String result;
    private String message;
    private List<PropertiesDiffModel> data;
    private int total;

}
