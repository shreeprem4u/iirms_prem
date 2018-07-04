package com.amrita.IIRMS.IndoorGML.Graph;
/*
 * File Name        : IIRMSVisualization.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Sindhya Kumari N (Junior Research Fellow)
 * Last Modified    : the 28th of March , 2015
 * Purpose          : Class to display the 3D view for the Graph visualization.
 */
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES1;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLJPanel;
import javax.media.opengl.glu.GLU;

import com.jogamp.newt.Window;

public class IndoorGML_GraphVisualization extends GLJPanel implements GLEventListener, KeyListener, MouseListener, MouseMotionListener,MouseWheelListener {

	
    public static Connection dbConn;    
    public static int x_prev, y_prev;
    private float rotateX, rotateY, rotateZ;
    public double zoom;
    public static int transValue;
    public static List<String> pathList = new ArrayList<>();
    public static Map<String, Method> graphMethodMap = new HashMap<String, Method>();
    public static int floorNumber;
    
	public IndoorGML_GraphVisualization() throws Exception {

        this.setSize(getPreferredSize());
        Db_Connection();
        constructHashMap();
        addGLEventListener(this);
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        rotateX = 0;
        rotateY = 0;
        rotateZ = 0;
        zoom = 0.5;
        transValue=10000;
       
    }
	
	/*
     * Method name              :   constructHashMap 
     * Method description       :   Method to construct hash map for visualizations and query
     * 								visualMethodMap	---> To map the selected indoor entity with the functions used to draw specific indoor entity
     * 								queryMethodMap	---> To map the query and the its functions to visualize the query
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    

    private void constructHashMap() throws Exception{
    	
    	graphMethodMap.put("2D_W", IndoorGML_GraphVisualization.class.getMethod("drawWall_2D", GL2.class,int.class));
    	graphMethodMap.put("2D_R", IndoorGML_GraphVisualization.class.getMethod("drawRoom_2D", GL2.class,int.class));
    	graphMethodMap.put("Adj_", IndoorGML_GraphVisualization.class.getMethod("drawAdjacencyGraph", GL2.class,int.class));
    	graphMethodMap.put("Con_", IndoorGML_GraphVisualization.class.getMethod("drawConnectivityGraph", GL2.class,int.class));
        graphMethodMap.put("Zon_", IndoorGML_GraphVisualization.class.getMethod("drawConstraintsGraph",GL2.class,int.class));

	}

	
	public void Db_Connection() throws Exception {
        Class.forName("org.postgresql.Driver");
        dbConn = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/" + IndoorGML_GraphInterface.dbNameSelected, "researcher", "researcher");
    }
	
	
	
	@Override
	public void init(GLAutoDrawable drawable) {
		
		int w = 1200, h = 700;
        float aspect = w / h;
        GL2 gl = drawable.getGL().getGL2();
        GLU glu = new GLU();

        gl.glViewport(50, 20 , w , h );
        gl.glClearDepth(2.0f);
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glDepthFunc(GL.GL_LEQUAL);
        gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
        gl.glEnable(GL2.GL_LINE_SMOOTH);
        gl.glHint(GL2.GL_LINE_SMOOTH_HINT, GL2.GL_DONT_CARE);
        gl.glLineWidth(0.1f);
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        gl.glClearColor(0.737f, 0.561f, 0.561f, 0.0f);
        
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        //gl.glTranslatef(-0.5f, 0f, 0f);
        gl.glOrtho(-zoom * aspect, zoom * aspect, -zoom, zoom, 1.0, -1.0);
        //gl.glTranslated(0.03f, -0.07f, 0.0f);
        //gl.glTranslatef(0.1f, -0.1f, 0f);
        //gl.glOrtho(-0.1 * aspect, 0.1 * aspect, -0.1, 0.1, 1.0, -1.0);
        //gl.glOrtho(-0.07 * aspect, 0.07* aspect, -0.15, 0.12, 1.0, -1.0);
        //gl.glFrustum(-0.1, 0.1, -0.1, 0.1, 1, 1);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(-1.5f, -0.6f, 0f);
        glu.gluLookAt( 0.09, 0.0, 0.0, 0.1, 0.1, -0.3, 0.0, 3.0, 0.0 );
        
		
	}

	@Override
	public void dispose(GLAutoDrawable drawable) {
		
		
	}

	@Override
	public void display(GLAutoDrawable drawable) {
		
        int w = 1200, h = 700;
        float aspect = w / h;

        GL2 gl = drawable.getGL().getGL2();
       
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glDepthFunc(GL2.GL_LEQUAL);
        gl.glClearDepth(2.0);

        //gl.glTranslatef(0.03f, -0.07f, 0.0f);
        //glu.gluLookAt(0.08, 0.0, 0.0, 0.1, 0.1, -0.1, 0.0, 1.0, 0.0);
        
        gl.glOrtho(-zoom * aspect, zoom * aspect, -zoom, zoom, 1.0, -1.0);
        
        //gl.glTranslatef(0.05f,0.01f,0.0f);
        gl.glPushMatrix();
        gl.glRotatef(rotateX, 1, 0, 0);
        gl.glRotatef(rotateY, 0, 1, 0);
        gl.glRotatef(rotateZ, 0, 0, 1);
        
        
        try {
       	
           if (!pathList.isEmpty()) {
               for (int i = 0; i < pathList.size(); i++) {
                   String path = pathList.get(i);
                   // System.out.println("display:" + path);
                   if (path.equals("clear")) {
                       graphMethodMap.get(path).invoke(null, gl);
                   } else {
                       String floorValue = path.replace("3D", "").replace("2D", "").replaceAll("[^0-9]", "");
                       String hashPath = path.substring(0, 4);
                       floorNumber = Integer.parseInt(floorValue);
                       graphMethodMap.get(hashPath).invoke(null, gl,floorNumber);
                   }
               }
           }
       } catch (Exception e) {
           e.printStackTrace();
       }
        
        gl.glPopMatrix(); 
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		
		
	}
	
	
	public static void addPathAndRepaint(String addPath) throws Exception {
        if (!(pathList.contains(addPath))) {
            pathList.add(addPath);
            //System.out.println("SetPath:" + pathList);
            IndoorGML_GraphMainVisualization.graphPanel.repaint();
        }
    }

    public static void removePathAndRepaint(String removePath) throws Exception {
        if (pathList.contains(removePath)) {
            pathList.remove(removePath);
            //System.out.println("removed:" + pathList);
            IndoorGML_GraphMainVisualization.graphPanel.repaint();
        }
    }
	
	
	/*
     * Method name              :   drawWall_2D
     * Method description       :   method to display 2D walls
     * Method Arguments         :   (GL2) gl,floorNum
     * Arguments description    :   gl ---> To initialize the OpenGL graphics context
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void drawWall_2D(GL2 gl,int floorNum) throws Exception {
    	
        String query1 = "select st_astext(the_geom) from wall where floor_id="+floorNum;
        Statement stmt1 = dbConn.createStatement();
        ResultSet rs1 = stmt1.executeQuery(query1);
        String[] w_pointIn = null;

        List<Float> w_pointList;
        while (rs1.next()) {
            w_pointIn = rs1.getString(1).replace("MULTILINESTRING", "").replace("LINESTRING", "").replace(",", " ").replace("(", "").replace(")", "").split(" ");
            w_pointList = new ArrayList<Float>();
            for (int i = 0; i < w_pointIn.length; i = i + 2) {
                float xPoint = Float.parseFloat(w_pointIn[i]);
                float yPoint = Float.parseFloat(w_pointIn[i + 1]);
                w_pointList.add(xPoint);
                w_pointList.add(yPoint);
            }
            gl.glBegin(GL2.GL_LINES);
            gl.glClearColor(0, 0, 0, 0);
            gl.glColor3f(0f, 0f, 0f);
            for (int j = 0; j < w_pointList.size(); j = j + 2) {
                gl.glVertex3f(w_pointList.get(j) / transValue, floorNum * 0.04f, w_pointList.get(j + 1) / transValue);
            }
            gl.glColor3f(1f, 1f, 1f);
            gl.glEnd();
            gl.glFlush();
        }
    }
    
    /*
     * Method name              :   drawRoom_2D
     * Method description       :   method to display 2D rooms
     * Method Arguments         :   (GL2) gl
     * Arguments description    :   gl ---> To initialize the OpenGL graphics context
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void drawRoom_2D(GL2 gl,int floorNum) throws Exception {

        String query2 = "select st_astext(the_geom),room_type from room where floor_id="+floorNum;
        Statement stmt2 = dbConn.createStatement();
        ResultSet rs2 = stmt2.executeQuery(query2);
        String[] r_pointIn = null;
        List<Float> r_pointList;
        while (rs2.next()) {
            r_pointIn = rs2.getString(1).replace("MULTIPOLYGON", "").replace("POLYGON", "").replace(",", " ").replace("(", "").replace(")", "").split(" ");
            String roomType = rs2.getString(2);
            if (!roomType.contains("Corridor")) {
                r_pointList = new ArrayList<Float>();
                for (int i = 0; i < r_pointIn.length; i = i + 2) {
                    float xPoint = Float.parseFloat(r_pointIn[i]);
                    float yPoint = Float.parseFloat(r_pointIn[i + 1]);
                    r_pointList.add(xPoint);
                    r_pointList.add(yPoint);
                }
                gl.glBegin(GL2.GL_POLYGON);
                gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                gl.glColor3f(0.20f, 0.20f, 0.20f);
                for (int j = 0; j < r_pointList.size(); j = j + 2) {
                    gl.glVertex3f(r_pointList.get(j) / transValue, floorNum * 0.04f, r_pointList.get(j + 1) / transValue);
                }
                gl.glColor3f(1f, 1f, 1f);
                gl.glEnd();
                gl.glFlush();
            }
        }
    }
    
    /*
     * Method name              :   drawState
     * Method description       :   method to display the states of the IndoorGML graph
     * Method Arguments         :   (GL2) gl
     * Arguments description    :   gl ---> To initialize the OpenGL graphics context
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    
    public static void drawState(GL2 gl,int floorNum) throws Exception
    {
    	
    	String queryState = "select st_astext(the_geom),state_id from state_indoorgml where floor_id="+floorNum;
        Statement stmtState = dbConn.createStatement();
        ResultSet rsState = stmtState.executeQuery(queryState);
        String[] s_pointIn = null;
        List<Float> s_pointList;
        while (rsState.next()) {
            s_pointIn = rsState.getString(1).replace("POINT", "").replace(",", " ").replace("(", "").replace(")", "").split(" ");
            String stateId=rsState.getString(2);
                s_pointList = new ArrayList<Float>();
                for (int i = 0; i < s_pointIn.length; i = i + 2) {
                    float xPoint = Float.parseFloat(s_pointIn[i]);
                    float yPoint = Float.parseFloat(s_pointIn[i + 1]);
                    s_pointList.add(xPoint);
                    s_pointList.add(yPoint);
                }
                gl.glPointSize(8.0f);
                gl.glBegin(GL2.GL_POINTS);
                //gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
                if(stateId.contains("S_R")){
                	//yellow
                    gl.glColor3f(1.0f, 1.0f, 0f);	
                }else if(stateId.contains("S_D")){
                	//cyan
                	gl.glColor3f(0.0f, 1.0f, 1.0f);
                }else if(stateId.contains("S_T")){
                	//magenta
                	gl.glColor3f(1.0f, 0.0f, 1.0f);
                }
                
                for (int j = 0; j < s_pointList.size(); j = j + 2) {
                	
                    gl.glVertex3f(s_pointList.get(j) / transValue, floorNum * 0.04f, s_pointList.get(j + 1) / transValue);
                }
                //gl.glColor3f(1f, 1f, 1f);
                gl.glEnd();
                gl.glFlush();
        }	
    }
    
    /*
     * Method name              :   drawAdjacencyGraph
     * Method description       :   method to display the adjacency graph
     * Method Arguments         :   (GL2) gl
     * Arguments description    :   gl ---> To initialize the OpenGL graphics context
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    
    public static void drawAdjacencyGraph(GL2 gl,int floorNum) throws Exception
    {
    	drawState(gl,floorNum);
    	
    	String query1 = "select st_astext(the_geom) from transition_indoorgml where relation='adjacency' and src_floor_id="+floorNum;
        Statement stmt1 = dbConn.createStatement();
        ResultSet rs1 = stmt1.executeQuery(query1);
        String[] w_pointIn = null;

        List<Float> w_pointList;
        while (rs1.next()) {
            w_pointIn = rs1.getString(1).replace("MULTILINESTRING", "").replace("LINESTRING", "").replace(",", " ").replace("(", "").replace(")", "").split(" ");
            w_pointList = new ArrayList<Float>();
            for (int i = 0; i < w_pointIn.length; i = i + 2) {
                float xPoint = Float.parseFloat(w_pointIn[i]);
                float yPoint = Float.parseFloat(w_pointIn[i + 1]);
                w_pointList.add(xPoint);
                w_pointList.add(yPoint);
            }
            gl.glBegin(GL2.GL_LINES);
            gl.glClearColor(0, 0, 0, 0);
            gl.glColor3f(1f, 0f, 0f);
            for (int j = 0; j < w_pointList.size(); j = j + 2) {
                gl.glVertex3f(w_pointList.get(j) / transValue, floorNum * 0.04f, w_pointList.get(j + 1) / transValue);
            }
            gl.glColor3f(1f, 1f, 1f);
            gl.glEnd();
            gl.glFlush();
        }	
    }
    
    /*
     * Method name              :   drawConnectivityGraph
     * Method description       :   method to display the connectivity graph
     * Method Arguments         :   (GL2) gl
     * Arguments description    :   gl ---> To initialize the OpenGL graphics context
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    
    public static void drawConnectivityGraph(GL2 gl,int floorNum) throws Exception
    {
    	drawState(gl,floorNum);
    	
    	String query1 = "select st_astext(the_geom),src_floor_id,dest_floor_id from transition_indoorgml where relation='connectivity' and src_floor_id="+floorNum;
        Statement stmt1 = dbConn.createStatement();
        ResultSet rs1 = stmt1.executeQuery(query1);
        String[] w_pointIn = null;

        List<Float> w_pointList;
        while (rs1.next()) {
            w_pointIn = rs1.getString(1).replace("MULTILINESTRING", "").replace("LINESTRING", "").replace(",", " ").replace("(", "").replace(")", "").split(" ");
            int src_floorId = rs1.getInt(2);
            int dest_floorId=rs1.getInt(3);
            w_pointList = new ArrayList<Float>();
            for (int i = 0; i < w_pointIn.length; i = i + 2) {
                float xPoint = Float.parseFloat(w_pointIn[i]);
                float yPoint = Float.parseFloat(w_pointIn[i + 1]);
                w_pointList.add(xPoint);
                w_pointList.add(yPoint);
            }
            
            	gl.glBegin(GL2.GL_LINES);
            	gl.glClearColor(0, 0, 0, 0);
            	gl.glColor3f(0f, 1f, 0f);
            	gl.glVertex3f(w_pointList.get(0) / transValue, src_floorId * 0.04f, w_pointList.get(1) / transValue);
            	gl.glVertex3f(w_pointList.get(2) / transValue, dest_floorId * 0.04f, w_pointList.get(3) / transValue);
            	gl.glColor3f(1f, 1f, 1f);
            	gl.glEnd();
            	gl.glFlush();
        }
    }
    
    /*
     * Method name              :   drawConstraintsGraph
     * Method description       :   method to display the constraints graph
     * Method Arguments         :   (GL2) gl
     * Arguments description    :   gl ---> To initialize the OpenGL graphics context
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
	
    public static void drawConstraintsGraph(GL2 gl,int floorNum) throws Exception
    {
    	
    	drawState(gl,floorNum);
    	String query1 = "select st_astext(the_geom),src_floor_id,dest_floor_id from transition_indoorgml where zonal_constraint='accessible' and src_floor_id="+floorNum;
        Statement stmt1 = dbConn.createStatement();
        ResultSet rs1 = stmt1.executeQuery(query1);
        String[] w_pointIn = null;

        List<Float> w_pointList;
        while (rs1.next()) {
            w_pointIn = rs1.getString(1).replace("MULTILINESTRING", "").replace("LINESTRING", "").replace(",", " ").replace("(", "").replace(")", "").split(" ");
            int src_floorId = rs1.getInt(2);
            int dest_floorId=rs1.getInt(3);
            w_pointList = new ArrayList<Float>();
            for (int i = 0; i < w_pointIn.length; i = i + 2) {
                float xPoint = Float.parseFloat(w_pointIn[i]);
                float yPoint = Float.parseFloat(w_pointIn[i + 1]);
                w_pointList.add(xPoint);
                w_pointList.add(yPoint);
            }
            
            	gl.glBegin(GL2.GL_LINES);
            	gl.glClearColor(0, 0, 0, 0);
            	gl.glColor3f(1.5f, 1.5f, 0f);
            	gl.glVertex3f(w_pointList.get(0) / transValue, src_floorId * 0.04f, w_pointList.get(1) / transValue);
            	gl.glVertex3f(w_pointList.get(2) / transValue, dest_floorId * 0.04f, w_pointList.get(3) / transValue);
            	gl.glColor3f(1f, 1f, 1f);
            	gl.glEnd();
            	gl.glFlush();
        }
        
    	
    }
    
    
	
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		
		int mouseCount=e.getWheelRotation();
		if(mouseCount==1)
		{
			zoom+=0.02;
		}
		else
		{
			zoom-=0.02;
		}
		repaint();
		
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int x = e.getX();
        int y = e.getY();
        
        int width = 0, height = 0;
		Object source = e.getSource();
		if (source instanceof Window) {
			Window window = (Window) source;
			width = window.getSurfaceWidth();
			height = window.getSurfaceHeight();
		} else if (source instanceof GLAutoDrawable) {
			GLAutoDrawable glad = (GLAutoDrawable) source;
			width = glad.getSurfaceWidth();
			height = glad.getSurfaceHeight();
		} else if (GLProfile.isAWTAvailable() && source instanceof java.awt.Component) {
			java.awt.Component comp = (java.awt.Component) source;
			width = comp.getWidth();
			height = comp.getHeight();
		}else {
			throw new RuntimeException("Event source neither Window nor Component: " + source);
		}
        
        float thetaY = 360.0f * ((float) (x - x_prev) / (float) width);
		float thetaX = 360.0f * ((float) (y_prev - y) / (float) height);
		
		x_prev = x;
		y_prev = y;
		rotateX += thetaX;
		rotateY += thetaY;

        repaint();
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		this.setFocusable(true);
        this.requestFocus();
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		x_prev = e.getX();
        y_prev = e.getY();
		
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

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            rotateY -= 10;
        } else if (key == KeyEvent.VK_RIGHT) {
            rotateY += 10;
        } else if (key == KeyEvent.VK_DOWN) {
            rotateX += 10;
        } else if (key == KeyEvent.VK_UP) {
            rotateX -= 10;
        } else if (key == KeyEvent.VK_PAGE_UP) {
            zoom -= 0.02;
        } else if (key == KeyEvent.VK_PAGE_DOWN) {
            zoom += 0.02;
        }
        repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

}
