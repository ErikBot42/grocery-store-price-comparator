from flask import Flask, render_template
from database import Database
 
# import request
from flask import request
app = Flask(__name__)
 
@app.route("/")
def showHomePage():
    return render_template("login.html")
 
@app.route("/debug", methods=["POST"])
def debug():
    text = request.form["sample"]
    print(text)
    return "received" 

@app.route("/login/<name>/<password>")
def addProduct(name, password):
    database = Database()
    print(f"INPUT: ({name}, {password})")
    if (database.logginValidation(email = name, password = password) == True):
        return f"Correct login"
    else: 
        return f"Bad login({name}, {password})"
   
def runServer():
    app.run(host="0.0.0.0", debug=True)
if __name__ == "__main__":
    runServer()

