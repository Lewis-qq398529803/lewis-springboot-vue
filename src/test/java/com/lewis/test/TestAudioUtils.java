package com.lewis.test;

import com.lewis.common.utils.file.AudioUtils;
import org.junit.Test;


/**
 * MP3转PCM
 *
 * @author Lewis
 */
public class TestAudioUtils {
    //测试播放音频
    @Test
    public void testPaly() throws Exception {
        AudioUtils utils = AudioUtils.getInstance();
        utils.playMp3("D:/xx.mp3");
    }

    @Test
    public void testConvert() throws Exception {
        AudioUtils utils = AudioUtils.getInstance();
        utils.convertMp32Pcm("D:/xx.mp3", "D:/xx.pcm");
    }
}