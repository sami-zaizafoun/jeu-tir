package graphics;

import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handler used to read and interprete XML level.
 * Juste implementing methods and read xml file.
 */
public class LevelHandlerParser extends DefaultHandler{

    Stack<String> balises= new Stack<>();
    public String chemin;
    public ArrayList<String> listCase = new ArrayList<>();
    String buildingChemin = "";
    public int x;
    public int y;
    public int nbLayer = 0;
    private final boolean end = false;

    public LevelHandlerParser(){
    }
    
    @Override
    public void startElement(String namespaceURI,String localName,String qname,Attributes atts) throws SAXException {
        balises.push(qname);
        if(qname == "layer"){
            nbLayer++;
            x = Integer.parseInt(atts.getValue("width"));
            y = Integer.parseInt(atts.getValue("height"));
        }
    }

    @Override
    public void endElement(String uri,String localName, String qname) {
        if(qname == "data"){
            listCase.add(",");
        }
        balises.pop();
    }
    @Override
    public void characters(char[] ch, int start, int length) {
        String str =  new String(ch,start,length);
        if(str.trim().length() > 0 ){
            listCase.add(str);
        }
    }
}
