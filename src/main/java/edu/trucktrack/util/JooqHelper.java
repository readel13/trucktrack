package edu.trucktrack.util;

import org.jooq.Field;
import org.jooq.impl.DSL;

public class JooqHelper {

   public static  <T extends Number> Field<Double> trunc(Field<T> val, Integer precision) {
        return DSL.field("trunc({0}::numeric, {1})", Double.class, val, DSL.inline(precision));
    }
}
