package com.shizy.ftd.mongo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultVo {
    private int sheetNo;
    private String sheetName;
    private int readCnt;
    private int writeCnt;
}