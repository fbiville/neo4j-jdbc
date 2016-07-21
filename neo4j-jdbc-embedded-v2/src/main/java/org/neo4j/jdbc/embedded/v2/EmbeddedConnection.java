package org.neo4j.jdbc.embedded.v2;

import org.neo4j.jdbc.Connection;
import org.neo4j.jdbc.DatabaseMetaData;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static java.sql.ResultSet.CLOSE_CURSORS_AT_COMMIT;

public class EmbeddedConnection extends Connection {

	protected EmbeddedConnection(Properties properties, String url) {
		super(properties, url, CLOSE_CURSORS_AT_COMMIT);
	}

	@Override public DatabaseMetaData getMetaData() throws SQLException {
		return null;
	}

	@Override public void setAutoCommit(boolean autoCommit) throws SQLException {
	}

	@Override public boolean getAutoCommit() throws SQLException {
		return false;
	}

	@Override public void commit() throws SQLException {

	}

	@Override public void rollback() throws SQLException {

	}

	@Override public Statement createStatement() throws SQLException {
		return null;
	}

	@Override public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
		return null;
	}

	@Override public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		return null;
	}

	@Override public PreparedStatement prepareStatement(String sql) throws SQLException {
		return null;
	}

	@Override public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
		return null;
	}

	@Override public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
		return null;
	}

	@Override public void close() throws SQLException {

	}

	@Override public boolean isClosed() throws SQLException {
		return false;
	}

	@Override public boolean isValid(int timeout) throws SQLException {
		return false;
	}
}
