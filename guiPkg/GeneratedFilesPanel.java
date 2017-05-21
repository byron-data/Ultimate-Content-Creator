package guiPkg;

import java.awt.event.*;
import java.awt.Insets;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;
//import org.jdesktop.jdic.desktop.DesktopException;

public class GeneratedFilesPanel extends JPanel implements ActionListener {
    private Table        generatedFilesTable;
    private JLabel       generatedFilesLbl           = new JLabel(cfgPkg.GUI.generatedFiles);
    private JButton      openDirectoryBtn            = new JButton(cfgPkg.GUI.open);
    private JCheckBox    generatedFilesCheckBox      = new JCheckBox(cfgPkg.GUI.selectAll);
    private JButton      addGeneratedFilesButton     = new JButton(cfgPkg.GUI.add);
    private JTextField   fileNameGeneratedFilesTxt   = new JTextField();
    private JButton      renameGeneratedFilesButton  = new JButton(cfgPkg.GUI.rename);
    private JButton      editorGeneratedFilesButton  = new JButton(cfgPkg.GUI.editor);
    private JButton      browserGeneratedFilesButton = new JButton(cfgPkg.GUI.browser);
    private JButton      deleteGeneratedFilesButton  = new JButton(cfgPkg.GUI.delete);

    private File         inputDir                    = new File("");
    private File         outputDir                   = new File("");

    private ProjectSetupTab projectSetupTab;

    /** Creates a new instance of GeneratedFilesPanel */
    public GeneratedFilesPanel(int listHeight, ProjectSetupTab projectSetupTab) {
        this.projectSetupTab = projectSetupTab;
        generatedFilesTable  = new Table(projectSetupTab);
	this.setLayout(null);
        generatedFilesLbl.setLocation(5, 0);
        generatedFilesLbl.setSize(150, 20);
        this.add(generatedFilesLbl);

        int[] blankChecks = {1};
        generatedFilesTable.initialise(blankChecks, projectSetupTab.stmGen, false);
        generatedFilesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        generatedFilesTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 1 || evt.getClickCount() == 2) {
                    if (generatedFilesTable.getSelectedRows().length == 1) {
                        int selected = generatedFilesTable.getSelectedRow();
                        String fileName = generatedFilesTable.getValueAt(selected, 1).toString();
                        File indexFile = new File(fileName);
                        fileName = indexFile.getName();
                        //fileName = fileName.substring(0, fileName.lastIndexOf('.'));
                        fileNameGeneratedFilesTxt.setText(fileName);
                    } else
                        fileNameGeneratedFilesTxt.setText("");
                    generatedFilesTable.repaint(); // fix for selection not working properly
                }
                if (evt.getClickCount() == 2) generatedFilesTable.editFiles();
            }
        });
        JScrollPane jsp = new JScrollPane(generatedFilesTable);
        jsp.setSize(750, listHeight);
        jsp.setLocation(5, 20);
        jsp.setBorder(BorderFactory.createEtchedBorder());
        generatedFilesTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        generatedFilesTable.getColumnModel().getColumn(1).setPreferredWidth(600);
        this.add(jsp);
        
        Insets insets = new Insets(0,0,0,0);

        openDirectoryBtn.setLocation(105, 0);
        openDirectoryBtn.setSize(50, 15);
        openDirectoryBtn.setMargin(insets);
        openDirectoryBtn.addActionListener(this);
        openDirectoryBtn.setToolTipText(cfgPkg.Text.GFopenDirectoryBtn);
        this.add(openDirectoryBtn);

        generatedFilesCheckBox.setLocation(5, listHeight + 30);
        generatedFilesCheckBox.setSize(80, 20);
        generatedFilesCheckBox.addActionListener(this);
        generatedFilesCheckBox.setToolTipText(cfgPkg.Text.MSGselect);
        this.add(generatedFilesCheckBox);

        addGeneratedFilesButton.setLocation(90, listHeight + 30);
        addGeneratedFilesButton.setSize(45, 20);
        addGeneratedFilesButton.setMargin(insets);
        addGeneratedFilesButton.addActionListener(this);
        addGeneratedFilesButton.setToolTipText(cfgPkg.Text.GFaddFilesButton);
        this.add(addGeneratedFilesButton);

        renameGeneratedFilesButton.setLocation(140, listHeight + 30);
        renameGeneratedFilesButton.setSize(70, 20);
        renameGeneratedFilesButton.setMargin(insets);
        renameGeneratedFilesButton.addActionListener(this);
        renameGeneratedFilesButton.setToolTipText(cfgPkg.Text.GFrenameFilesButton);
        this.add(renameGeneratedFilesButton);

        fileNameGeneratedFilesTxt.setLocation(215, listHeight + 30);
        fileNameGeneratedFilesTxt.setSize(330, 20);
        fileNameGeneratedFilesTxt.setToolTipText(cfgPkg.Text.GFfileNameFilesTxt);
        this.add(fileNameGeneratedFilesTxt);

        editorGeneratedFilesButton.setLocation(550, listHeight + 30);
        editorGeneratedFilesButton.setSize(55, 20);
        editorGeneratedFilesButton.setMargin(insets);
        editorGeneratedFilesButton.addActionListener(this);
        editorGeneratedFilesButton.setToolTipText(cfgPkg.Text.MSGeditSelectedFiles);
        this.add(editorGeneratedFilesButton);

        browserGeneratedFilesButton.setLocation(610, listHeight + 30);
        browserGeneratedFilesButton.setSize(70, 20);
        browserGeneratedFilesButton.setMargin(insets);
        browserGeneratedFilesButton.addActionListener(this);
        browserGeneratedFilesButton.setToolTipText(cfgPkg.Text.MSGbrowseSelectedFiles);
        this.add(browserGeneratedFilesButton);

        deleteGeneratedFilesButton.setLocation(685, listHeight + 30);
        deleteGeneratedFilesButton.setSize(65, 20);
        deleteGeneratedFilesButton.setMargin(insets);
        deleteGeneratedFilesButton.addActionListener(this);
        deleteGeneratedFilesButton.setToolTipText(cfgPkg.Text.GFdeleteFilesButton);
        this.add(deleteGeneratedFilesButton);  
    }
    
    public void setInputDir(File inputDir) {
        this.inputDir = inputDir;
    }
    
    public void setOutputDir(File outputDir) {
        this.outputDir = outputDir;
    }
    
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == openDirectoryBtn)           
            try {
                java.awt.Desktop.getDesktop().open(projectSetupTab.outputDir);
            } catch (IOException e) {
                System.err.println("Attempting to open output directory: " + e);
            }

        else if (evt.getSource() == generatedFilesCheckBox)
            generatedFilesTable.selectAll(generatedFilesCheckBox.isSelected());

        else if (evt.getSource() == addGeneratedFilesButton) {
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(true);
            chooser.setCurrentDirectory(inputDir);
            int response = chooser.showOpenDialog(this);
            if (response == JFileChooser.APPROVE_OPTION) {
                inputDir = chooser.getCurrentDirectory();
                File[] files = chooser.getSelectedFiles();
                for (int i=0; i<files.length; i++) {
                    String srcFileName = files[i].toString();
                    String dstFileName = srcFileName.substring(srcFileName.lastIndexOf(cfgPkg.Const.filesep())+1);
                    dstFileName = outputDir.getAbsolutePath() + cfgPkg.Const.filesep() + dstFileName;
                    
                    if (addFile(dstFileName)) {
                        cfgPkg.Files.copyDirectory(new File(srcFileName), new File(dstFileName), "");
                    }
                }
            }            
        }

        else if (evt.getSource() == renameGeneratedFilesButton) {
            fileNameGeneratedFilesTxt.setText(cfgPkg.Files.nameFile(fileNameGeneratedFilesTxt.getText()));
            generatedFilesTable.renameFile(fileNameGeneratedFilesTxt.getText().trim());
        }

        else if (evt.getSource() == editorGeneratedFilesButton)
            generatedFilesTable.editFiles();

        else if (evt.getSource() == browserGeneratedFilesButton)
            generatedFilesTable.browseFiles();

        else if (evt.getSource() == deleteGeneratedFilesButton) {
            generatedFilesTable.deleteRows();
        }
    }

    public boolean addFile(String fileName) {
        Object[] names = { "", fileName };
        return generatedFilesTable.addRow(names);
    }

    public void resetRows(DefaultTableModel model) {
        generatedFilesTable.resetRows(model);
        resizeRows();
    }

    public void resizeRows() {
        generatedFilesTable.getColumnModel().getColumn(0).setPreferredWidth(150);
        generatedFilesTable.getColumnModel().getColumn(1).setPreferredWidth(600);
    }

    public DefaultTableModel getModel() {
        return (DefaultTableModel)generatedFilesTable.getTableModel();
    }
    
    public Table getTable() {
        return generatedFilesTable;
    }
}
