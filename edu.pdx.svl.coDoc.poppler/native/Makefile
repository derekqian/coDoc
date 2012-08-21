platform := gtk

eclipse_exe := $_
eclipse_path := $(shell ../name2path $(eclipse_exe))
software_path := $(shell ../name2swpath $(eclipse_exe))
jdk_path := $(software_path)jdk/

#@env
@echo jdk_path = $(jdk_path)
@echo eclipse_path = ${eclipse_path}
@echo pwd = $(shell pwd)

all : libPopplerJNI-gtk.so.1.0
	@echo 'Copying target: $<'
	cp "$<" libPopplerJNI-gtk.so
	cp libPopplerJNI-gtk.so ../../
	@echo 'Finished copying: $<'
	@echo ' '

libPopplerJNI-gtk.so.1.0 : PopplerJNI.o
	@echo 'Building target: $@'
	@echo 'Invoking: GCC C Linker'
	gcc -lstdc++ -shared -Wl,-soname,libPopplerJNI-gtk.so.1 -Wall -Wno-long-long -DNDEBUG -DG_DISABLE_ASSERT -lgthread-2.0 -lrt -lglib-2.0 -lpoppler-glib -lpoppler -lgdk-x11-2.0 -lpangocairo-1.0 -lgdk_pixbuf-2.0 -lm -lcairo -lpng12 -lpango-1.0 -lgobject-2.0 -lgmodule-2.0 -lgthread-2.0 -lrt -lglib-2.0 -pthread -lgtk-x11-2.0 -lgdk-x11-2.0 -latk-1.0 -lgio-2.0 -lpangoft2-1.0 -lpangocairo-1.0 -lgdk_pixbuf-2.0 -lm -lcairo -lpng12 -lpango-1.0 -lfreetype -lfontconfig -lgobject-2.0 -lgmodule-2.0 -lgthread-2.0 -lrt -lglib-2.0 -o "$@" "$<"
	@echo 'Finished building target: $@'
	@echo ' '

./PopplerJNI.o : ../PopplerJNI.cpp ../edu_pdx_svl_coDoc_poppler_lib_PopplerJNI.h
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C Compiler'
	g++ -c -I../ -I/usr/include/glib-2.0 -I/usr/lib/glib-2.0/include -I/usr/include/poppler/glib -I/usr/include/poppler -I/usr/include/cairo -I/usr/include/gtk-2.0 -I/usr/lib/gtk-2.0/include -I/usr/include/gdk-pixbuf-2.0 -I/usr/include/pixman-1 -I/usr/include/freetype2 -I/usr/include/libpng12 -I/usr/include/gio-unix-2.0/ -I/usr/include/pango-1.0 -I$(jdk_path)include -I$(jdk_path)include/linux -fPIC -o "$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '

../edu_pdx_svl_coDoc_poppler_lib_PopplerJNI.h : ../../bin/edu/pdx/svl/coDoc/poppler/lib/PopplerJNI.class
	javah -jni -classpath ../../bin/:$(eclipse_path)plugins/org.eclipse.swt.gtk.linux.x86_64_3.100.0.v4233d.jar -d ../ edu.pdx.svl.coDoc.poppler.lib.PopplerJNI

clean : 
	rm -rf ./*
	rm -rf ../*.h
	rm -rf ../../*.so
	
libs : 
	sudo apt-get install libgtk2.0-dev
	sudo apt-get install libpoppler-glib-dev
	
class :
	javac -d ./ Hello.java
	javac -d ./ test.java
	
sig :
	javap -s -private PopplerJNI

micro :
	g++ -I./ -I/usr/include/glib-2.0 -I/usr/lib/glib-2.0/include -I/usr/include/poppler/glib -I/usr/include/poppler -I/usr/include/cairo -I/usr/include/gtk-2.0 -I/usr/lib/gtk-2.0/include -I/usr/include/gdk-pixbuf-2.0 -I/usr/include/pixman-1 -I/usr/include/freetype2 -I/usr/include/libpng12 -I/usr/include/gio-unix-2.0/ -I/usr/include/pango-1.0 -I/opt/sun/jdk1.6.0_24/include -I/opt/sun/jdk1.6.0_24/include/linux -dM -E PopplerJNI.cpp

test :
	java -Djava.library.path=./ com.derek.test
	
