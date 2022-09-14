from pickle import NONE
import sqlite3
print("This machine can run sqlite3")

con = sqlite3.connect("Test2.db")
cur = con.cursor()
res = cur.execute("SELECT * FROM REGISTER")
print("Print 1")
print(res.fetchall())
print("Print 2")