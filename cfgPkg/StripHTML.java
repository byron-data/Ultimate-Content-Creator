package cfgPkg;

import java.io.*;
import java.util.Vector;
import javax.swing.text.*;
import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;

public class StripHTML extends HTMLEditorKit.ParserCallback {
    boolean startTag = true;
    public  Vector vector;
    public  String getTxt = "";
    
    int     element = 0;

    public void handleText(char[] data, int pos) {
        String text = new String(data);

        if (startTag) {
            pos = text.indexOf(cfgPkg.Const.startPreserveTags);
            if (pos != -1) {                                                                         // found a start tag
                getTxt = getTxt + text.substring(0, pos);                                            // take any text up to the start tag
                int pos2 = text.indexOf(cfgPkg.Const.endPreserveTags);
                if (pos2 != -1) {                                                                    // found an end tag as well
                    if (vector.size() > element) getTxt = getTxt + vector.elementAt(element).toString(); // add the preserved text
                    getTxt = getTxt + text.substring(pos2+cfgPkg.Const.endPreserveTags.length());    // take any text after the end tag
                    element++;
                } else {
                    startTag = false;
                }
            }
            else                                                                                     // no start tag or end tag
                getTxt = getTxt + text;
        } else {
            int pos2 = text.indexOf(cfgPkg.Const.endPreserveTags);
            if (pos2 != -1) {                                                                        // found an end end tag
                if (vector.size() > element) getTxt = getTxt + vector.elementAt(element).toString(); // add the preserved text
                getTxt = getTxt + text.substring(pos2+cfgPkg.Const.endPreserveTags.length());        // take any text after the end tag
                element++;
                startTag = true;
            }
        }
    }

    public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        //if (preserveTags) { getTxt = getTxt + "<" + t + ">";
    }

    public void handleEndTag(HTML.Tag t, int pos) {
        //if (preserveTags) { getTxt = getTxt + "</" + t + ">";
    }

    public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos) {
        //if (preserveTags) { getTxt = getTxt + "</" + t + ">";
    }
}
