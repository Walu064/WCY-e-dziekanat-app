import uvicorn
from fastapi import FastAPI
from routers import user_router, schedule_router

app = FastAPI()
app.include_router(user_router)
app.include_router(schedule_router)

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8000)