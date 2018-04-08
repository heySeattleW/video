package com.hey.so;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import org.omg.PortableInterceptor.INACTIVE;

/**
 * Created by hey on 2018/3/27.
 */
public class TestSO {

    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary) Native.loadLibrary("test", CLibrary.class);

        String req();

    }

    public static void main(String[] args) {
        CLibrary.INSTANCE.req();
        for (int i=0;i < args.length;i++) {
            CLibrary.INSTANCE.req();
        }
    }
}
