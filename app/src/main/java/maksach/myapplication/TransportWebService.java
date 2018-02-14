package maksach.myapplication;

import android.os.AsyncTask;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Tang- on 10/27/2017.
 */

public  class TransportWebService extends AsyncTask
{

    ArrayList<String> year_list = new ArrayList<>();

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            URL url;
            url = new URL("http://www.fueleconomy.gov/ws/rest/vehicle/menu/year");
            URLConnection conn = url.openConnection();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream a = conn.getInputStream();
            Document doc = builder.parse(a);
            NodeList nodes = doc.getElementsByTagName("menuItem");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                year_list.add(element.getFirstChild().getTextContent());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return year_list;
    }
    public ArrayList<String> heads()
    {
        return year_list;
    }
}
