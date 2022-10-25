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
        db = fdb
        if request.method == "POST":
            usr = db.getUserSearch(request.form["userSearch"])
        else:
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
    db.addProductToDatabase(
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


def _productsToJson(products):
    data = []
    for item in products:
        if item.name != None:  
            temp = {
                "id":str(item.i),
                "name":item.name,
                "price":str(item.price_num),
                "price_kg":str(item.price_kg),
                "price_l":str(item.price_l),
                "image":item.url,
                "store":str(item.store),
                "store_id":str(item.store_id),
                "category":item.category
            }  
            data.append(temp)
    prod = '{"products":'+str(data)+'}'
    prod = prod.replace("'", '"').replace("\\", "")
    return prod

@app.route("/app/products/", methods=["POST", "GET"])
def sendProductsInJson():
    db = Database()
    prod = db.getAllProductsWithCategories(CATEGORIES)
    db.close()
    prod = _productsToJson(prod)
    return jsonify(prod)

def _getCategory(category):
    from database import DbProd
    db = Database()
    for cat in CATEGORIES:
        if category == cat[0]:
            prod: list[DbProd] = db.getProductCategory(cat[1])
            break
    else:
        if category == "Misc":
            prod: list[DbProd] = db.getProductWithoutCategory(CATEGORIES)
        elif category == "All":
            prod: list[DbProd] = db.getProductDataForAdmin()
        else:
            print(f"#### Not a mach found for: {category}")
            prod: list[DbProd] = []
    db.close()  
    return prod

@app.route("/products/category/<category>", methods = ["GET"])
def productCategory(category: str):
    from database import DbProd
    if "user" in session:
        prod = _getCategory(category)
        return render_template("admin_products.html", products=prod)
    else:
        return redirect(url_for("showHomePage"))
    
@app.route("/app/products/<category>", methods = ["GET"])
def appCategory(category):
    prod = _productsToJson(_getCategory(category=category))
    return jsonify(prod)

@app.route("/debug/")
def debug():
    return render_template("debug.html")

@app.route("/scrape/All")
def scrapeAll():
    if "user" in session:
        db = Database()
        db.runDropAll()
        db.runScraper()
        db.close()
        return redirect(url_for("products"))
    else:
        redirect(url_for("showHomePage"))

@app.route("/scrape/fast")
def scrapeFast():
    if "user" in session:
        db = Database()
        db.runDropAll()
        db.runScraper(fast=True)
        db.close()
        return redirect(url_for("products"))
    else:
        return redirect(url_for("showHomePage"))

@app.route("/dropAll")
def dropAll():
    if "user" in session:
        db = Database()
        db.runDropAll()
        db.close()
        return redirect(url_for("products"))
    else:
        return redirect(url_for("showHomePage"))

@app.route("/edit/<id>/", methods = ["POST"])
def edit(id):
    if "user" in session:
        db = Database()
        prod = db.getProductFromID(id)
        if prod == None:
            print(f"ID({id}) not found")
            return redirect(url_for("products"))
        else:
            return render_template("admin_edit", item = prod)
    else:
        return redirect(url_for("showHomePage"))

@app.route("/products/edit/", methods=["POST"])
def runEdit():
    if "user" in session:
        db =Database()
        print(f"Removing Product: {request.form['ID']}, {request.form['Product_Name']}")
        db.removeProduct(request.form['ID'])
        db.commitToDatabase()
        db.addProductToDatabase(
        category=-1,
        name=request.form["Product_Name"],
        store=request.form["Store"],
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
    else:
        return redirect(url_for("showHomePage"))
   
def runServer():
    app.run(host="192.168.10.221", debug=True)
if __name__ == "__main__":
    runServer()

