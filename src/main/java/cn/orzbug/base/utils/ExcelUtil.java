package cn.orzbug.base.utils;


import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

import javax.mail.internet.MimeUtility;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author :SongQijie
 * @Description:
 * @Date:Created in 11:37 2017/10/20
 * @Modified:
 */
public class ExcelUtil {

    private final static Logger logger = Logger.getLogger(ExcelUtil.class);
    public static final String DEFAULT_CHARSET = "utf-8";


    /**
     * 批量导出
     *
     * @param <T>
     * @param list      列表数据
     * @param title     列表标题
     * @param excelName 导出的文件名
     * @param out
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws SecurityException
     */
    public static <T> void exportExcel(List<T> list, Map<String, String> title, String excelName, OutputStream out, int flag) throws IOException, IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
        // 2003版的excel
        HSSFWorkbook excel = new HSSFWorkbook();
        if (excelName == null || excelName.length() == 0) {
            excelName = "成绩";
        }
        HSSFSheet sheet = excel.createSheet(excelName);
        HSSFPalette palette = excel.getCustomPalette();
        HSSFCellStyle style1 = excel.createCellStyle();
        HSSFCellStyle style2 = excel.createCellStyle();
//        style1.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//        style1.setBorderTop(HSSFCellStyle.BORDER_THIN);
//        style1.setBorderRight(HSSFCellStyle.BORDER_THIN);
//        style1.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//        style2.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//        style2.setBorderTop(HSSFCellStyle.BORDER_THIN);
//        style2.setBorderRight(HSSFCellStyle.BORDER_THIN);
//        style2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//        palette.setColorAtIndex((short) 11, (byte) (221), (byte) (221), (byte) (221));
//        palette.setColorAtIndex((short) 12, (byte) (192), (byte) (192), (byte) (192));
//        palette.setColorAtIndex((short) 13, (byte) (255), (byte) (255), (byte) (255));
        HSSFRow comment = null;
        HSSFRow head = null;
        if (1 == flag) {
            comment = sheet.createRow(0);
            head = sheet.createRow(1);
            HSSFCellStyle commentStyle = excel.createCellStyle();
            HSSFFont commentFont = excel.createFont();
            commentFont.setColor(HSSFColor.RED.index);
            //字体大小
            commentFont.setFontHeight((short) 230);
            commentFont.setFontName("宋体");
            //粗体
            commentFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
            commentStyle.setFont(commentFont);
            HSSFCell commentCell = comment.createCell(0);
            commentCell.setCellValue("注：灰色单元格内容请不要修改！！！");
            commentCell.setCellStyle(commentStyle);
        } else {
            head = sheet.createRow(0);
        }
        // 创建表头
        HSSFCellStyle headStyle = excel.createCellStyle();
//        headStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//        headStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
//        headStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//        headStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        if (flag == 1) {
//            headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
//            headStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
//            headStyle.setFillForegroundColor((short) 12);
        }
        HSSFFont hssfFont = excel.createFont();
        //字体大小
        hssfFont.setFontHeight((short) 36);
        hssfFont.setFontName("宋体");
        //粗体
        hssfFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        String[] headers = new String[title.size()];
        int num = 0;
        for (String key : title.keySet()) {
            headers[num] = key;
            num++;
        }

        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = head.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headStyle);
        }

        // 创建内容
        if (list != null) {
            int index = 1;
            if (1 == flag) {
                index = 2;
            }
            for (Object object : list) {
                HSSFRow row = sheet.createRow(index++);
                int colIdx = 0;
                for (String values : title.values()) {
                    if (ReflectionUtil.getValueForFiled(object, values) != null) {
                        if (ReflectionUtil.getValueForFiled(object, values) instanceof LinkedList<?>) {
                            List<Map<String, Object>> list2 = (List<Map<String, Object>>) ReflectionUtil.getValueForFiled(object, values);
                            for (Map<String, Object> map : list2) {
                                for (String key : title.keySet()) {
                                    if (map.get(key) != null) {
                                        val(row, colIdx++, map.get("score").toString(), flag, style1, style2);
                                    }
                                }
                            }
                        } else {
                            if (!values.contains("score")) {
                                val(row, colIdx++, ReflectionUtil.getValueForFiled(object, values).toString(), flag, style1, style2);
                            }
                        }
                    } else {
                        if (!values.contains("score")) {
                            val(row, colIdx++, "", flag, style1, style2);
                        }
                    }
                }
            }
        }

        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        // 输出到流
        excel.write(out);
        excel.close();
    }


    public static String strVal(Cell cell) {
        try {
            Object val = val(cell);
            if (val == null){ return "";}
            return String.valueOf(val);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取单元格的值
     *
     * @param cell
     * @return
     */
    public static Object val(Cell cell) {
        if (cell == null) {return null;}
        int type = cell.getCellType();
        switch (type) {
            case Cell.CELL_TYPE_BLANK:
                return "";
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case Cell.CELL_TYPE_ERROR:
                return cell.getErrorCellValue();
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            case Cell.CELL_TYPE_NUMERIC:
                return cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            default:
                return null;
        }
    }


    public static void buildDefaultAttachment(String fileName, HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding(DEFAULT_CHARSET);
        String ua = request.getHeader("user-Agent");
        if (ua != null) {
            ua = ua.toLowerCase();
            if (ua.contains("msie 6")) {
                //空格,注意，对于ie6，文件名长度必须小于150字符，这里未做处理
                fileName = encodeQuietly(fileName, DEFAULT_CHARSET).replace("\\+", "20%");
            } else if (ua.contains("firefox")) {
                try {
                    fileName = MimeUtility.encodeWord(fileName, DEFAULT_CHARSET, "B");
                    //用双引号包裹
                    fileName = "\"" + fileName + "\"";
                } catch (UnsupportedEncodingException ignore) {
                    logger.error("", ignore);
                }
            } else {
                fileName = encodeQuietly(fileName, DEFAULT_CHARSET).replace("\\+", "20%");
            }
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setContentType("application/octet-stream");
    }

    private static String encodeQuietly(String str, String enc) {
        try {
            return URLEncoder.encode(str, enc);
        } catch (UnsupportedEncodingException ignore) {
            // ignore this
            return null;
        }
    }

    protected static void val(HSSFRow row, int idx, String val, int flag, HSSFCellStyle style1, HSSFCellStyle style2) {
        HSSFCell cell = row.createCell(idx);
        if (1 == flag && idx < 3) {
            style1.setFillPattern(CellStyle.SOLID_FOREGROUND);
            style1.setFillForegroundColor((short) 11);
            cell.setCellStyle(style1);
        } else {
            style2.setFillPattern(CellStyle.SOLID_FOREGROUND);
            style2.setFillForegroundColor((short) 13);
            cell.setCellStyle(style2);
        }
        cell.setCellValue(val);
    }
}
