# Postman Testing Guide

## Files
- `Clinic-APIs.postman_collection.json`
- `Clinic-APIs-Local.postman_environment.json`

## Import Steps
1. Open Postman.
2. Click `Import`.
3. Import both files from this folder.
4. Select environment `Clinic APIs - Local`.

## Update Variables
1. Set `baseUrl` (default is `http://localhost:8080`).
2. Fill tokens:
   - `doctorToken`
   - `nurseToken`
   - `patientToken`
3. Optional: set `patientId`, `diagnosisId`, `prescriptionId`, `vitalsId`, `queueId`.

## Recommended Order
1. Run `00 Auth -> Register`
2. Run `00 Auth -> Login`
3. Use your JWT/token values in environment variables.
4. Test by section:
   - `01 Doctor APIs`
   - `02 Nurse APIs`
   - `03 Flutter Patient App APIs`

## Notes
- Requests that create records automatically store IDs to environment (when response contains `id`).
- `/auth/me` and `/prescriptions/{id}/pdf` are included for future use and may return `404` if not implemented.
- If you get `401/403`, verify token and role mapping (`DOCTOR`, `NURSE`, `PATIENT`).
