package com.weshopify.platform.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.weshopify.platform.bean.UserBean;
import com.weshopify.platform.model.WSO2User;
import com.weshopify.platform.outbound.UserMgmtClient;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserMgmtServiceImpl implements UserMgmtService {

	@Autowired
	private UserMgmtClient userMgmtClient;
	
	@Override
	public List<UserBean> getAllUsers() {
	 List<WSO2User> wso2UsersList = userMgmtClient.findAllUsers();
	 
	 if(!CollectionUtils.isEmpty(wso2UsersList)) {
		 log.info("wso2 Users size {}",wso2UsersList.size());
		 
		 List<UserBean> usersList = new ArrayList<>();
		
		 wso2UsersList.stream().forEach(wso2User->{
			 
			 /**
			  * Filter the users list to not displaying the 
			  * wso2 admin users details in the response
			  */
			 Arrays.asList(wso2User.getEmails()).forEach(email->{
				 if(!email.contentEquals("admin@wso2.com")) {
					 usersList.add(mapWSO2UserToUserBean(wso2User));
				 }
			 });
			 			 		 
		 });
		 
		 return usersList;
	 }else {
		 throw new RuntimeException("No Users Found");
	 }
	}
	
	private UserBean mapWSO2UserToUserBean(WSO2User wso2User) {
		UserBean userBean = UserBean
							.builder()
							.id(wso2User.getId())
							.firstName(wso2User.getName().getGivenName())
							.lastName(wso2User.getName().getFamilyName())
							.emails(wso2User.getEmails())
							.userId(wso2User.getUserName())
							.build();
		return userBean;
							
		
	}
	

	
	

}
