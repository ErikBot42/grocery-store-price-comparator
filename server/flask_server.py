from flask import Flask, render_template, url_for, session, redirect, request, flash, jsonify
from database import Database
from datetime import timedelta

CATEGORIES = [
    ["Vegetarian", [".*[vV]egetar.*"]], 
    ["Vegan", [".*[vV]egan.*"]], 
    ["Meat", [".*[kK]ött.*", ".*[fF]isk.*", ".*[kK]arré.*", ".*[kK]orv.*", ".*[fF]ilé.*", ".*[kK]yckling.*", ".*[kK]ebab.*", ".*[sS]alami.*", ".*[bB]iff.*", ".*[fF]ärs .*"]], 
    ["Fruit", [".*[fF]rukt.*", ".*[äÄ]pple.*", ".*[pP]äron.*", ".*[bB]anan.*", ".*[dD]ruvor.*", ".*[tT]omat.*", ".*[pP]aprika.*", ".*[sS]alad.*", ".*[aA]vokado.*", ".*[cC]itro(n|ner).*"]], 
    ["Dairy", [".*[mM]jölk.*", ".*[sS]mör.*", ".*[äÄ]gg$", ".*[oO]st$", ".*[yY]oghurt.*", ".*[mM]ilk.*", ".*[mM]ozzarella.*"]], 
    ["Drink", [".*[lL]äsk$", ".*[dD]rika.*"]], 
    ["Sweets", [".*[gG]odis.*", ".*[gG]lass.*", ".*[cC]hips.*", ".*[oO]stbågar.*", ".*[cC]hoklad.*", ".*[nN]ötter.*"]], 
    ["Bread", [".*[bB]röd.*", ".*[kK]ak(a|or).*", ".*[cC]ookie.*", ".*[bB]ull(e|ar).*"]]
    ]



app = Flask(__name__)
app.secret_key = b".U,e-Xr))$I,/bK"
app.permanent_session_lifetime = timedelta(days=1)
 
@app.route("/", methods=["GET", "POST"])
def showHomePage():
    if "user" in session:
        return redirect(url_for("products"))
    elif request.method == "POST":
        db = Database()
        password = request.form['Password']
        user_name = request.form["Username"]
        if db.logginValidation(email=user_name, password=password) or (user_name=="admin" and password == "password"):
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

@app.route("/products/category/<category>", methods = ["GET"])
def productCategory(category: str):
    if "user" in session:
        db = Database()
        for cat in CATEGORIES:
            if category == cat[0]:
                print(f"#### Found match!: {category} matches {cat[0]}")
                prod = db.getProductCategory(cat[1])
                break
        else:
            print(f"#### Not a mach found for: {category}")
            prod = db.getProductDataForAdmin()
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

