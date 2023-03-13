package com.weshopify.platform.resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.weshopify.platform.bean.BrandsBean;
import com.weshopify.platform.service.BrandsService;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BrandsResource {

	private BrandsService brandsService;
	
	public BrandsResource(BrandsService brandsService) {
		this.brandsService = brandsService;
	}
	
	@Operation(summary = "createBrands", security = @SecurityRequirement(name = "bearerAuth"))
	@PostMapping("/brands")
	//@CircuitBreaker(name = "brands-circuit", fallbackMethod = "createBrands_fallback")
	//@Retry(name = "brands-categories-retry")
	//@RateLimiter(name = "brands-api-ratelimitor",fallbackMethod = "createBrands_fallback")
	@Bulkhead(name = "brands-api-bulkhead", fallbackMethod = "createBrands_fallback")
	public ResponseEntity<Object> createBrands(@RequestBody BrandsBean brandsBean){
		log.info("barnds bean data is:\t"+brandsBean.toString());
		brandsBean = brandsService.createBrand(brandsBean);
		if(brandsBean != null) {
			return ResponseEntity.ok(brandsBean);
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Brands Not Created");
		}
		
		
	}
	
	public ResponseEntity<Object> createBrands_fallback(Throwable th){
		Map<String, String> map = new HashMap<>();
		map.put("key", "Categories Service is down at this moment!! Please try after some time");
		return ResponseEntity.ok(map);
		
	}
	
	@Operation(summary = "updateBrandsBean", security = @SecurityRequirement(name = "bearerAuth"))
	@PutMapping("/brands")
	public ResponseEntity<BrandsBean> updateBrandsBean(@RequestBody BrandsBean brandsBean){
		return ResponseEntity.ok(brandsService.updateBrand(brandsBean));
	}
	
	@Operation(summary = "deleteBrand", security = @SecurityRequirement(name = "bearerAuth"))
	@DeleteMapping("/brands/{brandId}")
	public ResponseEntity<List<BrandsBean>> deleteBrand(@PathVariable("brandId") int brandId){
		return ResponseEntity.ok(brandsService.deleteBrand(brandId));
	}
	
	@Operation(summary = "cleandb", security = @SecurityRequirement(name = "bearerAuth"))
	@DeleteMapping("/brands/cleandb")
	public ResponseEntity<Map<String,String>> cleandb(){
		brandsService.cleanDb();
		Map<String, String> messageMap = new HashMap<>();
		messageMap.put("message", "All the records deleted from the database");
		return ResponseEntity.ok(messageMap);
	}
	
	@Operation(summary = "findAllBrands", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/brands")
	public ResponseEntity<List<BrandsBean>> findAllBrands(){
		return ResponseEntity.ok(brandsService.findAllBrands());
	}
	
	@Operation(summary = "getBrand", security = @SecurityRequirement(name = "bearerAuth"))
	@GetMapping("/brands/{brandId}")
	public ResponseEntity<BrandsBean> getBrand(@PathVariable("brandId") int brandId){
		return ResponseEntity.ok(brandsService.findBrandById(brandId));
	}
	
}
