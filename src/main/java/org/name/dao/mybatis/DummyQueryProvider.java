package org.name.dao.mybatis;

import java.util.Map;

import lombok.extern.log4j.Log4j;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.util.StringUtils;

@Log4j
public class DummyQueryProvider {
	//If passed an id, it will return that specific data, else, it returns all data
	public String getDataByIdSQL(Map params) {
		final String id = (String) params.get("id");
		
		String sql = new SQL() {{
			SELECT("id as id,data as data");
			FROM("dummy_data");
			if(!StringUtils.isEmpty(id)){
				WHERE("id = #{id}");
			}
		}}.toString();
		
		log.info("QUERY STRING: " + sql);
		return sql;
	}
}
