/*
 * SharedTableModel.java
 *
 * Created on 11 October 2006, 22:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package guiPkg;

import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Byron
 */
public class SharedTableModel extends DefaultTableModel { 
    /** Creates a new instance of SharedTableModel */
    public SharedTableModel(String headers[]) {
        super(headers, 0);
    }
    
    public boolean isCellEditable(int x, int y) {
        return false;
    }
}
