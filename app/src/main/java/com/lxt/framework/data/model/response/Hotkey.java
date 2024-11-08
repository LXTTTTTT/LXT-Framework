package com.lxt.framework.data.model.response;

public class Hotkey {
    private int id;
    private String link;
    private String name;
    private int order;
    private int visible;

    // 无参构造函数
    public Hotkey() {}

    // 带参构造函数
    public Hotkey(int id, String link, String name, int order, int visible) {
        this.id = id;
        this.link = link;
        this.name = name;
        this.order = order;
        this.visible = visible;
    }

    // Getter 和 Setter 方法
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return "Interview{" +
                "id=" + id +
                ", link='" + link + '\'' +
                ", name='" + name + '\'' +
                ", order=" + order +
                ", visible=" + visible +
                '}';
    }
}
