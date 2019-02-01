package graphics;

import java.util.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Handler used for parse XML player's images coordinates.
 * basic handler parser.
 */
public class PlayerImageParser extends DefaultHandler {

    public String chemin;
    public ArrayList<String> listCase = new ArrayList<>();
    String buildingChemin = "";
    public int x;
    public int y;
    private final boolean end = false;
    HashMap<String, ArrayList<Integer>> playerImages = new HashMap<>();
    ArrayList<String> playerNames = new ArrayList<>();

    public PlayerImageParser(){
    }

    @Override
    public void startElement(String namespaceURI,String localName,String qname,Attributes atts) throws SAXException {
       if(qname == "SubTexture"){
           x = Integer.parseInt(atts.getValue("x"));
           y = Integer.parseInt(atts.getValue("y"));
           int width = Integer.parseInt(atts.getValue("width"));
           int height = Integer.parseInt(atts.getValue("height"));
           playerNames.add(atts.getValue("name"));
           playerImages.put(atts.getValue("name"), new ArrayList<>(Arrays.asList(
                   x,y,width,height
           )));
       }
    }

    @Override
    public void endElement(String uri,String localName, String qname) {
       if(qname == "data"){
           listCase.add(",");
       }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
       String str =  new String(ch,start,length);
       if(str.trim().length() > 0 ){
           listCase.add(str);
       }
    }
}
