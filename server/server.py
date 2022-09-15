from itertools import count
from pickle import NONE
import sqlite3

#Databasen "Grocery_Store_Database.db" måste ligga i samma map för att kunna köra koden(Ligger på Discord)
#TODO 
# Add product, 
# remove inputvariables for ID, 
# change input variables for refering keys to names from IDs,
# Add safe input (SQLinjections)

class Database:
    
    def __init__(self):
        self.con = sqlite3.connect("Grocery_Store_Database.db")  #Conection to database
        self.cur = self.con.cursor()                                  #cursor executes sql comands

    #Adds a product to the database
    def AddProductToDatabase(self, ID: int, name: str, store: str, price: str, category: int) -> bool:
        query = "INSERT INTO Product (Category_ID, Product_ID, Product_Name, Store_ID, Price) VALUES ('"+str(category)+"', '"+str(ID)+"', '"+name+"', '"+str(store)+"', '"+price+"')"
        print(query)
        try: 
            self.cur.execute(query)
            return True
        except:
            print("Unable to add product")
            return False
    
    #Adds a user to the database. Default values are only ment for testing
    def AddUserToDatabase(self, ID: int,email: str, mobile_nr: int, name: str = "TestName", password: str = "Password", date_of_birth: int = 19900101, city: str = "Karlstad", country: str = "Sweden", status: int = 0) -> bool:
        try:
            self.cur.execute("INSERT INTO Register (User_ID, Name, Email, Password, Mobile_Number,  Date_of_Birth, City, Country, Logged_in_Status) VALUES ('"+str(ID)+"', '"+name+"', '"+email+"', '"+password+"', '"+str(mobile_nr)+"', '"+str(date_of_birth)+"', '"+city+"', '"+country+"', '"+str(status)+"')")
            return True
        except:
            print("Unable to add user")
            return False
    
    def AddStoreToDatabase(self, ID: int, name: str) -> bool:
        try: 
            self.cur.execute("INSERT INTO Store VALUES ('"+str(ID)+"', '"+name+"')")
            return True
        except:
            print("Unable to add store")
            return False
    
    def AddCategoryToDatabase(self, ID: int, name: str) -> bool:
        try: 
            self.cur.execute("INSERT INTO Category VALUES ('"+str(ID)+"', '"+name+"')")
            return True
        except:
            print("Unable to add category")
            return False
    
    #Returns true if password matches password from database
    def Loggin(self, email: str, password: str) -> bool:
        print("TODO: Loggin")
        try:
            self.res = self.cur.execute("SELECT Password FROM Register WHERE email = '"+ email +"'")
        except:
            print("Unable to run query")
        temp = self.res.fetchone()[0]
        if (temp == password): 
            return True 
        else:
            return False
    
    #Save all new changes to the database. 
    def CommitToDatabase(self):
        self.con.commit()
    
    
    
    ##################################################################
    #                          Testing                               #
    ##################################################################
    
    def FillDatabase(self,input_nr: int = 5):
       for i in range(input_nr): self.AddUserToDatabase(ID = i, email = "test"+str(i)+"@email.com", mobile_nr = i+1, name = "User"+str(i))
       for i in range(input_nr): self.AddStoreToDatabase(ID = i, name = "Store"+str(i))
       for i in range(input_nr): self.AddCategoryToDatabase(ID = i, name = "Category"+str(i))
       for i in range(input_nr): self.AddProductToDatabase(ID = i, name = "Product"+str(i), store=i, price="10"+str(i), category=0)
        
    def Close(self):
        self.con.close()

database = Database()
database.FillDatabase()
database.CommitToDatabase()
database.Close()
#if (AddUserToDatabase(ID = 7, email = "test7@email.com", mobile_nr = 1415917)): print("Added User") 
#if (AddStoreToDatabase(4, "Pressbyron")): print("Added store") 
#if (AddProductToDatabase(1, "Chees", 1, "15", 1)): print("Added Product")
