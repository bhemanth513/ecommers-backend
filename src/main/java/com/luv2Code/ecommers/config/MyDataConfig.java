package com.luv2Code.ecommers.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import com.luv2Code.ecommers.entity.Product;
import com.luv2Code.ecommers.entity.ProductCategory;

@Configuration
public class MyDataConfig implements RepositoryRestConfigurer{
	
	private EntityManager entityManager;
	
	@Autowired
	public MyDataConfig(EntityManager theEntityManager) {
		entityManager = theEntityManager;
	}
	
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
		
		HttpMethod[] theUnsupportedActions = {HttpMethod.PUT,HttpMethod.POST, HttpMethod.DELETE};
			
		//disable http methods for Product class( POST,PUT and DELETE)
		config.getExposureConfiguration()
			.forDomainType(Product.class)
			.withCollectionExposure((metdata,httpMethods)->httpMethods.disable(theUnsupportedActions))
			.withCollectionExposure((metdata,httpMethods)->httpMethods.disable(theUnsupportedActions));
		

		//disable http methods for ProductCategory class( POST,PUT and DELETE)
		config.getExposureConfiguration()
		.forDomainType(ProductCategory.class)
		.withCollectionExposure((metdata,httpMethods)->httpMethods.disable(theUnsupportedActions))
		.withCollectionExposure((metdata,httpMethods)->httpMethods.disable(theUnsupportedActions));
	
		//call an internal helper method
		exposeIds(config);
	}

	private void exposeIds(RepositoryRestConfiguration config) {
		// expose entity ids
		Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
		List<Class> entityClasses = new ArrayList<>();
		for(EntityType t: entities) {
			entityClasses.add(t.getJavaType());
		}
		Class[] domainTypes = entityClasses.toArray(new Class[0]);
		config.exposeIdsFor(domainTypes);
	}
}
