#Databasen "Grocery_Store_Database.db" måste ligga i samma map för att kunna köra koden(Ligger på Discord)

import sqlite3

class Database:
    
    def __init__(self):
        self.connection = sqlite3.connect("Grocery_Store_Database.db")  #Conection to database
        self.cursor = self.connection.cursor()                          #cursor executes sql comands

    def _createInsertSQLQuery(self, table: str, categories: str, values: list[str]) -> str:
        query = "INSERT INTO "+table+" ("+categories+") VALUES ("
        query = query + "'" + values.pop(0) + "'"
        for value in values:
            value = value.replace("'", "")
            query = query + ", '" + value + "'"
        query = query + ")"
        return query

    def _runInsertSQLQuerry(self, query: str) -> bool:
        try:
            self.cursor.execute(query)
            return True
        except sqlite3.Error as er:
            print("\nCould not run querry: " + query)
            print('\tSQLite error: %s' % (' '.join(er.args)))
            return False

    def addProductToDatabase(self, name: str, store: str, price: str, category: int) -> bool:
        query = self._createInsertSQLQuery(
            "Product", 
            "Category_ID, Product_Name, Store_ID, Price", 
            [str(category), name, str(store), price]
            )
        return self._runInsertSQLQuerry(query)
    
    def addUserToDatabase(self, email: str, mobile_nr: int, name: str = "TestName", password: str = "Password", date_of_birth: int = 19900101, city: str = "Karlstad", country: str = "Sweden", status: int = 0) -> bool:
        query = self._createInsertSQLQuery(
            "Register",
            "Name, Email, Password, Mobile_Number, Date_of_Birth, City, Country, Logged_in_Status",
            [name, email, password, str(mobile_nr), str(date_of_birth), city, country, str(status)]
        ) 
        return self._runInsertSQLQuerry(query)
    
    def addStoreToDatabase(self, ID: int, name: str) -> bool:
        query = self._createInsertSQLQuery(
            "Store",
            ("Store_ID, Store_Name"),
            [str(ID), name]
        )
        return self._runInsertSQLQuerry(query)
    
    def addCategoryToDatabase(self, ID: int, name: str) -> bool:
        query = self._createInsertSQLQuery(
            "Category",
            "Category_ID, Category_Name",
            [str(ID), name]
        )
        return self._runInsertSQLQuerry(query)


    def addFavoriteProduct(self, user_ID: int, product_ID: int) -> bool:
        query = self._createInsertSQLQuery(
        "Favourite_Products",
        "User_ID, Product_ID",
        [str(user_ID), str(product_ID)]
        ) 
        return self._runInsertSQLQuerry(query)

    def addShopingList(self, list_name) -> bool:    #NOTE AutoIncrement needs to be added to List table in database, fix PascalCase on List.List_Name
        query = self._createInsertSQLQuery(
        "List",
        "List_Name",
        [list_name]
        ) 
        return self._runInsertSQLQuerry(query)

    def addShopingListOwner(self, user_ID: int, list_ID: int) -> bool: #NOTE Fix PascalCase on tale name: List_Owner
        query = self._createInsertSQLQuery(
        "List_Owner",
        "User_ID, List_ID",
        [str(user_ID), str(list_ID)]
        ) 
        return self._runInsertSQLQuerry(query)

    def addShopingListItem(self, list_ID: int, product_ID: int, amount: int) -> bool: #NOTE Fix PascalCase on List_Items.List_ID
        query = self._createInsertSQLQuery(
        "List_Items",
        "List_ID, Product_ID, Amount",
        [str(list_ID), str(product_ID), str(amount)]
        ) 
        return self._runInsertSQLQuerry(query)


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

    def addFavoriteStore(self, user_ID: int, store_ID: int) -> bool:
        query = self._createInsertSQLQuery(
        "Favourite_Store",
        "User_ID, Store_ID",
        [str(user_ID), str(store_ID)]
        ) 
        return self._runInsertSQLQuerry(query)
    
    #Save all new changes to the database. 
    def commitToDatabase(self):
        self.connection.commit()
     
    def Close(self):
        self.connection.close()
    
    
    ##################################################################
    #                          Testing                               #
    ##################################################################
    
    def fillDatabase(self,input_nr: int = 5):
        for i in range(input_nr): self.addUserToDatabase(email = "test"+str(i)+"@email.com", mobile_nr = i+1, name = "User"+str(i))
        self.addStoreToDatabase(ID = 1, name = "LIDL")
        self.addStoreToDatabase(ID = 2, name = "COOP")
        self.addStoreToDatabase(ID = 3, name = "ICA")
        self.addStoreToDatabase(ID = 4, name = "WILLYS")
        for i in range(input_nr): self.addCategoryToDatabase(ID = i, name = "Category"+str(i))
        for i in range(input_nr): self.addProductToDatabase(name = "Product"+str(i), store=i, price="10"+str(i), category=0)
        for i in range(input_nr): self.addFavoriteProduct(user_ID=i, product_ID=i)
        for i in range(input_nr): self.addShopingList(list_name="List" + str(i))
        for i in range(input_nr): self.addShopingListOwner(user_ID=i,list_ID=i)
        for i in range(input_nr): self.addShopingListItem(list_ID=i, product_ID=i, amount=i)
        for i in range(input_nr): self.addFavoriteStore(user_ID=i, store_ID=min(i+1, 4))


    def droppAllData(self): #Använd endast för att rensa databasen vid testning
        result = input("\n\nVARNING!\nÄr du säker på att du tömma databasen? y/n\n")
        if (result == 'y'):
            print("Raderar all data...")
            self.cursor.execute("DELETE FROM List_Items WHERE '1' == '1'")
            self.cursor.execute("DELETE FROM List_Owner WHERE '1' == '1'")
            self.cursor.execute("DELETE FROM List WHERE '1' == '1'")
            self.cursor.execute("DELETE FROM Favourite_Products WHERE '1' == '1'")
            self.cursor.execute("DELETE FROM Product WHERE '1' == '1'")
            self.cursor.execute("DELETE FROM Register WHERE '1' == '1'")
            self.cursor.execute("DELETE FROM Store WHERE '1' == '1'")
            self.cursor.execute("DELETE FROM Category WHERE '1' == '1'")
            self.cursor.execute("DELETE FROM List_Items WHERE '1' == '1'")
        else:
            print("Avbryter")



