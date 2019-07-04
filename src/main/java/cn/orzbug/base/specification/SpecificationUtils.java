package cn.orzbug.base.specification;


import cn.orzbug.base.entity.BaseEntity;
import cn.orzbug.base.utils.ReflectionUtil;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.criteria.*;
import javax.persistence.criteria.CriteriaBuilder.In;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author: SQJ
 * @data: 2018/4/7 15:31
 * @version:
 */
@Slf4j
public class SpecificationUtils {
    public static String getUpdateSql(BaseEntity t, String... ignore) {
        StringBuilder stringBuilder = new StringBuilder("update ");
        stringBuilder.append(getTableName(t));
        val map = getBeanFiled(t).entrySet();
        if (map.isEmpty()) {
            return null;
        }
        val it = map.iterator();
        stringBuilder.append(" set ");
        while (it.hasNext()) {
            val entry = it.next();
            if (!Arrays.asList(ignore).contains(entry.getKey())) {
                stringBuilder.append(entry.getKey());
                stringBuilder.append(" = '");
                stringBuilder.append(entry.getValue());
                stringBuilder.append("',");
            }
        }
        stringBuilder.delete(stringBuilder.length() - 1, stringBuilder.length());
        stringBuilder.append(" where ");
        stringBuilder.append(" id = ");
        stringBuilder.append(t.getId());
        return stringBuilder.toString();
    }

    /**
     * 获取bean filed对应的值
     *
     * @param t bean對象
     * @return map
     */
    public static Map<String, String> getBeanFiled(Object t) {
        Map<String, String> map = new HashMap<>();
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field f : fields) {
            Object object = ReflectionUtil.getFieldValue(t, f.getName());
            Transient aTransient = f.getAnnotation(Transient.class);
            if (null != object && aTransient == null) {
                map.put(getSqlName(f.getName()), object.toString());
            }
        }
        return map;
    }

    public static String getSqlName(String filedName) {
        Pattern p = Pattern.compile("[A-Z]");
        if (filedName == null || filedName.equals("")) {
            return "";
        }
        StringBuilder builder = new StringBuilder(filedName);
        Matcher mc = p.matcher(filedName);
        int i = 0;
        while (mc.find()) {
            builder.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
            i++;
        }
        if ('_' == builder.charAt(0)) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

    /**
     * 获取表名
     *
     * @param t 实体对象
     * @return 配置的表名称
     */
    public static String getTableName(Object t) {
        Table annotation = t.getClass().getAnnotation(Table.class);
        if (null != annotation) {
            return annotation.name();
        } else {
            throw new RuntimeException("object can't find @Table for class");
        }
    }

    /**
     * 更加注解从实体bean中获取配置的查询信息
     *
     * @param t 实体bean
     * @return 信息集合
     */
    private static List<QueryBean> getQueryBean(Object t) {
        Field[] fields = t.getClass().getDeclaredFields();
        List<QueryBean> list = new ArrayList<>();
        list.addAll(getQueryBeanForFields(t, fields));
        list.addAll(getQueryBeanForSuperClass(t, t.getClass().getSuperclass()));
        return list;
    }

    /**
     * 获取class的所有父类class的QueryBean信息
     *
     * @param t   实体bean
     * @param cla class类型
     * @return list
     */
    private static List<QueryBean> getQueryBeanForSuperClass(Object t, Class cla) {
        List<QueryBean> list = new ArrayList<>();
        log.debug("class type : {}", cla.getName());
        if (!cla.getName().equals("java.lang.Object")) {
            list.addAll(getQueryBeanForFields(t, cla.getDeclaredFields()));
            list.addAll(getQueryBeanForSuperClass(t, cla.getSuperclass()));
        }
        return list;
    }

    public static void main(String[] args) {
        String fielName = "isFinish";
        System.out.println(fielName.substring(fielName.indexOf("is") + "is".length(), fielName.length()));
    }

    private static List<QueryBean> getQueryBeanForFields(Object t, Field[] fields) {
        List<QueryBean> list = new ArrayList<>();
        for (Field f : fields) {
            String name = ReflectionUtil.getFieldName(t.getClass(), f.getName());
            name = ReflectionUtil.getGetName(name);
            log.debug("field getName : {}", name);
            try {
                Method method = t.getClass().getMethod(name);
                QueryPath annotation = method.getAnnotation(QueryPath.class);
                //没有配置查询，或者配置的查询是属从字段
                if (null == annotation || !annotation.relevancePk()) {
                    continue;
                }
                String fileName = annotation.filedName();
                // 如果没有设定属性名称，默认是当前属性名称
                if (Strings.isNullOrEmpty(fileName)) {
                    fileName = f.getName();
                }
                String fileValueName = annotation.filedValueName();
                // 如果没有设定获取值属性名称，默认是当前属性名称
                fileValueName = f.getName();
                if (Strings.isNullOrEmpty(fileValueName)) {
                }
                Object value = ReflectionUtil.getValueForFiled(t, fileValueName);
                if (null == value || Strings.isNullOrEmpty(value.toString())) {
                    // 如果查询条件值为空，则不计为查询条件
                    continue;
                }
                QueryBean queryBean = QueryBean.builder().fileName(fileName).fileValue(value).operator(annotation.operator()).build();
                //如果关系维护方，则获取设置关联属性配置
                if (annotation.relevancePk()) {
                    String relevance = annotation.relevance();
                    QueryBean relevanceBean = getQueryBeanForFieldName(t, relevance);
                    queryBean.setRelevance(relevanceBean);
                }
                list.add(queryBean);
            } catch (NoSuchMethodException e) {
                log.warn("getMethod for name [{}] error", name);
            }
        }
        return list;
    }

    private static QueryBean getQueryBeanForFieldName(Object t, String name) {
        try {
            Field f = t.getClass().getDeclaredField(name);
            name = ReflectionUtil.getGetName(name);
            log.debug("field getName : {}", name);
            Method method = t.getClass().getMethod(name);
            QueryPath annotation = method.getAnnotation(QueryPath.class);
            String fileName = annotation.filedName();
            // 如果没有设定属性名称，默认是当前属性名称
            if (Strings.isNullOrEmpty(fileName)) {
                fileName = f.getName();
            }
            String fileValueName = annotation.filedValueName();
            // 如果没有设定获取值属性名称，默认是当前属性名称
            if (Strings.isNullOrEmpty(fileValueName)) {
                fileValueName = f.getName();
            }
            Object value = ReflectionUtil.getFieldValue(t, fileValueName);
            if (null == value || Strings.isNullOrEmpty(value.toString())) {
                // 如果查询条件值为空，则不计为查询条件
                return null;
            }
            return QueryBean.builder().fileName(fileName).fileValue(value).operator(annotation.operator()).build();
        } catch (Exception e) {
            log.warn("getMethod for name [{}] error", name);
        }
        return null;
    }

    /**
     * 获取对象对应查询条件
     *
     * @param t   对象
     * @param <T> entity
     * @return 查询条件
     */
    public static <T> Specification<T> getSpecificationForEntity(final T t) {
        Preconditions.checkArgument(null != t, "查询实体对象不能为空");
        Specification<T> specification = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<Predicate>();
                for (QueryBean queryBean : getQueryBean(t)) {
                    predicates.addAll(getPredicate(root, queryBean, criteriaBuilder));
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        return specification;
    }

    public static List<Predicate> getPredicate(Root<?> root, QueryBean queryBean, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        Object value = queryBean.getFileValue();
        Path expression = root.get(queryBean.getFileName());
        switch (queryBean.getOperator()) {
            case EQ:
                predicates.add(criteriaBuilder.equal(expression, value));
                break;
            case NE:
                predicates.add(criteriaBuilder.notEqual(expression, value));
                break;
            case LIKE:
                predicates.add(criteriaBuilder.like(expression, "%" + value + "%"));
                break;
            case LT:
                predicates.add(criteriaBuilder.lessThan(expression, (Comparable) value));
                break;
            case GT:
                predicates.add(criteriaBuilder.greaterThan(expression, (Comparable) value));
                break;
            case LTE:
                predicates.add(criteriaBuilder.lessThanOrEqualTo(expression, (Comparable) value));
                break;
            case GTE:
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression, (Comparable) value));
                break;
            case OR:
                QueryBean qb = queryBean.getRelevance();
                List<Predicate> list = getPredicate(root, qb, criteriaBuilder);
                predicates.add(criteriaBuilder.or(criteriaBuilder.equal(expression, value), list.get(0)));
                break;
            case IN:
                if (value == null) {
                    break;
                }
                //增加支持逗号分隔字段
                Collection items = null;
                if (value instanceof String) {
                    items = Splitter.on(",").splitToList(value.toString());
                } else if (value instanceof Collection) {
                    items = (Collection) value;
                }
                if (null == items || items.size() <= 0) {
                    break;
                }
                Iterator iterator = items.iterator();
                In in = criteriaBuilder.in(expression);
                while (iterator.hasNext()) {
                    in.value(iterator.next());
                }
                predicates.add(in);
                break;
            default:
        }
        return predicates;
    }

    public static <T> Predicate getPredicateForEntity(final T t, Root root, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        for (QueryBean queryBean : getQueryBean(t)) {
            Path expression = root.get(queryBean.getFileName());
            Object value = queryBean.getFileValue();
            switch (queryBean.getOperator()) {
                case EQ:
                    predicates.add(criteriaBuilder.equal(expression, value));
                    break;
                case NE:
                    predicates.add(criteriaBuilder.notEqual(expression, value));
                    break;
                case LIKE:
                    predicates.add(criteriaBuilder.like(expression, "%" + value + "%"));
                    break;
                case LT:
                    predicates.add(criteriaBuilder.lessThan(expression, (Comparable) value));
                    break;
                case GT:
                    predicates.add(criteriaBuilder.greaterThan(expression, (Comparable) value));
                    break;
                case LTE:
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(expression, (Comparable) value));
                    break;
                case GTE:
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression, (Comparable) value));
                    break;
                case IN:
                    Collection<Comparable> items = (Collection<Comparable>) value;
                    if ((items == null) || (items.size() == 0)) {
                        break;
                    }
                    Iterator iterator = items.iterator();
                    In in = criteriaBuilder.in(expression);
                    while (iterator.hasNext()) {
                        in.value(iterator.next());
                    }
                    predicates.add(in);
                    break;
                default:
            }
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
