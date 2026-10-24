# MVP Requirements

The MVP (minimum viable product) is the smallest version of the tool that is useful. This document lists what the MVP must do, which parts are done, and which are still planned.

Status labels:

- **Done**: implemented and checked locally.
- **Planned**: not started. No code exists.

For how the pieces fit together, see [ARCHITECTURE.md](ARCHITECTURE.md).

## Goal

Take a small legacy Java program, modernize it, and show evidence that the modernized version behaves exactly like the original.

## Foundation requirements

| ID   | Requirement                                                                 | Status | How it was checked |
|------|-----------------------------------------------------------------------------|--------|--------------------|
| F-1  | Project is split into `frontend/`, `backend/`, `samples/`, `docs/` under Git | Done   | Folders exist; Git repository initialized |
| F-2  | Backend exposes `GET /api/health` returning `status`, `service`, `version` as JSON | Done | Called the endpoint; got `{"status":"ok",...}` |
| F-3  | Backend allows browser requests only from `http://localhost:5173`, `GET` only | Done | Response includes `access-control-allow-origin` for that origin and not for others |
| F-4  | Frontend landing page shows the project name, purpose, and four workflow stages | Done | `npm run build` and `npm run lint` pass; page renders in a browser |
| F-5  | Frontend shows backend status as Loading, Connected, or an error message      | Done   | Headless Edge showed *Connected* with backend on, error message with backend off |
| F-6  | A legacy Java sample with documented inputs and expected outputs exists       | Done   | `LegacyBonusCalculator.java` compiles on JDK 21; all 13 cases pass |

## Feature requirements

| ID   | Requirement | Status | Why it matters |
|------|-------------|--------|----------------|
| M-1  | **Code analysis:** read a Java file and report its structure (methods, nesting depth, magic numbers) | Planned | We need to understand code before changing it |
| M-2  | **AI transformation:** produce a modernized version of the sample without changing its behavior | Planned | This is the core purpose of the tool |
| M-3  | **Automated tests:** run the sample's documented cases against the modernized code automatically | Planned | Removes manual checking and catches mistakes |
| M-4  | **Differential validation:** run original and modernized code on the same inputs and flag any output difference | Planned | The main evidence that modernization is safe |
| M-5  | **Reporting:** show a summary of changes, test results, and differences in the frontend | Planned | Lets a person review and trust the result |
| M-6  | Frontend workflow cards trigger the matching backend stage | Planned | Today the cards are text only |

## Acceptance criteria for the finished MVP

The MVP is complete when, for `samples/LegacyBonusCalculator.java`:

1. The analysis report lists the method and its nesting depth.
2. A modernized version is produced and compiles.
3. All 13 documented cases give identical results in the original and modernized versions.
4. The frontend displays the original code, the modernized code, and a pass/fail report.

None of these criteria are met yet.

## Constraints

- Runs locally only (`127.0.0.1`). No deployment and no user accounts in the MVP.
- Before any endpoint accepts code or runs programs, it needs input limits and safe execution. Running untrusted code is a security risk.
- The original sample file must never be edited. Modernized code goes in a separate file.
- Dependencies are pinned to exact versions.

## Out of scope for the MVP

- Languages other than Java
- Multi-file projects or uploading whole repositories
- Authentication, user accounts, and saved history
- Cloud deployment
