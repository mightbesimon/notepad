import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

public class JNotePad extends JFrame {
	private JMenuItem menuOpen;
	private JMenuItem menuSave;
	private JMenuItem menuSaveAs;
	private JMenuItem menuClose;

	private JMenu editMenu;
	private JMenuItem menuCut;
	private JMenuItem menuCopy;
	private JMenuItem menuPaste;

	private JMenuItem menuHelvetica;
	private JMenuItem menuMonospaced;
	private JMenuItem menuTimesRoman;

	private JMenuItem menuAbout;

	private JTextArea textArea;
	private JLabel stateBar;
	private JFileChooser fileChooser;

	private JPopupMenu popUpMenu;

	public JNotePad() {
		super("untitled");
		setUpUIComponent();
		setUpEventListener();
		setVisible(true);
	}

	private void setUpUIComponent() {
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		setSize(640, 480);

		// MenuBar
		JMenuBar menuBar = new JMenuBar();

		// File Menu
		JMenu fileMenu = new JMenu("File");
		menuOpen	= new JMenuItem("Open");
		menuSave	= new JMenuItem("Save");
		menuSaveAs	= new JMenuItem("Save As");
		menuClose	= new JMenuItem("Close");
		// Shortcut Keys
		menuOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				InputEvent.META_MASK));
		menuSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				InputEvent.META_MASK));
		menuClose.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
				InputEvent.META_MASK));

		fileMenu.add(menuOpen);
		fileMenu.addSeparator(); // Separator
		fileMenu.add(menuSave);
		fileMenu.add(menuSaveAs);
		fileMenu.addSeparator(); // Separator
		fileMenu.add(menuClose);

		// Edit Menu
		JMenu editMenu = new JMenu("Edit");
		menuCut		= new JMenuItem("Cut");
		menuCopy	= new JMenuItem("Copy");
		menuPaste	= new JMenuItem("Paste");

		menuCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				InputEvent.META_MASK));
		menuCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				InputEvent.META_MASK));
		menuPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
				InputEvent.META_MASK));

		editMenu.add(menuCut);
		editMenu.add(menuCopy);
		editMenu.add(menuPaste);

		/* Font Menu */
		JMenu fontMenu	= new JMenu("Font");
		menuHelvetica	= new JMenuItem("Helvetica");
		menuMonospaced	= new JMenuItem("Monospaced");
		menuTimesRoman	= new JMenuItem("Times Roman");

		fontMenu.add(menuHelvetica);
		fontMenu.add(menuMonospaced);
		fontMenu.add(menuTimesRoman);

		// Help Menu
		JMenu helpMenu = new JMenu("Help");
		menuAbout	= new JMenuItem("About JNotePad");
		helpMenu.add(menuAbout);

		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		menuBar.add(fontMenu);
		menuBar.add(helpMenu);

		setJMenuBar(menuBar);

		// Text Edit Area
		textArea = new JTextArea();
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
		textArea.setLineWrap(true);
		JScrollPane panel = new JScrollPane(textArea,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		Container contentPane = getContentPane();
		contentPane.add(panel, BorderLayout.CENTER);

		// State Bar
		stateBar = new JLabel("'");
		stateBar.setHorizontalAlignment(SwingConstants.LEFT);
		stateBar.setBorder(BorderFactory.createEtchedBorder());
		contentPane.add(stateBar, BorderLayout.SOUTH);

		popUpMenu = editMenu.getPopupMenu();
		fileChooser = new JFileChooser();
	}

	private void setUpEventListener() {
		// Click on the Close Button
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				closeFile();
			}
		});

		// Menu - Open
		menuOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				openFile();
			}
		});

		// Menu - Save
		menuSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile();
			}
		});

		// Menu - Save As
		menuSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFileAs();
			}
		});

		// Menu - Close
		menuClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeFile();
			}
		});

		// Menu - Cut
		menuCut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cut();
			}
		});

		// Menu - Copy
		menuCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				copy();
			}
		});

		// Menu - Paste
		menuPaste.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				paste();
			}
		});

		/* fonts */
		menuHelvetica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeFont("SansSerif");
			}
		});
		menuMonospaced.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeFont("Monospaced");
			}
		});
		menuTimesRoman.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changeFont("Serif");
			}
		});

		// Menu - About
		menuAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Show Dialog
				JOptionPane.showOptionDialog(null,
						  "#==== JNotePad ====#\n\n"
						+ "by:\n"
						+ "    mightbesimon\n"
						+ "    github.com/mightbesimon\n\n"
						+ "Ultra lightweight text editor\n"
						+ "for Mac, made with Java.\n\n",
				"About JNotePad", JOptionPane.DEFAULT_OPTION,
				JOptionPane.INFORMATION_MESSAGE, null, null, null);
			}
		});

		// Text Area Key Action
		textArea.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				processTextArea();
			}
		});

		// Text Area Mouse Action
		textArea.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3)
					popUpMenu.show(editMenu, e.getX(), e.getY());
			}

			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1)
					popUpMenu.setVisible(false);
			}
		});
	}

	private void openFile() {
		if (isCurrentFileSaved()) { // Is the File Saved
			open(); // Open
		} else {
			// Show Dialog
			int option = JOptionPane.showConfirmDialog(null,
				  "The file has been edited.\n"
				+ "Would you like to save the changes?",
				"File Edited", JOptionPane.YES_NO_OPTION,
				JOptionPane.WARNING_MESSAGE, null);
			switch (option) {
			// Save the File
			case JOptionPane.YES_OPTION:
				saveFile(); // Save File
				break;
			// Do not Save the File
			case JOptionPane.NO_OPTION:
				open();
				break;
			}
		}
	}

	private boolean isCurrentFileSaved() {
		if (stateBar.getText().equals("Edited")) {
			return false;
		} else {
			return true;
		}
	}

	private void open() {
		// fileChooser is an Example of JFileChooser
		// Show OpenFile Dialog
		int option = fileChooser.showDialog(null, null);

		// Ok Button Pressed
		if (option == JFileChooser.APPROVE_OPTION) {
			try {
				// Open Chosen File
				BufferedReader buf = new BufferedReader(new FileReader(
						fileChooser.getSelectedFile()));

				// Title the New File
				setTitle(fileChooser.getSelectedFile().toString());
				// Clear Old File
				textArea.setText("");
				// Set State Bar
				stateBar.setText("'");
				// Get OS Line Separator
				String lineSeparator = System.getProperty("line.separator");
				// Read and Show File
				String text;
				while ((text = buf.readLine()) != null) {
					textArea.append(text);
					textArea.append(lineSeparator);
				}

				buf.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.toString(),
						"Failed to Open File", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void saveFile() {
		// Get File Name from Title
		File file = new File(getTitle());

		// If the File Do Not Exist
		if (!file.exists()) {
			// Do SaveAs
			saveFileAs();
		} else {
			try {
				// 'Open Chosen File
				BufferedWriter buf = new BufferedWriter(new FileWriter(file));
				// Write to Save File
				buf.write(textArea.getText());
				buf.close();
				// Set State Bar
				stateBar.setText("'");
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.toString(),
						"Failed to Write File", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void saveFileAs() {
		// Show File Dialog
		int option = fileChooser.showSaveDialog(null);

		// If a File is Chosen
		if (option == JFileChooser.APPROVE_OPTION) {
			// Get Chosen File
			File file = fileChooser.getSelectedFile();

			// Set Title
			setTitle(file.toString());

			try {
				// Make a New File
				file.createNewFile();
				// Save the File
				saveFile();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, e.toString(),
						"Failed to make a new file", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void closeFile() {
		// Is the File Saved
		if (isCurrentFileSaved()) {
			// 'Close the Program
			dispose();
		} else {
			int option = JOptionPane.showConfirmDialog(null,
					  "The file has been edited.\n"
					+ "Would you like to save the changes?",
					"File Edited", JOptionPane.YES_NO_OPTION,
					JOptionPane.WARNING_MESSAGE, null);

			switch (option) {
			case JOptionPane.YES_OPTION:
				saveFile();
				break;
			case JOptionPane.NO_OPTION:
				dispose();
			}
		}
	}

	private void cut() {
		textArea.cut();
		stateBar.setText("Edited");
		popUpMenu.setVisible(false);
	}

	private void copy() {
		textArea.copy();
		popUpMenu.setVisible(false);
	}

	private void paste() {
		textArea.paste();
		stateBar.setText("Edited");
		popUpMenu.setVisible(false);
	}

	private void processTextArea() {
		stateBar.setText("Edited");
	}

	private void changeFont(String fontName) {
		textArea.setFont(new Font(fontName, Font.PLAIN, 16));
	}

	public static void main(String[] args) {
		new JNotePad();
	}
}

