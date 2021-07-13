# Twitter Authentication and Feed (MVVM_Fresco_SQLite)

Below are the implemented tasks in this module:
1. Twitter logo animation using custom vector animation
2. Twitter authentication is provided and user is saved in shared preference for offline mode.
3. check internet connection and provide indication
4. MVVM Designing pattern is used.
5. Live Data is used to manipulate UI on data change
6. SwipeRefreshLayout is added for better user interaction
7. ConstraintLayout is used to avoid large vew hirarchy
8. Lazy loading(Pagination) is used while retrival of data from server
9. Offline data is saved in SQLite using Google Room library
10. Application is compatible with change in orientation. Livedata lifecycle is handling the data on the same.
11. If user in Offline mode and turn the daat on, network change is detected and new data is loaded.
12. Proguard rules are added for Model files and Room

Interested to see it working? click here 👉: https://drive.google.com/file/d/1mtgTPhxthfhKupGIT4AIy6Dp_OjT1wc4/view?usp=sharing

<b>Note :</b> After building this module on your machine, <b>Do not forget</b> to add your `twitter_consumer_key` and `twitter_consumer_secret` keys in `string.xml`. You can find the same on Twitter developer account settings.
