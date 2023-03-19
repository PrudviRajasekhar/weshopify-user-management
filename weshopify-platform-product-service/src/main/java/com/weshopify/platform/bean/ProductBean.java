package com.weshopify.platform.bean;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;


@Data
public class ProductBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private String type;
	private BigDecimal price;
	private int quantityReserved;
	private String status;
}
