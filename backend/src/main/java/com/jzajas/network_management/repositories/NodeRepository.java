package com.jzajas.network_management.repositories;

import com.jzajas.network_management.entities.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NodeRepository extends JpaRepository<Long, Node> {
}
