package com.amrita.IIRMS.IndoorGML.Graph;
/*
 * File Name        : IndoorGML_Layering.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Sindhya Kumari N (Junior Research Fellow),Dineshkumar E(Junior Research Fellow)
 * Last Modified    : the 28th of March , 2015
 * Purpose          : Class to add the components of building and Graphs in Layering panel.
 */
import java.awt.Graphics;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.amrita.IIRMS.IIRMSApplicationInterface;


public class IndoorGML_Layering extends JPanel{
	
	public JTree tree;
    //public JPanel treePanel;
    public static JScrollPane sPane;
    public static Connection conn;
    public static ResultSet rs;
    public static Statement stmt;
    public static List<String> floorList = new ArrayList<String>();
    CheckBoxTree AddCheckBox = new CheckBoxTree();
    public CheckBoxTree.CheckTreeManager checkTreeManager;
    protected TreePath selectedPath;
    public static Map<String, String> floorMap = new HashMap<String, String>();
    public static int numFloors=0;

    public IndoorGML_Layering() throws Exception {
        LoadDBDriver();
        setLayout(null);

        String queryFloor = "select floor_id from floor order by floor_id";
        stmt = conn.createStatement();
        rs = stmt.executeQuery(queryFloor);
        while (rs.next()) {
            floorList.add(rs.getString(1));
            numFloors++;
        }
        
        //Create tree node for JTree and put the values in hashmap
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Building");
        floorMap.put("Building", "Bld");
        DefaultMutableTreeNode root_2d = new DefaultMutableTreeNode("2D");
        root.add(root_2d);
        floorMap.put("2D", "2D");
        DefaultMutableTreeNode root_graph=new DefaultMutableTreeNode("Graph");
        root.add(root_graph);
        floorMap.put("Graph", "graph");
        
        for (String floorValue : floorList) {
            floorMap.put("Building,2D,Floor" + floorValue, "2D_F" + floorValue);
            DefaultMutableTreeNode floorNode = new DefaultMutableTreeNode("Floor" + floorValue);
            add2DFloorComponents(floorNode, floorValue);
            root_2d.add(floorNode);
        }
        
        for (String floorValue : floorList) {
            floorMap.put("Building,Graph,Floor" + floorValue, "GD_F" + floorValue);
            DefaultMutableTreeNode floorNode = new DefaultMutableTreeNode("Floor" + floorValue);
            addGraphFloorComponents(floorNode, floorValue);
            root_graph.add(floorNode);
        }

        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);
        tree.setShowsRootHandles(true);

        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }

        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        renderer.setLeafIcon(null);
        renderer.setClosedIcon(null);
        renderer.setOpenIcon(null);

        checkTreeManager = AddCheckBox.new CheckTreeManager(tree, null);
        TreePath checkedPath[] = checkTreeManager.getSelectionModel().getSelectionPaths();
        int i = 0;
        for (TreePath tp : checkedPath) {
            //System.out.println("checked path:"+checkedPath[i]);
            i++;
        }
        
        
        sPane = new JScrollPane();
        sPane.getViewport().add(tree);
        add(sPane).setSize(220, 350);

    }
   

    public void add2DFloorComponents(DefaultMutableTreeNode floorRoot, String floorNum) {
        floorMap.put("Building,2D", new String("2D"));
        floorMap.put("Building,2D,Floor" + floorNum + ",Rooms", new String("2D_R" + floorNum));
        floorMap.put("Building,2D,Floor" + floorNum + ",Walls", new String("2D_W" + floorNum));
        floorRoot.add(new DefaultMutableTreeNode("Rooms"));
        floorRoot.add(new DefaultMutableTreeNode("Walls"));
    }
    public void addGraphFloorComponents(DefaultMutableTreeNode floorRoot, String floorNum) {
        floorMap.put("Building,Graph", new String("graph"));
        floorMap.put("Building,Graph,Floor" + floorNum + ",Adjacency", new String("Adj_" + floorNum));
        floorMap.put("Building,Graph,Floor" + floorNum + ",Connectivity", new String("Con_" + floorNum));
        floorMap.put("Building,Graph,Floor" + floorNum + ",Constraints", new String("Zon_" + floorNum));
        floorRoot.add(new DefaultMutableTreeNode("Adjacency"));
        floorRoot.add(new DefaultMutableTreeNode("Connectivity"));
        floorRoot.add(new DefaultMutableTreeNode("Constraints"));
    }

    private void LoadDBDriver() throws Exception {
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/" + IndoorGML_GraphInterface.dbNameSelected, "researcher", "researcher");
    }

}
