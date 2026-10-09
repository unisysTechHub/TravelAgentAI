package com.unisystechhub.ai.guardrails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GuardRailService {
    private final PolicyEngineService policyEngineService;

    public PolicyDecission validateUserRequest(String request){
        log.info("===========================");
        log.info("GuardRail policy decision");
       PolicyDecission policyDecission = policyEngineService.evaluateUserRequest(request);
       if( !policyDecission.allow()){
           log.info(" user request not allowed with reason {}",policyDecission.reason());
       }
        log.info("===========================");
return policyDecission;

    }

}
