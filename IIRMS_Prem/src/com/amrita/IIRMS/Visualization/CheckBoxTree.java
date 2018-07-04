package com.amrita.IIRMS.Visualization;

/*
 * File Name        : CheckBoxImplementor.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Sindhya Kumari N (Junior Research Fellow)
 * Last Modified    : the 26th of March, 2015
 * Purpose          : Class to implement Checkbox Tree in IIRMSFloorVisualization panel.
 */

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

public class CheckBoxTree {

	public class CheckTreeSelectionModel extends DefaultTreeSelectionModel {

		static final long serialVersionUID = 0;
		private TreeModel model;

		public CheckTreeSelectionModel(TreeModel model) {
			this.model = model;

			setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		}

		// tests whether there is any unselected node in the subtree of given
		// path (DONT_CARE)
		public boolean isPartiallySelected(TreePath path) {
			if (isPathSelected(path, true)) {
				return false;
			}
			TreePath[] selectionPaths = getSelectionPaths();
			if (selectionPaths == null) {
				return false;
			}
			for (int j = 0; j < selectionPaths.length; j++) {
				if (isDescendant(selectionPaths[j], path)) {
					return true;
				}
			}
			return false;
		}

		// tells whether given path is selected.
		// if dig is true, then a path is assumed to be selected, if
		// one of its ancestor is selected.
		public boolean isPathSelected(TreePath path, boolean dig) {
			if (!dig)
				return super.isPathSelected(path);
			while (path != null && !super.isPathSelected(path)) {
				path = path.getParentPath();
				// System.out.println("***Path:"+path);
			}
			return path != null;
		}

		// is path1 descendant of path2
		private boolean isDescendant(TreePath path1, TreePath path2) {
			Object obj1[] = path1.getPath();
			Object obj2[] = path2.getPath();
			for (int i = 0; i < obj2.length; i++) {
				if (obj1[i] != obj2[i]) {
					return false;
				}
			}
			return true;
		}

		public void setSelectionPaths(TreePath[] pPaths) {
			throw new UnsupportedOperationException("not implemented yet!!!");
		}

		public void addSelectionPaths(TreePath[] paths) {
			// unselect all descendants of paths[]
			for (int i = 0; i < paths.length; i++) {
				TreePath path = paths[i];
				TreePath[] selectionPaths = getSelectionPaths();
				if (selectionPaths == null) {
					break;
				}
				ArrayList<TreePath> toBeRemoved = new ArrayList<TreePath>();
				for (int j = 0; j < selectionPaths.length; j++) {
					if (isDescendant(selectionPaths[j], path)) {
						toBeRemoved.add(selectionPaths[j]);
					}
				}
				super.removeSelectionPaths((TreePath[]) toBeRemoved.toArray(new TreePath[0]));
			}
			// if all siblings are selected then unselect them and select parent
			// recursively
			// otherwize just select that path.
			for (int i = 0; i < paths.length; i++) {
				TreePath path = paths[i];
				TreePath temp = null;
				while (areSiblingsSelected(path)) {
					temp = path;
					if (path.getParentPath() == null) {
						break;
					}
					path = path.getParentPath();
				}
				if (temp != null) {
					if (temp.getParentPath() != null) {
						addSelectionPath(temp.getParentPath());
					} else {
						if (!isSelectionEmpty()) {
							removeSelectionPaths(getSelectionPaths());
						}
						super.addSelectionPaths(new TreePath[] { temp });
					}
				} else {
					super.addSelectionPaths(new TreePath[] { path });
				}
			}
		}

		// tells whether all siblings of given path are selected.
		private boolean areSiblingsSelected(TreePath path) {
			TreePath parent = path.getParentPath();

			if (parent == null) {
				return true;
			}
			Object node = path.getLastPathComponent();
			Object parentNode = parent.getLastPathComponent();
			int childCount = model.getChildCount(parentNode);
			Boolean isParameters = false;
			Boolean isDescription = false;
			for (int i = 0; i < childCount; i++) {
				Object childNode = model.getChild(parentNode, i);
				if (childNode == node) {
					continue;
				}
				// If last Path component equals to "parameters" or
				// "description" - select second child too.
				if (childCount == 2) {
					if (childNode.toString().equals("parameters") && model.isLeaf(childNode)) {
						isParameters = true;
					}
					if (childNode.toString().equals("description") && model.isLeaf(childNode)) {
						isDescription = true;
					}
				}
				if (!isPathSelected(parent.pathByAddingChild(childNode)) && !isParameters && !isDescription) {
					return false;
				}
			}
			return true;
		}

		public void removeSelectionPaths(TreePath[] paths) {
			for (int i = 0; i < paths.length; i++) {
				TreePath path = paths[i];
				if (path.getPathCount() == 1) {
					super.removeSelectionPaths(new TreePath[] { path });
				} else {
					toggleRemoveSelection(path);
				}
			}
		}

		/**
		 * if any ancestor node of given path is selected then unselect it and
		 * selection all its descendants except given path and descendants.
		 * otherwise just unselect the given path
		 */
		private void toggleRemoveSelection(TreePath path) {
			Stack<TreePath> stack = new Stack<TreePath>();
			TreePath parent = path.getParentPath();
			Boolean isParameters = false;
			Boolean isDescription = false;
			while (parent != null && !isPathSelected(parent)) {
				stack.push(parent);
				parent = parent.getParentPath();
			}
			if (parent != null) {
				stack.push(parent);
			} else {
				super.removeSelectionPaths(new TreePath[] { path });
				return;
			}
			while (!stack.isEmpty()) {
				TreePath temp = (TreePath) stack.pop();
				TreePath peekPath = stack.isEmpty() ? path : (TreePath) stack.peek();
				Object node = temp.getLastPathComponent();
				Object peekNode = peekPath.getLastPathComponent();
				int childCount = model.getChildCount(node);
				for (int i = 0; i < childCount; i++) {
					Object childNode = model.getChild(node, i);
					if (childNode.toString().equals("parameters") && model.isLeaf(childNode)) {
						isParameters = true;
					}
					if (childNode.toString().equals("description") && model.isLeaf(childNode)) {
						isDescription = true;
					}
					if (childNode != peekNode) {
						if (!isParameters && !isDescription) {
							super.addSelectionPaths(new TreePath[] { temp.pathByAddingChild(childNode) });
						}
					}
				}
			}
			super.removeSelectionPaths(new TreePath[] { parent });
		}

		public TreeModel getModel() {
			return model;
		}
	}

	public class CheckTreeCellRenderer extends JPanel implements TreeCellRenderer {

		static final long serialVersionUID = 0;
		CheckTreeSelectionModel selectionModel;
		private TreeCellRenderer delegate;
		TristateCheckBox checkBox = new TristateCheckBox();

		public CheckTreeCellRenderer(TreeCellRenderer delegate, CheckTreeSelectionModel selectionModel) {
			this.delegate = delegate;
			this.selectionModel = selectionModel;
			setLayout(new BorderLayout());
			setOpaque(false);
			checkBox.setOpaque(false);
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row,
				boolean hasFocus) {
			Component renderer = delegate.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);
			TreePath path = tree.getPathForRow(row);
			if (path != null) {
				if (selectionModel.isPathSelected(path, true)) {
					checkBox.setState(checkBox.SELECTED);
				} else {
					checkBox.setState(checkBox.NOT_SELECTED);
				}

				if (selectionModel.isPartiallySelected(path)) {
					checkBox.setState(checkBox.DONT_CARE);
				}
			}

			removeAll();

			add(checkBox, BorderLayout.WEST);
			add(renderer, BorderLayout.CENTER);
			return this;
		}

		public TreeCellRenderer getDelegate() {
			return delegate;
		}

		public void setDelegate(TreeCellRenderer delegate) {
			this.delegate = delegate;
		}
	}

	public class CheckTreeManager extends MouseAdapter implements TreeSelectionListener {

		CheckTreeSelectionModel selectionModel;
		private JTree tree = new JTree();
		int hotspot = new JCheckBox().getPreferredSize().width;

		public CheckTreeManager(JTree tree, CheckTreeSelectionModel checkTreeSelectionModel) {
			this.tree = tree;

			if (checkTreeSelectionModel != null) {
				// selectionModel = new
				// CheckTreeSelectionModel(tree.getModel());
				selectionModel = checkTreeSelectionModel;
			} else {
				selectionModel = new CheckTreeSelectionModel(tree.getModel());
				// System.out.println(selectionModel.getSelectionPath());
			}
			tree.setCellRenderer(new CheckTreeCellRenderer(tree.getCellRenderer(), selectionModel));
			tree.addMouseListener(this);
			selectionModel.addTreeSelectionListener(this);
		}

		public void mouseClicked(MouseEvent me) {

			TreePath path = tree.getPathForLocation(me.getX(), me.getY());
			if (path == null) {
				// System.out.println("path==null");
				return;
			}

			if (me.getX() / 1.2 > tree.getPathBounds(path).x + hotspot) {
				// System.out.println("me.getX()/1.2>tree.getPathBounds(path).x+hotspot");
				return;
			}

			boolean selected = selectionModel.isPathSelected(path, true);
			// System.out.println("path:" + path + " select:" + selected);
			// selectionModel.removeTreeSelectionListener(this);

			try {
				if (selected) {
					selectionModel.removeSelectionPath(path);
					DefaultMutableTreeNode p = (DefaultMutableTreeNode) path.getLastPathComponent();
					String strPath = (String) p.getUserObject();
					TreePath uncheckedPath = path;
					String unselectedPath = uncheckedPath.toString().replace("[", "").replace("]", "").replace(" ", "");
					// System.out.println("remove path:" + unselectedPath);
					Iterator<String> keyIterator = IIRMSFloorVisualization.floorMap.keySet().iterator();

					while (keyIterator.hasNext()) {
						String key = keyIterator.next();
						if (key.equals(unselectedPath)) {
							String hashValue = IIRMSFloorVisualization.floorMap.get(key);
							try {
								if (hashValue.matches("Bld")) {
									for (int i = 1; i <= IIRMSFloorVisualization.numFloors; i++) {
										IIRMSVisualization.removePathAndRepaint("3D_W" + i);
										IIRMSVisualization.removePathAndRepaint("3D_R" + i);
										IIRMSVisualization.removePathAndRepaint("3D_N" + i);
										IIRMSVisualization.removePathAndRepaint("3D_D" + i);
										IIRMSVisualization.removePathAndRepaint("3D_S" + i);
										IIRMSVisualization.removePathAndRepaint("3D_E" + i);
										IIRMSVisualization.removePathAndRepaint("2D_W" + i);
										IIRMSVisualization.removePathAndRepaint("2D_R" + i);
										IIRMSVisualization.removePathAndRepaint("2D_N" + i);
										IIRMSVisualization.removePathAndRepaint("2D_D" + i);
										IIRMSVisualization.removePathAndRepaint("2D_S" + i);
										IIRMSVisualization.removePathAndRepaint("2D_E" + i);
										com.amrita.IIRMS.Visualization.IIRMSVisualization.text="";
									}
								} else if (hashValue.matches("3D")) {
									for (int i = 1; i <= IIRMSFloorVisualization.numFloors; i++) {
										IIRMSVisualization.removePathAndRepaint("3D_W" + i);
										IIRMSVisualization.removePathAndRepaint("3D_R" + i);
										IIRMSVisualization.removePathAndRepaint("3D_N" + i);
										IIRMSVisualization.removePathAndRepaint("3D_D" + i);
										IIRMSVisualization.removePathAndRepaint("3D_S" + i);
										IIRMSVisualization.removePathAndRepaint("3D_E" + i);
										com.amrita.IIRMS.Visualization.IIRMSVisualization.text="";
										
									}
								} else if (hashValue.matches("2D")) {
									for (int i = 1; i <= IIRMSFloorVisualization.numFloors; i++) {
										IIRMSVisualization.removePathAndRepaint("2D_W" + i);
										IIRMSVisualization.removePathAndRepaint("2D_R" + i);
										IIRMSVisualization.removePathAndRepaint("2D_N" + i);
										IIRMSVisualization.removePathAndRepaint("2D_D" + i);
										IIRMSVisualization.removePathAndRepaint("2D_S" + i);
										IIRMSVisualization.removePathAndRepaint("2D_E" + i);
										com.amrita.IIRMS.Visualization.IIRMSVisualization.text="";
										
									}
								} else if (hashValue.contains("F") && hashValue.contains("3D")) {
									String intValue = hashValue.replace("3D", "").replaceAll("[^0-9]", "");
									IIRMSVisualization.removePathAndRepaint("3D_W" + intValue);
									IIRMSVisualization.removePathAndRepaint("3D_R" + intValue);
									IIRMSVisualization.removePathAndRepaint("3D_N" + intValue);
									IIRMSVisualization.removePathAndRepaint("3D_D" + intValue);
									IIRMSVisualization.removePathAndRepaint("3D_S" + intValue);
									IIRMSVisualization.removePathAndRepaint("3D_E" + intValue);
									com.amrita.IIRMS.Visualization.IIRMSVisualization.text="";
								} else if (hashValue.contains("F") && hashValue.contains("2D")) {
									String intValue = hashValue.replace("2D", "").replaceAll("[^0-9]", "");
									IIRMSVisualization.removePathAndRepaint("2D_W" + intValue);
									IIRMSVisualization.removePathAndRepaint("2D_R" + intValue);
									IIRMSVisualization.removePathAndRepaint("2D_N" + intValue);
									IIRMSVisualization.removePathAndRepaint("2D_D" + intValue);
									IIRMSVisualization.removePathAndRepaint("2D_S" + intValue);
									IIRMSVisualization.removePathAndRepaint("2D_E" + intValue);
									com.amrita.IIRMS.Visualization.IIRMSVisualization.text="";
								} else {
									com.amrita.IIRMS.Visualization.IIRMSVisualization.text="";
									IIRMSVisualization.removePathAndRepaint(hashValue);
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

				} else {
					// System.out.println("not selected");
					selectionModel.addSelectionPath(path);
					DefaultMutableTreeNode p = (DefaultMutableTreeNode) path.getLastPathComponent();
					TreePath checkedPath = path;
					String selectedPath = checkedPath.toString().replace("[", "").replace("]", "").replace(" ", "");
					// System.out.println("add path:" + selectedPath);
					Iterator<String> keyIterator = IIRMSFloorVisualization.floorMap.keySet().iterator();

					while (keyIterator.hasNext()) {
						String key = keyIterator.next();
						if (key.equals(selectedPath)) {
							String hashValue = IIRMSFloorVisualization.floorMap.get(key);
							try {
								if (hashValue.matches("Bld")) {
									for (int i = 1; i <= IIRMSFloorVisualization.numFloors; i++) {
										IIRMSVisualization.addPathAndRepaint("3D_W" + i);
										IIRMSVisualization.addPathAndRepaint("3D_R" + i);
										IIRMSVisualization.addPathAndRepaint("3D_N" + i);
										IIRMSVisualization.addPathAndRepaint("3D_D" + i);
										IIRMSVisualization.addPathAndRepaint("3D_S" + i);
										IIRMSVisualization.addPathAndRepaint("3D_E" + i);
										IIRMSVisualization.addPathAndRepaint("2D_W" + i);
										IIRMSVisualization.addPathAndRepaint("2D_R" + i);
										IIRMSVisualization.addPathAndRepaint("2D_N" + i);
										IIRMSVisualization.addPathAndRepaint("2D_D" + i);
										IIRMSVisualization.addPathAndRepaint("2D_S" + i);
										IIRMSVisualization.addPathAndRepaint("2D_E" + i);
										com.amrita.IIRMS.Visualization.IIRMSVisualization.text="you are viewing 2D and 3D representation of building";
									}
								} else if (hashValue.matches("3D")) {
									for (int i = 1; i <= IIRMSFloorVisualization.numFloors; i++) {
										IIRMSVisualization.addPathAndRepaint("3D_W" + i);
										IIRMSVisualization.addPathAndRepaint("3D_R" + i);
										IIRMSVisualization.addPathAndRepaint("3D_N" + i);
										IIRMSVisualization.addPathAndRepaint("3D_D" + i);
										IIRMSVisualization.addPathAndRepaint("3D_S" + i);
										IIRMSVisualization.addPathAndRepaint("3D_E" + i);
										com.amrita.IIRMS.Visualization.IIRMSVisualization.text="you are viewing 3D representation of building";
									}
								} else if (hashValue.matches("2D")) {
									for (int i = 1; i <= IIRMSFloorVisualization.numFloors; i++) {
										IIRMSVisualization.addPathAndRepaint("2D_W" + i);
										IIRMSVisualization.addPathAndRepaint("2D_R" + i);
										IIRMSVisualization.addPathAndRepaint("2D_N" + i);
										IIRMSVisualization.addPathAndRepaint("2D_D" + i);
										IIRMSVisualization.addPathAndRepaint("2D_S" + i);
										IIRMSVisualization.addPathAndRepaint("2D_E" + i);
										com.amrita.IIRMS.Visualization.IIRMSVisualization.text="you are viewing 2D representation of building ";
									}
								} else if (hashValue.contains("F") && hashValue.contains("3D")) {
									String intValue = hashValue.replace("3D", "").replaceAll("[^0-9]", "");
									IIRMSVisualization.addPathAndRepaint("3D_W" + intValue);
									IIRMSVisualization.addPathAndRepaint("3D_R" + intValue);
									IIRMSVisualization.addPathAndRepaint("3D_N" + intValue);
									IIRMSVisualization.addPathAndRepaint("3D_D" + intValue);
									IIRMSVisualization.addPathAndRepaint("3D_S" + intValue);
									IIRMSVisualization.addPathAndRepaint("3D_E" + intValue);
									com.amrita.IIRMS.Visualization.IIRMSVisualization.text="you are viewing 3D representation of building";
								} else if (hashValue.contains("F") && hashValue.contains("2D")) {
									String intValue = hashValue.replace("2D", "").replaceAll("[^0-9]", "");
									IIRMSVisualization.addPathAndRepaint("2D_W" + intValue);
									IIRMSVisualization.addPathAndRepaint("2D_R" + intValue);
									IIRMSVisualization.addPathAndRepaint("2D_N" + intValue);
									IIRMSVisualization.addPathAndRepaint("2D_D" + intValue);
									IIRMSVisualization.addPathAndRepaint("2D_S" + intValue);
									IIRMSVisualization.addPathAndRepaint("2D_E" + intValue);
									com.amrita.IIRMS.Visualization.IIRMSVisualization.text="you are viewing 2D representation of building";

								} else {
									
									IIRMSVisualization.addPathAndRepaint(hashValue);
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			} finally {
				selectionModel.addTreeSelectionListener(this);
				tree.treeDidChange();
			}
		}

		public CheckTreeSelectionModel getSelectionModel() {
			return selectionModel;
		}

		public void setSelectionModel(CheckTreeSelectionModel selectionModel) {
			this.selectionModel = selectionModel;
		}

		public void valueChanged(TreeSelectionEvent e) {
			tree.treeDidChange();
		}
	}
}