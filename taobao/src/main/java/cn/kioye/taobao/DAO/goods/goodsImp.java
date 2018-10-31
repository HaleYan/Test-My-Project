package cn.kioye.taobao.DAO.goods;

import cn.kioye.taobao.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class goodsImp implements goodsDAO {//输入商品ID 得到商品一个对象，包含所有属性
    public goods getDetail(String id)
    {
        Connection conn = JDBCUtils.getConnection();//因为父类，可以直接调用
        String sql = "select ID,Gname,Category,Pic,Url from goods where ID=?";
        PreparedStatement ps = null;
        goods goods = null;
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, id);
            System.out.println(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next())
            {
            String ID = rs.getString(1);
            String Gname = rs.getString(2);
            String Category = rs.getString(3);
            String Pic = rs.getString(4);//rs.getNString(4);
            String Url = rs.getString(5);
            goods = new goods(ID, Gname, Category, Pic, Url);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,null,null);
        }
        return goods;
    }

    }
