package com.build.order.dao;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
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

import com.build.order.OrderApplication;
import com.build.order.domain.Order;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(OrderApplication.class)
@ActiveProfiles("unit")
@Transactional
public class OrderDaoIT {

	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final String SQL_INSERT_ORDER = "INSERT INTO orders (customer_id, line_item, unit_price, quantity) VALUES (?, ?, ?, ?)";
	private Integer testOrderId;
	
	@Before
	public void before() {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pstmt = con.prepareStatement(SQL_INSERT_ORDER, new String[] { "id" });
				pstmt.setInt(1, 3);
				pstmt.setString(2, "test line item");
				pstmt.setBigDecimal(3, new BigDecimal("20.00"));
				pstmt.setInt(4, 1);
				return pstmt;
			}
		}, keyHolder);
		testOrderId = keyHolder.getKey().intValue();
	}
	
	@Test
	public void testGetOrder() {
		Order order = orderDao.getOrder(testOrderId);
		assertThat(order, notNullValue());
		assertThat(order.getId(), is(testOrderId));
	}
	
	@Test
	public void testInsertOrder() {
		Order order = new Order();
		order.setCustomerId(-1);
		order.setLineItem("insert test");
		order.setUnitPrice(new BigDecimal("19.99"));
		order.setQuantity(30);
		orderDao.insertOrder(order);
		assertThat(order.getId(), notNullValue());
	}
	
	@Test
	public void testUpdateOrder() {
		Order order = new Order();
		order.setId(testOrderId);
		order.setLineItem("updated");
		order.setUnitPrice(new BigDecimal("100.00"));
		order.setQuantity(20);
		orderDao.updateOrder(order);
	}
	
	@Test
	public void testDeleteOrder() {
		orderDao.deleteOrder(testOrderId);
	}
}
