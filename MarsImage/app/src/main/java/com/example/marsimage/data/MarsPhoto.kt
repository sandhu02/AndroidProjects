package com.example.marsimage.data

data class MarsPhoto (
    val id:String ,
    val img_src:String
)

val marslist:List<MarsPhoto> = listOf(

        MarsPhoto(
            id= "424905",
            img_src= "https://mars.jpl.nasa.gov/msl-raw-images/msss/01000/mcam/1000MR0044631300503690E01_DXXX.jpg"
        ) ,
        MarsPhoto(
            id = "424906",
            img_src = "https://mars.jpl.nasa.gov/msl-raw-images/msss/01000/mcam/1000ML0044631300305227E03_DXXX.jpg"
        )

)