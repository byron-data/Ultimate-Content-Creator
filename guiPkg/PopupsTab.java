package guiPkg;

import cfgPkg.ProgramConfigurationFile;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.*;

public class PopupsTab extends JPanel implements ActionListener {
    private Table      table;
    private JTextField searchTxt      = new JTextField();
    private JTextField categoryTxt    = new JTextField();
    private JTextField descriptionTxt = new JTextField();
    private JTextField urlTxt         = new JTextField();
    
    private JCheckBox  checkBox       = new JCheckBox(cfgPkg.GUI.selectAll);
    private JButton    replaceButton  = new JButton("<html><h3>" + cfgPkg.GUI.replaceTokens + "</h3></html>");
    private JButton    copyAdvertBtn  = new JButton("<html><h3>" + cfgPkg.GUI.copyAdvert + "</h3></html>");
    private JButton    addButton      = new JButton(cfgPkg.GUI.add);
    private JButton    modifyButton   = new JButton(cfgPkg.GUI.modify);
    private JButton    deleteButton   = new JButton(cfgPkg.GUI.delete);

    private JComboBox  titleFontCombo;
    private JComboBox  titleSizeCombo;
    private JButton    titleColorButton               = new JButton(cfgPkg.GUI.titleColor);
    private JCheckBox  titleBoldCB                    = new JCheckBox(cfgPkg.GUI.bold);
    private JCheckBox  titleItalicsCB                 = new JCheckBox(cfgPkg.GUI.italics);
    private JCheckBox  titleUnderCB                   = new JCheckBox(cfgPkg.GUI.underline);

    private JComboBox  bodyFontCombo;
    private JComboBox  bodySizeCombo;
    private JButton    bodyColorButton                = new JButton(cfgPkg.GUI.bodyColor);
    private JCheckBox  bodyBoldCB                     = new JCheckBox(cfgPkg.GUI.bold);
    private JCheckBox  bodyItalicsCB                  = new JCheckBox(cfgPkg.GUI.italics);
    private JCheckBox  bodyUnderCB                    = new JCheckBox(cfgPkg.GUI.underline);
    
    private JButton    boxColorButton                 = new JButton(cfgPkg.GUI.advertBoxColor);
    private JButton    bodyBackgroundColorButton      = new JButton(cfgPkg.GUI.advertBackgroundColor);
    private String     advertStyles[]                 = {"default", "no title", "sticky", "follow"};
    private JComboBox  advertStyleCombo;
        
    private JPanel     samplePanel                    = new JPanel();
    private JPanel     sampleTitlePanel               = new JPanel();
    private JLabel     sampleTitleLbl                 = new JLabel(cfgPkg.GUI.demoTitle);
    private JLabel     sampleBodyLbl                  = new JLabel(cfgPkg.GUI.demoBody);
    
    private JTextField advertWidthTxt                 = new JTextField();
    private JTextField advertHeightTxt                = new JTextField();
    private JButton    demoHtmlButton                 = new JButton("<html><p align=center>" + cfgPkg.GUI.advertDemoInBrowser + "</p></html>");

    private Color      titleColor                     = Color.WHITE;
    private Font       titleFont                      = new Font("Verdana", Font.PLAIN, 12);
    private Color      bodyColor                      = Color.BLACK;
    private Font       bodyFont                       = new Font("Verdana", Font.PLAIN, 12);
    private Color      bodyBackgroundColor            = Color.WHITE;
    private Color      boxColor                       = Color.BLUE;

    private JColorChooser colorChooser                = new JColorChooser();

    private GeneratedFilesPanel gfp;
    private ProjectSetupTab     projectSetupTab;

    private JButton    productAssistantBtn = new JButton(cfgPkg.GUI.productAssistant);

    public PopupsTab(int tabWidth, int tabHeight, ProjectSetupTab projectSetupTab) {
        this.projectSetupTab = projectSetupTab;
        table                = new Table(projectSetupTab);
	this.setLayout(null);
        this.setPreferredSize(new Dimension(tabWidth, tabHeight));
        gfp = new GeneratedFilesPanel(345, projectSetupTab);
        this.addTabComponents();
    }

    private void addTabComponents() {
        JLabel advertsLbl = new JLabel(cfgPkg.GUI.advertsTokens);
        advertsLbl.setLocation(5, 0);
        advertsLbl.setSize(240, 20);
        this.add(advertsLbl);

        int[] blankChecks = {1, 3};

        table.initialise(cfgPkg.GUI.headersRSS, -1, blankChecks);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 1 || evt.getClickCount() == 2) {
                    if (table.getSelectedRows().length == 1) {
                        int selected = table.getSelectedRow();
                        searchTxt.setText(table.getValueAt(selected,0).toString());
                        categoryTxt.setText(table.getValueAt(selected,1).toString());
                        descriptionTxt.setText(table.getValueAt(selected,2).toString());
                        urlTxt.setText(table.getValueAt(selected,3).toString());
                    } else {
                        searchTxt.setText("");
                        categoryTxt.setText("");
                        descriptionTxt.setText("");
                        urlTxt.setText("");
                    }
                    table.repaint(); // fix for selection not working properly
                }
            }
        });
        JScrollPane tableSP = new JScrollPane(table);
        tableSP.setSize(1165, 345);
        tableSP.setLocation(5, 20);
        table.getColumnModel().getColumn(0).setPreferredWidth(170);
        table.getColumnModel().getColumn(1).setPreferredWidth(170);
        table.getColumnModel().getColumn(2).setPreferredWidth(350);
        table.getColumnModel().getColumn(3).setPreferredWidth(475);
        this.add(tableSP);

        JLabel searchLbl =  new JLabel(cfgPkg.GUI.search);
        searchLbl.setLocation(5, 370);
        searchLbl.setSize(80, 20);
        this.add(searchLbl);

        JLabel categoryLbl = new JLabel(cfgPkg.GUI.category);
        categoryLbl.setLocation(175, 370);
        categoryLbl.setSize(60, 20);
        this.add(categoryLbl);

        JLabel descriptionLbl = new JLabel(cfgPkg.GUI.description);
        descriptionLbl.setLocation(345, 370);
        descriptionLbl.setSize(80, 20);
        this.add(descriptionLbl);

        JLabel URLLbl = new JLabel("URL");
        URLLbl.setLocation(695, 370);
        URLLbl.setSize(60, 20);
        this.add(URLLbl);

        Insets insets = new Insets(0,0,0,0);

        productAssistantBtn.setLocation(755, 370);
        productAssistantBtn.setSize(120, 15);
        productAssistantBtn.setMargin(insets);
        productAssistantBtn.addActionListener(this);
        productAssistantBtn.setToolTipText(cfgPkg.Text.POPproductAssistantBtn);
        this.add(productAssistantBtn);

        searchTxt.setSize(165, 20);
        searchTxt.setLocation(5, 390);
        searchTxt.setToolTipText(cfgPkg.Text.RSSsearchTxt);
        this.add(searchTxt);

        categoryTxt.setLocation(175, 390);
        categoryTxt.setSize(165, 20);
        categoryTxt.setToolTipText(cfgPkg.Text.RSScategoryTxt);
        this.add(categoryTxt);

        descriptionTxt.setLocation(345, 390);
        descriptionTxt.setSize(345, 20);
        descriptionTxt.setToolTipText(cfgPkg.Text.RSSdescriptionTxt);
        this.add(descriptionTxt);

        urlTxt.setLocation(695, 390);
        urlTxt.setSize(470, 20);
        urlTxt.setToolTipText(cfgPkg.Text.RSSurlTxt);
        this.add(urlTxt);

        checkBox.setLocation(5, 420);
        checkBox.setSize(80, 20);
        checkBox.addActionListener(this);
        checkBox.setToolTipText(cfgPkg.Text.MSGselect);
        this.add(checkBox);

        replaceButton.setForeground(Color.RED);
        replaceButton.setLocation(175, 420);
        replaceButton.setSize(120, 20);
        replaceButton.setMargin(insets);
        replaceButton.addActionListener(this);
        replaceButton.setToolTipText(cfgPkg.Text.POPreplaceButton);
        this.add(replaceButton);

        copyAdvertBtn.setForeground(Color.RED);
        copyAdvertBtn.setLocation(345, 420);
        copyAdvertBtn.setSize(120, 20);
        copyAdvertBtn.setMargin(insets);
        copyAdvertBtn.addActionListener(this);
        copyAdvertBtn.setToolTipText(cfgPkg.Text.POPcopyAdvertBtn);
        this.add(copyAdvertBtn);

        addButton.setLocation(575, 420);
        addButton.setSize(120, 20);
        addButton.addActionListener(this);
        addButton.setToolTipText(cfgPkg.Text.RSSaddButton);
        this.add(addButton);

        modifyButton.setLocation(810, 420);
        modifyButton.setSize(120, 20);
        modifyButton.addActionListener(this);
        modifyButton.setToolTipText(cfgPkg.Text.RSSmodifyButton);
        this.add(modifyButton);

        deleteButton.setLocation(1045, 420);
        deleteButton.setSize(120, 20);
        deleteButton.addActionListener(this);
        deleteButton.setToolTipText(cfgPkg.Text.MSGdeleteListItems);
        this.add(deleteButton);

        gfp.setLocation(0, 450);
        gfp.setSize(755, 395);
        this.add(gfp);
        
        JLabel titleFontLbl = new JLabel(cfgPkg.GUI.titleFont);
        titleFontLbl.setLocation(765, 470);
        titleFontLbl.setSize(200, 20);
        this.add(titleFontLbl);

        GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String envfonts[] = gEnv.getAvailableFontFamilyNames();
        String sizes[] = {"1 (10 pt)","2 (12 pt)","3 (14 pt)","4 (16 pt)","5 (18 pt)","6 (20 pt)"};
        
        titleFontCombo = new JComboBox(envfonts);
        titleFontCombo.setLocation(765, 490);
        titleFontCombo.setSize(140, 20);
        titleFontCombo.addActionListener(this);
        titleFontCombo.setSelectedItem("Verdana");
        titleFontCombo.setToolTipText(cfgPkg.Text.POPtitleFontCombo);
        this.add(titleFontCombo);

        titleSizeCombo = new JComboBox(sizes);
        titleSizeCombo.setLocation(915, 490);
        titleSizeCombo.setSize(140, 20);
        titleSizeCombo.addActionListener(this);
        titleSizeCombo.setSelectedItem(sizes[1]);
        titleSizeCombo.setToolTipText(cfgPkg.Text.POPtitleSizeCombo);
        this.add(titleSizeCombo);

        titleColorButton.setLocation(1065, 490);
        titleColorButton.setSize(100, 20);
        titleColorButton.addActionListener(this);
        titleColorButton.setToolTipText(cfgPkg.Text.POPtitleColorButton);
        this.add(titleColorButton);

        titleBoldCB.setLocation(765, 515);
        titleBoldCB.setSize(80, 20);
        titleBoldCB.addActionListener(this);
        titleBoldCB.setToolTipText(cfgPkg.Text.POPtitleBoldCB);
        this.add(titleBoldCB);

        titleItalicsCB.setLocation(855, 515);
        titleItalicsCB.setSize(80, 20);
        titleItalicsCB.addActionListener(this);
        titleItalicsCB.setToolTipText(cfgPkg.Text.POPtitleItalicsCB);
        this.add(titleItalicsCB);

        titleUnderCB.setLocation(945, 515);
        titleUnderCB.setSize(80, 20);
        titleUnderCB.addActionListener(this);
        titleUnderCB.setToolTipText(cfgPkg.Text.POPtitleUnderCB);
        this.add(titleUnderCB);

        JLabel bodyFontLbl = new JLabel(cfgPkg.GUI.bodyFont);
        bodyFontLbl.setLocation(765, 550);
        bodyFontLbl.setSize(200, 20);
        this.add(bodyFontLbl);

        bodyFontCombo = new JComboBox(envfonts);
        bodyFontCombo.setLocation(765, 570);
        bodyFontCombo.setSize(140, 20);
        bodyFontCombo.addActionListener(this);
        bodyFontCombo.setSelectedItem("Verdana");
        bodyFontCombo.setToolTipText(cfgPkg.Text.POPbodyFontCombo);
        this.add(bodyFontCombo);

        bodySizeCombo = new JComboBox(sizes);
        bodySizeCombo.setLocation(915, 570);
        bodySizeCombo.setSize(140, 20);
        bodySizeCombo.addActionListener(this);
        bodySizeCombo.setSelectedItem(sizes[1]);
        bodySizeCombo.setToolTipText(cfgPkg.Text.POPbodySizeCombo);
        this.add(bodySizeCombo);

        bodyColorButton.setLocation(1065, 570);
        bodyColorButton.setSize(100, 20);
        bodyColorButton.addActionListener(this);
        bodyColorButton.setToolTipText(cfgPkg.Text.POPbodyColorButton);
        this.add(bodyColorButton);

        bodyBoldCB.setLocation(765, 595);
        bodyBoldCB.setSize(80, 20);
        bodyBoldCB.addActionListener(this);
        bodyBoldCB.setToolTipText(cfgPkg.Text.POPbodyBoldCB);
        this.add(bodyBoldCB);

        bodyItalicsCB.setLocation(855, 595);
        bodyItalicsCB.setSize(80, 20);
        bodyItalicsCB.addActionListener(this);
        bodyItalicsCB.setToolTipText(cfgPkg.Text.POPbodyItalicsCB);
        this.add(bodyItalicsCB);

        bodyUnderCB.setLocation(945, 595);
        bodyUnderCB.setSize(80, 20);
        bodyUnderCB.addActionListener(this);
        bodyUnderCB.setToolTipText(cfgPkg.Text.POPbodyUnderCB);
        this.add(bodyUnderCB);

        boxColorButton.setLocation(765, 640);
        boxColorButton.setSize(180, 20);
        boxColorButton.addActionListener(this);
        boxColorButton.setToolTipText(cfgPkg.Text.POPboxColorButton);
        this.add(boxColorButton);

        bodyBackgroundColorButton.setLocation(765, 670);
        bodyBackgroundColorButton.setSize(180, 20);
        bodyBackgroundColorButton.addActionListener(this);
        bodyBackgroundColorButton.setToolTipText(cfgPkg.Text.POPbodyBackColorButton);
        this.add(bodyBackgroundColorButton);

        samplePanel.setLayout(null);
        samplePanel.setLocation(955, 640);
        samplePanel.setSize(210, 55);
        samplePanel.setBackground(bodyBackgroundColor);
        samplePanel.setBorder(BorderFactory.createLineBorder(boxColor, 2));

        sampleTitlePanel.setLayout(null);
        sampleTitlePanel.setLocation(0, 0);
        sampleTitlePanel.setSize(210, 20);
        sampleTitlePanel.setBackground(boxColor);
        sampleTitlePanel.setBorder(BorderFactory.createLineBorder(boxColor, 2));
        samplePanel.add(sampleTitlePanel);

        sampleTitleLbl.setLocation(5, 0);
        sampleTitleLbl.setSize(200, 20);
        sampleTitleLbl.setForeground(titleColor);
        sampleTitlePanel.add(sampleTitleLbl);
        titleFont = new Font(titleFontCombo.getName(), Font.PLAIN, getFontSize(titleSizeCombo.getSelectedIndex()));
        sampleTitleLbl.setFont(titleFont);

        sampleBodyLbl.setLocation(5, 25);
        sampleBodyLbl.setSize(200, 20);
        sampleBodyLbl.setForeground(bodyColor);
        samplePanel.add(sampleBodyLbl);
        bodyFont = new Font(bodyFontCombo.getName(), Font.PLAIN, getFontSize(bodySizeCombo.getSelectedIndex()));
        sampleBodyLbl.setFont(bodyFont);
        
        this.add(samplePanel);

        JLabel style1Lbl = new JLabel(cfgPkg.GUI.advertStyle);
        style1Lbl.setLocation(765, 720);
        style1Lbl.setSize(100, 20);
        this.add(style1Lbl);

        advertStyleCombo = new JComboBox(advertStyles);
        advertStyleCombo.setLocation(765, 750);
        advertStyleCombo.setSize(80, 20);
        advertStyleCombo.addActionListener(this);
        advertStyleCombo.setSelectedItem(advertStyles[0]);
        advertStyleCombo.setToolTipText(cfgPkg.Text.POPadvertStyleCombo);
        this.add(advertStyleCombo);

        JLabel advertWidthLbl = new JLabel(cfgPkg.GUI.width);
        advertWidthLbl.setLocation(905, 720);
        advertWidthLbl.setSize(40, 20);
        this.add(advertWidthLbl);

        advertWidthTxt.setLocation(950, 720);
        advertWidthTxt.setSize(40, 20);
        advertWidthTxt.setToolTipText(cfgPkg.Text.POPadvertWidthTxt);
        this.add(advertWidthTxt);

        JLabel advertHeightLbl = new JLabel(cfgPkg.GUI.height);
        advertHeightLbl.setLocation(905, 750);
        advertHeightLbl.setSize(40, 20);
        this.add(advertHeightLbl);

        advertHeightTxt.setLocation(950, 750);
        advertHeightTxt.setSize(40, 20);
        advertHeightTxt.setToolTipText(cfgPkg.Text.POPadvertHeightTxt);
        this.add(advertHeightTxt);

        demoHtmlButton.setLocation(1045, 720);
        demoHtmlButton.setSize(120, 50);
        demoHtmlButton.addActionListener(this);
        demoHtmlButton.setToolTipText(cfgPkg.Text.POPdemoHtmlButton);
        this.add(demoHtmlButton);
        
        JLabel helpLbl = new JLabel("<html><p align=center>" + cfgPkg.GUI.contextualAdvertsLabel + "</p></html>");
        helpLbl.setLocation(760, 785);
        helpLbl.setSize(410, 60);
        this.add(helpLbl);
    }
    
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == checkBox)
            table.selectAll(checkBox.isSelected());

        else if (evt.getSource() == addButton) {
            Object[] rowData = { searchTxt.getText().trim(), categoryTxt.getText().trim(),
                                 descriptionTxt.getText().trim(), urlTxt.getText().trim() };
            table.addRow(rowData);
        }

        else if (evt.getSource() == modifyButton) {
            Object[] rowData = { searchTxt.getText().trim(), categoryTxt.getText().trim(),
                                 descriptionTxt.getText().trim(), urlTxt.getText().trim() };
            table.modifyRow(rowData);
        }

        else if (evt.getSource() == deleteButton)
            table.deleteRows();

        else if (evt.getSource() == replaceButton)
            replaceAdverts();

        else if (evt.getSource() == copyAdvertBtn)
            replaceAdvertsCopy();
        
        else if (evt.getSource() == titleFontCombo) {
            titleFont = new Font((String)titleFontCombo.getSelectedItem(), titleFont.getStyle(), titleFont.getSize());
            sampleTitleLbl.setFont(titleFont);
        }

        else if (evt.getSource() == titleSizeCombo) {
            titleFont = new Font(titleFont.getName(), titleFont.getStyle(), getFontSize(titleSizeCombo.getSelectedIndex()));
            sampleTitleLbl.setFont(titleFont);
        }

        else if (evt.getSource() == titleColorButton) {
            Color color = colorChooser.showDialog(this, cfgPkg.GUI.selectTitleTextColor, titleColor);
            if (color != null) {
                titleColor = color;
                sampleTitleLbl.setForeground(titleColor);
            }
        }

        else if (evt.getSource() == bodyFontCombo) {
            bodyFont = new Font((String)bodyFontCombo.getSelectedItem(), bodyFont.getStyle(), bodyFont.getSize());
            sampleBodyLbl.setFont(bodyFont);
        }

        else if (evt.getSource() == bodySizeCombo) {
            bodyFont = new Font(bodyFont.getName(), bodyFont.getStyle(), getFontSize(bodySizeCombo.getSelectedIndex()));
            sampleBodyLbl.setFont(bodyFont);
        }

        else if (evt.getSource() == bodyColorButton) {
            Color color = colorChooser.showDialog(this, cfgPkg.GUI.selectBodyTextColor, bodyColor);
            if (color != null) {
                bodyColor = color;
                sampleBodyLbl.setForeground(bodyColor);
            }
        }

        else if (evt.getSource() == bodyBackgroundColorButton) {
            Color color = colorChooser.showDialog(this, cfgPkg.GUI.selectAdvertBackgroundColor, bodyBackgroundColor);
            if (color != null) {
                bodyBackgroundColor = color;
                samplePanel.setBackground(bodyBackgroundColor);
            }
        }

        else if (evt.getSource() == boxColorButton) {
            Color color = colorChooser.showDialog(this, cfgPkg.GUI.selectAdvertBoxColor, boxColor);
            if (color != null) {
                boxColor = color;
                sampleTitlePanel.setBackground(boxColor);
                samplePanel.setBorder(BorderFactory.createLineBorder(boxColor, 2));
                sampleTitlePanel.setBorder(BorderFactory.createLineBorder(boxColor, 2));
            }
        }

        else if (evt.getSource() == titleBoldCB)    sampleTitleLbl.setText(getHtmlFormat(true, cfgPkg.GUI.demoTitle, false));
        else if (evt.getSource() == titleItalicsCB) sampleTitleLbl.setText(getHtmlFormat(true, cfgPkg.GUI.demoTitle, false));
        else if (evt.getSource() == titleUnderCB)   sampleTitleLbl.setText(getHtmlFormat(true, cfgPkg.GUI.demoTitle, false));
        else if (evt.getSource() == bodyBoldCB)     sampleBodyLbl.setText(getHtmlFormat(false, cfgPkg.GUI.demoBody, false));
        else if (evt.getSource() == bodyItalicsCB)  sampleBodyLbl.setText(getHtmlFormat(false, cfgPkg.GUI.demoBody, false));
        else if (evt.getSource() == bodyUnderCB)    sampleBodyLbl.setText(getHtmlFormat(false, cfgPkg.GUI.demoBody, false));

        else if (evt.getSource() == demoHtmlButton) {
            if (cfgPkg.Const.liteVersion) {
                JOptionPane.showMessageDialog(null, cfgPkg.Text.liteMessage, cfgPkg.Text.liteMessageTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            buildDemoHtml();
            String args = "\""+projectSetupTab.outputDir.getAbsolutePath()+cfgPkg.Const.filesep()+"DemoDHTML.html\"";
            cfgPkg.Files.browseFile("browser", args);
        }

        else if (evt.getSource() == productAssistantBtn)
            productAssistant();
    }
    
    private int getFontSize(int size) {
        if (size == 0) return 10;
        else if (size == 1) return 12;
        else if (size == 2) return 14;
        else if (size == 3) return 16;
        else if (size == 4) return 18;
        else return 20;
    }
    
    private int dhtmlFontSize(int size) {
        if (size == 10) return 1;
        else if (size == 12) return 2;
        else if (size == 14) return 3;
        else if (size == 16) return 4;
        else if (size == 18) return 5;
        else return 6;
    }
    
    private String getHtmlFormat(boolean title, String string, boolean demo) {
        String htmlStart = "";
        if (!demo) htmlStart = "<html>";
        if (title) {
            if (titleBoldCB.isSelected())    htmlStart = htmlStart + "<B>";
            if (titleItalicsCB.isSelected()) htmlStart = htmlStart + "<I>";
            if (titleUnderCB.isSelected())   htmlStart = htmlStart + "<U>";
        } else {
            if (bodyBoldCB.isSelected())    htmlStart = htmlStart + "<B>";
            if (bodyItalicsCB.isSelected()) htmlStart = htmlStart + "<I>";
            if (bodyUnderCB.isSelected())   htmlStart = htmlStart + "<U>";
        }

        String htmlEnd = "";
        if (title) {
            if (titleBoldCB.isSelected())    htmlEnd = htmlEnd + "</B>";
            if (titleItalicsCB.isSelected()) htmlEnd = htmlEnd + "</I>";
            if (titleUnderCB.isSelected())   htmlEnd = htmlEnd + "</U>";
        } else {
            if (bodyBoldCB.isSelected())    htmlEnd = htmlEnd + "</B>";
            if (bodyItalicsCB.isSelected()) htmlEnd = htmlEnd + "</I>";
            if (bodyUnderCB.isSelected())   htmlEnd = htmlEnd + "</U>";
        }
        if (!demo) htmlEnd = htmlEnd + "</html>";

        return htmlStart + string + htmlEnd;
    }

    private void buildDemoHtml() {
        String fileTxt = "<HTML>" + cfgPkg.Const.linesep() + "<HEAD>" + cfgPkg.Const.linesep() + cfgPkg.Const.dhtmlHeadCode;
        fileTxt = fileTxt + "<BODY>" + cfgPkg.Const.linesep() + cfgPkg.Const.dhtmlBodyCode;

        String productCode = getAdvertStr(cfgPkg.GUI.titleText, cfgPkg.GUI.bodyText, true);
        productCode = productCode.replace(cfgPkg.Const.dhtmlUrl, "http://www.google.com");
        productCode = productCode.replace(cfgPkg.Const.dhtmlWord, cfgPkg.GUI.example);
        fileTxt     = fileTxt + productCode + cfgPkg.Const.linesep() + "</BODY>" + cfgPkg.Const.linesep() + "</HTML>" + cfgPkg.Const.linesep();

        cfgPkg.Files.writeFile(projectSetupTab.outputDir.getAbsolutePath()+cfgPkg.Const.filesep()+"DemoDHTML.html", fileTxt);
    }
    
    private void replaceAdverts() {
        if (cfgPkg.Const.liteVersion) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.liteMessage, cfgPkg.Text.liteMessageTitle, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (gfp.getTable().getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGselectGeneratedFiles, cfgPkg.Text.MSGselectionError, JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (table.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGreplaceAdverts, cfgPkg.Text.MSGselectionError, JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.MSGconfirmAdverts, cfgPkg.Text.MSGconfirmReplace, JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.NO_OPTION) return;

        // loop through all selected Generated Files
        int[] selected = gfp.getTable().getSelectedRows();
        for (int i=0; i<selected.length; i++) {
            String genFileName  = gfp.getTable().getValueAt(selected[i], 1).toString();
            String fileTxt      = cfgPkg.Files.readFile(genFileName, false);
            String productCode  = "";

            Vector searchVector = new Vector();
            Vector thisVector;

            // loop through all selected product search/replace
            int[] selProduct = table.getSelectedRows();
            for (int j=0; j<selProduct.length; j++) {
                String searchTxt = table.getValueAt(selProduct[j], 0).toString();
                productCode = getProductStr(searchTxt, selProduct[j]);

                boolean found = false;
                for (int k=0; k<searchVector.size() && !found; k++) {
                    thisVector = (Vector)searchVector.get(k);
                    if (thisVector.get(0).toString().equals(searchTxt)) {
                        thisVector.addElement(productCode);
                        found = true;
                    }
                }

                if (!found) {
                    thisVector = new Vector();
                    thisVector.addElement(searchTxt);
                    thisVector.addElement(productCode);
                    searchVector.addElement(thisVector);
                }
            }
            
            for (int k=0; k<searchVector.size(); k++) {
                thisVector = (Vector)searchVector.get(k);
                fileTxt    = cfgPkg.Util.insertAdvert(fileTxt, thisVector.get(0).toString(), thisVector);
            }
            cfgPkg.Files.writeFile(genFileName, fileTxt);
        }
    }

    private void replaceAdvertsCopy() {
        if (cfgPkg.Const.liteVersion) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.liteMessage, cfgPkg.Text.liteMessageTitle, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (table.getSelectedRowCount() != 1) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGonlyOne, cfgPkg.Text.MSGselectionError, JOptionPane.ERROR_MESSAGE);
            return;
        }

        int selProduct     = table.getSelectedRow();
        String searchTxt   = table.getValueAt(selProduct, 0).toString();
        String productCode = getProductStr(searchTxt, selProduct);

        StringSelection ss = new StringSelection(productCode);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
    }

   private String getProductStr(String searchTxt, int selProduct) {
        String productCode = "";
        String dhtmlTitle  = "";
        String advertStyle = (String)advertStyleCombo.getSelectedItem(); //{"default", "no title", "sticky", "follow"}
        if (advertStyle != advertStyles[1]) {
            dhtmlTitle = searchTxt;
            /*dhtmlTitle = JOptionPane.showInputDialog(null, cfgPkg.Text.MSGadvertTitle + searchTxt, searchTxt);
            if (dhtmlTitle == null)
                return;
            else if (dhtmlTitle.equals("")) {
                JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGadvertTitleBlank, cfgPkg.Text.MSGinputError, JOptionPane.ERROR_MESSAGE);
                return;
            }*/
        }

        String dhtmlDesc = table.getValueAt(selProduct, 2).toString();
        productCode      = getAdvertStr(dhtmlTitle, dhtmlDesc, false);

        String dhtmlUrl  = table.getValueAt(selProduct, 3).toString();
        productCode      = productCode.replace(cfgPkg.Const.dhtmlUrl, dhtmlUrl);
        productCode      = productCode.replace(cfgPkg.Const.dhtmlWord, searchTxt);
        
        return productCode;
   }
   
    private String getAdvertStr(String dhtmlTitle, String dhtmlDesc, boolean demo) {
        // Alternative style formatting
        /*dhtmlTitle = "<font size=" + Integer.toString(dhtmlFontSize(titleFont.getSize())) + " face=" +
            titleFont.getName() + " color=" + getHtmlColor(titleColor) + ">" + getHtmlFormat(true, dhtmlTitle, demo);
        getHtmlFormat(true, dhtmlTitle, demo);
        dhtmlDesc = "<font size=" + Integer.toString(dhtmlFontSize(bodyFont.getSize())) + " face=" +
            bodyFont.getName() + " color=" + getHtmlColor(bodyColor) + ">" + dhtmlDesc;
        getHtmlFormat(true, dhtmlDesc, demo);*/

        //"Title","Body"
        String titleStr = "'" + getHtmlFormat(true, dhtmlTitle, demo) + "',";
        String bodyStr  = "'" + getHtmlFormat(false, dhtmlDesc, demo) + "',";

        //[Title,Body,TitleColor,TextColor,TitleBgColor,TextBgColor,TitleBgImag,TextBgImag,TitleTextAlign,TextTextAlign,
        // TitleFontFace,TextFontFace,TipPosition,StickyStyle,TitleFontSize,TextFontSize,Width,Height,BorderSize,
        // PadTextArea,CoordinateX,CoordinateY,TransitionNumber,TransitionDuration,TransparencyLevel,ShadowType,ShadowColor]
        
        //"Title","Body","black","black","blue","white","","","","","","","","","","",200,"",2,2,10,10,"","","","",""       default
        //"","Body","black","black","blue","white","","","","","","","","","","",200,"",2,2,10,10,"","","","",""            no title
        //"Title","Body","black","black","blue","white","","","","","","","","sticky","","",200,"",2,2,10,10,"","","","","" sticky
        //"Title","Body","black","black","blue","white","","","","","","","","keep","","",200,"",2,2,10,10,"","","","",""   keep (follow)
        //"Title","Body","white","black","black","white","","","right","","Impact","cursive","center","",3,5,200,150,5,20,10,0,50,1,80,"complex","gray"]
        String styleStr = "'TitleColor','TextColor','TitleBgColor','TextBgColor','','','',''," +
            "'TitleFontFace','TextFontFace','','StickyStyle',TitleFontSize,TextFontSize,Width,Height,2,2,10,10,'','','','',''";

        styleStr = styleStr.replace("TitleColor",    getHtmlColor(titleColor));
        styleStr = styleStr.replace("TextColor",     getHtmlColor(bodyColor));
        styleStr = styleStr.replace("TitleBgColor",  getHtmlColor(boxColor));
        styleStr = styleStr.replace("TextBgColor",   getHtmlColor(bodyBackgroundColor));
        styleStr = styleStr.replace("TitleFontFace", titleFont.getName());
        styleStr = styleStr.replace("TextFontFace",  bodyFont.getName());
        
        String advertStyle = (String)advertStyleCombo.getSelectedItem(); //{"default", "no title", "sticky", "follow"}
        if (advertStyle == advertStyles[0])
            advertStyle = "";
        else if (advertStyle == advertStyles[1]) {
            advertStyle = "";
            titleStr = "'',";
        }
        else if (advertStyle == advertStyles[2])
            advertStyle = advertStyles[2];
        else if (advertStyle == advertStyles[3])
            advertStyle = "keep";

        styleStr = styleStr.replace("StickyStyle", advertStyle);
        styleStr = styleStr.replace("TitleFontSize", Integer.toString(dhtmlFontSize(titleFont.getSize())));
        styleStr = styleStr.replace("TextFontSize", Integer.toString(dhtmlFontSize(bodyFont.getSize())));

        if (advertWidthTxt.getText().trim().equals(""))
            styleStr = styleStr.replace("Width", "''");
        else
            try {
                int advertWidth  = Integer.parseInt(advertWidthTxt.getText().trim());
                styleStr = styleStr.replace("Width", advertWidthTxt.getText());
            } catch(java.lang.NumberFormatException exp) {
                JOptionPane.showMessageDialog(null, exp.toString()+cfgPkg.Text.MSGadvertWidth, cfgPkg.Text.MSGnumericError, JOptionPane.ERROR_MESSAGE);
                styleStr = styleStr.replace("Width", "''");
            }

        if (advertHeightTxt.getText().trim().equals(""))
            styleStr = styleStr.replace("Height", "''");
        else
            try {
                int advertHeight = Integer.parseInt(advertHeightTxt.getText().trim());
                styleStr = styleStr.replace("Height", advertHeightTxt.getText());
            } catch(java.lang.NumberFormatException exp) {
                JOptionPane.showMessageDialog(null, exp.toString()+cfgPkg.Text.MSGadvertHeight, cfgPkg.Text.MSGnumericError, JOptionPane.ERROR_MESSAGE);
                styleStr = styleStr.replace("Height", "''");
            }

        return cfgPkg.Const.dhtmlAdvertCode.replace(cfgPkg.Const.dhtmlArguments, titleStr+bodyStr+styleStr);
    }

    private String getHtmlColor(Color color) {
        int    value = color.getRGB();
        String red   = Integer.toString(color.getRed(), 16);
        if (red.length() == 1) red = "0" + red;
        String green = Integer.toString(color.getGreen(), 16);
        if (green.length() == 1) green = "0" + green;
        String blue  = Integer.toString(color.getBlue(), 16);
        if (blue.length() == 1) blue = "0" + blue;
        
        return "#" + red + green + blue;
    }
        
    private void productAssistant() {
        if (cfgPkg.Const.liteVersion) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.liteMessage, cfgPkg.Text.liteMessageTitle, JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // loop through all selected Generated Files
        int[] selected = gfp.getTable().getSelectedRows();
        String keyword = "";
        /*if (selected.length == 1) {
            String genFileName = gfp.getValueAt(selected[0], 1).toString();
            keyword            = cfgPkg.Files.parseHTML(genFileName, "<body>", false);
        } else {
            keyword = JOptionPane.showInputDialog(null, cfgPkg.Text.MSGkeywordAdverts, "").trim();
        }*/
        cfgPkg.Files.UCCResearchBrowsers(cfgPkg.Const.productBrowser, "", "");
    }
    
    public void setAdvertsSearchReplace(DefaultTableModel model) {
        table.resetRows(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(170);
        table.getColumnModel().getColumn(1).setPreferredWidth(170);
        table.getColumnModel().getColumn(2).setPreferredWidth(350);
        table.getColumnModel().getColumn(3).setPreferredWidth(475);
    }

    public DefaultTableModel getAdvertsSearchReplace() {
        return (DefaultTableModel)table.getTableModel();
    }

    public void setDhtmlValues(
            String titleFont, int titleSize, Color titleColor,
            boolean titleBold, boolean titleItalics, boolean titleUnder,
            String bodyFont, int bodySize, Color bodyColor,
            boolean bodyBold, boolean bodyItalics, boolean bodyUnder,
            Color bodyBackgroundColor, Color boxColor, String advertStyle,
            String advertWidth, String advertHeight)
    {
        this.titleFont  = new Font(titleFont, Font.PLAIN, titleSize);
        titleFontCombo.setSelectedItem(titleFont);
        titleSizeCombo.setSelectedIndex(dhtmlFontSize(titleSize)-1);
        this.titleColor = titleColor;
        sampleTitleLbl.setForeground(titleColor);
        this.titleBoldCB.setSelected(titleBold);
        this.titleItalicsCB.setSelected(titleItalics);
        this.titleUnderCB.setSelected(titleUnder);

        this.bodyFont  = new Font(bodyFont, Font.PLAIN, bodySize);
        bodyFontCombo.setSelectedItem(bodyFont);
        bodySizeCombo.setSelectedIndex(dhtmlFontSize(bodySize)-1);
        this.bodyColor = bodyColor;
        sampleBodyLbl.setForeground(bodyColor);
        this.bodyBoldCB.setSelected(bodyBold);
        this.bodyItalicsCB.setSelected(bodyItalics);
        this.bodyUnderCB.setSelected(bodyUnder);
        
        this.bodyBackgroundColor = bodyBackgroundColor;
        samplePanel.setBackground(bodyBackgroundColor);
        samplePanel.setBorder(BorderFactory.createLineBorder(boxColor, 2));

        this.boxColor            = boxColor;
        sampleTitlePanel.setBackground(boxColor);
        sampleTitlePanel.setBorder(BorderFactory.createLineBorder(boxColor, 2));
        this.advertStyleCombo.setSelectedItem(advertStyle);
        this.advertWidthTxt.setText(advertWidth);
        this.advertHeightTxt.setText(advertHeight);
        
        sampleTitleLbl.setText(getHtmlFormat(true, cfgPkg.GUI.demoTitle, false));
        sampleBodyLbl.setText(getHtmlFormat(false, cfgPkg.GUI.demoBody, false));
    }

    public Vector getDhtmlValues() {
        Vector vector = new Vector();

        vector.addElement(titleFont.getName());
        vector.addElement(titleFont.getSize());
        vector.addElement(titleColor);
        vector.addElement(titleBoldCB.isSelected());
        vector.addElement(titleItalicsCB.isSelected());
        vector.addElement(titleUnderCB.isSelected());

        vector.addElement(bodyFont.getName());
        vector.addElement(bodyFont.getSize());
        vector.addElement(bodyColor);
        vector.addElement(bodyBoldCB.isSelected());
        vector.addElement(bodyItalicsCB.isSelected());
        vector.addElement(bodyUnderCB.isSelected());

        vector.addElement(bodyBackgroundColor);
        vector.addElement(boxColor);
        vector.addElement(advertStyleCombo.getSelectedItem().toString());
        vector.addElement(advertWidthTxt.getText().trim());
        vector.addElement(advertHeightTxt.getText().trim());

        return vector;
    }

    public void setGeneratedFiles() {
        gfp.resizeRows();
    }

    public void setDefaults() {
        gfp.setInputDir(projectSetupTab.inputDir);
        gfp.setOutputDir(projectSetupTab.outputDir);
    }
}
