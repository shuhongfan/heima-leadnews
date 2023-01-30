local size = redis.call('llen', KEYS[1]);
local list = redis.call('lrange', KEYS[1], 0, size-1);
redis.call('ltrim', KEYS[1], size, -1);
return list;