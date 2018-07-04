package com.amrita.IIRMS;

/*
 * File Name        : IIRMSLogin.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Ajeissh M (Junior Research Fellow) and Ramesh G (Junior Research Fellow), Dineshkumar E(Junior Research Fellow)
 * Last Modified    : the 12th of October, 2015
 * Purpose          : Class to display the Login Page.
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.*;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.amrita.IIRMS.*;
import com.amrita.IIRMS.DB.Management.SensorModeling;
public class IIRMSLogin {

    public static JFrame loginpage;
    public static JTextField Un, a, b;
    public static JPasswordField Ps;
    public static JPanel mainPanel;
    public static JLabel username, password;
    public static JButton Login1, Cancel;
    public static String curr_user;
    public static JLabel lTitle;

    /*
     * Method name              :   IIRMSLogin
     * Method description       :   Constructor to define the form components and their behaviors
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public IIRMSLogin() throws Exception {
    	System.out.println("In Login");
        //Defining the frame to contain the panel.
        loginpage = new JFrame("Login");
        loginpage.setBounds(500, 200, 600, 370);
        loginpage.setResizable(false);
        //Defining the panel to contain the form components.
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
       
        //mainPanel.setBackground(lightBlue);
        mainPanel.setBackground(Color.white);
        mainPanel.setBounds(20, 20, 200, 200);
        //mainPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Login"));
        
        Color blue=new Color(147,221,236);
        lTitle=new JLabel("LOGIN",SwingConstants.CENTER);
        lTitle.setBounds(0,0,600,50);
        lTitle.setFont(new Font("Serif",Font.BOLD,20));
        lTitle.setBackground(blue);
        lTitle.setOpaque(true);

        URL url=new URL("http://172.17.9.60/html/pics/login.png");
        ImageIcon icon1=new ImageIcon(url);
        Image img=icon1.getImage();
        ImageIcon loginIcon=new ImageIcon(img);
        JLabel loginImage=new JLabel(loginIcon);
        loginImage.setBounds(10, 50, 200, 200);
       
        Ps = new JPasswordField(25);

        Un = new JTextField(25);
        Ps.setBounds(330, 125, 200, 25);
        Un.setBounds(330, 85, 200, 25);
        a = new JTextField("admin");
        b = new JTextField("admin");
        
        Login1 = new JButton("Login");
        Login1.setBounds(300, 190, 100, 25);
		//Color blue=new Color(165,208,236);
		Login1.setBackground(blue);
        Cancel = new JButton("Cancel");
        Cancel.setBounds(430, 190, 100, 25);
        Cancel.setBackground(blue);

        username = new JLabel("Username");
        password = new JLabel("Password");
        username.setBounds(220, 75, 250, 50);
        // username.setSize(, );
        password.setSize(250, 80);
        password.setBounds(220, 115, 250, 50);
        mainPanel.add(loginImage);
        mainPanel.add(lTitle);
        mainPanel.add(Ps);
        mainPanel.add(Un);
        mainPanel.add(username);
        mainPanel.add(password);
        mainPanel.add(Login1);
        mainPanel.add(Cancel);
        loginpage.add(mainPanel);
        loginpage.setVisible(true);
        //Defining the action for login button
        Login1.addActionListener(new ActionListener() {
            @SuppressWarnings("deprecation")
			@Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (Ps.getText().equals("admin")) {
                        if (Un.getText().equals("admin")) {
                            IIRMSConfigurationInterface configInterface = new IIRMSConfigurationInterface();
                            //loginpage.removeAll();
                            loginpage.setVisible(false);
                            IIRMSNavigationWindow.mainWindow_NavigationIIRMS.setEnabled(false);
                            IIRMSNavigationWindow.mainWindow_NavigationIIRMS.setVisible(false);
                        }
                    }
                    else if(Ps.getText().equals("sadmin"))
                    {  
                    	if(Un.getText().equals("sadmin"))
                    	{   curr_user=Un.getText().toString().trim();
                    		//IIRMSSensor sensorInterface=new IIRMSSensor();
                    	System.out.println(curr_user);
                    	    SensorModeling sensorInterface=new SensorModeling();
                    		loginpage.setVisible(false);
                    		 //IIRMSNavigationWindow.mainWindow_NavigationIIRMS.setEnabled(false);
                             //IIRMSNavigationWindow.mainWindow_NavigationIIRMS.setVisible(false);
                    		
                    	}
                    	
                    }
                    
                    else if(Ps.getText().equals("senuser"))
                    {  
                    	if(Un.getText().equals("senuser"))
                    	{   curr_user=Un.getText().toString().trim();
                    	System.out.println(curr_user);
                    		//IIRMSSensor sensorInterface1=new IIRMSSensor();
                    	SensorModeling sensorInterface1=new SensorModeling();
                    		loginpage.setVisible(false);
                    		 //IIRMSNavigationWindow.mainWindow_NavigationIIRMS.setEnabled(false);
                             //IIRMSNavigationWindow.mainWindow_NavigationIIRMS.setVisible(false);
                    		
                    	}
                    	
                    }
                    
                } catch (Exception ex) {
                	
                    String msg = "";
                    StackTraceElement ste[] = ex.getStackTrace();
                    for (int i = 0; i < ste.length; i++) {
                        msg += ste[i].toString();
                    }
                   ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, msg);
                }
            }
        });
        //Defining the action for cancel button.
        Cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loginpage.setVisible(false);
                    IIRMSNavigationWindow.mainWindow_NavigationIIRMS.setEnabled(true);
                } catch (Exception ex) {
                }//catch(){}
            }
        });
    }
    
}