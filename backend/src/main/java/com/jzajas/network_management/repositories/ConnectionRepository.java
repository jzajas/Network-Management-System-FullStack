package com.jzajas.network_management.repositories;

import com.jzajas.network_management.entities.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {
}
