package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataUtils {
    private static final Logger logger = LoggerFactory.getLogger(DataUtils.class);

    private static Hashtable<String, Integer> findRowColumnCount(XSSFSheet sheet, Hashtable<String, Integer> rowColumnCount) {

        XSSFRow row = null;
        int rows;
        rows = sheet.getPhysicalNumberOfRows();
        int cols = 0;
        int tmp = 0;
        int counter = 0;
        String temp = null;

        for (int i = 0; i < 10 || i < rows; i++) {
            row = sheet.getRow(i);
            if (row != null) {
                temp = convertXSSFCellToString(row.getCell(0));
                if (!temp.isEmpty()) {
                    counter++;
                }
                tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                if (tmp > cols) {
                    cols = tmp;
                }
            }
        }

        rowColumnCount.put("RowCount", counter);
        rowColumnCount.put("ColumnCount", cols);

        return rowColumnCount;
    }

    private static Hashtable<String, Integer> readExcelHeaders(XSSFSheet sheet, Hashtable<String, Integer> excelHeaders, Hashtable<String, Integer> rowColumnCount) {

        XSSFRow row = null;
        XSSFCell cell = null;
        for (int r = 0; r < rowColumnCount.get("RowCount"); r++) {
            row = sheet.getRow(r);

            if (row != null) {
                for (int c = 0; c < rowColumnCount.get("ColumnCount"); c++) {
                    cell = row.getCell(c);
                    if (cell != null) {
                        excelHeaders.put(cell.toString(), c);
                    }
                }
                break;
            }
        }
        return excelHeaders;
    }

    private static String convertXSSFCellToString(XSSFCell cell) {
        String cellValue = null;
        if (cell != null) {
            cellValue = cell.toString();
            cellValue = cellValue.trim();
        } else {
            cellValue = "";
        }
        return cellValue;
    }

    public static HashMap<String, String> getTestData(String fileName, String sheetName, String testCaseId) {
        String filePath = "";
        HashMap<String, String> data = new HashMap<String, String>();
        
        try {

            String basePath = new File(".").getCanonicalPath() + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "testdata" + File.separator;
            logger.info("data utils base path: " + basePath);
            
            filePath = basePath + fileName;
            data = getTestData(filePath, fileName, sheetName, testCaseId);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return data;        
    }

    @SuppressWarnings("resource")
	private static HashMap<String, String> getTestData(String filePath, String workBook, String sheetName, String testCaseId) throws IOException {
        XSSFRow row = null;
        XSSFCell cell = null;

        // Establish connection to work sheet
        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(filePath));
        XSSFSheet sheet = wb.getSheet(sheetName);
        Hashtable<String, Integer> excelrRowColumnCount = new Hashtable<String, Integer>();
        excelrRowColumnCount = findRowColumnCount(sheet, excelrRowColumnCount);

        // function call to find excel header fields
        Hashtable<String, Integer> excelHeaders = new Hashtable<String, Integer>();
        excelHeaders = readExcelHeaders(sheet, excelHeaders, excelrRowColumnCount);
        HashMap<String, String> data = null;
        ArrayList<String> header = new ArrayList<String>();
        ArrayList<String> matcher = null;
        HashMap<String, String> matcherList = new HashMap<String, String>();

        // Get all header
        row = sheet.getRow(0);
        if (row != null) {
            for (int c = 0; c < excelrRowColumnCount.get("ColumnCount"); c++) {
                cell = sheet.getRow(0).getCell(c);
                if (cell != null) {
                    String temp = convertXSSFCellToString(row.getCell(c));
                    header.add(temp);
                }
            }
        }

        // Get test data set
        for (int r = 1; r < excelrRowColumnCount.get("RowCount"); r++) {
            row = sheet.getRow(r);
            if (row != null) {
                XSSFCell tempCell = sheet.getRow(r).getCell(0);
                if (tempCell != null) {
                    String tcID = convertXSSFCellToString(row.getCell(0));
                    if (tcID.equalsIgnoreCase(testCaseId)) {
                        data = new HashMap<String, String>();
                        matcher = new ArrayList<String>();
                        matcher.add(tcID);
                        for (int c = 1; c < excelrRowColumnCount.get("ColumnCount"); c++) {
                            cell = sheet.getRow(r).getCell(c);
                            DataFormatter formatter = new DataFormatter();
                            String temp = formatter.formatCellValue(cell);
                            matcher.add(temp);
                        }
                        // Add all the test data to a Map
                        for (int i = 0; i < matcher.size(); i++) {
                            data.put(header.get(i), matcher.get(i));
                        }
                        matcherList.putAll(data);
                    }
                }
            }
        }

        return matcherList;
    }

}