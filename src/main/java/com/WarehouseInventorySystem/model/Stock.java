package com.WarehouseInventorySystem.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.io.Serializable;

import com.WarehouseInventorySystem.model.StockKey;

@Entity
@Table(name = "stock")
@IdClass(StockKey.class)
public class Stock implements Serializable {

	@Id
	@Column(name = "location", nullable = false)
	private String location;

    @Id
    @Column(name = "code")
    private String code;

    @Column(name = "quantity", nullable = false)
    private long quantity;


    public Stock() {

	}

	public Stock(String location, String code, long quantity) {
		this.location = location;
		this.code = code;
		this.quantity = quantity;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	/*
	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}
	*/

	public long getQuantity() {
		return quantity;
	}

	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}

	public void tranferAddQuantity(long value) {
		quantity += value ;
	}

	public void tranferMinusQuantity(long value) {
		quantity -= value ;
	}

	@Override
	public String toString() {
		return "Stock [location=" + location + ", code=" + code + ", quantity=" + quantity + "]";
	}
}