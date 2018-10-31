<%--
  Created by IntelliJ IDEA.
  User: yanhong
  Date: 2018/10/12
  Time: 10:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>--%>
<%--<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix="sql"%>--%>
<html>
<head>
    <title>买买买情报局</title>
</head>
<body>
<%
    String address_my = request.getParameter("address");
//    <sql:setDataSource var="snapshot" driver="com.mysql.jdbc.Driver"
////    url="jdbc:mysql://localhost:3306/taobao?useUnicode=true&characterEncoding=utf-8"
////    user="root"  password="0000"/>
////
////    <sql:query dataSource="${snapshot}" var="result">
////    SELECT * from goods
////    where Url == url;
////    </sql:query>
    try {
    Class.forName("com.mysql.jdbc.Driver");
    String url = "jdbc:mysql://localhost:3306/taobao";
    String username = "root";
    String password = "0000";
    Connection conn = DriverManager.getConnection(url, username, password)

    if(conn != null){
//        out.print("数据库连接成功！");
//        out.print("<br />");
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "SELECT ID,Gname,Category,Pic,Url FROM goods WHERE Url=address_my;";
        stmt = conn.createStatement();
        rs = stmt.executeQuery(sql);
        out.print("查询结果：");
        out.print("<br />");
//        out.println("ID"+"  "+"NAME"+"  "+"number");
//        out.print("<br />");
        while (rs.next()) {
            out.println(rs.getString("ID")+" "+rs.getString("Gname")+"  "+rs.getString("Pic")+" "+rs.getString("Url"));
            out.print("<br />");
        }
    }else{
        out.print("连接失败！");
    }
    }catch (Exception e) {
    //e.printStackTrace();
    out.print("数据库连接异常！");
    }
%>

</body>
</html>
