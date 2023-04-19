package com.weshopify.platform.service;

import java.util.List;

import com.weshopify.platform.bean.ProductBean;

public interface ProductService {

	ProductBean createUpdateProduct(ProductBean productBean);
	List<ProductBean> listAllProducts();
	List<ProductBean> deleteProducts(List<Integer> idList);
	void deleteAProduct(int productId);
	ProductBean reservProduct(int productId, int desiredQuantity);
	ProductBean resetProduct(int productId, int desiredQuantity);
}
