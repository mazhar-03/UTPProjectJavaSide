/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class main_Main */

#ifndef _Included_main_Main
#define _Included_main_Main
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     main_Main
 * Method:    getBoardState
 * Signature: ()[[I
 */
JNIEXPORT jobjectArray JNICALL Java_main_Main_getBoardState
  (JNIEnv *, jclass);

/*
 * Class:     main_Main
 * Method:    movePiece
 * Signature: (IIII)Z
 */
JNIEXPORT jboolean JNICALL Java_main_Main_movePiece
  (JNIEnv *, jclass, jint, jint, jint, jint);

/*
 * Class:     main_Main
 * Method:    checkEndgame
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_main_Main_checkEndgame
  (JNIEnv *, jclass);

#ifdef __cplusplus
}
#endif
#endif