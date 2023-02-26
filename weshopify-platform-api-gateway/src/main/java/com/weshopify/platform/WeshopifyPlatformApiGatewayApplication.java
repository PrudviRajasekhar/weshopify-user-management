package com.weshopify.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class WeshopifyPlatformApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeshopifyPlatformApiGatewayApplication.class, args);
	}
	/*
	 * @Bean public List<GroupedOpenApi> apis() { List<GroupedOpenApi> groups = new
	 * ArrayList<>(); List<RouteDefinition> definitions =
	 * locator.getRouteDefinitions().collectList().block(); assert definitions !=
	 * null; definitions.stream().filter(routeDefinition ->
	 * routeDefinition.getId().matches(".*-service")).forEach(routeDefinition -> {
	 * String name = routeDefinition.getId().replaceAll("-service", "");
	 * groups.add(GroupedOpenApi.builder().pathsToMatch("/" + name +
	 * "/**").group(name).build()); }); return groups; }
	 */
}
