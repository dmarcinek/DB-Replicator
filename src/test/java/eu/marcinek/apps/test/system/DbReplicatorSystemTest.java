package eu.marcinek.apps.test.system;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import eu.marcinek.apps.dbreplicator.DbReplicator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "DbReplicatorSystemTest.xml" })
public class DbReplicatorSystemTest {

	static final int THIRD_ROW = 2;
	static final int SECOND_ROW = 1;
	static final int FIRST_ROW = 0;

	@Autowired
	DbReplicator dbReplicator;

	@Autowired
	JdbcTemplate sourceJdbcTemplate;

	@Autowired
	JdbcTemplate destinationJdbcTemplate;

	@Test
	public void makesCopyOfRowsFromSourceDbToDestinationDB() {
		// given
		List<Object[]> valuesToInsert = newArrayList();
		valuesToInsert.add(new Object[] { 1, "Jan", "Kowalski", "Kwiatkowa", "Rzeszow", "35-600" });
		valuesToInsert.add(new Object[] { 2, "Anna", "Nowak", "Rybia", "Warszawa", "00-800" });
		valuesToInsert.add(new Object[] { 3, "Roman", "Piekny", "Strusia", "Kraków", "33-399" });
		sourceJdbcTemplate.batchUpdate("INSERT INTO PERSON VALUES (?, ?, ?, ?, ?, ?)", valuesToInsert);

		// when
		dbReplicator.replicate();

		// then
		List<Map<String, Object>> copiedRows = destinationJdbcTemplate.queryForList("SELECT FIRST_NAME FROM PERSON");
		assertThat(copiedRows.size(), is(valuesToInsert.size()));
		assertThat(valueIn(copiedRows, FIRST_ROW, "FIRST_NAME"), is("Jan"));
		assertThat(valueIn(copiedRows, SECOND_ROW, "FIRST_NAME"), is("Anna"));
		assertThat(valueIn(copiedRows, THIRD_ROW, "FIRST_NAME"), is("Roman"));
	}

	private String valueIn(List<Map<String, Object>> copiedRows, int rowNo, String columnName) {
		return (String) copiedRows.get(rowNo).get(columnName);
	}

}