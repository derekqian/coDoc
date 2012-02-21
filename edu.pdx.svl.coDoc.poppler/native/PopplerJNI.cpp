#include <stdio.h>
#include <stdlib.h>
#include <memory.h>
#include <assert.h>
#include <poppler.h>

#include "edu_pdx_svl_coDoc_poppler_lib_PopplerJNI.h"



static PopplerDocument *g_document = NULL;
static PopplerPage *g_page = NULL;
static int g_pagenum = -1;
static int g_width = -1;
static int g_height = -1;

/*
 * Class:     edu_pdx_svl_coDoc_poppler_lib_PopplerJNI
 * Method:    document_new_from_file
 * Signature: (Ljava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_edu_pdx_svl_coDoc_poppler_lib_PopplerJNI_document_1new_1from_1file
  (JNIEnv *env, jobject obj, jstring _uri, jstring _password)
{
	gchar *uri = NULL;
	gchar *password = NULL;
	GError *error = NULL;

	printf("%s (%d): %s\n", __FILE__, __LINE__, __FUNCTION__);

	assert(_uri != NULL);
	uri = (gchar *)env->GetStringUTFChars(_uri, JNI_FALSE);

	if(g_document != NULL) g_object_unref(g_document);
	g_document = poppler_document_new_from_file (uri, NULL, &error);
	if(error) 
	{
		if(g_error_matches (error, POPPLER_ERROR, POPPLER_ERROR_ENCRYPTED)) 
		{
			assert(_password != NULL);
			password = (gchar *)env->GetStringUTFChars(_password, JNI_FALSE);
			g_document = poppler_document_new_from_file (uri, password, &error);
			env->ReleaseStringUTFChars(_password, (gchar *)password);
		}
		if(error) 
		{
			g_print ("Error: %s\n", error->message);
			g_error_free (error);

			return -1;
		}
	}

	env->ReleaseStringUTFChars(_uri, (gchar *)uri);

	assert(g_document != NULL);

	return 0;
}


/*
 * Class:     edu_pdx_svl_coDoc_poppler_lib_PopplerJNI
 * Method:    document_close
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_edu_pdx_svl_coDoc_poppler_lib_PopplerJNI_document_1close
  (JNIEnv *, jobject)
{
	if(g_document == NULL) return 0;

	printf("%s (%d): %s\n", __FILE__, __LINE__, __FUNCTION__);
	g_object_unref(g_document);
	g_document = NULL;
	return 0;
}


/*
 * Class:     edu_pdx_svl_coDoc_poppler_lib_PopplerJNI
 * Method:    document_get_n_pages
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_edu_pdx_svl_coDoc_poppler_lib_PopplerJNI_document_1get_1n_1pages
  (JNIEnv *, jobject)
{
	assert(g_document != NULL);

	printf("%s (%d): %s\n", __FILE__, __LINE__, __FUNCTION__);

	return (jint)poppler_document_get_n_pages(g_document);
}


/*
 * Class:     edu_pdx_svl_coDoc_poppler_lib_PopplerJNI
 * Method:    document_get_page
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_edu_pdx_svl_coDoc_poppler_lib_PopplerJNI_document_1get_1page
  (JNIEnv *, jobject, jint pagenum_j)
{
	assert(g_document != NULL);

	printf("%s (%d): %s\n", __FILE__, __LINE__, __FUNCTION__);

	g_pagenum = (int)(pagenum_j-1);

	if(g_page != NULL) g_object_unref(g_page);
	g_page = poppler_document_get_page (g_document, g_pagenum);
	assert(g_page != NULL);

	return 0;
}


/*
 * Class:     edu_pdx_svl_coDoc_poppler_lib_PopplerJNI
 * Method:    document_release_page
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_edu_pdx_svl_coDoc_poppler_lib_PopplerJNI_document_1release_1page
  (JNIEnv *, jobject)
{
	assert(g_document != NULL);

	if(g_page == NULL) return 0;

	printf("%s (%d): %s\n", __FILE__, __LINE__, __FUNCTION__);
	g_pagenum = -1;
	g_width = -1;
	g_height = -1;
	g_object_unref(g_page);
	g_page = NULL;
	return 0;
}


/*
 * Class:     edu_pdx_svl_coDoc_poppler_lib_PopplerJNI
 * Method:    page_get_size
 * Signature: ()Ljava/awt/Dimension;
 * javap -classpath /opt/eclipse/plugins/org.eclipse.swt.gtk.linux.x86_3.6.2.v3659b.jar -s -private org.eclipse.swt.graphics.Point
 */
JNIEXPORT jobject JNICALL Java_edu_pdx_svl_coDoc_poppler_lib_PopplerJNI_page_1get_1size
  (JNIEnv *env, jobject)
{
	double width = 0;
	double height = 0;

	assert(g_document != NULL);
	assert(g_page != NULL);

	printf("%s (%d): %s\n", __FILE__, __LINE__, __FUNCTION__);

	poppler_page_get_size (g_page, &width, &height);
	g_width = width;
	g_height = height;

    // get class
    jclass class_Dimension = env->FindClass("java/awt/Dimension");
    assert(class_Dimension != 0);

    // get constructor
    //jmethodID construct_Point = env->GetMethodID(class_Point, "<init></init>","()V");

    // create java object
    jobject obj_Dimension = env->AllocObject(class_Dimension);
    //jobject obj_Point = env->NewObject(class_Point, construct_Point, "");

    jfieldID id_width = env->GetFieldID(class_Dimension, "width", "I");   
    jfieldID id_height = env->GetFieldID(class_Dimension, "height", "I");   
  
    env->SetIntField(obj_Dimension, id_width, g_width);   
    env->SetIntField(obj_Dimension, id_height, g_height); 
    //env->SetObjectField(obj_Point, y, env->NewStringUTF((char*)"test"));   
    
    return obj_Dimension;   
}


/*
 * Class:     edu_pdx_svl_coDoc_poppler_lib_PopplerJNI
 * Method:    page_get_index
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_edu_pdx_svl_coDoc_poppler_lib_PopplerJNI_page_1get_1index
  (JNIEnv *, jobject)
{
	assert(g_document != NULL);

	printf("%s (%d): %s\n", __FILE__, __LINE__, __FUNCTION__);
    
	if(g_page == NULL) return -1;

    return (jint)poppler_page_get_index(g_page);   
}



/*
 * Class:     edu_pdx_svl_coDoc_poppler_lib_PopplerJNI
 * Method:    page_render
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_edu_pdx_svl_coDoc_poppler_lib_PopplerJNI_page_1render
  (JNIEnv *env, jobject, jbyteArray data_j)
{
    jboolean isCopy;
    cairo_surface_t *surface = NULL;
    cairo_t *cr = NULL;

	assert(g_document != NULL);
	assert(g_page != NULL);
	assert(g_pagenum != -1);
	assert(g_width != -1);
	assert(g_height != -1);

	printf("%s (%d): %s\n", __FILE__, __LINE__, __FUNCTION__);
  
    jsize len = env->GetArrayLength(data_j);
	printf("len: %d\n", len);

    jbyte* data = env->GetByteArrayElements(data_j, &isCopy);
    if(env->ExceptionCheck()) return -1; 
	printf("isCopy: %s\n", isCopy?"TRUE":"FALSE");

    //memset(data, 0xff, len); 

#if 0
	surface = cairo_image_surface_create (CAIRO_FORMAT_ARGB32, g_width, g_height);
#else
	//cairo_format_stride_for_width();
	surface = cairo_image_surface_create_for_data ((unsigned char *)data, CAIRO_FORMAT_ARGB32, g_width, g_height, g_width*4);
	cairo_status_t status = cairo_surface_status(surface);
	assert(status == CAIRO_STATUS_SUCCESS);
#endif
	cr = cairo_create (surface);

	cairo_save (cr);
	//cairo_translate (cr, x + width, -y);
	//cairo_scale (cr, demo->scale, demo->scale);
	poppler_page_render (g_page, cr);
	cairo_restore (cr);
	cairo_set_operator (cr, CAIRO_OPERATOR_DEST_OVER);
	cairo_set_source_rgb (cr, 1.0, 1.0, 1.0);
	cairo_paint (cr);
#if 0
	unsigned char * tempbuf = cairo_image_surface_get_data(surface);
	memcpy(data, tempbuf, len);
#endif
	cairo_destroy (cr);
	cairo_surface_destroy (surface);

    env->ReleaseByteArrayElements(data_j, data, JNI_COMMIT);
    if(env->ExceptionCheck()) return -1;
       
    return 0;   
}

/*
 * Class:     edu_pdx_svl_coDoc_poppler_lib_PopplerJNI
 * Method:    page_render_selection
 * Signature: ([BLorg/eclipse/swt/graphics/Rectangle;Lorg/eclipse/swt/graphics/Rectangle;)I
 */
JNIEXPORT jint JNICALL Java_edu_pdx_svl_coDoc_poppler_lib_PopplerJNI_page_1render_1selection
  (JNIEnv *, jobject, jbyteArray, jobject, jobject)
{
	assert(g_document != NULL);
	assert(g_page != NULL);
	assert(g_pagenum != -1);
	assert(g_width != -1);
	assert(g_height != -1);

	printf("%s (%d): %s\n", __FILE__, __LINE__, __FUNCTION__);

    return 0;   
}

/*
 * Class:     edu_pdx_svl_coDoc_poppler_lib_PopplerJNI
 * Method:    page_get_selected_region
 * Signature: (DLorg/eclipse/swt/graphics/Rectangle;)[Lorg/eclipse/swt/graphics/Rectangle;
 * javap -classpath /opt/sun/jdk1.6.0_24/jre/lib/rt.jar -s -private java.awt.Rectangle
 */
JNIEXPORT jobjectArray JNICALL Java_edu_pdx_svl_coDoc_poppler_lib_PopplerJNI_page_1get_1selected_1region
  (JNIEnv *env, jobject, jdouble scale_j, jobject rect_j)
{
    int i = 0;
    jobjectArray rectArray = 0;
    jsize        len = 0;

	assert(g_document != NULL);
	assert(g_page != NULL);
	assert(g_pagenum != -1);
	assert(g_width != -1);
	assert(g_height != -1);

	printf("%s (%d): %s\n", __FILE__, __LINE__, __FUNCTION__);

    // get class
    jclass class_rect_j = env->GetObjectClass(rect_j);
    assert(class_rect_j != 0);

#if 0 // for java.awt.Rectangle
    jmethodID method_getX = env->GetMethodID(class_rect_j, "getX", "()D");
    jdouble x = static_cast<jdouble>(env->CallDoubleMethod(rect_j, method_getX));
    jmethodID method_getY = env->GetMethodID(class_rect_j, "getY", "()D");
    jdouble y = static_cast<jdouble>(env->CallDoubleMethod(rect_j, method_getY));
    jmethodID method_getWidth = env->GetMethodID(class_rect_j, "getWidth", "()D");
    jdouble width = static_cast<jdouble>(env->CallDoubleMethod(rect_j, method_getWidth));
    jmethodID method_getHeight = env->GetMethodID(class_rect_j, "getHeight", "()D");
    jdouble height = static_cast<jdouble>(env->CallDoubleMethod(rect_j, method_getHeight));
#else
    jfieldID fid_x = env-> GetFieldID(class_rect_j, "x", "I");
    jint x = env->GetIntField(rect_j, fid_x);
    jfieldID fid_y = env-> GetFieldID(class_rect_j, "y", "I");
    jint y = env->GetIntField(rect_j, fid_y);
    jfieldID fid_width = env-> GetFieldID(class_rect_j, "width", "I");
    jint width = env->GetIntField(rect_j, fid_width);
    jfieldID fid_height = env-> GetFieldID(class_rect_j, "height", "I");
    jint height = env->GetIntField(rect_j, fid_height);
#endif

    //
    PopplerRectangle rect;
    rect.x1 = x;
    rect.y1 = y;
    rect.x2 = x + width;
    rect.y2 = y + height;
    printf("%s (%d): %s -> (%f, %f, %f, %f)\n", __FILE__, __LINE__, __FUNCTION__, rect.x1, rect.y1, rect.x2, rect.y2);
    GList *selections = poppler_page_get_selection_region(g_page, scale_j, POPPLER_SELECTION_GLYPH, &rect);
    for (GList *selection = g_list_first (selections) ; NULL != selection ; selection = g_list_next (selection)) 
    {
        len++;
    }

    //
    jclass class_Object = env->FindClass("java/lang/Object");
    rectArray = env->NewObjectArray(len, class_Object, 0);
    jclass class_Rectangle = env->FindClass("org/eclipse/swt/graphics/Rectangle");
    jfieldID id_x = env->GetFieldID(class_Rectangle,"x","I");
    jfieldID id_y = env->GetFieldID(class_Rectangle,"y","I");
    jfieldID id_width = env->GetFieldID(class_Rectangle,"width","I");
    jfieldID id_height = env->GetFieldID(class_Rectangle,"height","I");
    for (GList *selection = g_list_first (selections) ; NULL != selection ; selection = g_list_next (selection)) 
    {
        PopplerRectangle *rectangle = (PopplerRectangle *)selection->data;

        jobject obj_Rectangle = env->AllocObject(class_Rectangle);
        env->SetIntField(obj_Rectangle, id_x, (gint)rectangle->x1);
        env->SetIntField(obj_Rectangle, id_y, (gint)rectangle->y1);
        env->SetIntField(obj_Rectangle, id_width, (gint) (rectangle->x2 - rectangle->x1));
        env->SetIntField(obj_Rectangle, id_height, (gint) (rectangle->y2 - rectangle->y1));

        // add to array
        env->SetObjectArrayElement(rectArray, i++, obj_Rectangle);
    }
    poppler_page_selection_region_free (selections);

    return rectArray;   
}

/*
 * Class:     edu_pdx_svl_coDoc_poppler_lib_PopplerJNI
 * Method:    page_get_selected_text
 * Signature: (DLorg/eclipse/swt/graphics/Rectangle;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_edu_pdx_svl_coDoc_poppler_lib_PopplerJNI_page_1get_1selected_1text
  (JNIEnv *env, jobject, jdouble, jobject rect_j)
{
    jstring str = 0;

	assert(g_document != NULL);
	assert(g_page != NULL);
	assert(g_pagenum != -1);
	assert(g_width != -1);
	assert(g_height != -1);

	printf("%s (%d): %s\n", __FILE__, __LINE__, __FUNCTION__);

    // get class
    jclass class_rect_j = env->GetObjectClass(rect_j);
    assert(class_rect_j != 0);

#if 0 // for java.awt.Rectangle
    jmethodID method_getX = env->GetMethodID(class_rect_j, "getX", "()D");
    jdouble x = static_cast<jdouble>(env->CallDoubleMethod(rect_j, method_getX));
    jmethodID method_getY = env->GetMethodID(class_rect_j, "getY", "()D");
    jdouble y = static_cast<jdouble>(env->CallDoubleMethod(rect_j, method_getY));
    jmethodID method_getWidth = env->GetMethodID(class_rect_j, "getWidth", "()D");
    jdouble width = static_cast<jdouble>(env->CallDoubleMethod(rect_j, method_getWidth));
    jmethodID method_getHeight = env->GetMethodID(class_rect_j, "getHeight", "()D");
    jdouble height = static_cast<jdouble>(env->CallDoubleMethod(rect_j, method_getHeight));
#else
    jfieldID fid_x = env-> GetFieldID(class_rect_j, "x", "I");
    jint x = env->GetIntField(rect_j, fid_x);
    jfieldID fid_y = env-> GetFieldID(class_rect_j, "y", "I");
    jint y = env->GetIntField(rect_j, fid_y);
    jfieldID fid_width = env-> GetFieldID(class_rect_j, "width", "I");
    jint width = env->GetIntField(rect_j, fid_width);
    jfieldID fid_height = env-> GetFieldID(class_rect_j, "height", "I");
    jint height = env->GetIntField(rect_j, fid_height);
#endif

#if defined (HAVE_POPPLER_0_17_0)
#error HAVE_POPPLER_0_17_0
#elif defined (HAVE_POPPLER_0_15_0)
#error HAVE_POPPLER_0_15_0
#elif defined (HAVE_POPPLER_0_8_0)
#error HAVE_POPPLER_0_8_0
#elif defined (HAVE_POPPLER_0_6_0)
#error HAVE_POPPLER_0_6_0
#else
#endif

    //
    PopplerRectangle rect;
#if 0
    rect.x1 = x;
    rect.y1 = y;
    rect.x2 = x + width;
    rect.y2 = y + height;
    //gchar *text = poppler_page_get_selected_text(g_page, POPPLER_SELECTION_GLYPH, &rect);
    //gchar *text = poppler_page_get_text(g_page, &rect);
#else
    rect.x1 = x + width;
    rect.y1 = g_height - y;
    rect.x2 = x;
    rect.y2 = g_height - (y + height);
    gchar *text = poppler_page_get_text(g_page, POPPLER_SELECTION_GLYPH, &rect);
#endif

    int slen = strlen(text);
    str = (env)->NewStringUTF(text);

    return str;   
}

