package edu.hm.cs.projektstudium.findlunch.webapp.config;


import java.util.Collections;
import java.util.Properties;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/*
AppConfig File
BEANS

The @Configuration annotation indicates that the class declares one or more @Bean methods. 
These methods are invoked at runtime by Spring to manage lifecycle of the beans. 
In our case we have defined @Bean for view resolver for JSP view.
 */


/**
 * This class is responsible for defining necessary beans that are handled by Spring application context. These beans can then be injected to classes which need their functionality.
 */
@Configuration
@EnableSwagger2
public class Beans extends WebMvcConfigurerAdapter{
	
	/**
	 * Password encoder bean, needed for encrypting user passwords.
	 *
	 * @return the b crypt password encoder
	 */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(10);
	}
 
	/**
	 * Validator bean that is used during Hibernate annotation validation. 
	 * This validator bean makes it possible to resolve the error messages defined within the message source files.
	 *
	 * @return the local validator factory bean
	 */
	@Bean
	public LocalValidatorFactoryBean validator() {
	    LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
	    validatorFactoryBean.setValidationMessageSource(messageSource());
	    return validatorFactoryBean;
	}
	
	/**
	 * Message source which defines the basenames of the files in which messages/content for the homepage are stored.
	 *
	 * @return the resource bundle message source
	 */
	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames("messages/content", "messages/errors", "messages/info", "messages/success", "messages/bill","messages/email","messages/termsAndConditions","messages/privacy","messages/faq");
		return messageSource;	
	}
	
	/**
	 * Container customizer. Configures the embedded tomcat (e.g. post size)
	 *
	 * @return the embedded servlet container customizer
	 * @throws Exception the exception
	 */
	@Bean 
	public EmbeddedServletContainerCustomizer containerCustomizer(
	        ) throws Exception {
	 
	      
	      return new EmbeddedServletContainerCustomizer() {
			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
 
			      if (container instanceof TomcatEmbeddedServletContainerFactory) {
 
			          TomcatEmbeddedServletContainerFactory tomcat = (TomcatEmbeddedServletContainerFactory) container;
			          tomcat.addConnectorCustomizers(
			                  new TomcatConnectorCustomizer() {
								@Override
								public void customize(Connector connector) {
									  connector.setMaxPostSize(20000000);//20MB
								  }
							}
			          );
			      }
			  }
		};
	  }
	
	@Bean
	public Docket api() {                
	    return new Docket(DocumentationType.SWAGGER_2)          
	      .select()
	      .apis(RequestHandlerSelectors.basePackage("edu.hm.cs.projektstudium.findlunch.webapp.controller.rest"))
	      .paths(PathSelectors.any())
	      .build()
	      .apiInfo(apiInfo());
	}
	 
	private ApiInfo apiInfo() {
	     return new ApiInfo(
	       "FINDLUNCH REST API", 
	       "The findluch-Webapp Documentation of the Rest API.", 
	       "API TOS", 
	       "Terms of service", 
	       new Contact("Adrian Hufnagl", "www.example.com", "myeaddress@company.com"), 
	       "License of API", "API license URL", Collections.emptyList());
	}
	
	/**
	 * Returns the local validator
	 */
	@Override
	public Validator getValidator()
	{
	    return validator();
	}
	
}
