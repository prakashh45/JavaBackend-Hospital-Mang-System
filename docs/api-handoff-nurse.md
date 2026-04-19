# Nurse App API Handoff

## 1. Base Details
- Base URL: `http://localhost:8080`
- Auth: `Authorization: Bearer <JWT_TOKEN>`
- Content-Type: `application/json`
- Main role: `NURSE`

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

## 3. Patient Registration

### 3.1 Register New Patient
- Method: `POST`
- URL: `/patients`
- Role: `DOCTOR` or `NURSE`
- Request:
```json
{
  "firstName": "Anita",
  "lastName": "Verma",
  "gender": "Female",
  "dateOfBirth": "1995-10-11",
  "phone": "+919876543210",
  "email": "anita@example.com",
  "address": "Pune"
}
```
- Response: `PatientResponseDto`

## 4. Vitals APIs

### 4.1 Add Vitals
- Method: `POST`
- URL: `/vitals`
- Role: `NURSE`
- Request:
```json
{
  "patientId": 1,
  "recordedAt": "2026-04-19T09:45:00",
  "temperature": 98.6,
  "bloodPressure": "120/80",
  "pulse": 76,
  "respiratoryRate": 18,
  "oxygenSaturation": 99,
  "nurseName": "Nurse Priya"
}
```
- Response: `VitalsResponseDto`

### 4.2 Get Vitals by Patient
- Method: `GET`
- URL: `/vitals/patient/{patientId}`
- Role: `DOCTOR`, `NURSE`, `PATIENT`
- Response: `VitalsResponseDto[]`

### 4.3 Update Vitals
- Method: `PUT`
- URL: `/vitals/{id}`
- Role: `NURSE`
- Request: same as add vitals
- Response: `VitalsResponseDto`

## 5. Queue APIs

### 5.1 Add to Queue
- Method: `POST`
- URL: `/queue`
- Role: `NURSE`
- Request:
```json
{
  "patientId": 1,
  "tokenNumber": 42,
  "department": "General Medicine",
  "status": "WAITING",
  "queuedAt": "2026-04-19T10:00:00",
  "remarks": "Walk-in"
}
```
- Response: `QueueResponseDto`

### 5.2 View Queue
- Method: `GET`
- URL: `/queue`
- Role: `DOCTOR` or `NURSE`
- Response: `QueueResponseDto[]`

### 5.3 Update Queue Entry
- Method: `PUT`
- URL: `/queue/{id}`
- Role: `NURSE`
- Request: same as add to queue
- Response: `QueueResponseDto`

### 5.4 Delete Queue Entry
- Method: `DELETE`
- URL: `/queue/{id}`
- Role: `NURSE`
- Response: `200 OK` (empty body)

## 6. Queue Status Enum
- `WAITING`
- `IN_PROGRESS`
- `COMPLETED`
- `CANCELLED`
