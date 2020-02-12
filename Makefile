clean: 
	rm ./*.class

compile: 
	javac -Xlint:unchecked Main.java

run: compile
	java Main
