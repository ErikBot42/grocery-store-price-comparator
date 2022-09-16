#Databasen "Grocery_Store_Database.db" måste ligga i samma map för att kunna köra koden(Ligger på Discord)

from itertools import count
from pickle import NONE
import sqlite3
from weakref import ref

#TODO
# change input variables for refering keys to names from IDs,
# Add safe input (SQLinjections)

class Database:
    
    def __init__(self):
        self.connection = sqlite3.connect("Grocery_Store_Database.db")  #Conection to database
        self.cursor = self.connection.cursor()                             #cursor executes sql comands

    def _createInsertSQLQuery(self, table: str, categories: str, values: list[str]) -> str:
        query = "INSERT INTO "+table+" ("+categories+") VALUES ("
        query = query + "'" + values.pop(0) + "'"
        for value in values:
            query = query + ", '" + value + "'"
        query = query + ")"
        #('"+str(category)+"', '"+name+"', '"+str(store)+"', '"+price+"')"
        print(query)
        return query

    def addProductToDatabase(self, name: str, store: str, price: str, category: int) -> bool:
        query = self._createInsertSQLQuery(
            "Product", 
            "Category_ID, Product_Name, Store_ID, Price", 
            [str(category), name, str(store), price]
            )
        try: 
            self.cursor.execute(query)
            return True
        except:
            print("Unable to add product")
            return False
    
    def addUserToDatabase(self, email: str, mobile_nr: int, name: str = "TestName", password: str = "Password", date_of_birth: int = 19900101, city: str = "Karlstad", country: str = "Sweden", status: int = 0) -> bool:
        query = self._createInsertSQLQuery(
            "Register",
            "Name, Email, Password, Mobile_Number, Date_of_Birth, City, Country, Logged_in_Status",
            [name, email, password, str(mobile_nr), str(date_of_birth), city, country, str(status)]
        ) 
        try:
            self.cursor.execute(query)
            return True
        except:
            print("Unable to add user")
            return False
    
    def addStoreToDatabase(self, ID: int, name: str) -> bool:
        query = self._createInsertSQLQuery(
            "Store",
            ("Store_ID, Store_Name"),
            [str(ID), name]
        )
        try: 
            self.cursor.execute(query)
            return True
        except:
            print("Unable to add store")
            return False
    
    def addCategoryToDatabase(self, ID: int, name: str) -> bool:
        query = self._createInsertSQLQuery(
            "Category",
            "Category_ID, Category_Name",
            [str(ID), name]
        )
        try: 
            self.cursor.execute(query)
            return True
        except:
            print("Unable to add category")
            return False

    def logginValidation(self, email: str, password: str) -> bool:
        try:
            res = self.cursor.execute("SELECT Password FROM Register WHERE email = '"+ email +"'")
            temp = res.fetchone()[0]
        except:
            print("Unable to run query")
            return False
        if (temp == password): 
            return True 
        else:
            return False
    
    #Save all new changes to the database. 
    def commitToDatabase(self):
        self.connection.commit()
    
    
    
    ##################################################################
    #                          Testing                               #
    ##################################################################
    
    def fillDatabase(self,input_nr: int = 5):
       for i in range(input_nr): self.addUserToDatabase(email = "test"+str(i)+"@email.com", mobile_nr = i+1, name = "User"+str(i))
       #for i in range(input_nr): self.addStoreToDatabase(ID = i, name = "Store"+str(i))
       self.addStoreToDatabase(ID = 1, name = "LIDL")
       self.addStoreToDatabase(ID = 2, name = "COOP")
       self.addStoreToDatabase(ID = 3, name = "ICA")
       self.addStoreToDatabase(ID = 4, name = "WILLYS")
       for i in range(input_nr): self.addCategoryToDatabase(ID = i, name = "Category"+str(i))
       for i in range(input_nr): self.addProductToDatabase(name = "Product"+str(i), store=i, price="10"+str(i), category=0)
        
    def Close(self):
        self.connection.close()

database = Database()
database.fillDatabase(2)
database._createInsertSQLQuery("TableName", "Item1, item2, item3", ["value1", "value2", "value3"])
database.commitToDatabase()
database.Close()

