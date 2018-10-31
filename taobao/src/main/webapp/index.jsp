<%@page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> <!--输出,条件,迭代标签库-->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="fmt"%> <!--数据格式化标签库-->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="sql"%> <!--数据库相关标签库-->
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="fn"%> <!--常用函数标签库-->
<html lang="zh">
<head>
    <meta charset="UTF-8">
    <title>买买买情报局</title>

    <style>
        *{
            margin: 0;
            padding:0;
        }
        container{
            margin:20px 0px;
        }
    </style>
</head>
<body>
<div>
<h2>我是第一个框</h2>
    <form method="get" action="/search">
        <input name="key" placeholder="请输入商品URL"><input type="submit" value="搜索">
    </form>
</div>
<div>
    <h2>我是第二个框</h2>
    <img src="">
    <table>
        <thead>
        商品名
        </thead>
        <tbody>
        <tr>
            <th>时间</th>
            <td></td>
        </tr>
        <tr>
            <th>价格</th>
            <td></td>
        </tr>
        </tbody>
    </table>
<button>推荐</button>
</div>
<div>
    <table>
        <tbody>
        <c:forEach var="item" items="${items}">
            <tr><td>${item}</td></tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>
