from pydantic import BaseModel

class LecturerCreate(BaseModel):
    first_name: str
    last_name: str
    office: str
    telephone: str
    email_address: str

class LecturerUpdate(BaseModel):
    first_name: str = None
    last_name: str = None
    office: str = None
    telephone: str = None
    email_address: str = None