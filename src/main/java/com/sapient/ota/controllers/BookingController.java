package com.sapient.ota.controllers;

import javax.validation.Valid;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapient.ota.constants.TicketConstants;
import com.sapient.ota.entities.Ticket;
import com.sapient.ota.entities.TicketDTO;
import com.sapient.ota.services.BookingService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/booking")
@Slf4j
public class BookingController {
	
	@Autowired
	BookingService bookingService;
	
	
	@PostMapping("/bookTicket")
	public ResponseEntity<String> addProductDetails(@Valid @RequestBody TicketDTO ticketDTO,BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			log.error(bindingResult.getFieldErrors().toString());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
					.body(bindingResult.getFieldErrors().get(0).getDefaultMessage());
		}
		Ticket ticket = bookingService.saveBookingDetails(ticketDTO);
		return ResponseEntity.status(HttpStatus.OK)
				.body(ticket.getUrn());
		
	}
	@GetMapping("/bookTicket/{urn}")
	public Ticket getProductDetails(@PathVariable String urn) {
		Ticket ticket = bookingService.getBookingDetails(urn);
		return ticket;
		
	}
}
