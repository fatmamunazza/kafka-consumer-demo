package com.sapient.ota.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.sapient.ota.entities.Ticket;

@Repository
public interface TicketSearchRepository extends ElasticsearchRepository<Ticket, String>{
    List<Ticket> findByUrn(String tourName);
}
