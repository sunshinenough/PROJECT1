package cn.edu.hrbeu;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {

	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		
		try {
			XSSFWorkbook workbook = new XSSFWorkbook("E:\\json_to_rdf\\住院登记表.xlsx");
			XSSFSheet sheet = workbook.getSheetAt(0);
			
//			for(Row row : sheet) {
//				for(Cell cell : row) {
//					String value = cell.getStringCellValue();
//					System.out.println(value);
//				}
//			}
			
			int lastRowNum = sheet.getLastRowNum();
			int lastCellNum = sheet.getRow(0).getLastCellNum();
			String[][] pres = new String[lastRowNum + 1][lastCellNum];
			for(int i = 0; i <= lastRowNum; i++) {
				XSSFRow row = sheet.getRow(i);
				if(row!=null) {
					
					int cellNum = row.getLastCellNum();
					for(int j = 0; j < cellNum; j++) {
						XSSFCell cell = row.getCell(j);
						if(cell != null) {
							String stringCellValue = null;
							cell.setCellType(Cell.CELL_TYPE_STRING);
							stringCellValue = cell.getStringCellValue();
							pres[i][j] = stringCellValue;
//							System.out.println(stringCellValue);
						}
					}
				}
			}
			
			for(int i = 0; i < pres.length; i++) {
				for(int j = 0; j < pres[i].length; j++) {
					System.out.print(pres[i][j] + " ");
				}
				System.out.println();
			}
			
			
			workbook.close();
			
		}
		 catch (Exception e) {
			// TODO: handle exception
		}
	}

}
