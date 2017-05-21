package guiPkg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.html.*;
import javax.swing.text.html.parser.*;

import cfgPkg.*;

public class BatchProcessTab extends JPanel implements ActionListener {
    // Presentation
    private Table        tagTable;
    public  GeneratedFilesPanel gfp;

    private FileList     fileList;
    private JCheckBox    inputCheckBox             = new JCheckBox(cfgPkg.GUI.selectAll);
    private JButton      selectInputButton         = new JButton(cfgPkg.GUI.select);
    private JButton      editorInputButton         = new JButton(cfgPkg.GUI.editor);
    private JButton      stripInputButton          = new JButton(cfgPkg.GUI.strip);
    private JButton      removeInputButton         = new JButton(cfgPkg.GUI.remove);

    private JButton      generateContentButton     = new JButton("<html><h3>" + cfgPkg.GUI.generateContentPages + "</h3></html>");
    private JRadioButton indexLinks                = new JRadioButton(cfgPkg.GUI.indexLinks);
    private JRadioButton indexAndTextLinks         = new JRadioButton(cfgPkg.GUI.indexAndTextLinks);
    private JTextField   previewTag                = new JTextField();
    private JButton      generateIndexButton       = new JButton("<html><h3>" + cfgPkg.GUI.generateIndexPage + "</h3></html>");

    private ProjectSetupTab projectSetupTab;
    private File         inputDir                  = new File("");

    public BatchProcessTab(int tabWidth, int tabHeight, ProjectSetupTab projectSetupTab) {
        this.projectSetupTab = projectSetupTab;
        tagTable             = new Table(projectSetupTab);
        fileList             = new FileList(projectSetupTab);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(tabWidth, tabHeight));
        gfp = new GeneratedFilesPanel(350, projectSetupTab);
        this.addTabComponents();
    }
	
    private void addTabComponents() {
        addTagAndTokenComponents();
        gfp.setLocation(415, 445);
        gfp.setSize(755, 400);
        this.add(gfp);		
        addInputComponents();
        addGenerateComponents();
    }

    private void addTagAndTokenComponents() {
        JLabel tagLbl =  new JLabel(cfgPkg.GUI.tagsLabel);
        tagLbl.setLocation(425, 0);
        tagLbl.setSize(240, 20);
        this.add(tagLbl);

        int[] blankChecks = {0, 1, 3};
        tagTable.initialise(blankChecks, projectSetupTab.stmTags, true);
        
        JScrollPane jsp = new JScrollPane(tagTable);
        jsp.setSize(750, 420);
        jsp.setLocation(420, 20);
        tagTable.getColumnModel().getColumn(0).setPreferredWidth(155);
        tagTable.getColumnModel().getColumn(1).setPreferredWidth(175);
        tagTable.getColumnModel().getColumn(2).setPreferredWidth(175);
        tagTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        this.add(jsp);        
    }
		
    private void addInputComponents() {
        JLabel inputLbl =  new JLabel(cfgPkg.GUI.inputFiles);
        inputLbl.setLocation(5, 0);
        inputLbl.setSize(200, 20);
        this.add(inputLbl);

        fileList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) fileList.editFiles();
            }
        });
        JScrollPane jsp = new JScrollPane(fileList);
        jsp.setSize(410, 570);
        jsp.setLocation(5, 20);
        this.add(jsp);

        inputCheckBox.setLocation(5, 600);
        inputCheckBox.setSize(80, 20);
        inputCheckBox.addActionListener(this);
        inputCheckBox.setToolTipText(cfgPkg.Text.MSGselect);
        this.add(inputCheckBox);

        selectInputButton.setLocation(95, 600);
        selectInputButton.setSize(75, 20);
        selectInputButton.addActionListener(this);
        selectInputButton.setToolTipText(cfgPkg.Text.MSGselectInputFiles);
        this.add(selectInputButton);

        editorInputButton.setLocation(175, 600);
        editorInputButton.setSize(75, 20);
        editorInputButton.addActionListener(this);
        editorInputButton.setToolTipText(cfgPkg.Text.MSGeditSelectedFiles);
        this.add(editorInputButton);

        stripInputButton.setLocation(255, 600);
        stripInputButton.setSize(75, 20);
        stripInputButton.addActionListener(this);
        stripInputButton.setToolTipText(cfgPkg.Text.BPstripInputButton);
        this.add(stripInputButton);

        removeInputButton.setLocation(335, 600);
        removeInputButton.setSize(80, 20);
        removeInputButton.addActionListener(this);
        removeInputButton.setToolTipText(cfgPkg.Text.MSGremoveListItems);
        this.add(removeInputButton);
    }

    private void addGenerateComponents() {
        JLabel contentLbl = new JLabel("<html><p align=center>" + cfgPkg.GUI.buildContentLabel + "</p></html>");
        contentLbl.setLocation(5, 650);
        contentLbl.setSize(410, 45);
        this.add(contentLbl);

        generateContentButton.setForeground(Color.RED);
        generateContentButton.setLocation(5, 705);
        generateContentButton.setSize(410, 20);
        generateContentButton.addActionListener(this);
        generateContentButton.setToolTipText(cfgPkg.Text.BPgenerateContentButton);
        this.add(generateContentButton);

        JLabel indexLbl = new JLabel("<html><p align=center>" + cfgPkg.GUI.buildIndexLabel + "</p></html>");
        indexLbl.setLocation(5, 745);
        indexLbl.setSize(410, 45);
        this.add(indexLbl);

        indexLinks.setLocation(5, 795);
        indexLinks.setSize(80, 20);
        indexLinks.addActionListener(this);
        indexLinks.setToolTipText(cfgPkg.Text.BPindexLinks);
        this.add(indexLinks);

        indexAndTextLinks.setLocation(85, 795);
        indexAndTextLinks.setSize(110, 20);
        indexAndTextLinks.setSelected(true);
        indexAndTextLinks.addActionListener(this);
        indexAndTextLinks.setToolTipText(cfgPkg.Text.BPindexAndTextLinks);
        this.add(indexAndTextLinks);

        JLabel tagLbl = new JLabel(cfgPkg.GUI.tag);
        tagLbl.setLocation(195, 795);
        tagLbl.setSize(20, 20);
        this.add(tagLbl);

        previewTag.setLocation(220, 795);
        previewTag.setSize(195, 20);
        previewTag.setToolTipText(cfgPkg.Text.BPpreviewTag);
        this.add(previewTag);

        ButtonGroup indexGroup = new ButtonGroup();
        indexGroup.add(indexLinks);
        indexGroup.add(indexAndTextLinks);

        generateIndexButton.setForeground(Color.RED);
        generateIndexButton.setLocation(5, 825);
        generateIndexButton.setSize(410, 20);
        generateIndexButton.addActionListener(this);
        generateIndexButton.setToolTipText(cfgPkg.Text.BPgenerateIndexButton);
        this.add(generateIndexButton);		
    }
    
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == inputCheckBox)
            fileList.selectAll(inputCheckBox.isSelected());

        else if (evt.getSource() == selectInputButton) {
            JFileChooser chooser = new JFileChooser(inputDir);
            chooser.setMultiSelectionEnabled(true);
            int response = chooser.showOpenDialog(this);
            if (response == JFileChooser.APPROVE_OPTION) {
                inputDir = chooser.getCurrentDirectory();
                fileList.addFiles(chooser.getSelectedFiles());
            }
        }

        else if (evt.getSource() == editorInputButton)
            fileList.editFiles();
        
        else if (evt.getSource() == stripInputButton)
            stripFiles();
        
        else if (evt.getSource() == removeInputButton)
            fileList.removeFiles();
        
        else if (evt.getSource() == generateContentButton)
            generateContent();
        
        else if (evt.getSource() == indexAndTextLinks)
            previewTag.setEditable(indexAndTextLinks.isSelected());
        else if (evt.getSource() == indexLinks)
            previewTag.setEditable(indexAndTextLinks.isSelected());
        
        else if (evt.getSource() == generateIndexButton)
            generateIndex();
    }
    
    private void generateContent() {
        if (fileList.getSelectedIndices().length == 0) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGselectInputFiles, cfgPkg.Text.MSGselectionError, JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!projectSetupTab.outputDirCheck())       return;
        if (!projectSetupTab.contentTemplateCheck()) return;

        if (tagTable.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.BPselectTags, cfgPkg.Text.MSGselectionError, JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.BPgenerateContentButton, cfgPkg.Text.MSGconfirmContent, JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.NO_OPTION) return;

        // Read in the template
        String cpyFileName = projectSetupTab.contentTemplate.getAbsoluteFile().toString();
        String fileTxt     = cfgPkg.Files.readFile(cpyFileName, false);

        int filenamesChoice = 
                JOptionPane.showConfirmDialog(null, cfgPkg.Text.MSGcontentNames, cfgPkg.Text.MSGconfirmNameFiles, JOptionPane.YES_NO_OPTION);

        // start - loop through all selected Input Files
        int[] selected = fileList.getSelectedIndices();
        for (int i=0; i<selected.length; i++) {
            String srcFileName = fileList.getModel().getElementAt(selected[i]).toString();
            String dstFileName = srcFileName.substring(srcFileName.lastIndexOf(cfgPkg.Const.filesep())+1);
            if (dstFileName.indexOf(".strip") != -1)
                dstFileName = dstFileName.substring(0, dstFileName.indexOf(".strip"));
            int dotPos = dstFileName.lastIndexOf('.');
            
            // new code
            if (dotPos != -1)
                dstFileName = dstFileName.substring(0, dotPos);
            String title = "";
            if (filenamesChoice == JOptionPane.NO_OPTION)
                title = dstFileName;
            else {
                title = JOptionPane.showInputDialog(null, cfgPkg.Text.MSGcontentName, dstFileName);
                if (title == null || title.equals("")) {
                    title = dstFileName;
                }
            }
            dstFileName = title + "." + projectSetupTab.extensionTxt.getText();

            dstFileName = dstFileName.trim().replace(" ", "-");
            dstFileName = cfgPkg.Files.nameFile(dstFileName); // new code
            String outputDir = projectSetupTab.outputDir.getAbsolutePath();
            if (outputDir.lastIndexOf(cfgPkg.Const.filesep()) == (outputDir.length()-1))
                dstFileName = outputDir + dstFileName;
            else
                dstFileName = outputDir + cfgPkg.Const.filesep() + dstFileName;
            if (!gfp.addFile(dstFileName)) // add generated files to the generated files panel
                continue;

            String newTxt  = fileTxt; // make a copy of the template

            // start - loop through, collect all token text and replace in the template copy
            int[] selTags = tagTable.getSelectedRows();
            for (int j=0; j<selTags.length; j++) {
                String openTag  = tagTable.getValueAt(selTags[j], 1).toString();
                String closeTag = tagTable.getValueAt(selTags[j], 2).toString();
                String token    = tagTable.getValueAt(selTags[j], 3).toString();

                if (openTag.equals("%article-no-title%") || openTag.equals("%article-with-title%")) {
                    if (cfgPkg.Const.liteVersion) {
                        JOptionPane.showMessageDialog(null, cfgPkg.Text.liteMessage, cfgPkg.Text.liteMessageTitle, JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    
                    String tokenTxt = cfgPkg.Files.readFile(srcFileName, false);
                    if (openTag.equals("%article-with-title%")) {
                        int pos = tokenTxt.indexOf(cfgPkg.Const.linesep());
                        title    = tokenTxt.substring(0, pos);
                        tokenTxt = tokenTxt.substring(pos);
                    }

                    String paragraphSep = cfgPkg.Const.linesep() + cfgPkg.Const.linesep();
                    tokenTxt = tokenTxt.replace(paragraphSep, "<br>");
                    String[] tempArray  = tokenTxt.split("<br>");
                    
                    tokenTxt = "";
                    for (int k=0; k<tempArray.length; k++)
                    {
                        if (!tempArray[k].trim().equals("")) {
                            //System.out.println("[" + tempArray[i].trim() + "]");
                            tokenTxt = tokenTxt + "<p>" + tempArray[k] + "</p>" + cfgPkg.Const.linesep() + cfgPkg.Const.linesep();
                        }
                    }
                    
                    newTxt = newTxt.replace("%title%", title); // copy title to the title token
                    
                    newTxt = newTxt.replaceAll("(<p.*?class=\"authortext\".*?>.*?%author%.*?</p>)", "");
                    newTxt = newTxt.replaceAll("(<div.*?class=\"authortext\".*?>.*?%author%.*?</div>)", "");
                    newTxt = newTxt.replace("%author%", "");
                                        
                    newTxt = newTxt.replace(token, tokenTxt);  // copy all text to the article token
                    
                    newTxt = newTxt.replaceAll("(<p.*?class=\"abouttext\".*?>.*?%about%.*?</p>)", "");
                    newTxt = newTxt.replaceAll("(<div.*?class=\"abouttext\".*?>.*?%about%.*?</div>)", "");
                    newTxt = newTxt.replace("%about%", "");
                } else {
                    String tokenTxt = "";
                    if (openTag.indexOf("RegExp=") == 0) {
                        Pattern tags = Pattern.compile(openTag.substring(openTag.indexOf("=")+1), Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                        Matcher matcher = tags.matcher(cfgPkg.Files.readFile(srcFileName, false));
                        while (matcher.find()) {
                            //System.out.println(matcher.group(0) + " " + matcher.group(1));
                            tokenTxt = matcher.group(1);
                            break;
                        }
                    } else {
                        tokenTxt = cfgPkg.Files.readFileForTag(srcFileName, openTag, closeTag);
                    }
                    newTxt = newTxt.replace(token, tokenTxt); // make the substitution
                }
            } // end - loop through, collect all token text and replace in the template copy

            cfgPkg.Files.writeFile(dstFileName, newTxt);
        } // end - loop through all selected Input Files
    }

    private void generateIndex() {
        if (gfp.getTable().getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGselectGeneratedFiles, cfgPkg.Text.MSGselectionError, JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!projectSetupTab.outputDirCheck())     return;
        if (!projectSetupTab.indexTemplateCheck()) return;

        int indexGen = JOptionPane.showConfirmDialog(null, cfgPkg.Text.BPgenerateIndexButton, cfgPkg.Text.MSGconfirmIndex, JOptionPane.YES_NO_OPTION);
        if (indexGen == JOptionPane.NO_OPTION) return;

        int snippetGen = JOptionPane.showConfirmDialog(null, cfgPkg.Text.MSGgenerateSnippets, cfgPkg.Text.MSGconfirmGrouping, JOptionPane.YES_NO_OPTION);
        if (snippetGen == JOptionPane.YES_OPTION || indexAndTextLinks.isSelected()) {
            if (previewTag.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGpreviewTag, cfgPkg.Text.MSGinputError, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int snippetNum = 0;
        if (snippetGen == JOptionPane.YES_OPTION) {
            try {
                snippetNum = Integer.parseInt(projectSetupTab.snippetCharsTxt.getText().trim());
            } catch(java.lang.NumberFormatException exp) {
                JOptionPane.showMessageDialog(null, exp.toString()+cfgPkg.Text.MSGsnippetChar, cfgPkg.Text.MSGnumericError, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        int previewNum = 0;
        if (indexAndTextLinks.isSelected()) {
            try {
                previewNum = Integer.parseInt(projectSetupTab.previewCharsTxt.getText().trim());
            } catch(java.lang.NumberFormatException exp) {
                JOptionPane.showMessageDialog(null, exp.toString()+cfgPkg.Text.MSGpreviewChar, cfgPkg.Text.MSGnumericError, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        String dstFileName = 
            JOptionPane.showInputDialog(null, cfgPkg.Text.MSGindexName, "index." + projectSetupTab.extensionTxt.getText()).trim();
        if (dstFileName == null)
            return;
        else if (dstFileName.equals("")) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGfilenameBlank, cfgPkg.Text.MSGinputError, JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int dotPos = dstFileName.lastIndexOf('.');
        String indexName = "";
        if (dotPos != -1) {
            indexName   = dstFileName.substring(0, dotPos);
        } else {
            indexName   = dstFileName;
            dstFileName = dstFileName + "." + projectSetupTab.extensionTxt.getText();
        }

        boolean exists = false;
        for (int i=0; i<gfp.getTable().getRowCount(); i++)
            if (gfp.getTable().getValueAt(i, 0).equals(indexName)) {
                if (cfgPkg.Const.liteVersion) {
                    JOptionPane.showMessageDialog(null, indexName + cfgPkg.Text.MSGalreadyEntered, cfgPkg.Text.MSGduplicateEntry, JOptionPane.ERROR_MESSAGE);
                    return;
                }
                exists = true;
            }
        
        int indexAppend = JOptionPane.NO_OPTION;
        if (exists) {
            indexAppend = JOptionPane.showConfirmDialog(null, cfgPkg.Text.MSGindexAppend, cfgPkg.Text.MSGindexExists, JOptionPane.YES_NO_OPTION);
            if (indexAppend == JOptionPane.NO_OPTION) return;
        }

        String newSnippetFileName = indexName + "_" + cfgPkg.Const.phpSnippetFile;
        // Replace the default snippet file name in cfgPkg.Const.phpSnippetFile with the new name
        String codeTxt = cfgPkg.Const.phpSnippetCode.replace(cfgPkg.Const.phpSnippetFile, newSnippetFileName);

        String outputDirPath = projectSetupTab.outputDir.getAbsolutePath();
        if (outputDirPath.lastIndexOf(cfgPkg.Const.filesep()) != (outputDirPath.length()-1))
            outputDirPath = outputDirPath + cfgPkg.Const.filesep();
        String fileTxt = "";
        if (indexAppend == JOptionPane.NO_OPTION) {
            // Read in the template
            fileTxt = cfgPkg.Files.readFile(projectSetupTab.indexTemplate.getAbsoluteFile().toString(), false);
        } else {
            // Read in the existing index
            fileTxt = cfgPkg.Files.readFile(outputDirPath+dstFileName, false);            
        }

        int[] selected = gfp.getTable().getSelectedRows();
        String previewTxt = "";
        String shareTxt   = "";
        String snippetTxt = "";

        // start - loop through all selected Generated Files
        for (int i=0; i<selected.length; i++) {
            String srcFileName = gfp.getTable().getValueAt(selected[i], 1).toString();
            String linkTxt     = formatChildLinks(srcFileName, true); // get the link for this file

            // Add the link for this file to previewTxt and shareTxt
            previewTxt = previewTxt + linkTxt;
            shareTxt   = shareTxt + linkTxt + "<br>" + cfgPkg.Const.linesep();

            String getTxt = "";
            if (snippetGen == JOptionPane.YES_OPTION || indexAndTextLinks.isSelected()) {
                String openTag = previewTag.getText().trim();
                getTxt = cfgPkg.Files.readFile(srcFileName, false);
                
                String bodyStart = "<body";
                int checkBody = getTxt.indexOf(bodyStart);
                if (checkBody == -1)
                    bodyStart = "<BODY";
                String bodyEnd = "</body>";
                checkBody = getTxt.indexOf(bodyEnd);
                if (checkBody == -1)
                    bodyEnd = "</BODY>";

                getTxt = cfgPkg.Files.readFileForTag(srcFileName, bodyStart, bodyEnd);
                File tmp = null;
                try {
                    tmp = File.createTempFile("temp", "txt");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                cfgPkg.Files.writeFile(tmp.getAbsolutePath(), getTxt);
                getTxt = "<body>" + cfgPkg.Files.readFileForTag(tmp.getAbsolutePath(), openTag, "</body>") + "</body>";
                cfgPkg.Files.writeFile(tmp.getAbsolutePath(), getTxt);

                getTxt = cfgPkg.Files.parseHTML(tmp.getAbsolutePath(), openTag, true); // get the text from tmp without any tags
            }

            if (snippetGen == JOptionPane.YES_OPTION) {
                // Set the index name for the file
                gfp.getTable().setValueAt(indexName, selected[i], 0);
                String snippet = formatTextBite(getTxt, snippetNum);
                snippet        = snippet.replace(cfgPkg.Const.linesep(), "") + " ";
                snippetTxt     = snippetTxt + snippet + formatChildLinks(srcFileName, false) + cfgPkg.Const.linesep();

                String allTxt  = cfgPkg.Files.readFile(srcFileName, false);
                // Special replacement for predefined cfgPkg.Const.phpSnippetToken token
                allTxt = allTxt.replace(cfgPkg.Const.phpSnippetToken, codeTxt);                
                cfgPkg.Files.writeFile(srcFileName, allTxt);
            }

            if (indexAndTextLinks.isSelected()) { // add a preview from this file
                previewTxt = previewTxt + "<br>" + formatTextBite(getTxt, previewNum);
            }
            previewTxt = previewTxt + "<br><br>" + cfgPkg.Const.linesep();
        } // end - loop through all selected Input Files

        if (cfgPkg.Const.liteVersion) {
            if (fileTxt.indexOf(cfgPkg.Const.childLinksToken) != -1) {
                previewTxt = cfgPkg.Const.childLinksStart + cfgPkg.Const.linesep() + previewTxt + cfgPkg.Const.childLinksEnd;
                fileTxt = fileTxt.replace(cfgPkg.Const.childLinksToken, previewTxt);
            }
        } else {
            if (indexAppend == JOptionPane.NO_OPTION) {
                if (fileTxt.indexOf(cfgPkg.Const.childLinksToken) != -1) {
                    previewTxt = cfgPkg.Const.childLinksStart + cfgPkg.Const.linesep() + previewTxt + cfgPkg.Const.childLinksEnd;
                    fileTxt = fileTxt.replace(cfgPkg.Const.childLinksToken, previewTxt);
                }
            } else if (fileTxt.indexOf(cfgPkg.Const.childLinksEnd) != -1) {
                // Take the preview text and append it to the existing preview text
                fileTxt = fileTxt.replace(cfgPkg.Const.childLinksEnd, previewTxt+cfgPkg.Const.childLinksEnd);
            }
        }

        // Special replacement for predefined cfgPkg.Const.phpSnippetToken token
        fileTxt = fileTxt.replace(cfgPkg.Const.phpSnippetToken, codeTxt);
        cfgPkg.Files.writeFile(outputDirPath+dstFileName, fileTxt);
        if (indexAppend == JOptionPane.NO_OPTION)
            gfp.addFile(outputDirPath+dstFileName); // add generated index file to the generated files panel

        if (snippetGen == JOptionPane.YES_OPTION) {
            File file = new File(outputDirPath+newSnippetFileName);
            if (indexAppend == JOptionPane.YES_OPTION) {
                // Take the snippet text and append it to the existing snippet text
                if (file.exists())
                    snippetTxt = cfgPkg.Files.readFile(file.getAbsolutePath(), false) + snippetTxt;
            }
            // Take the snippet text and write it to the snippet file
            cfgPkg.Files.writeFile(file.getAbsolutePath(), snippetTxt);
        }
        
        // Append the share links to files already from the same index here
        if (indexAppend == JOptionPane.YES_OPTION) {
            for (int i=0; i<gfp.getTable().getRowCount(); i++) {
                if (gfp.getTable().getValueAt(i, 0).equals(indexName)) {
                    String srcFileName = gfp.getTable().getValueAt(i, 1).toString();
                    String allTxt      = cfgPkg.Files.readFile(srcFileName, false);

                    if (allTxt.indexOf(cfgPkg.Const.shareLinksEnd) != -1) {
                        // Take the share links and append it to the existing share links
                        allTxt = allTxt.replace(cfgPkg.Const.shareLinksEnd, shareTxt+cfgPkg.Const.shareLinksEnd);
                        cfgPkg.Files.writeFile(srcFileName, allTxt);
                    }
                }
            }
        }
        
        // Do the share links file here
        String shareFileLinksName = indexName + "_" + cfgPkg.Const.shareFileLinksFile;
        if (!cfgPkg.Const.liteVersion) {
            if (indexAppend == JOptionPane.NO_OPTION) {
                // Take the shareTxt and write it to the share file links file
                cfgPkg.Files.writeFile(outputDirPath+shareFileLinksName, shareTxt);
            } else {
                // Take the shareTxt and append it to the existing share file links file
                shareTxt = cfgPkg.Files.readFile(outputDirPath+shareFileLinksName, false) + shareTxt;
                cfgPkg.Files.writeFile(outputDirPath+shareFileLinksName, shareTxt);
            }
        }

        // Do the share links for the selected files here
        String newShareTxt       = cfgPkg.Const.shareLinksStart + cfgPkg.Const.linesep() + shareTxt + cfgPkg.Const.shareLinksEnd;
        String shareFileLinksTxt = "<?php include(\"" + shareFileLinksName  + "\"); ?>";
        for (int i=0; i<selected.length; i++) {
            // Set up the file name, open file for reading and read all the contents into allTxt
            String srcFileName = gfp.getTable().getValueAt(selected[i],1).toString();
            String allTxt      = cfgPkg.Files.readFile(srcFileName, false);
            boolean change     = false;
            
            if (cfgPkg.Const.liteVersion) {
                if (allTxt.indexOf(cfgPkg.Const.shareLinksToken) != -1) {
                    allTxt = allTxt.replace(cfgPkg.Const.shareLinksToken, newShareTxt);
                    change = true;
                }
            } else {
                if (indexAppend == JOptionPane.NO_OPTION) {
                    if (allTxt.indexOf(cfgPkg.Const.shareFileLinksToken) != -1) {
                        String newShareFileLinksTxt  = cfgPkg.Const.shareFileLinksStart + cfgPkg.Const.linesep() +
                                shareFileLinksTxt + cfgPkg.Const.linesep() + cfgPkg.Const.shareFileLinksEnd;
                        allTxt = allTxt.replace(cfgPkg.Const.shareFileLinksToken, newShareFileLinksTxt);
                        change = true;
                    } else if (allTxt.indexOf(cfgPkg.Const.shareLinksToken) != -1) {
                        allTxt = allTxt.replace(cfgPkg.Const.shareLinksToken, newShareTxt);
                        change = true;
                    }
                } else {
                    if (allTxt.indexOf(cfgPkg.Const.shareFileLinksEnd) != -1) {
                        String newShareFileLinksTxt  = shareFileLinksTxt + cfgPkg.Const.linesep() + cfgPkg.Const.shareFileLinksEnd;
                        allTxt = allTxt.replace(cfgPkg.Const.shareFileLinksEnd, newShareFileLinksTxt);
                        change = true;
                    } else if (allTxt.indexOf(cfgPkg.Const.shareLinksEnd) != -1) {
                        // Take the share links and append it to the existing share links
                        allTxt = allTxt.replace(cfgPkg.Const.shareLinksEnd, shareTxt+cfgPkg.Const.shareLinksEnd);
                        change = true;
                    } else if (allTxt.indexOf(cfgPkg.Const.shareFileLinksToken) != -1) {
                        String newShareFileLinksTxt  = cfgPkg.Const.shareFileLinksStart + cfgPkg.Const.linesep() +
                                shareFileLinksTxt + cfgPkg.Const.linesep() + cfgPkg.Const.shareFileLinksEnd;
                        allTxt = allTxt.replace(cfgPkg.Const.shareFileLinksToken, newShareFileLinksTxt);
                        change = true;
                    } else if (allTxt.indexOf(cfgPkg.Const.shareLinksToken) != -1) {
                        allTxt = allTxt.replace(cfgPkg.Const.shareLinksToken, newShareTxt);
                        change = true;
                    }
                }
            }

            if (change) cfgPkg.Files.writeFile(srcFileName, allTxt);
        }
    }

    private String formatChildLinks(String srcFileName, boolean preview) {
        String previewTxt  = "";
        String displayName = "More";
        File temp = new File(srcFileName);
        if (preview) {
            int pos = temp.getName().lastIndexOf('.');
            displayName = temp.getName();
            if (pos != -1)
                displayName = displayName.substring(0, pos);
            displayName = displayName.replace("-", " ");
            displayName = displayName.replace("_", " ");
        }
        previewTxt  = previewTxt + "<a href=\"" + temp.getName() + "\">" + displayName + "</a>";
        
        return previewTxt;
    }
    
    private String formatTextBite(String getTxt, int biteNum) {
        if (getTxt.length() == 0 || biteNum == 0) {
            return "...";
        } else if (getTxt.length() < biteNum) {
            int temp = getTxt.lastIndexOf(" ");
            if (temp != -1)
                biteNum = temp;
            else
                biteNum = getTxt.length();
        } else {
            int pos = getTxt.substring(biteNum).indexOf(" ");
            if (pos == -1) {
                int temp = getTxt.lastIndexOf(" ");
                if (temp != -1) biteNum = temp;
            } else
                biteNum = biteNum + pos;
        }

        return getTxt.substring(0, biteNum) + " ...";
    }
    
    private void stripFiles() {
        if (cfgPkg.Const.liteVersion) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.liteMessage, cfgPkg.Text.liteMessageTitle, JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (fileList.getSelectedIndices().length == 0) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGselectInputFiles, cfgPkg.Text.MSGselectionError, JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int stripType = JOptionPane.showOptionDialog(null, cfgPkg.Text.MSGstrip, cfgPkg.Text.MSGconfirmStrip,
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, cfgPkg.GUI.stripOptions, cfgPkg.GUI.stripOptions[0]);
        if (stripType == 2) return;
        else if (stripType == 1) {
            if (tagTable.getSelectedRowCount() == 0) {
                JOptionPane.showMessageDialog(null, cfgPkg.Text.BPselectTags, cfgPkg.Text.MSGselectionError, JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // start - loop through all selected Input Files
        int[] selected = fileList.getSelectedIndices();
        for (int i=0; i<selected.length; i++) {
            String srcFileName = fileList.getModel().getElementAt(selected[i]).toString();
            String fileTxt     = cfgPkg.Files.readFile(srcFileName, false);
            String outputDirPath = projectSetupTab.outputDir.getAbsolutePath();
            if (outputDirPath.lastIndexOf(cfgPkg.Const.filesep()) != (outputDirPath.length()-1))
                outputDirPath = outputDirPath + cfgPkg.Const.filesep();
            File newFile       = new File(outputDirPath+"strip");
            newFile.mkdir();
            srcFileName = newFile.getAbsolutePath() + srcFileName.substring(srcFileName.lastIndexOf(cfgPkg.Const.filesep())) + ".strip";
            cfgPkg.Files.writeFile(srcFileName, fileTxt);
            if (stripType == 0) {
                String stripTxt = cfgPkg.Files.stripHTML(srcFileName);
                cfgPkg.Files.writeFile(srcFileName, stripTxt);
            } else {
                int[] selTags = tagTable.getSelectedRows();
                for (int j=0; j<selTags.length; j++) {                    
                    String openTag  = tagTable.getValueAt(selTags[j], 1).toString();
                    String closeTag = tagTable.getValueAt(selTags[j], 2).toString();
                    String stripTxt = cfgPkg.Files.removeTagTextFromFile(srcFileName, openTag, closeTag, false);
                    cfgPkg.Files.writeFile(srcFileName, stripTxt);
                } // end - loop through, collect all token text and replace in the template copy
            }
            File[] files = {new File(srcFileName)};
            fileList.addFiles(files);
        } // end - loop through all selected Input Files
    }

    public void setTags(DefaultTableModel model) {
        tagTable.resetRows(model);
    }

    public void setTags() {
        tagTable.getColumnModel().getColumn(0).setPreferredWidth(155);
        tagTable.getColumnModel().getColumn(1).setPreferredWidth(175);
        tagTable.getColumnModel().getColumn(2).setPreferredWidth(175);
        tagTable.getColumnModel().getColumn(3).setPreferredWidth(130);
    }

    public void setGeneratedFiles(DefaultTableModel model) {
        gfp.resetRows(model);
    }

    public DefaultTableModel getGeneratedFiles() {
        return gfp.getModel();
    }

    public void setFileList(DefaultListModel model) {
        fileList.resetFiles(model);
    }
    
    public DefaultListModel getFileList() {
        return (DefaultListModel)fileList.getModel();
    }
    
    public void setPreviewTag(String previewTagTxt) {
        previewTag.setText(previewTagTxt);
    }
    
    public String getPreviewTag() {
        return previewTag.getText();
    }

    public void setDefaults() {
        this.inputDir = projectSetupTab.inputDir;
        gfp.setInputDir(projectSetupTab.inputDir);
        gfp.setOutputDir(projectSetupTab.outputDir);
    }
}
