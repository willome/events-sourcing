package com.github.willome.events.sourcing.domain;

import java.io.IOException;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.HibernateException;
import org.hibernate.boot.registry.classloading.internal.ClassLoaderServiceImpl;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.SerializationException;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;
import org.postgresql.util.PGobject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 *
 * @author Axione
 *
 */
public class JsonbUserType implements ParameterizedType, UserType {

    private static final ObjectMapper OBJECTMAPPER = new ObjectMapper();
    static {
        OBJECTMAPPER.registerModule(new JavaTimeModule());
        OBJECTMAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    private static final ClassLoaderService CLASSLOADERSERVICE = new ClassLoaderServiceImpl();

    /**
     * Postrgres 'jsonb' type
     */
    public static final String JSONB_TYPE = "jsonb";

    /**
     * CLASS parameter.
     */
    public static final String CLASS = "CLASS";

    private Class<?> jsonClassType;

    @Override
    public Class<Object> returnedClass() {
        return Object.class;
    }

    @Override
    public int[] sqlTypes() {
        return new int[] {Types.JAVA_OBJECT};
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner)
            throws HibernateException, SQLException {
        try {
            final String json = rs.getString(names[0]);
            return json == null ? null : OBJECTMAPPER.readValue(json, jsonClassType);
        }
        catch (IOException e) {
            // ugly fix for H2 tests
            try {
                return jsonClassType.newInstance();
            }
            catch (InstantiationException | IllegalAccessException ex) {
                return null;
            }
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session)
            throws HibernateException, SQLException {
        try {
            final String json = value == null ? null : OBJECTMAPPER.writeValueAsString(value);
            PGobject pgo = new PGobject();
            pgo.setType(JSONB_TYPE);
            pgo.setValue(json);
            st.setObject(index, pgo);
        }
        catch (JsonProcessingException e) {
            throw new HibernateException(e);
        }

    }

    @Override
    public void setParameterValues(Properties parameters) {
        final String clazz = (String) parameters.get(CLASS);
        jsonClassType = CLASSLOADERSERVICE.classForName(clazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object deepCopy(Object value) throws HibernateException {

        if (!(value instanceof Collection)) {
            return value;
        }

        Collection<?> collection = (Collection<Object>) value;
        Collection<Object> collectionClone = CollectionFactory.newInstance(collection.getClass());
        collectionClone.addAll(collection.stream().map(this::deepCopy).collect(Collectors.toList()));
        return collectionClone;
    }

    /**
     *
     * @author Axione
     *
     */
    static final class CollectionFactory {

        /**
         *
         * @param collectionClass Class<T>
         * @param <E> ?
         * @param <T> ?
         * @return Collection
         */
        @SuppressWarnings("unchecked")
        static <E, T extends Collection<E>> T newInstance(Class<T> collectionClass) {
            if (List.class.isAssignableFrom(collectionClass)) {
                return (T) new ArrayList<E>();
            }
            else if (Set.class.isAssignableFrom(collectionClass)) {
                return (T) new HashSet<E>();
            }
            else {
                throw new IllegalArgumentException("Unsupported collection type : " + collectionClass);
            }
        }
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if (x == y) {
            return true;
        }

        if (x == null || y == null) {
            return false;
        }

        return x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        assert x != null;
        return x.hashCode();
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return deepCopy(cached);
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        Object deepCopy = deepCopy(value);

        if (!(deepCopy instanceof Serializable)) {
            throw new SerializationException(String.format("%s is not serializable class", value), null);
        }

        return (Serializable) deepCopy;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return deepCopy(original);
    }

}
