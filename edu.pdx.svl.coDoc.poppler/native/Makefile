all : 
	javah -classpath ../bin/:/opt/eclipse/plugins/org.eclipse.swt.gtk.linux.x86_3.6.2.v3659b.jar -d ./ edu.pdx.svl.coDoc.poppler.lib.PopplerJNI
	g++ -I./ -I/usr/include/glib-2.0 -I/usr/lib/glib-2.0/include -I/usr/include/poppler/glib -I/usr/include/poppler -I/usr/include/cairo -I/usr/include/gtk-2.0 -I/usr/lib/gtk-2.0/include -I/usr/include/gdk-pixbuf-2.0 -I/usr/include/pixman-1 -I/usr/include/freetype2 -I/usr/include/libpng12 -I/usr/include/gio-unix-2.0/ -I/usr/include/pango-1.0 -I/opt/sun/jdk1.6.0_24/include -I/opt/sun/jdk1.6.0_24/include/linux -fPIC -c PopplerJNI.cpp
	gcc -lstdc++ -shared -Wl,-soname,libPopplerJNI.so.1 -Wall -Wno-long-long -DNDEBUG -DG_DISABLE_ASSERT -lgthread-2.0 -lrt -lglib-2.0 -lpoppler-glib -lpoppler -lgdk-x11-2.0 -lpangocairo-1.0 -lgdk_pixbuf-2.0 -lm -lcairo -lpng12 -lpango-1.0 -lgobject-2.0 -lgmodule-2.0 -lgthread-2.0 -lrt -lglib-2.0 -pthread -lgtk-x11-2.0 -lgdk-x11-2.0 -latk-1.0 -lgio-2.0 -lpangoft2-1.0 -lpangocairo-1.0 -lgdk_pixbuf-2.0 -lm -lcairo -lpng12 -lpango-1.0 -lfreetype -lfontconfig -lgobject-2.0 -lgmodule-2.0 -lgthread-2.0 -lrt -lglib-2.0 -o libPopplerJNI.so.1.0 PopplerJNI.o
	cp libPopplerJNI.so.1.0 libPopplerJNI.so
	cp libPopplerJNI.so ../bin/

class :
	javac -d ./ Hello.java
	javac -d ./ test.java
	
sig :
	javap -s -private PopplerJNI

test :
	java -Djava.library.path=./ com.derek.test
	
