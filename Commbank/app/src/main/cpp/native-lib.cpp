#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_test_commbank_di_ApiModuleKt_getCommbankBaseUrl(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF("https://gorest.co.in/public/v1/");
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_test_commbank_di_ApiModuleKt_getClientToken(JNIEnv *env, jclass clazz) {
    return env->NewStringUTF("ff204d63332ec73ee6b9256320e985acae910bb57263977debf581bfd96b3169");
}