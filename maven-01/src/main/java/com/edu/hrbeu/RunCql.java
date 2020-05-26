package com.edu.hrbeu;
import java.io.File;

import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilter;
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


public class RunCql {
	public static void main(String[] args){
		//上传测试
		String predir = "E:\\json_to_rdf\\处方.xlsx";
		String regdir = "E:\\json_to_rdf\\登记表.xlsx";

		


		new RunCql().check(predir,regdir);
	}
	//private final Session session = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "123456")).session();
	public StatementResult runCql(String itemname,String rulescode,Session session){
		return session.run(new CreateCql().createTypeOne(itemname, rulescode));
	}
	
	public StatementResult runCqlTwo(String itemcode,String rulescode,Session session) {
		return session.run(new CreateCql().createTypeTwo(itemcode, rulescode));
	}
	 
	public void check(String preDir,String regDir){
		//reglist是登记表，prelist是处方表
		String[][] prelist = null;
		String[][] reglist = null;
		try {
			prelist = new ReadExcel().readExcel(preDir);
			reglist = new ReadExcel().readExcel(regDir);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(prelist.length);
		//不读表头
		int i = 1;
		int j = 1;
		Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "123456"));
		Session session = driver.session();
		//注意判断字符串是否相等用equals方法
		while(i < reglist.length){
			while(j < prelist.length && reglist[i][3].equals(prelist[j][0])){
				int age = Integer.parseInt(reglist[i][8]);
				//System.out.println(age + "000000000");
				String itemname = prelist[j][4];
				String precode = prelist[j][1];
				String itemnode = prelist[j][3];
				double price =Double.parseDouble(prelist[j][13]); 
//				辅药审核
				StatementResult resultC007 = new RunCql().runCql(itemname, "C007",session);
				if(resultC007.hasNext()){
					System.out.println("处方：" + precode + "药品：" + itemname + "辅药使用情况审核异常！");
				}
				
//				老年人禁忌
				if(age > 70 && age < 150){
					//String itemnamec003 = prelist[j][4];
					StatementResult resultC003 = new RunCql().runCql(itemname, "C003",session);
					if(resultC003.hasNext()){
						System.out.println("处方：" + precode + "药品：" + itemname + "老年人用药禁忌审核异常！");
					}
				}
				
				//药品限价审核
				StatementResult resultB004 = new RunCql().runCqlTwo(itemnode, "B004", session);
				if(resultB004.hasNext()){
					//获取药品限定价格
					Record recordB004 = resultB004.next();
					String priceB004 = recordB004.values().toString().replace("\"", "").replace("[", "").replace("]", "");
//					System.out.println(priceB004);
					double price_check = Double.parseDouble(priceB004);
//					System.out.println(price_check);
					if(price > price_check)
						System.out.println("处方： "+ precode + " 药品编号：" + itemnode + " 限定价格： " + price_check + " 药品限价审核未通过");
				}
				
				//System.out.println("处方：" + precode + "药品：" + itemname + "正常");
				j ++;
			}
			i ++;
		}
		driver.close();
	}
}
