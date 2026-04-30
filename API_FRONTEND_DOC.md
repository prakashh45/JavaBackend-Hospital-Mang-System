# Hospital Backend API Documentation (Frontend Ready)

## 1. Base URL
- Local: `http://localhost:8080`
- Current production: `https://javabackend-hospital-mang-system-production.up.railway.app`

## 2. Authentication
- Header for protected APIs: `Authorization: Bearer <JWT_TOKEN>`
- Public APIs (no token required):
1. `POST /auth/login`
2. `POST /auth/register`
3. `POST /auth/google`
4. `POST /auth/apple`
5. `GET /auth/check-email?email=...`
6. `GET /auth/check-username?username=...`
7. `POST /auth/forgot-password`
8. `POST /auth/reset-password`
9. `POST /auth/verify`

## 3. Date Formats
- `LocalDate`: `YYYY-MM-DD`
- `LocalDateTime`: `YYYY-MM-DDTHH:mm:ss`

## 4. Auth APIs
| Method | Path | Auth | Request Body |
|---|---|---|---|
| POST | `/auth/login` | Public | `{ "username": "doctor1", "password": "pass123" }` |
| POST | `/auth/register` | Public | `{ "username": "doctor1", "password": "pass123", "role": "DOCTOR" }` |
| POST | `/auth/google` | Public | `{ "email": "a@b.com", "username": "doctor1", "role": "DOCTOR" }` |
| POST | `/auth/apple` | Public | `{ "email": "a@b.com", "username": "doctor1", "role": "PATIENT" }` |
| GET | `/auth/check-email?email=a@b.com` | Public | None |
| GET | `/auth/check-username?username=doctor1` | Public | None |
| POST | `/auth/logout` | Token | None |
| POST | `/auth/verify` | Public | `{ "token": "<JWT_TOKEN>" }` |
| POST | `/auth/set-role` | Token | `{ "username": "doctor1", "role": "NURSE" }` |
| POST | `/auth/forgot-password` | Public | `{ "username": "doctor1", "email": "a@b.com" }` |
| POST | `/auth/reset-password` | Public | `{ "username": "doctor1", "email": "a@b.com", "newPassword": "newPass@123" }` |
| GET | `/auth/me` | Token | None |

## 5. Doctor App APIs
| Method | Path | Auth Role | Request Body / Query |
|---|---|---|---|
| GET | `/doctor/dashboard` | `DOCTOR` | None |
| GET | `/patients` | `DOCTOR` | Optional query: `search`, `status`, `page`, `limit` |
| GET | `/patients/{id}` | `DOCTOR` | None |
| GET | `/patients/search?query=...` | `DOCTOR` | Query `query` |
| GET | `/patients/{id}/history` | `DOCTOR` | None |
| POST | `/diagnoses` or `/diagnosis` | `DOCTOR` | DiagnosisRequest body |
| GET | `/diagnoses/patient/{patientId}` | `DOCTOR` | None |
| GET | `/diagnoses?patientId=&priority=&status=` | `DOCTOR` | Optional filters |
| PUT | `/diagnoses/{id}` | `DOCTOR` | DiagnosisRequest body |
| PATCH | `/diagnoses/{id}/status` | `DOCTOR` | `{ "status": "OPEN" }` |
| DELETE | `/diagnoses/{id}` | `DOCTOR` | None |
| POST | `/prescriptions` | `DOCTOR` | PrescriptionRequest body |
| GET | `/prescriptions/{id}` | `DOCTOR` | None |
| GET | `/prescriptions/patient/{patientId}` | `DOCTOR` | None |
| GET | `/prescriptions?patientId=&status=` | `DOCTOR` | Optional filters |
| PUT | `/prescriptions/{id}` | `DOCTOR` | PrescriptionRequest body |
| PATCH | `/prescriptions/{id}/status` | `DOCTOR` | `{ "status": "ACTIVE" }` |
| DELETE | `/prescriptions/{id}` | `DOCTOR` | None |
| GET | `/prescriptions/{id}/pdf` | `DOCTOR` | None |

## 6. Nurse App APIs
| Method | Path | Auth Role | Request Body / Query |
|---|---|---|---|
| GET | `/nurse/dashboard` | `NURSE` | None |
| GET | `/nurse/ward-patients?wardId=&priority=` | `NURSE` | Optional query |
| POST | `/nurse/notes` | `NURSE` | NurseNoteRequest body |
| POST | `/patients` | `NURSE` | PatientRequest body |
| PUT | `/patients/{id}` | `NURSE` | PatientRequest body |
| PATCH | `/patients/{id}` | `NURSE` | PatientPatchRequest body |
| POST | `/vitals` | `NURSE` | VitalsRequest body |
| GET | `/vitals/patient/{patientId}` | `NURSE` | None |
| PUT | `/vitals/{id}` | `NURSE` | VitalsRequest body |
| POST | `/queue` | `NURSE` | QueueRequest body |
| GET | `/queue` | `NURSE` | None |
| PUT | `/queue/{id}` | `NURSE` | QueueRequest body |
| DELETE | `/queue/{id}` | `NURSE` | None |
| GET | `/tasks?wardId=&shiftDate=` | `NURSE` | Optional query |
| POST | `/tasks` | `NURSE` | TaskRequest body |
| PATCH | `/tasks/{id}` | `NURSE` | TaskRequest body (partial allowed) |

## 7. Patient App APIs
| Method | Path | Auth Role | Request Body / Query |
|---|---|---|---|
| GET | `/auth/me` | `PATIENT` | None |
| GET | `/patients/{id}` | `PATIENT` | None |
| GET | `/patients/{id}/history` | `PATIENT` | None |
| GET | `/patients/{id}/vitals/current` | `PATIENT` | None |
| GET | `/patients/{id}/diagnoses` | `PATIENT` | None |
| GET | `/patients/{id}/prescriptions` | `PATIENT` | None |
| GET | `/diagnoses/patient/{patientId}` | `PATIENT` | None |
| GET | `/prescriptions/patient/{patientId}` | `PATIENT` | None |
| GET | `/prescriptions/{id}` | `PATIENT` | None |
| GET | `/prescriptions/{id}/pdf` | `PATIENT` | None |
| GET | `/vitals/patient/{patientId}` | `PATIENT` | None |

## 8. Billing APIs (Doctor + Nurse)
| Method | Path | Auth Role | Request Body / Query |
|---|---|---|---|
| GET | `/billing/patients/search?q=` | `DOCTOR` or `NURSE` | Optional `q` |
| GET | `/insurance/providers` | `DOCTOR` or `NURSE` | None |
| POST | `/billing/invoices` | `DOCTOR` or `NURSE` | InvoiceRequest body |
| GET | `/billing/invoices/{id}` | `DOCTOR` or `NURSE` | None |
| PUT | `/billing/invoices/{id}` | `DOCTOR` or `NURSE` | InvoiceRequest body |
| POST | `/billing/invoices/{id}/items` | `DOCTOR` or `NURSE` | InvoiceItemRequest body |
| PUT | `/billing/invoices/{id}/items/{itemId}` | `DOCTOR` or `NURSE` | InvoiceItemRequest body |
| DELETE | `/billing/invoices/{id}/items/{itemId}` | `DOCTOR` or `NURSE` | None |
| POST | `/billing/invoices/{id}/draft` | `DOCTOR` or `NURSE` | None |
| POST | `/billing/invoices/{id}/send` | `DOCTOR` or `NURSE` | None |
| GET | `/billing/invoices/{id}/pdf` | `DOCTOR` or `NURSE` | None |

## 9. Request Body Schemas
### PatientRequest
```json
{
  "firstName": "Ravi",
  "lastName": "Kumar",
  "gender": "MALE",
  "dateOfBirth": "1998-04-21",
  "phone": "+919999999999",
  "email": "ravi@mail.com",
  "address": "Bangalore",
  "status": "ACTIVE",
  "wardId": "WARD-1",
  "priority": "NORMAL"
}
```

### PatientPatchRequest
```json
{
  "phone": "+918888888888",
  "address": "New address",
  "status": "ACTIVE"
}
```

### DiagnosisRequest
```json
{
  "patientId": 1,
  "diagnosisDate": "2026-04-30",
  "conditionName": "Viral Fever",
  "notes": "Take rest",
  "doctorName": "Dr. Sharma",
  "priority": "HIGH",
  "status": "OPEN"
}
```

### PrescriptionRequest
```json
{
  "patientId": 1,
  "prescribedDate": "2026-04-30",
  "instructions": "After food",
  "doctorName": "Dr. Sharma",
  "status": "ACTIVE",
  "medicines": [
    {
      "medicineName": "Paracetamol",
      "dosage": "500mg",
      "frequency": "2 times/day",
      "durationDays": 5
    }
  ]
}
```

### VitalsRequest
```json
{
  "patientId": 1,
  "recordedAt": "2026-04-30T10:30:00",
  "temperature": 38.6,
  "bloodPressure": "120/80",
  "pulse": 75,
  "respiratoryRate": 18,
  "oxygenSaturation": 98,
  "nurseName": "Nurse Asha"
}
```

### QueueRequest
```json
{
  "patientId": 1,
  "tokenNumber": 24,
  "department": "General Medicine",
  "status": "WAITING",
  "queuedAt": "2026-04-30T11:00:00",
  "remarks": "Walk-in patient"
}
```

### NurseNoteRequest
```json
{
  "patientId": 1,
  "nurseName": "Nurse Asha",
  "wardId": "WARD-1",
  "note": "Patient stable"
}
```

### TaskRequest
```json
{
  "title": "Give evening medicine",
  "description": "Bed 12",
  "wardId": "WARD-1",
  "shiftDate": "2026-04-30",
  "dueAt": "2026-04-30T19:30:00",
  "assignedTo": "Nurse Asha",
  "status": "PENDING",
  "priority": "HIGH"
}
```

### InvoiceRequest
```json
{
  "patientId": 1,
  "invoiceNumber": "INV-1001",
  "currency": "INR",
  "tax": 18.0,
  "notes": "Cashless insurance"
}
```

### InvoiceItemRequest
```json
{
  "itemName": "Consultation",
  "quantity": 1,
  "unitPrice": 500.0
}
```

### StatusPatchRequest
```json
{
  "status": "ACTIVE"
}
```

## 10. Important Notes For Frontend
1. Use `/diagnoses` as standard path in frontend. `/diagnosis` is also supported for backward compatibility.
2. There is no endpoint `/prescriptions/{id}/history`.
3. PDF APIs return file bytes:
   - `/prescriptions/{id}/pdf`
   - `/billing/invoices/{id}/pdf`
4. Most create/update APIs currently return `200 OK` with JSON body.
5. All non-public APIs require bearer token.
