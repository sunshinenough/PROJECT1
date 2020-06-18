package cn.edu.heu;

import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.StatementResult;

public class WriteLog {
	public void writeLogType1(StatementResult result,String[] cqlmain,String translabel2,String precode){
			String[] rule = result.next().values().toString().replace("\"", "").replace("[", "").replace("]", "").replace(" ", "").split(",");
			System.out.println("处方：" + precode + "  " +cqlmain[2] + "：" +translabel2 + rule[0] + "异常");
	}
	public void writeLogType2(StatementResult result,String[] cqlmain,String translabel2,String precode,String transjudgdata) {
		Record record2= result.next();
		String recordtype2 = record2.values().toString().replace("\"", "").replace("[", "").replace("]", "");
		String[] rectype2 = recordtype2.split(",");
		if(cqlmain[7].equals("yes")){
			if(cqlmain[6].equals("<=")){
				if(Double.parseDouble(transjudgdata) > Double.parseDouble(rectype2[1])){
					System.out.println("处方： "+ precode +"  " + cqlmain[2] + "：" + translabel2 +" " + cqlmain[5]
							+ "：" + transjudgdata + " "+ rectype2[0]);
				}
			}
			if(cqlmain[6].equals(">=")){
				if(Double.parseDouble(transjudgdata) < Double.parseDouble(rectype2[1])){
					System.out.println("处方： "+ precode +"  " + cqlmain[2] + "：" + translabel2 +" " + cqlmain[5]
							+ "：" + transjudgdata + " "+ rectype2[0]);
				}
			}
			if(cqlmain[6].equals("equals")){
				if(!transjudgdata.equals(rectype2[1])){
					System.out.println("处方："+ precode +"  " + cqlmain[2] + "：" + translabel2 +" " + cqlmain[5]
							+ "：" + transjudgdata + " " + rectype2[0] );
				}
			}
		}
		if(cqlmain[7].equals("no")){
			if(cqlmain[6].equals("<=")){
				if(Double.parseDouble(transjudgdata) < Double.parseDouble(rectype2[1])||Double.parseDouble(transjudgdata) == Double.parseDouble(rectype2[1])){
					System.out.println("处方： "+ precode +"  " + cqlmain[2] + "：" + translabel2 +" " + cqlmain[5]
							+ "：" + transjudgdata + " "+ rectype2[0]);
				}
			}
			if(cqlmain[6].equals(">=")){
				if(Double.parseDouble(transjudgdata) > Double.parseDouble(rectype2[1])||Double.parseDouble(transjudgdata) == Double.parseDouble(rectype2[1])){
					System.out.println("处方： "+ precode +"  " + cqlmain[2] + "：" + translabel2 +" " + cqlmain[5]
							+ "：" + transjudgdata + " "+ rectype2[0]);
				}
			}
			if(cqlmain[6].equals("equals")){
				if(transjudgdata.equals(rectype2[1])){
					System.out.println("处方："+ precode +"  " + cqlmain[2] + "：" + translabel2 +" " + cqlmain[5]
							+ "：" + transjudgdata + " " + rectype2[0] );
				}
			}
		}
	}
	public void writeLogType3(StatementResult result,String[] cqlmain,String translabel1,String translabel2,String code){
		String[] rule = result.next().values().toString().replace("\"", "").replace("[", "").replace("]", "").replace(" ", "").split(",");
		if(!translabel1.equals(translabel2))
			System.out.println("处方：" + code + "  " +cqlmain[1] + "：" +translabel1 + " " + cqlmain[2] + "：" +translabel2 +" " + rule[0]);
		else
			System.out.println("登记编号：" + code + "  " +cqlmain[1] + "：" +translabel1 + " " + cqlmain[2] + "：" +translabel2 +" " + rule[0]);
	}

}
