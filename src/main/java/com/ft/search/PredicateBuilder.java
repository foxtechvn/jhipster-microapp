package com.ft.search;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.querydsl.core.types.dsl.BooleanExpression;

public class PredicateBuilder<T> {
	    private List<SearchCriteria> params;
		private String className;

		public static final Pattern pattern = Pattern.compile("([\\w.]+?)(;|:|<|>|@|~|\\*)([\\w.]+?),");

	    public PredicateBuilder(String className) {
	        params = new ArrayList<SearchCriteria>();
	        this.className = className;
	    }
	    /**
	     * Build a Predicate for className and query string.
	     * @param className
	     * @param query follow pattern of [field]:[value], [field]>[value], [field]<[value], [field]@[list of | separated values], [field]~[like clause value]
	     */
	    public PredicateBuilder(String className, String query){
	    	params = new ArrayList<SearchCriteria>();
	        this.className = className;
	        Matcher matcher = pattern.matcher(query + ",");
			while (matcher.find()) {
				this.with(matcher.group(1), matcher.group(2), matcher.group(3));
			}
	    }

	    public PredicateBuilder<T> with(String key, String operation, Object value) {
	        params.add(new SearchCriteria(key, operation, value));
	        return this;
	    }

	    public BooleanExpression build() throws ClassNotFoundException, NoSuchFieldException, SecurityException {
	        if (params.size() == 0) {
	            return null;
	        }

	        List<BooleanExpression> predicates = new ArrayList<BooleanExpression>();
	        Predicate<T> predicate;
	        for (SearchCriteria param : params) {
	            predicate = new Predicate<T>(param, className);
	            BooleanExpression exp = predicate.getPredicate();
	            if (exp != null) {
	                predicates.add(exp);
	            }
	        }

	        BooleanExpression result = predicates.get(0);
	        for (int i = 1; i < predicates.size(); i++) {
	            result = result.and(predicates.get(i));
	        }
	        return result;
	    }
}
