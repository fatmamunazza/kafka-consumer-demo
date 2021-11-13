package com.sapient.ota.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sapient.ota.constants.Status;
import com.sapient.ota.constants.TicketConstants;
import com.sapient.ota.entities.Ticket;
import com.sapient.ota.entities.TicketDTO;

import com.sapient.ota.repositories.TicketSearchRepository;

@Service
public class BookingServiceImpl implements BookingService {

	
	
	@Autowired
	TicketSearchRepository ticketSearchRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

	@KafkaListener(groupId = TicketConstants.GROUP_ID_JSON, topics = TicketConstants.TOPIC_NAME, containerFactory = TicketConstants.KAFKA_LISTENER_CONTAINER_FACTORY)
	public void receivedMessage(Ticket ticket) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(ticket);
		logger.info("Json message received using Kafka listener " + jsonString);
		ticketSearchRepository.save(ticket);
	}

	@Override
	public Ticket getBookingDetails(String urn) {
		List<Ticket> ticket
		  = ticketSearchRepository.findByUrn(urn);
		return ticket.get(0);
	}
	

	@Override
	public Ticket saveBookingDetails(TicketDTO ticketDTO) {
		
		LocalDate localDate= LocalDate.now();
		DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
		Ticket ticket = new Ticket();
		ticket.setTourName(ticketDTO.getTourName());
		ticket.setDescription(ticketDTO.getDescription());
		ticket.setPrice(ticketDTO.getPrice());
		ticket.setTourDate(ticketDTO.getTourDate());
		ticket.setStatus(Status.CONFIRMED);
		ticket.setBookingDate(dateformatter.format(localDate));
		ticket.setUrn(UUID.randomUUID().toString());
		try {
			return ticketSearchRepository.save(ticket);
		} catch (Exception e) {
			System.out.println(e.toString());
			return ticket;
		}
		
	}
}
