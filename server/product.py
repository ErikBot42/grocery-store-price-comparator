# Product from the perspective of the web scraper
# This WILL have to be parsed later to provide useful info
# TODO: images
# from server import Database

from enum import Enum, unique
@unique
class Store(Enum):
    LIDL = 1
    COOP = 2
    ICA = 3
    WILLYS = 4
    def __str__(self):
        match self:
            case self.LIDL:
                return "LIDL"
            case self.COOP:
                return "COOP"
            case self.ICA:
                return "ICA"
            case self.WILLYS:
                return "WILLYS"

@unique
class Units(Enum):
    KG = 1 #mass
    L = 2 #volume
    NUMBER = 3 #number
    

def price(kr: int, öre: int) -> int:
    return 100*kr + öre

class ExtractedInfo:

    def __init__(self):
        self.price = ""
        pass
    def try_read(self, string: str):

        #price represented as "öre"

        import ply.lex as lex
        import re

        # List of token names.   This is always required
        tokens = (
                'NUMBER',
                'PRICE',
                'PER',
                'FOR',
                'UNIT',
                'PACK',
                'TO',
                'ERROR',
                )

        # Regular expression rules for simple tokens
        #t_TIMES   = r'\*'
        #t_DIVIDE  = r'/'
        #t_LPAREN  = r'\('
        #t_RPAREN  = r'\)'

        def to_unit(s: str):
            match s:
                case "st"|"ask"|"förp":
                    return (Units.NUMBER, 1)
                case "kg":
                    return (Units.KG, 1)
                case "g":
                    return (Units.KG, 1/1000.0)
                case "l"|"lit":
                    return (Units.L, 1)
                case "ml":
                    return (Units.L, 1/1000.0)
                case "cl":
                    return (Units.L, 1/100.0)
                case _:
                    print("'",s,"'", sep="")
                    assert False
        
        # denotes a range of values
        def t_TO(t):
            r'-'
            return t

        def t_FOR(t):
            r'för'
            t.value = ""
            return t
        
        def t_PACK(t):
            r'(\d+-pack)'
            #(\d+×) <- "3×212 ml" fails
            t.value = int(re.search(r"\d+", t.value).group())

            return t 
        
        def t_UNIT(t):
            r'((st)|(ask)|(kg)|(förp)|(g)|(l)|(lit)|(ml)|(cl))'
            #t.value = to_unit(t.value)
            t.value = to_unit(t.value)
            return t


        def t_PER(t):
            r'/((st)|(ask)|(kg)|(förp)|(g)|(l(it)?)|(ml)|(cl))'
            t.value = to_unit(t.value[1:])
            return t


        def t_PRICE(t):
            r'(\d+[ .:]\d\d)|(\d+:-)'
            s = [s for s in re.split(r"[ .:-]+", t.value) if s!=""]
            t.value = price(int(s[0]), int(s[1]) if len(s)>1 else 0)
            assert len(s) <= 2
            return t
        
        def t_NUMBER(t):
            r'\d+'
            t.value = int(t.value)
            return t
        
        
        # Error handling rule
        def t_error(t):
            print("'%s'" % t.value[0])
            t.lexer.skip(1)

        def t_ERROR(t):
            r'.'
            t.value = "'" + t.value + "'"
            return t
            
        # A string containing ignored characters (spaces and tabs)
        t_ignore  = ' '
        
        # Build the lexer
        lexer = lex.lex()

        print() 
        print(string)
        # Give the lexer some input
        lexer.input(string)
        token_list= []
        while True:
            tok = lexer.token()
            if not tok:
                break      # No more input
            token_list+=[tok]

            print(tok.type, ": " , tok.value, sep="")

        print(token_list)

        from itertools import zip_longest

        i = 0
        group_length = 2

        # Incredible: 
        token_iter = iter(token_list)
        token_iter_next = iter(token_list)
        token_iter_next.__next__()
        token_iter_next2 = iter(token_list)
        token_iter_next2.__next__()
        token_iter_next2.__next__()

        for (nxt2, nxt, curr) in zip_longest(token_iter_next2, token_iter_next, token_iter):

    
            if curr.type == "ERROR":
                continue

            


            if nxt.type != "ERROR" and nxt.type != None:
                # 2 token rules here

                print(curr.type, ": '", curr.value, "', ", nxt.type, ": '", nxt.value, "' ", sep = "")
                
                #TODO:
                #if match:
                #    continue

            # single token rules here.
            pass










class Product:
    #TODO: create function that may return product if it's valid
    def __init__(self,
            name: str,
            price: str,
            store: Store,
            description: str = "",
            category: str = "",
            image_url: str = "",
            #product_url: str = "",
            amount: str = "",
            modifier: str = "",
            ):
        self.amount = amount
        self.category = category
        self.description = description
        self.image_url = image_url
        self.modifier = modifier
        self.name = name
        self.price = price
        #self.product_url = product_url
        self.store = store

    def __str__(self):
        return self.name + ": (" + self.price + ") at " + str(self.store) + " " + self.description

    def __repr__(self):
        return self.name + ": (" + self.price + ") at " + str(self.store) + " " + self.description

    def print(self):
        attrs = vars(self)
        print('\n'.join("%s: %s" % item for item in attrs.items()))


