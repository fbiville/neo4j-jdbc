/*
 * Copyright (c) 2016 LARUS Business Automation [http://www.larus-ba.it]
 * <p>
 * This file is part of the "LARUS Integration Framework for Neo4j".
 * <p>
 * The "LARUS Integration Framework for Neo4j" is licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * Created on 03/02/16
 */
package org.neo4j.jdbc;

import org.neo4j.jdbc.utils.ExceptionBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import static java.util.Collections.singleton;

/**
 * @author AgileLARUS
 * @since 3.0.0
 */
public abstract class BaseDriver implements java.sql.Driver {

	/**
	 * JDBC prefix for the connection url.
	 */
	protected static final String JDBC_PREFIX = "jdbc:neo4j:";

	/**
	 * Driver prefixes for the connection url.
	 */
	private String[] DRIVER_PREFIXES = new String[0];

	/**
	 * Constructor for extended class.
	 *
	 * @param prefix Prefix of the driver for the connection url.
	 */
	protected BaseDriver(String prefix) throws SQLException {
		this(new String[] {prefix});
	}

	protected BaseDriver(String[] prefixes) throws SQLException {
		DRIVER_PREFIXES = prefixes;
		DriverManager.registerDriver(this);
	}

	@Override public abstract Connection connect(String url, Properties info) throws SQLException;

	@Override public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
		return new DriverPropertyInfo[0];
	}

	@Override public int getMajorVersion() {
		return 3;
	}

	@Override public int getMinorVersion() {
		return 0;
	}

	@Override public boolean jdbcCompliant() {
		return false;
	}

	@Override public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw ExceptionBuilder.buildUnsupportedOperationException();
	}

	@Override public boolean acceptsURL(String url) throws SQLException {
		if (url == null) {
			throw new SQLException("null is not a valid url");
		}
		String[] pieces = url.split(":");
		if (pieces.length > 3) {
			if (url.startsWith(JDBC_PREFIX)) {
				if (DRIVER_PREFIXES.length > 0) {
					for (String prefix : DRIVER_PREFIXES) {
						if(pieces[2].matches(prefix)) {
							return true;
						}
					}
				}
				else {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Parse the url string and construct a properties object.
	 *
	 * @param url The url to parse
	 * @param params The properties
	 */
	protected Properties parseUrlProperties(String url, Properties params) {
		Properties properties = new Properties();
		if(params != null) {
			for (Map.Entry<Object, Object> entry : params.entrySet()) {
				properties.put(entry.getKey().toString().toLowerCase(),entry.getValue());
			}
		}
		if (url.contains("?")) {
			String urlProps = url.substring(url.indexOf('?') + 1);
			urlProps = decodeUrlComponent(urlProps);
			String[] props = urlProps.split("[,&]");
			for (String prop : props) {
				int idx = prop.indexOf('=');
				if (idx != -1) {
					String key = prop.substring(0, idx);
					String value = prop.substring(idx + 1);
					properties.put(key.toLowerCase(), value);
				} else {
					properties.put(prop.toLowerCase(), "true");
				}
			}
			properties.put("user", getUser(properties));
		}

		return properties;
	}

	private String decodeUrlComponent(String urlProps) {
		try {
			return URLDecoder.decode(urlProps, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	protected String getUser(Properties properties) {
		String user = properties.getProperty("user");
		if (user!=null && !user.trim().isEmpty()) return user;
		user = properties.getProperty("username");
		if (user!=null && !user.trim().isEmpty()) return user;
		return "neo4j";
	}
}
