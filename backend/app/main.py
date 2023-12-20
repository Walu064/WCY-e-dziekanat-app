import uvicorn
from fastapi import FastAPI
from routers import register_router, login_router

app = FastAPI()
app.include_router(register_router)
app.include_router(login_router)

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)