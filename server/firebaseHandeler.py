import firebase_admin
from firebase_admin import credentials, auth, firestore

class firebaseHandeler():

    def __init__(self):
        cred = credentials.Certificate("key.json")
        firebase_admin.initialize_app(cred)

    def getUserData(self):
        usrs = firebase_admin.auth.list_users()
        result = []
        for usr in usrs.iterate_all():
            temp = [
                usr.uid,                            #id
                usr.email,                          #Email
                usr.password_hash[0:20] + "...",   #pass
                usr.phone_number,                   #number
                "No date avalible",                 #date
                "No city avalible",                 #City
                usr.display_name                    #name
            ]
            result.append(temp)
        return result

    def printUsers(self):
        usrs = firebase_admin.auth.list_users()
        for usr in usrs.iterate_all():
            print(f"User mail is: {usr.email}")

    def firestore(self):
        temp = firebase_admin.firestore.client()
        print(temp)

if __name__ == "__main__":
    db = firebaseHandeler()
    db.printUsers()
    db.getUserData()
    db.firestore()
