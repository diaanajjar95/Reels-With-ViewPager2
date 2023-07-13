package com.example.reelswithviewpageronly.ui.models

object ReelsFactory {

    fun getReels(): MutableList<ReelItem> {
        val reels: MutableList<ReelItem> = ArrayList()

        /**
         * https://dxmckoh3avtig.cloudfront.net/2023/Production/Teleporter/MatthiasWeger/June/41GlacierParagliding/41GlacierParagliding.m3u8
         * https://dxmckoh3avtig.cloudfront.net/2023/Production/Influencers/Mubashir/July06/July06.m3u8
         * https://dxmckoh3avtig.cloudfront.net/2023/Production/Teleporter/Airpano/Dubai/Dubai.m3u8
         * https://dxmckoh3avtig.cloudfront.net/2023/Production/Teleporter/California/ShakiraGrammy/ShakiraGrammy.m3u8
         * https://dxmckoh3avtig.cloudfront.net/2023/Production/Teleporter/MatthiasWeger/June/42CouloirsAlps/42CouloirsAlps.m3u8
         *
         * */

        val video1 =
            "https://dxmckoh3avtig.cloudfront.net/2023/Production/Teleporter/MatthiasWeger/June/41GlacierParagliding/41GlacierParagliding.m3u8"
        val video2 =
            "https://dxmckoh3avtig.cloudfront.net/2023/Production/Influencers/Mubashir/July06/July06.m3u8"
        val video3 =
            "https://dxmckoh3avtig.cloudfront.net/2023/Production/Teleporter/Airpano/Dubai/Dubai.m3u8"
        val video4 =
            "https://dxmckoh3avtig.cloudfront.net/2023/Production/Teleporter/California/ShakiraGrammy/ShakiraGrammy.m3u8"
        val video5 =
            "https://dxmckoh3avtig.cloudfront.net/2023/Production/Teleporter/MatthiasWeger/June/42CouloirsAlps/42CouloirsAlps.m3u8"


        reels.add(
            ReelItem(
                title = "title 1",
                desc = "description 1 ",
                url = video1
            )
        )

        reels.add(
            ReelItem(
                title = "title 2",
                desc = "description 2 ",
                url = video2
            )
        )

        reels.add(
            ReelItem(
                title = "title 3",
                desc = "description 3 ",
                url = video3
            )
        )

        reels.add(
            ReelItem(
                title = "title 4",
                desc = "description 4 ",
                url = video4
            )
        )

        reels.add(
            ReelItem(
                title = "title 5",
                desc = "description 5 ",
                url = video5
            )
        )

        return reels
    }

}