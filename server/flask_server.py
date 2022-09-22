from flask import Flask
 
# import request
from flask import request
app = Flask(__name__)
 
@app.route("/")
def showHomePage():
    return "This is home page"
 
@app.route("/debug", methods=["POST"])
def debug():
    text = request.form["sample"]
    print(text)
    return "received" 
   
def runServer():
    app.run(host="0.0.0.0")
if __name__ == "__main__":
    runServer()

