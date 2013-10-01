package eu.marcinek.apps.dbreplicator;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DbReplicator {

	@Autowired
	private JdbcTemplate sourceJdbcTemplate;

	@Autowired
	private JdbcTemplate destinationJdbcTemplate;

	@Value("${db_replicator.source.db.sql.query.columns_number}")
	private int columnsNumber;

	@Value("${db_replicator.source.db.sql.query}")
	private String querySql;

	@Value("${db_replicator.destination.db.sql.insert}")
	private String insertSql;

	public void replicate() {
		List<Map<String, Object>> rowsToCopy = sourceJdbcTemplate.queryForList(querySql);
		if (rowsToCopy.size() > 0) {
			List<Object[]> valuesToInsert = newArrayList();
			for (Map<String, Object> row : rowsToCopy) {
				valuesToInsert.add(row.values().toArray());
			}
			destinationJdbcTemplate.batchUpdate(insertSql, valuesToInsert);
		}
	}

	// for testing purposes
	void setQuerySql(String querySql) {
		this.querySql = querySql;
	}

	// for testing purposes
	void setInsertSql(String insertSql) {
		this.insertSql = insertSql;
	}
}