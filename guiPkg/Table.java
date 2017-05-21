package guiPkg;

import cfgPkg.Const;
import cfgPkg.ProgramConfigurationFile;
import java.io.*;
import javax.swing.*;
import javax.swing.table.*;
import java.util.Vector;

public class Table extends JTable {
    private DefaultTableModel tableModel;
    private int               uniqueCol;
    private int[]             blankChecks;
    private boolean           files;
    private TableSorter       sorter;

    private ProjectSetupTab   projectSetupTab;
    
    /** Creates a new instance of Table */
    public Table(ProjectSetupTab projectSetupTab) {
        this.projectSetupTab = projectSetupTab;
    }
    
    public void initialise(String headers[], int uniqueCol, int[] blankChecks) {
        tableModel =
            new DefaultTableModel(headers, 0) {
              // Make read-only
              public boolean isCellEditable(int x, int y) {
                return false;
              }
              
              public Class getColumnClass(int columnIndex) {
                return String.class;
              }
            };
        
        getTableHeader().setReorderingAllowed(false);
        this.uniqueCol   = uniqueCol;
        this.blankChecks = blankChecks;
        this.files       = false;
        sorter = new TableSorter(tableModel, getTableHeader());
        setModel(sorter);
        getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.setColumnSelectionAllowed(false);
    }
    
    public void initialise(int[] blankChecks, DefaultTableModel tableModel, boolean tags) {
        getTableHeader().setReorderingAllowed(false);
        if (tags)
            this.uniqueCol = -1;
        else
            this.uniqueCol = 1;
        this.blankChecks = blankChecks;
        this.tableModel  = tableModel;
        if (tags)
            this.files = false;
        else
            this.files = true;
        sorter = new TableSorter(tableModel, getTableHeader());
        setModel(sorter);
        getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.setColumnSelectionAllowed(false);
    }
    
    public void selectAll(boolean all) {
        if (all)
            this.selectAll();
        else
            this.clearSelection();
    }

    private boolean uniqueName(int column, String name, int selected) {
        boolean found = false;
        for (int i=0; i < tableModel.getRowCount() && !found; i++) {
            if (i != selected) {
                String temp = getValueAt(i, column).toString();
                if (temp.equals(name))
                    return false;
            }
        }

        return true;
    }

    private boolean checkRow(Object[] rowData, boolean add) {
        boolean unique   = true;
        int     count    = 0;
        String  value    = "";
        int     selected = -1;
        if (!add)
            selected = this.getSelectedRow();

        if (uniqueCol != -1) {
            value  = (String)rowData[uniqueCol];
            unique = uniqueName(uniqueCol, value, selected);
        }

        if (unique) {
            for (int i=0; i<tableModel.getRowCount(); i++) {
                count = 0;
                for (int j=0; j<tableModel.getColumnCount(); j++) {
                    value = (String)rowData[j];
                    if (value.equals(getValueAt(i, j).toString())) count++;
                }
                if (count == tableModel.getColumnCount()) break;
            }
            if (count < tableModel.getColumnCount()) {
                if (add)
                    tableModel.addRow(rowData);
                else {
                    for (int i=0; i<rowData.length; i++)
                        setValueAt(rowData[i], this.getSelectedRow(), i);
                }
                return true;
            }
            else {
                value = "";
                for (int k=0; k<rowData.length; k++)
                    value = value + rowData[k] + cfgPkg.Const.linesep();
                value = value + cfgPkg.Const.linesep();
                JOptionPane.showMessageDialog(null, value + cfgPkg.Text.MSGalreadyEntered,
                                              cfgPkg.Text.MSGduplicateEntry, JOptionPane.ERROR_MESSAGE);
            }
        }
        else
            JOptionPane.showMessageDialog(null, value + cfgPkg.Text.MSGalreadyEntered,
                                          cfgPkg.Text.MSGduplicateEntry, JOptionPane.ERROR_MESSAGE);

        return false;
    }

    public boolean addRow(Object[] rowData) {
        for (int i=0; i<blankChecks.length; i++) {
            if (rowData[blankChecks[i]].equals("")) {
                JOptionPane.showMessageDialog(null, tableModel.getColumnName(blankChecks[i]) + cfgPkg.Text.MSGblank,
                                              cfgPkg.Text.MSGinputError, JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return checkRow(rowData, true);
    }

    public boolean addTagRow(Object[] rowData) {
        if (rowData[2].toString().equals(Const.startPreserveTags) ||
            rowData[2].toString().equals(Const.endPreserveTags) ||
            rowData[2].toString().equals(Const.childLinksToken) ||
            rowData[2].toString().equals(Const.shareLinksToken) ||
            rowData[2].toString().equals(Const.shareFileLinksToken) ||
            rowData[2].toString().equals(Const.keyword) ||
            rowData[2].toString().equals(Const.phpSnippetToken)) {
                JOptionPane.showMessageDialog(null, rowData[2].toString() + cfgPkg.Text.MSGpredefinedToken,
                                              cfgPkg.Text.MSGinputError, JOptionPane.ERROR_MESSAGE);
                return false;
        }
        return addRow(rowData);
    }

    public void modifyRow(Object[] rowData) {
        if (this.getSelectedRowCount() != 1)
            JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGonlyOne, cfgPkg.Text.MSGselectionError, JOptionPane.ERROR_MESSAGE);
        else
            checkRow(rowData, false);
    }

    public void deleteRows() {
        if (this.getSelectedRowCount() == 0)
            JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGselectItemsDelete, cfgPkg.Text.MSGselectionError, JOptionPane.ERROR_MESSAGE);
        else if (this.getSelectedRowCount() > 0) {
            int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.MSGdeleteListItems, cfgPkg.Text.MSGconfirmDelete, JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                int[] selected = this.getSelectedRows();
                for (int i=selected.length; i > 0; i--) {
                    if (files) {
                        File file = new File(getValueAt(selected[i-1], 1).toString());
                        file.delete();

                        // Reset any files that refer back to the deleted file
                        String index = file.getName();
                        index = index.substring(0, index.lastIndexOf('.'));

                        String snippetsName = file.getParent() + cfgPkg.Const.filesep() + index + "_" + cfgPkg.Const.phpSnippetFile;
                        file = new File(snippetsName);
                        if (file.exists()) {
                            choice = JOptionPane.showConfirmDialog(null,
                                cfgPkg.Text.MSGdelete + snippetsName, cfgPkg.Text.MSGconfirmDelete, JOptionPane.YES_NO_OPTION);
                            if (choice == JOptionPane.YES_OPTION)
                                file.delete();
                        }

                        String shareFileName = file.getParent() + cfgPkg.Const.filesep() + index + "_" + cfgPkg.Const.shareFileLinksFile;
                        file = new File(shareFileName);
                        if (file.exists()) {
                            choice = JOptionPane.showConfirmDialog(null,
                                cfgPkg.Text.MSGdelete + shareFileName, cfgPkg.Text.MSGconfirmDelete, JOptionPane.YES_NO_OPTION);
                            if (choice == JOptionPane.YES_OPTION)
                                file.delete();
                        }
                        
                        String codeTxt = cfgPkg.Const.phpSnippetCode.replace(cfgPkg.Const.phpSnippetFile, index+"_"+cfgPkg.Const.phpSnippetFile);
                        for (int j=0; j<tableModel.getRowCount(); j++) {
                            if (getValueAt(j, 0).equals(index)) {
                                setValueAt("", j, 0);
                                String srcFileName = getValueAt(j, 1).toString();
                                String getTxt      = "";

                                getTxt = cfgPkg.Files.readFile(srcFileName, false);
                                if (getTxt.indexOf(cfgPkg.Const.shareLinksStart) != -1) {
                                    getTxt = cfgPkg.Files.removeTagTextFromFile(srcFileName,
                                            cfgPkg.Const.shareLinksStart, cfgPkg.Const.shareLinksEnd, true);
                                    getTxt = getTxt.replace(cfgPkg.Const.shareLinksStart+cfgPkg.Const.linesep()+cfgPkg.Const.shareLinksEnd, 
                                            cfgPkg.Const.shareLinksToken);
                                    cfgPkg.Files.writeFile(srcFileName, getTxt);
                                }
                                
                                if (getTxt.indexOf(cfgPkg.Const.shareFileLinksStart) != -1) {
                                    getTxt = cfgPkg.Files.removeTagTextFromFile(srcFileName,
                                            cfgPkg.Const.shareFileLinksStart, cfgPkg.Const.shareFileLinksEnd, true);
                                    getTxt = getTxt.replace(cfgPkg.Const.shareFileLinksStart+cfgPkg.Const.linesep()+cfgPkg.Const.shareFileLinksEnd, 
                                            cfgPkg.Const.shareFileLinksToken);
                                    cfgPkg.Files.writeFile(srcFileName, getTxt);
                                }

                                if (getTxt.indexOf(codeTxt) != -1) {
                                    getTxt = getTxt.replace(codeTxt, cfgPkg.Const.phpSnippetToken);
                                    cfgPkg.Files.writeFile(srcFileName, getTxt);
                                }
                            }
                        }
                    }
                    
                    tableModel.removeRow(selected[i-1]);
                }
            }
        }
    }

    public void resetRows(TableModel model) {
        for (int i=0; i<tableModel.getRowCount(); i++)
            tableModel.removeRow(i);
        if (model == null) return;
        tableModel.setRowCount(model.getRowCount());
        tableModel.setColumnCount(model.getColumnCount());
        for (int i=0; i<model.getRowCount(); i++) {
            for (int j=0; j<model.getColumnCount(); j++)
                tableModel.setValueAt(model.getValueAt(i, j), i, j);
        }
    }

    public void editFiles() {
        int[] selected = this.getSelectedRows();
        if (selected.length == 0) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGselectFilesEdit, cfgPkg.Text.MSGselectionError, JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        ProgramConfigurationFile pcf = cfgPkg.Files.openProgramFile();
        String editor = "";
        if (!pcf.nativeEdit) editor = projectSetupTab.editor.toString();
        
        if (!projectSetupTab.editorCheck()) return;
        for (int i=0; i<selected.length; i++) {
            if (i == cfgPkg.Const.maxOpens) {
                JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGselectFilesMax, cfgPkg.Text.MSGfilesMax, JOptionPane.PLAIN_MESSAGE);
                break;
            }
            cfgPkg.Files.editFile(editor, getValueAt(selected[i], 1).toString());
        }
    }

    public void browseFiles() {
        int[] selected = this.getSelectedRows();
        if (selected.length == 0) {
            JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGselectFilesBrowse, cfgPkg.Text.MSGselectionError, JOptionPane.ERROR_MESSAGE);
            return;
        }

        ProgramConfigurationFile pcf = cfgPkg.Files.openProgramFile();
        String args = "";
        for (int i=0; i<selected.length; i++) {
            if (i == cfgPkg.Const.maxOpens) {
                JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGselectFilesMax, cfgPkg.Text.MSGfilesMax, JOptionPane.PLAIN_MESSAGE);
                break;
            }
            if (!pcf.nativeBrowse) 
                args = args + "\"" + getValueAt(selected[i], 1).toString() + "\" ";
            else
                cfgPkg.Files.browseFile("", getValueAt(selected[i], 1).toString());
        }
        if (!pcf.nativeBrowse) cfgPkg.Files.browseFile("browser", args);
    }

    public void renameFile(String fileName) {
        int selected = this.getSelectedRow();
        String oldFileName = getValueAt(selected, 1).toString();
        File oldFile       = new File(oldFileName);
        String newName     = oldFile.getParent() + cfgPkg.Const.filesep() + fileName;/* +
                oldFile.getName().substring(oldFile.getName().lastIndexOf('.'));*/
        
        if (this.getSelectedRowCount() != 1)
            JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGonlyOne, cfgPkg.Text.MSGselectionError, JOptionPane.ERROR_MESSAGE);
        else if (fileName.equals(""))
             JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGfilenameBlank, cfgPkg.Text.MSGinputError, JOptionPane.ERROR_MESSAGE);
        else if (!uniqueName(1, newName, -1))
             JOptionPane.showMessageDialog(null, fileName + cfgPkg.Text.MSGalreadyEntered, cfgPkg.Text.MSGduplicateEntry, JOptionPane.ERROR_MESSAGE);
        else {
            if (!getValueAt(selected, 0).toString().equals("")) {
                int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.MSGindexAssigned, cfgPkg.Text.MSGconfirmRename, JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.NO_OPTION) return;
            } else {
                int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.GFrenameFilesButton, cfgPkg.Text.MSGconfirmRename, JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.NO_OPTION) return;
            }
            
            // Rename the file
            setValueAt(newName, selected, 1);
            cfgPkg.Files.renameFile(oldFile, newName);
            
            // Try to rename the snippets file (if there is one)
            String oldSnippetsName = oldFile.getAbsolutePath().substring(0, oldFile.getAbsolutePath().indexOf(".")) +
                "_" + cfgPkg.Const.phpSnippetFile;
            String newSnippetsName = oldFile.getParent() + cfgPkg.Const.filesep() + fileName.substring(0, fileName.indexOf(".")) +
                "_" + cfgPkg.Const.phpSnippetFile;
            boolean snippetFile    = false;
            if (cfgPkg.Files.renameFile(oldSnippetsName, newSnippetsName))
                snippetFile = true;

            // Try to rename the share file (if there is one)
            String oldShareName    = oldFile.getAbsolutePath().substring(0, oldFile.getAbsolutePath().indexOf(".")) +
                "_" + cfgPkg.Const.shareFileLinksFile;
            String newShareName    = oldFile.getParent() + cfgPkg.Const.filesep() + fileName.substring(0, fileName.indexOf(".")) +
                "_" + cfgPkg.Const.shareFileLinksFile;
            boolean shareFile      = false;
            if (cfgPkg.Files.renameFile(oldShareName, newShareName))
                shareFile = true;

            oldFileName = oldFile.getName().substring(0, oldFile.getName().lastIndexOf('.'));
            String newFileName = fileName.substring(0, fileName.lastIndexOf('.'));
            for (int i=0; i<tableModel.getRowCount(); i++) {
                if (getValueAt(i, 0).equals(oldFileName)) {
                    setValueAt(newFileName, i, 0);

                    if (snippetFile || shareFile) { // find anywhere else using the files and change
                        String srcFileName = getValueAt(i, 1).toString();
                        String allTxt      = cfgPkg.Files.readFile(srcFileName, false);
                        if (snippetFile)
                            allTxt = allTxt.replace(oldFileName + "_" + cfgPkg.Const.phpSnippetFile,
                                newFileName + "_" + cfgPkg.Const.phpSnippetFile);                
                        if (shareFile)
                            allTxt = allTxt.replace(oldFileName + "_" + cfgPkg.Const.shareFileLinksFile,
                                newFileName + "_" + cfgPkg.Const.shareFileLinksFile);                
                        cfgPkg.Files.writeFile(srcFileName, allTxt);
                    }
                }
            }
        }
    }

    public DefaultTableModel getTableModel() {
        DefaultTableModel dft = new DefaultTableModel();
        Vector vector = new Vector();
        for (int j=0; j<tableModel.getColumnCount(); j++) {
            vector.addElement(tableModel.getColumnName(j));
        }
        dft.setDataVector(tableModel.getDataVector(), vector);
        return dft;
    }
    
    public Object getValueAt(int row, int column) {
        //return getModel().getValueAt(row, column);
        return tableModel.getValueAt(row, column);
    }
    
    public void setValueAt(Object aValue, int row, int column) {
        //getModel().setValueAt(aValue, row, column);
        tableModel.setValueAt(aValue, row, column);
    }
}
