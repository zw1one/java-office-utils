package com.shizy.ftd.excel;

import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;

import java.io.InputStream;


public class ReadExcel {

    private InputStream inputStream;

    private Integer headLineMun;
    private ReadSheel readSheel;

    public ReadExcel(InputStream inputStream, Integer headLineMun, ReadSheel readSheel) {
        this.inputStream = inputStream;
        this.headLineMun = headLineMun;
        this.readSheel = readSheel;
    }

    public void read() {
        final ExcelReader excelReader = new ExcelReader(inputStream, null, new ThreadAnalysisEventListener(headLineMun, readSheel));

        for (Sheet sheet : excelReader.getSheets()) {
            excelReader.read(sheet);
        }

        //多线程处理。提升效率
        //这个实现有问题，所有线程共用一个ThreadAnalysisEventListener实例了
//
//        List runList = new ArrayList<>();
//
//        for (Sheet sheet : excelReader.getSheets()) {
//            Callable callable = () -> {
//                excelReader.read(sheet);
//                return null;
//            };
//            runList.add(callable);
//        }
//
//        ExecutorService executor = newFixedThreadPool(4);
//        try {
//            executor.invokeAll(runList);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }
}














