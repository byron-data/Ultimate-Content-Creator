package guiPkg;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import java.lang.Integer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.Document;
import javax.swing.text.html.parser.*;
import javax.swing.text.html.*;

import cfgPkg.*;

public class ContentAssistantTab extends ContentPanel {
    // Presentation
    private JButton     speedBtn    = new JButton(cfgPkg.GUI.speedTemplate);
    private JTextField  keywordsTxt = new JTextField();
    private JButton     contentBtn  = new JButton(cfgPkg.GUI.getContent);
    private JButton     manualBtn   = new JButton(cfgPkg.GUI.manual);
    private JButton     lsiBtn      = new JButton(cfgPkg.GUI.lsiAssistant);
    private JTextField  filterTxt   = new JTextField();
    private JComboBox   filterCombo;
    private JButton     filterBtn   = new JButton(cfgPkg.GUI.filter);
    protected JEditorPane contentArea = new JEditorPane();
    //private JTextArea contentArea   = new JTextArea();
    
    private SpeedTemplate  speedTemplate   = null;
    private ManualContent  manualContent   = null;

    private static String contentFindLimit = "25";
    private String all_articles            = "";
    private String[] snippets;

    private Vector word2Vector;
    private Vector word3Vector;

    public ContentAssistantTab(int tabWidth, int tabHeight, 
            ProjectSetupTab projectSetupTab, BatchProcessTab getInputsTab) {
        super(tabWidth, tabHeight, projectSetupTab, getInputsTab);

        Insets insets = new Insets(0,0,0,0);

        speedBtn.setLocation(380, 25);
        speedBtn.setSize(120, 15);
        speedBtn.setMargin(insets);
        speedBtn.addActionListener(this);
        speedBtn.setToolTipText(cfgPkg.Text.CPspeedBtn);
        pastePanel.add(speedBtn);

        JLabel keywordsLbl = new JLabel(cfgPkg.GUI.keywords);
        keywordsLbl.setLocation(515, 5);
        keywordsLbl.setSize(60, 20);
        this.add(keywordsLbl);
        
        keywordsTxt.setLocation(575, 5);
        keywordsTxt.setSize(265, 20);
        keywordsTxt.setToolTipText(cfgPkg.Text.CAkeywordsTxt);
        this.add(keywordsTxt);

        contentBtn.setLocation(845, 7);
        contentBtn.setSize(95, 15);
        contentBtn.setMargin(insets);
        contentBtn.addActionListener(this);
        contentBtn.setToolTipText(cfgPkg.Text.CAcontentBtn);
        this.add(contentBtn);

        manualBtn.setLocation(945, 7);
        manualBtn.setSize(95, 15);
        manualBtn.setMargin(insets);
        manualBtn.addActionListener(this);
        manualBtn.setToolTipText(cfgPkg.Text.CAmanualBtn);
        this.add(manualBtn);

        lsiBtn.setLocation(1050, 7);
        lsiBtn.setSize(120, 15);
        lsiBtn.setMargin(insets);
        lsiBtn.addActionListener(this);
        lsiBtn.setToolTipText(cfgPkg.Text.CPlsiBtn);
        this.add(lsiBtn);

        JLabel filterLbl = new JLabel(cfgPkg.GUI.filter);
        filterLbl.setLocation(515, 25);
        filterLbl.setSize(60, 20);
        this.add(filterLbl);
        
        filterTxt.setLocation(575, 25);
        filterTxt.setSize(265, 20);
        filterTxt.setToolTipText(cfgPkg.Text.CAfilterTxt);
        this.add(filterTxt);

        filterCombo = new JComboBox();
        filterCombo.setLocation(845, 25);
        filterCombo.setSize(200, 20);
        filterCombo.setToolTipText(cfgPkg.Text.CAfilterCombo);
        filterCombo.addActionListener(new actionAdapter(this));
        this.add(filterCombo);

        filterBtn.setLocation(1050, 27);
        filterBtn.setSize(120, 15);
        filterBtn.addActionListener(this);
        filterBtn.setToolTipText(cfgPkg.Text.CAfilterBtn);
        this.add(filterBtn);
        
        //contentArea.setLineWrap(true);
        contentArea.setContentType("text/html");
        JScrollPane contentSP = new JScrollPane(contentArea);
        contentSP.setLocation(515, 50);
        contentSP.setSize(655, 795);
        this.add(contentSP);

        resetArticleTxt();
    }

    public void actionExtra(ActionEvent evt) {
        if (evt.getSource() == lsiBtn) {
            if (cfgPkg.Const.liteVersion) {
                JOptionPane.showMessageDialog(null, cfgPkg.Text.liteMessage, cfgPkg.Text.liteMessageTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            cfgPkg.Files.UCCResearchBrowsers(cfgPkg.Const.lsiBrowser, "", "");
        }

        else if (evt.getSource() == speedBtn) {
            if (cfgPkg.Const.liteVersion) {
                JOptionPane.showMessageDialog(null, cfgPkg.Text.liteMessage, cfgPkg.Text.liteMessageTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            speedTemplate = new SpeedTemplate(this);
        }

        else if (evt.getSource() == contentBtn) {
            if (cfgPkg.Const.liteVersion) {
                JOptionPane.showMessageDialog(null, cfgPkg.Text.liteMessage, cfgPkg.Text.liteMessageTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            Cursor cursor = this.getCursor();
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

            //String isnare        = "(<a\\s+href=\"(http://www.isnare.com/\\?aid=[-+]?\\d+&ca=.*?)\".*?[^>]+>)"; // insnare article url match
            String isnare        = "(<a\\s+href=\"(http://www.isnare.com/\\?aid=[-+]?\\d+.*?)\".*?[^>]+>)"; // insnare article url match
            //String ezinearticles = "(<a\\s+href=\"(http://ezinearticles.com/\\?.*?&id=[-+]?\\d+?)\".*?[^>]+>)"; // ezinearticles article url match
            String ezinearticles = "(<a\\s+href=\"(http://ezinearticles.com/\\?.*?)\".*?[^>]+>)"; // ezinearticles article url match
            String wikipedia     = "(<a\\s+href=\"(/wiki/.*?)\".*?[^>]+>)";                                     // wikipedia article url match
            int    isnare_id        = 0;
            int    ezinearticles_id = 1;
            int    wikipedia_id     = 2;

            clearContent();
            Pattern p;
            int contentGetLimit;
            int total_count = 0;

            for (int site=1; site<2; site++) {
                URL searchUrl;
                String keywords = keywordsTxt.getText().trim().replace(' ', '+');
                try {
                    if (site == isnare_id) {
                        searchUrl = new URL("http://www.google.com.au/search?as_q=&hl=en&num=25&btnG=Google+Search&as_epq=" + keywords +
                                "&as_ft=i&as_qdr=all&as_occt=any&as_dt=i&as_sitesearch=isnare.com");
                        //searchUrl = new URL("http://www.isnare.com/search.php?SearchString=" + keywords + "&SearchIn=Title");
                        p = Pattern.compile(isnare, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                        contentGetLimit = 7;
                        // e.g. http://www.google.com.au/search?as_q=&hl=en&num=25&btnG=Google+Search&as_epq=internet+marketing&as_ft=i&as_qdr=all&as_occt=any&as_dt=i&as_sitesearch=isnare.com
                        // e.g. http://www.isnare.com/search.php?SearchString=dog-training&SearchIn=Title
                    } else if (site == ezinearticles_id) {
                        searchUrl = new URL("http://www.google.com/search?num=" + contentFindLimit + "&hl=en&lr=lang_en&as_qdr=all&q=" +
                            keywords + "+site%3Aezinearticles.com&btnG=Search&lr=lang_en");
                        p = Pattern.compile(ezinearticles, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                        contentGetLimit = 14 - total_count;
                        // e.g. http://www.google.com/search?num=25&hl=en&lr=lang_en&as_qdr=all&q=dog-training+site%3Aezinearticles.com&btnG=Search&lr=lang_en
                    } else {
                        searchUrl = new URL("http://en.wikipedia.org/wiki/Special:Search?search=" + keywords);
                        p = Pattern.compile(wikipedia, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                        contentGetLimit = 2;
                        // e.g. http://en.wikipedia.org/wiki/Special:Search?search=dog-training&go=Go
                    }
                } catch (MalformedURLException e) {
                    System.err.println(e.getMessage());
                    return;
                }

                try {
                    URLConnection connSearch = searchUrl.openConnection();
                    connSearch.setRequestProperty("User-agent","Mozilla/4.0"); //connSearch.connect();

                    int count = 0;
                    String lineSearch;
                    BufferedReader dataSearch = new BufferedReader(new InputStreamReader(connSearch.getInputStream()));

                    while ((lineSearch = dataSearch.readLine()) != null && count < contentGetLimit) {
                        Matcher m = p.matcher(lineSearch);
                        while (m.find()) {
                            String url = m.group(2);
                            if (site == wikipedia_id && lineSearch.indexOf("Relevance") == -1) continue;
                            
                            //System.out.println("url: ("+url+")"+cfgPkg.Const.linesep());
                            count++; total_count++;

                            url = url.replace("amp;","");
                            try {
                                if (site == isnare_id)
                                    searchUrl = new URL(url);
                                    //searchUrl = new URL("http://www.isnare.com"+url);
                                else if (site == ezinearticles_id)
                                    searchUrl = new URL(url);
                                else if (site == wikipedia_id)
                                    searchUrl = new URL("http://en.wikipedia.org"+url);
                            } catch (MalformedURLException e) {
                                System.err.println(e.getMessage());
                            }

                            URLConnection connArticle = searchUrl.openConnection();
                            connArticle.setRequestProperty("User-agent","Mozilla/4.0"); //connSearch.connect();

                            String lineArticle = "";
                            BufferedReader dataArticle = new BufferedReader(new InputStreamReader(connArticle.getInputStream()));
                            String article = "";
                            while ((lineArticle = dataArticle.readLine()) != null) {
                                article = article + lineArticle;
                            } // while

                            String startStr = "";
                            String endStr   = "";
                            if (site == isnare_id) {
                                startStr = "<p>";
                                endStr   = "</p>";
                            } else if (site == ezinearticles_id) {
                                //startStr = "<!-- google_ad_section_start -->";
                                startStr = "<div id=\"body\">";
                                endStr   = "<div class=\"sig\">";
                            } else if (site == wikipedia_id) {
                                startStr = "<!-- start content -->";
                                endStr   = "<p><a name=\"See_also\"";
                            }

                            int articleStart = article.indexOf(startStr);
                            if (articleStart != -1) {
                                int articleStartLen = startStr.length();
                                int articleEnd = article.indexOf(endStr, articleStart+articleStartLen);
                                if (articleEnd == -1) {
                                    if (site == ezinearticles_id) {
                                        endStr = "<div id=\"sig\"";
                                        articleEnd = article.indexOf(endStr, articleStart+articleStartLen);
                                    } else if (site == wikipedia_id) {
                                        endStr = "<p><a name=\"References\"";
                                        articleEnd = article.indexOf(endStr, articleStart+articleStartLen);
                                    }
                                }
                                if (articleEnd != -1) {
                                    article = article.substring(articleStart+articleStartLen, articleEnd);
                                    article = cfgPkg.Util.cleanupTags1(article);
                                    
                                    if (site == ezinearticles_id)
                                        article = article.replace("<div><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td valign=\"top\">", "");
                                    else if (site == wikipedia_id) {
                                        article = article.replaceAll("<div class=\"messagebox.*?</div>", "");
                                        article = article.replaceAll("<span class=\"editsection.*?</span>", "");

                                        Pattern tags    = Pattern.compile("<span class=\"mw-headline\".*?>(.*?)</span>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
                                        Matcher matcher = tags.matcher(article);
                                        while (matcher.find()) {
                                            article = article.replace(matcher.group(0), matcher.group(1));
                                        }

                                        article = article.replace("â€¢", "");
                                        article = article.replace("â€™", "’");
                                        article = article.replace("â€œ", "“");
                                        article = article.replace("â€?", "");
                                        article = article.replace("â€", "”");
                                        article = article.replace("?", "");
                                    }
                                    addContent(article);
                                }
                            }

                            if (count == contentGetLimit) {
                                break;
                            }
                        }
                    } // while

                    dataSearch.close();
                } catch (IOException e) {
                    contentArea.setText("IO Error:" + e.getMessage());
                    System.err.println("IO Error:" + e.getMessage());
                }
            }

            all_articles = cfgPkg.Util.cleanupTags2(all_articles);
            sortSnippets(all_articles);
            showSnippets("", true);
            this.setCursor(cursor);
            
        } else if (evt.getSource() == manualBtn) {
            if (cfgPkg.Const.liteVersion) {
                JOptionPane.showMessageDialog(null, cfgPkg.Text.liteMessage, cfgPkg.Text.liteMessageTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            manualContent = new ManualContent(this);
            
        } else if (evt.getSource() == filterBtn) {
            if (cfgPkg.Const.liteVersion) {
                JOptionPane.showMessageDialog(null, cfgPkg.Text.liteMessage, cfgPkg.Text.liteMessageTitle, JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            showSnippets(filterTxt.getText().trim(), true);
        }
    }
    
    protected void clearContent() {
        all_articles = "";
        filterTxt.setText("");
        word2Vector  = new Vector();
        word3Vector  = new Vector();
    }
    
    protected String addContent(String article) {
        all_articles = all_articles + article + "<br>";
        return all_articles;
    }
    
    protected String[] sortSnippets(String all_articles) {
        String[] tempArray = all_articles.split("<br>");

        Arrays.sort(tempArray);
        int j = 0;
        for (int i=1; i<tempArray.length; i++)
        {
            if (!tempArray[i].trim().equals(tempArray[i-1].trim()) && !tempArray[i].trim().equals("") && tempArray[i].indexOf("©") == -1) {
                //System.out.println("[" + tempArray[i].trim() + "]");
                tempArray[j++] = tempArray[i].trim();
            }
        }

        snippets = new String[j];
        System.arraycopy(tempArray, 0, snippets, 0, j);

        List tempList = Arrays.asList(snippets);
        Collections.shuffle(tempList);
        snippets = (String[]) tempList.toArray();
        
        return snippets;
    }
    
    protected String showSnippets(String filter, boolean main) {
        return showSnippets(snippets, filter, main);
    }
    
    protected String showSnippets(String[] snippets, String filter, boolean main) {
        String show  = "";
        String tagS  = "";
        String tagE  = "";
        String color = "";
        int    count = 0;
        
        for (int i=0; i<snippets.length; i++) {
            if ((count % 2) == 0)
                color = " color=\"#000000\">";
            else
                color = " color=\"#0000FF\">";

            tagS = "";
            tagE = "";
            if (snippets[i].indexOf("<ul") == 0) {
                snippets[i] = snippets[i].replace("<ul>", "<ul"+color);
            } else if (snippets[i].indexOf("<ol") == 0) {                
                snippets[i] = snippets[i].replace("<ol>", "<ol"+color);
            } else if (snippets[i].indexOf("<dl") == 0) {                
                snippets[i] = snippets[i].replace("<dl>", "<dl"+color);
            } else {
                tagS = "<p" + color;
                tagE = "</p>";
            }

            if (filter.equals("")) {
                //show = show + snippets[i] + "\n\n";
                show = show + tagS + snippets[i] + tagE;
                count++;
                //System.out.println("[" + snippets[i].trim() + "]");
            } else {
                String[] tempArray = (filter+",").split(",");
                for (int j=0; j<tempArray.length; j++)
                {
                    if (snippets[i].toLowerCase().indexOf(tempArray[j].trim()) != -1) {
                        //show = show + snippets[i] + "\n\n";
                        show = show + tagS + snippets[i] + tagE;
                        count++;
                        //System.out.println("[" + snippets[i].trim() + "]");
                        break;
                    }
                }
            }
            
            if (filter.equals("") && main) {
                cfgPkg.Util.buildWordCounts(snippets[i], 2, word2Vector);
                cfgPkg.Util.buildWordCounts(snippets[i], 3, word3Vector);
            }
        }

        if (main)
            contentArea.setText(show);

        if (filter.equals("") && main) {
            int size = filterCombo.getModel().getSize();
            filterCombo.setEditable(true);
            //System.out.println(cfgPkg.Const.linesep()+cfgPkg.Const.linesep()+"2 word sets");
            displayComboCounts(word2Vector, 5);
            //System.out.println(cfgPkg.Const.linesep()+"3 word sets");
            displayComboCounts(word3Vector, 5);
            for (int i=size; i>0; i--)
                filterCombo.removeItemAt(i-1);
            filterCombo.setEditable(false);
            filterTxt.setText("");
        }
        
        return show;
    }
    
    private void displayComboCounts(Vector wordVector, int countMin) {
        Vector thisVector;
        for (int i=0; i<wordVector.size(); i++) {
            thisVector = (Vector)wordVector.get(i);
            int count = Integer.decode(thisVector.get(1).toString());
            if (count >= countMin) {
                filterCombo.addItem(thisVector.get(0).toString());
                //System.out.println(thisVector.get(0).toString() + " " + thisVector.get(1).toString());
            }
        }
    }
    
    void combo_actionPerformed(ActionEvent e) {
        filterTxt.setText(filterCombo.getSelectedItem().toString());
    }
}

class actionAdapter implements java.awt.event.ActionListener {
    ContentAssistantTab adaptee;
    
    actionAdapter(ContentAssistantTab adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(ActionEvent e) {
        adaptee.combo_actionPerformed(e);
    }
}
