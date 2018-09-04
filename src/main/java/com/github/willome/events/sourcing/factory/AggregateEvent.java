package com.github.willome.events.sourcing.factory;

import java.io.Serializable;

/**
 *
 * @author willome
 *
 * @param <U> the source type
 */
public interface AggregateEvent<U> extends Serializable {

    /**
     *
     * @return the aggregate event version
     */
    int version();

    /**
     * schemas must conform to application/[prefix].[subject].v[version]+[format]
     * ex. application/vnd.com.user.v1+json
     *
     * @return the schema
     */
    default String schema() {
        return String.format("application/%s.%s.v%d+%s", "vnd", getClass().getCanonicalName(), version(), "json");
    }

}
