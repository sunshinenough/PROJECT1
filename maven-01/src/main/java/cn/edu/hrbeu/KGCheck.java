package cn.edu.hrbeu;

import java.io.File;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class KGCheck {

	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "123456"));
		Session session = driver.session();
		
		
		try {
//			XSSFWorkbook workbook = new XSSFWorkbook("D:\\文件\\医保业务规则\\门诊处方表 - 副本.xlsx");
			XSSFWorkbook workbook = new XSSFWorkbook("E:\\json_to_rdf\\处方.xlsx");
			XSSFSheet sheet = workbook.getSheetAt(0);
			
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
			
			for(int i = 1; i < pres.length; i++) {
					
					int Age = -1;
					float Price = Float.valueOf(pres[i][13]);
					int flag = 0;
					
						String cql = "MATCH (n:`object`{Name:" + "'" + pres[i][4] + "'" + " }) -[r]-(m:规则) return m.RulesName";
						StatementResult result = session.run(cql);
						while(result.hasNext()) {
							Record record = result.next();
//							System.out.println(record.values().toString().replace("\"", "").replace("[", "").replace("]", ""));
							String rules = record.values().toString().replace("\"", "").replace("[", "").replace("]", "");
							
							if(rules.equals("儿童用药禁忌") && Age >=0 && Age <= 14) {
								System.out.println(pres[i][0] + " " + pres[i][1] + " " + pres[i][2] + " " + pres[i][3] + " " + pres[i][4] + " " + rules);
								flag = 1;
							} else if (rules.equals("老年人用药禁忌(StartAge:80,EndAge:150)") && Age >=80 && Age <= 150) {
								System.out.println(pres[i][0] + " " + pres[i][1] + " " + pres[i][2] + " " + pres[i][3] + " " + pres[i][4] + " " + rules);
								flag = 1;
							}else if(rules.equals("药品限价") && Price > 3.51) {
								System.out.println(pres[i][0] + " " + pres[i][1] + " " + pres[i][2] + " " + pres[i][3] + " " + pres[i][4] + " " + " " + pres[i][13] + " " +" " + Price+ rules);
								flag = 1;
								
							}
							
						}
						if(flag == 1) {
							System.out.println(pres[i][0] + " " + pres[i][1] + " " + pres[i][2] + " " + pres[i][13] +" "+" "+Price+ "不符合医保规则");
						}
					
					
			}
			
			
			workbook.close();
			driver.close();
			
		}
		 catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}
}