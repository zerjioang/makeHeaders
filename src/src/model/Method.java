package model;

public class Method {

	private String name;
	private boolean isReturn;
	private String returnType;

	public Method(String name) {
		this.name = name;
	}
	
	public Method() {
		this.name = new String();
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		/*int lastParenthesis = name.lastIndexOf(")")+1;
		String finalname = name.substring(0, lastParenthesis);
		this.name = finalname;*/
		this.name = name;
		if(name.contains("void ")){
			isReturn = false;
		}
		else{
			isReturn = true;
		}
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public boolean isReturn() {
		return isReturn;
	}

	public void setReturn(boolean isReturn) {
		this.isReturn = isReturn;
	}
}
