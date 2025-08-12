package com.gtalent.demo.responses;

import com.gtalent.demo.models.Supplier;

import java.util.List;

public class SupplierResponse {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String email;

    //新定義一個ProductResponse型別的集合(List)
    private List<ProductResponse> product;

    //新增List<ProductResponse>型別物件的 GETTER 及 SETTER
    public List<ProductResponse> getProduct() {
        return product;
    }

    public void setProduct(List<ProductResponse> product) {
        this.product = product;
    }

    public SupplierResponse() {
    }

    public SupplierResponse(int id, String name, String address, String phone, String email) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    //從 User 轉成 DTO，此寫法是將其包裝成一個user物件(即對外的 DTO)。
    public SupplierResponse(Supplier supplier) {
        this.id = supplier.getId();
        this.name = supplier.getName();
        this.address = supplier.getAddress();
        this.phone = supplier.getPhone();
        this.email = supplier.getEmail();
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
