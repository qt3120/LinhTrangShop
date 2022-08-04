package com.entity;

public class CartItem {
    private Product product;
    private int quantity;
    private Color colorId;
    private Size sizeId;

    public CartItem() {

    }

    public CartItem(Product product, int quantity, Color colorId, Size sizeId) {
        this.product = product;
        this.quantity = quantity;
        this.colorId = colorId;
        this.sizeId = sizeId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Color getColorId() {
        return colorId;
    }

    public void setColorId(Color colorId) {
        this.colorId = colorId;
    }

    public Size getSizeId() {
        return sizeId;
    }

    public void setSizeId(Size sizeId) {
        this.sizeId = sizeId;
    }

}
