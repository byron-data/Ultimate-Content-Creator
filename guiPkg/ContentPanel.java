package guiPkg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.Document;
import javax.swing.text.html.parser.*;
import javax.swing.text.html.*;

import cfgPkg.*;

public class ContentPanel extends JPanel implements ActionListener {
    // Presentation
    protected JPanel pastePanel         = new JPanel();
    
    protected JTextField  keywordTxt    = new JTextField();
    private   JButton     densityBtn    = new JButton(cfgPkg.GUI.density);
    protected JTextField  densityTxt    = new JTextField();
    protected JTextField  countTxt      = new JTextField();
    private   JComboBox   headlineCombo;
    protected JTextPane   titleTxt      = new JTextPane();
    private   JLabel      titleLbl      = new JLabel(cfgPkg.GUI.title + " (%title%)");
    private   JButton     titleBtn      = new JButton(cfgPkg.GUI.title);
    private   JButton     saturationBtn = new JButton(cfgPkg.GUI.saturation);
    
    private   JLabel      authorLbl     = new JLabel(cfgPkg.GUI.author + " (%author%)");
    protected JEditorPane authorTxt     = new JEditorPane();
    private   JScrollPane authorSP      = new JScrollPane(authorTxt);
    private   JButton     authorBtn     = new JButton(cfgPkg.GUI.author);
    private   JButton     authorVBtn    = new JButton(cfgPkg.GUI.view);
    
    private   JLabel      articleLbl    = new JLabel(cfgPkg.GUI.article + " (%article%)");
    protected JEditorPane articleTxt    = new JEditorPane();
    private   JScrollPane articleSP     = new JScrollPane(articleTxt);
    private   JButton     articleBtn    = new JButton(cfgPkg.GUI.article);
    private   JButton     articleVBtn   = new JButton(cfgPkg.GUI.view);
    
    private   JLabel      aboutLbl      = new JLabel(cfgPkg.GUI.about + " " + cfgPkg.GUI.the + " " + cfgPkg.GUI.author + " (%about%)");
    protected JEditorPane aboutTxt      = new JEditorPane();
    private   JScrollPane aboutSP       = new JScrollPane(aboutTxt);
    private   JButton     aboutBtn      = new JButton(cfgPkg.GUI.about);
    private   JButton     aboutVBtn     = new JButton(cfgPkg.GUI.view);

    private   JButton     addButton     = new JButton(cfgPkg.GUI.create);
    private   JButton     resButton     = new JButton(cfgPkg.GUI.research);
    private   JButton     clearButton   = new JButton(cfgPkg.GUI.clear);
    
    protected ProjectSetupTab projectSetupTab;
    private BatchProcessTab   getInputsTab;

    public ContentPanel(int tabWidth, int tabHeight, ProjectSetupTab projectSetupTab, BatchProcessTab getInputsTab) {
        this.projectSetupTab = projectSetupTab;
        this.getInputsTab    = getInputsTab;
        this.setLayout(null);
        this.setPreferredSize(new Dimension(tabWidth, tabHeight));
        this.addTabComponents();
    }

    private void addTabComponents() {
        pastePanel.setLayout(null);
        pastePanel.setLocation(5, 5);
        pastePanel.setSize(510, 840);
        this.add(pastePanel);
        
        keywordTxt.setLocation(0, 0);
        keywordTxt.setSize(100, 20);
        keywordTxt.setText(cfgPkg.GUI.keyword);
        keywordTxt.setToolTipText(cfgPkg.Text.CPkeywordTxt);
        pastePanel.add(keywordTxt);

        Insets insets = new Insets(0,0,0,0);

        densityBtn.setLocation(105, 2);
        densityBtn.setSize(60, 15);
        densityBtn.addActionListener(this);
        densityBtn.setToolTipText(cfgPkg.Text.CPdensityBtn);
        densityBtn.setMargin(insets);
        pastePanel.add(densityBtn);

        densityTxt.setLocation(170, 0);
        densityTxt.setSize(40, 20);
        densityTxt.setText("");
        densityTxt.setEditable(false);
        densityTxt.setToolTipText(cfgPkg.Text.CPdensityTxt);
        pastePanel.add(densityTxt);

        countTxt.setLocation(215, 0);
        countTxt.setSize(40, 20);
        countTxt.setText("");
        countTxt.setEditable(false);
        countTxt.setToolTipText(cfgPkg.Text.CPcountTxt);
        countTxt.setHorizontalAlignment(JTextField.RIGHT);
        pastePanel.add(countTxt);

        if (!cfgPkg.Const.liteVersion) {
            headlineCombo = new JComboBox(cfgPkg.CopyWriting.proven);
            headlineCombo.setEditable(true);
        } else
            headlineCombo = new JComboBox();
        headlineCombo.setLocation(260, 0);
        headlineCombo.setSize(240, 20);
        headlineCombo.setToolTipText(cfgPkg.Text.CPheadlineCombo);
        pastePanel.add(headlineCombo);

        titleLbl.setLocation(0, 20);
        titleLbl.setSize(200, 20);
        pastePanel.add(titleLbl);

        titleBtn.setLocation(190, 25);
        titleBtn.setSize(120, 15);
        titleBtn.addActionListener(this);
        titleBtn.setToolTipText(cfgPkg.Text.CPtitleBtn);
        pastePanel.add(titleBtn);

        saturationBtn.setLocation(380, 25);
        saturationBtn.setSize(120, 15);
        saturationBtn.addActionListener(this);
        saturationBtn.setToolTipText(cfgPkg.Text.CPsaturationBtn);
        pastePanel.add(saturationBtn);

        JScrollPane titleSP = new JScrollPane(titleTxt);
        titleSP.setSize(505, 25);
        titleSP.setLocation(0, 45);
        pastePanel.add(titleSP);

        authorLbl.setLocation(0, 72);
        authorLbl.setSize(200, 20);
        pastePanel.add(authorLbl);

        authorBtn.setLocation(190, 75);
        authorBtn.setSize(120, 15);
        authorBtn.addActionListener(this);
        authorBtn.setToolTipText(cfgPkg.Text.CPauthorBtn);
        pastePanel.add(authorBtn);

        authorVBtn.setLocation(380, 75);
        authorVBtn.setSize(120, 15);
        authorVBtn.addActionListener(this);
        authorVBtn.setToolTipText(cfgPkg.Text.MSGviewEdit);
        pastePanel.add(authorVBtn);

        authorSP.setSize(505, 50);
        authorSP.setLocation(0, 95);
        pastePanel.add(authorSP);

        articleLbl.setLocation(0, 147);
        articleLbl.setSize(200, 20);
        pastePanel.add(articleLbl);

        articleBtn.setLocation(190, 150);
        articleBtn.setSize(120, 15);
        articleBtn.addActionListener(this);
        articleBtn.setToolTipText(cfgPkg.Text.CParticleBtn);
        pastePanel.add(articleBtn);

        articleVBtn.setLocation(380, 150);
        articleVBtn.setSize(120, 15);
        articleVBtn.addActionListener(this);
        articleVBtn.setToolTipText(cfgPkg.Text.MSGviewEdit);
        pastePanel.add(articleVBtn);

        articleSP.setSize(505, 530);
        articleSP.setLocation(0, 170);
        pastePanel.add(articleSP);

        aboutLbl.setLocation(0, 702);
        aboutLbl.setSize(200, 20);
        pastePanel.add(aboutLbl);

        aboutBtn.setLocation(190, 705);
        aboutBtn.setSize(120, 15);
        aboutBtn.addActionListener(this);
        aboutBtn.setToolTipText(cfgPkg.Text.CPaboutBtn);
        pastePanel.add(aboutBtn);

        aboutVBtn.setLocation(380, 705);
        aboutVBtn.setSize(120, 15);
        aboutVBtn.addActionListener(this);
        aboutVBtn.setToolTipText(cfgPkg.Text.MSGviewEdit);
        pastePanel.add(aboutVBtn);

        aboutSP.setSize(505, 85);
        aboutSP.setLocation(0, 725);
        pastePanel.add(aboutSP);

        addButton.setLocation(0, 820);
        addButton.setSize(120, 20);
        addButton.addActionListener(this);
        addButton.setToolTipText(cfgPkg.Text.CPaddButton);
        pastePanel.add(addButton);

        resButton.setLocation(190, 820);
        resButton.setSize(120, 20);
        resButton.addActionListener(this);
        resButton.setToolTipText(cfgPkg.Text.CPresButton);
        pastePanel.add(resButton);

        clearButton.setLocation(380, 820);
        clearButton.setSize(120, 20);
        clearButton.addActionListener(this);
        clearButton.setToolTipText(cfgPkg.Text.MSGclearText);
        pastePanel.add(clearButton);
    }
    
    protected void resetArticleTxt() {
        saturationBtn.setVisible(false);

        authorLbl.setVisible(false);
        authorSP.setVisible(false);
        authorBtn.setVisible(false);
        authorVBtn.setVisible(false);

        int increase = 75;
        articleLbl.setLocation(articleLbl.getX(), articleLbl.getY()-increase);
        articleSP.setSize(articleSP.getWidth(), articleSP.getHeight()+increase);
        articleSP.setLocation(0, articleSP.getY()-increase);
        articleBtn.setLocation(articleBtn.getX(), articleBtn.getY()-increase);
        articleVBtn.setLocation(articleVBtn.getX(), articleVBtn.getY()-increase);

        aboutLbl.setVisible(false);
        aboutSP.setVisible(false);
        aboutBtn.setVisible(false);
        aboutVBtn.setVisible(false);

        JLabel contentLbl = new JLabel("<html><p align=center>" + "" +
                                       cfgPkg.GUI.contentAssistantLabel + "</p></html>");
        contentLbl.setLocation(5, 715);
        contentLbl.setSize(500, 80);
        pastePanel.add(contentLbl);
    }
    
    private void buildDensity() {
        Vector countVector = new Vector();
        if (!cfgPkg.Const.liteVersion) {
            String display = Double.toString(cfgPkg.Util.buildDensity(titleTxt.getText()+" "+articleTxt.getText()+" "+aboutTxt.getText(), keywordTxt.getText(), countVector));
            if (display.indexOf(".") != -1)
                display = display.substring(0, display.indexOf(".")+2) + "%";
            else
                display = display + "%";
            densityTxt.setText(display);
            countTxt.setText(countVector.get(0).toString());
        }
    }
    
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == titleBtn) {
            titleTxt.setText(getFragment());
            buildDensity();
        }
        
        else if (evt.getSource() == saturationBtn) {
            cfgPkg.Files.UCCResearchBrowsers(cfgPkg.Const.saturationBrowser, titleTxt.getText(), getTokenTxt(authorVBtn, authorTxt.getText()));
        }

        else if (evt.getSource() == authorBtn)
            authorTxt.setText(getFragment());
        else if (evt.getSource() == authorVBtn)
            toggleView(authorVBtn, authorTxt);
        
        else if (evt.getSource() == articleBtn) {
            articleTxt.setText(getFragment());
            buildDensity();
        } else if (evt.getSource() == articleVBtn) {
            toggleView(articleVBtn, articleTxt);
            buildDensity();
        }
        
        else if (evt.getSource() == aboutBtn) {
            aboutTxt.setText(getFragment());
            buildDensity();
        }
        else if (evt.getSource() == aboutVBtn) {
            toggleView(aboutVBtn, aboutTxt);
            buildDensity();
        }

        else if (evt.getSource() == addButton) {
            if (!projectSetupTab.outputDirCheck())       return;
            if (!projectSetupTab.contentTemplateCheck()) return;
            
            String dstFileName = titleTxt.getText().trim().replace(" ", "-") + "." + projectSetupTab.extensionTxt.getText();
            dstFileName = cfgPkg.Files.nameFile(dstFileName);

            dstFileName = JOptionPane.showInputDialog(null, cfgPkg.Text.MSGcontentName, dstFileName);
            if (dstFileName == null)
                return;
            else if (dstFileName.equals("")) {
                JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGfilenameBlank, cfgPkg.Text.MSGinputError, JOptionPane.ERROR_MESSAGE);
                return;
            }
            int dotPos = dstFileName.lastIndexOf('.');
            if (dotPos == -1) {
                 dstFileName = dstFileName + "." + projectSetupTab.extensionTxt.getText();
            }
            dstFileName = cfgPkg.Files.nameFile(dstFileName);
            dstFileName = projectSetupTab.outputDir.getAbsolutePath() + cfgPkg.Const.filesep() + dstFileName;
            for (int i=0; i<getInputsTab.gfp.getTable().getRowCount(); i++)
                if (getInputsTab.gfp.getTable().getValueAt(i, 1).equals(dstFileName)) {
                    JOptionPane.showMessageDialog(null, dstFileName + cfgPkg.Text.MSGalreadyEntered,
                                                  cfgPkg.Text.MSGduplicateEntry, JOptionPane.ERROR_MESSAGE);
                    return;
                }

            // Read in the template
            String cpyFileName = projectSetupTab.contentTemplate.getAbsoluteFile().toString();
            String fileTxt     = cfgPkg.Files.readFile(cpyFileName, false);

            fileTxt = fileTxt.replace("%title%",   titleTxt.getText());
            String author = getTokenTxt(authorVBtn,  authorTxt.getText());
            if (author.equals("")) {
                fileTxt = fileTxt.replaceAll("(<p.*?class=\"authortext\".*?>.*?%author%.*?</p>)", "");
                fileTxt = fileTxt.replaceAll("(<div.*?class=\"authortext\".*?>.*?%author%.*?</div>)", "");
                fileTxt = fileTxt.replace("%author%", "");
            } else
                fileTxt = fileTxt.replace("%author%",  author);
            fileTxt = fileTxt.replace("%article%", getTokenTxt(articleVBtn, articleTxt.getText()));
            String about = getTokenTxt(aboutVBtn,   aboutTxt.getText());
            if (about.equals("")) {
                fileTxt = fileTxt.replaceAll("(<p.*?class=\"abouttext\".*?>.*?%about%.*?</p>)", "");
                fileTxt = fileTxt.replaceAll("(<div.*?class=\"abouttext\".*?>.*?%about%.*?</div>)", "");
                fileTxt = fileTxt.replace("%about%", "");
            } else
                fileTxt = fileTxt.replace("%about%",   about);

            cfgPkg.Files.writeFile(dstFileName, fileTxt);
            getInputsTab.gfp.addFile(dstFileName); // add generated files to the generated files list
        }

        else if (evt.getSource() == resButton) {
            if (cfgPkg.Const.liteVersion) {
                JOptionPane.showMessageDialog(null, cfgPkg.Text.liteMessage, cfgPkg.Text.liteMessageTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            cfgPkg.Files.UCCResearchBrowsers(cfgPkg.Const.nicheBrowser, "", "");
        }

        else if (evt.getSource() == clearButton) {
            int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.MSGclearText, cfgPkg.Text.MSGconfirmClear, JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.NO_OPTION) return;
            clearText();
            buildDensity();
        }
        
        else if (evt.getSource() == densityBtn) {
            if (cfgPkg.Const.liteVersion) {
                JOptionPane.showMessageDialog(null, cfgPkg.Text.liteMessage, cfgPkg.Text.liteMessageTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            buildDensity();
        }
        
        else
            actionExtra(evt);
    }
    
    public void actionExtra(ActionEvent evt) {}
    
    private String getFragment() {
        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable content = cb.getContents(this);

        try {
            DataFlavor df = new DataFlavor("text/html; class=java.lang.String; charset=Unicode");

            String OS_Id = cfgPkg.Const.OS();
            if (OS_Id.equals(cfgPkg.Const.Mac)) {
                System.err.println("DataFlavors.selectBestTextFlavor");
                System.err.println(DataFlavor.selectBestTextFlavor(cb.getAvailableDataFlavors()).getMimeType());
                System.err.println("Available DataFlavors");
                DataFlavor[] dfs = cb.getAvailableDataFlavors();
                for (int i=0; i<dfs.length; i++) {
                    System.err.println(dfs[i].getMimeType());
                }
            }

            df = DataFlavor.selectBestTextFlavor(cb.getAvailableDataFlavors());
            Reader reader = df.getReaderForText(content);
            String text   = "";
            int charValue = 0;
            while ((charValue = reader.read()) != -1) {
                text = text + (char)charValue;
            }
            if (OS_Id.equals(cfgPkg.Const.Mac)) System.err.println("Before cleaning" + cfgPkg.Const.linesep() + text);
            
            //if (cb.isDataFlavorAvailable(df)) {
                //text = (String)content.getTransferData(df);
                text = cfgPkg.Util.getFragmentString(text);
                if (OS_Id.equals(cfgPkg.Const.Mac)) System.err.println("After cleaning" + cfgPkg.Const.linesep() + text.trim());
                return text.trim();
            /*} else {
                text = (String)content.getTransferData(DataFlavor.stringFlavor);
                return text.trim();
            }*/
        }
        catch (Throwable e) {
            System.err.println(e);
        }
        return "";
    }
    
    private void toggleView(JButton button, JEditorPane editorPane) {
        String text = editorPane.getText();
        if (button.getText().equals(cfgPkg.GUI.view)) {
            editorPane.setContentType("text/html");
            editorPane.setEditable(false);
            button.setText(cfgPkg.GUI.edit);
        } else {
            editorPane.setContentType("text/plain");
            editorPane.setEditable(true);
            button.setText(cfgPkg.GUI.view);
            int pos = text.indexOf("<body>") + "<body>".length();
            text = text.substring(pos, text.indexOf("</body>")).trim();
            text = text.replace("    ", "");
        }
        editorPane.setText(text);
    }
    
    private String getTokenTxt(JButton button, String text) {
        if (button.getText().equals(cfgPkg.GUI.edit)) {
            int pos = text.indexOf("<body>") + "<body>".length();
            text = text.substring(pos, text.indexOf("</body>")).trim();
        }
        return text;
    }
    
    public void clearText() {
        titleTxt.setText("");
        authorTxt.setText("");
        articleTxt.setText("");
        aboutTxt.setText("");
    }
}
