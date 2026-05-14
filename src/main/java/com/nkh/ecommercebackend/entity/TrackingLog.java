package com.nkh.ecommercebackend.entity;

import com.nkh.ecommercebackend.common.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.scheduling.support.SimpleTriggerContext;

import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tracking_logs")
public class TrackingLog extends BaseEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, unique = true)
    private String id;

    @JoinColumn(name = "order_id",nullable = false)
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Order order;

    @Column(name = "from_status",nullable = false)
    private OrderStatus fromStatus;

    @Column(name = "to_status",nullable = false)
    private OrderStatus toStatus;

    @Column(name = "note")
    private String note;

    @Column(name = "location")
    private String location;
}
