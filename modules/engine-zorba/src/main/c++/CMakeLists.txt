cmake_minimum_required(VERSION 2.8)

project(unity-module-engine-zorba)

find_package(JNI)

add_library(unity-module-engine-zorba SHARED ZorbaDGScriptContext.cpp)

if (windows-msvc-x86_64)
	include_directories(
		"${CMAKE_SOURCE_DIR}/../include"
		"${CMAKE_SOURCE_DIR}/../../../../../target/windows-msvc-x86_64/include"
		"${JAVA_INCLUDE_PATH}"
		"${JAVA_INCLUDE_PATH2}"
		"${JAVA_AWT_INCLUDE_PATH}")
	target_link_libraries(unity-module-engine-zorba PRIVATE
		"${CMAKE_SOURCE_DIR}/../../../../../target/windows-msvc-x86_64/lib/zorba_simplestore.lib")

endif()

if (linux-gcc-x86_64)
	include_directories(
		"${CMAKE_SOURCE_DIR}/../include"
		"${CMAKE_SOURCE_DIR}/../../../../../target/linux-gcc-x86_64/include"
		"${JAVA_INCLUDE_PATH}"
		"${JAVA_INCLUDE_PATH2}"
		"${JAVA_AWT_INCLUDE_PATH}")
	target_link_libraries(unity-module-engine-zorba PRIVATE
		"${CMAKE_SOURCE_DIR}/../../../../../target/linux-gcc-x86_64/lib/libzorba_simplestore.so.3.0.0")

endif()
