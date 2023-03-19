package com.weshopify.platform.outbound;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ProductClient {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${product.svs.baseUrl}")
	private String productsServiceBaseUrl;

	public boolean reservProduct(int productId, int quantity) {

		String productUrl = productsServiceBaseUrl + "/" + productId + "/" + quantity;
		ResponseEntity<String> respEntity = restTemplate.getForEntity(productUrl, String.class);
		if (respEntity.getStatusCode().value() == HttpStatus.OK.value()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean resetProduct(int productId, int quantity) {

		String productUrl = productsServiceBaseUrl + "/reset/" + productId + "/" + quantity;
		ResponseEntity<String> respEntity = restTemplate.getForEntity(productUrl, String.class);
		if (respEntity.getStatusCode().value() == HttpStatus.OK.value()) {
			return true;
		} else {
			return false;
		}
	}
}
