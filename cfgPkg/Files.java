package cfgPkg;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.text.html.parser.*;
import java.util.Vector;

public class Files {
    /**
     * Creates a new instance of Files
     */
    public Files() {}

    static public ProgramConfigurationFile openProgramFile() {
        ProgramConfigurationFile pcf;

        try
        {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(cfgPkg.Const.programFile));
            pcf = (ProgramConfigurationFile) inputStream.readObject();
            inputStream.close();
        } catch(Exception ex) {
            System.err.println(ex.toString());
            pcf = new ProgramConfigurationFile();
        }

        return pcf;
    }

    static public void saveProgramFile(ProgramConfigurationFile pcfIn) {
        try
        {
            File newFile = new File("");
            
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(cfgPkg.Const.programFile));
            ProgramConfigurationFile pcf    = new ProgramConfigurationFile();

            // ProjectSetupTab values
            pcf.expertMode   = pcfIn.expertMode;
            pcf.editor       = pcfIn.editor;
            pcf.nativeEdit   = pcfIn.nativeEdit;
            pcf.nativeBrowse = pcfIn.nativeBrowse;

            outputStream.writeObject(pcf);
            outputStream.close();
        } catch(Exception ex) {
            System.err.println(ex.toString());
        }
    }

    static public String readFile(String fileName, boolean dollarFix) {
        String fileTxt = "";
        try {
            FileInputStream inputStream = new FileInputStream(new File(fileName));
            BufferedReader  reader      = new BufferedReader(new InputStreamReader(inputStream));
            String s;
            while ((s = reader.readLine()) != null) {
                if (dollarFix)
                    s = s.replace("$", "\\$");
                fileTxt = fileTxt + s + cfgPkg.Const.linesep();
            }
            reader.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            System.err.println("Files.readFile: " + e);
        } catch (IOException e) {
            System.err.println("Files.readFile: " + e);
        }
        
        return fileTxt;
    }
    
    static public String readFileForTag(String fileName, String openTag, String closeTag) {
        String tokenTxt = "";

        try {
            FileInputStream inputStream = new FileInputStream(new File(fileName));
            BufferedReader  reader      = new BufferedReader(new InputStreamReader(inputStream));
            String s;
            boolean openFlag            = true;

            // start - get the replacement text for one token
            while ((s = reader.readLine()) != null) {
                if (openFlag) {
                    int pos = s.indexOf(openTag);  // look for openTag
                    if (pos >= 0) {                // found it
                        openFlag = false;
                        pos = pos + openTag.length();
                        if (pos < s.length()) {    // some text after openTag
                            if (closeTag.equals("")) {
                                tokenTxt = tokenTxt + s.substring(pos) + cfgPkg.Const.linesep();
                                break;
                            }
                            int pos2 = s.substring(pos).indexOf(closeTag); // look for closeTag
                            if (pos2 != -1) {
                                tokenTxt = tokenTxt + s.substring(pos, pos+pos2);
                                break;
                            }
                            else
                                tokenTxt = tokenTxt + s.substring(pos) + cfgPkg.Const.linesep();
                        }
                        if (closeTag.equals("")) break; // we have read to the end of the line, no more to do
                    }
                } else {
                    int pos = s.indexOf(closeTag); // look for closeTag
                    if (pos >= 0) {                // found it
                        if (pos > 0)               // some text before closeTag
                            tokenTxt = tokenTxt + s.substring(0, pos) + cfgPkg.Const.linesep();
                        break;                     // we have the endTag, no more to do
                    }
                    else                           // keep building the tokenTxt
                        tokenTxt = tokenTxt + s + cfgPkg.Const.linesep();
                }
            } // end - get the replacement text for one token
            
            reader.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            System.err.println("Files.readFileForTag: " + e);
        } catch (IOException e) {
            System.err.println("Files.readFileForTag: " + e);
        }

        return tokenTxt;
    }
    
    static public Vector readFileSections(String fileName, String openTag, String closeTag) {
        Vector vector = new Vector();

        try {
            FileInputStream inputStream = new FileInputStream(new File(fileName));
            BufferedReader  reader      = new BufferedReader(new InputStreamReader(inputStream));
            String s;
            boolean openFlag = true;
            String  tokenTxt = "";

            // start - get all the text between the tokens
            while ((s = reader.readLine()) != null) {
                if (openFlag) {
                    int pos = s.indexOf(openTag);  // look for openTag
                    if (pos >= 0) {                // found it
                        openFlag = false;
                        pos = pos + openTag.length();
                        if (pos < s.length()) {    // some text after openTag
                            int pos2 = s.substring(pos).indexOf(closeTag); // look for closeTag
                            if (pos2 != -1) {
                                tokenTxt = s.substring(pos, pos+pos2);
                                vector.addElement(tokenTxt);
                                openFlag = true;
                                tokenTxt = "";
                            } else
                                tokenTxt = s.substring(pos) + cfgPkg.Const.linesep();
                        }
                    }
                } else {
                    int pos = s.indexOf(closeTag); // look for closeTag
                    if (pos >= 0) {                // found it
                        if (pos > 0)               // some text before closeTag
                            tokenTxt = tokenTxt + s.substring(0, pos) + cfgPkg.Const.linesep();
                        vector.addElement(tokenTxt);
                        openFlag = true;
                        tokenTxt = "";
                    }
                    else                           // keep building the tokenTxt
                        tokenTxt = tokenTxt + s + cfgPkg.Const.linesep();
                }
            }
            
            reader.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            System.err.println("Files.readFileSections: " + e);
        } catch (IOException e) {
            System.err.println("Files.readFileSections: " + e);
        }

        return vector;
    }
    
    static public String removeTagTextFromFile(String fileName, String openTag, String closeTag, boolean keepTags) {
        String fileTxt = "";

        fileTxt = readFile(fileName, false);
        int openTagStart = fileTxt.indexOf(openTag);
        if (openTagStart != -1) {
            int openTagLen    = openTag.length();
            int closeTagStart = fileTxt.indexOf(closeTag, openTagStart+openTagLen);
            if (closeTagStart != -1) {
                int closeTagLen = closeTag.length();
                if (keepTags) {
                    fileTxt = fileTxt.substring(0, openTagStart) + openTag + cfgPkg.Const.linesep() +
                        closeTag + fileTxt.substring(closeTagStart+closeTag.length());
                } else {
                    String eol   = cfgPkg.Const.linesep();
                    int addOn    = 0;
                    int startPos = closeTagStart+closeTag.length();
                    if ((startPos+eol.length()) <= fileTxt.length()) {
                        if (fileTxt.substring(startPos, startPos+eol.length()).equals(eol))
                            addOn = eol.length();
                    }
                    fileTxt = fileTxt.substring(0, openTagStart) +
                        fileTxt.substring(closeTagStart+closeTag.length()+addOn);
                }
            }
        }

        return fileTxt;
    }
    
    static public boolean writeFile(String fileName, String fileTxt) {
        try {
            FileOutputStream outputStream = new FileOutputStream(new File(fileName));
            outputStream.write(fileTxt.getBytes());
            outputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            System.err.println("Files.writeFile: " + e);
        } catch (IOException e) {
            System.err.println("Files.writeFile: " + e);
        }
        
        return false;
    }
    
    static public boolean renameFile(File oldFile, String newFileName) {
        return oldFile.renameTo(new File(newFileName));
    }
    
    static public boolean renameFile(String oldFileName, String newFileName) {
        File oldFile = new File(oldFileName);
        return oldFile.renameTo(new File(newFileName));
    }
    
    static public String nameFile(String oldFileName) {
        String pattern = "[?%\"'’`“”<>\\[\\]{}/\\\\,:;*|]"; // Do not allow ?%\"'’`“”<>[]{}/\,:;*| characters in file names
        oldFileName = oldFileName.replaceAll(pattern, "");
        return oldFileName;
    }
    
    static public void editFile(String editor, String fileName) {
        if (!editor.equals("")) {
            String cmd = "\"" + editor + "\" \"" + fileName + "\"";
            if (editor.toString().indexOf(".jar") != -1) {
                String OS_Id = cfgPkg.Const.OS();
                if (OS_Id.equals(cfgPkg.Const.Windows)) {
                    cmd = "javaw.exe -jar " + cmd;
                } else {
                    cmd = "java -jar " + cmd;
                }
            }
            try {
                Process proc = Runtime.getRuntime().exec(cmd);
            } catch (IOException e) {
                System.err.println("Attempting to edit files: " + e);
            }
        } else {
            File file = new File(fileName);
            try {
                java.awt.Desktop.getDesktop().edit(file);
            } catch (IOException e) {
                System.err.println("Attempting to edit files: " + e);
            }
        }
    }
    
    static public void browseFile(String browser, String fileName) {
        if (!browser.equals("")) {
            String cmd = browserCmdString() + "MultiTab " + fileName;
            
            try {
                Process proc = Runtime.getRuntime().exec(cmd);
            } catch (IOException e) {
                System.err.println("Attempting to browse files: " + e);
            }
        } else {
            File file = new File(fileName);
            try {
                java.awt.Desktop.getDesktop().browse(file.toURI());
            } catch (IOException e) {
                System.err.println("Attempting to browse files: " + e);
            }
        }
    }
    
    static private String browserCmdString() {
        boolean testing = false;

        String cmd      = "";
        String OS_Id = cfgPkg.Const.OS();
        if (OS_Id.equals(cfgPkg.Const.Windows)) {
            cmd = "javaw.exe -jar ";
        } else {
            cmd = "java -jar ";
        }

        if (testing) {
            String testPath = "C:\\Documents and Settings\\Byron\\My Documents\\Finance\\Unleash\\UCC work\\product\\UCC_Pro\\UCCBrowser.jar";
            cmd = cmd + "\"" + testPath + "\" ";
        } else
            cmd = cmd + "UCCBrowser.jar ";

        return cmd;
    }
    
    static public void UCCResearchBrowsers(int browserType, String arg1, String arg2) {
        String browser = "";

        if (browserType == cfgPkg.Const.nicheBrowser) {
            browser = "Niche ";
        } else if (browserType == cfgPkg.Const.lsiBrowser) {
            browser = "LSI ";
        } else if (browserType == cfgPkg.Const.saturationBrowser) {
            browser = "Saturation ";
            if (arg1.equals("")) arg1 = "title ";
            if (arg2.equals("")) arg2 = "author ";
        } else if (browserType == cfgPkg.Const.rssBrowser) {
            browser = "RSS ";
            if (arg1.equals("")) arg1 = "keyword ";
        } else if (browserType == cfgPkg.Const.productBrowser) {
            browser = "Product ";
        }
        
        if (!arg1.equals("")) arg1 = "\"" + arg1 + "\" ";
        if (!arg2.equals("")) arg2 = "\"" + arg2 + "\"";
        String cmd = browserCmdString() + "MultiWin " + browser + arg1 + arg2;

        try {
            Process proc = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            System.err.println("Attempting to open UCC Browser: " + e);
        }
    }
    
    static public void copyDirectory(File sourceLocation, File targetLocation, String suffix) {
        try {
            if (sourceLocation.isDirectory()) {
                if (!targetLocation.exists() && suffix.equals("")) {
                    targetLocation.mkdir();
                }

                String[] children = sourceLocation.list();
                for (int i=0; i<children.length; i++) {
                    File newSourceLocation = new File(sourceLocation, children[i]);
                    if (!suffix.equals("") && newSourceLocation.isDirectory()) continue;
                    copyDirectory(newSourceLocation, new File(targetLocation, children[i]), suffix);
                }
            } else {
                if (!suffix.equals("") && !sourceLocation.getPath().endsWith(suffix)) return;
                
                InputStream in   = new FileInputStream(sourceLocation);
                OutputStream out = new FileOutputStream(targetLocation);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
        } catch (FileNotFoundException e) {
            System.err.println("Files.copyDirectory: " + e);
        } catch (IOException e) {
            System.err.println("Files.copyDirectory: " + e);
        }
    }
    
    static public String parseHTML(String srcFileName, String openTag, boolean tags) {
        try {
            Reader r = new FileReader(srcFileName);
            ParserDelegator parser = new ParserDelegator();
            ParseHTML callback = new ParseHTML();
            callback.openTag   = openTag;
            callback.textSeek  = openTag.charAt(0) != '<' && openTag.charAt(openTag.length()-1) != '>';
            //parser.parse(r, callback, false);
            parser.parse(r, callback, true); // ignoreCharset == true
            if (tags)
                return callback.getTxt;
            else
                return callback.keyword;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    
    static public String stripHTML(String srcFileName) {
        try {
            Reader r = new FileReader(srcFileName);
            ParserDelegator parser = new ParserDelegator();
            Vector vector = cfgPkg.Files.readFileSections(srcFileName, cfgPkg.Const.startPreserveTags, cfgPkg.Const.endPreserveTags);
            StripHTML callback = new StripHTML();
            callback.vector = vector;
            //parser.parse(r, callback, false);
            parser.parse(r, callback, true); // ignoreCharset == true
            return callback.getTxt;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
