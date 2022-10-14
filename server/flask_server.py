from re import T
from flask import Flask, render_template, url_for, session, redirect, request, flash, jsonify
from database import Database
from firebaseHandeler import firebaseHandeler, userData
from datetime import timedelta
import json
import os.path
import sys
from category_regexes import CATEGORIES



app = Flask(__name__)
app.secret_key = b".U,e-Xr))$I,/bK"
app.permanent_session_lifetime = timedelta(days=1)
fdb = firebaseHandeler()



if not os.path.isfile("Grocery_Store_Database.db"):
    sys.exit("Could not find database")


 

@app.route("/", methods=["GET", "POST"])
def showHomePage():
    if "user" in session:
        return redirect(url_for("products"))
    elif request.method == "POST":
        db = Database()
        password = request.form['Password']
        user_name = request.form["Username"]
        if db.loginValidation(email=user_name, password=password) or (user_name=="admin" and password == "password"):
            flash(f"Logged in as: {request.form['Username']}", "info")
            session["user"] = request.form["Username"]
            session.permanent = True
            db.close()
            return redirect(url_for("products"))
        else:
            flash(f"Username or Password was incorect", "error")
            db.close()
            return render_template("login.html")
    else: 
        return render_template("login.html")
 
@app.route("/admin/")
@app.route("/products/", methods=["GET", "POST"])
def products():
    from database import DbProd
    if "user" in session:
        db = Database()
        if request.method == "POST":
            prod: list[DbProd] = db.searchProduct(request.form["productSearch"])
            db.close()
        else:
            prod: list[DbProd] = db.getAllProductsWithCategories(CATEGORIES)
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
            #usr = db.getUserDataForAdmin()
            usr = fdb.getUserData()
        return render_template("admin_users.html", users=usr)
    else:
        return redirect(url_for("showHomePage"))

@app.route("/logout/")
def logout():
    if "user" in session:
        session.pop("user", None)
        flash("You have been loged out", "info")
    return redirect(url_for("showHomePage"))

@app.route("/product/remove/<id>", methods=["Post"])
def removeProduct(id):
    db = Database()
    db.removeProduct(id)
    db.commitToDatabase()
    db.close()
    return redirect(url_for("products"))

@app.route("/products/new/", methods = ["POST"])
def addProduct():
    db = Database()
    query = db.addProductToDatabase(
        category=-1,
        name=request.form["Product_Name"],
        store=request.form["Store_ID"],
        price=-1,
        price_num=request.form["Price_num"],
        price_kg=request.form["Price_kg"],
        price_l=request.form["Price_l"],
        url=request.form["URL"],
        amount_kg=request.form["Amount_kg"],
        amount_l=request.form["Amount_l"]
    )
    db.commitToDatabase()
    db.close()
    return redirect(url_for("products"))

@app.route("/users/new/", methods = ["POST"])
def addUser():
    db = fdb
    db.addUser(userData(
        email=request.form["Email"],
        telephone=request.form["Mobile_Number"],
        password=request.form["Password"],
        city=request.form["City"],
        name=request.form["Name"],
        ica=False if request.form.get("ICA") is None else True,
        coop=False if request.form.get("COOP") is None else True,
        lidl=False if request.form.get("LIDL") is None else True,
        willys=False if request.form.get("Willys") is None else True
    ))
    return redirect(url_for("users"))

@app.route("/users/remove/<id>", methods=["Post"])
def removeUser(id):
    db = fdb
    db.removeUser(id)
    return redirect(url_for("users"))


@app.route("/app/login/", methods=["POST"])
def appLogin():
    db = Database()
    data = request.json
    print(f"INPUT: ({data['Username']}, {data['Password']})")
    if (db.loginValidation(email = data['Username'], password = data['Password']) == True):
        db.close()
        result = {'login': 'True'}
    else:
        db.close()
        result = {'login': 'False'}
    return jsonify(result)

@app.route("/app/products/", methods=["POST", "GET"])
def sendProductsInJson():
    db = Database()
    prod = db.getAllProductsWithCategories(CATEGORIES)
    db.close()
    data = []
    for item in prod:
        if item.name != None:  
            temp = {
                "id":str(item.i),
                "name":item.name,
                "price":str(item.price_num),
                "price_kg":str(item.price_num),
                "price_l":str(item.price_num),
                "image":item.url,
                "store":str(item.store),
                "store_id":str(item.store_id)
            }   
            data.append(temp)
    prod = '{"products":'+str(data)+'}'
    prod = prod.replace("'", '"').replace("\\", "")
    return jsonify(prod)


@app.route("/products/category/<category>", methods = ["GET"])
def productCategory(category: str):
    from database import DbProd
    if "user" in session:
        db = Database()
        for cat in CATEGORIES:
            if category == cat[0]:
                prod: list[DbProd] = db.getProductCategory(cat[1])
                break
        else:
            if category == "Misk":
                prod: list[DbProd] = db.getProductWithoutCategory(CATEGORIES)
                #print("TODO, OOPS")
                #prod: list[DbProd] = []
            elif category == "All":
                prod: list[DbProd] = db.getProductDataForAdmin()
            else:
                print(f"#### Not a mach found for: {category}")
                prod: list[DbProd] = []
        db.close()  
        return render_template("admin_products.html", products=prod)
    else:
        return redirect(url_for("showHomePage"))
    

@app.route("/debug/")
def debug():
    return render_template("debug.html")
   
def runServer():
    app.run(host="0.0.0.0", debug=True)
if __name__ == "__main__":
    runServer()

