JFLAGS = -g
JC = javac
.SUFFIXES: .java .class
.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = RPNCal.java\
		  Computorv2.java\
		  CreateVars.java\
		  Complex.java\

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class