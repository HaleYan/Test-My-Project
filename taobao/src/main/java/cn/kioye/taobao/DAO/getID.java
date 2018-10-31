package cn.kioye.taobao.DAO;

public class getID {
    public String getID(String url)//输入url得到id
    {
        StringBuffer ID=new StringBuffer();
       int i= url.indexOf("id=")+3;
       while (Character.isDigit(url.charAt(i)))
       {
           ID.append(url.charAt(i));
           i++;
       }
       return ID.toString();
    }
}
