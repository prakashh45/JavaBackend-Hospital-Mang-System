# Flutter Patient App API Handoff

## 1. Base Details
- Base URL: `http://localhost:8080`
- Auth for protected APIs: `Authorization: Bearer <JWT_TOKEN>`
- Content-Type: `application/json`
- Main role: `PATIENT`

## 2. Auth APIs

### 2.1 Register
- Method: `POST`
- URL: `/auth/register`
- Request:
```json
{
  "username": "patient1",
  "password": "pass123"
}
```
- Response:
```json
"User Registered Successfully ✅"
```

### 2.2 Login
- Method: `POST`
- URL: `/auth/login`
- Request:
```json
{
  "username": "patient1",
  "password": "pass123"
}
```
- Response:
```json
"Login successful ✅"
```

### 2.3 Profile
- Method: `GET`
- URL: `/auth/me`
- Current status: `NOT IMPLEMENTED YET`

## 3. Personal Data APIs

### 3.1 View Own Profile
- Method: `GET`
- URL: `/patients/{id}`
- Role: `DOCTOR`, `NURSE`, `PATIENT`
- Flutter note: pass logged-in patient id
- Response: `PatientResponseDto`

### 3.2 Full Medical History
- Method: `GET`
- URL: `/patients/{id}/history`
- Role: `DOCTOR`, `NURSE`, `PATIENT`
- Response: `PatientHistoryResponseDto`

### 3.3 Diagnosis List
- Method: `GET`
- URL: `/diagnosis/patient/{patientId}`
- Role: `DOCTOR` or `PATIENT`
- Response: `DiagnosisResponseDto[]`

## 4. Prescription APIs

### 4.1 All Prescriptions by Patient
- Method: `GET`
- URL: `/prescriptions/patient/{patientId}`
- Role: `DOCTOR` or `PATIENT`
- Response: `PrescriptionResponseDto[]`

### 4.2 View Single Prescription
- Method: `GET`
- URL: `/prescriptions/{id}`
- Role: `DOCTOR` or `PATIENT`
- Response: `PrescriptionResponseDto`

### 4.3 Download Prescription PDF
- Method: `GET`
- URL: `/prescriptions/{id}/pdf`
- Current status: `NOT IMPLEMENTED YET`

## 5. DTO Field Reference

### 5.1 PatientResponseDto
- `id` (Long)
- `firstName` (String)
- `lastName` (String)
- `gender` (String)
- `dateOfBirth` (Date: `yyyy-MM-dd`)
- `phone` (String)
- `email` (String)
- `address` (String)
- `createdAt` (DateTime: `yyyy-MM-dd'T'HH:mm:ss`)
- `updatedAt` (DateTime)

### 5.2 DiagnosisResponseDto
- `id` (Long)
- `patientId` (Long)
- `patientName` (String)
- `diagnosisDate` (Date)
- `conditionName` (String)
- `notes` (String)
- `doctorName` (String)

### 5.3 PrescriptionResponseDto
- `id` (Long)
- `patientId` (Long)
- `patientName` (String)
- `prescribedDate` (Date)
- `instructions` (String)
- `doctorName` (String)
- `createdAt` (DateTime)
- `updatedAt` (DateTime)
- `medicines` (List<MedicineResponseDto>)

### 5.4 MedicineResponseDto
- `id` (Long)
- `medicineName` (String)
- `dosage` (String)
- `frequency` (String)
- `durationDays` (Integer)
