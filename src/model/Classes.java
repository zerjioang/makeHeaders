package model;

import java.util.ArrayList;

public class Classes {

	private ArrayList<Method> method;
	private String name;
	
	public Classes() {
		this.setMethod(new ArrayList<Method>());
	}
	
	public Classes(String name) {
		this.setMethod(new ArrayList<Method>());
		setName(name);
	}

	public ArrayList<Method> getMethod() {
		return method;
	}

	public void setMethod(ArrayList<Method> classes) {
		this.method = classes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
