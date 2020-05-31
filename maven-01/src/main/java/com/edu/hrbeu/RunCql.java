package com.edu.hrbeu;
import java.awt.List;
import java.io.File;
import java.util.ArrayList;

import org.apache.lucene.analysis.miscellaneous.WordDelimiterFilter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.neo4j.cypher.internal.frontend.v2_3.perty.recipe.Pretty.nestWith;
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
import org.omg.CORBA.PRIVATE_MEMBER;
import java.util.*;
import io.netty.channel.group.ChannelMatcher;
import scala.collection.mutable.LinkedList;


public class RunCql {
	
	private static CreateCql createCql = new CreateCql();
	private static RunCql runCql = new RunCql();
	public static void main(String[] args){
		//上传测试
		
		String predir = "D:\\业务数据\\处方.xlsx";
		String regdir = "D:\\业务数据\\登记表.xlsx";

		runCql.check(predir,regdir);
	}
	
	//private final Session session = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "123456")).session();
	public StatementResult runCql(String itemname,String rulescode,Session session){
//		CreateCql createCql = new CreateCql();
		return session.run(createCql.createTypeOne(itemname, rulescode));
	}
	
	public StatementResult runCqlTwo(String itemcode,String rulescode,Session session) {
		return session.run(createCql.createTypeTwo(itemcode, rulescode));
	}
	
	public StatementResult runCqlThree(String itemCode,String itemCode2,Session session) {
		return session.run(createCql.createTypeThree(itemCode, itemCode2));
	}
	
	public StatementResult runCqlmain(Session session){
		return session.run(createCql.createMain());
	}
	 
	public void check(String preDir,String regDir){
		//reglist是登记表，prelist是处方表
//		RunCql runCql = new RunCql();
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
		//二维数组cqlRules存储下面while循环中的cqlmain,目前是四种规则
		//String[][] cqlRules = new String[4][8];
		ArrayList<String[]> cqlmains = new ArrayList<>();
		int index = 0;
		StatementResult resultmain = runCql.runCqlmain(session);
		while(resultmain.hasNext()){
			Record recordMain = resultmain.next();
			String cqlmaintemp = recordMain.values().toString().replace("\"", "").replace("[", "").replace("]", "").replace(" ", "");
			String[] cqlmain = cqlmaintemp.split(",");
			/*
			for(int a=0;a<cqlmain.length;a++){
				System.out.println(cqlmain[a]);
				//cqlRules[index][a] = cqlmain[a];
			}
			*/
			index ++;
			cqlmains.add(cqlmain);
		}
		/*
		for(int x = 0;x < cqlRules.length;x ++){
			for(int y = 0;y < cqlRules[0].length;y ++){
				System.out.println(cqlRules[x][y]);
			}
		}
		*/
		for(int x = 0;x < cqlmains.size();x ++){
			for(int y = 0;y < 8;y ++){
				System.out.println(cqlmains.get(x)[y]);
			}
		}
		while(i < reglist.length){
			//定义患者用药、项目列表
			String[] itemlist = new String[100];
			int itemindex = 0;
			while(j < prelist.length && reglist[i][3].equals(prelist[j][0])){
				int age = Integer.parseInt(reglist[i][8]);
				//System.out.println(age + "000000000");
				String itemname = prelist[j][4];
				String precode = prelist[j][1];
				String itemnode = prelist[j][3];
				
				//记录患者用药
				itemlist[itemindex] = itemnode;
				itemindex ++;
				
				double price =Double.parseDouble(prelist[j][13]);
				
				
				
//				辅药审核
				StatementResult resultC007 = runCql.runCql(itemname, "C007",session);
				if(resultC007.hasNext()){
					System.out.println("处方：" + precode + "药品：" + itemname + "辅药使用情况审核异常！");
				}
				
//				老年人禁忌
				if(age > 70 && age < 150){
					//String itemnamec003 = prelist[j][4];
					StatementResult resultC003 = runCql.runCql(itemname, "C003",session);
					if(resultC003.hasNext()){
						System.out.println("处方：" + precode + "药品：" + itemname + "老年人用药禁忌审核异常！");
					}
				}
				
				//药品限价审核
				StatementResult resultB004 = runCql.runCqlTwo(itemnode, "B004", session);
				if(resultB004.hasNext()){
					//获取药品限定价格
					Record recordB004 = resultB004.next();
					String priceB004 = recordB004.values().toString().replace("\"", "").replace("[", "").replace("]", "");
//					System.out.println(priceB004);
					double price_check = Double.parseDouble(priceB004);
//					System.out.println(price_check);
					if(price > price_check)
						System.out.println("门诊登记号： "+ precode + " 药品编号：" + itemnode + " 限定价格： " + price_check + " 药品限价审核未通过");
				}
				
				//System.out.println("处方：" + precode + "药品：" + itemname + "正常");
				j ++;
			}
			//判断两种药是否不能同时出现在一个药方中
			int judgei,judgej;
			for(judgei = 0;judgei < (itemindex-1);judgei ++){
				for(judgej = (judgei+1);judgej < itemindex;judgej++){
					//执行查询
					StatementResult resultUnion = runCql.runCqlThree(itemlist[judgei], itemlist[judgej], session);
					if(resultUnion.hasNext()){
						Record recordUnion = resultUnion.next();
						String union = recordUnion.values().toString().replace("\"", "").replace("[", "").replace("]", "");
						System.out.println("登记编号： "+ reglist[i][3] + " 对应的处方中同时使用了药品1：" + itemlist[judgei] + " 药品2： " + itemlist[judgej] + union );
					}
//					System.out.println(itemlist[judgei]);
//					if(itemlist[judgei].equals("X-J01FA-K067-E001-1V")){
//						System.out.println("登记编号： "+ reglist[i][3] + " 对应的处方中同时使用了药品1：" + itemlist[judgei] + " 药品2： " + itemlist[judgej] + " 药品不能同时使用");
//					}
				}
			}
			i ++;
		}
		driver.close();
	}
}
