package org.opens.webdevdataquery;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * Company : Open-S
 * @author Baptiste Le Bail
 * 
 */
public class QueryExecutor {
    
    public static final String statSeparator = "\n";
    public static final String statWithUrisSeparator = "------------------------------------------------------\n";
    
    public static void printManual(String message) {
        if (message != null ) {
            System.out.println("\n" + message);
        }
        
        System.out.println("\nUsage :\n\twebdevdata-query.sh [--withUris] <webDevDataDirPath> <CSSLikeQuery1> [<CSSLikeQuery2> ... <CSSLikeQueryN>]");
        System.out.println("\nParameters :");
        System.out.println("\t<webDevDataDirPath>\n\t\tpath to webdevdata files : /pathto/webdevdata/webdevdata.org-<datetime>/");
        System.out.println("\n\t<CSSLikeQuery>\n\t\t See http://jsoup.org/apidocs/org/jsoup/select/Selector.html selector syntax section");
        System.out.println("\n\t--withUris\n\t\tAlso display the list of URIs where instances matching css queries were found");
        System.out.println("\nExamples :");
        System.out.println("\twebdevdata-query.sh /home/john/webdevdata/webdevdata.org-2013-06-18-124603/ footer");
        System.out.println("\twebdevdata-query.sh --withUris /home/john/webdevdata/webdevdata.org-2013-06-18-124603/ a img:not([alt]) nav header article\n");
        System.exit(0);
    }
    
    public static void printParsingTime(long startTime, long endTime) {
        Long diff = (endTime - startTime);
        
        String time = String.format("%02d:%02d:%02d", 
            TimeUnit.MILLISECONDS.toHours(diff),
            TimeUnit.MILLISECONDS.toMinutes(diff) - 
            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(diff)),
            TimeUnit.MILLISECONDS.toSeconds(diff) - 
            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(diff)));
        
        System.out.println("\nTotal time..." + time + "s");
    }
    
    public static List<QueryStatisticsEntity> getQueryStatisticsList(String[] args, int argsStartIndex) {
        List<QueryStatisticsEntity> queryStatisticsList = new ArrayList<QueryStatisticsEntity>();
        
        for (int i = argsStartIndex + 1; i < args.length; i++) {
            queryStatisticsList.add(new QueryStatisticsEntity(args[i]));
        }
        
        return queryStatisticsList;
    }
    
    public static void printQueryStatisticsList(List<QueryStatisticsEntity> queryStatisticsList, boolean withUris) {
        String separator = (withUris ? statWithUrisSeparator : statSeparator);
        
        System.out.println(separator);
        
        if (!withUris) {
            System.out.print("CSS Query;Total number of instances;Total number of pages with feature;Max number of instances per page\n");
        }
        
        for (QueryStatisticsEntity queryStatistics : queryStatisticsList) {
                System.out.print(queryStatistics.toString(withUris));
                System.out.print(separator);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        String webDevDataPath = null;    
        boolean withUris = false;
        int argsStartIndex = 0;
        
        // Handling command line arguments
        try {
            if (args[0].equals("--withUris")) {
                withUris = true;
                argsStartIndex = 1;
            } 
        } catch (Exception e) {
            printManual(null);
        }
        
        try {
            webDevDataPath = args[argsStartIndex];
            File webDevData = new File(webDevDataPath);
            
            if (!webDevData.exists()) {
                throw new FileNotFoundException(args[argsStartIndex]);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            printManual("Wrong webdevdata directory path :" );
        } catch (FileNotFoundException e) {
            printManual("Webdevdata directory path not found : " + e.getMessage()); 
        } catch (Exception e){
            printManual(null);
        }
        
        if (args.length < (2 + argsStartIndex)) {
           printManual("Wrong/empty CSS queries :");
        }
        
        // Start of parsing
        System.out.println("parsing...\n");
        long startTime = Calendar.getInstance().getTimeInMillis();
        
        List<QueryStatisticsEntity> queryStatisticsList = getQueryStatisticsList(args, argsStartIndex);
        
        Iterator<File> iter =
                FileUtils.iterateFiles(new File(webDevDataPath), null, true);
         
        // Iterate recursively over the web dev data folder
        while (iter.hasNext()) {

            File file = iter.next();
            String html = FileUtils.readFileToString(file);
            Document doc = Jsoup.parse(html);
            
            // Build statistics
            for (QueryStatisticsEntity queryStatistics : queryStatisticsList) {
               
                Elements elements = doc.select(queryStatistics.getQuery());
                int instanceCount = elements.size();
                
                if (instanceCount > 0) {
                    queryStatistics.put(file.getAbsolutePath(), instanceCount);
                }
            }
        }
        
        long endTime = Calendar.getInstance().getTimeInMillis();
        
        // print query results and parsing time
        printQueryStatisticsList(queryStatisticsList, withUris);
        printParsingTime(startTime, endTime);
    }
}
