package com.amrita.IIRMS.DB.Management;

/*
 * File Name        : SensorModeling.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Mukunthan R(Junior Research Fellow),Sindhya Kumari N(Junior Research Fellow),Dineshkumar E(Junior Research Fellow)
 * Last Modified    : the 18th of December, 2015
 * Purpose          : Class for Sensor Configuration & 2D visualisation of sensors.
 */

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.media.opengl.GL2;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.awt.GLJPanel;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.geom.AffineTransform;

import com.amrita.IIRMS.IIRMSSensorWindow;

public class SensorModeling {
	public static JTabbedPane tabbedPane; // here loadPanel becomes viewpanel
	public static JPanel viewPanel, configPanel, delPanel, locPanel, drawPanel, htPanel, namePanel;
	public static ArrayList<String> list_sensors = new ArrayList<String>();

	public static JFrame sensorFrame;

	public static JLabel stype, sname, sdim, scost, scoverage, slifetime, sopformat, sfloor, swall, sroom;
	public static JLabel del_sfloor, del_roomname, del_list;
	public static JLabel adim, agen_code, acost, acoverage, alifetime, aopformat, afloor, awall, aroom, athe_geom, anat,
			agen_code_exp, agen_code_info;
	public static JLabel asel_sensor, asen_id, alocation, acov_type, anew_type;
	public static JComboBox jcb_asel_sensor, jcb_alocation, jcb_nature, jcb_coverage_type;
	public static JTextField jtf_new_type, jtf_asen_id, jtf_gen_code;

	public static JList jl_list;
	public JComboBox jcb_delfloor, jcb_delroom;
	public JScrollPane scroll;
	String del_query;
	public static JTextField jtf_type, jtf_name, jtf_dim, jtf_cost, jtf_coverage, jtf_lifetime, jtf_opformat, jtf_floor,
			jtf_wall, jtf_room;
	public static JTextArea jta_geom;

	public static JButton sdisp, sremove, sadd, sdeploy, tk, aview, aview1;
	public static String[] str_senName, dd_type, dd_floor, dd_room;
	public JComboBox jcb_name, jcb_type;
	public static JComboBox jcb_floor;
	public JComboBox jcb_room;
	public JComboBox jcb_atype, jcb_afloor, jcb_aroom;

	public static Connection conn;
	public static ResultSet rs;
	public static Statement stmt;
	public static Connection dbConn;
	public JFrame fv;
	public JPanel jp;
	public GL2 gl;
	protected static int wall, door, window;

	public Container pane;
	public GLCanvas glcanvas;
	public GLJPanel gljpanel;

	int x_prev, y_prev;

	String dd_stype, dd_sname, dd_sfloor, dd_sroom;
	String m_dfloor, m_droom;

	// * generic sen for admin
	public JLabel atype, aname, acategory, aAppln, amode, aact, adata_param, aActive;
	public JLabel aMode;
	public JPanel addPanel, addPanel1, modifyPanel, amPanel, modifyPanel1;
	public JTextField jtf_acategory, jtf_aAppln, jtf_adata_param;
	public JComboBox jcb_aActive, jcb_aMode;
	public JButton generic_add;
	public JLabel mtype, mname, mcategory, mAppln, mmode, mact, mdata_param, mActive;
	public JLabel mMode, mnew_type;
	public JTextField jtf_mcategory, jtf_mAppln, jtf_mdata_param, jtf_mnew_type, jtf_mname;
	public JComboBox jcb_mActive, jcb_mMode;

	public JButton generic_modify;
	// *

	// * non admin user
	public JLabel na_type, na_gen_code, na_spec_code, na_spec_code_exp, na_spec_code_info, na_name, na_dim, na_price,
			na_range, na_cnty, na_cp, na_life;
	public JTextField jtf_na_name, jtf_na_gen_code, jtf_na_spec_code, jtf_na_dim, jtf_na_price, jtf_na_range, jtf_na_cp,
			jtf_na_life;
	public JComboBox jcb_naType, jcb_na_cnty;
	public JButton na_add;
	public JPanel am_na_panel;

	public JLabel nm_type, nm_name, nm_dim, nm_price, nm_range, nm_cnty, nm_cp, nm_life;
	public JTextField jtf_nm_name, jtf_nm_dim, jtf_nm_price, jtf_nm_range, jtf_nm_cp, jtf_nm_life;
	public static JComboBox jcb_nmType;
	public JComboBox jcb_nm_cnty;
	public static JComboBox jcb_nm_name;
	public JButton nm_modify;

	public JLabel cfg_name, cfg_id, cfg_floor, cfg_room, cfg_gen_code, cfg_spec_code, cfg_sensorId_info,
			cfg_sensorId_exp;
	public static JComboBox jcb_cfg_floor;
	public JComboBox jcb_cfg_id;
	public static JComboBox jcb_cfg_room;
	public JComboBox jcb_cfg_name;
	public JTextField jtf_cfg_id, jtf_cfg_spec_code, jtf_cfg_gen_code;
	public JButton cfg_config;

	public static JTextField pt_locn;
	public static JTextField position;

	public static JSlider js_zm, js_ht;
	public JPanel jsPanel;
	private URL url;
	private JLabel na_ereq;
	private JTextField jtf_na_ereq;
	private JLabel na_output;
	private JTextField jtf_na_output;
	private JLabel na_app;
	private JTextField jtf_na_app;
	private JLabel na_weight;
	private JTextField jtf_na_weight;
	private JLabel nm_app;
	private JTextField jtf_nm_app;
	private JLabel na_lweight;
	private JTextField jtf_na_w;
	private JLabel na_llength;
	private JTextField jtf_na_l;
	private JLabel na_lheight;
	private JTextField jtf_na_h;
	private JLabel na_lcasting;
	private JTextField jtf_na_c;
	private JLabel na_lcurrenttype;
	private JTextField jtf_na_ct;
	private JLabel na_lamp;
	private JTextField jtf_na_amp;
	private JLabel na_lv;
	private JLabel na_p;
	private JLabel na_mweight;
	private JTextField jtf_na_mweight;
	private JLabel na_mereq;
	private JLabel na_mlv;
	private JTextField jtf_na_mereq;
	private JLabel na_mlcurrenttype;
	private JTextField jtf_na_mct;
	private JLabel na_mlamp;
	private JTextField jtf_na_mamp;
	private JLabel na_mp;
	private JLabel na_mrange;
	private JTextField jtf_na_mrange;
	private JLabel na_moutput;
	private JTextField jtf_na_moutput;
	private JLabel na_mprice;
	private JLabel na_mlweight;
	private JTextField jtf_na_mw;
	private JLabel na_mllength;
	private JTextField jtf_na_ml;
	private JLabel na_mlheight;
	private JTextField jtf_na_mh;
	private JLabel na_mlcasting;
	private JTextField jtf_na_mc;
	private JButton generic_next;
	private JButton na_next;
	private JButton na_back;
	private JButton nm_next;
	private JButton nm_back;
	private JButton c_next;
	private JButton c_back;
	private JButton v_back;
	private JButton v_close;
	private ImageIcon helpIcon;
	

	public static float xoom = 0.8f;

	public static ArrayList<String> sen_generic_list = new ArrayList<String>();

	// *

	// @SuppressWarnings("unchecked")
	public SensorModeling() throws Exception {
		// Initialize
		Db_Connection();
		glcanvas = new GLCanvas();
		sensorFrame = new JFrame("IIRMS Sensor Configuration ");
		tabbedPane = new JTabbedPane();
		tabbedPane.setBackground(Color.white);
		viewPanel = new JPanel();
		viewPanel.setName("View Sensors");
		viewPanel.setLayout(null);

		amPanel = new JPanel();
		amPanel.setName("Add Generic Sensor Details");
		amPanel.setLayout(null);

		JPanel amPanel1 = new JPanel();
		amPanel1.setName("Add/Modify specific Sensor");
		amPanel1.setLayout(null);
		configPanel = new JPanel();
		configPanel.setName("Configure Sensor position");
		configPanel.setLayout(null);
		stype = new JLabel("Select the sensor type:");
		stype.setBounds(60, 10, 200, 20);
		jcb_type = new JComboBox(sensorTypes().toArray());
		jcb_type.setBounds(250, 10, 200, 20);
		sname = new JLabel(" Select the sensor name:");
		sname.setBounds(60, 40, 200, 20);
		jcb_name = new JComboBox();
		jcb_name.setBounds(250, 40, 200, 20);
		sfloor = new JLabel("Select the floor:");
		sfloor.setBounds(60, 70, 200, 20);
		jcb_floor = new JComboBox(sensorFloor().toArray());
		jcb_floor.setBounds(250, 70, 200, 20);
		sroom = new JLabel("Select the room name:");
		sroom.setBounds(60, 100, 200, 20);
		jcb_room = new JComboBox(sensorRoom().toArray());// sensorRoom(jcb_cfg_floor.toString().trim()).toArray());
		jcb_room.setBounds(250, 100, 200, 20);

		

		addPanel = new JPanel();
		addPanel.setLayout(null);
		addPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Add Generic Sensor"));
		addPanel.setBounds(10, 10, 550, 300);
		addPanel1 = new JPanel();
		addPanel1.setLayout(null);
		addPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Add Sensor"));
		addPanel1.setBounds(10, 10, 550, 600);
		modifyPanel = new JPanel();
		modifyPanel.setLayout(null);
		modifyPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Modify Generic Sensor"));
		modifyPanel.setBounds(610, 10, 550, 500);
		modifyPanel1 = new JPanel();
		modifyPanel1.setLayout(null);
		modifyPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Modify Sensor"));
		modifyPanel1.setBounds(610, 10, 550, 600);

		// add starts
		atype = new JLabel("Sensor Type:");
		atype.setBounds(60, 20, 200, 20);
		jcb_atype = new JComboBox(selectSensor().toArray());
		jcb_atype.setBounds(260, 20, 200, 20);
		anew_type = new JLabel("");
		anew_type.setBounds(60, 60, 200, 20);
		jtf_new_type = new JTextField(15);
		jtf_new_type.setBounds(260, 60, 200, 20);
		aname = new JLabel("Sensor Name:");
		aname.setBounds(60, 100, 200, 20);
		jtf_name = new JTextField(15);
		jtf_name.setBounds(260, 100, 200, 20);
		agen_code = new JLabel("Sensor Generic Code:");
		agen_code.setBounds(60, 140, 200, 20);
		agen_code_info = new JLabel("(Enter 3 letter code)");
		agen_code_info.setBounds(60, 155, 200, 20);
		agen_code_info.setFont(new Font("Serif", Font.ITALIC, 11));
		jtf_gen_code = new JTextField(15);
		jtf_gen_code.setBounds(260, 140, 200, 20);
		agen_code_exp = new JLabel("(eg:WFI for WIFI Router)");
		agen_code_exp.setFont(new Font("Serif", Font.ITALIC, 11));
		agen_code_exp.setBounds(260, 155, 200, 20);

		generic_add = new JButton("Add");
		generic_add.setBounds(240, 200, 100, 20);
		generic_next = new JButton("Next");
		generic_next.setBounds(350, 200, 100, 20);
		 URL url=new URL("http://172.17.9.60/html/pics/help.png");
         ImageIcon icon = new ImageIcon(url);
         Image helpImg = icon.getImage();
         helpImg=helpImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
         helpIcon=new ImageIcon(helpImg);
         JLabel lhelp=new JLabel(helpIcon);
         lhelp.setBounds(500, 20, 30, 30);
         lhelp.setToolTipText("<html><center>This form is to adding new Generic Sensor type and name.</center>"
         		+ "1.To add new sensor type, select new and provide type and sensor name,code.eg(Environment,Temperature sensor,ETM)."
                 + "<br/>2.If you want to add humiditysensor, select sensor type as Environment and provide code and click Add button."
                 + "<br/>3.Click Next button to add Sensor details.");
         addPanel.add(lhelp);
		generic_next.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex()+1);	
			}
		});

		jcb_atype.setSelectedItem("new");

		generic_add.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String stype = "";
				if (jcb_atype.getSelectedItem().toString().trim().equalsIgnoreCase("new")) {
					stype = jtf_new_type.getText().toString().trim();
				} else {
					stype = jcb_atype.getSelectedItem().toString().trim();
				}
				String sname = jtf_name.getText().toString().trim();
				String scode = jtf_gen_code.getText().toString().trim();

				try {
					if (stype.equals("") || sname.equals("")) {
						throw new EmptyException("Entry in 'Add Sensor' section cannot be empty.");
					}

					Statement stmt = dbConn.createStatement();

					String query = "insert into sensor_master values('" + scode + "','" + stype + "','" + sname + "')";
					stmt.executeUpdate(query);

					addPanel1.remove(jcb_naType);
					jcb_naType = new JComboBox(getGenericSensorType().toArray());
					jcb_naType.setBounds(260, 20, 200, 20);
					addPanel1.add(jcb_naType);

					jcb_atype.setSelectedIndex(0);
					jtf_new_type.setText("");
					jtf_name.setText("");
					jtf_gen_code.setText("");

					addPanel.remove(jcb_atype);
					jcb_atype = new JComboBox(selectSensor().toArray());
					jcb_atype.setBounds(260, 20, 200, 20);
					addPanel.add(jcb_atype);
					jcb_atype.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							if (jcb_atype.getSelectedItem().toString().trim().equalsIgnoreCase("new")) {
								jtf_new_type.setEnabled(true);
								anew_type.setEnabled(true);
								// anew_type.setVisible(true);
								// jtf_new_type.setVisible(true);
							} else {
								jtf_new_type.setEnabled(false);
								anew_type.setEnabled(false);
								// anew_type.setVisible(false);
								// jtf_new_type.setVisible(false);
							}
						}
					});

					JOptionPane.showMessageDialog(null, "Sensor " + sname + " has been added successfully.");
					// to update sensor category in modify panel

					addPanel1.remove(jcb_naType);
					jcb_naType = new JComboBox(getGenericSensorType().toArray());
					jcb_naType.setBounds(260, 20, 200, 20);
					addPanel1.add(jcb_naType);
					jcb_naType.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent e) {
							String stype = jcb_naType.getSelectedItem().toString().trim();
							// System.out.println("-->stype:" + stype);
							try {
								addPanel1.remove(jtf_na_gen_code);
								String code = getGenericSensorCodebyGenericName(stype).toString().trim();
								jtf_na_gen_code = new JTextField(15);
								jtf_na_gen_code.setBounds(260, 60, 200, 20);
								jtf_na_gen_code.setEnabled(false);
								jtf_na_gen_code.setText(code);
								// jtf_na_gen_code.setBackground(Color.lightGray);
								// jtf_na_gen_code.setForeground(Color.BLACK);
								addPanel1.add(jtf_na_gen_code);
							} catch (Exception e1) {
								e1.printStackTrace();
							}

						}
					});
				} catch (EmptyException ee) {
					JOptionPane.showMessageDialog(null, ee.getMessage());
				} catch (Exception ee) {
					ee.printStackTrace();
				}
				
			}
			 

		});

		jcb_atype.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (jcb_atype.getSelectedItem().toString().trim().equalsIgnoreCase("new")) {
					jtf_new_type.setEnabled(true);
					anew_type.setEnabled(true);
					// anew_type.setVisible(true);
					// jtf_new_type.setVisible(true);
				} else {
					jtf_new_type.setEnabled(false);
					anew_type.setEnabled(false);
					// anew_type.setVisible(false);
					// jtf_new_type.setVisible(false);
				}
			}
		});

		addPanel.add(atype);
		addPanel.add(jcb_atype);
		addPanel.add(anew_type);
		addPanel.add(jtf_new_type);
		addPanel.add(aname);
		addPanel.add(jtf_name);
		addPanel.add(agen_code);
		addPanel.add(agen_code_info);
		addPanel.add(jtf_gen_code);
		addPanel.add(agen_code_exp);

		addPanel.add(generic_add);
		addPanel.add(generic_next);
		// * add ends

		{// add and modify specific sensor
			na_type = new JLabel("Sensor Name: ");
			na_type.setBounds(60, 20, 200, 20);
			// generic field sensor_name->sensor_type for nonadmin.Since
			// non-admin sensor name is different..
			jcb_naType = new JComboBox(getGenericSensorType().toArray());
			jcb_naType.setBounds(260, 20, 200, 20);
			na_gen_code = new JLabel("Sensor Generic Code:");
			na_gen_code.setBounds(60, 60, 200, 20);
			jtf_na_gen_code = new JTextField(15);
			jtf_na_gen_code.setBounds(260, 60, 200, 20);
			jtf_na_gen_code.setEnabled(false);
			na_spec_code = new JLabel("Sensor specific code:");
			na_spec_code.setBounds(60, 100, 200, 20);
			na_spec_code_info = new JLabel("(Code starts with generic code)");
			na_spec_code_info.setBounds(60, 115, 200, 20);
			na_spec_code_info.setFont(new Font("Serif", Font.ITALIC, 11));
			jtf_na_spec_code = new JTextField(15);
			jtf_na_spec_code.setBounds(260, 100, 200, 20);
			na_spec_code_exp = new JLabel("(eg:WFI001 for WFI)");
			na_spec_code_exp.setBounds(260, 115, 200, 20);
			na_spec_code_exp.setFont(new Font("Serif", Font.ITALIC, 11));
			na_name = new JLabel("Specific sensor Name: ");
			na_name.setBounds(60, 140, 200, 20);
			jtf_na_name = new JTextField(15);
			jtf_na_name.setBounds(260, 140, 200, 20);
			na_dim = new JLabel("Unique Identifier: ");
			na_dim.setBounds(60, 180, 200, 20);
			jtf_na_dim = new JTextField(15);
			jtf_na_dim.setBounds(260, 180, 200, 20);
			na_app = new JLabel("Intended Application: ");
			na_app.setBounds(60, 220, 200, 20);
			jtf_na_app = new JTextField(15);
			jtf_na_app.setBounds(260, 220, 200, 20);
			na_price = new JLabel("Physical Properties");
			na_price.setBounds(60, 250, 220, 20);
			na_price.setFont(new Font("Serif", Font.BOLD, 14));
			na_lweight = new JLabel("Weight(Grams): ");
			na_lweight.setBounds(60, 280, 100, 20);
			jtf_na_w = new JTextField(15);
			jtf_na_w.setBounds(160, 280, 75, 20);
			na_llength = new JLabel("Length(Centimeters):");
			na_llength.setBounds(260, 280, 140, 20);
			jtf_na_l = new JTextField(15);
			jtf_na_l.setBounds(390, 280, 70, 20);
			na_lheight = new JLabel("Height(Centimeters): ");
			na_lheight.setBounds(260, 310, 140, 20);
			jtf_na_h = new JTextField(15);
			jtf_na_h.setBounds(390, 310, 70, 20);
			na_lcasting = new JLabel("Casting Material:");
			na_lcasting.setBounds(60, 310, 100, 20);
			jtf_na_c = new JTextField(15);
			jtf_na_c.setBounds(160, 310, 75, 20);
			JLabel dim_exp = new JLabel("eg:1cm*2cm*1cm[width*length*height]");
			dim_exp.setBounds(260, 275, 200, 20);
			dim_exp.setFont(new Font("Serif", Font.ITALIC, 11));
			na_weight = new JLabel("Weight: ");
			na_weight.setBounds(60, 300, 220, 20);
			jtf_na_weight = new JTextField(15);
			jtf_na_weight.setBounds(260, 300, 200, 20);
			na_ereq = new JLabel("Electrical Requirements ");
			na_ereq.setBounds(60, 340, 200, 20);
			na_ereq.setFont(new Font("Serif", Font.BOLD, 14));
			na_lv = new JLabel("Voltage(Volts): ");
			na_lv.setBounds(60, 370, 100, 20);
			jtf_na_ereq = new JTextField(15);
			jtf_na_ereq.setBounds(160, 370, 70, 20);
			na_lcurrenttype = new JLabel("Current Type(AC/DC):");
			na_lcurrenttype.setBounds(260, 370, 140, 20);
			jtf_na_ct = new JTextField(15);
			jtf_na_ct.setBounds(390, 370, 70, 20);
			na_lamp = new JLabel("Ampere Range(Milliampere):");
			na_lamp.setBounds(60, 400, 180, 20);
			jtf_na_amp = new JTextField(15);
			jtf_na_amp.setBounds(230, 400, 75, 20);
			JLabel ereq_exp = new JLabel("eg:volts[12v DC 500mA]");
			ereq_exp.setFont(new Font("Serif", Font.ITALIC, 11));
			ereq_exp.setBounds(260, 355, 200, 20);
			na_p = new JLabel("Parameters");
			na_p.setBounds(60, 440, 200, 20);
			na_p.setFont(new Font("Serif", Font.BOLD, 14));
			na_range = new JLabel("Range of Influence(Meters): ");
			na_range.setBounds(60, 480, 200, 20);
			jtf_na_range = new JTextField(15);
			jtf_na_range.setBounds(260, 480, 200, 20);

			na_output = new JLabel("Output(Measurement Unit): ");
			na_output.setBounds(60, 520, 200, 20);
			jtf_na_output = new JTextField(15);
			jtf_na_output.setBounds(260, 520, 200, 20);
			na_add = new JButton("Add");
			na_add.setBounds(240, 560, 100, 20);
			na_next = new JButton("Next");
			na_next.setBounds(350, 560, 100, 20);
			
	         JLabel lhelp1=new JLabel(helpIcon);
	         lhelp1.setBounds(500, 20, 30, 30);
	         lhelp1.setToolTipText("<html><center>This form is to Adding Specific Sensors and its details.</center>"
	         		+ "1.To add New sensor, select sensor name and provide name and code for specific sensor.eg:(Temperature sensor,Honeywelltemp30,ETM,ETM001) "
	                 + "<br/>2.Then provide Uniqueidentifier,application,Physicalproperties<br/>such as,weight(Grams),length(cm),height(cm),casting_material(materialname).eg:(ETM:Honeywelltemp30,monitoring,100,15,5,plastic) "
	                 + "<br/>3.Provide the Electrical Requrements such as voltage(Volts),currenttype(AC/DC),ampererange(milliampere).eg:(12,DC,600)"
	                 +"<br/>4.Provide the parameters such as Range of Influence(meters),output measurement unit(name).eg:(2, celcius)"+
	                 "<br/>4.Once you filled the details, then click Add button"
	                 +"<br/>5.User can navigate the tab by clicking Next/Back button");
	         
	         addPanel1.add(lhelp1);
			na_next.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex()+1);	
				}
			});
			na_back = new JButton("Back");
			na_back.setBounds(130, 560, 100, 20);
			na_back.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex()-1);	
				}
			});

			jcb_naType.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String stype = jcb_naType.getSelectedItem().toString().trim();
					// System.out.println("-->stype:" + stype);
					try {
						addPanel1.remove(jtf_na_gen_code);
						String code = getGenericSensorCodebyGenericName(stype).toString().trim();
						jtf_na_gen_code = new JTextField(15);
						jtf_na_gen_code.setBounds(260, 60, 200, 20);
						jtf_na_gen_code.setEnabled(false);
						jtf_na_gen_code.setText(code);
						// jtf_na_gen_code.setBackground(Color.lightGray);
						// jtf_na_gen_code.setForeground(Color.BLACK);
						addPanel1.add(jtf_na_gen_code);
					} catch (Exception e1) {
						e1.printStackTrace();
					}

				}
			});

			na_add.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String genericcode = jtf_na_gen_code.getText().toString().trim();
					String spec_code = jtf_na_spec_code.getText().toString().trim();
					String sname = jtf_na_name.getText().toString().trim();
					String uid = jtf_na_dim.getText().toString().trim();
					String applic = jtf_na_app.getText().toString().trim();
					String weight = jtf_na_w.getText().toString().trim();
					String length = jtf_na_l.getText().toString().trim();
					String height = jtf_na_h.getText().toString().trim();
					String castingmaterial = jtf_na_c.getText().toString().trim();
					String voltage = jtf_na_ereq.getText().toString().trim();
					String currenttype = jtf_na_ct.getText().toString().trim();
					String amp_range = jtf_na_amp.getText().toString().trim();
					String range = jtf_na_range.getText().toString().trim();
					String output = jtf_na_output.getText().toString().trim();

					try {
						if (stype.equals("") || sname.equals("") || uid.equals("") || applic.equals("")
								|| weight.equals("") || length.equals("") || length.equals("") || height.equals("")
								|| castingmaterial.equals("") || voltage.equals("") || currenttype.equals("")
								|| amp_range.equals("") || range.equals("") || output.equals("") || genericcode == null
								|| sname == null || uid == null || applic == null || weight == null || length == null
								|| height == null || castingmaterial == null || voltage == null || output == null
								|| currenttype == null || amp_range == null || range == null) {
							throw new EmptyException("Entry in 'Add Sensor' section cannot be empty.\n"
									+ "If a particular entry is not applicable, please mention it as NA. ");
						}

						Statement stmt = dbConn.createStatement();
						System.out.println("length:" + " scode:" + spec_code.length() + " stype:" + genericcode.length()
								+ " sname:" + sname.length());
						String query = "insert into sensor_details values('" + spec_code + "','" + genericcode + "','"
								+ sname + "','" + uid + "','" + applic + "','" + weight + "','" + length + "','"
								+ height + "','" + castingmaterial + "','" + voltage + "','" + currenttype + "','"
								+ amp_range + "','" + range + "'" + ",'" + output + "')";
						stmt.executeUpdate(query);
						// loading details in configuration panel
						configPanel.remove(jcb_cfg_name);
						jcb_cfg_name = new JComboBox(getSensorName().toArray());
						jcb_cfg_name.setBounds(200, 20, 200, 20);
						configPanel.add(jcb_cfg_name);
						jcb_cfg_name.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								String sname = jcb_cfg_name.getSelectedItem().toString().trim();
								try {
									configPanel.remove(jtf_cfg_spec_code);
									String spec_code = getSpecificSensorCodeByName(sname).toString().trim();
									jtf_cfg_spec_code = new JTextField(15);
									jtf_cfg_spec_code.setBounds(200, 100, 200, 20);
									jtf_cfg_spec_code.setEnabled(false);
									jtf_cfg_spec_code.setText(spec_code);
									configPanel.add(jtf_cfg_spec_code);

									configPanel.remove(jtf_cfg_gen_code);
									String gen_code = getGenericSensorCodeByName(sname).toString().trim();
									jtf_cfg_gen_code = new JTextField(15);
									jtf_cfg_gen_code.setBounds(200, 60, 200, 20);
									jtf_cfg_gen_code.setEnabled(false);
									jtf_cfg_gen_code.setText(gen_code);
									configPanel.add(jtf_cfg_gen_code);

								} catch (Exception e2) {
									e2.printStackTrace();
								}
							}
						});

						jcb_naType.setSelectedIndex(0);
						jtf_na_gen_code.setText("");
						jtf_na_spec_code.setText("");
						jtf_na_name.setText("");
						jtf_na_dim.setText("");
						jtf_na_app.setText("");
						jtf_na_w.setText("");
						jtf_na_l.setText("");
						jtf_na_h.setText("");
						jtf_na_c.setText("");
						jtf_na_ereq.setText("");
						jtf_na_ct.setText("");
						jtf_na_amp.setText("");
						jtf_na_range.setText("");
						jtf_na_output.setText("");

						JOptionPane.showMessageDialog(null, sname + " has been added successfully.");
						modifyPanel1.remove(jcb_nmType);
						jcb_nmType = new JComboBox(getGenericSensorType().toArray());// new
						// String[]{"RF Sensor","Temperature Sensor"});
						jcb_nmType.setBounds(260, 20, 200, 20);
						modifyPanel1.add(jcb_nmType);
						jcb_nmType.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {
								String stype = jcb_nmType.getSelectedItem().toString().trim();
								// System.out.println("-->stype:" + stype);
								try {
									modifyPanel1.remove(jcb_nm_name);
									jcb_nm_name = new JComboBox(getSensorNamebytype(stype).toArray());
									jcb_nm_name.setBounds(260, 60, 200, 20);
									modifyPanel1.add(jcb_nm_name);
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}

							}
						});
					} catch (EmptyException ee) {
						JOptionPane.showMessageDialog(null, ee.getMessage());
					} catch (Exception ee) {
						ee.printStackTrace();
					}
					
				}

			});

			addPanel1.add(na_type);
			addPanel1.add(jcb_naType);
			addPanel1.add(na_spec_code);
			addPanel1.add(na_gen_code);
			addPanel1.add(jtf_na_gen_code);
			addPanel1.add(jtf_na_spec_code);
			addPanel1.add(na_spec_code_info);
			addPanel1.add(na_spec_code_exp);
			addPanel1.add(na_name);
			addPanel1.add(jtf_na_name);
			addPanel1.add(na_dim);
			addPanel1.add(jtf_na_dim);
			addPanel1.add(na_price);
			// addPanel1.add(jtf_na_price);
			addPanel1.add(na_range);
			addPanel1.add(jtf_na_range);
			addPanel1.add(na_output);
			addPanel1.add(jtf_na_output);
			addPanel1.add(na_ereq);
			addPanel1.add(jtf_na_ereq);
			addPanel1.add(na_app);
			addPanel1.add(jtf_na_app);
			// addPanel1.add(na_weight);
			// addPanel1.add(jtf_na_weight);
			// addPanel1.add(ereq_exp);
			// addPanel1.add(dim_exp);
			addPanel1.add(na_lweight);
			addPanel1.add(jtf_na_w);
			addPanel1.add(na_llength);
			addPanel1.add(jtf_na_l);
			addPanel1.add(na_lheight);
			addPanel1.add(jtf_na_h);
			addPanel1.add(na_lcasting);
			addPanel1.add(na_lv);
			addPanel1.add(jtf_na_c);
			addPanel1.add(na_lcurrenttype);
			addPanel1.add(jtf_na_ct);
			addPanel1.add(na_lamp);
			addPanel1.add(jtf_na_amp);
			addPanel1.add(na_p);

			addPanel1.add(na_add);
			addPanel1.add(na_next);
			addPanel1.add(na_back);

			// /////* specific sensor modify
			nm_type = new JLabel("Specify Sensor: ");
			nm_type.setBounds(60, 20, 200, 20);
			jcb_nmType = new JComboBox(getGenericSensorType().toArray());// new
																			// String[]{"RF
																			// Sensor","Temperature
																			// Sensor"});
			jcb_nmType.setBounds(260, 20, 200, 20);
			nm_name = new JLabel("Specific Sensor Name: ");
			nm_name.setBounds(60, 60, 200, 20);
			jcb_nm_name = new JComboBox();
			jcb_nm_name.setBounds(260, 60, 200, 20);
			aview1 = new JButton("View ");
			aview1.setBounds(240, 100, 75, 20);
			nm_dim = new JLabel("Unique Identifier: ");
			nm_dim.setBounds(60, 180, 200, 20);
			jtf_nm_dim = new JTextField(15);
			jtf_nm_dim.setBounds(260, 180, 200, 20);
			nm_app = new JLabel("Intended Application: ");
			nm_app.setBounds(60, 220, 200, 20);
			jtf_nm_app = new JTextField(15);
			jtf_nm_app.setBounds(260, 220, 200, 20);
			na_mprice = new JLabel("Physical Properties");
			na_mprice.setBounds(60, 250, 220, 20);
			na_mprice.setFont(new Font("Serif", Font.BOLD, 14));
			na_mlweight = new JLabel("Weight(Grams): ");
			na_mlweight.setBounds(60, 280, 100, 20);
			jtf_na_mw = new JTextField(15);
			jtf_na_mw.setBounds(160, 280, 75, 20);
			na_mllength = new JLabel("Length(Centimeters):");
			na_mllength.setBounds(260, 280, 140, 20);
			jtf_na_ml = new JTextField(15);
			jtf_na_ml.setBounds(390, 280, 70, 20);
			na_mlheight = new JLabel("Height(Centimeters): ");
			na_mlheight.setBounds(260, 310, 140, 20);
			jtf_na_mh = new JTextField(15);
			jtf_na_mh.setBounds(390, 310, 70, 20);
			na_mlcasting = new JLabel("Casting Material:");
			na_mlcasting.setBounds(60, 310, 100, 20);
			jtf_na_mc = new JTextField(15);
			jtf_na_mc.setBounds(160, 310, 75, 20);

			na_mweight = new JLabel("Weight: ");
			na_mweight.setBounds(60, 300, 220, 20);
			jtf_na_mweight = new JTextField(15);
			jtf_na_mweight.setBounds(260, 300, 200, 20);
			na_mereq = new JLabel("Electrical Requirements ");
			na_mereq.setBounds(60, 340, 200, 20);
			na_mereq.setFont(new Font("Serif", Font.BOLD, 14));
			na_mlv = new JLabel("Voltage(Volts): ");
			na_mlv.setBounds(60, 370, 100, 20);
			jtf_na_mereq = new JTextField(15);
			jtf_na_mereq.setBounds(160, 370, 70, 20);
			na_mlcurrenttype = new JLabel("Current Type(AC/DC):");
			na_mlcurrenttype.setBounds(260, 370, 140, 20);
			jtf_na_mct = new JTextField(15);
			jtf_na_mct.setBounds(390, 370, 70, 20);
			na_mlamp = new JLabel("Ampere Range(Milliampere):");
			na_mlamp.setBounds(60, 400, 180, 20);
			jtf_na_mamp = new JTextField(15);
			jtf_na_mamp.setBounds(230, 400, 75, 20);

			na_mp = new JLabel("Parameters");
			na_mp.setBounds(60, 440, 200, 20);
			na_mp.setFont(new Font("Serif", Font.BOLD, 14));
			na_mrange = new JLabel("Range of Influence(Meters): ");
			na_mrange.setBounds(60, 480, 200, 20);
			jtf_na_mrange = new JTextField(15);
			jtf_na_mrange.setBounds(260, 480, 200, 20);

			na_moutput = new JLabel("Output(Measurement Unit): ");
			na_moutput.setBounds(60, 520, 200, 20);
			jtf_na_moutput = new JTextField(15);
			jtf_na_moutput.setBounds(260, 520, 200, 20);

			nm_modify = new JButton("Modify");
			nm_modify.setBounds(240, 560, 100, 20);
			nm_next = new JButton("Next");
			nm_next.setBounds(350, 560, 100, 20);
			JLabel lhelp2=new JLabel(helpIcon);
	         lhelp2.setBounds(500, 20, 30, 30);
	         lhelp2.setToolTipText("<html><center>This form is to Modifiy Specific Sensors and its details.</center>"
	         		+ "1.To Modify sensor details, select specific sensor name and click View Button "
	                 + "<br/>2.Then modifiy the field in the form."+
	                 "<br/>3.Once you Modified the details, then click Modifiy button"
	                 +"<br/>4.User can navigate the tab by clicking Next/Back button");
	         
	         modifyPanel1.add(lhelp2);
			nm_next.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex()+1);	
				}
			});
			nm_back = new JButton("Back");
			nm_back.setBounds(130, 560, 100, 20);
			nm_back.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex()-1);	
				}
			});
			

			aview1.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String scode = null;
					String stype = jcb_nmType.getSelectedItem().toString().trim();
					String sname = jcb_nm_name.getSelectedItem().toString().trim();
					// System.out.print("sensorname:" + sname);
					String suid = "";
					String sapplic = "";
					String sweight = "";
					String slength = "";
					String sheight = "";
					String scastingmaterial = "";
					String svoltage = "";
					String scurrenttype = "";
					String samp_range = "";
					String srange = "";
					String soutput = "";

					try {

						String query1 = "select sensor_generic_code from sensor_master where sensor_generic_name like '%"
								+ stype + "%'";
						Statement stmt1 = dbConn.createStatement();
						ResultSet rs1 = stmt1.executeQuery(query1);
						while (rs1.next()) {
							scode = rs1.getString(1);
						}

						Statement stmt = dbConn.createStatement();
						String query = "select * from " + "sensor_details where sensor_generic_code like '%" + scode
								+ "%' and sensor_name like '%" + sname + "%'";
						ResultSet rs = stmt.executeQuery(query);
						while (rs.next()) {
							suid = rs.getString(4);
							sapplic = rs.getString(5);
							sweight = rs.getString(6);
							slength = rs.getString(7);
							sheight = rs.getString(8);
							scastingmaterial = rs.getString(9).toString().trim();
							svoltage = rs.getString(10);
							scurrenttype = rs.getString(11).toString().trim();
							samp_range = rs.getString(12);
							srange = rs.getString(13);
							soutput = rs.getString(14).toString().trim();

						}
						jcb_nm_name.setSelectedItem(sname);
						jtf_nm_dim.setText(suid);
						jtf_nm_app.setText(sapplic);
						jtf_na_mw.setText(sweight);
						jtf_na_ml.setText(slength);
						jtf_na_mh.setText(sheight);
						jtf_na_mc.setText(scastingmaterial);
						jtf_na_mereq.setText(svoltage);
						jtf_na_mct.setText(scurrenttype);
						jtf_na_mamp.setText(samp_range);
						jtf_na_mrange.setText(srange);
						jtf_na_moutput.setText(soutput);

					} catch (Exception ee) {
						ee.printStackTrace();
					}

				}
			});

			// /////* non admin modify
			jcb_nmType.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String stype = jcb_nmType.getSelectedItem().toString().trim();
					// System.out.println("-->stype:" + stype);
					try {
						modifyPanel1.remove(jcb_nm_name);
						jcb_nm_name = new JComboBox(getSensorNamebytype(stype).toArray());
						jcb_nm_name.setBounds(260, 60, 200, 20);
						modifyPanel1.add(jcb_nm_name);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			});

			nm_modify.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String stype = jcb_nmType.getSelectedItem().toString().trim();
					String sname = jcb_nm_name.getSelectedItem().toString().trim();
					String suid = jtf_nm_dim.getText().toString().trim();
					String sapplic = jtf_nm_app.getText().toString().trim();
					String sweight = jtf_na_mw.getText().toString().trim();
					String slength = jtf_na_ml.getText().toString().trim();
					String sheight = jtf_na_mh.getText().toString().trim();
					String scastingmaterial = jtf_na_mc.getText().toString().trim();
					String svoltage = jtf_na_mereq.getText().toString().trim();
					String scurrenttype = jtf_na_mct.getText().toString().trim();
					String samp_range = jtf_na_mamp.getText().toString().trim();
					String srange = jtf_na_mrange.getText().toString().trim();
					String soutput = jtf_na_moutput.getText().toString().trim();
					try {
						if (stype.equals("") || sname.equals("") || suid.equals("") || sapplic.equals("")
								|| sweight.equals("") || slength.equals("") || sheight.equals("")
								|| scastingmaterial.equals("") || svoltage.equals("") || scurrenttype.equals("")
								|| samp_range.equals("") || srange.equals("") || soutput.equals("") || stype == null
								|| sname == null || sweight == null || slength == null || sheight == null
								|| scastingmaterial == null || svoltage == null || soutput == null
								|| scurrenttype == null || samp_range == null || srange == null) {
							throw new EmptyException("Entry in 'Modify Sensor' section cannot be empty.\n"
									+ "If a particular entry is not applicable, please mention it as NA. ");
						}

						Statement stmt = dbConn.createStatement();
						String query = "update sensor_details set sensor_name='" + sname + "',unique_identifier='"
								+ suid + "',intended_application='" + sapplic + "',weight='" + sweight + "',length='"
								+ slength + "',height='" + sheight + "',casting_material='" + scastingmaterial
								+ "',voltage='" + svoltage + "',current_type='" + scurrenttype + "',amp_range='"
								+ samp_range + "',range_influence='" + srange + "',output='" + soutput
								+ "' where sensor_generic_code='" + stype + "'";
						stmt.executeUpdate(query);

						jcb_nmType.setSelectedIndex(0);
						jcb_nm_name.setSelectedIndex(0);
						jtf_nm_app.setText("");
						jtf_nm_dim.setText("");
						jtf_na_mw.setText("");
						jtf_na_ml.setText("");
						jtf_na_mh.setText("");
						jtf_na_mc.setText("");
						jtf_na_mereq.setText("");
						jtf_na_mct.setText("");
						jtf_na_mamp.setText("");
						jtf_na_mrange.setText("");
						jtf_na_moutput.setText("");

						JOptionPane.showMessageDialog(null, "" + sname + " has been modified successfully.");
					} catch (EmptyException ee) {
						JOptionPane.showMessageDialog(null, ee.getMessage());
					} catch (Exception ee) {
						ee.printStackTrace();
					}
					
				}

			});

			modifyPanel1.add(nm_type);
			modifyPanel1.add(jcb_nmType);
			modifyPanel1.add(nm_name);
			modifyPanel1.add(jcb_nm_name);
			modifyPanel1.add(aview1);
			modifyPanel1.add(nm_app);
			modifyPanel1.add(jtf_nm_app);
			modifyPanel1.add(nm_dim);
			modifyPanel1.add(jtf_nm_dim);
			modifyPanel1.add(na_mprice);
			// modifyPanel1.add(jtf_nm_price);
			modifyPanel1.add(na_mrange);
			modifyPanel1.add(jtf_na_mrange);
			modifyPanel1.add(na_moutput);
			modifyPanel1.add(jtf_na_moutput);
			modifyPanel1.add(na_mereq);
			modifyPanel1.add(jtf_na_mereq);
			// modifyPanel1.add(nm_weight);
			// modifyPanel1.add(jtf_nm_weight);
			modifyPanel1.add(nm_modify);
			modifyPanel1.add(na_mlweight);
			modifyPanel1.add(jtf_na_mw);
			modifyPanel1.add(na_mllength);
			modifyPanel1.add(jtf_na_ml);
			modifyPanel1.add(na_mlheight);
			modifyPanel1.add(jtf_na_mh);
			modifyPanel1.add(na_mlcasting);
			modifyPanel1.add(na_mlv);
			modifyPanel1.add(jtf_na_mc);
			modifyPanel1.add(na_mlcurrenttype);
			modifyPanel1.add(jtf_na_mct);
			modifyPanel1.add(na_mlamp);
			modifyPanel1.add(jtf_na_mamp);
			modifyPanel1.add(na_mp);
			modifyPanel1.add(nm_next);
			modifyPanel1.add(nm_back);
//configuration
			
			cfg_name = new JLabel("Sensor Name:");
			cfg_name.setBounds(40, 20, 200, 20);
			jcb_cfg_name = new JComboBox(getSensorName().toArray());
			jcb_cfg_name.setBounds(200, 20, 200, 20);
			cfg_gen_code = new JLabel("Sensor Generic code");
			cfg_gen_code.setBounds(40, 60, 200, 20);
			jtf_cfg_gen_code = new JTextField(15);
			jtf_cfg_gen_code.setBounds(200, 60, 200, 20);
			jtf_cfg_gen_code.setEnabled(false);
			cfg_spec_code = new JLabel("Sensor specific code:");
			cfg_spec_code.setBounds(40, 100, 200, 20);
			jtf_cfg_spec_code = new JTextField(15);
			jtf_cfg_spec_code.setBounds(200, 100, 200, 20);
			jtf_cfg_spec_code.setEnabled(false);
			cfg_id = new JLabel("Sensor ID: ");
			cfg_id.setBounds(40, 140, 200, 20);
			jtf_cfg_id = new JTextField(15);
			jtf_cfg_id.setBounds(200, 140, 200, 20);
			cfg_sensorId_info = new JLabel("Code starts with specific code");
			cfg_sensorId_info.setBounds(40, 155, 200, 20);
			cfg_sensorId_info.setFont(new Font("Serif", Font.ITALIC, 11));
			cfg_sensorId_exp = new JLabel("(eg: WF001_1,WF001_2 for WFI)");
			cfg_sensorId_exp.setBounds(200, 155, 200, 20);
			cfg_sensorId_exp.setFont(new Font("Serif", Font.ITALIC, 11));
			cfg_floor = new JLabel("Floor no: ");
			cfg_floor.setBounds(40, 180, 200, 20);
			jcb_cfg_floor = new JComboBox(sensorFloor().toArray());
			jcb_cfg_floor.setBounds(200, 180, 200, 20);
			cfg_room = new JLabel("Room Name:");
			cfg_room.setBounds(40, 220, 200, 20);
			jcb_cfg_room = new JComboBox(sensorRoomByFloor().toArray());// sensorRoom(jcb_cfg_floor.toString().trim()).toArray());
			jcb_cfg_room.setBounds(200, 220, 200, 20);
			JButton view = new JButton("Display");
			view.setBounds(430, 220, 120, 20);
			JLabel lhelp3=new JLabel(helpIcon);
	         lhelp3.setBounds(500, 20, 30, 30);
	         lhelp3.setToolTipText("<html><center>This form is to configuring the sensor into Building.</center>"
	         		+ "1.To Configure sensor, select specific sensor name and provide the sensorid(Unique) for Every Instance "
	                 + "<br/>2.Then select floorid,roomname to Deploy."+
	                 "<br/>3.Click Display Button to set position ,<br>by click position in highlighted area on the rightside of screen, then click Configure button."
	                 +"<br/>4.User can navigate the tab by clicking Next/Back button");
	         
	         configPanel.add(lhelp3);
			configPanel.add(view);

			JLabel locn = new JLabel("Location:");
			locn.setBounds(40, 260, 200, 20);
			pt_locn = new JTextField(15);
			pt_locn.setBounds(200, 260, 200, 20);
			pt_locn.setEditable(false);
			cfg_config = new JButton("Configure");
			cfg_config.setBounds(210, 500, 100, 20);
			cfg_config.setEnabled(false);
			position = new JTextField(15);
			position.setBounds(200, 540, 150, 20);
			c_next = new JButton("Next");
			c_next.setBounds(320, 500, 100, 20);
			c_next.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex()+1);	
				}
			});
			c_back = new JButton("Back");
			c_back.setBounds(100, 500, 100, 20);
			c_back.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex()-1);	
				}
			});

			jcb_cfg_name.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String sname = jcb_cfg_name.getSelectedItem().toString().trim();
					try {
						configPanel.remove(jtf_cfg_spec_code);
						String spec_code = getSpecificSensorCodeByName(sname).toString().trim();
						jtf_cfg_spec_code = new JTextField(15);
						jtf_cfg_spec_code.setBounds(200, 100, 200, 20);
						jtf_cfg_spec_code.setEnabled(false);
						jtf_cfg_spec_code.setText(spec_code);
						configPanel.add(jtf_cfg_spec_code);

						configPanel.remove(jtf_cfg_gen_code);
						String gen_code = getGenericSensorCodeByName(sname).toString().trim();
						jtf_cfg_gen_code = new JTextField(15);
						jtf_cfg_gen_code.setBounds(200, 60, 200, 20);
						jtf_cfg_gen_code.setEnabled(false);
						jtf_cfg_gen_code.setText(gen_code);
						configPanel.add(jtf_cfg_gen_code);

					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			});

			jcb_cfg_floor.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						configPanel.remove(jcb_cfg_room);
						jcb_cfg_room = new JComboBox(sensorRoomByFloor().toArray());
						jcb_cfg_room.setBounds(200, 220, 200, 20);
						configPanel.add(jcb_cfg_room);
					} catch (Exception ee) {
						ee.printStackTrace();
					}
				}

			});

			view.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {

					cfg_config.setEnabled(true);
					pt_locn.setText("");
					position.setText("");

					locPanel.removeAll();
					locPanel.invalidate();
					locPanel.repaint();
					try {

						paintPanel.array_pts.clear();
						paintPanel.room_disp.clear();
						paintPanel.room_points.clear();

						paintPanel.position_pts.clear();

						Statement stmt = dbConn.createStatement();
						int floor_id = Integer.parseInt(jcb_cfg_floor.getSelectedItem().toString().trim());
						String room_name = jcb_cfg_room.getSelectedItem().toString().trim();

						ArrayList<Float> pts = new ArrayList<Float>();
						ArrayList<Float> r_pointList = new ArrayList<Float>();
						Polygon p;

						String floor_query = "select room_name,floor_id,st_astext(the_geom) from room where floor_id="
								+ floor_id + "";
						ResultSet rs = stmt.executeQuery(floor_query);
						int ct = 1;
						while (rs.next()) {
							p = new Polygon();
							String[] r_pointIn = rs.getString(3).replace("MULTIPOLYGON", "").replace("POLYGON", "")
									.replace(",", " ").replace("(", "").replace(")", "").split(" ");
							float xp1 = 0.0f, yp1 = 0.0f;
							float[] arr = new float[r_pointIn.length];
							for (int i = 0; i < r_pointIn.length; i = i + 2) {
								float xPoint = Float.parseFloat(r_pointIn[i]);
								float yPoint = Float.parseFloat(r_pointIn[i + 1]);

								xp1 = xPoint;
								yp1 = yPoint;
								r_pointList.add(xp1);
								r_pointList.add(yp1);
								p.addPoint((int) (xp1 * 0.8), (int) (yp1 * 0.8));//
							}
							paintPanel.poly_pts.add(p);
							p.reset();

							for (int i = 0; i < arr.length; i++) {
								arr[i] = r_pointList.get(i);
								// System.out.println("at adding:" + arr[i]);
							}

							paintPanel.array_pts.add(arr);
							ArrayList<Float> xpt_al = new ArrayList<Float>();
							ArrayList<Float> ypt_al = new ArrayList<Float>();

							for (int j = 0; j < r_pointList.size(); j += 2) {
								xpt_al.add(r_pointList.get(j));
								ypt_al.add(r_pointList.get(j + 1));
							}
							java.util.HashSet<Float> xpt_hs = new java.util.HashSet<Float>(xpt_al);

							xpt_al.clear();
							xpt_al.addAll(xpt_hs);

							java.util.HashSet<Float> ypt_hs = new java.util.HashSet<Float>(ypt_al);

							ypt_al.clear();
							ypt_al.addAll(ypt_hs);

							Collections.sort(xpt_al);
							Collections.sort(ypt_al);

							Float xmin = xpt_al.get(0), ymin = ypt_al.get(0), xmax = xpt_al.get(xpt_al.size() - 1),
									ymax = ypt_al.get(ypt_al.size() - 1);

							paintPanel.room_points.addAll(r_pointList);
							r_pointList.clear();
							paintPanel.room_disp.add(rs.getString(1));//
							// System.out.println(" Room name:::" +
							// rs.getString(1) + " size: room_points: " +
							// paintPanel.room_points.size());

							ct++;
						}

						Statement stmt2 = dbConn.createStatement();
						String query2 = "select st_astext(the_geom) from floor where " + "floor_id=" + floor_id;// query2
																												// //
																												// record
						ResultSet rs2 = stmt2.executeQuery(query2);

						while (rs2.next()) {
							String[] f_pointIn = rs2.getString(1).replace("POLYGON", "").replace("MULTILINESTRING", "")
									.replace("LINESTRING", "").replace(",", " ").replace("(", "").replace(")", "")
									.split(" ");
							float xp1 = 0.0f, yp1 = 0.0f;
							ArrayList<Float> f_pointList = new ArrayList<Float>();
							for (int i = 0; i < f_pointIn.length; i = i + 2) {
								float xPoint = Float.parseFloat(f_pointIn[i]);
								float yPoint = Float.parseFloat(f_pointIn[i + 1]);
								xp1 = xPoint;
								yp1 = yPoint;

								f_pointList.add(xp1);
								f_pointList.add(yp1);
							}

							ArrayList<Float> xpt_al = new ArrayList<Float>();
							ArrayList<Float> ypt_al = new ArrayList<Float>();

							for (int j = 0; j < f_pointList.size(); j += 2) {
								xpt_al.add(f_pointList.get(j));
								ypt_al.add(f_pointList.get(j + 1));
							}
							java.util.HashSet<Float> xpt_hs = new java.util.HashSet<Float>(xpt_al);

							xpt_al.clear();
							xpt_al.addAll(xpt_hs);

							java.util.HashSet<Float> ypt_hs = new java.util.HashSet<Float>(ypt_al);

							ypt_al.clear();
							ypt_al.addAll(ypt_hs);

							Collections.sort(xpt_al);
							Collections.sort(ypt_al);

							Float xmin = xpt_al.get(0), ymin = ypt_al.get(0), xmax = xpt_al.get(xpt_al.size() - 1),
									ymax = ypt_al.get(ypt_al.size() - 1);

							paintPanel.floor_xmax = xmax;
							paintPanel.floor_ymax = ymax;
							paintPanel.floor_xmin = xmin;
							paintPanel.floor_ymin = ymin;
							f_pointList.clear();

						}
						locPanel = new paintPanel();
						locPanel.setBackground(Color.white);
						Rectangle original_dim = sensorFrame.getBounds();
						sensorFrame.setBounds(0, 0, 100, 100);

					} catch (Exception ee) {
						ee.printStackTrace();
					}

				}

			});

			paintPanel.title = "Set the desired location inside the room";
			try {
				locPanel = new paintPanel();
				configPanel.add(locPanel);
			} catch (Exception ee) {
				ee.printStackTrace();
			}
			configPanel.add(cfg_name);
			configPanel.add(jcb_cfg_name);
			configPanel.add(cfg_gen_code);
			configPanel.add(jtf_cfg_gen_code);
			configPanel.add(cfg_spec_code);
			configPanel.add(jtf_cfg_spec_code);
			configPanel.add(cfg_id);
			configPanel.add(jtf_cfg_id);
			configPanel.add(cfg_sensorId_info);
			configPanel.add(cfg_sensorId_exp);
			configPanel.add(cfg_floor);
			configPanel.add(jcb_cfg_floor);
			configPanel.add(cfg_room);
			configPanel.add(jcb_cfg_room);
			configPanel.add(cfg_config);
			configPanel.add(locn);
			configPanel.add(pt_locn);
			configPanel.add(c_back);
			configPanel.add(c_next);

			// / Zoom panel starts here
			jsPanel = new JPanel();
			jsPanel.setBounds(410, 280, 60, 190);
			jsPanel.setBackground(Color.white);
			jsPanel.setLayout(new BorderLayout());
			jsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Zoom"));
			js_zm = new JSlider(JSlider.VERTICAL, -2, 7, 1);
			js_zm.setBounds(0, 0, 20, 90);
			jsPanel.add(js_zm);

			js_zm.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {

					float s = 0.0f;
					s = ((js_zm.getValue() * 0.2f));
					xoom = s;
					locPanel = new paintPanel();
					sensorFrame.setBounds(0, 0, 100, 100);
					locPanel.revalidate();
					locPanel.repaint();
					// System.out.println("Slider val:" + s);
				}

			});

			configPanel.add(jsPanel);//
			// end of zoom panel

			// // height panel starts here
			htPanel = new JPanel();
			htPanel.setBounds(200, 300, 150, 150);
			htPanel.setBackground(Color.white);
			htPanel.setLayout(new BorderLayout());
			htPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Height (ft)"));
			configPanel.add(htPanel);
			js_ht = new JSlider(JSlider.VERTICAL, 0, 12, 6);
			js_ht.setBounds(0, 0, 0, 40);
			js_ht.setMinorTickSpacing(1);
			js_ht.setMajorTickSpacing(4);
			js_ht.setPaintLabels(true);
			js_ht.setPaintTicks(true);
			js_ht.setLabelTable(js_ht.createStandardLabels(4));
			htPanel.add(js_ht);
			// / end of height panel
			// tabbedPane.add(configPanel);//

			cfg_config.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						if (jtf_cfg_id.getText().toString().trim().equals("") || jtf_cfg_id.getText() == null) {
							throw new EmptyException("Sensor ID cannot be empty.");
						}
						if (pt_locn.getText().toString().trim().equals("") || pt_locn.getText() == null
								|| position.getText().toString().trim().equals("") || position.getText() == null) {

							throw new EmptyException("Location textbox cannot be empty.");
						}

						Statement stmt = dbConn.createStatement();
						String sensor_name = jcb_cfg_name.getSelectedItem().toString().trim();
						String sensor_spec_code = jtf_cfg_spec_code.getText().toString().trim();
						String sensor_gen_code = jtf_cfg_gen_code.getText().toString().trim();
						String sensor_id = jtf_cfg_id.getText().toString().trim();
						int floor_id = Integer.parseInt(jcb_cfg_floor.getSelectedItem().toString().trim());
						String room_name = jcb_cfg_room.getSelectedItem().toString().trim();
						String geom = pt_locn.getText().toString().trim();
						String pos = position.getText().toString().trim();
						String room_id = null;

						String query = "select room_id from room where room_name like '%" + room_name + "%'";
						Statement stmt1 = dbConn.createStatement();
						ResultSet rs1 = stmt1.executeQuery(query);
						while (rs1.next()) {
							room_id = rs1.getString(1);
						}

						String ins_query = "insert into sensors values('" + sensor_id + "','" + sensor_spec_code + "','"
								+ sensor_gen_code + "'," + "ST_GeomFromText(" + geom + ")" + ",'" + pos + "'," + room_id
								+ "," + floor_id + ")";
						Statement stmt2 = dbConn.createStatement();
						stmt2.executeUpdate(ins_query);

						jcb_cfg_name.setSelectedIndex(0);
						jtf_cfg_gen_code.setText("");
						jtf_cfg_spec_code.setText("");
						jtf_cfg_id.setText("");
						jcb_cfg_floor.setSelectedIndex(0);
						jcb_cfg_room.setSelectedIndex(0);
						pt_locn.setText("");
						position.setText("");

						JOptionPane.showMessageDialog(null, "" + sensor_name + " has been configured successfully.");

					} catch (EmptyException ee) {
						if (ee.getMessage().equals("Location textbox cannot be empty.")) {
							JOptionPane.showMessageDialog(null, ee.getMessage().toString());
						} else {
							JOptionPane.showMessageDialog(null, ee.getMessage().toString());
							pt_locn.setText("");
							position.setText("");
							jtf_cfg_id.requestFocusInWindow();
							jtf_cfg_id.selectAll();
						}
					} catch (Exception ee) {
						// System.out.println("ee.message::"+ee.getMessage());
						JOptionPane.showMessageDialog(null, ee.getMessage().toString());
						pt_locn.setText("");
						position.setText("");
						jtf_cfg_id.requestFocusInWindow();
						jtf_cfg_id.selectAll();
						jtf_cfg_id.setToolTipText("<html><h4 style='color:red'>Enter a unique Sensor ID.</h4></html>");

					}

				}

			});

		} // end of configuration 

		try {
			jp = new JPanel();
			// jp.setBounds(200, 200, 1000, 900);
			jp.setBounds(500, 10, 800, 900);
			jp.setBackground(Color.white);

			glcanvas = new GLCanvas();
			// glcanvas.setSize(1000, 900);
			glcanvas.setSize(700, 900);

			jcb_type.addActionListener(new ActionListener() {

				@SuppressWarnings("unchecked")
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						viewPanel.remove(jcb_name);
						Statement stmt = dbConn.createStatement();
						String query = "select  sensor_name from sensors where sensor_type like '%"
								+ jcb_type.getSelectedItem().toString().trim() + "%'";
						ResultSet rs = stmt.executeQuery(query);
						java.util.List<String> senName = new java.util.ArrayList<String>();
						senName.add("All");
						while (rs.next())
							senName.add(rs.getString(1));
						str_senName = senName.toArray(new String[senName.size()]);
						jcb_name = new JComboBox(str_senName);
						jcb_name.setBounds(250, 40, 200, 20);
						viewPanel.add(jcb_name);
					} catch (Exception ee) {
						// System.out.println("At iirmssensor.java");
					}
				}
			});
			sdisp = new JButton("Display");
			sdisp.setBounds(170, 140, 100, 20);
			sdisp.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						drawPanel.removeAll();
						drawPanel.invalidate();
						drawPanel.repaint();

						SensorVisualization.array_pts.clear();
						SensorVisualization.room_disp.clear();
						SensorVisualization.room_points.clear();

						// dd_stype=jcb_type.getSelectedItem().toString().trim();
						// dd_sname=jcb_name.getSelectedItem().toString().trim();//
						dd_sname = "All";
						if (!(dd_sname.equals(null)) || !(dd_sname.isEmpty()))
							if (dd_sname.equals("All"))
								dd_sname = "";
							else
								dd_sname = " and sensor_name like '%" + dd_sname + "%'";
						dd_sfloor = jcb_floor.getSelectedItem().toString().trim();
						if (dd_sfloor.equals("All"))
							dd_sfloor = "";
						else
							dd_sfloor = " and floor_id=" + jcb_floor.getSelectedItem().toString() + "";

						dd_sroom = jcb_room.getSelectedItem().toString().trim();
						if (dd_sroom.equals("All"))
							dd_sroom = "";
						else
							dd_sroom = " and room_name like '%" + jcb_room.getSelectedItem().toString() + "%'";

						SensorVisualization.findFloor();
						drawPanel = new SensorVisualization();
						drawPanel.setBackground(Color.white);
						viewPanel.add(drawPanel);

						// draw2DPanel.setFloor(jcb_floor.getSelectedItem().toString().trim());

						// visualize(1);
					} catch (Exception ee) {
						ee.printStackTrace();
					}
				}
			});
			SensorVisualization.title = "Title::::";
			drawPanel = new SensorVisualization();
			namePanel = new JPanel();
			namePanel.setLayout(null);
			namePanel.setBackground(Color.white);
			// /Remove duplicate entries in list_sensor arraylist
			java.util.HashSet<String> list_hs = new java.util.HashSet<String>(list_sensors);
			list_sensors.clear();
			list_sensors.addAll(list_hs);
			// / end of duplicate removal

			String path = "";
			namePanel.setBounds(260, 220, 290, 420);
			for (int x = 0; x < list_sensors.size(); x++) {
				if (list_sensors.get(x).equalsIgnoreCase("WIFI Router"))
					url = new URL("http://172.17.9.60/html/pics/WIFI_Router.png");
				else if (list_sensors.get(x).equalsIgnoreCase("Camera Sensor"))
					url = new URL("http://172.17.9.60/html/pics/Camera.png");
				else if (list_sensors.get(x).equalsIgnoreCase("Temperature Sensor"))
					url = new URL("http://172.17.9.60/html/pics/Temperature_sensor.png");
				else if (list_sensors.get(x).equalsIgnoreCase("RFID Reader"))
					url = new URL("http://172.17.9.60/html/pics/RFID_Reader.png");
				else if (list_sensors.get(x).equalsIgnoreCase("RFID Antenna"))
					url = new URL("http://172.17.9.60/html/pics/RFID_Antenna.png");
				// System.out.println("Image:" + path);
				Image img = Toolkit.getDefaultToolkit().getImage(url);
				JLabel jl = new JLabel(new ImageIcon(img));
				jl.setBounds(55 + x, 55 + x, 50, 50);
				// namePanel.add(jl);
			}

			// Image imgic=ImageIO.read(new
			// File("/home/researcher/eclipse-workspace/luna
			// wkspace/IIrms_Final_Deliverable1/src/com/amrita/IIRMS/pics/2d/WIFI_Router.png"));
			// JLabel jl=new JLabel(new ImageIcon(imgic));
			// jl.setBounds(1, 1, 50, 50);

			namePanel.repaint();
			viewPanel.add(namePanel);

			// jp.add(drawPanel);
		} catch (Exception ee) {
			ee.printStackTrace();
		}

		// // Comment the dropdowns other than floor -> starts here

		// viewPanel.add(sname);//
		// viewPanel.add(jcb_type);//
		// viewPanel.add(jcb_name);//
		// viewPanel.add(stype);//

		// viewPanel.add(sroom);//
		// viewPanel.add(jcb_room);//
		// // Comment the dropdowns other than floor -> ends here
		v_back = new JButton("Back");
		v_back.setBounds(60, 140, 100, 20);
		v_back.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(tabbedPane.getSelectedIndex()-1);	
			}
		});
		v_close = new JButton("Close");
		v_close.setBounds(280, 140, 100, 20);
		v_close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				sensorFrame.dispose();	
			}
		});
		JLabel lhelp4=new JLabel(helpIcon);
        lhelp4.setBounds(450, 20, 30, 30);
        lhelp4.setToolTipText("<html><center>This form is to View the sensor Deployed in the Building.</center>"
        		+ "1.To View sensor deployed, select floor_id and click Display Button "
                + "<br/>2.Now, you can view deployed sensors in rightside of the Screen."
                +"<br/>3.User can navigate the tab by clicking Back button");
        
        viewPanel.add(lhelp4);
		
		viewPanel.add(sfloor);
		viewPanel.add(jcb_floor);
		viewPanel.add(sdisp);
		viewPanel.add(jp);
		viewPanel.add(drawPanel);
		viewPanel.add(v_back);
		viewPanel.add(v_close);

		// Setting panels in tabbed pane and frame dimension
		addPanel.setBackground(Color.white);
		addPanel1.setBackground(Color.white);
		amPanel.setBackground(Color.white);
		amPanel1.setBackground(Color.white);
		modifyPanel.setBackground(Color.white);
		modifyPanel1.setBackground(Color.white);
		configPanel.setBackground(Color.white);
		viewPanel.setBackground(Color.white);
		sensorFrame.setBackground(Color.white);
		amPanel.add(addPanel);

		amPanel1.add(addPanel1);
		amPanel1.add(modifyPanel1);
		tabbedPane.add(amPanel);
		tabbedPane.add(amPanel1);
		tabbedPane.add(configPanel);
		tabbedPane.add(viewPanel);
		sensorFrame.add(tabbedPane);
		sensorFrame.setVisible(true);
		sensorFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		sensorFrame.setMinimumSize(new Dimension(1200,1200 ));
	}

	// // Utilities required to load dropdown values and DB connections begins
	// here

	public void Db_Connection() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");
		// System.out.println("Name is : " +
		// IIRMSApplicationInterface.dbNameSelected);
		String dbName = IIRMSSensorWindow.dbNameSelected;
		dbConn = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/" + dbName, "researcher",
				"researcher");

	}

	public static java.util.List<String> sensorTypes() throws Exception {

		java.util.List<String> senTypes = new java.util.ArrayList<String>();

		String query = "select distinct sensor_specific_code from sensors";
		stmt = dbConn.createStatement();
		rs = stmt.executeQuery(query);
		// senTypes.add("new");
		while (rs.next()) {
			String inDB = rs.getString(1).trim();
			// System.out.println(inDB);
			senTypes.add(inDB.trim());
		}
		java.util.HashSet<String> hs = new java.util.HashSet<String>(senTypes);

		senTypes.clear();
		senTypes.addAll(hs);

		return senTypes;
	}

	public static java.util.List<String> sensorFloor() throws Exception {

		java.util.List<String> senFloor = new java.util.ArrayList<String>();

		String query = "select distinct floor_id from floor order by floor_id";
		stmt = dbConn.createStatement();
		rs = stmt.executeQuery(query);
		// senFloor.add("All");
		while (rs.next()) {
			String inDB = rs.getString(1);
			// System.out.println(inDB);
			senFloor.add(inDB);
		}

		return senFloor;
	}

	public static java.util.List<String> sensorRoom() throws Exception {

		java.util.List<String> senRoom = new java.util.ArrayList<String>();
		String query = "";
		// if (floor_id.toString().trim().equalsIgnoreCase("All"))
		query = "select distinct room_name from room";// where floor_id=
														// "+floor_id+"";
		// else
		// query =
		// "select distinct room_name from room where floor_id=
		// "+Integer.parseInt(floor_id)+"";
		stmt = dbConn.createStatement();
		rs = stmt.executeQuery(query);
		senRoom.add("All");
		while (rs.next()) {
			String inDB = rs.getString(1);
			// System.out.println(inDB);
			senRoom.add(inDB);
		}
		return senRoom;
	}

	public static java.util.List<String> sensorRoomByFloor() throws Exception {

		java.util.List<String> senRoom = new java.util.ArrayList<String>();
		String query = "";
		if (jcb_cfg_floor.getSelectedItem().toString().trim().equalsIgnoreCase("All"))
			query = "select distinct room_name from room ";// where floor_id=
															// "+floor_id+"";
		else
			query = "select distinct room_name from room  where floor_id= "
					+ Integer.parseInt(jcb_cfg_floor.getSelectedItem().toString().trim()) + "";
		stmt = dbConn.createStatement();
		rs = stmt.executeQuery(query);
		// senRoom.add("All");
		while (rs.next()) {
			String inDB = rs.getString(1);
			// System.out.println(inDB);
			senRoom.add(inDB);
		}
		return senRoom;
	}

	public static java.util.List<String> selectSensor() throws Exception {

		java.util.List<String> senTypes = new java.util.ArrayList<String>();

		String query = "select distinct sensor_generic_type from sensor_master";
		stmt = dbConn.createStatement();
		rs = stmt.executeQuery(query);
		// senTypes.add("");

		while (rs.next()) {
			String inDB = rs.getString(1).trim();
			// System.out.println(inDB);
			senTypes.add(inDB.trim());
		}
		java.util.HashSet<String> hs = new java.util.HashSet<String>(senTypes);

		senTypes.clear();
		senTypes.addAll(hs);
		senTypes.add("new");
		return senTypes;
	}

	public static java.util.List<String> selectSensortoModify() throws Exception {

		java.util.List<String> senTypes = new java.util.ArrayList<String>();

		String query = "select distinct sensor_generic_type from sensor_master";
		stmt = dbConn.createStatement();
		rs = stmt.executeQuery(query);

		while (rs.next()) {
			String inDB = rs.getString(1).trim();
			// System.out.println(inDB);
			senTypes.add(inDB.trim());
		}
		java.util.HashSet<String> hs = new java.util.HashSet<String>(senTypes);

		senTypes.clear();
		senTypes.addAll(hs);

		return senTypes;
	}

	public static java.util.List<String> getGenericSensorType() throws Exception {

		java.util.List<String> senTypes = new java.util.ArrayList<String>();
		String query = "select distinct sensor_generic_name from sensor_master";
		stmt = dbConn.createStatement();
		rs = stmt.executeQuery(query);
		while (rs.next()) {
			String inDB = rs.getString(1).trim();
			// System.out.println(inDB);
			senTypes.add(inDB.trim());
		}
		java.util.HashSet<String> hs = new java.util.HashSet<String>(senTypes);

		senTypes.clear();
		senTypes.addAll(hs);

		return senTypes;
	}

	public static java.util.List<String> getGenericSensorNamebytype(String sensor_type) throws Exception {

		java.util.List<String> senTypes = new java.util.ArrayList<String>();
		String query = "select distinct sensor_generic_name from sensor_master where sensor_generic_type like '%"
				+ sensor_type /*
								 * jcb_mtype.getSelectedItem().toString().trim()
								 */ + "%'";
		stmt = dbConn.createStatement();
		rs = stmt.executeQuery(query);
		while (rs.next()) {
			String inDB = rs.getString(1).trim();
			// System.out.println(inDB);
			senTypes.add(inDB.trim());
		}
		java.util.HashSet<String> hs = new java.util.HashSet<String>(senTypes);

		senTypes.clear();
		senTypes.addAll(hs);

		return senTypes;
	}

	public static String getGenericSensorCodebyGenericName(String sensor_generic_name) throws Exception {

		String generic_code = null;
		String query = "select sensor_generic_code from sensor_master where sensor_generic_name like '%"
				+ sensor_generic_name + "%'";
		stmt = dbConn.createStatement();
		rs = stmt.executeQuery(query);
		while (rs.next()) {
			generic_code = rs.getString(1).trim();
		}
		return generic_code;
	}

	public static String getSpecificSensorCodeByName(String sensor_name) throws Exception {
		String specific_code = null;
		String query = "select sensor_specific_code from sensor_details where sensor_name like '%" + sensor_name + "%'";
		stmt = dbConn.createStatement();
		rs = stmt.executeQuery(query);
		while (rs.next()) {
			specific_code = rs.getString(1).trim();
		}
		return specific_code;
	}

	public static String getGenericSensorCodeByName(String sensor_name) throws Exception {
		String specific_code = null;
		String query = "select sensor_generic_code from sensor_details where sensor_name like '%" + sensor_name + "%'";
		stmt = dbConn.createStatement();
		rs = stmt.executeQuery(query);
		while (rs.next()) {
			specific_code = rs.getString(1).trim();
		}
		return specific_code;
	}

	public static java.util.List<String> getSensorNamebytype(String sensor_generic_name) throws Exception {

		String generic_code = null;
		String query1 = "select sensor_generic_code from sensor_master where sensor_generic_name like '%"
				+ sensor_generic_name.trim() + "%'";
		Statement stmt1 = dbConn.createStatement();
		ResultSet rs1 = stmt1.executeQuery(query1);
		while (rs1.next()) {
			generic_code = rs1.getString(1);
		}

		java.util.List<String> senTypes = new java.util.ArrayList<String>();
		String query = "select distinct sensor_name from sensor_details where sensor_generic_code like '%"
				+ generic_code.trim() + "%'";
		stmt = dbConn.createStatement();
		rs = stmt.executeQuery(query);
		while (rs.next()) {
			String inDB = rs.getString(1).trim();
			// System.out.println(inDB);
			senTypes.add(inDB.trim());
		}
		java.util.HashSet<String> hs = new java.util.HashSet<String>(senTypes);

		senTypes.clear();
		senTypes.addAll(hs);

		return senTypes;
	}

	public static java.util.List<String> getSensorName() throws Exception {

		java.util.List<String> senTypes = new java.util.ArrayList<String>();
		String query = "select distinct sensor_name from sensor_details";
		stmt = dbConn.createStatement();
		rs = stmt.executeQuery(query);
		while (rs.next()) {
			String inDB = rs.getString(1).trim();
			// System.out.println(inDB);
			senTypes.add(inDB.trim());
		}
		java.util.HashSet<String> hs = new java.util.HashSet<String>(senTypes);

		senTypes.clear();
		senTypes.addAll(hs);

		return senTypes;
	}

	// // Utilities required to load dropdown values and DB connections ends
	// here

}

class paintPanel extends JPanel {
	public static String title;
	public static float[] floor_points;
	public static float scale = 0.8f;

	public static ArrayList<Float> room_points = new ArrayList<Float>();
	public static ArrayList<Polygon> poly_pts = new ArrayList<Polygon>();

	public static ArrayList<float[]> array_pts = new ArrayList<float[]>();
	public static ArrayList<String> position_pts = new ArrayList<String>();

	public static ArrayList<String> room_disp = new ArrayList<String>();
	public static float room_xmax = 0.0f, room_ymax = 0.0f;
	public static float floor_xmax = 0.0f, floor_ymax = 0.0f, floor_xmin = 0.0f, floor_ymin = 0.0f;
	public static String geom_format = "", position_format = "";

	public static int panel_l = 620, panel_t = 10, panel_r = 750, panel_b = 870;
	Polygon room = new Polygon();
	public static JSlider js_zs;

	public paintPanel() {
		setLayout(new BorderLayout());
		setBounds(panel_l, panel_t, panel_r, panel_b);
		this.setBackground(Color.white);
		addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				float currx = (float) (e.getX() / 0.8); // Default --> before
														// zoom
				float curry = (float) (e.getY() / 0.8);// Default --> before
														// zoom
				// float currx=(float) (e.getX()/IIRMSSensorModeling.xoom); //
				// float curry=(float) (e.getY()/IIRMSSensorModeling.xoom);//
				// System.out.println("Mouse:::::::::::::" + currx + " , " +
				// curry + " floor:" + floor_xmax);
				float xdist = currx - floor_xmin;
				float ydist = curry - floor_ymin;
				// System.out.println("Mouse:::::::: appr pts" + (((xdist * 100)
				// / (floor_xmax - floor_xmin)) / 100) * floor_xmax + " , "
				// + ((100 - ((ydist * 100) / (floor_ymax - floor_ymin))) / 100)
				// * floor_ymax);

				float p_xval = (((xdist * 100) / (floor_xmax - floor_xmin)) / 100) * floor_xmax;
				float p_yval = ((100 - ((ydist * 100) / (floor_ymax - floor_ymin))) / 100) * floor_ymax;

				geom_format = "" + "'POLYGON((" + p_xval + " " + p_yval + "," + (p_xval + 70) + " " + p_yval + ","
						+ p_xval + " " + (p_yval + 70) + "," + (p_xval + 70) + " " + (p_yval + 70) + "," + p_xval + " "
						+ p_yval + "))'" + "";
				int int_p_xval = (int) (0.8 * (((xdist * 100) / (floor_xmax - floor_xmin)) / 100) * floor_xmax);
				int int_p_yval = (int) (0.8 * ((((ydist * 100) / (floor_ymax - floor_ymin))) / 100) * floor_ymax);
				position_format = "" + int_p_xval + "," + int_p_yval + "," + SensorModeling.js_ht.getValue() + "";
				SensorModeling.pt_locn.setText("");
				SensorModeling.pt_locn.setText(geom_format);
				SensorModeling.position.setText("");
				SensorModeling.position.setText(position_format);
				// System.out.println("Pos_format::" + position_format + " ,
				// xmax:" + floor_xmax + ",:xmin:" + floor_xmin);

			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}

		});
		repaint();
	}

	public void drawLayout(Graphics g) {
		this.setPreferredSize(new Dimension(750, 870));
		this.setBackground(Color.white);
		// g.setColor(new Color(222,184,135));
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform new_g = g2d.getTransform();
		g2d.setTransform(new_g);
		g2d.setColor(new Color(139, 69, 19));
		int bleft = 100, btop = 10, xn = 0, yn = 0, x_incr = 0, y_incr = 0;

		for (int i = 0; i < array_pts.size(); i++) {

			float[] pts = array_pts.get(i);
			int[] x_poly = new int[pts.length / 2];
			int[] y_poly = new int[pts.length / 2];
			for (int j = 0; j < pts.length; j += 2) {
				int x1 = (int) ((((((pts[j] * 0.8))))));
				int y1 = (int) ((((((pts[j + 1] * 0.8))))));
				x_poly[x_incr++] = x1;
				y_poly[y_incr++] = y1;
			}
			x_incr = 0;
			y_incr = 0;
			{
				// System.out.println("RN:::"+room_disp.get(x));
				String r_name = room_disp.get(i).toString().trim();
				if (r_name.equalsIgnoreCase(SensorModeling.jcb_cfg_room.getSelectedItem().toString().trim())) {
					g.setColor(Color.RED);
				}
			}

			g2d.drawPolyline(x_poly, y_poly, pts.length / 2);

			g.setColor(new Color(139, 69, 19));
			// g2d.drawString(room_disp.get(i), x_poly[0],(y_poly[0]+20) );
			// g.fillPolygon(x_poly, y_poly, pts.length/2);

		}

		for (int x = 0; x < array_pts.size(); x++)
			// System.out.println("Array_pts in paint component:" +
			// array_pts.get(x));

			// System.out.println("Array_pys.size():::::" + array_pts.size());
			new_g.createTransformedShape(room);
		x_incr = 0;
		y_incr = 0;
		// this.revalidate();
		// this.repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Draw 2D layout of the selected floor
		drawLayout(g);
	}

}

// // Custom Exception details...

class EmptyException extends Exception {
	private Connection dbConn;

	public EmptyException() {

	}

	public EmptyException(String ex) {
		super(ex);
	}

	public EmptyException(Throwable cause) {
		super(cause);
	}

	public EmptyException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmptyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public void Db_Connection() throws Exception {
		Class.forName("org.postgresql.Driver");
		// System.out.println("Name is : " + IIRMSSensorWindow.dbNameSelected);
		String dbName = IIRMSSensorWindow.dbNameSelected;
		dbConn = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/" + dbName, "researcher",
				"researcher");
	}
}
