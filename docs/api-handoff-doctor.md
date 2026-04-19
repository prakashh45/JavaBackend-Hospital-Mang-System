# Doctor App API Handoff

## 1. Base Details
- Base URL: `http://localhost:8080`
- Auth: `Authorization: Bearer <JWT_TOKEN>`
- Content-Type: `application/json`
- Role required: `DOCTOR`

## 2. Common Error Response
```json
{
  "timestamp": "2026-04-19T11:22:33.123",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "details": [
    "fieldName: reason"
  ]
}
```

## 3. Diagnosis APIs

### 3.1 Add Diagnosis
- Method: `POST`
- URL: `/diagnosis`
- Role: `DOCTOR`
- Request:
```json
{
  "patientId": 1,
  "diagnosisDate": "2026-04-19",
  "conditionName": "Viral Fever",
  "notes": "Hydration and rest advised",
  "doctorName": "Dr. Sharma"
}
```
- Response:
```json
{
  "id": 10,
  "patientId": 1,
  "patientName": "Ravi Kumar",
  "diagnosisDate": "2026-04-19",
  "conditionName": "Viral Fever",
  "notes": "Hydration and rest advised",
  "doctorName": "Dr. Sharma"
}
```

### 3.2 View Patient Diagnoses
- Method: `GET`
- URL: `/diagnosis/patient/{patientId}`
- Role: `DOCTOR` or `PATIENT`
- Response: `DiagnosisResponseDto[]`

### 3.3 Update Diagnosis
- Method: `PUT`
- URL: `/diagnosis/{id}`
- Role: `DOCTOR`
- Request: same as create diagnosis
- Response: `DiagnosisResponseDto`

### 3.4 Delete Diagnosis
- Method: `DELETE`
- URL: `/diagnosis/{id}`
- Role: `DOCTOR`
- Response: `200 OK` (empty body)

## 4. Prescription APIs

### 4.1 Create Prescription
- Method: `POST`
- URL: `/prescriptions`
- Role: `DOCTOR`
- Request:
```json
{
  "patientId": 1,
  "prescribedDate": "2026-04-19",
  "instructions": "Take after food",
  "doctorName": "Dr. Sharma",
  "medicines": [
    {
      "medicineName": "Paracetamol 650",
      "dosage": "1 tablet",
      "frequency": "Twice daily",
      "durationDays": 5
    }
  ]
}
```
- Response: `PrescriptionResponseDto`

### 4.2 View Prescription
- Method: `GET`
- URL: `/prescriptions/{id}`
- Role: `DOCTOR` or `PATIENT`
- Response: `PrescriptionResponseDto`

### 4.3 View All Prescriptions by Patient
- Method: `GET`
- URL: `/prescriptions/patient/{patientId}`
- Role: `DOCTOR` or `PATIENT`
- Response: `PrescriptionResponseDto[]`

### 4.4 Update Prescription
- Method: `PUT`
- URL: `/prescriptions/{id}`
- Role: `DOCTOR`
- Request: same as create prescription
- Response: `PrescriptionResponseDto`

### 4.5 Delete Prescription
- Method: `DELETE`
- URL: `/prescriptions/{id}`
- Role: `DOCTOR`
- Response: `200 OK` (empty body)

### 4.6 Download Prescription PDF
- Method: `GET`
- URL: `/prescriptions/{id}/pdf`
- Role: `DOCTOR` or `PATIENT`
- Current status: `NOT IMPLEMENTED YET`

## 5. Patient Read/Search APIs

### 5.1 List Patients
- Method: `GET`
- URL: `/patients`
- Role: `DOCTOR`, `NURSE`, `PATIENT`
- Response: `PatientResponseDto[]`

### 5.2 View Patient by ID
- Method: `GET`
- URL: `/patients/{id}`
- Role: `DOCTOR`, `NURSE`, `PATIENT`
- Response: `PatientResponseDto`

### 5.3 Search Patients
- Method: `GET`
- URL: `/patients/search?query={text}`
- Role: `DOCTOR`, `NURSE`, `PATIENT`
- Example: `/patients/search?query=ravi`
- Response: `PatientResponseDto[]`

### 5.4 Full Medical History
- Method: `GET`
- URL: `/patients/{id}/history`
- Role: `DOCTOR`, `NURSE`, `PATIENT`
- Response:
```json
{
  "patient": { },
  "diagnoses": [ ],
  "prescriptions": [ ],
  "vitals": [ ],
  "queueHistory": [ ]
}
```
