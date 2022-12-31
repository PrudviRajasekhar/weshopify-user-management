package com.weshopify.platform.model;

import java.io.Serializable;
import java.util.List;

import com.weshopify.platform.bean.RoleBean;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WSO2User implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 3877212295621231125L;
	
	private String[] schemas;
	private String userName;
	private String password;
	private String[] emails;
	private String id;
	
	private WSO2UserPersonals name;
	private List<RoleBean> roles;

}
