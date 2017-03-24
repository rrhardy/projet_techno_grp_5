JC = javac
.SUFFIXES: .java .class
LIBS = ./javax.json-1.0.jar
JFLAGS = -cp $(LIBS)
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = GpsMessage.java BusMessageAbstract.java BusMessage.java Client.java ClientManager.java MessageQueue.java Serveur.java TestJSON.java TestSrvJSON.java 

all: classes

GpsMessage.class: GpsMessage.java BusMessage.class BusMessageAbstract.class 
	$(JC) $(JFLAGS) $<

BusMessage.class: BusMessage.java
	$(JC) $(JFLAGS) $<

BusMessageAbstract.class: BusMessageAbstract.java BusMessage.class
	$(JC) $(JFLAGS) $<

MessageQueue.class: MessageQueue.java
	$(JC) $(JFLAGS) $<

ClientManager.class: ClientManager.java %.class
	$(JC) $(JFLAGS) $<

Serveur.class: Serveur.java ClientManager.class
	$(JC) $(JFLAGS) $<

Client.class: Client.java 
	$(JC) $(JFLAGS) $<

classes: $(CLASSES:.java=.class)

clean:	
	rm *.class
