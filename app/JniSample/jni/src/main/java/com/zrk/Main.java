package com.zrk;
public class Main
{
	public static native String natvieMethod();
	public static void main(String[] args)
{
	System.load("/home/zrk/gradle/GradleTest/src/main/jni/libNativeMethod.so");
	System.out.println("main");
	System.out.println(natvieMethod());
}


}

