package com.emsi.eventplatform.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service", path = "/api/notifications")
public interface NotificationClient {

    @PostMapping("/email")
    void email(@RequestBody EmailRequest req);

    @PostMapping("/sms")
    void sms(@RequestBody SmsRequest req);

    class EmailRequest {
        public String to;
        public String subject;
        public String message;
        public EmailRequest() {}
        public EmailRequest(String to, String subject, String message) {
            this.to = to; this.subject = subject; this.message = message;
        }
    }

    class SmsRequest {
        public String to;
        public String message;
        public SmsRequest() {}
        public SmsRequest(String to, String message) {
            this.to = to; this.message = message;
        }
    }
}
