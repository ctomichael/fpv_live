package com.dji.video.framing.utils;

import com.dji.video.framing.internal.parser.VideoStreamParseController;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class VideoStreamFilePreviewer {
    private boolean isRunning = false;

    public void runReadData(File sourceFile, VideoStreamParseController parseController, boolean isLoop) throws IOException, InterruptedException {
        this.isRunning = true;
        FileInputStream fis = new FileInputStream(sourceFile);
        byte[] buffer = new byte[2032];
        while (this.isRunning) {
            if (parseController.getCachedFrameNum() > 60) {
                Thread.sleep(200);
            } else {
                int readRst = fis.read(buffer);
                if (readRst > 0) {
                    parseController.feedData(buffer, 0, readRst);
                } else if (isLoop) {
                    fis.close();
                    fis = new FileInputStream(sourceFile);
                } else {
                    this.isRunning = false;
                }
            }
        }
        fis.close();
    }

    public void runReadData(File sourceFile, VideoStreamParseController parseController) throws IOException, InterruptedException {
        runReadData(sourceFile, parseController, true);
    }

    public void stopReadData() {
        this.isRunning = false;
    }
}
