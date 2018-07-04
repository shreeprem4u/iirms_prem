package com.amrita.IIRMS;

/*
 * File Name        : IIRMSQUERY.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Ajeissh M(Junior Research Fellow), Dinesh Kumar E(Junior Research Fellow),Sindhya Kumari N(Junior Research Fellow)
 * Last Modified    : the 18th of December, 2015
 * Purpose          : Class to display the Query UI for database.
 */
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.amrita.IIRMS.Navigation.Navigation;
import com.amrita.IIRMS.Visualization.IIRMSQueryInterface;
import com.amrita.IIRMS.Visualization.IIRMSVisualization;
import com.jogamp.opengl.util.awt.TextRenderer;

public class IIRMSQuery extends IIRMSVisualization {

	public int i;
	public static JFrame fQuery;
	public static JPanel pQuery;
	public static JButton bRoom, bWall, bWindow, bDoor, bReset, bCancel, bStair;
	
	// For rooms
	String[] sampleCombo = { " " };
	public static Connection conn;
	public static JLabel iRoom, iWall, iWindow, iDoor, iMain, larea, lareain, lvolume, lvolumein, lroomtype, lroomtypein, lroomin, lroom,
			lcapacity, ldist1, ldist2,lnavi, lwindowofroom, ladjacent, lroom250, lkrooms, lkrooms2, lexit, lkexits, lkexits2;
	public static JComboBox cAreaCondition, cAreaFloor, cVolumeCondition, cVolumeFloor, cRoomType, cRoomTypeFloor, cRoomFloor, cRoom,
			cSeating, cDist1, cDist2, cWindowsRoom, cAdjacent, cRoom250, cKRooms, cK, cExitRoom, cKE, cKExits;
	// For walls
	public static JLabel lInnerWalls, lWalls, lSelect, lCommonWalls, lWallsWindows;
	public static JComboBox cInnerWalls, cWalls, cSelect1, cSelect2, cSelect3, cCommonWalls, cWallsWindows, cMaterial, cType;
	// For windows
	public static JLabel lSliding, lWooden, lSingle, lDouble, lGlass;
	public static JComboBox cSliding, cWooden, cSingle, cDouble, cGlass;
	// For doors
	public static JLabel lInward, lOutward, lMaterial1, lMaterial2, lSingleDoors, lDoubleDoors;
	public static JComboBox cInward, cOutward, cMaterial1, cMaterial2, cSingleDoors, cDoubleDoors;
	public static ImageIcon iccMain, iccRoom, iccWall, iccWindow, iccDoor, iccStair;
	public static Image imgMain, imgRoom, imgWall, imgWindow, imgDoor, imgStair;
	// For stairs
	public static JLabel iStair, lStair;
	public static JComboBox cStair;
	// Text boxes for input of area and volume
	public static JTextField tArea, tVolume;
	public static Statement stmtFloor;
	public static ResultSet rsFloor;
	// Lists for results of walls
	public static java.util.List<BoundsGeom> innerWallBoundsList, innerWallList, commonWallList, windowRoomList;
	// Lists for results of rooms
	public static java.util.List<BoundsGeom> areaList, volumeList, roomTypeList, roomFloorList, roomList, seatingList, distList,
			roomWindowList, adjacentList, room250List, kRoomsList, kExitsList, exitRoomList;
	// Lists for results of windows
	public static java.util.List<BoundsGeom> slidingList, woodenList, singleList, doubleList, glassList;
	// Lists for results of doors
	public static java.util.List<BoundsGeom> inwardList, outwardList, materialList, singleDoorList, doubleDoorList;
	// List of results of stairs
	public static java.util.List<BoundsGeom> stairList;
	static String selectValueFinal = "";
	// initialization for visualisation and db
	private static final long serialVersionUID = 1L;
	
	public static Statement stmt;
	public static ResultSet rs;
	public static JButton rROOM;
	public static String roomno;
	public int roomid;
	private static JLabel lwindowtype;
	private static JLabel lwindowtype1;
	
	private static JComboBox Jcb_typewindows,source, destination, Jcb_materialwindows,Cwtype;
	private static JTextField tdistvalue;
	private static JLabel lroomtd;

	private static JLabel lwooden1;
	private static JLabel navigation;
	
	protected static double area;
	protected static int countWindows;
	private static IIRMSVisualization visual;
	protected static int wall;
	protected static int door;
	protected static int window;
	public static float rotateX, rotateY, rotateZ;
	public static double zoom;
	private static TextRenderer txtRenderer;
	private static String distance1;
	private static String room_id;
	private static String wallid;

	/*
	 * Method name : IIRMSQuery Method description : Constructor to define the
	 * form components and their behaviors. Method Arguments : null Arguments
	 * description : -- Return type : null Return type description : --
	 */
	public IIRMSQuery() throws Exception {
		visual = new IIRMSVisualization();
		rotateX = 20;
		rotateY = 0;
		rotateZ = 10;
		zoom = 0.5;
		com.amrita.IIRMS.Visualization.IIRMSVisualization.text="";
		// String[] floorValue = {};
		Class.forName("org.postgresql.Driver");
		String dbName = IIRMSQueryInterface.dbNameSelected;
		conn = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/" + dbName, "researcher", "researcher");

		// List to contain the list of floors in the building.
		java.util.List<Integer> floorList = new ArrayList<Integer>();
		stmtFloor = conn.createStatement();
		rsFloor = stmtFloor.executeQuery("select distinct floor_id from floor");
		while (rsFloor.next()) {
			floorList.add(rsFloor.getInt("floor_id"));
		}
		// String[] floorValue = (String[]) floorList.toArray();
		Collections.sort(floorList);
		// Sorted order of floor list with a "space" value in the beginning.
		java.util.List<String> floorFinalValue = new ArrayList<String>();
		floorFinalValue.add(" ");
		for (Integer inn : floorList) {
			// System.out.println(inn);
			floorFinalValue.add(Integer.toString(inn));
		}

		String[] floorValue = floorFinalValue.toArray(new String[floorFinalValue.size()]);

		Statement stmtRoom = conn.createStatement();
		ResultSet rsRoom = stmtRoom.executeQuery("select distinct room_name from room");
		// List to contain the rooms in the building.
		java.util.List<String> roomDisplayList = new ArrayList<String>();
		roomDisplayList.add(" ");
		while (rsRoom.next()) {
			// System.out.println(rsRoom.getString("room_name"));
			roomDisplayList.add(rsRoom.getString("room_name"));
		}
		String[] roomValue = roomDisplayList.toArray(new String[roomDisplayList.size()]);
		for (String ini : roomValue) {
			// System.out.println(ini);
		}

		Statement stmtTypeRoom = conn.createStatement();
		ResultSet rsTypeRoom = stmtTypeRoom.executeQuery("select distinct room_type from room");
		// List to contain the "types" of rooms in the building.
		java.util.List<String> roomTypeDisplayList = new ArrayList<String>();
		roomTypeDisplayList.add(" ");
		while (rsTypeRoom.next()) {
			roomTypeDisplayList.add(rsTypeRoom.getString("room_type"));
		}
		String[] roomTypeValue = roomTypeDisplayList.toArray(new String[roomTypeDisplayList.size()]);

		// Defining the frame for Query Window.
		fQuery = new JFrame("Query Window - IIRMS");

		pQuery = new JPanel();
		pQuery.setLayout(null);

		// Defining the background, to be added last
				URL url = new URL("http://172.17.9.60/html/pics/queryMain.jpg");
				ImageIcon icon = new ImageIcon(url);
				imgMain = icon.getImage();
				imgMain = imgMain.getScaledInstance(Toolkit.getDefaultToolkit().getScreenSize().width,
						Toolkit.getDefaultToolkit().getScreenSize().height, Image.SCALE_SMOOTH);

		iccMain = new ImageIcon(imgMain);
		iMain = new JLabel(iccMain);
		iMain.setBounds(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);

		// Defining the type components
		// Defining the type components
				url = new URL("http://172.17.9.60/html/pics/roomImage.jpg");
				icon = new ImageIcon(url);
				imgRoom = icon.getImage();
		iccRoom = new ImageIcon(imgRoom);
		iRoom = new JLabel(iccRoom);
		iRoom.setBounds(10, 10, 100, 100);

		// Button for "Room" querying.
		bRoom = new JButton("Room");
		bRoom.setBounds(10, 120, 100, 40);
		bRoom.setToolTipText("Click here for querying information about rooms  such as location of room, adjacent rooms,nearest rooms,area and volume of room");
		bRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				setRoomVisible(true);
				setWallVisible(false);
				setWindowVisible(false);
				setDoorVisible(false);
				setStairVisible(false);
			}
		});

		url = new URL("http://172.17.9.60/html/pics/wallImage.jpeg");
		icon = new ImageIcon(url);
		imgWall = icon.getImage();
		imgWall = imgWall.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		iccWall = new ImageIcon(imgWall);
		iWall = new JLabel(iccWall);
		iWall.setBounds(120, 10, 100, 100);

		// Button for "Wall" querying.
		bWall = new JButton("Wall");
		bWall.setBounds(120, 120, 100, 40);
		bWall.setToolTipText("Click here for querying information about walls such as material used to build, common walls");
		bWall.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				setRoomVisible(false);
				setWallVisible(true);
				setWindowVisible(false);
				setDoorVisible(false);
				setStairVisible(false);
			}
		});

		url = new URL("http://172.17.9.60/html/pics/doorImage.jpeg");
		icon = new ImageIcon(url);
		imgDoor = icon.getImage();
		imgDoor = imgDoor.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		iccDoor = new ImageIcon(imgDoor);
		iDoor = new JLabel(iccDoor);
		iDoor.setBounds(240, 10, 100, 100);

		// Button for "Door" querying.
		bDoor = new JButton("Door");
		bDoor.setToolTipText("Click here for querying information about doors such as material used to build, type of door");
		bDoor.setBounds(240, 120, 100, 40);
		bDoor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				setRoomVisible(false);
				setWallVisible(false);
				setWindowVisible(false);
				setDoorVisible(true);
				setStairVisible(false);
			}
		});

		url = new URL("http://172.17.9.60/html/pics/windowImage.jpeg");
		icon = new ImageIcon(url);
		imgWindow = icon.getImage();
		imgWindow = imgWindow.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		iccWindow = new ImageIcon(imgWindow);
		iWindow = new JLabel(iccWindow);
		iWindow.setBounds(360, 10, 100, 100);

		// Button for "Window" querying.
		bWindow = new JButton("Window");
		bWindow.setToolTipText("Click here for querying information about windows such as material used to build, type of window");
		bWindow.setBounds(360, 120, 100, 40);
		bWindow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				setRoomVisible(false);
				setWallVisible(false);
				setWindowVisible(true);
				setDoorVisible(false);
				setStairVisible(false);
			}
		});

		url = new URL("http://172.17.9.60/html/pics/stairImage.jpg");
		icon = new ImageIcon(url);
		imgStair = icon.getImage();
		imgStair = imgStair.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
		iccStair = new ImageIcon(imgStair);
		iStair = new JLabel(iccStair);
		iStair.setBounds(480, 10, 100, 100);

		// Button for "Stair" querying
		bStair = new JButton("Stair");
		bStair.setToolTipText("Click here for querying information about Stairs such as where it is located in building");
		bStair.setBounds(480, 120, 100, 40);
		bStair.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				setRoomVisible(false);
				setWallVisible(false);
				setWindowVisible(false);
				setDoorVisible(false);
				setStairVisible(true);
			}
		});

		// Button for Reset
		// Button for Reset
		bReset = new JButton("Reset");
		bReset.setBounds(500, 170, 75, 20);
		bReset.setToolTipText("Clear the visualization");
		bReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				
//				tdistvalue.setText(" ");
//				tArea.setText(" ");
//				tVolume.setText(" ");
//				cAreaCondition.setSelectedIndex(0);
//				cAreaFloor.setSelectedIndex(0);
//				cVolumeCondition.setSelectedIndex(0);
//				cVolumeFloor.setSelectedIndex(0);
//				cRoomType.setSelectedIndex(0);
//				cRoomTypeFloor.setSelectedIndex(0);
//				cRoomFloor.setSelectedIndex(0);
//				cRoom.setSelectedIndex(0);
//			    cDist1.setSelectedIndex(0);
//				cDist2.setSelectedIndex(0);
//				cWindowsRoom.setSelectedIndex(0);
//				cAdjacent.setSelectedIndex(0);
//				cRoom250.setSelectedIndex(0);
//				cKRooms.setSelectedIndex(0);
//				cK.setSelectedIndex(0);
//				cExitRoom.setSelectedIndex(0);
//				source.setSelectedIndex(0);
//         		 destination.setSelectedIndex(0);
//				cKE.setSelectedIndex(0);
//				cKExits.setSelectedIndex(0);
//			cInnerWalls.setSelectedIndex(0);
//				cWalls.setSelectedIndex(0);
//		
//			cCommonWalls.setSelectedIndex(0);
//				cWallsWindows.setSelectedIndex(0);
//				cMaterial.setSelectedIndex(0);
//			cType.setSelectedIndex(0);
//		
//			cWooden.setSelectedIndex(0);
//			cSingle.setSelectedIndex(0);
//			cDouble.setSelectedIndex(0);
//			cGlass.setSelectedIndex(0);
//			cInward.setSelectedIndex(0);
//			cOutward.setSelectedIndex(0);
//			cMaterial1.setSelectedIndex(0);
//			cMaterial2.setSelectedIndex(0);
//			cSingleDoors.setSelectedIndex(0);
//			cDoubleDoors.setSelectedIndex(0);
//			 cStair.setSelectedIndex(0);
//			 Jcb_typewindows.setSelectedIndex(0);
//			 Jcb_materialwindows.setSelectedIndex(0);
//			 Cwtype.setSelectedIndex(0);
			 IIRMSVisualization.dispQueryResult(0);
				// resetValues();

			}
		});
		fQuery.add(bReset);

		// Button for Cancel
		bCancel = new JButton("Close");
		bCancel.setBounds(500, 200, 75, 20);
		bCancel.setToolTipText("Close the Query window");
		bCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				IIRMSVisualization.dispQueryResult(0);
				fQuery.hide();
				// fVisual.setVisible(false);
			}
		});
		fQuery.add(bCancel);


		// Defining the window components
		// Labels to display the query being executed.
		String materialQuery = "SELECT distinct window_material from windows";
		Statement materialStmt = conn.createStatement();
		ResultSet rsMaterial = materialStmt.executeQuery(materialQuery);
		java.util.List<String> materialList = new ArrayList<String>();
		materialList.add(" ");
		while (rsMaterial.next()) {
			// System.out.println(rsMaterial.getString(1));
			materialList.add(rsMaterial.getString(1));
		}

		String typeQuery = "SELECT distinct window_type from windows";
		Statement typeStmt = conn.createStatement();
		ResultSet rsType = typeStmt.executeQuery(typeQuery);
		java.util.List<String> typeList = new ArrayList<String>();
		typeList.add(" ");
		while (rsType.next()) {
			// System.out.println(rsType.getString(1));
			typeList.add(rsType.getString(1));
		}

		final String[] wmaterialValue = materialList.toArray(new String[materialList.size()]);
		// for (String inn : materialValue) {
		// System.out.println(inn);
		// }
		final String[] wtypeValue = typeList.toArray(new String[typeList.size()]);
		/*
		 * for (String inn : typeValue) { System.out.println("******" + inn); }
		 */
       
		lWooden = new JLabel("Display windows of material");
		lWooden.setBounds(20, 180, 300, 20);
        Jcb_materialwindows= new JComboBox(wmaterialValue);
        Jcb_materialwindows.setBounds(230,180,75,20);
        lwooden1=new JLabel("in floor");
        lwooden1.setBounds(310,180,100,20);
        		
		cWooden = new JComboBox(floorValue);
		cWooden.setBounds(360, 180, 100, 20);
		cWooden.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				woodenList = new ArrayList<BoundsGeom>();
				try {
					IIRMSVisualization.dispQueryResult(31);

				} catch (Exception ex) {
				}
			}
		});
		//window type
				lwindowtype = new JLabel("Display windows of type");
				lwindowtype.setBounds(20, 220, 300, 20);
				Jcb_typewindows= new JComboBox(wtypeValue);
		        Jcb_typewindows.setBounds(230,220,75,20);
				lwindowtype1 = new JLabel("in floor");
				lwindowtype1.setBounds(310, 220, 300, 20);

		Cwtype = new JComboBox(floorValue);
		Cwtype.setBounds(360, 220, 100, 20);
		Cwtype.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				
				try {
					IIRMSVisualization.dispQueryResult(32);

				} catch (Exception ex) {
				}
			}
		});

		

		

		// Defining the door components
		// Labels to display the query being executed.
		lInward = new JLabel("Show me inward direction doors in floor");
		lInward.setBounds(20, 180, 300, 20);

		lOutward = new JLabel("Show me outward direction doors in floor");
		lOutward.setBounds(20, 220, 300, 20);

		lMaterial1 = new JLabel("Select doors which is of material");
		lMaterial1.setBounds(20, 260, 300, 20);

		lMaterial2 = new JLabel("of floor");
		lMaterial2.setBounds(350, 260, 100, 20);

		lSingleDoors = new JLabel("Show me single  doors in floor");
		lSingleDoors.setBounds(20, 300, 300, 20);

		lDoubleDoors = new JLabel("Show me double  doors in floor");
		lDoubleDoors.setBounds(20, 340, 300, 20);

		cInward = new JComboBox(floorValue);
		cInward.setBounds(360, 180, 100, 20);
		cInward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				inwardList = new ArrayList<BoundsGeom>();
				try {
					IIRMSVisualization.dispQueryResult(21);

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		cOutward = new JComboBox(floorValue);
		cOutward.setBounds(360, 220, 100, 20);
		cOutward.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				outwardList = new ArrayList<BoundsGeom>();
				try {
					IIRMSVisualization.dispQueryResult(22);

				} catch (Exception ex) {
				}
			}
		});

		Statement stmtDoorMaterial = conn.createStatement();

		String queryDoorMaterial = "SELECT distinct exit_material from exit";
		ResultSet rsDoorMaterial = stmtDoorMaterial.executeQuery(queryDoorMaterial);
		java.util.List<String> doorMaterialList = new ArrayList<String>();
		doorMaterialList.add(" ");
		while (rsDoorMaterial.next()) {
			doorMaterialList.add(rsDoorMaterial.getString(1));
		}

		String[] doorMaterialValue = doorMaterialList.toArray(new String[doorMaterialList.size()]);

		cMaterial1 = new JComboBox(doorMaterialValue);
		cMaterial1.setBounds(265, 260, 75, 20);

		cMaterial2 = new JComboBox(floorValue);
		cMaterial2.setBounds(410, 260, 50, 20);
		cMaterial2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				
				try {
					IIRMSVisualization.dispQueryResult(23);

				} catch (Exception ex) {
				}
			}
		});

		cSingleDoors = new JComboBox(floorValue);
		cSingleDoors.setBounds(360, 300, 100, 20);
		cSingleDoors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				singleDoorList = new ArrayList<BoundsGeom>();
				try {
					IIRMSVisualization.dispQueryResult(24);

				} catch (Exception ex) {
				}
			}
		});

		cDoubleDoors = new JComboBox(floorValue);
		cDoubleDoors.setBounds(360, 340, 100, 20);
		cDoubleDoors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				doubleDoorList = new ArrayList<BoundsGeom>();
				try {
					IIRMSVisualization.dispQueryResult(25);

				} catch (Exception ex) {
				}
			}
		});

		// Defining the wall components
		// Labels to display the query being executed.
		lInnerWalls = new JLabel("Display inner walls in floor");
		lInnerWalls.setBounds(20, 180, 300, 20);

		lWalls = new JLabel("Show me walls in floor");
		lWalls.setBounds(20, 220, 300, 20);

		lSelect = new JLabel("Select walls of");
		lSelect.setBounds(20, 260, 260, 20);

		lCommonWalls = new JLabel("Display common walls in floor");
		lCommonWalls.setBounds(20, 300, 300, 20);

		lWallsWindows = new JLabel("Display walls containing the window in room");
		lWallsWindows.setBounds(20, 340, 350, 20);

		cInnerWalls = new JComboBox(floorValue);
		cInnerWalls.setBounds(240, 180, 75, 20);

		cInnerWalls.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {

				innerWallBoundsList = new ArrayList<BoundsGeom>();
				try {
					IIRMSVisualization.dispQueryResult(11);

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		cWalls = new JComboBox(floorValue);
		cWalls.setBounds(240, 220, 75, 20);
		cWalls.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				
				try {
					IIRMSVisualization.dispQueryResult(12);

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		String materialQuery1 = "SELECT distinct wall_material from wall";
		Statement materialStmt1 = conn.createStatement();
		ResultSet rsMaterial1 = materialStmt1.executeQuery(materialQuery1);
		java.util.List<String> materialList1 = new ArrayList<String>();
		materialList1.add(" ");
		while (rsMaterial1.next()) {
		   
			materialList1.add(rsMaterial1.getString(1));
		}

		String typeQuery1 = "SELECT distinct wall_type from wall";
		Statement typeStmt1 = conn.createStatement();
		ResultSet rsType1 = typeStmt1.executeQuery(typeQuery1);
		java.util.List<String> typeList1 = new ArrayList<String>();
		typeList1.add(" ");
		while (rsType1.next()) {
			// System.out.println(rsType.getString(1));
			typeList1.add(rsType1.getString(1));
		}

		final String[] wallmaterialValue = materialList1.toArray(new String[materialList1.size()]);
//		for (String inn : wallmaterialValue) {
//	 System.out.println("waam:"+inn);
//		}
		final String[] typeValue = typeList1.toArray(new String[typeList.size()]);
		/*
		 * for (String inn : typeValue) { System.out.println("******" + inn); }
		 */
		String[] selectType = { " ", "material", "type" };
		cSelect1 = new JComboBox(selectType);
		cSelect1.setBounds(140, 260, 100, 20);
		cSelect1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if (((String) cSelect1.getSelectedItem()).equals("material")) {
					// cSelect2 = new JComboBox(materialValue);
					cMaterial.setVisible(true);
					cType.setVisible(false);
					cSelect2.setVisible(false);
					// pQuery.revalidate();
				} else if (((String) cSelect1.getSelectedItem()).equals("type")) {
					cMaterial.setVisible(false);
					cType.setVisible(true);
					cSelect2.setVisible(false);
				} else if (((String) cSelect1.getSelectedItem()).equals(" ")) {
					cMaterial.setVisible(false);
					cType.setVisible(false);
					cSelect2.setVisible(true);
				}
			}
		});

		cSelect2 = new JComboBox(sampleCombo);
		cSelect2.setBounds(250, 260, 100, 20);
		cSelect2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				selectValueFinal = (String) cSelect2.getSelectedItem();
			}
		});

		cType = new JComboBox(typeValue);
		cType.setBounds(250, 260, 100, 20);
		cType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				selectValueFinal = (String) cType.getSelectedItem();
			}
		});
		cType.setVisible(false);
		pQuery.add(cType);

		cMaterial = new JComboBox(wallmaterialValue);
		cMaterial.setBounds(250, 260, 100, 20);
		cMaterial.setVisible(false);
		cMaterial.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				selectValueFinal = (String) cMaterial.getSelectedItem();
			}
		});
		pQuery.add(cMaterial);

		cSelect3 = new JComboBox(floorValue);
		cSelect3.setBounds(360, 260, 75, 20);
		cSelect3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				if ((selectValueFinal.equals("")) || (((String) cSelect1.getSelectedItem()).equals(" "))) {
					JOptionPane.showMessageDialog(null, "Not a valid option!", "IIRMS - Error!", 2);
				} else {
					try {
						IIRMSVisualization.dispQueryResult(15);
					} catch (Exception ex) {
					}
				}
			}
		});

		cCommonWalls = new JComboBox(floorValue);
		cCommonWalls.setBounds(240, 300, 75, 20);
		cCommonWalls.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				commonWallList = new ArrayList<BoundsGeom>();
				try {
					IIRMSVisualization.dispQueryResult(13);

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		cWallsWindows = new JComboBox(roomValue);
		cWallsWindows.setBounds(350, 340, 100, 20);
		cWallsWindows.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				try {
					IIRMSVisualization.dispQueryResult(14);

				} catch (Exception ex) {
				}
			}
		});

		// Defining the room components
		// Labels to display the query being executed.
		larea = new JLabel("Display rooms by area");
		larea.setBounds(20, 180, 200, 20);

		String[] conditionValue = { " ", ">", "=", "<" };
		cAreaCondition = new JComboBox(conditionValue);
		cAreaCondition.setBounds(200, 180, 50, 20);

		tArea = new JTextField(100);
		tArea.setBounds(260, 180, 60, 20);

		lareain = new JLabel("of floor");
		lareain.setBounds(330, 180, 75, 20);

		cAreaFloor = new JComboBox(floorValue);
		cAreaFloor.setBounds(390, 180, 50, 20);
		cAreaFloor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				areaList = new ArrayList<BoundsGeom>();
				// if (!(cAreaCondition.getSelectedItem().equals(" ")) &&
				// (!tArea.getText().equals("")) &&
				// (!tArea.getText().equals(" "))) {
				try {
					IIRMSVisualization.dispQueryResult(4);

				} catch (Exception ex) {
					ex.printStackTrace();
				}
				// }
			}
		});

		pQuery.add(tArea);
		pQuery.add(cAreaCondition);
		pQuery.add(cAreaFloor);

		lvolume = new JLabel("Display rooms by volume");
		lvolume.setBounds(20, 220, 200, 20);

		cVolumeCondition = new JComboBox(conditionValue);
		cVolumeCondition.setBounds(200, 220, 50, 20);

		tVolume = new JTextField(100);
		tVolume.setBounds(260, 220, 60, 20);

		lvolumein = new JLabel("of floor");
		lvolumein.setBounds(330, 220, 75, 20);

		cVolumeFloor = new JComboBox(floorValue);
		cVolumeFloor.setBounds(390, 220, 50, 20);

		cVolumeFloor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				volumeList = new ArrayList<BoundsGeom>();
				// if (!(cVolumeCondition.getSelectedItem().equals(" ")) &&
				// (!tVolume.getText().equals("")) &&
				// (!tVolume.getText().equals(" "))) {
				try {
					IIRMSVisualization.dispQueryResult(3);

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		pQuery.add(cVolumeCondition);
		pQuery.add(tVolume);
		pQuery.add(cVolumeFloor);

		lroomtype = new JLabel("Select rooms of the type");
		lroomtype.setBounds(20, 260, 200, 20);

		cRoomType = new JComboBox(roomTypeValue);
		cRoomType.setBounds(200, 260, 120, 20);

		lroomtypein = new JLabel("of floor");
		lroomtypein.setBounds(330, 260, 100, 20);

		cRoomTypeFloor = new JComboBox(floorValue);
		cRoomTypeFloor.setBounds(390, 260, 75, 20);
		cRoomTypeFloor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				roomTypeList = new ArrayList<BoundsGeom>();
				try {
					IIRMSVisualization.dispQueryResult(5);

				} catch (Exception ex) {
				}
			}
		});

		pQuery.add(cRoomType);
		pQuery.add(cRoomTypeFloor);

		lroomin = new JLabel("Show me rooms in");
		lroomin.setBounds(20, 300, 150, 20);

		cRoomFloor = new JComboBox(floorValue);
		cRoomFloor.setBounds(200, 300, 75, 20);
		cRoomFloor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				roomFloorList = new ArrayList<BoundsGeom>();
				try {
					IIRMSVisualization.dispQueryResult(1);

				} catch (Exception ex) {
				}
			}
		});

		pQuery.add(cRoomFloor);

		lroom = new JLabel("Show me");
		lroom.setBounds(20, 340, 100, 20);

		cRoom = new JComboBox(roomValue);
		cRoom.setBounds(200, 340, 75, 20);
		cRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				roomList = new ArrayList<BoundsGeom>();
				try {
					IIRMSVisualization.dispQueryResult(2);
				} catch (Exception ex) {
				}

			}
		});

		pQuery.add(cRoom);

		navigation = new JLabel("Show the path from");
		navigation.setBounds(20, 380, 330, 20);

		source = new JComboBox(roomValue);
		source.setBounds(200, 380, 75, 20);
		 lnavi = new JLabel("to");
		 lnavi.setBounds(330, 380, 75, 20);
		destination = new JComboBox(roomValue);
		destination.setBounds(390, 380, 75, 20);
		destination.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent ae) {
	                try {
	                    //Deleting the existing path details from the database.
	                  
	                	String ipAddress=InetAddress.getLocalHost().getHostAddress();
	                    String sqlDelQuery = "delete from path ";
	                    Statement stmtDelQuery = conn.createStatement();
	                    stmtDelQuery.executeUpdate(sqlDelQuery);
	              
	                    
	                    Navigation navi=new Navigation(IIRMSQueryInterface.dbNameSelected);
	                    navi.pointsList.clear();
	                   // System.out.println(cSrc.getSelectedItem());
	                    //System.out.println(cSrc.getSelectedItem().toString());
	                    List<String> finalPath= Navigation.findPath(source.getSelectedItem().toString().toUpperCase(), destination.getSelectedItem().toString().toUpperCase());
	                    Navigation.loadPathToDB(finalPath);
	                    IIRMSVisualization.dispQueryResult(35);
	                    
	                } catch (Exception ex1) {
	                    ex1.printStackTrace();
	                }
	            }
	        });

		pQuery.add(source);
		pQuery.add(destination);
		

		ldist1 = new JLabel("Distance between");
		ldist1.setBounds(20, 420, 150, 20);

		cDist1 = new JComboBox(roomValue);
		cDist1.setBounds(200, 420, 75, 20);

		ldist2 = new JLabel("and");
		ldist2.setBounds(330, 420, 50, 20);

		cDist2 = new JComboBox(roomValue);
		cDist2.setBounds(390, 420, 75, 20);
		cDist2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				distList = new ArrayList<BoundsGeom>();
				try {
					IIRMSVisualization.dispQueryResult(36);

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		pQuery.add(cDist1);
		pQuery.add(cDist2);

		lwindowofroom = new JLabel("Number of windows present in a room");
		lwindowofroom.setBounds(20, 460, 300, 20);

		cWindowsRoom = new JComboBox(roomValue);
		cWindowsRoom.setBounds(330, 460, 75, 20);
		cWindowsRoom.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {
				roomWindowList = new ArrayList<BoundsGeom>();
				try {

					String query = "select count(w.window_id) from room r, windows w where r.room_name='" + cWindowsRoom.getSelectedItem()
							+ "'" + " and st_intersects(r.the_geom, w.the_geom)=TRUE";
					Statement stmtForQuery = conn.createStatement();
					ResultSet rsForQuery = stmtForQuery.executeQuery(query);
					String finalOutput = "";
					while (rsForQuery.next()) {
						countWindows = Integer.parseInt(rsForQuery.getString(1));
						String output = "Room Name : " + cWindowsRoom.getSelectedItem() + " \t Count : " + countWindows + "\n";

					}
					IIRMSVisualization.dispQueryResult(37);
				} catch (Exception ex) {
				}
			}
		});

		pQuery.add(cWindowsRoom);

		ladjacent = new JLabel("Adjacent rooms of");
		ladjacent.setBounds(20, 500, 200, 20);

		cAdjacent = new JComboBox(roomValue);
		cAdjacent.setBounds(330, 500, 75, 20);
		cAdjacent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				adjacentList = new ArrayList<BoundsGeom>();
				try {
					IIRMSVisualization.dispQueryResult(7);
				} catch (Exception ex) {
				}
			}
		});

		pQuery.add(cAdjacent);

		lroom250 = new JLabel("Display rooms within");
		lroom250.setBounds(20, 540, 200, 20);

		tdistvalue= new JTextField(); 
		tdistvalue.setBounds(150, 540, 60, 20);
		
		lroomtd= new JLabel("meters from");
		lroomtd.setBounds(230, 540, 200, 20);
		
		cRoom250 = new JComboBox(roomValue);
		cRoom250.setBounds(330, 540, 75, 20);
		cRoom250.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				room250List = new ArrayList<BoundsGeom>();
				try {
					IIRMSVisualization.dispQueryResult(6);

				} catch (Exception ex) {
				}
			}
		});

		pQuery.add(cRoom250);

		lkrooms = new JLabel("Display the ");
		lkrooms.setBounds(20, 580, 100, 20);

		String[] kValue = { " ", "3", "4", "5" };
		cK = new JComboBox(kValue);
		cK.setBounds(110, 580, 50, 20);

		pQuery.add(cK);

		lkrooms2 = new JLabel("nearest rooms of");
		lkrooms2.setBounds(170, 580, 200, 20);

		cKRooms = new JComboBox(roomValue);
		cKRooms.setBounds(330, 580, 75, 20);
		cKRooms.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				kRoomsList = new ArrayList<BoundsGeom>();
				try {
					IIRMSVisualization.dispQueryResult(8);
				} catch (Exception ex) {
				}
			}
		});

		pQuery.add(cKRooms);

		lexit = new JLabel("Display exit for room");
		lexit.setBounds(20, 620, 200, 20);

		cExitRoom = new JComboBox(roomValue);
		cExitRoom.setBounds(330, 620, 75, 20);
		cExitRoom.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				exitRoomList = new ArrayList<BoundsGeom>();
				try {
					IIRMSVisualization.dispQueryResult(9);

				} catch (Exception ex) {
				}
			}
		});

		pQuery.add(cExitRoom);

		lkexits = new JLabel("Find the ");
		lkexits.setBounds(20, 660, 90, 20);

		cKE = new JComboBox(kValue);
		cKE.setBounds(90, 660, 50, 20);

		lkexits2 = new JLabel("exits nearest to the vicinity of");
		lkexits2.setBounds(150, 660, 250, 20);

		cKExits = new JComboBox(roomValue);
		cKExits.setBounds(380, 660, 50, 20);
		cKExits.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				kExitsList = new ArrayList<BoundsGeom>();
				try {
					IIRMSVisualization.dispQueryResult(10);

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		// defining the steps components
		lStair = new JLabel("Display the stairs in the floor");
		lStair.setBounds(20, 180, 300, 20);

		cStair = new JComboBox(floorValue);
		cStair.setBounds(360, 180, 100, 20);
		cStair.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				stairList = new ArrayList<BoundsGeom>();
				try {
					IIRMSVisualization.dispQueryResult(38);

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		pQuery.add(cKE);
		pQuery.add(cKExits);

		// Adding entities to the panel
		// Adding the wall components
		pQuery.add(cWallsWindows);
		pQuery.add(cCommonWalls);
		pQuery.add(cSelect3);
		pQuery.add(cSelect2);
		pQuery.add(cSelect1);
		pQuery.add(cWalls);
		pQuery.add(cInnerWalls);
		pQuery.add(lWallsWindows);
		pQuery.add(lCommonWalls);
		pQuery.add(lSelect);
		pQuery.add(lWalls);
		pQuery.add(lInnerWalls);

		// Adding the room components
		pQuery.add(lkexits2);
		pQuery.add(lkrooms2);
		pQuery.add(ldist2);
		pQuery.add(lnavi);
		pQuery.add(lkexits);
		pQuery.add(lexit);
		pQuery.add(lkrooms);
		pQuery.add(lroom250);
		pQuery.add(ladjacent);
		pQuery.add(lwindowofroom);
		pQuery.add(ldist1);
		pQuery.add(navigation);
		pQuery.add(lroom);
		pQuery.add(lroomin);
		pQuery.add(lroomtype);
		pQuery.add(lroomtypein);
		pQuery.add(lvolume);
		pQuery.add(lvolumein);
		pQuery.add(larea);
		pQuery.add(lareain);
		pQuery.add(tdistvalue);
		pQuery.add(lroomtd);
		

		// Adding door components
		pQuery.add(cDoubleDoors);
		pQuery.add(cSingleDoors);
		pQuery.add(cMaterial2);
		pQuery.add(cMaterial1);
		pQuery.add(cOutward);
		pQuery.add(cInward);
		pQuery.add(lSingleDoors);
		pQuery.add(lDoubleDoors);
		pQuery.add(lMaterial2);
		pQuery.add(lMaterial1);
		pQuery.add(lOutward);
		pQuery.add(lInward);

		// Adding window components
		
		
		pQuery.add(cWooden);
		pQuery.add(Jcb_materialwindows);
		pQuery.add(lWooden);
		pQuery.add(lwooden1);
		pQuery.add(lwindowtype);
		pQuery.add(lwindowtype1);
		pQuery.add(Cwtype);
		pQuery.add(Jcb_typewindows);
		

		// Adding stair components
		pQuery.add(lStair);
		pQuery.add(cStair);

		// Adding the type components
		pQuery.add(bWindow);
		pQuery.add(iWindow);
		pQuery.add(bDoor);
		pQuery.add(iDoor);
		pQuery.add(bWall);
		pQuery.add(bStair);
		pQuery.add(iStair);
		pQuery.add(bRoom);
		pQuery.add(iRoom);
		pQuery.add(iWall);
		pQuery.add(bReset);
		pQuery.add(bCancel);

		// Adding the background finally
		pQuery.add(iMain);

		// Initially setting everything to false. Later visible
		// based on the button clicked.
		setRoomVisible(false);
		setWallVisible(false);
		setWindowVisible(false);
		setDoorVisible(false);
		setStairVisible(false);

		// Adding panel to the frame
		fQuery.add(pQuery);

		// Setting properties to the frame
		fQuery.setVisible(true);
		// fQuery.setExtendedState(JFrame.MAXIMIZED_VERT);
		fQuery.setMinimumSize(new Dimension(600, 780));
		fQuery.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public static void resetValues() {
		// Visibility of room combo boxes
		/*
		 * cAreaCondition.setSelectedIndex(0); cAreaFloor.setSelectedIndex(0);
		 * tArea.setText(""); cVolumeCondition.setSelectedIndex(0);
		 * cVolumeFloor.setSelectedIndex(0); tVolume.setText("");
		 * cRoomType.setSelectedIndex(0); cRoomTypeFloor.setSelectedIndex(0);
		 * cRoomFloor.setSelectedIndex(0); cRoom.setSelectedIndex(0);
		 * cSeating.setSelectedIndex(0); cDist1.setSelectedIndex(0);
		 * cDist2.setSelectedIndex(0); cWindowsRoom.setSelectedIndex(0);
		 * cAdjacent.setSelectedIndex(0); cRoom250.setSelectedIndex(0);
		 * cK.setSelectedIndex(0); cKRooms.setSelectedIndex(0);
		 * cExitRoom.setSelectedIndex(0); cKE.setSelectedIndex(0);
		 * cKExits.setSelectedIndex(0); cMaterial.setSelectedIndex(0);
		 * cType.setSelectedIndex(0);
		 */

		// Visibility of wall combo boxes
		cInnerWalls.setSelectedIndex(0);
		cWalls.setSelectedIndex(0);
		cSelect1.setSelectedIndex(0);
		cSelect2.setSelectedIndex(0);
		cSelect3.setSelectedIndex(0);
		cCommonWalls.setSelectedIndex(0);
		cWallsWindows.setSelectedIndex(0);
		cMaterial.setSelectedIndex(0);
		cType.setSelectedIndex(0);

	}

	/*
	 * Method name : setRoomVisible 
	 * Method description : Method to set the visibility of "Room" querying components 
	 * Method Arguments : boolean(value) 
	 * Arguments description : "value" --> true (is visible), false (is not visible) 
	 * Return type : void Return type description : Returns nothing.
	 */
	public static void setRoomVisible(boolean value) {
		// Visibility of the labels
		larea.setVisible(value);
		lareain.setVisible(value);
		lvolume.setVisible(value);
		lvolumein.setVisible(value);
		lroomtype.setVisible(value);
		lroomtypein.setVisible(value);
		lroomin.setVisible(value);
		lroom.setVisible(value);
		navigation.setVisible(value);
		ldist1.setVisible(value);
		ldist2.setVisible(value);
		lnavi.setVisible(value);
		lwindowofroom.setVisible(value);
		ladjacent.setVisible(value);
		lroom250.setVisible(value);
		lkrooms.setVisible(value);
		lkrooms2.setVisible(value);
		lexit.setVisible(value);
		lkexits.setVisible(value);
		lkexits2.setVisible(value);
	    tdistvalue.setVisible(value);
	    lroomtd.setVisible(value);

		// Visibility of combo boxes
		cAreaCondition.setVisible(value);
		cAreaFloor.setVisible(value);
		tArea.setVisible(value);
		cVolumeCondition.setVisible(value);
		cVolumeFloor.setVisible(value);
		tVolume.setVisible(value);
		cRoomType.setVisible(value);
		cRoomTypeFloor.setVisible(value);
		cRoomFloor.setVisible(value);
		cRoom.setVisible(value);
		source.setVisible(value);
		destination.setVisible(value);
		cDist1.setVisible(value);
		cDist2.setVisible(value);
		cWindowsRoom.setVisible(value);
		cAdjacent.setVisible(value);
		cRoom250.setVisible(value);
		cK.setVisible(value);
		cKRooms.setVisible(value);
		cExitRoom.setVisible(value);
		cKE.setVisible(value);
		cKExits.setVisible(value);
		cMaterial.setVisible(false);
		cType.setVisible(false);

		pQuery.revalidate();
		pQuery.repaint();
	}

	/*
	 * Method name : setWallVisible 
	 * Method description : Method to set the visibility of "Wall" querying components 
	 * Method Arguments : boolean(value) 
	 * Arguments description : "value" --> true (is visible), false (is not visible) 
	 * Return type : void Return type description : Returns nothing.
	 */
	public static void setWallVisible(boolean value) {
		// Visibility of labels
		lInnerWalls.setVisible(value);
		lWalls.setVisible(value);
		lSelect.setVisible(value);
		lCommonWalls.setVisible(value);
		lWallsWindows.setVisible(value);

		// Visibility of combo boxes
		cInnerWalls.setVisible(value);
		cWalls.setVisible(value);
		cSelect1.setVisible(value);
		cSelect2.setVisible(value);
		cSelect3.setVisible(value);
		cCommonWalls.setVisible(value);
		cWallsWindows.setVisible(value);
		cMaterial.setVisible(false);
		cType.setVisible(false);

		pQuery.revalidate();
		pQuery.repaint();
	}

	/*
	 * Method name : setWindowVisible 
	 * Method description : Method to set the visibility of "Window" querying components 
	 * Method Arguments : boolean(value) 
	 * Arguments description : "value" --> true (is visible), false (is not visible) 
	 * Return type : void Return type description : Returns nothing.
	 */
	public static void setWindowVisible(boolean value) {
		// Visibility of labels
		// lSliding.setVisible(value);
		lWooden.setVisible(value);
		lwooden1.setVisible(value);
		lwindowtype.setVisible(value);
		lwindowtype1.setVisible(value);
		Cwtype.setVisible(value);
		Jcb_typewindows.setVisible(value);
		
		cWooden.setVisible(value);
		
		
		cMaterial.setVisible(false);
		cType.setVisible(false);
		Jcb_materialwindows.setVisible(value);
       
		// Visibility of combo boxes
		pQuery.revalidate();
		pQuery.repaint();
	}

	/*
	 * Method name : setDoorVisible 
	 * Method description : Method to set the visibility of "Door" querying components 
	 * Method Arguments : boolean(value) 
	 * Arguments description : "value" --> true (is visible), false (is not visible) 
	 * Return type : void 
	 * Return type description : Returns nothing.
	 */
	public static void setDoorVisible(boolean value) {
		// Visibility of labels
		lInward.setVisible(value);
		lOutward.setVisible(value);
		lMaterial1.setVisible(value);
		lMaterial2.setVisible(value);
		lSingleDoors.setVisible(value);
		lDoubleDoors.setVisible(value);

		// Visibility of combo boxes

		cInward.setVisible(value);
		cOutward.setVisible(value);
		cMaterial1.setVisible(value);
		cMaterial2.setVisible(value);
		cSingleDoors.setVisible(value);
		cDoubleDoors.setVisible(value);
		cMaterial.setVisible(false);
		cType.setVisible(false);

		pQuery.revalidate();
		pQuery.repaint();
	}

	/*
	 * Method name : setStairVisible 
	 * Method description : Method to set the visibility of "Stair" querying components 
	 * Method Arguments : boolean(value) 
	 * Arguments description : "value" --> true (is visible), false (is not visible) 
	 * Return type : void Return type description : Returns nothing.
	 */
	public static void setStairVisible(boolean value) {
		// Visibility of labels
		lStair.setVisible(value);

		// Visibility of combo boxes

		cStair.setVisible(value);

		pQuery.revalidate();
		pQuery.repaint();
	}

	/*
	 * Method name : DBConnectionQuery 
	 * Method description : Method to load the driver for PostgreSQL database and set the connection to the db. 
	 * Method Arguments : null 
	 * Arguments description : -- 
	 * Return type : void Return
	 * type description : Returns nothing.
	 */
	public static void DBConnectionQuery() throws Exception {
		Class.forName("org.postgresql.Driver");
		// Query UI has to be displayed only for the building that is selected
		// by the user.
		// This is identified by the variable below.
		//System.out.println("Name is : " + IIRMSApplicationInterface.dbNameSelected);
		String dbName = IIRMSQueryInterface.dbNameSelected;
		// Establish the connection with the required building (database)
		conn = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/" + IIRMSQueryInterface.dbNameSelected, "researcher",
				"researcher");
	}
	
	/*
	 * Method name : dispRooms 
	 * Method description : Method to display the selected room name  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */


	public static void dispRooms(GL2 gl) throws Exception {

		String query1 = "select st_astext(the_geom),floor_id from room " + "where room_name = '" + (String) cRoom.getSelectedItem() + "'";
		Statement stmt1 = conn.createStatement();
		ResultSet rs1 = stmt1.executeQuery(query1);
		String[] r_pointIn = null;
		List<Float> r_pointList;

		while (rs1.next()) {
			r_pointIn = rs1.getString(1).replace("MULTIPOLYGON", "").replace("POLYGON", "").replace(",", " ").replace("(", "")
					.replace(")", "").split(" ");
			String r_floor = rs1.getString(2);
			int r_floorNow = Integer.parseInt(r_floor);
			r_pointList = new ArrayList<Float>();
			for (int i = 0; i < r_pointIn.length; i = i + 2) {
				float xPoint = Float.parseFloat(r_pointIn[i]);
				float yPoint = Float.parseFloat(r_pointIn[i + 1]);
				r_pointList.add(xPoint);
				r_pointList.add(yPoint);
			}
					
			
			 gl.glEnable(GL2.GL_TEXTURE_2D);
             gl.glBindTexture(GL2.GL_TEXTURE_2D, com.amrita.IIRMS.Visualization.IIRMSVisualization.wallTexture);

             gl.glBegin(GL2.GL_QUADS);
             gl.glColor3f(1f, 1f, 1f);
             for (int j = 0; j < r_pointList.size()-2; j = j + 2) {
             	gl.glTexCoord2f(0f,0f);
             	gl.glVertex3f(r_pointList.get(j) / transValue, (r_floorNow * 0.04f) , r_pointList.get(j + 1) / transValue);
             	gl.glTexCoord2f(1f,0f);
             	gl.glVertex3f(r_pointList.get(j+2) / transValue, (r_floorNow * 0.04f), r_pointList.get(j + 3) / transValue);
             	gl.glTexCoord2f(1f, 1f);
             	gl.glVertex3f(r_pointList.get(j+2) / transValue, (r_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 3) / transValue);
             	gl.glTexCoord2f(0f, 1f);
             	gl.glVertex3f(r_pointList.get(j) / transValue, (r_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 1) / transValue);                    
             }
             gl.glEnd();
             gl.glDisable(GL2.GL_TEXTURE_2D);
         	gl.glFlush();
		}
		String text = "SELECTED ROOM : " + (String) cRoom.getSelectedItem();
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 200, 650);
		txtRenderer.endRendering();

	}
	
	/*
	 * Method name : dispRoomsOfVolume 
	 * Method description : Method to display the rooms of the specified volume  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispRoomsOfVolume(GL2 gl) throws Exception {

		String query1 = "select st_astext(the_geom),room_name from room " + "where volume " + cVolumeCondition.getSelectedItem() + ""
				+ (String) tVolume.getText() + " and floor_id = '" + (String) cVolumeFloor.getSelectedItem() + "'";
		Statement stmt1 = conn.createStatement();
		ResultSet rs1 = stmt1.executeQuery(query1);
		String[] r_pointIn = null;
		ArrayList<String> roomVolList = new ArrayList<String>();
		List<Float> r_pointList;
		String r_floor = (String) cVolumeFloor.getSelectedItem();
		int r_floorNow = Integer.parseInt(r_floor);
		if(rs1==null)
		{
			JOptionPane.showMessageDialog(null, "No Record Found for Query", "IIRMS - Warning", 2);
		}
		while (rs1.next()) {
			r_pointIn = rs1.getString(1).replace("MULTIPOLYGON", "").replace("POLYGON", "").replace(",", " ").replace("(", "")
					.replace(")", "").split(" ");
			roomVolList.add(rs1.getString(2));
			r_pointList = new ArrayList<Float>();
			for (int i = 0; i < r_pointIn.length; i = i + 2) {
				float xPoint = Float.parseFloat(r_pointIn[i]);
				float yPoint = Float.parseFloat(r_pointIn[i + 1]);
				r_pointList.add(xPoint);
				r_pointList.add(yPoint);
			}
			 gl.glEnable(GL2.GL_TEXTURE_2D);
             gl.glBindTexture(GL2.GL_TEXTURE_2D, com.amrita.IIRMS.Visualization.IIRMSVisualization.wallTexture);

             gl.glBegin(GL2.GL_QUADS);
             gl.glColor3f(1f, 1f, 1f);
             for (int j = 0; j < r_pointList.size()-2; j = j + 2) {
             	gl.glTexCoord2f(0f,0f);
             	gl.glVertex3f(r_pointList.get(j) / transValue, (r_floorNow * 0.04f) , r_pointList.get(j + 1) / transValue);
             	gl.glTexCoord2f(1f,0f);
             	gl.glVertex3f(r_pointList.get(j+2) / transValue, (r_floorNow * 0.04f), r_pointList.get(j + 3) / transValue);
             	gl.glTexCoord2f(1f, 1f);
             	gl.glVertex3f(r_pointList.get(j+2) / transValue, (r_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 3) / transValue);
             	gl.glTexCoord2f(0f, 1f);
             	gl.glVertex3f(r_pointList.get(j) / transValue, (r_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 1) / transValue);                    
             }
             gl.glEnd();
             gl.glDisable(GL2.GL_TEXTURE_2D);
         	gl.glFlush();
		}
		String text = "ROOMS WITH VOLUME " + cVolumeCondition.getSelectedItem() + " " + tVolume.getText() + " : " + roomVolList;
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();

	}
	
	/*
	 * Method name : dispRoomsOfArea
	 * Method description : Method to display the rooms of the specified area   
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispRoomsOfArea(GL2 gl) throws Exception {

		String query1 = "select st_astext(the_geom), room_name from room " + "where area " + cAreaCondition.getSelectedItem() + "'"
				+ (String) tArea.getText() + "' and floor_id = '" + (String) cAreaFloor.getSelectedItem() + "'";
		Statement stmt1 = conn.createStatement();
		ResultSet rs1 = stmt1.executeQuery(query1);
		String[] r_pointIn = null;
		ArrayList<String> roomAreaList = new ArrayList<String>();
		List<Float> r_pointList;
		String r_floor = (String) cAreaFloor.getSelectedItem();
		int r_floorNow = Integer.parseInt(r_floor);
		if(rs1==null)
		{
			JOptionPane.showMessageDialog(null, "No Record Found for Query", "IIRMS - Warning", 2);
		}
		
		while (rs1.next()) {
			r_pointIn = rs1.getString(1).replace("MULTIPOLYGON", "").replace("POLYGON", "").replace(",", " ").replace("(", "")
					.replace(")", "").split(" ");
			roomAreaList.add(rs1.getString(2));
			r_pointList = new ArrayList<Float>();
			for (int i = 0; i < r_pointIn.length; i = i + 2) {
				float xPoint = Float.parseFloat(r_pointIn[i]);
				float yPoint = Float.parseFloat(r_pointIn[i + 1]);
				r_pointList.add(xPoint);
				r_pointList.add(yPoint);
			}
			 gl.glEnable(GL2.GL_TEXTURE_2D);
             gl.glBindTexture(GL2.GL_TEXTURE_2D, com.amrita.IIRMS.Visualization.IIRMSVisualization.wallTexture);

             gl.glBegin(GL2.GL_QUADS);
             gl.glColor3f(1f, 1f, 1f);
             for (int j = 0; j < r_pointList.size()-2; j = j + 2) {
             	gl.glTexCoord2f(0f,0f);
             	gl.glVertex3f(r_pointList.get(j) / transValue, (r_floorNow * 0.04f) , r_pointList.get(j + 1) / transValue);
             	gl.glTexCoord2f(1f,0f);
             	gl.glVertex3f(r_pointList.get(j+2) / transValue, (r_floorNow * 0.04f), r_pointList.get(j + 3) / transValue);
             	gl.glTexCoord2f(1f, 1f);
             	gl.glVertex3f(r_pointList.get(j+2) / transValue, (r_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 3) / transValue);
             	gl.glTexCoord2f(0f, 1f);
             	gl.glVertex3f(r_pointList.get(j) / transValue, (r_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 1) / transValue);                    
             }
             gl.glEnd();
             gl.glDisable(GL2.GL_TEXTURE_2D);
         	gl.glFlush();
		}
		String text = "ROOMS WITH AREA " + cAreaCondition.getSelectedItem() + " " + tArea.getText() + " : " + roomAreaList;
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();

	}
	
	/*
	 * Method name : dispRoomsOfType
	 * Method description : Method to display the rooms of the specified type  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispRoomsOfType(GL2 gl) throws Exception {

		String query1 = "select st_astext(the_geom), room_name from room " + "where room_type ='" + (String) cRoomType.getSelectedItem()
				+ "' and floor_id = '" + (String) cRoomTypeFloor.getSelectedItem() + "'";
		Statement stmt1 = conn.createStatement();
		ResultSet rs1 = stmt1.executeQuery(query1);
		String[] r_pointIn = null;
		ArrayList<String> roomTypeList = new ArrayList<String>();
		List<Float> r_pointList;
		String r_floor = (String) cRoomTypeFloor.getSelectedItem();
		int r_floorNow = Integer.parseInt(r_floor);
		if(rs1==null)
		{
			JOptionPane.showMessageDialog(null, "No Record Found for Query", "IIRMS - Warning", 2);
		}
		while (rs1.next()) {
			r_pointIn = rs1.getString(1).replace("MULTIPOLYGON", "").replace("POLYGON", "").replace(",", " ").replace("(", "")
					.replace(")", "").split(" ");
			roomTypeList.add(rs1.getString(2));
			r_pointList = new ArrayList<Float>();
			for (int i = 0; i < r_pointIn.length; i = i + 2) {
				float xPoint = Float.parseFloat(r_pointIn[i]);
				float yPoint = Float.parseFloat(r_pointIn[i + 1]);
				r_pointList.add(xPoint);
				r_pointList.add(yPoint);
			}
			 gl.glEnable(GL2.GL_TEXTURE_2D);
             gl.glBindTexture(GL2.GL_TEXTURE_2D, com.amrita.IIRMS.Visualization.IIRMSVisualization.wallTexture);

             gl.glBegin(GL2.GL_QUADS);
             gl.glColor3f(1f, 1f, 1f);
             for (int j = 0; j < r_pointList.size()-2; j = j + 2) {
             	gl.glTexCoord2f(0f,0f);
             	gl.glVertex3f(r_pointList.get(j) / transValue, (r_floorNow * 0.04f) , r_pointList.get(j + 1) / transValue);
             	gl.glTexCoord2f(1f,0f);
             	gl.glVertex3f(r_pointList.get(j+2) / transValue, (r_floorNow * 0.04f), r_pointList.get(j + 3) / transValue);
             	gl.glTexCoord2f(1f, 1f);
             	gl.glVertex3f(r_pointList.get(j+2) / transValue, (r_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 3) / transValue);
             	gl.glTexCoord2f(0f, 1f);
             	gl.glVertex3f(r_pointList.get(j) / transValue, (r_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 1) / transValue);                    
             }
             gl.glEnd();
             gl.glDisable(GL2.GL_TEXTURE_2D);
         	gl.glFlush();
		}
		String text = "ROOMS OF TYPE '" + cRoomType.getSelectedItem().toString().toUpperCase().trim() + "' : " + roomTypeList;
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();
	}
	
	/*
	 * Method name : dispRoomsOfFloor 
	 * Method description : Method to display the rooms of a particular floor  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispRoomsOfFloor(GL2 gl) throws Exception {

		String r_floor = (String) cRoomFloor.getSelectedItem();
		int r_floorNow = Integer.parseInt(r_floor);
		String query3 = "select st_astext(the_geom),room_type,room_name from room where floor_id=" + r_floorNow;
		Statement stmt3 = conn.createStatement();
		ResultSet rs3 = stmt3.executeQuery(query3);
		String[] r_pointIn = null;
		ArrayList<String> roomFloorList = new ArrayList<String>();
		List<Float> r_pointList;
		if(rs3==null)
		{
			JOptionPane.showMessageDialog(null, "No Record Found for Query", "IIRMS - Warning", 2);
		}
		while (rs3.next()) {
			r_pointIn = rs3.getString(1).replace("MULTIPOLYGON", "").replace("POLYGON", "").replace(",", " ").replace("(", "")
					.replace(")", "").split(" ");
			r_pointList = new ArrayList<Float>();
			String roomType = rs3.getString(2);
			roomFloorList.add(rs3.getString(3));
			if (!roomType.contains("Corridor")) {
				for (int i = 0; i < r_pointIn.length; i = i + 2) {
					float xPoint = Float.parseFloat(r_pointIn[i]);
					float yPoint = Float.parseFloat(r_pointIn[i + 1]);
					r_pointList.add(xPoint);
					r_pointList.add(yPoint);
				}
				 gl.glEnable(GL2.GL_TEXTURE_2D);
	             gl.glBindTexture(GL2.GL_TEXTURE_2D, com.amrita.IIRMS.Visualization.IIRMSVisualization.wallTexture);

	             gl.glBegin(GL2.GL_QUADS);
	             gl.glColor3f(1f, 1f, 1f);
	             for (int j = 0; j < r_pointList.size()-2; j = j + 2) {
	             	gl.glTexCoord2f(0f,0f);
	             	gl.glVertex3f(r_pointList.get(j) / transValue, (r_floorNow * 0.04f) , r_pointList.get(j + 1) / transValue);
	             	gl.glTexCoord2f(1f,0f);
	             	gl.glVertex3f(r_pointList.get(j+2) / transValue, (r_floorNow * 0.04f), r_pointList.get(j + 3) / transValue);
	             	gl.glTexCoord2f(1f, 1f);
	             	gl.glVertex3f(r_pointList.get(j+2) / transValue, (r_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 3) / transValue);
	             	gl.glTexCoord2f(0f, 1f);
	             	gl.glVertex3f(r_pointList.get(j) / transValue, (r_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 1) / transValue);                    
	             }
	             gl.glEnd();
	             gl.glDisable(GL2.GL_TEXTURE_2D);
	         	gl.glFlush();
			}
		}
		String text = "ROOMS OF FLOOR" + r_floor + " : " + roomFloorList;
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();
	}
	
	/*
	 * Method name : dispRoomsByDistance 
	 * Method description : Method to display the rooms within 250 units from the selected room  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispRoomsByDistance(GL2 gl) throws Exception {
        
		String query4 = "select st_astext(r1.the_geom),r1.floor_id,r1.room_type ,r2.floor_id,r1.room_name,st_distance(r1.the_geom, r2.the_geom) as dist "
				+ "from room r1, room r2 "
				+ "where st_distance(r1.the_geom, r2.the_geom) <='" +tdistvalue.getText()+"'and r2.room_name='"
				+ cRoom250.getSelectedItem() + "' order by dist asc";
		Statement stmt4 = conn.createStatement();
		ResultSet rs4 = stmt4.executeQuery(query4);
		String[] r_pointIn = null;
		ArrayList<String> roomDistList = new ArrayList<String>();
		List<Float> r_pointList;
		String selectedroom = (String) cRoom250.getSelectedItem();
		if(rs4==null)
		{
			JOptionPane.showMessageDialog(null, "No Record Found for Query", "IIRMS - Warning", 2);
		}
		while (rs4.next()) {
			r_pointIn = rs4.getString(1).replace("MULTIPOLYGON", "").replace("POLYGON", "").replace(",", " ").replace("(", "")
					.replace(")", "").split(" ");
			String r_floor = rs4.getString(2);
			int r_floorNow = Integer.parseInt(r_floor);
			int r_floorNow1 = Integer.parseInt(rs4.getString(4));
			r_pointList = new ArrayList<Float>();
			String roomType = rs4.getString(3);
			String room = rs4.getString(5);
			
			if (!roomType.contains("Corridor") && r_floorNow == r_floorNow1) {
				for (int i = 0; i < r_pointIn.length; i = i + 2) {
					float xPoint = Float.parseFloat(r_pointIn[i]);
					float yPoint = Float.parseFloat(r_pointIn[i + 1]);
					r_pointList.add(xPoint);
					r_pointList.add(yPoint);
				}
				gl.glEnable(GL.GL_BLEND);
				gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
				if (room.equals(selectedroom)) {
					gl.glColor4f(1f, 0f, 0f, 0.9f);

				} else {
					roomDistList.add(room);
					gl.glColor4f(0f, 1f, 0f, 0.9f);
				}
				 gl.glEnable(GL2.GL_TEXTURE_2D);
	             gl.glBindTexture(GL2.GL_TEXTURE_2D, com.amrita.IIRMS.Visualization.IIRMSVisualization.wallTexture);

	             gl.glBegin(GL2.GL_QUADS);
	            
	             for (int j = 0; j < r_pointList.size()-2; j = j + 2) {
	             	gl.glTexCoord2f(0f,0f);
	             	gl.glVertex3f(r_pointList.get(j) / transValue, (r_floorNow * 0.04f) , r_pointList.get(j + 1) / transValue);
	             	gl.glTexCoord2f(1f,0f);
	             	gl.glVertex3f(r_pointList.get(j+2) / transValue, (r_floorNow * 0.04f), r_pointList.get(j + 3) / transValue);
	             	gl.glTexCoord2f(1f, 1f);
	             	gl.glVertex3f(r_pointList.get(j+2) / transValue, (r_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 3) / transValue);
	             	gl.glTexCoord2f(0f, 1f);
	             	gl.glVertex3f(r_pointList.get(j) / transValue, (r_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 1) / transValue);                    
	             }
	             gl.glEnd();
	             gl.glDisable(GL2.GL_TEXTURE_2D);
	         	gl.glFlush();

				

			}
		}
		String text = "ROOMS WITHIN "+tdistvalue.getText()+"METERS FROM "+selectedroom+" : " + roomDistList;
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();
	}
	
	/*
	 * Method name : dispNearestRooms 
	 * Method description : Method to display the nearest rooms of the selected room  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispNearestRooms(GL2 gl) throws Exception {

		String query5 = "select st_astext(r1.the_geom),r1.floor_id,r1.room_type ,r2.floor_id,r1.room_name,st_distance(r1.the_geom, r2.the_geom) as dist "
				+ "from room r1, room r2 "
				+ "where st_distance(r1.the_geom, r2.the_geom) <=1000.0 "
				+ "and r1.room_type  NOT LIKE '%Corridor%'" + "and r2.room_name='" + cKRooms.getSelectedItem() + "'order by dist ASC";
		Statement stmt5 = conn.createStatement();
		ResultSet rs5 = stmt5.executeQuery(query5);
		String[] r_pointIn = null;
		ArrayList<String> roomNearestList = new ArrayList<String>();
		List<Float> r_pointList;
		String selectedroom = (String) cKRooms.getSelectedItem();
		int num = Integer.parseInt((String) cK.getSelectedItem());
		int n = 0;
		if(rs5==null)
		{
			JOptionPane.showMessageDialog(null, "No Record Found for Query", "IIRMS - Warning", 2);
		}
		while (rs5.next()) {
			r_pointIn = rs5.getString(1).replace("MULTIPOLYGON", "").replace("POLYGON", "").replace(",", " ").replace("(", "")
					.replace(")", "").split(" ");
			String r_floor = rs5.getString(2);
			int r_floorNow = Integer.parseInt(r_floor);
			int r_floorNow1 = Integer.parseInt(rs5.getString(4));
			r_pointList = new ArrayList<Float>();
			String roomType = rs5.getString(3);
			String room = rs5.getString(5);
			System.out.println(room);
			if (!roomType.contains("Corridor") && n <= num && r_floorNow == r_floorNow1) {
				for (int i = 0; i < r_pointIn.length; i = i + 2) {
					float xPoint = Float.parseFloat(r_pointIn[i]);
					float yPoint = Float.parseFloat(r_pointIn[i + 1]);
					r_pointList.add(xPoint);
					r_pointList.add(yPoint);
				}
				gl.glEnable(GL.GL_BLEND);
				gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
				if (room.equals(selectedroom)) {
					gl.glColor4f(1f, 0f, 0f, 0.9f);
				}

				else {
					roomNearestList.add(room);
					gl.glColor4f(0f, 1f, 0f, 0.9f);
				}

				 gl.glEnable(GL2.GL_TEXTURE_2D);
	             gl.glBindTexture(GL2.GL_TEXTURE_2D, com.amrita.IIRMS.Visualization.IIRMSVisualization.wallTexture);

	             gl.glBegin(GL2.GL_QUADS);
	             //gl.glColor3f(1f, 1f, 1f);
	             for (int j = 0; j < r_pointList.size()-2; j = j + 2) {
	             	gl.glTexCoord2f(0f,0f);
	             	gl.glVertex3f(r_pointList.get(j) / transValue, (r_floorNow * 0.04f) , r_pointList.get(j + 1) / transValue);
	             	gl.glTexCoord2f(1f,0f);
	             	gl.glVertex3f(r_pointList.get(j+2) / transValue, (r_floorNow * 0.04f), r_pointList.get(j + 3) / transValue);
	             	gl.glTexCoord2f(1f, 1f);
	             	gl.glVertex3f(r_pointList.get(j+2) / transValue, (r_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 3) / transValue);
	             	gl.glTexCoord2f(0f, 1f);
	             	gl.glVertex3f(r_pointList.get(j) / transValue, (r_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 1) / transValue);                    
	             }
	             gl.glEnd();
	             gl.glDisable(GL2.GL_TEXTURE_2D);
	         	gl.glFlush();
				n++;
			}

		}
		String text = "THE " + num + " NEAREST ROOMS OF " + (String) cK.getSelectedItem() + " : " + roomNearestList;
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();
	}
	
	/*
	 * Method name : dispAdjacentRooms 
	 * Method description : Method to display the adjacent rooms of the selected room  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispAdjacentRooms(GL2 gl) throws Exception {
		String adjroomof = (String) cAdjacent.getSelectedItem();
		ArrayList<Integer> roomAdj = new ArrayList<Integer>();
		ArrayList<String> roomAdjList = new ArrayList<String>();
		String query1 = "select r2.room_id from room as r1,room as r2 where r1.room_name='" + cAdjacent.getSelectedItem() + "'"
				+ " and r1.floor_id=r2.floor_id and st_dwithin(r1.the_geom,r2.the_geom,10)";
		Statement stmt1 = conn.createStatement();
		ResultSet rs1 = stmt1.executeQuery(query1);
		while (rs1.next()) {
			int roomId = rs1.getInt(1);
			roomAdj.add(roomId);
			// System.out.println("RoomID:" + roomId);
		}
		String query2 = "select r2.room_id from room as r1,room as r2 where r1.room_name='" + cAdjacent.getSelectedItem() + "'"
				+ " and (r2.floor_id=r1.floor_id+1 or r2.floor_id=r1.floor_id-1) and st_intersects(r1.the_geom,r2.the_geom)=TRUE";
		Statement stmt2 = conn.createStatement();
		ResultSet rs2 = stmt2.executeQuery(query2);
		while (rs2.next()) {
			int roomId = rs2.getInt(1);
			roomAdj.add(roomId);
			// System.out.println("RoomID:" + roomId);
		}
		for (int r_id = 0; r_id < roomAdj.size(); r_id = r_id + 1) {
			String queryAdj = "select st_astext(the_geom),floor_id,room_type,room_name from room where room_id=" + roomAdj.get(r_id);
			Statement stmtAdj = conn.createStatement();
			ResultSet rsAdj = stmtAdj.executeQuery(queryAdj);
			while (rsAdj.next()) {
				String[] r_pointIn = rsAdj.getString(1).replace("MULTIPOLYGON", "").replace("POLYGON", "").replace(",", " ")
						.replace("(", "").replace(")", "").split(" ");
				ArrayList<Float> r_pointList = new ArrayList<Float>();
				String r_floor = rsAdj.getString(2);
				int r_floorNow = Integer.parseInt(r_floor);
				String roomType = rsAdj.getString(3);
				if (!roomType.contains("Corridor")) {
					for (int i = 0; i < r_pointIn.length; i = i + 2) {
						float xPoint = Float.parseFloat(r_pointIn[i]);
						float yPoint = Float.parseFloat(r_pointIn[i + 1]);
						r_pointList.add(xPoint);
						r_pointList.add(yPoint);
					}
					gl.glEnable(GL.GL_BLEND);
					gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
					String selectedroom = rsAdj.getString(4);
					//System.out.println(selectedroom);
					if (selectedroom.equals(adjroomof)) {

						gl.glColor4f(1f, 0f, 0f, 0.9f);

					} else {
						roomAdjList.add(selectedroom);
						gl.glColor4f(0f, 1f, 0f, 0.9f);
					}
					 gl.glEnable(GL2.GL_TEXTURE_2D);
		             gl.glBindTexture(GL2.GL_TEXTURE_2D, com.amrita.IIRMS.Visualization.IIRMSVisualization.wallTexture);

		             gl.glBegin(GL2.GL_QUADS);
		            // gl.glColor3f(1f, 1f, 1f);
		             for (int j = 0; j < r_pointList.size()-2; j = j + 2) {
		             	gl.glTexCoord2f(0f,0f);
		             	gl.glVertex3f(r_pointList.get(j) / transValue, (r_floorNow * 0.04f) , r_pointList.get(j + 1) / transValue);
		             	gl.glTexCoord2f(1f,0f);
		             	gl.glVertex3f(r_pointList.get(j+2) / transValue, (r_floorNow * 0.04f), r_pointList.get(j + 3) / transValue);
		             	gl.glTexCoord2f(1f, 1f);
		             	gl.glVertex3f(r_pointList.get(j+2) / transValue, (r_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 3) / transValue);
		             	gl.glTexCoord2f(0f, 1f);
		             	gl.glVertex3f(r_pointList.get(j) / transValue, (r_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 1) / transValue);                    
		             }
		             gl.glEnd();
		             gl.glDisable(GL2.GL_TEXTURE_2D);
		         	gl.glFlush();
				}
			}
		}
		String text = "ADJACENT ROOMS OF " + cAdjacent.getSelectedItem() + " : " + roomAdjList;
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();
	}
	
	/*
	 * Method name : dispExitOfRooms 
	 * Method description : Method to display the exit of the selected room  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispExitOfRoom(GL2 gl) throws Exception {
		
		int room_id=0;
		String query1="select room_id from room where room_name='"+ (String) cExitRoom.getSelectedItem() + "'";
		Statement stmt1=conn.createStatement();
		ResultSet rs1=stmt1.executeQuery(query1);
		while(rs1.next())
		{
			room_id=rs1.getInt(1);
		}

		String query2 = "select st_astext(exit.the_geom),exit.floor_id from room_exit inner join exit on room_exit.exit_id=exit.exit_id where room_id="+room_id;
		Statement stmt2 = conn.createStatement();
		ResultSet rs2 = stmt2.executeQuery(query2);
		String[] d_pointIn = null;
		List<Float> d_pointList;
		while (rs2.next()) {
			d_pointIn = rs2.getString(1).replace("MULTIPOINT", "").replace(",", " ").replace("(", "").replace(")", "").split(" ");
			int d_floorNow = Integer.parseInt(rs2.getString(2));
			d_pointList = new ArrayList<Float>();
			for (int i = 0; i < d_pointIn.length; i = i + 2) {
				float xPoint = Float.parseFloat(d_pointIn[i]);
				float yPoint = Float.parseFloat(d_pointIn[i + 1]);
				d_pointList.add(xPoint);
				d_pointList.add(yPoint);
			}
			 gl.glEnable(GL2.GL_TEXTURE_2D);
             gl.glBindTexture(GL2.GL_TEXTURE_2D, com.amrita.IIRMS.Visualization.IIRMSVisualization.doorTexture);
             gl.glBegin(GL2.GL_QUADS);
             gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
             gl.glColor3f(1.0f, 1.0f, 1.0f);

             for (int j = 0; j < d_pointList.size()-2; j = j + 2) {
                 gl.glTexCoord2f(0f,0f);
                 gl.glVertex3f(d_pointList.get(j) / transValue, d_floorNow * 0.04f, d_pointList.get(j + 1) / transValue);
                 gl.glTexCoord2f(1f,0f);
                 gl.glVertex3f(d_pointList.get(j+2) / transValue, d_floorNow * 0.04f, d_pointList.get(j + 3) / transValue);
                 gl.glTexCoord2f(1f, 1f);
                 gl.glVertex3f(d_pointList.get(j+2) / transValue, (d_floorNow * 0.04f) + 0.03f, d_pointList.get(j + 3) / transValue);
                 gl.glTexCoord2f(0f, 1f);
                 gl.glVertex3f(d_pointList.get(j) / transValue, (d_floorNow * 0.04f) + 0.03f, d_pointList.get(j + 1) / transValue);
                 
             }
             gl.glEnd();
             gl.glDisable(GL2.GL_TEXTURE_2D);
         	gl.glFlush();
		}
		String text = "EXIT OF ROOM " + cExitRoom.getSelectedItem();
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();
	}
	
	/*
	 * Method name : dispRoomsByExit 
	 * Method description : Method to display the exits nearest to the vicinity of the selected room  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispRoomsByExit(GL2 gl) throws Exception {
		
		int exit_id=0;
		String query1="select room_exit.exit_id,room.floor_id from room_exit inner join room on room_exit.room_id=room.room_id where room.room_name='"+cKExits.getSelectedItem()+"'";
		Statement stmt1=conn.createStatement();
		ResultSet rs1=stmt1.executeQuery(query1);
		while(rs1.next())
		{
			exit_id=rs1.getInt(1);
		}

		String query2 = "select st_astext(r1.the_geom),r1.floor_id, st_distance(r1.the_geom, r2.the_geom) as dist "
				+ "from exit r1, exit r2 " + "where st_distance(r1.the_geom, r2.the_geom) <=250.0 " + "and r2.exit_id="
				+ exit_id + " and r2.floor_id=r1.floor_id"+" order by dist  LIMIT " + (String) cKE.getSelectedItem();
		Statement stmt2 = conn.createStatement();
		ResultSet rs2 = stmt2.executeQuery(query2);
		String[] d_pointIn = null;
		List<Float> r_pointList;
		while (rs2.next()) {
			d_pointIn = rs2.getString(1).replace("MULTIPOINT", "").replace(",", " ").replace("(", "").replace(")", "").split(" ");
			int d_floorNow = Integer.parseInt(rs2.getString(2));
			r_pointList = new ArrayList<Float>();
			for (int i = 0; i < d_pointIn.length; i = i + 2) {
				float xPoint = Float.parseFloat(d_pointIn[i]);
				float yPoint = Float.parseFloat(d_pointIn[i + 1]);
				r_pointList.add(xPoint);
				r_pointList.add(yPoint);
			}
			gl.glEnable(GL2.GL_TEXTURE_2D);
            gl.glBindTexture(GL2.GL_TEXTURE_2D, doorTexture);

            gl.glBegin(GL2.GL_QUADS);
            gl.glColor3f(1f, 1f, 1f);
            for (int j = 0; j < r_pointList.size()-2; j = j + 2) {
            	gl.glTexCoord2f(0f,0f);
            	gl.glVertex3f(r_pointList.get(j) / transValue, (d_floorNow * 0.04f) , r_pointList.get(j + 1) / transValue);
            	gl.glTexCoord2f(1f,0f);
            	gl.glVertex3f(r_pointList.get(j+2) / transValue, (d_floorNow * 0.04f), r_pointList.get(j + 3) / transValue);
            	gl.glTexCoord2f(1f, 1f);
            	gl.glVertex3f(r_pointList.get(j+2) / transValue, (d_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 3) / transValue);
            	gl.glTexCoord2f(0f, 1f);
            	gl.glVertex3f(r_pointList.get(j) / transValue, (d_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 1) / transValue);                    
            }
            gl.glEnd();
            gl.glDisable(GL2.GL_TEXTURE_2D);
            gl.glFlush();
		}
		String text = "THE " + (String) cKE.getSelectedItem() + " EXITS NEAREST TO THE VICINITY OF ROOM " + cKExits.getSelectedItem();
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));
		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();
	}
	
	
	/*
	 * Method name : dispSingleWindows 
	 * Method description : Method to display the single windows in the selected floor  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void disptypeWindows(GL2 gl) throws Exception {

		int win_floorNow;
		String query1 = "select st_astext(the_geom),floor_id,room_id from windows where window_type ='"+Jcb_typewindows.getSelectedItem()+"' and floor_id='"
				+ (String) Cwtype.getSelectedItem() + "'";
		
		Statement stmt1 = conn.createStatement();
		ResultSet rs1 = stmt1.executeQuery(query1);
		String[] win_pointIn = null;
		List<Integer> w_roomIdList = new ArrayList<Integer>();
		List<String> w_roomNameList=new ArrayList<String>();
		List<Float> win_pointList;
		while (rs1.next()) {
			win_pointIn = rs1.getString(1).replace("MULTILINESTRING", "").replace("LINESTRING", "").replace(",", " ").replace("(", "")
					.replace(")", "").split(" ");
			win_floorNow = Integer.parseInt(rs1.getString(2));
			int roomId = rs1.getInt(3);
			w_roomIdList.add(roomId);
			win_pointList = new ArrayList<Float>();
			for (int i = 0; i < win_pointIn.length; i = i + 2) {
				float xPoint = Float.parseFloat(win_pointIn[i]);
				float yPoint = Float.parseFloat(win_pointIn[i + 1]);
				win_pointList.add(xPoint);
				win_pointList.add(yPoint);
			}
			gl.glEnable(GL2.GL_TEXTURE_2D);
            gl.glBindTexture(GL2.GL_TEXTURE_2D, windowTexture);
            gl.glBegin(GL2.GL_QUADS);
            gl.glColor3f(1f, 1f, 1f);
            for (int i = 0; i < win_pointList.size()-2; i = i + 2) {
            	gl.glTexCoord2f(0f, 0f);
                gl.glVertex3f(win_pointList.get(i) / transValue, (win_floorNow * 0.04f) + 0.01f, win_pointList.get(i + 1) / transValue);
                gl.glTexCoord2f(1f, 0f);
                gl.glVertex3f(win_pointList.get(i+2) / transValue, (win_floorNow * 0.04f) + 0.01f, win_pointList.get(i + 3) / transValue);
                gl.glTexCoord2f(1f, 1f);
                gl.glVertex3f(win_pointList.get(i+2) / transValue, (win_floorNow * 0.04f) + 0.03f, win_pointList.get(i + 3) / transValue);
                gl.glTexCoord2f(0f, 1f);
                gl.glVertex3f(win_pointList.get(i) / transValue, (win_floorNow * 0.04f) + 0.03f, win_pointList.get(i + 1) / transValue);
            }
            gl.glDisable(GL2.GL_TEXTURE_2D);
            gl.glEnd();
		    gl.glFlush();
		}
		
		for(int i=0;i<w_roomIdList.size();i++)
		{
			String query2="select room_name from room where room_id="+w_roomIdList.get(i);
			Statement stmt2=conn.createStatement();
			ResultSet rs2=stmt2.executeQuery(query2);
			while(rs2.next())
			{
				String roomName=rs2.getString(1);
				w_roomNameList.add(roomName);
			}
		}
		
		String text =  Jcb_typewindows.getSelectedItem().toString().toUpperCase()+"TYPE WINDOWS IN FLOOR" + Cwtype.getSelectedItem() + " : " + w_roomNameList;
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();
	}
	
	/*
	 * Method name : dispMaterialWindows 
	 * Method description : Method to display the windows(material) in the selected floor  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispMaterialWindows(GL2 gl) throws Exception {

		int win_floorNow;
		String query1 = "select st_astext(the_geom),floor_id,room_id from windows where window_material= '"+Jcb_materialwindows.getSelectedItem()+"' and floor_id='"
				+ (String) cWooden.getSelectedItem() + "'";
		
		Statement stmt1 = conn.createStatement();
		ResultSet rs1 = stmt1.executeQuery(query1);
		String[] win_pointIn = null;
		List<Integer> w_roomIdList = new ArrayList<Integer>();
		List<String> w_roomNameList=new ArrayList<String>();

		List<Float> win_pointList;
		while (rs1.next()) {
			win_pointIn = rs1.getString(1).replace("MULTILINESTRING", "").replace("LINESTRING", "").replace(",", " ").replace("(", "")
					.replace(")", "").split(" ");
			win_floorNow = Integer.parseInt(rs1.getString(2));
			int roomId = rs1.getInt(3);
			w_roomIdList.add(roomId);
			win_pointList = new ArrayList<Float>();
			for (int i = 0; i < win_pointIn.length; i = i + 2) {
				float xPoint = Float.parseFloat(win_pointIn[i]);
				float yPoint = Float.parseFloat(win_pointIn[i + 1]);
				win_pointList.add(xPoint);
				win_pointList.add(yPoint);
			}
			gl.glEnable(GL2.GL_TEXTURE_2D);
            gl.glBindTexture(GL2.GL_TEXTURE_2D, windowTexture);
            gl.glBegin(GL2.GL_QUADS);
            gl.glColor3f(1f, 1f, 1f);
            for (int i = 0; i < win_pointList.size()-2; i = i + 2) {
            	gl.glTexCoord2f(0f, 0f);
                gl.glVertex3f(win_pointList.get(i) / transValue, (win_floorNow * 0.04f) + 0.01f, win_pointList.get(i + 1) / transValue);
                gl.glTexCoord2f(1f, 0f);
                gl.glVertex3f(win_pointList.get(i+2) / transValue, (win_floorNow * 0.04f) + 0.01f, win_pointList.get(i + 3) / transValue);
                gl.glTexCoord2f(1f, 1f);
                gl.glVertex3f(win_pointList.get(i+2) / transValue, (win_floorNow * 0.04f) + 0.03f, win_pointList.get(i + 3) / transValue);
                gl.glTexCoord2f(0f, 1f);
                gl.glVertex3f(win_pointList.get(i) / transValue, (win_floorNow * 0.04f) + 0.03f, win_pointList.get(i + 1) / transValue);
            }
            gl.glDisable(GL2.GL_TEXTURE_2D);
            gl.glEnd();
		    gl.glFlush();
		}
		for(int i=0;i<w_roomIdList.size();i++)
		{
			String query2="select room_name from room where room_id="+w_roomIdList.get(i);
			Statement stmt2=conn.createStatement();
			ResultSet rs2=stmt2.executeQuery(query2);
			while(rs2.next())
			{
				String roomName=rs2.getString(1);
				w_roomNameList.add(roomName);
			}
		}
		String text = "WINDOWS MADE UP OF "+ Jcb_materialwindows.getSelectedItem().toString().toUpperCase() +" IN FLOOR" + cWooden.getSelectedItem() + " : " + w_roomNameList;
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();

	}
	
	/*
	 * Method name : dispDoubleDoors 
	 * Method description : Method to display the double doors in the selected floor  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispDoubleDoors(GL2 gl) throws Exception {

		String query1 = "select st_astext(the_geom),open_inward_dir from exit where exit_type LIKE '%double%' and floor_id = '"
				+ (String) cDoubleDoors.getSelectedItem() + "'";
		Statement stmt1 = conn.createStatement();
		ResultSet rs1 = stmt1.executeQuery(query1);
		int d_floorNow = Integer.parseInt((String) cDoubleDoors.getSelectedItem());
		String[] d_pointIn = null;
		ArrayList<String> roomDdoorList = new ArrayList<String>();
		List<Float> d_pointList;
		while (rs1.next()) {
			d_pointIn = rs1.getString(1).replace("MULTIPOINT", "").replace(",", " ").replace("(", "").replace(")", "").split(" ");
			d_pointList = new ArrayList<Float>();
			String roomname = rs1.getString(2);
			roomDdoorList.add(roomname);
			for (int i = 0; i < d_pointIn.length; i = i + 2) {
				float xPoint = Float.parseFloat(d_pointIn[i]);
				float yPoint = Float.parseFloat(d_pointIn[i + 1]);
				d_pointList.add(xPoint);
				d_pointList.add(yPoint);
			}
			gl.glEnable(GL2.GL_TEXTURE_2D);
            gl.glBindTexture(GL2.GL_TEXTURE_2D, doorTexture);

            gl.glBegin(GL2.GL_QUADS);
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            gl.glColor3f(1.0f, 1.0f, 1.0f);

            for (int j = 0; j < d_pointList.size()-2; j = j + 2) {
                gl.glTexCoord2f(0f,0f);
                gl.glVertex3f(d_pointList.get(j) / transValue, d_floorNow * 0.04f, d_pointList.get(j + 1) / transValue);
                gl.glTexCoord2f(1f,0f);
                gl.glVertex3f(d_pointList.get(j+2) / transValue, d_floorNow * 0.04f, d_pointList.get(j + 3) / transValue);
                gl.glTexCoord2f(1f, 1f);
                gl.glVertex3f(d_pointList.get(j+2) / transValue, (d_floorNow * 0.04f) + 0.03f, d_pointList.get(j + 3) / transValue);
                gl.glTexCoord2f(0f, 1f);
                gl.glVertex3f(d_pointList.get(j) / transValue, (d_floorNow * 0.04f) + 0.03f, d_pointList.get(j + 1) / transValue);
                
            }
            gl.glDisable(GL2.GL_TEXTURE_2D);
            gl.glEnd();
           
		}
		String text = "DOUBLE DOORS IN FLOOR " + cDoubleDoors.getSelectedItem(); //+ roomDdoorList;
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();

	}
	
	/*
	 * Method name : dispSingleDoors 
	 * Method description : Method to display the single doors in the selected floor  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispSingleDoors(GL2 gl) throws Exception {

		String query1 = "select st_astext(the_geom),open_inward_dir from exit where exit_type LIKE '%single%' and floor_id = '"
				+ (String) cSingleDoors.getSelectedItem() + "'";
		Statement stmt1 = conn.createStatement();
		ResultSet rs1 = stmt1.executeQuery(query1);
		int d_floorNow = Integer.parseInt((String) cSingleDoors.getSelectedItem());
		String[] d_pointIn = null;
		ArrayList<String> roomSdoorList = new ArrayList<String>();
		List<Float> d_pointList;
		while (rs1.next()) {
			d_pointIn = rs1.getString(1).replace("MULTIPOINT", "").replace(",", " ").replace("(", "").replace(")", "").split(" ");
			d_pointList = new ArrayList<Float>();
			String roomname = rs1.getString(2);
			roomSdoorList.add(roomname);
			for (int i = 0; i < d_pointIn.length; i = i + 2) {
				float xPoint = Float.parseFloat(d_pointIn[i]);
				float yPoint = Float.parseFloat(d_pointIn[i + 1]);
				d_pointList.add(xPoint);
				d_pointList.add(yPoint);
			}
			gl.glEnable(GL2.GL_TEXTURE_2D);
            gl.glBindTexture(GL2.GL_TEXTURE_2D, doorTexture);

            gl.glBegin(GL2.GL_QUADS);
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            gl.glColor3f(1.0f, 1.0f, 1.0f);

            for (int j = 0; j < d_pointList.size()-2; j = j + 2) {
                gl.glTexCoord2f(0f,0f);
                gl.glVertex3f(d_pointList.get(j) / transValue, d_floorNow * 0.04f, d_pointList.get(j + 1) / transValue);
                gl.glTexCoord2f(1f,0f);
                gl.glVertex3f(d_pointList.get(j+2) / transValue, d_floorNow * 0.04f, d_pointList.get(j + 3) / transValue);
                gl.glTexCoord2f(1f, 1f);
                gl.glVertex3f(d_pointList.get(j+2) / transValue, (d_floorNow * 0.04f) + 0.03f, d_pointList.get(j + 3) / transValue);
                gl.glTexCoord2f(0f, 1f);
                gl.glVertex3f(d_pointList.get(j) / transValue, (d_floorNow * 0.04f) + 0.03f, d_pointList.get(j + 1) / transValue);
                
            }
            gl.glDisable(GL2.GL_TEXTURE_2D);
            gl.glEnd();
			gl.glFlush();
		}
		String text = "SINGLE DOORS IN FLOOR " + cSingleDoors.getSelectedItem();// + roomSdoorList;
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();
	}
	
	/*
	 * Method name : dispDoorsOfType 
	 * Method description : Method to display doors of the selected type  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispDoorsOfType(GL2 gl) throws Exception {

		String query1 = "select st_astext(the_geom),open_inward_dir from exit where exit_material = '"
				+ (String) cMaterial1.getSelectedItem() + "' and " + "floor_id = '" + (String) cMaterial2.getSelectedItem() + "'";
		Statement stmt1 = conn.createStatement();
		ResultSet rs1 = stmt1.executeQuery(query1);
		int d_floorNow = Integer.parseInt((String) cMaterial2.getSelectedItem());
		String[] d_pointIn = null;
		ArrayList<String> roomTypeList = new ArrayList<String>();
		List<Float> d_pointList;
		while (rs1.next()) {
			d_pointIn = rs1.getString(1).replace("MULTIPOINT", "").replace(",", " ").replace("(", "").replace(")", "").split(" ");
			d_pointList = new ArrayList<Float>();
			String roomname = rs1.getString(2);
			roomTypeList.add(roomname);
			for (int i = 0; i < d_pointIn.length; i = i + 2) {
				float xPoint = Float.parseFloat(d_pointIn[i]);
				float yPoint = Float.parseFloat(d_pointIn[i + 1]);
				d_pointList.add(xPoint);
				d_pointList.add(yPoint);
			}
			gl.glEnable(GL2.GL_TEXTURE_2D);
            gl.glBindTexture(GL2.GL_TEXTURE_2D, doorTexture);

            gl.glBegin(GL2.GL_QUADS);
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            gl.glColor3f(1.0f, 1.0f, 1.0f);

            for (int j = 0; j < d_pointList.size()-2; j = j + 2) {
                gl.glTexCoord2f(0f,0f);
                gl.glVertex3f(d_pointList.get(j) / transValue, d_floorNow * 0.04f, d_pointList.get(j + 1) / transValue);
                gl.glTexCoord2f(1f,0f);
                gl.glVertex3f(d_pointList.get(j+2) / transValue, d_floorNow * 0.04f, d_pointList.get(j + 3) / transValue);
                gl.glTexCoord2f(1f, 1f);
                gl.glVertex3f(d_pointList.get(j+2) / transValue, (d_floorNow * 0.04f) + 0.03f, d_pointList.get(j + 3) / transValue);
                gl.glTexCoord2f(0f, 1f);
                gl.glVertex3f(d_pointList.get(j) / transValue, (d_floorNow * 0.04f) + 0.03f, d_pointList.get(j + 1) / transValue);
                
            }
            gl.glDisable(GL2.GL_TEXTURE_2D);
            gl.glEnd();
			gl.glFlush();
		}
		String text = "DOORS OF " + cMaterial1.getSelectedItem().toString().toUpperCase().trim() + " MATERIAL IN FLOOR"
				+ cMaterial2.getSelectedItem();// + roomTypeList;
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));
		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();
	}
	
	/*
	 * Method name : dispOutwardDoors 
	 * Method description : Method to display the outward doors in the selected floor  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispOutwardDoors(GL2 gl) throws Exception {

		String query1 = "select exit_id,st_astext(the_geom) from exit where open_inward_dir='false' and floor_id='"
				+ (String) cOutward.getSelectedItem() + "'";
		Statement stmt1 = conn.createStatement();
		ResultSet rs1 = stmt1.executeQuery(query1);
		int d_floorNow = Integer.parseInt((String) cOutward.getSelectedItem());
		String[] d_pointIn = null;
		ArrayList<String> roomOutdirList = new ArrayList<String>();
		ArrayList<Integer> exitIdList=new ArrayList<Integer>();
		List<Float> d_pointList;
		while (rs1.next()) {
			int exit_id = rs1.getInt(1);
			exitIdList.add(exit_id);
			d_pointIn = rs1.getString(2).replace("MULTIPOINT", "").replace(",", " ").replace("(", "").replace(")", "").split(" ");
			d_pointList = new ArrayList<Float>();
			for (int i = 0; i < d_pointIn.length; i = i + 2) {
				float xPoint = Float.parseFloat(d_pointIn[i]);
				float yPoint = Float.parseFloat(d_pointIn[i + 1]);
				d_pointList.add(xPoint);
				d_pointList.add(yPoint);
			}
			gl.glEnable(GL2.GL_TEXTURE_2D);
            gl.glBindTexture(GL2.GL_TEXTURE_2D, doorTexture);

            gl.glBegin(GL2.GL_QUADS);
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            gl.glColor3f(1.0f, 1.0f, 1.0f);

            for (int j = 0; j < d_pointList.size()-2; j = j + 2) {
                gl.glTexCoord2f(0f,0f);
                gl.glVertex3f(d_pointList.get(j) / transValue, d_floorNow * 0.04f, d_pointList.get(j + 1) / transValue);
                gl.glTexCoord2f(1f,0f);
                gl.glVertex3f(d_pointList.get(j+2) / transValue, d_floorNow * 0.04f, d_pointList.get(j + 3) / transValue);
                gl.glTexCoord2f(1f, 1f);
                gl.glVertex3f(d_pointList.get(j+2) / transValue, (d_floorNow * 0.04f) + 0.03f, d_pointList.get(j + 3) / transValue);
                gl.glTexCoord2f(0f, 1f);
                gl.glVertex3f(d_pointList.get(j) / transValue, (d_floorNow * 0.04f) + 0.03f, d_pointList.get(j + 1) / transValue);
                
            }
            gl.glDisable(GL2.GL_TEXTURE_2D);
            gl.glEnd();
			gl.glFlush();
		}
		String text = "THE OUTWARD DIRECTION DOORS IN FLOOR" + cOutward.getSelectedItem() ;// + roomOutdirList;
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();
	}
	
	/*
	 * Method name : dispInwardDoors 
	 * Method description : Method to display the inward doors in the selected floor  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispInwardDoors(GL2 gl) throws Exception {

		String query1 = "select exit_id,st_astext(the_geom) from exit where open_inward_dir='true' and floor_id='"
				+ (String) cInward.getSelectedItem() + "'";
		Statement stmt1 = conn.createStatement();
		ResultSet rs1 = stmt1.executeQuery(query1);
		int d_floorNow = Integer.parseInt((String) cInward.getSelectedItem());
		String[] d_pointIn = null;
		ArrayList<String> roomInwdirList = new ArrayList<String>();
		ArrayList<Integer> exitIdList=new ArrayList<Integer>();

		List<Float> d_pointList;
		while (rs1.next()) {
			int exit_id = rs1.getInt(1);
			exitIdList.add(exit_id);
			d_pointIn = rs1.getString(2).replace("MULTIPOINT", "").replace(",", " ").replace("(", "").replace(")", "").split(" ");
			d_pointList = new ArrayList<Float>();
			for (int i = 0; i < d_pointIn.length; i = i + 2) {
				float xPoint = Float.parseFloat(d_pointIn[i]);
				float yPoint = Float.parseFloat(d_pointIn[i + 1]);
				d_pointList.add(xPoint);
				d_pointList.add(yPoint);
			}
			gl.glEnable(GL2.GL_TEXTURE_2D);
            gl.glBindTexture(GL2.GL_TEXTURE_2D, doorTexture);

            gl.glBegin(GL2.GL_QUADS);
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            gl.glColor3f(1.0f, 1.0f, 1.0f);

            for (int j = 0; j < d_pointList.size()-2; j = j + 2) {
                gl.glTexCoord2f(0f,0f);
                gl.glVertex3f(d_pointList.get(j) / transValue, d_floorNow * 0.04f, d_pointList.get(j + 1) / transValue);
                gl.glTexCoord2f(1f,0f);
                gl.glVertex3f(d_pointList.get(j+2) / transValue, d_floorNow * 0.04f, d_pointList.get(j + 3) / transValue);
                gl.glTexCoord2f(1f, 1f);
                gl.glVertex3f(d_pointList.get(j+2) / transValue, (d_floorNow * 0.04f) + 0.03f, d_pointList.get(j + 3) / transValue);
                gl.glTexCoord2f(0f, 1f);
                gl.glVertex3f(d_pointList.get(j) / transValue, (d_floorNow * 0.04f) + 0.03f, d_pointList.get(j + 1) / transValue);
                
            }
            gl.glDisable(GL2.GL_TEXTURE_2D);
            gl.glEnd();
			gl.glFlush();
		}
		
		String text = "THE INWARD DIRECTION DOORS IN FLOOR" + cInward.getSelectedItem() ;// + roomInwdirList;
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();

	}
	
	/*
	 * Method name : dispWallsOfWindow 
	 * Method description : Method to display the walls containing window in the selected room  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispWallsOfWindow(GL2 gl) throws Exception {

		int room_id=0;
		String query1="select room_id from room where room_name='"+(String) cWallsWindows.getSelectedItem()+"'";
		Statement stmt1=conn.createStatement();
		ResultSet rs1=stmt1.executeQuery(query1);
		while(rs1.next())
		{
			room_id=rs1.getInt(1);
		}
		String query = "select st_astext(w.the_geom),w.floor_id from wall w,windows win where win.room_id='"
				+ room_id + "'and win.wall_id=w.wall_id";
		Statement stmt3 = conn.createStatement();
		ResultSet rs3 = stmt3.executeQuery(query);
		String[] w_pointIn = null;

		List<Float> w_pointList;
		while (rs3.next()) {
			w_pointIn = rs3.getString(1).replace("MULTILINESTRING", "").replace("LINESTRING", "").replace(",", " ").replace("(", "")
					.replace(")", "").split(" ");
			int w_floorNow = Integer.parseInt(rs3.getString(2));
			w_pointList = new ArrayList<Float>();
			for (int i = 0; i < w_pointIn.length; i = i + 2) {
				float xPoint = Float.parseFloat(w_pointIn[i]);
				float yPoint = Float.parseFloat(w_pointIn[i + 1]);
				w_pointList.add(xPoint);
				w_pointList.add(yPoint);
			}
			 for(int i=0;i<w_pointList.size();i=i+4)
	            {
	            	gl.glEnable(GL2.GL_TEXTURE_2D);
	            	gl.glBindTexture(GL2.GL_TEXTURE_2D, wallTexture);
	            	gl.glBegin(GL2.GL_QUAD_STRIP);
	            	gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	                gl.glColor3f(1.0f, 1.0f, 1.0f);
	            	float t = 0f;
	            	for (int j = i; j < i+4; j = j + 2) {
	            		gl.glTexCoord2f(t * 1f, t * 0f);
	            		gl.glVertex3f(w_pointList.get(j) / transValue, w_floorNow * 0.04f, w_pointList.get(j + 1) / transValue);
	            		gl.glTexCoord2f(t, 1f);
	            		gl.glVertex3f(w_pointList.get(j) / transValue, (w_floorNow * 0.04f) + 0.04f, w_pointList.get(j + 1) / transValue);
	            		t = 1f;
	            	}
	            	gl.glEnd();
	            	gl.glDisable(GL2.GL_TEXTURE_2D);
	            	gl.glFlush();
	            }
		}
		String text = "WALLS CONTAINING THE WINDOW IN ROOM " + cWallsWindows.getSelectedItem();
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();
	}
	
	/*
	 * Method name : dispWallsOfType 
	 * Method description : Method to display the walls of the selected type  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispWallsOfType(GL2 gl) throws Exception {

		String query = "select st_astext(the_geom) from wall where wall_" + (String) cSelect1.getSelectedItem() + "='" + selectValueFinal
				+ "' and floor_id = '" + (String) cSelect3.getSelectedItem() + "'";
		Statement stmt3 = conn.createStatement();
		ResultSet rs3 = stmt3.executeQuery(query);
		String[] w_pointIn = null;

		List<Float> w_pointList;
		while (rs3.next()) {
			w_pointIn = rs3.getString(1).replace("MULTILINESTRING", "").replace("LINESTRING", "").replace(",", " ").replace("(", "")
					.replace(")", "").split(" ");

			int w_floorNow = Integer.parseInt((String) cSelect3.getSelectedItem());
			w_pointList = new ArrayList<Float>();
			for (int i = 0; i < w_pointIn.length; i = i + 2) {
				float xPoint = Float.parseFloat(w_pointIn[i]);
				float yPoint = Float.parseFloat(w_pointIn[i + 1]);
				w_pointList.add(xPoint);
				w_pointList.add(yPoint);
			}
			for(int i=0;i<w_pointList.size();i=i+4)
            {
            	gl.glEnable(GL2.GL_TEXTURE_2D);
            	gl.glBindTexture(GL2.GL_TEXTURE_2D, wallTexture);
            	gl.glBegin(GL2.GL_QUAD_STRIP);
            	gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                gl.glColor3f(1.0f, 1.0f, 1.0f);
            	float t = 0f;
            	for (int j = i; j < i+4; j = j + 2) {
            		gl.glTexCoord2f(t * 1f, t * 0f);
            		gl.glVertex3f(w_pointList.get(j) / transValue, w_floorNow * 0.04f, w_pointList.get(j + 1) / transValue);
            		gl.glTexCoord2f(t, 1f);
            		gl.glVertex3f(w_pointList.get(j) / transValue, (w_floorNow * 0.04f) + 0.04f, w_pointList.get(j + 1) / transValue);
            		t = 1f;
            	}
            	gl.glEnd();
            	gl.glDisable(GL2.GL_TEXTURE_2D);
            	gl.glFlush();
            }
		}
		String text = "WALLS OF " + selectValueFinal.toUpperCase().trim() + " "
				+ cSelect1.getSelectedItem().toString().toUpperCase().trim() + " IN FLOOR" + cSelect3.getSelectedItem();
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();
	}
	
	/*
	 * Method name : dispCommonWallsOfFloor 
	 * Method description : Method to display the common walls in the selected floor  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispCommonWallsOfFloor(GL2 gl) throws Exception {

		String query = "select st_astext(the_geom),floor_id from wall where wall_type='common' and floor_id = '"
				+ (String) cCommonWalls.getSelectedItem() + "'";
		Statement stmt3 = conn.createStatement();
		ResultSet rs3 = stmt3.executeQuery(query);
		String[] w_pointIn = null;

		List<Float> w_pointList;
		while (rs3.next()) {
			w_pointIn = rs3.getString(1).replace("MULTILINESTRING", "").replace("LINESTRING", "").replace(",", " ").replace("(", "")
					.replace(")", "").split(" ");
			int w_floorNow = Integer.parseInt(rs3.getString(2));

			w_pointList = new ArrayList<Float>();
			for (int i = 0; i < w_pointIn.length; i = i + 2) {
				float xPoint = Float.parseFloat(w_pointIn[i]);
				float yPoint = Float.parseFloat(w_pointIn[i + 1]);
				w_pointList.add(xPoint);
				w_pointList.add(yPoint);
			}
			for(int i=0;i<w_pointList.size();i=i+4)
            {
            	gl.glEnable(GL2.GL_TEXTURE_2D);
            	gl.glBindTexture(GL2.GL_TEXTURE_2D, wallTexture);
            	gl.glBegin(GL2.GL_QUAD_STRIP);
            	gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                gl.glColor3f(1.0f, 1.0f, 1.0f);
            	float t = 0f;
            	for (int j = i; j < i+4; j = j + 2) {
            		gl.glTexCoord2f(t * 1f, t * 0f);
            		gl.glVertex3f(w_pointList.get(j) / transValue, w_floorNow * 0.04f, w_pointList.get(j + 1) / transValue);
            		gl.glTexCoord2f(t, 1f);
            		gl.glVertex3f(w_pointList.get(j) / transValue, (w_floorNow * 0.04f) + 0.04f, w_pointList.get(j + 1) / transValue);
            		t = 1f;
            	}
            	gl.glEnd();
            	gl.glDisable(GL2.GL_TEXTURE_2D);
            	gl.glFlush();
            }
		}
		String text = "COMMON WALLS IN FLOOR" + cCommonWalls.getSelectedItem();
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();
	}
	
	/*
	 * Method name : dispWallsOfFloor 
	 * Method description : Method to display walls in the selected floor  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispWallsOfFloor(GL2 gl) throws Exception {

		String query = "select st_astext(the_geom),floor_id from wall where  floor_id = '" + (String) cWalls.getSelectedItem() + "'";
		Statement stmt3 = conn.createStatement();
		ResultSet rs3 = stmt3.executeQuery(query);
		String[] w_pointIn = null;

		List<Float> w_pointList;
		while (rs3.next()) {
			w_pointIn = rs3.getString(1).replace("MULTILINESTRING", "").replace("LINESTRING", "").replace(",", " ").replace("(", "")
					.replace(")", "").split(" ");
			int w_floorNow = Integer.parseInt(rs3.getString(2));
			w_pointList = new ArrayList<Float>();
			for (int i = 0; i < w_pointIn.length; i = i + 2) {
				float xPoint = Float.parseFloat(w_pointIn[i]);
				float yPoint = Float.parseFloat(w_pointIn[i + 1]);
				w_pointList.add(xPoint);
				w_pointList.add(yPoint);
			}
			for(int i=0;i<w_pointList.size();i=i+4)
            {
            	gl.glEnable(GL2.GL_TEXTURE_2D);
            	gl.glBindTexture(GL2.GL_TEXTURE_2D, wallTexture);
            	gl.glBegin(GL2.GL_QUAD_STRIP);
            	gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                gl.glColor3f(1.0f, 1.0f, 1.0f);
            	float t = 0f;
            	for (int j = i; j < i+4; j = j + 2) {
            		gl.glTexCoord2f(t * 1f, t * 0f);
            		gl.glVertex3f(w_pointList.get(j) / transValue, w_floorNow * 0.04f, w_pointList.get(j + 1) / transValue);
            		gl.glTexCoord2f(t, 1f);
            		gl.glVertex3f(w_pointList.get(j) / transValue, (w_floorNow * 0.04f) + 0.04f, w_pointList.get(j + 1) / transValue);
            		t = 1f;
            	}
            	gl.glEnd();
            	gl.glDisable(GL2.GL_TEXTURE_2D);
            	gl.glFlush();
            }
		}
		String text = "WALLS IN FLOOR" + (String) cWalls.getSelectedItem();
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();

	}
	
	/*
	 * Method name : dispInnerWalls 
	 * Method description : Method to display the walls in the selected floor  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispInnerWalls(GL2 gl) throws Exception {

		String query1 = "select st_astext(the_geom),floor_id from wall where wall_type LIKE '%inner%' and floor_id = '"
				+ (String) cInnerWalls.getSelectedItem() + "'";
		Statement stmt1 = conn.createStatement();
		ResultSet rs1 = stmt1.executeQuery(query1);
		String[] w_pointIn = null;
		List<Float> w_pointList;
		while (rs1.next()) {
			w_pointIn = rs1.getString(1).replace("MULTILINESTRING", "").replace("LINESTRING", "").replace(",", " ").replace("(", "")
					.replace(")", "").split(" ");
			int w_floorNow = Integer.parseInt(rs1.getString(2));
			w_pointList = new ArrayList<Float>();
			for (int i = 0; i < w_pointIn.length; i = i + 2) {
				float xPoint = Float.parseFloat(w_pointIn[i]);
				float yPoint = Float.parseFloat(w_pointIn[i + 1]);
				w_pointList.add(xPoint);
				w_pointList.add(yPoint);
			}
			for(int i=0;i<w_pointList.size();i=i+4)
            {
            	gl.glEnable(GL2.GL_TEXTURE_2D);
            	gl.glBindTexture(GL2.GL_TEXTURE_2D, wallTexture);
            	gl.glBegin(GL2.GL_QUAD_STRIP);
            	gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                gl.glColor3f(1.0f, 1.0f, 1.0f);
            	float t = 0f;
            	for (int j = i; j < i+4; j = j + 2) {
            		gl.glTexCoord2f(t * 1f, t * 0f);
            		gl.glVertex3f(w_pointList.get(j) / transValue, w_floorNow * 0.04f, w_pointList.get(j + 1) / transValue);
            		gl.glTexCoord2f(t, 1f);
            		gl.glVertex3f(w_pointList.get(j) / transValue, (w_floorNow * 0.04f) + 0.04f, w_pointList.get(j + 1) / transValue);
            		t = 1f;
            	}
            	gl.glEnd();
            	gl.glDisable(GL2.GL_TEXTURE_2D);
            	gl.glFlush();
            }
		}
		String text = "INNER WALLS IN FLOOR" + cInnerWalls.getSelectedItem();
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();
	}
	
	/*
	 * Method name : dispSeatCapacityOfRoom 
	 * Method description : Method to display the seating capacity of the selected room  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispSeatCapacityOfRoom(GL2 gl) throws Exception {
		String query1 = "select st_astext(the_geom),floor_id from room " + "where room_name = '" + (String) cSeating.getSelectedItem()
				+ "'";

		Statement stmt1 = conn.createStatement();
		ResultSet rs1 = stmt1.executeQuery(query1);
		String[] r_pointIn = null;

		List<Float> r_pointList;

		while (rs1.next()) {
			r_pointIn = rs1.getString(1).replace("MULTIPOLYGON", "").replace("POLYGON", "").replace(",", " ").replace("(", "")
					.replace(")", "").split(" ");
			String r_floor = rs1.getString(2);
			int r_floorNow = Integer.parseInt(r_floor);
			r_pointList = new ArrayList<Float>();
			for (int i = 0; i < r_pointIn.length; i = i + 2) {
				float xPoint = Float.parseFloat(r_pointIn[i]);
				float yPoint = Float.parseFloat(r_pointIn[i + 1]);
				r_pointList.add(xPoint);
				r_pointList.add(yPoint);
			}
			 gl.glEnable(GL2.GL_TEXTURE_2D);
             gl.glBindTexture(GL2.GL_TEXTURE_2D, wallTexture);

             gl.glBegin(GL2.GL_QUADS);
             gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
             gl.glColor3f(1.0f, 1.0f, 1.0f);
             for (int j = 0; j < r_pointList.size()-2; j = j + 2) {
             	gl.glTexCoord2f(0f,0f);
             	gl.glVertex3f(r_pointList.get(j) / transValue, (r_floorNow * 0.04f) , r_pointList.get(j + 1) / transValue);
             	gl.glTexCoord2f(1f,0f);
             	gl.glVertex3f(r_pointList.get(j+2) / transValue, (r_floorNow * 0.04f), r_pointList.get(j + 3) / transValue);
             	gl.glTexCoord2f(1f, 1f);
             	gl.glVertex3f(r_pointList.get(j+2) / transValue, (r_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 3) / transValue);
             	gl.glTexCoord2f(0f, 1f);
             	gl.glVertex3f(r_pointList.get(j) / transValue, (r_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 1) / transValue);                    
             }
             gl.glEnd();
             gl.glDisable(GL2.GL_TEXTURE_2D);
             gl.glFlush();
		}
		String text = "SEATING CAPACITY OF ROOM " + (String) cSeating.getSelectedItem() + ": " + area;
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();
	}
	
	/*
	 * Method name : dispDistBetweenRooms 
	 * Method description : Method to display the distance between selected rooms  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispDistBetweenRooms(GL2 gl) throws Exception {
		ArrayList<Integer> r_pointArray = new ArrayList<Integer>();
		String query = "select r1.room_id,r2.room_id,st_distance(r1.the_geom, r2.the_geom)from room r1, room r2 " + "where r1.room_name='"
				+ cDist1.getSelectedItem() + "' and r2.room_name='" + cDist2.getSelectedItem() + "'";
		Statement stmt4 = conn.createStatement();
		ResultSet rsRooms = stmt4.executeQuery(query);
		while (rsRooms.next()) {
			String r1 = rsRooms.getString(1);
			String r2 = rsRooms.getString(2);
			distance1 = rsRooms.getString(3);
			r_pointArray.add(Integer.parseInt(r1));
			r_pointArray.add(Integer.parseInt(r2));
		}
		for (int c = 0; c < r_pointArray.size(); c = c + 1) {
			String query1 = "select st_astext(the_geom),floor_id,room_type,room_name from room where room_id=" + r_pointArray.get(c);
			Statement stmt1 = conn.createStatement();
			ResultSet rsRooms1 = stmt1.executeQuery(query1);

			while (rsRooms1.next()) {
				String[] r_pointIn = rsRooms1.getString(1).replace("MULTIPOLYGON", "").replace("POLYGON", "").replace(",", " ")
						.replace("(", "").replace(")", "").split(" ");
				ArrayList<Float> r_pointList = new ArrayList<Float>();
				String r_floor = rsRooms1.getString(2);
				int r_floorNow = Integer.parseInt(r_floor);
				String roomType = rsRooms1.getString(3);
				if (!roomType.contains("Corridor")) {
					for (int i = 0; i < r_pointIn.length; i = i + 2) {
						float xPoint = Float.parseFloat(r_pointIn[i]);
						float yPoint = Float.parseFloat(r_pointIn[i + 1]);
						r_pointList.add(xPoint);
						r_pointList.add(yPoint);
					}
					gl.glEnable(GL2.GL_TEXTURE_2D);
		             gl.glBindTexture(GL2.GL_TEXTURE_2D, wallTexture);

		             gl.glBegin(GL2.GL_QUADS);
		             gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		             gl.glColor3f(1.0f, 1.0f, 1.0f);
		             
		             for (int j = 0; j < r_pointList.size()-2; j = j + 2) {
		             	gl.glTexCoord2f(0f,0f);
		             	gl.glVertex3f(r_pointList.get(j) / transValue, (r_floorNow * 0.04f) , r_pointList.get(j + 1) / transValue);
		             	gl.glTexCoord2f(1f,0f);
		             	gl.glVertex3f(r_pointList.get(j+2) / transValue, (r_floorNow * 0.04f), r_pointList.get(j + 3) / transValue);
		             	gl.glTexCoord2f(1f, 1f);
		             	gl.glVertex3f(r_pointList.get(j+2) / transValue, (r_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 3) / transValue);
		             	gl.glTexCoord2f(0f, 1f);
		             	gl.glVertex3f(r_pointList.get(j) / transValue, (r_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 1) / transValue);                    
		             }
		             gl.glEnd();
		             gl.glDisable(GL2.GL_TEXTURE_2D);
		             gl.glFlush();
					
				}
			}
		}

		String text = "DISTANCE BETWEEN " + cDist1.getSelectedItem() + " AND " + cDist2.getSelectedItem() + " : " + distance1 + " Meters";
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();
	}
	
	/*
	 * Method name : dispWindowsOfRooms 
	 * Method description : Method to display the windows in the selected room  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispWindowsOfRoom(GL2 gl) throws Exception {
		String query1 = "select st_astext(the_geom),floor_id from room " + "where room_name = '" + cWindowsRoom.getSelectedItem() + "'";

		Statement stmt1 = conn.createStatement();
		ResultSet rs1 = stmt1.executeQuery(query1);
		String[] r_pointIn = null;

		List<Float> r_pointList;

		while (rs1.next()) {
			r_pointIn = rs1.getString(1).replace("MULTIPOLYGON", "").replace("POLYGON", "").replace(",", " ").replace("(", "")
					.replace(")", "").split(" ");
			String r_floor = rs1.getString(2);
			int r_floorNow = Integer.parseInt(r_floor);
			r_pointList = new ArrayList<Float>();
			for (int i = 0; i < r_pointIn.length; i = i + 2) {
				float xPoint = Float.parseFloat(r_pointIn[i]);
				float yPoint = Float.parseFloat(r_pointIn[i + 1]);
				r_pointList.add(xPoint);
				r_pointList.add(yPoint);
			}
			
			
			gl.glEnable(GL2.GL_TEXTURE_2D);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, com.amrita.IIRMS.Visualization.IIRMSVisualization.wallTexture);

			gl.glBegin(GL2.GL_QUAD_STRIP);
			
            gl.glColor3f(1.0f, 1.0f, 1.0f);
			float f2 = 0f;
			for (int j = 0; j < r_pointList.size(); j = j + 2) {
				gl.glTexCoord2f(f2 * 1f, f2 * 0f);
				gl.glVertex3f(r_pointList.get(j) / 10000.0f, (r_floorNow * 0.04f), r_pointList.get(j + 1) / 10000.0f);
				gl.glTexCoord2f(f2, 1f);
				gl.glVertex3f(r_pointList.get(j) / 10000.0f, (r_floorNow * 0.04f) + 0.04f, r_pointList.get(j + 1) / 10000.0f);
				f2 = 1f;
			}
			gl.glEnd();
			gl.glDisable(GL2.GL_TEXTURE_2D);
			
			gl.glFlush();
		}

		String text = "NUMBER OF WINDOWS IN ROOM " + cWindowsRoom.getSelectedItem() + " : " + countWindows;
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();
	}
	
	/*
	 * Method name : dispStairsOfFloor 
	 * Method description : Method to display stairs in the selected floor  
	 * Method Arguments : GL2(gl)
	 * Arguments description : "gl"	---> To initialize the OpenGL graphics context  
	 * Return type : void Return
	 * Return type description : Returns nothing.
	 */

	public static void dispStairsOfFloor(GL2 gl) throws Exception {
		
		 String floor=cStair.getSelectedItem().toString();
		 int floor_id= Integer.parseInt(floor);
		 System.out.println("stairs of floor:"+floor_id);
		 com.amrita.IIRMS.Visualization.IIRMSVisualization.drawStair_3D(gl,floor_id);
		String text = "STAIRS OF FLOOR " + cStair.getSelectedItem();
		txtRenderer = new TextRenderer(new Font("sansserif", Font.BOLD, 16));

		txtRenderer.beginRendering(1200, 700);
		txtRenderer.setColor(1.0f, 0.0f, 0.0f, 0.8f);
		txtRenderer.draw(text, 150, 650);
		txtRenderer.endRendering();

	}
	 public static void visualizeNavigation(GL2 gl) throws Exception
	    {
	    	List<Float> lineList;
	    	int stairFlag=0;
	    	List<NavigationPath> pathList=new ArrayList<NavigationPath>();
	    	int floorNow=0;
	    	String[] r_srcPointIn = null,r_destPointIn=null;
			List<Float> r_srcPointlist,r_destPointlist;
			//visualize source room
	    	String querySrc="Select st_astext(the_geom),floor_id from room where room_name='"+source.getSelectedItem().toString().toUpperCase().trim()+"'";
	    	Statement stmtSrc=conn.createStatement();
	    	ResultSet rsSrc=stmtSrc.executeQuery(querySrc);
	    	while(rsSrc.next())
	    	{
	    		r_srcPointIn = rsSrc.getString(1).replace("MULTIPOLYGON", "").replace("POLYGON", "").replace(",", " ").replace("(", "")
						.replace(")", "").split(" ");
				int r_floorNow = rsSrc.getInt(2);
				r_srcPointlist = new ArrayList<Float>();
				for (int i = 0; i < r_srcPointIn.length; i = i + 2) {
					float xPoint = Float.parseFloat(r_srcPointIn[i]);
					float yPoint = Float.parseFloat(r_srcPointIn[i + 1]);
					r_srcPointlist.add(xPoint);
					r_srcPointlist.add(yPoint);
				}
				gl.glEnable(GL2.GL_BLEND);
				gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
				gl.glColor4f(0f, 1f, 0f, 0.4f);

				gl.glBegin(GL2.GL_QUAD_STRIP);
				for (int j = 0; j < r_srcPointlist.size(); j = j + 2) {
					gl.glVertex3f(r_srcPointlist.get(j) / 10000.0f, (r_floorNow * 0.04f), r_srcPointlist.get(j + 1) / 10000.0f);
					gl.glVertex3f(r_srcPointlist.get(j) / 10000.0f, (r_floorNow * 0.04f) + 0.04f, r_srcPointlist.get(j + 1) / 10000.0f);
				}
				gl.glEnd();
				gl.glDisable(GL2.GL_BLEND);
				//gl.glFlush();
	    	}
	    	
	    	//visualize source room
	    	String queryDest="Select st_astext(the_geom),floor_id from room where room_name='"+destination.getSelectedItem().toString().toUpperCase().trim()+"'";
	    	Statement stmtDest=conn.createStatement();
	    	ResultSet rsDest=stmtDest.executeQuery(queryDest);
	    	while(rsDest.next())
	    	{
	    		r_destPointIn = rsDest.getString(1).replace("MULTIPOLYGON", "").replace("POLYGON", "").replace(",", " ").replace("(", "")
						.replace(")", "").split(" ");
				int r_floorNow = rsDest.getInt(2);
				r_destPointlist = new ArrayList<Float>();
				for (int i = 0; i < r_destPointIn.length; i = i + 2) {
					float xPoint = Float.parseFloat(r_destPointIn[i]);
					float yPoint = Float.parseFloat(r_destPointIn[i + 1]);
					r_destPointlist.add(xPoint);
					r_destPointlist.add(yPoint);
				}
				gl.glEnable(GL2.GL_BLEND);
				gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
				gl.glColor4f(0f, 1f, 0f, 0.4f);

				gl.glBegin(GL2.GL_QUAD_STRIP);
				for (int j = 0; j < r_destPointlist.size(); j = j + 2) {
					gl.glVertex3f(r_destPointlist.get(j) / 10000.0f, (r_floorNow * 0.04f), r_destPointlist.get(j + 1) / 10000.0f);
					gl.glVertex3f(r_destPointlist.get(j) / 10000.0f, (r_floorNow * 0.04f) + 0.04f, r_destPointlist.get(j + 1) / 10000.0f);
				}
				gl.glEnd();
				gl.glDisable(GL2.GL_BLEND);
				//gl.glFlush();
	    	}
	    	
	    	String query1="Select st_asText(the_geom),floor_id from path";
	    	Statement stmt1=conn.createStatement();
	    	ResultSet rs1=stmt1.executeQuery(query1);
	    	while(rs1.next())
	    	{
	    		String geom=rs1.getString(1);
	    		floorNow=rs1.getInt(2);
	    		if(geom.contains("POINT"))
	    		{	
	    			String cent=geom.replace("POINT", "").replace("(", "").replace(")", "");
	    			String centroid[]=cent.split(" ");
	    			gl.glBegin(GL2.GL_POINTS);
	    				gl.glVertex3f(Float.parseFloat(centroid[0]), floorNow*0.04f, Float.parseFloat(centroid[1]));
	    			gl.glEnd();
	    			 
	    		}
	    		if((geom.contains("LINESTRING"))||(geom.contains("MULTILINESTRING")))
	    		{
	    			String line=geom.replace("MULTILINESTRING","").replace("LINESTRING ","").replace("(","").replace(")", "").replace(",", " ");
	    			String lines[]=line.split(" ");
	    			lineList=new ArrayList<Float>();
	    			for (int i = 0; i < lines.length; i = i + 2) {
	                    float xPoint = Float.parseFloat(lines[i]);
	                    float yPoint = Float.parseFloat(lines[i + 1]);
	                    NavigationPath pathIn=new NavigationPath();
	                    pathIn.xPoint=xPoint;
	                    pathIn.yPoint=yPoint;
	                    pathIn.path_floor=floorNow;
	                    pathList.add(pathIn);
	                    if(i==0)
	                    {
	                    	if(stairFlag==1)
	                    	{
	                    		pathIn.xPoint=xPoint;
	                            pathIn.yPoint=yPoint;
	                            pathIn.path_floor=floorNow;
	                            pathList.add(pathIn);
	                    	}
	                    }
	                    if(i==lines.length-2)
	                    {
	                    	pathIn.xPoint=xPoint;
	                        pathIn.yPoint=yPoint;
	                        pathIn.path_floor=floorNow;
	                        pathList.add(pathIn);
	                        stairFlag=1;
	                    }
	    			}
	    		}
	    	}
	    	gl.glBegin(GL2.GL_LINES);
			gl.glClearColor(0, 0, 0, 0);
			gl.glColor3f(0.000f, 0.392f, 0.000f);
			gl.glLineWidth(0.01f);
			for(int j=0;j<pathList.size();j++)
	    	{
				NavigationPath pathIn1=pathList.get(j);
				//System.out.println("points:"+pathIn1.xPoint+" "+pathIn1.yPoint+" "+pathIn1.path_floor);
	    		gl.glVertex3f(pathIn1.xPoint / 10000.0f, pathIn1.path_floor * 0.04f, pathIn1.yPoint / 10000.0f);
	    	}
			//gl.glColor3f(1f, 1f, 1f);
			gl.glEnd();
			gl.glFlush();
	    	}

	  

	
}

class BoundsGeom {

	String geomValue;
	int floorValue;
}

class RoomGeom {

	double xMin, xMax, yMin, yMax;
	int floorValue;
	String roomName;
}
class NavigationPath
{
	float xPoint,yPoint;
	int path_floor;
}