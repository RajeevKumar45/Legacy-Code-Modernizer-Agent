from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

# Name and version the service reports about itself
SERVICE_NAME = "Legacy Code Modernizer Backend"
SERVICE_VERSION = "0.1.0"

# The only website allowed to call this API from a browser: the Vite dev server.
FRONTEND_ORIGINS = ["http://localhost:5173"]

# Create the application object. Uvicorn will look for this name.
app = FastAPI(title=SERVICE_NAME, version=SERVICE_VERSION)

# CORS: tell browsers that pages from FRONTEND_ORIGINS may read our responses.
# Only GET is allowed because that is the only method the API has right now.
app.add_middleware(
    CORSMiddleware,
    allow_origins=FRONTEND_ORIGINS,
    allow_methods=["GET"],
)


# Register a GET route at /api/health.
# FastAPI turns the returned dict into JSON automatically.
@app.get("/api/health")
def health_check():
    return {
        "status": "ok",
        "service": SERVICE_NAME,
        "version": SERVICE_VERSION,
    }
