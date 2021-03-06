package com.client.order.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.client.order.model.Order;
import com.client.order.service.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	private Logger log = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private WebClient.Builder webClient;

	@Autowired
	private OrderService orderService;

	@GetMapping("/hello")
	public String sayHello() {
		return "Saying hello from order controller..";
	}

	@GetMapping("/createOrder")
	public ResponseEntity<Order> createOrder() {
		
		log.info("Order Creation Initialized");
		
		Order order = new Order();
		order.setOrderName("Burrito");
		order.setOrderNo("12");
		order.setOrderPlacedBy("Demo name");
		orderService.save(order);
		
		log.info("Initialising Payment Service");
		
		String didWeReceiveThePayment = webClient.build()
		.get()
		.uri("http://localhost:8092/payments/payment")
		.retrieve()
		.bodyToMono(String.class)
		.block();
		
		System.out.println(didWeReceiveThePayment);

		return new ResponseEntity<Order>(order, HttpStatus.OK);
	}

}
