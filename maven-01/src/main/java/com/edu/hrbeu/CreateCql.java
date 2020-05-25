package com.edu.hrbeu;

public class CreateCql {
	public String createTypeOne(String itemname,String rulescode){
//		匹配语句
		String cql = "MATCH (m:规则)-[r:relation]->(n:object) where n.Name ='" + itemname + "'and m.RulesCode = '" + rulescode + "' return r.relation";
		return cql;
		
	}

}
