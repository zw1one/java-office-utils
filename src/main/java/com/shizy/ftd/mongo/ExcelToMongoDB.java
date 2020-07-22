package com.shizy.ftd.mongo;

import com.alibaba.excel.context.AnalysisContext;
import com.shizy.ftd.excel.ReadExcel;
import com.shizy.ftd.excel.ReadSheel;
import com.shizy.ftd.schema.ExcelSchema;
import com.shizy.ftd.util.StringUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class ExcelToMongoDB {

    private static final Logger logger = LoggerFactory.getLogger(ExcelToMongoDB.class);
    List resultList = Collections.synchronizedList(new ArrayList());
    private ExcelSchema excelBiz;
    private ExecutorService executor = Executors.newCachedThreadPool();

    public ExcelToMongoDB(ExcelSchema excelBiz) {
        this.excelBiz = excelBiz;
    }

    public void processExcel(String filePath) {

        //filePath is null
        if (StringUtils.isBlack(filePath)) {
            try {
                throw new Exception("filePath[" + filePath + "] is null!");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        //start read
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(new File(filePath)));
            processExcel(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public Object[] processExcel(InputStream inputStream) {

        logger.info("");
        logger.info("============================================================");
        logger.info("starting import xlsx data to MongoDB!");
        logger.info("============================================================");
        logger.info("");

        logger.info("reading xlsx...");
        logger.info("");

        //读完一页或一页读了1000行的处理 自定义的接口
        ReadSheel readSheel = (context, data) -> {
            logger.info("-- " + "reading sheet " + context.getCurrentSheet().getSheetNo() + " [" + context.getCurrentSheet().getSheetName() + "]" + "...");
            processData(context, data);
        };

        long a = System.currentTimeMillis();

        //开读!
        new ReadExcel(inputStream, 0, readSheel)
                .read();

        //等待所有子线程执行完毕
        executor.shutdown();
        while (!executor.isTerminated()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long b = System.currentTimeMillis();

        logger.info("");
        logger.info("============================================================");
        logger.info("ending import xlsx data to MongoDB!");
        logger.info("============================================================");

        logger.info("use time: " + (b - a) + "ms");

        //处理返回的展示结果
        Map rstMap = (Map) resultList.stream().collect(Collectors.groupingBy(ResultVo::getSheetNo));

        List<ResultVo> sumList = new ArrayList();
        rstMap.forEach((k, v) -> {//k是sheetNo，v是sheetNo的list

            List<ResultVo> valueList = (List<ResultVo>) v;
            int sumReadCnt = 0;
            int sumWriteCnt = 0;

            for (ResultVo resultVo : valueList) {
                sumReadCnt += resultVo.getReadCnt();
                sumWriteCnt += resultVo.getWriteCnt();
            }

            ResultVo sumResultVo = new ResultVo(valueList.get(0).getSheetNo(), valueList.get(0).getSheetName(), sumReadCnt, sumWriteCnt);
            sumResultVo.setReadCnt(sumReadCnt);
            sumResultVo.setWriteCnt(sumWriteCnt);
            sumList.add(sumResultVo);
        });

        List resultList = sumList.stream().sorted(
                Comparator.comparing(ResultVo::getSheetNo)
        ).collect(Collectors.toList());

        return new Object[]{resultList, (b - a)};
    }

    /**
     * 读完一页或一页读了1000行的处理
     * <p>
     * 这里将读取的excel数据进行：
     * 1、按业务进行数据处理
     * 2、多线程写入数据库，因为写入的基本都是不同的表，不是单表，多线程可以提高效率。
     */
    private void processData(AnalysisContext context, List<Map> param) {

        int sheetNo = context.getCurrentSheet().getSheetNo();
        String sheetName = context.getCurrentSheet().getSheetName();

        //获取处理过后的，要插入的数据
        Object[] bizParam = excelBiz.process(context, param);

        //3、insert into databae
        Runnable callable = () -> {

            MongoUtils.insertListMap((List<Map>) bizParam[0], (String) bizParam[1], (String) bizParam[2]);
            logger.info("-- writing sheet " + sheetNo + " [" + sheetName + "]" + " to MongoDB...");

            resultList.add(new ResultVo(sheetNo, sheetName, param.size(), ((List<Map>) bizParam[0]).size()));
        };

        /**
         * 使用executor.execute()和executor.submit()时，主线程不等待。
         *
         * 区别：
         *  execute()会在主线程抛出子线程的异常，submit()不会抛出线程的运行时异常，会放在线程返回值中。
         *
         *  submit().get()获取线程返回值时(需将Runnable改成Callable)，主线程会等待。
         *  submit()的返回值是线程执行信息，该方法不等待。submit().get()的返回值是线程执行结果，该方法会使主线程等待。
         *
         *  使用executor.invokeAll()执行所有线程并获取执行结果时，主线程会等待。
         */

        /**
         * 若读取速度太快，数据量过大，写入速度跟不上，导致内存中的过多而溢栈时。
         * 则改成几个Runnable就执行一次，执行完成前，主线程等待，不继续读取数据。
         * 执行完成后，若有需要，手动释放内存。再继续读取。
         */

        executor.execute(callable);

    }
}












