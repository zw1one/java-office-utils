package com.shizy.office.poi.xssf;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelRead {

    public JSONObject process(MultipartFile file, Map<String, Object> params) throws Exception {

        System.out.println("读excel");
        List data = null;
        try (InputStream im = file.getInputStream()) {
            data = readExcel(im);
        }

        System.out.println("写入数据");

        System.out.println("处理完成");

        return null;
    }

    private List readExcel(InputStream file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file);

//        for (Sheet xssfSheet : workbook) {
//            if (xssfSheet == null) {
//                continue;
//            }
//            readSheet(xssfSheet, 1);
//        }

        Sheet sheet = workbook.getSheetAt(0);
        return readSheet(sheet, 1, columnName);
    }

    private List readSheet(Sheet xssfSheet, Integer headNum, Map columnName) {

        List data = new ArrayList();
        List head = new ArrayList();

        for (int rowi = 0; rowi < xssfSheet.getLastRowNum(); rowi++) {
            Row row = xssfSheet.getRow(rowi);

            if (row == null) {
                return data;
            }

            if (rowi + 1 == headNum) {
                head = getHead(row);
                continue;
            }

            Map record = new HashMap();
            for (int rowiColumni = 0; rowiColumni < head.size(); rowiColumni++) {
                Cell cell = row.getCell(rowiColumni);
                String value = null;
                if (cell != null) {
                    value = cell.toString();
                }
                record.put(columnName.get(head.get(rowiColumni)), value);
            }
            data.add(record);
        }
        return data;
    }

    private List getHead(Row row) {

        List head = new ArrayList();

        for (int rowiColumni = 0; rowiColumni < row.getPhysicalNumberOfCells(); rowiColumni++) {
            Cell cell = row.getCell(rowiColumni);
            String value = null;
            if (cell != null) {
                cell.setCellType(CellType.STRING);
                value = cell.getStringCellValue();
            }
            head.add(value);
        }

        return head;
    }

    private static Map columnName = new HashMap();

    static {
        columnName.put("专业标准分类", "zybzfl");
        columnName.put("专业大类代码", "zydldm");
        columnName.put("专业大类名称", "zydlmc");
        columnName.put("专业代码", "zydm");
        columnName.put("专业名称", "zymc");
        columnName.put("质量类型", "zllx");
        columnName.put("指标名称", "zbmc");
        columnName.put("单位", "dw");
        columnName.put("指标类型", "zblx");
        columnName.put("预期值", "yqz");
        columnName.put("数据类型", "sjlx");
        columnName.put("国家标准", "gjbz");
        columnName.put("一级标准", "yjbz");
        columnName.put("二级标准", "ejbz");
        columnName.put("三级标准", "sjbz");
        columnName.put("版本", "bb");
        columnName.put("指标说明", "zbsm");
        columnName.put("指标名称映射", "zbmcys");

    }


}































