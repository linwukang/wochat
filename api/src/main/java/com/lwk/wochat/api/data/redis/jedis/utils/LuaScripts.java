package com.lwk.wochat.api.data.redis.jedis.utils;

public class LuaScripts {
    /**
     * key 数量: 0
     * ARGV 数量: 0
     */
    public static final String REDIS_VALUES = "local keys = redis.call('keys', ARGV[1] .. '*')\n" +
            "local values = {}\n" +
            "for i=1, #keys do\n" +
            "    local value = redis.call('GET', keys[i])\n" +
            "    table.insert(values, value)\n" +
            "end\n" +
            "return values";

    /**
     * key 数量: 0
     * ARGV 数量: 0
     */
    public static final String REDIS_CLEAR = "local keys = redis.call('KEYS', ARGV[1] .. '*')\n" +
            "for i, key in ipairs(keys) do\n" +
            "    redis.call('DEL', key)\n" +
            "end\n" +
            "return #keys";

    /**
     * key 数量: 0
     * ARGV 数量: ARGV[1]
     */
    public static final String REDIS_PUT_ALL = "for i = 2, ARGV[1], 2 do\n" +
            "    redis.call('SET', ARGV[i], ARGV[i + 1])\n" +
            "end\n" +
            "return math.floor(ARGV[1] / 2)";

    /**
     * key 数量: 1
     * ARGV 数量: 0
     */
    public static final String REDIS_REMOVE = "local value = redis.call('GET', KEYS[1])\n" +
            "redis.call('DEL', KEYS[1])\n" +
            "return value";

    /**
     * key 数量: 1
     * ARGV 数量: n
     */
    public static final String REDIS_REMOVE_ALL = "local key = KEYS[1]\n" +
            "local elements = {}\n" +
            "for i, v in ipairs(ARGV) do\n" +
            "    elements[i] = v\n" +
            "end\n" +
            "redis.call('LREM', key, 0, unpack(elements))\n";

    /**
     * key 数量: 1
     * ARGV 数量: n
     */
    public static final String REDIS_RETAIN_ALL = "local key = KEYS[1]\n" +
            "local retainElements = {}\n" +
            "for i, v in ipairs(ARGV) do\n" +
            "    retainElements[v] = true\n" +
            "end\n" +
            "local elements = redis.call('LRANGE', key, 0, -1)\n" +
            "for i, element in ipairs(elements) do\n" +
            "    if not retainElements[element] then\n" +
            "        redis.call('LREM', key, 0, element)\n" +
            "    end\n" +
            "end";

    public static final String REDIS_GET_RANGE = "local key = KEYS[1]\n" +
            "local startOffset = tonumber(ARGV[1])\n" +
            "local endOffset = tonumber(ARGV[2])\n" +
            "local byteStart = math.floor(startOffset / 8)\n" +
            "local byteEnd = math.floor(endOffset / 8)\n" +
            "local bitStart = startOffset % 8\n" +
            "local bitEnd = endOffset % 8\n" +
            "local values = {}\n" +
            "for byte = byteStart, byteEnd do\n" +
            "    local byteValue = tonumber(redis.call('GETRANGE', key, byte, byte):byte()) or 0\n" +
            "    local startBit = (byte == byteStart) and bitStart or 0\n" +
            "    local endBit = (byte == byteEnd) and bitEnd or 7\n" +
            "    for bit = startBit, endBit do\n" +
            "        local bitValue = bit.band(bit.rshift(byteValue, (7 - bit)), 1)\n" +
            "        table.insert(values, bitValue)\n" +
            "    end\n" +
            "end\n" +
            "return values";
}
