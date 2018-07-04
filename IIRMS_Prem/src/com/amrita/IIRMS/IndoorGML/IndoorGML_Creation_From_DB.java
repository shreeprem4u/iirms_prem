package com.amrita.IIRMS.IndoorGML;

/*
 * File Name        : IndoorGML_Creation_DB.java
 * Project Name     : Indoor Information Representation and Management System.
 * Author           : Ajeissh M (Junior Research Fellow),Sindhya Kumari N(Junior Research Fellow)
 * Last Modified    : the 6th of October, 2015
 * Purpose          : Class to create the IndoorGML Instance Document using DOM Parser.
 */
import java.io.*;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.util.*;
import javax.swing.JOptionPane;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

import java.awt.HeadlessException;

public class IndoorGML_Creation_From_DB {

	public static String xmlFilePath = "";
	public DocumentBuilderFactory documentFactory;
	public DocumentBuilder documentBuilder;
	public Document doc;
	public static Document docNavi;
	public int lsCount;

	/*
	 * Method name : IndoorGML_Creation 
	 * Method description : Constructor to initialize the documentFactory for creating XML file. 
	 * Method Arguments :String (pathValue) 
	 * Arguments description : documentName ---> name of the IndoorGML document 
	 * Return type : null 
	 * Return type description : --
	 */
	public IndoorGML_Creation_From_DB(String pathValue) {
		try {
			xmlFilePath = pathValue;
			documentFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentFactory.newDocumentBuilder();
			doc = documentBuilder.newDocument();
			docNavi = documentBuilder.newDocument();
			lsCount = 1;
		} catch (Exception ex) {
		}
	}

	/*
	 * Method name : XML_Creation 
	 * Method description : Method that creates the IndoorGML Instance Document 
	 * Method Arguments : null 
	 * Arguments description: -- 
	 * Return type : void 
	 * Return type description : Returns nothing.
	 */
	public void XML_Creation() throws Exception {
		try {
			// Adding root element 'IndoorFeatures' to the document
			Element elk =doc.createElementNS("http://www.opengis.net/indoorgml/1.0/core","indoorCore:IndoorFeatures");
			//Element elk = doc.createElementNS("http://www.opengis.net/indoorgml/navigation/1.0", "indoorCore:IndoorFeatures");
			//Element elk =doc.createElementNS("http://www.opengis.net/indoorgml/navigation/1.0","IndoorFeatures");
			elk.setAttribute("xmlns:gml", "http://www.opengis.net/gml/3.2");
			elk.setAttribute("xmlns:xlink", "http://www.w3.org/1999/xlink");
			elk.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
			elk.setAttribute("xmlns", "http://www.opengis.net/indoorgml/1.0/core");
			// elk.setAttribute("xsi:schemaLocation","http://schemas.opengis.net/indoorgml/1.0/indoorgmlcore.xsd");
			elk.setAttribute("xmlns:indoorCore", "http://www.opengis.net/indoorgml/1.0/core");
			elk.setAttribute("xsi:schemaLocation", "http://www.opengis.net/indoorgml/core/1.0 IndoorGMLCore.xsd");
			elk.setAttribute("xmlns:indoorNavi", "http://www.opengis.net/indoorgml/navigation/1.0");
			elk.setAttribute("gml:id", "IFs");

			String NS = "http://www.opengis.net/indoorgml/1.0/core";
			String gmlNS = "http://www.opengis.net/gml/3.2";

			// Adding 'primalSpaceFeatures' element to 'IndoorFeatures' element
			Element primalSpaceFeatures = doc.createElementNS(NS, "indoorCore:primalSpaceFeatures");

			// Adding 'MultiLayeredGraph' element to 'IndoorFeatures' element
			Element multiLayeredGraph = doc.createElementNS(NS, "indoorCore:MultiLayeredGraph");
			multiLayeredGraph.setAttributeNS(gmlNS, "gml:id", "ML1");

			// Adding 'PrimalSpaceFeatures' element to 'primalSpaceFeatures'.
			// Note the difference in the case of the first letter
			Element primalSpaceFeaturesTag = doc.createElementNS(NS, "indoorCore:PrimalSpaceFeatures");
			primalSpaceFeaturesTag.setAttributeNS(gmlNS, "gml:id", "PS1");

			// Adding 'spaceLayers' tag to 'MultiLayeredGraph' tag
			Element spaceLayers = doc.createElementNS(NS, "indoorCore:spaceLayers");
			spaceLayers.setAttributeNS(gmlNS, "gml:id", "SLayer1");

			// Adding 'spaceLayerMember' tag to 'spaceLayers'
			Element spaceLayerMember = doc.createElementNS(NS, "indoorCore:spaceLayerMember");

			// Adding 'spaceLayer' tag to 'spaceLayerMember'
			Element spaceLayer = doc.createElementNS(NS, "indoorCore:SpaceLayer");
			spaceLayer.setAttributeNS(gmlNS, "gml:id", "SL1");

			// Adding 'nodes' tag to 'spaceLayer'
			Element nodes = doc.createElementNS(NS, "indoorCore:nodes");
			nodes.setAttributeNS(gmlNS, "gml:id", "N1");

			// Adding 'edges' tag to 'spaceLayer'
			Element edges = doc.createElementNS(NS, "indoorCore:edges");
			edges.setAttributeNS(gmlNS, "gml:id", "E1");

			for (IndoorGML_Room igmlRoom : IndoorGML_DB_Parser.roomList) {

				com.infomatiq.jsi.Point ppIn = new com.infomatiq.jsi.Point((float) igmlRoom.room_xcenter, (float) igmlRoom.room_ycenter);
				ListIntProcedure lipIn = new ListIntProcedure();
				// IndoorGML_File_Parser.rTree.nearest(ppIn);
				//System.out.println("*****"+igmlRoom.room_name+"*****");

				IndoorGML_DB_Parser.rTree.nearestN(ppIn, lipIn, 40, Float.POSITIVE_INFINITY);
				List<Object> nearestObjs = lipIn.getSolution();
				//System.out.println("The list is : \n " + nearestObjs + "\n");
				List<IndoorGML_Room> roomIntersects = new ArrayList<IndoorGML_Room>();
				List<IndoorGML_Walls> wallIntersects = new ArrayList<IndoorGML_Walls>();
				List<IndoorGML_Doors> doorIntersects = new ArrayList<IndoorGML_Doors>();
				//System.out.println("Nearest are: ");
				
				for (Object objIn : nearestObjs) {
					if (IndoorGML_DB_Parser.roomMap.get(objIn) != null) {
						IndoorGML_Room roomObj = (IndoorGML_Room) IndoorGML_DB_Parser.roomMap.get(objIn);
						//System.out.println(roomObj.room_name);
						roomIntersects.add(roomObj);
					} else if (IndoorGML_DB_Parser.wallsMap.get(objIn) != null) {
						IndoorGML_Walls wallObj = (IndoorGML_Walls) IndoorGML_DB_Parser.wallsMap.get(objIn);
						wallIntersects.add(wallObj);
					} else if (IndoorGML_DB_Parser.doorMap.get(objIn) != null) {
						IndoorGML_Doors doorObj = (IndoorGML_Doors) IndoorGML_DB_Parser.doorMap.get(objIn);
						doorIntersects.add(doorObj);
					}
				}

				// Adding 'cellSpaceMember' tag to 'PrimalSpaceFeaturesTag'
				Element cellSpaceMember = doc.createElementNS(NS, "cellSpaceMember");

				//System.out.println("RoomType:" + igmlRoom.room_type.toLowerCase());
				Element rCellspace;
				System.out.println(igmlRoom.room_type);
				if (igmlRoom.room_type.trim().toLowerCase().contains("corridor")) {
					// Adding 'CellSpace' tag to 'cellSpaceMember'
					rCellspace = doc.createElementNS(NS, "TransitionSpace");
					rCellspace.setAttributeNS(gmlNS, "gml:id", "CS" + igmlRoom.count);
				} else {
					rCellspace = doc.createElementNS(NS, "GeneralSpace");
					rCellspace.setAttributeNS(gmlNS, "gml:id", "CS" + igmlRoom.count);
				}

				// Adding 'name' gml tag to 'CellSpace'
				Element namexml = doc.createElementNS(gmlNS, "gml:name");
				Text textxml = doc.createTextNode("RoomName");
				// System.out.println(igmlRoom.room_name);
				textxml = doc.createTextNode(igmlRoom.room_name.trim());
				namexml.appendChild(textxml);
				rCellspace.appendChild(namexml);

				// Adding 'Geometry3D' to the 'CellSpace'
				Element geometry3D = doc.createElementNS(NS, "Geometry3D");

				// Adding 'Solid' tag to 'Geometry3D'
				Element solid = doc.createElementNS(gmlNS, "gml:Solid");
				solid.setAttributeNS(gmlNS, "gml:id", "sl" + igmlRoom.count);

				// Adding 'exterior' tag to 'Solid'
				Element exterior = doc.createElementNS(gmlNS, "gml:exterior");

				// Adding 'shell' tag to 'exterior'
				Element shell = doc.createElementNS(gmlNS, "gml:Shell");

				// Adding 'surfaceMember' tag to 'shell'
				Element surfaceMember = doc.createElementNS(gmlNS, "gml:surfaceMember");

				// Adding 'Polygon' tag to 'surfaceMember'
				Element polygon = doc.createElementNS(gmlNS, "gml:Polygon");
				polygon.setAttributeNS(gmlNS, "gml:id", "P" + igmlRoom.count);

				// Adding 'exterior' tag to 'polygon'
				Element exterior2 = doc.createElementNS(gmlNS, "gml:exterior");

				// Adding 'LinearRing' tag to 'exterior' i.e., exterior2 element
				Element linearRing = doc.createElementNS(gmlNS, "gml:LinearRing");

				// Adding 'pos' tag to 'LinearRing'
				String geomNow = igmlRoom.room_transGeom.toString().replace("POLYGON ", "").replace("(", "").replace(")", "");
				int floorNum = igmlRoom.room_floor;
				String zPoint = Double.toString(floorNum * 0.04);
				String[] geomNowSplit = geomNow.split(", ");
				for (String entry : geomNowSplit) {
					String coord = entry.trim().concat(" ").concat(zPoint);
					Element pos = doc.createElementNS(gmlNS, "gml:pos");
					Text textxml2 = doc.createTextNode("EX");
					// System.out.println(entry);
					textxml2 = doc.createTextNode(coord);
					pos.appendChild(textxml2);
					linearRing.appendChild(pos);
				}
				List<IndoorGML_Room> adjacentOnes = new ArrayList<IndoorGML_Room>();
				List<Geometry> intersectAdjacent = new ArrayList<Geometry>();
				//System.out.println(igmlRoom.room_name + ": " + wallIntersects.size() + " " + roomIntersects.size());
				for (IndoorGML_Walls wallObj : wallIntersects) {
					if (igmlRoom.room_geom.intersects(wallObj.wall_geom)) {
						//System.out.println("Adjacency:"+igmlRoom.room_name+" "+wallObj.wall_count);
						for (IndoorGML_Room roomObjWall : roomIntersects) {
							if (wallObj.wall_geom.intersects(roomObjWall.room_geom)) {
								if (!(roomObjWall.room_name.equals(igmlRoom.room_name))) {
									//System.out.println("Adjacent1:"+roomObjWall.room_name);
									intersectAdjacent.add(wallObj.wall_geom);
									if (!(adjacentOnes.contains(roomObjWall))) {
										adjacentOnes.add(roomObjWall);
									}
								}
							}
						}
						igmlRoom.Set_Nearest(roomIntersects);
					}
				}
				igmlRoom.Set_Adjacent(adjacentOnes);

				exterior2.appendChild(linearRing);
				polygon.appendChild(exterior2);
				surfaceMember.appendChild(polygon);
				shell.appendChild(surfaceMember);
				exterior.appendChild(shell);
				solid.appendChild(exterior);
				geometry3D.appendChild(solid);
				rCellspace.appendChild(geometry3D);

				// Adding 'duality' to 'cellSpace'
				Element duality = doc.createElementNS(NS, "duality");
				duality.setAttribute("xlink:type", "simple");
				duality.setAttribute("xlink:href", "#S_R_" + igmlRoom.count + "_" + igmlRoom.room_floor);
				rCellspace.appendChild(duality);

				// Adding 'externalReference' to 'cellSpace'
				// Element externalReference =
				// doc.createElement("externalReference");
				// cellSpace.appendChild(externalReference);

				cellSpaceMember.appendChild(rCellspace);

				// Creating elements for MultiLayeredGraphs
				// Adding 'stateMember' to nodes
				Element stateMember = doc.createElementNS(NS, "indoorCore:stateMember");

				// Adding 'State' to 'stateMember'
				Element state = doc.createElementNS(NS, "indoorCore:State");
				state.setAttributeNS(gmlNS, "gml:id", "S_R_" + igmlRoom.count + "_" + igmlRoom.room_floor);

				// Adding 'gml:name' to 'State'
				Element namexml2 = doc.createElementNS(gmlNS, "gml:name");
				Text textxmlState = doc.createTextNode(igmlRoom.room_name.trim());
				namexml2.appendChild(textxmlState);
				state.appendChild(namexml2);

				// Adding 'duality' to 'State'
				Element dualityState = doc.createElementNS(NS, "duality");
				dualityState.setAttribute("xlink:type", "simple");
				dualityState.setAttribute("xlink:href", "#CS" + igmlRoom.count);
				state.appendChild(dualityState);

				// Adding 'geometry' to 'State'
				Element geometryState = doc.createElementNS(NS, "geometry");

				// Adding 'gml:Point' to 'geometry'
				Element pointIn = doc.createElement("gml:Point");
				pointIn.setAttributeNS(gmlNS, "gml:id", "Point" + igmlRoom.count);

				// Adding 'gml:pos' to 'gml:Point'
				Element posInState = doc.createElementNS(gmlNS, "gml:pos");
				Text posTextState = doc.createTextNode(igmlRoom.room_transGeom.getCentroid().getX() + " " + igmlRoom.room_transGeom.getCentroid().getY());
				posInState.appendChild(posTextState);
				pointIn.appendChild(posInState);
				geometryState.appendChild(pointIn);

				// Need to add the adjacent ones to connects ones
				// for (IndoorGML_Room roomObjNow : adjacentOnes) {

				for (int i = 0; i < adjacentOnes.size(); i++) {
					IndoorGML_Room roomObjNow = adjacentOnes.get(i);
					Element connects = doc.createElementNS(NS, "adjacent");
					connects.setAttribute("xlink:href", "#T_" + roomObjNow.room_name.trim() + "_" + igmlRoom.room_name.trim() + "_"
							+ roomObjNow.room_floor);
					state.appendChild(connects);
					// Creating the transition for each 'connects'
					Element transitionMember = doc.createElementNS(NS, "indoorCore:transitionMember");

					// Adding 'Transition' tag to 'transitionMember'
					Element transition = doc.createElementNS(NS, "indoorCore:Transition");
					transition.setAttributeNS(gmlNS, "gml:id", "T_" + roomObjNow.room_name.trim() + "_" + igmlRoom.room_name.trim() + "_"
							+ roomObjNow.room_floor);

					// Adding weight to Transition
					Element weight = doc.createElementNS(NS, "weight");
					Text textWeight = doc.createTextNode("1");
					weight.appendChild(textWeight);
					
					List<String> r_transArray=new ArrayList<String>();

					// Adding start to Transition
					Element startNode = doc.createElementNS(NS, "start");
					startNode.setAttribute("xlink:href", "#S_R_" + igmlRoom.count + "_" + igmlRoom.room_floor);
					r_transArray.add(igmlRoom.room_transGeom.getCentroid().toString());

					// Adding end to Transition
					Element endNode = doc.createElementNS(NS, "end");
					endNode.setAttribute("xlink:href", "#S_R_" + roomObjNow.count + "_" + roomObjNow.room_floor);
					r_transArray.add(roomObjNow.room_transGeom.getCentroid().toString());

					// Adding geometry to Transition
					Element geometryxml = doc.createElementNS(NS, "geometry");

					// Adding gml:LineString to geometry
					Element lineStringxml = doc.createElementNS(gmlNS, "gml:LineString");
					lineStringxml.setAttributeNS(gmlNS, "gml:id", "LS" + lsCount);
					lsCount++;

					// Adding gml:pos to gml:LineString
					//String geomIn = intersectAdjacent.get(i).toString().replace("MULTIPOINT ", "").replace("(", "").replace(")", "").replace("LINESTRING ", "").replace("POLYGON", "");
					//String[] geomInSplit = geomIn.split(", ");
					
					for (String entry : r_transArray) {
						String geomIn=entry.replace("POINT", "").replace("(", "").replace(")", "");
						Element posxml = doc.createElementNS(gmlNS, "gml:pos");
						Text textxml2 = doc.createTextNode(geomIn);
						posxml.appendChild(textxml2);
						lineStringxml.appendChild(posxml);
					}

					geometryxml.appendChild(lineStringxml);
					transition.appendChild(weight);
					transition.appendChild(startNode);
					transition.appendChild(endNode);
					transition.appendChild(geometryxml);
					transitionMember.appendChild(transition);
					edges.appendChild(transitionMember);
				}

				// Need to add the connects code
				for (IndoorGML_Doors doorObjNow : doorIntersects) {
					// boolean touches =
					// doorObjNow.door_geom.touches(igmlRoom.room_geom);
					Geometry geomIntersection = doorObjNow.door_geom.intersection(igmlRoom.room_geom);
					if (!geomIntersection.isEmpty()) {
						// Adding the 'connects' link to the corresponding
						// transition.
						Element connects = doc.createElementNS(NS, "connects");
						connects.setAttribute("xlink:href", "#T_" + doorObjNow.count + "_" + igmlRoom.room_name.trim() + "_"
								+ doorObjNow.door_floor);
						state.appendChild(connects);

						// Creating the transition for each 'connects'
						Element transitionMember = doc.createElementNS(NS, "indoorCore:transitionMember");

						// Adding 'Transition' tag to 'transitionMember'
						Element transition = doc.createElementNS(NS, "indoorCore:Transition");
						transition.setAttributeNS(gmlNS, "gml:id", "T_" + doorObjNow.count + "_" + igmlRoom.room_name.trim() + "_"
								+ doorObjNow.door_floor);

						// Adding weight to Transition
						Element weight = doc.createElementNS(NS, "weight");
						Text textWeight = doc.createTextNode("1");
						weight.appendChild(textWeight);
						
						List<String> r_transArray=new ArrayList<String>();

						// Adding start to Transition
						Element startNode = doc.createElementNS(NS, "start");
						startNode.setAttribute("xlink:href", "#S_R_" + igmlRoom.count + "_" + igmlRoom.room_floor);
						r_transArray.add(igmlRoom.room_transGeom.getCentroid().toString());

						// Adding end to Transition
						Element endNode = doc.createElementNS(NS, "end");
						endNode.setAttribute("xlink:href", "#S_D_" + doorObjNow.count + "_" + doorObjNow.door_floor);
						r_transArray.add(doorObjNow.door_transGeom.getCentroid().toString());

						// Adding geometry to Transition
						Element geometryxml = doc.createElementNS(NS, "geometry");

						// Adding gml:LineString to geometry
						Element lineStringxml = doc.createElementNS(gmlNS, "gml:LineString");
						lineStringxml.setAttributeNS(gmlNS, "gml:id", "LS" + lsCount);
						lsCount++;

						// Adding gml:pos to gml:LineString
						//String geomIn = geomIntersection.toString().replace("MULTIPOINT ", "").replace("(", "").replace(")", "").replace("LINESTRING ", "").replace("POLYGON", "");
						//String[] geomInSplit = geomIn.split(", ");
						
						for (String entry : r_transArray) {
							String geomIn=entry.replace("POINT", "").replace("(", "").replace(")", "");
							Element posxml = doc.createElementNS(gmlNS, "gml:pos");
							Text textxml2 = doc.createTextNode(geomIn);
							posxml.appendChild(textxml2);
							lineStringxml.appendChild(posxml);
						}

						geometryxml.appendChild(lineStringxml);
						transition.appendChild(weight);
						transition.appendChild(startNode);
						transition.appendChild(endNode);
						transition.appendChild(geometryxml);
						transitionMember.appendChild(transition);
						edges.appendChild(transitionMember);
					}
				}

				// Adding the tags back to its parent.
				// state.appendChild(namexml2);
				// state.appendChild(dualityState);
				state.appendChild(geometryState);
				stateMember.appendChild(state);
				nodes.appendChild(stateMember);

				primalSpaceFeaturesTag.appendChild(cellSpaceMember);
			}

			// Repeating the existing code for doorsList
			for (IndoorGML_Doors igmlDoor : IndoorGML_DB_Parser.doorsList) {

				com.infomatiq.jsi.Point ppIn = new com.infomatiq.jsi.Point((float) igmlDoor.door_xcenter, (float) igmlDoor.door_ycenter);
				ListIntProcedure lipIn = new ListIntProcedure();
				// IndoorGML_File_Parser.rTree.nearest(ppIn);
				IndoorGML_DB_Parser.rTree.nearestN(ppIn, lipIn, 10, Float.POSITIVE_INFINITY);
				List<Object> nearestObjs = lipIn.getSolution();
				//System.out.println("The list is : \n " + nearestObjs + "\n");
				List<IndoorGML_Room> roomIntersects2 = new ArrayList<IndoorGML_Room>();
				List<IndoorGML_Walls> wallIntersects2 = new ArrayList<IndoorGML_Walls>();
				List<IndoorGML_Doors> doorIntersects2 = new ArrayList<IndoorGML_Doors>();

				for (Object objIn : nearestObjs) {
					if (IndoorGML_DB_Parser.roomMap.get(objIn) != null) {
						IndoorGML_Room roomObj = (IndoorGML_Room) IndoorGML_DB_Parser.roomMap.get(objIn);
						roomIntersects2.add(roomObj);
					} else if (IndoorGML_DB_Parser.wallsMap.get(objIn) != null) {
						IndoorGML_Walls wallObj = (IndoorGML_Walls) IndoorGML_DB_Parser.wallsMap.get(objIn);
						wallIntersects2.add(wallObj);
					} else if (IndoorGML_DB_Parser.doorMap.get(objIn) != null) {
						IndoorGML_Doors doorObj = (IndoorGML_Doors) IndoorGML_DB_Parser.doorMap.get(objIn);
						doorIntersects2.add(doorObj);
					}
				}

				// Adding 'cellSpaceMember' tag to 'PrimalSpaceFeaturesTag'
				Element cellSpaceMember = doc.createElementNS(NS, "cellSpaceMember");

				// Adding 'CellSpace' tag to 'cellSpaceMember'
				Element dCellspace = doc.createElementNS(NS, "ConnectionSpace");
				dCellspace.setAttributeNS(gmlNS, "gml:id", "CS" + igmlDoor.count);

				// Adding 'name' gml tag to 'CellSpace'
				Element namexml = doc.createElementNS(gmlNS, "gml:name");
				Text textxml = doc.createTextNode("DoorID");
				// System.out.println(igmlRoom.room_name);
				textxml = doc.createTextNode(Integer.toString(igmlDoor.door_count));
				namexml.appendChild(textxml);
				dCellspace.appendChild(namexml);

				// Adding 'Geometry3D' to the 'CellSpace'
				Element geometry3D = doc.createElementNS(NS, "Geometry3D");

				// Adding 'Solid' tag to 'Geometry3D'
				Element solid = doc.createElementNS(gmlNS, "gml:Solid");
				solid.setAttributeNS(gmlNS, "gml:id", "sl" + igmlDoor.count);

				// Adding 'exterior' tag to 'Solid'
				Element exterior = doc.createElementNS(gmlNS, "gml:exterior");

				// Adding 'shell' tag to 'exterior'
				Element shell = doc.createElementNS(gmlNS, "gml:Shell");

				// Adding 'surfaceMember' tag to 'shell'
				Element surfaceMember = doc.createElementNS(gmlNS, "gml:surfaceMember");

				// Adding 'Polygon' tag to 'surfaceMember'
				Element polygon = doc.createElementNS(gmlNS, "gml:Polygon");
				polygon.setAttributeNS(gmlNS, "gml:id", "P" + igmlDoor.count);

				// Adding 'exterior' tag to 'polygon'
				Element exterior2 = doc.createElementNS(gmlNS, "gml:exterior");

				// Adding 'LinearRing' tag to 'exterior' i.e., exterior2 element
				Element linearRing = doc.createElementNS(gmlNS, "gml:LinearRing");

				// Adding 'pos' tag to 'LinearRing'
				//System.out.println("geomType:"+igmlDoor.door_geom.getGeometryType());
				String geomNow = igmlDoor.door_transGeom.toString().replace("POLYGON ", "").replace("(", "").replace(")", "");
				int floorNum = igmlDoor.door_floor;
				String zPoint = Double.toString(floorNum * 0.04);
				String[] geomNowSplit = geomNow.split(", ");
				for (String entry : geomNowSplit) {
					String coord = entry.trim().concat(" ").concat(zPoint);
					Element pos = doc.createElementNS(gmlNS, "gml:pos");
					Text textxml2 = doc.createTextNode("EX");
					// System.out.println(entry);
					textxml2 = doc.createTextNode(coord);
					pos.appendChild(textxml2);
					linearRing.appendChild(pos);
				}

				exterior2.appendChild(linearRing);
				polygon.appendChild(exterior2);
				surfaceMember.appendChild(polygon);
				shell.appendChild(surfaceMember);
				exterior.appendChild(shell);
				solid.appendChild(exterior);
				geometry3D.appendChild(solid);
				dCellspace.appendChild(geometry3D);

				// Adding 'duality' to 'cellSpace'
				Element duality = doc.createElementNS(NS, "duality");
				duality.setAttribute("xlink:type", "simple");
				duality.setAttribute("xlink:href", "#S_D_" + igmlDoor.count + "_" + igmlDoor.door_floor);
				dCellspace.appendChild(duality);

				// Adding 'externalReference' to 'cellSpace'
				// Element externalReference =
				// doc.createElement("externalReference");
				// cellSpace.appendChild(externalReference);

				cellSpaceMember.appendChild(dCellspace);

				// Creating elements for MultiLayeredGraphs
				// Adding 'stateMember' to nodes
				Element stateMember = doc.createElementNS(NS, "indoorCore:stateMember");

				// Adding 'State' to 'stateMember'
				Element state = doc.createElementNS(NS, "indoorCore:State");
				state.setAttributeNS(gmlNS, "gml:id", "S_D_" + igmlDoor.count + "_" + igmlDoor.door_floor);

				// Adding 'gml:name' to 'State'
				Element namexml2 = doc.createElementNS(gmlNS, "gml:name");
				Text textxmlState = doc.createTextNode(Integer.toString(igmlDoor.door_count));
				namexml2.appendChild(textxmlState);
				state.appendChild(namexml2);

				// Adding 'duality' to 'State'
				Element dualityState = doc.createElementNS(NS, "duality");
				dualityState.setAttribute("xlink:type", "simple");
				dualityState.setAttribute("xlink:href", "#CS" + igmlDoor.count);
				state.appendChild(dualityState);

				// Adding 'geometry' to 'State'
				Element geometryState = doc.createElementNS(NS, "geometry");

				// Adding 'gml:Point' to 'geometry'
				Element pointIn = doc.createElementNS(gmlNS, "gml:Point");
				pointIn.setAttributeNS(gmlNS, "gml:id", "Point" + igmlDoor.count);

				// Adding 'gml:pos' to 'gml:Point'
				Element posInState = doc.createElementNS(gmlNS, "gml:pos");
				Text posTextState = doc.createTextNode(igmlDoor.door_transGeom.getCentroid().getX() + " " + igmlDoor.door_transGeom.getCentroid().getY());
				posInState.appendChild(posTextState);
				pointIn.appendChild(posInState);
				geometryState.appendChild(pointIn);

				// Need to add the connects code
				for (IndoorGML_Room roomObjNow : roomIntersects2) {
					// boolean touches =
					// doorObjNow.door_geom.touches(igmlRoom.room_geom);
					Geometry geomIntersection = roomObjNow.room_geom.intersection(igmlDoor.door_geom);
					if (!geomIntersection.isEmpty()) {
						// Adding the 'connects' link to the corresponding
						// transition.
						Element connects = doc.createElementNS(NS, "connects");
						connects.setAttribute("xlink:href", "#T_" + roomObjNow.room_name.trim() + "_" + igmlDoor.count + "_"
								+ roomObjNow.room_floor);
						state.appendChild(connects);

						// Creating the transition for each 'connects'
						Element transitionMember = doc.createElementNS(NS, "indoorCore:transitionMember");

						// Adding 'Transition' tag to 'transitionMember'
						Element transition = doc.createElementNS(NS, "indoorCore:Transition");
						transition.setAttributeNS(gmlNS, "gml:id", "T_" + roomObjNow.room_name.trim() + "_" + igmlDoor.count + "_"
								+ roomObjNow.room_floor);

						// Adding weight to Transition
						Element weight = doc.createElementNS(NS, "weight");
						Text textWeight = doc.createTextNode("1");
						weight.appendChild(textWeight);
						
						List<String> d_transArray=new ArrayList<String>();

						// Adding start to Transition
						Element startNode = doc.createElementNS(NS, "start");
						startNode.setAttribute("xlink:href", "#S_D_" + igmlDoor.count + "_" + igmlDoor.door_floor);
						d_transArray.add(igmlDoor.door_transGeom.getCentroid().toString());

						// Adding end to Transition
						Element endNode = doc.createElementNS(NS, "end");
						endNode.setAttribute("xlink:href", "#S_R_" + roomObjNow.count + "_" + roomObjNow.room_floor);
						d_transArray.add(roomObjNow.room_transGeom.getCentroid().toString());

						// Adding geometry to Transition
						Element geometryxml = doc.createElementNS(NS, "geometry");

						// Adding gml:LineString to geometry
						Element lineStringxml = doc.createElementNS(gmlNS, "gml:LineString");
						lineStringxml.setAttributeNS(gmlNS, "gml:id", "LS" + lsCount);
						lsCount++;

						// Adding gml:pos to gml:LineString
						//String geomIn = geomIntersection.toString().replace("MULTIPOINT ", "").replace("(", "").replace(")", "").replace("LINESTRING ", "").replace("POLYGON ", "");
						//String[] geomInSplit = geomIn.split(", ");
						
						for (String entry : d_transArray) {
							String geomIn=entry.replace("POINT", "").replace("(", "").replace(")", "");
							Element posxml = doc.createElementNS(gmlNS, "gml:pos");
							Text textxml2 = doc.createTextNode(geomIn);
							posxml.appendChild(textxml2);
							lineStringxml.appendChild(posxml);
						}

						geometryxml.appendChild(lineStringxml);
						transition.appendChild(weight);
						transition.appendChild(startNode);
						transition.appendChild(endNode);
						transition.appendChild(geometryxml);
						transitionMember.appendChild(transition);
						edges.appendChild(transitionMember);
					}
				}

				// Adding the tags back to its parent.
				// state.appendChild(namexml2);
				// state.appendChild(dualityState);
				state.appendChild(geometryState);
				stateMember.appendChild(state);
				nodes.appendChild(stateMember);

				primalSpaceFeaturesTag.appendChild(cellSpaceMember);
			}

			// Repeating the existing code for StairsList
			for (IndoorGML_Stairs igmlStairs : IndoorGML_DB_Parser.stairsList) {

				com.infomatiq.jsi.Point ppIn = new com.infomatiq.jsi.Point((float) igmlStairs.stair_xcenter,
						(float) igmlStairs.stair_ycenter);
				ListIntProcedure lipIn = new ListIntProcedure();
				// IndoorGML_File_Parser.rTree.nearest(ppIn);
				IndoorGML_DB_Parser.rTree.nearestN(ppIn, lipIn, 10, Float.POSITIVE_INFINITY);
				List<Object> nearestObjs = lipIn.getSolution();
				//System.out.println("The list is : \n " + nearestObjs + "\n");
				List<IndoorGML_Room> roomIntersects3 = new ArrayList<IndoorGML_Room>();
				List<IndoorGML_Walls> wallIntersects3 = new ArrayList<IndoorGML_Walls>();
				List<IndoorGML_Doors> doorIntersects3 = new ArrayList<IndoorGML_Doors>();

				for (Object objIn : nearestObjs) {
					if (IndoorGML_DB_Parser.roomMap.get(objIn) != null) {
						IndoorGML_Room roomObj = (IndoorGML_Room) IndoorGML_DB_Parser.roomMap.get(objIn);
						roomIntersects3.add(roomObj);
					} else if (IndoorGML_DB_Parser.wallsMap.get(objIn) != null) {
						IndoorGML_Walls wallObj = (IndoorGML_Walls) IndoorGML_DB_Parser.wallsMap.get(objIn);
						wallIntersects3.add(wallObj);
					} else if (IndoorGML_DB_Parser.doorMap.get(objIn) != null) {
						IndoorGML_Doors doorObj = (IndoorGML_Doors) IndoorGML_DB_Parser.doorMap.get(objIn);
						doorIntersects3.add(doorObj);
					}
				}

				// Adding 'cellSpaceMember' tag to 'PrimalSpaceFeaturesTag'
				Element cellSpaceMember = doc.createElementNS(NS, "cellSpaceMember");

				// Adding 'CellSpace' tag to 'cellSpaceMember'
				Element sCellspace = doc.createElementNS(NS, "TransitionSpace");
				sCellspace.setAttributeNS(gmlNS, "gml:id", "CS" + igmlStairs.count);

				// Adding 'name' gml tag to 'CellSpace'
				Element namexml = doc.createElementNS(gmlNS, "gml:name");
				Text textxml = doc.createTextNode("StairID");
				// System.out.println(igmlRoom.room_name);
				//textxml = doc.createTextNode("ST_" + Integer.toString(igmlStairs.stair_count));
				textxml = doc.createTextNode(igmlStairs.stair_name);
				namexml.appendChild(textxml);
				sCellspace.appendChild(namexml);

				// Adding 'Geometry3D' to the 'CellSpace'
				Element geometry3D = doc.createElementNS(NS, "Geometry3D");

				// Adding 'Solid' tag to 'Geometry3D'
				Element solid = doc.createElementNS(gmlNS, "gml:Solid");
				solid.setAttributeNS(gmlNS, "gml:id", "sl" + igmlStairs.count);

				// Adding 'exterior' tag to 'Solid'
				Element exterior = doc.createElementNS(gmlNS, "gml:exterior");

				// Adding 'shell' tag to 'exterior'
				Element shell = doc.createElementNS(gmlNS, "gml:Shell");

				// Adding 'surfaceMember' tag to 'shell'
				Element surfaceMember = doc.createElementNS(gmlNS, "gml:surfaceMember");

				// Adding 'Polygon' tag to 'surfaceMember'
				Element polygon = doc.createElementNS(gmlNS, "gml:Polygon");
				polygon.setAttributeNS(gmlNS, "gml:id", "P" + igmlStairs.count);

				// Adding 'exterior' tag to 'polygon'
				Element exterior2 = doc.createElementNS(gmlNS, "gml:exterior");

				// Adding 'LinearRing' tag to 'exterior' i.e., exterior2 element
				Element linearRing = doc.createElementNS(gmlNS, "gml:LinearRing");

				// Adding 'pos' tag to 'LinearRing'
				String geomNow = igmlStairs.stair_transGeom.toString().replace("POLYGON", "").replace("MULTILINESTRING", "").replace("LINESTRING ", "").replace("(", "").replace(")", "");
				int floorNum = igmlStairs.stair_floor;
				String zPoint = Double.toString(floorNum * 0.04);
				String[] geomNowSplit = geomNow.split(", ");
				for (String entry : geomNowSplit) {
					String coord = entry.trim().concat(" ").concat(zPoint);
					Element pos = doc.createElementNS(gmlNS, "gml:pos");
					Text textxml2 = doc.createTextNode("EX");
					// System.out.println(entry);
					textxml2 = doc.createTextNode(coord);
					pos.appendChild(textxml2);
					linearRing.appendChild(pos);
				}

				exterior2.appendChild(linearRing);
				polygon.appendChild(exterior2);
				surfaceMember.appendChild(polygon);
				shell.appendChild(surfaceMember);
				exterior.appendChild(shell);
				solid.appendChild(exterior);
				geometry3D.appendChild(solid);
				sCellspace.appendChild(geometry3D);

				// Adding 'duality' to 'cellSpace'
				Element duality = doc.createElementNS(NS, "duality");
				duality.setAttribute("xlink:type", "simple");
				duality.setAttribute("xlink:href", "#S_T_" + igmlStairs.count + "_" + igmlStairs.stair_floor);
				sCellspace.appendChild(duality);

				// Adding 'externalReference' to 'cellSpace'
				// Element externalReference =
				// doc.createElement("externalReference");
				// cellSpace.appendChild(externalReference);

				cellSpaceMember.appendChild(sCellspace);

				// Creating elements for MultiLayeredGraphs
				// Adding 'stateMember' to nodes
				Element stateMember = doc.createElementNS(NS, "indoorCore:stateMember");

				// Adding 'State' to 'stateMember'
				Element state = doc.createElementNS(NS, "indoorCore:State");
				state.setAttributeNS(gmlNS, "gml:id", "S_T_" + igmlStairs.count + "_" + igmlStairs.stair_floor);

				// Adding 'gml:name' to 'State'
				Element namexml2 = doc.createElementNS(gmlNS, "gml:name");
				Text textxmlState = doc.createTextNode(igmlStairs.stair_name);
				namexml2.appendChild(textxmlState);
				state.appendChild(namexml2);

				// Adding 'duality' to 'State'
				Element dualityState = doc.createElementNS(NS, "duality");
				dualityState.setAttribute("xlink:type", "simple");
				dualityState.setAttribute("xlink:href", "#CS" + igmlStairs.count);
				state.appendChild(dualityState);

				// Adding 'geometry' to 'State'
				Element geometryState = doc.createElementNS(NS, "geometry");

				// Adding 'gml:Point' to 'geometry'
				Element pointIn = doc.createElementNS(gmlNS, "gml:Point");
				pointIn.setAttributeNS(gmlNS, "gml:id", "Point" + igmlStairs.count);

				// Adding 'gml:pos' to 'gml:Point'
				Element posInState = doc.createElementNS(gmlNS, "gml:pos");
				Text posTextState = doc.createTextNode(igmlStairs.stair_transGeom.getCentroid().getX() + " " + igmlStairs.stair_transGeom.getCentroid().getY());
				posInState.appendChild(posTextState);
				pointIn.appendChild(posInState);
				geometryState.appendChild(pointIn);

				// Need to add the connects code
				for (IndoorGML_Room roomObjNow : roomIntersects3) {
					// boolean touches =
					// doorObjNow.door_geom.touches(igmlRoom.room_geom);
					Geometry geomIntersection = roomObjNow.room_geom.intersection(igmlStairs.stair_geom);
					if (!geomIntersection.isEmpty()) {
						// Adding the 'connects' link to the corresponding
						// transition.
						Element connects = doc.createElementNS(NS, "connects");
						connects.setAttribute("xlink:href", "#T_" + roomObjNow.room_name.trim() + "_" + igmlStairs.count + "_"
								+ roomObjNow.room_floor);
						state.appendChild(connects);

						// Creating the transition for each 'connects'
						Element transitionMember = doc.createElementNS(NS, "indoorCore:transitionMember");

						// Adding 'Transition' tag to 'transitionMember'
						Element transition = doc.createElementNS(NS, "indoorCore:Transition");
						transition.setAttributeNS(gmlNS, "gml:id", "T_" + roomObjNow.room_name.trim() + "_" + igmlStairs.count + "_"
								+ roomObjNow.room_floor);

						// Adding weight to Transition
						Element weight = doc.createElementNS(NS, "weight");
						Text textWeight = doc.createTextNode("1");
						weight.appendChild(textWeight);
						
						List<String> s_transArray=new ArrayList<String>();

						// Adding start to Transition
						Element startNode = doc.createElementNS(NS, "start");
						startNode.setAttribute("xlink:href", "#S_T_" + igmlStairs.count + "_" + igmlStairs.stair_floor);
						s_transArray.add(igmlStairs.stair_transGeom.getCentroid().toString());

						// Adding end to Transition
						Element endNode = doc.createElementNS(NS, "end");
						endNode.setAttribute("xlink:href", "#S_R_" + roomObjNow.count + "_" + roomObjNow.room_floor);
						s_transArray.add(roomObjNow.room_transGeom.getCentroid().toString());

						// Adding geometry to Transition
						Element geometryxml = doc.createElementNS(NS, "geometry");

						// Adding gml:LineString to geometry
						Element lineStringxml = doc.createElementNS(gmlNS, "gml:LineString");
						lineStringxml.setAttributeNS(gmlNS, "gml:id", "LS" + lsCount);
						lsCount++;

						// Adding gml:pos to gml:LineString
						//String geomIn = geomIntersection.toString().replace("MULTIPOINT ", "").replace("(", "").replace(")", "").replace("MULTILINESTRING ", "").replace("LINESTRING ", "").replace("POLYGON ", "");
						//String[] geomInSplit = geomIn.split(", ");
						for (String entry : s_transArray) {
							String geomIn=entry.replace("POINT", "").replace("(", "").replace(")", "");
							Element posxml = doc.createElementNS(gmlNS, "gml:pos");
							Text textxml2 = doc.createTextNode(geomIn);
							posxml.appendChild(textxml2);
							lineStringxml.appendChild(posxml);
						}

						geometryxml.appendChild(lineStringxml);
						transition.appendChild(weight);
						transition.appendChild(startNode);
						transition.appendChild(endNode);
						transition.appendChild(geometryxml);
						transitionMember.appendChild(transition);
						edges.appendChild(transitionMember);
					}
					
				}
				
				//Transform the stair geometry to check if it overlaps with the stairs in the next floor
				int stair_next_floor=(igmlStairs.stair_floor+1);
				int stair_prev_floor=(igmlStairs.stair_floor-1);
				Geometry stairNow_geom=igmlStairs.stair_transGeom;
				int flagForStairs=0;
				for(IndoorGML_Stairs stairIn:IndoorGML_DB_Parser.stairsList)
				{
					if((stair_next_floor==stairIn.stair_floor)||(stair_prev_floor==stairIn.stair_floor))
					{
						flagForStairs=1;
						Geometry stairNext_geom=stairIn.stair_transGeom;
						if(stairNow_geom.overlaps(stairNext_geom))
						{
							Element connects = doc.createElementNS(NS, "connects");
							connects.setAttribute("xlink:href", "#T_" + stairIn.stair_name.trim() + "_" + igmlStairs.count + "_"
									+ stairIn.stair_floor);
							state.appendChild(connects);

							// Creating the transition for each 'connects'
							Element transitionMember = doc.createElementNS(NS, "indoorCore:transitionMember");

							// Adding 'Transition' tag to 'transitionMember'
							Element transition = doc.createElementNS(NS, "indoorCore:Transition");
							transition.setAttributeNS(gmlNS, "gml:id", "T_" + stairIn.stair_name.trim() + "_" + igmlStairs.count + "_"
									+ stairIn.stair_floor);

							// Adding weight to Transition
							Element weight = doc.createElementNS(NS, "weight");
							Text textWeight = doc.createTextNode("1");
							weight.appendChild(textWeight);
							
							//List<String> s_transArray=new ArrayList<String>();

							// Adding start to Transition
							Element startNode = doc.createElementNS(NS, "start");
							startNode.setAttribute("xlink:href", "#S_T_" + igmlStairs.count + "_" + igmlStairs.stair_floor);
							//s_transArray.add(igmlStairs.stair_centroid.toString());

							// Adding end to Transition
							Element endNode = doc.createElementNS(NS, "end");
							endNode.setAttribute("xlink:href", "#S_T_" + stairIn.count + "_" + stairIn.stair_floor);
							//s_transArray.add(stairIn.stair_centroid.toString());

							// Adding geometry to Transition
							Element geometryxml = doc.createElementNS(NS, "geometry");

							// Adding gml:LineString to geometry
							Element lineStringxml = doc.createElementNS(gmlNS, "gml:LineString");
							lineStringxml.setAttributeNS(gmlNS, "gml:id", "LS" + lsCount);
							lsCount++;
							ArrayList<Coordinate> coordList=new ArrayList<Coordinate>();
							coordList.add(new Coordinate(igmlStairs.stair_transGeom.getCentroid().getX(),igmlStairs.stair_transGeom.getCentroid().getY()));
							coordList.add(new Coordinate(stairIn.stair_transGeom.getCentroid().getX(),stairIn.stair_transGeom.getCentroid().getY()));
							Coordinate[] coordArray = coordList.toArray(new Coordinate[coordList.size()]);
							GeometryFactory gf2 = new GeometryFactory();
							LineString ls=gf2.createLineString(coordArray);
							String geomIn = ls.toString().replace("MULTIPOINT ", "").replace("(", "").replace(")", "").replace("MULTILINESTRING ", "").replace("LINESTRING ", "").replace("POLYGON ", "");
							String[] geomInSplit = geomIn.split(", ");
							for (String entry : geomInSplit) {
								Element posxml = doc.createElementNS(gmlNS, "gml:pos");
								Text textxml2 = doc.createTextNode(entry);
								posxml.appendChild(textxml2);
								lineStringxml.appendChild(posxml);
							}

							geometryxml.appendChild(lineStringxml);
							transition.appendChild(weight);
							transition.appendChild(startNode);
							transition.appendChild(endNode);
							transition.appendChild(geometryxml);
							transitionMember.appendChild(transition);
							edges.appendChild(transitionMember);

						}
					}
				}
				/*if(flagForStairs==0)
				{
					//To connect stairs to the next floor corridor
					for(IndoorGML_Room roomIn:IndoorGML_File_Parser.roomList)
					{
						if((stair_next_floor)==roomIn.room_floor)
						{
							if(roomIn.room_name.toLowerCase().contains("corridor"))
							{
								Element connects = doc.createElementNS(NS, "connects");
								connects.setAttribute("xlink:href", "#T_" + roomIn.room_name.trim() + "_" + igmlStairs.count + "_"
											+ roomIn.room_floor);
								state.appendChild(connects);
											
								//Creating the transition for each 'connects'
								Element transitionMember = doc.createElementNS(NS, "indoorCore:transitionMember");

								// Adding 'Transition' tag to 'transitionMember'
								Element transition = doc.createElementNS(NS, "indoorCore:Transition");
								transition.setAttributeNS(gmlNS, "gml:id", "T_" + roomIn.room_name.trim() + "_" + igmlStairs.count + "_"
											+ roomIn.room_floor);

								// Adding weight to Transition
								Element weight = doc.createElementNS(NS, "weight");
								Text textWeight = doc.createTextNode("1");
								weight.appendChild(textWeight);

								// Adding start to Transition
								Element startNode = doc.createElementNS(NS, "start");
								startNode.setAttribute("xlink:href", "#S_T_" + igmlStairs.count + "_" + igmlStairs.stair_floor);

								// Adding end to Transition
								Element endNode = doc.createElementNS(NS, "end");
								endNode.setAttribute("xlink:href", "#S_R_" + roomIn.count + "_" + roomIn.room_floor);

								// Adding geometry to Transition
								Element geometryxml = doc.createElementNS(NS, "geometry");

								// Adding gml:LineString to geometry
								Element lineStringxml = doc.createElementNS(gmlNS, "gml:LineString");
								lineStringxml.setAttributeNS(gmlNS, "gml:id", "LS" + lsCount);
								lsCount++;
								ArrayList<Coordinate> coordList=new ArrayList<Coordinate>();
								coordList.add(new Coordinate(igmlStairs.stair_xcenter,igmlStairs.stair_ycenter));
								coordList.add(new Coordinate(roomIn.room_xcenter,roomIn.room_ycenter));
								Coordinate[] coordArray = coordList.toArray(new Coordinate[coordList.size()]);
								GeometryFactory gf2 = new GeometryFactory();
								LineString ls=gf2.createLineString(coordArray);
								String geomIn = ls.toString().replace("MULTIPOINT ", "").replace("(", "").replace(")", "").replace("MULTILINESTRING ", "").replace("LINESTRING ", "").replace("POLYGON ", "");
								String[] geomInSplit = geomIn.split(", ");
								for (String entry : geomInSplit) {
									Element posxml = doc.createElementNS(gmlNS, "gml:pos");
									Text textxml2 = doc.createTextNode(entry);
									posxml.appendChild(textxml2);
									lineStringxml.appendChild(posxml);
								}

								geometryxml.appendChild(lineStringxml);
								transition.appendChild(weight);
								transition.appendChild(startNode);
								transition.appendChild(endNode);
								transition.appendChild(geometryxml);
								transitionMember.appendChild(transition);
								edges.appendChild(transitionMember);
							}
						}
					}
				}*/
				// Adding the tags back to its parent.
				// state.appendChild(namexml2);
				// state.appendChild(dualityState);
				state.appendChild(geometryState);
				stateMember.appendChild(state);
				nodes.appendChild(stateMember);
				primalSpaceFeaturesTag.appendChild(cellSpaceMember);
			}

			// Repeating the existing code for WindowList
			/*for (IndoorGML_Windows igmlWindows : IndoorGML_File_Parser.windowsList) {

				com.infomatiq.jsi.Point ppIn = new com.infomatiq.jsi.Point((float) igmlWindows.window_xcenter,
						(float) igmlWindows.window_ycenter);
				ListIntProcedure lipIn = new ListIntProcedure();
				// IndoorGML_File_Parser.rTree.nearest(ppIn);
				IndoorGML_File_Parser.rTree.nearestN(ppIn, lipIn, 10, Float.POSITIVE_INFINITY);
				List<Object> nearestObjs = lipIn.getSolution();
				System.out.println("The list is : \n " + nearestObjs + "\n");
				List<IndoorGML_Room> roomIntersects4 = new ArrayList<IndoorGML_Room>();
				List<IndoorGML_Walls> wallIntersects4 = new ArrayList<IndoorGML_Walls>();
				List<IndoorGML_Doors> doorIntersects4 = new ArrayList<IndoorGML_Doors>();

				for (Object objIn : nearestObjs) {
					if (IndoorGML_File_Parser.roomMap.get(objIn) != null) {
						IndoorGML_Room roomObj = (IndoorGML_Room) IndoorGML_File_Parser.roomMap.get(objIn);
						roomIntersects4.add(roomObj);
					} else if (IndoorGML_File_Parser.wallsMap.get(objIn) != null) {
						IndoorGML_Walls wallObj = (IndoorGML_Walls) IndoorGML_File_Parser.wallsMap.get(objIn);
						wallIntersects4.add(wallObj);
					} else if (IndoorGML_File_Parser.doorMap.get(objIn) != null) {
						IndoorGML_Doors doorObj = (IndoorGML_Doors) IndoorGML_File_Parser.doorMap.get(objIn);
						doorIntersects4.add(doorObj);
					}
				}

				// Adding 'cellSpaceMember' tag to 'PrimalSpaceFeaturesTag'
				Element cellSpaceMember = doc.createElementNS(NS, "cellSpaceMember");

				// Adding 'CellSpace' tag to 'cellSpaceMember'
				Element wCellspace = doc.createElementNS(NS, "ConnectionSpace");
				wCellspace.setAttributeNS(gmlNS, "gml:id", "CS" + igmlWindows.count);

				// Adding 'name' gml tag to 'CellSpace'
				Element namexml = doc.createElementNS(gmlNS, "gml:name");
				Text textxml = doc.createTextNode("StairID");
				// System.out.println(igmlRoom.room_name);
				textxml = doc.createTextNode("WD" + Integer.toString(igmlWindows.window_count));
				namexml.appendChild(textxml);
				wCellspace.appendChild(namexml);

				// Adding 'Geometry3D' to the 'CellSpace'
				Element geometry3D = doc.createElementNS(NS, "Geometry3D");

				// Adding 'Solid' tag to 'Geometry3D'
				Element solid = doc.createElementNS(gmlNS, "gml:Solid");
				solid.setAttributeNS(gmlNS, "gml:id", "sl" + igmlWindows.count);

				// Adding 'exterior' tag to 'Solid'
				Element exterior = doc.createElementNS(gmlNS, "gml:exterior");

				// Adding 'shell' tag to 'exterior'
				Element shell = doc.createElementNS(gmlNS, "gml:Shell");

				// Adding 'surfaceMember' tag to 'shell'
				Element surfaceMember = doc.createElementNS(gmlNS, "gml:surfaceMember");

				// Adding 'Polygon' tag to 'surfaceMember'
				Element polygon = doc.createElementNS(gmlNS, "gml:Polygon");
				polygon.setAttributeNS(gmlNS, "gml:id", "P" + igmlWindows.count);

				// Adding 'exterior' tag to 'polygon'
				Element exterior2 = doc.createElementNS(gmlNS, "gml:exterior");

				// Adding 'LinearRing' tag to 'exterior' i.e., exterior2 element
				Element linearRing = doc.createElementNS(gmlNS, "gml:LinearRing");

				// Adding 'pos' tag to 'LinearRing'
				String geomNow = igmlWindows.window_geom.toString().replace("POLYGON", "").replace("MULTILINESTRING", "")
						.replace("LINESTRING ", "").replace("(", "").replace(")", "");
				int floorNum = igmlWindows.window_floor;
				String zPoint = Double.toString(floorNum * 0.04);
				String[] geomNowSplit = geomNow.split(", ");
				for (String entry : geomNowSplit) {
					String coord = entry.trim().concat(" ").concat(zPoint);
					Element pos = doc.createElementNS(gmlNS, "gml:pos");
					Text textxml2 = doc.createTextNode("EX");
					// System.out.println(entry);
					textxml2 = doc.createTextNode(coord);
					pos.appendChild(textxml2);
					linearRing.appendChild(pos);
				}

				exterior2.appendChild(linearRing);
				polygon.appendChild(exterior2);
				surfaceMember.appendChild(polygon);
				shell.appendChild(surfaceMember);
				exterior.appendChild(shell);
				solid.appendChild(exterior);
				geometry3D.appendChild(solid);
				wCellspace.appendChild(geometry3D);

				// Adding 'duality' to 'cellSpace'
				Element duality = doc.createElementNS(NS, "duality");
				duality.setAttribute("xlink:type", "simple");
				duality.setAttribute("xlink:href", "#S_W_" + igmlWindows.count + "_" + igmlWindows.window_floor);
				wCellspace.appendChild(duality);

				// Adding 'externalReference' to 'cellSpace'
				// Element externalReference =
				// doc.createElement("externalReference");
				// cellSpace.appendChild(externalReference);

				cellSpaceMember.appendChild(wCellspace);

				// Creating elements for MultiLayeredGraphs
				// Adding 'stateMember' to nodes
				Element stateMember = doc.createElementNS(NS, "stateMember");

				// Adding 'State' to 'stateMember'
				Element state = doc.createElementNS(NS, "indoorCore:State");
				state.setAttributeNS(gmlNS, "gml:id", "S_W_" + igmlWindows.count + "_" + igmlWindows.window_floor);

				// Adding 'gml:name' to 'State'
				Element namexml2 = doc.createElementNS(gmlNS, "gml:name");
				Text textxmlState = doc.createTextNode(Integer.toString(igmlWindows.window_count));
				namexml2.appendChild(textxmlState);
				state.appendChild(namexml2);

				// Adding 'duality' to 'State'
				Element dualityState = doc.createElementNS(NS, "duality");
				dualityState.setAttribute("xlink:type", "simple");
				dualityState.setAttribute("xlink:href", "#CS" + igmlWindows.count);
				state.appendChild(dualityState);

				// Adding 'geometry' to 'State'
				Element geometryState = doc.createElementNS(NS, "geometry");

				// Adding 'gml:Point' to 'geometry'
				Element pointIn = doc.createElementNS(gmlNS, "gml:Point");
				pointIn.setAttributeNS(gmlNS, "gml:id", "Point" + igmlWindows.count);

				// Adding 'gml:pos' to 'gml:Point'
				Element posInState = doc.createElementNS(gmlNS, "gml:pos");
				Text posTextState = doc.createTextNode(igmlWindows.window_xcenter + " " + igmlWindows.window_ycenter);
				posInState.appendChild(posTextState);
				pointIn.appendChild(posInState);
				geometryState.appendChild(pointIn);

				// Need to add the connects code
				for (IndoorGML_Room roomObjNow : roomIntersects4) {
					// boolean touches =
					// doorObjNow.door_geom.touches(igmlRoom.room_geom);
					Geometry geomIntersection = roomObjNow.room_geom.intersection(igmlWindows.window_geom);
					if (!geomIntersection.isEmpty()) {
						// Adding the 'connects' link to the corresponding
						// transition.
						Element connects = doc.createElementNS(NS, "connects");
						connects.setAttribute("xlink:href", "#T_" + roomObjNow.room_name.trim() + "_" + igmlWindows.count + "_"
								+ roomObjNow.room_floor);
						state.appendChild(connects);

						// Creating the transition for each 'connects'
						Element transitionMember = doc.createElementNS(NS, "transitionMember");

						// Adding 'Transition' tag to 'transitionMember'
						Element transition = doc.createElementNS(NS, "indoorCore:Transition");
						transition.setAttributeNS(gmlNS, "gml:id", "T_" + roomObjNow.room_name.trim() + "_" + igmlWindows.count + "_"
								+ roomObjNow.room_floor);

						// Adding weight to Transition
						Element weight = doc.createElementNS(NS, "weight");
						Text textWeight = doc.createTextNode("1");
						weight.appendChild(textWeight);

						// Adding start to Transition
						Element startNode = doc.createElementNS(NS, "start");
						startNode.setAttribute("xlink:href", "#S_W_" + igmlWindows.count + "_" + igmlWindows.window_floor);

						// Adding end to Transition
						Element endNode = doc.createElementNS(NS, "end");
						endNode.setAttribute("xlink:href", "#S_R_" + roomObjNow.count + "_" + roomObjNow.room_floor);

						// Adding geometry to Transition
						Element geometryxml = doc.createElementNS(NS, "geometry");

						// Adding gml:LineString to geometry
						Element lineStringxml = doc.createElementNS(gmlNS, "gml:LineString");
						lineStringxml.setAttributeNS(gmlNS, "gml:id", "LS" + lsCount);
						lsCount++;

						// Adding gml:pos to gml:LineString
						String geomIn = geomIntersection.toString().replace("MULTIPOINT ", "").replace("(", "").replace(")", "")
								.replace("MULTILINESTRING ", "").replace("LINESTRING ", "").replace("POLYGON ", "");
						String[] geomInSplit = geomIn.split(", ");
						for (String entry : geomInSplit) {
							Element posxml = doc.createElementNS(gmlNS, "gml:pos");
							Text textxml2 = doc.createTextNode(entry);
							posxml.appendChild(textxml2);
							lineStringxml.appendChild(posxml);
						}

						geometryxml.appendChild(lineStringxml);
						transition.appendChild(weight);
						transition.appendChild(startNode);
						transition.appendChild(endNode);
						transition.appendChild(geometryxml);
						transitionMember.appendChild(transition);
						edges.appendChild(transitionMember);
					}
				}

				// Adding the tags back to its parent.
				// state.appendChild(namexml2);
				// state.appendChild(dualityState);
				state.appendChild(geometryState);
				stateMember.appendChild(state);
				nodes.appendChild(stateMember);

				primalSpaceFeaturesTag.appendChild(cellSpaceMember);
			}*/

			spaceLayer.appendChild(nodes);
			spaceLayer.appendChild(edges);
			spaceLayerMember.appendChild(spaceLayer);
			spaceLayers.appendChild(spaceLayerMember);
			multiLayeredGraph.appendChild(spaceLayers);
			primalSpaceFeatures.appendChild(primalSpaceFeaturesTag);
			elk.appendChild(primalSpaceFeatures);
			elk.appendChild(multiLayeredGraph);

			doc.appendChild(elk);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(doc);
			File fileFinal = new File(xmlFilePath + "/IndoorGML_Document.xml");
			StreamResult streamResult = new StreamResult(fileFinal);

			transformer.transform(domSource, streamResult);
			//validate(xmlFilePath + "/IndoorGML_Document.xml");
			JOptionPane.showMessageDialog(null, "IndoorGML_Document.xml" + " is created!", "IIRMS - Message", 1);
		} catch (DOMException | TransformerFactoryConfigurationError | TransformerException | HeadlessException Ex) {
			Ex.printStackTrace();
		}
	}


	public static void validate(String gmlFile) throws Exception {
		//URL schemaFile = new URL("http://schemas.opengis.net/indoorgml/1.0/indoorgmlcore.xsd");
		//URL schemaFile = new URL("http://schemas.opengis.net/indoorgml/1.0/indoorgmlnavi.xsd");
		File schemaFile=new File("/home/researcher/workspace/IndoorGML_Toolkit/src/IndoorNavigation.xsd");
		Source xmlFile = new StreamSource(new File(gmlFile));
		SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = schemaFactory.newSchema(schemaFile);

		try {
			Validator validator = schema.newValidator();
			validator.validate(xmlFile);
			JOptionPane.showMessageDialog(null, "IndoorGML_Document.xml" + " is created and is valid aganist the IndoorGML schema!",
					"IndoorGMLToolkit - Message", 1);
			// System.out.println(xmlFile.getSystemId()+" is valid");
		} catch (SAXException sax) {
			// System.out.println(xmlFile.getSystemId()+" is not valid");
			JOptionPane.showMessageDialog(null, "IndoorGML_Document.xml" + " is created but is not valid aganist the IndoorGML schema!",
					"IndoorGMLToolkit - Message", 1);
			System.out.println("Reason:" + sax.getLocalizedMessage());
		}
	}
}