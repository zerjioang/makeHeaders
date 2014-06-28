package makeHeaders;

public class Controller {

	private Main m;
	private static Controller g;
	
	public static Controller getInstance(){
		if(g==null){
			g = new Controller();
		}
		return g;
	}
	
	public void setStatus(String status){
		m.setStatus(status);
	}

	public Main getM() {
		return m;
	}

	public void setM(Main m) {
		this.m = m;
	}
}
