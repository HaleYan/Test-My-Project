package cn.kioye.taobao.DAO.category;


import cn.kioye.taobao.utils.JDBCUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class categoryImp implements categoryDAO {

    public String getPId(String cid) {//输入cid 得到pid
        Connection conn= JDBCUtils.getConnection();//因为父类，可以直接调用
        String sql="select P_ID from category where C_ID=?";
        PreparedStatement ps = null;
        String pid = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,cid);
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {
                pid = rs.getString("P_ID");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,null,null);
        }
        return pid;
    }

    @Override
    public String getPName(String temp) {//输入cid(category),得到名字
        Connection conn= JDBCUtils.getConnection();//因为父类，可以直接调用
        PreparedStatement ps = null;
        String sql1="select P_ID from category where C_ID=?";
        String pid=temp;
        while(!pid.equalsIgnoreCase("0"))
        {
            try {
                ps=conn.prepareStatement(sql1);
                ps.setString(1,pid);
                ResultSet rs=ps.executeQuery();
                while (rs.next())
                {
                 temp=pid;
                pid=rs.getString("P_ID");
                System.out.println("还在寻找的PID="+pid);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        String sql2="select name from category where C_ID=?";
        String pName = null;
        try {
            ps = conn.prepareStatement(sql2);
            ps.setString(1, temp);
            ResultSet rs=ps.executeQuery();
            while (rs.next())
            {
                pName = rs.getString("name");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,null,null);
        }
        return pName;
    }

    @Override
    public String getCID(String id) {
        Connection conn= JDBCUtils.getConnection();//因为父类，可以直接调用
        String sql="select Category from goods where ID=?";
        PreparedStatement ps = null;
        String cid = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            ResultSet rs=ps.executeQuery();
            while (rs.next()) {
                cid = rs.getString("Category");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,null,null);
        }
        return cid;
    }

    @Override
    public List<String> getIds(String myid) {//输入id得到所有同类型的商品id
        Connection conn= JDBCUtils.getConnection();//因为父类，可以直接调用
        String cid=getCID(myid);
        String sql="select ID from goods where goods.Category=?";
        PreparedStatement ps = null;
        List<String>  ids = new ArrayList<>();
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1,cid);
            ResultSet rs=ps.executeQuery();
            while(rs.next()){
                String id=rs.getString("ID");
                ids.add(id);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,null,null);
        }
        return ids;
    }
}
