package cfgPkg;

public class GUI {
    // Window titles
    public final static String  unnamedProject              = "Unnamed project";
    public final static String  speedTemplateTitle          = "Speed Writing Template";
    public final static String  manualContentTitle          = "Manual Content Adder";
    
    // Table headers
    public final static String headersTags[]                = { "Category", "Open Tag", "Close Tag", "Token" };
    public final static String headersGenerated[]           = { "Index Name", "File Name" };
    public final static String headersRSS[]                 = { "Search", "Category", "Description", "URL" };
    public final static String headersText[]                = { "Search Text", "Replace Text" };
    
    // Combo Box values
    public final static Object[] stripOptions               = {"All HTML", "Non Preserved HTML", "Cancel"};
    
    // Check Box values
    public final static String nativeEdit                   = "Use native desktop settings for editing files";
    public final static String nativeBrowse                 = "Use native desktop settings for browsing files";
    public final static String selectAll                    = "Select all";
    public final static String bold                         = "Bold";
    public final static String italics                      = "Italics";
    public final static String underline                    = "Underline";
    public final static String regExp                       = "RegExp";
    
    // Radio Button values
    public final static String indexLinks                   = "links only";
    public final static String indexAndTextLinks            = "links and text";

    // Tab names
    public final static String contentAssistantStr          = "Content Assistant";
    public final static String grabContentStr               = "Grab Content";
    public final static String batchProcessStr              = "Batch Process and Index Generation";
    public final static String rssFeedsStr                  = "RSS Feeds";
    public final static String popupsStr                    = "Contextual Ads";
    public final static String textStr                      = "Text Replace";
    public final static String projectSetupStr              = "Project Setup";

    // Generic values
    public final static String inputDirectory               = "Input Directory";
    public final static String outputDirectory              = "Output Directory";
    public final static String snippetCharacters            = "Snippet Characters";
    public final static String fileExtension                = "File Extension";
    public final static String previewCharacters            = "Preview Characters";
    public final static String contentTemplate              = "Content Template";
    public final static String indexTemplate                = "Index Template";
    public final static String select                       = "Select";

    public final static String density                      = "Density";
    public final static String title                        = "Title";
    public final static String saturation                   = "Saturation";
    public final static String author                       = "Author";
    public final static String article                      = "Article";
    public final static String about                        = "About";
    public final static String view                         = "View";
    public final static String edit                         = "Edit";
    public final static String the                          = "the";
    public final static String keyword                      = "keyword";
    public final static String create                       = "Create";
    public final static String research                     = "Research";
    public final static String clear                        = "Clear";
    public final static String file                         = "File";
    public final static String newStr                       = "New";
    public final static String open                         = "Open";
    public final static String close                        = "Close";
    public final static String save                         = "Save";
    public final static String saveAs                       = "Save As";
    public final static String defaults                     = "Defaults";
    public final static String revertsTo                    = "Revert To";
    public final static String exit                         = "Exit";
    public final static String help                         = "Help";
    public final static String add                          = "Add";
    public final static String modify                       = "Modify";
    public final static String delete                       = "Delete";

    public final static String speedTemplate                = "Speed Template";
    public final static String getContent                   = "Get Content";
    public final static String manual                       = "Manual";
    public final static String lsiAssistant                 = "LSI Assistant";
    public final static String applyFilter                  = "Apply Filter";

    public final static String copyText                     = "Copy Text";
    public final static String resetAllText                 = "Reset All Text";
    public final static String closeWindow                  = "Close Window";
    public final static String addParagraph                 = "Add Paragraph";
    public final static String deleteParagraph              = "Delete Paragraph";
    public final static String keywordParagraph             = "Paragraph Keyword";
    public final static String resetParagraph               = "Reset Paragraph";
    public final static String resetSnippets                = "Reset Snippets";
    public final static String appendText                   = "Append Text";
    public final static String preview                      = "Preview";
    public final static String clearText                    = "Clear Text";
    
    public final static String keywords                     = "Keywords";
    public final static String filter                       = "Filter";
    public final static String inputFiles                   = "Input Files";
    public final static String tag                          = "tag";
    public final static String generatedFiles               = "Generated Files";
    public final static String rename                       = "Rename";
    public final static String editor                       = "Editor";
    public final static String browser                      = "Browser";
    public final static String categoryOrUrl                = "Category (or URL)";
    public final static String openTag                      = "Open Tag";
    public final static String closeTag                     = "Close Tag";
    public final static String token                        = "Token";
    public final static String strip                        = "Strip";
    public final static String remove                       = "Remove";
    public final static String generateContentPages         = "Generate Content Pages";
    public final static String generateIndexPage            = "Generate Index Page";

    public final static String replaceTokens                = "Replace Tokens";
    public final static String copyRSS                      = "Copy RSS";
    public final static String rssAssistant                 = "RSS Assistant";
    public final static String rssTokens                    = "RSS Feeds search/replace (multi select)";
    public final static String search                       = "Search Text";
    public final static String category                     = "Category";
    public final static String description                  = "Description";
    public final static String copyAdvert                   = "Copy Advert";
    public final static String titleColor                   = "Title Color";
    public final static String bodyColor                    = "Body Color";
    public final static String advertBoxColor               = "Advert Box Color";
    public final static String advertBackgroundColor        = "Advert Background Color";
    public final static String advertDemoInBrowser          = "Advert Demo<br>in Browser";
    public final static String productAssistant             = "Product Assistant";
    public final static String advertsTokens                = "Adverts search/replace (multi select)";
    public final static String titleFont                    = "Title Text Font, Size and Color";
    public final static String bodyFont                     = "Body Text Font, Size and Color";
    public final static String advertStyle                  = "Advert Style";
    public final static String height                       = "Height";
    public final static String width                        = "Width";
    public final static String selectTitleTextColor         = "Select title text color";
    public final static String selectBodyTextColor          = "Select body text color";
    public final static String selectAdvertBackgroundColor  = "Select Advert background color";
    public final static String selectAdvertBoxColor         = "Select Advert box color";
    public final static String titleText                    = "Title Text";
    public final static String bodyText                     = "Body Text";
    public final static String example                      = "Example";
    public final static String demoTitle                    = "Title will look like this";
    public final static String demoBody                     = "Description will look like this";
    public final static String replaceText                  = "Replace Text";
    public final static String specialKeywordReplace        = "Special Keyword Replace";
    public final static String textTokens                   = "Text (multi line) search/replace (multi select)";

    // Label values
    public final static String tagsLabel                    = "Category / Open Tag / Close Tag / Token";
    public final static String buildContentLabel            = "To generate new Content Pages from files you already have, " +
               "select which Input Files to include, select which Tag / Token combinations to use, " +
               "and then press the Generate Content Pages button";
    public final static String buildIndexLabel              = "To generate an Index Page select which Generated Files " +
               "to include and then press the Generate Index Page button";
    public final static String contentAssistantLabel        = "To get writing inspiration enter your keywords in the top right corner and press the<br>" +
               "Get Content button. You can filter content by entering filter words and pressing the<br>" +
               "Apply Filter button. Always be careful to write your own content as well as<br>" +
               "borrowing from other people's work. You should check your uploaded content by<br>" +
               "putting it through www.copyscape.com or similar.";
    public final static String rssFeedsLabel                = "Select the RSS Feeds search / replace combinations (see top left), " +
               "select the Generated Files to apply them to (see bottom left) and then press the Replace button.<br><br>" + 
               "If you want to combine feeds together select more than 1 feed with the same search text and those feeds will be "+
               "combined when you press the Replace button.";
    public final static String contextualAdvertsLabel       = "Select the Advert search / replace combinations (see top left), " +
               "select the Advert style and appearance (see above), select the Generated Files to apply them to (see bottom left), " +
               "and then press the Replace button";
    public final static String textReplaceLabel             = "Select the Text search / replace combinations (see top left), " +
               "select the Generated Files to apply them to (see bottom left), and then press the Replace button";
}
