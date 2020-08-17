package utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	private Workbook wbook = null;
	private Sheet sheet = null;
	private FileInputStream in = null;

	public ExcelUtils(String exceLocation) {

		try {
			in = new FileInputStream(exceLocation);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String excelExtension = exceLocation.split("\\.")[1];
		try {
			switch (excelExtension) {
			case "xlsx":
				wbook = new XSSFWorkbook(in);
				break;
			case "xls":
				wbook = new HSSFWorkbook(in);
				break;

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Sheet getSheet() {
		return sheet;
	}

	public void setSheet(String sheetName) {
sheet=wbook.getSheet(sheetName);

	}
	
	
	public Map<String,Integer>getHeaders(String sheetName){
	
		if(sheet==null) {
			sheet=wbook.getSheet(sheetName);
		}
		else if(!sheet.getSheetName().equalsIgnoreCase(sheetName)) {
			sheet=wbook.getSheet(sheetName);
		}
		Map<String,Integer>headers=new HashMap<>();
		int i=0;
		for(int j=0;j<sheet.getRow(i).getLastCellNum();j++) {
			headers.put(this.returnCellValue(i, j).toString(),sheet.getRow(i).getCell(j).getColumnIndex());
		}
		
		return headers;
	}
	
	public Map<String,Integer>getFirstColumnValues(String sheetName){
		
		if(sheet==null) {
			sheet=wbook.getSheet(sheetName);
		}
		else if(!sheet.getSheetName().equalsIgnoreCase(sheetName)) {
			sheet=wbook.getSheet(sheetName);
		}
		Map<String,Integer>firstColumnValues=new HashMap<>();
		int j=0;
		for(int i=0;i<sheet.getLastRowNum();i++) {
			firstColumnValues.put(this.returnCellValue(i, j).toString(),sheet.getRow(i).getCell(j).getColumnIndex());

		}
		
		return firstColumnValues;
	}
	
	
	public String returnCellValue(int rowNum, int cellNum) {
		String cellValue = null;
		if(sheet.getRow(rowNum)==null) {
			sheet.createRow(rowNum);
		}
	if (sheet.getRow(rowNum).getCell(cellNum) == null) {
		sheet.getRow(rowNum).getCell(cellNum, MissingCellPolicy.CREATE_NULL_AS_BLANK);
			sheet.getRow(rowNum).getCell(cellNum).setCellValue("");
			cellValue = sheet.getRow(rowNum).getCell(cellNum).getStringCellValue();
			return cellValue;

		}
		if (sheet.getRow(rowNum).getCell(cellNum).getCellType() == CellType.BOOLEAN) {
			cellValue = String.valueOf(sheet.getRow(rowNum).getCell(cellNum).getBooleanCellValue());
			return cellValue;
		}
		if (sheet.getRow(rowNum).getCell(cellNum).getCellType() == CellType.NUMERIC) {
			cellValue = String.valueOf(sheet.getRow(rowNum).getCell(cellNum).getNumericCellValue());
			return cellValue;
		}
		if (sheet.getRow(rowNum).getCell(cellNum).getCellType() == CellType.STRING) {
			cellValue =  String.valueOf(sheet.getRow(rowNum).getCell(cellNum).getStringCellValue());
			return cellValue;
		}
		if (sheet.getRow(rowNum).getCell(cellNum).getCellType() == CellType.FORMULA) {
			cellValue =  String.valueOf(sheet.getRow(rowNum).getCell(cellNum).getCellFormula());
			return cellValue;
		}
		return cellValue;
	
}
	
	
	
}
