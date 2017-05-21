package guiPkg;

import java.awt.Dimension;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.html.parser.*;
import javax.swing.text.html.*;

import cfgPkg.*;

public class ProjectSetupTab extends JPanel implements ActionListener {
    // Presentation
    private JTextField   inputDirTxt           = new JTextField();
    private JButton      inputDirButton        = new JButton(cfgPkg.GUI.select);

    private JTextField   outputDirTxt          = new JTextField();
    private JButton      outputDirButton       = new JButton(cfgPkg.GUI.select);

    public  JTextField   snippetCharsTxt       = new JTextField();
    public  JTextField   extensionTxt          = new JTextField();
    public  JTextField   previewCharsTxt       = new JTextField();

    private JTextField   contentTemplateTxt    = new JTextField();
    private JButton      contentTemplateButton = new JButton(cfgPkg.GUI.select);

    private JTextField   indexTemplateTxt      = new JTextField();
    private JButton      indexTemplateButton   = new JButton(cfgPkg.GUI.select);

    private JTextField   editorTxt             = new JTextField();
    private JButton      editorButton          = new JButton(cfgPkg.GUI.select);

    private JCheckBox    nativeEdit            = new JCheckBox(cfgPkg.GUI.nativeEdit);
    private JCheckBox    nativeBrowse          = new JCheckBox(cfgPkg.GUI.nativeBrowse);

    final public  SharedTableModel stmTags     = new SharedTableModel(cfgPkg.GUI.headersTags);
    final public  SharedTableModel stmGen      = new SharedTableModel(cfgPkg.GUI.headersGenerated);

    // Data
    public  File inputDir        = null;
    public  File outputDir       = null;
    public  File contentTemplate = null;
    public  File indexTemplate   = null;
    public  File editor          = null;
    private File editorDir       = null;    

    public ProjectSetupTab(int tabWidth, int tabHeight) {
        this.setLayout(null);
        this.setPreferredSize(new Dimension(tabWidth, tabHeight));
        this.addTabComponents();
    }
	
    private void addTabComponents() {
        JLabel didLbl = new JLabel(cfgPkg.GUI.inputDirectory);
        didLbl.setLocation(20, 20);
        didLbl.setSize(150, 40);
        this.add(didLbl);

        inputDirTxt.setLocation(170, 30);
        inputDirTxt.setSize(600, 20);
        inputDirTxt.setEditable(false);
        inputDirTxt.setToolTipText(cfgPkg.Text.PSinputDirTxt);
        this.add(inputDirTxt);

        inputDirButton.setLocation(780, 30);
        inputDirButton.setSize(90, 20);
        inputDirButton.addActionListener(this);
        inputDirButton.setToolTipText(cfgPkg.Text.PSinputDirButton);
        this.add(inputDirButton);

        JLabel odLbl = new JLabel(cfgPkg.GUI.outputDirectory);
        odLbl.setLocation(20, 50);
        odLbl.setSize(150, 40);
        this.add(odLbl);

        outputDirTxt.setLocation(170, 60);
        outputDirTxt.setSize(600, 20);
        outputDirTxt.setEditable(false);
        outputDirTxt.setToolTipText(cfgPkg.Text.PSoutputDirTxt);
        this.add(outputDirTxt);

        outputDirButton.setLocation(780, 60);
        outputDirButton.setSize(90, 20);
        outputDirButton.addActionListener(this);
        outputDirButton.setToolTipText(cfgPkg.Text.PSoutputDirButton);
        this.add(outputDirButton);

        JLabel snippetLbl = new JLabel(cfgPkg.GUI.snippetCharacters);
        snippetLbl.setLocation(20, 80);
        snippetLbl.setSize(150, 40);
        this.add(snippetLbl);

        snippetCharsTxt.setHorizontalAlignment(JTextField.LEFT);
        snippetCharsTxt.setLocation(170, 90);
        snippetCharsTxt.setSize(600, 20);
        snippetCharsTxt.setEditable(true);
        snippetCharsTxt.setToolTipText(cfgPkg.Text.PSsnippetCharsTxt);
        this.add(snippetCharsTxt);

        JLabel extenstonLbl = new JLabel(cfgPkg.GUI.fileExtension);
        extenstonLbl.setLocation(20, 110);
        extenstonLbl.setSize(150, 40);
        this.add(extenstonLbl);

        extensionTxt.setHorizontalAlignment(JTextField.LEFT);
        extensionTxt.setLocation(170, 120);
        extensionTxt.setSize(600, 20);
        extensionTxt.setEditable(true);
        extensionTxt.setToolTipText(cfgPkg.Text.PSextensionTxt);
        this.add(extensionTxt);

        JLabel previewLbl = new JLabel(cfgPkg.GUI.previewCharacters);
        previewLbl.setLocation(20, 140);
        previewLbl.setSize(150, 40);
        this.add(previewLbl);

        previewCharsTxt.setHorizontalAlignment(JTextField.LEFT);
        previewCharsTxt.setLocation(170, 150);
        previewCharsTxt.setSize(600, 20);
        previewCharsTxt.setEditable(true);
        previewCharsTxt.setToolTipText(cfgPkg.Text.PSpreviewCharsTxt);
        this.add(previewCharsTxt);

        JLabel contentTemplateLbl = new JLabel(cfgPkg.GUI.contentTemplate);
        contentTemplateLbl.setLocation(20, 180);
        contentTemplateLbl.setSize(150, 20);
        this.add(contentTemplateLbl);

        contentTemplateTxt.setLocation(170, 180);
        contentTemplateTxt.setSize(600, 20);
        contentTemplateTxt.setEditable(false);
        contentTemplateTxt.setToolTipText(cfgPkg.Text.PScontentTemplateTxt);
        this.add(contentTemplateTxt);

        contentTemplateButton.setLocation(780, 180);
        contentTemplateButton.setSize(90, 20);
        contentTemplateButton.addActionListener(this);
        contentTemplateButton.setToolTipText(cfgPkg.Text.PScontentTemplateButton);
        this.add(contentTemplateButton);

        JLabel indexTemplateLbl = new JLabel(cfgPkg.GUI.indexTemplate);
        indexTemplateLbl.setLocation(20, 210);
        indexTemplateLbl.setSize(150, 20);
        this.add(indexTemplateLbl);

        indexTemplateTxt.setLocation(170, 210);
        indexTemplateTxt.setSize(600, 20);
        indexTemplateTxt.setEditable(false);
        indexTemplateTxt.setToolTipText(cfgPkg.Text.PSindexTemplateTxt);
        this.add(indexTemplateTxt);

        indexTemplateButton.setLocation(780, 210);
        indexTemplateButton.setSize(90, 20);
        indexTemplateButton.addActionListener(this);
        indexTemplateButton.setToolTipText(cfgPkg.Text.PSindexTemplateButton);
        this.add(indexTemplateButton);

        JLabel editorLbl = new JLabel(cfgPkg.GUI.editor);
        editorLbl.setLocation(20, 260);
        editorLbl.setSize(150, 40);
        this.add(editorLbl);

        editorTxt.setLocation(170, 270);
        editorTxt.setSize(600, 20);
        editorTxt.setEditable(false);
        editorTxt.setToolTipText(cfgPkg.Text.PSeditorTxt);
        this.add(editorTxt);

        editorButton.setLocation(780, 270);
        editorButton.setSize(90, 20);
        editorButton.addActionListener(this);
        editorButton.setToolTipText(cfgPkg.Text.PSeditorButton);
        this.add(editorButton);

        nativeEdit.setLocation(170, 310);
        nativeEdit.setSize(400, 20);
        nativeEdit.addActionListener(this);
        nativeEdit.setToolTipText(cfgPkg.Text.PSnativeEditor);
        this.add(nativeEdit);

        nativeBrowse.setLocation(170, 340);
        nativeBrowse.setSize(400, 20);
        nativeBrowse.addActionListener(this);
        nativeBrowse.setToolTipText(cfgPkg.Text.PSnativeBrowser);
        this.add(nativeBrowse);
    }
    
    private File getDir(File dir, boolean dirOnly) {
        JFileChooser chooser;
        if (dir == null)
            chooser = new JFileChooser();
        else
            chooser = new JFileChooser(dir);

        if (dirOnly) chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int response = chooser.showOpenDialog(this);
        if (response == JFileChooser.APPROVE_OPTION)
            return chooser.getSelectedFile();
        else
            return dir;
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == inputDirButton) {
            inputDir = getDir(inputDir, true);
            if (inputDir != null)
                inputDirTxt.setText(inputDir.getPath());
        }

        else if (evt.getSource() == outputDirButton) {
            outputDir = getDir(outputDir, true);
            if (outputDir != null) {
                outputDirTxt.setText(outputDir.getPath());
                cfgPkg.Files.copyDirectory(new File("includes"), new File(outputDir.getPath() + cfgPkg.Const.filesep() + "includes"), "");
            }
        }

        else if (evt.getSource() == contentTemplateButton) {
            contentTemplate = getDir(contentTemplate, false);
            if (contentTemplate != null) {
                contentTemplateTxt.setText(contentTemplate.getPath());
                cfgPkg.Files.copyDirectory(new File(contentTemplate.getParent()), new File(outputDir.getPath()), ".css");
                File images = new File(contentTemplate.getParent() + cfgPkg.Const.filesep() + "images");
                if (images.exists())
                    cfgPkg.Files.copyDirectory(images, new File(outputDir.getPath() + cfgPkg.Const.filesep() + "images"), "");
            }
        }

        else if (evt.getSource() == indexTemplateButton) {
            indexTemplate = getDir(indexTemplate, false);
            if (indexTemplate != null) {
                indexTemplateTxt.setText(indexTemplate.getPath());
                cfgPkg.Files.copyDirectory(new File(contentTemplate.getParent()), new File(outputDir.getPath()), ".css");
                File images = new File(contentTemplate.getParent() + cfgPkg.Const.filesep() + "images");
                if (images.exists())
                    cfgPkg.Files.copyDirectory(images, new File(outputDir.getPath() + cfgPkg.Const.filesep() + "images"), "");
            }
        }

        else if (evt.getSource() == editorButton) {
            editor = getDir(editor, false);
            if (editor != null) {
                editorTxt.setText(editor.getAbsolutePath());
                editorDir = editor.getParentFile();
                
                ProgramConfigurationFile pcf = new ProgramConfigurationFile();
                pcf.editor       = editorTxt.getText().trim();
                pcf.nativeEdit   = nativeEdit.isSelected();
                pcf.nativeBrowse = nativeBrowse.isSelected();
                cfgPkg.Files.saveProgramFile(pcf);
            }
        }

        else if (evt.getSource() == nativeEdit) {
            ProgramConfigurationFile pcf = new ProgramConfigurationFile();
            pcf.editor       = editorTxt.getText().trim();
            pcf.nativeEdit   = nativeEdit.isSelected();
            pcf.nativeBrowse = nativeBrowse.isSelected();
            cfgPkg.Files.saveProgramFile(pcf);
        }

        else if (evt.getSource() == nativeBrowse) {
            ProgramConfigurationFile pcf = new ProgramConfigurationFile();
            pcf.editor       = editorTxt.getText().trim();
            pcf.nativeEdit   = nativeEdit.isSelected();
            pcf.nativeBrowse = nativeBrowse.isSelected();
            cfgPkg.Files.saveProgramFile(pcf);
        }
    }
    
    public void setInputDir(String inputDir) {
        inputDirTxt.setText(inputDir);
        this.inputDir = new File(inputDir);
    }
    
    public String getInputDir() {
        return inputDirTxt.getText().trim();
    }

    public void setOutputDir(String outputDir) {
        outputDirTxt.setText(outputDir);
        this.outputDir = new File(outputDir);
    }
    
    public String getOutputDir() {
        return outputDirTxt.getText().trim();
    }

    public void setSnippetChars(String snippetChars) {
        snippetCharsTxt.setText(snippetChars);
    }
    
    public String getSnippetChars() {
        return snippetCharsTxt.getText().trim();
    }

    public void setExtension(String extension) {
        extensionTxt.setText(extension);
    }
    
    public String getExtension() {
        return extensionTxt.getText().trim();
    }

    public void setPreviewChars(String indexPreview) {
        previewCharsTxt.setText(indexPreview);
    }
    
    public String getPreviewChars() {
        return previewCharsTxt.getText().trim();
    }

    public void setContentTemplate(String contentTemplate) {
        contentTemplateTxt.setText(contentTemplate);
        this.contentTemplate = new File(contentTemplate);
    }
    
    public String getContentTemplate() {
        return contentTemplateTxt.getText().trim();
    }
    
    public void setIndexTemplate(String indexTemplate) {
        indexTemplateTxt.setText(indexTemplate);
        this.indexTemplate = new File(indexTemplate);
    }
    
    public String getIndexTemplate() {
        return indexTemplateTxt.getText().trim();
    }
    
    public void setEditor(String editor) {
        editorTxt.setText(editor);
        this.editor = new File(editor);
    }
    
    public String getEditor() {
        return editorTxt.getText().trim();
    }
    
    public boolean outputDirCheck() {
        if (outputDir == null) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.PSoutputDirButton, cfgPkg.Text.MSGprojectSetup, JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (!outputDir.isDirectory()) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGoutputDirectoryInvalid, cfgPkg.Text.MSGprojectSetup, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    
    public boolean contentTemplateCheck() {
        if (contentTemplate == null) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.PScontentTemplateButton, cfgPkg.Text.MSGprojectSetup, JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (!contentTemplate.isFile()) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGcontentTemplateInvalid, cfgPkg.Text.MSGprojectSetup, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean indexTemplateCheck() {
        if (indexTemplate == null) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.PSindexTemplateButton, cfgPkg.Text.MSGprojectSetup, JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (!indexTemplate.isFile()) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGindexTemplateInvalid, cfgPkg.Text.MSGprojectSetup, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean editorCheck() {
        if (editor == null) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.PSeditorButton, cfgPkg.Text.MSGprojectSetup, JOptionPane.ERROR_MESSAGE);
            return false;
        } else if (!editor.isFile()) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGeditorInvalid, cfgPkg.Text.MSGprojectSetup, JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void setDefaults(ProgramConfigurationFile pcf) {
        //pcf.expertMode;
        editorTxt.setText(pcf.editor);
        editor = new File(pcf.editor);
        nativeEdit.setSelected(pcf.nativeEdit);
        nativeBrowse.setSelected(pcf.nativeBrowse);
    }
}
