package org.neo4j.jdbc.embedded.v2;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DriverTest {

	@Rule public ExpectedException thrown = ExpectedException.none();

	private Driver driver;

	@Before public void prepare() throws Exception {
		driver = new Driver();
	}

	@Test public void failsToConnectsToInvalidUri() throws Exception {
		thrown.expect(SQLException.class);
		thrown.expectMessage("JDBC URL is not correct.\n"
			+ "Valid URL formats are:\n"
			+ "\t - 'jdbc:neo4j:mem:<name>'\n"
			+ "\t - 'jdbc:neo4j:file:/path/to/graph.db'");

		driver.connect("foo", new Properties());
	}

	@Test public void connectsToValidInMemoryUri() throws Exception {
		Connection connection = driver.connect("jdbc:neo4j:mem:mytestdb", new Properties());

		assertThat(connection, is(instanceOf(EmbeddedConnection.class)));
	}

	@Test public void connectsToValidFileUri() throws Exception {
		Connection connection = driver.connect("jdbc:neo4j:file:/tmp/graph.db", new Properties());

		assertThat(connection, is(instanceOf(EmbeddedConnection.class)));
	}

	@Test public void returnsProperVersion() {
		assertThat(driver.getMajorVersion(), is(equalTo(2)));
		assertThat(driver.getMinorVersion(), is(equalTo(0)));
	}
}