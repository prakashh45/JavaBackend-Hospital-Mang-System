class ApiConfig {
  static const String baseUrl = "http://3.26.10.0:8080";
}

class ApiEndpoints {
  // Auth
  static const String login = "/auth/login";
  static const String register = "/auth/register";
  static const String google = "/auth/google";
  static const String apple = "/auth/apple";
  static const String checkEmail = "/auth/check-email";
  static const String checkUsername = "/auth/check-username";
  static const String forgotPassword = "/auth/forgot-password";
  static const String resetPassword = "/auth/reset-password";
  static const String verifyToken = "/auth/verify";
  static const String logout = "/auth/logout";
  static const String me = "/auth/me";

  // Doctor Dashboard
  static const String doctorDashboard = "/doctor/dashboard";

  // Patients
  static const String patients = "/patients";
  static String patientById(int id) => "/patients/$id";
  static String patientHistory(int id) => "/patients/$id/history";
  static String patientVitalsCurrent(int id) => "/patients/$id/vitals/current";
  static String patientDiagnoses(int id) => "/patients/$id/diagnoses";
  static String patientPrescriptions(int id) => "/patients/$id/prescriptions";
  static const String patientsSearch = "/patients/search";

  // Diagnoses
  static const String diagnoses = "/diagnoses";
  static String diagnosesByPatient(int patientId) => "/diagnoses/patient/$patientId";
  static String diagnosisById(int id) => "/diagnoses/$id";
  static String diagnosisStatus(int id) => "/diagnoses/$id/status";

  // Prescriptions
  static const String prescriptions = "/prescriptions";
  static String prescriptionById(int id) => "/prescriptions/$id";
  static String prescriptionsByPatient(int patientId) => "/prescriptions/patient/$patientId";
  static String prescriptionStatus(int id) => "/prescriptions/$id/status";
  static String prescriptionPdf(int id) => "/prescriptions/$id/pdf";

  // Nurse
  static const String nurseDashboard = "/nurse/dashboard";
  static const String nurseWardPatients = "/nurse/ward-patients";
  static const String nurseNotes = "/nurse/notes";

  // Vitals
  static const String vitals = "/vitals";
  static String vitalsByPatient(int patientId) => "/vitals/patient/$patientId";
  static String vitalsById(int id) => "/vitals/$id";

  // Queue
  static const String queue = "/queue";
  static String queueById(int id) => "/queue/$id";

  // Tasks
  static const String tasks = "/tasks";
  static String taskById(int id) => "/tasks/$id";

  // Billing
  static const String billingPatientsSearch = "/billing/patients/search";
  static const String insuranceProviders = "/insurance/providers";
  static const String billingInvoices = "/billing/invoices";
  static String billingInvoiceById(int id) => "/billing/invoices/$id";
  static String billingInvoiceItems(int id) => "/billing/invoices/$id/items";
  static String billingInvoiceItemById(int id, int itemId) => "/billing/invoices/$id/items/$itemId";
  static String billingInvoiceDraft(int id) => "/billing/invoices/$id/draft";
  static String billingInvoiceSend(int id) => "/billing/invoices/$id/send";
  static String billingInvoicePdf(int id) => "/billing/invoices/$id/pdf";

  // Health
  static const String test = "/test";
}

