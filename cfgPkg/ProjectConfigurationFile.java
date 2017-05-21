package cfgPkg;

import java.awt.Color;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;

public class ProjectConfigurationFile implements Serializable {
    // ProjectSetupTab values
    public String            inputDir            = "";
    public String            outputDir           = "";
    public String            snippetCharsTxt     = "250";
    public String            extensionTxt        = "php";
    public String            previewCharsTxt     = "250";
    public String            contentTemplate     = "";
    public String            indexTemplate       = "";
    public String            editor              = cfgPkg.Const.defaultLoc;
    
    // GetTagsTab values
    public DefaultTableModel tagTableModel       = null;
    
    // BatchProcessTab values
    public DefaultListModel  fileListModel       = null;
    public String            previewTag          = "";
    public DefaultTableModel generatedFilesModel = null;
    
    // RssFeedTab values
    public DefaultTableModel rssSearchReplaceTableModel    = null;

    // PopupsTab values
    public DefaultTableModel popupsSearchReplaceTableModel = null;
    
    public String            titleFont                     = "Verdana";
    public int               titleSize                     = 12;
    public Color             titleColor                    = Color.WHITE;
    public boolean           titleBold                     = false;
    public boolean           titleItalics                  = false;
    public boolean           titleUnder                    = false;

    public String            bodyFont                      = "Verdana";
    public int               bodySize                      = 12;
    public Color             bodyColor                     = Color.BLACK;
    public boolean           bodyBold                      = false;
    public boolean           bodyItalics                   = false;
    public boolean           bodyUnder                     = false;

    public Color             bodyBackgroundColor           = Color.WHITE;
    public Color             boxColor                      = Color.BLUE;
    public String            popupStyle                    = "default";
    public String            popupWidth                    = "";
    public String            popupHeight                   = "";

    // TextTab values
    public DefaultTableModel textSearchReplaceTableModel   = null;
}
