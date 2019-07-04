LOCAL_PATH := $(call my-dir)  
include $(CLEAR_VARS)      
LOCAL_MODULE_TAGS := optional
LOCAL_SRC_FILES := main.c
LOCAL_MODULE := libmain
LOCAL_MODULE_CLASS := EXECUTABLES  
include $(BUILD_SHARED_LIBRARY)
