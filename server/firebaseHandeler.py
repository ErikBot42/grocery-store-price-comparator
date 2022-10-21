import requests
from socket import MsgFlag
import telnetlib
from urllib import response
import firebase_admin
from firebase_admin import credentials, auth, firestore
import json

class userData():

    def __init__(self, email,  password, telephone, name, city,ica, coop, lidl, willys, uid=None) -> None:
        self.uid = uid
        self.email = email
        self.password = password
        self.telephone = telephone
        self.name = name
        self.city = city
        self.ica = ica
        self.coop = coop
        self.lidl = lidl
        self.willys = willys


class firebaseHandeler():

    def __init__(self):
        self.usr_collection = u'user_profile'
        try:
            cred = credentials.Certificate("server/key.json")
        except:
            print("#####Could not authenticate to firestore\n")
            exit()
        try:
            self.default_app = firebase_admin.initialize_app(cred)
        except:
            print("#####Could not initialise App\n")
            exit()
        try:
            self.db = firestore.client()
        except:
            print("#####Could not start firebase client\n")

    def _getUserObject(self, uid, data) -> userData:
        return userData(
            uid=uid,
            email=data["Email"] if "Email" in data else None,
            name=data["Name"] if "Name" in data else None,
            password=data["Password"] if "Password" in data else None,
            telephone=data["Telephone"] if "Telephone" in data else None,
            city=data["City"] if "City" in data else None,
            ica = data["ICA"] if "ICA" in data else False,
            coop = data["COOP"] if "COOP" in data else False,
            lidl = data["LIDL"] if "LIDL" in data else False,
            willys = data["Willys"] if "Willys" in data else False,
        )

    def getUserData(self) -> list[userData]:
        data = []
        usrs = self.db.collection(self.usr_collection).get()
        for usr in usrs:
            data.append(self._getUserObject(usr.id, usr.to_dict()))
        return data
    
    def addUser(self, usr: userData):
        usr_data = {
            u'Email': usr.email,
            u'Name': usr.name,
            u'Password': usr.password,
            u'Telephone': usr.telephone,
            u'city': usr.city,
            u'ICA': usr.ica,
            u'COOP': usr.coop,
            u'LIDL': usr.lidl,
            u'Willys': usr.willys
        }
        self.db.collection(self.usr_collection).add(usr_data)

    def removeUser(self, uid):
        self.db.collection(self.usr_collection).document(uid).delete()

    def getUserSearch(self, search_str: str):
        print(f"searching for {search_str}")
        docs = self.db.collection(self.usr_collection).where(u'Name', u'==', search_str).stream()
        data = []
        for doc in docs:
            data.append(self._getUserObject(doc.id, doc.to_dict()))
        return data

    def sendNotification():
        serverToken = 'BMy_3_VpR0EKsX_Edz6sYPu3dHWTDoX_UU-hGIq_B7JSvlCa7OZ4_82zSLtOn5GRd4zPDcjfbjs2qLxStKPpQi0'
        deviceToken = 'dkbWvKJ1yHgEQXTq4rT5'
        headers = {
                'Content-Type': 'application/json',
                'Authorization': 'key=' + serverToken,
            }
        body = {
                'notification': {'title': 'Sending push form python script',
                                    'body': 'New Message'
                                    },
                'to':
                    deviceToken,
                'priority': 'high',
                #   'data': dataPayLoad,
                }
        response = requests.post("https://fcm.googleapis.com/fcm/send",headers = headers, data=json.dumps(body))
        print(response.status_code)


if __name__ == "__main__":
    fire_db = firebaseHandeler()
    fire_db.sendPush("Hi", "This is a message from the flask server")
