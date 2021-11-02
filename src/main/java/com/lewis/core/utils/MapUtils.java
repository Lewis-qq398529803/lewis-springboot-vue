package com.lewis.core.utils;

import com.alibaba.fastjson.JSON;
import com.lewis.core.base.domain.BaseResult;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * map工具类
 *
 * @author Lewis - 2021年10月15日, 015 - 14:18:30
 */
public class MapUtils {

	public static void main(String[] args) {
		BaseResult ok = BaseResult.ok();
		Map<?, ?> map = object2MapByBeanUtils(ok);
		System.out.println("map1 -> " + map);
		Map<String, Object> stringObjectMap = object2MapByIntroSpector(ok);
		System.out.println("map2 -> " + stringObjectMap);
		Map<String, Object> stringObjectMap1 = object2MapByReflect(ok);
		System.out.println("map3 -> " + stringObjectMap1);
	}

	/**
	 * 实体类转map
	 * @param object
	 * @return
	 */
	public static Map object2Map(Object object) {
		return JSON.parseObject(JSON.toJSONString(object), Map.class);
	}

	/**
	 * 使用org.apache.commons.beanutils进行转换
	 */
	public static Object map2ObjectByBeanUtils(Map<String, Object> map, Class<?> beanClass) throws Exception {
		if (map == null) {
			return null;
		}
		Object obj = beanClass.newInstance();
		org.apache.commons.beanutils.BeanUtils.populate(obj, map);
		return obj;
	}

	public static Map<?, ?> object2MapByBeanUtils(Object obj) {
		if(obj == null) {
			return null;
		}
		return new org.apache.commons.beanutils.BeanMap(obj);
	}

	/**
	 * 使用Introspector进行转换
	 */
	public static Object map2ObjectByIntroSpector(Map<String, Object> map, Class<?> beanClass) throws Exception {
		if (map == null) {
			return null;
		}
		Object obj = beanClass.newInstance();
		BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		for (PropertyDescriptor property : propertyDescriptors) {
			Method setter = property.getWriteMethod();
			if (setter != null) {
				setter.invoke(obj, map.get(property.getName()));
			}
		}
		return obj;
	}

	public static Map<String, Object> object2MapByIntroSpector(Object obj) {
		if(obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		BeanInfo beanInfo = null;
		try {
			beanInfo = Introspector.getBeanInfo(obj.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (PropertyDescriptor property : propertyDescriptors) {
				String key = property.getName();
				if (key.compareToIgnoreCase("class") == 0) {
					continue;
				}
				Method getter = property.getReadMethod();
				Object value = getter!=null ? getter.invoke(obj) : null;
				map.put(key, value);
			}
		} catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 使用reflect进行转换
	 */
	public static Object map2ObjectByReflect(Map<String, Object> map, Class<?> beanClass) throws Exception {
		if (map == null) {
			return null;
		}
		Object obj = beanClass.newInstance();
		Field[] fields = obj.getClass().getDeclaredFields();
		for (Field field : fields) {
			int mod = field.getModifiers();
			if(Modifier.isStatic(mod) || Modifier.isFinal(mod)){
				continue;
			}
			field.setAccessible(true);
			field.set(obj, map.get(field.getName()));
		}
		return obj;
	}

	public static Map<String, Object> object2MapByReflect(Object obj) {
		if(obj == null){
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		Field[] declaredFields = obj.getClass().getDeclaredFields();
		for (Field field : declaredFields) {
			field.setAccessible(true);
			try {
				map.put(field.getName(), field.get(obj));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return map;
	}
}
