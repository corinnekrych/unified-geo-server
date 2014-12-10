package org.jboss.aerogear.geo.util;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.TermQuery;
import org.hibernate.search.annotations.Factory;
import org.hibernate.search.annotations.Key;
import org.hibernate.search.filter.FilterKey;
import org.hibernate.search.filter.StandardFilterKey;
import org.hibernate.search.filter.impl.CachingWrapperFilter;



public class ApplicationFilterFactory {
	private String apiKey;

    /**
     * injected parameter
     */
    public void setApiKey(String  apiKey) {
        this.apiKey = apiKey;
    }

    @Key
    public FilterKey getKey() {
        StandardFilterKey key = new StandardFilterKey();
        key.addParameter( apiKey );
        return key;
    }

    @Factory
    public Filter getFilter() {
        Query query = new TermQuery( new Term("application.apiKey", apiKey ) );
        return new CachingWrapperFilter( new QueryWrapperFilter(query) );
    }	

}
