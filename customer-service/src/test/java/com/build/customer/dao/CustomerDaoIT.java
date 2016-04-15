package com.build.customer.dao;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.build.customer.CustomerApplication;
import com.build.customer.domain.Customer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CustomerApplication.class)
@ActiveProfiles("unit")
@Transactional
public class CustomerDaoIT {
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final String SQL_INSERT_CUSTOMER = "INSERT INTO customer (name, email, internal_data) VALUES (?, ?, ?)";
	
	private Integer testCustomerId;
	
	@Before
	public void before() {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pstmt = con.prepareStatement(SQL_INSERT_CUSTOMER, new String[] {"id"});
				pstmt.setString(1, "Test");
				pstmt.setString(2, "test@test.com");
				pstmt.setString(3, "test internal data");
				return pstmt;
			}
		}, keyHolder);
		testCustomerId = keyHolder.getKey().intValue();
	}
	
	@Test
	public void testInsertCustomer() {
		Customer customer = new Customer();
		customer.setName("Insert Test");
		customer.setEmail("insert.test@test.com");
		customer.setInternalData("internal data");
		customerDao.insertCustomer(customer);
		assertThat(customer.getId(), notNullValue());
	}
	
	@Test
	public void testGetCustomer() {
		Customer customer = customerDao.getCustomer(testCustomerId);
		assertThat(customer, notNullValue());
		assertThat(customer.getId(), is(testCustomerId));
	}
	
	@Test
	public void testUpdateCustomer() {
		Customer customer = new Customer();
		customer.setId(testCustomerId);
		customer.setName("Updated");
		customer.setEmail("updated@updated.com");
		customer.setInternalData("updated");
		customerDao.updateCustomer(customer);
	}

	@Test
	public void testDeleteCustomer() {
		customerDao.deleteCustomer(testCustomerId);
	}
}
