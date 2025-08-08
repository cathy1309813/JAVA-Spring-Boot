package com.gtalent.demo.responses;

import com.gtalent.demo.models.Product;
import com.gtalent.demo.models.User;
import jakarta.persistence.Column;

import java.math.BigDecimal;

public class GetProductResponse {
    private int id;
    private String name;
    private BigDecimal price;
    private int quantity;
    private boolean status;

    public GetProductResponse() {
    }

    public GetProductResponse(int id, String name, BigDecimal price, int quantity, boolean status) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
    }

    // 新增這個：從 User 轉成 DTO，此寫法是將其包裝成一個user物件(即對外的 DTO)。
    public GetProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.quantity = product.getQuantity();
        this.status = product.isStatus();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
