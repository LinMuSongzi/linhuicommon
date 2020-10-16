package com.lin.testmodel.common;

import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class RunImp extends BaseRun {

    private static final String TAG = "RunImp";
    private static final long TU = 10000;
    private MediaCodec otherAudioCodec;
    private final MediaCodec.BufferInfo otherAudioBufferInfo = new MediaCodec.BufferInfo();
    private MediaFormat inputOtherAudioFramat;
    private int otherAudioindex;
    private int otherAudioInputIndex;

    private MediaCodec nativeAudioCodec;


    private MediaMuxer mediaMuxer;
    private MediaExtractor mediaExtractor;

    private Handler handler;
    private HandlerThread handlerThread;
    private boolean otherAudioCodeconOutputFormatChanged = false;
    private long sampleTime;
    private int otherAudioIndex;
    private long TIMEOUT_MSEC = 1000;

    private final List<Long> longs = new ArrayList<>();
    private boolean mediaMuxerStart = false;
    private long mediaMuxerStartTime;


    public RunImp(CodeManager.Config config) {
        super(config);
        handlerThread = new HandlerThread("" + hashCode());
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });
    }

    private void init() {

        try {
            mediaExtractor = new MediaExtractor();
            String p = config.getAudioVoice();
            if (new File(p).exists()) {
                mediaExtractor.setDataSource(p);
            } else {
                return;
            }
            int i = mediaExtractor.getTrackCount();

            for (int c = 0; c < i; c++) {
                MediaFormat mediaFormat = mediaExtractor.getTrackFormat(c);
                if (mediaFormat.getString(MediaFormat.KEY_MIME).startsWith("audio")) {
                    inputOtherAudioFramat = mediaFormat;
                    otherAudioInputIndex = c;
                    break;
                }
            }
            if (inputOtherAudioFramat != null) {
                mediaExtractor.selectTrack(otherAudioInputIndex);
                otherAudioCodec = MediaCodec.createEncoderByType(inputOtherAudioFramat.getString(MediaFormat.KEY_MIME));
                otherAudioCodec.configure(inputOtherAudioFramat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
//                otherAudioCodec.start();
            }
            mediaMuxerInit();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void mediaMuxerInit() {

        try {
            mediaMuxer = new MediaMuxer(buildOutputFile(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            Log.i(TAG, "mediaMuxerInit: 初始化成功");
//                otherAudioCodec.flush();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "mediaMuxerInit: 初始化错误");
        }


    }

    private String buildOutputFile() {

        File p = new File(config.getAudioVoice());

        p = new File(new File(p.getParent()).getParent(), "linhui_test_20201014.mp3");
        if (p.exists()) {
            p.delete();
        }
        try {
            p.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p.getAbsolutePath();
    }

    @Override
    public void start() {
        if (super.config == null || super.isStart()) {
            return;
        }
        setStart(true);
        new Thread(){
            @Override
            public void run() {
                startCode();
            }
        }.start();

    }

    private void startCode() {

        if (otherAudioCodec != null) {
            otherAudioCodec.start();
        }

        while (isStart()) {

            long nTime = System.nanoTime();

            inputOtherAudioAudioAudio(nTime);

            drainOtherAudioAudio(nTime);

            if (otherAudioIndex != -1 && !mediaMuxerStart) {
                mediaMuxer.start();
                mediaMuxerStart = true;
                mediaMuxerStartTime = System.nanoTime();
            }
        }
        exit();
        Log.i(TAG, "startCode: 编码完毕");
    }

    private void inputOtherAudioAudioAudio(long nTime) {

        ByteBuffer byteBuffer = ByteBuffer.allocate(1024 * 3 / 2);
        byteBuffer.clear();
        int readBytes = mediaExtractor.readSampleData(byteBuffer, 0);
        mediaExtractor.advance();
        if (readBytes > 0) {
            int inputIndex = otherAudioCodec.dequeueInputBuffer(TU);
            if (inputIndex >= 0) {
                byteBuffer.position(readBytes);
                byteBuffer.flip();
                ByteBuffer inputBuffer = otherAudioCodec.getInputBuffer(inputIndex);
                inputBuffer.clear();
                inputBuffer.put(byteBuffer);
                otherAudioCodec.queueInputBuffer(inputIndex, 0, readBytes,
                        (mediaMuxerStartTime - nTime) / 1000, 0);
            }
        } else if (readBytes == AudioRecord.ERROR_INVALID_OPERATION) {
            Log.i(TAG, "AudioRecord.ERROR_INVALID_OPERATION");

        }else{
            setStart(false);
            exit();
        }

    }

    private void drainOtherAudioAudio(long time) {

        int index = otherAudioCodec.dequeueOutputBuffer(otherAudioBufferInfo, TU);

        if (index > 0) {

            ByteBuffer byteBuffer = otherAudioCodec.getOutputBuffer(index);
            byteBuffer.flip();
            byteBuffer.limit(otherAudioBufferInfo.offset + otherAudioBufferInfo.size);
            otherAudioBufferInfo.presentationTimeUs = (time - mediaMuxerStartTime) / 1000;
            if (mediaMuxerStart) {
                mediaMuxer.writeSampleData(otherAudioIndex, byteBuffer, otherAudioBufferInfo);
            }
            otherAudioCodec.releaseOutputBuffer(index, false);
        } else if (index == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
            Log.i(TAG, "drainOtherAudioAudio: INFO_OUTPUT_FORMAT_CHANGED");
            otherAudioIndex = mediaMuxer.addTrack(otherAudioCodec.getOutputFormat());
        }

    }

    @Override
    public void stop() {
        if (super.config == null || !super.isStart()) {
            return;
        }
        setStart(false);
    }

    private void exit() {
        Log.i(TAG, "exit: 停止任务");
        if (otherAudioCodec != null) {
            otherAudioCodec.stop();
            otherAudioCodec.release();
        }
        if (mediaMuxer != null) {
            mediaMuxer.stop();
            mediaMuxer.release();
        }

        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (handlerThread != null) {
            handlerThread.quitSafely();
            handlerThread = null;
        }

    }

}
