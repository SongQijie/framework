package cn.orzbug.base.utils;

import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author: SQJ
 * @data: 2018/4/7 15:41
 * @version:
 */
@Slf4j
public class ReflectionUtil {
    /**
     * 获取对应名字 get方法返回值
     *
     * @param t    对象
     * @param name 名字
     * @return 方法返回值
     */
    public static Object getMethodValue(Object t, String name) {
        try {
            name = getGetName(name);
            Method method = t.getClass().getMethod(name);
            return method.invoke(t);
        } catch (Exception e) {
            log.warn("getMethod for name [{}] error", name);
        }
        return null;
    }

    /**
     * 获取字段值（通过其get方法）
     *
     * @param t        对象
     * @param fielName 字段名称
     * @return 字段get方法返回值
     */
    public static Object getFieldValue(Object t, String fielName) {
        String name = getFieldName(t.getClass(), fielName);
        if (null == name) {
            log.warn("getMethod for name [{}] error", name);
            return null;
        }
        return getMethodValue(t, name);
    }

    public static String getFieldName(Class t, String fielName) {
        try {
            Field field = t.getDeclaredField(fielName);
            if (null != field) {
                //java.io.Serializable 类型竟然属于Boolean
                if (!field.getType().getName().equals("java.io.Serializable")&&field.getType().isAssignableFrom(Boolean.class)) {
                    fielName = fielName.substring(fielName.indexOf("is") + "is".length(), fielName.length());
                    fielName = fielName.toLowerCase();
                }
                return fielName;
            }
        } catch (NoSuchFieldException e) {
            if (null == t.getSuperclass()) {
                log.warn("getMethod for name [{}] error", fielName);
            } else {
                return getFieldName(t.getSuperclass(), fielName);
            }
        }
        return null;
    }

    /**
     * 获取get方法
     *
     * @param name field 名称
     * @return field对应get名称
     */
    public static String getGetName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return "get" + String.valueOf(cs);
    }

    /**
     * 获取字段set方法
     *
     * @param name 字段名称
     * @return
     */
    public static String getSetName(String name) {
        char[] cs = name.toCharArray();
        cs[0] -= 32;
        return "set" + String.valueOf(cs);
    }

    /**
     * 设置字段值（通过set方法）
     *
     * @param t        对象
     * @param fielName 字段名称
     */
    public static void setFiledValue(Object t, String fielName, Object... value) {
        String name = getFieldName(t.getClass(), fielName);
        if (!Strings.isNullOrEmpty(name)) {
            name = getSetName(name);
            try {
                Field field = t.getClass().getDeclaredField(fielName);
                Method method = null;
                if (field.getType().isAssignableFrom(Boolean.class)) {
                    method = t.getClass().getMethod(name, Boolean.class);
                } else {
                    Class[] classes = new Class[value.length];
                    for (int i = 0; i < value.length; i++) {
                        classes[i] = value[i].getClass();
                    }
                    method = t.getClass().getMethod(name, classes);
                }
                Object[] objects = new Object[method.getParameterTypes().length];
                int i = 0;
                for (Type type : method.getGenericParameterTypes()) {
                    objects[i] = getValueForType(type.toString(), value[i]);
                    i++;
                }
                method.invoke(t, objects);
            } catch (Exception e) {
                log.warn("setMethod for name [{}] error", name);
            }
        }
    }

    /**
     * 获取符合类型的值（自动转换）
     *
     * @param typeName 类型名称
     * @param value    值
     * @return 转换后的值
     */
    public static Object getValueForType(String typeName, Object value) {
        if (typeName.equals("class java.lang.String")) {
            return value.toString();
        } else if (typeName.equals("class java.lang.Boolean")) {
            return Boolean.parseBoolean(value.toString());
        } else if (typeName.equals("class java.lang.Integer")) {
            return Integer.parseInt(value.toString());
        } else if (typeName.equals("class java.lang.Long")) {
            return Long.parseLong(value.toString());
        } else {
            return value;
        }
    }

    /**
     * 获取属性值为空的字段名称集合
     *
     * @param source 元数据
     * @return 为空的名字集合
     */
    public static String[] getNullPropertyNames(Object source, String... ignoreProperties) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = null;
            try {
                srcValue = src.getPropertyValue(pd.getName());
            } catch (BeansException e) {
                log.warn("not find filed {}",pd.getName());
                continue;
            }
            if (srcValue == null){
                emptyNames.add(pd.getName());
            }
        }
        for (String ignore : ignoreProperties) {
            emptyNames.add(ignore);
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }


    /**
     * 根据字段名称获取值（支持两级例如 "user.userName"）
     * 且支持 集合操作（user.children.name:age==10）//取出 user中children集合中  age==10 的小孩 name 值
     *
     * @param obj       对象
     * @param filedName 字段名称
     * @param <V>       字段值类型
     * @return 字段值
     */
    public static <V> V getValueForFiled(Object obj, String filedName) {
        if (filedName.contains(".")) {
            List<String> list = Splitter.on(".").splitToList(filedName);
            V v1 = (V) ReflectionUtil.getMethodValue(obj, list.get(0));
            if (null == v1) {
                return null;
            }
            //处理字段为集合类型
            if (v1 instanceof Collection) {
                Collection<V> c = (Collection<V>) v1;
                //aa:bb==1
                String role = list.get(list.size() - 1);
                List<String> rolelist = Splitter.on(":").splitToList(role);
                //bb==1
                role = rolelist.get(rolelist.size() - 1);

                String roles[] = role.split("==");

                if (!c.isEmpty()) {
                    Iterator<V> it = c.iterator();
                    while (it.hasNext()) {
                        V listValue = it.next();
                        V value = (V) ReflectionUtil.getFieldValue(listValue, roles[0]);
                        if (value.equals(roles[1])) {
                            return (V) ReflectionUtil.getFieldValue(listValue, rolelist.get(0));
                        }
                    }
                } else {
                    return null;
                }
            }
            filedName = filedName.substring(filedName.indexOf(".") + 1, filedName.length());
            V v2 = (V) ReflectionUtil.getValueForFiled(v1, filedName);
            return v2;
        }
        return (V) ReflectionUtil.getFieldValue(obj, filedName);
    }
}
