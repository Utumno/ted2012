package com.ted.controller;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Controller extends HttpServlet implements Addresses {

	private static final long serialVersionUID = -3909058201424153832L;
	protected static ServletContext sc;
	private static final String DATE_FORMAT = "yyyy/MM/dd";
	private static final String SUCCESS_MESSAGES_MAP = "messages";
	protected Logger log;

	protected DateTime getDateFromString(String string)
			throws IllegalArgumentException {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(DATE_FORMAT);
		return formatter.parseDateTime(string);
	}

	@Override
	public void init() throws ServletException {
		super.init();
		sc = getServletContext();
		log = LoggerFactory.getLogger(this.getClass());
	}

	// pros8hkh eggrafhs me tuxaio kleidi kai mhnyma to message , hashmap me ta
	// mhnymata
	protected String messageKey(HttpServletRequest request, String message) {
		HttpSession session = request.getSession(false);
		if (session == null) return null; // should not happen
		Map<String, String> m = (Map<String, String>) session
				.getAttribute(SUCCESS_MESSAGES_MAP);
		String rand;
		synchronized (m) {
			do {
				rand = Long.toHexString(Double.doubleToLongBits(Math.random()));
			} while (m.containsKey(rand));
			m.put(rand, message);
		}
		return rand;
	}
}
