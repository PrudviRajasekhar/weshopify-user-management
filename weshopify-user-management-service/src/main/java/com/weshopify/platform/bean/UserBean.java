package com.weshopify.platform.bean;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

/**
 *Configure Jackson to use the builder for deserialization using
 * {@code @JsonDeserialize(builder=Foobar.FoobarBuilder[Impl].class)}
 */
@Jacksonized
@Data
@Builder
public class UserBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 788701831130890437L;
	
	private String id;
	private String firstName;
	private String lastName;
	private String[] emails;
	private String password;
	private String userId;
	private String mobile;
	private boolean status;

}
