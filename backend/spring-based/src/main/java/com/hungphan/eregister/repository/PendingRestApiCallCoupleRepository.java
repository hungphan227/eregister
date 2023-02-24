package com.hungphan.eregister.repository;

import com.hungphan.eregister.model.PendingRestApiCallCouple;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PendingRestApiCallCoupleRepository extends JpaRepository<PendingRestApiCallCouple, Long> {

    PendingRestApiCallCouple findByRequestId(String requestId);

}
