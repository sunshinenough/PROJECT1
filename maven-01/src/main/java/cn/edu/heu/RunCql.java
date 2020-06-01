package cn.edu.heu;
import java.awt.List;
import java.io.File;

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


public class RunCql {
	
	private static CreateCql createCql = new CreateCql();
	private static RunCql runCql = new RunCql();
	public static void main(String[] args){
		//上传测试
		
		String predir = "D:\\业务数据\\处方.xlsx";
		String regdir = "D:\\业务数据\\登记表.xlsx";

		runCql.check(predir,regdir);
	}
	
	public void check(String preDir,String regDir){
		//reglist是登记表，prelist是处方表
//		RunCql runCql = new RunCql();
		LinkedList<Map<String, String>> prelist = null;
		LinkedList<Map<String, String>> reglist = null;
		try {
			prelist = new ReadExcel().readExcel(preDir);
			reglist = new ReadExcel().readExcel(regDir);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(prelist.length);
		//不用不读表头，表头被 当成map的key了
		int linereg = 0;
		int linepre = 0;
		
		Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "123456"));
		Session session = driver.session();
		
		//二维数组cqlRules存储下面while循环中的cqlmain,目前是四种规则
		//String[][] cqlRules = new String[4][8];
		LinkedList<String[]> cqlmains = new LinkedList<>();
		//注意判断字符串是否相等用equals方法
		int index = 0;
		String cqltypemain = createCql.createMain();
		StatementResult resultmain = session.run(cqltypemain);
		while(resultmain.hasNext()){
			Record recordMain = resultmain.next();
			String cqlmaintemp = recordMain.values().toString().replace("\"", "").replace("[", "").replace("]", "").replace(" ", "");
			String[] cqlmain = cqlmaintemp.split(",");
			index ++;
			cqlmains.add(cqlmain);
		}
		
		for(int x = 0;x < cqlmains.size();x ++){
			for(int y = 0;y < 8;y ++){
				System.out.println(cqlmains.get(x)[y]);
			}
		}
		
		while(linereg < reglist.size()){
			
			//定义患者用药、项目列表
			String[] itemlist = new String[120];
			int itemindex = 0;
			
			while(linepre < prelist.size() && reglist.get(linereg).get("ClinicRegisterCode").equals(prelist.get(linepre).get("ClinicRegisterCode"))){
				int age = Integer.parseInt(reglist.get(linereg).get("Age"));
				//System.out.println(age + "000000000");
				String itemname = prelist.get(linepre).get("ItemName");
				String precode = prelist.get(linepre).get("PreCode");
				String itemcode = prelist.get(linepre).get("ItemCode");
				
				//记录患者用药
				
				itemlist[itemindex] = itemcode;
				itemindex ++;
				
				double price =Double.parseDouble(prelist.get(linepre).get("PRICE")); 
				for(String[] cqlmain : cqlmains){
					if(cqlmain[0].equals("1")){
						
						//以后会改为：业务数据中的表头各项内容
						if(cqlmain[2].equals("ItemName")){
							String cqltype1 = createCql.createType1(cqlmain, itemname);
							StatementResult result = session.run(cqltype1);
							if(result.hasNext()){
								String[] rule = result.next().values().toString().replace("\"", "").replace("[", "").replace("]", "").replace(" ", "").split(",");
								System.out.println("处方：" + precode + cqlmain[2] + itemname + rule[0] + "异常");
							}
						}
					}

					if(cqlmain[0].equals("2")){
						if(cqlmain[2].equals("ItemCode")){
							String cqltype2 = createCql.createType2(cqlmain, itemcode);
//							System.out.println(cqltype2);
							StatementResult result = session.run(cqltype2);
							if(result.hasNext()){
								//获取药品限定价格
								Record record2= result.next();
								String recordtype2 = record2.values().toString().replace("\"", "").replace("[", "").replace("]", "");
								String[] rectype2 = recordtype2.split(",");
								if(cqlmain[5].equals("PRICE")){
									if(cqlmain[6].equals("<=")){
										if(price > Double.parseDouble(rectype2[1])){
											System.out.println("门诊登记号： "+ precode + " 药品编号：" + itemcode + " 限定价格： " + rectype2[1] + rectype2[0] + "异常");
										}
									}
								}
							}
						}
					}
				}
				linepre ++;
			}
			linereg ++;
		}
		driver.close();
	}
}