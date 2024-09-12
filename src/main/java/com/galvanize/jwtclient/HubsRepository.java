package com.galvanize.jwtclient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HubsRepository extends JpaRepository<Hub, Long> {
    List<Hub> findByName(String name);
}
