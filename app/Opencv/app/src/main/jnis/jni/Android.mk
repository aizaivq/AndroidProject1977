LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)


OpenCV_INSTALL_MODULES := on
OpenCV_CAMERA_MODULES := off

OPENCV_LIB_TYPE :=STATIC

ifeq ("$(wildcard $(OPENCV_MK_PATH))","")
include ../../../../../native/jni/OpenCV.mk
else
include $(OPENCV_MK_PATH)
endif

LOCAL_MODULE := OpenCV

LOCAL_SRC_FILES :=

LOCAL_LDLIBS +=  -lm -llog

include $(BUILD_SHARED_LIBRARY)



include $(CLEAR_VARS)
LOCAL_LDLIBS    := -llog
LOCAL_MODULE    := OpencvManager
LOCAL_SRC_FILES := com_via_opencv_natives_OpencvManager.cpp
#LOCAL_CFLAGS += -I/home/nick/git/gitmy/android-demo/Opencv/native/jni/include
LOCAL_CFLAGS += -I$(LOCAL_PATH)/../../../../../native/jni/include

#LOCAL_STATIC_LIBRARIES += libOpenCV
LOCAL_LDLIBS += -L/home/nick/git/gitmy/android-demo/Opencv/app/src/main/jniLibs/armeabi -lOpenCV
LOCAL_LDLIBS += -L/home/nick/git/gitmy/android-demo/Opencv/app/src/main/jniLibs/armeabi -lopencv_info
LOCAL_LDLIBS += -L/home/nick/git/gitmy/android-demo/Opencv/app/src/main/jniLibs/armeabi -lopencv_java




LOCAL_LDLIBS += -latomic







LOCAL_LDFLAGS += /home/nick/git/gitmy/android-demo/Opencv/app/src/main/jnis/jni/libopencv_calib3d.a
LOCAL_LDFLAGS += /home/nick/git/gitmy/android-demo/Opencv/app/src/main/jnis/jni/libopencv_highgui.a
LOCAL_LDFLAGS += /home/nick/git/gitmy/android-demo/Opencv/app/src/main/jnis/jni/libopencv_androidcamera.a
LOCAL_LDFLAGS += /home/nick/git/gitmy/android-demo/Opencv/app/src/main/jnis/jni/libopencv_contrib.a
#LOCAL_LDFLAGS += /home/nick/git/gitmy/android-demo/Opencv/app/src/main/jnis/jni/libopencv_core.a
LOCAL_LDFLAGS += /home/nick/git/gitmy/android-demo/Opencv/app/src/main/jnis/jni/libopencv_features2d.a
LOCAL_LDFLAGS += /home/nick/git/gitmy/android-demo/Opencv/app/src/main/jnis/jni/libopencv_flann.a
LOCAL_LDFLAGS += /home/nick/git/gitmy/android-demo/Opencv/app/src/main/jnis/jni/libopencv_imgproc.a
LOCAL_LDFLAGS += /home/nick/git/gitmy/android-demo/Opencv/app/src/main/jnis/jni/libopencv_legacy.a
LOCAL_LDFLAGS += /home/nick/git/gitmy/android-demo/Opencv/app/src/main/jnis/jni/libopencv_ml.a
LOCAL_LDFLAGS += /home/nick/git/gitmy/android-demo/Opencv/app/src/main/jnis/jni/libopencv_objdetect.a
LOCAL_LDFLAGS += /home/nick/git/gitmy/android-demo/Opencv/app/src/main/jnis/jni/libopencv_ocl.a
LOCAL_LDFLAGS += /home/nick/git/gitmy/android-demo/Opencv/app/src/main/jnis/jni/libopencv_photo.a
LOCAL_LDFLAGS += /home/nick/git/gitmy/android-demo/Opencv/app/src/main/jnis/jni/libopencv_stitching.a
LOCAL_LDFLAGS += /home/nick/git/gitmy/android-demo/Opencv/app/src/main/jnis/jni/libopencv_superres.a
LOCAL_LDFLAGS += /home/nick/git/gitmy/android-demo/Opencv/app/src/main/jnis/jni/libopencv_ts.a
LOCAL_LDFLAGS += /home/nick/git/gitmy/android-demo/Opencv/app/src/main/jnis/jni/libopencv_video.a
LOCAL_LDFLAGS += /home/nick/git/gitmy/android-demo/Opencv/app/src/main/jnis/jni/libopencv_videostab.a



#LOCAL_CFLAGS += -pie -fPIE
#LOCAL_LDFLAGS += -pie -fPIE


include $(BUILD_SHARED_LIBRARY)


#include $(BUILD_EXECUTABLE)
