# Product from the perspective of the web scraper
# This WILL have to be parsed later to provide useful info
# TODO: images

from enum import Enum, unique
@unique
class Store(Enum):
    LIDL = 1
    COOP = 2
    ICA = 3
    def __str__(self):
        match self:
            case self.LIDL:
                return "Lidl"
            case self.COOP:
                return "Coop"
            case self.ICA:
                return "Ica"

class Product:
    #TODO: filter valid products
    def __init__(self, name: str, price: str, store: Store, description: str = "", category: str = ""):
        #assert name != ""
        self.price = price
        self.name = name
        self.store = store
        self.description = description
        self.category = category
    def __str__(self):
        return self.name + ": (" + self.price + ") at " + str(self.store) + " " + self.description
    def __repr__(self):
        return self.name + ": (" + self.price + ") at " + str(self.store) + " " + self.description

