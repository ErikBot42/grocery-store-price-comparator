from flask import Flask, render_template, url_for, session, redirect, request, flash, jsonify
from database import Database
from datetime import timedelta
 
# import request
from flask import request
app = Flask(__name__)
app.secret_key = b".U,e-Xr))$I,/bK"
app.permanent_session_lifetime = timedelta(days=1)
 
@app.route("/", methods=["GET", "POST"])
def showHomePage():
    if "user" in session:
        return redirect(url_for("products"))
    elif request.method == "POST":
        db = Database()
        flash(f"Logged in as: {request.form['Username']}", "info")
        if db.logginValidation(email=request.form["Username"], password=request.form['Password']):
            session["user"] = request.form["Username"]
            session.permanent = True
            db.close()
            return redirect(url_for("products"))
        else:
            db.close()
            return render_template("login.html")
    else: 
        return render_template("login.html")
 
@app.route("/admin/")
@app.route("/products/", methods=["GET", "POST"])
def products():
    if "user" in session:
        db = Database()
        if request.method == "POST":
            prod = db.searchProduct(request.form["productSearch"])
        else:
            prod = db.getProductDataForAdmin()
            db.close()
        return render_template("admin_products.html", products=prod)
    else:
        return redirect(url_for("showHomePage"))


@app.route("/users/", methods=["GET", "POST"])
def users():
    if "user" in session:
        db = Database()
        if request.method == "POST":
            usr = db.searchUser(request.form["userSearch"])
        else:
            usr = db.getUserDataForAdmin()
        db.close()
        return render_template("admin_users.html", users=usr)
    else:
        return redirect(url_for("showHomePage"))

@app.route("/logout/")
def logout():
    if "user" in session:
        session.pop("user", None)
        flash("You have been loged out", "info")
    return redirect(url_for("showHomePage"))

@app.route("/remove/product/<id>", methods=["Post"])
def removeProduct(id):
    db = Database()
    db.removeProduct(id)
    db.commitToDatabase()
    db.close()
    return redirect(url_for("admin_products.html"))

@app.route("/login/app", methods=["POST"])
def appLogin():
    database = Database()
    data = request.json
    print(f"INPUT: ({data['Username']}, {data['Password']})")
    if (database.logginValidation(email = data['Username'], password = data['Password']) == True):
        result = {'login': 'True'}
    else:
        result = {'login': 'False'}
    return jsonify(result)

@app.route("/debug/")
def debug():
    return render_template("debug.html")
   
def runServer():
    app.run(host="0.0.0.0", debug=True)
if __name__ == "__main__":
    runServer()

