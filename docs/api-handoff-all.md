# Complete API Handoff (Doctor + Nurse + Flutter Patient)

This document is the combined frontend handoff package.

## A. Quick Summary by App
- Doctor App: Diagnosis, Prescription, Patient search/read/history
- Nurse App: Patient registration, Vitals, Queue
- Flutter Patient App: Auth, own profile, history, diagnosis list, prescription list/view

## B. Shared Setup
- Base URL: `http://localhost:8080`
- Header: `Authorization: Bearer <JWT_TOKEN>`
- Content-Type: `application/json`
- Error response format:
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

---

## C. Doctor APIs (Line by Line)
1. `POST /diagnosis`
2. `GET /diagnosis/patient/{patientId}`
3. `PUT /diagnosis/{id}`
4. `DELETE /diagnosis/{id}`
5. `POST /prescriptions`
6. `GET /prescriptions/{id}`
7. `GET /prescriptions/patient/{patientId}`
8. `PUT /prescriptions/{id}`
9. `DELETE /prescriptions/{id}`
10. `GET /prescriptions/{id}/pdf` (`NOT IMPLEMENTED YET`)
11. `GET /patients`
12. `GET /patients/{id}`
13. `GET /patients/search?query=...`
14. `GET /patients/{id}/history`

---

## D. Nurse APIs (Line by Line)
1. `POST /patients`
2. `POST /vitals`
3. `GET /vitals/patient/{patientId}`
4. `PUT /vitals/{id}`
5. `POST /queue`
6. `GET /queue`
7. `PUT /queue/{id}`
8. `DELETE /queue/{id}`

---

## E. Flutter Patient APIs (Line by Line)
1. `POST /auth/register`
2. `POST /auth/login`
3. `GET /auth/me` (`NOT IMPLEMENTED YET`)
4. `GET /patients/{id}`
5. `GET /patients/{id}/history`
6. `GET /diagnosis/patient/{patientId}`
7. `GET /prescriptions/patient/{patientId}`
8. `GET /prescriptions/{id}`
9. `GET /prescriptions/{id}/pdf` (`NOT IMPLEMENTED YET`)

---

## F. Request Body Templates

### F.1 Login/Register
```json
{
  "username": "patient1",
  "password": "pass123"
}
```

### F.2 Patient Create/Update
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

### F.3 Diagnosis Create/Update
```json
{
  "patientId": 1,
  "diagnosisDate": "2026-04-19",
  "conditionName": "Viral Fever",
  "notes": "Hydration and rest advised",
  "doctorName": "Dr. Sharma"
}
```

### F.4 Prescription Create/Update
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

### F.5 Vitals Create/Update
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

### F.6 Queue Create/Update
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

## G. Queue Status Values
- `WAITING`
- `IN_PROGRESS`
- `COMPLETED`
- `CANCELLED`
