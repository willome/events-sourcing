package com.github.willome.events.sourcing.test;

import java.sql.Types;

/**
 *
 * @author Axione
 *
 */
public class H2JsonDialect extends org.hibernate.dialect.H2Dialect {

    /**
     * Constructor
     */
    public H2JsonDialect() {
        super();
        registerColumnType(Types.JAVA_OBJECT, "varchar");
    }

}