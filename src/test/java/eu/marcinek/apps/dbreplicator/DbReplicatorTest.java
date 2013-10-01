package eu.marcinek.apps.dbreplicator;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.newLinkedList;
import static com.google.common.collect.Maps.newHashMap;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import com.google.common.collect.ImmutableList;

@RunWith(MockitoJUnitRunner.class)
public class DbReplicatorTest {
	
	static final String SAMPLE_QUERY_SQL = "querySql";
	static final String SAMPLE_INSERT_SQL = "insertSql";
	
	static final List<Map<String, Object>> NO_ROWS_IN_SOURCE_DB = newLinkedList();
	
	@Mock
	JdbcTemplate sourceJdbcTemplate;
	@Mock
	JdbcTemplate destinationJdbcTemplate;
	@InjectMocks
	DbReplicator dbReplicator = new DbReplicator();
	
	List<Map<String, Object>> sampleQueryResult;
	
	@Before 
	public void prepareSampleQueryResult() {
		sampleQueryResult = newArrayList();
		Map<String, Object> row1 = newHashMap();
		row1.put("row1_column1", "row1_value1");
		row1.put("row1_column2", "row1_value2");
		sampleQueryResult.add(row1);
		Map<String, Object> row2 = newHashMap();
		row1.put("row2_column1", "row2_value1");
		row1.put("row2_column2", "row2_value2");
		sampleQueryResult.add(row2);
	}
	
	@Before 
	public void injectPropertyValues() {
		setField(dbReplicator, "querySql", SAMPLE_QUERY_SQL);
		setField(dbReplicator, "insertSql", SAMPLE_INSERT_SQL);
	}
	
	@Test
	public void rowsFromSourceDbAreCopiedToDestinationDb() {
		//given
		given(sourceJdbcTemplate.queryForList(SAMPLE_QUERY_SQL)).willReturn(sampleQueryResult);
	
		//when
		dbReplicator.replicate();
		
		//then
		verify(destinationJdbcTemplate).batchUpdate(SAMPLE_INSERT_SQL, ImmutableList.of(
					new Object[] {"row1_value1", "row1_value2"},
					new Object[] {"row2_value1", "row2_value2"}
		));
	}	
	
	@Test
	public void rowsAreNotCopiedWhenThereIsNothingToCopy() {
		//given
		given(sourceJdbcTemplate.queryForList(SAMPLE_QUERY_SQL)).willReturn(NO_ROWS_IN_SOURCE_DB);
		
		//when
		dbReplicator.replicate();
		
		//then
		verifyZeroInteractions(destinationJdbcTemplate);
	}
}