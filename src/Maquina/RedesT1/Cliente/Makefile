JFLAGS = -g
JC = javac
JVM = java
.SUFFIXES: .java .class
.java.class:
								$(JC) $(JFLAGS) $*.java

CLASSES = \
        ./client.java\

MAIN = ./client

default: classes

classes: $(CLASSES:.java=.class)

run: $(MAIN).class
								$(JVM) $(MAIN)

clean:
								$(RM) *.classXXA
