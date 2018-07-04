package com.amrita.IIRMS;
/*
 * File Name        : IIRMSBuildingAdmin_UI.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Dinesh Kumar E (Junior Research Fellow),Sindhya Kumari N(Junior Research Fellow)
 * Last Modified    : the 5th of October, 2015
 * Purpose          : GUI design of Building Administrator in Configuration Interface  .
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.amrita.IIRMS.DB.Management.Modify_Attribute;
import com.amrita.IIRMS.DB.Management.SensorModeling;



public class IIRMSBuildingAdmin_UI  extends JFrame{
	private JPanel BadminPanel;
	private JButton Buildingadmin_modifyattribute;
	private JButton Buildingadmin_SensorConfiguration;
	private JLabel backgroundImage,ladmin,adminIcon;
	private ImageIcon img_buildConfig,img_sensorConfig;
	public URL url;

	public IIRMSBuildingAdmin_UI() throws Exception	{
		BadminPanel = new JPanel();
        BadminPanel.setLayout(null);
        Color blue=new Color(100,191,219);
        
        url=new URL("http://172.17.9.60/html/pics/background1.png");
        ImageIcon icon1=new ImageIcon(url);
        Image bgImage=icon1.getImage();
        bgImage=bgImage.getScaledInstance(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, Image.SCALE_SMOOTH);
        ImageIcon bgIcon=new ImageIcon(bgImage);
        backgroundImage=new JLabel(bgIcon);
        backgroundImage.setBounds(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        
        ladmin=new JLabel("Building Administrator");
        ladmin.setBounds(1100, 30, 200, 20);
        ladmin.setForeground(Color.WHITE);
        
        url=new URL("http://172.17.9.60/html/pics/build-admin.jpg");
        ImageIcon icon2=new ImageIcon(url);
        Image image=icon2.getImage();
        image=image.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon icc1 = new ImageIcon(image);
        adminIcon = new JLabel(icc1);
        adminIcon.setBounds(1030, 20,50 ,50 );
        
        url=new URL("http://172.17.9.60/html/pics/modify-attr.png");
        ImageIcon icon3=new ImageIcon(url);
        Image img1=icon3.getImage();
        img1=img1.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        img_buildConfig=new ImageIcon(img1);
        
        Buildingadmin_modifyattribute= new JButton("Building Configuration",img_buildConfig);
        Buildingadmin_modifyattribute.setVerticalTextPosition(SwingConstants.CENTER);
        Buildingadmin_modifyattribute.setHorizontalTextPosition(SwingConstants.RIGHT);
        Buildingadmin_modifyattribute.setBounds(10, 180, 300, 50);
        Buildingadmin_modifyattribute.setToolTipText("Click to Modify the Building Details");

        //Buildingadmin_modifyattribute.setBackground(blue);
    	//action listener
        Buildingadmin_modifyattribute.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent ae) {
                 try {
                	 Modify_Attribute modifyatt= new Modify_Attribute();
                 } catch (Exception ex) {
                     ex.printStackTrace();
                 }
             }
         });
        
        url=new URL("http://172.17.9.60/html/pics/sensor-config.png");
        ImageIcon icon4=new ImageIcon(url);
        Image img2=icon4.getImage();
        img2=img2.getScaledInstance(70, 50, Image.SCALE_SMOOTH);
        img_sensorConfig=new ImageIcon(img2);
        
        Buildingadmin_SensorConfiguration = new JButton("Sensor Configuration",img_sensorConfig);
        Buildingadmin_SensorConfiguration.setVerticalTextPosition(SwingConstants.CENTER);
        Buildingadmin_SensorConfiguration.setHorizontalTextPosition(SwingConstants.RIGHT);
        Buildingadmin_SensorConfiguration.setBounds(340, 180, 300, 50);
        Buildingadmin_SensorConfiguration.setToolTipText("Click to Configure Sensors in the Building");

        //Buildingadmin_SensorConfiguration.setBackground(blue);
    	//action listener
        Buildingadmin_SensorConfiguration.addActionListener(new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent ae) {
                 try {
                	 IIRMSSensorWindow sensorInterface=new IIRMSSensorWindow();
                 } catch (Exception ex) {
                     ex.printStackTrace();
                 }
             }
         });
        
        BadminPanel.add(ladmin);
        BadminPanel.add(adminIcon);
        BadminPanel.add(Buildingadmin_modifyattribute);
        BadminPanel.add(Buildingadmin_SensorConfiguration);
        BadminPanel.add(backgroundImage);
        add(BadminPanel);
        setTitle("Building Adminstrator UI");
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(750, 500));
	}

}
