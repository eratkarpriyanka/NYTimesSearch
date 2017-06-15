# NYTimesSearch
Search articles of your interest. Apply filters to set criteria to display articles. 
Share the interesting article link with your friends.

* Use the NY Times "Article Search" API. 
- https://developer.nytimes.com/article_search_v2.json#/Documentation/GET/articlesearch.json

** Home Screen:
- Allows the user to enter a search term and displays the results in either a list or grid view.
- Use the ActionBar SearchView or custom layout as the query box instead of an EditText.
- Results should display:
    - Article thumbnail
    - Article headline
- Selecting an item in search results opens the detail screen.
- Pagination of results supported

* Detail Screen: 
- Displays a detailed view of article.
- User can share a link to their friends or email it to themselves

* Filter Screen : 
- Allows user to set criteria to filter the results based on (Date, Sort order, Article category *news desk values.)

* Walkthrough the app : 

<img src='http://imgur.com/yIgR8RN.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

* GIF created with [LiceCap](http://www.cockos.com/licecap/). <br/>
* Use of Retrofit to communicate with server. <br/>
* Use of Glide library to download images. <br/>
