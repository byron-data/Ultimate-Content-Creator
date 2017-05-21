package cfgPkg;

import java.awt.*;
import java.util.*;

public class Const {
    public final static boolean liteVersion = false;
    
    public final static String  title              = "Ultimate Content Creator Pro";
    public final static String  about              = "<html>Version 2.3 - Copyright 2008, All rights reserved.<br><br>" +
            "http://www.ultimatecontentcreator.com</html>";
    public final static String  suffix             = "ucc";
    
    public final static int     maxOpens           = 12;
    public final static String  newWindowException = "http://www.goarticles.com";

    public final static String programFile         = "UCC.cfg";
    public final static String defaultSettingsFile = "defaults." + suffix;
    public final static String defaultLoc          = "C:\\"; //"C:\\Program Files\\Windows NT\\Accessories\\wordpad.exe"
    public final static String defaultPage         = "page" + filesep() + "AutoSave.htm";
    public final static String defaultUrlPage      = "page" + filesep() + "AutoSaveURL.htm";

    public final static int nicheBrowser           = 1;
    public final static int lsiBrowser             = 2;
    public final static int saturationBrowser      = 3;
    public final static int rssBrowser             = 4;
    public final static int productBrowser         = 5;

    public final static String startPreserveTags   = "%preserve_start%";   // predefined token
    public final static String endPreserveTags     = "%preserve_end%";     // predefined token
    
    public final static String childLinksToken     = "%child_links%";      // predefined token
    public final static String childLinksStart     = "<!-- child_links start -->";
    public final static String childLinksEnd       = "<!-- child_links end -->";
    
    public final static String shareLinksToken     = "%share_links%";      // predefined token
    public final static String shareLinksStart     = "<!-- share_links start -->";
    public final static String shareLinksEnd       = "<!-- share_links end -->";

    public final static String shareFileLinksToken = "%share_file_links%"; // predefined token
    public final static String shareFileLinksStart = "<!-- share_file_links start -->";
    public final static String shareFileLinksEnd   = "<!-- share_file_links end -->";
    public final static String shareFileLinksFile  = "share_links.inc";

    public final static String keyword             = "%keyword%";          // predefined token

    public final static String phpSnippetToken     = "%php_snippet%";      // predefined token
    public final static String phpSnippetFile      = "snippets.txt";
    public final static String phpSnippetCode      = "<?php random_snippet(\"" + phpSnippetFile + "\"); ?>";
    
    public final static String phpRssFeed          = "rss.xml";
    public final static String phpRssFeeds         = "<?php readFeed('" + phpRssFeed + "', 4, 6); ?>";
    public final static String phpAddRssFeeds      = "<?php addFeed('" + phpRssFeed + "'); ?>";
    public final static String phpMergeRssFeeds    = "<?php mergeFeeds(8, 12); ?>";
    
    public final static String dhtmlUrl            = "dhtmlUrl";
    public final static String dhtmlWord           = "dhtmlWord";

    public final static String dhtmlArguments      = "arguments";
    public final static String dhtmlAdvertCode     =
        "<a href='" + dhtmlUrl + "' target =_blank onMouseOver=\"stm(" + dhtmlArguments + ")\" onMouseOut=\"htm()\">" + dhtmlWord + "</a>";

    public final static String dhtmlHeadCode       = // To replace the </HEAD> tag
        "<script language=\"JavaScript1.2\" src=\"includes/main.js\" type=\"text/javascript\"></script>" + linesep() + "</HEAD>";
    
    public final static String dhtmlBodyCode       = // To be added after the <BODY> tag
        "<div id=\"TipLayer\" style=\"visibility:hidden;position:absolute;z-index:1000;top:-100\"></div>" + linesep() +
        "<script language=\"JavaScript1.2\" type=\"text/javascript\">" + linesep() +
        "var FiltersEnabled = 1 // if you're not going to use transitions or filters set this to 0" + linesep() +
        "applyCssFilter()" + linesep() + "</script>";

    public final static String Windows             = "Windows";
    public final static String Mac                 = "Mac";
    public final static String Unix                = "Unix";
    
    public final static String WindowsCharset      = "Unicode";
    public final static String MacCharset          = "MacRoman";
    public final static String UnixCharset         = "UTF-8";
    
    static public String OS() {
        String OS_Id = System.getProperty("os.name");
        if (OS_Id.indexOf(Windows) != -1) {
            return Windows;
	} else if (OS_Id.indexOf(Mac) != -1) {
            return Mac;
	} else {
            return Unix;
	}
    }
    
    static public String filesep() {
        return System.getProperty("file.separator");
    }
    
    static public String linesep() {
        return System.getProperty("line.separator");
    }
    
    static public String pathsep() {
        return System.getProperty("path.separator");
    }
}
