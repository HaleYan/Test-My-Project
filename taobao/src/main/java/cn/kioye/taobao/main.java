package cn.kioye.taobao;


import cn.kioye.taobao.DAO.category.categoryImp;
import cn.kioye.taobao.DAO.comment.commentImp;

import java.util.List;

public class main {
    public static void main(String args[]) {
        categoryImp categoryImp=new categoryImp();
      // String PID=categoryImp.getPId("1101");
      // System.out.println(PID);

       // String name=categoryImp.getPName("110204");
      // System.out.println(name);
        //goodsImp goodsImp=new goodsImp();
        //System.out.println(goodsImp.getDetail("17211797925"));
        List<String> list=categoryImp.getIds("536644160168");
        int i=0;
        System.out.println("同种类的id有");
        while (i<list.size())
        {
            System.out.println(list.get(i++));
        }
        commentImp commentImp=new commentImp();
        List<String> list2=commentImp.getRecommend(list);
        System.out.println("推荐的ID有");
        i=0;
        while (i<list2.size())
        {
            System.out.println(list2.get(i++));
        }

    }
}