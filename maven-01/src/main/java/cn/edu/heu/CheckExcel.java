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
			String>> reglist,String[] cqlmain,Map<String, String> checkmap,int line,int labelindex){
		
		String translabel;//获取标签2在表格中所对应的值
		if(checkExcel.checkExcel(checkmap, cqlmain[labelindex]).equals("处方表")){
			translabel = prelist.get(line).get(cqlmain[labelindex]);
		}
		else{
			translabel = reglist.get(line).get(cqlmain[labelindex]);
		}
		return translabel;
	}

}
