package com.azami.patient_service.kafka;

import com.azami.patient_service.Model.Patient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    private static final String EVENT_TYPE_CREATED = "PATIENT_CREATED";

    private static final String TOPIC_NAME_PATIENT = "patient";

    public void sendEvent(Patient patient) {
        PatientEvent event = PatientEvent
                .newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType(EVENT_TYPE_CREATED)
                .build();

        try {
            kafkaTemplate.send(TOPIC_NAME_PATIENT, event.toByteArray());
        } catch (Exception e) {
            log.error("Error sending PatientCreated event: {}", event);
        }
    }
}
