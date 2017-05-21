package guiPkg;

import java.awt.event.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

import cfgPkg.*;
import javax.swing.table.DefaultTableModel;

public class GrabContentTab extends ContentPanel {
    private Timer timer        = new Timer();
    private long  lastModified = 0;

    // Presentation
    private Table tagTable;
    private JTextField categoryTxt    = new JTextField();
    private JTextField openTagTxt     = new JTextField();
    private JTextField closeTagTxt    = new JTextField();
    private JTextField tokenTxt       = new JTextField();

    private JCheckBox  checkBox       = new JCheckBox(cfgPkg.GUI.selectAll);
    private JButton    addButton      = new JButton(cfgPkg.GUI.add);
    private JButton    modifyButton   = new JButton(cfgPkg.GUI.modify);
    private JButton    deleteButton   = new JButton(cfgPkg.GUI.delete);

    public GrabContentTab(int tabWidth, int tabHeight,
            ProjectSetupTab projectSetupTab, BatchProcessTab batchProcessTab) {
        super(tabWidth, tabHeight, projectSetupTab, batchProcessTab);

        tagTable = new Table(projectSetupTab);
        JLabel tagLbl =  new JLabel(cfgPkg.GUI.tagsLabel);
        tagLbl.setLocation(425, 0);
        tagLbl.setSize(240, 20);
        this.add(tagLbl);

        int[] blankChecks = {0, 1, 3};
        tagTable.initialise(blankChecks, projectSetupTab.stmTags, true);
        
        tagTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 1 || evt.getClickCount() == 2) {
                    if (tagTable.getSelectedRows().length == 1) {
                        int selected = tagTable.getSelectedRow();
                        categoryTxt.setText(tagTable.getValueAt(selected,0).toString());
                        openTagTxt.setText(tagTable.getValueAt(selected,1).toString());
                        closeTagTxt.setText(tagTable.getValueAt(selected,2).toString());
                        tokenTxt.setText(tagTable.getValueAt(selected,3).toString());
                    } else {
                        categoryTxt.setText("");
                        openTagTxt.setText("");
                        closeTagTxt.setText("");
                        tokenTxt.setText("");
                    }
                    tagTable.repaint(); // fix for selection not working properly
                }
            }
        });
        JScrollPane jsp = new JScrollPane(tagTable);
        jsp.setSize(655, 755);
        jsp.setLocation(515, 20);
        tagTable.getColumnModel().getColumn(0).setPreferredWidth(155);
        tagTable.getColumnModel().getColumn(1).setPreferredWidth(175);
        tagTable.getColumnModel().getColumn(2).setPreferredWidth(175);
        tagTable.getColumnModel().getColumn(3).setPreferredWidth(130);
        this.add(jsp);

        JLabel categoryLbl =  new JLabel(cfgPkg.GUI.categoryOrUrl);
        categoryLbl.setLocation(515, 775);
        categoryLbl.setSize(115, 20);
        this.add(categoryLbl);

        JLabel openLbl = new JLabel(cfgPkg.GUI.openTag);
        openLbl.setLocation(675, 775);
        openLbl.setSize(60, 20);
        this.add(openLbl);

        JLabel closeLbl = new JLabel(cfgPkg.GUI.closeTag);
        closeLbl.setLocation(855, 775);
        closeLbl.setSize(80, 20);
        this.add(closeLbl);

        JLabel tokenLbl = new JLabel(cfgPkg.GUI.token);
        tokenLbl.setLocation(1035, 775);
        tokenLbl.setSize(60, 20);
        this.add(tokenLbl);

        categoryTxt.setSize(155, 20);
        categoryTxt.setLocation(515, 795);
        categoryTxt.setToolTipText(cfgPkg.Text.TScategoryTxt);
        this.add(categoryTxt);

        openTagTxt.setLocation(675, 795);
        openTagTxt.setSize(175, 20);
        openTagTxt.setToolTipText(cfgPkg.Text.TSopenTagTxt);
        this.add(openTagTxt);

        closeTagTxt.setLocation(855, 795);
        closeTagTxt.setSize(175, 20);
        closeTagTxt.setToolTipText(cfgPkg.Text.TScloseTagTxt);
        this.add(closeTagTxt);

        tokenTxt.setLocation(1035, 795);
        tokenTxt.setSize(130, 20);
        tokenTxt.setToolTipText(cfgPkg.Text.TStokenTxt);
        this.add(tokenTxt);

        checkBox.setLocation(515, 825);
        checkBox.setSize(80, 20);
        checkBox.addActionListener(this);
        checkBox.setToolTipText(cfgPkg.Text.MSGselect);
        this.add(checkBox);

        addButton.setLocation(665, 825);
        addButton.setSize(120, 20);
        addButton.addActionListener(this);
        addButton.setToolTipText(cfgPkg.Text.TSaddButton);
        this.add(addButton);

        modifyButton.setLocation(855, 825);
        modifyButton.setSize(120, 20);
        modifyButton.addActionListener(this);
        modifyButton.setToolTipText(cfgPkg.Text.TSmodifyButton);
        this.add(modifyButton);

        deleteButton.setLocation(1045, 825);
        deleteButton.setSize(120, 20);
        deleteButton.addActionListener(this);
        deleteButton.setToolTipText(cfgPkg.Text.TSdeleteButton);
        this.add(deleteButton);
    }

    public void actionExtra(ActionEvent evt) {
        if (evt.getSource() == checkBox)
            tagTable.selectAll(checkBox.isSelected());

        else if (evt.getSource() == addButton) {
            Object[] rowData = { categoryTxt.getText().trim(), openTagTxt.getText().trim(),
                                 closeTagTxt.getText().trim(), tokenTxt.getText().trim() };
            tagTable.addRow(rowData);
        }

        else if (evt.getSource() == modifyButton) {
            Object[] rowData = { categoryTxt.getText().trim(), openTagTxt.getText().trim(),
                                 closeTagTxt.getText().trim(), tokenTxt.getText().trim() };
            tagTable.modifyRow(rowData);
        }

        else if (evt.getSource() == deleteButton)
            tagTable.deleteRows();
    }
    
    public void browserPage(File file) {
        clearText();

        File tmp = null;
        try {
            tmp = File.createTempFile("temp", "txt");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        String fileTxt = "";
        String url     = "";
        File urlFile = new File(cfgPkg.Const.defaultUrlPage);
        if (urlFile.exists()) {
            String urlTxt = cfgPkg.Files.readFile(urlFile.getAbsolutePath(), false);
            int pos = urlTxt.indexOf("<!--URL=");
            if (pos != -1) {
                url = urlTxt.substring(pos+"<!--URL=".length());
                url = url.substring(0, url.indexOf("-->")); // example <!--URL=http://forums.mozillazine.org/viewtopic.php?p=3040298#3040298-->
            } else
                return;
        }
        fileTxt = cfgPkg.Files.readFile(file.getAbsolutePath(), false);
        cfgPkg.Files.writeFile(tmp.getAbsolutePath(), fileTxt);

        // start - loop through, collect all token text and replace in the template copy
        for (int i=0; i<tagTable.getRowCount(); i++) {
            if (url.indexOf(tagTable.getValueAt(i, 0).toString()) != -1) {
                String openTag  = tagTable.getValueAt(i, 1).toString();
                String closeTag = tagTable.getValueAt(i, 2).toString();
                String token    = tagTable.getValueAt(i, 3).toString();
                
                String tokenTxt = "";
                if (openTag.indexOf("RegExp=") == 0) {
                    Pattern tags = Pattern.compile(openTag.substring(openTag.indexOf("=")+1), Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                    Matcher matcher = tags.matcher(fileTxt);
                    while (matcher.find()) {
                        //System.out.println(matcher.group(0) + " " + matcher.group(1));
                        tokenTxt = matcher.group(1);
                        break;
                    }
                } else {
                    tokenTxt = cfgPkg.Files.readFileForTag(tmp.getAbsolutePath(), openTag, closeTag);
                }
                
                if (token.equals("%title%"))        titleTxt.setText(tokenTxt);
                else if (token.equals("%author%"))  authorTxt.setText(tokenTxt);
                else if (token.equals("%article%")) articleTxt.setText(tokenTxt);
                else if (token.equals("%about%"))   aboutTxt.setText(tokenTxt);
            }
        } // end - loop through, collect all token text and replace in the template copy

        tmp.delete();
    }

    public class ReadFileTask extends TimerTask {
        public void run() {
            File pageFile = new File(cfgPkg.Const.defaultPage);
            //System.out.println("ReadFileTask " + pageFile.getAbsolutePath());
            if (pageFile.exists()) {
                if (pageFile.lastModified() != lastModified) {
                    lastModified = pageFile.lastModified();
                    browserPage(pageFile);
                }
            }
        }     
    }
    
    public void startTimer() {
        timer = new Timer();
        timer.schedule(new ReadFileTask(), 2000, 2000);
        //System.out.println("startTimer");
    }
    
    public void stopTimer() {
        timer.cancel();        
        //System.out.println("stopTimer");
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
    
    public DefaultTableModel getTags() {
        return (DefaultTableModel)tagTable.getTableModel();
    }
}
