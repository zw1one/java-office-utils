package com.shizy.controller;

import com.shizy.ftd.mongo.ExcelToMongoDB;
import com.shizy.ftd.mongo.MongoUtils;
import com.shizy.ftd.mongo.ResultVo;
import com.shizy.ftd.schema.ExcelSchema;
import com.shizy.ftd.schema.impl.DemoExcelSchema;
import com.shizy.ftd.util.PropertiesUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

//@ApiIgnore
@RestController
@RequestMapping("/ftd")
@Api(tags = "FileToDatabase", description = "文件导入到数据库")
public class FileToDatabaseController {

    private static final Logger logger = LoggerFactory.getLogger(FileToDatabaseController.class);

    @ApiOperation(value = "XLSX to MongoDB", notes = "")
    @RequestMapping(value = "excel2mongo", method = RequestMethod.POST)
    public Map excel2mongo(@RequestParam("file") MultipartFile file, @RequestParam Map<String, Object> params) {

        Map rtnMap = new HashMap();
        String msg = "msg";

        if (!Objects.requireNonNull(file.getOriginalFilename()).contains(".xlsx")) {
            rtnMap.put("flag", false);
            rtnMap.put("msg", "file format error!");
            return rtnMap;
        }

        try (InputStream inputStream = file.getInputStream()) {
            //do this
            synchronized (FileToDatabaseController.class) {
                msg = start(inputStream);
            }
        } catch (Exception e) {
            e.printStackTrace();

            rtnMap.put("flag", false);
            rtnMap.put("msg", e.getMessage());
            return rtnMap;
        }

        rtnMap.put("flag", true);
        rtnMap.put("msg", msg);
        return rtnMap;
    }

    /**************************************************/

    private String start(InputStream inputStream) {

        initMongoDB();

        //do process Excel
        ExcelSchema excelBiz = new DemoExcelSchema();
        Object[] param = new ExcelToMongoDB(excelBiz).processExcel(inputStream);

        //parse result
        List<ResultVo> result = (List) param[0];
        long time = (long) param[1];

        StringBuilder rtn = new StringBuilder();
        rtn.append("<br>").append("--------------------------------").append("<br>");
        rtn.append("use time: ").append(time).append("ms").append("<br><br>");

        for (ResultVo resultVo : result) {
            rtn.append("SheetNo=").append(resultVo.getSheetNo()).append(", ")
                    .append("SheetName=").append(resultVo.getSheetName()).append(", ")
                    .append("recordSize=").append(resultVo.getWriteCnt())
                    .append("<br>");
        }

        rtn.append("--------------------------------").append("<br>");

        return rtn.toString();
    }

    private void initMongoDB() {
        //read properties
        Properties properties = PropertiesUtils.getPropertiesInProject("config.properties");

        String mongoDBhost = properties.getProperty("mongoDB.host", "localhost");
        int mongoDBport = Integer.parseInt(properties.getProperty("mongoDB.port", "27017"));
        String mongoDBuser = properties.getProperty("mongoDB.user");
        String mongoDBpassword = properties.getProperty("mongoDB.password");
        String mongoDBdatabase = properties.getProperty("mongoDB.database");

        //print properties
        logger.info("");
        logger.info("=======================Properties Param========================");
        logger.info("mongoDB.host: " + mongoDBhost);
        logger.info("mongoDB.port: " + mongoDBport);
        logger.info("mongoDB.user: " + mongoDBuser);
        logger.info("mongoDB.password: " + mongoDBpassword);
        logger.info("mongoDB.database: " + mongoDBdatabase);

        logger.info("==============================================================");
        logger.info("");

        //init MongoDB
        MongoUtils.initMongoClient(
                mongoDBhost,
                mongoDBport,
                mongoDBuser,
                mongoDBpassword,
                mongoDBdatabase
        );
    }

}



























