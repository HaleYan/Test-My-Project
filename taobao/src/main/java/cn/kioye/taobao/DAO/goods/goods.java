package cn.kioye.taobao.DAO.goods;

public class goods {
    private String ID;
    private String name;
    private String category;
    private String pic;
    private String url;

    public goods(String ID, String name, String category, String pic, String url) {
        this.ID = ID;
        this.name = name;
        this.category = category;
        this.pic = pic;
        this.url = url;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "goods{" +
                "ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", pic='" + pic + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
