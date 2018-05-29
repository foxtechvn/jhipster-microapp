package com.ft.search;

import org.apache.commons.lang3.StringUtils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;

@SuppressWarnings("unchecked")
public class Predicate<T> {

	private SearchCriteria criteria;
	private String className;

	public Predicate(SearchCriteria criteria, String className) {
		super();
		this.criteria = criteria;
		this.className = className;
	}

	@SuppressWarnings("rawtypes")
	public BooleanExpression getPredicate() throws ClassNotFoundException{
		PathBuilder<?> entityPath = new PathBuilder(Class.forName(this.className), StringUtils.uncapitalize(this.className.substring(this.className.lastIndexOf(".") + 1)));
		Long value = numericValue(criteria.getValue());

		if (criteria.getValue().toString().equalsIgnoreCase("true")) {
			BooleanPath path = entityPath.getBoolean(criteria.getKey());
    		return path.eq(true);
		} else if (criteria.getValue().toString().equalsIgnoreCase("false")) {
			BooleanPath path = entityPath.getBoolean(criteria.getKey());
    		return path.eq(false);
		} else if (criteria.getOperation().equalsIgnoreCase("@")){ // Filter WHERE IN
    		String[] val = StringUtils.split(criteria.getValue().toString(), ".");
            Long[] e = new Long[val.length];
        	for (int i = 0; i < val.length; i++) {
        		e[i] = numericValue(val[i]);
        		if (e[i] == null) {
        			e = null;
        			break;
        		}
        	}
    		if (e != null) {
            	NumberPath<Long> path = entityPath.getNumber(criteria.getKey(), Long.class);
            	return path.in(e);
    		} else {
    			StringPath path = entityPath.getString(criteria.getKey());
    			return path.in(val);
    		}
    	} else if ((!criteria.getOperation().equalsIgnoreCase("*")) && (value != null)){ // Numeric type try first
        	NumberPath<Long> path = entityPath.getNumber(criteria.getKey(), Long.class);
            if (criteria.getOperation().equalsIgnoreCase(":")) {
                return path.eq(value);
            }
            else if (criteria.getOperation().equalsIgnoreCase(">")) {
                return path.goe(value);
            }
            else if (criteria.getOperation().equalsIgnoreCase("<")) {
                return path.loe(value);
            }
        } else { // Default to STRING
        	StringPath path = entityPath.getString(criteria.getKey());
        	if (criteria.getOperation().equalsIgnoreCase(":")) {
        		return path.equalsIgnoreCase(criteria.getValue().toString());
        	} else if (criteria.getOperation().equalsIgnoreCase("~")) {
        		return path.containsIgnoreCase(criteria.getValue().toString());
        	} else if (criteria.getOperation().equalsIgnoreCase("@")){
        		return path.in(StringUtils.split(criteria.getValue().toString(), "."));
        	} else {
        		return path.eq(criteria.getValue().toString());
        	}
        }
        // TODO: Add attributes for handling Date Type
        return null;
    }

	public static Long numericValue(Object value){
    	try {
    		return Long.parseLong(value.toString());
    	} catch (NumberFormatException e){
    		return null;
    	}
    }
}
