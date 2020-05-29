package com.goodperson.layered.dao;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import com.goodperson.layered.dto.Guestbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import static com.goodperson.layered.dao.GuestDaoSqls.*;

@Repository
public class GuestbookDao {
    
    @Autowired
    private NamedParameterJdbcTemplate jdbc;
    
    @Autowired
    private DataSource dataSource;

    private SimpleJdbcInsert insertAction;
    private RowMapper<Guestbook> rowMapper = BeanPropertyRowMapper.newInstance(Guestbook.class);

    @PostConstruct
    private void postConstruct(){
        insertAction = new SimpleJdbcInsert(dataSource)
        .withTableName("guestbook")
        .usingGeneratedKeyColumns("id");

    }

    public List<Guestbook> selectAll(int start, int limit){
        Map<String, Integer> params = new HashMap<>();
        params.put("start", start);
        params.put("limit", limit);
        return jdbc.query(SELECT_PAGING, params, rowMapper);
    }

    public long insert(Guestbook guestbook){
        SqlParameterSource params = new BeanPropertySqlParameterSource(guestbook);
        return insertAction.executeAndReturnKey(params).longValue();
    }

    public int deleteById(long id){
        Map<String, Long> params = Collections.singletonMap("id", id);
        return jdbc.update(DELETE_BY_ID, params);
    }

    public int selectCount(){
        return jdbc.queryForObject(SELECT_COUNT, Collections.emptyMap(), Integer.class);
    }
}