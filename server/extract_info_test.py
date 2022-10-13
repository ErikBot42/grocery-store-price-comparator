import unittest


#https://pypi.org/project/pylexer/



#89 90 /kg
#30:- /kg
#129:- /kg
#179:- /kg


#32.90
#14.90
#24.90
#34.90
#44.90
#54.90
#16.90
#26.90
#17.90
#19.90
#29.90
#39.90
#49.90
#79.90
#5.90

#52 90
#62 90
#23 90
#24 90
#37 90
#47 90
#29 90
#16 90
#49 90
#59 90
#69 90


#100:-
#10:-
#210:-
#20:-
#30:-
#130:-
#40:-
#60:-
#80:-
#90:-
#12:-
#22:-
#24:-
#34:-
#105:-
#15:-
#25:-
#32:-
#52:-
#62:-
#45:-
#55:-
#75:-
#17:-
#28:-
#38:-
#109:-
#119:-
#29:-
#129:-
#229:-
#39:-
#139:-
#239:-
#43:-
#35:-
#49:-
#349:-
#59:-
#159:-
#79:-
#179:-
#99:-
#199:-
#1299:-
#499:-
#599:-
#999:-
#89:-
#42:-

#2 för 42:-
#3 för 43:-
#2 för 25:-
#2 för 35:-
#2 för 55:-
#2 för 39:-
#4 för 89:-
#2 för 40:-



#32 90 /st
#23 90 /st
#24 90 /st
#34 90 /st
#16 90 /st
#17 90 /st
#27 90 /st
#29 90 /st
#39 90 /st
#49 90 /st
#59 90 /st
#69 90 /st

#29 90 /förp

#30:- /förp
#69:- /st
#79:- /st
#20:- /st

#21 90 /st
#19 90 /ask

# very difficult case
#Köp 4 betala för 3

#20%  rabatt
#50% rabatt
#25%  rabatt


from product import ExtractedInfo
from product import Product 


class TestProducts(unittest.TestCase):
    #def test_extract_infer(self):
    #    ex = ExtractedInfo()
    #    ex.try_read("89 90 /kg 500 g")
    #    print(ex.price)
    #    print(ex.price_kg)
    #    print(ex.price_l)
    #    assert ex.price == 89.90*0.5
    def test_price_extract(self):
        cases = [
                #input,                                                                                                   price,         price_l, price_kg
                ("89 90 /kg",                                                                                              None,         None,       89.90, ),
                ("30:- /kg",                                                                                               None,         None,          30, ),
                ("129:- /kg",                                                                                              None,         None,         129, ),
                ("32.90",                                                                                                 32.90,         None,        None, ),
                ("52 90",                                                                                                 52.90,         None,        None, ),
                ("100:-",                                                                                                   100,         None,        None, ),
                #("2 för 42:-",      None,     None,        None,),                   
                ("32 90 /st",                                                                                             32.90,         None,        None, ),
                ("29 90 /förp",                                                                                           29.90,         None,        None, ),
                ("30:- /ask",                                                                                                30,         None,        None, ),
                ("30:00",                                                                                                    30,         None,        None, ),
                ("69:- /st",                                                                                                 69,         None,        None, ),
                ("Göl. 375 g. Kyld. Välj mellan olika sorter. Jfr-pris 106:40/kg.",                                0.375*106.40,         None,      106.40, ),
                ("Italien. 500 g. Klass 1. Kärnfria. Jfr-pris 39:80/kg.",                                             0.5*39.80,         None,       39.80, ),
                ("OLW. 200-275 g. Flera olika sorter. Max 1 köp/hushåll",                                                  None,         None,        None, ),
                ("Hälsans kök. 180-320 g. Flera olika sorter. Gäller ej Sojafärs, Plant-Based burger. Max 1 köp/hushåll",  None,         None,        None, ),
                ]
        #ex.try_read("")
        #ex.try_read("")
        #ex.try_read("")
        #ex.try_read("")
        for (string, price, price_lit, price_kg) in cases:
            ex = ExtractedInfo()
            ex.try_read(string)
            assert ex.price == price,                  (string, "price not matching",    ex.price, price)
            assert ex.price_l == price_lit,    (string, "price/l not matching",  ex.price_per_litre, price_lit)
            assert ex.price_kg== price_kg,        (string, "price/kg not matching", ex.price_per_kg, price_kg)

#89 90 /kg
#30:- /kg
#129:- /kg
#32.90
#52 90
#100:-
#2 för 42:-
#32 90 /st
#29 90 /förp
#30:- /förp
#69:- /st
#21 90 /st
#19 90 /ask
#Köp 4 betala för 3
#20%  rabatt
        #ex.try_read("89 90 /kg")
        #ex.try_read("30:- /kg")
        #ex.try_read("129:- /kg")
        #ex.try_read("32.90")
        #ex.try_read("52 90")
        #ex.try_read("100:-")
        #ex.try_read("2 för 42:-")
        #ex.try_read("32 90 /st")
        #ex.try_read("29 90 /förp")
        #ex.try_read("30:- /förp")
        #ex.try_read("69:- /st")
        #ex.try_read("21 90 /st")
        #ex.try_read("19 90 /ask")
        #ex.try_read("Köp 4 betala för 3")
        #ex.try_read("20%  rabatt")
        #ex.try_read("3 för 90:-")
        #ex.try_read("17 90 /st")
        #ex.try_read("150 g/förp.")
        #ex.try_read("1 kg")
        #ex.try_read("1 l")
        #ex.try_read("200 ml")
        #ex.try_read("200 ml–500 ml, 30 st")
        #ex.try_read("20-pack")
        #ex.try_read("250 g")
        #ex.try_read("2 l")
        #ex.try_read("2-pack. Victor® Clean-Kill* musfälla, är en fälla där du slipper se och röra musen och som är lätt att beta och ställa in. Ord pris 129:-/st")
        #ex.try_read("3×212 ml")
        #ex.try_read("340 g")
        #ex.try_read("350 blad")
        #ex.try_read("350 g")
        #ex.try_read("3×8 ml")
        #ex.try_read("3 Vänners Glass. 500 ml. Välj mellan olika sorter. Jfr-pris 99:80/lit.")
        #ex.try_read("400 g")
        #ex.try_read("420 g/förp.")
        #ex.try_read("500 g")
        #ex.try_read("500 g/förp.")
        #ex.try_read("500 ml. Välj mellan vit, lila, grön, grå och svart. Ord. pris 99:-/st.")
        #ex.try_read("5 l")
        #ex.try_read("600 ml")
        #ex.try_read("64-pack")
        #ex.try_read("700 g, 720 g")
        #ex.try_read("8-pack")
        #ex.try_read("Ajax. 750 ml, 1500 ml. Flera olika sorter")
        #ex.try_read("ALF. Välj mellan flera olika. Ord pris 179:-/st")
        #ex.try_read("Algomin. 8 kg")
        #ex.try_read("Allerum. 375-400 g. Lagrad 12 månader. Välj mellan Präst, Grevé, Herrgård och Rike. Jfr-pris 149:75/kg.")
        #ex.try_read("Allerum. Ca 700 g. 17-35%. 12-18 månader")
        #ex.try_read("Almondy/Oreo. 400-450 g. Flera olika sorter. Max 2 köp/hushåll")
        #ex.try_read("Änglamark. 1 kg. Jfr-pris 39:90/kg.")
        #ex.try_read("Arla. 1 kg. Original, ekologisk, laktosfri. Flera olika sorter")
        #ex.try_read("Arla. 1 liter. Lätt, mellan, standard. Original, ekologisk")
        #ex.try_read("Arla. 2 dl. Naturell. Gäller även laktosfri. Max 1 köp/hushåll")
        #ex.try_read("Arla. 500 g. Gäller ej ekologiskt. Max 2 köp/hushåll")
        #ex.try_read("Arla. Ca 1100-2200 g. I bit. 17-26%. Jfr-pris 89:90/kg.")
        #ex.try_read("Barebells. 55 g, 330 ml")
        #ex.try_read("Barilla. 1000 g. Original. Max 1 köp/hushåll")
        #ex.try_read("Ca 2 kg")
        #ex.try_read("Ca 925 g")
        #ex.try_read("Castello. 180-200 g. Flera olika sorter")
        #ex.try_read("Cloetta. 150-200 g. Välj mellan olika sorter. Jfr-pris 97:50/kg.")
        #ex.try_read("Coop. 125 g. Jfr-pris 100:-/kg.")
        #ex.try_read("Coop. 1 liter. Jfr-pris 14:33/lit.")
        #ex.try_read("Coop. 200 g. Välj mellan ljus och mörk. Jfr-pris 97:50/kg.")
        #ex.try_read("Coop. 250 g. Frysta. Jfr-pris 80:-/kg.")
        #ex.try_read("Coop. 250 g. Jfr-pris 99:60/st.")
        #ex.try_read("Coop. 500 g. Jfr-pris 40:-/kg.")
        #ex.try_read("Coop. 550 g. Benfri. Skivad. Noga utvalt från Svenska gårdar. Jfr-pris 127:09/kg.")
        #ex.try_read("Dafgårds. 240 g. Frysta. Välj mellan olika sorter. Jfr-pris 114:58/kg.")
        #ex.try_read("DMTech. BHL-4300. Kraftfull bluetooth högtalare med LED-lampor i skiftande färger. Anslutning för både mikrofon och gitarr. Dubbla basar för maxat ljud. Upp till 27 timmars batteritid. Enkel laddning med den medföljande strömadaptern. Mått : 35x32x69 cm. Ord pris")
        #ex.try_read("En elstöt dödar en mus på 5 sekunder. Smala fack håller musen på plats för 100% dödlighet - inga rymningar! Lätt att använda * bara att agna, slå på och tömma. Ord pris 399:-/st")
        #ex.try_read("Esska. Gäller alla nappar och napphållare från Esska. Ord pris 31:90-139:-/st")
        #ex.try_read("Estrella. 150-275 g. Välj mellan olika sorter. Jfr-pris 72:73/kg.")
        #ex.try_read("Estrella. 150-275 g. Välj mellan olika sorter. Jfr-pris 72:73/kg.")
        #ex.try_read("Fast och utfällbar ramp med metallfälgar och punkteringsfria däck. Höjd 118 cm. Bredd 55 cm. Djup 57 cm. Ord pris 799:-/st")
        #ex.try_read("Fazer. 300-350 g")
        #ex.try_read("Felix. 1000 g. Max 2 köp/hushåll")
        #ex.try_read("Felix. 210-220 g. Flera olika sorter. Gäller ej västerbottenpaj")
        #ex.try_read("Felix. 210-220 g. Flera olika sorter. Gäller ej västerbottenpaj. Max 1 köp/hushåll")
        #ex.try_read("Felix. 210-220 g. Flera olika sorter. Gäller ej västerbottenpaj. Max 1 köp/hushåll")
        #ex.try_read("Felix. 470-475 g. Gäller ej eko")
        #ex.try_read("Felix. 470-475 g. Gäller ej eko. Max 1 köp/hushåll")
        #ex.try_read("Felix. 470-475 g. Gäller ej eko. Max 1 köp/hushåll")
        #ex.try_read("Ferrari, Toms. 75-130 g. Gäller Ferrari godispåse 110-130 g, Pingvinstång 3-pack 75-87 g")
        #ex.try_read("Findus. 340-400 g. Dagens. Flera olika sorter")
        #ex.try_read("Fire Up. 100-pack. FSC märkta tändkuber i kartong, består av komprimerad furuspån och vegetabilisk olja. Brinntid cirka 10 minuter. Innehåller inga giftiga ämnen")
        #ex.try_read("Fontana. 150-200 g. Välj mellan olika sorter. Jfr-pris 140:-/kg.")
        #ex.try_read("Gäller Bamselekasker, spel och pussel. Flera olika i butik. Ord pris 9:90-349:-/st")
        #ex.try_read("Garnier, Ambre solaire. 1-pack. Gäller ej Fresh tissue mask")
        #ex.try_read("Garnier Olia")
        #ex.try_read("Garnier Skin Naturals, Active. 50-400 ml. Gäller ej wipes eller ansiktsmasker")
        #ex.try_read("GB Glace, GB Signature. 750-1000 ml. Flera olika sorter. Gäller ej Choice")
        #ex.try_read("Gevalia. 200 g. Välj mellan olika sorter. Jfr-pris 299:50/kg.")
        #ex.try_read("Gevalia. 400-500 g. Flera olika sorter")
        #ex.try_read("God Morgon. 1 liter. Välj mellan apelsin, apelsin/rödgrapefrukt, tropical, och äppel. Jfr-pris 16:90/lit.")
        #ex.try_read("Göl. 375 g. Kyld. Välj mellan olika sorter. Jfr-pris 106:40/kg.")
        #ex.try_read("Gooh. 370-400 g. Gäller ej Pasta pomodoro")
        #ex.try_read("Göteborgs. 190-225 g. Flera olika sorter")
        #ex.try_read("Hälsans kök. 180-320 g. Flera olika sorter. Gäller ej Sojafärs, Plant-Based burger. Max 1 köp/hushåll")
        #ex.try_read("Hemtex 24h. Gäller utvalda handdukar. Ord pris 39:90-159:-/st")
        #ex.try_read("Höjd: 130 cm")
        #ex.try_read("Höjd: 30 cm")
        #ex.try_read("Höjd: 35 cm")
        #ex.try_read("ICA. 10 LED. Utomhusgodkänd. Ord pris 249:-/st")
        #ex.try_read("ICA. 20-pack. Grå. 50% återvunnen plast. Ord pris 23:90/frp")
        #ex.try_read("ICA. 250 g. Gäller naturell. Max 1 köp/hushåll")
        #ex.try_read("ICA. 300 g. Kyld")
        #ex.try_read("ICA. 3 kg. Bildar på kort tid en fin och slitstark gräsmatta. Fröåtgång: 1 kg/35 m2 . Ord pris 229:-/st. Finns även 1 kg för 89:-/st")
        #ex.try_read("ICA. 65 g")
        #ex.try_read("ICA. Filament. Välj mellan kron, klot eller normal. E14-27 sockel. Ljusflöde 250-470 lumen. Ej dimbar. Ord pris 34:90-39:90/st")
        #ex.try_read("ICA. Filament. Välj mellan kron, klot eller normal. E14-27 sockel. Ljusflöde 470-806 lumen. Dimbar. Ord pris 49:90-69:90/st")
        #ex.try_read("ICA Garden. 40 liter. Svenskproducerad. Gäller ej KRAV-märkt plantjord. Ord pris 34:90/st.")
        #ex.try_read("ICA Garden. Ord pris 29:90-59:90:-/st")
        #ex.try_read("ICA. Höjd 10 cm. Vit. Ord pris 19:90/st")
        #ex.try_read("ICA I love eco. Italien. 500 g. Klass 1")
        #ex.try_read("ICA I Love Eco. Ursprung Sverige. Ca 700 g. Av benfri kotlett")
        #ex.try_read("ICA. Marocko. 500 g. Klass 1")
        #ex.try_read("ICA. Nederländerna. 200 g. Klass 1")
        #ex.try_read("ICA. Sverige. 1,2 kg. Klass 1")
        #ex.try_read("ICA. Sydafrika. 175-200 g. Klass 1")
        #ex.try_read("ICA. Ursprung Sverige. Ca 1000 g. Mörad. Av benfri kotlett. Max 2 köp/hushåll")
        #ex.try_read("ICA. Ursprung Sverige. Ca 300 g. Av nöt")
        #ex.try_read("ICA. Ursprung Sverige. Ca 450 g. Av nötrulle")
        #ex.try_read("ICA. Ursprung Sverige. Ca 700 g. Skivad")
        #ex.try_read("Ingelsta kalkon. Ursprung Sverige. 450-480 g")
        #ex.try_read("Isadora")
        #ex.try_read("IsaDora")
        #ex.try_read("Italien. 500 g. Klass 1. Kärnfria. Jfr-pris 39:80/kg.")
        #ex.try_read("Italien. 500 g. Klass 1. Kärnfria. Jfr-pris 39:80/kg.")
        #ex.try_read("Italien/Änglamark. Klass 1. Jfr-pris 30:-/kg.")
        #ex.try_read("Järbo. 200g. Finns i flera färger. Gäller Verona garn. Ord pris 109:-/st")
        #ex.try_read("Kalles. 250-300 g. Välj mellan olika sorter. Jfr-pris 99:67/kg.")
        #ex.try_read("Kalles. 250-300 g. Välj mellan olika sorter. Jfr-pris 99:67/kg.")
        #ex.try_read("Kelda. 400 - 500 ml")
        #ex.try_read("Kotivara. 80-120 g. Flera olika sorter")
        #ex.try_read("Kronfågel. Ursprung Sverige. Ca 600-925 g. Naturell")
        #ex.try_read("Kungsörnen. 2 kg. Gäller Vetemjöl av fint kärnvete. Max 1 köp/hushåll")
        #ex.try_read("Kungsörnen. 2 kg. Jfr-pris 10:95/kg.")
        #ex.try_read("Kungsörnen. 750 g. Snabb, gammeldags")
        #ex.try_read("Lambi. 568-832 g. Svanenmärkt. Välj mellan 4-pack hushållspapper och 8-pack toalettpapper. Jfr-pris 39:17/kg.")
        #ex.try_read("Lambi. 568-832 g. Svanenmärkt. Välj mellan 4-pack hushållspapper och 8-pack toalettpapper. Jfr-pris 39:17/kg.")
        #ex.try_read("Libresse. 9-50 pack, 200 ml")
        #ex.try_read("Lindahls. 500 g. Flera olika sorter. Gäller ej laktosfri. Max 1 köp/hushåll")
        #ex.try_read("Lindt. 80-100 g. Flera olika sorter")
        #ex.try_read("Lipton. 100 st. Earl grey, Yellow label. Max 2 köp/hushåll")
        #ex.try_read("Lithells. 750-900 g. Max 2 köp/hushåll")
        #ex.try_read("Lohmanders. 250 ml. Kyld. Välj mellan olika sorter. Jfr-pris 111:60/lit.")
        #ex.try_read("Loka. 150 cl. Max 1 köp/hushåll")
        #ex.try_read("L´oréal Paris")
        #ex.try_read("L´oréal Paris")
        #ex.try_read("Max Factor")
        #ex.try_read("Max Factor")
        #ex.try_read("Maybelline")
        #ex.try_read("Maybelline")
        #ex.try_read("Medium")
        #ex.try_read("mywear. Stl 36-46. Ord.pris 149:-")
        #ex.try_read("Nederländerna. 170-200 g. Röd. Klass 1. Jfr-pris 89:50/kg.")
        #ex.try_read("Njie. 330 ml. Välj mellan olika sorter. Jfr-pris 53:03/lit.")
        #ex.try_read("Norrlands Guld. 6 x 50 cl")
        #ex.try_read("Nutrisse. 1-pack")
        #ex.try_read("Ø60 cm. Transportabelt eldfat. Perfekt till trädgården eller fritidshuset. Tillverkat i målat järn. Levereras med avtagbara ben. Ord pris 599:-/st")
        #ex.try_read("Old el Paso. 145-193 g. Flera olika sorter. Gäller Tortilla Stand'n'Stuff, Bowls, Strips")
        #ex.try_read("Olika sorter")
        #ex.try_read("OLW. 200-275 g. Flera olika sorter")
        #ex.try_read("OLW. 200-275 g. Flera olika sorter")
        #ex.try_read("OLW. 200-275 g. Flera olika sorter. Max 1 köp/hushåll")
        #ex.try_read("Ord pris 159-179:-/st")
        #ex.try_read("Pågen. 320-450 g. Välj mellan Hönökaka och Grötbräck. Jfr-pris 53:11/kg.")
        #ex.try_read("Pågen. 650-700 g. Lantgoda, Kärnsund, Italienskt Lantbröd")
        #ex.try_read("Palmolive. 50-300 ml. Gäller ej skumtvål")
        #ex.try_read("Palmolive. 50-300 ml. Gäller ej skumtvål")
        #ex.try_read("Palmolive. 50-300 ml. Gäller ej skumtvål. Max 1 köp/hushåll")
        #ex.try_read("Pampers. 84-120 pack. Storlek 4-6. Max 2 köp/hushåll")
        #ex.try_read("Pärsons. 90-160 g. Gäller även vego")
        #ex.try_read("Pauluns. 375-450 g. Flera olika sorter")
        #ex.try_read("Peas of Heaven. 210-250 g. Flera olika sorter")
        #ex.try_read("Pepsi, Zingo, 7-up. 150 cl. Flera olika sorter. Max 1 köp/hushåll")
        #ex.try_read("Peru/Mexiko/Sydafrika/Chile/Kenya/Colombia. 165 g. Klass 1")
        #ex.try_read("Philips. 1-3 st. 10W-100W. Välj mellan olika sorter. Jfr-pris 65:95/st.")
        #ex.try_read("POP! Bakery. 260-600 g. Flera olika sorter")
        #ex.try_read("ProPud. 200 g, 330 ml. Flera olika sorter")
        #ex.try_read("Quorn. 300 g. Fryst. Välj mellan bitar, filéer och färs. Jfr-pris 109:67/kg.")
        #ex.try_read("Respons. 200-250 ml")
        #ex.try_read("Royal Greenland. 1000 g. Max 2 köp/hushåll")
        #ex.try_read("Royal Greenland. 400 g. Frysta. Storlek 60/80. Jfr-pris 99:75/kg. Latin: Pandalus borealis.")
        #ex.try_read("Royal Greenland. 420 g. Max 2 köp/hushåll")
        #ex.try_read("Santa Maria. 30-40 g, 250 ml. Flera olika sorter")
        #ex.try_read("Scan. 1000 g. Kylda. Mammas. Max 2 köp/hushåll")
        #ex.try_read("Scan. 800 g. Max 1 köp/hushåll")
        #ex.try_read("Sense. Gäller på alla Senes akryl och akvarell artiklar. Ord pris 49:90-179:-/st")
        #ex.try_read("Singer Start 1306. 6 sömmar. 1 st 4-stegs knapphål, Extra högt pressarfotslyft. Friarm. Snap-On pressarfötter. Lätt och bärbar. Ord pris 1499:-/st")
        #ex.try_read("Spanien. 400 g. Klass 1. I hink. Jfr-pris 75:-/kg.")
        #ex.try_read("Spira. 40 cm. Välj mellan olika färger.")
        #ex.try_read("Sverige/Arla. 500 g. Normalsaltat. Välj mellan olika sorter. Gäller ej ekologisk, laktosfri. Jfr-pris 99:80/kg.")
        #ex.try_read("Sverige/Arla. 500 g. Normalsaltat. Välj mellan olika sorter. Gäller ej ekologisk, laktosfri. Jfr-pris 99:80/kg.")
        #ex.try_read("Sverige. Ca 350-800 g. I skivor. Jfr-pris 179:-/kg.")
        #ex.try_read("Sverige/Coop. 500 g. Noga utvalt från svenska gårdar. Fetthalt lägre än 12%. Jfr-pris 99:80/kg.")
        #ex.try_read("Sverige/Guldfågeln. Ca 900 g. Kyld. Välj mellan kycklingbröstfilé och kycklinglårfilé. Jfr-pris 129:-/kg.")
        #ex.try_read("Sverige/Guldfågeln. Ca 900 g. Kyld. Välj mellan kycklingbröstfilé och kycklinglårfilé. Jfr-pris 129:-/kg.")
        #ex.try_read("Sverige/Kronfågel. 700 g. Fryst. Välj mellan inner- och lårfilé. Jfr-pris 112:86/kg.")
        #ex.try_read("Tactic. Ord pris 129:-/st")
        #ex.try_read("Torrboll Home är skapad för att balansera luftfuktigheten i sin omgivning samt motverka dålig lukt och mögel. Den är tillverkad i Sverige av 100 % återvunnet material. En påse räcker ca 1-3 månader och passar i utrymmen upp till 50m³. 1 refillpåse medfölj")
        #ex.try_read("Triumfglass. Gäller ej Farbror Arnes. Välj mellan olika 4-6 pack. Jfr-pris 5:82/st.")
        #ex.try_read("Ursprung Sverige. Ca 1000 g. I bit. Av nöt")
        #ex.try_read("Ursprung Sverige. Ca 1100 g")
        #ex.try_read("Välj mellan flera olika titlar i butik. Ord pris 59-79:-/st")
        #ex.try_read("Victor. 2-pack. Victor® Mini PRO PestChaser® avger högfrekventa ljudvågor som effektivt avskräcker gnagare från det skyddade området. För användning i medelstora rum, kök, garage, vindar och källare. Ord pris 399:-/st")
        #ex.try_read("Weleda. 10-200 ml, 4,8 g")
        #ex.try_read("Xtra. Svart. Tillverkade i Sverige av praktisk, hygienisk PP-plast. Stor: 350x250x4mm. Liten: 250x150x4,3mm. Tål maskindisk. Ord. pris 49:90/st.")
        #ex.try_read("YES. 70-pack, 50-pack")
        #ex.try_read("Zeta. 100-125 g. Original, ekologisk")
        #ex.try_read("Zeta. 380 g. Flera olika sorter")
        #ex.try_read("Zeta. 380 g. Flera olika sorter. Max 1 köp/hushåll")
        #ex.try_read("Zeta. 380 g. Flera olika sorter. Max 1 köp/hushåll")
        #ex.try_read("Zingo. 140-150 cl. Välj mellan olika sorter. Jfr-pris 8:33/lit.")
        #ex.try_read("Zingo. 140-150 cl. Välj mellan olika sorter. Jfr-pris 8:33/lit.")
        pass

if __name__ == '__main__':
    unittest.main()
