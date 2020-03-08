import xmlrpclib, sys

#specify server hostname as command line argument
name = "http://"+sys.argv[1]+":8888"

server = xmlrpclib.Server(name)

answer = server.sample.getByTopic("College Life")

for i in range(len(answer)/2):
    print "Title:", answer[i*2]
    print "ID:", answer[i*2 + 1]
