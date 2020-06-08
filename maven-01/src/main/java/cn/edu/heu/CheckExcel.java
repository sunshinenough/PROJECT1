package cn.edu.heu;

import java.util.LinkedList;
import java.util.Map;

public class CheckExcel {
	
	private static CheckExcel checkExcel = new CheckExcel();
	public String checkExcel(Map<String, String> map,String string1){
		String checkanswer = null;
		for (Map.Entry<String,String> entry : map.entrySet()) {
		    if(entry.getKey().equals(string1)){
		    	checkanswer = entry.getValue();
		    	break;
		    }
		}
		return checkanswer;
	}
	
	public String getTranslabel(LinkedList<Map<String, String>> prelist,LinkedList<Map<String, 
			String>> reglist,String[] cqlmain,Map<String, String> checkmap,int linepre,int linereg,int labelindex){
		
		String translabel;//获取标签2在表格中所对应的值
		if(checkExcel.checkExcel(checkmap, cqlmain[labelindex]).equals("处方表")){
			translabel = prelist.get(linepre).get(cqlmain[labelindex]);
		}
		else{
			translabel = reglist.get(linereg).get(cqlmain[labelindex]);
		}
		return translabel;
	}
	public LinkedList<String> checkCqlUnion(LinkedList<String[]> cqlmains){
		LinkedList<String> cqlUnion = new LinkedList<>();
		for(String[] cqlmain : cqlmains){
			if((cqlmain[0].equals("3")&&cqlmain[1].equals(cqlmain[2]))||(cqlmain[0].equals("2")&&cqlmain[5].equals("sum"))){
				cqlUnion.add(cqlmain[2]);
			}
		}
		return cqlUnion;
	}

}
