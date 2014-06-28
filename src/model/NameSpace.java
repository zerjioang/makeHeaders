package model;

import java.util.ArrayList;
import java.util.HashMap;

public class NameSpace {

	private String name;
	private ArrayList<Classes> classes;
	private static HashMap<String, Integer> clases = new HashMap<String, Integer>();

	public NameSpace() {
		this.setClasses(new ArrayList<Classes>());
	}

	public ArrayList<Classes> getClasses() {
		return classes;
	}

	public void setClasses(ArrayList<Classes> classes) {
		this.classes = classes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static int getClassIndex(String className){
		Integer i= clases.get(className);
		if(i==null)
			return -1;
		return i;
	}
	
	public static void addClassIndex(String className, int index){
		clases.put(className, index);
	}

	public static void clear() {
		clases.clear();
	}
}
