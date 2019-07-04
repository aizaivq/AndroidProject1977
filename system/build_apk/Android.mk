LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := A
LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES := A.apk
LOCAL_MODULE_CLASS := APPS
LOCAL_MODULE_PATH := $(TARGET_OUT)/app
#LOCAL_PREBUILT_JNI_LIBS := libso1.so \
                        libso2.so
LOCAL_MODULE_INCLUDE_LIBRARY := true
LOCAL_CERTIFICATE := platform
include $(BUILD_PREBUILT)

