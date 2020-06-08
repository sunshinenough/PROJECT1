package cn.edu.heu;


public class CreateCql {

	public String createType1(String[] strings,String sent){
			String cql = "MATCH (m:" + strings[1] + ")-[r:relation]->(n:" + strings[2] + ") where n.name ='" + 
			sent + "'and m.RulesCode = '" + strings[3] + "' return r.relation";
			return cql;
	}

	public String createType2(String[] strings,String sent){
		if(strings.length != 0 || strings == null){
			String cql = "MATCH (m:" + strings[1] + ")-[r:relation]->(n:" + strings[2] + ") where n.name ='" + sent 
					+ "'and m.RulesCode = '" + strings[3] + "' return r.relation,r." + strings[5];
			return cql;
		}
		return null;
	}
	
	public String createType3(String[] strings,String sent1,String sent2){
		if(strings.length != 0 || strings == null){
			String cql = "MATCH (m:" + strings[1] + ")-[r:relation]->(n:" + strings[2] + ") where n.name ='" + sent1 + "'and m.name = '" + sent2 + "' return r.relation";
			return cql;
		}
		return null;
	}
	public String createMain(){
//		语句生成
		String cql = "MATCH (n:cql) RETURN n.type,n.label1,n.label2,n.label1_name,n.label2_name,n.judgdata,n.judgtype,n.result";
		return cql;
		
	}

}

