package com.shizy.ftd.schema.impl;

import com.alibaba.excel.context.AnalysisContext;
import com.shizy.ftd.schema.ExcelSchema;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DemoExcelSchema implements ExcelSchema {

    @Override
    public Object[] process(AnalysisContext context, List<Map> param) {

        //collectionName
        String collectionName = context.getCurrentSheet().getSheetName();

        //databaseName
        String databaseName = "databaseTest";

        //insertData
        List<Map> insertData = new ArrayList<>();
        insertData = param;

        return new Object[]{insertData, collectionName, databaseName};
    }
}
















