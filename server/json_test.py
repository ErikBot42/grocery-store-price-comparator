import requests
import json
URL = "http://127.0.0.1:5000/app/products"
USERNAME = "test0@email.com"
PASSWORD = "Password"

print(f"Sending POST request to server {URL}")
res = requests.post(URL, json={"Username":USERNAME, "Password": PASSWORD})
if res.ok:
    print(res.json())
else:   
    print(f"res is not ok: {res.ok}")