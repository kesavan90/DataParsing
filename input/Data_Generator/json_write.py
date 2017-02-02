__author__ = 'rravicha'

import json
import random

number_of_record = 10



def getTS(y,mo,d,h):
    m,s,ms = rand_min_s_ms()
    return y+"-"+mo+"-"+h+":"+d+":"+m+":"+s+"."+ms+"Z"

def rand_min_s_ms():
    m = str(random.randrange(0,59)).zfill(2)
    s = str(random.randrange(0,59)).zfill(2)
    ms = str(random.randrange(0,999)).zfill(3)
    return (m,s,ms)

def generate_amt(x):
    amt = []
    for i in range (0,x):
        amt_mux = random.randrange(10, 100)
        amt_fl = float('{0:.3g}'.format(random.random()))
        amt.append(str(amt_mux*amt_fl) + " USD")
        print(amt)
    return amt

def generate_key(x):
    key = []
    for i in range (0,x):
        key.append(''.join(random.choice('0123456789ABCDEF') for i in range(16)))
        print(key)
    return key



'E2C6B2E19E4A7777'

name = "Smith"
key = "96f55c7d8f42"


name_list = ["Smith", "Andy", "John", "Mary", "Alex", "Bob", "Alice", "Tracy", "Mike", "James", "George", "Richard", "Amy", "Paul", "Rosy", "Ron", "Jaguar", "Tiger", "Leopard", "Zeus"]
key_list = generate_key(20)

# amount = "12.34 USD"
# timestamp = y+"-"+mo+"-"+h+":"+d+":"+m+":"+s+"."+ms+"Z"

# print(timestamp)
amt_list = generate_amt(number_of_record)

for name, key in zip (name_list, key_list):

    for amount in amt_list:

        y = str(random.randrange(2000,2010)).zfill(4)
        mo = str(random.randrange(1,12)).zfill(2)
        d = str(random.randrange(0,30)).zfill(2)
        h = str(random.randrange(0,23)).zfill(2)


        data= [
        {
        "type": "CUSTOMER",
        "verb": "UPDATE",
        "key": "{}".format(key),
        "event_time": "{}".format(getTS(y,mo,d,h)),
        "last_name": "{}".format(name),
        "adr_city": "Middletown",
        "adr_state": "AK"
        },
        {
        "type": "SITE_VISIT",
        "verb": "NEW",
        "key": "ac05e815502f",
        "event_time": "{}".format(getTS(y,mo,d,h)),
        "customer_id": "{}".format(key),
        "tags": {"some key": "some value"}
        },
        {
        "type": "IMAGE",
        "verb": "UPLOAD",
        "key": "d8ede43b1d9f",
        "event_time": "{}".format(getTS(y,mo,d,h)),
        "customer_id": "{}".format(key),
        "camera_make": "Canon",
        "camera_model": "EOS 80D"
        },
        {

        "verb": "NEW",

        "key":  "68d84e5d1a43",
        "event_time": "{}".format(getTS(y,mo,d,h)),
         "customer_id": "{}".format(key),
         "total_amount": "{}".format(amount),
         "type":"ORDER"
        }
        ]

        with open('data.txt', 'a+') as outfile:
            json.dump(data, outfile)