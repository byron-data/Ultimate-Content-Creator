package cfgPkg;

import java.io.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;

public class ParseHTML extends HTMLEditorKit.ParserCallback {
    // parameters
    public String  openTag  = "";
    public boolean textSeek = false;

    // return values
    public  String getTxt  = "";
    public  String keyword = "";

    boolean bodyFound = false;
    boolean tagFound  = false;
    
    public void handleText(char[] data, int pos) {
        if (bodyFound) {
            String text = new String(data);

            pos = -1;
            if (!tagFound && textSeek)
                pos = text.indexOf(openTag);

            if (pos != -1) {
                tagFound = true;
                getTxt = text.substring(pos);
            } else {
                if (getTxt.length() == 0) {
                    getTxt = text;
                } else
                    getTxt = getTxt + text;
            }
        }
    }

    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        if (bodyFound && (t == HTML.Tag.BR || t == HTML.Tag.P))
            getTxt = getTxt + "<" + t + ">";        
        if (t == HTML.Tag.BODY) {
            bodyFound = true;
        }
    }

    public void handleEndTag(HTML.Tag t, int pos) {
        if (bodyFound && t == HTML.Tag.P)
            getTxt = getTxt + "</" + t + ">";
        if (t == HTML.Tag.BODY) {
            bodyFound = false;
        }
    }

    public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        //if (bodyFound) getTxt = getTxt + "</" + t + ">";
        if (t.toString().toLowerCase().equals("meta")) {
            String name = (String)a.getAttribute(HTML.Attribute.NAME);
            if (name != null && name.toLowerCase().equals("keywords")) {
                //System.out.println("name:"+name);
                keyword = (String)a.getAttribute(HTML.Attribute.CONTENT);
                if (keyword != null) {
                    pos = keyword.indexOf(",");
                    if (pos == -1)
                        keyword = keyword.toLowerCase();
                    else
                        keyword = keyword.substring(0, keyword.indexOf(",")).toLowerCase();
                    //System.out.println("content:"+keyword);
                }
            }
        }
    }
}
