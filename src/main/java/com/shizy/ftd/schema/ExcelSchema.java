package com.shizy.ftd.schema;

import com.alibaba.excel.context.AnalysisContext;

import java.util.List;
import java.util.Map;

public interface ExcelSchema {

    public Object[] process(AnalysisContext context, List<Map> param);
}
