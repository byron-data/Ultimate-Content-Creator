package guiPkg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;

public class TextTab extends JPanel implements ActionListener {
    private Table      table;    
    private JCheckBox  checkBox       = new JCheckBox(cfgPkg.GUI.selectAll);
    private JCheckBox  regExpBox      = new JCheckBox(cfgPkg.GUI.regExp);
    private JButton    replaceButton  = new JButton("<html><h3>" + cfgPkg.GUI.replaceText + "</h3></html>");
    private JButton    addButton      = new JButton(cfgPkg.GUI.add);
    private JButton    modifyButton   = new JButton(cfgPkg.GUI.modify);
    private JButton    deleteButton   = new JButton(cfgPkg.GUI.delete);

    private JTextArea  searchTxt      = new JTextArea();
    private JButton    keywordButton  = new JButton(cfgPkg.GUI.specialKeywordReplace);
    private JTextArea  replaceTxt     = new JTextArea();
    
    private GeneratedFilesPanel gfp;
    private ProjectSetupTab     projectSetupTab;

    public TextTab(int tabWidth, int tabHeight, ProjectSetupTab projectSetupTab) {
        this.projectSetupTab = projectSetupTab;
        table                = new Table(projectSetupTab);    
	this.setLayout(null);
        this.setPreferredSize(new Dimension(tabWidth, tabHeight));
        gfp = new GeneratedFilesPanel(345, projectSetupTab);
        this.addTabComponents();
    }
	
    private void addTabComponents() {
        JLabel textLbl = new JLabel(cfgPkg.GUI.textTokens);
        textLbl.setLocation(5, 0);
        textLbl.setSize(300, 20);
        this.add(textLbl);

        int[] blankChecks = {0};

        table.initialise(cfgPkg.GUI.headersText, -1, blankChecks);
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 1 || evt.getClickCount() == 2) {
                    if (table.getSelectedRows().length == 1) {
                        int selected = table.getSelectedRow();
                        searchTxt.setText(table.getValueAt(selected,0).toString());
                        replaceTxt.setText(table.getValueAt(selected,1).toString());
                    } else {
                        searchTxt.setText("");
                        replaceTxt.setText("");
                    }
                    table.repaint(); // fix for selection not working properly
                }
            }
        });
        JScrollPane tableSP = new JScrollPane(table);
        tableSP.setSize(750, 390);
        tableSP.setLocation(5, 20);
        table.getColumnModel().getColumn(0).setPreferredWidth(375);
        table.getColumnModel().getColumn(1).setPreferredWidth(375);
        table.setRowHeight(50);
        table.setDefaultRenderer(String.class, new MultiLineCellRenderer());
        this.add(tableSP);

        JLabel searchLbl =  new JLabel(cfgPkg.GUI.search);
        searchLbl.setLocation(760, 0);
        searchLbl.setSize(80, 20);
        this.add(searchLbl);

        JScrollPane searchSP = new JScrollPane(searchTxt);
        searchSP.setSize(410, 370);
        searchSP.setLocation(760, 20);
        searchTxt.setToolTipText(cfgPkg.Text.TXTsearchTxt);
        this.add(searchSP);

        JLabel replaceLbl = new JLabel(cfgPkg.GUI.replaceText);
        replaceLbl.setLocation(760, 395);
        replaceLbl.setSize(80, 20);
        this.add(replaceLbl);

        keywordButton.setForeground(Color.RED);
        keywordButton.setLocation(990, 398);
        keywordButton.setSize(180, 15);
        keywordButton.addActionListener(this);
        keywordButton.setToolTipText(cfgPkg.Text.TXTkeywordButton);
        this.add(keywordButton);

        JScrollPane replaceSP = new JScrollPane(replaceTxt);
        replaceSP.setSize(410, 370);
        replaceSP.setLocation(760, 415);
        replaceTxt.setToolTipText(cfgPkg.Text.TXTreplaceTxt);
        this.add(replaceSP);

        JLabel helpLbl = new JLabel("<html><p align=center>" + cfgPkg.GUI.textReplaceLabel + "</p></html>");
        helpLbl.setLocation(760, 795);
        helpLbl.setSize(410, 50);
        this.add(helpLbl);

        checkBox.setLocation(5, 420);
        checkBox.setSize(80, 20);
        checkBox.addActionListener(this);
        checkBox.setToolTipText(cfgPkg.Text.MSGselect);
        this.add(checkBox);

        regExpBox.setLocation(85, 420);
        regExpBox.setSize(70, 20);
        regExpBox.addActionListener(this);
        regExpBox.setToolTipText(cfgPkg.Text.TXTregExpBox);
        this.add(regExpBox);

        Insets insets = new Insets(0,0,0,0);

        replaceButton.setForeground(Color.RED);
        replaceButton.setLocation(165, 420);
        replaceButton.setSize(120, 20);
        replaceButton.setMargin(insets);
        replaceButton.addActionListener(this);
        replaceButton.setToolTipText(cfgPkg.Text.TXTreplaceButton);
        this.add(replaceButton);

        addButton.setLocation(320, 420);
        addButton.setSize(120, 20);
        addButton.addActionListener(this);
        addButton.setToolTipText(cfgPkg.Text.RSSaddButton);
        this.add(addButton);

        modifyButton.setLocation(475, 420);
        modifyButton.setSize(120, 20);
        modifyButton.addActionListener(this);
        modifyButton.setToolTipText(cfgPkg.Text.RSSmodifyButton);
        this.add(modifyButton);

        deleteButton.setLocation(630, 420);
        deleteButton.setSize(120, 20);
        deleteButton.addActionListener(this);
        deleteButton.setToolTipText(cfgPkg.Text.MSGdeleteListItems);
        this.add(deleteButton);

        gfp.setLocation(0, 450);
        gfp.setSize(755, 395);
        this.add(gfp);
    }
    
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == checkBox)
            table.selectAll(checkBox.isSelected());

        else if (evt.getSource() == addButton) {
            Object[] rowData = { searchTxt.getText().trim(), replaceTxt.getText().trim() };
            table.addRow(rowData);
        }

        else if (evt.getSource() == modifyButton) {
            Object[] rowData = { searchTxt.getText().trim(), replaceTxt.getText().trim() };
            table.modifyRow(rowData);
        }

        else if (evt.getSource() == deleteButton)
            table.deleteRows();

        else if (evt.getSource() == replaceButton)
            replaceText();

        else if (evt.getSource() == keywordButton)
            replaceKeyword();
    }
    
    private void replaceText() {
        if (gfp.getTable().getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGselectGeneratedFiles, cfgPkg.Text.MSGselectionError, JOptionPane.ERROR_MESSAGE);
            return;
        }
        int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.MSGconfirmText, cfgPkg.Text.MSGconfirmReplace, JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.NO_OPTION) return;

        // loop through all selected Generated Files
        int[] selected = gfp.getTable().getSelectedRows();
        for (int i=0; i<selected.length; i++) {
            String genFileName = gfp.getTable().getValueAt(selected[i], 1).toString();
            String fileTxt     = cfgPkg.Files.readFile(genFileName, false);
            String keepTxt     = fileTxt;
            // loop through all selected text search/replace
            int[] selText = table.getSelectedRows();
            for (int j=0; j<selText.length; j++) {
                String searchTxt  = table.getValueAt(selText[j], 0).toString().replace("\r","").replace("\n",cfgPkg.Const.linesep());
                String replaceTxt = table.getValueAt(selText[j], 1).toString().replace("\r","").replace("\n",cfgPkg.Const.linesep());
                if (regExpBox.isSelected())
                    fileTxt = fileTxt.replaceAll(searchTxt, replaceTxt);
                else
                    fileTxt = fileTxt.replace(searchTxt, replaceTxt);
            }
            if (!keepTxt.equals(fileTxt))
                cfgPkg.Files.writeFile(genFileName, fileTxt);
        }
    }

    private void replaceKeyword() {
        if (cfgPkg.Const.liteVersion) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.liteMessage, cfgPkg.Text.liteMessageTitle, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (gfp.getTable().getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGselectGeneratedFiles, cfgPkg.Text.MSGselectionError, JOptionPane.ERROR_MESSAGE);
            return;
        }
        int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.TXTkeywordButton, cfgPkg.Text.MSGconfirmReplace, JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.NO_OPTION) return;

        // loop through all selected Generated Files
        int[] selected = gfp.getTable().getSelectedRows();
        for (int i=0; i<selected.length; i++) {
            String genFileName = gfp.getTable().getValueAt(selected[i], 1).toString();
            String keyword     = cfgPkg.Files.parseHTML(genFileName, "<body>", false);
            String fileTxt     = cfgPkg.Files.readFile(genFileName, false);
            fileTxt = fileTxt.replace("%keyword%", keyword);
            cfgPkg.Files.writeFile(genFileName, fileTxt);
        }
    }
    
    public void setTextSearchReplace(DefaultTableModel model) {
        table.resetRows(model);
    }

    public DefaultTableModel getTextSearchReplace() {
        return (DefaultTableModel)table.getTableModel();
    }

    public void setGeneratedFiles() {
        gfp.resizeRows();
    }

    public void setDefaults() {
        gfp.setInputDir(projectSetupTab.inputDir);
        gfp.setOutputDir(projectSetupTab.outputDir);
    }
}
