package com.build.customer.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.build.customer.controller.ResourceNotFoundException;
import com.build.customer.domain.Customer;

@Repository
public class CustomerDaoImpl implements CustomerDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String SQL_GET_CUSTOMER = "SELECT id, name, email, internal_data from customer where id = ?";
	private static final String SQL_INSERT_CUSTOMER = "INSERT into customer (name, email, internal_data) VALUES (?, ?, ?)";
	private static final String SQL_UPDATE_CUSTOMER = "UPDATE customer SET name = ?, email = ?, internal_data = ? WHERE id = ?";
	private static final String SQL_DELETE_CUSTOMER = "DELETE FROM customer WHERE id = ?";
	private static final String SQL_GET_CUSTOMERS_NAME = "SELECT id, name, email, internal_data from customer where name like ?";
	
	private RowMapper<Customer> customerRowMapper = new RowMapper<Customer>() {
		
		@Override
		public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
			Customer customer = new Customer();
			customer.setId(rs.getInt("id"));
			customer.setName(rs.getString("name"));
			customer.setEmail(rs.getString("email"));
			customer.setInternalData(rs.getString("internal_data"));
			return customer;
		}
	};

	@Override
	public Customer getCustomer(int id) {
		try {
			return jdbcTemplate.queryForObject(SQL_GET_CUSTOMER, customerRowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException();
		}
	}
	
	@Override
	public List<Customer> getCustomersByName(String name) {
		return jdbcTemplate.query(SQL_GET_CUSTOMERS_NAME, customerRowMapper, name + '%');
	}

	@Override
	public void insertCustomer(Customer customer) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
				PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT_CUSTOMER, new String[] {"id"});
				pstmt.setString(1, customer.getName());
				pstmt.setString(2, customer.getEmail());
				pstmt.setString(3, customer.getInternalData());
				return pstmt;
			}
		}, keyHolder);
		customer.setId(keyHolder.getKey().intValue());
	}

	@Override
	public void updateCustomer(Customer customer) {
		int rowCount = jdbcTemplate.update(SQL_UPDATE_CUSTOMER, customer.getName(), customer.getEmail(), customer.getInternalData(), customer.getId());
		if (rowCount == 0) {
			throw new ResourceNotFoundException();
		}
	}

	@Override
	public void deleteCustomer(int id) {
		int rowCount = jdbcTemplate.update(SQL_DELETE_CUSTOMER, id);
		if (rowCount == 0) {
			throw new ResourceNotFoundException();
		}
	}
}
