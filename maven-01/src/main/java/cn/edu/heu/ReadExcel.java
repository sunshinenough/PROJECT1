package cn.edu.heu;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.jam.mutable.MPackage;
import org.junit.Test;
import org.neo4j.cypher.internal.compiler.v3_1.codegen.setStaticField;

import java.util.HashMap;
public class ReadExcel {
	//读取excel表格
	public static void main(String[] args){
		try {
			String predir = "D:\\BaiduNetdiskDownload\\IDEA_code\\处方.xlsx";
			String resdir = "D:\\BaiduNetdiskDownload\\IDEA_code\\登记表.xlsx";
			
			//Map<String, String> premap = new ReadExcel().readExcel(predir);
			//Map<String, String> regmap = new ReadExcel().readExcel(resdir);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public String[][] readExcel(String dir) throws IOException{
		
		
		XSSFWorkbook workbook = new XSSFWorkbook(dir);
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
		Map<String, String> map = new HashMap<>();
		LinkedList<Map<String, String>> list = new LinkedList<>();
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
						if(i != 0){
							map.put(pres[0][j], pres[i][j]);
						}
					}
				}
				list.add(map);
			}
		}
		/*
		for(int i = 0; i < pres.length; i++) {
			for(int j = 0; j < pres[i].length; j++) {
				System.out.print(pres[i][j] + " ");
			}
			System.out.println();
		}
		*/
		
		workbook.close();
		//释放掉二维数组
		//pres = null;
		System.out.println(list.get(1).get("Name"));
		System.out.println(list.get(1).get("ItemCode"));
		return pres;
		//return map;
		
	}
}
