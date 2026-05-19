export const API_BASE_URL = "http://3.26.10.0:8080";

export const API = {
  auth: {
    login: "/auth/login",
    register: "/auth/register",
    google: "/auth/google",
    apple: "/auth/apple",
    checkEmail: "/auth/check-email",
    checkUsername: "/auth/check-username",
    forgotPassword: "/auth/forgot-password",
    resetPassword: "/auth/reset-password",
    verify: "/auth/verify",
    logout: "/auth/logout",
    me: "/auth/me",
  },

  doctor: {
    dashboard: "/doctor/dashboard",
  },

  patients: {
    list: "/patients",
    search: "/patients/search",
    byId: (id: number | string) => `/patients/${id}`,
    history: (id: number | string) => `/patients/${id}/history`,
    currentVitals: (id: number | string) => `/patients/${id}/vitals/current`,
    diagnoses: (id: number | string) => `/patients/${id}/diagnoses`,
    prescriptions: (id: number | string) => `/patients/${id}/prescriptions`,
  },

  diagnoses: {
    list: "/diagnoses",
    byPatient: (patientId: number | string) => `/diagnoses/patient/${patientId}`,
    byId: (id: number | string) => `/diagnoses/${id}`,
    status: (id: number | string) => `/diagnoses/${id}/status`,
  },

  prescriptions: {
    list: "/prescriptions",
    byId: (id: number | string) => `/prescriptions/${id}`,
    byPatient: (patientId: number | string) => `/prescriptions/patient/${patientId}`,
    status: (id: number | string) => `/prescriptions/${id}/status`,
    pdf: (id: number | string) => `/prescriptions/${id}/pdf`,
  },

  nurse: {
    dashboard: "/nurse/dashboard",
    wardPatients: "/nurse/ward-patients",
    notes: "/nurse/notes",
  },

  vitals: {
    list: "/vitals",
    byId: (id: number | string) => `/vitals/${id}`,
    byPatient: (patientId: number | string) => `/vitals/patient/${patientId}`,
  },

  queue: {
    list: "/queue",
    byId: (id: number | string) => `/queue/${id}`,
  },

  tasks: {
    list: "/tasks",
    byId: (id: number | string) => `/tasks/${id}`,
  },

  billing: {
    patientSearch: "/billing/patients/search",
    insuranceProviders: "/insurance/providers",
    invoices: "/billing/invoices",
    invoiceById: (id: number | string) => `/billing/invoices/${id}`,
    invoiceItems: (id: number | string) => `/billing/invoices/${id}/items`,
    invoiceItemById: (id: number | string, itemId: number | string) =>
      `/billing/invoices/${id}/items/${itemId}`,
    invoiceDraft: (id: number | string) => `/billing/invoices/${id}/draft`,
    invoiceSend: (id: number | string) => `/billing/invoices/${id}/send`,
    invoicePdf: (id: number | string) => `/billing/invoices/${id}/pdf`,
  },

  health: {
    test: "/test",
  },
};

