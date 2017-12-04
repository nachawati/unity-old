/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

#include <unity_modules_engines_zorba_ZorbaDXScriptContext.h>
#include <ZorbaDXScriptContext.h>

#include <zorba/api_shared_types.h>
#include <zorba/static_context_consts.h>
#include <zorba/zorba.h>

zorba::StaticContext* getStaticContext(JNIEnv * env, jobject obj) {
	jclass cls = env->GetObjectClass(obj);
	jfieldID fldStaticContext = env->GetFieldID(cls, "sctx",
			"Lio/zorba/api/StaticContext;");
	jobject objStaticContext = env->GetObjectField(obj, fldStaticContext);
	jclass clsStaticContext = env->GetObjectClass(objStaticContext);
	jfieldID fld = env->GetFieldID(clsStaticContext, "swigCPtr", "J");
	return *((zorba::StaticContext**) env->GetLongField(objStaticContext, fld));
}

JNIEXPORT jboolean JNICALL Java_unity_modules_engines_zorba_ZorbaDXScriptContext_construct(
		JNIEnv* env, jobject obj) {
	try {
		zorba::StaticContext* sctx = getStaticContext(env, obj);
		if (sctx == NULL)
			return JNI_FALSE;
		ZorbaDXResolver* resolver = new ZorbaDXResolver(env, obj);
		jclass cls = env->GetObjectClass(obj);
		jfieldID fld = env->GetFieldID(cls, "resolverCPtr", "J");
		env->SetLongField(obj, fld, (jlong) resolver);
		sctx->registerURLResolver(resolver);
		sctx->registerURIMapper(resolver);
		return JNI_TRUE;
	} catch (...) {
		return JNI_FALSE;
	}
}

JNIEXPORT jboolean JNICALL Java_unity_modules_engines_zorba_ZorbaDXScriptContext_destroy(
		JNIEnv* env, jobject obj) {
	try {
		jclass cls = env->GetObjectClass(obj);
		jfieldID fld = env->GetFieldID(cls, "resolverCPtr", "J");
		jlong resolverCPtr = env->GetLongField(obj, fld);
		env->SetLongField(obj, fld, 0);
		if (resolverCPtr != 0)
			delete (ZorbaDXResolver*) resolverCPtr;
		return JNI_TRUE;
	} catch (...) {
		return JNI_FALSE;
	}
}

JNIEXPORT jboolean JNICALL Java_unity_modules_engines_zorba_ZorbaDXScriptContext_setJSONiqVersion(
		JNIEnv * env, jobject obj, jint version) {
	try {
		zorba::StaticContext* sctx = getStaticContext(env, obj);
		if (sctx == NULL)
			return JNI_FALSE;
		if (version == 100)
			sctx->setJSONiqVersion(zorba::jsoniq_version_1_0);
		else
			sctx->setJSONiqVersion(zorba::jsoniq_version_1_0);
		return JNI_TRUE;
	} catch (...) {
		return JNI_FALSE;
	}
}

JNIEXPORT jboolean JNICALL Java_unity_modules_engines_zorba_ZorbaDXScriptContext_setXQueryVersion(
		JNIEnv * env, jobject obj, jint version) {
	try {
		zorba::StaticContext* sctx = getStaticContext(env, obj);
		if (sctx == NULL)
			return JNI_FALSE;
		if (version == 100)
			sctx->setXQueryVersion(zorba::xquery_version_1_0);
		else if (version == 300)
			sctx->setXQueryVersion(zorba::xquery_version_3_0);
		else
			sctx->setXQueryVersion(zorba::xquery_version_3_0);
		return JNI_TRUE;
	} catch (...) {
		return JNI_FALSE;
	}
}

ZorbaDXExternalFunction::ZorbaDXExternalFunction(JavaVM* javaVM, jobject obj,
		ZorbaDXExternalModule* module, const std::string name) :
		javaVM(javaVM), obj(obj), module(module), name(name) {
}

ZorbaDXExternalFunction::~ZorbaDXExternalFunction() {
}

zorba::ItemSequence_t ZorbaDXExternalFunction::evaluate(
		const Arguments_t & args, const zorba::StaticContext * sctx,
		const zorba::DynamicContext * dctx) const {
	JNIEnv* env = NULL;
	javaVM->AttachCurrentThread((void **) &env, NULL);
	if (env == NULL)
		return zorba::ItemSequence_t(new zorba::EmptySequence());
	zorba::ItemSequence_t sequence = NULL;
	try {
		jclass cls = env->GetObjectClass(obj);
		jclass clsItemSequence = env->FindClass("io/zorba/api/ItemSequence");
		jmethodID mEvaluate =
				env->GetMethodID(cls, "evaluate",
						"(Ljava/lang/String;Ljava/lang/String;Lio/zorba/api/StaticContext;Lio/zorba/api/DynamicContext;[Lio/zorba/api/ItemSequence;)Ljava/lang/Object;");
		jmethodID mItemSequence = env->GetMethodID(clsItemSequence, "<init>",
				"(JZ)V");
		jobjectArray itemSequenceArray = env->NewObjectArray(
				(jsize) args.size(), clsItemSequence, NULL);
		for (size_t i = 0; i < args.size(); i++) {
			jlong swigCPtr = (jlong) & args[i];
			jobject itemSequence = env->NewObject(clsItemSequence,
					mItemSequence, swigCPtr, false);
			env->SetObjectArrayElement(itemSequenceArray, (jsize) i,
					itemSequence);
		}
		jstring moduleURI = env->NewStringUTF(getURI().c_str());
		jstring localName = env->NewStringUTF(name.c_str());
		jclass clsStaticContext = env->FindClass("io/zorba/api/StaticContext");
		jmethodID mStaticContext = env->GetMethodID(clsStaticContext, "<init>",
				"(JZ)V");
		jobject staticContext = env->NewObject(clsStaticContext, mStaticContext,
				(jlong) sctx, false);
		jclass clsDynamicContext = env->FindClass(
				"io/zorba/api/DynamicContext");
		jmethodID mDynamicContext = env->GetMethodID(clsDynamicContext,
				"<init>", "(JZ)V");
		jobject dynamicContext = env->NewObject(clsDynamicContext,
				mDynamicContext, (jlong) dctx, false);
		jobject result = env->CallObjectMethod(obj, mEvaluate, moduleURI,
				localName, staticContext, dynamicContext, itemSequenceArray);

		if (result != NULL) {
			sequence =
					zorba::ItemSequence_t(
							*((zorba::ItemSequence**) env->GetLongField(result,
									env->GetFieldID(clsItemSequence, "swigCPtr",
											"J"))));
		} else {
			sequence = zorba::ItemSequence_t(new zorba::EmptySequence());
		}
	} catch (...) {
	}

	javaVM->DetachCurrentThread();
	return sequence;
}

zorba::String ZorbaDXExternalFunction::getLocalName() const {
	return name;
}

zorba::String ZorbaDXExternalFunction::getURI() const {
	return module->getURI();
}

ZorbaDXExternalModule::ZorbaDXExternalModule(JNIEnv * env, jobject obj,
		const std::string identifier) :
		identifier(identifier) {
	env->GetJavaVM(&javaVM);
	this->obj = env->NewGlobalRef(obj);
}

ZorbaDXExternalModule::~ZorbaDXExternalModule() {
	JNIEnv* env = NULL;
	try {
		if (obj == NULL)
			return;
		javaVM->AttachCurrentThread((void **) &env, NULL);
		if (env == NULL)
			return;
		env->DeleteGlobalRef(obj);
	} catch (...) {
	}

	javaVM->DetachCurrentThread();
	std::map<std::string, ZorbaDXExternalFunction*>::iterator i;
	for (i = functions.begin(); i != functions.end(); i++)
		if (i->second != NULL)
			delete i->second;
}

zorba::ExternalFunction* ZorbaDXExternalModule::getExternalFunction(
		const zorba::String& name) {
	ZorbaDXExternalFunction* function = functions[name.str()];
	if (function != NULL)
		return function;
	function = new ZorbaDXExternalFunction(javaVM, obj, this, name.str());
	functions[name.str()] = function;
	return function;
}

zorba::String ZorbaDXExternalModule::getURI() const {
	return identifier;
}

ZorbaDXResolver::ZorbaDXResolver(JNIEnv * env, jobject obj) {
	env->GetJavaVM(&javaVM);
	this->obj = env->NewGlobalRef(obj);
}

ZorbaDXResolver::~ZorbaDXResolver() {
	JNIEnv* env = NULL;
	try {
		if (obj == NULL)
			return;
		javaVM->AttachCurrentThread((void **) &env, NULL);
		if (env == NULL)
			return;
		env->DeleteGlobalRef(obj);
	} catch (...) {
	}

	javaVM->DetachCurrentThread();
}

zorba::URIMapper::Kind ZorbaDXResolver::mapperKind() {
	return (zorba::URIMapper::Kind) 1;
}

zorba::Resource* ZorbaDXResolver::resolveURL(const zorba::String& aUrl,
		zorba::EntityData const* aEntityData) {
	zorba::Resource* resource = NULL;
	JNIEnv* env = NULL;
	try {
		javaVM->AttachCurrentThread((void **) &env, NULL);
		if (env == NULL)
			return NULL;
		jclass cls = env->GetObjectClass(obj);
		jmethodID resolve = env->GetMethodID(cls, "resolve",
				"(Ljava/lang/String;I)Ljava/lang/String;");
		jstring result = (jstring) env->CallObjectMethod(obj, resolve,
				env->NewStringUTF(aUrl.c_str()), aEntityData->getKind());
		if (result != NULL) {
			const char *resultUTFChars = env->GetStringUTFChars(result, NULL);
			std::stringstream* stream = new std::stringstream(resultUTFChars);
			env->ReleaseStringUTFChars(result, resultUTFChars);
			resource = zorba::StreamResource::create(stream, &releaseStream,
					true);
		}
	} catch (...) {
	}

	javaVM->DetachCurrentThread();
	return resource;
}

void ZorbaDXResolver::mapURI(const zorba::String aUri,
		zorba::EntityData const* aEntityData,
		std::vector<zorba::String>& oUris) {
	JNIEnv* env = NULL;
	try {
		javaVM->AttachCurrentThread((void **) &env, NULL);
		if (env == NULL)
			return;
		jclass cls = env->GetObjectClass(obj);
		jmethodID map = env->GetMethodID(cls, "map",
				"(Ljava/lang/String;I)Ljava/lang/String;");
		jstring result = (jstring) env->CallObjectMethod(obj, map,
				env->NewStringUTF(aUri.c_str()), aEntityData->getKind());
		if (result != NULL) {
			const char *resultUTFChars = env->GetStringUTFChars(result, NULL);
			oUris.push_back(resultUTFChars);
			env->ReleaseStringUTFChars(result, resultUTFChars);
		}
	} catch (...) {
	}
	javaVM->DetachCurrentThread();
}

JNIEXPORT jboolean JNICALL Java_unity_modules_engines_zorba_ZorbaDXScriptContext_register(
		JNIEnv * env, jobject obj, jstring ns) {
	try {
		const char *nsUTFChars = env->GetStringUTFChars(ns, NULL);
		ZorbaDXExternalModule* module = new ZorbaDXExternalModule(env, obj,
				nsUTFChars);
		env->ReleaseStringUTFChars(ns, nsUTFChars);
		zorba::StaticContext* sctx = getStaticContext(env, obj);
		if (sctx == NULL)
			return JNI_FALSE;
		sctx->registerModule(module);
		return JNI_TRUE;
	} catch (...) {
		return JNI_FALSE;
	}
}
