package com.lin.testmodel.common;

import android.icu.text.MeasureFormat;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.RequiresApi;

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
                    try {
                        if (Build.VERSION.SDK_INT >= 29) {
                            printMediaFormat(mediaFormat);
                        }
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
            if (inputOtherAudioFramat != null) {
                mediaExtractor.selectTrack(otherAudioInputIndex);
                otherAudioCodec = MediaCodec.createEncoderByType(inputOtherAudioFramat.getString(MediaFormat.KEY_MIME));
                MediaFormat audioFormat = MediaFormat.createAudioFormat("audio/mp4a-latm", 44100, 1);
                audioFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
                audioFormat.setInteger(MediaFormat.KEY_CHANNEL_MASK, AudioFormat.CHANNEL_IN_MONO);
                audioFormat.setInteger(MediaFormat.KEY_BIT_RATE, 44100);
                audioFormat.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
                otherAudioCodec.configure(audioFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
//                otherAudioCodec.start();
            }
            mediaMuxerInit();
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "init: error");
        }


    }

    @RequiresApi(api = 29)
    private void printMediaFormat(MediaFormat mediaFormat) throws NoSuchFieldException, IllegalAccessException {

        Set<String> strings = mediaFormat.getKeys();
        Log.i(TAG, "printMediaFormat: " + Arrays.toString(strings.toArray()));

        Field field = mediaFormat.getClass().getDeclaredField("mMap");
        field.setAccessible(true);
        Map<String,Object> objectMap = (Map<String, Object>) field.get(mediaFormat);

        for (String s : strings) {
            Log.i(TAG, "printMediaFormat: " + s + ":" + objectMap.get(s));
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

        p = new File(p.getParent(), "linhui_test_20201014.mp3");
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
        new Thread() {
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

        ByteBuffer byteBuffer = ByteBuffer.allocate(inputOtherAudioFramat.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE));
        byteBuffer.clear();
        int readBytes = mediaExtractor.readSampleData(byteBuffer, 0);
        if (mediaExtractor.getSampleTime()==0) {
            mediaExtractor.advance();
        }
        if (readBytes > 0) {
            int inputIndex = otherAudioCodec.dequeueInputBuffer(TU);
            if (inputIndex >= 0) {
                byteBuffer.position(readBytes);
                byteBuffer.flip();
                ByteBuffer inputBuffer = otherAudioCodec.getInputBuffer(inputIndex);
                inputBuffer.clear();
                inputBuffer.put(byteBuffer);
                Log.i(TAG, "inputOtherAudioAudioAudio: "+(mediaMuxerStartTime - nTime) / 1000);
                otherAudioCodec.queueInputBuffer(inputIndex, 0, readBytes,
                        (mediaMuxerStartTime - nTime) / 1000, 0);
            }
        } else if (readBytes == AudioRecord.ERROR_INVALID_OPERATION) {
            Log.i(TAG, "AudioRecord.ERROR_INVALID_OPERATION");
            setStart(false);
            exit();
        } else {

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
