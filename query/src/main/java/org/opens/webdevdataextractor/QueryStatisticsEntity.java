package org.opens.webdevdataextractor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author opens
 */
public class QueryStatisticsEntity {
    private final String separator = "\n";
    private final String urlSeparator = "\n";
    private final String instanceCountAndUrlSeparator = "\t";

    private Map<String, Integer> instanceCountByUrlMap;
    
    private String query;
    private long totalInstanceCount;
    private long pageWithFeatureCount;
    enum Order {ASC, DESC};
    
    /*
     * Returns a list of the map entries sorted by values (instanceCount) in ASC or DESC order
     */
    private List<Map.Entry<String, Integer>> getSortedMapEntriesList(final QueryStatisticsEntity.Order order) {
        List<Map.Entry<String, Integer>> sortedEntriesList;
        sortedEntriesList = new ArrayList<Map.Entry<String, Integer>>(instanceCountByUrlMap.entrySet());
        
        Collections.sort(sortedEntriesList,
                 new Comparator() {
                     @Override
                     public int compare(Object o1, Object o2) {
                         Map.Entry e1 = (Map.Entry) o1;
                         Map.Entry e2 = (Map.Entry) o2;
                         if (order == Order.ASC) {
                            return ((Comparable) e1.getValue()).compareTo(e2.getValue());
                         } else {
                            return -((Comparable) e1.getValue()).compareTo(e2.getValue());
                         }
                     }
                 });
       return sortedEntriesList;
    }
    
    private void init() {
        this.instanceCountByUrlMap = new HashMap<String, Integer>();
        this.totalInstanceCount = 0;
        this.pageWithFeatureCount = 0;
    }
    
    public QueryStatisticsEntity(String query) {
        this.init();
        this.query = query;
    }
    
    public void put(String url, int instanceCount) {
        this.instanceCountByUrlMap.put(url, instanceCount);
        
        this.totalInstanceCount += instanceCount;
        this.pageWithFeatureCount++;
    }
            
    public String toString(boolean withUrls) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("CSS Query : ").append(this.query)
                .append(this.separator)
                .append("Total # instances : ").append(this.totalInstanceCount)
                .append(this.separator)
                .append("Total # pages with feature : ").append(this.pageWithFeatureCount)
                .append(this.separator);
        
        if (withUrls && !this.instanceCountByUrlMap.isEmpty()) {
            List<Map.Entry<String, Integer>> sortedEntriesList = this.getSortedMapEntriesList(Order.DESC);
            
            sb.append(this.separator)
                    .append("Instances")
                    .append(this.instanceCountAndUrlSeparator)
                    .append("URI")
                    .append(this.separator);
            
            for (Map.Entry<String, Integer> entry : sortedEntriesList) {
                sb.append(entry.getValue())
                        .append(this.instanceCountAndUrlSeparator)
                        .append(entry.getKey())
                        .append(this.urlSeparator);
            }
        }
        
        return sb.toString();
    }
    
    /*
     * Accessors
     */
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
