from itertools import count
from pickle import NONE
import sqlite3

#Databasen "Grocery_Store_Database.db" måste ligga i samma map för att kunna köra koden(Ligger på Discord)
#TODO Add product, add store, Loggin

def AddProductToDatabase():
    print("TODO: AddProductToDatabase")

#Adds a user to the database. Default values are only ment for testing
def AddUserToDatabase(ID: int,email: str, mobile_nr: int, name: str = "TestName", password: str = "Password", date_of_birth: int = 19900101, city: str = "Karlstad", country: str = "Sweden", status: int = 0):
    try:
        cur.execute("INSERT INTO Register (User_ID, Name, Email, Password, Mobile_Number,  Date_of_Birth, City, Country, Logged_in_Status) VALUES ('"+str(ID)+"', '"+name+"', '"+email+"', '"+password+"', '"+str(mobile_nr)+"', '"+str(date_of_birth)+"', '"+city+"', '"+country+"', '"+str(status)+"')")
        #cur.execute("INSERT INTO Register (User_ID, Name, Email, Password, Mobile_Number,  Date_of_Birth, City, Country, Logged_in_Status) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)", (ID, name, email, password, mobile_nr, date_of_birth, city, country, status))
        #TODO: fix safe imput. Example -> cur.execute("select * from register where name =  %s", (input))
    except:
        print("\nUnable to add user\n")

def AddStoreToDatabase():
    print("TODO: AddStoreToDatabase")

def Loggin() -> bool:
    print("TODO: Loggin")

#Save all new changes to the database. 
def CommitToDatabase():
    con.commit()


con = sqlite3.connect("Grocery_Store_Database.db")  #Conection to database
cur = con.cursor()                                  #cursor executes sql comands
AddUserToDatabase(ID = 7, email = "test7@email.com", mobile_nr = 1415917)   #Testar att lägga till en användare i databasen
res = cur.execute("SELECT * FROM Register")         
print("Users:")   
for result in res:  print(result)
CommitToDatabase()          
con.close()