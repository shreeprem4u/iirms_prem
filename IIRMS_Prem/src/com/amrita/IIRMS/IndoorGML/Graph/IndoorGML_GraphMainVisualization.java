package com.amrita.IIRMS.IndoorGML.Graph;
/*
 * File Name        : IndoorGML_GraphMainVisualization.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Sindhya Kumari N (Junior Research Fellow)
 * Last Modified    : the 26th of March, 2015
 * Purpose          : Class to display the Graph Visualization window with options for floors 
 *                    and the 3D Jpanel  and Drag-And-Drop window.
 */
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;

import com.amrita.IIRMS.IIRMSApplicationInterface;
import com.amrita.IIRMS.IIRMSQuery;
import com.amrita.IIRMS.Visualization.IIRMSFloorVisualization;
import com.amrita.IIRMS.Visualization.IIRMSVisualization;

public class IndoorGML_GraphMainVisualization {
	

    public static JFrame jf;
    public static JPanel jpParent, jpEast, jpCenter, jpWest,jpWestBottom;
    public static JScrollPane sPane;
    public static IndoorGML_GraphVisualization graphPanel;
	private JLabel ltext,lTitle;
	private String text1;
	private ImageIcon helpIcon;
	private ImageIcon img_back;
	private JButton back;
   
    public IndoorGML_GraphMainVisualization()throws Exception {
        graphPanel = new IndoorGML_GraphVisualization();
       
        try {
            jf = new JFrame();
            jpParent = new JPanel();
            jpParent.setLayout(null);
            jpParent.setBackground(Color.WHITE);
            
            lTitle=new JLabel("IndoorGML Graph Display");
            lTitle.setBounds(560, 30, 350, 20);
            lTitle.setFont(new Font("Serif",Font.BOLD,20));
            URL url= new URL("http://172.17.9.60/html/pics/help.png");
            ImageIcon icon = new ImageIcon(url);
    		Image helpImg = icon.getImage();
            helpImg=helpImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            helpIcon=new ImageIcon(helpImg);
            JLabel lhelp=new JLabel(helpIcon);
            lhelp.setBounds(1200, 30, 30, 30);
            lhelp.setToolTipText("<html><center>This tool helps to View the Graph created using IndoorGML.</center>"
            		+ "1. Click the checkbox in the leftside to view entity."
                    + "<br/>2. Scroll the Mouse to zoom in/out."
                    + "<br/>3. Drag the image in the visual using mouse to rotate the building up/down/left/right </html>");

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
                    	 IndoorGML_GraphInterface.dbNameSelected= null;
                         IndoorGML_Layering.floorList.clear();
                         IndoorGML_GraphVisualization.pathList.clear();
                         jf.dispose();
                     } catch (Exception ex) {
                         ex.printStackTrace();
                     }
                 }
             });
           
            
            jpWest = new IndoorGML_Layering();
            
            jpWestBottom=new DrawPanel();
            jpWestBottom.setSize(220, 350);
            jpWestBottom.setLocation(0, 500);
            jpWestBottom.setBackground(Color.WHITE);
            
            jpEast = new JPanel();
            jpEast.setLayout(null);
            jpEast.setBackground(Color.WHITE);
            jpEast.setLocation(1350, 100);
            jpEast.setSize(200, 1000);

            jpCenter = new JPanel();
            jpCenter.setLayout(null);
            jpCenter.setBackground(Color.WHITE);
            
            
            jpCenter.setLocation(220, 100);
            
            jpCenter.setSize(1280, 700);
            jpCenter.add(graphPanel).setSize(1280, 700);
            jpWest.setAutoscrolls(true);
            sPane = new JScrollPane(jpWest, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            sPane.setSize(220, 350);
            sPane.setToolTipText("Click the checkbox to view the graph");
            sPane.setLocation(0, 100);
            sPane.setPreferredSize(jpWest.getPreferredSize());
            sPane.setAutoscrolls(true);
            ltext=new JLabel();
            text1="Click checkboxes on the leftside to view Graphs generated from the IndoorGML document";
            ltext.setText(text1);
            ltext.setBounds(460, 60, 1000, 20);
            jpParent.add(lTitle);
            jpParent.add(lhelp);
            jpParent.add(ltext);
            jpParent.add(jpCenter);
            jpParent.add(sPane);
            jpParent.add(back);
            jpParent.add(jpWestBottom);
            jf.add(jpParent);
            jf.setTitle(" Graph Visualization Window of " + com.amrita.IIRMS.IndoorGML.Graph.IndoorGML_GraphInterface.dbNameSelected);
            jf.setExtendedState(JFrame.MAXIMIZED_BOTH);
            
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
                    IndoorGML_GraphInterface.dbNameSelected= null;
                    IndoorGML_Layering.floorList.clear();
                    IndoorGML_GraphVisualization.pathList.clear();
                }

                @Override
                public void windowClosed(WindowEvent e) {
                    jf.setVisible(false);
                    jf.setEnabled(false);
                    
                    IndoorGML_GraphInterface.dbNameSelected= null;
                    IndoorGML_Layering.floorList.clear();
                    IndoorGML_GraphVisualization.pathList.clear();
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
                    graphPanel.setFocusable(true);
                    graphPanel.requestFocusInWindow(); 
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
class DrawPanel extends JPanel
{
	
	public DrawPanel()
	{   this.setBounds(0, 400, 200, 450); 
		this.setToolTipText("Representation Details");
		
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g.drawString("STATES", 10, 10);
		Stroke stroke = new BasicStroke(4f);
	    g2d.setStroke(stroke);
		//drawing rectangle
	    g.setColor(Color.yellow);
	    g.fillRect (10, 20, 20, 20);
	    g.setColor(Color.cyan);
	    g.fillRect (10, 50, 20, 20);
	    g.setColor(Color.magenta);
	    g.fillRect (10, 80, 20, 20);
	    g.setColor(Color.red);
	    g.drawLine (5, 150, 30, 150);
	    g.setColor(Color.green);
	    g.drawLine (5, 180, 30, 180);
	    g.setColor(Color.yellow);
	    g.drawLine (5, 210, 30, 210);
	    g.setColor(Color.BLACK);
	    //g.fillRect(, y, width, height);
	    g.setColor(Color.BLACK);
	    
	    g.drawString("TRANSITION", 10, 130);
	    //drawing outline and text
	    stroke = new BasicStroke(2f);
	    g2d.setStroke(stroke);
	    g.drawString("ROOM", 40, 35);
	    g.drawRect (10, 20, 20, 20);
	    g.drawString("DOOR", 40, 65);
	    g.drawRect (10, 50, 20, 20);
	    g.drawString("STAIR", 40, 95);
	    g.drawRect(10, 80, 20, 20);
	    g.drawString("ADJACENCY", 40, 155);
	    g.drawString("CONNECTIVITY", 40, 185);
	    g.drawString("CONSTRAINT", 40, 215);
	}	
}