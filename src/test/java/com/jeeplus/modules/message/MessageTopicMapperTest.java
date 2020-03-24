package com.jeeplus.modules.message;

import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MessageTopicMapperTest {
    static final String DECLARE = "/sys/workflow/v1/GLOBAL/COMMON,/workflow;/sys/device/cover/guard/v1/GLOBAL/COMMON,/guard";

    @Test
    void register() {
        MessageTopicMapper mapper = new MessageTopicMapper();
        mapper.register(DECLARE);
        String ext, loc;
        Optional<String> optional;

        // 1. workflow
        ext = "/sys/workflow/v1/GLOBAL/COMMON/create";
        loc = "/workflow/create";
        // 1.1 workflow - local
        optional = mapper.toLocal(ext);
        assertTrue(optional.isPresent(), "1.1.exists");
        assertEquals(loc, optional.get(), "1.1.mapping");

        // 1.2 workflow - external
        optional = mapper.toExternal(loc);
        assertTrue(optional.isPresent(), "1.2.exists");
        assertEquals(ext, optional.get(), "1.2.mapping");

        // 2. guard:alarm
        ext = "/sys/device/cover/guard/v1/GLOBAL/COMMON/alarm";
        loc = "/guard/alarm";
        // 2.1 guard:alarm - local
        optional = mapper.toLocal(ext);
        assertTrue(optional.isPresent(), "2.1.exists");
        assertEquals(loc, optional.get(), "2.1.mapping");

        // 2.2 guard:alarm - external
        optional = mapper.toExternal(loc);
        assertTrue(optional.isPresent(), "2.2.exists");
        assertEquals(ext, optional.get(), "2.2.mapping");

        // 3 guard:online
        ext = "/sys/device/cover/guard/v1/GLOBAL/COMMON/online";
        loc = "/guard/online";
        // 3.1 guard:online - local
        optional = mapper.toLocal(ext);
        assertTrue(optional.isPresent(), "3.1.exists");
        assertEquals(loc, optional.get(), "3.1.mapping");

        // 3.2 guard:online - external
        optional = mapper.toExternal(loc);
        assertTrue(optional.isPresent(), "3.2.exists");
        assertEquals(ext, optional.get(), "3.2.mapping");
    }
}