package com.blinkfox.fenix.specification.handler.impl;

import com.blinkfox.fenix.specification.annotation.NotIn;
import com.blinkfox.fenix.specification.handler.AbstractSpecificationHandler;
import com.blinkfox.fenix.specification.predicate.FenixBooleanStaticPredicate;

import java.util.Arrays;
import java.util.Collection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Predicate.BooleanOperator;

import org.hibernate.query.criteria.internal.CriteriaBuilderImpl;

/**
 * 构建“范围不匹配条件”({@code field NOT IN ('xxx', 'yyy')})场景的 Specification 监听器.
 *
 * @author YangWenpeng on 2019-12-17
 * @author blinkfox on 2020-01-14
 * @since v2.2.0
 */
public class NotInSpecificationHandler extends AbstractSpecificationHandler {

    @Override
    protected <Z, X> Predicate buildPredicate(
            CriteriaBuilder criteriaBuilder, From<Z, X> from, String name, Object value, Object annotation) {
        CriteriaBuilder.In<Object> in = criteriaBuilder.in(from.get(name));
        value = value.getClass().isArray() ? Arrays.asList((Object[]) value) : value;

        if (value instanceof Collection) {
            Collection<?> list = (Collection<?>) value;
            if (list.isEmpty()) {
                return new FenixBooleanStaticPredicate(
                        (CriteriaBuilderImpl) criteriaBuilder, true, BooleanOperator.AND);
            } else {
                list.forEach(in::value);
            }
        } else {
            in.value(value);
        }
        return criteriaBuilder.not(in);
    }

    @Override
    protected <Z, X> Predicate buildPredicate(CriteriaBuilder criteriaBuilder, From<Z, X> from, String fieldName, Object value) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<NotIn> getAnnotation() {
        return NotIn.class;
    }

}