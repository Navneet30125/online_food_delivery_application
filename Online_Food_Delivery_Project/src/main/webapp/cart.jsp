<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%@ page import="com.foodorder.dao.CartDAO, com.foodorder.model.CartItem, java.util.List" %>
<%
    int userId = (Integer) session.getAttribute("user_id");
    CartDAO cartDAO = new CartDAO();
    List<CartItem> cartItems = cartDAO.getCartItems(userId);
%>
<h1>Your Cart</h1>
<table class="table">
    <tr>
        <th>Item</th>
        <th>Quantity</th>
        <th>Price</th>
    </tr>
    <% for (CartItem item : cartItems) { %>
    <tr>
        <td><%= item.getItemName() %></td>
        <td><%= item.getQuantity() %></td>
        <td><%= item.getPrice() %></td>
    </tr>
    <% } %>
</table>
<a href="checkout.jsp" class="btn btn-primary">Checkout</a>
</body>
</html>