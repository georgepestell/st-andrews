all: runtm

runtm: turingmachine tape Main.class  runtm.jar
	@/bin/echo -e '#!/bin/sh\njava -jar runtm.jar $$@' > runtm
	chmod +x runtm

runtm.jar:
	jar cfe runtm.jar Main ./

turingmachine:
	javac turingmachine/*.java

tape:
	javac tape/*.java

Main.class:
	javac Main.java

clean:
	rm -f *.class
	rm -f */*.class
	rm -f runtm.jar
	rm -f runtm