package com.doc.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang.StringUtils;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class JsonWrapper {

	protected JsonWrapper() {
		super();
	}

	private static ThreadLocal<ObjectMapper> mapper = new ThreadLocal<ObjectMapper>() {

		@Override
		protected ObjectMapper initialValue() {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(format);
			mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
			return mapper;
		}

	};

	public static <T> T readValue(String json, Class<T> clazz) {
		if (StringUtils.isBlank(json) || clazz == null) {
			return null;
		}
		try {
			return mapper.get().readValue(json, clazz);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static Map<String, String> readValue(String json) {
		if (StringUtils.isBlank(json)) {
			return null;
		}
		try {
			return mapper.get().readValue(json,
					new TypeReference<Map<String, String>>() {
					});
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("deprecation")
	public static List<?> readListValue(String json, Class<?> clazz) {
		if (StringUtils.isBlank(json) || clazz == null) {
			return null;
		}
		try {
			JavaType javaType = mapper.get().getTypeFactory()
					.constructParametricType(List.class, clazz);
			// 如果是Map类型
			// mapper.getTypeFactory().constructParametricType(HashMap.class,String.class,
			// Bean.class);
			List<?> list = mapper.get().readValue(json, javaType);
			return list;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String writeValue(Object obj) {
		if (obj == null) {
			return null;
		}
		try {
			StringWriter sw = new StringWriter();
			mapper.get().writeValue(sw, obj);
			return sw.toString();
			// return mapper.get().writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
