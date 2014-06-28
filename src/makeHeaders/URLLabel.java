package makeHeaders;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;

import javax.swing.JLabel;


@SuppressWarnings("serial")
public class URLLabel extends JLabel {

	private String url;

	public URLLabel() {
		this(Messages.getString(""),Messages.getString(""));
	}

	public URLLabel(String label, String url) {
		super(label);
		this.url = url;
		setCursor(new Cursor(Cursor.HAND_CURSOR));
		addMouseListener(new URLOpenAdapter());
	}

	public void setURL(String url) {
		this.url = url;
	}

	//this is used to underline the text
	//@Override
	/*protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.blue);

		Insets insets = getInsets();

		int left = insets.left;
		if (getIcon() != null) {
			left += getIcon().getIconWidth() + getIconTextGap();
		}

		g.drawLine(left, getHeight() - 1 - insets.bottom, 
				(int) getPreferredSize().getWidth()
				- insets.right, getHeight() - 1 - insets.bottom);
	}*/

	private class URLOpenAdapter extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			if (Desktop.isDesktopSupported()) {
				try {
					Desktop.getDesktop().browse(new URI(url));
				} catch (Throwable t) {
					//
				}
			}
		}
	}
}