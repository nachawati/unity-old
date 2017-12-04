/*******************************************************************************
 * بسم الله الرحمن الرحيم
 *
 * In the name of Allah, the Most Compassionate, the Most Merciful
 *
 * This software is authored by Mohamad Omar Nachawati, 1436-1439 AH
 *******************************************************************************/

#ifndef _Included_UnityDXZorbaQueryContext
#define _Included_UnityDXZorbaQueryContext

#include <iostream>

#include <jni.h>

#include <zorba/api_shared_types.h>
#include <zorba/empty_sequence.h>
#include <zorba/external_module.h>
#include <zorba/item.h>
#include <zorba/singleton_item_sequence.h>
#include <zorba/vector_item_sequence.h>
#include <zorba/zorba.h>

class ZorbaDXResolver;
class ZorbaDXExternalFunction;
class ZorbaDXExternalModule;

zorba::StaticContext* getStaticContext(JNIEnv * env, jobject obj);

static void releaseStream(std::istream* stream) {
	try {
		if (stream != NULL)
			delete stream;
	} catch (const std::exception&) {
	}
}

class ZorbaDXResolver: public zorba::URIMapper, public zorba::URLResolver {
public:
	ZorbaDXResolver(JNIEnv * env, jobject obj);

	virtual ~ZorbaDXResolver();

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

class ZorbaDXExternalFunction: public virtual zorba::ContextualExternalFunction {
public:
	ZorbaDXExternalFunction(JavaVM* javaVM, jobject obj,
			ZorbaDXExternalModule* module, const std::string name);

	virtual ~ZorbaDXExternalFunction();

	virtual zorba::ItemSequence_t evaluate(const Arguments_t & args,
			const zorba::StaticContext * sctx,
			const zorba::DynamicContext * dctx) const;

	virtual zorba::String getLocalName() const;

	virtual zorba::String getURI() const;

private:
	jobject obj;
	JavaVM* javaVM;
	std::string name;
	ZorbaDXExternalModule* module;
};

class ZorbaDXExternalModule: public zorba::ExternalModule {
public:
	ZorbaDXExternalModule(JNIEnv * env, jobject obj,
			const std::string identifier);

	virtual ~ZorbaDXExternalModule();

	virtual zorba::ExternalFunction* getExternalFunction(
			const zorba::String& name);

	virtual zorba::String getURI() const;

private:
	jobject obj;
	JavaVM* javaVM;
	std::string identifier;
	std::map<std::string, ZorbaDXExternalFunction*> functions;
};

#endif
