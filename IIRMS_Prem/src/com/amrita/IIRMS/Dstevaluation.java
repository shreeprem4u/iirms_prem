package com.amrita.IIRMS;
/*
 * File Name        : Dstevaluation.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Dinesh Kumar E (Junior Research Fellow) 
 * Last Modified    : the 4th of November, 2015
 * Purpose          : Class to Display the Dst Evaluation text .
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Dstevaluation {
	public static JFrame DstWindow_IIRMS;
	 public static JButton Enter;
	private JLabel lWelcomed;
	private JLabel img_dstIRMS;
	private JPanel mainPanel1_NavigationIIRMS;
	private JLabel lWelcome1;
	private JLabel lWelcome2;
	 public Dstevaluation() throws Exception {
		 System.out.println("IN Dstevaluation constructor");
		 DstWindow_IIRMS = new JFrame("Indoor Information Representation and Management System");
		 mainPanel1_NavigationIIRMS = new JPanel();
	        mainPanel1_NavigationIIRMS.setLayout(null);
	        lWelcomed=new JLabel("<html><center>This system is developed by Amrita Multidimensional Data Analytics Lab, Amrita Vishwa Vidyapeetham(University), </center> </html>",SwingConstants.CENTER);
	        lWelcomed.setBounds(3,0,1400,300);
	        lWelcomed.setFont(new Font("Serif",Font.BOLD,22));
	        lWelcomed.setForeground(Color.white);
	        lWelcome1=new JLabel("<html> Coimbatore, as part of the project funded by NRDMS, DST, Govt of India(NRDMS/11/1925/012) </html>",SwingConstants.CENTER);
	        lWelcome1.setBounds(10,30,1400,300);
	        lWelcome1.setFont(new Font("Serif",Font.BOLD,22));
	        lWelcome1.setForeground(Color.white);
	        lWelcome2=new JLabel("<html> (For DST Evaluation Purposes Only) </html>",SwingConstants.CENTER);
	        lWelcome2.setBounds(10,100,1400,300);
	        lWelcome2.setFont(new Font("Serif",Font.BOLD,22));
	        lWelcome2.setForeground(Color.white);
	        
	        URL url4=new URL("http://172.17.9.60/html/pics/background.jpg");
	        ImageIcon icon4=new ImageIcon(url4);
	        Image imgg = icon4.getImage();

	        imgg = imgg.getScaledInstance(Toolkit.getDefaultToolkit().getScreenSize().width,
	                Toolkit.getDefaultToolkit().getScreenSize().height, Image.SCALE_SMOOTH);

	        ImageIcon icc3 = new ImageIcon(imgg);
	        img_dstIRMS = new JLabel(icc3);
	        img_dstIRMS.setBounds(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width,
	                Toolkit.getDefaultToolkit().getScreenSize().height);
	        img_dstIRMS.add(lWelcomed);
	        img_dstIRMS.add(lWelcome1);
	        img_dstIRMS.add(lWelcome2);
	        //button
	        Enter = new JButton("Enter");
	        Enter.setBounds(600, 330, 200, 50);
	        Enter.setToolTipText("Click here to run a application");
	        Enter.setFont(new Font("Arial", Font.BOLD, 17));
	      //Defining the action for Application UI button
	        Enter.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                try {
	                	IIRMSNavigationWindow navi=new IIRMSNavigationWindow();
	                } catch (Exception ex) {
	                }
	            }
	        });
	        mainPanel1_NavigationIIRMS.add(Enter);
	        mainPanel1_NavigationIIRMS.add(img_dstIRMS);
	        
	        DstWindow_IIRMS.add(mainPanel1_NavigationIIRMS);
	        DstWindow_IIRMS.setVisible(true);
	             
	        DstWindow_IIRMS.setSize(Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
	        DstWindow_IIRMS.setExtendedState(JFrame.MAXIMIZED_BOTH);
	        DstWindow_IIRMS.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        
	       
	 }
	 

	

}
