#include <stdio.h>
#include <stdlib.h>
#include <memory.h>
#include <assert.h>
#include <poppler.h>

#include "edu_pdx_svl_coDoc_poppler_PopplerJNI.h"



static PopplerDocument *g_document = NULL;
static PopplerPage *g_page = NULL;
static int g_pagenum = -1;
static int g_width = -1;
static int g_height = -1;

/*
 * Class:     edu_pdx_svl_coDoc_poppler_PopplerJNI
 * Method:    document_new_from_file
 * Signature: (Ljava/lang/String;Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_edu_pdx_svl_coDoc_poppler_PopplerJNI_document_1new_1from_1file
  (JNIEnv *env, jobject obj, jstring _uri, jstring _password)
{
	gchar *uri = NULL;
	gchar *password = NULL;
	GError *error = NULL;

	printf("%s (%d): %s\n", __FILE__, __LINE__, __FUNCTION__);

	assert(_uri != NULL);
	uri = (gchar *)env->GetStringUTFChars(_uri, JNI_FALSE);

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
 * Class:     edu_pdx_svl_coDoc_poppler_PopplerJNI
 * Method:    document_close
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_edu_pdx_svl_coDoc_poppler_PopplerJNI_document_1close
  (JNIEnv *, jobject)
{
	assert(g_document != NULL);

	printf("%s (%d): %s\n", __FILE__, __LINE__, __FUNCTION__);
	g_object_unref(g_document);
	g_document = NULL;
	return 0;
}


/*
 * Class:     edu_pdx_svl_coDoc_poppler_PopplerJNI
 * Method:    document_get_n_pages
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_edu_pdx_svl_coDoc_poppler_PopplerJNI_document_1get_1n_1pages
  (JNIEnv *, jobject)
{
	assert(g_document != NULL);

	printf("%s (%d): %s\n", __FILE__, __LINE__, __FUNCTION__);

	return (jint)poppler_document_get_n_pages(g_document);
}


/*
 * Class:     edu_pdx_svl_coDoc_poppler_PopplerJNI
 * Method:    document_get_page
 * Signature: (I)I
 */
JNIEXPORT jint JNICALL Java_edu_pdx_svl_coDoc_poppler_PopplerJNI_document_1get_1page
  (JNIEnv *, jobject, jint pagenum_j)
{
	assert(g_document != NULL);

	printf("%s (%d): %s\n", __FILE__, __LINE__, __FUNCTION__);

	g_pagenum = (int)pagenum_j;

	g_page = poppler_document_get_page (g_document, g_pagenum);
	assert(g_page != NULL);

	return 0;
}


/*
 * Class:     edu_pdx_svl_coDoc_poppler_PopplerJNI
 * Method:    document_release_page
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_edu_pdx_svl_coDoc_poppler_PopplerJNI_document_1release_1page
  (JNIEnv *, jobject)
{
	assert(g_document != NULL);
	assert(g_page != NULL);

	printf("%s (%d): %s\n", __FILE__, __LINE__, __FUNCTION__);
	g_pagenum = -1;
	g_width = -1;
	g_height = -1;
	g_object_unref(g_page);
	g_page = NULL;
	return 0;
}


/*
 * Class:     edu_pdx_svl_coDoc_poppler_PopplerJNI
 * Method:    page_get_size
 * Signature: ()Lorg/eclipse/swt/graphics/Point;
 * javap -classpath /opt/eclipse/plugins/org.eclipse.swt.gtk.linux.x86_3.6.2.v3659b.jar -s -private org.eclipse.swt.graphics.Point
 */
JNIEXPORT jobject JNICALL Java_edu_pdx_svl_coDoc_poppler_PopplerJNI_page_1get_1size
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
    jclass class_Point = env->FindClass("org/eclipse/swt/graphics/Point");
    assert(class_Point != 0);

    // get constructor
    //jmethodID construct_Point = env->GetMethodID(class_Point, "<init></init>","()V");

    // create java object
    jobject obj_Point = env->AllocObject(class_Point);
    //jobject obj_Point = env->NewObject(class_Point, construct_Point, "");

    jfieldID x = env->GetFieldID(class_Point, "x", "I");   
    jfieldID y = env->GetFieldID(class_Point, "y", "I");   
  
    env->SetIntField(obj_Point, x, g_width);   
    env->SetIntField(obj_Point, y, g_height); 
    //env->SetObjectField(obj_Point, y, env->NewStringUTF((char*)"test"));   
    
    return obj_Point;   
}


/*
 * Class:     edu_pdx_svl_coDoc_poppler_PopplerJNI
 * Method:    page_render
 * Signature: ([B)I
 */
JNIEXPORT jint JNICALL Java_edu_pdx_svl_coDoc_poppler_PopplerJNI_page_1render
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


