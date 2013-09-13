package org.opens.webdevdataquery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Company : Open-S
 * @author Baptiste Le Bail
 */
public class QueryStatisticsEntity {
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
            
    public String toString(boolean withUris) {
        StringBuilder sb = new StringBuilder();
        
        if (withUris) {
            sb.append("CSS Query : ").append(this.query)
                    .append("\n")
                    .append("Total number of instances : ").append(this.totalInstanceCount)
                    .append("\n")
                    .append("Total number of pages with feature : ").append(this.pageWithFeatureCount)
                    .append("\n");

            if (!this.instanceCountByUrlMap.isEmpty()) {
                List<Map.Entry<String, Integer>> sortedEntriesList = this.getSortedMapEntriesList(Order.DESC);
                sb.append("Max number of instances per page : ");
                for (Map.Entry<String, Integer> entry : sortedEntriesList) {
                    sb.append(entry.getValue());
                    break;
                }
                sb.append("\n");
                
                sb.append("\n")
                        .append("Instances")
                        .append("\t")
                        .append("URI")
                        .append("\n");

                for (Map.Entry<String, Integer> entry : sortedEntriesList) {
                    sb.append(entry.getValue())
                            .append("\t")
                            .append(entry.getKey())
                            .append("\n");
                }
            }
        } else {
            sb.append(this.query).append(";");
            sb.append(this.totalInstanceCount).append(";");
            sb.append(this.pageWithFeatureCount).append(";");

            if (!this.instanceCountByUrlMap.isEmpty()) {
                List<Map.Entry<String, Integer>> sortedEntriesList = this.getSortedMapEntriesList(Order.DESC);
                for (Map.Entry<String, Integer> entry : sortedEntriesList) {
                    sb.append(entry.getValue());
                    break;
                }
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

    public long getTotalInstanceCount() {
        return totalInstanceCount;
    }

    public void setTotalInstanceCount(long totalInstanceCount) {
        this.totalInstanceCount = totalInstanceCount;
    }

    public long getPageWithFeatureCount() {
        return pageWithFeatureCount;
    }

    public void setPageWithFeatureCount(long pageWithFeatureCount) {
        this.pageWithFeatureCount = pageWithFeatureCount;
    }
}
