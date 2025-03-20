# NutriSearch - Technical Challenge

## Nutri Features
- Retrieves data from NutriSearch API with Retrofit and stores it for offline usage with Room
- Responses from the main search API endpoint are also cached for a set amount of time to not be doing the same requests all the time
- Pictures are fetched and cached with Glide. A default picture is shown when no image is available
- The app is composed of two screens, each in its own Fragment
- The user's preferred sorting option is stored in the shared preferences
- More results from the Search API are fetched when the last visible item in the Recyclerview corresponds to the last result available
- The Nutrition Professional details share the same component in the two fragments and offer a pleasing shared transition
- In the Details screen, when a Professional's about me section spans more than 3 lines the text content is ellipsized and a Show More button is shown
- The app offers a collapsible Material tool bar and up navigation between all fragments
- When opening the app you'll see the animated splashscreen from one of my other apps, Sum Up, whose logo I also used for this one
- You can change the app's language using Android's per app languages settings

## Watch demo on YouTube
[![Demo](https://img.youtube.com/vi/X8nDHou0bd0/maxresdefault.jpg)](https://www.youtube.com/watch?v=X8nDHou0bd0)

## Future Improvements
- Dynamic launcher shortcuts to the most important Nutrition Professionals
- Improvements to the way data is being stored since a lot of information is shared between the two endpoints
- Use expertise Chips in order to filter for professionals
