package com.foodorder.dao;

import com.foodorder.util.DBConnection;
import java.sql.*;

public class CartDAO {
    public boolean addToCart(int userId, int itemId, int quantity) {
        String query = "INSERT INTO cart (user_id, item_id, quantity) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, userId);
            pst.setInt(2, itemId);
            pst.setInt(3, quantity);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}