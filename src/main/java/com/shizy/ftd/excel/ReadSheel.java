package com.shizy.ftd.excel;

import com.alibaba.excel.context.AnalysisContext;

import java.util.List;
import java.util.Map;

public interface ReadSheel {
    public void doAfterReadSheel(AnalysisContext context, List<Map> data);
}
