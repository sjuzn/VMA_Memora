package sk.upjs.druhypokus.intro

//zdroj https://www.geeksforgeeks.org/create-custom-intro-slider-of-an-android-app-with-kotlin/

data class SliderData(
    // on below line we are creating a string
    // for our slide title, slide
    // description and image.
    var slideTitle: String,
    var slideDescription: String,
    var slideImage: Int
)
