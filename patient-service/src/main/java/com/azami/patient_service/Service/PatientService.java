package com.azami.patient_service.Service;

import com.azami.patient_service.DTO.PatientRequestDTO;
import com.azami.patient_service.DTO.PatientResponseDTO;
import com.azami.patient_service.Exception.EmailAlreadyExistsException;
import com.azami.patient_service.Exception.PatientNotFoundException;
import com.azami.patient_service.Mapper.PatientMapper;
import com.azami.patient_service.Model.Patient;
import com.azami.patient_service.Repository.PatientRepository;
import com.azami.patient_service.grpc.BillingServiceGrpcClient;
import com.azami.patient_service.kafka.KafkaProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {

    private final PatientRepository patientRepository;

    private final BillingServiceGrpcClient billingServiceGrpcClient;

    private final KafkaProducer kafkaProducer;

    public List<PatientResponseDTO> getPatient() {
        List<Patient> patients = patientRepository.findAll();

        return patients
                .stream()
                .map(PatientMapper::toDTO)
                .toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email "+ patientRequestDTO.getEmail() +" already exists");
        }

        Patient newPatient = patientRepository
                .save(
                        PatientMapper
                                .toModel(patientRequestDTO)
                );

        billingServiceGrpcClient.createBillingAccount(
                newPatient.getId().toString(),
                newPatient.getName(),
                newPatient.getEmail()
        );

        kafkaProducer.sendEvent(newPatient);

        return PatientMapper.toDTO(newPatient);
    }

    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        Patient patient = patientRepository
                .findById(id)
                .orElseThrow(
                        () -> new PatientNotFoundException("Patient not found with ID " + id)
                );

        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(), id)) {
            throw new EmailAlreadyExistsException("A patient with this email "+ patientRequestDTO.getEmail() +" already exists");
        }

        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        return PatientMapper
                .toDTO(
                        patientRepository
                                .save(patient)
                );
    }


    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
