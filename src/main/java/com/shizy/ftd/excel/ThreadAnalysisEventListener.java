package com.shizy.ftd.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.shizy.ftd.mongo.ExcelToMongoDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 只有一个AnalysisEventListener实例，来顺序读取所有页
 * <p>
 * 想修改成一个线程new一个AnalysisEventListener来读一页，未成功。
 */
public class ThreadAnalysisEventListener extends AnalysisEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ExcelToMongoDB.class);

    private Integer headLineMun;
    private ReadSheel readSheel;

    private List<String> titles;//列名
    private List<Map> data = new ArrayList();//数据

    public ThreadAnalysisEventListener(Integer headLineMun, ReadSheel readSheel) {
        this.headLineMun = headLineMun;
        this.readSheel = readSheel;
    }

    /**
     * 读完一行
     * 把读到的数据丢到dataList中
     */
    @Override
    public void invoke(Object object, AnalysisContext context) {
        List<String> rowData = (List<String>) object;
        //取title
        if (context.getCurrentRowNum().equals(headLineMun)) {
            titles = rowData;
            return;
        }
        //set data
        Map rowMap = new HashMap<String, Object>();
        for (int i = 0; i < rowData.size(); i++) {
            String rowStr = rowData.get(i);
            if (rowStr != null) {
                rowStr = rowStr;
            }
            rowMap.put(titles.get(i), rowStr);
        }
        data.add(rowMap);

        //读一页的数据时，list达到5000时，写入一次。否则list过大会溢栈
        if (data.size() >= 5000) {
            readSheel.doAfterReadSheel(context, data);
            logger.info("-- " + "commit sheet " + context.getCurrentSheet().getSheetNo() + " [" + context.getCurrentSheet().getSheetName() + "]" + " 提交数：" + data.size());
            data = new ArrayList();//清空上一次的data
        }
    }

    /**
     * 读完一页
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        readSheel.doAfterReadSheel(context, data);
        logger.info("-- " + "commit sheet " + context.getCurrentSheet().getSheetNo() + " [" + context.getCurrentSheet().getSheetName() + "]" + " 提交数：" + data.size());
//        logger.info("");
        data = new ArrayList();//清空上一次的data
    }
}























