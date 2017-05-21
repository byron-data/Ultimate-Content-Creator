package guiPkg;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;
import javax.swing.*;
import javax.swing.table.*;

public class RssFeedsTab extends JPanel implements ActionListener {
    private Table      table;
    private JTextField searchTxt      = new JTextField();
    private JTextField categoryTxt    = new JTextField();
    private JTextField descriptionTxt = new JTextField();
    private JTextField urlTxt         = new JTextField();
    
    private JCheckBox  checkBox       = new JCheckBox(cfgPkg.GUI.selectAll);
    private JButton    replaceButton  = new JButton("<html><h3>" + cfgPkg.GUI.replaceTokens + "</h3></html>");
    private JButton    copyRSSBtn     = new JButton("<html><h3>" + cfgPkg.GUI.copyRSS + "</h3></html>");
    private JButton    addButton      = new JButton(cfgPkg.GUI.add);
    private JButton    modifyButton   = new JButton(cfgPkg.GUI.modify);
    private JButton    deleteButton   = new JButton(cfgPkg.GUI.delete);

    private GeneratedFilesPanel gfp;
    private ProjectSetupTab     projectSetupTab;

    private JButton    rssAssistantBtn = new JButton(cfgPkg.GUI.rssAssistant);

    public RssFeedsTab(int tabWidth, int tabHeight, ProjectSetupTab projectSetupTab) {
        this.projectSetupTab = projectSetupTab;
        table                = new Table(projectSetupTab);    
	this.setLayout(null);
        this.setPreferredSize(new Dimension(tabWidth, tabHeight));
        gfp = new GeneratedFilesPanel(345, projectSetupTab);
        this.addTabComponents();
    }

    private void addTabComponents() {
        JLabel rssLbl =  new JLabel(cfgPkg.GUI.rssTokens);
        rssLbl.setLocation(5, 0);
        rssLbl.setSize(240, 20);
        this.add(rssLbl);

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

        rssAssistantBtn.setLocation(755, 370);
        rssAssistantBtn.setSize(120, 15);
        rssAssistantBtn.setMargin(insets);
        rssAssistantBtn.addActionListener(this);
        rssAssistantBtn.setToolTipText(cfgPkg.Text.RSSrssAssistantBtn);
        this.add(rssAssistantBtn);

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
        replaceButton.setToolTipText(cfgPkg.Text.RSSreplaceButton);
        this.add(replaceButton);

        copyRSSBtn.setForeground(Color.RED);
        copyRSSBtn.setLocation(345, 420);
        copyRSSBtn.setSize(120, 20);
        copyRSSBtn.setMargin(insets);
        copyRSSBtn.addActionListener(this);
        copyRSSBtn.setToolTipText(cfgPkg.Text.RSScopyRSSBtn);
        this.add(copyRSSBtn);

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

        JLabel helpLbl = new JLabel("<html><p align=center>" + cfgPkg.GUI.rssFeedsLabel + "</p></html>");
        helpLbl.setLocation(760, 470);
        helpLbl.setSize(410, 120);
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
            replaceRss();

        else if (evt.getSource() == copyRSSBtn)
            replaceRssCopy();
        
        else if (evt.getSource() == rssAssistantBtn)
            rssAssistant();
    }
    
    private void replaceRss() {
        if (cfgPkg.Const.liteVersion) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.liteMessage, cfgPkg.Text.liteMessageTitle, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (gfp.getTable().getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGselectGeneratedFiles, cfgPkg.Text.MSGselectionError, JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (table.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGreplaceRss, cfgPkg.Text.MSGselectionError, JOptionPane.ERROR_MESSAGE);
            return;
        }
        int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.MSGconfirmRss, cfgPkg.Text.MSGconfirmReplace, JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.NO_OPTION) return;

        // loop through all selected Generated Files
        int[] selected = gfp.getTable().getSelectedRows();
        for (int i=0; i<selected.length; i++) {
            String genFileName = gfp.getTable().getValueAt(selected[i], 1).toString();
            String fileTxt     = cfgPkg.Files.readFile(genFileName, false);
            String feedCode    = "";

            Vector searchVector = new Vector();
            Vector thisVector;

            // loop through all selected Rss search/replace
            int[] selRss = table.getSelectedRows();
            for (int j=0; j<selRss.length; j++) {
                String searchTxt   = table.getValueAt(selRss[j], 0).toString();
                String rssFeed     = table.getValueAt(selRss[j], 3).toString();

                boolean found = false;
                for (int k=0; k<searchVector.size() && !found; k++) {
                    thisVector = (Vector)searchVector.get(k);
                    if (thisVector.get(0).toString().equals(searchTxt)) {
                        thisVector.addElement(rssFeed);
                        found = true;
                    }
                }

                if (!found) {
                    thisVector = new Vector();
                    thisVector.addElement(searchTxt);
                    thisVector.addElement(rssFeed);
                    searchVector.addElement(thisVector);
                }
            }
            
            for (int k=0; k<searchVector.size(); k++) {
                thisVector = (Vector)searchVector.get(k);
                if (thisVector.size() == 2)
                    feedCode = feedCode + cfgPkg.Const.phpRssFeeds.replace(cfgPkg.Const.phpRssFeed, thisVector.get(1).toString());
                else {
                    feedCode = cfgPkg.Const.linesep();
                    for (int l=1; l<thisVector.size(); l++) {
                        feedCode = feedCode + cfgPkg.Const.phpAddRssFeeds.replace(cfgPkg.Const.phpRssFeed, thisVector.get(l).toString());
                        feedCode = feedCode + cfgPkg.Const.linesep();
                    }
                    feedCode = feedCode + cfgPkg.Const.phpMergeRssFeeds + cfgPkg.Const.linesep();
                }
                fileTxt  = fileTxt.replace(thisVector.get(0).toString(), feedCode);
            }
            cfgPkg.Files.writeFile(genFileName, fileTxt);
        }
    }

    private void replaceRssCopy() {
        if (cfgPkg.Const.liteVersion) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.liteMessage, cfgPkg.Text.liteMessageTitle, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (table.getSelectedRowCount() == 0) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGreplaceRss, cfgPkg.Text.MSGselectionError, JOptionPane.ERROR_MESSAGE);
            return;
        }

        String feedCode    = "";

        Vector searchVector = new Vector();
        Vector thisVector;

        // loop through all selected Rss search/replace
        int[] selRss = table.getSelectedRows();
        for (int j=0; j<selRss.length; j++) {
            String searchTxt   = table.getValueAt(selRss[j], 0).toString();
            String rssFeed     = table.getValueAt(selRss[j], 3).toString();

            boolean found = false;
            for (int k=0; k<searchVector.size() && !found; k++) {
                thisVector = (Vector)searchVector.get(k);
                if (thisVector.get(0).toString().equals(searchTxt)) {
                    thisVector.addElement(rssFeed);
                    found = true;
                }
            }

            if (!found) {
                thisVector = new Vector();
                thisVector.addElement(searchTxt);
                thisVector.addElement(rssFeed);
                searchVector.addElement(thisVector);
            }
        }

        for (int k=0; k<searchVector.size(); k++) {
            thisVector = (Vector)searchVector.get(k);
            if (thisVector.size() == 2)
                feedCode = feedCode + cfgPkg.Const.phpRssFeeds.replace(cfgPkg.Const.phpRssFeed, thisVector.get(1).toString());
            else {
                feedCode = cfgPkg.Const.linesep();
                for (int l=1; l<thisVector.size(); l++) {
                    feedCode = feedCode + cfgPkg.Const.phpAddRssFeeds.replace(cfgPkg.Const.phpRssFeed, thisVector.get(l).toString());
                    feedCode = feedCode + cfgPkg.Const.linesep();
                }
                feedCode = feedCode + cfgPkg.Const.phpMergeRssFeeds + cfgPkg.Const.linesep();
            }
        }

        StringSelection ss = new StringSelection(feedCode);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
    }

    private void rssAssistant() {
        if (cfgPkg.Const.liteVersion) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.liteMessage, cfgPkg.Text.liteMessageTitle, JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // loop through all selected Generated Files
        int[] selected = gfp.getTable().getSelectedRows();
        String keyword = "";
        if (selected.length == 1) {
            String genFileName = gfp.getTable().getValueAt(selected[0], 1).toString();
            keyword            = cfgPkg.Files.parseHTML(genFileName, "<body>", false);
        } else {
            keyword = JOptionPane.showInputDialog(null, cfgPkg.Text.MSGkeywordRss, "").trim();
        }
        cfgPkg.Files.UCCResearchBrowsers(cfgPkg.Const.rssBrowser, keyword, "");
    }
    
    public void setRssSearchReplace(DefaultTableModel model) {
        table.resetRows(model);
        table.getColumnModel().getColumn(0).setPreferredWidth(170);
        table.getColumnModel().getColumn(1).setPreferredWidth(170);
        table.getColumnModel().getColumn(2).setPreferredWidth(350);
        table.getColumnModel().getColumn(3).setPreferredWidth(475);
    }

    public DefaultTableModel getRssSearchReplace() {
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
