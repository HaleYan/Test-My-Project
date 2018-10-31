package cn.kioye.taobao.DAO;


import cn.kioye.taobao.DAO.category.categoryImp;
import cn.kioye.taobao.DAO.comment.commentImp;

import java.util.List;
import java.util.Scanner;

/*
getCID->getPID->getIds->getRecommend
 */
public class recommend {
    public static void main(String[] args){
        commentImp com = new commentImp();
        categoryImp cat =new categoryImp();
        Scanner sc= new Scanner(System.in);
        String id = sc.next();
        String cid = cat.getCID(id);
        String pid = cat.getPId(cid);
        List<String> ID = cat.getIds(pid);
        List<String> ids = com.getRecommend(ID);//最终推荐，前五
    }
}
