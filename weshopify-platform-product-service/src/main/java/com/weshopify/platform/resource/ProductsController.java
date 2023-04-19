package com.weshopify.platform.resource;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.weshopify.platform.bean.ProductBean;
import com.weshopify.platform.service.ProductService;

@RestController
public class ProductsController {

	@Autowired
	private ProductService productService;
	
	@PostMapping("/products")
	public ResponseEntity<ProductBean> createProduct(@RequestBody ProductBean prodBean){
		prodBean = productService.createUpdateProduct(prodBean);
		return ResponseEntity.ok(prodBean);
	}
	
	@GetMapping("/products")
	public ResponseEntity<List<ProductBean>> listAllProduct(){
		List<ProductBean> prodBeanList = productService.listAllProducts();
		return ResponseEntity.ok(prodBeanList);
	}
	
	@DeleteMapping("/products")
	public ResponseEntity<List<ProductBean>> deleteProducts(@RequestBody List<Integer> payload){
		List<ProductBean> prodBeanList = productService.deleteProducts(payload);
		return ResponseEntity.ok(prodBeanList);
	}
	
	@GetMapping("/products/{productId}/{quantity}")
	public ResponseEntity<ProductBean> reservProduct(@PathVariable("productId") int productId,
													 @PathVariable("quantity") int quantity){
		
		ProductBean productBean = productService.reservProduct(productId, quantity);
		return ResponseEntity.ok(productBean);
	}
	
	@GetMapping("/products/reset/{productId}/{quantity}")
	public ResponseEntity<ProductBean> resetProducts(@PathVariable("productId") int productId,
													 @PathVariable("quantity") int quantity){
		
		ProductBean productBean = productService.resetProduct(productId, quantity);
		return ResponseEntity.ok(productBean);
	}
	
	
	
}
