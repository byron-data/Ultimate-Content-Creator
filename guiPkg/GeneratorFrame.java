package guiPkg;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.*;
import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.UIManager.LookAndFeelInfo;

import cfgPkg.*;

public class GeneratorFrame extends JFrame implements ActionListener {
    private final static int screenWidth  = 1200;
    private final static int screenHeight = 900;
    private final static int bounds       = 10;
    private final static int tabWidth     = screenWidth - (2*bounds);
    private final static int tabHeight    = screenHeight - (2*bounds);
    
    private JTabbedPane         tabbedPane;
    private JScrollPane         scrollPane             = new JScrollPane();
    private ProjectSetupTab     projectSetupTab;
    private ContentAssistantTab contentAssistantTab;
    private GrabContentTab      grabContentTab;
    private BatchProcessTab     batchProcessTab;
    private RssFeedsTab         rssFeedsTab;
    private PopupsTab           popupsTab;
    private TextTab             textTab;
    private JMenuBar            menuBar;
    private JMenu               fileMenu               = new JMenu(cfgPkg.GUI.file);
    private JMenuItem           newMenuItem            = new JMenuItem(cfgPkg.GUI.newStr, KeyEvent.VK_N);
    private JMenuItem           openMenuItem           = new JMenuItem(cfgPkg.GUI.open, KeyEvent.VK_O);
    private JMenuItem           closeMenuItem          = new JMenuItem(cfgPkg.GUI.close, KeyEvent.VK_C);
    private JMenuItem           saveMenuItem           = new JMenuItem(cfgPkg.GUI.save, KeyEvent.VK_S);
    private JMenuItem           saveAsMenuItem         = new JMenuItem(cfgPkg.GUI.saveAs, KeyEvent.VK_A);
    private JMenuItem           defaultsSaveMenuItem   = new JMenuItem(cfgPkg.GUI.saveAs + " " + cfgPkg.GUI.defaults, KeyEvent.VK_D);
    private JMenuItem           defaultsRevertMenuItem = new JMenuItem(cfgPkg.GUI.revertsTo + " " + cfgPkg.GUI.defaults, KeyEvent.VK_R);
    private JMenuItem           exitMenuItem           = new JMenuItem(cfgPkg.GUI.exit, KeyEvent.VK_X);
    private JMenu               helpMenu               = new JMenu(cfgPkg.GUI.help);
    private JMenuItem           aboutMenuItem          = new JMenuItem(cfgPkg.GUI.about, KeyEvent.VK_B);

    private File                projectFile  = null;
    private static Vector       framesVector = new Vector();
    
    /**
    * @param args the command line arguments
    */
    public static void main(String[] args) {
        File f = new File("errorlog.txt");
        PrintStream printStream;
        try {
            printStream = new PrintStream(f);
            System.setErr(printStream);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        
        try {
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {}
        
        String OS_Id = cfgPkg.Const.OS();
        System.err.println("Operating system is " + OS_Id);

        GeneratorFrame pageFrame = null;
        if (args.length > 0 && args[0] != "")
            pageFrame = new GeneratorFrame(new File(args[0]));
        else
            pageFrame = new GeneratorFrame();
        pageFrame.setVisible(true);
    }

    public GeneratorFrame() {
        this.addFrameComponents(null);
    }
    
    public GeneratorFrame(File projectFile) {
        this.addFrameComponents(projectFile);
        if (projectFile != null && projectFile.exists())
            getProjectDetails(projectFile);
    }
    
    private void addFrameComponents(File projectFile) {
        JDesktopPane desktopPane = new JDesktopPane();
        desktopPane.setPreferredSize(new Dimension(screenWidth, screenHeight));
        getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(tabWidth, tabHeight));
        tabbedPane.setBounds(bounds, bounds, tabWidth, tabHeight);
        
        scrollPane.setViewportView(tabbedPane);
        scrollPane.setPreferredSize(new Dimension(tabWidth+3, tabHeight+3));
        desktopPane.add(scrollPane, javax.swing.JLayeredPane.DEFAULT_LAYER);
        
        projectSetupTab     = new ProjectSetupTab(tabWidth, tabHeight);
        batchProcessTab     = new BatchProcessTab(tabWidth, tabHeight, projectSetupTab);
        contentAssistantTab = new ContentAssistantTab(tabWidth, tabHeight, projectSetupTab, batchProcessTab);
        grabContentTab      = new GrabContentTab(tabWidth, tabHeight, projectSetupTab, batchProcessTab);
        rssFeedsTab         = new RssFeedsTab(tabWidth, tabHeight, projectSetupTab);
        popupsTab           = new PopupsTab(tabWidth, tabHeight, projectSetupTab);
        textTab             = new TextTab(tabWidth, tabHeight, projectSetupTab);

        menuBar  = new JMenuBar();
        fileMenu.setMnemonic(KeyEvent.VK_F);
        newMenuItem.addActionListener(this);
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_N, ActionEvent.ALT_MASK));
        openMenuItem.addActionListener(this);
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_O, ActionEvent.ALT_MASK));
        closeMenuItem.addActionListener(this);
        closeMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_C, ActionEvent.ALT_MASK));
        saveMenuItem.addActionListener(this);
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_S, ActionEvent.ALT_MASK));
        saveAsMenuItem.addActionListener(this);
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_A, ActionEvent.ALT_MASK));

        defaultsSaveMenuItem.addActionListener(this);
        defaultsSaveMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_D, ActionEvent.ALT_MASK));
        defaultsRevertMenuItem.addActionListener(this);
        defaultsRevertMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_R, ActionEvent.ALT_MASK));
        
        exitMenuItem.addActionListener(this);
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_X, ActionEvent.ALT_MASK));
        helpMenu.setMnemonic(KeyEvent.VK_H);
        aboutMenuItem.addActionListener(this);
        aboutMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_B, ActionEvent.ALT_MASK));

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                windowClose();
            }
        });

        tabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (tabbedPane.getSelectedComponent() == grabContentTab)
                    grabContentTab.startTimer();
                else
                    grabContentTab.stopTimer();
                
                batchProcessTab.setDefaults();
                rssFeedsTab.setDefaults();
                popupsTab.setDefaults();
                textTab.setDefaults();
            }
        });

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        getContentPane().setLayout(new BorderLayout());
        addTabs();
        addMenu();
        pack();
        
        
        setIconImage(new ImageIcon(GeneratorFrame.class.getResource("images/UCC.jpg")).getImage());
        projectSetupTab.setDefaults(cfgPkg.Files.openProgramFile());

        if (projectFile != null && projectFile.exists())
            setTitle(cfgPkg.Const.title + " - " + projectFile.getAbsolutePath());
        else {
            getProjectDetails(new File(cfgPkg.Const.defaultSettingsFile));
            projectFile = null;
            setTitle(cfgPkg.Const.title + " - " + cfgPkg.GUI.unnamedProject);
        }
        
        framesVector.addElement(this);
    }

    private void addTabs() {
        tabbedPane.add(contentAssistantTab, cfgPkg.GUI.contentAssistantStr);
        tabbedPane.add(grabContentTab,      cfgPkg.GUI.grabContentStr);
        tabbedPane.add(batchProcessTab,     cfgPkg.GUI.batchProcessStr);
        tabbedPane.add(rssFeedsTab,         cfgPkg.GUI.rssFeedsStr);
        tabbedPane.add(popupsTab,           cfgPkg.GUI.popupsStr);
        tabbedPane.add(textTab,             cfgPkg.GUI.textStr);
        tabbedPane.add(projectSetupTab,     cfgPkg.GUI.projectSetupStr);
        this.add(scrollPane);
        if (cfgPkg.Const.liteVersion)
            tabbedPane.setSelectedComponent(grabContentTab);
    }

    private void addMenu(){
        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(closeMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.add(saveAsMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(defaultsSaveMenuItem);
        fileMenu.add(defaultsRevertMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);
        this.setJMenuBar(menuBar);
    }
	
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == newMenuItem) {
            newProject();
        }
        else if (e.getSource() == openMenuItem) {
            openProject();
        }
        else if (e.getSource() == closeMenuItem) {
            this.setVisible(false);
            this.dispose();
            windowClose();
        }
        else if (e.getSource() == saveMenuItem) {
            if (projectFile == null)
                saveAsProject();
            else
                saveProject();
        }
        else if (e.getSource() == saveAsMenuItem) {
            saveAsProject();
        }
        else if (e.getSource() == defaultsSaveMenuItem) {
            int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.MSGprojectDefaults, cfgPkg.Text.MSGconfirmDefaults, JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                File oldProjectFile = projectFile;
                projectFile         = new File(cfgPkg.Const.defaultSettingsFile);
                saveProject();
                projectFile         = oldProjectFile;
            }
        }
        else if (e.getSource() == defaultsRevertMenuItem) {
            int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.MSGprojectRevert, cfgPkg.Text.MSGrevertDefaults, JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                File oldProjectFile = projectFile;
                getProjectDetails(new File(cfgPkg.Const.defaultSettingsFile));
                projectFile         = oldProjectFile;
            }
        }
        else if (e.getSource() == exitMenuItem) {
            int size = framesVector.size();
            for (int i=size; i>0; i--) {
                GeneratorFrame frame = (GeneratorFrame)framesVector.get(i-1);
                frame.setAlwaysOnTop(true);
            }
            for (int i=size; i>0; i--) {
                GeneratorFrame frame = (GeneratorFrame)framesVector.get(i-1);
                frame.windowClose();
                frame.setVisible(false);
            }
        } else if (e.getSource() == aboutMenuItem) {
            JOptionPane.showMessageDialog(null, cfgPkg.Const.about, cfgPkg.Const.title,
                    JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("images/cover.jpg")));
        }
        
        if (projectFile == null)
            setTitle(cfgPkg.Const.title + " - " + cfgPkg.GUI.unnamedProject);
        else
            setTitle(cfgPkg.Const.title + " - " + projectFile.getAbsolutePath());
    }
    
    private void windowClose() {
        if (projectFile == null)
            saveAsProject();
        else {
            int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.MSGprojectSave, cfgPkg.Text.MSGconfirmSave, JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION)
                saveProject();
        }
        framesVector.removeElement(this);
        if (framesVector.size() == 0) System.exit(0);            
    }
    
    private void newProject() {
        GeneratorFrame pageFrame = new GeneratorFrame();
        pageFrame.setVisible(true);
    }
    
    public class MyProjectFileFilter extends javax.swing.filechooser.FileFilter {
        public boolean accept(File file) {
            if (file.isDirectory()) {
                return true;
            }

            String filename = file.getName();
            return filename.endsWith("." + cfgPkg.Const.suffix);
        }
    
        public String getDescription() {
            return "*." + cfgPkg.Const.suffix;
        }
    }
    
    private void getProjectDetails(File projectFile) {
        ProjectConfigurationFile pcf;

        try
        {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(projectFile));
            pcf  = (ProjectConfigurationFile) inputStream.readObject();
            inputStream.close();
            this.projectFile = projectFile;
        } catch(Exception ex) {
            System.err.println(ex.toString());
            pcf = new ProjectConfigurationFile();
            this.projectFile = null;
        }

        // projectSetupTab values
        projectSetupTab.setInputDir(pcf.inputDir);
        projectSetupTab.setOutputDir(pcf.outputDir);
        projectSetupTab.setSnippetChars(pcf.snippetCharsTxt);
        projectSetupTab.setExtension(pcf.extensionTxt);
        projectSetupTab.setPreviewChars(pcf.previewCharsTxt);
        projectSetupTab.setContentTemplate(pcf.contentTemplate);
        projectSetupTab.setIndexTemplate(pcf.indexTemplate);
        //projectSetupTab.setEditor(pcf.editor);

        // GrabContentTab values
        grabContentTab.setTags(pcf.tagTableModel);

        // BatchProcessTab values
        batchProcessTab.setTags(pcf.tagTableModel);
        batchProcessTab.setFileList(pcf.fileListModel);
        batchProcessTab.setPreviewTag(pcf.previewTag);
        batchProcessTab.setGeneratedFiles(pcf.generatedFilesModel);
        batchProcessTab.setDefaults();

        // Reset the table column widths
        grabContentTab.setTags();
        batchProcessTab.setTags();

        // RssFeedsTab values
        rssFeedsTab.setRssSearchReplace(pcf.rssSearchReplaceTableModel);
        rssFeedsTab.setGeneratedFiles();
        rssFeedsTab.setDefaults();

        // PopupsTab values
        popupsTab.setAdvertsSearchReplace(pcf.popupsSearchReplaceTableModel);
        popupsTab.setGeneratedFiles();
        popupsTab.setDefaults();

        popupsTab.setDhtmlValues(
            pcf.titleFont, pcf.titleSize, pcf.titleColor,
            pcf.titleBold, pcf.titleItalics, pcf.titleUnder,
            pcf.bodyFont, pcf.bodySize, pcf.bodyColor,
            pcf.bodyBold, pcf.bodyItalics, pcf.bodyUnder,
            pcf.bodyBackgroundColor, pcf.boxColor, pcf.popupStyle, pcf.popupWidth, pcf.popupHeight);

        // TextTab values
        textTab.setTextSearchReplace(pcf.textSearchReplaceTableModel);
        textTab.setGeneratedFiles();
        textTab.setDefaults();
    }
    
    private void openProject() {
        int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.MSGprojectNew, cfgPkg.Text.MSGconfirmWindow, JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.NO_OPTION) {
            int choice2 = JOptionPane.showConfirmDialog(null, cfgPkg.Text.MSGprojectSave + cfgPkg.Text.MSGbeforeClosing,
                    cfgPkg.Text.MSGconfirmSave, JOptionPane.YES_NO_OPTION);
            if (choice2 == JOptionPane.YES_OPTION) {
                if (projectFile == null)
                    saveAsProject();
                else
                    saveProject();
            }
        }

        JFileChooser chooser       = new JFileChooser();
        MyProjectFileFilter filter = new MyProjectFileFilter();
        chooser.setFileFilter(filter);
        int response = chooser.showOpenDialog(this);
        if (response == JFileChooser.APPROVE_OPTION) {
            if (choice == JOptionPane.NO_OPTION)
                getProjectDetails(chooser.getSelectedFile()); // open project in same frame
            else {
                GeneratorFrame pageFrame = new GeneratorFrame(chooser.getSelectedFile());
                //pageFrame.getProjectDetails(chooser.getSelectedFile());
                pageFrame.setVisible(true);
            }
        }
    }
    
    private void saveProject() {
        try
        {
            File newFile = new File("");
            
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(projectFile));
            ProjectConfigurationFile pcf    = new ProjectConfigurationFile();

            // ProjectSetupTab values
            pcf.inputDir            = projectSetupTab.getInputDir();
            pcf.outputDir           = projectSetupTab.getOutputDir();
            pcf.snippetCharsTxt     = projectSetupTab.getSnippetChars();
            pcf.extensionTxt        = projectSetupTab.getExtension();
            pcf.previewCharsTxt     = projectSetupTab.getPreviewChars();
            pcf.contentTemplate     = projectSetupTab.getContentTemplate();
            pcf.indexTemplate       = projectSetupTab.getIndexTemplate();
            //pcf.editor              = projectSetupTab.getEditor();

            // GrabContentTab values
            pcf.tagTableModel       = grabContentTab.getTags();

            // BatchProcessTab values
            pcf.fileListModel       = batchProcessTab.getFileList();
            pcf.previewTag          = batchProcessTab.getPreviewTag();
            pcf.generatedFilesModel = batchProcessTab.getGeneratedFiles();

            // RssFeedsTab values
            pcf.rssSearchReplaceTableModel    = rssFeedsTab.getRssSearchReplace();

            // PopupsTab values
            pcf.popupsSearchReplaceTableModel = popupsTab.getAdvertsSearchReplace();

            Vector vector                     = popupsTab.getDhtmlValues();

            try {
                pcf.titleFont                       = (String)vector.get(0);
                pcf.titleSize                       = Integer.parseInt(vector.get(1).toString());
                pcf.titleColor                      = (Color)vector.get(2);
                pcf.titleBold                       = (vector.get(3).toString() == "true");
                pcf.titleItalics                    = (vector.get(4).toString() == "true");
                pcf.titleUnder                      = (vector.get(5).toString() == "true");
                pcf.bodyFont                        = (String)vector.get(6);
                pcf.bodySize                        = Integer.parseInt(vector.get(7).toString());
                pcf.bodyColor                       = (Color)vector.get(8);
                pcf.bodyBold                        = (vector.get(9).toString() == "true");
                pcf.bodyItalics                     = (vector.get(10).toString() == "true");
                pcf.bodyUnder                       = (vector.get(11).toString() == "true");
                pcf.bodyBackgroundColor             = (Color)vector.get(12);
                pcf.boxColor                        = (Color)vector.get(13);
                pcf.popupStyle                      = (String)vector.get(14);
                pcf.popupWidth                      = (String)vector.get(15);
                pcf.popupHeight                     = (String)vector.get(16);
            } catch(java.lang.NumberFormatException exp) {
                System.err.println(exp);
                return;
            }
            
            // TextTab values
            pcf.textSearchReplaceTableModel     = textTab.getTextSearchReplace();
            
            outputStream.writeObject(pcf);
            outputStream.close();
        } catch(Exception ex) {
            System.err.println(ex.toString());
        }
    }

    private void saveAsProject() {
        JFileChooser chooser       = new JFileChooser(projectFile);
        MyProjectFileFilter filter = new MyProjectFileFilter();
        chooser.setFileFilter(filter);
        int chooserResponse = chooser.showSaveDialog(this);
        if (chooserResponse == JFileChooser.APPROVE_OPTION) {
            projectFile = chooser.getSelectedFile();
            String fileName = projectFile.getName();
            if (!fileName.endsWith("." + cfgPkg.Const.suffix))
                projectFile = new File(projectFile.getAbsolutePath() + "." + cfgPkg.Const.suffix);
            saveProject();
        }
    }
}
