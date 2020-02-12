clean: 
	rm ./*.class

compile: 
	javac Main.java

run: compile
	java Main
