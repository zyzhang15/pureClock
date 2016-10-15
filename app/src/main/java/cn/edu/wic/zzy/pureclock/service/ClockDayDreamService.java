package cn.edu.wic.zzy.pureclock.service;

import android.service.dreams.DreamService;
import cn.edu.wic.zzy.pureclock.R;

public class ClockDayDreamService extends DreamService{
    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();
        setFullscreen(true);
        setScreenBright(false);
        setContentView(R.layout.activity_main);
    }
}
