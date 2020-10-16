package com.linhuicommon.common;

import android.content.Context;
import android.view.ViewGroup;

public interface IAndroidView<T extends ViewGroup> {

   T getMainView();

   Context getContext();

}
