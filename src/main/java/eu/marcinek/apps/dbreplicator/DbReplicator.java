package eu.marcinek.apps.dbreplicator;

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
	}

}