#Databasen "Grocery_Store_Database.db" måste ligga i samma map för att kunna köra koden(Ligger på Discord)

import sqlite3
import re

class Database:
    
    def __del__(self):
        self.close()
    def __init__(self):
        self.connection = sqlite3.connect("Grocery_Store_Database.db")  #Conection to database
        self.cursor = self.connection.cursor()                          #cursor executes sql comands
        self.connection.create_function(
                           'REGEXP', 2, 
                           lambda exp, item : re.search(exp, item) != None)

    def _createInsertSQLQuery(self, table: str, categories: str, values: list[str]) -> str:
        query = f"INSERT INTO {table} ({categories}) VALUES ("
        query = f"{query} ?"
        for value in range(len(values) - 1):
            query = f"{query}, ?"
        query = f"{query} )"
        return query

    def _runSQLQuery(self, query: str, data: list[str]) -> bool:
        try:
            self.cursor.execute(query, data)
            return True
        except sqlite3.Error as er:
            print("\nCould not run query: " + query)
            print('\tSQLite error: %s' % (' '.join(er.args)))
            print("Om du saknar tabeller, testa att uppdatera databasen med Database.recreateDatabase()")
            return False

    def _runSQLQueryWithResults(self, query: str) -> list:
        try:
            result = self.cursor.execute(query)
            return result.fetchall()
        except sqlite3.Error as er:
            print("\nCould not run querry: " + query)
            print('\tSQLite error: %s' % (' '.join(er.args)))
            return []

    def addProductToDatabase(self,
            name: str,
            store: str,
            price: str,
            category: int,
            price_num: float | None = None,
            price_kg: float | None = None,
            price_l: float | None = None,
            amount_kg: float | None = None,
            amount_l: float | None = None,
            url: str = "",
            ) -> bool:
        data = [str(category), name, str(self.getStoreID(store)), price, price_num, price_kg, price_l, amount_kg, amount_l, url]
        query = self._createInsertSQLQuery(
            "Product", 
            "Category_ID, Product_Name, Store_ID, Price, Price_num, Price_kg, Price_l, Amount_kg, Amount_l, URL", 
            data
            )
        return self._runSQLQuery(query, data)
    
    def addUserToDatabase(self, email: str, mobile_nr: int, name: str = "TestName", password: str = "Password", date_of_birth: int = 19900101, city: str = "Karlstad", country: str = "Sweden", status: int = 0) -> bool:
        data = [name, email, password, str(mobile_nr), str(date_of_birth), city, country, str(status)]
        query = self._createInsertSQLQuery(
            "Register",
            "Name, Email, Password, Mobile_Number, Date_of_Birth, City, Country, Logged_in_Status",
            data
        ) 
        return self._runSQLQuery(query, data)
    
    def addStoreToDatabase(self, ID: int, name: str) -> bool:
        data = [str(ID), name]
        query = self._createInsertSQLQuery(
            "Store",
            ("Store_ID, Store_Name"),
            data
        )
        return self._runSQLQuery(query, data)
    
    def addCategoryToDatabase(self, ID: int, name: str) -> bool:
        data = [str(ID), name]
        query = self._createInsertSQLQuery(
            "Category",
            "Category_ID, Category_Name",
            data
        )
        return self._runSQLQuery(query, data)


    def addFavoriteProduct(self, user_ID: int, product_ID: int) -> bool:
        data = [str(user_ID), str(product_ID)]
        query = self._createInsertSQLQuery(
            "Favourite_Products",
            "User_ID, Product_ID",
            data
            ) 
        return self._runSQLQuery(query, data)

    def addShopingList(self, list_name) -> bool:    #NOTE AutoIncrement needs to be added to List table in database, fix PascalCase on List.List_Name
        data = [list_name]
        query = self._createInsertSQLQuery(
            "List",
            "List_Name",
            data
            ) 
        return self._runSQLQuery(query, data)

    def addShopingListOwner(self, user_ID: int, list_ID: int) -> bool: #NOTE Fix PascalCase on tale name: List_Owner
        data = [str(user_ID), str(list_ID)]
        query = self._createInsertSQLQuery(
            "List_Owner",
            "User_ID, List_ID",
            data
            ) 
        return self._runSQLQuery(query, data)

    def addShopingListItem(self, list_ID: int, product_ID: int, amount: int) -> bool: #NOTE Fix PascalCase on List_Items.List_ID
        data = [str(list_ID), str(product_ID), str(amount)]
        query = self._createInsertSQLQuery(
            "List_Items",
            "List_ID, Product_ID, Amount",
            data
            ) 
        return self._runSQLQuery(query, data)


    def loginValidation(self, email: str, password: str) -> bool:
        query = f"SELECT Password FROM Register WHERE email == '{email}'"
        print(f"query is: {query}")
        try:
            res = self.cursor.execute(query)
            res = res.fetchone()
            print(f"Result from query: {res}")
            if (res != None):
                res = res[0]
            else: 
                res = ""
        except sqlite3.Error as er:
            print("\nCould not run querry: " + query)
            print('\tSQLite error: %s' % (' '.join(er.args)))
            return False
        print(f"Password from database is {res}, input is {password}")
        if (res == password): 
            return True 
        else:
            return False

    def addFavoriteStore(self, user_ID: int, store_ID: int) -> bool:
        data = [str(user_ID), str(store_ID)]
        query = self._createInsertSQLQuery(
            "Favourite_Store",
            "User_ID, Store_ID",
            data
            ) 
        return self._runSQLQuery(query, data)

    def getProductDataForAdmin(self):
        query = "SELECT Product_Name, Price_num, Store_Name, Product_ID FROM Product JOIN Store USING (Store_ID)"
        result = self._runSQLQueryWithResults(query)
        return result

    def getUserDataForAdmin(self):
        query = "SELECT User_id, Email, Password, Mobile_Number, Date_of_Birth, City, Name FROM Register"
        result = self._runSQLQueryWithResults(query)
        return result
    
    def getStoreID(self, store_name: str) -> int:
        result = self.cursor.execute(f"SELECT Store_ID FROM Store WHERE Store_Name == '{store_name}'").fetchone()
        if result is None: return -1
        else: return result[0]

    def removeProduct(self, id):
        query = f"DELETE FROM Product WHERE Product_ID == '?'"
        data = [str(id)]
        print(f"TODO: Handle exceptions in removeProduct")
        try:
            self.cursor.execute(query, data)
        except sqlite3.Error:
            print("Error in removing product: TODO: Handle error in removeProduct")

    def searchProduct(self, search_term: str):
        query = f"""SELECT Product_Name, Price_num, Store_Name, Product_ID 
            FROM Product JOIN Store USING (Store_ID)
            WHERE Product_Name LIKE '%{search_term}%' 
            OR Price_num LIKE '%{search_term}%' 
            OR Store_Name LIKE '%{search_term}%' 
        """
        res = self.cursor.execute(query)
        return res.fetchall()

    def searchUser(self, search_term: str):
        query = f"""SELECT User_id, Email, Password, Mobile_Number, Date_of_Birth, City, Name 
            FROM Register
            WHERE User_id LIKE '%{search_term}%' 
            OR Email LIKE '%{search_term}%' 
            OR Password LIKE '%{search_term}%' 
            OR Mobile_Number LIKE '%{search_term}%' 
            OR Date_of_Birth LIKE '%{search_term}%' 
            OR City LIKE '%{search_term}%' 
            OR Name LIKE '%{search_term}%' 
        """
        res = self.cursor.execute(query)
        return res.fetchall()

    def getProductCategory(self, category_terms: list[str]):
        if len(category_terms) != 0:
            query = f"""SELECT Product_Name, Price, Store_Name, Product_ID 
                FROM Product JOIN Store USING (Store_ID)
                WHERE REGEXP('{category_terms.pop(0)}', Product_Name)
            """
            for regex in category_terms:
                query = f"{query} or REGEXP('{regex}', lower(Product_Name)  )"
            try:
                return self.cursor.execute(query).fetchall()
            except sqlite3.Error as er:
                print("\nCould not run query: " + query)
                print('\tSQLite error: %s' % (' '.join(er.args)))
                return []
        else:
            return []   

    def getAllProductsWithCategories(self, category_list: list) -> list:
        if len(category_list) != 0:
            query = f"""SELECT Product_Name, Price_num, Store_Name, Product_ID, Store_Name, URL 
                FROM Product JOIN Store USING (Store_ID) WHERE
            """
            for category in category_list:
                for reg in category[1]:
                    if query[-1] == ')':
                        query = f"{query} OR"
                    query = f"{query} REGEXP('{reg}', lower(Product_Name))"
            #TODO: sort by: 
            #    "Price_num" FLOAT,
            #    "Price_kg" FLOAT,
            #    "Price_l" FLOAT,

            
            return self.cursor.execute(query).fetchall()
        else:
            return []   

    def getProductWithoutCategory(self, category_list: list):
        if len(category_list) != 0:
            query_category = f"SELECT Product_ID FROM Product JOIN Store USING (Store_ID) WHERE"
            for category in category_list:
                for reg in category[1]:
                    if query_category[-1] == ')':
                        query_category = f"{query_category} OR"
                    query_category = f"{query_category} REGEXP('{reg}', Product_Name)"
        else: 
            query_category = "SELECT Product_ID FROM Product JOIN Store USING (Store_ID) WHERE Product_Name == ";""
        query_no_category = f"""SELECT Product_Name, Price, Store_Name, Product_ID, URL 
            FROM Product JOIN Store USING (Store_ID) WHERE Product_ID NOT IN ({query_category})"""
        return self.cursor.execute(query_no_category).fetchall()
       
       
        
        

    def getProductString(self, values: list):
        print("(***WARNING***)Function is no longer supported")
        temp = f"""<h1>This method is no longer suposed to be used!</h1>
                    <tr>
                            <td>{values[0]}</td>
                            <td>{values[1]}</td>
                            <td>{values[2]}</td>
                            <td>
                                <ul class="">
                                    <li class="list-inline-item">
                                        <button class="btn btn-success btn-sm rounded-0" type="button" data-toggle="tooltip" data-placement="top" title="Edit"><i class="fa fa-edit"></i>Edit</button>
                                    </li>
                                    <li class="list-inline-item">
                                        <button class="btn btn-danger btn-sm rounded-0" type="button" data-toggle="tooltip" data-placement="top" title="Delete"><i class="fa fa-trash">Delete</i></button>
                                    </li>
                                </ul>
                            </td>
                            
                        </tr>"""
        return temp
    
    
    #Save all new changes to the database. 
    def commitToDatabase(self):
        self.connection.commit()
     
    def close(self):
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
        for i in range(input_nr): self.addProductToDatabase(name = "Product"+str(i), store="ICA", price="10"+str(i), category=0, url = "")
        for i in range(input_nr): self.addFavoriteProduct(user_ID=i, product_ID=i)
        for i in range(input_nr): self.addShopingList(list_name="List" + str(i))
        for i in range(input_nr): self.addShopingListOwner(user_ID=i,list_ID=i)
        for i in range(input_nr): self.addShopingListItem(list_ID=i, product_ID=i, amount=i)
        for i in range(input_nr): self.addFavoriteStore(user_ID=i, store_ID=min(i+1, 4))


    def dropAllData(self, run: bool = False): #Använd endast för att rensa databasen vid testning
        if (not run): result = input("\n\nVARNING!\nÄr du säker på att du tömma databasen? y/n\n")
        else: result = 'y'
        if (result == 'y'):
            print("Raderar all data...")
            self.fallableExecute("DELETE FROM List_Items WHERE '1' == '1'")
            self.fallableExecute("DELETE FROM List_Owner WHERE '1' == '1'")
            self.fallableExecute("DELETE FROM List WHERE '1' == '1'")
            self.fallableExecute("DELETE FROM Favourite_Products WHERE '1' == '1'")
            self.fallableExecute("DELETE FROM Product WHERE '1' == '1'")
            self.fallableExecute("DELETE FROM Register WHERE '1' == '1'")
            self.fallableExecute("DELETE FROM Store WHERE '1' == '1'")
            self.fallableExecute("DELETE FROM Category WHERE '1' == '1'")
            self.fallableExecute("DELETE FROM List_Items WHERE '1' == '1'")

            from product import Store
            for store in Store:
                self.addStoreToDatabase(ID = store.value, name = store.name)
        else:
            print("Avbryter")
    
    # try and execute, ignore errors
    def fallableExecute(self, sql_string: str):
        try:
            self.cursor.execute(sql_string)
            return True
        except sqlite3.OperationalError:
            print("sqlite3.OperationalError for:", sql_string)
            return False

    def recreateDatabase(self):
        self.dropAllData(run = True)
        self.fallableExecute("DROP TABLE Category")
        self.fallableExecute("DROP TABLE Favourite_Products")
        self.fallableExecute("DROP TABLE List")
        self.fallableExecute("DROP TABLE List_Items")
        self.fallableExecute("DROP TABLE List_owner")
        self.fallableExecute("DROP TABLE Login")
        self.fallableExecute("DROP TABLE Product")
        self.fallableExecute("DROP TABLE Register")
        self.fallableExecute("DROP TABLE Store")
        self.fallableExecute("DROP TABLE Favourite_Store")
        
        self.fallableExecute("""CREATE TABLE "Favourite_Products" (
                "User_ID" INTEGER,
                "Product_ID" INTEGER,
                FOREIGN KEY("Product_ID") REFERENCES "Product"("Product_ID"),
                FOREIGN KEY("User_ID") REFERENCES "Register"("User_ID"),
                PRIMARY KEY("User_ID","Product_ID")
            )
        """)
        self.fallableExecute("""CREATE TABLE "List" (
                "List_name" INTEGER NOT NULL,
                "List_ID" INTEGER NOT NULL,
                PRIMARY KEY("List_ID")
            )
        """)
        self.fallableExecute("""CREATE TABLE "List_Items" (
                "List_ID" INTEGER NOT NULL,
                "Product_ID" INTEGER NOT NULL,
                "Amount" INTEGER NOT NULL,
                PRIMARY KEY("List_ID","Product_ID")
            )
        """)
        self.fallableExecute("""CREATE TABLE "List_owner" (
                "User_ID" INTEGER NOT NULL,
                "List_ID" INTEGER NOT NULL,
                PRIMARY KEY("User_ID","List_ID"),
                FOREIGN KEY("List_ID") REFERENCES "List"("List_ID")
            )
        """)
        self.fallableExecute("""CREATE TABLE "Login" (
                "Day" INTEGER,
                "Time" INTEGER,
                "User_ID" INTEGER,
                PRIMARY KEY("User_ID"),
                FOREIGN KEY("User_ID") REFERENCES "Register"("User_ID")
            )
        """)
        self.fallableExecute("""CREATE TABLE "Product" (
                "Category_ID" INTEGER,
                "Product_ID" INTEGER,
                "Product_Name" TEXT,
                "Store_ID" INTEGER,
                "Price" TEXT,
                "Price_num" FLOAT,
                "Price_kg" FLOAT,
                "Price_l" FLOAT,
                "Amount_kg" FLOAT,
                "Amount_l" FLOAT,
                "URL" TEXT,
                PRIMARY KEY("Product_ID"),
                FOREIGN KEY("Category_ID") REFERENCES "Category"("Category_ID"),
                FOREIGN KEY("Store_ID") REFERENCES "Store"("Store_ID")
            )
        """)
        self.fallableExecute("""CREATE TABLE "Category" (
            "Category_ID" INTEGER,
            "Category_Name" INTEGER NOT NULL UNIQUE,
            PRIMARY KEY("Category_ID")
            )""")
        self.fallableExecute("""CREATE TABLE "Register" (
                "User_ID" INTEGER,
                "Email" TEXT NOT NULL UNIQUE,
                "Password" TEXT NOT NULL,
                "Mobile_Number" INTEGER UNIQUE,
                "Date_of_Birth" INTEGER,
                "City" TEXT,
                "Country" TEXT,
                "Logged_in_Status" INTEGER,
                "Name" TEXT,
                PRIMARY KEY("User_ID")
            )
        """)
        self.fallableExecute("""CREATE TABLE "Store" (
                "Store_ID" INTEGER,
                "Store_Name" TEXT,
                PRIMARY KEY("Store_ID")
            )
        """)

        self.fallableExecute("""CREATE TABLE "Favourite_Store" (
                "Store_ID" INTEGER,
                "User_ID" INTEGER,
                FOREIGN KEY("User_ID") REFERENCES "Register"("User_ID"),
                FOREIGN KEY("Store_ID") REFERENCES "Store"("Store_ID"),
                PRIMARY KEY("Store_ID","User_ID")
            )
        """)
        from product import Store
        for store in Store:
            self.addStoreToDatabase(ID = store.value, name = store.name)
        self.commitToDatabase()
