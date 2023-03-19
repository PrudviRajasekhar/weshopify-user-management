package com.weshopify.platform.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weshopify.platform.bean.ProductBean;
import com.weshopify.platform.model.Product;
import com.weshopify.platform.model.Quantity;
import com.weshopify.platform.repo.ProductRepo;
import com.weshopify.platform.repo.QuantityRepo;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ProductRepo productRepo;
	
	@Autowired
	private QuantityRepo quntRepo;
	
	@Override
	public ProductBean createUpdateProduct(ProductBean productBean) {
		Product product = new Product();
		String imeiNumber = UUID.randomUUID().toString();
		product.setProductIMEI(imeiNumber);
		BeanUtils.copyProperties(productBean, product);
		product.setStatus("CREATED");
		productRepo.save(product);
		
		//creating the quantity
		Quantity quantity = new Quantity();
		quantity.setProductId(product.getId());
		quantity.setId(product.getProductIMEI());
		quantity.setQuantity(productBean.getQuantityReserved());
		
		quntRepo.save(quantity);
		
		BeanUtils.copyProperties(product, productBean);
		return productBean;
	}

	@Override
	public List<ProductBean> listAllProducts() {
		List<Product> productList = productRepo.findAll();
		List<ProductBean> productBeanList = new ArrayList<>();
		Optional.of(productList).stream().forEach(product->{
			ProductBean prodBean = new ProductBean();
			BeanUtils.copyProperties(product, prodBean);
			productBeanList.add(prodBean);
		});
		return productBeanList;
	}

	@Override
	public List<ProductBean> deleteProducts(List<Integer> idList) {
		//productRepo.deleteAllById(idList);
		
		idList.forEach(productId->{
			deleteAProduct(productId);
		});
		
		return listAllProducts();
	}
	
	@Override
	public void deleteAProduct(int productId){
		Product p= productRepo.findById(productId).get();
		String imei = p.getProductIMEI();
		Quantity q = quntRepo.findById(imei).get();
		quntRepo.delete(q);
		productRepo.delete(p);
		//return listAllProducts();
	}

	@Override
	public ProductBean reservProduct(int productId, int desiredQuantity) {
		Product p= productRepo.findById(productId).get();
		String imei = p.getProductIMEI();
		
		Quantity q = quntRepo.findById(imei).get();
		int quantity = q.getQuantity();
		
		if(quantity > desiredQuantity) {
			quantity = quantity-desiredQuantity;
		}else {
			throw new RuntimeException("Desired Quantity of the Products were nt available in the store");
		}
		q.setQuantity(quantity);
		quntRepo.save(q);
		
		ProductBean productBean = new ProductBean();
		BeanUtils.copyProperties(p, productBean);
		productBean.setQuantityReserved(desiredQuantity);
		productBean.setStatus("RESERVED");
		return productBean;
	}
	
	@Override
	public ProductBean resetProduct(int productId, int desiredQuantity) {
		Product p= productRepo.findById(productId).get();
		String imei = p.getProductIMEI();
		
		Quantity q = quntRepo.findById(imei).get();
		int quantity = q.getQuantity();
		
		/*
		 * if(quantity > desiredQuantity) { quantity = quantity-desiredQuantity; }else {
		 * throw new
		 * RuntimeException("Desired Quantity of the Products were nt available in the store"
		 * ); }
		 */
		quantity = quantity+desiredQuantity;
		q.setQuantity(quantity);
		quntRepo.save(q);
		
		ProductBean productBean = new ProductBean();
		BeanUtils.copyProperties(p, productBean);
		productBean.setQuantityReserved(desiredQuantity);
		productBean.setStatus("RESETTED");
		return productBean;
	}

}
