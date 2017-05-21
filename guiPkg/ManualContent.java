package guiPkg;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ManualContent extends JFrame implements ActionListener {
    private   JPanel      mainPanel     = new JPanel();
    private   JEditorPane paragraphArea = new JEditorPane();
    private   JPanel      cmdPanel      = new JPanel();
    private   JButton     copyBtn       = new JButton(cfgPkg.GUI.copyText);
    private   JButton     appendBtn     = new JButton(cfgPkg.GUI.appendText);
    private   JButton     previewBtn    = new JButton(cfgPkg.GUI.preview);
    private   JButton     viewBtn       = new JButton(cfgPkg.GUI.edit);
    private   JButton     clearBtn      = new JButton(cfgPkg.GUI.clearText);
    private   JButton     closeBtn      = new JButton(cfgPkg.GUI.close);
    
    private   int         buttonHeight    = 20;
    private   int         paragraphWidth  = 800;
    private   int         paragraphHeight = 940;
    
    private   ContentAssistantTab contentAssistantTab;
    
    public ManualContent(ContentAssistantTab contentAssistantTab) {
        this.contentAssistantTab = contentAssistantTab;
        setIconImage(new ImageIcon(ManualContent.class.getResource("images/UCC.jpg")).getImage());
        setTitle(cfgPkg.GUI.manualContentTitle);

        getContentPane().setLayout(new GridLayout(1, 1));
        getContentPane().add(mainPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                windowClose();
            }
        });
        
        paragraphArea.setContentType("text/html");
        //paragraphArea.setContentType("text/plain");
        //paragraphArea.setEditable(false);
        paragraphArea.setToolTipText(cfgPkg.Text.MCparagraphSP);
        JScrollPane paragraphSP = new JScrollPane(paragraphArea);
        paragraphSP.setPreferredSize(new Dimension(paragraphWidth, paragraphHeight));
        mainPanel.add(paragraphSP);

        cmdPanel.setLayout(new FlowLayout());
        
        copyBtn.setPreferredSize(new Dimension(100, buttonHeight));
        copyBtn.addActionListener(this);
        copyBtn.setToolTipText(cfgPkg.Text.MCcopyManual);
        cmdPanel.add(copyBtn);
        
        Insets insets = new Insets(0,0,0,0);

        appendBtn.setPreferredSize(new Dimension(100, buttonHeight));
        appendBtn.addActionListener(this);
        appendBtn.setToolTipText(cfgPkg.Text.MCappendManual);
        appendBtn.setMargin(insets);
        cmdPanel.add(appendBtn);
        
        previewBtn.setPreferredSize(new Dimension(100, buttonHeight));
        previewBtn.addActionListener(this);
        previewBtn.setToolTipText(cfgPkg.Text.MCpreviewManual);
        previewBtn.setMargin(insets);
        cmdPanel.add(previewBtn);
        
        /*viewBtn.setPreferredSize(new Dimension(100, buttonHeight));
        viewBtn.addActionListener(this);
        viewBtn.setToolTipText(cfgPkg.Text.MSGviewEdit);
        viewBtn.setMargin(insets);
        cmdPanel.add(viewBtn);*/

        clearBtn.setPreferredSize(new Dimension(100, buttonHeight));
        clearBtn.addActionListener(this);
        clearBtn.setToolTipText(cfgPkg.Text.MSGclearText);
        clearBtn.setMargin(insets);
        cmdPanel.add(clearBtn);
        
        closeBtn.setPreferredSize(new Dimension(100, buttonHeight));
        closeBtn.addActionListener(this);
        closeBtn.setToolTipText(cfgPkg.Text.MSGcloseWindow);
        closeBtn.setMargin(insets);
        cmdPanel.add(closeBtn);

        mainPanel.add(cmdPanel);

        pack();
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == copyBtn) {
            int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.MCcopyManual, cfgPkg.Text.MSGconfirmCopy, JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.NO_OPTION) return;
            
            contentAssistantTab.clearContent();
            String getText = formatContent();
            getText = contentAssistantTab.addContent(getText);
            contentAssistantTab.sortSnippets(getText);
            contentAssistantTab.showSnippets("", true);
            paragraphArea.setText("");
        }

        else if (evt.getSource() == appendBtn) {
            int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.MCappendManual, cfgPkg.Text.MSGconfirmAppend, JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.NO_OPTION) return;
            
            String getText = formatContent();
            getText = contentAssistantTab.addContent(getText);
            contentAssistantTab.sortSnippets(getText);
            contentAssistantTab.showSnippets("", true);
            paragraphArea.setText("");
        }
        
        else if (evt.getSource() == previewBtn) {
            int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.MCpreviewManual, cfgPkg.Text.MSGconfirmPreview, JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.NO_OPTION) return;
            
            String getText = formatContent();
            String[] snippets = contentAssistantTab.sortSnippets(getText);
            getText = contentAssistantTab.showSnippets(snippets, "", false);
            paragraphArea.setText(getText);
        }
        
        /*else if (evt.getSource() == viewBtn)
            toggleView(viewBtn, paragraphArea);*/
        
        else if (evt.getSource() == clearBtn) {
            int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.MSGclearText, cfgPkg.Text.MSGconfirmClear, JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.NO_OPTION) return;

            paragraphArea.setText("");
        }
        
        else if (evt.getSource() == closeBtn) {
            int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.MSGcloseWindow, cfgPkg.Text.MSGconfirmCloseWindow, JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                windowClose();
            }
        }        
    }
    
    private void windowClose() {
        setVisible(false);
        dispose();
    }
    
    private String formatContent() {
        String text = paragraphArea.getText();
        text = cfgPkg.Util.getFragmentString(text);
        text = cfgPkg.Util.cleanupTags1(text);
        text = cfgPkg.Util.cleanupTags2(text);

        return text;
    }

    /*private void toggleView(JButton button, JEditorPane editorPane) {
        String text = editorPane.getText();
        if (button.getText().equals("View")) {
            editorPane.setContentType("text/html");
            editorPane.setEditable(false);
            button.setText("Edit");
        } else {
            editorPane.setContentType("text/plain");
            editorPane.setEditable(true);
            button.setText("View");
            int pos = text.indexOf("<body>") + "<body>".length();
            text = text.substring(pos, text.indexOf("</body>")).trim();
            text = text.replace("    ", "");
        }
        editorPane.setText(text);
    }*/
}
