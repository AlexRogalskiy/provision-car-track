package com.provision.cartrack.registry.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.provision.cartrack.helpers.Pagination;
import com.provision.cartrack.registry.EngineType;
import com.provision.cartrack.registry.Make;

public class ModelRepositoryImpl implements ModelRepositoryCustom {

	@Autowired
	private NamedParameterJdbcTemplate db;

	@Override
	public Pagination<Model> search(Pagination<Model> request) {

		String columns = "select m.id, m.model_name, m.model_year, make.id make_id, make.make_name, et.id engine_type_id, et.engine_type_name ";

		String sql = " from models m join makes make on make.id=m.make_id "
				+ " join engine_types et on et.id=m.engine_type_id " + " where true ";
		Map<String, Object> filters = request.getFilters();
		if (filters.containsKey("model_name")) {
			sql += " and m.model_name ILIKE :model_name";
			filters.put("model_name", "%" + filters.get("model_name") + "%");
		}
		if (filters.containsKey("model_year")) {
			sql += " and m.model_year = :model_year";
		}
		if (filters.containsKey("make_name")) {
			sql += " and make.make_name = :make_name";
		}

		String countQuery = "select count(1) total " + sql;
		Map<String, Object> countResult = db.queryForMap(countQuery, filters);
		Long totalCount = Long.parseLong("" + countResult.get("total"));
		request.setTotalCount(totalCount);

		Map<String, String> safeSorts = new HashMap<>();
		safeSorts.put("model_name", " ORDER BY m.model_name");
		safeSorts.put("model_year", " ORDER BY m.model_year");
		safeSorts.put("make_name", " ORDER BY make.make_name");
		safeSorts.put("engine_type_name", " ORDER BY et.engine_type_name");
		String sortCol = safeSorts.containsKey(request.getSortColumn()) ? safeSorts.get(request.getSortColumn())
				: " ORDER BY m.id ";
		String sortOrder = request.getSortOrder().equals("DESC") ? " DESC " : " ASC ";
		String orderby = sortCol + sortOrder;
		String limit = " LIMIT " + request.getPageSize();
		String offset = " OFFSET " + ((request.getCurrentPage() - 1) * request.getPageSize());

		List<Model> results = db.query(columns + sql + orderby + limit + offset, new MapSqlParameterSource(filters),
				(rs, i) -> {
					Model m = new Model();
					int colIdx = 1;
					m.setId(rs.getLong(colIdx++));
					m.setName(rs.getString(colIdx++));
					m.setYear(rs.getInt(colIdx++));
					Make make = new Make();
					make.setId(rs.getLong(colIdx++));
					make.setName(rs.getString(colIdx++));
					m.setMake(make);
					EngineType engine = new EngineType();
					engine.setId(rs.getLong(colIdx++));
					engine.setName(rs.getString(colIdx++));
					m.setEngineType(engine);
					return m;
				});

		request.setResults(results);
		return request;
	}

}