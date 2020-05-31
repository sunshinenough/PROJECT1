package com.edu.hrbeu;

public class CreateCql {
	public String createTypeOne(String itemname,String rulescode){
//		语句生成
		
		String cql = "MATCH (m:规则)-[r:relation]->(n:object) where n.Name ='" + itemname + "'and m.RulesCode = '" + rulescode + "' return properties(r)";
		return cql;
		
	}
	public String createTypeTwo(String ItemCode,String rulescode){
//		语句生成
//<<<<<<< HEAD
		String cql = "MATCH (m:规则)-[r:relation]->(n:objectCode) where n.Code ='" + ItemCode + "'and m.RulesCode = '" + rulescode + "' return r.PRICE";      
//=======
//		String cql = "MATCH (m:规则)-[r:relation]->(n:objectCode) where n.Code ='" + ItemCode + "'and m.RulesCode = '" + rulescode + "' return properties(r)";      
//>>>>>>> branch 'master' of https://github.com/sunshinenough/PROJECT1.git
		return cql;	
		
	}
	public String createTypeThree(String itemCode,String itemCode2){
		
		String cql = "MATCH (m:objectCode)-[r:relation]->(n:objectCode) where n.Code ='" + itemCode + "'and m.Code = '" + itemCode2 + "' return properties(r)";
		
		//药品名来判断，备选
//		String cql = "MATCH (m:object)-[r:relation]->(n:object) where n.Name ='" + itemCode + "'and m.Name = '" + itemCode2 + "' return r.relation";
		
		return cql;	
		
	}
	public String createMain(){
//		语句生成
		
		String cql = "MATCH (n:cql) RETURN n.type,n.label1,n.label2,n.label1_code,n.label2_code,n.judgdata,n.judgtype,n.result";
		return cql;
		
	}

}
