package com.github.blackenwhite.jscreener.gui;

import com.github.blackenwhite.jscreener.ScreenCapturer;
import com.github.blackenwhite.jscreener.Shared;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created on 28.09.2015.
 */
public class GuiManager extends JFrame {
	private static final String PRT_SCR_ICON = "/prt_scr.png";
	private static final String CONFIG_MANAGER_TITLE = "JScreener Config Manager";
	private static final String SCR_DIR_LABEL = "Screenshots will be saved to: ";
	private static final String SCR_NAME_LABEL = "Screenshot name prefix: ";
	private static final String BROWSE_BTN = "Browse";
	private static final String SAVE_BTN = "Save";
	private static final String CANCEL_BTN = "Hide";
	private static final String HELP_BTN = "Help";

	private static final int SCR_DIR_TF_SIZE = 20;
	private static final int SCR_NAME_TF_SIZE = 20;

	private JLabel scrDirLabel;
	private JLabel scrNameLabel;
	private JTextField scrDirTF;
	private JTextField scrNameTF;
	private JButton browseBtn;
	private JButton saveBtn;
	private JButton cancelBtn;
	private JButton helpBtn;

	private JTextPane helpMsg;

	// This is for control Save button state
	private String oldFilenameValue;


	public GuiManager() {

		// Init tray and bind gui manager to it
		Tray tray = new Tray(this);

		// Init help message
		initHelpMessage();

		setTitle(CONFIG_MANAGER_TITLE);
		setIconImage(tray.getTrayImage());
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		JPanel scrDirLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		scrDirLabel = new JLabel(SCR_DIR_LABEL);
		scrDirLabelPanel.add(scrDirLabel);

		JPanel scrDirTFPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		scrDirTF = new JTextField(SCR_DIR_TF_SIZE);
		scrDirTF.setPreferredSize(new Dimension(0, 20));
		scrDirTF.setEditable(false);
		scrDirTF.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				try {
					Desktop.getDesktop().open(new File(scrDirTF.getText()));
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				super.mouseEntered(e);
				Font bold = new Font(scrDirTF.getFont().toString(), Font.BOLD, scrDirTF.getFont().getSize());
				scrDirTF.setFont(bold);
				ColorUIResource color = new ColorUIResource(93, 148, 233);
				scrDirTF.setForeground(color);
				Cursor hourglassCursor = new Cursor(Cursor.HAND_CURSOR);
				setCursor(hourglassCursor);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				super.mouseExited(e);
				Font plain = new Font(scrDirTF.getFont().toString(), Font.PLAIN, scrDirTF.getFont().getSize());
				scrDirTF.setFont(plain);
				scrDirTF.setForeground(Color.BLACK);
				Cursor hourglassCursor = new Cursor(Cursor.DEFAULT_CURSOR);
				setCursor(hourglassCursor);
			}
		});
		browseBtn = new JButton(BROWSE_BTN);
		browseBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setCurrentDirectory(new File(scrDirTF.getText()));
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					scrDirTF.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		});
		scrDirTFPanel.add(scrDirTF);
		scrDirTFPanel.add(browseBtn);

		JPanel scrNameLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		scrNameLabel = new JLabel(SCR_NAME_LABEL);
		scrNameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrNameLabelPanel.add(scrNameLabel);

		JPanel scrNameTFPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		scrNameTF = new JTextField(SCR_NAME_TF_SIZE);
		scrNameTF.setPreferredSize(new Dimension(0, 20));
		scrNameTF.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!scrNameTF.getText().equals(oldFilenameValue)) {
					saveBtn.setEnabled(true);
				} else {
					saveBtn.setEnabled(false);
				}
			}
		});
		scrNameTFPanel.add(scrNameTF);

		// Buttons panel
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
		saveBtn = new JButton(SAVE_BTN);
		saveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String dir = scrDirTF.getText();
				String filename = scrNameTF.getText();
				if (dir != null && filename != null) {
					Shared.writeCfg(dir, filename);
					ScreenCapturer.setFieldsFromCfg();
					refresh();
				}
			}
		});
		saveBtn.setEnabled(false);

		cancelBtn = new JButton(CANCEL_BTN);
		cancelBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});

		helpBtn = new JButton(HELP_BTN);
		helpBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showHelpMessage();
			}
		});

		btnPanel.add(saveBtn);
		btnPanel.add(cancelBtn);
		btnPanel.add(helpBtn);

		// Put everything together
		panel.add(Box.createVerticalStrut(10));
		panel.add(scrDirLabelPanel);
		panel.add(scrDirTFPanel);
		panel.add(Box.createVerticalStrut(10));
		panel.add(scrNameLabelPanel);
		panel.add(scrNameTFPanel);
		panel.add(Box.createVerticalStrut(20));
		panel.add(btnPanel);

		setContentPane(panel);
		setResizable(false);
	}

	/** Shows config manager */
	public void showConfigManager() {
		refresh();
		pack();
		centreWindow(this);
		setVisible(true);
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = GuiManager.class.getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/** Shows help message */
	protected void showHelpMessage() {
		JOptionPane.showMessageDialog(
				null, helpMsg,
				"Help",
				JOptionPane.INFORMATION_MESSAGE
		);
	}

	/** Creates help message. */
	private void initHelpMessage() {
		// Create background with light gray color
		UIManager UI = new UIManager();
		ColorUIResource bkg = new ColorUIResource(242, 242, 242);
		UI.put("OptionPane.background", bkg);
		UI.put("Panel.background", bkg);

		helpMsg = new JTextPane();

		// Bind StyledDocument to TextPane
		StyledDocument doc = helpMsg.getStyledDocument();
		Style def = StyleContext.getDefaultStyleContext().
				getStyle(StyleContext.DEFAULT_STYLE);

		// Init styles
		Style regular = doc.addStyle("regular", def);
		StyleConstants.setFontFamily(def, "Verdana");
		StyleConstants.setFontSize(regular, 13);

		Style s = doc.addStyle("small_italic", regular);
		StyleConstants.setItalic(s, true);
		StyleConstants.setFontSize(s, 10);

		s = doc.addStyle("italic", regular);
		StyleConstants.setItalic(s, true);

		s = doc.addStyle("bold", regular);
		StyleConstants.setBold(s, true);

		s = doc.addStyle("prt_scr_icon", regular);
		ImageIcon prtScr = createImageIcon(PRT_SCR_ICON);
		StyleConstants.setIcon(s, prtScr);

		// Create styled message
		try {
			StringBuffer message = new StringBuffer();
			doc.insertString(doc.getLength(), "JScreener ", doc.getStyle("bold"));
			message.append("is the app for making screenshots.\n");
			message.append("With this, you don't need to open paint app\n");
			message.append("and CTRL-V each screenshot manually.\n\n");
			doc.insertString(doc.getLength(), message.toString(), doc.getStyle("regular"));
			message.delete(0, message.length());

			message.append("USAGE\n");
			doc.insertString(doc.getLength(), message.toString(), doc.getStyle("italic"));
			message.delete(0, message.length());
			message.append("• Specify <DIRECTORY> and <NAME PREFIX>\n").
					append("• Hit ");
			doc.insertString(doc.getLength(), message.toString(), doc.getStyle("regular"));
			message.delete(0, message.length());
			doc.insertString(doc.getLength(), " ", doc.getStyle("prt_scr_icon"));
			message.append(" to make a screenshot\n");
			message.append("• Screenshots will be captured and saved in <DIRECTORY>\n");
			message.append("• To open directory, click on a textfield\n\n");
			doc.insertString(doc.getLength(), message.toString(), doc.getStyle("regular"));

			String copyright = "© 2015 Lex Blacken lexblacken@gmail.com";
			doc.insertString(doc.getLength(), copyright, doc.getStyle("small_italic"));
		} catch (BadLocationException e) {
			e.printStackTrace();
		}

		helpMsg.setEditable(false);
		helpMsg.setBackground(bkg);
	}

	/** Centres app window on the screen. */
	private void centreWindow(Window frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}

	/** Refreshes data in textfields by reading it from config
	 *  Refreshes value for the {@String oldFilenameValue}
	 *  Sets the Save button disabled */
	private void refresh() {
		HashMap<String, String> data = Shared.readCfg();
		if (data != null) {
			scrDirTF.setText(data.get(Shared.DIRECTORY_MARK));
			scrNameTF.setText(data.get(Shared.FILENAME_MARK));
		}
		oldFilenameValue = scrNameTF.getText();
		saveBtn.setEnabled(false);
	}
}
