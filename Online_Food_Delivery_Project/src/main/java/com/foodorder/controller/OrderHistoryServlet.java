package com.foodorder.controller;

import java.io.IOException;

import com.foodorder.dao.OrderDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class OrderHistoryServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("user_id"));
        OrderDAO orderDAO = new OrderDAO();
        request.setAttribute("orders", orderDAO.getOrderHistory(userId));
        request.getRequestDispatcher("orderHistory.jsp").forward(request, response);
    }
}