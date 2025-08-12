package com.gtalent.demo.models;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name="products")  //指定映射到名稱為 "users" 的資料表
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //自動遞增主鍵
    private int id;  //這是主Key
    @Column(name = "name")  //代表對應的資料表欄位
    private String name;
    @Column(name = "price")
    private BigDecimal price;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "status")
    private boolean status;
//    @Column(name = "supplier_id")
//    private int supplierId;

    //因Supplier_id在資料庫中有外鍵功能
    @ManyToOne  //Many Products to One Supplier
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;  //連帶讀取資料庫中supplier表格內容

    public Product() {
    }

//    public Product(int id, String name, BigDecimal price, int quantity, boolean status, int supplierId) {
//        this.id = id;
//        this.name = name;
//        this.price = price;
//        this.quantity = quantity;
//        this.status = status;
//        this.supplierId = supplierId;
//    }


    public Product(int id, String name, BigDecimal price, int quantity, boolean status, Supplier supplier) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.status = status;
        this.supplier = supplier;
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

//    public int getSupplierId() {
//        return supplierId;
//    }
//
//    public void setSupplierId(int supplierId) {
//        this.supplierId = supplierId;
//    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", status=" + status +
                '}';
    }
}
