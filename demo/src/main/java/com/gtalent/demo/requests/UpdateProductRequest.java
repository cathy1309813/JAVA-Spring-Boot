package com.gtalent.demo.requests;

import java.math.BigDecimal;

public class UpdateProductRequest {
    private String name;
    private BigDecimal price;
    private int quantity;
    private boolean status;

    public UpdateProductRequest() {
    }

    public UpdateProductRequest(String name, BigDecimal price, int quantity, boolean status) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
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
