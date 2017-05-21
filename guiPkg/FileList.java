package guiPkg;

import cfgPkg.ProgramConfigurationFile;
import java.io.*;
import javax.swing.*;

public class FileList extends JList {
    private DefaultListModel listModel = new DefaultListModel();
    private ProjectSetupTab  projectSetupTab;

    public FileList(ProjectSetupTab projectSetupTab) {
        setModel(listModel);
        this.projectSetupTab = projectSetupTab;
    }
    
    public void selectAll(boolean all) {
        if (all) {
            this.setSelectionInterval(0, listModel.getSize()-1);
            this.repaint(); // fix for setSelectionInterval not working properly
        }
        else
            this.clearSelection();
    }

    private boolean uniqueName(String name) {
        boolean found = false;
        for (int i = 0; i < listModel.size() && !found; i++) {
            String temp = listModel.get(i).toString();
            if (temp.equals(name))
                return false;
        }

        return true;
    }

    public void addFiles(File[] files) {
        for (int i=0; i<files.length; i++) {
            if (uniqueName(files[i].toString()))
                listModel.addElement(files[i]);
        }
    }

    public void removeFiles() {
        int[] selected = this.getSelectedIndices();
        if (selected.length == 0)
            JOptionPane.showMessageDialog(null, cfgPkg.Text.MSGselectFilesRemove, cfgPkg.Text.MSGselectionError, JOptionPane.ERROR_MESSAGE);
        else if (selected.length > 0) {
            int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.MSGremoveListItems, cfgPkg.Text.MSGconfirmRemove, JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                for (int i=selected.length; i > 0; i--)
                    listModel.remove(selected[i-1]);
            }
        }
    }

    public void resetFiles(DefaultListModel model) {
        listModel.clear();
        if (model == null) return;
        
        for (int i=0; i<model.getSize(); i++) {
            listModel.addElement(model.get(i));
        }
    }

    public void editFiles() {
        int[] selected = this.getSelectedIndices();
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
            cfgPkg.Files.editFile(editor, listModel.get(selected[i]).toString());
        }
    }
    
    public void browseFiles() {
        int[] selected = this.getSelectedIndices();
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
                args = args + "\"" + listModel.get(selected[i]).toString() + "\" ";
            else
                cfgPkg.Files.browseFile("", listModel.get(selected[i]).toString());
        }
        if (!pcf.nativeBrowse) cfgPkg.Files.browseFile("browser", args);
    }
}
