route: iplist.class
	java iplist > route
iplist.class: *.java
	javac -g:none -O iplist.java
clean:
	rm -f *.class route
