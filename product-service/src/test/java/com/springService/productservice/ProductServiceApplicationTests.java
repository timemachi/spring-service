package com.springService.productservice;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.springService.productservice.dto.ProductRequest;
import com.springService.productservice.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductServiceApplicationTests {
	@Container
	static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:4.0.10"));

	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	ProductRepository productRepository;

	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
		dynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
	}

	@Test
	@Order(1)
	void createProduct() throws Exception {
		ProductRequest productRequest = getProductRequest();
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(productRequest)))
				.andExpect(status().isCreated());
		Assertions.assertEquals(1, productRepository.findAll().size());
	}

	@Test
	@Order(2)
	void cantCreateProductWithSameName() throws Exception {
		ProductRequest productRequest = getProductRequest();
		mockMvc.perform(MockMvcRequestBuilders.post("/api/product")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(productRequest)))
				.andExpect(status().isConflict());
	}

	@Test
	@Order(3)
	void findProductWithName() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/api/product/iphone 13"))
				.andExpect(status().isOk());
		mockMvc.perform(MockMvcRequestBuilders.get("/api/product/randomName"))
				.andExpect(status().isNotFound());
	}

	@Test
	@Order(4)
	void deleteProductWithName() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/product/randomName"))
				.andExpect(status().isNotFound());
		mockMvc.perform(MockMvcRequestBuilders.delete("/api/product/iphone 13"))
				.andExpect(status().isOk());
		Assertions.assertEquals(0, productRepository.findAll().size());
	}


	private ProductRequest getProductRequest() {
		return ProductRequest.builder()
				.name("iPhone 13")
				.description("Apple SmartPhone")
				.price(BigDecimal.valueOf(1200))
				.build();
	}

}
