package com.amrita.IIRMS.DB;

/*
 * File Name        : Sensors.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Mukunthan R (Junior Research Fellow)
 * Last Modified    : the 11th of June, 2015
 * Purpose          : Class to contain the data of ROOMS to be stored in database.
 */
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;

import java.util.ArrayList;
import java.util.List;

import org.nocrala.tools.gis.data.esri.shapefile.shape.PointData;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolygonShape;
import org.nocrala.tools.gis.data.esri.shapefile.shape.shapes.PolylineShape;

public class Sensors {

	public String sensor_name, sensor_type, dimension, coverage, lifetime, outputFormat, cost, room_name;
	public int floor_id, wall_id, sensor_id, sensor_floor, sensor_count, count;
	public Geometry sensor_geom;

	public Sensors() {
	}

	public static void Sensor_Construction() throws Exception {
		List<PolygonShape> polygonList = DB_Script_File.polygon_sensor_List;
		List<Sensors> sensorList = DB_Script_File.sensorList;
		for (int i = 0, j = 0; i < polygonList.size() && j < sensorList.size(); i++, j++) {
			PolygonShape psIn = polygonList.get(i);
			Sensors sensorIn = sensorList.get(j);
			int floor_id = sensorIn.sensor_floor;

			ArrayList<Coordinate> pointList = new ArrayList<Coordinate>();
			
			PointData[] points = psIn.getPoints();
			for (int k = 0; k < psIn.getNumberOfPoints(); k++) {
				double sensor_p1X = points[k].getX() - DB_Script_File.shiftDistX(floor_id);
				double sensor_p1Y = points[k].getY()-DB_Script_File.shiftDistY(floor_id);
				pointList.add(new Coordinate(sensor_p1X, sensor_p1Y));
			}
			GeometryFactory gf = new GeometryFactory();
			Coordinate[] coordArray = pointList.toArray(new Coordinate[pointList.size()]);
			Polygon polygon = gf.createPolygon(new LinearRing(new CoordinateArraySequence(coordArray), gf), null);
			sensorIn.sensor_geom = polygon;

			DB_Script_File.finalSensorList.add(sensorIn);
		}
	}

}
