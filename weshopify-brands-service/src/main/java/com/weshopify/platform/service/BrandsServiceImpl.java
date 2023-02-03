package com.weshopify.platform.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weshopify.platform.bean.BrandsBean;
import com.weshopify.platform.bean.CategoryBean;
import com.weshopify.platform.model.Brands;
import com.weshopify.platform.outbound.CategoriesApiClient;
import com.weshopify.platform.repository.BrandsRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BrandsServiceImpl implements BrandsService {

	@Autowired
	private BrandsRepo brandsRepo;
	
	@Autowired
	private CategoriesApiClient catApiClient;
	
	@Autowired
	private ObjectMapper mapper;
	
	@Override
	public BrandsBean createBrand(BrandsBean brandsBean) {
		log.info("Brands Creation Service Method");
		return convertEntityToBean(brandsRepo.save(convertBeanToEntity(brandsBean)));
	}

	@Override
	public BrandsBean updateBrand(BrandsBean brandsBean) {
		return convertEntityToBean(brandsRepo.save(convertBeanToEntity(brandsBean)));
	}

	@Override
	public List<BrandsBean> findAllBrands() {
		List<Brands> brandsList = brandsRepo.findAll();
		if(!CollectionUtils.isEmpty(brandsList)) {
			List<BrandsBean> bransBeanList = new ArrayList<>();
			brandsList.stream().forEach(brand->{
				bransBeanList.add(convertEntityToBean(brand));
			});
			
			return bransBeanList;
		}else {
			log.error("No Brands Available in DB");
			throw new RuntimeException("No Brands Available in DB");
		}
	}

	@Override
	public BrandsBean findBrandById(int brandId) {
		return convertEntityToBean(brandsRepo.findById(brandId).get());
	}

	@Override
	public List<BrandsBean> deleteBrand(int brandId) {
		brandsRepo.deleteById(brandId);
		return findAllBrands();
	}
	
	/**
	 * converting the bean to entity model
	 * 
	 * @param catBean
	 * @return
	 */
	private Brands convertBeanToEntity(BrandsBean brandsBean) {
		Brands brandsEntity = new Brands();
		brandsEntity.setName(brandsBean.getName());
		brandsEntity.setLogoPath(brandsBean.getLogoPath());
		
		/**
		 * invoke the categories service and fetch the categories
		 * from categories service
		 */
		if(!CollectionUtils.isEmpty(brandsBean.getCategories())) {
			List<CategoryBean> orignalCats = new ArrayList<CategoryBean>();
				brandsBean.getCategories().parallelStream().forEach(catbean->{
			
					String catResp = catApiClient.findCategoryById(null, catbean.getId());
					try {
						orignalCats.add(mapper.readValue(catResp, CategoryBean.class));
					} catch (JsonMappingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		
				
			});
			brandsEntity.setCategories(orignalCats);
		}
		
		if(StringUtils.hasText(brandsBean.getId())) {
			brandsEntity.setId(brandsBean.getId());
		}

		return brandsEntity;
	}

	/**
	 * converting the entity model to bean
	 * 
	 * @param catBean
	 * @return
	 */
	private BrandsBean convertEntityToBean(Brands brandsEntity) {
		BrandsBean brandsBean = new BrandsBean();
		brandsBean.setName(brandsEntity.getName());
		brandsBean.setLogoPath(brandsEntity.getLogoPath());
		brandsBean.setCategories(brandsEntity.getCategories());
		brandsBean.setId(brandsEntity.getId());
	    return brandsBean;
	}

}
