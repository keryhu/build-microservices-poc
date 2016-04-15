package com.build.order.dao;

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

import com.build.order.controller.ResourceNotFoundException;
import com.build.order.domain.Order;

@Repository
public class OrderDaoImpl implements OrderDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String SQL_GET_ORDER = "SELECT id, customer_id, line_item, unit_price, quantity FROM orders WHERE id = ?";
	private static final String SQL_INSERT_ORDER = "INSERT INTO orders (customer_id, line_item, unit_price, quantity) VALUES (?, ?, ?, ?)";
	private static final String SQL_UPDATE_ORDER = "UPDATE orders SET line_item = ?, unit_price = ?, quantity = ? WHERE id = ?";
	private static final String SQL_DELETE_ORDER = "DELETE FROM orders WHERE id = ?";
	private static final String SQL_GET_CUSTOMER_ORDERS = "SELECT id, customer_id, line_item, unit_price, quantity FROM orders WHERE customer_id = ?";
	
	private RowMapper<Order> orderRowMapper = new RowMapper<Order> () {
		@Override
		public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
			Order order = new Order();
			order.setId(rs.getInt("id"));
			order.setCustomerId(rs.getInt("customer_id"));
			order.setLineItem(rs.getString("line_item"));
			order.setUnitPrice(rs.getBigDecimal("unit_price"));
			order.setQuantity(rs.getInt("quantity"));
			return order;
		}
	};

	@Override
	public Order getOrder(int id) {
		try {
			return jdbcTemplate.queryForObject(SQL_GET_ORDER, orderRowMapper, id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException();
		}
	}

	@Override
	public void insertOrder(Order order) {
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement pstmt = con.prepareStatement(SQL_INSERT_ORDER, new String[] { "id" });
				pstmt.setInt(1, order.getCustomerId());
				pstmt.setString(2, order.getLineItem());
				pstmt.setBigDecimal(3, order.getUnitPrice());
				pstmt.setInt(4, order.getQuantity());
				return pstmt;
			}
		}, keyHolder);
		order.setId(keyHolder.getKey().intValue());
	}

	@Override
	public void updateOrder(Order order) {
		int rowCount = jdbcTemplate.update(SQL_UPDATE_ORDER, order.getLineItem(), order.getUnitPrice(), order.getQuantity(), order.getId());
		if (rowCount == 0) {
			throw new ResourceNotFoundException();
		}
	}

	@Override
	public void deleteOrder(int id) {
		int rowCount = jdbcTemplate.update(SQL_DELETE_ORDER, id);
		if (rowCount == 0) {
			throw new ResourceNotFoundException();
		}
	}
	
	@Override
	public List<Order> getCustomerOrders(int customerId) {
		return jdbcTemplate.query(SQL_GET_CUSTOMER_ORDERS, orderRowMapper, customerId);
	}

}
