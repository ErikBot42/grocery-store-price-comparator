import requests
URL = ""
USERNAME = "test0@email.com"
PASSWORD = "Password"
print("Sending POST request to server http://127.0.0.1:5000/login/app")
res = requests.post('http://127.0.0.1:5000/login/app', json={"Username":USERNAME, "Password": PASSWORD})
if res.ok:
    print(res.json())
else: 
    print(f"res is not ok: {res.ok}")