package com.foodorder.dao;
import com.foodorder.model.Order;
import com.foodorder.model.OrderItem;
import com.foodorder.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // Method to create a new order
    public boolean createOrder(int userId, int restaurantId, double totalAmount, List<OrderItem> orderItems) {
        String orderQuery = "INSERT INTO orders (user_id, restaurant_id, total_amount, status) VALUES (?, ?, ?, ?)";
        String orderItemQuery = "INSERT INTO order_items (order_id, item_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            // Disable auto-commit to handle transactions
            conn.setAutoCommit(false);

            // Insert into orders table
            try (PreparedStatement orderStmt = conn.prepareStatement(orderQuery, Statement.RETURN_GENERATED_KEYS)) {
                orderStmt.setInt(1, userId);
                orderStmt.setInt(2, restaurantId);
                orderStmt.setDouble(3, totalAmount);
                orderStmt.setString(4, "pending"); // Default status
                int rowsAffected = orderStmt.executeUpdate();

                if (rowsAffected > 0) {
                    // Retrieve the generated order ID
                    ResultSet rs = orderStmt.getGeneratedKeys();
                    if (rs.next()) {
                        int orderId = rs.getInt(1);

                        // Insert into order_items table
                        try (PreparedStatement orderItemStmt = conn.prepareStatement(orderItemQuery)) {
                            for (OrderItem item : orderItems) {
                                orderItemStmt.setInt(1, orderId);
                                orderItemStmt.setInt(2, item.getItemId());
                                orderItemStmt.setInt(3, item.getQuantity());
                                orderItemStmt.setDouble(4, item.getPrice());
                                orderItemStmt.addBatch();
                            }
                            orderItemStmt.executeBatch();
                        }

                        // Commit the transaction
                        conn.commit();
                        return true;
                    }
                }
            } catch (SQLException e) {
                // Rollback the transaction in case of an error
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to retrieve order history for a user
    public List<Order> getOrderHistory(int userId) {
        List<Order> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_date DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setOrderId(rs.getInt("order_id"));
                order.setUserId(rs.getInt("user_id"));
                order.setRestaurantId(rs.getInt("restaurant_id"));
                order.setTotalAmount(rs.getDouble("total_amount"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setStatus(rs.getString("status"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    // Method to retrieve order items for a specific order
    public List<OrderItem> getOrderItems(int orderId) {
        List<OrderItem> orderItems = new ArrayList<>();
        String query = "SELECT * FROM order_items WHERE order_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, orderId);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setOrderItemId(rs.getInt("order_item_id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setItemId(rs.getInt("item_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getDouble("price"));
                orderItems.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItems;
    }

    // Method to update the status of an order
    public boolean updateOrderStatus(int orderId, String status) {
        String query = "UPDATE orders SET status = ? WHERE order_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setString(1, status);
            pst.setInt(2, orderId);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}