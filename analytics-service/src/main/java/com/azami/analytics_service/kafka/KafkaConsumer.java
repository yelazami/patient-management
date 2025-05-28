package com.azami.analytics_service.kafka;


import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
@Slf4j
public class KafkaConsumer {

    private static final String TOPIC_NAME_PATIENT = "patient";


    @KafkaListener(topics = TOPIC_NAME_PATIENT, groupId = "analytics-service")
    public void consumeEvent(byte[] event) {
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);
            // ... perform any bisness logic related to analytics here ....

            log.info("Received Patient Event: [" +
                    "PatientId={}, PatientName={} PatientEmail={} ]",
                    patientEvent.getPatientId(),
                    patientEvent.getName(),
                    patientEvent.getEmail()
            );

        } catch (InvalidProtocolBufferException e) {
            log.error("Error deserializing event {}", e.getMessage());
        }
    }
}
