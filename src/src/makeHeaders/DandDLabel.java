package makeHeaders;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class DandDLabel extends JLabel {
	
	public static final String CODE = "CODE";
	public static final String HEADER = "HEADER";
	
	private String type;
	
	public DandDLabel(){
		super();
		init();
	}

	public DandDLabel(String s){
		super(s);
		init();
	}
	
	
	private void init() {
		new  FileDrop(this, new FileDrop.Listener()
		{
			public void  filesDropped( java.io.File[] files ){   
				// handle file drop
				new ThreadProcess(files, getType()).start();
			}   // end filesDropped
		}); // end FileDrop.Listener
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
