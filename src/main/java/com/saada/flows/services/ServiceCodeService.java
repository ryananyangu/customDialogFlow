// package com.custom.dialog.lab.services;

// import javax.validation.Valid;

// import com.custom.dialog.lab.models.Organization;
// import com.custom.dialog.lab.models.ServiceCode;
// import com.custom.dialog.lab.repositories.ServiceCodeRepository;
// import com.custom.dialog.lab.utils.Props;

// import org.json.JSONObject;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.web.bind.annotation.RequestBody;

// @Service
// public class ServiceCodeService {

//     @Autowired
//     private Props props;

//     @Autowired
//     private ServiceCodeRepository serviceCodeRepository;

//     @Autowired
//     private OrganizationService organizationService;

//     public JSONObject getAvailableServiceCodes() {

//         return props.getStatusResponse("200_SCRN", serviceCodeRepository.findByActive(false).collectList().block());

//     }

//     public JSONObject getOrganizationServiceCodes() {
//         return props.getStatusResponse("200_SCRN",
//                 serviceCodeRepository.findByOrganization(organizationService.getLoggedInUserOrganization().getName()));

//     }

//     // public JSONObject setAppliedServiceCode(String serviceCode) {
//     //     ServiceCode serviceCodeObject = serviceCodeRepository.findById(serviceCode).block();
//     //     // serviceCodeObject.isActive() 
//     //     // update organization to shortcode
//     // }

//     // public String getAllShortCodes() {

//     // }

//     // public String createShortCode(@RequestBody @Valid ServiceCode serviceCode) {

//     // }

// }