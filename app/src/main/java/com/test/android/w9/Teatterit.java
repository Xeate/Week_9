package com.test.android.w9;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class Teatterit {

    private static String teatteriNimi;
    private static String teatteriId;

    List<String> Shows = new ArrayList();
    List<String> Locations = new ArrayList();
    List<String> Times = new ArrayList();

    // Singleton
    private static Teatterit teatterit = new Teatterit();
    ArrayList<TeatteriInfo> teatteriList = new ArrayList();
    public static Teatterit getInstance(){
        return teatterit;
    }

    private Teatterit() {

        readTheaterXML();

    }

    //Haetaan alueet/teatterit
    public void readTheaterXML() {

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String urlString = "https://www.finnkino.fi/xml/TheatreAreas/";
            Document doc = builder.parse(urlString);
            doc.getDocumentElement().normalize();
            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

            NodeList TeatteriList = doc.getDocumentElement().getElementsByTagName("TheatreArea");

            for (int i = 0 ; i < TeatteriList.getLength() ; i++) {

                Node node = TeatteriList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    System.out.println(element.getElementsByTagName("ID").item(0).getTextContent());
                    teatteriId = element.getElementsByTagName("ID").item(0).getTextContent();
                    System.out.println(element.getElementsByTagName("Name").item(0).getTextContent());
                    teatteriNimi = element.getElementsByTagName("Name").item(0).getTextContent();

                    if(i == 1) {
                        addTheater("Kaikki", "0");
                    }

                    addTheater(teatteriNimi, teatteriId);

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }   finally {
            System.out.println("##############DONE##############");
        }
    }

    //Haetaan leffat alueella/teatterissa
    public List readShowXML(String id, String pvm, String after, String before) {

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String urlString = "https://www.finnkino.fi/xml/Schedule/?area=" + id + "&dt=" + pvm;
            Document doc = builder.parse(urlString);
            doc.getDocumentElement().normalize();
            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

            NodeList ShowList = doc.getDocumentElement().getElementsByTagName("Show");

            for (int i = 0 ; i < ShowList.getLength() ; i++) {

                Node node = ShowList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    System.out.println(element.getElementsByTagName("Title").item(0).getTextContent());
                    String showTitle = element.getElementsByTagName("Title").item(0).getTextContent();
                    String showStart = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();

                    //Muodostetaan käyttäjän aikasyötteistä vertailtavia osia
                    Integer showStartH = Integer.parseInt(showStart.substring(11,13));
                    Integer showStartM = Integer.parseInt(showStart.substring(14,16));

                    Integer userAfterH = Integer.parseInt(after.substring(0,2));
                    Integer userAfterM = Integer.parseInt(after.substring(3,5));

                    Integer userBeforeH = Integer.parseInt(before.substring(0,2));
                    Integer userBeforeM = Integer.parseInt(before.substring(3,5));

                    System.out.println("########## " + userBeforeH + "   " + userBeforeM + " " + showStartH + " " + showStartM + " ##########");

                    //Vertaillaan käyttäjän aikasyötteitä leffojen alkamiseen
                    if (userAfterH < showStartH && userBeforeH > showStartH) {
                        Shows.add(showTitle);
                        Times.add(showStart);
                    } else if (userAfterH.equals(showStartH) && userBeforeH.equals(showStartH)) {
                        if (userAfterM <= showStartM && userBeforeM >= userBeforeM) {
                            Shows.add(showTitle);
                            Times.add(showStart);
                        }
                    } else if (userAfterH.equals(showStartH)) {
                        if (userAfterM <= showStartM) {
                            Shows.add(showTitle);
                            Times.add(showStart);
                        }
                    } else if (userBeforeH.equals(showStartH)) {
                        if (userBeforeM >= userBeforeM) {
                            Shows.add(showTitle);
                            Times.add(showStart);
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }   finally {
            System.out.println("##############DONE##############");
        }

        if (Shows.size() < 1) {
            Shows.add("Ei näytöksiä :(");
            Times.add("");
        }
        return Shows;
    }

    //Kun haetaan leffan nimen perusteella
    public List readShowXMLMovies(String id, String pvm, String after, String before, String movieName) {

        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            String urlString = "https://www.finnkino.fi/xml/Schedule/?area=" + id + "&dt=" + pvm;
            Document doc = builder.parse(urlString);
            doc.getDocumentElement().normalize();
            System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

            NodeList ShowList = doc.getDocumentElement().getElementsByTagName("Show");

            for (int i = 0 ; i < ShowList.getLength() ; i++) {

                Node node = ShowList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    System.out.println(element.getElementsByTagName("Title").item(0).getTextContent());
                    String showTitle = element.getElementsByTagName("Title").item(0).getTextContent();
                    String showStart = element.getElementsByTagName("dttmShowStart").item(0).getTextContent();
                    String showLocation = element.getElementsByTagName("Theatre").item(0).getTextContent();

                    //Muodostetaan käyttäjän aikasyötteistä vertailtavia osia
                    Integer showStartH = Integer.parseInt(showStart.substring(11,13));
                    Integer showStartM = Integer.parseInt(showStart.substring(14,16));

                    Integer userAfterH = Integer.parseInt(after.substring(0,2));
                    Integer userAfterM = Integer.parseInt(after.substring(3,5));

                    Integer userBeforeH = Integer.parseInt(before.substring(0,2));
                    Integer userBeforeM = Integer.parseInt(before.substring(3,5));

                    System.out.println("########## " + userBeforeH + "   " + userBeforeM + " " + showStartH + " " + showStartM + " ##########");

                    //Vertaillaan käyttäjän aikasyötteitä leffojen alkamiseen
                    if (movieName.equals(showTitle)) {
                        if (userAfterH < showStartH && userBeforeH > showStartH) {
                            Locations.add(showLocation);
                            Times.add(showStart);
                        } else if (userAfterH.equals(showStartH) && userBeforeH.equals(showStartH)) {
                            if (userAfterM <= showStartM && userBeforeM >= userBeforeM) {
                                Locations.add(showLocation);
                                Times.add(showStart);
                            }
                        } else if (userAfterH.equals(showStartH)) {
                            if (userAfterM <= showStartM) {
                                Locations.add(showLocation);
                                Times.add(showStart);
                            }
                        } else if (userBeforeH.equals(showStartH)) {
                            if (userBeforeM >= userBeforeM) {
                                Locations.add(showLocation);
                                Times.add(showStart);
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }   finally {
            System.out.println("##############DONE##############");
        }

        if (Locations.size() < 1) {
            Locations.add("Ei näytöksiä haetulle elokuvalle :(");
            Times.add("");
        }
        return Locations;
    }

    public void addTheater(String area, String id){
        TeatteriInfo teatteri = new TeatteriInfo(area, id);
        teatteriList.add(teatteri);
    }

    public String setTheaterList(int i){

        return teatteriList.get(i).getArea();
    }

    public List getTimeList() {
        return Times;
    }


    public int getTheaterAmount() {

        return teatteriList.size();
    }

    public String getTheaterId(String theaterName) {
        for(int i = 0; i < teatteriList.size(); i++){
            if(theaterName == teatteriList.get(i).getArea()){
                return teatteriList.get(i).getID();
            }
        }
        return "1029";
    }
}