import requests
import re
import product
from bs4 import BeautifulSoup

#does this string contain amount, eg "Ca 200g", or not, eg "Klass 1"
def is_amount(amount_str: str) -> bool:
    #TODO: implement
    return True

#remove surrounding whitespace and empty elements
def remove_whitespace_elements(original: list[str]) -> list[str]:
    return [el.strip() for el in original if len(el.strip()) != 0]



def filter_price_string(price_string: str) -> str:
    return re.sub(r'[^\d.,]*', '', price_string.strip())

# wrapper around http request to prevent errors
def safe_request(http_str: str) -> bytes:
    try:
        r = requests.get(http_str)
        return r.content
    except requests.exceptions.ConnectionError:
        return bytes()

# make request from http address and return soup object
def address_to_soup(address: str) -> BeautifulSoup:
    content = safe_request(address)
    return BeautifulSoup(content, 'html.parser')

# Parse lidl offers from html
def lidl_parse(soup: BeautifulSoup) -> list[product.Product]:
    s = soup.find_all('div', class_ = "nuc-a-flex-item nuc-a-flex-item--width-6 nuc-a-flex-item--width-4@sm")
    product_list: list[product.Product] = []
    for el in s:
        #TODO: Image link
        product_price_el = el.find('span', class_ = "lidl-m-pricebox__price");
        product_name_el = el.find('h3', class_ = "ret-o-card__headline");
        price_str = filter_price_string(product_price_el.string)
        name_str = product_name_el.string.strip();
        print("name:", name_str)
        print("price:", price_str)
        print()
        product_list.append(product.Product(name_str, float(price_str), product.Store.LIDL))
    return product_list

#<div class="ItemTeaser-content">
#<div class="Grid-cell u-size1of2 u-xsm-size1of2 u-md-size1of4 u-lg-size1of6 js-drOffer js-offerItem" data-eag-id="12960" data-store-id="165420">
#<article class="ItemTeaser" itemscope="" itemtype="http://schema.org/Product">
def coop_parse(soup: BeautifulSoup) -> list[product.Product]:
    #s = soup.find_all('div', class_ = "ItemTeaser-info")
    s = soup.find_all('article', class_ = "ItemTeaser")
    for el in s:
        #print(el.prettify())
        #assert False
        print("image link:", el.find('img', class_ = "u-posAbsoluteCenter").get('src'))
        price_raw = remove_whitespace_elements(list(el.find('span', class_ = "Splash-content").strings))
        #['3 för', '79:-']
        #['50%', 'rabatt']
        #['49', '90', '/st']
        print("price (sometimes):", price_raw)
        print("heading:", el.find('h3', class_ = "ItemTeaser-heading").string)
        #print("brand:", el.find('span', class_ = "ItemTeaser-brand").string)
        description_list = list(el.find('p', class_ = "ItemTeaser-description").strings);
        description_list = remove_whitespace_elements(description_list)
        brand: str = "" 
        description: str = ""
        amount: str = "" 
        try:
            brand = description_list[0]
            description_split = description_list[1].split(".")
            maybe_amount: str = description_split[0]
            if is_amount(maybe_amount):
                amount = maybe_amount
                description_split.pop(0)
            description = ".".join(description_split)
        except IndexError:
            pass

        print("brand:", brand)
        print("description:", description)
        print("amount:", amount)
        print()
    


#
soup = address_to_soup('https://www.coop.se/butiker-erbjudanden/coop/coop-kronoparken/')
coop_parse(soup)
#print(coop_parse(soup))
soup = address_to_soup('https://www.lidl.se/veckans-erbjudanden')
lidl_parse(soup)
#print(lidl_parse(soup))

