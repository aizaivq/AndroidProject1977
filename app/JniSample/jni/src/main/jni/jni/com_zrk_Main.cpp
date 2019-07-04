#include "com_zrk_Main.h"
#include <iostream>  
using namespace std;
JNIEXPORT jstring JNICALL Java_com_zrk_Main_natvieMethod
  (JNIEnv * env, jclass)
  {
	return env->NewStringUTF("nativeMethod");
  }

