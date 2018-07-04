package com.amrita.IIRMS;

/*
 * File Name        : IIRMSMainVisualization.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Ajeissh M (Junior Research Fellow),Sindhya Kumari N(Junior Research Fellow)
 * Last Modified    : the 28th of March, 2014
 * Purpose          : Class to display the main Visualization window with options for floors 
 *                    and the 3D canvas and Drag-And-Drop window.
 */

import com.amrita.IIRMS.Visualization.IIRMSFloorVisualization;
import com.amrita.IIRMS.Visualization.IIRMSQueryInterface;
import com.amrita.IIRMS.Visualization.IIRMSVisualization;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;

import javax.swing.*;

public class IIRMSMainVisualization {   

    public static String text1 = null;
	public static JFrame jf;
    public static JPanel jpParent, jpEast, jpCenter, jpWest;
    public static JScrollPane sPane;
    public static IIRMSVisualization visualPanel;
    private JButton bQuery;
	private ImageIcon helpIcon;
	private ImageIcon img_back;
	private JButton back;
	public static JLabel ltext;
	

    
    

    /*
     * Method name              :   IIRMSMainVisualization
     * Method description       :   Constructor to define the form components and their behaviors
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public IIRMSMainVisualization()throws Exception {
        visualPanel = new IIRMSVisualization();
       
        try {
            jf = new JFrame();
            jpParent = new JPanel();
            jpParent.setLayout(null);
            jpParent.setBackground(Color.WHITE);

            jpWest = new IIRMSFloorVisualization();
            
            bQuery = new JButton("Query Interface");
            bQuery.setToolTipText("Click here for querying");
            bQuery.setBounds(650, 25, 200, 25);
            bQuery.setBackground(Color.PINK);
            bQuery.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                	try {
                            new IIRMSQuery();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            
            

            jpEast = new JPanel();
            jpEast.setLayout(null);
            jpEast.setBackground(Color.WHITE);
            jpEast.setLocation(1350, 100);
            //jpEast.add(hi);
            //jpEast.add(btn);
            //jpEast.add(bNavigation);
            jpEast.setSize(200, 1000);

            jpCenter = new JPanel();
            jpCenter.setLayout(null);
            jpCenter.setBackground(Color.WHITE);
            
            
            jpCenter.setLocation(220, 100);
            jpCenter.setSize(1280, 700);
            jpCenter.add(visualPanel).setSize(1280, 700);
            jpWest.setAutoscrolls(true);
            sPane = new JScrollPane(jpWest, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            sPane.setSize(220, 700);
            sPane.setLocation(0, 100);
            sPane.setPreferredSize(jpWest.getPreferredSize());
            sPane.setAutoscrolls(true);

            jpParent.add(jpCenter);
            //jpParent.add(panel).setSize(1200,700);
            jpParent.add(bQuery);
            ltext=new JLabel();
            text1="Click checkboxes on the leftside to view entities in the building";
            ltext.setText(text1);
            ltext.setBounds(530, 60, 1000, 20);
            jpParent.add(ltext);
            jpParent.add(sPane);
            jf.add(jpParent);
            
            jf.setTitle("Visualization Window of " + IIRMSQueryInterface.dbNameSelected);
            jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
            URL url=new URL("http://172.17.9.60/html/pics/help.png");
            ImageIcon icon = new ImageIcon(url);
            Image helpImg = icon.getImage();
            helpImg=helpImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            helpIcon=new ImageIcon(helpImg);
            JLabel lhelp=new JLabel(helpIcon);
            lhelp.setBounds(1200, 30, 30, 30);
            lhelp.setToolTipText("<html><center>This tool helps to View the Building.</center>"
            		+ "1. Click the checkbox in the leftside to view entity."
                    + "<br/>2. Scroll the Mouse to zoom in/out."
                    + "<br/>3. Drag the image in the visual using mouse to rotate the building up/down/left/right </html>");
            jpParent.add(lhelp);
//back button
            url=new URL("http://172.17.9.60/html/pics/back.png");
            ImageIcon icon5=new ImageIcon(url);
            Image img5=icon5.getImage();
            img5=img5.getScaledInstance(80, 50, Image.SCALE_SMOOTH);
            img_back=new ImageIcon(img5);
            
            back = new JButton("back",img_back);
            back.setVerticalTextPosition(SwingConstants.CENTER);
            back.setHorizontalTextPosition(SwingConstants.RIGHT);
            //Databaseadmin_shptiposgres.setBounds(230, 30, 210, 180);
            back.setBounds(50, 20, 80, 50);
            back.setToolTipText("Click to go previous window");

            //Databaseadmin_shptiposgres.setBackground(blue);
        	//action listener
            back.addActionListener(new ActionListener() {
                 @Override
                 public void actionPerformed(ActionEvent ae) {
                     try {
                    	 
                    	 jf.dispose();
                    	
                     } catch (Exception ex) {
                         ex.printStackTrace();
                     }
                 }
             });
            jpParent.add(back);
            
            jf.add(jpParent);
            
            jf.getContentPane().add(jpParent, BorderLayout.CENTER);
            jf.pack();
            jf.setVisible(true);
            
            jf.addWindowListener(new WindowListener() {
                @Override
                public void windowOpened(WindowEvent e) {
                    jf.setVisible(true);
                    jf.setEnabled(true);
                }

                @Override
                public void windowClosing(WindowEvent e) {
                    jf.setVisible(false);
                    jf.setEnabled(false);
                    IIRMSFloorVisualization.floorList.clear();
                    IIRMSVisualization.pathList.clear();
                }

                @Override
                public void windowClosed(WindowEvent e) {
                    jf.setVisible(false);
                    jf.setEnabled(false);
                    IIRMSQueryInterface.dbNameSelected = null;
                    IIRMSFloorVisualization.floorList.clear();
                    IIRMSVisualization.pathList.clear();
                }

                @Override
                public void windowIconified(WindowEvent e) {
                    jf.setVisible(true);
                    jf.setEnabled(true);
                }

                @Override
                public void windowDeiconified(WindowEvent e) {
                    jf.setVisible(true);
                    jf.setEnabled(true);
                }

                @Override
                public void windowActivated(WindowEvent e) {
                    jf.setVisible(true);
                    jf.setEnabled(true);
                    visualPanel.setFocusable(true);
                    visualPanel.requestFocusInWindow();
                   
                }

                @Override
                public void windowDeactivated(WindowEvent e) {
                    jf.setEnabled(false);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
         jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
}