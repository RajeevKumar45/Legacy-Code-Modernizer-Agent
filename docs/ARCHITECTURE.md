# Architecture

Legacy Code Modernizer Agent is a tool for turning old code into modern code while proving it still behaves the same. This document describes how the project is built today and what is planned.

Status labels used below:

- **Implemented**: exists in the repository and has been run locally.
- **Planned**: an idea for a later milestone. No code exists for it yet.

## Overview

```
 Browser                          Your PC only (127.0.0.1)
┌──────────────────────────┐     ┌─────────────────────────────┐
│ React frontend           │     │ FastAPI backend             │
│ http://localhost:5173    │ ──► │ http://127.0.0.1:8000       │
│ (Vite dev server)        │ GET │ GET /api/health → JSON      │
└──────────────────────────┘ ◄── └─────────────────────────────┘

 samples/LegacyBonusCalculator.java   (run by hand with a JDK,
                                       not connected to the app yet)
```

## Folder layout

| Folder      | Contains                                   | Why it is separate                                   |
|-------------|--------------------------------------------|------------------------------------------------------|
| `frontend/` | React + TypeScript web page, built by Vite | UI code has its own language, tools, and dependencies |
| `backend/`  | Python FastAPI server                      | Server logic stays independent from the UI            |
| `samples/`  | Legacy code used as test input             | Test material never mixes with application code       |
| `docs/`     | Project documentation                      | One place to explain decisions                        |

## Components

### Frontend (Implemented)

- **What:** a single landing page (`src/App.tsx`) with the project name, a short purpose statement, a backend status box, and four cards for the planned workflow (Analyze, Modernize, Test, Compare).
- **Why:** gives users a place to interact with the tool, and gives us a visible way to confirm the frontend and backend can talk.
- **Tech:** React 19 for building the page from components, TypeScript 6 for type checking, Vite 8 for the dev server and production build.
- **Backend connection:** `src/HealthStatus.tsx` calls `GET http://127.0.0.1:8000/api/health` once when the page loads and shows *Loading*, *Connected*, or an error message.
- **Note:** the workflow cards are text only. Clicking them does nothing.

### Backend (Implemented)

- **What:** a FastAPI app in `backend/main.py` with one endpoint, `GET /api/health`, which returns:
  ```json
  {"status": "ok", "service": "Legacy Code Modernizer Backend", "version": "0.1.0"}
  ```
- **Why:** later stages (analysis, transformation, testing) need a server to do the heavy work. The health endpoint proves the server is running before we build on it.
- **Tech:** FastAPI 0.162.0 for routing and JSON, Uvicorn 0.54.0 as the web server. Packages are installed in `backend/.venv` and pinned in `requirements.txt`.
- **CORS:** only `http://localhost:5173` may read responses from a browser, and only for `GET` requests.
- **Security:** no authentication. This is acceptable only because the server listens on `127.0.0.1` (your PC only) and returns no sensitive data.

### Sample legacy program (Implemented)

- **What:** `samples/LegacyBonusCalculator.java` calculates an employee bonus from years of service, salary, and full-time status using deeply nested `if/else` blocks.
- **Why:** it is the **original baseline**. Future modernized versions will be checked against its behavior. Its legacy habits (nesting, magic numbers, `double` for money, silent `0` on bad input) are intentional and must not be "fixed" in this file.
- **Self-check:** its `main` method runs 13 documented cases and prints `PASS`/`FAIL`. All 13 pass on JDK 21.
- **Note:** it is run manually from a terminal. The frontend and backend do not use it yet.

## Planned stages (not implemented)

None of these exist in code yet. The order shows the intended pipeline.

| # | Stage                      | Purpose                                                                 | UI card   |
|---|----------------------------|-------------------------------------------------------------------------|-----------|
| 1 | Code analysis              | Read legacy code and report its structure, complexity, and risky areas  | Analyze   |
| 2 | AI transformation          | Use an AI model to rewrite the code in a modern style                   | Modernize |
| 3 | Automated tests            | Generate and run tests against the modernized code                      | Test      |
| 4 | Differential validation    | Run original and modernized code on the same inputs and compare outputs | Compare   |
| 5 | Reporting                  | Summarize what changed, test results, and any behavior differences      | (none yet)|

Why this order: we must understand code before changing it, and we must prove the new code matches the old before trusting it. The sample's 13 cases are the first inputs for differential validation.

## Running locally

```powershell
# Terminal 1: backend
cd backend
.\.venv\Scripts\python.exe -m uvicorn main:app --reload --host 127.0.0.1 --port 8000

# Terminal 2: frontend (first time: npm install)
cd frontend
npm run dev            # then open http://localhost:5173

# Sample (needs a JDK)
cd samples
javac LegacyBonusCalculator.java
java LegacyBonusCalculator
```

## Known limitations

- The backend URL is hard-coded in `HealthStatus.tsx`, and the CORS origin is hard-coded in `main.py`. Both must change before deployment.
- The page only works at `http://localhost:5173`. Opening `http://127.0.0.1:5173` is a different origin and CORS will block it.
- The health check runs once per page load and does not retry.
- There is no automated test suite for the frontend or backend yet.
