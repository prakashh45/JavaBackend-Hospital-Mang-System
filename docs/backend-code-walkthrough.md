# Backend Code Walkthrough (File-by-File)

This explains what each file does and how request flow works in your Spring Boot project.

## 1) Request Flow (How API works end-to-end)
1. Client sends HTTP request.
2. `SecurityConfig` checks if route is public or protected.
3. For protected routes, `JwtAuthenticationFilter` reads bearer token.
4. `JwtService` validates token and extracts username/role.
5. `AppUserDetailsService` loads user from DB and sets Spring Security auth context.
6. Controller method receives request.
7. Controller calls Service.
8. Service runs business logic and calls Repository.
9. Repository talks to MySQL using JPA/Hibernate.
10. Service maps Entities to DTO response.
11. GlobalExceptionHandler formats errors if anything fails.

---

## 2) Root / Boot

- `src/main/java/com/example/demo/DemoApplication.java`  
  Starts Spring Boot application.

---

## 3) Config + Security

- `src/main/java/com/example/demo/config/SecurityConfig.java`  
  Central Spring Security setup: stateless JWT, public `/auth/**` routes, secure all others, role method security (`@PreAuthorize`), auth provider, password encoder, CORS.

- `src/main/java/com/example/demo/security/JwtAuthenticationFilter.java`  
  Intercepts request, parses `Authorization: Bearer ...`, validates token, sets authentication.

- `src/main/java/com/example/demo/security/JwtService.java`  
  Generates and validates JWT token, extracts `sub` (username), `role`, and expiration.

- `src/main/java/com/example/demo/security/AppUserDetailsService.java`  
  Loads user from `users` table and converts to Spring `UserDetails` with `ROLE_*`.

---

## 4) Controllers (API layer)

- `src/main/java/com/example/demo/controller/AuthController.java`  
  Auth APIs: login/register/social, username/email checks, verify token, set role, forgot/reset password, me profile.

- `src/main/java/com/example/demo/controller/PatientController.java`  
  Patient CRUD, search/filter, history, current vitals, patient diagnoses/prescriptions.

- `src/main/java/com/example/demo/controller/DiagnosisController.java`  
  Diagnosis create/read/update/delete and status patch.

- `src/main/java/com/example/demo/controller/PrescriptionController.java`  
  Prescription create/read/update/delete, status patch, PDF download.

- `src/main/java/com/example/demo/controller/VitalsController.java`  
  Vitals create/read/update.

- `src/main/java/com/example/demo/controller/QueueController.java`  
  Queue create/read/update/delete.

- `src/main/java/com/example/demo/controller/DoctorController.java`  
  Doctor dashboard endpoint.

- `src/main/java/com/example/demo/controller/NurseController.java`  
  Nurse dashboard, ward patients, nurse notes.

- `src/main/java/com/example/demo/controller/TaskController.java`  
  Task list/create/patch for nurse workflows.

- `src/main/java/com/example/demo/controller/BillingController.java`  
  Billing patient search, invoice lifecycle, invoice items, invoice PDF.

- `src/main/java/com/example/demo/controller/InsuranceController.java`  
  Insurance providers listing.

- `src/main/java/com/example/demo/controller/TestController.java`  
  Basic `/test` endpoint for quick alive check.

---

## 5) Services (Business logic)

- `src/main/java/com/example/demo/service/AuthService.java`  
  Auth logic, JWT creation, role normalization, social auth, password operations.

- `src/main/java/com/example/demo/service/PatientService.java`  
  Patient business logic, mapping, history aggregation, ward filters.

- `src/main/java/com/example/demo/service/DiagnosisService.java`  
  Diagnosis rules and filters.

- `src/main/java/com/example/demo/service/PrescriptionService.java`  
  Prescription logic and medicine mapping.

- `src/main/java/com/example/demo/service/VitalsService.java`  
  Vitals logic and mapping.

- `src/main/java/com/example/demo/service/QueueService.java`  
  Queue logic and mapping.

- `src/main/java/com/example/demo/service/DoctorDashboardService.java`  
  Computes doctor dashboard stats and activity blocks.

- `src/main/java/com/example/demo/service/NurseService.java`  
  Computes nurse dashboard stats, ward patient filtering, note creation.

- `src/main/java/com/example/demo/service/TaskService.java`  
  Task create/list/patch logic.

- `src/main/java/com/example/demo/service/BillingService.java`  
  Invoice creation/update, item management, total recalculation, status transitions, PDF bytes.

---

## 6) Repositories (DB query layer)

- `src/main/java/com/example/demo/repository/UserRepository.java`  
  User lookups and uniqueness checks.

- `src/main/java/com/example/demo/repository/PatientRepository.java`  
  Patient search, pageable filter, ward/priority filter.

- `src/main/java/com/example/demo/repository/DiagnosisRepository.java`  
  Diagnosis filters by patient/priority/status and sorted lists.

- `src/main/java/com/example/demo/repository/PrescriptionRepository.java`  
  Prescription filters by patient/status and sorted lists.

- `src/main/java/com/example/demo/repository/VitalsRepository.java`  
  Patient vitals list and latest vitals query.

- `src/main/java/com/example/demo/repository/QueueRepository.java`  
  Queue list and queue counts by enum status.

- `src/main/java/com/example/demo/repository/MedicineRepository.java`  
  Medicine table CRUD.

- `src/main/java/com/example/demo/repository/NurseNoteRepository.java`  
  Nurse notes query by patient.

- `src/main/java/com/example/demo/repository/TaskItemRepository.java`  
  Task filtering by ward/shift date.

- `src/main/java/com/example/demo/repository/InvoiceRepository.java`  
  Invoice lookup and unique invoice number lookup.

- `src/main/java/com/example/demo/repository/InvoiceItemRepository.java`  
  Invoice item CRUD.

- `src/main/java/com/example/demo/repository/InsuranceProviderRepository.java`  
  Insurance provider CRUD.

---

## 7) Models / Entities (DB tables)

- `src/main/java/com/example/demo/model/User.java`  
  `users` table for authentication users, role, provider, email.

- `src/main/java/com/example/demo/model/Patient.java`  
  `patients` table with links to diagnoses/prescriptions/vitals/queue.

- `src/main/java/com/example/demo/model/Diagnosis.java`  
  `diagnoses` table, many-to-one with patient.

- `src/main/java/com/example/demo/model/Prescription.java`  
  `prescriptions` table, many-to-one with patient, one-to-many medicines.

- `src/main/java/com/example/demo/model/Medicine.java`  
  `medicines` table, many-to-one with prescription.

- `src/main/java/com/example/demo/model/Vitals.java`  
  `vitals` table, many-to-one with patient.

- `src/main/java/com/example/demo/model/Queue.java`  
  `patient_queue` table, many-to-one with patient, status enum.

- `src/main/java/com/example/demo/model/QueueStatus.java`  
  Queue enum: `WAITING`, `IN_PROGRESS`, `COMPLETED`, `CANCELLED`.

- `src/main/java/com/example/demo/model/NurseNote.java`  
  `nurse_notes` table, note per patient.

- `src/main/java/com/example/demo/model/TaskItem.java`  
  `tasks` table for nurse task management.

- `src/main/java/com/example/demo/model/InsuranceProvider.java`  
  `insurance_providers` table.

- `src/main/java/com/example/demo/model/Invoice.java`  
  `invoices` table with totals/status/timestamps and item relationship.

- `src/main/java/com/example/demo/model/InvoiceItem.java`  
  `invoice_items` table, recalculates amount = qty * unit price.

---

## 8) DTOs (Input/Output contracts)

### Base Auth DTO
- `src/main/java/com/example/demo/dto/LoginRequest.java`: login/register input.
- `src/main/java/com/example/demo/dto/AuthResponse.java`: token output.

### Auth module DTOs
- `src/main/java/com/example/demo/dto/auth/SocialAuthRequest.java`
- `src/main/java/com/example/demo/dto/auth/ForgotPasswordRequest.java`
- `src/main/java/com/example/demo/dto/auth/ResetPasswordRequest.java`
- `src/main/java/com/example/demo/dto/auth/SetRoleRequest.java`
- `src/main/java/com/example/demo/dto/auth/VerifyTokenRequest.java`

### Patient DTOs
- `src/main/java/com/example/demo/dto/patient/PatientRequestDto.java`
- `src/main/java/com/example/demo/dto/patient/PatientPatchRequestDto.java`
- `src/main/java/com/example/demo/dto/patient/PatientResponseDto.java`
- `src/main/java/com/example/demo/dto/patient/PatientHistoryResponseDto.java`

### Diagnosis DTOs
- `src/main/java/com/example/demo/dto/diagnosis/DiagnosisRequestDto.java`
- `src/main/java/com/example/demo/dto/diagnosis/DiagnosisResponseDto.java`

### Prescription DTOs
- `src/main/java/com/example/demo/dto/prescription/MedicineRequestDto.java`
- `src/main/java/com/example/demo/dto/prescription/MedicineResponseDto.java`
- `src/main/java/com/example/demo/dto/prescription/PrescriptionRequestDto.java`
- `src/main/java/com/example/demo/dto/prescription/PrescriptionResponseDto.java`

### Vitals DTOs
- `src/main/java/com/example/demo/dto/vitals/VitalsRequestDto.java`
- `src/main/java/com/example/demo/dto/vitals/VitalsResponseDto.java`

### Queue DTOs
- `src/main/java/com/example/demo/dto/queue/QueueRequestDto.java`
- `src/main/java/com/example/demo/dto/queue/QueueResponseDto.java`

### Common DTO
- `src/main/java/com/example/demo/dto/common/StatusUpdateRequest.java`

### Nurse DTOs
- `src/main/java/com/example/demo/dto/nurse/NurseNoteRequest.java`
- `src/main/java/com/example/demo/dto/nurse/TaskRequest.java`

### Billing DTOs
- `src/main/java/com/example/demo/dto/billing/InvoiceRequest.java`
- `src/main/java/com/example/demo/dto/billing/InvoiceItemRequest.java`

---

## 9) Exception handling

- `src/main/java/com/example/demo/exception/ApiErrorResponse.java`  
  Standard error response object.

- `src/main/java/com/example/demo/exception/BadRequestException.java`  
  Custom 400 exception.

- `src/main/java/com/example/demo/exception/ResourceNotFoundException.java`  
  Custom 404 exception.

- `src/main/java/com/example/demo/exception/GlobalExceptionHandler.java`  
  Converts all common exceptions to consistent JSON error payload.

---

## 10) Utility

- `src/main/java/com/example/demo/util/AppUtil.java`  
  Small string helpers (`isBlank`, `safe`).

---

## 11) Example path trace

### A) `POST /auth/login`
1. `SecurityConfig`: permits `/auth/login`.
2. `AuthController.login`.
3. `AuthService.login`.
4. `AuthenticationManager` + `UserRepository`.
5. `JwtService.generateToken`.
6. returns `AuthResponse`.

### B) `GET /patients/{id}/history`
1. `SecurityConfig`: authenticated required.
2. `JwtAuthenticationFilter`: validates bearer token.
3. `PatientController.getPatientHistory`.
4. `PatientService.getPatientHistory`.
5. Uses `DiagnosisRepository`, `PrescriptionRepository`, `VitalsRepository`, `QueueRepository`.
6. returns `PatientHistoryResponseDto`.

