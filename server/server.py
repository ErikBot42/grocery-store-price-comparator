from pickle import NONE
import sqlite3
print("This machine can run sqlite3")

con = sqlite3.connect("Grocery_Store_Database.db")
cur = con.cursor()
res = cur.execute("SELECT * FROM Register")
print(res.fetchall())
con.commit()
con.close()