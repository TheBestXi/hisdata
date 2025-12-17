# Full Frontend Implementation Plan (Mock-First Approach)

This plan aims to build a fully functional frontend that simulates the entire HIS workflow. It uses a Mock Layer to intercept API requests, ensuring the UI code remains "backend-ready" without requiring changes when the real backend is connected.

## 1. Infrastructure & Mocking Layer

**Goal**: Create a seamless environment where the frontend "thinks" it's talking to a real server.

* **Mock Strategy**: Use `mockjs` (or custom interceptors) to intercept Axios requests.

* **Directory**: `src/mocks/` containing handlers for `auth`, `patient`, `doctor`, `appointment`, `inventory`.

* **Backend Compatibility**:

  * `src/api/` files will define strict types and real endpoints (e.g., `/api/patient/search`).

  * The mock layer will intercept these specific URL patterns.

  * A simple `.env` flag `VITE_USE_MOCK=true` will toggle this behavior.

## 2. Core Layout & Navigation

**Goal**: Professional medical-grade interface.

* **Layout Component** (`src/layouts/MainLayout.vue`):

  * **Sidebar**: Collapsible, high-contrast navigation menu.

  * **Header**: Breadcrumbs, User Profile, Global Search.

  * **Main Area**: Route transition effects.

* **Navigation Structure**:

  * 📊 **Dashboard** (Stats & Overview)

  * 🏥 **Outpatient**

    * Registration (挂号)

    * Appointment History (挂号记录)

  * 👨‍⚕️ **Doctor Station**

    * My Patient Queue (候诊列表)

    * Diagnosis & Prescription (诊疗)

  * 💊 **Pharmacy**

    * Dispensing (发药)

    * Inventory (库存)

  * 💰 **Finance**

    * Bill Settlement (收费)

## 3. Module Implementation Details

### 3.1 Outpatient Registration (挂号)

* **Features**:

  * Patient Search (Autocomplete by name/ID).

  * Department/Doctor Selection (Cascading dropdowns).

  * Fee Calculation (Auto-display based on doctor level).

* **Mock Data**: Pre-filled list of patients and doctors.

### 3.2 Doctor Workstation (医生工作站)

* **Features**:

  * **Patient Queue**: List of "Waiting" patients.

  * **Clinical Note**: Form for Chief Complaint, Diagnosis.

  * **Prescription**: Searchable medicine table with stock check simulation.

  * **History**: Timeline of previous visits.

### 3.3 Pharmacy Management (药房)

* **Features**:

  * **Pending Prescriptions**: Real-time list from Doctor Station.

  * **Dispense Action**: One-click dispense (updates mock inventory).

  * **Low Stock Alerts**: Visual highlighting.

### 3.4 Finance (收费)

* **Features**:

  * **Unpaid Bills**: Grouped by patient.

  * **Payment Modal**: Simulate QR Code/Cash payment.

  * **Receipt Preview**: Simple print layout.

## 4. Technical Steps

1. **Install Mocking Lib**: `npm install mockjs -D` (or implement lightweight custom mocks).
2. **Scaffold Layout**: Build `MainLayout.vue` and Sidebar.
3. **Implement API & Mocks**: Create `src/api` definitions matched with `src/mocks` data.
4. **Build Views**: Iteratively build the Vue pages for each module.
5. **State Management**: Use Pinia for `UserStore` (Auth) and `AppStore` (Global UI).

## 5. Verification

* **Scenario Run**:

  1. Register "Zhang San".
  2. Doctor sees "Zhang San" in queue -> Prescribes "Amoxicillin".
  3. Pharmacy sees order -> Dispenses.
  4. Finance sees bill -> Collects payment.

