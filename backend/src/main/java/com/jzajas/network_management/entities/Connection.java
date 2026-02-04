package com.jzajas.network_management.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "connections")
@Getter
@NoArgsConstructor
public class Connection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "device_a_id")
    private Device deviceA;

    @ManyToOne(optional = false)
    @JoinColumn(name = "device_b_id")
    private Device deviceB;

    public Connection(final Device deviceB, final Device deviceA) {
        this.deviceB = deviceB;
        this.deviceA = deviceA;
    }
}
