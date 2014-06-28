package makeHeaders;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class Main extends JFrame {

	private JPanel contentPane;
	private JLabel lblStatusMsg;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					Controller.getInstance().setM(frame);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Main() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource(Messages.getString("Main.0")))); //$NON-NLS-1$
		setBackground(SystemColor.window);
		setType(Type.POPUP);
		setTitle(Messages.getString("Main.1")); //$NON-NLS-1$
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 367, 258);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		DandDLabel lblGenCode = new DandDLabel();
		lblGenCode.setIcon(new ImageIcon(Main.class.getResource(Messages.getString("Main.2")))); //$NON-NLS-1$
		lblGenCode.setType(DandDLabel.CODE);
		lblGenCode.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblGenCode.setHorizontalAlignment(SwingConstants.CENTER);
		lblGenCode.setBorder(null);
		
		DandDLabel lblGenHeader = new DandDLabel();
		lblGenHeader.setIcon(new ImageIcon(Main.class.getResource(Messages.getString("Main.3")))); //$NON-NLS-1$
		lblGenHeader.setHorizontalTextPosition(SwingConstants.CENTER);
		lblGenHeader.setType(DandDLabel.HEADER);
		lblGenHeader.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblGenHeader.setHorizontalAlignment(SwingConstants.CENTER);
		lblGenHeader.setBorder(null);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		
		JLabel lblStatus = new JLabel(Messages.getString("Main.4")); //$NON-NLS-1$
		lblStatus.setForeground(Color.WHITE);
		
		lblStatusMsg = new JLabel(Messages.getString("Main.5")); //$NON-NLS-1$
		lblStatusMsg.setForeground(Color.WHITE);
		
		JLabel lblI = new JLabel(Messages.getString("Main.6")); //$NON-NLS-1$
		lblI.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				About a = new About();
				a.setLocationRelativeTo(contentPane);
				a.setVisible(true);
			}
		});
		lblI.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		lblI.setHorizontalAlignment(SwingConstants.CENTER);
		lblI.setForeground(Color.WHITE);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblGenCode, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
					.addComponent(lblGenHeader, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)
					.addGap(11))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(9)
							.addComponent(lblStatus)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblStatusMsg, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(lblI, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(8)
							.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 339, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(10, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(5)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(lblGenCode, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblGenHeader, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE))
					.addGap(12)
					.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblStatus)
						.addComponent(lblStatusMsg)
						.addComponent(lblI))
					.addGap(5))
		);
		contentPane.setLayout(gl_contentPane);
		setLocationRelativeTo(null);
	}

	public void setStatus(String status) {
		lblStatusMsg.setText(status);
	}
}
