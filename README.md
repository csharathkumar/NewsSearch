# New York Times Article Search

This Android application allows a user to find old articles using the [New York Times Search API](http://developer.nytimes.com/docs/read/article_search_api_v2). 

Time spent: 15 hours spent in total

Completed user stories:

 * [x] Required: User can enter a search query that will display a grid of news articles using the thumbnail and headline from the New York Times Search API
 * [x] Required: User can click on "filter" icon which allows selection of advanced search options to filter articles.
 * [x] Required: User can configure advanced search filters such as: begin date, sort order and news desk values(categories)
 * [x] Required: Subsequent searches will have any filters applied to the search results. 
 * [x] Required: User can tap on any article in results to view the contents in an embedded browser.
 * [x] Required: User can scroll down "infinitely" to continue loading more news articles. The maximum number of articles is limited by the API search.
 
Completed advanced user stories:

 * [x] Optional: Robust error handling, check if internet is available, handle error cases, network failures.
 * [x] Optional: Use the ActionBar SearchView or custom layout as the query box instead of an EditText.
 * [x] Optional: User can share a link to their friends or email it to themselves.
 * [x] Optional: Replace Filter Settings Activity with a lightweight modal overlay.
 * [x] Optional: Improve the user interface and experiment with image assets and/or styling and coloring.
 * [x] Optional: Use the RecyclerView with the StaggeredGridLayoutManager to display improve the grid of image results.
 * [x] Optional: For different news articles that only have text or have text with thumbnails, use Heterogenous Layouts with RecyclerView.
 * [x] Optional: Apply the popular ButterKnife annotation library to reduce view boilerplate.
 * [x] Optional: Use Parcelable instead of Serializable using the popular Parceler library. 
 * [x] Optional: Replace all icon drawables and other static image assets with vector drawables where appropriate.
 * [x] Optional: Leverage the data binding support module to bind data into one or more activity layout templates.
 * [x] Optional: Replace Picasso with Glide for more efficient image rendering.
 * [x] Optional: Switch to using retrolambda expressions to cleanup event handling blocks.
 * [x] Optional: Leverage the popular GSON library to streamline the parsing of JSON data.
 * [x] Optional: Consume the New York Times API using the popular Retrofit networking library instead of Android Async HTTP.
 
 Enhancements:
 
 * [x] Optional: Included Chrome Custom tabs instead of Webview.

 
Notes:


Walkthrough of all user stories:

![Video Walkthrough](anim_rotten_tomatoes.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).