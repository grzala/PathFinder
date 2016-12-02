gcc -Wall -O2 -std=c11 -m32 -Iinclude -c src/main.c -o obj/main.o

windres -J rc -O coff -Iinclude -i resource/resource.rc -o obj/resource/resource.res 

gcc -Wall -O2 -std=c11 -m32 -static-libgcc -mwindows -Iinclude obj/main.o obj/resource/resource.res -o bin/pathfinder.exe -lgdi32
