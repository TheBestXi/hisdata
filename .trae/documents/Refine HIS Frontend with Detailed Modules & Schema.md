# HIS Frontend Refinement Plan

Based on your detailed requirements (Modules 1-9) and the specific database table structures, I will refine the current Mock-first frontend implementation. The goal is to strictly align the data models and functionality with your specifications.

## 1. Data Model Alignment (TypeScript Interfaces)
I will update `src/api/*.ts` to strictly match the provided table schemas.
- **Patient**: Add `medical_history`, `allergy_history`, `created_at`.
- **Appointment**: Add `status`, `registration_fee` (ensure match).
- **MedicalRecord**: Create new interface with `chief_complaint`, `present_illness`, `physical_examination`, `preliminary_diagnosis`.
- **Pharmacy**: Add `manufacturer`, `category`, `expiration_date` (rename from `expiryDate`).
- **Finance**: Create `FinanceRecord` interface with detailed fee breakdown (`medicine_fee`, `discount`, `total_fee`).

## 2. Mock Data Enhancement
I will update `src/mocks/*.ts` to generate realistic data conforming to the new interfaces.
- **Simulate Linkage**: Ensure "appointments" in the mock data logically link to "patients" and "doctors" by ID.
- **Status Logic**: Enhance mock logic to handle state transitions (e.g., Doctor completing a visit -> updates Appointment status -> generates Finance record).

## 3. Module Implementation Refinements
I will iterate through the modules to add missing features:

### Module 1: Registration (挂号管理)
- **H2 (Old Patient)**: Add a "Quick Search" tab to search by Name/ID and auto-fill registration form.
- **H4 (Stats)**: Add a chart component in the Registration view showing daily registration counts.

### Module 2: Patient Info (患者信息管理)
- **P1/P3**: Update Create/Edit forms to include `Medical History` and `Allergy History` text areas.

### Module 3: Doctor Workstation (医生看诊)
- **D3 (Quick View)**: Update the Patient Info Card to display `Allergy History` (highlighted in red) and `Medical History`.
- **D4 (Stats)**: Add a "Workload" statistic card (e.g., "Today's Patients: X").

### Module 4: Medical Record (病历书写)
- **M1/M2**: Update the "Medical Record" form to have separate fields for `Chief Complaint`, `Present Illness`, `Physical Exam`, `Diagnosis`. Implement a mock "Save" button that stores to a local list.

### Module 6: Pharmacy (药房管理)
- **S1 (CRUD)**: Add "Add Medicine" and "Edit Medicine" buttons/dialogs in the Inventory view.
- **S5 (Expiry)**: Enhance the table row styling to highlight medicines expiring within 3 months (Yellow) or 1 month (Red).

### Module 7 & 9: Stats & Finance (统计/收费)
- **T1/T2**: Create a new `src/views/statistics/index.vue` page with charts (using ECharts or simple CSS bars for now) for "Daily Visits" and "Financial Income".
- **F1**: Update Finance view to show a detailed bill breakdown (Registration + Medicine + Tests).

## 4. Verification
- I will verify that the project builds and runs (`npm run dev`) after these changes.
- I will ensure the "Run" script still works.

***
*Note on "3-part System":* I will maintain the current unified web portal structure (SPA) as it is standard for modern HIS frontends, but I will organize the menu to clearly separate "Registration/Patient" (Entry), "Doctor/Tech" (Review), and "Finance/Pharmacy/Stats" (Management) if needed. For now, the sidebar already groups them by function.
