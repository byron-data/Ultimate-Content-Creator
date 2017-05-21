package guiPkg;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.BevelBorder;

public class SpeedTemplate extends JFrame implements ActionListener {
    private   JPanel      mainPanel    = new JPanel();
    private   JPanel      articlePanel = new JPanel();
    private   JScrollPane articleSP    = new JScrollPane(articlePanel);
    private   JPanel      cmdPanel     = new JPanel();
    protected JTextField  keywordTxt   = new JTextField();
    private   JButton     copyBtn      = new JButton(cfgPkg.GUI.copyText);
    private   JLabel      titleLbl     = new JLabel(cfgPkg.GUI.title);
    protected JTextField  titleTxt     = new JTextField();
    private   JButton     densityBtn   = new JButton(cfgPkg.GUI.density);
    protected JTextField  densityTxt   = new JTextField();
    protected JTextField  countTxt     = new JTextField();
    private   JComboBox   headlineCombo;
    private   JButton     resetBtn     = new JButton(cfgPkg.GUI.resetAllText);
    private   JButton     closeBtn     = new JButton(cfgPkg.GUI.close);
    
    private   int         buttonHeight    = 20;
    private   int         space           = 5;
    private   int         extraHeight     = 0;
    private   int         writingWidth    = 700;
    private   int         writingHeight   = 160;
    private   int         paragraphHeight = 0;
    private   int         paragraphWidth  = 0;
    
    private   Vector      paragraphVector = new Vector();
    
    private   ContentAssistantTab contentAssistantTab;
    
    public SpeedTemplate(ContentAssistantTab contentAssistantTab) {
        this.contentAssistantTab = contentAssistantTab;
        setIconImage(new ImageIcon(SpeedTemplate.class.getResource("images/UCC.jpg")).getImage());
        setTitle(cfgPkg.GUI.speedTemplateTitle);

        getContentPane().setLayout(new GridLayout(1, 1));
        getContentPane().add(mainPanel);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                copyArticle();
                windowClose();
            }
        });
        
        cmdPanel.setLayout(new FlowLayout());
        cmdPanel.setPreferredSize(new Dimension(writingWidth*2, buttonHeight+2*space));
        mainPanel.add(cmdPanel);
        extraHeight = getPreferredSize().height + 2*space;
                
        copyBtn.setPreferredSize(new Dimension(100, buttonHeight));
        copyBtn.addActionListener(this);
        copyBtn.setToolTipText(cfgPkg.Text.STcopyArticle);
        cmdPanel.add(copyBtn);
        
        titleLbl.setPreferredSize(new Dimension(28, buttonHeight));
        cmdPanel.add(titleLbl);
        
        titleTxt.setPreferredSize(new Dimension(400, buttonHeight));
        titleTxt.setText("");
        titleTxt.setToolTipText(cfgPkg.Text.STtitleTxt);
        cmdPanel.add(titleTxt);

        keywordTxt.setPreferredSize(new Dimension(100, buttonHeight));
        keywordTxt.setText(cfgPkg.GUI.keyword);
        keywordTxt.setToolTipText(cfgPkg.Text.CPkeywordTxt);
        cmdPanel.add(keywordTxt);

        densityBtn.setPreferredSize(new Dimension(100, buttonHeight));
        densityBtn.addActionListener(this);
        densityBtn.setToolTipText(cfgPkg.Text.CPdensityBtn);
        cmdPanel.add(densityBtn);

        densityTxt.setPreferredSize(new Dimension(40, buttonHeight));
        densityTxt.setText("");
        densityTxt.setEditable(false);
        densityTxt.setToolTipText(cfgPkg.Text.CPdensityTxt);
        cmdPanel.add(densityTxt);

        countTxt.setPreferredSize(new Dimension(40, buttonHeight));
        countTxt.setText("");
        countTxt.setEditable(false);
        countTxt.setToolTipText(cfgPkg.Text.CPcountTxt);
        countTxt.setHorizontalAlignment(JTextField.RIGHT);
        cmdPanel.add(countTxt);

        headlineCombo = new JComboBox(cfgPkg.CopyWriting.proven);
        headlineCombo.setEditable(true);
        headlineCombo.setLocation(260, 0);
        headlineCombo.setPreferredSize(new Dimension(240, buttonHeight));
        headlineCombo.setToolTipText(cfgPkg.Text.CPheadlineCombo);
        cmdPanel.add(headlineCombo);
        
        Insets insets = new Insets(0,0,0,0);

        resetBtn.setPreferredSize(new Dimension(100, buttonHeight));
        resetBtn.addActionListener(this);
        resetBtn.setToolTipText(cfgPkg.Text.STresetArticle);
        resetBtn.setMargin(insets);
        cmdPanel.add(resetBtn);
        
        closeBtn.setPreferredSize(new Dimension(100, buttonHeight));
        closeBtn.addActionListener(this);
        closeBtn.setToolTipText(cfgPkg.Text.MSGcloseWindow);
        closeBtn.setMargin(insets);
        cmdPanel.add(closeBtn);

        articlePanel.setLayout(new BoxLayout(articlePanel, BoxLayout.Y_AXIS));
        mainPanel.add(articleSP);

        startArticle();

        int preferredHeight = articleSP.getPreferredSize().height + cmdPanel.getPreferredSize().height + extraHeight + space;
        setPreferredSize(new Dimension(writingWidth*2+6*space, preferredHeight));
        setMinimumSize(new Dimension(writingWidth*2+6*space, preferredHeight));
        pack();
        setVisible(true);
    }
    
    private void buildDensity() {
        Vector countVector = new Vector();
        if (!cfgPkg.Const.liteVersion) {
            String article = "";
            for (int i=0; i<paragraphVector.size(); i++) {
                ParagraphPanel pp = (ParagraphPanel)paragraphVector.get(i);
                article = article + pp.paragraphArea.getText();
            }
            String display = Double.toString(cfgPkg.Util.buildDensity(article, keywordTxt.getText(), countVector));
            if (display.indexOf(".") != -1)
                display = display.substring(0, display.indexOf(".")+2) + "%";
            else
                display = display + "%";
            densityTxt.setText(display);
            countTxt.setText(countVector.get(0).toString());
        }
    }

    public void actionPerformed(ActionEvent evt) {
        if (evt.getSource() == densityBtn) {
            buildDensity();
        }

        else if (evt.getSource() == copyBtn) {
            copyArticle();
        }

        else if (evt.getSource() == resetBtn) {
            int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.STresetArticle, cfgPkg.Text.MSGrevertDefaults, JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION)
                resetAllParagraphs(true);
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
    
    private void copyArticle() {
        int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.STcopyArticle, cfgPkg.Text.MSGconfirmCopy, JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.NO_OPTION) return;

        contentAssistantTab.titleTxt.setText(titleTxt.getText());
        String article = "";
        for (int i=0; i<paragraphVector.size(); i++) {
            ParagraphPanel pp = (ParagraphPanel)paragraphVector.get(i);
            article = article + "<p>" + pp.paragraphArea.getText() + "</p>";
        }
        contentAssistantTab.articleTxt.setText(article);

        Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable transferText = new StringSelection(titleTxt.getText() + cfgPkg.Const.linesep() + cfgPkg.Const.linesep() + article);
        cb.setContents(transferText, null);
    }
    
    private class ParagraphPanel extends JPanel implements ActionListener {
        private JPanel      buttonPanel       = new JPanel();
        private JButton     addBtn            = new JButton(cfgPkg.GUI.addParagraph);
        private JButton     deleteBtn         = new JButton(cfgPkg.GUI.deleteParagraph);
        private JLabel      keywordLbl        = new JLabel(cfgPkg.GUI.keywordParagraph);
        private JTextField  keywordTxt        = new JTextField("");
        private JButton     resetParagraphBtn = new JButton(cfgPkg.GUI.resetParagraph);
        private JButton     resetSnippetsBtn  = new JButton(cfgPkg.GUI.resetSnippets);
        
        private JPanel      textPanel         = new JPanel();
        private JTextArea   paragraphArea     = new JTextArea();
        private JEditorPane snippetsArea      = new JEditorPane();
        
        private int         buttonHeight      = 18;
        private int         buttonWidth       = 160;

        public ParagraphPanel(int position) {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            
            buttonPanel.setLayout(new FlowLayout());
            buttonPanel.setBounds(0, 0, writingWidth*2, buttonHeight);
            
            addBtn.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
            addBtn.addActionListener(this);
            addBtn.setToolTipText(cfgPkg.Text.STaddBtn);
            buttonPanel.add(addBtn);
            if (position == -1)
                addBtn.setEnabled(false);

            deleteBtn.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
            deleteBtn.addActionListener(this);
            deleteBtn.setToolTipText(cfgPkg.Text.STdeleteParagraph);
            buttonPanel.add(deleteBtn);
            if (position == 0 || position == -1)
                deleteBtn.setEnabled(false);

            buttonPanel.add(keywordLbl);
            keywordTxt.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
            keywordTxt.addActionListener(this);
            keywordTxt.setToolTipText(cfgPkg.Text.STkeywordTxt);
            buttonPanel.add(keywordTxt);
            
            resetParagraphBtn.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
            resetParagraphBtn.addActionListener(this);
            resetParagraphBtn.setToolTipText(cfgPkg.Text.STresetParagraph);
            buttonPanel.add(resetParagraphBtn);

            resetSnippetsBtn.setPreferredSize(new Dimension(buttonWidth, buttonHeight));
            resetSnippetsBtn.addActionListener(this);
            resetSnippetsBtn.setToolTipText(cfgPkg.Text.STresetSnippets);
            buttonPanel.add(resetSnippetsBtn);

            this.add(buttonPanel);
            
            textPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));

            paragraphArea.setLineWrap(true);
            paragraphArea.setToolTipText(cfgPkg.Text.STparagraphArea);
            JScrollPane paragraphSP = new JScrollPane(paragraphArea);
            paragraphSP.setPreferredSize(new Dimension(writingWidth, writingHeight));
            textPanel.add(paragraphSP);
            
            snippetsArea.setContentType("text/html");
            snippetsArea.setToolTipText(cfgPkg.Text.STsnippetsArea);
            JScrollPane snippetsSP = new JScrollPane(snippetsArea);
            snippetsSP.setPreferredSize(new Dimension(writingWidth, writingHeight));
            textPanel.add(snippetsSP);
            
            this.add(textPanel);
            paragraphHeight = this.getPreferredSize().height;
            paragraphWidth  = this.getPreferredSize().width;
        }

        private void setText(int position) {
            if (position == 0) {
                paragraphArea.setText(cfgPkg.Text.STopenDefaultText1 + keywordTxt.getText() + "." + cfgPkg.Const.linesep() + cfgPkg.Const.linesep() +
                        cfgPkg.Text.STopenDefaultText2 + keywordTxt.getText() + "." + cfgPkg.Const.linesep() + cfgPkg.Const.linesep() +
                        cfgPkg.Text.STopenDefaultText3 + cfgPkg.Const.linesep() + cfgPkg.Const.linesep() +
                        cfgPkg.Text.STopenDefaultText4);
            } else if (position == -1) {
                paragraphArea.setText(cfgPkg.Text.STcloseDefaultText1 + cfgPkg.Const.linesep() + cfgPkg.Const.linesep() +
                        cfgPkg.Text.STcloseDefaultText2 + cfgPkg.Const.linesep() + cfgPkg.Const.linesep() +
                        cfgPkg.Text.STcloseDefaultText3);
            } else {
                if (keywordTxt.getText().equals("") || keywordTxt.getText().indexOf("keyword") == 0)
                    keywordTxt.setText(cfgPkg.GUI.keyword+position);
                String nextSubtopic = ".";
                int index = paragraphVector.indexOf(this);
                if (index < (paragraphVector.size()-2)) { // Exclude last subtopic
                    ParagraphPanel pp = (ParagraphPanel)paragraphVector.get(index+1);
                    nextSubtopic = cfgPkg.Text.STsubDefaultText5 + pp.keywordTxt.getText() + nextSubtopic;
                }
                paragraphArea.setText(cfgPkg.Text.STsubDefaultText1 + keywordTxt.getText() + "." + cfgPkg.Const.linesep() + cfgPkg.Const.linesep() +
                        cfgPkg.Text.STsubDefaultText2 + keywordTxt.getText() + cfgPkg.Text.STsubDefaultText3 + cfgPkg.Const.linesep() + cfgPkg.Const.linesep() +
                        cfgPkg.Text.STsubDefaultText4 + keywordTxt.getText() + nextSubtopic);
            }
        }
        
        public void actionPerformed(ActionEvent evt) {
            if (evt.getSource() == addBtn) {
                addParagraph(paragraphVector.indexOf(this), true);
            }
        
            else if (evt.getSource() == deleteBtn) {
                int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.STdeleteParagraph, cfgPkg.Text.MSGconfirmDelete, JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION)
                    delParagraph(paragraphVector.indexOf(this));
            }
            
            else if (evt.getSource() == resetParagraphBtn) {
                int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.STresetParagraph, cfgPkg.Text.MSGrevertDefaults, JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION)
                    setText(paragraphVector.indexOf(this));
            }
            
            else if (evt.getSource() == resetSnippetsBtn) {
                int choice = JOptionPane.showConfirmDialog(null, cfgPkg.Text.STresetSnippets, cfgPkg.Text.MSGconfirmResetSnippets, JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION)
                    snippetsArea.setText(contentAssistantTab.showSnippets(keywordTxt.getText(), false));
            }
        }
    }
    
    private void startArticle() {
        addParagraph(0, false);
        addParagraph(1, false);
        addParagraph(2, false);
        addParagraph(3, false);
        addParagraph(-1, false);

        resetAllParagraphs(false);
        resetArticlePanel();
    }
    
    private void resetAllParagraphs(boolean snippets) {
        for (int i=paragraphVector.size()-1; i>=0; i--) {
            ParagraphPanel pp = (ParagraphPanel)paragraphVector.get(i);
            if (i == (paragraphVector.size()-1)) {
                pp.setText(-1);
                if (snippets) pp.snippetsArea.setText(contentAssistantTab.showSnippets(pp.keywordTxt.getText(), false));
            } else {
                pp.setText(i);
                if (snippets) pp.snippetsArea.setText(contentAssistantTab.showSnippets(pp.keywordTxt.getText(), false));
            }
        }
    }
    
    private void addParagraph(int position, boolean insert) {
        if (insert) position++;
        ParagraphPanel pp = new ParagraphPanel(position);

        if (insert) {
            paragraphVector.insertElementAt(pp, position);
            pp.setText(position);
        } else {
            paragraphVector.addElement(pp);
        }

        if (insert)
            resetArticlePanel();
    }
    
    private void delParagraph(int position) {
        paragraphVector.remove(position);
        resetArticlePanel();
    }
    
    private void resetArticlePanel() {
        articlePanel = new JPanel();
        articleSP.setViewportView(articlePanel);
        for (int i=0; i<paragraphVector.size(); i++) {
            ParagraphPanel pp = (ParagraphPanel)paragraphVector.get(i);
            articlePanel.add(pp);
        }
        
        articlePanel.setPreferredSize(new Dimension(paragraphWidth, paragraphVector.size()*(paragraphHeight+space)));
        articlePanel.validate();
    }
}
