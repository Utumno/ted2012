package com.ted.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DBConnectionPool {

	// TODO error handling in clients

	private DataSource ds = null;
	private static final Logger log = LoggerFactory
			.getLogger("DBConnectionPool");

	private DBConnectionPool() {
		try {
			Context context = new InitialContext();
			Context envctx = (Context) context.lookup("java:comp/env");
			ds = (DataSource) envctx.lookup("jdbc/TestDB");
		} catch (Exception e) {
			log.error("DBConnectionPool::DBConnectionPool", e);
		}
	}

	private static enum PoolSingleton {
		POOL_INSTANCE;

		private static final DBConnectionPool singleton = new DBConnectionPool();

		private DBConnectionPool getSingleton() {
			return singleton;
		}
	}

	private static DBConnectionPool getDBConnectionPoolInstance() {
		return PoolSingleton.POOL_INSTANCE.getSingleton();
	}

	static Connection getConnection() {
		try {
			return getDBConnectionPoolInstance().ds.getConnection();
		} catch (SQLException e) {
			log.error("DBConnectionPool::getConnection", e);
			return null;
		}
	}

	static void freeConnection(Connection c) {
		try {
			c.close();
		} catch (SQLException e) {
			log.error("DBConnectionPool::freeConnection", e);
		}
	}

	// static void freeDS() {
	// try {
	// getDBConnectionPoolInstance().ds.???;
	// } catch (SQLException e) {
	// log.error("DBConnectionPool::freeDS", e);
	// }
	// }

	/**
	 * This is intended to be used in finally blocks and therefore MUST NOT
	 * THROW. See : http://today.java.net/article/2006/04/04/exception-handling-
	 * antipatterns
	 * 
	 * @param set
	 * @param statement
	 * @param conn
	 */
	static void closeResources(ResultSet set, Statement statement,
			Connection conn) {
		if (set != null) {
			try {
				set.close();
			} catch (SQLException e) {
				log.warn("DBConnectionPool::closeResources", e);
			}
		}
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				log.warn("DBConnectionPool::closeResources", e);
			}
		}
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				log.warn("DBConnectionPool::closeResources", e);
			}
		}
	}
}
