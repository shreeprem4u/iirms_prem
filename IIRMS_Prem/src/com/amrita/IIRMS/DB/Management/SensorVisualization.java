package com.amrita.IIRMS.DB.Management;

/*
 * File Name        : SensorVisualization.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Mukunthan R(Junior Research Fellow)
 * Last Modified    : the 26th of March, 2015
 * Purpose          : Class to visualize the sensors in a 2D floor.
 */

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.*;

import javax.swing.*;

import java.awt.geom.AffineTransform;

import com.amrita.IIRMS.IIRMSSensorWindow;

public class SensorVisualization extends JPanel {
	public static String title;
	public static float[] floor_points;

	public static ArrayList<Float> room_points = new ArrayList<Float>();
	public static ArrayList<Polygon> poly_pts = new ArrayList<Polygon>();

	public static ArrayList<float[]> array_pts = new ArrayList<float[]>();
	public static ArrayList<String> position_pts = new ArrayList<String>();

	public static ArrayList<String> room_disp = new ArrayList<String>();
	public static ArrayList<String> icon_dim = new ArrayList<String>();

	public static float room_xmax = 0.0f, room_ymax = 0.0f;
	public static float floor_xmax = 0.0f, floor_ymax = 0.0f, floor_xmin = 0.0f, floor_ymin = 0.0f;
	int panel_l = 520, panel_t = 10, panel_r = 750, panel_b = 870;
	private URL url;

	public SensorVisualization() {
		setLayout(new BorderLayout());
		setBounds(panel_l, panel_t, panel_r, panel_b);
		this.setBackground(Color.white);
		// findFloor();
		repaint();

	}

	public void drawLayout(Graphics g) throws MalformedURLException {

		// g.setColor(new Color(222,184,135));

		Graphics2D g2d = (Graphics2D) g;
		g.setColor(new Color(139, 69, 19));
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
			g.drawPolyline(x_poly, y_poly, pts.length / 2);
			String r_name = room_disp.get(i).toString().trim();

			// String room_name[]=room_disp.get(i).split(",");
			// g.drawString(room_name[2],
			// (int)Integer.parseInt(room_name[0]),(int)
			// Integer.parseInt(room_name[1]));

		}

		// // As of now, create just two icons(for camera and wifi sensors),
		// then extend to all kind of sensors.
		String path = "", gname;
		for (int x = 0; x < icon_dim.size(); x++) {
			gname = position_pts.get(x).split(",")[0];// gname=icon_dim.get(x).split(",")[0];
			if (gname.equalsIgnoreCase("WIFI Router"))
				url = new URL("http://172.17.9.60/html/pics/WIFI_Router.png");
			else if (gname.equalsIgnoreCase("Camera Sensor"))
				url = new URL("http://172.17.9.60/html/pics/Camera.png");
			else if (gname.equalsIgnoreCase("Temperature Sensor"))
				url = new URL("http://172.17.9.60/html/pics/Temperature_sensor.png");
			else if (gname.equalsIgnoreCase("RFID Reader"))
				url = new URL("http://172.17.9.60/html/pics/RFID_Reader.png");
			else if (gname.equalsIgnoreCase("RFID Antenna"))
				url = new URL("http://172.17.9.60/html/pics/RFID_Antenna.png");
			else
			{
				url = new URL("http://172.17.9.60/html/pics/WIFI_Router.png");
			}
			 
			Image img = Toolkit.getDefaultToolkit().getImage(url);
			// int xpt=
			g.drawImage(img, Integer.parseInt(position_pts.get(x).split(",")[1]), Integer.parseInt(position_pts.get(x).split(",")[2]), this);// g.drawImage(img,Integer.parseInt(icon_dim.get(x).split(",")[1]),Integer.parseInt(icon_dim.get(x).split(",")[2]),this);
			// g.drawString("*",Integer.parseInt(position_pts.get(x).split(",")[1]),Integer.parseInt(position_pts.get(x).split(",")[2]));
			// System.out.println("generic_name"+gname);
		}
		// g.drawString("373,408", 375, 204);
		AffineTransform new_g = g2d.getTransform();
		new_g.scale(0.8, 0.8);//
		g2d.setTransform(new_g);
		x_incr = 0;
		y_incr = 0;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		try {
			drawLayout(g);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void findFloor() {
		String floor_id = SensorModeling.jcb_floor.getSelectedItem().toString().trim();
		if (floor_id.toString().trim().equalsIgnoreCase("All")) {
			// System.out.println("All floors are selected:");
		} else {

			int fid = Integer.parseInt(floor_id);
			try {
				// IIRMSSensorModeling.namePanel.removeAll();
				// // IIRMSSensorModeling.namePanel.invalidate();
				// IIRMSSensorModeling.namePanel.repaint();

				array_pts.clear();
				room_disp.clear();
				room_points.clear();
				position_pts.clear();
				String dbName = IIRMSSensorWindow.dbNameSelected;
				Class.forName("org.postgresql.Driver");
				Connection conn = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/" + dbName, "researcher", "researcher");
				Statement stmt = conn.createStatement();
				String query = "select room_name,st_astext(the_geom) from room where floor_id=" + fid + "";
				ResultSet rs = stmt.executeQuery(query);
				ArrayList<Float> r_pointList = new ArrayList<Float>();
				Polygon p;

				while (rs.next()) {
					p = new Polygon();
					String[] r_pointIn = rs.getString(2).replace("MULTIPOLYGON", "").replace("POLYGON", "").replace(",", " ")
							.replace("(", "").replace(")", "").split(" ");
					float xp1 = 0.0f, yp1 = 0.0f;
					float[] arr = new float[r_pointIn.length];
					for (int i = 0; i < r_pointIn.length; i = i + 2) {
						float xPoint = Float.parseFloat(r_pointIn[i]);
						float yPoint = Float.parseFloat(r_pointIn[i + 1]);

						xp1 = xPoint;
						yp1 = yPoint;
						r_pointList.add(xp1);
						r_pointList.add(yp1);
						p.addPoint((int) (xp1 / 0.8), (int) (yp1 / 0.8));
					}

					for (int i = 0; i < arr.length; i++) {
						arr[i] = r_pointList.get(i);
					}
					array_pts.add(arr);
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

					Float xmin = xpt_al.get(0), ymin = ypt_al.get(0), xmax = xpt_al.get(xpt_al.size() - 1), ymax = ypt_al
							.get(ypt_al.size() - 1);// Float
													// xmin=xpt_al.get(0)*0.8f,ymin=ypt_al.get(0)*0.8f,xmax=xpt_al.get(xpt_al.size()-1)*0.8f,ymax=ypt_al.get(ypt_al.size()-1)*0.8f;//

					// room_disp.add(Integer.parseInt(""+Math.round(xmin+(xmax-xmin)/2))+","+Integer.parseInt(""+Math.round(ymin+(ymax-ymin)/2))+","+rs.getString(1));
					room_disp.add("" + (Integer.parseInt("" + Math.round(xmin)) + Integer.parseInt("" + Math.round(xmax))) / 2 + ","
							+ (Integer.parseInt("" + Math.round(ymin)) + Integer.parseInt("" + Math.round(ymax))) / 2 + ","
							+ rs.getString(1));//
					// room_disp.add(rs.getString(1));//
					r_pointList.clear();
				}

				Statement stmt2 = conn.createStatement();
				// query2 return only one record
				String query2 = "select st_astext(the_geom) from floor where " + "floor_id=" + floor_id;
				ResultSet rs2 = stmt2.executeQuery(query2);

				while (rs2.next()) {
					String[] f_pointIn = rs2.getString(1).replace("POLYGON","").replace("MULTILINESTRING", "").replace("LINESTRING", "").replace(",", " ")
							.replace("(", "").replace(")", "").split(" ");
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

					Float xmin = xpt_al.get(0), ymin = ypt_al.get(0), xmax = xpt_al.get(xpt_al.size() - 1), ymax = ypt_al
							.get(ypt_al.size() - 1);

					floor_xmax = xmax;
					floor_ymax = ymax;
					floor_xmin = xmin;
					floor_ymin = ymin;
					f_pointList.clear();

				}
				icon_dim.clear();
				SensorModeling.list_sensors.clear();
				String generic_code=null;
				String[] pts=null;
				Statement stmt3 = conn.createStatement();
				String query3 = "select sensor_generic_code,st_astext(the_geom),position from sensors where floor_id=" + fid + "";
				ResultSet rs3 = stmt3.executeQuery(query3);
				while (rs3.next()) {
					pts = rs3.getString(2).replace("MULTIPOLYGON", "").replace("POLYGON", "").replace(",", " ").replace("(", "")
							.replace(")", "").split(" ");
					generic_code=rs3.getString(1);
			
					String generic_name=null;
					String query4="select sensor_generic_name from sensor_master where sensor_generic_code like '%"+generic_code+"%'";
					Statement stmt4=conn.createStatement();
					ResultSet rs4=stmt4.executeQuery(query4);
					while(rs4.next())
					{
						generic_name=rs4.getString(1);
					}
					position_pts.add("" + generic_name + "," + rs3.getString(3));
					icon_dim.add("" + generic_name + "," + new Double(Double.parseDouble("" + pts[0])).intValue() + ","
						+ new Double(Double.parseDouble("" + pts[1])).intValue());
					SensorModeling.list_sensors.add(generic_name);
				
				}

			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
	}

}