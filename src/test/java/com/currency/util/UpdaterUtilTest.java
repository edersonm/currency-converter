package com.currency.util;

import org.junit.Test;

public class UpdaterUtilTest {

    @Test
    public void testUpdateTimer(){
        UpdaterUtil.updateTimer(10);
        assert(UpdaterUtil.getUpdateTime() == 10);
    }
    
    @Test
    public void testUpdateTimerError(){
        UpdaterUtil.updateTimer(-1);
        assert(!UpdaterUtil.updateTimer(-1));
    }
    
    @Test
    public void testUpdateRequestTimerError(){
        UpdaterUtil.updateRequestTimer(0);
        assert(UpdaterUtil.getGoToDbTimer() == 0);
    }
}
