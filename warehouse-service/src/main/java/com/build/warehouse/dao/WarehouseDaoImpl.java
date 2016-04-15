package com.build.warehouse.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.build.event.customer.CustomerEventData;
import com.build.event.order.OrderEventData;

@Repository
public class WarehouseDaoImpl implements WarehouseDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public void markCustomerDeleted(int customerId) {
		jdbcTemplate.update("UPDATE customer SET deleted = true WHERE id = ?", customerId);
	}

	@Override
	public void saveCustomer(CustomerEventData customer) {
		int rowCount = jdbcTemplate.update("UPDATE customer SET email = ?, name = ? WHERE id = ?", customer.getEmail(),
				customer.getName(), customer.getId());
		if (rowCount == 0) {
			jdbcTemplate.update("INSERT INTO customer (id, email, name) VALUES (?, ?, ?)", customer.getId(),
					customer.getEmail(), customer.getName());
		}
		jdbcTemplate.update("UPDATE customer_order SET email = ?, name = ? WHERE customer_id = ?", customer.getEmail(),
				customer.getName(), customer.getId());
	}

	@Override
	public void markOrderDeleted(int orderId) {
		jdbcTemplate.update("UPDATE customer_order SET deleted = true WHERE order_id = ?", orderId);
	}

	@Override
	public void saveOrder(OrderEventData order) {
		int rowCount = jdbcTemplate.update(
				"UPDATE customer_order SET line_item = ?, unit_price = ?, quantity = ? WHERE order_id = ?",
				order.getLineItem(), order.getUnitPrice(), order.getQuantity(), order.getId());
		if (rowCount == 0) {
			CustomerEventData customer = jdbcTemplate.queryForObject("SELECT id, email, name FROM customer WHERE id = ?",
					new RowMapper<CustomerEventData>() {

						@Override
						public CustomerEventData mapRow(ResultSet rs, int rowNum) throws SQLException {
							CustomerEventData customer = new CustomerEventData();
							customer.setId(rs.getInt("id"));
							customer.setEmail(rs.getString("email"));
							customer.setName(rs.getString("name"));
							return customer;
						}
					}, order.getCustomerId());
			jdbcTemplate.update(
					"INSERT INTO customer_order (order_id, customer_id, email, name, line_item, unit_price, quantity) VALUES (?, ?, ?, ?, ?, ?, ?)",
					order.getId(), customer.getId(), customer.getEmail(), customer.getName(), order.getLineItem(),
					order.getUnitPrice(), order.getQuantity());
		}
	}
}
