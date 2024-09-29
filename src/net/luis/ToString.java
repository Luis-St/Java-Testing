package net.luis;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ToString {
	
	private final Object object;
	private final List<String> excludeFields;
	
	public ToString(Object object, String... excludeFields) {
		this.object = object;
		this.excludeFields = Arrays.asList(excludeFields);
	}
	
	public static String toString(Object object) {
		return new ToString(object).toString();
	}
	
	public static String toString(Object object, String... excludeFields) {
		return new ToString(object, excludeFields).toString();
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder(this.object.getClass().getSimpleName());
		List<Field> fields = this.getFields();
		if (fields.size() > 0) {
			builder.append("{");
			for (int i = 0; i < fields.size(); i++) {
				String name = fields.get(i).getName();
				if (!this.excludeFields.contains(name)) {
					if (i != 0) {
						builder.append(",");
					}
					builder.append(name).append("=").append(get(fields.get(i), this.object).toString());
				}
			}
			builder.append("}");
		}
		return builder.toString();
	}
	
	private <T> List<Field> getFields() {
        List<List<Field>> lists = new ArrayList<>();
        Class<?> clazz = this.object.getClass();
        while (clazz.getSuperclass() != null) {
        	lists.add(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        List<Field> fields = new ArrayList<>();
        for (List<Field> list : reverseList(lists)) {
			fields.addAll(list);
		}
        return fields;
    }
	
	private Object get(Field field, Object instance) {
		Object value = null;
		try {
			field.setAccessible(true);
			value = field.get(instance);
		} catch (Exception ignored) {
			
		}
		return value;
	}
	
	private <T> List<T> reverseList(List<T> list) {
		List<T> reversedList = new ArrayList<>();
		for (int i = list.size(); i-- > 0;) {
			reversedList.add(list.get(i));
		}
		return reversedList;
	}
	
}
