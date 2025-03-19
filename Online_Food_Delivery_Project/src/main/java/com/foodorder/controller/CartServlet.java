package com.foodorder.controller;

import com.foodorder.dao.CartDAO;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

@SuppressWarnings("serial")
public class CartServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("user_id"));
        int itemId = Integer.parseInt(request.getParameter("item_id"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        CartDAO cartDAO = new CartDAO();
        if (cartDAO.addToCart(userId, itemId, quantity)) {
            response.sendRedirect("cart.jsp");
        } else {
            response.sendRedirect("menu.jsp");
        }
    }
}