from server import Database


database = Database()
database.fillDatabase()
#database.droppAllData()
database.commitToDatabase()
database.Close()
