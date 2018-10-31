package cn.kioye.taobao.DAO.comment;

import cn.kioye.taobao.utils.JDBCUtils;
import com.sun.xml.internal.bind.v2.model.core.ID;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class commentImp implements commentDAO{
    @Override
    public List<String> getRecommend(List<String> ids) {//categorytImp的方法getIDs返回为参数,得到推荐的五个商品ID
        List<String> rec = new ArrayList<>();
        if(ids.size()==0) return rec;
        Connection conn= JDBCUtils.getConnection();
        StringBuffer sql = new StringBuffer("select x.ID,sum(x.rate)/count(x.Users) r from (select * from comments where ID in(?");
        for(int i=1;i<ids.size();++i){
            sql.append(",?");
        }
        sql.append(")) x  group by x.ID order by r desc");
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql.toString());
            for(int i=1;i<=ids.size();++i){
                ps.setString(i,ids.get(i-1));
            }
            ResultSet rs=ps.executeQuery();
            int num=0;
            while(rs.next()&&num<5){
                String id=rs.getString("ID");
                String r=rs.getString("r");
                rec.add(id);
                num++;
            }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            JDBCUtils.close(conn,null,null);
        }
        return rec;
    }
}
