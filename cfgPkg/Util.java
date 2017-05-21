package cfgPkg;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    /**
     * Creates a new instance of Files
     */
    public Util() {}

    static public boolean checkForWord(int advertPos, String section, String searchTxt) {
        if (advertPos > 0) {
            section = section.substring(advertPos-1);
            char character = section.charAt(0);
            if (character >= 'a' && character <= 'z') return false;
            if (character >= 'A' && character <= 'Z') return false;
            section = section.substring(1);
        } else
            section = section.substring(advertPos);

        if (section.length() > searchTxt.length()) {
            char character = section.charAt(searchTxt.length());
            if (character >= 'a' && character <= 'z') return false;
            if (character >= 'A' && character <= 'Z') return false;
        }

        return true;
    }
    
    static public void buildWordCounts(String snippet, int wordCount, Vector wordVector) {
        Vector thisVector;

        snippet = snippet.replaceAll("<.*?>", "");
        snippet = snippet.replaceAll("[\\^]", "");
        snippet = snippet.replace("\r", " ");
        snippet = snippet.replace("\n", " ");
        String [] phrases = snippet.split("[.,:;!?\"“”(){}\\[\\]]");
        for (int p=0; p<phrases.length; p++) {
            String [] words = phrases[p].split(" ");
            boolean found = false;

            String combo = "";
            for (int i=0; i<words.length; i++) {
                if (i >= (wordCount-1)) {
                    combo = "";
                    int count = 0;
                    for (int k=0; k<wordCount; k++) {
                        if (!words[i-(wordCount-1)+k].trim().equals("") && !words[i-(wordCount-1)+k].trim().equals("-")) {
                            if (k == 0 || ((k+1) == wordCount)) {
                                if (words[i-(wordCount-1)+k].trim().equals("a"))
                                    continue;
                                else if (words[i-(wordCount-1)+k].trim().equals("to"))
                                    continue;
                                else if (words[i-(wordCount-1)+k].trim().equals("and"))
                                    continue;
                            }
                            combo = combo + " " + words[i-(wordCount-1)+k].trim();
                            count++;
                        }
                    }
                    if (count < wordCount) continue;
                    combo = combo.trim().toLowerCase();
                    
                    for (int j=0; j<wordVector.size() && !found; j++) {
                        thisVector = (Vector)wordVector.get(j);
                        if (thisVector.get(0).toString().equals(combo)) {
                            count = Integer.decode(thisVector.get(1).toString()) + 1;
                            thisVector.set(1, count);
                            found = true;
                        }
                    }

                    if (!found) {
                        thisVector = new Vector();
                        thisVector.addElement(combo);
                        thisVector.addElement(1);
                        wordVector.addElement(thisVector);
                    }
                }
            }
        }
    }
    
    static public double buildDensity(String article, String keyword, Vector countVector) {
        double count      = 0;
        double totalCount = 0;

        article = article.replaceAll("<.*?>", "");
        article = article.replaceAll("[\\^]", "");
        article = article.replace("\r", " ");
        article = article.replace("\n", " ");
        keyword = keyword.toLowerCase();
        String [] phrases = article.split("[.,:;!?\"“”(){}\\[\\]]");

        for (int p=0; p<phrases.length; p++) {
            String [] words = phrases[p].split(" ");
            for (int b=0; b<words.length; b++) {
                if (!words[b].trim().equals(""))
                    totalCount++;
            }
            
            if (keyword.equals("")) continue; // nothing to count!
            
            String phraseTxt = phrases[p].toLowerCase();
            int prevPos      = 0;
            while (true/*!phraseTxt.equals("")*/) {
                int keywordPos = phraseTxt.substring(prevPos).indexOf(keyword);
                if (keywordPos != -1) {
                    if (checkForWord(keywordPos+prevPos, phraseTxt, keyword))
                        count++;
                    prevPos = prevPos + keywordPos+keyword.length();
                    //phraseTxt = phraseTxt.substring(keywordPos+keyword.length());
                } else
                    break;
            }
        }

        String totalCountStr = Double.toString(totalCount);
        totalCountStr        = totalCountStr.substring(0, totalCountStr.indexOf("."));
        int intTotalCount    = Integer.parseInt(totalCountStr);
        countVector.addElement(intTotalCount);
        if (totalCount == 0.0)
            return 0.0;
        else
            return count/totalCount*100.0;
    }
    
    static public String getFragmentString(String text) {
        int pos = text.indexOf("<!--StartFragment-->");
        if (pos != -1) {
            pos = pos + "<!--StartFragment-->".length();
            text = text.substring(pos, text.lastIndexOf("<!--EndFragment-->"));
        } else {
            pos = text.indexOf("<body>");
            if (pos != -1) {
                pos = pos + "<body>".length();
                text = text.substring(pos, text.lastIndexOf("</body>"));
            } else {
                text = text.replace("<html>", "");
                text = text.replace("</html>", "");
                text = text.replace("<head>", "");
                text = text.replace("</head>", "");
                text = text.replace("<body>", "");
                text = text.replace("</body>", "");                        
            }
            
            text = text.replaceAll("<div.*?>", "");
            text = text.replaceAll("<p.*?>", "<p>");
            text = text.replaceAll("<span.*?>", "");
            text = text.replaceAll("<ul.*?>", "<ul>");
            text = text.replaceAll("<ol.*?>", "<ol>");
            text = text.replaceAll("<dl.*?>", "<dl>");
        }
        
        return text;
    }
    
    static public String cleanupTags1(String article) {
        article = article.replace("\n", "");
        article = article.replace("\r", "");
        article = article.replaceAll("<script .*?</script>", "");
        Pattern tags = Pattern.compile("<a .*?>(.*?)</a>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = tags.matcher(article);
        while (matcher.find()) {
            //System.out.println(matcher.group(0) + " " + matcher.group(1));
            article = article.replace(matcher.group(0), matcher.group(1));
        }
        article = article.replaceAll("<h.*?>.*?</h.*?>", "");
        article = article.replaceAll("<img.*?>", "");
        article = article.replaceAll("<table.*?>.*?</table>", "");
        article = article.replaceAll("<strong></strong>", "");
        article = article.replaceAll("<sup.*?</sup>", "");

        return article;
    }
    
    static public String cleanupTags2(String all_articles) {
        all_articles = all_articles.replaceAll("<br.*?>", "<br>");

        all_articles = all_articles.replaceAll("<div.*?>", "");
        all_articles = all_articles.replaceAll("</div>", "");
        all_articles = all_articles.replaceAll("<DIV.*?>", "");
        all_articles = all_articles.replaceAll("</DIV>", "");

        all_articles = all_articles.replaceAll("<p.*?>", "");
        all_articles = all_articles.replaceAll("</p>", "<br>");
        all_articles = all_articles.replaceAll("<P.*?>", "");
        all_articles = all_articles.replaceAll("</P>", "<br>");

        all_articles = all_articles.replaceAll("<li.*?>", "<li>");
        all_articles = all_articles.replaceAll("<LI.*?>", "<li>");

        all_articles = all_articles.replaceAll("<ul.*?>", "<ul>");
        all_articles = all_articles.replaceAll("</ul>", "</ul><br>");
        all_articles = all_articles.replaceAll("<UL.*?>", "<ul>");
        all_articles = all_articles.replaceAll("</UL>", "</ul><br>");

        all_articles = all_articles.replaceAll("<ol.*?>", "<ol>");
        all_articles = all_articles.replaceAll("</ol>", "</ol><br>");
        all_articles = all_articles.replaceAll("<OL.*?>", "<ol>");
        all_articles = all_articles.replaceAll("</OL>", "</ol><br>");

        all_articles = all_articles.replaceAll("<dl.*?>", "<dl>");
        all_articles = all_articles.replaceAll("</dl>", "</dl><br>");
        all_articles = all_articles.replaceAll("<DL.*?>", "<dl>");
        all_articles = all_articles.replaceAll("</DL>", "</dl><br>");
        
        all_articles = all_articles.replaceAll("<span.*?>", "");
        all_articles = all_articles.replaceAll("</span>", ""); // replace with <br> instead ???
        all_articles = all_articles.replaceAll("<SPAN.*?>", "");
        all_articles = all_articles.replaceAll("</SPAN>", ""); // replace with <br> instead ???

        return all_articles;
    }
    
    static public String insertAdvert(String fileTxt, String searchTxt, Vector advertCode) {
        int pos = fileTxt.indexOf("</head>"); // look for end of the head section
        if (pos == -1)
            pos = fileTxt.indexOf("</HEAD>");
        if (pos == -1) return fileTxt;

        pos = pos + "</head>".length();
        String newFileTxt = fileTxt.substring(0, pos);
        fileTxt = fileTxt.substring(pos);

        int numberOfChars = fileTxt.length();
        int openTagPos    = 0;
        int closeTagPos   = 0;
        int advertNum     = 1;

        while (!fileTxt.equals("")) {
            String thisTxt = fileTxt;
            openTagPos = fileTxt.indexOf("<");
            if (openTagPos != -1)
                thisTxt = thisTxt.substring(0, openTagPos);
            int prevPos = 0;
            
            while (true/*!thisTxt.equals("")*/) {
                int advertPos = thisTxt.substring(prevPos).indexOf(searchTxt);
                //int advertPos = thisTxt.indexOf(searchTxt);
                if (advertPos != -1) {
                    if (cfgPkg.Util.checkForWord(advertPos+prevPos, thisTxt, searchTxt))
                    {
                        newFileTxt = newFileTxt + thisTxt.substring(prevPos, advertPos+prevPos) + (String)advertCode.get(advertNum);
                        if (++advertNum == advertCode.size()) advertNum = 1;
                    } else {
                        newFileTxt = newFileTxt + thisTxt.substring(prevPos, advertPos+prevPos+searchTxt.length());
                    }
                    prevPos = prevPos + advertPos+searchTxt.length();
                    //thisTxt = thisTxt.substring(advertPos+searchTxt.length());
                } else {
                    newFileTxt = newFileTxt + thisTxt.substring(prevPos);
                    break;
                }
            }

            String[] exceptions = { "<script", "</script>", "<a", "</a>", "<?php", "?>", 
                "<h1", "</h1>", "<h2", "</h2>", "<h3", "</h3>", "<h4", "</h4>", "<h5", "</h5>", "<h6", "</h6>" };
            boolean  exceptionFound = false;
            boolean  exceptionBreak = false;
            
            if (openTagPos != -1) {
                for (int i=0; i<exceptions.length && !exceptionFound;) {
                    int openPos = fileTxt.indexOf(exceptions[i]);
                    if (openTagPos != openPos)
                        openPos = fileTxt.indexOf(exceptions[i].toUpperCase());
                    if (openTagPos == openPos) {
                        exceptionFound = true;
                        int closePos = fileTxt.indexOf(exceptions[i+1]);
                        if (closePos == -1)
                            closePos = fileTxt.indexOf(exceptions[i+1].toUpperCase());
                        if (closePos != -1) {
                            newFileTxt = newFileTxt + fileTxt.substring(openPos, closePos+exceptions[i+1].length());
                            fileTxt = fileTxt.substring(closePos+exceptions[i+1].length());
                       } else {
                            newFileTxt = newFileTxt + fileTxt.substring(openPos);
                            exceptionBreak = true;
                        }
                    }

                    i = i + 2;
                }

                if (!exceptionFound) {
                    //closeTagPos = fileTxt.indexOf(">"); // doesn't always work, make sure ">" is after "<"
                    closeTagPos = fileTxt.substring(openTagPos+1).indexOf(">")+openTagPos+1;
                    if (closeTagPos != -1) {
                        newFileTxt = newFileTxt + fileTxt.substring(openTagPos, closeTagPos+1);
                        fileTxt    = fileTxt.substring(closeTagPos+1);
                    } else {
                        newFileTxt = newFileTxt + fileTxt.substring(openTagPos);
                        exceptionBreak = true;
                    }
                }
                
                if (exceptionBreak) break;                
            } else {
                //newFileTxt = newFileTxt + fileTxt; // not needed?
                System.err.println("Files.insertAdverts no more tags");
                break;
            }           
        }
        
        return newFileTxt;
    }    
}
