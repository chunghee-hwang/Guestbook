package com.goodperson.layered.dao;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import com.goodperson.layered.dto.Log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class LogDao {

    @Autowired
    private DataSource dataSource;

    private SimpleJdbcInsert insertAction;

    // 초기화 애노테이션
    @PostConstruct
    private void postConstruct() {
        insertAction = 
        new SimpleJdbcInsert(dataSource)
        .withTableName("log")
        .usingGeneratedKeyColumns("id"); //auto_increment
    }

    public long insert(Log log){
        SqlParameterSource params = new BeanPropertySqlParameterSource(log);
        
        // 생성된 id 리턴
        return insertAction.executeAndReturnKey(params).longValue();
    }

}