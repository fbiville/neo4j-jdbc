package org.neo4j.jdbc.embedded.v2;

import org.neo4j.jdbc.BaseDriver;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class Driver extends BaseDriver {

	/**
	 * Constructor for the embedded driver
	 */
	protected Driver() throws SQLException {
		super(new String[] { "mem", "file" });
	}

	@Override public Connection connect(String url, Properties info) throws SQLException {
		if (!acceptsURL(url)) {
			throw new SQLException(
					"JDBC URL is not correct.\nValid URL formats are:\n" + "\t - 'jdbc:neo4j:mem:<name>'\n" + "\t - 'jdbc:neo4j:file:/path/to/graph.db'");
		}
		return new EmbeddedConnection(info, url);
	}

	@Override public int getMajorVersion() {
		return 2;
	}

	@Override public int getMinorVersion() {
		return 0;
	}
}
