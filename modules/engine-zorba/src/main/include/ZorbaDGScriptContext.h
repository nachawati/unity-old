/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

#ifndef _Included_UnityDGZorbaQueryContext
#define _Included_UnityDGZorbaQueryContext

#include <iostream>

#include <jni.h>

#include <zorba/api_shared_types.h>
#include <zorba/empty_sequence.h>
#include <zorba/external_module.h>
#include <zorba/item.h>
#include <zorba/singleton_item_sequence.h>
#include <zorba/vector_item_sequence.h>
#include <zorba/zorba.h>

class ZorbaDGResolver;
class ZorbaDGExternalFunction;
class ZorbaDGExternalModule;

zorba::StaticContext* getStaticContext(JNIEnv * env, jobject obj);

static void releaseStream(std::istream* stream) {
	try {
		if (stream != NULL)
			delete stream;
	} catch (const std::exception&) {
	}
}

class ZorbaDGResolver: public zorba::URIMapper, public zorba::URLResolver {
public:
	ZorbaDGResolver(JNIEnv * env, jobject obj);

	virtual ~ZorbaDGResolver();

	virtual zorba::URIMapper::Kind mapperKind();

	virtual zorba::Resource* resolveURL(const zorba::String& aUrl,
			zorba::EntityData const* aEntityData);

	virtual void mapURI(const zorba::String aUri,
			zorba::EntityData const* aEntityData,
			std::vector<zorba::String>& oUris);
private:
	JavaVM* javaVM;
	jobject obj;
};

class ZorbaDGExternalFunction: public virtual zorba::ContextualExternalFunction {
public:
	ZorbaDGExternalFunction(JavaVM* javaVM, jobject obj,
			ZorbaDGExternalModule* module, const std::string name);

	virtual ~ZorbaDGExternalFunction();

	virtual zorba::ItemSequence_t evaluate(const Arguments_t & args,
			const zorba::StaticContext * sctx,
			const zorba::DynamicContext * dctx) const;

	virtual zorba::String getLocalName() const;

	virtual zorba::String getURI() const;

private:
	jobject obj;
	JavaVM* javaVM;
	std::string name;
	ZorbaDGExternalModule* module;
};

class ZorbaDGExternalModule: public zorba::ExternalModule {
public:
	ZorbaDGExternalModule(JNIEnv * env, jobject obj,
			const std::string identifier);

	virtual ~ZorbaDGExternalModule();

	virtual zorba::ExternalFunction* getExternalFunction(
			const zorba::String& name);

	virtual zorba::String getURI() const;

private:
	jobject obj;
	JavaVM* javaVM;
	std::string identifier;
	std::map<std::string, ZorbaDGExternalFunction*> functions;
};

#endif
