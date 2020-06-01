package cn.edu.heu;


public class CreateCql {
	public String createTypeOne(String itemname,String rulescode){
//		语句生成
		
		String cql = "MATCH (m:规则)-[r:relation]->(n:object) where n.Name ='" + itemname + "'and m.RulesCode = '" + rulescode + "' return r.relation";
		return cql;
		
	}
	public String createType1(String[] strings,String sent){
		
			String cql = "MATCH (m:" + strings[1] + ")-[r:relation]->(n:" + strings[2] + ") where n.name ='" + sent + "'and m.RulesCode = '" + strings[3] + "' return r.relation";
			
			return cql;
		
	}
	public String createTypeTwo(String ItemCode,String rulescode){
//		语句生成
		String cql = "MATCH (m:规则)-[r:relation]->(n:objectCode) where n.Code ='" + ItemCode + "'and m.RulesCode = '" + rulescode + "' return r.PRICE";      
		return cql;	
		
	}
	public String createType2(String[] strings,String sent){
		if(strings.length != 0 || strings == null){
			String cql = "MATCH (m:" + strings[1] + ")-[r:relation]->(n:" + strings[2] + ") where n.name ='" + sent + "'and m.RulesCode = '" + strings[3] + "' return r.relation,r." + strings[5];
			return cql;
		}
		return null;
	}
	public String createTypeThree(String itemCode,String itemCode2){
		
		String cql = "MATCH (m:objectCode)-[r:relation]->(n:objectCode) where n.Code ='" + itemCode + "'and m.Code = '" + itemCode2 + "' return r.relation";
		
		//药品名来判断，备选
//		String cql = "MATCH (m:object)-[r:relation]->(n:object) where n.Name ='" + itemCode + "'and m.Name = '" + itemCode2 + "' return r.relation";
		
		return cql;	
		
	}
	public String createMain(){
//		语句生成
		
		String cql = "MATCH (n:cql) RETURN n.type,n.label1,n.label2,n.label1_name,n.label2_name,n.judgdata,n.judgtype,n.result";
		return cql;
		
	}

}

