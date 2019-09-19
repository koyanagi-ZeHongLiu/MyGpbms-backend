package com.example.gpbms.util;

import net.sf.jxls.transformer.XLSTransformer;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导出至Excel
 *
 */
public class ExcelUtil {
    
    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);
    
    /**
     * 导出数据, 多个sheet
     * @param in
     * @param out
     * @param outputList
     * @param amountPerSheet
     * @param sheetName
     * @param beanName
     */
    public static void innerExportMultiSheet(InputStream in, OutputStream out, List outputList, int amountPerSheet, String sheetName, String beanName) {
        
        //数据数量不超过1个sheet，则直接导出，不分sheet
        if(outputList.size() <= amountPerSheet) {
            innerExport(in, out, MapUtil.asMap(beanName, outputList));
        } else {
            
            List<List> listForSheet = new ArrayList<>();
            List<String> sheetNames = new ArrayList<>();
            
            for(int i = 0; i < (int) Math.ceil((double) outputList.size() / amountPerSheet); i++) {
                
                int k = ((i + 1) * amountPerSheet < outputList.size()) ? ((i + 1) * amountPerSheet) : outputList.size();
                List resultList = outputList.subList(i * amountPerSheet, k);
                
                String perSheetName = sheetName + "_" + String.valueOf(i + 1);
                
                listForSheet.add(resultList);
                sheetNames.add(perSheetName);
            }
            innerExportMultiSheet(in, out, listForSheet, sheetNames, beanName);
        }
    }
    
    public static void innerExport(InputStream in, OutputStream out, Map params) {
        XLSTransformer transformer = new XLSTransformer();
        
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new BufferedInputStream(in);
            org.apache.poi.ss.usermodel.Workbook workbook = transformer.transformXLS(is, params);
            os = new BufferedOutputStream(out);
            workbook.write(os);
            os.flush();
        } catch(Exception e) {
            logger.error("Excel导出出错>>", e);
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
                os.close();
            } catch(IOException e) {
                logger.error("Excel导出出错>>", e);
            }
        }
    }
    
    /**
     * 导出数据, 多个sheet
     * @param in
     * @param out
     * @param params
     * @param sheetNames
     * @param beanName
     */
    private static void innerExportMultiSheet(InputStream in, OutputStream out, List params, List sheetNames, String beanName) {
        XLSTransformer transformer = new XLSTransformer();
        
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new BufferedInputStream(in);
            Workbook workbook = transformer.transformMultipleSheetsList(is, params, sheetNames, beanName, new HashMap(), 0);
            os = new BufferedOutputStream(out);
            
            workbook.write(os);
            os.flush();
        } catch(Exception e) {
            logger.error("Excel导出出错>>", e);
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
                os.close();
            } catch(IOException e) {
                logger.error("Excel导出出错>>", e);
            }
        }
    }
}
