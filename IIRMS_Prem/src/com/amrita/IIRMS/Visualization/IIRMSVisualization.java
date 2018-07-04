package com.amrita.IIRMS.Visualization;


/*
 * File Name        : IIRMSVisualization.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Sindhya Kumari N (Junior Research Fellow)
 * Last Modified    : the 28th of March , 2015
 * Purpose          : Class contain the functionality of 2D and 3D View for the visualization.
 */
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.lang.reflect.Method;
import java.net.URL;
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

import com.amrita.IIRMS.IIRMSMainVisualization;
import com.amrita.IIRMS.IIRMSQuery;
import com.jogamp.newt.Window;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.WKTReader;

public class IIRMSVisualization extends GLJPanel implements GLEventListener, KeyListener, MouseListener, MouseMotionListener,MouseWheelListener {

    private static final long serialVersionUID = 1L;
    public static Connection dbConn;
    public static Statement stmt;
    public static ResultSet rs;
    public static List<Float> floorList = new ArrayList<Float>();
    public static List<String> pathList = new ArrayList<>();
    public static int x_prev, y_prev;
    private float rotateX, rotateY, rotateZ;
    public double zoom;
	private static TextRenderer txtRenderer;
	public static int wallTexture,doorTexture,floorTexture,windowTexture,sen_rfid_Texture,sen_temp_Texture,sen_wifi_Texture,sen_cam_Texture,stairTexture;
    public static Map<String, Method> visualMethodMap = new HashMap<String, Method>();
    public static Map<Integer, Method> queryMethodMap = new HashMap<Integer, Method>();
    public static int queryHashNum = 0;
    public static int floorNumber;
    public static int transValue;
    public static URL sensorurl;
	public static String text=null;
    

    /*
     * Method name              :   IIRMSVisualization
     * Method description       :   Constructor to add the jogl,mouse and key listener events
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   null
     * Return type description  :   --
     */
    public IIRMSVisualization() throws Exception {

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
        if(IIRMSQueryInterface.dbNameSelected.contains("cse"))
        {
        //transValue=transformVal();
        transValue=10000;
        //System.out.println("trans:"+transValue);
        
        }
        else
        {
        	transValue=10000;
        }
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
    	
    	visualMethodMap.put("3D_W", IIRMSVisualization.class.getMethod("drawWall_3D",GL2.class,int.class));
        visualMethodMap.put("3D_R", IIRMSVisualization.class.getMethod("drawRoom_3D", GL2.class,int.class));
        visualMethodMap.put("3D_D", IIRMSVisualization.class.getMethod("drawDoor_3D", GL2.class,int.class));
        visualMethodMap.put("3D_N", IIRMSVisualization.class.getMethod("drawWindow_3D", GL2.class,int.class));
        visualMethodMap.put("3D_S", IIRMSVisualization.class.getMethod("drawStair_3D", GL2.class,int.class));
        visualMethodMap.put("3D_E", IIRMSVisualization.class.getMethod("drawSensors_3D", GL2.class,int.class));
        visualMethodMap.put("2D_W", IIRMSVisualization.class.getMethod("drawWall_2D", GL2.class,int.class));
        visualMethodMap.put("2D_R", IIRMSVisualization.class.getMethod("drawRoom_2D", GL2.class,int.class));
        visualMethodMap.put("2D_D", IIRMSVisualization.class.getMethod("drawDoor_2D", GL2.class,int.class));
        visualMethodMap.put("2D_N", IIRMSVisualization.class.getMethod("drawWindow_2D", GL2.class,int.class));
        visualMethodMap.put("2D_S", IIRMSVisualization.class.getMethod("drawStair_2D", GL2.class,int.class));
        visualMethodMap.put("2D_E", IIRMSVisualization.class.getMethod("drawSensors_2D", GL2.class,int.class));
        
        
        queryMethodMap.put(1, IIRMSQuery.class.getMethod("dispRoomsOfFloor", GL2.class));
        queryMethodMap.put(2, IIRMSQuery.class.getMethod("dispRooms", GL2.class));
        queryMethodMap.put(3, IIRMSQuery.class.getMethod("dispRoomsOfVolume", GL2.class));
        queryMethodMap.put(4, IIRMSQuery.class.getMethod("dispRoomsOfArea", GL2.class));
        queryMethodMap.put(5, IIRMSQuery.class.getMethod("dispRoomsOfType", GL2.class));
        queryMethodMap.put(6, IIRMSQuery.class.getMethod("dispRoomsByDistance", GL2.class));
        queryMethodMap.put(7, IIRMSQuery.class.getMethod("dispAdjacentRooms", GL2.class));
        queryMethodMap.put(8, IIRMSQuery.class.getMethod("dispNearestRooms", GL2.class));
        queryMethodMap.put(9, IIRMSQuery.class.getMethod("dispExitOfRoom", GL2.class));
        queryMethodMap.put(10, IIRMSQuery.class.getMethod("dispRoomsByExit", GL2.class));
        queryMethodMap.put(11, IIRMSQuery.class.getMethod("dispInnerWalls", GL2.class));
        queryMethodMap.put(12, IIRMSQuery.class.getMethod("dispWallsOfFloor", GL2.class));
        queryMethodMap.put(13, IIRMSQuery.class.getMethod("dispCommonWallsOfFloor", GL2.class));
        queryMethodMap.put(14, IIRMSQuery.class.getMethod("dispWallsOfWindow", GL2.class));
        queryMethodMap.put(15, IIRMSQuery.class.getMethod("dispWallsOfType", GL2.class));
        queryMethodMap.put(21, IIRMSQuery.class.getMethod("dispInwardDoors", GL2.class));
        queryMethodMap.put(22, IIRMSQuery.class.getMethod("dispOutwardDoors", GL2.class));
        queryMethodMap.put(23, IIRMSQuery.class.getMethod("dispDoorsOfType", GL2.class));
        queryMethodMap.put(24, IIRMSQuery.class.getMethod("dispSingleDoors", GL2.class));
        queryMethodMap.put(25, IIRMSQuery.class.getMethod("dispDoubleDoors", GL2.class));
        queryMethodMap.put(31, IIRMSQuery.class.getMethod("dispMaterialWindows", GL2.class));
        queryMethodMap.put(32, IIRMSQuery.class.getMethod("disptypeWindows", GL2.class));
        queryMethodMap.put(35, IIRMSQuery.class.getMethod("visualizeNavigation", GL2.class));
        queryMethodMap.put(36, IIRMSQuery.class.getMethod("dispDistBetweenRooms", GL2.class));
        queryMethodMap.put(37, IIRMSQuery.class.getMethod("dispWindowsOfRoom", GL2.class));
        queryMethodMap.put(38,IIRMSQuery.class.getMethod("dispStairsOfFloor", GL2.class));
       // queryMethodMap.put(39,IIRMSNavigation.class.getMethod("visualizeNavigation", GL2.class));

		
	}



	/*
     * Method name              :  Db_Connection 
     * Method description       :   Method to establish connection with the database
     * Method Arguments         :   null
     * Arguments description    :   --
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public void Db_Connection() throws Exception {
        Class.forName("org.postgresql.Driver");
        dbConn = DriverManager.getConnection("jdbc:postgresql://172.17.9.60:5432/" + IIRMSQueryInterface.dbNameSelected, "researcher", "researcher");
    }

    /*
     * Method name              :   init
     * Method description       :   init method of GLEventListener to initialize the Jogl drawing.It is called by the drawable after the Jogl context is initialized. 
     * Method Arguments         :   (GLAutodrawable) drawable 
     * Arguments description    :   drawable ---> To initialialize the OpenGL graphics context
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public void init(GLAutoDrawable drawable) {
    	pathList.clear();
    	queryHashNum=0;
        int w = 1200, h = 700;
        //int w = 1450, h = 930;
        float aspect = w / h;
        GL2 gl = drawable.getGL().getGL2();
        GLU glu = new GLU();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
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
        //medium wood
        //gl.glClearColor(0.65f,0.50f,0.39f,0.0f);
        //gl.glClearColor(0.847f, 0.749f, 0.847f,0.0f);
        //gl.glClearColor(0.878f, 1.000f, 1.000f, 1.0f);
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
        
        try{
        	URL url = new URL("http://172.17.9.60/html/pics/wall1.JPG");
        	Texture text1 = TextureIO.newTexture(url,true,"jpg");
        	wallTexture = text1.getTextureObject(gl);

        	url = new URL("http://172.17.9.60/html/pics/door.bmp");
        	Texture text2 =TextureIO.newTexture(url,true,"bmp");
        	doorTexture = text2.getTextureObject(gl);

        	url = new URL("http://172.17.9.60/html/pics/window.png");
        	Texture text3 = TextureIO.newTexture(url,true,"png");
        	windowTexture = text3.getTextureObject(gl);

        	url = new URL("http://172.17.9.60/html/pics/floor.jpg");
        	Texture text4 = TextureIO.newTexture(url,true,"jpg");
        	floorTexture = text4.getTextureObject(gl);
        
        	url = new URL("http://172.17.9.60/html/pics/stair.jpeg");
           	Texture text5 = TextureIO.newTexture(url,true,"jpeg");
        	stairTexture = text5.getTextureObject(gl);
    
            url = new URL("http://172.17.9.60/html/pics/Temperature_sensor.png");
            Texture text6 = TextureIO.newTexture(url,true,"png");
            sen_temp_Texture=text6.getTextureObject(gl);
            
            
            url = new URL("http://172.17.9.60/html/pics/RFID_Reader.png");
            Texture text7 = TextureIO.newTexture(url,true,"png");
            sen_rfid_Texture=text7.getTextureObject(gl);
            
            url = new URL("http://172.17.9.60/html/pics/WIFI_Router.png");
            Texture text8 = TextureIO.newTexture(url,true,"png");
            sen_wifi_Texture=text8.getTextureObject(gl);
            
            url = new URL("http://172.17.9.60/html/pics/Camera.png");
            Texture text9 = TextureIO.newTexture(url,true,"png");
            sen_cam_Texture=text9.getTextureObject(gl);
        	
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
    }

    /*
     * Method name              :   display
     * Method description       :   display method of GLEventListener to render the Jogl graphics
     * Method Arguments         :   (GLAutodrawable) drawable 
     * Arguments description    :   drawable ---> To initialize the OpenGL graphics context 
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
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
        
        //gl.glTranslatef(-0.05f,-0.01f,0.0f);
        
        
        
        try {
        	 if (queryHashNum != 0) {
         		queryMethodMap.get(queryHashNum).invoke(null, gl);
         		}
        	
            if (!pathList.isEmpty()) {
                for (int i = 0; i < pathList.size(); i++) {
                    String path = pathList.get(i);
                    // System.out.println("display:" + path);
                    
                    if (path.equals("clear")) {
                        visualMethodMap.get(path).invoke(null, gl);
                    } else {
                    	String floorValue = path.replace("3D", "").replace("2D", "").replaceAll("[^0-9]", "");
                        String hashPath = path.substring(0, 4);
                        floorNumber = Integer.parseInt(floorValue);
                        visualMethodMap.get(hashPath).invoke(null, gl,floorNumber);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        gl.glPopMatrix(); 
    }

    /*
     * Method name              :   dispose
     * Method description       :   dispose method of GLEventListener to destroy the OpenGL context
     * Method Arguments         :   (GLAutodrawable) drawable 
     * Arguments description    :   drawable ---> To initialize the jogl graphics context 
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public void dispose(GLAutoDrawable drawable) {
    }

    /*
     * Method name              :   reshape
     * Method description       :   reshape method of GLEventListener is called when the user resizes the window
     * Method Arguments         :   (GLAutodrawable) drawable 
     * Arguments description    :   drawable ---> To initialize the OpenGL graphics context 
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    }

    public static void addPathAndRepaint(String addPath) throws Exception {
        if (!(pathList.contains(addPath))) {
            pathList.add(addPath);
           // System.out.println("SetPath:" + pathList);
            IIRMSMainVisualization.visualPanel.repaint();
        }
    }

    public static void removePathAndRepaint(String removePath) throws Exception {
        if (pathList.contains(removePath)) {
            pathList.remove(removePath);
            //System.out.println("removed:" + pathList);
            IIRMSMainVisualization.visualPanel.repaint();
        }
    }
    
    
    public static int transformVal() throws Exception
    {
    	float maxCoord = 0 ;
    	int length,trans=1;
		String queryFloor = "select st_xmax(the_geom) from floor where floor_id=1";
        Statement stmtFloor = dbConn.createStatement();
        ResultSet rsc = stmtFloor.executeQuery(queryFloor);
        while (rsc.next()) {
        	maxCoord=Float.parseFloat(rsc.getString(1));
        	//System.out.println("maxX:"+maxCoord);
        }
        Integer num = Math.round(maxCoord);
        length=num.toString().length();
        for(int i=0;i<length;i++)
        {
        	trans=trans*10;
        }
        return trans;
    }
    


    /*
     * Method name              :   drawFloor
     * Method description       :   method to display floor
     * Method Arguments         :   (GL2) gl,floorNum
     * Arguments description    :   gl ---> To initialize the OpenGL graphics context,
     * 								floorNum ---> floor Number
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void drawFloor(GL2 gl,int floorNum) throws Exception {
    	
            String query1 = "select st_astext(the_geom) from floor where floor_id=" + floorNum;
            Statement stmt1 = dbConn.createStatement();
            ResultSet rs1 = stmt1.executeQuery(query1);
            String[] w_pointIn = null;

            List<Float> w_pointList;
            while (rs1.next()) {
                w_pointIn = rs1.getString(1).replace("MULTILINESTRING", "").replace("LINESTRING", "").replace("POLYGON", "").replace(",", " ").replace("(", "").replace(")", "").split(" ");
                w_pointList = new ArrayList<Float>();
                for (int i = 0; i < w_pointIn.length; i = i + 2) {
                    float xPoint = Float.parseFloat(w_pointIn[i]);
                    float yPoint = Float.parseFloat(w_pointIn[i + 1]);
                    w_pointList.add(xPoint);
                    w_pointList.add(yPoint);
                }

                gl.glEnable(GL2.GL_TEXTURE_2D);
                gl.glBindTexture(GL2.GL_TEXTURE_2D, floorTexture);
                gl.glBegin(GL2.GL_POLYGON);
                gl.glColor3f(1f, 1f, 1f);
                float f2 = 0f;
                gl.glTexCoord2f(f2 * 1f, f2 * 0f);
                for (int j = 0; j < w_pointList.size(); j = j + 2) {
                    gl.glVertex3f(w_pointList.get(j) / transValue, floorNum*0.04f, w_pointList.get(j + 1) / transValue);         
                    f2 = 1f;
                    gl.glTexCoord2f(f2 * 1f, 1);
                }
                gl.glEnd();
                gl.glDisable(GL2.GL_TEXTURE_2D);
                gl.glFlush();
            }
    }

    /*
     * Method name              :   drawWall_2D
     * Method description       :   method to display 2D walls
     * Method Arguments         :   (GL2) gl,floorNum
     * Arguments description    :   gl ---> To initialize the OpenGL graphics context
     * 								floorNum ---> floor Number
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void drawWall_2D(GL2 gl,int floorNum) throws Exception {
    	
        String query1 = "select st_astext(the_geom) from wall where floor_id=" + floorNum;
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
     * Method name              :   drawWall_3D
     * Method description       :   method to display 3D walls
     * Method Arguments         :   (GL2) gl,floorNum
     * Arguments description    :   gl ---> To initialize the OpenGL graphics context
     * 								floorNum ---> floor Number
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void drawWall_3D(GL2 gl,int floorNum) throws Exception {
    
        drawFloor(gl,floorNum);
        String query1 = "select st_astext(the_geom) from wall where floor_id=" + floorNum;
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
            for(int i=0;i<w_pointList.size();i=i+4)
            {
            	gl.glEnable(GL2.GL_TEXTURE_2D);
            	gl.glBindTexture(GL2.GL_TEXTURE_2D, wallTexture);
            	gl.glBegin(GL2.GL_QUAD_STRIP);
            	gl.glColor3f(1f, 1f, 1f);
            	float t = 0f;
            	for (int j = i; j < i+4; j = j + 2) {
            		gl.glTexCoord2f(t * 1f, t * 0f);
            		gl.glVertex3f(w_pointList.get(j) / transValue, floorNum * 0.04f, w_pointList.get(j + 1) / transValue);
            		gl.glTexCoord2f(t, 1f);
            		gl.glVertex3f(w_pointList.get(j) / transValue, (floorNum * 0.04f) + 0.04f, w_pointList.get(j + 1) / transValue);
            		t = 1f;
            	}
            	gl.glEnd();
            	gl.glDisable(GL2.GL_TEXTURE_2D);
            	gl.glFlush();
            }
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

        String query2 = "select st_astext(the_geom),room_type from room where floor_id=" + floorNum;
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
                gl.glClearColor(0, 0, 0, 0);
                gl.glColor3f(0.20f, 0.20f, 0.20f);
                for (int j = 0; j < r_pointList.size(); j = j + 2) {
                    gl.glVertex3f(r_pointList.get(j) / transValue, floorNum * 0.04f, r_pointList.get(j + 1) / transValue);
                }
               
                gl.glEnd();
                gl.glFlush();
            }
        }

    }

    /*
     * Method name              :   drawRoom_3D
     * Method description       :   method to display 3D rooms
     * Method Arguments         :   (GL2) gl,floorNum
     * Arguments description    :   gl ---> To initialize the OpenGL graphics context
     * 								floorNum ---> floor Number
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void drawRoom_3D(GL2 gl,int floorNum) throws Exception {

        String query2 = "select st_astext(the_geom),room_type from room where floor_id=" + floorNum;
        Statement stmt2 = dbConn.createStatement();
        ResultSet rs2 = stmt2.executeQuery(query2);
        String[] r_pointIn = null;
        List<Float> r_pointList;
        while (rs2.next()) {
            r_pointIn = rs2.getString(1).replace("MULTIPOLYGON", "").replace("POLYGON", "").replace(",", " ").replace("(", "").replace(")", "").split(" ");
            String roomType = rs2.getString(2).toLowerCase();
            if (!roomType.contains("corridor")) {
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
                gl.glColor3f(1f, 1f, 1f);
                for (int j = 0; j < r_pointList.size()-2; j = j + 2) {
                	gl.glTexCoord2f(0f,0f);
                	gl.glVertex3f(r_pointList.get(j) / transValue, (floorNum * 0.04f) , r_pointList.get(j + 1) / transValue);
                	gl.glTexCoord2f(1f,0f);
                	gl.glVertex3f(r_pointList.get(j+2) / transValue, (floorNum * 0.04f), r_pointList.get(j + 3) / transValue);
                	gl.glTexCoord2f(1f, 1f);
                	gl.glVertex3f(r_pointList.get(j+2) / transValue, (floorNum * 0.04f) + 0.04f, r_pointList.get(j + 3) / transValue);
                	gl.glTexCoord2f(0f, 1f);
                	gl.glVertex3f(r_pointList.get(j) / transValue, (floorNum * 0.04f) + 0.04f, r_pointList.get(j + 1) / transValue);                    
                }
                gl.glEnd();
                gl.glDisable(GL2.GL_TEXTURE_2D);
                gl.glFlush();
            }
        }

    }

    /*
     * Method name              :   drawWindow_2D
     * Method description       :   method to display 2D windows
     * Method Arguments         :   (GL2) gl,floorNum
     * Arguments description    :   gl ---> To initialize the OpenGL graphics context
     * 								floorNum ---> floor Number
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void drawWindow_2D(GL2 gl,int floorNum) throws Exception {

        String query3 = "select st_astext(the_geom) from windows where floor_id=" + floorNum;
        Statement stmt3 = dbConn.createStatement();
        ResultSet rs3 = stmt3.executeQuery(query3);
        String[] win_pointIn = null;

        List<Float> win_pointList;
        while (rs3.next()) {
            win_pointIn = rs3.getString(1).replace("MULTILINESTRING", "").replace("LINESTRING", "").replace(",", " ").replace("(", "").replace(")", "").split(" ");
            win_pointList = new ArrayList<Float>();
            for (int i = 0; i < win_pointIn.length; i = i + 2) {
                float xPoint = Float.parseFloat(win_pointIn[i]);
                float yPoint = Float.parseFloat(win_pointIn[i + 1]);
                win_pointList.add(xPoint);
                win_pointList.add(yPoint);
            }
            gl.glBegin(GL2.GL_LINES);
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            gl.glColor3f(0f, 0f, 0f);
            for (int i = 0; i < win_pointList.size(); i = i + 2) {
                gl.glVertex3f(win_pointList.get(i) / transValue, floorNum * 0.04f, win_pointList.get(i + 1) / transValue);
            }
            gl.glColor3f(1f, 1f, 1f);
            gl.glEnd();
            gl.glFlush();
        }

    }

    /*
     * Method name              :   drawWindow_3D
     * Method description       :   method to display 3D windows
     * Method Arguments         :   (GL2) gl,floorNum
     * Arguments description    :   gl ---> To initialize the OpenGL graphics context
     * 								floorNum ---> floor Number
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void drawWindow_3D(GL2 gl,int floorNum) throws Exception {

        String query3 = "select st_astext(st_envelope(the_geom)) from windows where floor_id=" + floorNum;
        Statement stmt3 = dbConn.createStatement();
        ResultSet rs3 = stmt3.executeQuery(query3);
        String[] win_pointIn = null;

        List<Float> win_pointList;
        while (rs3.next()) {
            win_pointIn = rs3.getString(1).replace("POLYGON", "").replace("LINESTRING", "").replace(",", " ").replace("(", "").replace(")", "").split(" ");
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
                gl.glVertex3f(win_pointList.get(i) / transValue, (floorNum * 0.04f) + 0.01f, win_pointList.get(i + 1) / transValue);
                gl.glTexCoord2f(1f, 0f);
                gl.glVertex3f(win_pointList.get(i+2) / transValue, (floorNum * 0.04f) + 0.01f, win_pointList.get(i + 3) / transValue);
                gl.glTexCoord2f(1f, 1f);
                gl.glVertex3f(win_pointList.get(i+2) / transValue, (floorNum * 0.04f) + 0.03f, win_pointList.get(i + 3) / transValue);
                gl.glTexCoord2f(0f, 1f);
                gl.glVertex3f(win_pointList.get(i) / transValue, (floorNum * 0.04f) + 0.03f, win_pointList.get(i + 1) / transValue);
            }
                    
            gl.glDisable(GL2.GL_TEXTURE_2D);
            gl.glEnd();
        }

    }

    /*
     * Method name              :   drawDoor_2D
     * Method description       :   method to display 2D doors
     * Method Arguments         :   (GL2) gl,floorNum
     * Arguments description    :   gl ---> To initialize the OpenGL graphics context
     * 								floorNum ---> floor Number
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void drawDoor_2D(GL2 gl,int floorNum) throws Exception {

        String query4 = "select st_astext(st_envelope(the_geom)) from exit where floor_id=" + floorNum;
        Statement stmt4 = dbConn.createStatement();
        ResultSet rs4 = stmt4.executeQuery(query4);
        String[] d_pointIn = null;

        List<Float> d_pointList;
        while (rs4.next()) {
            d_pointIn = rs4.getString(1).replace("POLYGON", "").replace("LINESTRING", "").replace(",", " ").replace("(", "").replace(")", "").split(" ");
            d_pointList = new ArrayList<Float>();
            for (int i = 0; i < d_pointIn.length; i = i + 2) {
                float xPoint = Float.parseFloat(d_pointIn[i]);
                float yPoint = Float.parseFloat(d_pointIn[i + 1]);
                d_pointList.add(xPoint);
                d_pointList.add(yPoint);
            }

            gl.glBegin(GL2.GL_POLYGON);
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            gl.glColor3f(0f, 0f, 0f);

            for (int i = 0; i < d_pointList.size(); i = i + 2) {
                gl.glVertex3f(d_pointList.get(i) / transValue, (floorNum * 0.04f), d_pointList.get(i + 1) / transValue);
            }
            gl.glColor3f(1f, 1f, 1f);
            gl.glEnd();
            gl.glFlush();
        }

    }

    /*
     * Method name              :   drawDoor_3D
     * Method description       :   method to display 3D doors
     * Method Arguments         :   (GL2) gl,floorNum
     * Arguments description    :   gl ---> To initialize the OpenGL graphics context
     * 								floorNum ---> floor Number
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void drawDoor_3D(GL2 gl,int floorNum) throws Exception {
        
        String query4 = "select st_astext(st_envelope(the_geom)) from exit where floor_id=" + floorNum;
        Statement stmt4 = dbConn.createStatement();
        ResultSet rs4 = stmt4.executeQuery(query4);
        String[] d_pointIn = null;

        List<Float> d_pointList;
        while (rs4.next()) {
            d_pointIn = rs4.getString(1).replace("POLYGON", "").replace("LINESTRING", "").replace(",", " ").replace("(", "").replace(")", "").split(" ");
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
                gl.glVertex3f(d_pointList.get(j) / transValue, floorNum * 0.04f, d_pointList.get(j + 1) / transValue);
                gl.glTexCoord2f(1f,0f);
                gl.glVertex3f(d_pointList.get(j+2) / transValue, floorNum * 0.04f, d_pointList.get(j + 3) / transValue);
                gl.glTexCoord2f(1f, 1f);
                gl.glVertex3f(d_pointList.get(j+2) / transValue, (floorNum * 0.04f) + 0.03f, d_pointList.get(j + 3) / transValue);
                gl.glTexCoord2f(0f, 1f);
                gl.glVertex3f(d_pointList.get(j) / transValue, (floorNum * 0.04f) + 0.03f, d_pointList.get(j + 1) / transValue);
                
            }
            gl.glEnd();
            gl.glDisable(GL2.GL_TEXTURE_2D);
        }

		
    }
    
    /*
     * Method name              :   drawStair_2D
     * Method description       :   method to display 2D stairs
     * Method Arguments         :   (GL2) gl,floorNum
     * Arguments description    :   gl ---> To initialize the OpenGL graphics context
     * 								floorNum ---> floor Number
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void drawStair_2D(GL2 gl,int floorNum) throws Exception{
    	
         String query3 = "select st_astext(the_geom) from stairs where floor_id=" + floorNum;
         Statement stmt3 = dbConn.createStatement();
         ResultSet rs3 = stmt3.executeQuery(query3);
         String[] st_pointIn = null;

         List<Float> st_pointList;
         while (rs3.next()) {
             st_pointIn = rs3.getString(1).replace("MULTILINESTRING", "").replace("LINESTRING", "").replace(",", " ").replace("(", "").replace(")", "").split(" ");
             st_pointList = new ArrayList<Float>();
             for (int i = 0; i < st_pointIn.length; i = i + 2) {
                 float xPoint = Float.parseFloat(st_pointIn[i]);
                 float yPoint = Float.parseFloat(st_pointIn[i + 1]);
                 st_pointList.add(xPoint);
                 st_pointList.add(yPoint);
             }
             gl.glBegin(GL2.GL_LINES);
             gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
             gl.glColor3f(1.0f, 1.0f, 1.0f);
             for (int i = 0; i < st_pointList.size(); i = i + 2) {
                 gl.glVertex3f(st_pointList.get(i) / transValue, floorNum * 0.04f, st_pointList.get(i + 1) / transValue);
             }
             
             gl.glEnd();
             gl.glFlush();
         }

    }
    
    /*
     * Method name              :   drawStair_3D
     * Method description       :   method to display 3D stairs
     * Method Arguments         :   (GL2) gl,floorNum
     * Arguments description    :   gl ---> To initialize the OpenGL graphics context
     * 								floorNum ---> floor Number
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void drawStair_3D(GL2 gl,int floorNum) throws Exception {
    
        WKTReader reader=new WKTReader();
        String query4 = "select st_astext(st_envelope(the_geom)) from stairs where floor_id=" +floorNum+" and exists(select the_geom from stairs where floor_id="+(floorNum+1)+")";
        Statement stmt4 = dbConn.createStatement();
        ResultSet rs4 = stmt4.executeQuery(query4);
        
        while (rs4.next()) {    
        	String stair_wkt=rs4.getString(1);
        	Polygon stair_geom=(Polygon) reader.read(stair_wkt);
        	String st_query="select st_astext(st_envelope(the_geom)) from stairs where floor_id="+(floorNum+1);
        	Statement st_stmt=dbConn.createStatement();
        	ResultSet st_rs=st_stmt.executeQuery(st_query);
        	while(st_rs.next())
        	{
        		String nextStair_wkt=st_rs.getString(1);
        		Polygon next_stair_geom=(Polygon)reader.read(nextStair_wkt);
        		if(stair_geom.overlaps(next_stair_geom))
        		{
        			Coordinate[] stair_coord=stair_geom.getCoordinates();
        	        
                	Coordinate p1=stair_coord[0];
                	Coordinate p2=stair_coord[1];
                	Coordinate p3=stair_coord[2];
                	Coordinate p4=stair_coord[3];
                    double diff1=p1.distance(p2);
                    double diff2=p1.distance(p4);
                    if(diff1>diff2)
                    {
                    	//swap p2 and p4
                    	Coordinate temp;
                    	temp=p2;
                    	p2=p4;
                    	p4=temp;
                    }
                    drawStairPolygon(p1,p2,gl);
        		}
        	}
        }

		
    }
    
    public static void drawStairPolygon(Coordinate point1,Coordinate point2,GL2 gl)
    {
    	ArrayList<Double> pointList=new ArrayList<Double>();
        Coordinate pt1=new Coordinate(point1.x,point1.y);
        Coordinate pt2=new Coordinate(point2.x,point2.y);
        pointList.add(pt1.x);
        pointList.add(pt1.y);
        pointList.add(pt2.x);
        pointList.add(pt2.y);

        Double x=pointList.get(0)/transValue;
        Double y=0.04;
        Double z=pointList.get(1)/transValue;
        Double x1=pointList.get(2)/transValue;
        Double z1=pointList.get(3)/transValue;
        
        for(Double h=0.04;h<0.08;h=h+0.005)
        {   
        	gl.glEnable(GL2.GL_TEXTURE_2D);
            gl.glBindTexture(GL2.GL_TEXTURE_2D,stairTexture );
            gl.glBegin(GL2.GL_QUADS);
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            gl.glColor3f(1.0f, 1.0f, 1.0f);
            gl.glTexCoord2f(0,0);
            gl.glVertex3d(x,y,z);
            gl.glTexCoord2f(1,0);
            gl.glVertex3d(x1,y,z1);
            gl.glTexCoord2f(0,1);
            gl.glVertex3d(x1,y+0.008,z1);
            gl.glTexCoord2f(1,1);
            gl.glVertex3d(x,y+0.008,z);
            //gl.glColor3f(1f, 1f, 1f);
            gl.glEnd();
            gl.glDisable(GL2.GL_TEXTURE_2D);
            
            gl.glEnable(GL2.GL_TEXTURE_2D);
            gl.glBindTexture(GL2.GL_TEXTURE_2D, stairTexture);
            gl.glBegin(GL2.GL_QUADS);
            //gl.glColor3f(0f, 0f, 0f);
            gl.glTexCoord2f(0,0);
            gl.glVertex3d(x,y+0.008,z);
            gl.glTexCoord2f(1,0);
            gl.glVertex3d(x1,y+0.008,z1);
            gl.glTexCoord2f(0,1);
            gl.glVertex3d(x1+0.003,y+0.008,z1);
            gl.glTexCoord2f(1,1);
            gl.glVertex3d(x+0.003,y+0.008,z);
            //gl.glColor3f(1f, 1f, 1f);
            gl.glEnd();
            gl.glDisable(GL2.GL_TEXTURE_2D);
            x=x+0.003;
            y=y+0.008;
            x1=x1+0.003;
            h=y;
        }
        
        gl.glFlush(); 
       
    }
    
    /*
     * Method name              :   drawSensors_2D
     * Method description       :   method to display 2D windows
     * Method Arguments         :   (GL2) gl
     * Arguments description    :   gl ---> To initialize the OpenGL graphics context
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public static void drawSensors_2D(GL2 gl,int fNum) throws Exception {
    	
        int win_floorNow = fNum;

        String query3 = "select st_astext(the_geom) from sensors where floor_id=" + win_floorNow;
        Statement stmt3 = dbConn.createStatement();
        ResultSet rs3 = stmt3.executeQuery(query3);
        String[] win_pointIn = null;
        float xp = 0f, yp = 0f;

        List<Float> win_pointList;
        while (rs3.next()) {
            win_pointIn = rs3.getString(1).replace("POLYGON", "").replace(",", " ").replace("(", "").replace(")", "").split(" ");
            win_pointList = new ArrayList<Float>();
            for (int i = 0; i < win_pointIn.length; i = i + 2) {
                float xPoint = Float.parseFloat(win_pointIn[i]);
                float yPoint = Float.parseFloat(win_pointIn[i + 1]);
                xp = xPoint;
                yp = yPoint;
                win_pointList.add(xp);
                win_pointList.add(yp);
            }
            gl.glBegin(GL2.GL_POLYGON);
            gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            gl.glColor3f(0.282f, 0.820f, 0.800f);
            for (int i = 0; i < win_pointList.size(); i = i + 2) {
                gl.glVertex3f(win_pointList.get(i) / transValue, win_floorNow * 0.04f, win_pointList.get(i + 1) / transValue);
            }
            gl.glColor3f(1f, 1f, 1f);
            gl.glEnd();
        }

		
    }
    /*
     * Method name              :   drawSensors_3D
     * Method description       :   method to display 3D Sensors
     * Method Arguments         :   (GL2) gl
     * Arguments description    :   gl ---> To initialize the OpenGL graphics context
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
public static void drawSensors_3D(GL2 gl,int fNum) throws Exception {
    	
        int win_floorNow = fNum;

        String query3 = "select st_astext(the_geom),sensor_master.sensor_generic_name from sensors inner join sensor_master on sensors.sensor_generic_code=sensor_master.sensor_generic_code where floor_id=" + win_floorNow;
        Statement stmt3 = dbConn.createStatement();
        ResultSet rs3 = stmt3.executeQuery(query3);
        String[] sen_pointIn = null;
        float xp = 0f, yp = 0f;

        List<Float> win_pointList;
        while (rs3.next()) {
            sen_pointIn = rs3.getString(1).replace("POLYGON", "").replace(",", " ").replace("(", "").replace(")", "").split(" ");
            win_pointList = new ArrayList<Float>();
            String sensortype=rs3.getString(2);
            for (int i = 0; i < sen_pointIn.length; i = i + 2) {
                float xPoint = Float.parseFloat(sen_pointIn[i]);
                float yPoint = Float.parseFloat(sen_pointIn[i + 1]);
                xp = xPoint;
                yp = yPoint;
                win_pointList.add(xp);
                win_pointList.add(yp);
            }
            
            
            if(sensortype.toLowerCase().contains("temperature"))
            {
            	gl.glEnable(GL2.GL_TEXTURE_2D);
            	gl.glBindTexture(GL2.GL_TEXTURE_2D,sen_temp_Texture);               	
            }
            else if(sensortype.toLowerCase().contains("rfid"))
            {
            	gl.glEnable(GL2.GL_TEXTURE_2D);
            	gl.glBindTexture(GL2.GL_TEXTURE_2D,sen_rfid_Texture); 
            }
            else if(sensortype.toLowerCase().contains("wifi"))
            {
            	gl.glEnable(GL2.GL_TEXTURE_2D);
            	gl.glBindTexture(GL2.GL_TEXTURE_2D,sen_wifi_Texture);
            }
            else if(sensortype.toLowerCase().contains("camera"))
            {
            	gl.glEnable(GL2.GL_TEXTURE_2D);
            	gl.glBindTexture(GL2.GL_TEXTURE_2D,sen_cam_Texture);
            }else
            {
            	gl.glEnable(GL2.GL_TEXTURE_2D);
            	gl.glBindTexture(GL2.GL_TEXTURE_2D,sen_rfid_Texture);
            }
         
            
            gl.glBegin(GL2.GL_QUAD_STRIP);
          

            float t = 0f;
            for (int i = 0; i < 4; i = i + 2) {
                gl.glTexCoord2f(t * 1f, t * 0f);
                gl.glVertex3f(win_pointList.get(i) / transValue, (win_floorNow * 0.04f) + 0.01f, win_pointList.get(i + 1) / transValue);
                gl.glTexCoord2f(1, 0);
                gl.glTexCoord2f(t, 1f);
                gl.glVertex3f(win_pointList.get(i) / transValue, (win_floorNow * 0.04f) + 0.02f, win_pointList.get(i + 1) / transValue);
                gl.glTexCoord2f(0, 1);
                t = 1f;
            }
            gl.glEnd();
            gl.glDisable(GL2.GL_TEXTURE_2D);
        }
    }


    /*
     * Method name              :   keyPressed
     * Method description       :   method of KeyListener which is called when a key is pressed.
     * Method Arguments         :   (KeyEvent) e 
     * Arguments description    :   e ---> To get the key event
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            rotateY -= 2;
        } else if (key == KeyEvent.VK_RIGHT) {
            rotateY += 2;
        } else if (key == KeyEvent.VK_DOWN) {
            rotateX += 2;
        } else if (key == KeyEvent.VK_UP) {
            rotateX -= 2;
        } else if (key == KeyEvent.VK_PAGE_UP) {
            zoom -= 0.02;
        } else if (key == KeyEvent.VK_PAGE_DOWN) {
            zoom += 0.02;
        }
        repaint();
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    /*
     * Method name              :   mouseClicked
     * Method description       :   method of MouseListener which is called when the mouse is clicked.
     * Method Arguments         :   (MouseEvent) e 
     * Arguments description    :   e ---> To get the mouse event
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public void mouseClicked(MouseEvent e) {
        this.setFocusable(true);
        this.requestFocus();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    /*
     * Method name              :   mousePressed
     * Method description       :   method of MouseListener which is called when the mouse is pressed.
     * Method Arguments         :   (MouseEvent) e 
     * Arguments description    :   e ---> To get the mouse event
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
    public void mousePressed(MouseEvent e) {
        x_prev = e.getX();
        y_prev = e.getY();

    }

    public void mouseReleased(MouseEvent e) {
    }

    /*
     * Method name              :   mouseDragged
     * Method description       :   method of MouseListener which is called when the mouse is dragged.
     * Method Arguments         :   (MouseEvent) e 
     * Arguments description    :   e ---> To get the mouse event
     * Return type              :   void
     * Return type description  :   Returns nothing.
     */
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

    public void mouseMoved(MouseEvent arg0) {
    }
    
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

    public static void dispQueryResult(int qNum) {
        queryHashNum = qNum;
        IIRMSMainVisualization.visualPanel.repaint();
    }

	
}