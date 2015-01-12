route: parse iplist.class
	java iplist |grep -v CN |./parse >route
parse: parse.c
	gcc -O3 -Wall -Wextra -std=c99 -march=native -mtune=native -o parse parse.c
	strip --strip-unneeded -R .comment -R .GCC.command.line -R .note.gnu.gold-version parse
iplist.class: iplist.java
	javac -g:none -O iplist.java
clean:
	rm -f parse iplist.class route
