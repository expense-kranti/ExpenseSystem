import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;


import com.boilerplate.java.collections.BoilerplateMap;

public class checkingMapvalueprint {

	public static void main(String[] args){
		Map<String,String> mapdemo = new BoilerplateMap<>();
		mapdemo.put("test", "a");
		mapdemo.put("test", "b");
		mapdemo.put("test", "c");
		mapdemo.put("test1", "d");
		
		System.out.println("simple :" +mapdemo.get("test"));
		
		for(Map.Entry mapentryset : mapdemo.entrySet()){
			System.out.println(mapentryset.getValue());
		}
		
		for(String value : mapdemo.values()){
			System.out.println("values: " + value);
		}
		ArrayList<String> arList = new ArrayList<String>();
		Collection<String> coll;
		for(String key : mapdemo.keySet()){
			System.out.println("values are : " + mapdemo.get(key));
			arList.add(mapdemo.get(key));
			
			
		}
		coll = mapdemo.values();
	     Object[] o = coll.toArray();
		
		for(int i = 0 ; i< arList.size(); i++){
			System.out.println(arList.get(i));
			System.out.println("hope so : " +(String)o[i]);
		}
	}
}
