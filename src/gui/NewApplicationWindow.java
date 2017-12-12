package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import network.IO;
import util.EmailErrorReporter;
import util.ExpireProcess;
import application.Application;
import application.ConfigurationException;
import application.FileAndPathNames;
import bus.CommsBus;
import database.DatabaseManager;
import ebay.Category;
import ebay.EBadListingProcessor;

public class NewApplicationWindow implements PropertyChangeListener {

	private JFrame frame;
	private JProgressBar progressBar;
	private JTree categoryTree;
	private JComboBox<String> domainsDropDown, wordRestrictionDropDown;
	private JButton btnSearch, btnCancel;
	public volatile static boolean RUN;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		/*
		String expireDate = "30/04/2015  00:00:00";
		if (ExpireProcess.expired(expireDate)) {
			String message = "Program expired " + expireDate;
			new MessageDialogue(message, MessageDialogue.ERROR_MESSAGE, true);
			return;
		}
*/
		try {
			Application.initialize();
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						NewApplicationWindow window = new NewApplicationWindow();
						window.frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (final Exception e) {

			EmailErrorReporter.addError(e, "Something went wrong during initialization", NewApplicationWindow.class.getName(), "main");
			EmailErrorReporter.sendErrorEmail();

			// TODO Auto-generated catch block
			e.printStackTrace();
			String message = "Sorry. We have a problem. We have been notified of the problem and it will be fixed soon(ish). For further assistance please contact us at ebaybetterlistings@gmail.com";
			new MessageDialogue(message, MessageDialogue.ERROR_MESSAGE, true);

		}
	}

	/**
	 * Create the application.
	 * 
	 * @throws Exception
	 */
	public NewApplicationWindow() throws ConfigurationException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() throws ConfigurationException {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 900, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.setTitle("eBad");

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frame.setVisible(false);
				RUN = false;
				finalizeSession();
			}
		});

		JLabel lblDomain = new JLabel("Domain");
		lblDomain.setBounds(12, 12, 70, 25);
		frame.getContentPane().add(lblDomain);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(298, 67, 589, 523);
		final DefaultMutableTreeNode allCategories = new DefaultMutableTreeNode("Categories");
		final JTree tree = new JTree(allCategories);
		categoryTree = tree;
		categoryTree.expandRow(0);
		categoryTree.setRootVisible(false);
		categoryTree.setShowsRootHandles(true);
		categoryTree.getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		scrollPane.setViewportView(categoryTree);
		frame.getContentPane().add(scrollPane);

		domainsDropDown = new JComboBox<String>();
		domainsDropDown.setBounds(138, 12, 155, 25);
		frame.getContentPane().add(domainsDropDown);

		String[] domains = Application.getDomains();
		for (String c : domains) {
			domainsDropDown.addItem(c);
		}
		String selectedDomain = Application.getSelectedDomain();
		domainsDropDown.setSelectedItem(selectedDomain);
		domainsDropDown.addItemListener(new ItemListener() {
			{
				repaintCategories();
			}

			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					Object item = event.getItem();
					try {
						Application.saveSelectedDomain((String) item);
						repaintCategories();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						EmailErrorReporter.addError(e, "Something went wrong while saving selected domain", this.getClass().getName(), "initialize");
					}
				}
			}

			private void repaintCategories() throws ConfigurationException {
				DefaultTreeModel model = ((DefaultTreeModel) categoryTree.getModel());
				DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
				root.removeAllChildren();

				Map<String, Category> categories = Application.getCategories(Application.getSelectedDomain());
				for (Map.Entry<String, Category> entry : categories.entrySet()) {
					Category parent = entry.getValue();
					DefaultMutableTreeNode parentTN = new DefaultMutableTreeNode(parent);
					allCategories.add(parentTN);
					if (parent.hasChildren()) {
						for (Category child : parent.getCategories()) {
							DefaultMutableTreeNode childTN = new DefaultMutableTreeNode(child);
							parentTN.add(childTN);
							if (child.hasChildren()) {
								for (Category grandChild : child.getCategories()) {
									DefaultMutableTreeNode grandChildTN = new DefaultMutableTreeNode(grandChild);
									childTN.add(grandChildTN);
									if (grandChild.hasChildren()) {
										for (Category greatGrandChild : grandChild.getCategories()) {
											DefaultMutableTreeNode greatGrandChildTN = new DefaultMutableTreeNode(greatGrandChild);
											grandChildTN.add(greatGrandChildTN);
											if (greatGrandChild.hasChildren()) {
												for (Category greatGreatGrandChild : greatGrandChild.getCategories()) {
													DefaultMutableTreeNode greatGreatGrandChildTN = new DefaultMutableTreeNode(greatGreatGrandChild);
													greatGrandChildTN.add(greatGreatGrandChildTN);
													if (greatGreatGrandChild.hasChildren()) {
														for (Category greatGreatGreatGrandChild : greatGreatGrandChild.getCategories()) {
															DefaultMutableTreeNode greatGreatGreatGrandChildTN = new DefaultMutableTreeNode(greatGreatGreatGrandChild);
															greatGreatGrandChildTN.add(greatGreatGreatGrandChildTN);
														}
													}
												}

											}
										}
									}
								}
							}
						}
					}
				}
				model.reload();

			}
		});

		JLabel lblWords = new JLabel("Words in listing");
		lblWords.setBounds(12, 42, 120, 25);
		frame.getContentPane().add(lblWords);

		wordRestrictionDropDown = new JComboBox<String>();
		wordRestrictionDropDown.setBounds(138, 42, 155, 25);
		frame.getContentPane().add(wordRestrictionDropDown);
		wordRestrictionDropDown.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				if (event.getStateChange() == ItemEvent.SELECTED) {
					Object item = event.getItem();
					try {
						Application.saveSelectedWordRestriction((String) item);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						EmailErrorReporter.addError(e, "Something went wrong while saving word restriction", this.getClass().getName(), "initialize");
					}
				}
			}
		});
		String wordRestrictionSelection = String.valueOf(Application.getSelectedWordRestriction());
		wordRestrictionDropDown.addItem(wordRestrictionSelection);
		String[] wordNos = new String[] { "1", "2", "3" };
		for (String c : wordNos) {
			if (!wordRestrictionSelection.equals(c)) {
				wordRestrictionDropDown.addItem(c);
			}
		}

		btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startSearch();
			}
		});
		btnSearch.setBounds(138, 72, 155, 25);
		frame.getContentPane().add(btnSearch);

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelSearch();
			}
		});
		btnCancel.setBounds(138, 102, 155, 25);
		btnCancel.setEnabled(false);
		frame.getContentPane().add(btnCancel);

		MessageDisplayArea lblMessage = new MessageDisplayArea("Ready...");
		lblMessage.setBounds(12, 132, 270, 150);
		frame.getContentPane().add(lblMessage);
		Application.addMessageListener(lblMessage);

		MessageDisplayLabel lblError = new MessageDisplayLabel();
		lblError.setBounds(12, 292, 270, 75);
		lblError.setForeground(Color.RED);
		frame.getContentPane().add(lblError);
		Application.addErrorMessageListener(lblError);

		progressBar = new JProgressBar();
		progressBar.setBounds(298, 12, 589, 25);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		frame.getContentPane().add(progressBar);

		JLabel lblShift = new JLabel("Hold 'Ctrl' for multiple selections");
		lblShift.setBounds(320, 42, 300, 25);
		frame.getContentPane().add(lblShift);

		JButton btnHelp = new JButton("Help");
		btnHelp.setBounds(138, 563, 155, 25);
		frame.getContentPane().add(btnHelp);
		btnHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Application.openHelp();
			}
		});

		try {
			// load up some icons
			Image ebay_24_black_png = IO.loadImage("ebay_24_black.png");
			ImageIcon treeIcon = new ImageIcon(ebay_24_black_png);

			DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
			renderer.setLeafIcon(treeIcon);
			tree.setCellRenderer(renderer);

			if (SystemTray.isSupported()) {
				Image ebay_png = IO.loadImage("ebay.png");
				ImageIcon trayIcon = new ImageIcon(ebay_png);
				Image img = trayIcon.getImage();
				TrayIcon icon = new TrayIcon(img);
				icon.setImageAutoSize(true);
				SystemTray.getSystemTray().add(icon);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			EmailErrorReporter.addError(e, "Something went wrong while loading image files", this.getClass().getName(), "initialize");
		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if ("progress".equals(evt.getPropertyName())) {
			int value = (Integer) evt.getNewValue();
			progressBar.setValue(value);
		}
	}

	private List<Category> getSelectedCategories() {
		List<Category> categories = new LinkedList<Category>();
		if (countSelectedCategories() > 0) {
			TreePath[] paths = categoryTree.getSelectionPaths();
			for (TreePath path : paths) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
				if (node == null)
					continue;

				Enumeration<DefaultMutableTreeNode> en = node.depthFirstEnumeration();
				while (en.hasMoreElements()) {
					DefaultMutableTreeNode node1 = en.nextElement();
					if (node1.isLeaf()) {
						Object object = node1.getUserObject();
						Category category = (Category) object;
						category.setPathSteps(node1.getPath());
						if (!categories.contains(category)) {
							categories.add(category);
						}
						
					}
				}

			}
		}
		return categories;
	}

	private void startSearch() {
		if (countSelectedCategories() == 0) {
			String message = "You have not selected any categories to search.";
			new MessageDialogue(message, MessageDialogue.INFORMATION_MESSAGE, false);
			return;
		}

		SearchTask searchTask = new SearchTask();
		searchTask.addPropertyChangeListener(this);
		searchTask.execute();
	}

	private void cancelSearch() {
		RUN = false;
	}

	private int countSelectedCategories() {
		return categoryTree.getSelectionCount();
	}

	private class SearchTask extends SwingWorker<Void, Void> {

		@Override
		public Void doInBackground() {
			RUN = true;

			enableComponents(false);
			setProgress(1);
			List<Category> categories = getSelectedCategories();
			DatabaseManager databaseManager = new DatabaseManager();
			try {
				databaseManager.increaseCategoryCount(categories.size());
			} catch (SQLException e1) {
				e1.printStackTrace();
				EmailErrorReporter.addError(e1, "Something went wrong while persisting category count ", this.getClass().getName(), "doInBackground");
			}
			EBadListingProcessor processor = new EBadListingProcessor();

			float progress = 0, increment = 100 / ((float) categories.size());
			for (Category category : categories) {
				if (!RUN)//close operation set run to false
					break;
				try {
					processor.processCategory(category);
				} catch (Exception e) {
					e.printStackTrace();
					EmailErrorReporter.addError(e, "Something went wrong while searching through categories. Current category: " + category.getName(), this.getClass().getName(), "doInBackground");
				}
				progress += increment;
				setProgress((int) Math.min(progress, 100));
			}

			setProgress(100);
			processor.closeConnections();

			boolean resultsPageDisplayed = Application.showSearchresults();

			if (!resultsPageDisplayed) {
				String message = "Please view search results in file " + FileAndPathNames.RESULTS_PAGE_PATH;
				new MessageDialogue(message, MessageDialogue.INFORMATION_MESSAGE, false);
			}
			return null;
		}

		/*
		 * Executed in event dispatching thread
		 */
		@Override
		public void done() {
			DatabaseManager databaseManager = new DatabaseManager();
			try {
				CommsBus.messageSent("Found " + databaseManager.getItemCount() + " items in " + databaseManager.getCategoryCount() + " categories.");
			} catch (SQLException e1) {
				e1.printStackTrace();
				EmailErrorReporter.addError(e1, "Something went wrong while accessing item or category count ", this.getClass().getName(), "done");
			}

			// as the program can be left running without closing must clear
			// tables after each new listing
			Application.shutdown();

			enableComponents(true);
		}
	}

	private void enableComponents(boolean b) {
		btnCancel.setEnabled(!b);
		btnSearch.setEnabled(b);
		domainsDropDown.setEnabled(b);
		wordRestrictionDropDown.setEnabled(b);
		categoryTree.setEnabled(b);
	}

	private void finalizeSession() {
		String wordRestriction = (String) wordRestrictionDropDown.getSelectedItem();
		String domain = (String) domainsDropDown.getSelectedItem();
		List<Category> categories = getSelectedCategories();

		try {
			Application.saveAllSelectionsToConfig(wordRestriction, domain, categories);
			Application.saveConfig();
		} catch (Exception e) {
			EmailErrorReporter.addError(e, "Something went wrong during shutdown", this.getClass().getName(), "finalizeSession");
			e.printStackTrace();
		}
		if (EmailErrorReporter.hasErrors()) {
			EmailErrorReporter.sendErrorEmail();
		}

	}

}
