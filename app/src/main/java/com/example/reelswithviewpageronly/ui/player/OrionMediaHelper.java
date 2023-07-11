package com.example.reelswithviewpageronly.ui.player;

import android.content.Context;


import fi.finwe.orion360.sdk.pro.OrionContext;
import fi.finwe.orion360.sdk.pro.texture.OrionTexture;
import fi.finwe.orion360.sdk.pro.texture.OrionVideoTexture;

public class OrionMediaHelper {

    public OrionTexture initializeOrionTexture(Context context, OrionContext mOrionContext, ExoPlayerWrapper mVideoPlayer, String uri) {
        try {
            //mPanoramaTexture = OrionTexture.createTextureFromURI(mOrionContext, context, holder.getStreamUrl()?: "")
            OrionTexture mPanoramaTexture = new OrionVideoTexture(
                    mOrionContext, mVideoPlayer, uri
            );

            return mPanoramaTexture;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
