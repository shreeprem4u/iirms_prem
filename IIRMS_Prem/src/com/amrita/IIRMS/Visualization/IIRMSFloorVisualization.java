package com.amrita.IIRMS.Visualization;
/*
 * File Name        : IIRMSFloorVisualization.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Sindhya Kumari N (Junior Research Fellow),Dineshkumar E(Junior Research Fellow)
 * Last Modified    : the 28th of March , 2015
 * Purpose          : Class to add the components of building in Layering Panel.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.*;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;


public class IIRMSFloorVisualization extends JPanel {

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

    public IIRMSFloorVisualization() throws Exception {
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
        DefaultMutableTreeNode root_3d = new DefaultMutableTreeNode("3D");
        root.add(root_3d);
        floorMap.put("3D", "3D");
        DefaultMutableTreeNode root_2d = new DefaultMutableTreeNode("2D");
        root.add(root_2d);
        floorMap.put("2D", "2D");
        for (String floorValue : floorList) {
            floorMap.put("Building,3D,Floor" + floorValue, "3D_F" + floorValue);
            DefaultMutableTreeNode floorNode = new DefaultMutableTreeNode("Floor" + floorValue);
            add3DFloorComponents(floorNode, floorValue);
            root_3d.add(floorNode);
        }
        for (String floorValue : floorList) {
            floorMap.put("Building,2D,Floor" + floorValue, "2D_F" + floorValue);
            DefaultMutableTreeNode floorNode = new DefaultMutableTreeNode("Floor" + floorValue);
            add2DFloorComponents(floorNode, floorValue);
            root_2d.add(floorNode);
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
        add(sPane).setSize(220, 700);

    }

    public void add3DFloorComponents(DefaultMutableTreeNode floorRoot, String floorNum) {
        floorMap.put("Building,3D", new String("3D"));
        floorMap.put("Building,3D,Floor" + floorNum + ",Rooms", new String("3D_R" + floorNum));
        floorMap.put("Building,3D,Floor" + floorNum + ",Walls", new String("3D_W" + floorNum));
        floorMap.put("Building,3D,Floor" + floorNum + ",Doors", new String("3D_D" + floorNum));
        floorMap.put("Building,3D,Floor" + floorNum + ",Windows", new String("3D_N" + floorNum));
        floorMap.put("Building,3D,Floor" + floorNum +",Stairs", new String("3D_S"+floorNum));
        floorMap.put("Building,3D,Floor" + floorNum +",Sensors", new String("3D_E"+floorNum));
        floorRoot.add(new DefaultMutableTreeNode("Rooms"));
        floorRoot.add(new DefaultMutableTreeNode("Walls"));
        floorRoot.add(new DefaultMutableTreeNode("Doors"));
        floorRoot.add(new DefaultMutableTreeNode("Windows"));
        floorRoot.add(new DefaultMutableTreeNode("Stairs"));
        floorRoot.add(new DefaultMutableTreeNode("Sensors"));
    }

    public void add2DFloorComponents(DefaultMutableTreeNode floorRoot, String floorNum) {
        floorMap.put("Building,2D", new String("2D"));
        floorMap.put("Building,2D,Floor" + floorNum + ",Rooms", new String("2D_R" + floorNum));
        floorMap.put("Building,2D,Floor" + floorNum + ",Walls", new String("2D_W" + floorNum));
        floorMap.put("Building,2D,Floor" + floorNum + ",Doors", new String("2D_D" + floorNum));
        floorMap.put("Building,2D,Floor" + floorNum + ",Windows", new String("2D_N" + floorNum));
        floorMap.put("Building,2D,Floor" + floorNum + ",Stairs", new String("2D_S" + floorNum));
        floorMap.put("Building,2D,Floor" + floorNum + ",Sensors", new String("2D_E" + floorNum));
        floorRoot.add(new DefaultMutableTreeNode("Rooms"));
        floorRoot.add(new DefaultMutableTreeNode("Walls"));
        floorRoot.add(new DefaultMutableTreeNode("Doors"));
        floorRoot.add(new DefaultMutableTreeNode("Windows"));
        floorRoot.add(new DefaultMutableTreeNode("Stairs"));
        floorRoot.add(new DefaultMutableTreeNode("Sensors"));
    }

    private void LoadDBDriver() throws Exception {
        Class.forName("org.postgresql.Driver");
        conn = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/" + IIRMSQueryInterface.dbNameSelected, "researcher", "researcher");
    }
}
