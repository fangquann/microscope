package com.vipshop.microscope.hive.repository;

import java.util.List;

import org.apache.hadoop.hive.service.HiveClient;
import org.springframework.data.hadoop.hive.HiveClientCallback;
import org.springframework.data.hadoop.hive.HiveTemplate;

import com.vipshop.microscope.hive.HiveTemplateFactory;

public class TraceRepository {
	
	private HiveTemplate hiveTemplate = HiveTemplateFactory.HIVE_TEMPLATE;
	
	public void create() {
		hiveTemplate.execute(new HiveClientCallback<Boolean>() {

			@Override
			public Boolean doInHive(HiveClient hiveClient) throws Exception {
				try {
					hiveClient.execute("use default");
					hiveClient.execute("CREATE EXTERNAL TABLE h_trace(key string, trace_id bigint , duration bigint,trace_name string,start_date bigint )"    
									 + "STORED BY \'org.apache.hadoop.hive.hbase.HBaseStorageHandler\'" 
                                     + "WITH SERDEPROPERTIES (\"hbase.columns.mapping\" = \":key,cf:trace_id,cf:duration,cf:trace_name,cf:start_timestamp\")"
                                     + "TBLPROPERTIES(\"hbase.table.name\" = \"trace\"") ;
				} catch (Exception e) {
					return false;
				}
				return true;
			}
		});
	}
	
	public void drop() {
		hiveTemplate.execute(new HiveClientCallback<Boolean>() {

			@Override
			public Boolean doInHive(HiveClient hiveClient) throws Exception {
				try {
					hiveClient.execute("use default");
					hiveClient.execute("drop table h_trace");
				} catch (Exception e) {
					return false;
				}
				return true;
			}
		});
	}

	
	public int count() {
		return hiveTemplate.queryForInt("select count(*) from test.h_trace");
	}
	
	public List<String> callTime() {
		return hiveTemplate.query("select trace_name, COUNT(duration) as dur_count from default.h_trace  GROUP BY trace_name order by dur_count desc limit 2");
	}
	
	public List<String> callDura() {
		return hiveTemplate.query("select trace_name,trace_id,duration  from h_trace   order by duration desc limit 2");
	}
	
}
