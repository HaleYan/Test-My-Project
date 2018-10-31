package cn.kioye.taobao.DAO.category;

import java.util.List;

public interface categoryDAO {
    public String getPId(String cid);
    public String getPName(String pid);
    public String getCID(String id);
    public List<String> getIds(String pid);
}
