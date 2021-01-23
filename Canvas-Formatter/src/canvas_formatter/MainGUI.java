/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package canvas_formatter;

import java.awt.Color;
import javax.swing.table.TableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListModel;
import javax.swing.DefaultListModel;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.awt.datatransfer.StringSelection;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.util.Arrays;
import javax.swing.JFrame;

/**
 *
 * @author devinmuzzy
 */

@SuppressWarnings("serial")
public class MainGUI extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8299956505960677883L;
	Object[][] currentTable;
	/**
	 * Creates new form MainGUI
	 */

	/**
	 * groups should be formatted such that [0] contains "Name" and the name [1]
	 * contains "Classes" and the classes [2] contains "Students" and the Students
	 * 
	 */
	public Map<String, List<String>[]> groups;

	void updateCreateGroup() {
		System.out.println("shown");
		groupClassList.setModel(new javax.swing.AbstractListModel<String>() {
			String[] strings = CanvasMain.getClassList();

			public int getSize() {
				return strings.length;
			}

			public String getElementAt(int i) {
				return strings[i];
			}
		});
		groupStudentList.setModel(new javax.swing.AbstractListModel<String>() {
			String[] strings = CanvasMain.getStudentList();

			public int getSize() {
				return strings.length;
			}

			public String getElementAt(int i) {
				return strings[i];
			}
		});
	}

	public MainGUI() {
		mainLoop();
		initComponents();
		defaultGUI();

	}

	void updateAuth() {
		tokenTextField.setText(CanvasMain.getToken());
		institutionTextField.setText(CanvasMain.getInstitution());
	}

	void mainLoop() {
		groups = CanvasMain.loadPreferences();
	}

	void defaultGUI() {
		cleanGroupMakingExit(); // cause spaghetti

		groupComboBox.setModel(
				new javax.swing.DefaultComboBoxModel<>(groups.keySet().toArray(new String[groups.keySet().size()])));

		groupGradesComboBox.setModel(
				new javax.swing.DefaultComboBoxModel<>(groups.keySet().toArray(new String[groups.keySet().size()])));
	}

	void createGroup() {

		@SuppressWarnings("unchecked")
		List<String>[] groupList = new List[2];
		groupList[0] = new ArrayList<>();
		groupList[1] = new ArrayList<>();
		groupList[0].add("Classes");
		groupList[1].add("Students");

		String groupName = (groupNameField.getText());

		for (String s : groupClassList.getSelectedValuesList()) {
			groupList[0].add(s);
		}
		for (String s : groupStudentList.getSelectedValuesList()) {
			groupList[1].add(s);
		}

		groups.put(groupName, groupList);
		// System.out.println("added : " + groupName + " to groups");

		groupComboBox.setModel(
				new javax.swing.DefaultComboBoxModel<>(groups.keySet().toArray(new String[groups.keySet().size()])));
		groupGradesComboBox.setModel(
				new javax.swing.DefaultComboBoxModel<>(groups.keySet().toArray(new String[groups.keySet().size()])));

	}

	void cleanGroupMakingExit() {
		groupPopupFrame.setVisible(false);
		groupStudentList.clearSelection();
		groupClassList.clearSelection();
		groupNameField.setText(" ");
		DefaultListModel<String> groupList = new DefaultListModel<>();
		int i = 0;
		for (String s : groups.keySet().toArray(new String[0])) {
			groupList.add(i++, s);

		}

		groupJList.setModel(groupList);
		// System.out.println("Updated -- TEMP");

	}

	TableModel getActivityTable() {

		String groupName = (String) groupComboBox.getSelectedItem();
		List<String>[] group = groups.get(groupName);
		List<String> classList = group[0];
		List<String> studentList = group[1];
		DefaultTableModel model = new DefaultTableModel();

		currentTable = CanvasMain.getActivityTableArray(classList, studentList); // FIXME

		// System.out.println("OBJECT[0][0]: " + currentTable[0][0]);

		for (int i = 0; i < currentTable[0].length - 1; i++) {
			model.addColumn(currentTable[0][i]);
		}
		for (int i = 1; i < currentTable.length - 1; i++) {
			model.addRow(currentTable[i]);
		}

		// System.out.println("model: " + model.toString());
		// System.out.println("cols: " + model.getColumnCount());
		// System.out.println("rows: " + model.getRowCount());

		// System.out.println("RETURNED NEW TABLE MODEL!");

		return model;
	}

	String[] getStudentList() {
		return CanvasMain.getStudentList();
	}

	TableModel getReportTable() {

		String student = studentJList.getSelectedValue();
		List<String> classList = Arrays.asList(CanvasMain.getClassList());
		List<String> studentList = new ArrayList<>(Arrays.asList("Students", student));
		DefaultTableModel model = new DefaultTableModel();
		currentTable = CanvasMain.getReportTableArray(classList, studentList); // FIXME
		// System.out.println("OBJECT[0][0]: " + currentTable[0][0]);

		for (int i = 0; i < currentTable[0].length - 1; i++) {
			model.addColumn(currentTable[0][i]);
		}
		for (int i = 1; i < currentTable.length - 1; i++) {
			model.addRow(currentTable[i]);
		}

		// System.out.println("model: " + model.toString());
		// System.out.println("cols: " + model.getColumnCount());
		// System.out.println("rows: " + model.getRowCount());

		// System.out.println("RETURNED NEW TABLE MODEL!");

		return model;

	}

	// sets the default table model for all tables
	TableModel defaultTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		List<String> list = new ArrayList<String>();
		list.add(" ");
		list.add(" ");
		list.add(" ");
		list.add(" ");
		Object[] header = new String[10];
		for (int i = 0; i < 10; i++) {
			header[i] = "  ";
			model.addColumn(header[i]);
		}
		model.addRow(list.toArray());
		model.addRow(list.toArray());

		// System.out.println("model: " + model.toString());
		// System.out.println("cols: " + model.getColumnCount());
		// System.out.println("rows: " + model.getRowCount());

		return model;
	}

	TableModel getGradesTable() {

		String groupName = (String) groupGradesComboBox.getSelectedItem();
		List<String>[] group = groups.get(groupName);
		List<String> classList = group[0];
		List<String> studentList = group[1];
		DefaultTableModel model = new DefaultTableModel();

		currentTable = CanvasMain.getGradesTableArray(classList, studentList); // FIXME

		// System.out.println("OBJECT[0][0]: " + currentTable[0][0]);

		for (int i = 0; i < currentTable[0].length - 1; i++) {
			model.addColumn(currentTable[0][i]);
		}
		for (int i = 1; i < currentTable.length - 1; i++) {
			model.addRow(currentTable[i]);
		}

		// System.out.println("model: " + model.toString());
		// System.out.println("cols: " + model.getColumnCount());
		// System.out.println("rows: " + model.getRowCount());

		// System.out.println("RETURNED NEW TABLE MODEL!");

		return model;

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */

	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		groupPopupFrame = new javax.swing.JFrame();
		jLabel7 = new javax.swing.JLabel();
		jPanel1 = new javax.swing.JPanel();
		jButton1 = new javax.swing.JButton();
		jLabel8 = new javax.swing.JLabel();
		jLabel9 = new javax.swing.JLabel();
		jButton4 = new javax.swing.JButton();
		jScrollPane5 = new javax.swing.JScrollPane();
		groupClassList = new javax.swing.JList<>();
		jScrollPane6 = new javax.swing.JScrollPane();
		groupStudentList = new javax.swing.JList<>();
		groupNameField = new javax.swing.JTextField();
		jLabel10 = new javax.swing.JLabel();
		popupToken = new javax.swing.JPopupMenu();
		copyMenuItem = new javax.swing.JMenuItem();
		ogPopupToken = new javax.swing.JPopupMenu();
		copyMenuItem1 = new javax.swing.JMenuItem();
		jOptionPane1 = new javax.swing.JOptionPane();
		jButton2 = new javax.swing.JButton();
		OuterJPanel = new javax.swing.JPanel();
		sidebarPanel = new javax.swing.JPanel();
		homeToggleBtn = new javax.swing.JToggleButton();
		studentReportTbtn = new javax.swing.JToggleButton();
		gradeToggleBtn = new javax.swing.JToggleButton();
		recentActivityBtn = new javax.swing.JToggleButton();
		settingsToggleBtn = new javax.swing.JToggleButton();
		cardholderPanel = new javax.swing.JPanel();
		homePanel = new javax.swing.JPanel();
		jLabel1 = new javax.swing.JLabel();
		jScrollPane4 = new javax.swing.JScrollPane();
		jTextArea1 = new javax.swing.JTextArea();
		studReportPanel = new javax.swing.JPanel();
		jLabel6 = new javax.swing.JLabel();
		studReportInternalPanel = new javax.swing.JPanel();
		jPanel3 = new javax.swing.JPanel();
		jLabel4 = new javax.swing.JLabel();
		jScrollPane2 = new javax.swing.JScrollPane();
		studentJList = new javax.swing.JList<>();
		getReportBtn = new javax.swing.JButton();
		jScrollPane1 = new javax.swing.JScrollPane();
		studentReportTable = new javax.swing.JTable();
		gradesPanel = new javax.swing.JPanel();
		jLabel12 = new javax.swing.JLabel();
		gradesInternalPanel = new javax.swing.JPanel();
		gradesInternalUpperPanel = new javax.swing.JPanel();
		updateBtn1 = new javax.swing.JButton();
		groupGradesLabel = new javax.swing.JLabel();
		groupGradesComboBox = new javax.swing.JComboBox<>();
		tableScrollPane1 = new javax.swing.JScrollPane();
		gradesTable = new javax.swing.JTable();
		copyClipboardBtn1 = new javax.swing.JButton();
		activityPanel = new javax.swing.JPanel();
		jLabel5 = new javax.swing.JLabel();
		activityInternalPanel = new javax.swing.JPanel();
		activityInternalUpperPanel = new javax.swing.JPanel();
		updateBtn = new javax.swing.JButton();
		groupLabel = new javax.swing.JLabel();
		groupComboBox = new javax.swing.JComboBox<>();
		tableScrollPane = new javax.swing.JScrollPane();
		activityTable = new javax.swing.JTable();
		copyClipboardBtn = new javax.swing.JButton();
		settingsPanel = new javax.swing.JPanel();
		settingsTitleJLabel = new javax.swing.JLabel();
		jPanel5 = new javax.swing.JPanel();
		createGroupBtn = new javax.swing.JButton();
		jScrollPane3 = new javax.swing.JScrollPane();
		groupJList = new javax.swing.JList<>();
		jLabel11 = new javax.swing.JLabel();
		editGroupBtn = new javax.swing.JButton();
		deleteGroupBtn = new javax.swing.JButton();
		jLabel2 = new javax.swing.JLabel();
		tokenTextField = new javax.swing.JTextField();
		saveNewTokenBtn = new javax.swing.JButton();
		institutionTextField = new javax.swing.JTextField();
		saveNewInstitutionBtn = new javax.swing.JButton();
		jButton3 = new javax.swing.JButton();

		groupPopupFrame.setTitle("Create Group");
		groupPopupFrame.setAlwaysOnTop(true);
		groupPopupFrame.setSize(new java.awt.Dimension(600, 500));
		groupPopupFrame.addComponentListener(new java.awt.event.ComponentAdapter() {
			public void componentShown(java.awt.event.ComponentEvent evt) {
				groupPopupFrameComponentShown(evt);
			}
		});

		jLabel7.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
		jLabel7.setText("Create Group");
		groupPopupFrame.getContentPane().add(jLabel7, java.awt.BorderLayout.PAGE_START);

		jButton1.setText("Save and Exit");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		jLabel8.setText("Students");

		jLabel9.setText("Classes:");

		jButton4.setText("Cancel");
		jButton4.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton4ActionPerformed(evt);
			}
		});

		groupClassList.setModel(new javax.swing.AbstractListModel<String>() {
			String[] strings = CanvasMain.getClassList();

			public int getSize() {
				return strings.length;
			}

			public String getElementAt(int i) {
				return strings[i];
			}
		});
		jScrollPane5.setViewportView(groupClassList);

		groupStudentList.setModel(new javax.swing.AbstractListModel<String>() {
			String[] strings = CanvasMain.getStudentList();

			public int getSize() {
				return strings.length;
			}

			public String getElementAt(int i) {
				return strings[i];
			}
		});
		jScrollPane6.setViewportView(groupStudentList);

		groupNameField.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				groupNameFieldActionPerformed(evt);
			}
		});

		jLabel10.setText("Group Name:");

		javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
		jPanel1.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
						.addContainerGap()
						.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										jPanel1Layout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE)
												.addComponent(jButton4)
												.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(jButton1))
								.addGroup(jPanel1Layout.createSequentialGroup().addGap(11, 11, 11)
										.addComponent(jLabel10).addGap(18, 18, 18)
										.addComponent(groupNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 145,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(0, 0, Short.MAX_VALUE))
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout
										.createSequentialGroup()
										.addGroup(jPanel1Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 141,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 187,
														javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addGroup(jPanel1Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
												.addGroup(jPanel1Layout.createSequentialGroup()
														.addComponent(jScrollPane6,
																javax.swing.GroupLayout.PREFERRED_SIZE, 0,
																Short.MAX_VALUE)
														.addGap(42, 42, 42))
												.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout
														.createSequentialGroup()
														.addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE,
																141, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addGap(96, 96, 96)))))
						.addContainerGap()));
		jPanel1Layout
				.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel1Layout.createSequentialGroup().addGap(27, 27, 27).addGroup(jPanel1Layout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel10)
								.addComponent(groupNameField, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGap(11, 11, 11)
								.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 28,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGap(18, 18, 18)
								.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 149,
												Short.MAX_VALUE)
										.addComponent(jScrollPane6))
								.addGap(23, 23, 23)
								.addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
										.addComponent(jButton1).addComponent(jButton4))
								.addContainerGap()));

		groupPopupFrame.getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

		popupToken.setToolTipText("");

		copyMenuItem.setText("Paste");
		copyMenuItem.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				copyMenuItemMouseClicked(evt);
			}

			public void mouseEntered(java.awt.event.MouseEvent evt) {
				copyMenuItemMouseEntered(evt);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				copyMenuItemMouseExited(evt);
			}
		});
		copyMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				copyMenuItemActionPerformed(evt);
			}
		});
		popupToken.add(copyMenuItem);

		ogPopupToken.setToolTipText("");

		copyMenuItem1.setText("Paste");
		copyMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				copyMenuItem1MouseClicked(evt);
			}

			public void mouseEntered(java.awt.event.MouseEvent evt) {
				copyMenuItem1MouseEntered(evt);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				copyMenuItem1MouseExited(evt);
			}
		});
		copyMenuItem1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				copyMenuItem1ActionPerformed(evt);
			}
		});
		ogPopupToken.add(copyMenuItem1);

		jButton2.setText("jButton2");

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Canvas API Access");
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosed(java.awt.event.WindowEvent evt) {
				formWindowClosed(evt);
			}

			public void windowClosing(java.awt.event.WindowEvent evt) {
				formWindowClosing(evt);
			}
		});

		sidebarPanel.setBackground(new java.awt.Color(204, 204, 204));
		sidebarPanel.setLayout(new java.awt.GridLayout(5, 1));

		homeToggleBtn.setSelected(true);
		homeToggleBtn.setText("About");
		homeToggleBtn.setOpaque(true);
		homeToggleBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				homeToggleBtnActionPerformed(evt);
			}
		});
		sidebarPanel.add(homeToggleBtn);

		studentReportTbtn.setText("Student Report");
		studentReportTbtn.setOpaque(true);
		studentReportTbtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				studentReportTbtnActionPerformed(evt);
			}
		});
		sidebarPanel.add(studentReportTbtn);

		gradeToggleBtn.setText("Grades");
		gradeToggleBtn.setOpaque(true);
		gradeToggleBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				gradeToggleBtnActionPerformed(evt);
			}
		});
		sidebarPanel.add(gradeToggleBtn);

		recentActivityBtn.setText("Recent Activity");
		recentActivityBtn.setOpaque(true);
		recentActivityBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				recentActivityBtnActionPerformed(evt);
			}
		});
		sidebarPanel.add(recentActivityBtn);

		settingsToggleBtn.setText("Settings");
		settingsToggleBtn.setOpaque(true);
		settingsToggleBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				settingsToggleBtnActionPerformed(evt);
			}
		});
		sidebarPanel.add(settingsToggleBtn);

		cardholderPanel.setLayout(new java.awt.CardLayout());

		homePanel.setLayout(new java.awt.BorderLayout());

		jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
		jLabel1.setText("Home");
		homePanel.add(jLabel1, java.awt.BorderLayout.PAGE_START);

		jTextArea1.setEditable(false);
		jTextArea1.setColumns(20);
		jTextArea1.setRows(5);
		jTextArea1.setText("Author - Devin Muzzy\n");
		jScrollPane4.setViewportView(jTextArea1);

		homePanel.add(jScrollPane4, java.awt.BorderLayout.CENTER);

		cardholderPanel.add(homePanel, "card2");

		studReportPanel.setLayout(new java.awt.BorderLayout());

		jLabel6.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
		jLabel6.setText("Student Report");
		studReportPanel.add(jLabel6, java.awt.BorderLayout.PAGE_START);

		studReportInternalPanel.setLayout(new java.awt.BorderLayout());

		jLabel4.setText("Student:");

		studentJList.setModel(new javax.swing.AbstractListModel<String>() {
			String[] strings = getStudentList();

			public int getSize() {
				return strings.length;
			}

			public String getElementAt(int i) {
				return strings[i];
			}
		});
		jScrollPane2.setViewportView(studentJList);

		getReportBtn.setText("Get Report");
		getReportBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				getReportBtnActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
		jPanel3.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup().addContainerGap()
						.addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 102,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 230,
								javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
								javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(getReportBtn).addGap(71, 71, 71)));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										jPanel3Layout.createSequentialGroup().addGap(0, 9, Short.MAX_VALUE)
												.addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 129,
														javax.swing.GroupLayout.PREFERRED_SIZE))
								.addGroup(jPanel3Layout.createSequentialGroup()
										.addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 102,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(0, 0, Short.MAX_VALUE)))
						.addContainerGap())
				.addGroup(jPanel3Layout.createSequentialGroup().addGap(53, 53, 53).addComponent(getReportBtn)
						.addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		studReportInternalPanel.add(jPanel3, java.awt.BorderLayout.PAGE_START);

		studentReportTable.setModel(defaultTableModel());
		jScrollPane1.setViewportView(studentReportTable);

		studReportInternalPanel.add(jScrollPane1, java.awt.BorderLayout.CENTER);

		studReportPanel.add(studReportInternalPanel, java.awt.BorderLayout.CENTER);

		cardholderPanel.add(studReportPanel, "card2");

		gradesPanel.setLayout(new java.awt.BorderLayout());

		jLabel12.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
		jLabel12.setText("Grades");
		gradesPanel.add(jLabel12, java.awt.BorderLayout.PAGE_START);

		gradesInternalPanel.setLayout(new java.awt.BorderLayout());

		updateBtn1.setText("Update");
		updateBtn1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				updateBtn1ActionPerformed(evt);
			}
		});

		groupGradesLabel.setText("Select a group:");

		groupGradesComboBox.setModel(
				new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
		groupGradesComboBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				groupGradesComboBoxActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout gradesInternalUpperPanelLayout = new javax.swing.GroupLayout(gradesInternalUpperPanel);
		gradesInternalUpperPanel.setLayout(gradesInternalUpperPanelLayout);
		gradesInternalUpperPanelLayout.setHorizontalGroup(gradesInternalUpperPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(gradesInternalUpperPanelLayout.createSequentialGroup().addGap(24, 24, 24)
						.addComponent(groupGradesLabel).addGap(51, 51, 51)
						.addComponent(groupGradesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
								javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 156, Short.MAX_VALUE)
						.addComponent(updateBtn1).addGap(23, 23, 23)));
		gradesInternalUpperPanelLayout.setVerticalGroup(gradesInternalUpperPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(gradesInternalUpperPanelLayout.createSequentialGroup().addContainerGap(38, Short.MAX_VALUE)
						.addGroup(gradesInternalUpperPanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gradesInternalUpperPanelLayout
										.createSequentialGroup()
										.addGroup(gradesInternalUpperPanelLayout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(groupGradesLabel).addComponent(groupGradesComboBox,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(34, 34, 34))
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										gradesInternalUpperPanelLayout.createSequentialGroup()
												.addComponent(updateBtn1, javax.swing.GroupLayout.PREFERRED_SIZE, 37,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(25, 25, 25)))));

		gradesInternalPanel.add(gradesInternalUpperPanel, java.awt.BorderLayout.PAGE_START);

		gradesTable.setModel(defaultTableModel());
		tableScrollPane1.setViewportView(gradesTable);
		gradesTable.getColumnModel().getSelectionModel()
				.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

		gradesInternalPanel.add(tableScrollPane1, java.awt.BorderLayout.CENTER);

		gradesPanel.add(gradesInternalPanel, java.awt.BorderLayout.CENTER);

		copyClipboardBtn1.setText("Copy As Comma Seperated Values");
		copyClipboardBtn1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				copyClipboardBtn1ActionPerformed(evt);
			}
		});
		gradesPanel.add(copyClipboardBtn1, java.awt.BorderLayout.PAGE_END);

		cardholderPanel.add(gradesPanel, "card2");

		activityPanel.setLayout(new java.awt.BorderLayout());

		jLabel5.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
		jLabel5.setText("Student Activity");
		activityPanel.add(jLabel5, java.awt.BorderLayout.PAGE_START);

		activityInternalPanel.setLayout(new java.awt.BorderLayout());

		updateBtn.setText("Update");
		updateBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				updateBtnActionPerformed(evt);
			}
		});

		groupLabel.setText("Select a group:");

		groupComboBox.setModel(
				new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
		groupComboBox.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				groupComboBoxActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout activityInternalUpperPanelLayout = new javax.swing.GroupLayout(
				activityInternalUpperPanel);
		activityInternalUpperPanel.setLayout(activityInternalUpperPanelLayout);
		activityInternalUpperPanelLayout.setHorizontalGroup(
				activityInternalUpperPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(activityInternalUpperPanelLayout.createSequentialGroup().addGap(24, 24, 24)
								.addComponent(groupLabel).addGap(51, 51, 51)
								.addComponent(groupComboBox, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(updateBtn).addGap(23, 23, 23)));
		activityInternalUpperPanelLayout.setVerticalGroup(activityInternalUpperPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(activityInternalUpperPanelLayout.createSequentialGroup().addContainerGap(38, Short.MAX_VALUE)
						.addGroup(activityInternalUpperPanelLayout
								.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										activityInternalUpperPanelLayout.createSequentialGroup()
												.addGroup(activityInternalUpperPanelLayout
														.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(groupLabel).addComponent(groupComboBox,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
												.addGap(34, 34, 34))
								.addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
										activityInternalUpperPanelLayout.createSequentialGroup()
												.addComponent(updateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 37,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGap(25, 25, 25)))));

		activityInternalPanel.add(activityInternalUpperPanel, java.awt.BorderLayout.PAGE_START);

		activityTable.setModel(defaultTableModel());
		activityTable.setColumnSelectionAllowed(true);
		tableScrollPane.setViewportView(activityTable);
		activityTable.getColumnModel().getSelectionModel()
				.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

		activityInternalPanel.add(tableScrollPane, java.awt.BorderLayout.CENTER);

		activityPanel.add(activityInternalPanel, java.awt.BorderLayout.CENTER);

		copyClipboardBtn.setText("Copy As Comma Seperated Values");
		copyClipboardBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				copyClipboardBtnActionPerformed(evt);
			}
		});
		activityPanel.add(copyClipboardBtn, java.awt.BorderLayout.PAGE_END);

		cardholderPanel.add(activityPanel, "card2");

		settingsPanel.setLayout(new java.awt.BorderLayout());

		settingsTitleJLabel.setFont(new java.awt.Font("Lucida Grande", 1, 24)); // NOI18N
		settingsTitleJLabel.setText("Settings");
		settingsPanel.add(settingsTitleJLabel, java.awt.BorderLayout.PAGE_START);

		createGroupBtn.setText("Create New Group");
		createGroupBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				createGroupBtnActionPerformed(evt);
			}
		});

		groupJList.setModel(new javax.swing.AbstractListModel<String>() {
			String[] strings = groups.keySet().toArray(new String[0]);

			public int getSize() {
				return strings.length;
			}

			public String getElementAt(int i) {
				return strings[i];
			}
		});
		groupJList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
		groupJList.setToolTipText("");
		jScrollPane3.setViewportView(groupJList);

		jLabel11.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
		jLabel11.setText("Groups:");

		editGroupBtn.setText("Edit Group");
		editGroupBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				editGroupBtnActionPerformed(evt);
			}
		});

		deleteGroupBtn.setText("Delete Group");
		deleteGroupBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				deleteGroupBtnActionPerformed(evt);
			}
		});

		jLabel2.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
		jLabel2.setText("Authentication:");

		tokenTextField.setText(CanvasMain.getToken());
		tokenTextField.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				tokenTextFieldMouseClicked(evt);
			}
		});
		tokenTextField.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				tokenTextFieldActionPerformed(evt);
			}
		});

		saveNewTokenBtn.setText("Save New Token");
		saveNewTokenBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveNewTokenBtnActionPerformed(evt);
			}
		});

		institutionTextField.setText(CanvasMain.getInstitution());
		institutionTextField.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				institutionTextFieldActionPerformed(evt);
			}
		});

		saveNewInstitutionBtn.setText("Save New Institution");
		saveNewInstitutionBtn.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveNewInstitutionBtnActionPerformed(evt);
			}
		});

		jButton3.setText("Clear Cache");
		jButton3.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton3ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
		jPanel5.setLayout(jPanel5Layout);
		jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel5Layout.createSequentialGroup().addGroup(jPanel5Layout
						.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(jPanel5Layout.createSequentialGroup().addGap(15, 15, 15)
								.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
										.addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 390,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(jLabel11)))
						.addGroup(jPanel5Layout.createSequentialGroup().addGap(38, 38, 38)
								.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
										.addComponent(institutionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 188,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addComponent(tokenTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 188,
												javax.swing.GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addGroup(jPanel5Layout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(saveNewInstitutionBtn, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(saveNewTokenBtn, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addGroup(jPanel5Layout.createSequentialGroup().addGap(31, 31, 31)
								.addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 134,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(jPanel5Layout
										.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
										.addComponent(createGroupBtn, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(editGroupBtn, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(deleteGroupBtn, javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
						.addComponent(jButton3)).addContainerGap(120, Short.MAX_VALUE)));
		jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(jPanel5Layout.createSequentialGroup().addContainerGap().addComponent(jButton3)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel11)
						.addGap(18, 18, 18)
						.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 161,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGroup(jPanel5Layout.createSequentialGroup().addGap(23, 23, 23)
										.addComponent(createGroupBtn).addGap(18, 18, 18).addComponent(editGroupBtn)
										.addGap(18, 18, 18).addComponent(deleteGroupBtn)))
						.addGap(18, 18, 18).addComponent(jLabel2)
						.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
								.addGroup(jPanel5Layout.createSequentialGroup().addComponent(saveNewTokenBtn)
										.addContainerGap())
								.addGroup(jPanel5Layout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE)
										.addComponent(tokenTextField, javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addGroup(jPanel5Layout
												.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
												.addComponent(institutionTextField,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(saveNewInstitutionBtn))))));

		settingsPanel.add(jPanel5, java.awt.BorderLayout.CENTER);

		cardholderPanel.add(settingsPanel, "card2");

		javax.swing.GroupLayout OuterJPanelLayout = new javax.swing.GroupLayout(OuterJPanel);
		OuterJPanel.setLayout(OuterJPanelLayout);
		OuterJPanelLayout
				.setHorizontalGroup(OuterJPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(OuterJPanelLayout.createSequentialGroup()
								.addComponent(sidebarPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
								.addComponent(cardholderPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addContainerGap()));
		OuterJPanelLayout.setVerticalGroup(OuterJPanelLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(sidebarPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE,
						Short.MAX_VALUE)
				.addGroup(OuterJPanelLayout.createSequentialGroup().addContainerGap()
						.addComponent(cardholderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 385, Short.MAX_VALUE)
						.addContainerGap()));

		getContentPane().add(OuterJPanel, java.awt.BorderLayout.CENTER);

		pack();
	}// </editor-fold>

	private void cardPanelsOff(javax.swing.JPanel panel) {
		homePanel.setVisible(false);
		studReportPanel.setVisible(false);
		gradesPanel.setVisible(false);
		activityPanel.setVisible(false);
		settingsPanel.setVisible(false);
		panel.setVisible(true);

	}

	private void unselectButtons(javax.swing.JToggleButton btn) {
		settingsToggleBtn.setSelected(false);
		recentActivityBtn.setSelected(false);
		studentReportTbtn.setSelected(false);
		homeToggleBtn.setSelected(false);
		gradeToggleBtn.setSelected(false);
		btn.setSelected(true);
	}

	private void setScene(javax.swing.JPanel panel, javax.swing.JToggleButton btn) {
		unselectButtons(btn);
		cardPanelsOff(panel);
	}

	private void settingsToggleBtnActionPerformed(java.awt.event.ActionEvent evt) {
		setScene(settingsPanel, settingsToggleBtn);

		updateAuth();
	}

	private void recentActivityBtnActionPerformed(java.awt.event.ActionEvent evt) {
		setScene(activityPanel, recentActivityBtn);
		groupComboBox.setModel(
				new javax.swing.DefaultComboBoxModel<>(groups.keySet().toArray(new String[groups.keySet().size()])));
	}

	private void studentReportTbtnActionPerformed(java.awt.event.ActionEvent evt) {
		setScene(studReportPanel, studentReportTbtn);
		studentJList.setModel(new javax.swing.AbstractListModel<String>() {
			String[] strings = getStudentList();

			public int getSize() {
				return strings.length;
			}

			public String getElementAt(int i) {
				return strings[i];
			}
		});

	}

	private void homeToggleBtnActionPerformed(java.awt.event.ActionEvent evt) {
		setScene(homePanel, homeToggleBtn);
	}

	private void gradeToggleBtnActionPerformed(java.awt.event.ActionEvent evt) {
		setScene(gradesPanel, gradeToggleBtn);

	}

	private void updateBtnActionPerformed(java.awt.event.ActionEvent evt) {

		activityTable.setModel(getActivityTable());
	}

	private void createGroupBtnActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		updateCreateGroup();
		groupPopupFrame.setVisible(true);
		// System.out.println("idk but : " + groupPopupFrame.getComponentCount());

	}

	private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		cleanGroupMakingExit();
	}

	private void groupNameFieldActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		createGroup();
		cleanGroupMakingExit();
	}

	private void tokenTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void groupComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void editGroupBtnActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		updateCreateGroup();
		ListModel<String> jModel;
		String groupName = (groupJList.getSelectedValue());
		List<String>[] selectedGroup = groups.get(groupName);
		List<String> selectedGroupClassList = selectedGroup[0];
		List<String> selectedGroupStudentList = selectedGroup[1];

		groupNameField.setText(groupName);

		// classes

		jModel = groupClassList.getModel();
		for (String s : selectedGroupClassList) {
			for (int i = 0; i < jModel.getSize(); i++) {
				if (s.equals(jModel.getElementAt(i))) {
					groupClassList.addSelectionInterval(i, i);
				}
			}
		}
		jModel = groupStudentList.getModel();
		for (String s : selectedGroupStudentList) {
			for (int i = 0; i < jModel.getSize(); i++) {
				if (s.equals(jModel.getElementAt(i))) {
					groupStudentList.addSelectionInterval(i, i);
				}
			}
		}

		// System.out.println("hope XVI");

		groupPopupFrame.setVisible(true);
	}

	private void deleteGroupBtnActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		groups.remove(groupJList.getSelectedValue());
		cleanGroupMakingExit();
	}

	private void copyClipboardBtnActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:

		String formattedString = "";
		for (int i = 0; i < currentTable.length; i++) {
			for (int j = 0; j < currentTable[0].length; j++) {

				formattedString = formattedString + currentTable[i][j] + ",";
			}
			formattedString = formattedString + "\n";
		}

		StringSelection stringSelection = new StringSelection(formattedString);
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);

	}

	private void updateBtn1ActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		gradesTable.setModel(getGradesTable());
	}

	private void groupGradesComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void copyClipboardBtn1ActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void getReportBtnActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		studentReportTable.setModel(getReportTable());
	}

	private void saveNewTokenBtnActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:

		CanvasMain.setToken(tokenTextField.getText());

	}

	private void institutionTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void saveNewInstitutionBtnActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		CanvasMain.setInstitution(institutionTextField.getText());

	}

	private void tokenTextFieldMouseClicked(java.awt.event.MouseEvent evt) {
		// TODO add your handling code here:
		if (evt.getButton() == 3) {
			popupToken.setLocation(evt.getLocationOnScreen());
			popupToken.setVisible(true);
		}
	}

	private void copyMenuItemMouseClicked(java.awt.event.MouseEvent evt) {
		// TODO add your handling code here:
		try {
			String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
			tokenTextField.setText(data);
		} catch (Exception e) {
			e.getMessage();
		}
		popupToken.setVisible(false);
	}

	private void copyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void copyMenuItemMouseEntered(java.awt.event.MouseEvent evt) {
		// TODO add your handling code here:
		copyMenuItem.setOpaque(true);
		copyMenuItem.setBackground(Color.CYAN);
		copyMenuItem.updateUI();
	}

	private void copyMenuItemMouseExited(java.awt.event.MouseEvent evt) {
		// TODO add your handling code here:
		popupToken.setVisible(false);
	}

	private void copyMenuItem1MouseClicked(java.awt.event.MouseEvent evt) {
		// TODO add your handling code here:
		try {
			@SuppressWarnings("unused")
			String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (Exception e) {
			e.getMessage();
		}
		ogPopupToken.setVisible(false);
	}

	private void copyMenuItem1MouseEntered(java.awt.event.MouseEvent evt) {
		// TODO add your handling code here:
		copyMenuItem1.setOpaque(true);
		copyMenuItem1.setBackground(Color.CYAN);
		copyMenuItem1.updateUI();
	}

	private void copyMenuItem1MouseExited(java.awt.event.MouseEvent evt) {
		// TODO add your handling code here:
		ogPopupToken.setVisible(false);
	}

	private void copyMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
	}

	private void groupPopupFrameComponentShown(java.awt.event.ComponentEvent evt) {
		// TODO add your handling code here:
	}

	@SuppressWarnings("static-access")
	private void formWindowClosing(java.awt.event.WindowEvent evt) {
		// TODO add your handling code here:
		JFrame frame;
		frame = new JFrame();
		int result = jOptionPane1.showConfirmDialog(frame, "Do you want to save your Preferences?",
				"Exit Confirmation : ", jOptionPane1.YES_NO_OPTION);
		if (result == jOptionPane1.YES_OPTION) {
			CanvasMain.saveOnExit(groups);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		} else if (result == jOptionPane1.NO_OPTION) {
			CanvasMain.deleteOnExit();
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		}

	}

	private void formWindowClosed(java.awt.event.WindowEvent evt) {
		// TODO add your handling code here:
	}

	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO add your handling code here:
		CanvasMain.clearCache();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		// <editor-fold defaultstate="collapsed" desc=" Look and feel setting code
		// (optional) ">
		/*
		 * If Nimbus (introduced in Java SE 6) is not available, stay with the default
		 * look and feel. For details see
		 * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
		 */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MainGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainGUI().setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify
	private javax.swing.JPanel OuterJPanel;
	private javax.swing.JPanel activityInternalPanel;
	private javax.swing.JPanel activityInternalUpperPanel;
	private javax.swing.JPanel activityPanel;
	private javax.swing.JTable activityTable;
	private javax.swing.JPanel cardholderPanel;
	private javax.swing.JButton copyClipboardBtn;
	private javax.swing.JButton copyClipboardBtn1;
	private javax.swing.JMenuItem copyMenuItem;
	private javax.swing.JMenuItem copyMenuItem1;
	private javax.swing.JButton createGroupBtn;
	private javax.swing.JButton deleteGroupBtn;
	private javax.swing.JButton editGroupBtn;
	private javax.swing.JButton getReportBtn;
	private javax.swing.JToggleButton gradeToggleBtn;
	private javax.swing.JPanel gradesInternalPanel;
	private javax.swing.JPanel gradesInternalUpperPanel;
	private javax.swing.JPanel gradesPanel;
	private javax.swing.JTable gradesTable;
	private javax.swing.JList<String> groupClassList;
	private javax.swing.JComboBox<String> groupComboBox;
	private javax.swing.JComboBox<String> groupGradesComboBox;
	private javax.swing.JLabel groupGradesLabel;
	private javax.swing.JList<String> groupJList;
	private javax.swing.JLabel groupLabel;
	private javax.swing.JTextField groupNameField;
	private javax.swing.JFrame groupPopupFrame;
	private javax.swing.JList<String> groupStudentList;
	private javax.swing.JPanel homePanel;
	private javax.swing.JToggleButton homeToggleBtn;
	public javax.swing.JTextField institutionTextField;
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JButton jButton3;
	private javax.swing.JButton jButton4;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel10;
	private javax.swing.JLabel jLabel11;
	private javax.swing.JLabel jLabel12;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel4;
	private javax.swing.JLabel jLabel5;
	private javax.swing.JLabel jLabel6;
	private javax.swing.JLabel jLabel7;
	private javax.swing.JLabel jLabel8;
	private javax.swing.JLabel jLabel9;
	protected javax.swing.JOptionPane jOptionPane1;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JPanel jPanel3;
	private javax.swing.JPanel jPanel5;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JScrollPane jScrollPane4;
	private javax.swing.JScrollPane jScrollPane5;
	private javax.swing.JScrollPane jScrollPane6;
	private javax.swing.JTextArea jTextArea1;
	private javax.swing.JPopupMenu ogPopupToken;
	private javax.swing.JPopupMenu popupToken;
	private javax.swing.JToggleButton recentActivityBtn;
	private javax.swing.JButton saveNewInstitutionBtn;
	private javax.swing.JButton saveNewTokenBtn;
	private javax.swing.JPanel settingsPanel;
	private javax.swing.JLabel settingsTitleJLabel;
	private javax.swing.JToggleButton settingsToggleBtn;
	private javax.swing.JPanel sidebarPanel;
	private javax.swing.JPanel studReportInternalPanel;
	private javax.swing.JPanel studReportPanel;
	private javax.swing.JList<String> studentJList;
	private javax.swing.JTable studentReportTable;
	private javax.swing.JToggleButton studentReportTbtn;
	private javax.swing.JScrollPane tableScrollPane;
	private javax.swing.JScrollPane tableScrollPane1;
	public javax.swing.JTextField tokenTextField;
	private javax.swing.JButton updateBtn;
	private javax.swing.JButton updateBtn1;
	// End of variables declaration

}