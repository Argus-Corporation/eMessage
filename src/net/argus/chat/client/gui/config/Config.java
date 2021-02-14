package net.argus.chat.client.gui.config;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import net.argus.chat.client.gui.GUIClient;
import net.argus.file.cjson.CJSONFile;
import net.argus.file.cjson.CJSONPareser;
import net.argus.gui.OptionPane;
import net.argus.gui.Panel;
import net.argus.gui.tree.CardinalTree;
import net.argus.gui.tree.Tree;
import net.argus.gui.tree.TreeListener;
import net.argus.lang.Lang;
import net.argus.util.ThreadManager;

public class Config {

	private JFrame fen;
	private JSplitPane split;
	
	private JScrollPane scrollPanTree;
	
	private Tree configTree;
	private Panel optionPanel;
	
	private ConfigManager confManager;

	public Config() {
		configTree = getDefaultTree();
		initComponent();
	}

	private void initComponent() {
		fen = new JFrame("Config");
		fen.setResizable(false);
		fen.setSize(810, 360);
		fen.setIconImage(new ImageIcon(GUIClient.icon16).getImage());
		fen.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		fen.addWindowListener(getFrameListener());
		
		scrollPanTree = getTree();
		optionPanel = new Panel();

		configTree.addTreeListener(getTreeListener());
		
		split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPanTree, optionPanel);
		fen.add(split);
	}
	
	public void setSelectedTree(int id) {
		configTree.setSelection(id);
	}
	
	private TreeListener getTreeListener() {
		return new TreeListener() {
			public void selectedItemValue(int id) {
				getFrameListener().windowClosing(null);
				
				confManager = ConfigManager.getConfigManager(id);
				
				optionPanel = confManager.getConfigPanel();
				scrollPanTree.setPreferredSize(scrollPanTree.getSize());
				
				split.setRightComponent(optionPanel);
				split.repaint();
						
			}
		};
	}
	
	private WindowListener getFrameListener() {
		return new WindowListener() {
			public void windowOpened(WindowEvent e) {}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
			public void windowClosing(WindowEvent e) {
				if(confManager != null) {
					int result = confManager.apply();
					
					if(result == -1) {
						new ThreadManager("Error Config").start(getErrorRun(e));
						if(e != null)
							ThreadManager.stop(Thread.currentThread());
					}
				}
			}
			public void windowClosed(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
			
			private Runnable getErrorRun(WindowEvent e) {
				return () -> {
					int result = OptionPane.showConfirmDialog(fen, Lang.get("config.errorinfo.name"), "Error", JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
					if(result == JOptionPane.YES_OPTION) {
						if(e != null)
							fen.setVisible(false);
					}else
						if(e == null)
							configTree.setSelectionOldItem();
				};
			}
		};
	}
	
	private JScrollPane getTree() {
		JScrollPane	scrollPanTree = new JScrollPane(configTree);
		scrollPanTree.setMinimumSize(new Dimension(80, fen.getHeight()));
		scrollPanTree.setPreferredSize(new Dimension(180, fen.getHeight()));
		scrollPanTree.setMaximumSize(new Dimension(280, fen.getHeight()));
		
		return scrollPanTree;
	}

	private Tree getDefaultTree() {
		Tree tree = new Tree(new CardinalTree(CJSONPareser.parse(new CJSONFile("config", "bin")), "Preference"));
		return tree;
	}

	public void show() {
		fen.setLocationRelativeTo(GUIClient.frame);
		fen.setVisible(true);
	}

	public void hide() {
		fen.setVisible(false);
	}

}
