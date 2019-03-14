package com.maya.mounty.mountyinternship;

public class Item
{
    private String name;
    private String description;
    private String price;
    private String imagePath;

    public Item()
    {

    }

    public Item(String name, String desc, String price, String imagePath)
    {
        this.name = name;
        this.description = desc;
        this.price = price;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
