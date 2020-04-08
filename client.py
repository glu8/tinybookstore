import xmlrpclib, sys
import timeit


#specify server hostname as command line argument


name = "http://"+sys.argv[1]+":8888"

server = xmlrpclib.Server(name)

def testSearch():

    start_time = timeit.default_timer()

    for i in range(125):
        server.sample.search("Distributed Systems")
        server.sample.search("College Life")
    
    return timeit.default_timer() - start_time

def testBuy():
    
    start_time = timeit.default_timer()
    for i in range(125):
        server.sample.buy("53477")
        server.sample.buy("12498")
    
    return timeit.default_timer() - start_time

while (True):
    input = raw_input("Type your command:\n")
    input = input.split()

    if len(input) == 0: 
        print("Invalid Command")
    elif len(input) == 1 and input[0].lower() == "quit":
        break
    elif input[0].lower() == "testsearch":
        print(testSearch())
    elif input[0].lower() == "testbuy":
        print(testBuy())
    elif len(input) < 2:
        print("Invalid Command")
    elif input[0].lower() == "search":
        answer = server.sample.search(str(" ".join(input[1:])))
        for i in range(len(answer)/2):
            print("Title: {}".format(answer[i*2]))
            print "ID: {}".format(answer[i*2 + 1])
    elif input[0].lower() == "lookup":
        answer = server.sample.lookup(str(input[1]))
        print("Title: {}\nTopic: {}\nPrice: ${} \n{} in stock".format(answer[0],answer[1],answer[2],answer[3]))
    elif input[0].lower() == "buy":
        answer = server.sample.buy(str(input[1]))
        print(answer[0])
    else: 
        print("Invalid Command")
    
'''
for i in range(len(answer)/2):
    print("Title: {}".format(answer[i*2]))
    print "ID: {}".format(answer[i*2 + 1])
'''