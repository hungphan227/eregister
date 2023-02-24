package com.hungphan.eregister.restclient;

import com.hungphan.eregister.dto.HoldingCreditRequestDto;
import com.hungphan.eregister.dto.HoldingCreditResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "eregister-user-service", url = "eregister-user-service:10000")
public interface UserService {

    @RequestMapping(method = RequestMethod.POST, value = "/user/balance/hold-credit")
    HoldingCreditResponseDto holdCredit(@RequestBody HoldingCreditRequestDto holdingCreditRequestDto);

    @RequestMapping(method = RequestMethod.PUT, value = "/user/balance/use-credit/{requestId}")
    void useCredit(@PathVariable String requestId);

    @RequestMapping(method = RequestMethod.PUT, value = "/user/balance/release-credit/{requestId}")
    void releaseCredit(@PathVariable String requestId);

}
