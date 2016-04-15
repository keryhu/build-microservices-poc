package com.build.coordination;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@EnableDiscoveryClient
@RibbonClients(value = { @RibbonClient(name = "customer-service"), @RibbonClient(name = "order-service") })
@EnableCircuitBreaker
public class CoordinationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoordinationApplication.class, args);
	}
	
	// copied from spencer gibb @ https://github.com/spencergibb/myfeed/blob/master/myfeed-core/src/main/java/myfeed/core
	// should not be needed after https://github.com/spring-cloud/spring-cloud-commons/issues/36
	// BEGIN spender gibb ripoff
	private void addConverters(RestTemplate restTemplate) {
		List<HttpMessageConverter<?>> converters = getHttpMessageConverters();
		restTemplate.setMessageConverters(converters);
	}

	private List<HttpMessageConverter<?>> getHttpMessageConverters() {
		List<HttpMessageConverter<?>> converters = new ArrayList<>();
		converters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		converter.setObjectMapper(mapper);

		converters.add(converter);
		return converters;
	}
	
	@Bean
	public RibbonAsyncClientHttpRequestFactory asyncRequestFactory(LoadBalancerClient loadBalancerClient) {
		RibbonAsyncClientHttpRequestFactory requestFactory = new RibbonAsyncClientHttpRequestFactory(loadBalancerClient);
		requestFactory.setTaskExecutor(new SimpleAsyncTaskExecutor());
		return requestFactory;
	}
	
	@Bean
	public AsyncRestTemplate asyncRestTemplate(LoadBalancerInterceptor interceptor, LoadBalancerClient loadBalancer) {
		RibbonAsyncClientHttpRequestFactory requestFactory = asyncRequestFactory(loadBalancer);
		Rest rest = new Rest(requestFactory);
		addConverters(rest);
		AsyncRest asyncRest = new AsyncRest(requestFactory, rest);
		asyncRest.setMessageConverters(getHttpMessageConverters());
		return asyncRest;
	}
	
	// END spender gibb ripoff
	
}
