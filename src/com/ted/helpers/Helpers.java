package com.ted.helpers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public final class Helpers {

	private static final String CHARSET_FOR_URL_ENCODING = "UTF-8";

	private Helpers() {
		// prevent instantiation;
	}

	public static String encodeUri(String baseLink, String parameter)
			throws UnsupportedEncodingException {
		return encodeString(baseLink, parameter);
	}

	private static String encodeString(String baseLink, String parameter)
			throws UnsupportedEncodingException {
		return String.format(baseLink + "%s",
				URLEncoder.encode(parameter, CHARSET_FOR_URL_ENCODING));
	}

	public static String decodeRequest(String parameter)
			throws UnsupportedEncodingException {
		if (parameter == null) return null;
		return new String(parameter.getBytes("iso-8859-1"),
				CHARSET_FOR_URL_ENCODING);
	}
}
