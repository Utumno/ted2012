package com.ted.controller;

import java.util.HashMap;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ted.helpers.Helpers;

public class Controller extends HttpServlet implements Addresses {

	private static final long serialVersionUID = -3909058201424153832L;

	protected static ServletContext sc;

	private static final String DATE_FORMAT = "yyyy/MM/dd";
	private static final String SUCCESS_MESSAGES_MAP = "messages";
	protected Logger log;
	static {
		MBeanServer mBeanServer = MBeanServerFactory.findMBeanServer(null).get(
				0);
		ObjectName name = null;
		try {
			name = new ObjectName("Catalina", "type", "Server");
		} catch (MalformedObjectNameException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Server server = null;
		try {
			server = (Server) mBeanServer.getAttribute(name, "managedResource");
		} catch (AttributeNotFoundException | InstanceNotFoundException
				| MBeanException | ReflectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Service[] services = server.findServices();
		for (Service service : services) {
			for (Connector connector : service.findConnectors()) {
				System.out.println(connector);
				String uriEncoding = connector.getURIEncoding();
				System.out.println("URIEncoding : " + uriEncoding);
				boolean use = connector.getUseBodyEncodingForURI();
				// TODO : if(use && connector.get uri enc...)
				Helpers.setCHARSET_FOR_URI_ENCODING(uriEncoding);
				// ProtocolHandler protocolHandler = connector
				// .getProtocolHandler();
				// if (protocolHandler instanceof Http11Protocol
				// || protocolHandler instanceof Http11AprProtocol
				// || protocolHandler instanceof Http11NioProtocol) {
				// int serverPort = connector.getPort();
				// System.out.println("HTTP Port: " + connector.getPort());
				// }
			}
		}
	}

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
		if (session == null)
			return null; // should not happen
		HashMap<String, String> m = (HashMap<String, String>) session
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
