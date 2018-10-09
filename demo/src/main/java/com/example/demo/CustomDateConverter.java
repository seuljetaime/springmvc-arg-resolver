package com.example.demo;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义日期格式转换
 */
public class CustomDateConverter implements Converter<String, Date> {

	@Override
	public Date convert(String source) {
		if (StringUtils.isBlank(source)) {
			return null;
		} else if (StringUtils.isNumeric(source)) {
			return new Date(Long.valueOf(source).longValue());
		} else {
			if (source.contains("-")) {
				try {
					SimpleDateFormat formatter;
					if (source.contains(":")) {
						formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					} else {
						formatter = new SimpleDateFormat("yyyy-MM-dd");
					}
					return formatter.parse(source);
				} catch (ParseException e) {
					e.printStackTrace();
					throw new RuntimeException("日期格式不正确！");
				}
			} else {
				throw new RuntimeException("日期格式不正确！");
			}
		}
	}
}
