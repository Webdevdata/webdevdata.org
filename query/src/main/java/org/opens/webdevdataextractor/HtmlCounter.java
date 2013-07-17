package org.opens.webdevdataextractor;

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
 * @author opens
 */
public class HtmlCounter {
    
    public static final String statSeparator = "------------------------------------------------------";
    
    public static void printManual(String message) {
        if (message != null ) {
            System.out.println(message);
        }
        
        System.out.println("\nUsage :\n\t[--withURIs] <webDevDataDirPath> <CSSQuery1> [<CSSQuery2> ... <CSSQueryN>]");
        System.out.println("\n--withURIs :\n\talso display the list of URIs where instances matching css queries were found");
        System.out.println("\n<webDevDataDirPath> :\n\ti.e /pathto/webdevdata/webdevdata.org-2013-06-18-124603/");
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
    
    public static void printQueryStatisticsList(List<QueryStatisticsEntity> queryStatisticsList, boolean withUrls) {
        System.out.println(statSeparator);
        
        for (QueryStatisticsEntity queryStatistics : queryStatisticsList) {
                System.out.print(queryStatistics.toString(withUrls));
                System.out.println(statSeparator);
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        String webDevDataPath = null;    
        boolean withURIs = false;
        int argsStartIndex = 0;
        
        // Handling command line arguments
        try {
            if (args[0].equals("--withURIs")) {
                withURIs = true;
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
        
        String[] extension = {"html.txt"};
        Iterator<File> iter =
                FileUtils.iterateFiles(new File(webDevDataPath), extension, true);

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
                    queryStatistics.put(file.getName(), instanceCount);
                }
            }
        }
        
        long endTime = Calendar.getInstance().getTimeInMillis();
        
        printQueryStatisticsList(queryStatisticsList, withURIs);
        printParsingTime(startTime, endTime);
    }
}
