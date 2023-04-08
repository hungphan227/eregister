package com.hungphan.eregister.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class PendingRestApiCallCouple extends BasicEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(generator = "PENDING_REST_API_CALL_GEN", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "PENDING_REST_API_CALL_GEN", sequenceName = "PENDING_REST_API_CALL_SEQ", allocationSize = 1)
    private Long id;
    private String requestId;
    private String className;
    private String firstMethodName;
    private String secondMethodName;

}
