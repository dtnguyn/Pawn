<h1 align="center">Polyglot</h1>

<p align="center">  
Polyglot is a language application that help users learn vocabulary in different languages.
</p>
<p align="center">  
(Only support English, Spanish, German and French right now, more coming soon!)
</p>

</br>

<p align="center">
<img src="/mobile/android/screenshots/collections.png" alt="Images comming soon"/>
</p>

## Try out this app
The app is almost finshed now. Once it's finished, a link to the Google Play Store will be posted.
For now, you can clone it and use Android Studio to run this app


## Tech stack & libraries

### Server
- Written in [TypeScript](https://www.typescriptlang.org/)
- [Node.js]()
- [TypeORM]()
- [PostgreSQL]()
- JWT Authentication

### Android
- Written in [Kotlin](https://kotlinlang.org/) 
- MVVM Architecture
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/) for asynchronous.
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) - A new library built on top of Dagger for dependency injection
- [Retrofit2](https://github.com/square/retrofit) - Pull data from network to build paging data
- [Room](https://developer.android.com/topic/libraries/architecture/room) - Cache data from network to optimize API calls
- [Paging 3.0](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) (alpha) - Paging system to manage data flow.
- [Gson](https://github.com/google/gson) - A JSON serialize & deserialize library
- [Glide](https://github.com/bumptech/glide) - loading images.

### iOS 
Coming Soon!




## Architecture & Design Pattern
Polyglot is based on MVVM architecture with repository pattern. 

![architecture](https://cdn.journaldev.com/wp-content/uploads/2018/04/android-mvvm-pattern.png)

## API

Polyglot are currently using the following api services:

- [Free Dictionary API](https://dictionaryapi.dev/)
- [Youtube Data API](https://developers.google.com/youtube/v3)
- [Youtube Subtitles API](https://rapidapi.com/yashagarwal/api/subtitles-for-youtube/)
- [Newscratcher API](https://rapidapi.com/newscatcher-api-newscatcher-api-default/api/free-news/)



## Support :heart:
If you find this repo useful or you like the code that I write, leave a star ⭐

# License
```xml

Copyright 2021 dtnguyn (Adron Nguyen)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and 
associated documentation files (the "Software"), to deal in the Software without restriction, including 
without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell 
copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the 
following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial 
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
DEALINGS IN THE SOFTWARE.
```
