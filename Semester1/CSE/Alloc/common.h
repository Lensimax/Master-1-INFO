#ifndef __COMMON_H__
#define __COMMON_H__
#include <stdlib.h>

#ifdef DEBUG
#define debug(...) fprintf(stderr, __VA_ARGS__)
#else
#define debug(...)
#endif

void *get_memory_adr();
size_t get_memory_size();

#endif
